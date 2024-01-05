package com.au5tie.minecraft.tobnet.game.arena.game;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ArenaGameStatusHeartbeat implements Runnable {

  private final ArenaGameManager gameManager;

  @Override
  public void run() {
    gameManager.evaluateGameStatus();
  }
}
