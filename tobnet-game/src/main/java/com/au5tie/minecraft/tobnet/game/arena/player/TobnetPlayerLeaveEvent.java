package com.au5tie.minecraft.tobnet.game.arena.player;

import com.au5tie.minecraft.tobnet.game.arena.TobnetArena;
import com.au5tie.minecraft.tobnet.game.event.TobnetCustomEvent;
import com.au5tie.minecraft.tobnet.game.player.GamePlayer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * The Player Leave Event represents a player leaving the arena. The event itself does not specify the terms on which the
 * player left, whether they were kicked, quit, the game ended, etc.
 *
 * @author au5tie
 */
@AllArgsConstructor
@Builder
@Getter
public class TobnetPlayerLeaveEvent extends TobnetCustomEvent {

  private final TobnetArena arena;
  private final GamePlayer player;
}
