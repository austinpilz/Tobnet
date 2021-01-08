package com.au5tie.minecraft.tobnet.game.arena.countdown;

import lombok.AllArgsConstructor;

/**
 * The ArenaCountdownUpdate runnable is the task responsible for calling the {@link ArenaCountdownManager}'s heartbeat on
 * a recurring basis. This is how the countdown actually counts down.
 *
 * @author au5tie
 */
@AllArgsConstructor
public class ArenaCountdownUpdateTask implements Runnable {

    private final ArenaCountdownManager countdownManager;

    @Override
    public void run() {
        // Call the heartbeat, it will take care of doing the decrement.
        countdownManager.heartbeat();
    }
}
