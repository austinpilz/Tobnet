package com.au5tie.minecraft.tobnet.game.arena.chest;

import com.au5tie.minecraft.tobnet.game.arena.TobnetArena;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.bukkit.Location;
import org.bukkit.block.Chest;

@Data
@AllArgsConstructor
@Builder
public class ArenaChest {
    private final TobnetArena arena;
    private final ArenaChestType type;
    private final Location location;

    /**
     * Returns the {@link Chest} for the Arena Chest. This will convert the location of the chest into the Bukkit
     * representation.
     * @return Chest.
     * @author au5tie
     */
    private Chest getChest() {
        return (Chest)location.getBlock().getState();
    }
}
