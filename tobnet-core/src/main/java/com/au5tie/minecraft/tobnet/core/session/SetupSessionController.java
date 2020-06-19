package com.au5tie.minecraft.tobnet.core.session;

import org.bukkit.entity.Player;

import java.util.*;

public abstract class SetupSessionController {

    private final Map<String, SetupSession> sessions;

    public SetupSessionController() {

        sessions = new HashMap<>();
    }

    /**
     * Begins a new {@link SetupSession} for the requested player. This method should create the new session in the
     * implemeing class, creating the SetupSession of the generic type.
     * @param player Player.
     * @return Setup Session.
     * @author au5tie
     */
    abstract SetupSession prepareNewSession(Player player);

    /**
     * Registers a new {@link SetupSession} with the controller.
     * @param session Arena Setup Session.
     * @author au5tie
     */
    private final void addSession(SetupSession session) {

        sessions.put(session.getPlayerUuid(), session);
    }

    /**
     * Re-registers a {@link SetupSession} for the supplied user.
     * @param playerUuid Player UUID.
     * @author au5tie
     */
    private final void removeSession(String playerUuid) {

        sessions.remove(playerUuid);
    }

    /**
     * Returns all of the registered {@link SetupSession}.
     * @return Setup Sessions.
     * @author au5tie
     */
    protected final List<SetupSession> getSessions() {

        return new ArrayList<>(sessions.values());
    }

    /**
     * Requests a new {@link SetupSession} be created and prepared for the requesting user. This will create the new
     * session, prepare it, and register it with the controller.
     * @param player Player.
     * @return Setup Session.
     * @author au5tie
     */
    public SetupSession requestNewSession(Player player) {

        if (!doesUserHaveExistingSession(player.getUniqueId().toString())) {
            // Begin the new session.
            return beginNewSession(player);
        } else {
            // The user already has an existing setup session in progress. Terminate their existing in progress session.
            requestSessionTermination(player.getUniqueId().toString());

            // Begin the session request process again.
            return requestNewSession(player);
        }
    }

    /**
     * Begins a new {@link SetupSession} for the requesting user. This will allow the implementing controller to take care
     * of creating the specific session and then this will register it with the controller.
     * @param player Player.
     * @return Setup Session.
     * @author au5tie
     */
    private SetupSession beginNewSession(Player player) {

        // Have the implementing controller prepare the specific session.
        SetupSession session = prepareNewSession(player);

        // Register the new session with the controller.
        addSession(session);

        return session;
    }

    /**
     * Processes the request to terminate an existing {@link SetupSession}.
     * @param playerUuid Player UUID.
     * @author au5tie
     */
    public boolean requestSessionTermination(String playerUuid) {

        // Locate the session in progress.
        Optional<SetupSession> session = getUserSession(playerUuid);

        if (session.isPresent()) {
            // Notify the session we're terminating it to allow cleanup.
            session.get().onSessionTerminate();

            // De-register the session with the controller.
            removeSession(playerUuid);

            return true;
        } else {
            // The user did not have an existing setup session in progress.
            return false;
        }
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
    public final boolean doesUserHaveExistingSession(String playerUuid) {

        return getUserSession(playerUuid).isPresent();
    }

    /**
     * Returns the existing {@link SetupSession} for the supplied player.
     * @param playerUuid Player UUID.
     * @return Arena Setup Session.
     * @author au5tie
     */
    private final Optional<SetupSession> getUserSession(String playerUuid) {

        return Optional.ofNullable(sessions.get(playerUuid));
    }
}
