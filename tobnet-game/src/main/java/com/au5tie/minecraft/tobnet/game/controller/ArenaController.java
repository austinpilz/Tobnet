package com.au5tie.minecraft.tobnet.game.controller;

import com.au5tie.minecraft.tobnet.core.arena.Arena;
import com.au5tie.minecraft.tobnet.game.arena.util.ArenaManagerUtils;
import com.au5tie.minecraft.tobnet.game.session.arena.ArenaSetupSessionController;
import com.au5tie.minecraft.tobnet.game.util.TobnetLogUtils;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ArenaController {

    private final List<Arena> arenas;
    private final ArenaSetupSessionController setupSessionController;

    public ArenaController() {

        this.arenas = new ArrayList<>();
        this.setupSessionController = new ArenaSetupSessionController();
    }

    /**
     * Returns list of all registered {@link Arena}.
     * @return Registered Arenas.
     * @author au5tie
     */
    public List<Arena> getArenas() {

        return new ArrayList<>(arenas);
    }

    /**
     * Adds {@link Arena} to the controller.
     * @param arena Arena.
     * @author au5tie
     */
    public void addArena(Arena arena) {

        this.arenas.add(arena);

        TobnetLogUtils.info("Arena Controller >> addArena() >> Registered arena " + arena.getName());
    }

    /**
     * Removes {@link Arena} from the controller.
     * @param arena Arena.
     * @author au5tie
     */
    public void removeArena(Arena arena) {

        this.arenas.remove(arena);
    }

    /**
     * Returns the {@link Arena} that encompasses the supplied {@link Location}.
     * @param location Location.
     * @return Arena that contains location.
     * @author au5tie
     */
    public Optional<Arena> getLocationArena(Location location) {

        return arenas.stream()
                .filter(arena -> arena.isLocationWithinArena(location))
                .findFirst();
    }

    /**
     * Obtains the {@link Arena} that the supplied player is currently playing in.
     *
     * Note: This makes the assumption that the Arena has an ArenaPlayerManager registered, as they all should!
     *
     * @param uuid Player UUID.
     * @return The arena the player is in, if any.
     * @author au5tie
     */
    public Optional<Arena> getPlayerArena(String uuid) {

        return arenas.stream()
                .filter(arena -> ArenaManagerUtils.getPlayerManager(arena)
                        .orElseThrow(() -> new RuntimeException("Arena " + arena + " does not have a registered Player Manager"))
                        .isPlaying(uuid))
                .findFirst();
    }

    /**
     * Returns the {@link ArenaSetupSessionController}.
     * @return Arena Setup Session Controller.
     * @author au5tie
     */
    public ArenaSetupSessionController getSetupSessionController() {

        return setupSessionController;
    }
}
