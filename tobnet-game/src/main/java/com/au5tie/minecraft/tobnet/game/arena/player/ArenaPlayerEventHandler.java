package com.au5tie.minecraft.tobnet.game.arena.player;

import com.au5tie.minecraft.tobnet.game.arena.handler.ArenaEventHandler;
import com.au5tie.minecraft.tobnet.game.player.GamePlayer;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityRegainHealthEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.*;

public class ArenaPlayerEventHandler extends ArenaEventHandler {

    private final ArenaPlayerManager playerManager;

    public ArenaPlayerEventHandler(ArenaPlayerManager playerManager) {

        super(playerManager.getArena(), playerManager);

        this.playerManager = playerManager;
    }

    @EventHandler(priority = EventPriority.MONITOR, ignoreCancelled = true)
    public void onPlayerJoin(PlayerJoinEvent event) {
        // TODO Account for players re-joining after quitting, which means they couldn't be TP'd
        // out of the arena. We need to see if they're in an arena when not registered as a player,
        // which we should send them to return point then. or spawn. idk.
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerQuit(PlayerQuitEvent event) {

        if (playerManager.isPlaying(event.getPlayer().getUniqueId().toString())) {
            GamePlayer gamePlayer = playerManager.getGamePlayer(event.getPlayer().getUniqueId().toString());

            playerManager.leaveGame(gamePlayer);
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerDeath(PlayerDeathEvent event) {
        //
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        //
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerInteract(PlayerInteractEvent event) {
        //
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerMove(PlayerMoveEvent event) {
        //
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onHungerDeplete(FoodLevelChangeEvent event) {
        //
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerCommandPreprocess(PlayerCommandPreprocessEvent event) {
        //
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void onPlayerDamage(EntityDamageEvent event) {
        //
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onHealthRegen(EntityRegainHealthEvent event) {
        //
    }

    @EventHandler(priority = EventPriority.NORMAL, ignoreCancelled = true)
    public void onItemDrop(PlayerDropItemEvent event) {
        //
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerMessage(AsyncPlayerChatEvent event) {
        //
    }
}