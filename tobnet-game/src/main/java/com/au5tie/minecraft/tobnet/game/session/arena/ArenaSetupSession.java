package com.au5tie.minecraft.tobnet.game.session.arena;

import com.au5tie.minecraft.tobnet.core.arena.Arena;
import com.au5tie.minecraft.tobnet.core.session.SetupSession;
import com.au5tie.minecraft.tobnet.core.session.SetupSessionStep;
import com.au5tie.minecraft.tobnet.core.session.SetupSessionStepInvocationContext;
import com.au5tie.minecraft.tobnet.game.TobnetGamePlugin;
import com.au5tie.minecraft.tobnet.game.session.arena.step.ArenaSetupSessionStepBoundaryOne;
import com.au5tie.minecraft.tobnet.game.session.arena.step.ArenaSetupSessionStepName;
import lombok.Getter;
import org.bukkit.entity.Player;

@Getter
public class ArenaSetupSession extends SetupSession {

    public ArenaSetupSession(Player player) {

        super(player);
    }

    /**
     * Configures the {@link SetupSessionStep}s to be invoked during this session.
     *
     * This will configure all of the basic Arena Setup
     *
     * @author au5tie
     */
    @Override
    protected void configureSteps() {

        // Arena Name.
        ArenaSetupSessionStepName nameStep = new ArenaSetupSessionStepName(0, this);
        registerStep(nameStep);

        // Boundary One.
        ArenaSetupSessionStepBoundaryOne boundaryOneStep = new ArenaSetupSessionStepBoundaryOne(1, this);
        registerStep(boundaryOneStep);

        // Boundary Two.
        ArenaSetupSessionStepBoundaryOne boundaryTwoStep = new ArenaSetupSessionStepBoundaryOne(2, this);
        //registerStep(boundaryTwoStep);

    }

        @Override
    protected void completeSession(SetupSessionStepInvocationContext context) {

        ArenaSetupSessionStepName nameStep = (ArenaSetupSessionStepName)getStepByName("arena-name").get();

        Arena arena = new Arena(nameStep.getArenaName());

        TobnetGamePlugin.getArenaController().addArena(arena);

        if (context.hasPlayer()) {
            context.getPlayer().sendMessage("Setup session completed successfully.");
        }
    }
}
