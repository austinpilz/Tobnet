package com.au5tie.minecraft.tobnet.game.io;

import com.au5tie.minecraft.tobnet.game.TobnetGamePlugin;
import com.au5tie.minecraft.tobnet.game.arena.TobnetArena;
import com.au5tie.minecraft.tobnet.game.util.TobnetLogUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.lang.reflect.Constructor;
import java.sql.*;

/**
 * TODO! EXplain reflections.
 */
public class ArenaStorageManager extends StorageManager {

    public ArenaStorageManager(ExternalStorage externalStorage) {
        super(StorageManagerType.ARENA, externalStorage);
    }

    @Override
    public void prepareTables() {
        prepareArenaTable();
    }

    /**
     * Prepares the Arena table.
     * @author au5tie.
     */
    protected void prepareArenaTable() {
        // Establish our database connection.
        Connection connection = getExternalStorage().getConnection();

        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS \"arena\" (\"Name\" VARCHAR PRIMARY KEY NOT NULL, \"Class\" VARCHAR NOT NULL, \"B1X\" DOUBLE, \"B1Y\" DOUBLE, \"B1Z\" DOUBLE, \"B2X\" DOUBLE, \"B2Y\" DOUBLE, \"B2Z\" DOUBLE, \"World\" VARCHAR)");

            connection.commit();
            statement.close();
        } catch (Exception exception) {
            // Encountered an issue while trying to create the arena table.
            TobnetLogUtils.error("ArenaStorageManager >> prepareArenaTable() >> ", exception);
        }
    }

    /**
     * Load arenas into arena controller memory.
     * @author au5tie.
     */
    public void loadArenas() {
        try {
            Connection connection = getExternalStorage().getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement("SELECT `Name`, `Class`, `B1X`, `B1Y`, `B1Z`, `B2X`, `B2Y`, `B2Z`, `World` FROM `arena`");

            // Execute the query.
            ResultSet result = preparedStatement.executeQuery();

            // Count the number of arena's we've loaded.
            int count = 0;

            while (result.next()) {
                loadArena(result);
                count++;
            }

            connection.commit();
            preparedStatement.close();

            TobnetLogUtils.info("Loaded " + count + " arena(s).");
         } catch (Exception exception) {
            // We ran into some issue while attempting to load our arenas.
            TobnetLogUtils.error("ArenaStorageManager >> loadArenas() >> Encountered an error while attempting to load arenas", exception);
        }
    }

    /**
     * Converts the database representation of the Arena into the Arena (in it's subclass implementation). This will also
     * register the arena with the Arena Controller to make it accessible to the entire plugin.
     * @param result Arena ResultSet.
     * @throws SQLException SQL Exception while loading arena from the database.
     * @author au5tie.
     */
    private void loadArena(ResultSet result) throws Exception {
        // Create the arena by instantiating it via reflection.
        TobnetArena arena = createArena(result.getString("Name"), result.getString("Class"));

        // Boundaries.
        Location locationOne = new Location(Bukkit.getWorld(result.getString("World")), result.getDouble("B1X"),result.getDouble("B1Y"),result.getDouble("B1Z"));
        arena.setBoundaryOne(locationOne);

        Location locationTwo = new Location(Bukkit.getWorld(result.getString("World")), result.getDouble("B2X"),result.getDouble("B2Y"),result.getDouble("B2Z"));
        arena.setBoundaryTwo(locationTwo);

        // Register the arena with the Arena Controller.
        TobnetGamePlugin.getArenaController().registerArena(arena);
    }

    /**
     * Creates the arena of the implementing plugin's subclass of {@link TobnetArena}. The arenas are stored in the data
     * structure with their class name. Since we need to load the arenas in the Tobnet engine but the actual Arena class
     * type they'll be using is defined in the implementing plugin, we instantiate it via reflection.
     * @param arenaName Arena Name.
     * @param className Implemeting Arena Class Name.
     * @return Arena.
     * @throws Exception Exception while attempting to instantiate arena via reflection.
     * @author Austin Pilz n0286596
     */
    private TobnetArena createArena(String arenaName, String className) throws Exception {
        Class arenaClass = Class.forName(className);

        // Obtain the constructor defined in the arena contract.
        Class[] types = {String.class};
        Constructor arenaConstructor = arenaClass.getConstructor(types);

        Object[] parameters = {arenaName};
        return (TobnetArena) arenaConstructor.newInstance(parameters);
    }
}
