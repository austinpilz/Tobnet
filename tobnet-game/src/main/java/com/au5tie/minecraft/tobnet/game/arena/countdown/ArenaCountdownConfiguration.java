package com.au5tie.minecraft.tobnet.game.arena.countdown;

import com.au5tie.minecraft.tobnet.game.arena.configuration.ArenaConfiguration;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class ArenaCountdownConfiguration extends ArenaConfiguration {

  private boolean displayChatIntervals = true;
  private boolean displayCountdownUI = true;
}
