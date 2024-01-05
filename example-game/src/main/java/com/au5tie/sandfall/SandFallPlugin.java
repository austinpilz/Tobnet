package com.au5tie.sandfall;

import com.au5tie.minecraft.tobnet.game.TobnetGamePlugin;
import com.au5tie.minecraft.tobnet.game.session.TobnetSetupSessionController;
import com.au5tie.sandfall.arena.setup.SandFallArenaSetupSession;
import com.au5tie.sandfall.command.SandFallCommandListener;

/**
 * This is the "main" class of our example game, SandFall. This is where we will take care of setting up our portion of
 * the plugin in order to allow the Tobnet engine to take care of the heavy lifting.
 *
 * @author au5tie
 */
public class SandFallPlugin extends TobnetGamePlugin {

  public static final String gameCommand = "sf";

  /**
   * When this is called, the Tobnet engine has already performed the majority of the startup preparation. We'll
   * only need to register anything specific to our plugin here that isn't already handled by the engine out of the
   * box.
   */
  @Override
  public void enablePlugin() {
    getCommandController()
      .registerCommandListener(new SandFallCommandListener());

    configureSetupSessions();
  }

  /**
   * Creates the setup session controller which will be used by the Tobnet Engine to control and manage all the setup
   * sessions for users.
   *
   * In this example, we override (but don't have to) the default implementation in order to configure the command we
   * want the sessions to listen on. By default, the setup session controller will listen on the tobnet engine command
   * (tob), but we want it to listen on (sf) instead. We provide that to the constructor which will have it listen on
   * our desired command.
   *
   * @return Setup Session Controller configured to listen to the SandFall command.
   * @author au5tie
   */
  @Override
  protected TobnetSetupSessionController createSetupSessionController() {
    return new TobnetSetupSessionController(gameCommand);
  }

  /**
   * Configures the setup session types which will be supported in our SandFall game.
   *
   * In this example, we register the "arena" session type which corresponds to the {@link SandFallArenaSetupSession}
   * class. If a user goes through the setup session process and provides the type as "arena", the setup session controller
   * will instantiate a new {@link SandFallArenaSetupSession} for them. This will allow users to go through the arena
   * setup process. If we had any additional setup session types we'd want to have supported, this is where we want to
   * register them. You also do not have to have any setup sessions supported if you don't want to.
   *
   * @author au5tie
   */
  private void configureSetupSessions() {
    getSetupSessionController()
      .registerSessionType(SandFallArenaSetupSession.class, "arena");
  }

  @Override
  public void disablePlugin() {
    //
  }
}
