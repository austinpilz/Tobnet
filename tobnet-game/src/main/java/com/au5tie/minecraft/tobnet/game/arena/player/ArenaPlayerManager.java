package com.au5tie.minecraft.tobnet.game.arena.player;

import com.au5tie.minecraft.tobnet.game.arena.TobnetArena;
import com.au5tie.minecraft.tobnet.game.arena.exception.ArenaStatusNotJoinableException;
import com.au5tie.minecraft.tobnet.game.arena.game.ArenaGameManager;
import com.au5tie.minecraft.tobnet.game.arena.manager.ArenaManager;
import com.au5tie.minecraft.tobnet.game.arena.manager.ArenaManagerType;
import com.au5tie.minecraft.tobnet.game.arena.manager.ArenaManagerUtils;
import com.au5tie.minecraft.tobnet.game.event.TobnetEventPublisher;
import com.au5tie.minecraft.tobnet.game.exception.TobnetEngineException;
import com.au5tie.minecraft.tobnet.game.player.GamePlayer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArenaPlayerManager extends ArenaManager {

    private ArenaGameManager gameManager;

    private Map<String, GamePlayer> players;

    public ArenaPlayerManager(TobnetArena arena) {

        super(arena);

        this.players = new HashMap<>();
    }

    @Override
    public ArenaManagerType getType() {

        return ArenaManagerType.PLAYER;
    }

    @Override
    public void prepareManager() {

        registerEventHandler(new ArenaPlayerEventHandler(this));
    }

    @Override
    public void destroyManager() {
        //
    }

    @Override
    public void afterArenaPreparationComplete() {

        gameManager = (ArenaGameManager) ArenaManagerUtils.getManagerOfType(getArena(), ArenaManagerType.GAME).orElseThrow(TobnetEngineException::new);
    }

    /**
     * Determines if the player is currently playing.
     *
     * @param player Game Player.
     * @return If the player is playing.
     * @author au5tie.
     */
    public final boolean isPlaying(GamePlayer player) {

        return isPlaying(player.getUuid());
    }

    /**
     * Determines if the player is currently playing.
     *
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
     *
     * @param uuid Player UUID.
     * @return Game Player of provided UUID, null if player does not exist.
     * @author au5tie
     */
    public final GamePlayer getGamePlayer(String uuid) {

        return players.get(uuid);
    }

    /**
     * Requests the player to join the arena game. This will evaluate if they player is able to join and will admit them
     * to the game if all checks complete successfully.
     *
     * @param player Player.
     * @author au5tie
     */
    public final synchronized void joinGame(GamePlayer player) {

        ArenaGameManager gameManager = ArenaManagerUtils.getGameManager(getArena()).get();

        // Verify that the arena status is one that is joinable.
        if (isArenaStatusJoinable()) {

            // Publish intent for this player to join the arena.
            TobnetPlayerPreJoinEvent preJoinEvent = new TobnetPlayerPreJoinEvent(getArena(), player);
            TobnetEventPublisher.publishEvent(preJoinEvent);

            if (!preJoinEvent.isCancelled()) {
                // Join the player into the game.
                performPlayerJoin(player);
            } else {
                // The pre-join event was cancelled, thus we cannot admit this player to the game.
                throw new ArenaStatusNotJoinableException("Player pre-join event was cancelled.");
            }
        } else {
            // The arena is in a status which means the player cannot join.
            throw new ArenaStatusNotJoinableException("Arena status is " + gameManager.getGameStatus() + " and is not joinable.");
        }
    }

    /**
     * Performs the joining of the player into the arena game.
     *
     * @param player Player.
     * @author au5tie
     */
    private void performPlayerJoin(GamePlayer player) {
        // Store the player in the manager as them officially joining.
        this.players.put(player.getUuid(), player);

        // Allow overriding player manager to perform custom operations.
        onPlayerJoin(player);

        // Publish event out that the player has joined the game.
        TobnetEventPublisher.publishEvent(new TobnetPlayerPostJoinEvent(getArena(), player));
    }

    /**
     * Joins the player into the arena. This will prepare the player, add them into the arena game, and publish events
     * out notifying of the join.
     *
     * @param player Player.
     * @author au5tie
     */
    protected void onPlayerJoin(GamePlayer player) {

        // TODO NEXT to this. Figure out what to do to players when they join.

        // ON PLAYER JOIN
            // IF WAITING/EMPTY, announce arrival to all players.

        // PLAYER ROLES
            // Player status (Alive, Dead, Spectator)
            // Player role (custom for jason, counselor, spectator, etc.)

        // LOCATIONS
            // Store all locations via events.
            // Load locations to generate Location Manager.
            // Then each specialized manager will load from that generalized manager.

    }

    /**
     * Requests the player to leave the game. This will remove the player from the arena and game if they are currently
     * playing.
     *
     * @param player Player.
     * @author au5tie
     */
    public final synchronized void leaveGame(GamePlayer player) {

        if (isPlaying(player.getUuid())) {
            // Remove the player from the game.
            performPlayerLeave(player);
        }
    }

    /**
     * Cleans player up and removes them from the arena game.
     *
     * @param player Player.
     * @author au5tie
     */
    private void performPlayerLeave(GamePlayer player) {
        // Notify listeners of player leaving.
        TobnetEventPublisher.publishEvent(new TobnetPlayerLeaveEvent(getArena(), player));

        // Hide & destroy all display components for the user. This cleans up their display and all like references.
        player.getDisplayManager().destroyComponents();

        // Stop all sounds.

        // Remove all potion effects.

        // Teleport out.

        // Allow overriding player manager to perform custom operations.
        onPlayerLeave(player);

        // Remove player from manager.
        players.remove(player.getUuid());
    }

    protected void onPlayerLeave(GamePlayer player) {
        //
    }

    protected void onPlayerDeath(GamePlayer player) {
        //
    }

    /**
     * Determines if the status of the arena indicates that a player can join.
     *
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
