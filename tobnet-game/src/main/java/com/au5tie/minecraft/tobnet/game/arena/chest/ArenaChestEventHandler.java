package com.au5tie.minecraft.tobnet.game.arena.chest;

import com.au5tie.minecraft.tobnet.game.arena.TobnetArena;
import com.au5tie.minecraft.tobnet.game.arena.handler.ArenaEventHandler;
import com.au5tie.minecraft.tobnet.game.arena.manager.ArenaManagerUtils;
import com.au5tie.minecraft.tobnet.game.util.TobnetLogUtils;
import org.bukkit.Location;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.inventory.Inventory;

import java.util.Optional;

public class ArenaChestEventHandler extends ArenaEventHandler {

    private ArenaChestManager chestManager;

    public ArenaChestEventHandler(TobnetArena arena, ArenaChestManager manager) {

        super(arena, manager);

        chestManager = manager;
    }

    @EventHandler(priority = EventPriority.NORMAL)
    public void inventoryOpenEvent(InventoryOpenEvent event) {

       if (ArenaManagerUtils.getPlayerManager(getArena()).get().isPlaying(event.getPlayer().getUniqueId().toString())) {
           // Registered Chest Check.
           validateUnregisteredChestOpen(event);

           Optional<ArenaChest> chest = chestManager.getChestAtLocation(event.getInventory().getLocation());

           if (chest.isPresent()) {
               // The chest is registered.

               // Validate Concurrent Opens.
               validateMultipleChestOpens(chest.get(), event);
           } else {
               // The chest is unregistered.

               // Validate ability to open unregistered chests.
               validateUnregisteredChestOpen(event);
           }
       }
    }

    @EventHandler(priority = EventPriority.LOW)
    public void inventoryCloseEvent(InventoryCloseEvent event) {


        TobnetLogUtils.getLogger().warning("CHEST CLOSED");
    }

    /**
     * Determines if the location of the inventory being opened is for one of the chests registered to the manager.
     * @param location Location.
     * @return If the chest location is a registered {@link ArenaChest}.
     * @author au5tie.
     */
    private boolean isChestRegistered(Location location) {
        return chestManager.getChestAtLocation(location).isPresent();
    }

    /**
     * Determines if the inventory is currently opened by someone other than the player attempting to open it.
     * @param inventory Inventory.
     * @param player Player.
     * @return If the inventory is already opened by another player.
     * @author au5tie.
     */
    private boolean isInventoryAlreadyOpenedByOthers(Inventory inventory, HumanEntity player) {
        return inventory.getViewers().stream()
                .filter(viewer -> !viewer.getUniqueId().equals(player.getUniqueId()))
                .count() > 0;
    }

    private void validateUnregisteredChestOpen(InventoryOpenEvent event) {

        if (!isChestRegistered(event.getInventory().getLocation())) {
            // The chest is not registered.
            event.setCancelled(true);

            // Log that we denied the chest open because it is not registered.
            TobnetLogUtils.warn("Player " + event.getPlayer().getName() + " attempted to open a chest while playing" +
                    " in arena " + getArena().getName() + " which was unregistered. " + event.getInventory().getLocation().toString());
        }
    }

    private void validateMultipleChestOpens(ArenaChest chest, InventoryOpenEvent event) {

        if (isInventoryAlreadyOpenedByOthers(event.getInventory(), event.getPlayer())) {
            // The inventory is already opened by another player.
            event.setCancelled(true);

            // Notify the user why we canceled the event.
            event.getPlayer().sendMessage("");

            return;
        }
    }
}
