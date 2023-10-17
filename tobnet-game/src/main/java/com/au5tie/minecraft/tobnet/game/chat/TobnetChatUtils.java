package com.au5tie.minecraft.tobnet.game.chat;

import com.au5tie.minecraft.tobnet.game.TobnetGamePlugin;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TobnetChatUtils {

    /**
     * Sends {@link Player} the provided chat message.
     *
     * @param player Player.
     * @param message Message.
     * @author au5tie
     */
    public static void sendPlayerMessage(Player player, String message) {

       sendPlayerMessage(player, message, true);
    }

    /**
     * Sends {@link Player} the provided chat message.
     *
     * @param player Player.
     * @param message Message.
     * @param prefix If the chat prefix should prepend the message.
     * @author au5tie
     */
    public static void sendPlayerMessage(Player player, String message, boolean prefix) {

        if (prefix) {
            player.sendMessage(TobnetGamePlugin.chatPrefix + message);
        } else {
            player.sendMessage(message);
        }
    }

    public static void sendCommandSenderMessage(CommandSender sender, String message) {

        sender.sendMessage(TobnetGamePlugin.chatPrefix + message);
    }
}
