package com.au5tie.minecraft.tobnet.core.arena.sign;

import com.au5tie.minecraft.tobnet.core.arena.Arena;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.bukkit.Location;

@Data
@AllArgsConstructor
@Builder
public class ArenaSign {
    private final Arena arena;
    private final ArenaSignType type;
    private final Location location;
}
