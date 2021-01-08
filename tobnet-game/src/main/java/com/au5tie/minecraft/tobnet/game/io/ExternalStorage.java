package com.au5tie.minecraft.tobnet.game.io;

import com.au5tie.minecraft.tobnet.game.TobnetGamePlugin;
import com.au5tie.minecraft.tobnet.game.util.TobnetLogUtils;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * External Storage is the Tobnet Engine's entry point to external data storage, retrieval, and configuration management.
 * This class is responsible for managing the plugin's overall connection to external sources.
 * @author au5tie
 */
public class ExternalStorage {

    private final Map<StorageManagerType, StorageManager> storageManagers;
    private Connection connection;

    public ExternalStorage() {

        // Prepare the various storage managers.
        storageManagers = new HashMap<>();
        prepareManagers();
    }

    /**
     * Prepares all of the storage managers which control the storage/retrieval of their various managed components.
     * @author au5tie
     */
    private void prepareManagers() {

        addManager(new ArenaStorageManager(this));
    }

    /**
     * Registers a new Storage Manager.
     * @param manager Storage Manager.
     * @author au5tie
     */
    private void addManager(StorageManager manager) {

        storageManagers.put(manager.getType(), manager);
    }

    /**
     * Obtains the {@link StorageManager} for the provided {@link StorageManagerType}.
     * @param type Storage Manager Type.
     * @return Storage Manager for provided Storage Manager Type.
     * @author au5tie
     */
    public StorageManager getManager(StorageManagerType type) {

        return storageManagers.get(type);
    }

    /**
     * Creates the local plugin directory if it does not exist already.
     * @author au5tie
     */
    private void createLocalDirectory() {
        if (!TobnetGamePlugin.instance.getDataFolder().exists()) {
            try {
                (TobnetGamePlugin.instance.getDataFolder()).mkdir();
            } catch (Exception exception) {
                // We encountered an error while attempting to create the local directory.
                TobnetLogUtils.error("Encountered an error while attempting to create local directory", exception);
            }
        }
    }

    /**
     * Obtains the external storage connection. If an existing connection does not already exist or if the
     * existing connection has closed, this will create a new one.
     * @return Connection.
     * @author au5tie
     */
    synchronized Connection getConnection() {

        if (connection == null) connection = createConnection();

        try {
            if(connection.isClosed()) connection = createConnection();
        } catch (SQLException exception) {
            // There was an error attempting to check if the connection was closed.
            TobnetLogUtils.error("External Storage >> getConnection() >> Encountered an error while attempting to verify if connection is already closed", exception);
        }

        return connection;
    }

    /**
     * Creates a new connection to the external storage.
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
                TobnetLogUtils.error("External Storage >> freeConnection() >> Unable to free connection", exception);
            }
        }
    }

    /**
     * Performs external storage startup operations.
     * @author au5tie
     */
    public void onStartup() {
        // Create local directory if does not exist already.
        createLocalDirectory();

        // Invoke the underlying storage managers to prepare their tables.
        storageManagers.values().forEach(StorageManager::prepareTables);
    }

    /**
     * Performs external storage shutdown operations when the plugin is being disabled for whatever reason.
     * @author au5tie
     */
    public void onShutdown() {

        freeConnection();
    }
}
