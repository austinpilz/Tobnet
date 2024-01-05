package com.au5tie.minecraft.tobnet.game.arena.event;

import com.au5tie.minecraft.tobnet.game.arena.TobnetArena;
import com.au5tie.minecraft.tobnet.game.arena.manager.ArenaManager;
import lombok.Getter;
import org.bukkit.event.Listener;

/**
 * The Arena Event Handler is Tobnet's implementation of the server event Listener. When this class is extended and
 * registered with the server, it will be eligible to receive event notifications like a normal listener.
 *
 * @author au5tie
 */
@Getter
public class ArenaEventHandler implements Listener {

  private final TobnetArena arena;
  private final ArenaManager manager;

  public ArenaEventHandler(TobnetArena arena, ArenaManager manager) {
    this.arena = arena;
    this.manager = manager;
  }
}
