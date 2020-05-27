package com.au5tie.minecraft.tobnet.core.player;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class GamePlayer {

    private String uuid;
    private String username;

    // Mode - Survivor, Killer, Spectator?

    // Spawn Preference.

    // Character?

    // Perks.

}
