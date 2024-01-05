package com.au5tie.minecraft.tobnet.game.arena.player;

import com.au5tie.minecraft.tobnet.game.arena.TobnetArena;
import com.au5tie.minecraft.tobnet.game.event.TobnetCustomCancellableEvent;
import com.au5tie.minecraft.tobnet.game.player.GamePlayer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * The Player Pre-Joint event signifies an arenas intent to add a player to the arena game. This gives listeners the
 * opportunity to prepare for the player to join. It is cancellable to allow for listeners to prevent the player from
 * joining the arena, if desired.
 *
 * @author au5tie
 */
@AllArgsConstructor
@Builder
@Getter
public class TobnetPlayerPreJoinEvent extends TobnetCustomCancellableEvent {

  private final TobnetArena arena;
  private final GamePlayer player;
}
