package com.au5tie.minecraft.tobnet.game.session.arena.step;

import com.au5tie.minecraft.tobnet.core.session.SetupSession;
import com.au5tie.minecraft.tobnet.core.session.SetupSessionChatUtils;
import com.au5tie.minecraft.tobnet.core.session.SetupSessionStep;
import com.au5tie.minecraft.tobnet.core.session.SetupSessionStepInvocationContext;
import org.bukkit.ChatColor;
import org.bukkit.Location;

public class ArenaSetupSessionStepBoundaryOne extends SetupSessionStep {

    private Location boundaryOneLocation;

    public ArenaSetupSessionStepBoundaryOne(int order, SetupSession session) {

        super("boundary-two", "Boundary Two", order, session);
    }

    @Override
    protected void displayPromptBody(SetupSessionStepInvocationContext context) {
        // Send the instructions on how to configure the name.
        context.getPlayer().sendMessage("Go to the lower location of the arena boundary." +
                SetupSessionChatUtils.generateColoredChatSegment("/idkyet setup [arenaName]", ChatColor.GREEN, ChatColor.WHITE, true));
    }

    @Override
    protected boolean invoke(SetupSessionStepInvocationContext context) {

        return true;
    }
}
