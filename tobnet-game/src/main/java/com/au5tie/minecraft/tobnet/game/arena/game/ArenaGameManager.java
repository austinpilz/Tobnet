package com.au5tie.minecraft.tobnet.game.arena.game;

import com.au5tie.minecraft.tobnet.game.annotation.TobnetRecommendOverride;
import com.au5tie.minecraft.tobnet.game.arena.TobnetArena;
import com.au5tie.minecraft.tobnet.game.arena.countdown.ArenaCountdownManager;
import com.au5tie.minecraft.tobnet.game.arena.manager.ArenaManager;
import com.au5tie.minecraft.tobnet.game.arena.manager.ArenaManagerType;
import com.au5tie.minecraft.tobnet.game.arena.manager.ArenaManagerUtils;
import com.au5tie.minecraft.tobnet.game.arena.player.ArenaPlayerManager;
import com.au5tie.minecraft.tobnet.game.arena.task.ArenaTask;
import com.au5tie.minecraft.tobnet.game.arena.task.ArenaTaskMode;
import com.au5tie.minecraft.tobnet.game.arena.task.ArenaTaskType;
import com.au5tie.minecraft.tobnet.game.event.TobnetEventPublisher;
import com.au5tie.minecraft.tobnet.game.util.TobnetLogUtils;

public class ArenaGameManager extends ArenaManager {

    // Game Status represents the overall state of the game within the arena.
    private ArenaGameStatus gameStatus;

    // Scheduled Tasks.
    ArenaTask arenaGameStatusHeartbeatTask;

    public ArenaGameManager(TobnetArena arena) {

        super(arena);

        // The arena enters preparation phase when it is first created.
        changeGameStatus(ArenaGameStatus.PREPARING);
    }

    @Override
    public ArenaManagerType getType() {

        return ArenaManagerType.GAME;
    }

    @Override
    public void prepareManager() {
        // Schedule the heartbeat task.
        scheduleArenaGameStatusHeartbeatTask();

        // Transition the game to EMPTY as we've prepared as much as we can with this manager.
        changeGameStatus(ArenaGameStatus.EMPTY);
    }

    @Override
    public void destroyManager() {
        // Cancel the heartbeat task.
        cancelArenaGameStatusHeartbeatTask();
    }

    /**
     * @return The Arena's Game Status.
     * @author au5tie
     */
    public final ArenaGameStatus getGameStatus() {

        return this.gameStatus;
    }

    /**
     * @return If the arena status is EMPTY.
     * @author au5tie
     */
    public final boolean isStatusEmpty() {

        return ArenaGameStatus.EMPTY.equals(getGameStatus());
    }

    /**
     * @return If the arena status is WAITING.
     * @author au5tie
     */
    public final boolean isStatusWaiting() {

        return ArenaGameStatus.WAITING.equals(getGameStatus());
    }

    /**
     * @return If the arena status is COUNTDOWN.
     * @author au5tie
     */
    public final boolean isStatusCountdown() {

        return ArenaGameStatus.COUNTDOWN.equals(getGameStatus());
    }

    /**
     * @return If the arena status is IN PROGRESS.
     * @author au5tie
     */
    public final boolean isStatusInProgress() {

        return ArenaGameStatus.IN_PROGRESS.equals(getGameStatus());
    }

    /**
     * The heartbeat of the game. This method is invoked on a scheduled timer which evaluates the current status of the
     * game and updates the status / takes action accordingly.
     *
     * TODO Document further
     *
     * @author
     */
    public void evaluateGameStatus() {

        if (isStatusEmpty()) {
            evaluateEmptyStatus();
        } else if (isStatusWaiting()) {
            evaluateWaitingStatus();
        } else if (isStatusCountdown()) {
            evaluateCountdownStatus();
        } else if (isStatusInProgress()) {
            //TODO
        }
    }

    /**
     * Evaluates the arena when in EMPTY state. This will validate if the arena should still be in EMPTY state
     * and transition it to WAITING if required.
     *
     * @author au5tie
     */
    private void evaluateEmptyStatus() {

        ArenaPlayerManager playerManager = getPlayerManager();

        if (playerManager.getNumberOfPlayers() > 0) {
            // When at least one player is present, we can transition to waiting state.
            changeGameStatus(ArenaGameStatus.WAITING);
        }
    }

