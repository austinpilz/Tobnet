package com.au5tie.minecraft.tobnet.core.session;

import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

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
    public final void registerStep(SetupSessionStep step) {

        steps.add(step);
    }

    /**
     * Un-registers an existing {@link SetupSessionStep} from the list of steps for this session.
     * @param step Setup Session Step.
     * @author au5tie
     */
    public final void removeStep(SetupSessionStep step) {

        steps.remove(step);
    }

    /**
     * Prepares the session for invocation. This will configure the session steps and other applicable values required
     * to begin the user's interaction with the seesion.
     * @author au5tie
     */
    public final void prepareSession() {

        // Configure the steps to be invoked.
        configureSteps();
    }
}
