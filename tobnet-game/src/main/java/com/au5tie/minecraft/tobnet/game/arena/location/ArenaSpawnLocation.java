package com.au5tie.minecraft.tobnet.game.arena.location;

import com.au5tie.minecraft.tobnet.game.arena.TobnetArena;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.bukkit.Location;

/**
 * Spawn Location is a location within an arena where players begin the game. The location has a type which dictates
 * which kind of players are eligible to spawn at the location.
 * @author au5tie.
 */
@Data
@AllArgsConstructor
@ToString
public class ArenaSpawnLocation {
    private TobnetArena arena;
    private ArenaSpawnLocationType type;
    private Location location;
}
