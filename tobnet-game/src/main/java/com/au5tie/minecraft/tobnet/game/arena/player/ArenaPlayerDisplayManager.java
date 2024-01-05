package com.au5tie.minecraft.tobnet.game.arena.player;

import com.au5tie.minecraft.tobnet.game.arena.TobnetArena;
import com.au5tie.minecraft.tobnet.game.arena.manager.ArenaManager;
import com.au5tie.minecraft.tobnet.game.arena.manager.ArenaManagerType;
import com.au5tie.minecraft.tobnet.game.arena.manager.ArenaManagerUtils;
import com.au5tie.minecraft.tobnet.game.exception.TobnetEngineException;
import com.au5tie.minecraft.tobnet.game.player.GamePlayer;
import java.util.List;
import java.util.stream.Collectors;
import lombok.Getter;

@Getter
public class ArenaPlayerDisplayManager extends ArenaManager {

  private final TobnetArena arena;
  private ArenaPlayerManager playerManager;

  public ArenaPlayerDisplayManager(TobnetArena arena) {
    super(arena);
    this.arena = arena;
  }

  @Override
  public ArenaManagerType getType() {
    return ArenaManagerType.PLAYER_DISPLAY;
  }

  @Override
  public void prepareManager() {
    //
  }

  @Override
  public void destroyManager() {
    //
  }

  @Override
  public void afterArenaPreparationComplete() {
    // Link to the player manager.
    playerManager =
      (ArenaPlayerManager) ArenaManagerUtils
        .getManagerOfType(getArena(), ArenaManagerType.PLAYER)
        .orElseThrow(TobnetEngineException::new);
  }

  /**
   * Determines which players are missing a display component with the provided name.
   *
   * @param componentName Display component name.
   * @return Players missing the display component with the provided name.
   * @author au5tie
   */
  public List<GamePlayer> getPlayersMissingComponent(String componentName) {
    return getPlayerManager()
      .getPlayers()
      .stream()
      .filter(player ->
        !player.getDisplayManager().doesHaveComponent(componentName)
      )
      .collect(Collectors.toList());
  }

  public void test() {
    getPlayerManager()
      .getPlayers()
      .forEach(player -> player.getDisplayManager());
  }
}
