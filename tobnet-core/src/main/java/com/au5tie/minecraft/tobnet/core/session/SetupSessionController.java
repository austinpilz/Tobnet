package com.au5tie.minecraft.tobnet.core.session;

import org.bukkit.entity.Player;

import java.util.*;

public abstract class SetupSessionController {

    private final Map<String, SetupSession> sessions;

    public SetupSessionController() {

        sessions = new HashMap<>();
    }

    /**
     * Registers a new {@link SetupSession} with the controller.
     * @param session Arena Setup Session.
     * @author au5tie
     */
    private void addSession(SetupSession session) {

        sessions.put(session.getPlayerUuid(), session);
    }

    /**
     * Re-registers a {@link SetupSession} for the supplied user.
     * @param playerUuid Player UUID.
     * @author au5tie
     */
    private void removeSession(String playerUuid) {

        sessions.remove(playerUuid);
    }

    /**
     * Returns all of the registered {@link SetupSession}.
     * @return Setup Sessions.
     * @author au5tie
     */
    protected List<SetupSession> getSessions() {

        return new ArrayList<>(sessions.values());
    }

    public void requestNewSession(Player player) {

        if (!doesUserHaveExistingSession(player.getUniqueId().toString())) {
            //
        } else {
            // The user already has an existing setup session in progress.

            // Terminate their existing in progress session.
            requestSessionTermination(player.getUniqueId().toString());

            // Begin the session request process again.
            requestNewSession(player);
        }
    }

    /**
     * Processes the request to terminate an existing {@link SetupSession}.
     * @param playerUuid Player UUID.
     * @author au5tie
     */
    public void requestSessionTermination(String playerUuid) {

        // TODO terminate the session?

        // De-register the session.
        removeSession(playerUuid);
    }

    public void sessionStepInvoke(String playerUuid) {
        //
    }

    /**
     * Returns if the supplied player already has an existing {@link SetupSession} ongoing.
     * @param playerUuid Player UUID.
     * @return If the player already has an existing setup session in progress.
     * @author au5tie
     */
    public boolean doesUserHaveExistingSession(String playerUuid) {

        return getUserSession(playerUuid).isPresent();
    }

    /**
     * Returns the existing {@link SetupSession} for the supplied player.
     * @param playerUuid Player UUID.
     * @return Arena Setup Session.
     * @author au5tie
     */
    private Optional<SetupSession> getUserSession(String playerUuid) {

        return Optional.ofNullable(sessions.get(playerUuid));
    }

    /*
    NEED to make setup session controller one generic place that routes all incoing things into each one.
     */
}
