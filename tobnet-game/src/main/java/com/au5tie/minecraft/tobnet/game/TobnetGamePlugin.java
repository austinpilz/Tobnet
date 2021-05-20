package com.au5tie.minecraft.tobnet.game;

import com.au5tie.minecraft.tobnet.game.command.TobnetCommandController;
import com.au5tie.minecraft.tobnet.game.command.listener.TobnetBaseArenaCommandListener;
import com.au5tie.minecraft.tobnet.game.controller.ArenaController;
import com.au5tie.minecraft.tobnet.game.guice.TobnetPluginInjector;
import com.au5tie.minecraft.tobnet.game.io.TobnetStorageController;
import com.au5tie.minecraft.tobnet.game.message.TobnetMessageController;
import com.au5tie.minecraft.tobnet.game.time.TimeDifference;
import com.au5tie.minecraft.tobnet.game.util.TobnetLogUtils;
import com.google.inject.Binder;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public abstract class TobnetGamePlugin extends JavaPlugin implements Module {

    public static TobnetGamePlugin instance;

    private static ArenaController arenaController;
    private static TobnetStorageController storageController;
    private static TobnetCommandController commandController;
    private static TobnetMessageController messageController;

    public static String chatPrefix = "[Tobnet] ";

    // Guice.
    private Injector injector;
    private final List<Module> modules = new ArrayList<>();

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

        // Guice.
        this.injector = Guice.createInjector(new TobnetPluginInjector(this));
        this.injector.injectMembers(this);
        this.modules.forEach(this.injector::injectMembers);

        // Setup Tobnet engine base components.
        registerBaseControllers();
        registerBaseCommandListeners();

        // External Storage - Prepare and load data from external data source.
        storageController.onStartup();

        // Allow the implementing plugin to perform configuration of its own.
        enablePlugin();

        // Load complete. Log the round trip time.
        TobnetLogUtils.info("Engine loaded successfully. Took " +
                new TimeDifference(pluginLoadStart, LocalDateTime.now()));
    }

    @Override
    public void configure(Binder binder) {

        //binder.requestStaticInjection(TobnetStorageController.class);
        //binder.requestStaticInjection(TobnetCommandController.class);
    }

    @Override
    public final void onDisable() {
        // Begin unloading the engine.
        LocalDateTime pluginUnloadStart = LocalDateTime.now();
        TobnetLogUtils.getLogger().info("Engine unload begin.");

        // Allow the implementing plugin to perform shutdown operations.
        disablePlugin();

        // Allow external storage to perform shutdown operations.
        storageController.onShutdown();

        // Unload complete. Log the round trip time.
        TobnetLogUtils.info("Engine unloaded successfully. Took " +
                new TimeDifference(pluginUnloadStart, LocalDateTime.now()));
    }

    private void registerBaseControllers() {

        arenaController = new ArenaController();
        storageController = new TobnetStorageController();
        commandController = new TobnetCommandController();
        messageController = new TobnetMessageController();
    }

    private void registerBaseCommandListeners() {

        getCommandController().registerCommandLister(new TobnetBaseArenaCommandListener());
    }

    /**
     * Returns the Tobnet Engine Command Controller.
     *
     * @return Command Controller.
     * @author au5tie
     */
    public final TobnetCommandController getCommandController() {

        return commandController;
    }

    /**
     * Returns the {@link TobnetMessageController}.
     *
     * @return Message Controller.
     * @author au5tie
     */
    public static TobnetMessageController getMessageController() {

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

    public static TobnetGamePlugin getInstance() {

        return instance;
    }
}
