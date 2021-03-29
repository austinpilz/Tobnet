package com.au5tie.minecraft.tobnet.game.arena.chat.handler;

import com.au5tie.minecraft.tobnet.game.arena.chat.ArenaChatManager;
import com.au5tie.minecraft.tobnet.game.util.TobnetPlayerUtils;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * The Isolation Chat Handler performs basic chat isolation of players messages from inside and outside of the arena. When
 * in use, the chat from in game players is visible to other players of the arena, but not visible to players in other arenas
 * or not within the game at all. Likewise, players within the arena cannot see chat message sent by players outside of
 * the arena.
 *
 * @author au5tie
 */
public class IsolationChatHandler extends TobnetChatHandler {

    public IsolationChatHandler(ArenaChatManager chatManager) {
        super(chatManager);
    }

    @Override
    void performMessageHandling(AsyncPlayerChatEvent event) {

        if (getChatManager().getPlayerManager().isPlaying(event.getPlayer().getUniqueId().toString())) {
            // The player sending the message is playing in the arena.
            // The only recipients should be the players currently in the arena.
            event.getRecipients().clear();
            event.getRecipients().addAll(TobnetPlayerUtils.gamePlayerToBukkitPlayerList(getChatManager().getPlayerManager().getPlayers()));
        } else {
            // The player sending the message is outside the arena / not playing, our players should not be able to see it.
            // Remove all of this arena's players from the recipients of the message.
            event.getRecipients().removeAll(TobnetPlayerUtils.gamePlayerToBukkitPlayerList(getChatManager().getPlayerManager().getPlayers()));
        }
    }
}
