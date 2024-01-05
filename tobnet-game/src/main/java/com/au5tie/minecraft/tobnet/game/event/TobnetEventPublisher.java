package com.au5tie.minecraft.tobnet.game.event;

import org.bukkit.Bukkit;
import org.bukkit.event.Event;

/**
 * The TobnetEventPublisher handles the publishing of all Tobnet custom events to the server event distributor.
 *
 * @author au5tie
 */
public class TobnetEventPublisher {

  /**
   * Publishes event for distribution.
   *
   * @param event Event.
   * @author au5tie
   */
  public static void publishEvent(Event event) {
    Bukkit.getServer().getPluginManager().callEvent(event);
  }
}
