package com.au5tie.minecraft.tobnet.game.util;

import com.au5tie.minecraft.tobnet.game.player.GamePlayer;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.collections4.CollectionUtils;
import org.bukkit.entity.Player;

public class TobnetPlayerUtils {

  /**
   * Converts Tobnet {@link GamePlayer} to Bukkit's {@link Player}.
   *
   * @param players List of Tobnet players.
   * @return List of Bukkit players.
   * @author au5tie
   */
  public static List<Player> gamePlayerToBukkitPlayerList(
    List<GamePlayer> players
  ) {
    if (CollectionUtils.isNotEmpty(players)) {
      return players
        .stream()
        .map(GamePlayer::getPlayer)
        .collect(Collectors.toList());
    } else {
      return Collections.emptyList();
    }
  }

  /**
   * Converts Tobnet {@link GamePlayer} to Bukkit's {@link Player}.
   *
   * @param players List of Tobnet players.
   * @return Set of Bukkit players.
   * @author au5tie
   */
  public static Set<Player> gamePlayerToBukkitPlayerSet(
    List<GamePlayer> players
  ) {
    if (CollectionUtils.isNotEmpty(players)) {
      return players
        .stream()
        .map(GamePlayer::getPlayer)
        .collect(Collectors.toSet());
    } else {
      return Collections.emptySet();
    }
  }
}
