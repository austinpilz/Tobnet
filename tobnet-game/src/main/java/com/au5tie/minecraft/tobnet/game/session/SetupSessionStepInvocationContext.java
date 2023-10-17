package com.au5tie.minecraft.tobnet.game.session;

import com.google.common.collect.ImmutableList;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * The SetupSessionStepInvocationContext serves as the context for the step being invoked by the user. It's designed
 * to convey information of the various data points that setup session rely upon. For example, the player invoking the
 * setup, the block being clicked, the command line arguments entered, etc.
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class SetupSessionStepInvocationContext {

    private Player player;
    private Block block;
    private String command;
    private String sessionType;
    private List<String> commandArguments;
    private String label;

    /**
     * @return If the context has a player associated to it.
     * @author au5tie
     */
    public final boolean hasPlayer() {

        return player != null;
    }

    public List<String> getCommandArguments() {
        return ImmutableList.copyOf(commandArguments);
    }
}
