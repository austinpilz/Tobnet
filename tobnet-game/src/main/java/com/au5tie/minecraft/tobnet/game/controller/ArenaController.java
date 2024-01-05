package com.au5tie.minecraft.tobnet.game.controller;

import com.au5tie.minecraft.tobnet.game.arena.TobnetArena;
import com.au5tie.minecraft.tobnet.game.arena.manager.ArenaManagerUtils;
import com.au5tie.minecraft.tobnet.game.log.TobnetLogUtils;
import com.google.inject.Singleton;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import org.bukkit.Location;

@Singleton
public class ArenaController implements TobnetController {

  private final List<TobnetArena> arenas;

  public ArenaController() {
    this.arenas = new ArrayList<>();
  }

  @Override
  public void prepare() {}

  /**
   * Returns list of all registered {@link TobnetArena}.
   *
   * @return Registered Arenas.
   * @author au5tie
   */
  public List<TobnetArena> getArenas() {
    return Collections.unmodifiableList(arenas);
  }

  /**
   * Adds {@link TobnetArena} to the controller.
   *
   * @param arena Arena.
   * @author au5tie
   */
  public void registerArena(TobnetArena arena) {
    this.arenas.add(arena);
  }

  /**
   * Removes {@link TobnetArena} from the controller.
   *
   * @param arena Arena.
   * @author au5tie
   */
  public void removeArena(TobnetArena arena) {
    this.arenas.remove(arena);
  }

  /**
   * Returns the {@link TobnetArena} that encompasses the supplied {@link Location}.
   *
   * @param location Location.
   * @return Arena that contains location.
   * @author au5tie
   */
  public Optional<TobnetArena> getLocationArena(Location location) {
    return arenas
      .stream()
      .filter(arena -> arena.isLocationWithinArena(location))
      .findFirst();
  }

  /**
   * Obtains the {@link TobnetArena} that the supplied player is currently playing in.
   *
   * Note: This makes the assumption that the Arena has an ArenaPlayerManager registered, as they all should!
   *
   * @param uuid Player UUID.
   * @return The arena the player is in, if any.
   * @author au5tie
   */
  public Optional<TobnetArena> getPlayerArena(String uuid) {
    return arenas
      .stream()
      .filter(arena ->
        ArenaManagerUtils
          .getPlayerManager(arena)
          .orElseThrow(() ->
            new RuntimeException(
              "Arena " + arena + " does not have a registered Player Manager"
            )
          )
          .isPlaying(uuid)
      )
      .findFirst();
  }

  /**
   * Obtains an {@link TobnetArena} by the supplied arena name.
   *
   * @param arenaName Arena Name.
   * @return Arena, if one exists.
   * @author au5tie
   */
  public Optional<TobnetArena> getArenaByName(String arenaName) {
    return arenas
      .stream()
      .filter(arena -> arena.getName().equalsIgnoreCase(arenaName))
      .findFirst();
  }
}
