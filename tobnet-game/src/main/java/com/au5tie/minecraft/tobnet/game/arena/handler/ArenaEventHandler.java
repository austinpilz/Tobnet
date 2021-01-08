package com.au5tie.minecraft.tobnet.game.arena.handler;

import com.au5tie.minecraft.tobnet.game.arena.TobnetArena;
import com.au5tie.minecraft.tobnet.game.arena.manager.ArenaManager;
import org.bukkit.event.Listener;

public class ArenaEventHandler implements Listener {

    private final TobnetArena arena;
    private final ArenaManager manager;

    public ArenaEventHandler(TobnetArena arena, ArenaManager manager) {

        this.arena = arena;
        this.manager = manager;
    }

    /**
     * @return Arena.
     * @author au5tie
     */
    public TobnetArena getArena() {
        return arena;
    }
}
