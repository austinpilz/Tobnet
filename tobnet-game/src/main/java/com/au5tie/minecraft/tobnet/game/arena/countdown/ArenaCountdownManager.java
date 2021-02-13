package com.au5tie.minecraft.tobnet.game.arena.countdown;

import com.au5tie.minecraft.tobnet.game.arena.TobnetArena;
import com.au5tie.minecraft.tobnet.game.arena.countdown.display.ArenaCountdownDisplayComponent;
import com.au5tie.minecraft.tobnet.game.arena.countdown.display.ArenaCountdownDisplayComponentBossBar;
import com.au5tie.minecraft.tobnet.game.arena.countdown.start.ArenaCountdownSecondsProvider;
import com.au5tie.minecraft.tobnet.game.arena.manager.ArenaManager;
import com.au5tie.minecraft.tobnet.game.arena.manager.ArenaManagerType;
import com.au5tie.minecraft.tobnet.game.arena.manager.ArenaManagerUtils;
import com.au5tie.minecraft.tobnet.game.arena.player.ArenaPlayerDisplayManager;
import com.au5tie.minecraft.tobnet.game.arena.task.ArenaTask;
import com.au5tie.minecraft.tobnet.game.arena.task.ArenaTaskMode;
import com.au5tie.minecraft.tobnet.game.arena.task.ArenaTaskType;
import com.au5tie.minecraft.tobnet.game.display.component.GamePlayerDisplayComponent;
import com.au5tie.minecraft.tobnet.game.event.TobnetEventPublisher;
import com.au5tie.minecraft.tobnet.game.exception.TobnetEngineException;
import com.au5tie.minecraft.tobnet.game.player.GamePlayer;
import com.au5tie.minecraft.tobnet.game.time.TimeDifference;
import lombok.Getter;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

/**
 * The Arena Countdown Manager is responsible for the management of the countdown within the arena. It will manage the
 * countdown seconds remaining, the scheduled task which will perform the automated countdown, etc. This manager does not
 * control any user display data, that is managed by the Player Manager.
 *
 * The ArenaCountdownSecondsProvider is what determines the number of seconds that the countdown begins at and will count
 * down from. There are out of the box solutions, like the static one, but also allows for custom solutions like making
 * it dynamic. All you have to do is extend the ArenaCountdownSecondsProvider class and provide that when you create this
 * manager.
 *
 * @author au5tie
 */
@Getter
public class ArenaCountdownManager extends ArenaManager {

    private final ArenaCountdownConfiguration configuration;
    private final ArenaCountdownSecondsProvider secondsProvider;

    private ArenaTask arenaCountdownTask;

    private int secondsLeft;
    private LocalDateTime countdownLastUpdated;

    private ArenaPlayerDisplayManager playerDisplayManager;
    private final String playerCountdownDisplayComponentName = "countdown";

    public ArenaCountdownManager(TobnetArena arena, ArenaCountdownConfiguration configuration, ArenaCountdownSecondsProvider secondsProvider) {
        super(arena);

        this.configuration = configuration;
        this.secondsProvider = secondsProvider;
    }

    @Override
    public ArenaManagerType getType() {
        return ArenaManagerType.COUNTDOWN;
    }

    @Override
    public void prepareManager() {
        // Register event handler.
        registerEventHandler(new ArenaCountdownEventHandler(getArena(), this));

        // Prepare the initial countdown configuration.
        resetCountdownSeconds();
    }

    @Override
    public void destroyManager() {

    }

    @Override
    public void afterArenaPreparationComplete() {
        // Link to the player display manager.
        playerDisplayManager = (ArenaPlayerDisplayManager) ArenaManagerUtils.getManagerOfType(getArena(), ArenaManagerType.PLAYER_DISPLAY).orElseThrow(TobnetEngineException::new);
    }

    /**
     * Returns the Arena Countdown Configuration.
     *
     * @return Arena Countdown Configuration.
     * @author au5tie
     */
    public ArenaCountdownConfiguration getConfiguration() {

        return configuration;
    }

    /**
     * Determines if the countdown has completed. A countdown completes when there are no remaining seconds left in the
     * countdown.
     *
     * @return If the countdown has completed.
     * @author au5tie
     */
    public final boolean hasCountdownFinished() {

        return secondsLeft <= 0;
    }

    /**
     * Initiates the countdown within the arena. This will schedule the countdown task to begin and will initiate the
     * display of the countdown to users.
     *
     * @author au5tie
     */
    public void beginCountdown() {
        // Reset the countdown so we can ensure we're getting a clean start.
        resetCountdownSeconds();

        if (arenaCountdownTask == null) {
            // Create the countdown task for the first time.
            arenaCountdownTask = ArenaTask.builder()
                    .runnable(new ArenaCountdownUpdateTask(this))
                    .manager(this)
                    .mode(ArenaTaskMode.SYNC)
                    .type(ArenaTaskType.REPEATING)
                    .initialDelay(5)
                    .interval(5)
                    .build();
        }

        if (arenaCountdownTask != null && arenaCountdownTask.isScheduled()) {
            // There is already a countdown running, we have to cancel it.
            arenaCountdownTask.cancelTask();
        }

        // Schedule the countdown task.
        arenaCountdownTask.scheduleTask();
    }

