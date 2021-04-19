package com.au5tie.minecraft.tobnet.game.event;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.jetbrains.annotations.NotNull;

/**
 * A custom Tobnet engine event which can be published through the Bukkit/Spigot server.
 *
 * @author au5tie
 */
public abstract class TobnetCustomEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    @Override
    public @NotNull HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
