package com.au5tie.minecraft.tobnet.game.io;

import com.au5tie.minecraft.tobnet.game.log.TobnetLogUtils;
import com.au5tie.minecraft.tobnet.game.time.TimeDifference;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

/**
 * Storage Manager represents a manager of a specific type of element in regards to the external storage of the application.
 *
 * @author au5tie
 */
@Getter
@AllArgsConstructor
public abstract class StorageManager {

    private final StorageManagerType type;

    @Getter(AccessLevel.PROTECTED)
    private final TobnetStorageController tobnetStorageController;

    /**
     * Prepares the external storage tables for the manager. This is where the implementing storage manager will create
     * and/or update table schemas.
     *
     * @author au5tie
     */
    public abstract void prepareTables();

    /**
     * Performs the loading of the manager's specific data type from the data source. This will load the data and populate
     * it in the necessary locations.
     *
     * @author au5tie
     */
    protected abstract void loadData();

    /**
     * Begins the data load process for the manager. This will kick off the loading of the manager's specific data elements
     * from storage into memory, logging the performance.
     *
     * @author au5tie
     */
    public final void performDataLoad() {

        TobnetLogUtils.performance(this.getClass().getSimpleName() + " Data load initiated.");

        LocalDateTime loadStart = LocalDateTime.now();

        // Kick off the loading of the actual data.
        loadData();

        // Log our performance.
        TobnetLogUtils.performance(this.getClass().getSimpleName() + " Data load took " + new TimeDifference(loadStart, LocalDateTime.now()));
    }
}
