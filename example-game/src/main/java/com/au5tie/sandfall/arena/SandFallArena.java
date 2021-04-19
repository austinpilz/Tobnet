package com.au5tie.sandfall.arena;

import com.au5tie.minecraft.tobnet.game.arena.TobnetArena;
import com.au5tie.minecraft.tobnet.game.arena.chat.ArenaChatConfiguration;
import com.au5tie.minecraft.tobnet.game.arena.chat.ArenaChatManager;
import com.au5tie.minecraft.tobnet.game.arena.chat.handler.IsolationChatHandler;
import com.au5tie.minecraft.tobnet.game.arena.chat.handler.OpenChatHandler;
import com.au5tie.minecraft.tobnet.game.arena.chat.handler.TobnetChatHandler;
import com.au5tie.minecraft.tobnet.game.arena.chest.ArenaChestManager;
import com.au5tie.minecraft.tobnet.game.arena.countdown.ArenaCountdownConfiguration;
import com.au5tie.minecraft.tobnet.game.arena.countdown.ArenaCountdownManager;
import com.au5tie.minecraft.tobnet.game.arena.countdown.start.ArenaCountdownSecondsProvider;
import com.au5tie.minecraft.tobnet.game.arena.countdown.start.ArenaCountdownSecondsStaticProvider;
import com.au5tie.minecraft.tobnet.game.arena.game.ArenaGameManager;
import com.au5tie.minecraft.tobnet.game.arena.game.ArenaGameStatus;
import com.au5tie.minecraft.tobnet.game.arena.location.ArenaLocationManager;
import com.au5tie.minecraft.tobnet.game.arena.player.ArenaPlayerDisplayManager;
import com.au5tie.minecraft.tobnet.game.arena.player.ArenaPlayerManager;
import com.au5tie.minecraft.tobnet.game.arena.sign.ArenaSignManager;
import com.au5tie.minecraft.tobnet.game.arena.spawn.ArenaSpawnLocationManager;
import com.au5tie.minecraft.tobnet.game.arena.wait.ArenaWaitingRoomManager;

import java.util.Arrays;

public class SandFallArena extends TobnetArena {

    public SandFallArena(String name) {
        super(name);
    }

    @Override
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
        prepareLocationManager();
        prepareSpawnLocationManager();

        // Waiting Room.
        prepareWaitingRoomManager();
    }

    /**
     * Prepares the chat manager for the game. This manager will control how chat is routed, displayed, etc. during various
     * phases of the game.
     *
     * In this example, we'll just use the engine provided chat manager. When players are waiting for the game to begin,
     * we'll use the open chat handler so they can interact with friends not in the lobby yet. Once the game begins, we'll
     * use chat isolation so only chat from within the arena is visible to players playing.
     *
     * @author au5tie
     */
    private void prepareChatManager() {

        ArenaChatConfiguration configuration = ArenaChatConfiguration.builder()
                .announcePlayerJoin(true)
                .announcePlayerLeave(true)
                .build();

        ArenaChatManager chatManager = new ArenaChatManager(this, configuration);

        // Register the chat handlers. This will use chat isolation for all games modes, restrictions apply.
        TobnetChatHandler isolationChatHandler = new IsolationChatHandler(chatManager);

        // By default we'll have the entire game use isolation so player chat inside and outside the arena is separated.
        Arrays.asList(ArenaGameStatus.values()).forEach(status -> chatManager.registerChatHandler(status, isolationChatHandler));

        // Except for in WAITING, where we will allow open so they can beg their friends to come play with them.
        chatManager.registerChatHandler(ArenaGameStatus.WAITING, new OpenChatHandler(chatManager));

        // Finally, register the manager with the arena so it will be used.
        registerManager(chatManager);
    }

    private void prepareArenaChestManager() {

        ArenaChestManager chestManager = new ArenaChestManager(this);
        registerManager(chestManager);
    }

    /**
     * Prepares the countdown manager for the game. In this example, we'll use the engine provided countdown manager and
     * pass in some basic configuration which allows us to customize the display of the countdown process.
     *
     * Here, we use the {@link ArenaCountdownSecondsStaticProvider} to have a static number of seconds in the countdown
     * phase until the game begins. We can use any of the {@link ArenaCountdownSecondsProvider} extending classes or
     * even create our own.
     *
     * @author au5tie
     */
    private void prepareCountdownManager() {
        // Create the basic configuration for how we'd like our countdown phase to behave.
        ArenaCountdownConfiguration configuration = ArenaCountdownConfiguration.builder()
                .displayChatIntervals(true)
                .displayCountdownUI(true)
                .build();

        // We need to provide a countdown provider to the manager so it knows how we want it count down when the time comes.
        ArenaCountdownSecondsProvider provider = new ArenaCountdownSecondsStaticProvider(this, 15);

        // Create our countdown manager and provide our configuration to it.
        ArenaCountdownManager countdownManager = new ArenaCountdownManager(this, configuration, provider);

        // Finally, register the manager with the arena so it will be used.
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

    private void prepareLocationManager() {

        ArenaLocationManager locationManager = new ArenaLocationManager(this);
        registerManager(locationManager);
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
