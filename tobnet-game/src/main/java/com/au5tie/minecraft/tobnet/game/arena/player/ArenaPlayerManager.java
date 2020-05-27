package com.au5tie.minecraft.tobnet.game.arena.player;


import com.au5tie.minecraft.tobnet.core.arena.Arena;
import com.au5tie.minecraft.tobnet.core.arena.manager.ArenaManager;
import com.au5tie.minecraft.tobnet.core.arena.manager.ArenaManagerType;
import com.au5tie.minecraft.tobnet.core.player.GamePlayer;

import java.util.HashMap;
import java.util.Map;

public class ArenaPlayerManager extends ArenaManager {

    private Arena arena;

    private Map<String, GamePlayer> players;

    public ArenaPlayerManager(Arena arena) {

        super(arena);

        this.arena = arena;

        this.players = new HashMap<>();
    }

    @Override
    public ArenaManagerType getType() {
        return ArenaManagerType.PLAYER;
    }

    @Override
    public void prepareManager() {
        //
    }

    /**
     * Determines if the player is currently playing.
     * @param uuid Player UUID.
     * @return If the player is playing.
     * @author au5tie.
     */
    public boolean isPlaying(String uuid) {
        return players.containsKey(uuid);
    }

}
