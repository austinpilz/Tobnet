package com.au5tie.minecraft.tobnet.game.session;

import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.Arrays;

/**
 * This class represents a distinct step which will can be registered to be invoked during a setup session. The step is
 * designed to action as an abstract setup step which the implementing plugin will extend and tailor to the needs of their
 * setup sessions.
 *
 * The order of the step dictates the order in which the session itself will invoke them. If multiple steps have the same
 * order, there is no guarantee which of the duplicate orders will be invoked first. Each step should have a unique order.
 *
 * @author au5tie
 */
@Getter
public abstract class SetupSessionStep {

    private final String name;
    private final int order;
    private boolean complete;
    private SetupSession setupSession;

    private String displayName;
    private ChatColor promptBorderColor;

    /**
     * @param name The internal step name.
     * @param displayName The display name of the step to be used in chat.
     * @param order Step order.
     * @author au5tie
     */
    public SetupSessionStep(String name, String displayName, int order, SetupSession setupSession) {

        this.name = name;
        this.displayName = displayName;
        this.order = order;
        this.complete = false;
        this.setupSession = setupSession;

        // Default the border color. This can be changed by the implementing step ez.
        promptBorderColor = ChatColor.AQUA;
    }

    /**
     * Invokes the step at the request/action of the provided {@link Player}.
     *
     * This is the point where the actual processing of the step needs to be implemented by each extending step.
     *
     * @param context Invocation Context.
     * @return  If the step invocation was completed successfully.
     * @author au5tie
     */
    protected abstract boolean invoke(SetupSessionStepInvocationContext context);

    /**
     * Displays the prompt of the step to the user. This should be utilized to provide the user with instruction on how
     * to invoke the step. The prompt and the invocation are separate operations.
     *
     * Note: This will only be invoked by the prompt displayed if there is a player associated with the context. If there
     * is no player with the entire prompt is being generated, the body will not attempt to be displayed either.
     *
     * @param context Invocation Context.
     * @author au5tie
     */
    protected abstract void displayPromptBody(SetupSessionStepInvocationContext context);

    /**
     * Sets the {@link ChatColor} to be used for the prompt's border (header & footer).
     *
     * @param color Color.
     * @author au5tie
     */
    public void setPromptBorderColor(ChatColor color) {

        this.promptBorderColor = color;
    }

    /**
     * Displays the initial step prompt to the user. This will take care of the custom border and header for the step.
     * This delegates the context of the prompt to the implementing step via the displayPromptBody() method.
     *
     * @param context Invocation Context.
     * @author au5tie
     */
    public final void displayPrompt(SetupSessionStepInvocationContext context) {

        if (context.hasPlayer()) {
            // ----------- [StepName] | 0/n -----------
            String header = getPromptBorderColor() + "----------------- " + getDisplayName() + " | " + getOrder() + " / " + getSetupSession().getSteps().size() + " -----------------";
            context.getPlayer().sendMessage(header);

            // Allow the implementing step to display the body to the user.
            displayPromptBody(context);

            // ----------------------------------------
            StringBuilder footer = new StringBuilder();
            footer.append(getPromptBorderColor());

            // Make the footer dashes go as far out as the total length of the header, since it can vary depending on step name and such.
            Arrays.stream(header.split("")).forEach(character -> footer.append("-"));
            context.getPlayer().sendMessage(footer.toString());
        }
    }

    /**
     * Invokes the step processing. This handles preparation of the step, invocation of the implementing step processing,
     * and auto completing the step (if enabled) post processing.
     *
     * This will evaluate the invocation success. If the step was successfully invoked and responds as having completed
     * successfully, this will mark the step as having been completed. We don't want to automatically mark the step
     * as having been complete as validation errors & such can result in the step not being successful. We want to keep
     * it marked as incomplete so during the next user invocation they'll be routed back to the step.
     *
     * @param context Invocation Context.
     * @author au5tie
     */
    public final void invokeStep(SetupSessionStepInvocationContext context) {

        // Invoke the implementing step.
        boolean invocationSuccess = invoke(context);

        if (invocationSuccess) {
            // The step invocation was successful, mark the step as having been complete.
            completeStep();
        }
    }

    /**
     * Dictates if the step should not be auto-continue once the step has been invoked. By default, once the step has
     * been invoked, it will automatically be set to complete so the session will advance to the next step.
     *
     * This is sometimes done when a step validates the type of a location or object and the validation fails, thus
     * requiring the step to be invoked again during the next user action.
     *
     * @return If step auto continue after completion should be disabled.
     * @author au5tie
     */
    protected boolean disableAutoContinue() {

        return false;
    }

    /**
     * Marks the step as having been completed.
     *
     * @author au5tie
     */
    protected final void completeStep() {

        this.complete = true;
    }
}
