package com.au5tie.minecraft.tobnet.game.message;

import com.au5tie.minecraft.tobnet.game.event.TobnetEventPublisher;
import com.au5tie.minecraft.tobnet.game.message.provider.EnglishMessageProvider;
import com.au5tie.minecraft.tobnet.game.message.provider.MessageProvider;
import com.au5tie.minecraft.tobnet.game.message.provider.TobnetMessageProviderChangedEvent;
import com.au5tie.minecraft.tobnet.game.util.TobnetLogUtils;

import java.text.MessageFormat;
import java.util.Optional;

public final class MessageController {

    private final MessageProvider defaultProvider;
    private MessageProvider provider;

    public MessageController() {

        defaultProvider = new EnglishMessageProvider();

        // When we are not provided with a provider, we'll default to English (US).
        changeProvider(new EnglishMessageProvider());
    }

    public MessageController(MessageProvider provider) {

        defaultProvider = new EnglishMessageProvider();

        changeProvider(provider);
    }

    /**
     * Returns the Message Provider for the plugin.
     * @return Message Provider.
     * @author au5tie
     */
    public final MessageProvider getProvider() {

        return provider;
    }

    /**
     * Changes the {@link MessageProvider} to be used for plugin messages.
     * @param provider Message Provider.
     * @author au5tie
     */
    public final void changeProvider(MessageProvider provider) {

        MessageProvider priorProvider = this.provider;

        this.provider = provider;

        // Publish an event out notifying that we've changed the message provider for the plugin.
        TobnetEventPublisher.publishEvent(new TobnetMessageProviderChangedEvent(priorProvider, provider));
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

        if (!message.isPresent()) {
            // The selected provider doesn't have the message, fallback to the default provider.
            message = defaultProvider.getMessage(messageName);
        }

        if (message.isPresent()) {
            // Format the message with our provided arguments.
            return MessageFormat.format(message.get(), arguments);
        } else {
            // Neither our selected provider nor the default have this message.
            TobnetLogUtils.warn("Message Controller >> Requested message " + messageName + " not found in either provider.");

            return "[Message " + messageName + " unavailable]";
        }
    }
}