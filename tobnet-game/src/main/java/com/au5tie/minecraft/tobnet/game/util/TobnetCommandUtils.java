package com.au5tie.minecraft.tobnet.game.util;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TobnetCommandUtils {

    /**
     * Determines if the {@link CommandSender} is a player.
     * @param sender Command Sender.
     * @return If the command sender is a player.
     * @author au5tie
     */
    public static boolean isSenderAPlayer(CommandSender sender) {

        return sender instanceof Player;
    }
}
