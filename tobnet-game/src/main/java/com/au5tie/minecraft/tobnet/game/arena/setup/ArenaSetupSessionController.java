package com.au5tie.minecraft.tobnet.game.arena.setup;

import com.au5tie.minecraft.tobnet.game.session.SetupSession;
import com.au5tie.minecraft.tobnet.game.session.SetupSessionController;
import org.bukkit.entity.Player;

public class ArenaSetupSessionController extends SetupSessionController {

    public ArenaSetupSessionController() {
        super("arena");
    }

    protected SetupSession prepareNewSession(Player player) {

        return new ArenaSetupSession(player);
    }
}
