package com.au5tie.minecraft.tobnet.core.arena.manager;

import com.au5tie.minecraft.tobnet.core.arena.Arena;
import com.au5tie.minecraft.tobnet.core.arena.handler.ArenaEventHandler;
import lombok.Getter;

@Getter
public abstract class ArenaManager {

    private final Arena arena;
    private ArenaEventHandler eventHandler;

    public ArenaManager(Arena arena) {
        this.arena = arena;

        // Register default event handler.
        registerEventHandler(new ArenaEventHandler(arena, this));
    }

    /**
     * Returns the {@link ArenaManagerType}.
     * @return Arena Manager Type.
     * @author au5tie
     */
    public abstract ArenaManagerType getType();

    /**
     * Prepare's the manager for first use.
     * @author au5tie
     */
    public abstract void prepareManager();

    /**
     * Registers the {@link ArenaEventHandler} to be used by the manager.
     * @param eventHandler Arena Event Handler.
     * @author au5tie
     */
    public final void registerEventHandler(ArenaEventHandler eventHandler) {

        this.eventHandler = eventHandler;
    }
}
