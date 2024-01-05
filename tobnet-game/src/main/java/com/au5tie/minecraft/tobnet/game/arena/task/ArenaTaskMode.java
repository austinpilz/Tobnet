package com.au5tie.minecraft.tobnet.game.arena.task;

/**
 * The ArenaTaskMode specifies the mode in which {@link ArenaTask}'s are scheduled to run with the scheduler.
 * @author au5tie
 */
public enum ArenaTaskMode {
  SYNC,

  @Deprecated
  ASYNC,
}