    /**
     * Stops the countdown within the arena. This will take care of cancelling the countdown task as well as cleaning up
     * any visual displays to the players.
     *
     * @author au5tie
     */
    public void stopCountdown() {
        // Stop the task.
        if (arenaCountdownTask != null && arenaCountdownTask.isScheduled()) {
            // The countdown task is running, let's cancel it.
            arenaCountdownTask.cancelTask();
        }
    }

    /**
     * Completes the countdown process.
     *
     * @author au5tie
     */
    public void completeCountdown() {
        // Stop the task.
        stopCountdown();
    }

    /**
     * Resets the countdown to its beginning values.
     *
     * @author au5tie
     */
    private final void resetCountdownSeconds() {

        // Reset the actual countdown bak to the starting value.
        secondsLeft = getStartingSeconds();
        countdownLastUpdated = LocalDateTime.now();
    }

    /**
     * Obtains the starting number of seconds for the countdown. This will refer to the countdown configuration by default
     * to determine the seconds. The implementing plugin can override this method to add more advanced functionality, like
     * dynamic seconds based on the number of players, etc.
     *
     * @return The starting number of seconds for the countdown.
     * @author au5tie
     */
    private final int getStartingSeconds() {

        return Math.max(1, secondsProvider.getStartingSeconds());
    }

    /**
     * Decrements the countdown timer.
     *
     * This is designed to be ignorant of the actual timing of whatever task will call for the countdown to be decremented.
     * It will keep track of the last time we updated the countdown so it can ensure we never decrement the countdown if
     * less than a second has passed.
     *
     * @author au5tie
     */
    private final void decrementCountdown() {
        // Verify we're only decrementing if a second or more has passed, so we don't worry about task timing.
        if (new TimeDifference(countdownLastUpdated, LocalDateTime.now()).getSecondsBetween() >= 1) {

            if (secondsLeft > 0) {
                // Decrement one second from our countdown.
                secondsLeft--;
                countdownLastUpdated = LocalDateTime.now();

                // Publish event that we've decremented the time.
                TobnetEventPublisher.publishEvent(new TobnetArenaCountdownDecrementedEvent(getArena(), this, secondsLeft + 1, secondsLeft));
            }
        }
    }

    /**
     * The countdown heartbeat is responsible for the recurring decrementing of the counter. This will initiate the
     * decrement of the countdown timer and is meant to be called on an interval.
     *
     * @author au5tie
     */
    void heartbeat() {
        // Decrement the countdown.
        decrementCountdown();

        // Display countdown to users.
        displayCountdownToUsers();
    }

    private void displayCountdownToUsers() {

        // TODO Register. Document how this adds for people who join late.
        registerCountdownDisplayToUsers();

        getPlayerDisplayManager().getPlayerManager().getPlayers().forEach(player -> player.getDisplayManager().requestComponentDisplay(getPlayerCountdownDisplayComponentName()));

        // TODO Update.
        getPlayerDisplayManager().getPlayerManager().getPlayers().forEach(player -> updateUserCountdownDisplay(player));


    }

    private void registerCountdownDisplayToUsers() {
        // Obtain all players who are missing the countdown display component, we'll need to create and regiser one for them.
        List<GamePlayer> players = getPlayerDisplayManager().getPlayersMissingComponent(getPlayerCountdownDisplayComponentName());

        players.forEach(player -> player.getDisplayManager().registerComponent(createUserCountdownDisplay(player)));
    }

    private void hideCountdownFromPlayers() {
        //
    }

    private GamePlayerDisplayComponent createUserCountdownDisplay(GamePlayer player) {

        return new ArenaCountdownDisplayComponentBossBar(getPlayerCountdownDisplayComponentName(), 5, player, "Game Countdown", BarColor.WHITE, BarStyle.SOLID, BarFlag.CREATE_FOG);
    }

    private void updateUserCountdownDisplay(GamePlayer player) {

       Optional<GamePlayerDisplayComponent> component = player.getDisplayManager().getComponent(getPlayerCountdownDisplayComponentName());

       if (component.isPresent() && component.get() instanceof ArenaCountdownDisplayComponent) {

           ArenaCountdownDisplayComponent displayComponent = (ArenaCountdownDisplayComponent) component.get();

           // Update the progress of the display.
           displayComponent.updateProgress(getSecondsLeft(), getStartingSeconds());
       }
    }
}