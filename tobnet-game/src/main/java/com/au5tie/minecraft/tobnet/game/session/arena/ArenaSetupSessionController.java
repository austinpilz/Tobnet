package com.au5tie.minecraft.tobnet.game.session.arena;

import com.au5tie.minecraft.tobnet.core.session.SetupSession;
import com.au5tie.minecraft.tobnet.core.session.SetupSessionController;
import org.bukkit.entity.Player;

public class ArenaSetupSessionController extends SetupSessionController {


    public ArenaSetupSessionController() {

        super();
    }

    protected SetupSession prepareNewSession(Player player) {

        return new ArenaSetupSession(player);
    }
}
