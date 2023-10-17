package com.au5tie.sandfall.arena.setup;

import com.au5tie.minecraft.tobnet.game.TobnetGamePlugin;
import com.au5tie.minecraft.tobnet.game.arena.setup.step.ArenaSetupSessionStepBoundaryOne;
import com.au5tie.minecraft.tobnet.game.arena.setup.step.ArenaSetupSessionStepBoundaryTwo;
import com.au5tie.minecraft.tobnet.game.arena.setup.step.ArenaSetupSessionStepName;
import com.au5tie.minecraft.tobnet.game.session.SetupSession;
import com.au5tie.minecraft.tobnet.game.session.SetupSessionChatUtils;
import com.au5tie.minecraft.tobnet.game.session.SetupSessionStep;
import com.au5tie.minecraft.tobnet.game.session.SetupSessionStepInvocationContext;
import com.au5tie.minecraft.tobnet.game.util.TobnetChatUtils;
import com.au5tie.sandfall.arena.SandFallArena;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

/**
 * The SandFall Arena Setup Session is the specific {@link SetupSession} which allows us to have the user create new
 * {@link com.au5tie.sandfall.arena.SandFallArena}s via the command line.
 *
 * The session itself is organized into two sections below:
 * - Step Configuration is where we'll provide the session with all the steps we want the player to go through.
 * - Session Completion is what happens when the session completes.
 *
 * @author au5tie
 */
@Getter
public class SandFallArenaSetupSession extends SetupSession {

    public SandFallArenaSetupSession(Player player) {

        super(player);
    }

    /**
     * Configures the {@link SetupSessionStep}s to be invoked during this session. This is where we'll tell the session
     * which steps we want the user to go through and in what order.
     *
     * The Tobnet engine provides us with a library of out of the box steps which we can quickly and easily leverage for
     * our session - we use a few of those below. We can also make our own step to meet the unique needs of our arena.
     *
     * In this example, we have the user going through 3 steps:
     * - Select the name for the arena.
     * - Define the cuboid boundary 1.
     * - Define the cuboid boundary 2.
     * - Provide a custom message to be repeated to us.
     *
     * @author au5tie
     */
    @Override
    protected void configureSteps() {

        // Arena Name (Tobnet Provided Step).
        ArenaSetupSessionStepName nameStep = new ArenaSetupSessionStepName(0, this);
        registerStep(nameStep);

        // Boundary One (Tobnet Provided Step).
        ArenaSetupSessionStepBoundaryOne boundaryOneStep = new ArenaSetupSessionStepBoundaryOne(1, this);
        registerStep(boundaryOneStep);

        // Boundary Two (Tobnet Provided Step).
        ArenaSetupSessionStepBoundaryTwo boundaryTwoStep = new ArenaSetupSessionStepBoundaryTwo(2, this);
        registerStep(boundaryTwoStep);

        // Custom Message (Custom Step).
        CustomSetupSessionStepExample customStep = new CustomSetupSessionStepExample(3, this);
        registerStep(customStep);
    }

    @Override
    protected void onSessionComplete(SetupSessionStepInvocationContext context) {

        // Link back to all the steps that were completed during the session.
        ArenaSetupSessionStepName nameStep = (ArenaSetupSessionStepName)getStepByName("arena-name").get();
        ArenaSetupSessionStepBoundaryOne boundaryOneStep = (ArenaSetupSessionStepBoundaryOne)getStepByName("boundary-one").get();
        ArenaSetupSessionStepBoundaryTwo boundaryTwoStep = (ArenaSetupSessionStepBoundaryTwo)getStepByName("boundary-two").get();

        // Let's connect back to our custom step and grab the message we stored in it.
        CustomSetupSessionStepExample customStep = (CustomSetupSessionStepExample)getStepByName("custom-example-name").get();

        // Repeat the word the player provided back to them to prove our nifty step was able to collect & store it.
        TobnetChatUtils.sendPlayerMessage(context.getPlayer(), "The custom message you entered was " +
                SetupSessionChatUtils.generateColoredChatSegment(customStep.getCustomMessage(), ChatColor.DARK_PURPLE, ChatColor.WHITE, true));

        // Create the arena.
        SandFallArena arena = new SandFallArena(nameStep.getArenaName());

        // Configure the boundaries.
        arena.setBoundaryOne(boundaryOneStep.getBoundaryLocation());
        arena.setBoundaryTwo(boundaryTwoStep.getBoundaryLocation());

        // Register the arena with the controller.
        TobnetGamePlugin.getArenaController().registerArena(arena);

        // TODO Store the arena in the database.

        // Notify the user of the setup success.
        TobnetChatUtils.sendPlayerMessage(context.getPlayer(), "Setup of arena " + arena.getName() + " completed successfully.");
    }
}
