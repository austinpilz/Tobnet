package com.au5tie.minecraft.tobnet.game.message.provider;

import com.au5tie.minecraft.tobnet.game.message.MessageConstants;

/**
 * The English Message Provider is the default and fully populated message provider that comes with Tobnet. It is
 * guaranteed to provide every engine-provided message.
 *
 * @author au5tie
 */
public class EnglishMessageProvider extends MessageProvider {

    public EnglishMessageProvider() {

        super(MessageProviderLanguage.ENGLISH_US);
    }

    public void registerMessages() {

        // Console.
        registerMessage(MessageConstants.CONSOLE_CHAT_HANDLER, "Chat Handler");
        registerMessage(MessageConstants.CONSOLE_COUNTDOWN, "Countdown");
        registerMessage(MessageConstants.CONSOLE_SECONDS, "seconds");
        registerMessage(MessageConstants.CONSOLE_GAME, "Game Status");
        registerMessage(MessageConstants.CONSOLE_LOCATIONS, "Locations");
        registerMessage(MessageConstants.CONSOLE_SPAWN_LOCATIONS, "Spawn Locations");

        // Setup.
        registerMessage(MessageConstants.SETUP_SESSION_COMMAND, "setup");
        registerMessage(MessageConstants.SETUP_SESSION_END_COMMAND, "end");
        registerMessage(MessageConstants.SETUP_SESSION_TYPE_COMMAND_MISSING_ERROR, "Setup session type was not provided. Please verify command syntax.");
        registerMessage(MessageConstants.SETUP_SESSION_NON_PLAYER_INVOKE_ERROR, "Console-based setup sessions are not supported. Sessions must be run by a player in-game.");
        registerMessage(MessageConstants.SETUP_SESSION_TYPE_UNSUPPORTED_ERROR, "The provided session type {0} is not a supported session type.");
        registerMessage(MessageConstants.SETUP_SESSION_ENDED, "Your existing {0} setup session has been ended.");
        registerMessage(MessageConstants.SETUP_SESSION_END_MISSING, "You do not have an existing setup session in progress.");

        // Arena.
        registerMessage(MessageConstants.ARENA_CONSOLE_HEADER, "-- Arenas --");
        registerMessage(MessageConstants.ARENA_NONE_REGISTERED, "There are no registered arenas.");
        registerMessage(MessageConstants.ARENA_NO_EXIST, "Arena {} does not exist.");

        // Countdown.
        registerMessage(MessageConstants.COUNTDOWN_GAME_BEGINS, "Game begins in {0}");
        registerMessage(MessageConstants.COUNTDOWN_DISPLAY_TITLE, "Game Countdown");

        // Player.
        registerMessage(MessageConstants.PLAYER_JOIN, "{0} has joined the game");
        registerMessage(MessageConstants.PLAYER_LEAVE, "{0} has left the game");
    }
}