    /**
     * Evaluates the arena when in WAITING state. This will handle the transition from waiting to COUNTDOWN and also
     * back to EMPTY if the basic requirements are no longer met.
     *
     * @author au5tie
     */
    private void evaluateWaitingStatus() {

        ArenaPlayerManager playerManager = getPlayerManager();

        if (playerManager.getNumberOfPlayers() > 0) {
            // We have at least 1 player, so we aren't empty.
            if (areMinimumPlayerRequirementsMetToBegin()) {
                // Begin countdown if it's not already going.
                changeGameStatus(ArenaGameStatus.COUNTDOWN);
            }
        } else {
            // There are no players which means the arena cannot be in waiting.
            changeGameStatus(ArenaGameStatus.EMPTY);
        }
    }

    /**
     * Evaluates the arena when in COUNTDOWN state. This is responsible for initiating the countdown process and stopping
     * it should requirements fail to be met prior to countdown completion.
     *
     * @author au5tie
     */
    private void evaluateCountdownStatus() {

        ArenaPlayerManager playerManager = (ArenaPlayerManager) ArenaManagerUtils.getManagerOfType(getArena(), ArenaManagerType.PLAYER).get();
        ArenaCountdownManager countdownManager = (ArenaCountdownManager) ArenaManagerUtils.getManagerOfType(getArena(), ArenaManagerType.COUNTDOWN).get();

        if (playerManager.getNumberOfPlayers() > 0 && areMinimumPlayerRequirementsMetToBegin()) {

            if (countdownManager.hasCountdownFinished()) {
                // Change the status to in-progress.
                changeGameStatus(ArenaGameStatus.IN_PROGRESS);
            }
        } else {
            // Transition the arena back to WAITING while we await requirements to be met. STOP THE COUNT lul
            changeGameStatus(ArenaGameStatus.WAITING);
        }
    }

    /**
     * Transitions the game status to a new state. This will publish transition events before and after the game status
     * has been updated.
     *
     * @param gameStatus New Game Status.
     * @author au5tie
     */
    private void changeGameStatus(ArenaGameStatus gameStatus) {

        ArenaGameStatus originalStatus = getGameStatus();

        // Publish pre-change event.
        TobnetEventPublisher.publishEvent(new TobnetArenaStatusPreChangeEvent(getArena(), originalStatus, gameStatus));

        // Change the game status.
        this.gameStatus = gameStatus;

        // Publish post-change event.
        TobnetEventPublisher.publishEvent(new TobnetArenaStatusPostChangeEvent(getArena(), originalStatus, gameStatus));

        // Log the status change.
        TobnetLogUtils.info("Arena " + getArena().getName() + " status changed from " + originalStatus + " to " + getGameStatus());
    }

    /**
     * Schedules the {@link ArenaGameStatusHeartbeat} task to be run. This task is responsible for the game manager being
     * able to monitor the status of the arena constantly and react to changes. Although gameplay is largely event based,
     * this heartbeat is responsible for catching anything events have failed to, like a final safety net.
     *
     * @author au5tie
     */
    private void scheduleArenaGameStatusHeartbeatTask() {

        if (arenaGameStatusHeartbeatTask == null) {
            // Create the task for the first time.
            arenaGameStatusHeartbeatTask = ArenaTask.builder()
                    .runnable(new ArenaGameStatusHeartbeat(this))
                    .manager(this)
                    .mode(ArenaTaskMode.SYNC)
                    .type(ArenaTaskType.REPEATING)
                    .initialDelay(20)
                    .interval(20)
                    .build();
        }

        // Schedule the task.
        arenaGameStatusHeartbeatTask.scheduleTask();
    }

    /**
     * Cancels the {@link ArenaGameStatusHeartbeat} task from running.
     *
     * @author au5tie
     */
    private void cancelArenaGameStatusHeartbeatTask() {

        if (arenaGameStatusHeartbeatTask != null) {
            arenaGameStatusHeartbeatTask.cancelTask();
        }
    }

    /**
     * Determines if the minimum requirements for players have been met in order for the game to begin.
     *
     * This allows for simple requirement checks (like static number of users required) to advanced checks like specific
     * player role balancing, etc.
     *
     * @return If the minimum player requirements have been met for the game to begin.
     * @author au5tie
     */
    @TobnetRecommendOverride
    boolean areMinimumPlayerRequirementsMetToBegin() {

        // TODO
        return true;
    }

    /**
     * Determines the maximum number of players who can play in the arena.
     *
     * This determination considers both arena configuration and spawn points when calculating the player limit. Some
     * arenas can be a static number of users, where others can be dynamic based on factors like the spawn point.
     *
     * @return The maximum number of players who can play within the arena.
     * @author au5tiw
     */
    @TobnetRecommendOverride
    int getMaximumAllowedNumberOfPlayers() {
        // Look at the gameplay configuration.
        // TODO
        return 2;
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
