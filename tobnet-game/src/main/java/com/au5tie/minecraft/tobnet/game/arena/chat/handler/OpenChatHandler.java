package com.au5tie.minecraft.tobnet.game.arena.chat.handler;

import com.au5tie.minecraft.tobnet.game.arena.chat.ArenaChatManager;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * The Open Chat Handler provides full open communication, it will perform no filtering nor isolation of player messages.
 * This means that players outside of the game will be able to see messages from player in game and vise versa. Using this
 * handler is essentially telling the plugin not to do anything with messages.
 *
 * @author au5tie
 */
public class OpenChatHandler extends TobnetChatHandler {

  public OpenChatHandler(ArenaChatManager chatManager) {
    super(chatManager);
  }

  @Override
  void performMessageHandling(AsyncPlayerChatEvent event) {
    // We do nothing, this is open communication.
  }
}
