package com.au5tie.minecraft.tobnet.game.event;

import org.bukkit.event.Cancellable;

/**
 * A custom Tobnet engine event which can be cancelled by handlers after publishing.
 *
 * @author au5tie
 */
public abstract class TobnetCustomCancellableEvent extends TobnetCustomEvent implements Cancellable {

    private boolean cancelled;

    @Override
    public boolean isCancelled() {

        return cancelled;
    }

    @Override
    public void setCancelled(boolean isCancelled) {

        this.cancelled = isCancelled;
    }
}
