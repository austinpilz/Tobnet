package com.au5tie.minecraft.tobnet.game.io;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ExternalStorage {

    /*private static Connection connection;

    private final ArenaController arenaController;
    private final ArenaStorageManager arenaStorageManager;

    public ExternalStorage(ArenaController arenaController) {
        createLocalDirectory();

        this.arenaController = arenaController;

        // Storage Managers.
        arenaStorageManager = new ArenaStorageManager(this, arenaController);
    }

    *//**
     * Creates the local plugin directory if it does not exist already.
     * @author au5tie.
     *//*
    private void createLocalDirectory() {
        if (!BloodWardenCore.instance.getDataFolder().exists()) {
            try {
                (BloodWardenCore.instance.getDataFolder()).mkdir();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    *//**
     * Prepares the database connection.
     * @return Connection.
     * @author au5tie.
     *//*
    private static Connection createConnection() {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection ret = DriverManager.getConnection("jdbc:sqlite:" +  new File(BloodWardenCore.instance.getDataFolder().getPath(), "db.sqlite").getPath());
            ret.setAutoCommit(false);
            return ret;
        } catch (Exception exception) {
            BloodWardenCore.log.log(Level.SEVERE, BloodWardenCore.consolePrefix + "Exception occurred while " +
                    "attempting to setup database connection: " + exception.getLocalizedMessage());
            return null;
        }
    }

    public static synchronized Connection getConnection() {
        if (connection == null) connection = createConnection();

        try {
            if(connection.isClosed()) connection = createConnection();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        return connection;
    }

    public static synchronized void freeConnection() {
        Connection conn = getConnection();
        if(conn != null) {
            try {
                conn.close();
                conn = null;
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    *//**
     * Prepares the database tables.
     * @author au5tie.
     *//*
    public void prepareTables() {
        // Arena.
        arenaStorageManager.prepareArenaTable();
    }*/
}
