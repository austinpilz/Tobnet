package com.au5tie.minecraft.tobnet.game.arena.location;

import org.apache.commons.collections4.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ArenaLocationUtils {

    /**
     * Filters the provided list of locations to ones of the provided location type.
     *
     * @param type Location type desired.
     * @param locations Locations.
     * @return Locations of provided location type.
     * @author au5tie
     */
    public static List<ArenaLocation> filterLocationsByType(String type, List<ArenaLocation> locations) {

        if (CollectionUtils.isNotEmpty(locations)) {

            return locations.stream()
                    .filter(location -> type.equalsIgnoreCase(location.getType()))
                    .collect(Collectors.toList());
        } else {
            return Collections.emptyList();
        }
    }
}
