package com.au5tie.minecraft.tobnet.game.arena.chat;

import com.au5tie.minecraft.tobnet.game.TobnetGamePlugin;
import com.au5tie.minecraft.tobnet.game.arena.TobnetArena;
import com.au5tie.minecraft.tobnet.game.arena.chat.handler.OpenChatHandler;
import com.au5tie.minecraft.tobnet.game.arena.chat.handler.TobnetChatHandler;
import com.au5tie.minecraft.tobnet.game.arena.game.ArenaGameManager;
import com.au5tie.minecraft.tobnet.game.arena.game.ArenaGameStatus;
import com.au5tie.minecraft.tobnet.game.arena.manager.ArenaManager;
import com.au5tie.minecraft.tobnet.game.arena.manager.ArenaManagerType;
import com.au5tie.minecraft.tobnet.game.arena.manager.ArenaManagerUtils;
import com.au5tie.minecraft.tobnet.game.arena.player.ArenaPlayerManager;
import com.au5tie.minecraft.tobnet.game.exception.TobnetEngineException;
import com.au5tie.minecraft.tobnet.game.log.TobnetLogUtils;
import com.au5tie.minecraft.tobnet.game.message.MessageConstants;
import com.au5tie.minecraft.tobnet.game.player.GamePlayer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.event.player.AsyncPlayerChatEvent;

/**
 * The Arena Chat Manager handles all the player to player messaging management/routing, as well as the plugin sending
 * messages to players. It acts as much as possible as the centralized messaging point for all chat messages to and from
 * players.
 *
 * Every {@link ArenaGameStatus} has a linked {@link TobnetChatHandler} which handles the chat in a specific way depending
 * on the game status within the arena. This allows for deep customization of chat depending on the current progress of
 * the game. You can register any of the Tobnet-provided {@link TobnetChatHandler}s or register your own which implements
 * the interface. These handlers allow for deep messaging customization like chat isolation, proximity chat, etc. By default,
 * the {@link OpenChatHandler} will be registered for every game status so that chat handles like vanilla Minecraft.
 *
 * @author au5tie
 */
@Getter
public class ArenaChatManager extends ArenaManager {

  private final Map<ArenaGameStatus, TobnetChatHandler> handlerMap =
    new HashMap<>();

  private final ArenaChatConfiguration configuration;

  private ArenaGameManager gameManager;
  private ArenaPlayerManager playerManager;

  public ArenaChatManager(
    TobnetArena arena,
    ArenaChatConfiguration configuration
  ) {
    super(arena);
    this.configuration = configuration;

    registerEventHandler(new ArenaChatEventHandler(getArena(), this));

    // Initialize the handler map to open for everything.
    Arrays
      .asList(ArenaGameStatus.values())
      .forEach(status -> registerChatHandler(status, new OpenChatHandler(this))
      );
  }

  @Override
  public ArenaManagerType getType() {
    return ArenaManagerType.CHAT;
  }

  @Override
  public void prepareManager() {
    //
  }

  @Override
  public void destroyManager() {
    //
  }

  @Override
  public void afterArenaPreparationComplete() {
    playerManager =
      (ArenaPlayerManager) ArenaManagerUtils
        .getManagerOfType(getArena(), ArenaManagerType.PLAYER)
        .orElseThrow(TobnetEngineException::new);
    gameManager =
      (ArenaGameManager) ArenaManagerUtils
        .getManagerOfType(getArena(), ArenaManagerType.GAME)
        .orElseThrow(TobnetEngineException::new);
  }

  /**
   * Registers the {@link TobnetChatHandler} to be used when the arena is in the provided {@link ArenaGameStatus}.
   *
   * @param gameStatus Arena Game Status.
   * @param chatHandler Tobnet Chat Handler.
   * @author au5tie
   */
  public final void registerChatHandler(
    ArenaGameStatus gameStatus,
    TobnetChatHandler chatHandler
  ) {
    handlerMap.put(gameStatus, chatHandler);
  }

  /**
   * Obtains the {@link TobnetChatHandler} to be used for the current status within the arena. If there is no handler
   * provided via configuration for the current game mode, this will default back to the most basic chat handler and
   * will log the warning to the console.
   *
   * @return Tobnet Chat Handler to be used for the current game mode.
   * @author au5tie
   */
  protected TobnetChatHandler getHandlerForCurrentArenaStatus() {
    // Obtain the specific handler being used for the current status of the arena.
    TobnetChatHandler chatHandler = handlerMap.get(gameManager.getGameStatus());

    if (chatHandler == null) {
      // There was no chat handler provided for this game mode, so we'll provide a basic one.
      chatHandler = new OpenChatHandler(this);
      handlerMap.put(gameManager.getGameStatus(), chatHandler);

      TobnetLogUtils.warn(
        "No chat handler provided for arena " +
        getArena().getName() +
        " in status " +
        gameManager.getGameStatus()
      );
    }

    return chatHandler;
  }

  /**
   * Handles when a player to player message is initiated. This will route the message to the configured chat handler
   * for specific game mode handling.
   *
   * @param event Player chat event.
   * @author au5tie
   */
  protected void handlePlayerToPlayerMessage(AsyncPlayerChatEvent event) {
    // Determine which chat handler we'll use for this message based on the current arena status.
    TobnetChatHandler chatHandler = getHandlerForCurrentArenaStatus();

    // Hand the event off to the handler to manage.
    chatHandler.handlePlayerToPlayerMessage(event);
  }

  /**
   * Announces when a new player has joined the arena. This will handle notifying players that a new player has joined.
   *
   * @author au5tie
   */
  protected void announcePlayerJoin(GamePlayer player) {
    if (
      getPlayerManager().isPlaying(player) &&
      getConfiguration().isAnnouncePlayerJoin()
    ) {
      // Send a message to all players in the arena notifying them of a player joining.
      sendMessageToAllPlayers(
        TobnetGamePlugin
          .getMessageController()
          .getMessage(MessageConstants.PLAYER_JOIN, player.getUsername())
      );
    }
  }

  /**
   * Announces when a new player has left the arena. This will handle notifying players that an existing player/team
   * member has left the arena game.
   *
   * @author au5tie
   */
  protected void announcePlayerLeave(GamePlayer player) {
    if (getConfiguration().isAnnouncePlayerLeave()) {
      // Send a message to all players in the arena notifying them of a player leaving.
      sendMessageToAllPlayers(
        TobnetGamePlugin
          .getMessageController()
          .getMessage(MessageConstants.PLAYER_LEAVE, player.getUsername())
      );
    }
  }

  /**
   * Sends a message to every player in the game.
   *
   * @param message Message to send.
   * @author au5tie
   */
  public void sendMessageToAllPlayers(String message) {
    sendMessageToPlayers(playerManager.getPlayers(), message);
  }

  /**
   * Sends a message to provided players.
   *
   * @param players Players to send to.
   * @param message Message to send.
   * @author au5tie
   */
  public void sendMessageToPlayers(List<GamePlayer> players, String message) {
    String newMessage =
      ChatColor.RED + TobnetGamePlugin.chatPrefix + ChatColor.RESET + message;

    players.forEach(player -> player.getPlayer().sendMessage(newMessage));
  }

  /**
   * Generates the console status lines pertaining the game status.
   *
   * @return Console status lines.
   * @author au5tie
   */
  @Override
  public List<String> getConsoleStatusLines(CommandSender sender) {
    return List.of(
      TobnetGamePlugin
        .getMessageController()
        .getMessage(MessageConstants.CONSOLE_CHAT_HANDLER) +
      ": " +
      getHandlerForCurrentArenaStatus().getClass().getSimpleName()
    );
  }
}
