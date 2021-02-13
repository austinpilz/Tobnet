package com.au5tie.minecraft.tobnet.game.display.component;

import com.au5tie.minecraft.tobnet.game.player.GamePlayer;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 * A Game Player Display Component is a render-able visual component on a player's screen. It is specifically for a single
 * player.
 *
 * The location attribute dictates where on the screen the component is rendered. It's entirely a reporting field, the
 * actual component can render wherever it feels. The priority dictates the layering of the component. If there are two
 * or more components active and registered for the same rendering location, the priority will dictate which component/
 * layer gets put in the foreground. The higher the priority (the lower the number) is the closer to the foreground
 * that it will be rendered.
 *
 * @author au5tie
 */
@RequiredArgsConstructor
@Data
public abstract class GamePlayerDisplayComponent {

    private final String name;
    private final int priority;
    private final GamePlayerDisplayComponentLocation location;
    private final GamePlayer player;

    private boolean shouldBeVisible;
    private boolean isVisible;

    /**
     * Performs the component tasks required to render and display the component to the user.
     *
     * @author au5tie
     */
    protected abstract void display();

    /**
     * Performs the component tasks required to hide the component from the user.
     *
     * @author au5tie
     */
    protected abstract void hide();

    /**
     * Performs component cleanup when it is no longer needed.
     *
     * @author au5tie
     */
    protected abstract void destroy();

    /**
     * Requests the component be displayed to the player. This will invoke the component's display() method which will
     * perform the necessary tasks to render and generate the display to the user.
     *
     * @author au5tie
     */
    public void displayComponent() {
        // Displays the component to the player.
        display();

        // Marks the display as visible for management purposes.
        setVisible(true);
    }

    /**
     * Requests the component to be hidden from the player. This will invoke the component's hide() method which will
     * perform the necessary tasks to hide the component from player view.
     *
     * @author au5tie
     */
    public void hideComponent() {
        // Hides the component from the player.
        hide();

        // Marks that the display is no longer visible to the user.
        setVisible(false);
    }

    /**
     * Prepares the component for and destroys the component. This will hide the component from view and perform all tasks
     * to destroy it.
     *
     * @author au5tie
     */
    public void destroyComponent() {
        // Hide the component from the screen.
        hideComponent();

        // Mark that it should not be visible for future prioritization since we're de_stroying it.
        setShouldBeVisible(false);

        // Hand off to the implementing component for destroy actions.
        destroy();
    }

    /**
     * Returns if the component should be visible to the user.
     *
     * @return If the component should be visible to the user.
     * @author au5tie
     */
    public boolean isShouldBeVisible() {

        return shouldBeVisible;
    }

    /**
     * Sets if the component should be visible to the user.
     *
     * @param shouldBeVisible If the component should be visible to the user.
     * @author au5tie
     */
    public void setShouldBeVisible(boolean shouldBeVisible) {

        this.shouldBeVisible = shouldBeVisible;
    }

    /**
     * Returns if the component is currently visible to the user.
     *
     * @return If the component is currently visible to the user.
     * @author au5tie
     */
    public boolean isVisible() {

        return isVisible;
    }

    /**
     * Sets if the component is currently visible to the user.
     *
     * @param isVisible If the component is currently visible to the user.
     * @author au5tie
     */
    private void setVisible(boolean isVisible) {

        this.isVisible = isVisible;
    }

    /**
     * Returns if the current component is a higher priority than the provided component.
     *
     * @param component Component in question.
     * @return If the current component is higher priority than the provided component.
     * @author au5tie
     */
    public boolean isHigherPriorityThan(GamePlayerDisplayComponent component) {

        return getPriority() > component.getPriority();
    }

    /**
     * Returns if the current component is a lower priority than the provided component.
     *
     * @param component Component in question.
     * @return If the current component is lower priority than the provided component.
     * @author au5tie
     */
    public boolean isLowerPriorityThan(GamePlayerDisplayComponent component) {

        return getPriority() < component.getPriority();
    }
}
