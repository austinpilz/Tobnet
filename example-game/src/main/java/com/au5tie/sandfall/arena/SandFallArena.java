package com.au5tie.sandfall.arena;

import com.au5tie.minecraft.tobnet.game.arena.TobnetArena;
import com.au5tie.minecraft.tobnet.game.arena.chest.ArenaChestManager;
import com.au5tie.minecraft.tobnet.game.arena.sign.ArenaSignManager;
import com.au5tie.minecraft.tobnet.game.arena.wait.ArenaWaitingRoomManager;

public class SandFallArena extends TobnetArena {

    public SandFallArena(String name) {
        super(name);
    }

    @Override
    protected void prepareAdditionalManagers() {
        // Chest Manager.
        prepareArenaChestManager();

        // Sign Manager.
        prepareSignManager();

        // Waiting Room.
        prepareWaitingRoomManager();
    }

    private void prepareArenaChestManager() {

        ArenaChestManager chestManager = new ArenaChestManager(this);
        registerManager(chestManager);
    }

    private void prepareSignManager() {

        ArenaSignManager signManager = new ArenaSignManager(this);
        registerManager(signManager);
    }

    private void prepareWaitingRoomManager () {

        // TODO Need to pass in waiting room config

        ArenaWaitingRoomManager waitingRoomManager = new ArenaWaitingRoomManager(this, null);
        registerManager(waitingRoomManager);
    }
}
