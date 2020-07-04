package com.au5tie.mypetpie;

import com.au5tie.minecraft.tobnet.core.arena.Arena;
import com.au5tie.minecraft.tobnet.core.arena.manager.ArenaManagerType;
import com.au5tie.minecraft.tobnet.game.TobnetGamePlugin;
import com.au5tie.minecraft.tobnet.game.arena.chest.ArenaChestManager;
import com.au5tie.minecraft.tobnet.game.arena.spawn.ArenaSpawnLocationManager;
import com.au5tie.mypetpie.manager.PieManager;

public class ExampleTobnetPlugin extends TobnetGamePlugin {

    public void enablePlugin() {
        //

        testArenaConfig();

        getCommand("pie").setExecutor(new TempCommandHandler());
    }

    public void disablePlugin() {
        //
    }

    public void testArenaConfig() {

        Arena arena = new Arena("pie-factory");

        // Chest.
        ArenaChestManager chestManager = new ArenaChestManager(arena);
        arena.registerManager(chestManager);

        // Spawn Point.
        ArenaSpawnLocationManager locationManager = new ArenaSpawnLocationManager(arena);
        arena.registerManager(locationManager);

        // Custom Manager.
        PieManager pieManager = new PieManager(arena);
        arena.registerManager(pieManager);

        // Register the arena with the controller.
        TobnetGamePlugin.getArenaController().addArena(arena);

        // Get the chest controller and simulate a chest closed event.
        ArenaChestManager arenaChestManager = (ArenaChestManager)arena.getManager(ArenaManagerType.CHEST).get();
        arenaChestManager.getEventHandler().inventoryCloseEvent(null);
    }
}
