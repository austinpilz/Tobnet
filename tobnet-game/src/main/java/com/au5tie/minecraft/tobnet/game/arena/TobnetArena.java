package com.au5tie.minecraft.tobnet.game.arena;

import com.au5tie.minecraft.tobnet.game.TobnetGamePlugin;
import com.au5tie.minecraft.tobnet.game.arena.chat.ArenaChatConfiguration;
import com.au5tie.minecraft.tobnet.game.arena.chat.ArenaChatManager;
import com.au5tie.minecraft.tobnet.game.arena.chat.handler.IsolationChatHandler;
import com.au5tie.minecraft.tobnet.game.arena.chat.handler.OpenChatHandler;
import com.au5tie.minecraft.tobnet.game.arena.chat.handler.TobnetChatHandler;
import com.au5tie.minecraft.tobnet.game.arena.convert.BaseArenaConverter;
import com.au5tie.minecraft.tobnet.game.arena.convert.TobnetArenaConverter;
import com.au5tie.minecraft.tobnet.game.arena.countdown.ArenaCountdownConfiguration;
import com.au5tie.minecraft.tobnet.game.arena.countdown.ArenaCountdownManager;
import com.au5tie.minecraft.tobnet.game.arena.countdown.start.ArenaCountdownSecondsProvider;
import com.au5tie.minecraft.tobnet.game.arena.countdown.start.ArenaCountdownSecondsStaticProvider;
import com.au5tie.minecraft.tobnet.game.arena.event.ArenaEventHandler;
import com.au5tie.minecraft.tobnet.game.arena.game.ArenaGameManager;
import com.au5tie.minecraft.tobnet.game.arena.game.ArenaGameStatus;
import com.au5tie.minecraft.tobnet.game.arena.location.ArenaLocationManager;
import com.au5tie.minecraft.tobnet.game.arena.manager.ArenaManager;
import com.au5tie.minecraft.tobnet.game.arena.manager.ArenaManagerType;
import com.au5tie.minecraft.tobnet.game.arena.player.ArenaPlayerDisplayManager;
import com.au5tie.minecraft.tobnet.game.arena.player.ArenaPlayerManager;
import com.au5tie.minecraft.tobnet.game.arena.spawn.ArenaSpawnLocationManager;
import com.au5tie.minecraft.tobnet.game.exception.TobnetEngineException;
import com.google.common.collect.ImmutableList;
import java.util.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.Location;

/**
 * The base Tobnet Arena representation. Each Tobnet Arena is a shell of a basic functioning arena,
 * complete with basic locations, statistics, etc. Each implementing game will require more advanced
 * and specific fields, which are handled
 *
 * Tobnet Arena -> Extending Arena. Extending Arena uses //TODO
 *
 */
@Data
@AllArgsConstructor
public abstract class TobnetArena {

  private String name;

  private Map map;

  // Arena Boundaries.
  private Location boundaryOne;
  private Location boundaryTwo;

  // Arena Managers.
  private Map<ArenaManagerType, ArenaManager> managers;
  private boolean managerRegistrationComplete;

  // Converter.
  private TobnetArenaConverter converter;

  /**
   * Arena instantiation is done via reflection.
   * @param name Arena Name.
   * @author au5tie
   */
  public TobnetArena(String name) {
    this.name = name;

    // By default, we use the base arena converter.
    converter = new BaseArenaConverter(this);

    // Manager preparation.
    managers = new HashMap<>();
    prepareArenaManagers();
  }

  /**
   * Allows the implementing plugin to configure additional managers, such as optional or custom managers. All of the
   * arenas required/main/default managers are already configured prior to this.
   *
   * @author au5tie
   */
  protected abstract void prepareAdditionalManagers();

  /**
   * Returns all the Arena's {@link ArenaManager}.
   *
   * The returned list is a new list which contains all of the Arena's {@link ArenaManager}. It is not
   * the Arena's actual list which manages them all, thus there is no risk for the caller to be able
   * to modify the collection integrity.
   *
   * @return Arena Managers.
   * @author Austin Pilz n0286596
   */
  public List<ArenaManager> getManagers() {
    return ImmutableList.copyOf(managers.values());
  }

  /**
   * Obtains the {@link ArenaManager} of the supplied type.
   *
   * @param type Arena Manager Type.
   * @return Arena Manager of type.
   * @author au5tie
   */
  public Optional<ArenaManager> getManager(ArenaManagerType type) {
    return Optional.ofNullable(managers.get(type));
  }

