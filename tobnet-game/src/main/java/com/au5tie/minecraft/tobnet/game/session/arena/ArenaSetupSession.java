package com.au5tie.minecraft.tobnet.game.session.arena;

import com.au5tie.minecraft.tobnet.core.session.SetupSession;
import com.au5tie.minecraft.tobnet.core.session.SetupSessionStep;
import com.au5tie.minecraft.tobnet.game.session.arena.step.ArenaSetupSessionStepBoundaryOne;
import lombok.Getter;
import org.bukkit.entity.Player;

@Getter
public class ArenaSetupSession extends SetupSession {

    public ArenaSetupSession(Player player) {

        super(player);
    }

    /**
     * Configures the {@link SetupSessionStep}s to be invoked during this session.
     * @author au5tie
     */
    protected void configureSteps() {

        // Boundary One.
        ArenaSetupSessionStepBoundaryOne boundaryOneStep = new ArenaSetupSessionStepBoundaryOne(1);
        registerStep(boundaryOneStep);

        // Boundary Two.
        ArenaSetupSessionStepBoundaryOne boundaryTwoStep = new ArenaSetupSessionStepBoundaryOne(2);
        registerStep(boundaryTwoStep);

    }
}
