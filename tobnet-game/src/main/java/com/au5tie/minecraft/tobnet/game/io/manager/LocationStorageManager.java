package com.au5tie.minecraft.tobnet.game.io.manager;

import com.au5tie.minecraft.tobnet.game.TobnetGamePlugin;
import com.au5tie.minecraft.tobnet.game.arena.TobnetArena;
import com.au5tie.minecraft.tobnet.game.io.ExternalStorage;
import com.au5tie.minecraft.tobnet.game.io.StorageManager;
import com.au5tie.minecraft.tobnet.game.io.StorageManagerType;
import com.au5tie.minecraft.tobnet.game.util.TobnetLogUtils;
import org.apache.commons.collections4.CollectionUtils;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

/**
 * The Location Storage Manager is responsible for the storage and retrieval of the {@link com.au5tie.minecraft.tobnet.game.arena.location.ArenaLocation}s
 * for every arena.
 *
 * @author au5tie
 */
public class LocationStorageManager extends StorageManager {

    public LocationStorageManager(ExternalStorage externalStorage) {
        super(StorageManagerType.LOCATION, externalStorage);
    }

    @Override
    public void prepareTables() {

        prepareLocationTable();
    }

    /**
     * Prepares the Location table.
     *
     * @author au5tie.
     */
    protected void prepareLocationTable() {
        // Establish our database connection.
        Connection connection = getExternalStorage().getConnection();

        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS \"location\" (\"Name\" VARCHAR PRIMARY KEY NOT NULL, \"Class\" VARCHAR NOT NULL, \"Arena\" VARCHAR NOT NULL, \"Type\" VARCHAR NOT NULL, \"X\" DOUBLE, \"Y\" DOUBLE, \"Z\" DOUBLE, \"Pitch\" FLOAT, \"Yaw\" FLOAT, \"World\" VARCHAR)");
            // TODO Foreign Key

            connection.commit();
            statement.close();
        } catch (Exception exception) {
            // Encountered an issue while trying to create the table.
            TobnetLogUtils.error("LocationStorageManager >> prepareLocationTable() >> ", exception);
        }
    }

    /**
     * Performs the loading of all locations for all of the currently registered arenas.
     *
     * This requires that the arenas have already been loaded and registered with the plugin. This will nicely iterate
     * through each one, loading all of their locations and registering them as appropriate. If there have been no arenas
     * registered with the game, this will effectively perform nothing.
     *
     * @author au5tie
     */
    protected void loadData() {

        if (TobnetGamePlugin.getArenaController() != null && CollectionUtils.isNotEmpty(TobnetGamePlugin.getArenaController().getArenas())) {
            // Load the locations for each arena, one by one.
            TobnetGamePlugin.getArenaController().getArenas().forEach(this::loadLocationsForArena);
        }
    }

    /**
     * Loads all locations for the provided arena.
     *
     * @param arena Arena.
     * @author au5tie
     */
    private void loadLocationsForArena(TobnetArena arena) {
        //
    }

    private void loadLocation(ResultSet result) {
        //
    }
}
