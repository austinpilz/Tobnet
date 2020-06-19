package com.au5tie.minecraft.tobnet.core.session;

import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Getter
public abstract class SetupSession {

    private final String playerUuid;
    private final Player player;
    private final List<SetupSessionStep> steps;

    public SetupSession(Player player) {

        this.player = player;
        this.playerUuid = player.getUniqueId().toString();

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
     * Called when the session is being terminated by the controller. This is designed to allow the session to perform any
     * cleanup operations before being de-registered from the controller and becoming unreachable.
     * @author au5tie
     */
    protected void onSessionTerminate() {
        //
    }

    public void invokeStep(SetupSessionStepInvocationContext context) {

        // Find the next step which is to be invoked.
        Optional<SetupSessionStep> step = determineNextInvocableStep();

        if (step.isPresent()) {
            // Invoke the step by passing in the context.
            step.get().invokeStep(context);


        } else {
            // There is no next invocable step.
            //TODO What happens when the session is completed?
        }



        // if it's been completed and auto continue is enable, recursivly invoke the next


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
}
