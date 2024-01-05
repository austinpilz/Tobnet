package com.au5tie.minecraft.tobnet.game.arena.setup;

import com.au5tie.minecraft.tobnet.game.TobnetGamePlugin;
import com.au5tie.minecraft.tobnet.game.arena.TobnetArena;
import com.au5tie.minecraft.tobnet.game.arena.setup.step.ArenaSetupSessionStepBoundaryOne;
import com.au5tie.minecraft.tobnet.game.arena.setup.step.ArenaSetupSessionStepBoundaryTwo;
import com.au5tie.minecraft.tobnet.game.arena.setup.step.ArenaSetupSessionStepName;
import com.au5tie.minecraft.tobnet.game.chat.TobnetChatUtils;
import com.au5tie.minecraft.tobnet.game.session.SetupSession;
import com.au5tie.minecraft.tobnet.game.session.SetupSessionStep;
import com.au5tie.minecraft.tobnet.game.session.SetupSessionStepInvocationContext;
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
   * This will configure all of the basic Arena Setup steps to define the name, boundaries, etc.
   *
   * @author au5tie
   */
  @Override
  protected void configureSteps() {
    // Arena Name.
    ArenaSetupSessionStepName nameStep = new ArenaSetupSessionStepName(0, this);
    registerStep(nameStep);

    // Boundary One.
    ArenaSetupSessionStepBoundaryOne boundaryOneStep =
      new ArenaSetupSessionStepBoundaryOne(1, this);
    registerStep(boundaryOneStep);

    // Boundary Two.
    ArenaSetupSessionStepBoundaryTwo boundaryTwoStep =
      new ArenaSetupSessionStepBoundaryTwo(2, this);
    registerStep(boundaryTwoStep);
  }

  @Override
  protected void onSessionComplete(SetupSessionStepInvocationContext context) {
    // Link back to all of the steps that were completed during the session.
    ArenaSetupSessionStepName nameStep =
      (ArenaSetupSessionStepName) getStepByName("arena-name").get();
    ArenaSetupSessionStepBoundaryOne boundaryOneStep =
      (ArenaSetupSessionStepBoundaryOne) getStepByName("boundary-one").get();
    ArenaSetupSessionStepBoundaryTwo boundaryTwoStep =
      (ArenaSetupSessionStepBoundaryTwo) getStepByName("boundary-two").get();

    // TODO Need to figure out consistency in creating the object.

    // Create the arena.
    TobnetArena arena = null; //TODO Reflection? Or does setup have to be in their code?!

    // Configure the boundaries.
    arena.setBoundaryOne(boundaryOneStep.getBoundaryLocation());
    arena.setBoundaryTwo(boundaryTwoStep.getBoundaryLocation());

    // Register the arena with the controller.
    TobnetGamePlugin.getArenaController().registerArena(arena);

    // TODO Store the arena in the database.

    // Notify the user of the setup success.
    TobnetChatUtils.sendPlayerMessage(
      context.getPlayer(),
      "Setup of arena " + arena.getName() + " completed successfully."
    );
  }
}
