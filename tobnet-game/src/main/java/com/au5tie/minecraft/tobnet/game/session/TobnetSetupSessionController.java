package com.au5tie.minecraft.tobnet.game.session;

import com.au5tie.minecraft.tobnet.game.TobnetGamePlugin;
import com.au5tie.minecraft.tobnet.game.command.listener.CommandListener;
import com.au5tie.minecraft.tobnet.game.controller.TobnetController;
import com.au5tie.minecraft.tobnet.game.exception.TobnetEngineException;
import org.bukkit.entity.Player;

import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * The Setup Session Controller manages and handles all the setup sessions occurring in the plugin. It supports an unlimited
 * number of session types and will, once registered, be able to instantiate custom setup session types.
 *
 * @author au5tie
 */
public class TobnetSetupSessionController implements TobnetController {

    private final Map<String, SetupSession> sessions;
    private final Map<String, Class> supportedSessionTypes;
    private final SetupSessionCommandListener commandListener;

    public TobnetSetupSessionController(String command) {

        sessions = new HashMap<>();
        supportedSessionTypes = new HashMap<>();

        // Command Listener.
        commandListener = new SetupSessionCommandListener(this, command);
    }

    @Override
    public void prepare() {
        // Register the command listener.
        TobnetGamePlugin.getCommandController().registerCommandLister(commandListener);
    }

    /**
     * Returns the {@link CommandListener} used for all setup sessions.
     *
     * @return The command listener used for setup sessions.
     * @author au5tie
     */
    public CommandListener getCommandListener() {

        return commandListener;
    }

    /**
     *
     * @param sessionClass Setup Session class.
     * @param commandLevelType Command line name for the session type.
     * @author au5tie
     *
     */
    public void registerSessionType(Class sessionClass, String commandLevelType) {

        supportedSessionTypes.put(commandLevelType.toUpperCase(), sessionClass);
    }

    /**
     * Determines if the provided session type is supported.
     *
     * @param sessionType Setup session type.
     * @return If the session type is supported.
     * @author au5tie
     */
    public boolean isSessionTypeSupported(String sessionType) {

        return supportedSessionTypes.containsKey(sessionType.toUpperCase());
    }

    /**
     * Obtains the {@link SetupSession} implementation class for the provided session type, if exists.
     *
     * @param sessionType Session Type.
     * @return Session type class.
     * @author au5tie
     */
    public Optional<Class> getSupportedSessionClass(String sessionType) {

        return Optional.ofNullable(supportedSessionTypes.get(sessionType.toUpperCase()));
    }

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
     * De-registers a {@link SetupSession} for the supplied user.
     *
     * @param playerUuid Player UUID.
     * @author au5tie
     */
    private final void removeSession(String playerUuid) {

        sessions.remove(playerUuid);
    }

    /**
     * Returns all the registered {@link SetupSession}.
     *
     * @return Setup Sessions.
     * @author au5tie
     */
    protected final List<SetupSession> getSessions() {

        return sessions.values().stream().collect(Collectors.toUnmodifiableList());
    }

    /**
     * Requests a new {@link SetupSession} be created and prepared for the requesting user. This will create the new
     * session, prepare it, register it with the controller, and begin it.
     *
     * @param context Setup Session Context.
     * @return Setup Session.
     * @author au5tie
     */
    public SetupSession requestNewSession(SetupSessionStepInvocationContext context) {

        if (!doesUserHaveExistingSession(context.getPlayer().getUniqueId().toString())) {
            // Begin the new session.
            return beginNewSession(context.getSessionType(), context.getPlayer());
        } else {
            // The user already has an existing setup session in progress. Terminate their existing in progress session.
            requestSessionTermination(context.getPlayer().getUniqueId().toString());

            // Begin the session request process again.
            return requestNewSession(context);
        }
    }

    /**
     * Begins a new {@link SetupSession} for the requesting user. This will create a new session and initiate the session
     * progress line.
     *
     * @param player Player.
     * @return Setup Session.
     * @author au5tie
     */
    private SetupSession beginNewSession(String sessionType, Player player) {

        // Have the implementing controller prepare the specific session.
        SetupSession session = prepareNewSession(sessionType, player);

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
     * Creates a new {@link SetupSession} for the requested player.
     *
     * @param sessionType Setup Session Type.
     * @param player Player.
     * @return Setup Session.
     * @author au5tie
     */
    private SetupSession prepareNewSession(String sessionType, Player player) {

        Optional<Class> sessionClass = getSupportedSessionClass(sessionType);

        try {
            Class[] types = {Player.class};
            Constructor arenaConstructor = sessionClass.orElseThrow(SetupSessionTypeNotSupportedException::new).getConstructor(types);
            Object[] parameters = {player};
            return (SetupSession) arenaConstructor.newInstance(parameters);
        } catch (Exception exception) {
            throw new TobnetEngineException("Encountered an error while attempting to create new setup session", exception);
        }
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
     * Obtains the type of the setup session that user has in progress, if there is one.
     *
     * @param playerUuid Player UUID.
     * @return In progress setup session type.
     * @author au5tie
     */
    public final Optional<String> getUserExistingSessionType(String playerUuid) {

        Optional<SetupSession> userSession = getUserSession(playerUuid);

        if (userSession.isPresent()) {
            Optional<Map.Entry<String, Class>> sessionClassEntry = supportedSessionTypes.entrySet().stream().filter(entry -> entry.getValue().equals(userSession.get().getClass())).findAny();

            if (sessionClassEntry.isPresent()) {
                return Optional.of(sessionClassEntry.get().getKey());
            }
        }

        return Optional.empty();
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
