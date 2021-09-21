package dev.lightdream.chunkgangs.command.fight;


import dev.lightdream.chunkgangs.config.Lang;
import dev.lightdream.chunkgangs.config.Settings;
import dev.lightdream.chunkgangs.core.BSubCommand;
import dev.lightdream.chunkgangs.fight.FightArena;

public class CmdFightLeave extends BSubCommand {
    public CmdFightLeave() {
        this.aliases.add("leave");
        this.aliases.add("l");
        this.permission = "gangsplus.fight.leave";
        this.requiredRank = Settings.getRequiredRank("fightLeave");
        this.senderMustBePlayer = true;
        this.senderMustBeInGang = true;
        this.senderMustBeWithoutGang = false;
    }

    public void execute() {
        if (!this.main.getFightManager().isGangInArena(this.gang)) {
            this.msg(Lang.MSG_FIGHT_LEAVE_NOTFIGHTING.toMsg());
        } else if (!this.main.getFightManager().isPlayerInArena(this.player)) {
            this.msg(Lang.MSG_FIGHT_LEAVE_NOTINARENA.toMsg());
        } else {
            FightArena var1 = this.main.getFightManager().getPlayersArena(this.player);
            var1.removePlayer(this.player, true, true, false, true);
        }
    }
}
