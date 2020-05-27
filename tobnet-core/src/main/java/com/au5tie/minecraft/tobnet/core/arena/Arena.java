package com.au5tie.minecraft.tobnet.core.arena;

import com.au5tie.minecraft.tobnet.core.arena.manager.ArenaManager;
import com.au5tie.minecraft.tobnet.core.arena.manager.ArenaManagerType;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.collections4.CollectionUtils;
import org.bukkit.Location;

import java.util.*;

@Data
@AllArgsConstructor
public class Arena {

    private String name;

    private Map map;

    // Boundaries.
    private Location boundaryOne;
    private Location boundaryTwo;

    // Managers.
    private Map<ArenaManagerType, ArenaManager> managers;

    public Arena(String name) {

        this.name = name;
        managers = new HashMap<>();
    }

    public List<ArenaManager> getManagers() {

        return new ArrayList(managers.values());
    }

    public Optional<ArenaManager> getManager(ArenaManagerType type) {

        return Optional.ofNullable(managers.get(type));
    }

    public void registerManager(ArenaManager manager) {

        managers.put(manager.getType(), manager);
    }

    /**
     * Prepares all of the arena's managers.
     * @author au5tie.
     */
    private void prepareManagers() {

        if (CollectionUtils.isNotEmpty(managers.values())) {
            // Prepare each manager.
            managers.values().forEach(manager -> manager.prepareManager());
        }
    }

    /**
     * Checks if supplied location is within the {@link Arena} based on the boundaries.
     * @param location Location.
     * @return If the {@link Location} is within the Arena.
     * @author au5tie.
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
