package com.au5tie.minecraft.tobnet.game.arena.location;

import com.au5tie.minecraft.tobnet.game.arena.TobnetArena;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.json.simple.JSONObject;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * The Arena Location acts as a generic location within an arena to be used for various purposes.
 *
 * @author au5tie
 */
@Data
public class ArenaLocation {

    private final TobnetArena arena;
    private final String name;
    private String type;
    private Location location;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private final Map<String, String> metadata;

    public ArenaLocation(TobnetArena arena, String name, String type, Location location) {

        this.arena = arena;
        this.name = name;
        this.type = type;
        this.location = location;
        this.metadata = new HashMap<>();
    }

    /**
     * Adds metadata element to the location.
     *
     * @param key Element name.
     * @param value Element value.
     * @author au5tie
     */
    public void addMetadataElement(String key, String value) {

        metadata.put(key, value);
    }

    /**
     * Removes metadata element from the location if it exists.
     *
     * @param key Element name to remove.
     * @author au5tie
     */
    public void removeMetadataElement(String key) {

        metadata.remove(key);
    }

    /**
     * Obtains metadata element by the provided name, if exists.
     *
     * @param key Element name.
     * @return Element value.
     * @author au5tie
     */
    public Optional<String> getMetadataElement(String key) {

        return Optional.ofNullable(metadata.get(key));
    }

    /**
     * Returns the location's metadata map.
     *
     * @return Location metadata.
     * @author au5tie
     */
    public Map<String, String> getMetadata() {

        return Collections.unmodifiableMap(metadata);
    }

    /**
     * Returns the location's metadata as a JSON object.
     *
     * @return Metadata in JSON format.
     * @author au5tie
     */
    public JSONObject getMetadataAsJson() {

        return new JSONObject(metadata);
    }
}
