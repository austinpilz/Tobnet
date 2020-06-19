package com.au5tie.minecraft.tobnet.core.session;

import org.bukkit.ChatColor;

public class SetupSessionChatUtils {

    public static String generateColoredChatSegment(String content, ChatColor contentColor, ChatColor returnColor, boolean bold) {

        StringBuilder chatString = new StringBuilder();

        // Configure the color to be used for the content.
        chatString.append(contentColor);

        if (bold) {
            // Bold just the command itself.
            chatString.append(" ").append(ChatColor.BOLD);
        }

        // Append the content itself.
        chatString.append(content);

        // Return the color back to the return value.
        chatString.append("").append(ChatColor.RESET).append("").append(returnColor);

        return chatString.toString();
    }

}
