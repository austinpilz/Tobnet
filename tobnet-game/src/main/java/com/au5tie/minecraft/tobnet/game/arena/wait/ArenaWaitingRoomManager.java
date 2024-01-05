package com.au5tie.minecraft.tobnet.game.arena.wait;

import com.au5tie.minecraft.tobnet.game.arena.TobnetArena;
import com.au5tie.minecraft.tobnet.game.arena.manager.ArenaManager;
import com.au5tie.minecraft.tobnet.game.arena.manager.ArenaManagerType;
import lombok.Getter;

@Getter
public class ArenaWaitingRoomManager extends ArenaManager {

  private final ArenaWaitingRoomConfiguration configuration;

  public ArenaWaitingRoomManager(
    TobnetArena arena,
    ArenaWaitingRoomConfiguration configuration
  ) {
    super(arena);
    this.configuration = configuration;
  }

  @Override
  public ArenaManagerType getType() {
    return ArenaManagerType.WAITING_ROOM;
  }

  @Override
  public void prepareManager() {
    //
  }

  @Override
  public void destroyManager() {
    //
  }
}
