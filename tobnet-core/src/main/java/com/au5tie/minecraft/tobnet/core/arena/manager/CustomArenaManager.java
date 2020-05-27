package com.au5tie.minecraft.tobnet.core.arena.manager;

import com.au5tie.minecraft.tobnet.core.arena.Arena;

public abstract class CustomArenaManager extends ArenaManager {

    private final String customType;

    public CustomArenaManager(String type, Arena arena) {

        super(arena);

        this.customType = type;
    }

    @Override
    public final ArenaManagerType getType() {

        return ArenaManagerType.CUSTOM;
    }

    /**
     * Returns the custom type for the manager.
     * @return Arena Manager Custom Type.
     * @author au5tie
     */
    public final String getCustomType() {

        return customType;
    }
}
