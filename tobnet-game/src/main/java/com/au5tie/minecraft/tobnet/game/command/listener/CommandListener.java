package com.au5tie.minecraft.tobnet.game.command.listener;

import com.au5tie.minecraft.tobnet.game.exception.TobnetEngineException;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

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
    }

    /**
     * Retrieves all the top level commands that the listener is interested in subscribing to.
     *
     * @return Subscribed commands.
     * @author au5tie
     */
    public List<String> getSupportedCommands() {

        return Collections.unmodifiableList(supportedCommands);
    }

    /**
     * Returns the first supported command by this listener.
     *
     * @return First supported command by this listener.
     * @author au5tie
     */
    public Optional<String> getFirstSupportedCommand() {

        if (supportedCommands.size() > 0) {
            return Optional.of(supportedCommands.get(0));
        } else {
            return Optional.empty();
        }
    }

    /**
     * Registers a command which the listener is interested in subscribing to.
     *
     * @param command Top Level Command.
     * @author au5tie
     */
    protected void registerCommand(String command) {

        if (StringUtils.isBlank(command)) {
            throw new TobnetEngineException(this.getClass().getSimpleName() + " attempted to register a null command, " +
                    "which is not supported.");
        }

        supportedCommands.add(command);
    }

    /**
     * Registers the commands which the implementing listener wants to subscribe to. All top level commands which the listener
     * wishes to subscribe to must be populated when this is called.
     *
     * This used to be called by the constructor, but this did not support dynamic or param-based command registration.
     * Instead, this is now called by the global command controller whenever a listener is registered with it.
     *
     * @author au5tie
     */
    public abstract void registerCommands();

    /**
     * Triggered when a command is executed which the listener is subscribed to.
     *
     * @param sender Command Sender.
     * @param command Command.
     * @param label Label.
     * @param args Arguments.
     * @return If the listener serviced this command successfully. Failures result in processing termination.
     * @author au5tie
     */
    public abstract boolean onCommandExecuted(CommandSender sender, Command command, String label, String[] args);
}
