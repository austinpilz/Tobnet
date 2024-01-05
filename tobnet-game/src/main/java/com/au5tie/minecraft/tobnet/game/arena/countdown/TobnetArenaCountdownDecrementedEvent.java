package com.au5tie.minecraft.tobnet.game.arena.countdown;

import com.au5tie.minecraft.tobnet.game.arena.TobnetArena;
import com.au5tie.minecraft.tobnet.game.event.TobnetCustomEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * The TobnetArenaCountdownDecremented is called when one second being decremented from the countdown timer. This event
 * allows interested parties to subscribe to being notified when the countdown value changes, rather than polling the value
 * continuously.
 *
 * @author au5tie
 */
@Getter
@AllArgsConstructor
public class TobnetArenaCountdownDecrementedEvent extends TobnetCustomEvent {

  private final TobnetArena arena;
  private final ArenaCountdownManager countdownManager;
  private final int oldValue;
  private final int newValue;
}
