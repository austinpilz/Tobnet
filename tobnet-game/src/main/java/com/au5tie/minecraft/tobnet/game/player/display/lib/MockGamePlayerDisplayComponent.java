package com.au5tie.minecraft.tobnet.game.player.display.lib;

import com.au5tie.minecraft.tobnet.game.player.GamePlayer;
import com.au5tie.minecraft.tobnet.game.player.display.component.GamePlayerDisplayComponent;
import com.au5tie.minecraft.tobnet.game.player.display.component.GamePlayerDisplayComponentLocation;

/**
 * Mock/placeholder display component used for testing. This display component will not interact with nor display anything
 * to the provided player, but will instead take a place in the priority queue for verifying display correctness.
 *
 * @author au5tie
 */
public final class MockGamePlayerDisplayComponent
  extends GamePlayerDisplayComponent {

  public MockGamePlayerDisplayComponent(
    String name,
    int priority,
    GamePlayer player,
    GamePlayerDisplayComponentLocation location
  ) {
    super(name, priority, location, player);
  }

  @Override
  protected void display() {
    // Nothing for mock.
  }

  @Override
  protected void hide() {
    // Nothing for mock.
  }

  @Override
  protected void destroy() {
    // Nothing for mock.
  }
}
