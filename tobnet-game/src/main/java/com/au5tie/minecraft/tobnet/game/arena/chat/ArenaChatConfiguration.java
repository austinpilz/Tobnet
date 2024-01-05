package com.au5tie.minecraft.tobnet.game.arena.chat;

import com.au5tie.minecraft.tobnet.game.arena.configuration.ArenaConfiguration;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ArenaChatConfiguration extends ArenaConfiguration {

  private boolean announcePlayerJoin = true;
  private boolean announcePlayerLeave = true;
}
