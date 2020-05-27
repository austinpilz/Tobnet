package com.au5tie.minecraft.tobnet.core.arena.handler;

import com.au5tie.minecraft.tobnet.core.arena.Arena;
import com.au5tie.minecraft.tobnet.core.arena.manager.ArenaManager;
import org.bukkit.event.Listener;

public class ArenaEventHandler implements Listener {

    private final Arena arena;
    private final ArenaManager manager;

    public ArenaEventHandler(Arena arena, ArenaManager manager) {

        this.arena = arena;
        this.manager = manager;
    }


    /**
     * @return Arena.
     * @author au5tie
     */
    public Arena getArena() {
        return arena;
    }
}
