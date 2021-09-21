package dev.lightdream.chunkgangs.command.fight;


import dev.lightdream.chunkgangs.config.Lang;
import dev.lightdream.chunkgangs.config.Settings;
import dev.lightdream.chunkgangs.core.BSubCommand;
import dev.lightdream.chunkgangs.fight.FightChallenge;
import dev.lightdream.chunkgangs.gang.Gang;

public class CmdFightDecline extends BSubCommand {
    public CmdFightDecline() {
        this.aliases.add("decline");
        this.aliases.add("reject");
        this.aliases.add("d");
        this.correctUsage = "/fight decline <gang>";
        this.permission = "gangsplus.fight.decline";
        this.requiredRank = Settings.getRequiredRank("fightDecline");
        this.senderMustBePlayer = true;
        this.senderMustBeInGang = true;
        this.senderMustBeWithoutGang = false;
    }

    public void execute() {
        if (this.args.length < 1) {
            this.sendCorrectUsage();
        } else {
            String var1 = this.buildStringFromArgs(0, this.args.length - 1);
            if (!this.main.getGangManager().isGang(var1)) {
                this.msg(Lang.MSG_INVALIDGANG.toMsg());
            } else {
                Gang var2 = this.main.getGangManager().getGang(var1);
                if (!this.main.getFightManager().isChallenged(var2, this.gang)) {
                    this.msg(Lang.MSG_FIGHT_DECLINE_NOTCHALLENGED.toMsg().replace("%gang%", var2.getName()));
                } else {
                    FightChallenge var3 = this.main.getFightManager().getChallenge(this.gang, var2);
                    this.main.getFightManager().removeChallenge(var3);
                    this.msg(Lang.MSG_FIGHT_DECLINE_DECLINED.toMsg().replace("%gang%", var2.getName()));
                }
            }
        }
    }
}
