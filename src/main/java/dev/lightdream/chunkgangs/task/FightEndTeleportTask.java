package dev.lightdream.chunkgangs.task;

import dev.lightdream.chunkgangs.fight.FightArena;
import dev.lightdream.chunkgangs.fight.FightArenaState;

public class FightEndTeleportTask implements Runnable {
    private final FightArena fightArena;

    public FightEndTeleportTask(FightArena var1) {
        this.fightArena = var1;
    }

    public void run() {
        this.fightArena.removeAllPlayers();
        this.fightArena.setMoney(0.0D);
        this.fightArena.setMembers(0);
        this.fightArena.setGang1(null);
        this.fightArena.setGang2(null);
        this.fightArena.setState(FightArenaState.EMPTY);
    }
}

