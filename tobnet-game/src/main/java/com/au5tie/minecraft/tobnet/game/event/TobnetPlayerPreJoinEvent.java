package com.au5tie.minecraft.tobnet.game.event;

import com.au5tie.minecraft.tobnet.game.arena.TobnetArena;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.bukkit.entity.Player;

/**
 * The TobnetPlayerPreJoinEvent indicates the intent of a player to join a specific arena. The event is called right before
 * it admits the player into the arena for play. The event is cancellable, a cancelled event results in the player being
 * rejected from joining the arena.
 * @author au5tie
 */
@Getter
@AllArgsConstructor
public class TobnetPlayerPreJoinEvent extends TobnetCustomCancellableEvent {

    private final TobnetArena arena;
    private final Player player;
}
