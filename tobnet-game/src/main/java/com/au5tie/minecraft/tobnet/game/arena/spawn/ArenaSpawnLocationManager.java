package com.au5tie.minecraft.tobnet.game.arena.spawn;


import com.au5tie.minecraft.tobnet.game.arena.TobnetArena;
import com.au5tie.minecraft.tobnet.game.arena.location.ArenaLocation;
import com.au5tie.minecraft.tobnet.game.arena.location.ArenaLocationManager;
import com.au5tie.minecraft.tobnet.game.arena.location.ArenaLocationUtils;
import com.au5tie.minecraft.tobnet.game.arena.manager.ArenaManager;
import com.au5tie.minecraft.tobnet.game.arena.manager.ArenaManagerType;
import com.au5tie.minecraft.tobnet.game.arena.manager.ArenaManagerUtils;
import com.au5tie.minecraft.tobnet.game.exception.TobnetEngineException;

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

    private void loadLocations() {
        // Obtain just the spawn locations from the location storage manager.
        List<ArenaLocation> spawnLocations = ArenaLocationUtils.filterLocationsByType(SPAWN_LOCATION_TYPE, locationManager.getLocations());

        locations.addAll(spawnLocations);
    }

    public List<ArenaLocation> getSpawnLocations() {

        return Collections.unmodifiableList(locations);
    }
}
