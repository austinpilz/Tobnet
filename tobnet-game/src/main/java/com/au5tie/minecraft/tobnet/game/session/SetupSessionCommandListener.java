package com.au5tie.minecraft.tobnet.game.session;

import com.au5tie.minecraft.tobnet.game.TobnetGamePlugin;
import com.au5tie.minecraft.tobnet.game.command.listener.CommandListener;
import com.au5tie.minecraft.tobnet.game.message.MessageConstants;
import com.au5tie.minecraft.tobnet.game.util.TobnetCommandUtils;
import lombok.AllArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 *
 * Command Line Structure: /[topLevelCommand] setup [sessionType] [args...]
 *                                            [0]   [1]           [n]
 *
 * /[topLevelCommand] setup end
 */
@AllArgsConstructor
public class SetupSessionCommandListener extends CommandListener {

    private final TobnetSetupSessionController controller;

    @Override
    protected void registerCommands() {
        //TODO Top Level Command? Inheir from implementing plugin IDK
        registerCommand("tobsetup");
    }

    @Override
    public boolean onCommandExecuted(CommandSender sender, Command command, String label, String[] args) {

        // Validate that setup type of the command matches the type that the controller manages.
        if (args.length > 0 && TobnetGamePlugin.getMessageController().getMessage(MessageConstants.SETUP_SESSION_COMMAND).equalsIgnoreCase(args[0])) {

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

        if (context.getCommandArguments().size() >= 2) {

            if (TobnetGamePlugin.getMessageController().getMessage(MessageConstants.SETUP_SESSION_END_COMMAND).equalsIgnoreCase(context.getCommandArguments().get(1))) {
                // Session end requested.
                handleEndSetup(context);
            } else {
                // The session type was provided.
                if (controller.doesUserHaveExistingSession(context.getPlayer().getUniqueId().toString())) {
                    // Player has existing session, pass the context into the step invocation.
                    controller.sessionStepInvoke(context.getPlayer().getUniqueId().toString(), context);
                } else {
                    // Player does not already have a session, begin a new one.
                    handleStartSetup(context);
                }
            }
        } else {
            // Session type was not provided.
            context.getPlayer().sendMessage(TobnetGamePlugin.getMessageController().getMessage(MessageConstants.SETUP_SESSION_TYPE_COMMAND_MISSING_ERROR));
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
        controller.requestNewSession( context);
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

        sender.sendMessage(TobnetGamePlugin.getMessageController().getMessage(MessageConstants.SETUP_SESSION_NON_PLAYER_INVOKE_ERROR));
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

        SetupSessionStepInvocationContext context = SetupSessionStepInvocationContext
                .builder()
                .player((Player)sender)
                .command(command.getName())
                .commandArguments(Arrays.asList(args))
                .build();

        if (args.length > 1) {
            context.setSessionType(args[1]);
        }

        return context;
    }
}
