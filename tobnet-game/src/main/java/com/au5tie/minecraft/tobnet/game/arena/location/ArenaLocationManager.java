package com.au5tie.minecraft.tobnet.game.arena.location;

import com.au5tie.minecraft.tobnet.game.arena.TobnetArena;
import com.au5tie.minecraft.tobnet.game.arena.manager.ArenaManager;
import com.au5tie.minecraft.tobnet.game.arena.manager.ArenaManagerType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * The Arena Location Manager handles the storage of all storable locations within the arena. Although other managers will
 * manage the specific type of locations, this is the central manager for all game locations which are stored persistently.
 *
 * When the arena loads, this manager is populated with all stored locations
 *
 * @author autie
 */
public class ArenaLocationManager extends ArenaManager {

    private final List<ArenaLocation> locations;

    public ArenaLocationManager(TobnetArena arena) {
        super(arena);

        this.locations = new ArrayList<>();
    }

    @Override
    public ArenaManagerType getType() {
        return ArenaManagerType.LOCATION;
    }

    @Override
    public void prepareManager() {

    }

    @Override
    public void destroyManager() {

    }

    /**
     * Returns all of the registered arena locations.
     *
     * This will return an unmodifiable list to prevent modification outside of the manager.
     *
     * @return Registered arena locations.
     * @author au5tie
     */
    public final List<ArenaLocation> getLocations() {

        return Collections.unmodifiableList(locations);
    }

    public void loadLocation(ArenaLocation location) {
        //TODO Is when loading from DB.
    }

    public void registerLocation(ArenaLocation location) {
        //TODO When creating new one, will need to call to store it.
    }
}
