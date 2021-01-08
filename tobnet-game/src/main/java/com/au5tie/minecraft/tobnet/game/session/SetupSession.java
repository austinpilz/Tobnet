package com.au5tie.minecraft.tobnet.game.session;

import com.au5tie.minecraft.tobnet.game.util.TobnetLogUtils;
import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * The Setup Session represents a unique user session for the setup of some object. The session itself manages all of the
 * required steps, user information, and the processing of the outcome. The Setup Session has to be extended by each
 * implementing Setup Session type.
 * @author au5tie
 */
@Getter
public abstract class SetupSession {

    private final String playerUuid;
    private final Player player;
    private final List<SetupSessionStep> steps;
    private boolean complete;

    public SetupSession(Player player) {

        this.player = player;
        this.playerUuid = player.getUniqueId().toString();
        this.complete = false;

        // Steps.
        this.steps = new ArrayList<>();

        // Prepare the session.
        prepareSession();
    }

    /**
     * Configures the {@link SetupSessionStep}s to be invoked during this session.
     *
     * This is the responsibility of each implementing Setup Session to provide configuration/definition for all of the
     * steps in that session.
     *
     * @author au5tie
     */
    protected abstract void configureSteps();

    /**
     * Registers a new {@link SetupSessionStep} in the list of steps for this session.
     * @param step Setup Session Step.
     * @author au5tie
     */
    protected final void registerStep(SetupSessionStep step) {

        steps.add(step);
    }

    /**
     * Un-registers an existing {@link SetupSessionStep} from the list of steps for this session.
     * @param step Setup Session Step.
     * @author au5tie
     */
    protected final void removeStep(SetupSessionStep step) {

        steps.remove(step);
    }

    /**
     * Prepares the session for invocation. This will configure the session steps and other applicable values required
     * to begin the user's interaction with the session.
     * @author au5tie
     */
    protected final void prepareSession() {

        // Configure the steps to be invoked.
        configureSteps();
    }

    /**
     * Called when the session is beginning for the first time. This will begin the session by displaying the prompt
     * message of the first step to be invoked to the user.
     * @param context Invocation Context.
     * @author au5tie
     */
    protected final void onSessionBegin(SetupSessionStepInvocationContext context) {
        // Find the first step which is to be invoked.
        Optional<SetupSessionStep> step = determineNextInvocableStep();

        if (step.isPresent()) {
            // Prompt the first step.
            step.get().displayPrompt(context);
        }
    }


    /**
     * Called when all of the steps in the session have been completed successfully. This is the responsibility to each
     * implementing Setup Session to provide the implementation for what should happen when the session completes.
     * @author au5tie
     */
    protected void onSessionComplete(SetupSessionStepInvocationContext context) {
        //
    }

    /**
     * Called when the session is being terminated by the controller. This is designed to allow the session to perform any
     * cleanup operations before being de-registered from the controller and becoming unreachable.
     * @author au5tie
     */
    protected void onSessionTerminate() {
        //
    }

    /**
     * This will invoke the next invocable step. This will determine which step is next to be invoked and will notify
     * the step of the invocation request.
     * @param context Invocation Context.
     * @author au5tie
     */
    public void invokeStep(SetupSessionStepInvocationContext context) {

        // Find the next step which is to be invoked.
        Optional<SetupSessionStep> step = determineNextInvocableStep();

        if (step.isPresent()) {
            // Invoke the step by passing in the context.
            step.get().invokeStep(context);

            if (step.get().isComplete() && !step.get().disableAutoContinue()) {
                // The step has been completed, let's auto continue to prompt the next step.
                Optional<SetupSessionStep> nextStep = determineNextInvocableStep();

                if (nextStep.isPresent()) {
                    // Auto display the prompt for the next step.
                    nextStep.get().displayPrompt(context);
                } else {
                    // There is no next step, meaning the session is complete.
                    completeSession(context);
                }
            }
        } else {
            // There is no next invocable step.
            TobnetLogUtils.warn("There is no next invocable step for player " + getPlayerUuid());
        }
    }

    /**
     * Completes the session. This will notify the implementing class that the session is being completed and will then
     * mark the session as being completed.
     * @param context Invocation Context.
     * @author au5tie
     */
    private final void completeSession(SetupSessionStepInvocationContext context) {
        // Notify the implementing class that we're finishing up the session.
        onSessionComplete(context);

        // Mark the session as having been complete.
        this.complete = true;
    }

    /**
     * Determines the next {@link SetupSessionStep} to be invoked. This will determine the next step in order that
     * has not yet been completed.
     * @return Setup Session Step.
     * @author au5tie
     */
    private Optional<SetupSessionStep> determineNextInvocableStep() {

        if (CollectionUtils.isNotEmpty(steps)) {
            // Filter the steps to find the next invocable step in order which has not yet been completed.
            return steps.stream()
                    .filter(step -> !step.isComplete()) // Find only steps which have not yet been completed.
                    .sorted(Comparator.comparingInt(SetupSessionStep::getOrder)) // Order them by order ASC.
                    .findFirst();
        } else {
            // There are no registered session steps.
            return Optional.empty();
        }
    }

    /**
     * Obtains an {@link SetupSessionStep} by the provided internal name.
     * @param name Name.
     * @return Setup Session Step.
     * @author au5tie
     */
    protected final Optional<SetupSessionStep> getStepByName(String name) {

        if (CollectionUtils.isNotEmpty(steps)) {
            return steps.stream()
                    .filter(step -> step.getName().equalsIgnoreCase(name))
                    .findFirst();
        } else {
            // There are no registered session steps.
            return Optional.empty();
        }
    }
}
