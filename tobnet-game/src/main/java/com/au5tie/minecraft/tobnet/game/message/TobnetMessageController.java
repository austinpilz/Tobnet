package com.au5tie.minecraft.tobnet.game.message;

import com.au5tie.minecraft.tobnet.game.controller.TobnetController;
import com.au5tie.minecraft.tobnet.game.event.TobnetEventPublisher;
import com.au5tie.minecraft.tobnet.game.log.TobnetLogUtils;
import com.au5tie.minecraft.tobnet.game.message.provider.EnglishMessageProvider;
import com.au5tie.minecraft.tobnet.game.message.provider.MessageProvider;
import com.au5tie.minecraft.tobnet.game.message.provider.TobnetMessageProviderChangedEvent;
import com.google.inject.Singleton;
import java.text.MessageFormat;
import java.util.Optional;

/**
 * The Tobnet Message Controller acts as the engine's message dictionary and translator. It uses a {@link MessageProvider}
 * as a dictionary of all game messages in a particular language. This allows implementing plugins to change the language
 * of the messages sent/displayed in game.
 *
 * Implementing plugins are free to change the provider used for message lookup at any time by calling the changeProvider()
 * method.
 *
 * @author au5tie
 */
@Singleton
public final class TobnetMessageController implements TobnetController {

  private final MessageProvider defaultProvider = new EnglishMessageProvider();
  private MessageProvider provider;

  public TobnetMessageController() {
    // When we are not provided with a provider, we'll default to English (US).
    changeProvider(defaultProvider);
  }

  public TobnetMessageController(MessageProvider provider) {
    changeProvider(provider);
  }

  @Override
  public void prepare() {
    //
  }

  /**
   * Returns the Message Provider for the plugin.
   * @return Message Provider.
   * @author au5tie
   */
  public MessageProvider getProvider() {
    return provider;
  }

  /**
   * Changes the {@link MessageProvider} to be used for plugin messages.
   * @param newProvider Message Provider.
   * @author au5tie
   */
  public void changeProvider(MessageProvider newProvider) {
    MessageProvider priorProvider = this.provider;

    this.provider = newProvider;

    if (priorProvider != null) {
      // Publish an event out notifying that we've changed the message provider for the plugin.
      TobnetEventPublisher.publishEvent(
        new TobnetMessageProviderChangedEvent(priorProvider, newProvider)
      );
    }
  }

  /**
   * Obtains the requested message. This will obtain the translation for the requested message from the selected provider.
   * If the selected provider does not have the message, it will fall back to the engine's default provider. If neither
   * provider have the requested message, it will return placeholder text evident of the error.
   *
   * @param messageName Message name.
   * @param arguments Arguments to substitute into the message.
   * @return Translated message with injected arguments.
   * @author au5tie
   */
  public String getMessage(String messageName, String... arguments) {
    // Obtain the message from our selected provider.
    Optional<String> message = provider.getMessage(messageName);

    if (message.isEmpty()) {
      // The selected provider doesn't have the message, fallback to the default provider.
      message = defaultProvider.getMessage(messageName);
    }

    if (message.isPresent()) {
      // Format the message with our provided arguments.
      return MessageFormat.format(message.get(), arguments);
    } else {
      // Neither our selected provider nor the default have this message.
      TobnetLogUtils.warn(
        "Message Controller >> Requested message " +
        messageName +
        " not found in either provider."
      );

      return "[Message " + messageName + " unavailable]";
    }
  }
}
