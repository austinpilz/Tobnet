package com.au5tie.minecraft.tobnet.game.arena.chest;

import com.au5tie.minecraft.tobnet.game.arena.TobnetArena;
import com.au5tie.minecraft.tobnet.game.arena.manager.ArenaManager;
import com.au5tie.minecraft.tobnet.game.arena.manager.ArenaManagerType;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class ArenaChestManager extends ArenaManager {

    private final List<ArenaChest> chests;

    public ArenaChestManager(TobnetArena arena) {
        super(arena);

        chests = new ArrayList<>();
    }

    @Override
    public ArenaManagerType getType() {
        return ArenaManagerType.CHEST;
    }

    @Override
    public void prepareManager() {
        // Register event handler.
        registerEventHandler(new ArenaChestEventHandler(getArena(), this));
    }

    @Override
    public void destroyManager() {

    }

    /**
     * Adds {@link ArenaChest}.
     * @param chest Arena Chest.
     * @author au5tie.
     */
    public void addChest(ArenaChest chest) {
        this.chests.add(chest);
    }

    /**
     * Removes {@link ArenaChest}.
     * @param chest Arena Chest.
     * @author au5tie.
     */
    public void removeChest(ArenaChest chest) {
        this.chests.remove(chest);
    }

    /**
     * Obtains the {@link ArenaChest} at the provided {@link Location}.
     * @param location Location.
     * @return Arena Chest at provided Location.
     * @author au5tie.
     */
    public Optional<ArenaChest> getChestAtLocation(Location location) {
        return chests.stream()
                .filter(chest -> chest.getLocation().equals(location))
                .findFirst();
    }

    /**
     * Returns all {@link ArenaChest} that are of the provided {@link ArenaChestType}.
     * @param type Chest Type.
     * @return Chests of type.
     * @author au5tie.
     */
    public List<ArenaChest> getChestsOfType(ArenaChestType type) {
        return chests.stream()
                .filter(chest -> chest.getType().equals(type))
                .collect(Collectors.toList());
    }
}
