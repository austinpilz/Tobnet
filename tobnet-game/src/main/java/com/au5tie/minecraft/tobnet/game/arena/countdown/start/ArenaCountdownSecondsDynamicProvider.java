package com.au5tie.minecraft.tobnet.game.arena.countdown.start;

import com.au5tie.minecraft.tobnet.game.arena.TobnetArena;
import com.au5tie.minecraft.tobnet.game.arena.manager.ArenaManagerType;
import com.au5tie.minecraft.tobnet.game.arena.manager.ArenaManagerUtils;
import com.au5tie.minecraft.tobnet.game.arena.player.ArenaPlayerManager;

/**
 * The static Arena Countdown Seconds Provider provides a countdown value of X seconds per player in the lobby.
 *
 * @author au5tie
 */
public class ArenaCountdownSecondsDynamicProvider extends ArenaCountdownSecondsProvider {

    private final int seconds;

    public ArenaCountdownSecondsDynamicProvider (TobnetArena arena, int seconds) {

        super(ArenaCountdownSecondsMode.CUSTOM, arena);

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

        ArenaPlayerManager playerManager = (ArenaPlayerManager) ArenaManagerUtils.getManagerOfType(getArena(), ArenaManagerType.PLAYER).get();

        return seconds * playerManager.getNumberOfPlayers();
    }
}
