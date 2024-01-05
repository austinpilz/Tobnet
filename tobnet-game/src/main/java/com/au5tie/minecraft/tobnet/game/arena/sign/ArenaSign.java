package com.au5tie.minecraft.tobnet.game.arena.sign;

import com.au5tie.minecraft.tobnet.game.arena.TobnetArena;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.bukkit.Location;

@Data
@AllArgsConstructor
@Builder
public class ArenaSign {

  private final TobnetArena arena;
  private final ArenaSignType type;
  private final Location location;
}
