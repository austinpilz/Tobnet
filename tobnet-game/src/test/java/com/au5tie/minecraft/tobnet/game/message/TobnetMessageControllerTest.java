package com.au5tie.minecraft.tobnet.game.message;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mockStatic;

import com.au5tie.minecraft.tobnet.game.event.TobnetEventPublisher;
import com.au5tie.minecraft.tobnet.game.message.provider.EnglishMessageProvider;
import com.au5tie.minecraft.tobnet.game.message.provider.MessageProvider;
import org.bukkit.event.Event;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.runner.JUnitPlatform;
import org.junit.runner.RunWith;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@RunWith(JUnitPlatform.class)
public class TobnetMessageControllerTest {

  private TobnetMessageController controller;

  @BeforeEach
  void setup() {
    controller = new TobnetMessageController();
  }

  @DisplayName("Message Retrieve - Found")
  @Test
  void itSuccessfullyObtainsMessageFoundInDatabase() {
    String message = controller.getMessage(
      MessageConstants.COUNTDOWN_GAME_BEGINS,
      "6 seconds"
    );

    Assertions.assertEquals("Game begins in 6 seconds", message);
  }

  @DisplayName("Message Retrieve - Not Found Any")
  @Test
  void itReturnsPlaceholderWhenMessageNotFoundInDatabase() {
    String message = controller.getMessage(
      "thisMessageIdDoesNotExistTrustMe",
      "kekw"
    );

    Assertions.assertEquals(
      "[Message thisMessageIdDoesNotExistTrustMe unavailable]",
      message
    );
  }

  @DisplayName("Message Provider Change")
  @Test
  void itEmitsProviderChangeEvent() {
    try (MockedStatic mocked = mockStatic(TobnetEventPublisher.class)) {
      MessageProvider newProvider = new EnglishMessageProvider();

      // Change the message provider.
      controller.changeProvider(newProvider);

      // Ensure we emit an event that we changed providers.
      mocked.verify(() -> TobnetEventPublisher.publishEvent(any(Event.class)));

      Assertions.assertEquals(newProvider, controller.getProvider());
    }
  }
}
