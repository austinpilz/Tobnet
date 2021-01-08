package com.au5tie.f13;

import com.au5tie.f13.command.F13BaseCommandListener;
import com.au5tie.minecraft.tobnet.game.TobnetGamePlugin;

public class FridayThe13thPlugin extends TobnetGamePlugin {

    public void enablePlugin() {

        // Create the base F13 command listener for player interface.
        getCommandController().registerCommandLister(new F13BaseCommandListener());

    }

    public void disablePlugin() {
        //
    }
}
