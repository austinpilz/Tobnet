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
 * @author au5tie
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
        //
    }

    @Override
    public void destroyManager() {

        locations.clear();
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

    /**
     * Loads the location from storage into manager memory. This specifically bypasses saving the location to storage,
     * so it should only be used when performing loads.
     *
     * @param location Arena Location.
     * @author au5tie
     */
    public void loadLocation(ArenaLocation location) {

        locations.add(location);
    }

    public void registerLocation(ArenaLocation location) {
        //TODO When creating new one, will need to call to store it.
    }

    public void removeLocation(ArenaLocation location) {
        // TODO When removing a location from arena, also need to delete from storage DB.
    }
}
