package com.au5tie.sandfall.arena.setup;

import com.au5tie.minecraft.tobnet.game.session.SetupSession;
import com.au5tie.minecraft.tobnet.game.session.SetupSessionChatUtils;
import com.au5tie.minecraft.tobnet.game.session.SetupSessionStep;
import com.au5tie.minecraft.tobnet.game.session.SetupSessionStepInvocationContext;
import org.bukkit.ChatColor;

public class CustomSetupSessionStepExample extends SetupSessionStep {

    private String ourCustomMessage;

    public CustomSetupSessionStepExample(int order, SetupSession session) {

        super("custom-example-name", "Custom Step", order, session);
    }

    @Override
    protected boolean invoke(SetupSessionStepInvocationContext context) {
        //TODO
        return false;
    }

    /**
     * Displays the step prompt to the player. This is where we will send a message to the player telling them what this
     * step is, what information we need, and what command to execute in order to store their response.
     *
     * In this example, we're asking the player to execute the setup command again with a new parameter that is their
     * message. We'll grab and store that information in the invoke() method later.
     *
     * @param context Invocation Context.
     * @author au5tie
     */
    @Override
    protected void displayPromptBody(SetupSessionStepInvocationContext context) {

        context.getPlayer().sendMessage("Configure a custom message for this example by executing the command " +
                SetupSessionChatUtils.generateColoredChatSegment("/sf setup arena [myMessage]", ChatColor.GREEN, ChatColor.WHITE, true));
    }

    /**
     * Returns the custom message that the player provided while in the step.
     *
     * @return Player's custom message.
     * @author au5tie
     */
    public String getCustomMessage() {

        return ourCustomMessage;
    }
}
