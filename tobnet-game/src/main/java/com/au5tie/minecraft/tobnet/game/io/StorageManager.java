package com.au5tie.minecraft.tobnet.game.io;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Storage Manager represents a manager of a specific type of element in regards to the external storage of the application.
 * @author au5tie
 */
@Getter
@AllArgsConstructor
public abstract class StorageManager {

    private final StorageManagerType type;
    private final ExternalStorage externalStorage;

    /**
     * Prepares the external storage tables for the manager. This is where the implementing storage manager will create
     * and/or update table schemas.
     * @author au5tie
     */
    public abstract void prepareTables();
}
