package com.au5tie.minecraft.tobnet.game.arena.chat;

import com.au5tie.minecraft.tobnet.game.TobnetGamePlugin;
import com.au5tie.minecraft.tobnet.game.arena.TobnetArena;
import com.au5tie.minecraft.tobnet.game.arena.chat.handler.OpenChatHandler;
import com.au5tie.minecraft.tobnet.game.arena.chat.handler.TobnetChatHandler;
import com.au5tie.minecraft.tobnet.game.arena.game.ArenaGameManager;
import com.au5tie.minecraft.tobnet.game.arena.game.ArenaGameStatus;
import com.au5tie.minecraft.tobnet.game.arena.manager.ArenaManager;
import com.au5tie.minecraft.tobnet.game.arena.manager.ArenaManagerType;
import com.au5tie.minecraft.tobnet.game.arena.manager.ArenaManagerUtils;
import com.au5tie.minecraft.tobnet.game.arena.player.ArenaPlayerManager;
import com.au5tie.minecraft.tobnet.game.exception.TobnetEngineException;
import com.au5tie.minecraft.tobnet.game.util.TobnetLogUtils;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 *
 *
 * @author au5tie
 */
@Getter
public class ArenaChatManager extends ArenaManager {

    private final Map<ArenaGameStatus, TobnetChatHandler> handlerMap = new HashMap<>();

    private ArenaGameManager gameManager;
    private ArenaPlayerManager playerManager;

    public ArenaChatManager(TobnetArena arena) {

        super(arena);

        registerEventHandler(new ArenaChatEventHandler(getArena(), this));

        // Initialize the handler map to open for everything.
        Arrays.asList(ArenaGameStatus.values()).forEach(status -> registerChatHandler(status, new OpenChatHandler(this)));
    }

    @Override
    public ArenaManagerType getType() {
        return ArenaManagerType.CHAT;
    }

    @Override
    public void prepareManager() {

    }

    @Override
    public void destroyManager() {

    }

    @Override
    public void afterArenaPreparationComplete() {
        // Link to the player manager.
        playerManager = (ArenaPlayerManager) ArenaManagerUtils.getManagerOfType(getArena(), ArenaManagerType.PLAYER).orElseThrow(TobnetEngineException::new);

        gameManager = (ArenaGameManager) ArenaManagerUtils.getManagerOfType(getArena(), ArenaManagerType.GAME).orElseThrow(TobnetEngineException::new);
    }

    /**
     * Registers the {@link TobnetChatHandler} to be used when the arena is in the provided {@link ArenaGameStatus}.
     *
     * @param gameStatus Arena Game Status.
     * @param chatHandler Tobnet Chat Handler.
     * @author au5tie
     */
    public void registerChatHandler(ArenaGameStatus gameStatus, TobnetChatHandler chatHandler) {

        handlerMap.put(gameStatus, chatHandler);
    }

    public void sendMessageToAllPlayers(String message) {

        String newMessage = ChatColor.RED + TobnetGamePlugin.chatPrefix + ChatColor.RESET + message;

        ArenaPlayerManager playerManager = ArenaManagerUtils.getPlayerManager(getArena()).get();

        playerManager.getPlayers()
                .forEach(player -> player.getPlayer().sendMessage(newMessage));
    }


    /**
     * Handles when a player to player message is initiated. This will route the message to the configured chat handler
     * for specific game mode handling.
     *
     * @param event Player chat event.
     * @author au5tie
     */
    void handlePlayerToPlayerMessage(AsyncPlayerChatEvent event) {
        // Determine which chat handler we'll use for this message based on the current arena status.
        TobnetChatHandler chatHandler = getHandlerForCurrentArenaStatus();

        // Hand the event off to the handler to manage.
        chatHandler.handlePlayerToPlayerMessage(event);
    }

    /**
     * Obtains the {@link TobnetChatHandler} to be used for the current status within the arena. If there is no handler
     * provided via configuration for the current game mode, this will default back to the most basic chat handler and
     * will log the warning to the console.
     *
     * @return Tobnet Chat Handler to be used for the current game mode.
     * @author au5tie
     */
    private TobnetChatHandler getHandlerForCurrentArenaStatus() {
        // Obtain the specific handler being used for the current status of the arena.
        TobnetChatHandler chatHandler = handlerMap.get(gameManager.getGameStatus());

        if (chatHandler == null) {
            // There was no chat handler provided for this game mode, so we'll provide a basic one.
            chatHandler = new OpenChatHandler(this);

            // Store the new handler so we're not doing this every time a message is sent in this mode.
            handlerMap.put(gameManager.getGameStatus(), chatHandler);

            TobnetLogUtils.warn("No chat handler provided for arena " + getArena().getName() + " in status " + gameManager.getGameStatus());
        }

        return chatHandler;
    }
}
