package com.au5tie.minecraft.tobnet.game.session.arena.step;

import com.au5tie.minecraft.tobnet.core.session.SetupSession;
import com.au5tie.minecraft.tobnet.core.session.SetupSessionChatUtils;
import com.au5tie.minecraft.tobnet.core.session.SetupSessionStep;
import com.au5tie.minecraft.tobnet.core.session.SetupSessionStepInvocationContext;
import org.bukkit.ChatColor;

public class ArenaSetupSessionStepName extends SetupSessionStep {

    private String arenaName;

    public ArenaSetupSessionStepName(int order, SetupSession session) {

        super("arena-name", "Arena Name", order, session);
    }

    @Override
    protected void displayPromptBody(SetupSessionStepInvocationContext context) {
        // Send the instructions on how to configure the name.
        context.getPlayer().sendMessage("To configure the name of the arena, execute the command " +
                SetupSessionChatUtils.generateColoredChatSegment("/idkyet setup [arenaName]", ChatColor.GREEN, ChatColor.WHITE, true));
    }

    @Override
    protected boolean invoke(SetupSessionStepInvocationContext context) {

        return true;
    }
}
