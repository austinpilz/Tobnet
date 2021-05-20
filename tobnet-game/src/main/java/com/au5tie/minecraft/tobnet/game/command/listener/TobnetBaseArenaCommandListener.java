package com.au5tie.minecraft.tobnet.game.command.listener;

import com.au5tie.minecraft.tobnet.game.TobnetGamePlugin;
import com.au5tie.minecraft.tobnet.game.arena.TobnetArena;
import com.au5tie.minecraft.tobnet.game.arena.game.ArenaGameManager;
import com.au5tie.minecraft.tobnet.game.arena.location.ArenaLocationManager;
import com.au5tie.minecraft.tobnet.game.arena.manager.ArenaManagerType;
import com.au5tie.minecraft.tobnet.game.arena.manager.ArenaManagerUtils;
import com.au5tie.minecraft.tobnet.game.exception.TobnetEngineMissingManagerException;
import com.au5tie.minecraft.tobnet.game.message.MessageConstants;
import com.au5tie.minecraft.tobnet.game.util.TobnetChatUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Optional;

/**
 * This is the engine base Arena Command Listener. It provides engine level information and actions for every registered
 * {@link TobnetArena} and its respective components.
 *
 * @author au5tie
 */
public class TobnetBaseArenaCommandListener extends CommandListener {

    @Override
    protected void registerCommands() {

        registerCommand("tobnet");
    }

    @Override
    public boolean onCommandExecuted(CommandSender sender, Command command, String label, String[] args) {

        // Validate that setup type of the command matches the type that the controller manages.
        if (args.length > 0) {

            if (args.length == 1 && args[0].equalsIgnoreCase("arenas")) {
                // Display list of arenas.
                displayArenaList(sender, args);
                return true;
            } else if (args.length == 2 && args[0].equalsIgnoreCase("arenas")) {
                // Display specific arena general information.
                displayArenaInformation(sender, args[1]);
                return true;
            }
        }

        // Return false to indicate that this listener did not service the command.
        return false;
    }

    /**
     * Generates and displays a list of arenas to the sender.
     *
     * @param sender Command Sender.
     * @param args Arguments.
     * @author au5tie
     */
    private void displayArenaList(CommandSender sender, String[] args) {

        if (TobnetGamePlugin.getArenaController().getArenas().size() > 0) {

            // Header.
            TobnetChatUtils.sendCommandSenderMessage(sender,
                    TobnetGamePlugin.getMessageController().getMessage(MessageConstants.ARENA_CONSOLE_HEADER));

            // Per-Arena Info Line.
            TobnetGamePlugin
                    .getArenaController()
                    .getArenas()
                    .forEach(arena ->
                            TobnetChatUtils.sendCommandSenderMessage(sender,
                                    "- " + generateArenaDisplayLine(arena)));
        } else {
            // There are no registered arenas.
            TobnetChatUtils.sendCommandSenderMessage(sender,
                    TobnetGamePlugin.getMessageController().getMessage(MessageConstants.ARENA_NONE_REGISTERED));
        }
    }

    /**
     * Generates the {@link TobnetArena}'s display line for when the arena appears in a general console list.
     *
     * @param arena Arena.
     * @return Arena console display line.
     * @author au5tie
     */
    private String generateArenaDisplayLine(TobnetArena arena) {

        ArenaGameManager gameManager = (ArenaGameManager) ArenaManagerUtils.getManagerOfType(arena, ArenaManagerType.GAME).orElseThrow(TobnetEngineMissingManagerException::new);

        return arena.getName() + " (" + gameManager.getGameStatus().name() + ")";
    }

    private void displayArenaInformation(CommandSender sender, String arenaName) {

        Optional<TobnetArena> arena = TobnetGamePlugin.getArenaController().getArenaByName(arenaName);

        if (arena.isEmpty()) {
            // The provided arena does not exist.
            TobnetChatUtils.sendCommandSenderMessage(sender,
                    TobnetGamePlugin.getMessageController().getMessage(MessageConstants.ARENA_NO_EXIST));
        }

        // Link managers.
        ArenaGameManager gameManager = (ArenaGameManager) ArenaManagerUtils.getManagerOfType(arena.get(), ArenaManagerType.GAME).orElseThrow(TobnetEngineMissingManagerException::new);
        ArenaLocationManager locationManager = (ArenaLocationManager) ArenaManagerUtils.getManagerOfType(arena.get(), ArenaManagerType.LOCATION).orElseThrow(TobnetEngineMissingManagerException::new);

        TobnetChatUtils.sendCommandSenderMessage(sender, "-- " + arenaName + " ---");
        TobnetChatUtils.sendCommandSenderMessage(sender, "Status: " + gameManager.getGameStatus());
        TobnetChatUtils.sendCommandSenderMessage(sender, "Locations: " + locationManager.getNumberLocations());

        // -- Arena Name --
        // Status: X
        // Locations:

        // TODO Display
    }
}
