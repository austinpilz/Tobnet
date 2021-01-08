package com.au5tie.minecraft.tobnet.game.arena.countdown;

import com.au5tie.minecraft.tobnet.game.arena.configuration.ArenaConfiguration;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ArenaCountdownConfiguration extends ArenaConfiguration {

    private boolean displayChatIntervals = true;
}
