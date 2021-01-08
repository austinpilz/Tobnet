package com.au5tie.minecraft.tobnet.game.arena.countdown;

import com.au5tie.minecraft.tobnet.game.arena.TobnetArena;
import com.au5tie.minecraft.tobnet.game.arena.chat.ArenaChatManager;
import com.au5tie.minecraft.tobnet.game.arena.game.ArenaGameStatus;
import com.au5tie.minecraft.tobnet.game.arena.game.TobnetArenaStatusPostChangeEvent;
import com.au5tie.minecraft.tobnet.game.arena.game.TobnetArenaStatusPreChangeEvent;
import com.au5tie.minecraft.tobnet.game.arena.handler.ArenaEventHandler;
import com.au5tie.minecraft.tobnet.game.arena.manager.ArenaManagerType;
import com.au5tie.minecraft.tobnet.game.arena.manager.ArenaManagerUtils;
import com.au5tie.minecraft.tobnet.game.time.TobnetTimeUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;

/**
 * The ArenaCountdownEventHandler is responsible for responding to events which alter the countdown within the arena. This
 * primarily listens to the game status transition events to detect when the game overall enters and exits the countdown
 * status. We kick off and kill the countdown depending on when the game manager actually changes the arena status.
 *
 * Countdown is a special state which alters the users ability, display, etc. drastically from other states. Pay close
 * attention to the documentation on the two transition event listeners below, it's important the order in which we start
 * /stop the countdown pre/post transition.
 *
 * @author au5tie
 */
public class ArenaCountdownEventHandler extends ArenaEventHandler {

    private final ArenaCountdownManager countdownManager;

    public ArenaCountdownEventHandler(TobnetArena arena, ArenaCountdownManager manager) {
        super(arena, manager);

        countdownManager = manager;
    }

    /**
     * On the pre-transition event, we will handle stopping the countdown when we are transitioning out of countdown. This
     * allows us to tidy up the countdown so that when the actual transition event goes out and the display listeners
     * change things over, we'll have already cleaned our stuff up here.
     *
     * @param event Status Pre-Change Event.
     * @author au5tie
     */
    @EventHandler(priority = EventPriority.HIGH, ignoreCancelled = true)
    public void gameStatusPreTransition(TobnetArenaStatusPreChangeEvent event) {

        if (getArena().equals(event.getArena())) {
            if (ArenaGameStatus.COUNTDOWN.equals(event.getPriorStatus()) && ArenaGameStatus.IN_PROGRESS.equals(event.getNewStatus())) {
                // The countdown has finished and the game has begun.
                countdownManager.completeCountdown();
            } else if (ArenaGameStatus.COUNTDOWN.equals(event.getPriorStatus()) && !ArenaGameStatus.COUNTDOWN.equals(event.getNewStatus())) {
                // Stop the countdown as we're transitioning to a status that isn't in progress. Could be back to waiting if
                // req are no longer met for the game to begin.
                countdownManager.stopCountdown();
            }
        }
    }

    /**
     * On the post-transition event where we are entering the countdown we will actually begin the physical countdown. We
     * want to do this post-transition to allow any other displays to finish their cleanup prior to us beginning the
     * countdown which drastically affects user display.
     *
     * @param event Status Post-Change Event.
     * @author au5tie
     */
    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void gameStatusPostTransition(TobnetArenaStatusPostChangeEvent event) {

        if (getArena().equals(event.getArena())) {
            if (ArenaGameStatus.WAITING.equals(event.getPriorStatus()) && ArenaGameStatus.COUNTDOWN.equals(event.getNewStatus())) {
                // Begin the countdown.
                countdownManager.beginCountdown();
            }
        }
    }

    /**
     * This is responsible for sending the chat messages to all playing players within the arena whenever the countdown
     * seconds reaches certain criteria. This will refer to the countdown configuration to determine if the implementing
     * plugin wants us to display the countdown in chat.
     *
     * @param event Countdown decremented event.
     * @author au5tie
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void countdownChatMessageSender(TobnetArenaCountdownDecrementedEvent event) {

        if (getArena().equals(event.getArena()) && countdownManager.getConfiguration().isDisplayChatIntervals()) {

            ArenaChatManager chatManager = (ArenaChatManager) ArenaManagerUtils.getManagerOfType(getArena(), ArenaManagerType.CHAT).get();

            if ((event.getNewValue() <= 15 && event.getNewValue() > 5 && event.getNewValue() % 5 == 0) || (event.getNewValue() <= 5 && event.getNewValue() > 0)) {
                // Send a message to all players in the arena.
                chatManager.sendMessageToAllPlayers("Game begins in " + TobnetTimeUtils.secondsToDuration(event.getNewValue()));
            }
        }
    }
}
