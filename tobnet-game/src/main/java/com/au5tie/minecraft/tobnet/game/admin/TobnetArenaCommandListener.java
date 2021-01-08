package com.au5tie.minecraft.tobnet.game.admin;

import com.au5tie.minecraft.tobnet.game.TobnetGamePlugin;
import com.au5tie.minecraft.tobnet.game.arena.TobnetArena;
import com.au5tie.minecraft.tobnet.game.command.CommandListener;
import com.au5tie.minecraft.tobnet.game.util.TobnetChatUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TobnetArenaCommandListener extends CommandListener {

    @Override
    protected void registerCommands() {

        getSupportedCommands().add("tobadmin");
    }

    @Override
    public boolean onCommandExecuted(CommandSender sender, Command command, String label, String[] args) {

        // Validate that setup type of the command matches the type that the controller manages.
        if (args.length > 0) {

            if (args[0].equalsIgnoreCase("arenas")) {
                handleArenaList(sender, args);
            }

            // Return true to indicate that we serviced the command.
            return true;
        }

        // Return false to indicate that this listener did not service the command.
        return false;
    }

    private void handleArenaList(CommandSender sender, String[] args) {

        if (TobnetGamePlugin.getArenaController().getArenas().size() > 0) {

            int arenaIndex = 0;

            for (TobnetArena arena : TobnetGamePlugin.getArenaController().getArenas()) {
                TobnetChatUtils.sendPlayerMessage((Player) sender,"" + arenaIndex++ + ") " + arena.getName());
            }

        } else {
            // There are no registered arenas.
            TobnetChatUtils.sendPlayerMessage((Player) sender, "There are no registered arenas.");
        }
    }
}
