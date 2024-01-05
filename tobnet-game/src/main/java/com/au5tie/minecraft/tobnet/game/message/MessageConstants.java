package com.au5tie.minecraft.tobnet.game.message;

/**
 * These are the _names_ (or internal identifiers) of messages that are provided by Tobnet by default. These names are used
 * in each message provider to allow the lookup of the same message in different languages.
 *
 * This class houses Tobnet-default messages. If you're looking to add your own messages to your own provider, you'll
 * need to make your own constants class.
 */
public class MessageConstants {

  // Console.
  public static final String CONSOLE_CHAT_HANDLER = "console_chat_handler";
  public static final String CONSOLE_COUNTDOWN = "console_countdown";
  public static final String CONSOLE_SECONDS = "console_countdown_seconds";
  public static final String CONSOLE_GAME = "console_game_status";
  public static final String CONSOLE_LOCATIONS = "console_locations";
  public static final String CONSOLE_SPAWN_LOCATIONS =
    "console_spawn_locations";

  // Setup.
  public static final String SETUP_SESSION_COMMAND = "setup_command_name";
  public static final String SETUP_SESSION_END_COMMAND =
    "setup_command_end_name";
  public static final String SETUP_SESSION_NON_PLAYER_INVOKE_ERROR =
    "setup_command_nonplayer_invoke_error";
  public static final String SETUP_SESSION_TYPE_COMMAND_MISSING_ERROR =
    "setup_command_type_missing_cli_error";
  public static final String SETUP_SESSION_TYPE_UNSUPPORTED_ERROR =
    "setup_command_type_unsupported";
  public static final String SETUP_SESSION_ENDED = "setup_session_ended";
  public static final String SETUP_SESSION_END_MISSING =
    "setup_session_end_missing";

  // Arena.
  public static final String ARENA_CONSOLE_HEADER = "arena_console_header";
  public static final String ARENA_NO_EXIST = "arena_no_exist";
  public static final String ARENA_NONE_REGISTERED = "arena_none_registered";

  // Countdown.
  public static final String COUNTDOWN_GAME_BEGINS = "countdown_gameBegins";
  public static final String COUNTDOWN_DISPLAY_TITLE =
    "countdown_display_title";

  // Player.
  public static final String PLAYER_JOIN = "player_join";
  public static final String PLAYER_LEAVE = "player_leave";
}
