package com.au5tie.minecraft.tobnet.game.command.listener;

import com.au5tie.minecraft.tobnet.game.exception.TobnetEngineException;
import com.google.common.collect.ImmutableSet;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.HashSet;
import java.util.Set;

/**
 * CommandListener is the way for intra-Tobnet components to subscribe to and act on commands being executed. The
 * CommandListener has to be extended by an implementing listener which customizes the behavior of the command execution.
 * The supportedCommands list are the top level commands which the listener is subscribed to within Tobnet internal
 * command routing. When the CommandListener is created, it will call the registerCommands() method which allows/requires
 * the implementing class to specify the commands it's interested in listening on. With the supported commands being a
 * list, a single CommandListener can listen to N number of top level commands.
 *
 * @author au5tie
 */
public abstract class CommandListener {

    private final Set<String> supportedCommands;

    public CommandListener() {
        supportedCommands = new HashSet<>();

        // Register all the commands.
        registerCommands();
    }

    /**
     * Retrieves all the top level commands that the listener is interested in subscribing to.
     *
     * @return Subscribed commands.
     * @author au5tie
     */
    public Set<String> getSupportedCommands() {
        return ImmutableSet.copyOf(supportedCommands);
    }

    /**
     * Determines if the provided top level command is subscribed to by the listener.
     *
     * @param command
     * @return If this listener subscribes to the command.
     * @author au5tie
     */
    public boolean isSupportedCommand(String command) {
        return supportedCommands.contains(command.toLowerCase());
    }

    /**
     * Registers a command which the listener is interested in subscribing to.
     *
     * Note: This registers the command within the listener itself which will have the command routed from the controller
     * here, but it does not register it with the server. See the sync method within {@link com.au5tie.minecraft.tobnet.game.command.TobnetCommandController}.
     *
     * @param command Top Level Command.
     * @author au5tie
     */
    protected void registerCommand(String command) {

        if (StringUtils.isBlank(command)){
            throw new TobnetEngineException(this.getClass().getSimpleName() + " attempted to register an empty top level command which is not supported.");
        }

        supportedCommands.add(command.toLowerCase());
    }

    /**
     * Disassociated a command that the listener was previously interested in listening to.
     *
     * @param command Top Level Command.
     * @author au5tie
     */
    protected void removeCommand(String command) {
        supportedCommands.remove(command.toLowerCase());
    }

    /**
     * Registers the commands which the implementing listener wants to subscribe to.
     *
     * @author au5tie
     */
    protected abstract void registerCommands();

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
