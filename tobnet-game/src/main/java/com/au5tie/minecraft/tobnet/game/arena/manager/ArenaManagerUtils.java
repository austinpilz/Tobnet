package com.au5tie.minecraft.tobnet.game.arena.manager;

import com.au5tie.minecraft.tobnet.game.arena.TobnetArena;
import com.au5tie.minecraft.tobnet.game.arena.game.ArenaGameManager;
import com.au5tie.minecraft.tobnet.game.arena.player.ArenaPlayerManager;

import java.util.Optional;

public class ArenaManagerUtils {

    /**
     * Obtains the {@link ArenaManager} of the provided {@link ArenaManagerType} within the supplied {@link TobnetArena}.
     * @param arena Arena.
     * @param type Arena Manager Type.
     * @return Arena Manager of type.
     * @author au5tie
     */
    public static Optional<ArenaManager> getManagerOfType(TobnetArena arena, ArenaManagerType type) {

        return arena.getManagers().stream()
                .filter(manager -> manager.getType().equals(type))
                .findFirst();
    }

    /**
     * Obtains the {@link CustomArenaManager} of the provided custom type, if one exists.
     * @param arena Arena.
     * @param customType Custom Arena Manager Type.
     * @return Custom Arena Manager.
     * @author au5tie
     */
    public static Optional<CustomArenaManager> getCustomManager(TobnetArena arena, String customType) {

        return arena.getManagers().stream()
                .filter(manager -> manager.getType().equals(ArenaManagerType.CUSTOM))
                .map(CustomArenaManager.class::cast)
                .filter(manager -> manager.getCustomType().equalsIgnoreCase(customType))
                .findFirst();
    }

    /**
     * Obtains the {@link ArenaGameManager} within the supplied {@link TobnetArena}.
     * @param arena Arena.
     * @return Arena Game Manager, if registered.
     * @author au5tie
     */
    public static Optional<ArenaGameManager> getGameManager(TobnetArena arena) {

        Optional<ArenaManager> gameManager = getManagerOfType(arena, ArenaManagerType.GAME);

        if (gameManager.isPresent()) {
            return Optional.of((ArenaGameManager) gameManager.get());
        } else {
            return Optional.empty();
        }
    }

    /**
     * Obtains the {@link ArenaPlayerManager} within the supplied {@link TobnetArena}.
     * @param arena Arena.
     * @return Arena Player Manager, if registered.
     * @author au5tie
     */
    public static Optional<ArenaPlayerManager> getPlayerManager(TobnetArena arena) {

        Optional<ArenaManager> playerManager = getManagerOfType(arena, ArenaManagerType.PLAYER);

        if (playerManager.isPresent()) {
            return Optional.of((ArenaPlayerManager)playerManager.get());
        } else {
            return Optional.empty();
        }
    }
}
