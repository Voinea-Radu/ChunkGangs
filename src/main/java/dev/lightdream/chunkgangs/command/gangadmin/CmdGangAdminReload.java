package dev.lightdream.chunkgangs.command.gangadmin;

import dev.lightdream.chunkgangs.config.Lang;
import dev.lightdream.chunkgangs.core.BSubCommand;

public class CmdGangAdminReload extends BSubCommand {
    public CmdGangAdminReload() {
        this.aliases.add("reload");
        this.aliases.add("r");
        this.permission = "gangsplus.gangadmin.reload";
        this.requiredRank = -1;
        this.senderMustBePlayer = false;
        this.senderMustBeInGang = false;
        this.senderMustBeWithoutGang = false;
    }

    public void execute() {
        this.main.reload();
        this.msg(Lang.MSG_GANGADMIN_RELOAD_RELOADED.toMsg());
    }
}
