package com.au5tie.minecraft.tobnet.game.message;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
class MessageControllerTest {

    private TobnetMessageController controller;

    @BeforeEach
    void setup() {

        controller = new TobnetMessageController();
    }

    @DisplayName("Message Retrieve - Found")
    @Test
    void testGetMessage() {

        String message = controller.getMessage(MessageConstants.COUNTDOWN_GAME_BEGINS, "6 seconds");

        Assertions.assertEquals("Game begins in 6 seconds", message);
    }

    @DisplayName("Message Retrieve - Not Found Any")
    @Test
    void testGetMessageNotFoundAny() {

        String message = controller.getMessage("thisMessageIdDoesNotExistTrustMe", "kekw");

        Assertions.assertEquals("[Message thisMessageIdDoesNotExistTrustMe unavailable]", message);
    }
}