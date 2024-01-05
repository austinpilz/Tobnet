package com.au5tie.minecraft.tobnet.game.arena.spawn;

import com.au5tie.minecraft.tobnet.game.arena.TobnetArena;
import com.au5tie.minecraft.tobnet.game.arena.location.ArenaLocation;
import lombok.ToString;
import org.bukkit.Location;

/**
 * Spawn Location is a location within an arena where players begin the game. The location has a type which dictates
 * which kind of players are eligible to spawn at the location.
 *
 * @author au5tie.
 */
@ToString
public class ArenaSpawnLocation extends ArenaLocation {

  private ArenaSpawnLocationType type;

  public ArenaSpawnLocation(
    TobnetArena arena,
    String name,
    String type,
    Location location
  ) {
    super(arena, name, type, location);
  }
}
