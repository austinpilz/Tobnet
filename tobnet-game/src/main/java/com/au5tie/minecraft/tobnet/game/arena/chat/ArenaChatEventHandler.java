package com.au5tie.minecraft.tobnet.game.arena.chat;

import com.au5tie.minecraft.tobnet.game.arena.TobnetArena;
import com.au5tie.minecraft.tobnet.game.arena.handler.ArenaEventHandler;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * The Arena Chat Event Handler is the enabler for the arena to control player to player communication during the game.
 * It allows us to respond to messages, intercept them, and manipulate them if desired. The engine does not control chat
 * at a high level, rather each arena manages the chat display to all of its players.
 *
 * @author au5tie
 */
public class ArenaChatEventHandler extends ArenaEventHandler {

    private final ArenaChatManager chatManager;

    public ArenaChatEventHandler(TobnetArena arena, ArenaChatManager manager) {
        super(arena, manager);

        chatManager = manager;
    }

    /**
     * This is the event which is fired whenever a player sends a message in chat. This is very specifically for when a
     * messages from a player themselves, not when we send a message from the plugin itself. This is how we are able to
     * control message visibility for chat isolation, proximity chat, etc.
     *
     * @param event Player chat event.
     * @author au5tie
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerMessage(AsyncPlayerChatEvent event) {
        // Route the event back into the chat manager
        chatManager.handlePlayerToPlayerMessage(event);
    }
}
