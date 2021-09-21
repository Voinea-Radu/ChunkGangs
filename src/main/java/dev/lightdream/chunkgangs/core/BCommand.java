package dev.lightdream.chunkgangs.core;

import dev.lightdream.chunkgangs.GangsPlugin;

import java.util.ArrayList;
import java.util.List;

public class BCommand {
    public GangsPlugin main;
    public List<BSubCommand> subcommands;

    public BCommand(GangsPlugin var1) {
        this.main = var1;
        this.subcommands = new ArrayList();
    }
}
