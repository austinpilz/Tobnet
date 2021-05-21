package com.au5tie.minecraft.tobnet.game.arena.manager;

import com.au5tie.minecraft.tobnet.game.arena.TobnetArena;
import com.au5tie.minecraft.tobnet.game.arena.event.ArenaEventHandler;
import lombok.Getter;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * An Arena Manager is a core component of a {@link TobnetArena}. A manager (lol) manages an individual aspect of gameplay
 * within the arena.
 *
 * Managers can register {@link ArenaEventHandler} which will have server events routed to them when called. This allows
 * the manager to be self contained with their assets and react to external events from other managers, rather than direct
 * contact between the managers.
 *
 * @author au5tie
 */
@Getter
public abstract class ArenaManager {

    private final TobnetArena arena;

    private final List<ArenaEventHandler> eventHandlers = new ArrayList<>();

    public ArenaManager(TobnetArena arena) {
        this.arena = arena;

        // Register default event handler.
        registerEventHandler(new ArenaEventHandler(arena, this));
    }

    /**
     * Returns the {@link ArenaManagerType}.
     *
     * @return Arena Manager Type.
     * @author au5tie
     */
    public abstract ArenaManagerType getType();

    /**
     * Prepares the manager for first use.
     *
     * @author au5tie
     */
    public abstract void prepareManager();

    /**
     * Destroys the manager when no longer in use. This is critical for memory cleanup.
     *
     * @author au5tie
     */
    public abstract void destroyManager();

    /**
     * Notifies the manager that all of the initial arena managers have been fully setup and prepared.
     *
     * @author au5tie
     */
    public void afterArenaPreparationComplete() {
        // Nothing by default.
    }

    /**
     * Notifies the manager that all of the arena's storage has been loaded into the various storage managers and that
     * this manager can safely reference/load those objects.
     *
     * @author au5tie
     */
    public void afterArenaStorageLoadComplete() {
        // Nothing by default.
    }

    /**
     * Registers an {@link ArenaEventHandler} to be used by the manager.
     *
     * @param eventHandler Arena Event Handler.
     * @author au5tie
     */
    public final void registerEventHandler(ArenaEventHandler eventHandler) {

        eventHandlers.add(eventHandler);
    }

    /**
     * Generates console status lines to be displayed to the sender. These are generated whenever the console is attempting
     * to display information about the arena as a whole. This allows for per-manager implementation to avoid having the
     * command listener to have to keep track of every manager under the sun.
     *
     * @return List of status messages to display in the console.
     * @author au5tie
     */
    public List<String> getConsoleStatusLines(CommandSender sender) {

        return Collections.emptyList();
    }
}