  /**
   * Registers a new {@link ArenaManager}.
   *
   * @param manager Arena Manager.
   * @author au5tie
   */
  public void registerManager(ArenaManager manager) {
    if (!managerRegistrationComplete) {
      // Register the manager within the arena.
      managers.put(manager.getType(), manager);
    } else {
      // The manager registration period has ended.
      throw new TobnetEngineException(
        "Arena Manager registration period for " + getName() + " has ended."
      );
    }
  }

  /**
   * Prepares the managers in the arena for gameplay. This will allow the implementing arena to handle the creation and
   * registration of their desired managers in the arena. Then this will handle registering all of the manager listeners
   * with the server API.
   *
   * @author Austin Pilz n0286596
   */
  private final void prepareArenaManagers() {
    // Create all default managers to handle basic requirements.
    prepareDefaultManagers();

    // Allow implementing arena to register managers.
    prepareAdditionalManagers();

    // Invoke each manager to prepare itself.
    getManagers().forEach(ArenaManager::prepareManager);

    // Register manager event listeners.
    registerManagerListeners();

    // Mark that registration is complete which will not allow any further managers to register.
    managerRegistrationComplete = true;

    // Notify all managers we've finished configuring them. This will allow them to dynamically link to one another.
    getManagers().forEach(ArenaManager::afterArenaPreparationComplete);
  }

  /**
   * Registers all the manager's event listeners to Bukkit so they'll receive event notifications as they occur directly
   * to their class.
   *
   * Note: There is no way to de-register events from the Bukkit event scheduler once they've been subscribed. This
   * introduces complexity in the scenario we want to remove an arena, as the garbage collection will be forced to keep
   * the arena and all of its components in memory.
   *
   * @author au5tie
   */
  private void registerManagerListeners() {
    // Compile all of the handlers registered by all of the managers.
    List<ArenaEventHandler> handlers = new ArrayList<>();

    getManagers()
      .stream()
      .flatMap(manager -> manager.getEventHandlers().stream())
      .forEach(handler ->
        Bukkit
          .getServer()
          .getPluginManager()
          .registerEvents(handler, TobnetGamePlugin.getInstance())
      );
  }

  /**
   * Checks if supplied location is within the {@link TobnetArena} based on the boundaries.
   *
   * @param location Location.
   * @return If the {@link Location} is within the Arena.
   * @author au5tie
   */
  public boolean isLocationWithinArena(Location location) {
    double[] dim = new double[2];

    dim[0] = boundaryOne.getX();
    dim[1] = boundaryTwo.getX();
    Arrays.sort(dim);
    if (location.getX() > dim[1] || location.getX() < dim[0]) return false;

    dim[0] = boundaryOne.getY();
    dim[1] = boundaryTwo.getY();
    Arrays.sort(dim);
    if (location.getY() > dim[1] || location.getY() < dim[0]) return false;

    dim[0] = boundaryOne.getZ();
    dim[1] = boundaryTwo.getZ();
    Arrays.sort(dim);

    return !(location.getZ() > dim[1] || location.getZ() < dim[0]);
  }

  /**
   * Prepares all default {@link ArenaManager} for the arena. This will create all managers within the arena with engine
   * default specifications.
   *
   * This is done to allow for basic configuration that implementing plugins won't need to do. Additionally, this allows
   * Tobnet to add future required managers which the implementing plugin has not gotten around/doesn't need to implement
   * as this will provide a default.
   *
   * This is NOT the place for the implementing plugin to override and configure all of their manager. This will setup
   * all the default managers and the implementing plugin can override each prepare() method to customize that specific
   * manager. After all the main managers have been set up, then prepareAdditionalManagers() will be called where the
   * implementing plugin can register any custom managers.
   *
   * @author au5tie
   */
  private final void prepareDefaultManagers() {
    // Chat.
    prepareChatManager();

    // Countdown.
    prepareCountdownManager();

    // Game.
    prepareGamePlayer();

    // Location.
    prepareLocationManager();

    // Player.
    preparePlayerManager();

    // Player Display.
    preparePlayerDisplayManager();

    // Spawn Location.
    prepareSpawnLocationManager();
  }

