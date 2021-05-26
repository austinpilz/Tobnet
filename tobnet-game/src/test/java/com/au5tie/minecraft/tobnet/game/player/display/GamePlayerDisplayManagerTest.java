package com.au5tie.minecraft.tobnet.game.player.display;

import com.au5tie.minecraft.tobnet.game.player.GamePlayer;
import com.au5tie.minecraft.tobnet.game.player.display.component.GamePlayerDisplayComponentLocation;
import com.au5tie.minecraft.tobnet.game.player.display.exception.DuplicatePlayerDisplayComponentException;
import com.au5tie.minecraft.tobnet.game.player.display.lib.MockGamePlayerDisplayComponent;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class GamePlayerDisplayManagerTest {

    private GamePlayerDisplayManager displayManager;

    @Mock
    private GamePlayer player;

    @BeforeEach
    void setup() {

        this.displayManager = new GamePlayerDisplayManager(player);
    }

    @DisplayName("Basic component registration")
    @Test
    void shouldTestBasicComponentRegistration() {

        MockGamePlayerDisplayComponent component = new MockGamePlayerDisplayComponent("display1", 5, player, GamePlayerDisplayComponentLocation.BOSS_BAR);

        // Register the component to the display manager.
        displayManager.registerComponent(component);

        // Verify that the display manager has our component registered.
        assertTrue(displayManager.doesHaveComponent(component.getName()));
        assertTrue(displayManager.getComponent(component.getName()).isPresent());
        assertEquals(component, displayManager.getComponent(component.getName()).orElse(null));
    }

    @DisplayName("Duplicate registration of same component")
    @Test
    void shouldTestDuplicateComponentRegistration() {

        MockGamePlayerDisplayComponent component = new MockGamePlayerDisplayComponent("display1", 1, player, GamePlayerDisplayComponentLocation.BOSS_BAR);
        MockGamePlayerDisplayComponent differentComponent = new MockGamePlayerDisplayComponent("display1", 5, player, GamePlayerDisplayComponentLocation.BOSS_BAR);

        try {
            // Register the same component twice which is not an issue.
            displayManager.registerComponent(component);
            displayManager.registerComponent(component);

            // Unregister the first component to allow for error free registration of the second.
            displayManager.unregisterComponent(component);
            displayManager.registerComponent(differentComponent);
        } catch (DuplicatePlayerDisplayComponentException exception) {
            fail("The same unique component was registered twice which should not throw an error. The error is only applicable if two different components with the same name are registered.");
        }
    }

    @DisplayName("Duplicate registration of same name but different components")
    @Test()
    void shouldTestDuplicateComponentRegistrationDifferentUnique() {

        MockGamePlayerDisplayComponent component = new MockGamePlayerDisplayComponent("display1", 1, player, GamePlayerDisplayComponentLocation.BOSS_BAR);
        MockGamePlayerDisplayComponent differentComponent = new MockGamePlayerDisplayComponent("display1", 5, player, GamePlayerDisplayComponentLocation.BOSS_BAR);

        displayManager.registerComponent(component);

        Assertions.assertThrows(DuplicatePlayerDisplayComponentException.class, () -> {
            // Enforce that we get the exception since it's 2 diff components with same name.
            displayManager.registerComponent(differentComponent);
        });
    }

    @DisplayName("Component display prioritization - Same Location")
    @Test
    void shouldTestComponentDisplaySameLocation() {

        MockGamePlayerDisplayComponent highestPriorityComponent = new MockGamePlayerDisplayComponent("display1", 1, player, GamePlayerDisplayComponentLocation.BOSS_BAR);
        displayManager.registerComponent(highestPriorityComponent);

        MockGamePlayerDisplayComponent mediumPriorityComponent = new MockGamePlayerDisplayComponent("display2", 5, player, GamePlayerDisplayComponentLocation.BOSS_BAR);
        displayManager.registerComponent(mediumPriorityComponent);

        MockGamePlayerDisplayComponent secondMediumPriorityComponent = new MockGamePlayerDisplayComponent("display3", 5, player, GamePlayerDisplayComponentLocation.BOSS_BAR);
        displayManager.registerComponent(secondMediumPriorityComponent);

        // Request the display of our lower priority component. It should be displayed.
        displayManager.requestComponentDisplay(mediumPriorityComponent.getName());
        assertTrue(mediumPriorityComponent.isShouldBeVisible());
        assertTrue(mediumPriorityComponent.isVisible());

        // Request that the higher priority one is displayed which should auto hide any lower priority.
        displayManager.requestComponentDisplay(highestPriorityComponent.getName());
        assertTrue(highestPriorityComponent.isShouldBeVisible());
        assertTrue(highestPriorityComponent.isVisible());
        assertFalse(mediumPriorityComponent.isVisible());

        // In this scenario, both should be in the status "should be visible" even though only highest priority actually is.
        assertTrue(mediumPriorityComponent.isShouldBeVisible());

        // Verify that if we have two of same priority, they won't replace one another. FCFS.
        displayManager.requestComponentHide(highestPriorityComponent.getName());
        displayManager.requestComponentDisplay(secondMediumPriorityComponent.getName());
        assertTrue(secondMediumPriorityComponent.isShouldBeVisible());
        assertFalse(secondMediumPriorityComponent.isVisible());
        assertTrue(mediumPriorityComponent.isVisible());

        // Hide the original medium display component and verify we display the equal priority one.
        displayManager.requestComponentHide(mediumPriorityComponent.getName());
        assertFalse(mediumPriorityComponent.isShouldBeVisible());
        assertFalse(mediumPriorityComponent.isVisible());
        assertTrue(secondMediumPriorityComponent.isVisible());
    }

    @DisplayName("Component display prioritization - Different Locations")
    @Test
    void shouldTestComponentDisplayDifferentLocations() {

        MockGamePlayerDisplayComponent bossBarHighestPriorityComponent = new MockGamePlayerDisplayComponent("display1", 1, player, GamePlayerDisplayComponentLocation.BOSS_BAR);
        displayManager.registerComponent(bossBarHighestPriorityComponent);
        displayManager.requestComponentDisplay(bossBarHighestPriorityComponent.getName());

        MockGamePlayerDisplayComponent bossBarLowestPriorityComponent = new MockGamePlayerDisplayComponent("display2", 5, player, GamePlayerDisplayComponentLocation.BOSS_BAR);
        displayManager.registerComponent(bossBarLowestPriorityComponent);
        displayManager.requestComponentDisplay(bossBarLowestPriorityComponent.getName());

        assertTrue(bossBarHighestPriorityComponent.isVisible());
        assertFalse(bossBarLowestPriorityComponent.isVisible());

        MockGamePlayerDisplayComponent actionBarHighestPriorityComponent = new MockGamePlayerDisplayComponent("display3", 1, player, GamePlayerDisplayComponentLocation.ACTION_BAR);
        displayManager.registerComponent(actionBarHighestPriorityComponent);
        displayManager.requestComponentDisplay(actionBarHighestPriorityComponent.getName());

        // Verify that the two components are both visible, despite same priority, since they're in diff locations.
        assertTrue(actionBarHighestPriorityComponent.isVisible());
        assertTrue(bossBarHighestPriorityComponent.isVisible());
    }
}