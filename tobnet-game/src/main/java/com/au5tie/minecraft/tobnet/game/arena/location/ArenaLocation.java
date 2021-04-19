package com.au5tie.minecraft.tobnet.game.arena.location;

import com.au5tie.minecraft.tobnet.game.arena.TobnetArena;
import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Location;
import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Data
public class ArenaLocation {

    private TobnetArena arena;
    private String name;
    private String type;
    private Location location;

    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private final Map<String, String> metadata;

    public ArenaLocation() {

        this(UUID.randomUUID().toString());
    }

    public ArenaLocation(String name) {

        this.name = name;

        this.metadata = new HashMap<>();
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

    public void addMetadata(String key, String value) {

        metadata.put(key, value);
    }

    public void removeMetadata(String key) {

        metadata.remove(key);
    }
}
