package com.au5tie.minecraft.tobnet.game.player.display;

import com.au5tie.minecraft.tobnet.game.player.GamePlayer;
import com.au5tie.minecraft.tobnet.game.player.display.component.GamePlayerDisplayComponent;
import com.au5tie.minecraft.tobnet.game.player.display.component.GamePlayerDisplayComponentLocation;
import org.apache.commons.collections4.CollectionUtils;

import java.util.*;
import java.util.stream.Collectors;

/**
 * The Game Player Display Manager handles a player's entire display. It controls which elements are displayed
 * to the user in various locations on the screen. Specifically it handles the automatic prioritization of the display
 * components. Different code areas can request that components be displayed, but this manager is ultimately in charge
 * of which one is displayed based on the highest priority. It will take care of performing re-prioritization of the
 * display
 *
 * @author au5tie
 */
public class GamePlayerDisplayManager {

    private final GamePlayer player;
    private final Map<String, GamePlayerDisplayComponent> displayComponents;

    public GamePlayerDisplayManager(GamePlayer player) {

        this.player = player;
        this.displayComponents = new HashMap<>();
    }

    /**
     * Returns the {@link GamePlayer} that the manager belongs to.
     *
     * @return Game Player that the manager belongs to.
     * @author au5tie
     */
    private final GamePlayer getPlayer() {

        return player;
    }

    /**
     * Adds new {@link GamePlayerDisplayComponent} to the manager.
     *
     * @param component Game Player Display Component.
     * @author au5tie
     */
    private final void addComponent(GamePlayerDisplayComponent component) {

        displayComponents.put(component.getName(), component);
    }

    /**
     * Removes {@link GamePlayerDisplayComponent} from the manager.
     *
     * @param component Game Player Display Component.
     * @author au5tie
     */
    private final void removeComponent(GamePlayerDisplayComponent component) {

        displayComponents.remove(component.getName());
    }

    /**
     * Destroys all currently registered components.
     *
     * @author au5tie
     */
    public void destroyComponents() {
        // Destroy all of the components.
        displayComponents.values().forEach(GamePlayerDisplayComponent::destroyComponent);
        displayComponents.clear();
    }

    /**
     * Determines if there is a registered {@link GamePlayerDisplayComponent} with the provided name.
     *
     * @param name Component name.
     * @return If component with provided name is registered.
     * @author au5tie
     */
    public final boolean doesHaveComponent(String name) {

        return displayComponents.containsKey(name);
    }

    /**
     * Obtains {@link GamePlayerDisplayComponent} for the provided name.
     *
     * @param name Name of the display component.
     * @return GamePlayerDisplayComponent with provided name.
     * @author au5tie
     */
    public final Optional<GamePlayerDisplayComponent> getComponent(String name) {

        return Optional.ofNullable(displayComponents.get(name));
    }

    /**
     * Registers new display component with the manager.
     *
     * @param component Display component.
     * @author au5tie
     */
    public void registerComponent(GamePlayerDisplayComponent component) {
        // Add the component to the manager.
        addComponent(component);
    }

    /**
     * Requests that the display component be displayed directly to the user. This will mark that the component should be
     * visible if priority allows. If the priority allows, the component will be displayed and any lower priority components
     * in the same location hidden.
     *
     * @param name Display component name.
     * @return If the requested component is now currently visible by the player.
     * @author au5tie
     */
    public boolean requestComponentDisplay(String name) {

        // Obtain the component based on the name provided to us.
        Optional<GamePlayerDisplayComponent> component = getComponent(name);

        if (component.isPresent()) {
            // Mark that the component should be visible when priority allows.
            component.get().setShouldBeVisible(true);

            // Perform visibility prioritization now that we've marked that this one should be displayed.
            performLocationPrioritization(component.get().getLocation());

            // Return if the component is now the one actually visible.
            return component.get().isVisible();
        }

        return false;
    }

    /**
     * Requests that the component be hidden from user view. This will immediately hide the component from user view and
     * then perform prioritization on the location to see if a component should replace it in view.
     *
     * @param name Display component name.
     * @return If the component was hidden from user view.
     * @author au5tie
     */
    public boolean requestComponentHide(String name) {
        // Obtain the component based on the name provided to us.
        Optional<GamePlayerDisplayComponent> component = getComponent(name);

        if (component.isPresent()) {
            // Mark that the component should be visible when priority allows.
            component.get().setShouldBeVisible(false);

            // Actually hide the component since hiding should be immediate.
            component.get().hideComponent();

            // Perform visibility prioritization to see if something should take our place.
            performLocationPrioritization(component.get().getLocation());

            return true;
        }

        return false;
    }

