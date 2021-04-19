package com.au5tie.minecraft.tobnet.game;

import com.au5tie.minecraft.tobnet.game.admin.TobnetArenaCommandListener;
import com.au5tie.minecraft.tobnet.game.command.CommandController;
import com.au5tie.minecraft.tobnet.game.controller.ArenaController;
import com.au5tie.minecraft.tobnet.game.io.ExternalStorage;
import com.au5tie.minecraft.tobnet.game.message.MessageController;
import com.au5tie.minecraft.tobnet.game.time.TimeDifference;
import com.au5tie.minecraft.tobnet.game.util.TobnetLogUtils;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.LocalDateTime;

public abstract class TobnetGamePlugin extends JavaPlugin {

    public static TobnetGamePlugin instance;

    private static MessageController messageController;
    private static CommandController commandController;
    private static ArenaController arenaController;

    private static ExternalStorage externalStorage;

    public static String chatPrefix = "[Tobnet] ";

    public TobnetGamePlugin() {

        // Instance.
        instance = this;
    }

    /**
     * Performs implementing plugin on load tasks. This allows for the implementing plugin to perform it's own onEnable
     * actions in addition to that of the core engine.
     *
     * @author au5tie
     */
    public abstract void enablePlugin();

    /**
     * Performs implementing plugin on unload tasks. This allows for the implementing plugin to perform it's own onDisable
     * actions in addition to that of the core engine.
     *
     * @author au5tie
     */
    public abstract void disablePlugin();

    @Override
    public final void onEnable() {

        // Begin loading the engine.
        LocalDateTime pluginLoadStart = LocalDateTime.now();
        TobnetLogUtils.getLogger().info("Engine loading begin.");

        // Setup Controllers.
        this.messageController = new MessageController();
        this.commandController = new CommandController(this);
        this.arenaController = new ArenaController();

        // External Storage - Prepare and load data from external data source.
        this.externalStorage = new ExternalStorage();
        externalStorage.onStartup();

        // Allow the implementing plugin to perform configuration of it's own.
        enablePlugin();

        // Register Tobnet Engine global admin commands.
        getCommandController().registerCommandLister(new TobnetArenaCommandListener());

        // Load complete. Log the round trip time.
        TobnetLogUtils.info("Engine loaded successfully. Took " +
                new TimeDifference(pluginLoadStart, LocalDateTime.now()));
    }

    @Override
    public final void onDisable() {
        // Begin unloading the engine.
        LocalDateTime pluginUnloadStart = LocalDateTime.now();
        TobnetLogUtils.getLogger().info("Engine unload begin.");

        // Allow the implementing plugin to perform shutdown operations.
        disablePlugin();

        // Allow external storage to perform shutdown operations.
        externalStorage.onShutdown();

        // Unload complete. Log the round trip time.
        TobnetLogUtils.info("Engine unloaded successfully. Took " +
                new TimeDifference(pluginUnloadStart, LocalDateTime.now()));
    }

    /**
     * Returns the {@link MessageController}.
     *
     * @return Message Controller.
     * @author au5tie
     */
    public static MessageController getMessageController() {

        return messageController;
    }

    /**
     * Returns the {@link ArenaController}.
     *
     * @return Arena Controller.
     * @author au5tie
     */
    public static ArenaController getArenaController() {

        return arenaController;
    }

    /**
     * Returns the {@link CommandController}.
     *
     * @return Command Controller.
     * @author au5tie
     */
    public static CommandController getCommandController() {

        return commandController;
    }

    public static TobnetGamePlugin getInstance() {

        return instance;
    }
}
