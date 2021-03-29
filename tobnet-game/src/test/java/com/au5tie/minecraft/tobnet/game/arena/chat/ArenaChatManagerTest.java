package com.au5tie.minecraft.tobnet.game.arena.chat;

import com.au5tie.minecraft.tobnet.game.arena.chat.handler.OpenChatHandler;
import com.au5tie.minecraft.tobnet.game.arena.chat.handler.TobnetChatHandler;
import com.au5tie.minecraft.tobnet.game.arena.game.ArenaGameManager;
import com.au5tie.minecraft.tobnet.game.arena.game.ArenaGameStatus;
import com.au5tie.minecraft.tobnet.game.arena.manager.ArenaManagerTest;
import com.au5tie.minecraft.tobnet.game.arena.manager.ArenaManagerType;
import com.au5tie.minecraft.tobnet.game.arena.player.ArenaPlayerManager;
import org.bukkit.entity.Player;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashSet;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class ArenaChatManagerTest extends ArenaManagerTest {

    private ArenaChatManager manager;

    @Mock
    private ArenaPlayerManager playerManager;

    @Mock
    private ArenaGameManager gameManager;

    @BeforeEach
    void setup() {

        ArenaChatConfiguration chatConfiguration = ArenaChatConfiguration.builder()
                .announcePlayerJoin(true)
                .announcePlayerLeave(true)
                .build();

        manager = new ArenaChatManager(getArena(), chatConfiguration);
    }

    @DisplayName("Chat Handler - Registered Game Mode")
    @Test
    void shouldTestGetHandlerForRegisteredGameMode() {

        // Register the arena managers directly used for this test.
        registerMockArenaManager(gameManager, ArenaManagerType.GAME);
        registerMockArenaManager(playerManager, ArenaManagerType.PLAYER);
        completeMockArenaManagerRegistration(manager);

        // Mock that the game status will always come back as empty.
        registerArenaGameStatusMock(gameManager, ArenaGameStatus.EMPTY);

        // Create a new chat handler which will be used for this game status.
        TobnetChatHandler chatHandler = new OpenChatHandler(manager);

        // Register chat provider for the game state.
        manager.registerChatHandler(ArenaGameStatus.EMPTY, chatHandler);

        // Verify that we're given the correct chat handler for the current game status.
        assertEquals(chatHandler, manager.getHandlerForCurrentArenaStatus());
    }

    @DisplayName("Chat Handler - Non-Registered Game Mode")
    @Test
    void shouldTestGetHandlerForNonRegisteredGameMode() {
        // Register the arena managers directly used for this test.
        registerMockArenaManager(gameManager, ArenaManagerType.GAME);
        registerMockArenaManager(playerManager, ArenaManagerType.PLAYER);
        completeMockArenaManagerRegistration(manager);

        // Mock that the game status will always come back as empty.
        registerArenaGameStatusMock(gameManager, ArenaGameStatus.EMPTY);

        // Register no chat provider for the game state.
        manager.registerChatHandler(ArenaGameStatus.EMPTY, null);

        // Verify that we're given some handler even though we provided a null. This tests the failover.
        assertNotNull(manager.getHandlerForCurrentArenaStatus());
    }

    @DisplayName("Chat Handler - Player to Player Messaging")
    @Test
    void shouldTestPlayerToPlayerMessageHandling() {

        // Register the arena managers directly used for this test.
        registerMockArenaManager(gameManager, ArenaManagerType.GAME);
        registerMockArenaManager(playerManager, ArenaManagerType.PLAYER);
        completeMockArenaManagerRegistration(manager);

        // Mock that the game status will always come back as empty.
        registerArenaGameStatusMock(gameManager, ArenaGameStatus.EMPTY);

        // Register a spy chat handler for the game status.
        manager.registerChatHandler(ArenaGameStatus.EMPTY, Mockito.spy(new OpenChatHandler(manager)));

        Player player = Mockito.spy(Player.class);

        // Create the player chat event.
        AsyncPlayerChatEvent event = new AsyncPlayerChatEvent(true, player, "Test message", new HashSet<>());

        // Call the event to be handled.
        manager.handlePlayerToPlayerMessage(event);

        // Verify that our logic has handed the message off to our desired handler to be processed.
        Mockito.verify(manager.getHandlerForCurrentArenaStatus(), Mockito.times(1)).handlePlayerToPlayerMessage(event);
    }
}