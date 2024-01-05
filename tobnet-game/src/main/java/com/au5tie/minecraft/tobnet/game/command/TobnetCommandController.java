package com.au5tie.minecraft.tobnet.game.command;

import com.au5tie.minecraft.tobnet.game.TobnetGamePlugin;
import com.au5tie.minecraft.tobnet.game.command.listener.CommandListener;
import com.au5tie.minecraft.tobnet.game.controller.TobnetController;
import com.au5tie.minecraft.tobnet.game.exception.TobnetEngineException;
import com.au5tie.minecraft.tobnet.game.log.TobnetLogUtils;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

/**
 * The Tobnet Command Controller acts as the interface layer between the server API (Spigot) and the implementing game code.
 * CommandListeners will be registered with the Command Controller which will handle the routing of command events to
 * interested listeners.
 *
 * @author au5tie
 */
public final class TobnetCommandController
  implements CommandExecutor, TobnetController {

  private final Map<String, List<CommandListener>> commandToListenersMap =
    new HashMap<>();

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
    // Invoke the listener's method to register all of its configured commands within itself before we do anything
    // globally. This originally as done via constructor but didn't support param-based commands, so moved here.
    listener.registerCommands();

    if (CollectionUtils.isNotEmpty(listener.getSupportedCommands())) {
      // Register each of the listener's supported commands.
      listener
        .getSupportedCommands()
        .forEach(command -> registerCommand(listener, command));
    } else {
      TobnetLogUtils.warn(
        listener.getClass().getSimpleName() +
        " was registered with the command controller but had " +
        "no registered commands to register globally."
      );
    }
  }

  /**
   * Registers the command with the {@link CommandListener}. This will register the command with Bukkit so that the server
   * will route the command to this controller. This will also register the listener as interested in the command with
   * this controller which will ensure the command gets routed to that listener.
   *
   * @param listener Command Listener.
   * @param command Command.
   * @author au5tie
   */
  private void registerCommand(CommandListener listener, String command) {
    if (StringUtils.isBlank(command)) {
      // The command attempting to be registered is blank.
      throw new TobnetEngineException(
        listener.getClass().getSimpleName() +
        " listener attempted to register a null command."
      );
    }

    // Convert the command to lower case.
    command = command.toLowerCase();

    // Register the command with Bukkit to route to this controller.

    if (TobnetGamePlugin.getInstance().getCommand(command) == null) {
      throw new TobnetEngineException(
        listener.getClass().getSimpleName() +
        " listener attempted to register command " +
        command +
        ", but was not pre-registered with the server."
      );
    }

    // Tell the server to register this command controller as the listener for this command.
    TobnetGamePlugin.getInstance().getCommand(command).setExecutor(this);

    if (commandToListenersMap.containsKey(command)) {
      // The command is already registered, add this listener as an additionally interested party.
      commandToListenersMap.get(command).add(listener);
    } else {
      // This command has not yet been registered.
      List<CommandListener> listeners = new ArrayList<>();

      // Add this listener to the list of listeners linked to this command.
      listeners.add(listener);

      // Add this registered command to our map.
      commandToListenersMap.put(command, listeners);
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
    return commandToListenersMap.containsKey(command);
  }

  /**
   * Performs command execution handling when a command is invoked by a user/console. This will find all of the linked
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
  public boolean onCommand(
    CommandSender sender,
    Command command,
    String label,
    String[] args
  ) {
    if (isRegisteredCommand(command.getName().toLowerCase())) {
      // Obtain the list of interested listeners.
      List<CommandListener> listeners = commandToListenersMap.get(
        command.getName().toLowerCase()
      );

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
