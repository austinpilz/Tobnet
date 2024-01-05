package com.au5tie.minecraft.tobnet.game.arena.game;

import com.au5tie.minecraft.tobnet.game.arena.TobnetArena;
import com.au5tie.minecraft.tobnet.game.event.TobnetCustomEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * The TobnetArenaStatusChangeEvent represents the intent of the arena's game status to be transitioned to a different
 * state.
 *
 * @author au5tie
 */
@Getter
@AllArgsConstructor
public class TobnetArenaStatusPreChangeEvent extends TobnetCustomEvent {

  private final TobnetArena arena;
  private final ArenaGameStatus priorStatus;
  private final ArenaGameStatus newStatus;
}
