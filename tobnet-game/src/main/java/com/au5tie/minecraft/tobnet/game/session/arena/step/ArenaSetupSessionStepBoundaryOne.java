package com.au5tie.minecraft.tobnet.game.session.arena.step;

import com.au5tie.minecraft.tobnet.core.session.SetupSessionStep;
import org.bukkit.Location;
import org.bukkit.entity.Player;

public class ArenaSetupSessionStepBoundaryOne extends SetupSessionStep {

    private Location boundaryOneLocation;

    public ArenaSetupSessionStepBoundaryOne(int order) {

        super("boundary-one", order);
    }

    /**
     * Invokes the step at the request/action of the provided {@link Player}.
     * @param player Player.
     * @author au5tie
     */
    @Override
    protected void invoke(Player player) {

    }
}
