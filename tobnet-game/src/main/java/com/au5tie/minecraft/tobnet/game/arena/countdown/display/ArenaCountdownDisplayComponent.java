package com.au5tie.minecraft.tobnet.game.arena.countdown.display;

/**
 * The Arena Countdown Display Component represents any display of the countdown to the user during the arena countdown
 * phase, regardless of display location.
 *
 * @author au5tie
 */
public interface ArenaCountdownDisplayComponent {

    /**
     * Updates the countdown progress
     *
     * @param secondsLeft Seconds left in the countdown.
     * @param maxSeconds Maximum seconds left in the countdown.
     * @author au5tie
     */
    void updateProgress(int secondsLeft, int maxSeconds);
}
