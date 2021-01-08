package com.au5tie.minecraft.tobnet.game.arena.player;


import com.au5tie.minecraft.tobnet.game.arena.TobnetArena;
import com.au5tie.minecraft.tobnet.game.arena.exception.ArenaStatusNotJoinableException;
import com.au5tie.minecraft.tobnet.game.arena.game.ArenaGameManager;
import com.au5tie.minecraft.tobnet.game.arena.manager.ArenaManager;
import com.au5tie.minecraft.tobnet.game.arena.manager.ArenaManagerType;
import com.au5tie.minecraft.tobnet.game.arena.manager.ArenaManagerUtils;
import com.au5tie.minecraft.tobnet.game.player.GamePlayer;
import com.au5tie.minecraft.tobnet.game.util.TobnetLogUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArenaPlayerManager extends ArenaManager {

    private final TobnetArena arena;

    private Map<String, GamePlayer> players;

    public ArenaPlayerManager(TobnetArena arena) {

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
        // Register event handler.
        registerEventHandler(new ArenaPlayerEventHandler(this));
    }

    @Override
    public void destroyManager() {

    }

    /**
     * Determines if the player is currently playing.
     * @param uuid Player UUID.
     * @return If the player is playing.
     * @author au5tie.
     */
    public final boolean isPlaying(String uuid) {
        return players.containsKey(uuid);
    }

    /**
     * @return The numbers of players in the arena.
     * @author au5tie
     */
    public final int getNumberOfPlayers() {
        return players.size();
    }

    /**
     * @return All arena players.
     * @author au5tie
     */
    public final List<GamePlayer> getPlayers() {

        return new ArrayList<>(players.values());
    }

    /**
     * Obtains the {@link GamePlayer} for the player with the provided UUID.
     * @param uuid Player UUID.
     * @return Game Player of provided UUID, null if player does not exist.
     * @author au5tie
     */
    public final GamePlayer getGamePlayer(String uuid) {

        return players.get(uuid);
    }

    public synchronized void joinGame(GamePlayer player) {

        ArenaGameManager gameManager = ArenaManagerUtils.getGameManager(getArena()).get();

        // Verify that the arena status is one that is joinable.
        if (isArenaStatusJoinable()) {

            // Verify that there is room
            //TODO

            // Verify banned?

            // Publish pre-join event. This can be cancelled.
            // TODO

            this.players.put(player.getUuid(), player);
        } else {
            // The arena is in a status which means the player cannot join.
            throw new ArenaStatusNotJoinableException("Arena status is " + gameManager.getGameStatus() + " and is not joinable.");
        }
    }

    public synchronized void leaveGame(GamePlayer player) {

        if (isPlaying(player.getUuid())) {

            players.remove(player.getUuid());

            TobnetLogUtils.info(player.getUsername() + "left the game in " + getArena().getName());
        }
    }

    /**
     * Determines if the status of the arena indicates that a player can join.
     * @return If arena status dictates that a player can join.
     * @author au5tie
     */
    private boolean isArenaStatusJoinable() {

        ArenaGameManager gameManager = ArenaManagerUtils.getGameManager(getArena()).get();

        return gameManager.isStatusEmpty() || gameManager.isStatusWaiting() || gameManager.isStatusCountdown();
    }

    private boolean isRoomToJoin() {
        // TODO Obtain the number of players allowed from the game manager.
        return false;
    }
}
