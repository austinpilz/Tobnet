package com.au5tie.sandfall;

import com.au5tie.minecraft.tobnet.game.TobnetGamePlugin;
import com.au5tie.sandfall.arena.setup.SandFallArenaSetupSession;
import com.au5tie.sandfall.command.F13BaseCommandListener;

/**
 * This is the "main" class of our example game, SandFall. This is where we will take care of setting up our portion of
 * the plugin in order to allow the Tobnet engine to take care of the heavy lifting.
 *
 * @author au5tie
 */
public class SandFallPlugin extends TobnetGamePlugin {

    @Override
    public void enablePlugin() {

        // Create the base F13 command listener for player interface. TODO REVIEW
        getCommandController().registerCommandLister(new F13BaseCommandListener());



        configureSetupSessions();

    }

    private void configureSetupSessions() {
        getSetupSessionController().registerSessionType(SandFallArenaSetupSession.class, "arena");
    }

    @Override
    public void disablePlugin() {
        //
    }
}
