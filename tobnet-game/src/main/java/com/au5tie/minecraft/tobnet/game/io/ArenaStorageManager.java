package com.au5tie.minecraft.tobnet.game.io;

import lombok.AllArgsConstructor;

import java.sql.SQLException;

@AllArgsConstructor
public class ArenaStorageManager {

    /*private ExternalStorage externalStorage;
    private ArenaController arenaController;

    *//**
     * Prepares the Arena table.
     * @author au5tie.
     *//*
    protected void prepareArenaTable() {
        // Establish our database connection.
        Connection connection = externalStorage.getConnection();

        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS \"arena\" (\"Name\" VARCHAR PRIMARY KEY NOT NULL, \"B1X\" DOUBLE, \"B1Y\" DOUBLE, \"B1Z\" DOUBLE, \"B2X\" DOUBLE, \"B2Y\" DOUBLE, \"B2Z\" DOUBLE, \"World\" VARCHAR, \"LifeTimeGames\" INTEGER DEFAULT 0)");

            connection.commit();
            statement.close();
        } catch (Exception exception) {
            exception.printStackTrace();
        }
    }

    *//**
     * Load arenas into arena controller memory.
     * @author au5tie.
     *//*
    public void loadArenas() {
        try {
            Connection connection = externalStorage.getConnection();

            PreparedStatement preparedStatement = connection.prepareStatement("SELECT `Name`, `B1X`, `B1Y`, `B1Z`, `B2X`, `B2Y`, `B2Z`, `World`, `LifetimeGames` FROM `arena`");

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

            BloodWardenCore.log.info("Arena Storage Manager >> loadArenas() >> Loaded " + count + " arenas.");
         } catch (Exception exception) {
            BloodWardenCore.log.severe("Arena Storage Manager >> loadArenas() >> Encountered an error while attempting" +
                    " to load arenas: " + exception.getLocalizedMessage());
        }
    }

    *//**
     * Loads the {@link Arena} from the database result and adds it to the {@link ArenaController}.
     * @param result Arena ResultSet.
     * @throws SQLException SQL Exception while loading arena from the database.
     * @author au5tie.
     *//*
    private void loadArena(ResultSet result) throws SQLException {

        String arenaName = result.getString("Name");

        // Create the arena.
        Arena arena = new Arena(arenaName);

        // Boundary 1.
        Location boundary1 = new Location(Bukkit.getWorld(result.getString("World")), result.getDouble("B1X"),result.getDouble("B1Y"),result.getDouble("B1Z"));
        arena.setBoundaryOne(boundary1);

        // Boundary 2.
        Location boundary2 = new Location(Bukkit.getWorld(result.getString("World")), result.getDouble("B2X"),result.getDouble("B2Y"),result.getDouble("B2Z"));
        arena.setBoundaryTwo(boundary2);

        // Map.
        //TODO

        // Add the arena to the controller. This is what makes it accessible.
        arenaController.addArena(arena);
    }*/
}
