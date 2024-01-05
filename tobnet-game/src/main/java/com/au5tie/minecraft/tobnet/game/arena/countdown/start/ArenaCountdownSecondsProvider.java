package com.au5tie.minecraft.tobnet.game.arena.countdown.start;

import com.au5tie.minecraft.tobnet.game.arena.TobnetArena;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * The ArenaCountdownSecondsProvider acts as the provider for the number of seconds that the countdown will start with
 * and countdown from. This allows for out of the box implementations as well as custom solutions. Using this approach
 * allows for easy solutions like a static X seconds all the way to dynamic number of seconds depending on factors like
 * the number of players.
 *
 * @author au5tie
 */
@AllArgsConstructor
@Getter
public abstract class ArenaCountdownSecondsProvider {

  private final ArenaCountdownSecondsMode mode;
  private final TobnetArena arena;

  /**
   * Determines the starting number of seconds that the countdown should count down from.
   *
   * @return Countdown starting seconds.
   * @author au5tie
   */
  public abstract int getStartingSeconds();
}
