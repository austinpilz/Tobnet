package com.au5tie.minecraft.tobnet.game.arena.countdown.start;


import com.au5tie.minecraft.tobnet.game.arena.TobnetArena;

/**
 * The static Arena Countdown Seconds Provider provides a static number of seconds as specified in the Arena Countdown
 * Configuration.
 *
 * @author au5tie
 */
public class ArenaCountdownSecondsStaticProvider extends ArenaCountdownSecondsProvider {

    private final int seconds;

    public ArenaCountdownSecondsStaticProvider (TobnetArena arena, int seconds) {

        super(ArenaCountdownSecondsMode.STATIC, arena);

        this.seconds = seconds;
    }

    /**
     * Determines the starting number of seconds that the countdown should count down from.
     *
     * @return Countdown starting seconds.
     * @author au5tie
     */
    @Override
    public int getStartingSeconds() {

        return seconds;
    }
}
