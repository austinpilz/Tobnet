package com.au5tie.minecraft.tobnet.game;

import com.au5tie.minecraft.tobnet.core.arena.Arena;
import com.au5tie.minecraft.tobnet.game.controller.ArenaController;
import com.au5tie.minecraft.tobnet.game.util.TimeDifference;
import com.au5tie.minecraft.tobnet.game.util.TobnetLogUtils;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.LocalDateTime;

public abstract class TobnetGamePlugin extends JavaPlugin {

    public static TobnetGamePlugin instance;

    private static ArenaController arenaController;

    public TobnetGamePlugin() {

        // Instance.
        instance = this;

        // Arena.
        this.arenaController = new ArenaController();
    }

    /**
     * Performs implementing plugin on load tasks. This allows for the implementing plugin to perform it's own onEnable
     * actions in addition to that of the core engine.
     * @author au5tie
     */
    public abstract void enablePlugin();

    /**
     * Performs implementing plugin on unload tasks. This allows for the implementing plugin to perform it's own onDisable
     * actions in addition to that of the core engine.
     * @author au5tie
     */
    public abstract void disablePlugin();

    @Override
    public void onEnable() {

        // Begin loading the engine.
        LocalDateTime pluginLoadStart = LocalDateTime.now();
        TobnetLogUtils.getLogger().info("Engine loading begin.");

        // Allow the implementing plugin to perform configuration of it's own.
        enablePlugin();

        // Event Listeners.
        registerEventListeners();

        // Load complete. Log the round trip time.
        TobnetLogUtils.info("Engine loaded successfully. Took " +
                new TimeDifference(pluginLoadStart, LocalDateTime.now()));
    }

    @Override
    public void onDisable() {
        // Begin unloading the engine.
        LocalDateTime pluginUnloadStart = LocalDateTime.now();
        TobnetLogUtils.getLogger().info("Engine unload begin.");

        // Unload complete. Log the round trip time.
        TobnetLogUtils.info("Engine unloaded successfully. Took " +
                new TimeDifference(pluginUnloadStart, LocalDateTime.now()));
    }

    /**
     * Returns the {@link ArenaController}.
     * @return Arena Controller.
     * @author au5tie
     */
    public static ArenaController getArenaController() {

        return arenaController;
    }

    public static TobnetGamePlugin getInstance() {

        return instance;
    }

    /**
     * Registers are necessary event listeners.
     * @author au5tie
     */
    private void registerEventListeners() {

        // Register arena-specific event listeners.
        arenaController.getArenas().forEach(this::registerArenaListeners);
    }

    /**
     * Registers all of the provided {@link Arena}'s
     * as an event listener with Bukkit.
     * @param arena Arena.
     * @author au5tie
     */
    private void registerArenaListeners(Arena arena) {
        // Register all of the arena's manager event handlers with Bukkit as an event listener.
        arena.getManagers().forEach(manager -> getServer()
                .getPluginManager()
                .registerEvents(manager.getEventHandler(), this));
    }
}
