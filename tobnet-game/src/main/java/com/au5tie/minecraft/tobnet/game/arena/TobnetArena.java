package com.au5tie.minecraft.tobnet.game.arena;

import com.au5tie.minecraft.tobnet.game.TobnetGamePlugin;
import com.au5tie.minecraft.tobnet.game.arena.convert.BaseArenaConverter;
import com.au5tie.minecraft.tobnet.game.arena.convert.TobnetArenaConverter;
import com.au5tie.minecraft.tobnet.game.arena.event.ArenaEventHandler;
import com.au5tie.minecraft.tobnet.game.arena.manager.ArenaManager;
import com.au5tie.minecraft.tobnet.game.arena.manager.ArenaManagerType;
import com.au5tie.minecraft.tobnet.game.exception.TobnetEngineException;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.*;

/**
 * The base Tobnet Arena representation. Each Tobnet Arena is a shell of a basic functioning arena,
 * complete with basic locations, statistics, etc. Each implementing game will require more advanced
 * and specific fields, which are handled
 *
 * Tobnet Arena -> Extending Arena. Extending Arena uses //TODO
 *
 */
@Data
@AllArgsConstructor
public abstract class TobnetArena {

    private String name;

    private Map map;

    // Arena Boundaries.
    private Location boundaryOne;
    private Location boundaryTwo;

    // Arena Managers.
    private Map<ArenaManagerType, ArenaManager> managers;
    private boolean managerRegistrationComplete;

    // Converter.
    private TobnetArenaConverter converter;

    /**
     * Arena instantiation is done via reflection.
     * @param name Arena Name.
     * @author au5tie
     */
    public TobnetArena(String name) {

        this.name = name;

        // By default, we use the base arena converter.
        converter = new BaseArenaConverter(this);

        // Manager preparation.
        managers = new HashMap<>();
        prepareArenaManagers();
    }

    /**
     * Prepares the arena's managers. This is where the implementing arena defines all of the managers which control the
     * functionality of the arena overall.
     *
     * @author au5tie
     */
    protected abstract void prepareManagers();

    /**
     * Returns all of the Arena's {@link ArenaManager}.
     *
     * The returned list is a new list which contains all of the Arena's {@link ArenaManager}. It is not
     * the Arena's actual list which manages them all, thus there is no risk for the caller to be able
     * to modify the collection integrity.
     *
     * @return Arena Managers.
     * @author Austin Pilz n0286596
     */
    public List<ArenaManager> getManagers() {

        return new ArrayList(managers.values());
    }

    /**
     * Obtains the {@link ArenaManager} of the supplied type.
     *
     * @param type Arena Manager Type.
     * @return Arena Manager of type.
     * @author au5tie
     */
    public Optional<ArenaManager> getManager(ArenaManagerType type) {

        return Optional.ofNullable(managers.get(type));
    }

    /**
     * Registers a new {@link ArenaManager}.
     *
     * @param manager Arena Manager.
     * @author au5tie
     */
    public void registerManager(ArenaManager manager) {

        if (!managerRegistrationComplete) {
            // Register the manager within the arena.
            managers.put(manager.getType(), manager);
        } else {
            // The manager registration period has ended.
            throw new TobnetEngineException("Arena Manager registration period for " + getName() + " has ended.");
        }
    }

    /**
     * Prepares the managers in the arena for gameplay. This will allow the implementing arena to handle the creation and
     * registration of their desired managers in the arena. Then this will handle registering all of the manager listeners
     * with the server API.
     *
     * @author Austin Pilz n0286596
     */
    private final void prepareArenaManagers() {
        // Allow implementing arena to register managers.
        prepareManagers();

        // Invoke each manager to prepare itself.
        getManagers().forEach(ArenaManager::prepareManager);

        // Register manager event listeners.
        registerManagerListeners();

        // Mark that registration is complete which will not allow any further managers to register.
        managerRegistrationComplete = true;

        // Notify all managers we've finished configuring them. This will allow them to dynamically link.
        getManagers().forEach(ArenaManager::afterArenaPreparationComplete);
    }

    /**
     * Registers all of the manager's event listeners to Bukkit so they'll receive event notifications as they occur directly
     * to their class.
     *
     * Note: There is no way to de-register events from the Bukkit event scheduler once they've been subscribed. This
     * introduces complexity in the scenario we want to remove an arena, as the garbage collection will be forced to keep
     * the arena and all of its components in memory.
     *
     * @author au5tie
     */
    private void registerManagerListeners() {
        // Compile all of the handlers registered by all of the managers.
        List<ArenaEventHandler> handlers = new ArrayList<>();
        getManagers().forEach(manager -> handlers.addAll(manager.getEventHandlers()));

        // Register each handler with the server.
        handlers.forEach(handler -> Bukkit.getServer()
                .getPluginManager()
                .registerEvents(handler, TobnetGamePlugin.getInstance()));
    }

    /**
     * Checks if supplied location is within the {@link TobnetArena} based on the boundaries.
     *
     * @param location Location.
     * @return If the {@link Location} is within the Arena.
     * @author au5tie
     */
    public boolean isLocationWithinArena(Location location) {
        double[] dim = new double[2];

        dim[0] = boundaryOne.getX();
        dim[1] = boundaryTwo.getX();
        Arrays.sort(dim);
        if(location.getX() > dim[1] || location.getX() < dim[0])
            return false;

        dim[0] = boundaryOne.getY();
        dim[1] = boundaryTwo.getY();
        Arrays.sort(dim);
        if(location.getY() > dim[1] || location.getY() < dim[0])
            return false;

        dim[0] = boundaryOne.getZ();
        dim[1] = boundaryTwo.getZ();
        Arrays.sort(dim);

        return !(location.getZ() > dim[1] || location.getZ() < dim[0]);
    }
}
