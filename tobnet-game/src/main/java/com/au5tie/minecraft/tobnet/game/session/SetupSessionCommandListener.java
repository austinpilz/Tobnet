package com.au5tie.minecraft.tobnet.game.session;

import com.au5tie.minecraft.tobnet.game.command.listener.CommandListener;
import com.au5tie.minecraft.tobnet.game.util.TobnetCommandUtils;
import lombok.AllArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

@AllArgsConstructor
public class SetupSessionCommandListener extends CommandListener {

    private final SetupSessionController controller;

    @Override
    protected void registerCommands() {
        registerCommand("tobsetup");
    }

    @Override
    public boolean onCommandExecuted(CommandSender sender, Command command, String label, String[] args) {

        // Validate that setup type of the command matches the type that the controller manages.
        if (args.length > 0 && args[0].equalsIgnoreCase(controller.getSessionType())) {

            // Validate that the sender is a player.
            if (!TobnetCommandUtils.isSenderAPlayer(sender)) {
                // The sender is likely the console, sessions can only be done by players.
                handleNonPlayerSender(sender);
                return false;
            }

            // Everything looks good, handle the command.
            handleCommand(buildContext(sender, command, label, args));

            // Return true to indicate that we serviced the command.
            return true;
        }

        // Return false to indicate that this listener did not service the command.
        return false;
    }

    /**
     * Handles the setup session command invocation.
     *
     * @param context Setup Session Step Invocation Context.
     * @author au5tie
     */
    private void handleCommand(SetupSessionStepInvocationContext context) {

        if (context.getCommandArguments().size() == 1) {
            // Start Session command.
            handleStartSetup(context);
        } else if (context.getCommandArguments().size() > 1) {

            if (context.getCommandArguments().get(1).equalsIgnoreCase("end")) {
                // User is requesting we terminate the session.
                handleEndSetup(context);
            } else {
                // User is invoking a sessions step.
                controller.sessionStepInvoke(context.getPlayer().getUniqueId().toString(), context);
            }
        }
    }

    /**
     * Handles the request to start a new {@link SetupSession}. If the user already has an existing setup session, this
     * will terminate the existing session before beginning the new one as only one session can be in progress per player
     * at the same time.
     *
     * @param context Setup Session Step Invocation Context.
     * @author au5tie
     */
    private void handleStartSetup(SetupSessionStepInvocationContext context) {

        // See if they already have a session in progress
        if (controller.doesUserHaveExistingSession(context.getPlayer().getUniqueId().toString())) {
            // The user has an existing session, terminate it.
            controller.requestSessionTermination(context.getPlayer().getUniqueId().toString());
        }

        // Request the new session.
        controller.requestNewSession(context.getPlayer());
    }

    /**
     * Handles the request to end the user's current {@link SetupSession}. This will request the termination of the ongoing
     * setup session, if one exists.
     *
     * @param context Setup Session Step Invocation Context.
     * @author au5tie
     */
    private void handleEndSetup(SetupSessionStepInvocationContext context) {

        if (controller.doesUserHaveExistingSession(context.getPlayer().getUniqueId().toString())) {
            // The user has an existing session, terminate it.
            controller.requestSessionTermination(context.getPlayer().getUniqueId().toString());
        }
    }

    /**
     * Handles the case of the console sending setup session commands. This will send the console back a message
     * letting them know that only players can interact with setup sessions.
     *
     * @param sender Command Sender.
     * @author au5tie
     */
    private void handleNonPlayerSender(CommandSender sender) {

        sender.sendMessage("[Tobnet] Setup sessions can only be invoked by a player, not the console.");
    }

    /**
     * Builds the {@link SetupSessionStepInvocationContext} for the executed command.
     *
     * @param sender Command Sender.
     * @param command Command.
     * @param label Command Label.
     * @param args Arguments.
     * @return Setup Session Step Inovcation Context.
     * @author au5tie
     */
    private SetupSessionStepInvocationContext buildContext(CommandSender sender, Command command, String label, String[] args) {

        return SetupSessionStepInvocationContext.builder()
                .player((Player)sender)
                .command(command.getName())
                .commandArguments(Arrays.asList(args))
                .build();
    }
}
