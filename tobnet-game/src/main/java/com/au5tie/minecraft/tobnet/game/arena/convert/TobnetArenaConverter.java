package com.au5tie.minecraft.tobnet.game.arena.convert;

import com.au5tie.minecraft.tobnet.game.arena.TobnetArena;

import java.util.HashMap;
import java.util.Map;

public abstract class TobnetArenaConverter {

    private final TobnetArena arena;
    private final Map<String, String> properties;

    public TobnetArenaConverter(TobnetArena arena) {
        this.arena = arena;
        properties = new HashMap<>();
    }

    public abstract void convertFromProperties();

    /**
     *
     * 1 - Base Arena is stored in a row with colunms
     *
     * 2 - Implementing arena converts into key-value properties
     *
     * 3 - When writing arena, converter puts key values into JSON which go in database column.
     *
     *
     */

    public Map<String, String> convertToProperties() {

        return properties;
    }
}
