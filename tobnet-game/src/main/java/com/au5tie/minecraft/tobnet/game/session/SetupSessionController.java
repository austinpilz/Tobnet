package com.au5tie.minecraft.tobnet.game.session;

import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.*;

/**
 * The Setup Session Controller is an abstract controller which manages all of the {@link SetupSession}'s of the implementing
 * type. This class will manage keeping track of the session, creating new ones, and routing user interactions to the right
 * session.
 *
 * @author au5tie
 */
@Getter
public abstract class SetupSessionController {

    private final String sessionType;
    private final Map<String, SetupSession> sessions;
    private final SetupSessionCommandListener commandListener;

    public SetupSessionController(String sessionType) {

        sessions = new HashMap<>();

        // Define the type of session this controller manages.
        this.sessionType = sessionType;

        // Command Listener.
        commandListener = new SetupSessionCommandListener(this);
        //TobnetGamePlugin.getCommandController().registerCommandLister(commandListener);
    }

    /**
     * Begins a new {@link SetupSession} for the requested player. This method should create the new session in the
     * implementing class, creating the SetupSession of the generic type.
     *
     * @param player Player.
     * @return Setup Session.
     * @author au5tie
     */
    protected abstract SetupSession prepareNewSession(Player player);

    /**
     * Registers a new {@link SetupSession} with the controller.
     *
     * @param session Arena Setup Session.
     * @author au5tie
     */
    private final void addSession(SetupSession session) {

        sessions.put(session.getPlayerUuid(), session);
    }

    /**
     * Re-registers a {@link SetupSession} for the supplied user.
     *
     * @param playerUuid Player UUID.
     * @author au5tie
     */
    private final void removeSession(String playerUuid) {

        sessions.remove(playerUuid);
    }

    /**
     * Returns all of the registered {@link SetupSession}.
     *
     * @return Setup Sessions.
     * @author au5tie
     */
    protected final List<SetupSession> getSessions() {

        return new ArrayList<>(sessions.values());
    }

    /**
     * Requests a new {@link SetupSession} be created and prepared for the requesting user. This will create the new
     * session, prepare it, and register it with the controller.
     *
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
     *
     * @param player Player.
     * @return Setup Session.
     * @author au5tie
     */
    private SetupSession beginNewSession(Player player) {

        // Have the implementing controller prepare the specific session.
        SetupSession session = prepareNewSession(player);

        // Register the new session with the controller.
        addSession(session);

        // Build the initial context to kick off the session.
        SetupSessionStepInvocationContext context = SetupSessionStepInvocationContext.builder()
                .player(player)
                .build();

        // Auto notify the session we're getting started.
        session.onSessionBegin(context);

        return session;
    }

    /**
     * Processes the request to terminate an existing {@link SetupSession}.
     *
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

    /**
     * Requests the invocation of the next {@link SetupSessionStep} on user input/interaction. This will pass in context
     * which contains player interaction information which will get routed to the next step to be invoked.
     *
     * @param playerUuid Player UUID.
     * @param context Setup Session Step Invocation Context.
     * @author au5tie
     */
    public void sessionStepInvoke(String playerUuid, SetupSessionStepInvocationContext context) {
        // Locate the session in progress.
        Optional<SetupSession> session = getUserSession(playerUuid);

        if (session.isPresent()) {
            // The session has been found, let it know we want to invoke the next step.
            session.get().invokeStep(context);

            if (session.get().isComplete()) {
                // De-register the session with the controller.
                removeSession(playerUuid);
            }
        }
    }

    /**
     * Returns if the supplied player already has an existing {@link SetupSession} ongoing.
     *
     * @param playerUuid Player UUID.
     * @return If the player already has an existing setup session in progress.
     * @author au5tie
     */
    public final boolean doesUserHaveExistingSession(String playerUuid) {

        return getUserSession(playerUuid).isPresent();
    }

    /**
     * Returns the existing {@link SetupSession} for the supplied player.
     *
     * @param playerUuid Player UUID.
     * @return Arena Setup Session.
     * @author au5tie
     */
    private final Optional<SetupSession> getUserSession(String playerUuid) {

        return Optional.ofNullable(sessions.get(playerUuid));
    }
}
