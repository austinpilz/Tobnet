package com.au5tie.minecraft.tobnet.game.arena.chat;

import com.au5tie.minecraft.tobnet.game.TobnetGamePlugin;
import com.au5tie.minecraft.tobnet.game.arena.TobnetArena;
import com.au5tie.minecraft.tobnet.game.arena.manager.ArenaManager;
import com.au5tie.minecraft.tobnet.game.arena.manager.ArenaManagerType;
import com.au5tie.minecraft.tobnet.game.arena.manager.ArenaManagerUtils;
import com.au5tie.minecraft.tobnet.game.arena.player.ArenaPlayerManager;
import com.au5tie.minecraft.tobnet.game.exception.TobnetEngineException;
import org.bukkit.ChatColor;

/**
 * The Arena Chat Manager controls
 *
 * @author au5tie
 */
public class ArenaChatManager extends ArenaManager {

    private ArenaPlayerManager playerManager;

    public ArenaChatManager(TobnetArena arena) {
        super(arena);
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
    }


    public void sendMessageToAllPlayers(String message) {
        // Prepend the game's prefix to the message we'll send.
        String newMessage = ChatColor.RED + TobnetGamePlugin.chatPrefix + ChatColor.RESET + message;

        // Send the message to all current players.
        playerManager.getPlayers().stream()
                .filter(player -> player.getPlayer().isOnline()) // Ensure we don't try to send messages to offline players.
                .forEach(player -> player.getPlayer().sendMessage(newMessage));
    }
}
