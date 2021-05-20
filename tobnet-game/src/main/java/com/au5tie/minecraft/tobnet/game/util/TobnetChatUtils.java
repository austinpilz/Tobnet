package com.au5tie.minecraft.tobnet.game.util;

import com.au5tie.minecraft.tobnet.game.TobnetGamePlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TobnetChatUtils {

    /**
     * Sends {@link Player} the provided chat message.
     * @param player Player.
     * @param message Message.
     * @author au5tie
     */
    public static void sendPlayerMessage(Player player, String message) {

        player.sendMessage(TobnetGamePlugin.chatPrefix + message);
    }

    public static void sendCommandSenderMessage(CommandSender sender, String message) {

        sender.sendMessage(TobnetGamePlugin.chatPrefix + message);
    }
}
