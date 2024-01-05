package com.au5tie.sandfall.arena.setup;

import com.au5tie.minecraft.tobnet.game.chat.TobnetChatUtils;
import com.au5tie.minecraft.tobnet.game.session.SetupSession;
import com.au5tie.minecraft.tobnet.game.session.SetupSessionChatUtils;
import com.au5tie.minecraft.tobnet.game.session.SetupSessionStep;
import com.au5tie.minecraft.tobnet.game.session.SetupSessionStepInvocationContext;
import org.bukkit.ChatColor;

/**
 * This CustomSetupSessionStepExample demonstrates how you can create your own custom {@link SetupSessionStep} to fully
 * customize your setup session. This example setup is designed to prompt the user to provide a random name/string/etc. via
 * the CLI. Once the user enters the command line argument with the name, we'll extract it and store it within this step.
 * Once the overall setup session completes, the session will refer back to us in this step to grab the name to use for
 * creation of the arena.
 *
 * @author au5tie
 */
public class CustomSetupSessionStepExample extends SetupSessionStep {

  private String ourCustomMessage;

  public CustomSetupSessionStepExample(int order, SetupSession session) {
    super("custom-example-name", "Custom Step", order, session);
  }

  @Override
  protected boolean invoke(SetupSessionStepInvocationContext context) {
    if (context.getCommandArguments().size() >= 3) {
      // The user provided an argument with an example value, we can store that!
      ourCustomMessage = context.getCommandArguments().get(2);
      return true;
    } else {
      // The user did not provide enough arguments with the requested value.
      displayPromptBody(context);
      return false;
    }
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
    TobnetChatUtils.sendPlayerMessage(
      context.getPlayer(),
      "Configure a custom message for this example by executing the command " +
      SetupSessionChatUtils.generateColoredChatSegment(
        "/sf setup arena [myMessage]",
        ChatColor.GREEN,
        ChatColor.WHITE,
        true
      ),
      false
    );
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
