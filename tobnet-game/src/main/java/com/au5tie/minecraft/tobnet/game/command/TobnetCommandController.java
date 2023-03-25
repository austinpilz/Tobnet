package com.au5tie.minecraft.tobnet.game.command;

import com.au5tie.minecraft.tobnet.game.TobnetGamePlugin;
import com.au5tie.minecraft.tobnet.game.command.listener.CommandListener;
import com.au5tie.minecraft.tobnet.game.controller.TobnetController;
import com.au5tie.minecraft.tobnet.game.exception.TobnetEngineException;
import com.google.common.collect.ImmutableSet;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;

import java.util.HashSet;
import java.util.Set;

/**
 * The Tobnet Command Controller acts as the interface layer between the server API (Spigot) and the implementing game code.
 * CommandListeners will be registered with the Command Controller which will handle the routing of command events to
 * interested listeners.
 *
 * @author au5tie
 */
public final class TobnetCommandController implements CommandExecutor, TobnetController {

    private final Set<CommandListener> commandListeners = new HashSet<>();

    @Override
    public void prepare() {
        //
    }

    /**
     * Registers the {@link CommandListener}'s supported commands. This will register each of the interested commands
     * to the listener itself.
     *
     * @param listener Command Listener.
     * @author au5tie
     */
    public void registerCommandListener(CommandListener listener) {
        commandListeners.add(listener);

        synchronizeCommandListener(listener);
    }

    /**
     * Synchronizes the {@link CommandListener} with the server implementation by registering all top level supported
     * commands as being serviced by this controller. This is what ensures
     *
     * @param listener Command Listener.
     * @author au5tie
     */
    public void synchronizeCommandListener(CommandListener listener) {

        for (String command : listener.getSupportedCommands()) {

            PluginCommand pluginCommand = TobnetGamePlugin.getInstance().getCommand(command);

            if (pluginCommand == null) {

                throw new TobnetEngineException("Command listener " + listener.getClass().getSimpleName() + " attempted to register command " + command + " but it is not registered at the server level.");
            } else {
                pluginCommand.setExecutor(this);
            }
        }
    }

    /**
     * Determines of the provided command is registered to one or more {@link CommandListener}s.
     *
     * @param command Command.
     * @return If the command is registered to one or more Command Listeners.
     * @author au5tie
     */
    public boolean isRegisteredCommand(String command) {

        return !getSubscribedListeners(command).isEmpty();
    }

    /**
     * Returns all the subscribed {@link CommandListener}s for the provided top level command.
     *
     * @param command Top level command.
     * @return All subscribed Command Listeners, if any.
     * @author au5tie
     */
    private Set<CommandListener> getSubscribedListeners(String command) {
        return commandListeners.stream().filter(listener -> listener.isSupportedCommand(command.toLowerCase())).collect(ImmutableSet.toImmutableSet());
    }

    /**
     * Performs command execution handling when a command is invoked by a user/console. This will find all the linked
     * {@link CommandListener}s for the command and allow each one to handle the command invocation.
     *
     * If any listeners respond with false, meaning the handling encountered an error, the command handling will stop and
     * any subsequent listeners will not be invoked.
     *
     * @param sender Command sender.
     * @param command Command.
     * @param label Command Label.
     * @param args Arguments.
     * @return If the command handling was successful.
     * @author au5tie
     */
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (isRegisteredCommand(command.getName().toLowerCase())) {
            // Obtain the list of interested listeners.
            Set<CommandListener> listeners = getSubscribedListeners(command.getName());

            for (CommandListener listener : listeners) {
                // Invoke the listener's command handling.
                if (!listener.onCommandExecuted(sender, command, label, args)) {
                    // This listener failed to execute the command successfully.
                    return false;
                }
            }
        }

        return true;
    }
}
