package com.au5tie.minecraft.tobnet.game.arena.player;

import com.au5tie.minecraft.tobnet.game.arena.TobnetArena;
import com.au5tie.minecraft.tobnet.game.event.TobnetCustomEvent;
import com.au5tie.minecraft.tobnet.game.player.GamePlayer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * The Player Post Join Event notifies listeners that a player has completed joining the arena game. This event is published
 * once the player has been fully admitted into the arena.
 *
 * @author au5tie
 */
@AllArgsConstructor
@Builder
@Getter
public class TobnetPlayerPostJoinEvent extends TobnetCustomEvent {

  private final TobnetArena arena;
  private final GamePlayer player;
}
