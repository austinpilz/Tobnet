package com.au5tie.minecraft.tobnet.game.io;

import com.au5tie.minecraft.tobnet.game.TobnetGamePlugin;
import com.au5tie.minecraft.tobnet.game.controller.TobnetController;
import com.au5tie.minecraft.tobnet.game.event.TobnetEventPublisher;
import com.au5tie.minecraft.tobnet.game.io.event.TobnetExternalStorageLoadCompleteEvent;
import com.au5tie.minecraft.tobnet.game.io.manager.ArenaStorageManager;
import com.au5tie.minecraft.tobnet.game.io.manager.LocationStorageManager;
import com.au5tie.minecraft.tobnet.game.util.TobnetLogUtils;
import com.google.inject.Singleton;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * External Storage is the Tobnet Engine's entry point to external data storage, retrieval, and configuration management.
 * This class is responsible for managing the plugin's overall connection to external sources.
 *
 * @author au5tie
 */
@Singleton
public class TobnetStorageController implements TobnetController {

    private final Map<StorageManagerType, StorageManager> storageManagers;
    private Connection connection;

    public TobnetStorageController() {

        // Prepare the various storage managers.
        storageManagers = new HashMap<>();
        prepareManagers();
    }

    /**
     * Prepares all of the storage managers which control the storage/retrieval of their various managed components.
     *
     * @author au5tie
     */
    private void prepareManagers() {

        addManager(new ArenaStorageManager(this));
        addManager(new LocationStorageManager(this));
    }

    /**
     * Registers a new Storage Manager.
     *
     * @param manager Storage Manager.
     * @author au5tie
     */
    private void addManager(StorageManager manager) {

        storageManagers.put(manager.getType(), manager);
    }

    /**
     * Obtains the {@link StorageManager} for the provided {@link StorageManagerType}.
     *
     * @param type Storage Manager Type.
     * @return Storage Manager for provided Storage Manager Type.
     * @author au5tie
     */
    public StorageManager getManager(StorageManagerType type) {

        return storageManagers.get(type);
    }

    /**
     * Creates the local plugin directory if it does not exist already.
     *
     * @author au5tie
     */
    private void createLocalDirectory() {

        if (!TobnetGamePlugin.instance.getDataFolder().exists()) {
            try {
                (TobnetGamePlugin.instance.getDataFolder()).mkdir();
            } catch (Exception exception) {
                // We encountered an error while attempting to create the local directory.
                TobnetLogUtils.error("ExternalStorage >> createLocalDirectory() >> Encountered an error while attempting to create local directory", exception);
            }
        }
    }

    /**
     * Obtains the external storage connection. If an existing connection does not already exist or if the
     * existing connection has closed, this will create a new one.
     *
     * @return Connection.
     * @author au5tie
     */
    public synchronized Connection getConnection() {

        if (connection == null) connection = createConnection();

        try {
            if(connection.isClosed()) connection = createConnection();
        } catch (SQLException exception) {
            // There was an error attempting to check if the connection was closed.
            TobnetLogUtils.error("ExternalStorage >> getConnection() >> Encountered an error while attempting to verify if connection is already closed", exception);
        }

        return connection;
    }

    /**
     * Creates a new connection to the external storage.
     *
     * @return Connection.
     * @author au5tie
     */
    private Connection createConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection ret = DriverManager.getConnection("jdbc:sqlite:" +  new File(TobnetGamePlugin.instance.getDataFolder().getPath(), "db.sqlite").getPath());
            ret.setAutoCommit(false);

            return ret;
        } catch (Exception exception) {
            TobnetLogUtils.error("ExternalStorage >> createConnection() >> Encountered an error while attempting to create a new connection", exception);
            return null;
        }
    }

    /**
     * Closes the connection to the external storage, if one exists and is currently open.
     *
     * @author au5tie
     */
    private synchronized void freeConnection() {

        Connection connection = getConnection();

        if (connection != null) {
            try {
                connection.close();
                this.connection = null;
            } catch (SQLException exception) {
                // Encountered an error while attempting to close the existing database connection.
                TobnetLogUtils.error("ExternalStorage >> freeConnection() >> Unable to free connection", exception);
            }
        }
    }

    /**
     * Performs external storage startup operations.
     *
     * @author au5tie
     */
    public void onStartup() {
        // Create local directory if does not exist already.
        createLocalDirectory();

        // Invoke the underlying storage managers to prepare their tables.
        storageManagers.values().forEach(StorageManager::prepareTables);

        // Have all of the managers load data into memory.
        loadData();

        // Publish event out that we've finished loading data for each arena. This alerts arena managers to pull data.
        TobnetEventPublisher.publishEvent(new TobnetExternalStorageLoadCompleteEvent());
    }

    /**
     * Initiates the loading of all of the storage managers. This will load all of the necessary data from external data
     * into memory.
     *
     * The {@link ArenaStorageManager} has to be explicitly run first since the arenas are what all of the other data is
     * associated with and requires for storage. Once all of the areas have been loaded, then we nicely loop through all
     * of the other registered managers and have them perform their load process.
     *
     * @author au5tie
     */
    private void loadData() {
        // Load all of the arenas first.
        ArenaStorageManager arenaStorageManager = (ArenaStorageManager)getManager(StorageManagerType.ARENA);
        arenaStorageManager.performDataLoad();

        // Have all of the other managers load their data into memory now that the arenas are in place.
        storageManagers.values().stream()
                .filter(manager -> !StorageManagerType.ARENA.equals(manager.getType()))
                .forEach(StorageManager::performDataLoad);
    }

    /**
     * Performs external storage shutdown operations when the plugin is being disabled for whatever reason.
     *
     * @author au5tie
     */
    public void onShutdown() {

        freeConnection();
    }
}
