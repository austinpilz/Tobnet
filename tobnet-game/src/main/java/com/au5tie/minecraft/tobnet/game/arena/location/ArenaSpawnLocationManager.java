package com.au5tie.minecraft.tobnet.game.arena.location;


import com.au5tie.minecraft.tobnet.game.arena.TobnetArena;
import com.au5tie.minecraft.tobnet.game.arena.manager.ArenaManager;
import com.au5tie.minecraft.tobnet.game.arena.manager.ArenaManagerType;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ArenaSpawnLocationManager extends ArenaManager {

    private final List<ArenaSpawnLocation> locations;

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

    /**
     * Adds {@link ArenaSpawnLocation}.
     * @param location Spawn Location.
     * @author au5tie.
     */
    public void addLocation(ArenaSpawnLocation location) {
        this.locations.add(location);
    }

    /**
     * Removes {@link ArenaSpawnLocation}.
     * @param location Spawn Location.
     * @author au5tie.
     */
    public void removeLocation(ArenaSpawnLocation location) {
        this.locations.remove(location);
    }

    /**
     * Returns all {@link ArenaSpawnLocation} that are of the provided {@link ArenaSpawnLocationType}.
     * @param type Spawn Location Type.
     * @return Spawn Locations of type.
     * @author au5tie.
     */
    public List<ArenaSpawnLocation> getLocationsOfType(ArenaSpawnLocationType type) {
        return locations.stream()
                .filter(location -> location.getType().equals(type))
                .collect(Collectors.toList());
    }
}