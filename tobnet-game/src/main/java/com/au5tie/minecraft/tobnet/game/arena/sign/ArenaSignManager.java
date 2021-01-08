package com.au5tie.minecraft.tobnet.game.arena.sign;

import com.au5tie.minecraft.tobnet.game.arena.TobnetArena;
import com.au5tie.minecraft.tobnet.game.arena.manager.ArenaManager;
import com.au5tie.minecraft.tobnet.game.arena.manager.ArenaManagerType;

import java.util.ArrayList;
import java.util.List;

public class ArenaSignManager extends ArenaManager {

    private final List<ArenaSign> signs;

    public ArenaSignManager(TobnetArena arena) {
        super(arena);

        signs = new ArrayList<>();
    }

    @Override
    public ArenaManagerType getType() {
        return ArenaManagerType.SIGN;
    }

    @Override
    public void prepareManager() {
        //
    }

    @Override
    public void destroyManager() {

    }
}
