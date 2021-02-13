package com.au5tie.minecraft.tobnet.game.player;

import com.au5tie.minecraft.tobnet.game.arena.TobnetArena;
import com.au5tie.minecraft.tobnet.game.player.display.GamePlayerDisplayManager;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.bukkit.entity.Player;

@Data
@AllArgsConstructor
@Builder
public class GamePlayer {

    private final TobnetArena arena;
    private final String uuid;
    private final String username;
    private final Player player;
    private final GamePlayerDisplayManager displayManager;

    public GamePlayer(TobnetArena arena, Player player) {
        this.arena = arena;
        this.uuid = player.getUniqueId().toString();
        this.username = player.getDisplayName();
        this.player = player;
        this.displayManager = new GamePlayerDisplayManager(this);
    }

    // Mode - Survivor, Killer, Spectator?

    // Spawn Preference.

    // Character?

    // Perks.

    // TODO Every player has display update task which handles update of display for them
}
