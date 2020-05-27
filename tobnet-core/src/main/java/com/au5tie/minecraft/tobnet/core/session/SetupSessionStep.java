package com.au5tie.minecraft.tobnet.core.session;

import lombok.Getter;
import org.bukkit.entity.Player;

/**
 * This class represents a distinct step which will can be registered to be invoked during a setup session. The step is
 * designed to action as an abstract setup step which the implementing plugin will extend and tailor to the needs of their
 * setup sessions.
 *
 * The order of the step dictates the order in which the session will invoke them.
 *
 * @author au5tie
 */
@Getter
public abstract class SetupSessionStep {

    private final String name;
    private final int order;
    private boolean complete;

    public SetupSessionStep(String name, int order) {

        this.name = name;
        this.order = order;
        this.complete = false;
    }

    /**
     * Invokes the step at the request/action of the provided {@link Player}.
     *
     * This is the point where the actual processing of the step needs to be implemented by each extending step.
     *
     * @param player Player.
     * @author au5tie
     */
    protected abstract void invoke(Player player);

    /**
     * Invokes the step processing. This handles preparation of the step, invocation of the implementing step processing,
     * and auto completing the step (if enabled) post processing.
     * @param player Player.
     * @author au5tie
     */
    public final void invokeStep(Player player) {

        // Invoke the implementing step.
        invoke(player);

        if (!disableAutoCompletion()) {
            // Auto-complete the step so the session will advance to the next step.
            completeStep();
        }
    }

    /**
     * Dictates if the step should not be auto-completed once the step has been invoked. By default, once the step has
     * been invoked, it will automatically be set to complete so the session will advance to the next step.
     *
     * This is sometimes done when a step validates the type of a location or object and the validation fails, thus
     * requiring the step to be invoked again during the next user action.
     *
     * @return If step auto completion after invocation should be disabled.
     * @author au5tie
     */
    protected boolean disableAutoCompletion() {

        return false;
    }

    /**
     * Marks the step as having been completed.
     * @author au5tie
     */
    protected final void completeStep() {

        this.complete = true;
    }
}
