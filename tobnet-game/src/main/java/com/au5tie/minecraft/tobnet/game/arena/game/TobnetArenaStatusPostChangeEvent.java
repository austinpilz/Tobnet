package com.au5tie.minecraft.tobnet.game.arena.game;

import com.au5tie.minecraft.tobnet.game.arena.TobnetArena;
import com.au5tie.minecraft.tobnet.game.event.TobnetCustomEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * The TobnetArenaStatusChangeEvent represents once the arena's game status has been successfully transitioned. The event
 * will only be published after the status has been successfully changed.
 *
 * @author au5tie
 */
@Getter
@AllArgsConstructor
public class TobnetArenaStatusPostChangeEvent extends TobnetCustomEvent {

  private final TobnetArena arena;
  private final ArenaGameStatus priorStatus;
  private final ArenaGameStatus newStatus;
}
