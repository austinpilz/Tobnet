package com.au5tie.minecraft.tobnet.game.message.provider;

import java.util.HashMap;
import java.util.Optional;

/**
 * A Tobnet Message Provider manages language specific translations of plugin messages. The provider itself maintains
 * a collection of messages and their translations into the provider specific language.
 *
 * If you'd like to create your own provider with custom messages and/or translations of existing messages, extend this
 * class and register it with the {@link com.au5tie.minecraft.tobnet.game.message.MessageController}. You should pay special
 * care to ensure your custom provider includes a translation for every message that the default provider includes. If
 * your provider misses a message that the game tries to pull from your provider, the controller will fall back to obtaining
 * it from the default provider.
 *
 * @author au5tie
 */
public abstract class MessageProvider {

    private final MessageProviderLanguage language;
    private final HashMap<String, String> messages;

    public MessageProvider(MessageProviderLanguage language) {

        this.language = language;
        this.messages = new HashMap<>();

        // Have the provider store register all of its messages.
        registerMessages();
    }

    /**
     * Returns the language of the messages the provider handles.
     *
     * @return Message Provider Language.
     * @author au5tie
     */
    public final MessageProviderLanguage getLanguage() {

        return language;
    }

    /**
     * Registers all of the messages which the provider will manage.
     *
     * @author au5tie
     */
    public abstract void registerMessages();

    /**
     * Registers a message within the provider.
     *
     * @param name Message name.
     * @param message Message content.
     * @author au5tie
     */
    protected final void registerMessage(String name, String message) {

        messages.put(name, message);
    }

    /**
     * Obtains the message content for the provided message name.
     *
     * @param name Message Name.
     * @return The message content, if it exists within the provider.
     * @author au5tie
     */
    public final Optional<String> getMessage(String name) {

        return Optional.ofNullable(messages.get(name));
    }
}
