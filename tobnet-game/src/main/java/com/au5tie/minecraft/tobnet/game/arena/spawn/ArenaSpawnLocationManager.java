package com.au5tie.minecraft.tobnet.game.arena.spawn;


import com.au5tie.minecraft.tobnet.game.TobnetGamePlugin;
import com.au5tie.minecraft.tobnet.game.arena.TobnetArena;
import com.au5tie.minecraft.tobnet.game.arena.location.ArenaLocation;
import com.au5tie.minecraft.tobnet.game.arena.location.ArenaLocationManager;
import com.au5tie.minecraft.tobnet.game.arena.location.ArenaLocationUtils;
import com.au5tie.minecraft.tobnet.game.arena.manager.ArenaManager;
import com.au5tie.minecraft.tobnet.game.arena.manager.ArenaManagerType;
import com.au5tie.minecraft.tobnet.game.arena.manager.ArenaManagerUtils;
import com.au5tie.minecraft.tobnet.game.exception.TobnetEngineException;
import com.au5tie.minecraft.tobnet.game.message.MessageConstants;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The Arena Spawn Location Manager handles the management of possible spawn locations for players when the game begins.
 *
 * @author au5tie
 */
public class ArenaSpawnLocationManager extends ArenaManager {

    public static final String SPAWN_LOCATION_TYPE = "SPAWN";

    private final List<ArenaLocation> locations;

    private ArenaLocationManager locationManager;

    public ArenaSpawnLocationManager(TobnetArena arena) {
        super(arena);

        this.locations = new ArrayList<>();
    }

    @Override
    public ArenaManagerType getType() {
        return ArenaManagerType.SPAWN_POINT;
    }

    @Override
    public void prepareManager() {
        //
    }

    @Override
    public void destroyManager() {
        //
    }

    @Override
    public void afterArenaPreparationComplete() {
        // Link to the player manager.
        locationManager = (ArenaLocationManager) ArenaManagerUtils.getManagerOfType(getArena(), ArenaManagerType.LOCATION).orElseThrow(TobnetEngineException::new);
    }

    @Override
    public void afterArenaStorageLoadComplete() {
        // The arena has completed loading data, we can load the spawn points from the main location manager.
        loadLocations();
    }

    /**
     * Loads all of the spawn points for the arena. This will pull just the Spawn type locations from the overall arena
     * location manager.
     *
     * @author au5tie
     */
    private void loadLocations() {
        // Obtain just the spawn locations from the location storage manager.
        List<ArenaLocation> spawnLocations = ArenaLocationUtils.filterLocationsByType(SPAWN_LOCATION_TYPE, locationManager.getLocations());

        locations.addAll(spawnLocations);
    }

    /**
     * Returns all of the registered spawn points in the arena.
     *
     * @return Spawn points.
     * @author au5tie
     */
    public List<ArenaLocation> getSpawnLocations() {

        return Collections.unmodifiableList(locations);
    }

    /**
     * Returns the number of registered spawn locations.
     *
     * @return Number of registered spawn locations.
     * @author au5tie
     */
    public int getNumberLocations() {

        return locations.size();
    }

    /**
     * Generates the console status lines pertaining the game status.
     *
     * @return Console status lines.
     * @author au5tie
     */
    @Override
    public List<String> getConsoleStatusLines(CommandSender sender) {

        return List.of(TobnetGamePlugin.getMessageController().getMessage(MessageConstants.CONSOLE_SPAWN_LOCATIONS) +
                ": " + getNumberLocations());
    }
}
