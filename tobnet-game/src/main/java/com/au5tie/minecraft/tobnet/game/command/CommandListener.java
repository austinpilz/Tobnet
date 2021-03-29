package com.au5tie.minecraft.tobnet.game.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

/**
 * CommandListener is the way for intra-Tobnet components to subscribe to and act on commands being executed. The
 * CommandListener has to be extended by an implementing listener which customizes the behavior of the command execution.
 * The supportedCommands list are the top level commands which the listener is subscribed to within Tobnets internal
 * command routing. When the CommandListener is created, it will call the registerCommands() method which allows/requires
 * the implementing class to specify the commands it's interested in listening on. With the supported commands being a
 * list, a single CommandListener can listen to N number of top level commands.
 *
 * @author au5tie
 */
public abstract class CommandListener {

    // The commands which this listener wishes to subscribe to.
    private final List<String> supportedCommands;

    public CommandListener() {

        supportedCommands = new ArrayList<>();

        // Register all of the commands.
        registerCommands();
    }

    /**
     * Retrieves all of the top level commands that the listener is interested in subscribing to.
     * @return Subscribed commands.
     * @author au5tie
     */
    public List<String> getSupportedCommands() {

        return new ArrayList<>(supportedCommands);
    }

    /**
     * Registers a command which the listener is interested in subscribing to.
     * @param command Top Level Command.
     * @author au5tie
     */
    protected void registerCommand(String command) {

        supportedCommands.add(command);
    }

    /**
     * Registers the commands which the implementing listener wants to subscribe to. All top level commands which the listener
     * wishes to subscribe to must be populated when this is called.
     * @author au5tie
     */
    protected abstract void registerCommands();

    /**
     * Triggered when a command is executed which the listener is subscribed to.
     * @param sender Command Sender.
     * @param command Command.
     * @param label Label.
     * @param args Arguments.
     * @return If the listener serviced this command successfully. Failures result in processing termination.
     * @author au5tie
     */
    public abstract boolean onCommandExecuted(CommandSender sender, Command command, String label, String[] args);
}
