package com.au5tie.minecraft.tobnet.game.arena.player;

import com.au5tie.minecraft.tobnet.game.arena.TobnetArena;
import com.au5tie.minecraft.tobnet.game.event.TobnetCustomEvent;
import com.au5tie.minecraft.tobnet.game.player.GamePlayer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class TobnetPlayerLeaveEvent extends TobnetCustomEvent {

    private final TobnetArena arena;
    private final GamePlayer player;
}
