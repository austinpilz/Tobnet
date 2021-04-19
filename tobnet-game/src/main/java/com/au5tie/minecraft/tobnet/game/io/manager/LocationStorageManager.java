package com.au5tie.minecraft.tobnet.game.io.manager;

import com.au5tie.minecraft.tobnet.game.TobnetGamePlugin;
import com.au5tie.minecraft.tobnet.game.arena.TobnetArena;
import com.au5tie.minecraft.tobnet.game.arena.location.ArenaLocation;
import com.au5tie.minecraft.tobnet.game.arena.location.ArenaLocationManager;
import com.au5tie.minecraft.tobnet.game.arena.manager.ArenaManagerType;
import com.au5tie.minecraft.tobnet.game.arena.manager.ArenaManagerUtils;
import com.au5tie.minecraft.tobnet.game.exception.TobnetEngineException;
import com.au5tie.minecraft.tobnet.game.io.ExternalStorage;
import com.au5tie.minecraft.tobnet.game.io.StorageManager;
import com.au5tie.minecraft.tobnet.game.io.StorageManagerType;
import com.au5tie.minecraft.tobnet.game.util.TobnetLogUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.collections4.CollectionUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.lang.reflect.Constructor;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * The Location Storage Manager is responsible for the storage and retrieval of the {@link com.au5tie.minecraft.tobnet.game.arena.location.ArenaLocation}s
 * for every arena.
 *
 * @author au5tie
 */
public class LocationStorageManager extends StorageManager {

    private static final String LOCATION_COLUMN_NAME = "Name";
    private static final String LOCATION_COLUMN_CLASS = "Class";
    private static final String LOCATION_COLUMN_ARENA = "Arena";
    private static final String LOCATION_COLUMN_TYPE = "Type";
    private static final String LOCATION_COLUMN_METADATA = "Metadata";
    private static final String LOCATION_COLUMN_X = "X";
    private static final String LOCATION_COLUMN_Y = "Y";
    private static final String LOCATION_COLUMN_Z = "Z";
    private static final String LOCATION_COLUMN_WORLD = "World";
    private static final String LOCATION_COLUMN_PITCH = "Pitch";
    private static final String LOCATION_COLUMN_YAQ = "Yaw";

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
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS \"location\" (\"Name\" VARCHAR PRIMARY KEY NOT NULL, \"Class\" VARCHAR NOT NULL, \"Arena\" VARCHAR NOT NULL, \"Type\" VARCHAR NOT NULL, \"X\" DOUBLE, \"Y\" DOUBLE, \"Z\" DOUBLE, \"Pitch\" FLOAT, \"Yaw\" FLOAT, \"World\" VARCHAR, \"Metadata\" TEXT, FOREIGN KEY(\"Arena\") REFERENCES \"Arena\"(\"Name\"))");

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

        try {
            Connection connection = getExternalStorage().getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement("SELECT `Name`, `Class`, `Arena`, `Type`, `X`, `Y`, `Z`, `World`, `Yaw`, `Pitch` FROM `location` WHERE `Arena` = ?");

            preparedStatement.setString(1, arena.getName() + "");

            // Execute the query.
            ResultSet result = preparedStatement.executeQuery();

            // Count the number of locations we've loaded.
            int count = 0;

            while (result.next()) {
                loadLocation(result);
                count++;
            }

            connection.commit();
            preparedStatement.close();

            if (count > 0) {
                TobnetLogUtils.info("Loaded " + count + " location(s) for " + arena.getName() + ".");
            }
        } catch (Exception exception) {
            // We ran into some issue while attempting to load locations for this arena.
            TobnetLogUtils.error("LocationStorageManager >> loadLocations() >> Encountered an error while attempting to load locations for arena " + arena.getName(), exception);
        }
    }

    /**
     * Loads the ResultSet into an {@link ArenaLocation}. This will create the location, populate the data fields, and
     * register the newly loaded location within the arena.
     *
     * @param result Location result set.
     * @author au5tie
     */
    private void loadLocation(ResultSet result) {

        try {

            // Attempt to locate the arena to which this location belongs.
            Optional<TobnetArena> arena = TobnetGamePlugin.getArenaController().getArenaByName(result.getString(LOCATION_COLUMN_ARENA));

            if (!arena.isPresent()) {
                // The arena this location belongs do is not loaded.
                return;
            }

            // Create the location via reflection.
            ArenaLocation arenaLocation = createLocation(result.getString(LOCATION_COLUMN_NAME), result.getString(LOCATION_COLUMN_CLASS));

            // Populate additional fields.
            arenaLocation.setType(result.getString(LOCATION_COLUMN_TYPE));

            // Obtain the bucket location.
            Location serverLocation = new Location(Bukkit.getWorld(result.getString(LOCATION_COLUMN_WORLD)), result.getDouble(LOCATION_COLUMN_X),
                    result.getDouble(LOCATION_COLUMN_Y), result.getDouble(LOCATION_COLUMN_Z), result.getFloat(LOCATION_COLUMN_YAQ),
                    result.getFloat(LOCATION_COLUMN_PITCH));

            arenaLocation.setLocation(serverLocation);

            // Metadata conversion.
            Map<String, String> metadata = metadataFromJson(result.getString(LOCATION_COLUMN_METADATA));
            metadata.forEach((key,value) -> arenaLocation.addMetadata(key, value));

            // Link the arena.
            arenaLocation.setArena(arena.get());

            // Register the finalized location with the arena's location manager.
            ArenaLocationManager locationManager = (ArenaLocationManager) ArenaManagerUtils.getManagerOfType(arena.get(), ArenaManagerType.LOCATION).orElseThrow(TobnetEngineException::new);
            locationManager.loadLocation(arenaLocation);
        } catch (Exception exception) {
            // We encountered some error while loading this location.
            TobnetLogUtils.error("LocationStorageManager >> loadLocation() >> There was an error attempting to load a location {" + result.toString() + "}", exception);
        }
    }

    /**
     * Obtains metadata map from the JSON representation.
     *
     * @param rawMetadata Metadata JSON representation.
     * @return Metadata map.
     * @throws Exception Mapping exception.
     * @author au5tie
     */
    private Map<String, String> metadataFromJson(String rawMetadata) throws Exception {

        return new ObjectMapper().readValue(rawMetadata, HashMap.class);
    }

    /**
     * Converts a location's metadata to a JSON string for storage.
     *
     * @param location Arena Location.
     * @return Location metadata as a String.
     * @throws Exception Mapping exception.
     * @author au5tie
     */
    private String metadataToJson(ArenaLocation location) throws Exception {

        return new ObjectMapper().writeValueAsString(location.getMetadataAsJson());
    }

    /**
     * Creates the {@link ArenaLocation}. This will use the provided class name to perform a reflection to create the
     * correct implementing class object.
     *
     * @param name Arena Location Name/ID.
     * @param className Arena Location implementing class name.
     * @return Arena Location.
     * @throws Exception Exception during location creation.
     * @author au5tie
     */
    private ArenaLocation createLocation(String name, String className) throws Exception {

        Class arenaClass = Class.forName(className);

        // Obtain the constructor defined in the location contract.
        Class[] types = {String.class};
        Constructor constructor = arenaClass.getConstructor(types);

        Object[] parameters = {name};
        return (ArenaLocation) constructor.newInstance(parameters);
    }
}
