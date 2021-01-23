package com.au5tie.minecraft.tobnet.game.message.provider;

import com.au5tie.minecraft.tobnet.game.event.TobnetCustomEvent;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

/**
 * The Tobnet Message Provider Changed Event represents the message for the provider being changed.
 *
 * @author au5tie
 */
@Getter
@AllArgsConstructor
@Builder
public class TobnetMessageProviderChangedEvent extends TobnetCustomEvent {

    private final MessageProvider priorProvider;
    private final MessageProvider newProvider;
}
