package com.au5tie.minecraft.tobnet.game.event;

import com.au5tie.minecraft.tobnet.core.arena.Arena;
import com.au5tie.minecraft.tobnet.game.arena.chest.ArenaChestManager;
import com.au5tie.minecraft.tobnet.game.arena.util.ArenaManagerUtils;
import com.au5tie.minecraft.tobnet.game.controller.ArenaController;
import lombok.AllArgsConstructor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;

import java.util.Optional;

@AllArgsConstructor
public class GlobalInventoryListener implements Listener {

    private final ArenaController arenaController;

    /**
     * 1 - Tons of global event listeners which route to arena manager event handler
     * 2 - Arena Event Controller which performs reflections to find events
     * 3 - Each Manager Event Handler registers itself as a listener. Let Bukkit do the hard work.
     */

    /**
     * Handles the event when a chest inventory is opened.
     * @param event Inventory Open Event.
     * @author au5tie.
     */
    @EventHandler(priority = EventPriority.HIGH)
    public void onChestInventoryOpenEvent(InventoryOpenEvent event) {

        if (event.getInventory().getType().equals(InventoryType.CHEST)) {
            // Chest Inventory Open Event.
            Optional<Arena> arena = arenaController.getPlayerArena(event.getPlayer().getUniqueId().toString());

            // The player is playing in an arena, route the event to that arena's chest event handler.
            if (arena.isPresent()) {
                // Attempt to obtain the Chest Manager for the arena, if one is registered.
                Optional<ArenaChestManager> chestManager = ArenaManagerUtils.getChestManager(arena.get());

                // Route the event to the manager if it exists.
                chestManager.ifPresent(manager -> manager.getEventHandler().inventoryOpenEvent(event));
            }
        }
    }

    /**
     * Handles the event when a chest inventory is opened.
     * @param event Inventory Open Event.
     * @author au5tie.
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onChestInventoryCloseEvent(InventoryCloseEvent event) {

        if (event.getInventory().getType().equals(InventoryType.CHEST)) {

            // Chest Inventory Open Event.
            Optional<Arena> arena = arenaController.getPlayerArena(event.getPlayer().getUniqueId().toString());

            // The player is playing in an arena, route the event to that arena's chest event handler.
            if (arena.isPresent()) {
                // Attempt to obtain the Chest Manager for the arena, if one is registered.
                Optional<ArenaChestManager> chestManager = ArenaManagerUtils.getChestManager(arena.get());

                // Route the event to the manager if it exists.
                chestManager.ifPresent(manager -> manager.getEventHandler().inventoryCloseEvent(event));
            }
        }
    }
}