    /**
     * Performs prioritization of the display components within the provided location. This will evaluate all of the
     * registered components for the location and determine which one is the highest priority and should be displayed,
     * if any. It will take care of swapping out displayed components that are not the highest priority.
     *
     * @param location Display location.
     * @author au5tie
     */
    private void performLocationPrioritization(GamePlayerDisplayComponentLocation location) {
        // Determine which component is currently visible to the user.
        Optional<GamePlayerDisplayComponent> currentlyVisibleComponent = getCurrentlyVisibleComponentByLocation(location);

        // Find all components for this location which should be visible to the user.
        List<GamePlayerDisplayComponent> shouldBeVisibleComponents = getShouldBeVisibleComponentsByLocation(location);

        if (currentlyVisibleComponent.isPresent()) {
            // Remove the currently visible one so we don't compare against it.
            shouldBeVisibleComponents.remove(currentlyVisibleComponent);
        }

        // Determine the highest priority component which should be in view.
        GamePlayerDisplayComponent highestPriorityComponent = getHighestPriorityComponent(shouldBeVisibleComponents);

        if (currentlyVisibleComponent.isPresent()) {
            // Check to see if the one visible is the highest priority.
            if (highestPriorityComponent != null && highestPriorityComponent.isHigherPriorityThan(currentlyVisibleComponent.get())) {
                // The currently visible one is not the highest priority, we gotta switch 'em out.
                currentlyVisibleComponent.get().hideComponent();

                // Show the one that is higher priority.
                highestPriorityComponent.displayComponent();
            }
        } else if (highestPriorityComponent != null) {
            // There is no currently visible component to the user but we have one that should be.
            highestPriorityComponent.displayComponent();
        }
    }

    /**
     * Obtains the currently visible component in a particular location to the user. This will find a component that not
     * only should be displayed, but is THE currently displayed component, if any.
     *
     * @param location Display location.
     * @return The currently visible component in the specified location, if any.
     * @author au5tie
     */
    private Optional<GamePlayerDisplayComponent> getCurrentlyVisibleComponentByLocation(GamePlayerDisplayComponentLocation location) {

        return displayComponents.values().stream()
                .filter(component -> location.equals(component.getLocation()))
                .filter(component -> component.isVisible() && component.isShouldBeVisible())
                .findFirst();
    }

    /**
     * Obtains all of the components in a particular location that should be to the user, but may not necessarily be visible
     * due to priority. This is inclusive of the currently visible component, if any.
     *
     * @param location Display location.
     * @return All components that should be visible in the specified location.
     * @author au5tie
     */
    private List<GamePlayerDisplayComponent> getShouldBeVisibleComponentsByLocation(GamePlayerDisplayComponentLocation location) {

        return displayComponents.values().stream()
                .filter(component -> location.equals(component.getLocation()))
                .filter(component -> component.isShouldBeVisible())
                .collect(Collectors.toList());
    }

    /**
     * Obtains all registered components based on the location they are displayed in.
     *
     * @param location Game Player Display Component Location.
     * @return All components displayed in the provided location.
     * @author au5tie
     */
    private List<GamePlayerDisplayComponent> getComponentsByLocation(GamePlayerDisplayComponentLocation location) {

        return displayComponents.values().stream()
                .filter(component -> location.equals(component.getLocation()))
                .collect(Collectors.toList());
    }

    /**
     * Obtains the highest priority component of the provided list.
     *
     * @param components Highest priority component.
     * @return Highest priority component.
     * @author au5tie
     */
    private GamePlayerDisplayComponent getHighestPriorityComponent(List<GamePlayerDisplayComponent> components) {

        if (CollectionUtils.isNotEmpty(components)) {
            // Sort the components by priority.
            components.sort(Comparator.comparing(GamePlayerDisplayComponent::getPriority));

            // Return top element which is highest priority (lowest number)
            return components.get(0);
        }

        return null;
    }
}
