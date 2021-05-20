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
