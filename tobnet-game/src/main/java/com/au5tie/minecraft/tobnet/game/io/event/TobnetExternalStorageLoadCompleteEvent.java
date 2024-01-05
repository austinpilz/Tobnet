package com.au5tie.minecraft.tobnet.game.io.event;

import com.au5tie.minecraft.tobnet.game.event.TobnetCustomEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * The event represents a notification that the plugin has completed loading of all external data, regardless of source,
 * implied that it is for all arenas. In essence, this event gives every arena, manager, etc. the green light to begin
 * pulling data from wherever it's registered from the storage managers in the arena.
 *
 * @author au5tie
 */
@Getter
@AllArgsConstructor
public class TobnetExternalStorageLoadCompleteEvent extends TobnetCustomEvent {}
