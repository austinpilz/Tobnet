package com.au5tie.minecraft.tobnet.game.arena.setup.step;

import com.au5tie.minecraft.tobnet.game.TobnetGamePlugin;
import com.au5tie.minecraft.tobnet.game.chat.TobnetChatUtils;
import com.au5tie.minecraft.tobnet.game.session.SetupSession;
import com.au5tie.minecraft.tobnet.game.session.SetupSessionChatUtils;
import com.au5tie.minecraft.tobnet.game.session.SetupSessionStep;
import com.au5tie.minecraft.tobnet.game.session.SetupSessionStepInvocationContext;
import org.bukkit.ChatColor;

public class ArenaSetupSessionStepName extends SetupSessionStep {

    private String arenaName;

    public ArenaSetupSessionStepName(int order, SetupSession session) {

        super("arena-name", "Arena Name", order, session);
    }

    @Override
    protected void displayPromptBody(SetupSessionStepInvocationContext context) {
        // Send the instructions on how to configure the name.
        context.getPlayer().sendMessage("To configure the name of the arena, execute the command " +
                SetupSessionChatUtils.generateColoredChatSegment("/" + TobnetGamePlugin.getSetupSessionController().getCommandListener().getFirstSupportedCommand().orElse("UNKNOWN")
                        + " setup arena [arenaName]", ChatColor.GREEN, ChatColor.WHITE, true));
    }

    @Override
    protected boolean invoke(SetupSessionStepInvocationContext context) {

        if (context.getCommandArguments().size() < 2) {
            // The arena name was not provided in the right place in the command arguments.
            TobnetChatUtils.sendPlayerMessage(context.getPlayer(), "The arena name was not provided in the command arguments.");
            return false;
        }

        // Extract the arena name from the arguments.
        String providedArenaName = context.getCommandArguments().get(1);

        if (TobnetGamePlugin.getArenaController().getArenaByName(providedArenaName).isPresent()) {
            // An Arena with the provided name already exists.
            TobnetChatUtils.sendPlayerMessage(context.getPlayer(), "An arena with the name " + providedArenaName + " already exists. Please choose another name.");

            return false;
        }

        // Store the provided arena name in the step.
        arenaName = providedArenaName;

        // Return that the step completed successfully.
        return true;
    }

    /**
     * Returns the Arena Name stored in the step.
     *
     * @return Arena Name.
     * @author au5tie
     */
    public String getArenaName() {

        return arenaName;
    }
}
