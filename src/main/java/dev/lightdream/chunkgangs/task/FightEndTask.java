package dev.lightdream.chunkgangs.task;

import dev.lightdream.chunkgangs.config.Lang;
import dev.lightdream.chunkgangs.config.Settings;
import dev.lightdream.chunkgangs.fight.FightArena;
import dev.lightdream.chunkgangs.fight.FightArenaState;

public class FightEndTask implements Runnable {
    private final FightArena fightArena;

    public FightEndTask(FightArena var1) {
        this.fightArena = var1;
    }

    public void run() {
        this.fightArena.setState(FightArenaState.ENDED);
        if (Settings.getBroadcast("fightEnd")) {
            Settings.broadcast(Lang.MSG_FIGHTS_END_ENDED_BROADCAST.toString().replace("%gang1%", this.fightArena.getGang1().getName()).replace("%gang2%", this.fightArena.getGang2().getName()));
        }

        this.fightArena.finish();
    }
}
