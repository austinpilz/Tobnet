package com.au5tie.minecraft.tobnet.game.arena.game;

import com.au5tie.minecraft.tobnet.core.arena.Arena;
import com.au5tie.minecraft.tobnet.core.arena.game.ArenaGameStatus;
import com.au5tie.minecraft.tobnet.core.arena.manager.ArenaManager;
import com.au5tie.minecraft.tobnet.core.arena.manager.ArenaManagerType;
import com.au5tie.minecraft.tobnet.game.arena.player.ArenaPlayerManager;
import com.au5tie.minecraft.tobnet.game.arena.util.ArenaManagerUtils;
import lombok.Getter;

@Getter
public class ArenaGameManager extends ArenaManager {

    private ArenaGameStatus gameStatus;

    public ArenaGameManager(Arena arena) {

        super(arena);

        this.gameStatus = ArenaGameStatus.EMPTY;
    }

    @Override
    public ArenaManagerType getType() {

        return ArenaManagerType.GAME;
    }

    @Override
    public void prepareManager() {
    //
    }

    /**
     * Returns the {@link ArenaPlayerManager}.
     * @return Arena Player Manager.
     * @author au5tie
     */
    private ArenaPlayerManager getPlayerManager() {
        return ArenaManagerUtils.getPlayerManager(getArena()).get();
    }
}
