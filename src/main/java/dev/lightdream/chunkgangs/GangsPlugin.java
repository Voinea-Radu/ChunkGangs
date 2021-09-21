package dev.lightdream.chunkgangs;

import com.google.gson.Gson;
import dev.lightdream.chunkgangs.command.*;
import dev.lightdream.chunkgangs.config.Lang;
import dev.lightdream.chunkgangs.config.Settings;
import dev.lightdream.chunkgangs.core.BConfig;
import dev.lightdream.chunkgangs.database.DataManager;
import dev.lightdream.chunkgangs.database.MySqlDataManager;
import dev.lightdream.chunkgangs.database.SqliteDataManager;
import dev.lightdream.chunkgangs.dependency.CombatHandler;
import dev.lightdream.chunkgangs.dependency.PlaceholderApiHandler;
import dev.lightdream.chunkgangs.fight.FightManager;
import dev.lightdream.chunkgangs.gang.GangManager;
import dev.lightdream.chunkgangs.listener.CrackShotListener;
import dev.lightdream.chunkgangs.listener.EntityListener;
import dev.lightdream.chunkgangs.listener.GangsListener;
import dev.lightdream.chunkgangs.listener.PlayerListener;
import dev.lightdream.chunkgangs.player.PlayerManager;
import dev.lightdream.chunkgangs.task.SaveDataTask;
import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public class GangsPlugin extends JavaPlugin {
    @Getter
    private static GangsPlugin instance;
    @Getter
    private BConfig configLang;
    @Getter
    private BConfig configMain;
    @Getter
    private DataManager dataManager;
    @Getter
    private FightManager fightManager;
    @Getter
    private GangManager gangManager;
    @Getter
    private PlayerManager playerManager;
    @Getter
    private CombatHandler combatHandler;
    @Getter
    private Economy economy;
    @Getter
    private Gson gson;
    private Logger logger;
    @Getter
    private int saveDataTaskId = -1;

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new EntityListener(this), this);
        getServer().getPluginManager().registerEvents(new GangsListener(this), this);
        getServer().getPluginManager().registerEvents(new PlayerListener(this), this);
    }

    private void registerCommands() {
        getCommand("allychat").setExecutor(new CmdAllyChat(this));
        getCommand("gang").setExecutor(new CmdGang(this));
        getCommand("fight").setExecutor(new CmdFight(this));
        getCommand("gangadmin").setExecutor(new CmdGangAdmin(this));
        getCommand("gangchat").setExecutor(new CmdGangChat(this));
    }


    public void load() {
        this.gangManager.loadGangs();
        this.fightManager.loadArenas();
        if (Settings.saveDataPeriodically) {
            this.saveDataTaskId = Bukkit.getScheduler().runTaskTimer(this, new SaveDataTask(this), (long) (Settings.dataSaveInterval * 60) * 20L, (long) (Settings.dataSaveInterval * 60) * 20L).getTaskId();
        }

    }

    public void reload() {
        this.configLang = new BConfig(this, "lang-gangs.yml");
        this.configMain = new BConfig(this, "config-gangs.yml");
        Lang.setConfig(this.configLang);
        Settings.setConfig(this.configMain);
    }

    @Override
    public void onEnable() {
        instance = this;
        this.logger = getLogger();
        this.gson = new Gson();
        if (this.setupCrackShot()) {
            this.logger.info("CrackShot support enabled!");
        }

        if (this.setupClipPlaceholders()) {
            this.logger.info("Clip's PlaceholderAPI support enabled!");
        }

        if (this.setupMVDWPlaceholders()) {
            this.logger.info("MVdWPlaceholderAPI support enabled!");
        }

        if (!this.setupEconomy()) {
            this.logger.severe("Plugin disabled due to no Vault/economy dependency found!");
            onDisable();
        } else {
            this.configLang = new BConfig(this, "lang-gangs.yml");
            this.configMain = new BConfig(this, "config-gangs.yml");
            Lang.setConfig(this.configLang);
            Settings.setConfig(this.configMain);
            this.fightManager = new FightManager(this);
            this.gangManager = new GangManager(this);
            this.playerManager = new PlayerManager(this);
            this.combatHandler = new CombatHandler(this);
            switch (Settings.databaseType) {
                case MYSQL:
                    this.dataManager = new MySqlDataManager(this);
                    break;
                case SQLITE:
                    this.dataManager = new SqliteDataManager(this);
            }

            this.dataManager.open();
            this.dataManager.setup();
            this.registerListeners();
            this.registerCommands();
        }

    }

    @Override
    public void onDisable() {
        if (this.dataManager != null) {
            this.dataManager.close();
        }

        if (Bukkit.getScheduler().isCurrentlyRunning(this.saveDataTaskId)) {
            Bukkit.getScheduler().cancelTask(this.saveDataTaskId);
        }
    }

    public void logRaw(String var1) {
        System.out.println(var1);
    }

    private boolean setupCrackShot() {
        if (getServer().getPluginManager().getPlugin("CrackShot") == null) {
            return false;
        } else {
            getServer().getPluginManager().registerEvents(new CrackShotListener(this), this);
            return true;
        }
    }

    private boolean setupEconomy() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            return false;
        } else {
            RegisteredServiceProvider var1 = getServer().getServicesManager().getRegistration(Economy.class);
            if (var1 == null) {
                return false;
            } else {
                this.economy = (Economy) var1.getProvider();
                return this.economy != null;
            }
        }
    }

    private boolean setupClipPlaceholders() {
        return getServer().getPluginManager().getPlugin("PlaceholderAPI") == null ? false : (new PlaceholderApiHandler(this)).register();
    }

    private boolean setupMVDWPlaceholders() {
        return true;
        /*
        if (getServer().getPluginManager().getPlugin("MVdWPlaceholderAPI") == null) {
            return false;
        } else {
            //(new MvdwPlaceholderHandler(this)).registerPlaceholders();
            return true;
        }

         */
    }
}
