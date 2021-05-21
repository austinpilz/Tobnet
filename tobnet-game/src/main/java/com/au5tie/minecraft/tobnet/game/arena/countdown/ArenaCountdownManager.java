package com.au5tie.minecraft.tobnet.game.arena.countdown;

import com.au5tie.minecraft.tobnet.game.TobnetGamePlugin;
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
import com.au5tie.minecraft.tobnet.game.event.TobnetEventPublisher;
import com.au5tie.minecraft.tobnet.game.exception.TobnetEngineException;
import com.au5tie.minecraft.tobnet.game.message.MessageConstants;
import com.au5tie.minecraft.tobnet.game.player.GamePlayer;
import com.au5tie.minecraft.tobnet.game.player.display.component.GamePlayerDisplayComponent;
import com.au5tie.minecraft.tobnet.game.time.TimeDifference;
import lombok.Getter;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.command.CommandSender;

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
        // Only thing to do would be cancel the countdown which will stop task and remove display from all users.
        stopCountdown();
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

        // Remove the countdown display from players so they can transition into the actual game.
        hideCountdownFromPlayers();
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
    final void heartbeat() {
        // Decrement the countdown.
        decrementCountdown();

        // Display countdown to users.
        displayCountdownToUsers();
    }

    /**
     * Displays the countdown display component to all users. This will take care of registering new display components
     * for each player, making the component visible, and keeping it updated with the latest values. This is like the
     * heartbeat of the update display.
     *
     * Players can join the countdown after it has begun, which is ok. Since this is called by the repeating task to update
     * the countdown value, this will quickly add the countdown display for players who join at any point during the
     * pre-game status. There is no need to worry about players not having the display at all.
     *
     * @author au5tie
     */
    private final void displayCountdownToUsers() {

        if (configuration.isDisplayCountdownUI()) {
            // Register the countdown display component to all users who need it but do not already have it.
            registerCountdownDisplayToUsers();

            // Request that the countdown component be displayed. Will only be visible if highest priority.
            getPlayerDisplayManager().getPlayerManager().getPlayers().forEach(player -> player.getDisplayManager().requestComponentDisplay(getPlayerCountdownDisplayComponentName()));

            // Keep the countdown display updated for each player with the current progress.
            getPlayerDisplayManager().getPlayerManager().getPlayers().forEach(player -> updateUserCountdownDisplay(player));
        }
    }

    /**
     * Hides the countdown display component from all players who have it.
     *
     * @author au5tie
     */
    private final void hideCountdownFromPlayers() {

        getPlayerDisplayManager().getPlayerManager().getPlayers().forEach(player -> player.getDisplayManager().requestComponentHide(getPlayerCountdownDisplayComponentName()));
    }

    /**
     * This will create a new countdown display component and register it with the display manager for any player who does
     * not already have it registered. This will not make the countdown visible, but rather setup the countdown display
     * component for when we want to make it visible, which is a different operation.
     *
     * @author au5tie
     */
    private final void registerCountdownDisplayToUsers() {
        // Obtain all players who are missing the countdown display component, we'll need to create and register one for them.
        List<GamePlayer> players = getPlayerDisplayManager().getPlayersMissingComponent(getPlayerCountdownDisplayComponentName());

        // Register a new display component for each one.
        players.forEach(player -> registerCountdownDisplayToUser(player));
    }

    /**
     * Registers a new {@link ArenaCountdownDisplayComponent} with the player's display manager to display the countdown.
     *
     * This will throw an exception if the generated component does not inherit from the ArenaCountdownDisplayComponent
     * interface.
     *
     * @param player Player.
     * @author au5tie
     */
    private final void registerCountdownDisplayToUser(GamePlayer player) {
        // Create the countdown display component.
        GamePlayerDisplayComponent displayComponent = createUserCountdownDisplay(player);

        if (displayComponent != null && displayComponent instanceof ArenaCountdownDisplayComponent) {
            // The display component is the right type, we can register it in good faith.
            player.getDisplayManager().registerComponent(displayComponent);
        } else {
            // The component does not inherit the ArenaCountdownDisplayComponent class, so we'd be unable to update it.
            throw new TobnetEngineException("The countdown display component provided to " + player + " must inherit from the ArenaCountdownDisplayComponent interface.");
        }
    }

    /**
     * Creates the {@link ArenaCountdownDisplayComponentBossBar} display component for the provided player. This is the
     * actual countdown display component which will get registered and rendered for the user display.
     *
     * @param player Player.
     * @return Countdown display component.
     * @author au5tie
     */
    private GamePlayerDisplayComponent createUserCountdownDisplay(GamePlayer player) {
        // Obtain the translated title.
        String title = TobnetGamePlugin.getMessageController().getMessage(MessageConstants.COUNTDOWN_DISPLAY_TITLE);

        return new ArenaCountdownDisplayComponentBossBar(getPlayerCountdownDisplayComponentName(), 2, player, title, BarColor.WHITE, BarStyle.SOLID, BarFlag.CREATE_FOG);
    }

    /**
     * Updates the countdown display component progress for the provided player. This will provide the player's specific
     * countdown display component with the updated countdown stats.
     *
     * @param player Game player.
     * @author au5tie
     */
    private final void updateUserCountdownDisplay(GamePlayer player) {

       Optional<GamePlayerDisplayComponent> component = player.getDisplayManager().getComponent(getPlayerCountdownDisplayComponentName());

       if (component.isPresent() && component.get() instanceof ArenaCountdownDisplayComponent) {

           ArenaCountdownDisplayComponent displayComponent = (ArenaCountdownDisplayComponent) component.get();

           // Update the progress of the display.
           displayComponent.updateProgress(getSecondsLeft(), getStartingSeconds());
       }
    }

    /**
     * Generates the console status lines pertaining the game status.
     *
     * @return Console status lines.
     * @author au5tie
     */
    @Override
    public List<String> getConsoleStatusLines(CommandSender sender) {

        return List.of(TobnetGamePlugin.getMessageController().getMessage(MessageConstants.CONSOLE_COUNTDOWN) +
                ": " + getSecondsLeft() + " " +
                TobnetGamePlugin.getMessageController().getMessage(MessageConstants.CONSOLE_SECONDS));
    }
}