package com.au5tie.minecraft.tobnet.game.message.provider;

import com.au5tie.minecraft.tobnet.game.message.MessageConstants;

public class EnglishMessageProvider extends MessageProvider {

    public EnglishMessageProvider() {

        super(MessageProviderLanguage.ENGLISH_US);
    }

    public void registerMessages() {

        // Countdown.
        registerMessage(MessageConstants.COUNTDOWN_GAME_BEGINS, "Game begins in {0}");
    }
}
