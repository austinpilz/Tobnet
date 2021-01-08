package com.au5tie.minecraft.tobnet.game.arena.chat;

import com.au5tie.minecraft.tobnet.game.TobnetGamePlugin;
import com.au5tie.minecraft.tobnet.game.arena.TobnetArena;
import com.au5tie.minecraft.tobnet.game.arena.manager.ArenaManager;
import com.au5tie.minecraft.tobnet.game.arena.manager.ArenaManagerType;
import com.au5tie.minecraft.tobnet.game.arena.manager.ArenaManagerUtils;
import com.au5tie.minecraft.tobnet.game.arena.player.ArenaPlayerManager;
import com.au5tie.minecraft.tobnet.game.player.GamePlayer;
import org.bukkit.ChatColor;

public class ArenaChatManager extends ArenaManager {

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

    public void sendMessageToAllPlayers(String message) {

        String newMessage = ChatColor.RED + TobnetGamePlugin.chatPrefix + ChatColor.RESET + message;

        ArenaPlayerManager playerManager = ArenaManagerUtils.getPlayerManager(getArena()).get();

        playerManager.getPlayers()
                .forEach(player -> player.getPlayer().sendMessage(newMessage));



        for (GamePlayer player : playerManager.getPlayers()) {
            //player.getPlayer().spigot().sendMessage(ChatMessageType.ACTION_BAR, new TextComponent(message));
        }

    }
}
