package com.au5tie.mypetpie.manager;

import com.au5tie.minecraft.tobnet.core.arena.Arena;
import com.au5tie.minecraft.tobnet.core.arena.manager.CustomArenaManager;
import com.au5tie.minecraft.tobnet.core.util.TobnetLogUtils;

public class PieManager extends CustomArenaManager {

    public PieManager(Arena arena) {

        super("pie-manager", arena);
    }

    @Override
    public void prepareManager() {

        TobnetLogUtils.info("Pie Manager prepared!");
    }
}