  /**
   * Prepares the chat manager for the game. This manager will control how chat is routed, displayed, etc. during various
   * phases of the game.
   *
   * In this default, we'll just use the engine provided chat manager. When players are waiting for the game to begin,
   * we'll use the open chat handler so they can interact with friends not in the lobby yet. Once the game begins, we'll
   * use chat isolation so only chat from within the arena is visible to players playing.
   *
   * @author au5tie
   */
  protected void prepareChatManager() {
    ArenaChatConfiguration configuration = ArenaChatConfiguration
      .builder()
      .announcePlayerJoin(true)
      .announcePlayerLeave(true)
      .build();

    ArenaChatManager chatManager = new ArenaChatManager(this, configuration);

    // Register the chat handlers. This will use chat isolation for all games modes, restrictions apply.
    TobnetChatHandler isolationChatHandler = new IsolationChatHandler(
      chatManager
    );

    // By default, we'll have the entire game use isolation so player chat inside and outside the arena is separated.
    Arrays
      .asList(ArenaGameStatus.values())
      .forEach(status ->
        chatManager.registerChatHandler(status, isolationChatHandler)
      );

    // Except for in WAITING, where we will allow open, so they can beg their friends to come play with them.
    chatManager.registerChatHandler(
      ArenaGameStatus.WAITING,
      new OpenChatHandler(chatManager)
    );

    // Finally, register the manager with the arena, so it will be used.
    registerManager(chatManager);
  }

  /**
   * Prepares the countdown manager for the game. In this example, we'll use the engine provided countdown manager and
   * pass in some basic configuration which allows us to customize the display of the countdown process.
   *
   * Here, we use the {@link ArenaCountdownSecondsStaticProvider} to have a static number of seconds in the countdown
   * phase until the game begins. We can use any of the {@link ArenaCountdownSecondsProvider} extending classes or
   * even create our own.
   *
   * @author au5tie
   */
  protected void prepareCountdownManager() {
    // Create the basic configuration for how we'd like our countdown phase to behave.
    ArenaCountdownConfiguration configuration = ArenaCountdownConfiguration
      .builder()
      .displayChatIntervals(true)
      .displayCountdownUI(true)
      .build();

    // We need to provide a countdown provider to the manager, so it knows how we want it count down when the time comes.
    ArenaCountdownSecondsProvider provider =
      new ArenaCountdownSecondsStaticProvider(this, 15);

    // Create our countdown manager and provide our configuration to it.
    ArenaCountdownManager countdownManager = new ArenaCountdownManager(
      this,
      configuration,
      provider
    );

    // Finally, register the manager with the arena, so it will be used.
    registerManager(countdownManager);
  }

  /**
   * Prepares the game manager for the arena.
   *
   * Here, we'll use the default {@link ArenaGameManager}.
   *
   * @author au5tie
   */
  protected void prepareGamePlayer() {
    ArenaGameManager gameManager = new ArenaGameManager(this);

    // Finally, register the manager with the arena so it will be used.
    registerManager(gameManager);
  }

  /**
   * Prepares the location manager for the arena.
   *
   * Here, we'll use the default {@link ArenaLocationManager}.
   *
   * @author au5tie
   */
  protected void prepareLocationManager() {
    ArenaLocationManager locationManager = new ArenaLocationManager(this);

    // Finally, register the manager with the arena so it will be used.
    registerManager(locationManager);
  }

  /**
   * Prepares the player manager for the arena.
   *
   * Here, we'll use the default {@link ArenaPlayerManager}.
   *
   * @author au5tie
   */
  protected void preparePlayerManager() {
    ArenaPlayerManager playerManager = new ArenaPlayerManager(this);

    // Finally, register the manager with the arena so it will be used.
    registerManager(playerManager);
  }

  /**
   * Prepares the player display manager for the arena.
   *
   * Here, we'll use the default {@link ArenaPlayerDisplayManager}.
   *
   * @author au5tie
   */
  protected void preparePlayerDisplayManager() {
    ArenaPlayerDisplayManager playerDisplayManager =
      new ArenaPlayerDisplayManager(this);

    // Finally, register the manager with the arena so it will be used.
    registerManager(playerDisplayManager);
  }

  /**
   * Prepares the spawn location manager for the arena.
   *
   * Here, we'll use the default {@link ArenaSpawnLocationManager}.
   *
   * @author au5tie
   */
  protected void prepareSpawnLocationManager() {
    ArenaSpawnLocationManager spawnLocationManager =
      new ArenaSpawnLocationManager(this);

    // Finally, register the manager with the arena so it will be used.
    registerManager(spawnLocationManager);
  }
}
