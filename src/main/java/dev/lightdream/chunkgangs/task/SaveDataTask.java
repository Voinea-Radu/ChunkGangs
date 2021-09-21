package dev.lightdream.chunkgangs.task;

import dev.lightdream.chunkgangs.GangsPlugin;
import dev.lightdream.chunkgangs.gang.Gang;
import dev.lightdream.chunkgangs.player.PlayerData;

import java.util.Iterator;

public class SaveDataTask implements Runnable {
    private final GangsPlugin main;

    public SaveDataTask(GangsPlugin var1) {
        this.main = var1;
    }

    public void run() {
        Iterator var1 = this.main.getGangManager().getAllGangs().iterator();

        while (var1.hasNext()) {
            Gang var2 = (Gang) var1.next();
            if (var2.isRequiresUpdate()) {
                this.main.getDataManager().updateGang(var2);
                var2.setRequiresUpdate(false);
            }
        }

        var1 = this.main.getPlayerManager().getAllPlayerData().values().iterator();

        while (var1.hasNext()) {
            PlayerData var3 = (PlayerData) var1.next();
            if (var3.isRequiresUpdate()) {
                this.main.getDataManager().updatePlayerData(var3);
            }
        }

    }
}

