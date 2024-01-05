package com.au5tie.minecraft.tobnet.game.log;

import java.util.logging.Logger;

public class TobnetLogUtils {

  private static final Logger log = Logger.getLogger("Minecraft");

  public static Logger getLogger() {
    return log;
  }

  public static void performance(String message) {
    info(message);
  }

  public static void info(String message) {
    log.info("[TobnetCore] " + message);
  }

  public static void warn(String message) {
    log.warning("[TobnetCore] " + message);
  }

  public static void error(String message, Exception exception) {
    log.warning("[TobnetCore] " + message);
    exception.printStackTrace(); //TODO make this pretty
  }
}
