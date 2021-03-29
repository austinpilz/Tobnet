package com.au5tie.minecraft.tobnet.game.arena.location;

import com.au5tie.minecraft.tobnet.game.arena.TobnetArena;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bukkit.Location;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ArenaLocation {

    private TobnetArena arena;
    private String name;
    private String type;
    private Location location;
    private Map<String, String> metadata;
}
