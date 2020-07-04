package com.au5tie.minecraft.tobnet.game.arena.util;

import com.au5tie.minecraft.tobnet.core.arena.Arena;
import com.au5tie.minecraft.tobnet.core.arena.manager.ArenaManager;
import com.au5tie.minecraft.tobnet.core.arena.manager.ArenaManagerType;
import com.au5tie.minecraft.tobnet.game.arena.chest.ArenaChestManager;
import com.au5tie.minecraft.tobnet.game.arena.player.ArenaPlayerManager;

import java.util.Optional;

public class ArenaManagerUtils {

    /**
     * Obtains the {@link ArenaManager} of the provided {@link ArenaManagerType} within the supplied {@link Arena}.
     * @param arena Arena.
     * @param type Arena Manager Type.
     * @return Arena Manager of type.
     * @author au5tie
     */
    public static Optional<ArenaManager> getManagerOfType(Arena arena, ArenaManagerType type) {

        return arena.getManagers().stream()
                .filter(manager -> manager.getType().equals(type))
                .findFirst();
    }

    /**
     * Obtains the {@link ArenaChestManager} within the supplied {@link Arena}.
     * @param arena Arena.
     * @return Arena Chest Manager, if registered.
     * @author au5tie
     */
    public static Optional<ArenaChestManager> getChestManager(Arena arena) {

        Optional<ArenaManager> playerManager = getManagerOfType(arena, ArenaManagerType.CHEST);

        if (playerManager.isPresent()) {
            return Optional.of((ArenaChestManager) playerManager.get());
        } else {
            return Optional.empty();
        }
    }

    /**
     * Obtains the {@link ArenaPlayerManager} within the supplied {@link Arena}.
     * @param arena Arena.
     * @return Arena Player Manager, if registered.
     * @author au5tie
     */
    public static Optional<ArenaPlayerManager> getPlayerManager(Arena arena) {

        Optional<ArenaManager> playerManager = getManagerOfType(arena, ArenaManagerType.PLAYER);

        if (playerManager.isPresent()) {
            return Optional.of((ArenaPlayerManager)playerManager.get());
        } else {
            return Optional.empty();
        }
    }
}