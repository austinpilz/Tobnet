package com.au5tie.minecraft.tobnet.game.arena.setup.step;

import com.au5tie.minecraft.tobnet.game.session.SetupSession;
import com.au5tie.minecraft.tobnet.game.session.SetupSessionChatUtils;
import com.au5tie.minecraft.tobnet.game.session.SetupSessionStep;
import com.au5tie.minecraft.tobnet.game.session.SetupSessionStepInvocationContext;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.Location;

@Getter
public class ArenaSetupSessionStepBoundaryOne extends SetupSessionStep {

    private Location boundaryLocation;

    public ArenaSetupSessionStepBoundaryOne(int order, SetupSession session) {

        super("boundary-one", "Boundary One", order, session);
    }

    @Override
    protected void displayPromptBody(SetupSessionStepInvocationContext context) {
        // Send the instructions on how to configure the name.
        context.getPlayer().sendMessage("Go to the lower location of the arena boundary." +
                SetupSessionChatUtils.generateColoredChatSegment("/tobsetup arena here", ChatColor.GREEN, ChatColor.WHITE, true));
    }

    @Override
    protected boolean invoke(SetupSessionStepInvocationContext context) {

        // Obtain the player's current location as the boundary position.
        this.boundaryLocation = context.getPlayer().getLocation();

        return true;
    }
}