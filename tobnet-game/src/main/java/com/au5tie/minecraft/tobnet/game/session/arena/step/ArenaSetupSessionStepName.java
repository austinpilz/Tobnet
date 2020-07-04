package com.au5tie.minecraft.tobnet.game.session.arena.step;

import com.au5tie.minecraft.tobnet.core.session.SetupSession;
import com.au5tie.minecraft.tobnet.core.session.SetupSessionChatUtils;
import com.au5tie.minecraft.tobnet.core.session.SetupSessionStep;
import com.au5tie.minecraft.tobnet.core.session.SetupSessionStepInvocationContext;
import com.au5tie.minecraft.tobnet.game.TobnetGamePlugin;
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
                SetupSessionChatUtils.generateColoredChatSegment("/idkyet setup [arenaName]", ChatColor.GREEN, ChatColor.WHITE, true));
    }

    @Override
    protected boolean invoke(SetupSessionStepInvocationContext context) {

        // Extract the arena name from the command params.

        // Make sure the name isn't already in use.

        // Save the name onto this step object.

        String providedArenaName = "temporaryName";

        if (TobnetGamePlugin.getArenaController().getArenaByName(providedArenaName).isPresent()) {
            // An Arena with the provided name already exists.
            context.getPlayer().sendMessage("An arena with the name " + providedArenaName + " already exists. Please choose another name.");

            return false;
        }

        // Store the provided arena name in the step.
        arenaName = providedArenaName;

        // Return that the step completed successfully.
        return true;
    }

    /**
     * Returns the Arena Name stored in the step.
     * @return Arena Name.
     * @author au5tie
     */
    public String getArenaName() {

        return arenaName;
    }
}
