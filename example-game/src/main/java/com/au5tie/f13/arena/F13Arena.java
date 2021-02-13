package com.au5tie.f13.arena;

import com.au5tie.minecraft.tobnet.game.arena.TobnetArena;
import com.au5tie.minecraft.tobnet.game.arena.chat.ArenaChatManager;
import com.au5tie.minecraft.tobnet.game.arena.chest.ArenaChestManager;
import com.au5tie.minecraft.tobnet.game.arena.countdown.ArenaCountdownConfiguration;
import com.au5tie.minecraft.tobnet.game.arena.countdown.ArenaCountdownManager;
import com.au5tie.minecraft.tobnet.game.arena.countdown.start.ArenaCountdownSecondsProvider;
import com.au5tie.minecraft.tobnet.game.arena.countdown.start.ArenaCountdownSecondsStaticProvider;
import com.au5tie.minecraft.tobnet.game.arena.game.ArenaGameManager;
import com.au5tie.minecraft.tobnet.game.arena.location.ArenaSpawnLocationManager;
import com.au5tie.minecraft.tobnet.game.arena.player.ArenaPlayerDisplayManager;
import com.au5tie.minecraft.tobnet.game.arena.player.ArenaPlayerManager;
import com.au5tie.minecraft.tobnet.game.arena.sign.ArenaSignManager;
import com.au5tie.minecraft.tobnet.game.arena.wait.ArenaWaitingRoomManager;

public class F13Arena extends TobnetArena {

    public F13Arena(String name) {
        super(name);
    }

    protected void prepareManagers() {
        // Chat Manager.
        prepareChatManager();

        // Chest Manager.
        prepareArenaChestManager();

        // Countdown Manager.
        prepareCountdownManager();

        // Game Manager.
        prepareArenaGameManager();

        // Player Manager.
        preparePlayerManager();

        // Sign Manager.
        prepareSignManager();

        // Spawn Location Manager.
        prepareSpawnLocationManager();

        // Waiting Room.
        prepareWaitingRoomManager();
    }

    private void prepareChatManager() {

        ArenaChatManager chatManager = new ArenaChatManager(this);
        registerManager(chatManager);
    }

    private void prepareArenaChestManager() {

        ArenaChestManager chestManager = new ArenaChestManager(this);
        registerManager(chestManager);
    }

    private void prepareCountdownManager() {

        ArenaCountdownConfiguration configuration = ArenaCountdownConfiguration.builder()
                .displayChatIntervals(true)
                .build();

        ArenaCountdownSecondsProvider provider = new ArenaCountdownSecondsStaticProvider(this, 15);

        ArenaCountdownManager countdownManager = new ArenaCountdownManager(this, configuration, provider);
        registerManager(countdownManager);
    }

    private void prepareArenaGameManager() {

        ArenaGameManager gameManager = new ArenaGameManager(this);
        registerManager(gameManager);
    }

    private void preparePlayerManager() {

        ArenaPlayerManager playerManager = new ArenaPlayerManager(this);
        registerManager(playerManager);

        ArenaPlayerDisplayManager playerDisplayManager = new ArenaPlayerDisplayManager(this);
        registerManager(playerDisplayManager);
    }

    private void prepareSignManager() {

        ArenaSignManager signManager = new ArenaSignManager(this);
        registerManager(signManager);
    }

    private void prepareSpawnLocationManager() {

        ArenaSpawnLocationManager spawnLocationManager = new ArenaSpawnLocationManager(this);
        registerManager(spawnLocationManager);
    }

    private void prepareWaitingRoomManager () {

        // TODO Need to pass in waiting room config

        ArenaWaitingRoomManager waitingRoomManager = new ArenaWaitingRoomManager(this, null);
        registerManager(waitingRoomManager);
    }
}
