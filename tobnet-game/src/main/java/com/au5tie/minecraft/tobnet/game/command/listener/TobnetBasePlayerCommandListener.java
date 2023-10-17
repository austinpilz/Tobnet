package com.au5tie.minecraft.tobnet.game.command.listener;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class TobnetBasePlayerCommandListener extends CommandListener {
    @Override
    public void registerCommands() {

    }

    // tobnet arenas
    // tobnet arena $name (show status, high level details)
    // tobnet arena $name locations

    @Override
    public boolean onCommandExecuted(CommandSender sender, Command command, String label, String[] args) {
        return false;
    }
}
