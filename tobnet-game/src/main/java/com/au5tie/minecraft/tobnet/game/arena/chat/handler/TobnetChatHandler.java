package com.au5tie.minecraft.tobnet.game.arena.chat.handler;

import com.au5tie.minecraft.tobnet.game.arena.chat.ArenaChatManager;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * A Tobnet Chat Handler is a specific implementation of a chat handler which controls the visibility and routing of player
 * messages sent on the server.
 *
 * @author au5tie
 */
@AllArgsConstructor
@Getter
public abstract class TobnetChatHandler {

    private final ArenaChatManager chatManager;

    /**
     * Performs the handler's message handling functionality.
     *
     * @param event Player chat event.
     * @author au5tie
     */
    abstract void performMessageHandling(AsyncPlayerChatEvent event);

    /**
     * Handles player to player message event.
     *
     * @param event Player chat event.
     * @author au5tie
     */
    public void handlePlayerToPlayerMessage(AsyncPlayerChatEvent event) {

        performMessageHandling(event);
    }
}
