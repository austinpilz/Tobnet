package com.au5tie.minecraft.tobnet.game.player.display.exception;

/**
 * The exception is thrown whenever an attempt is made to register a {@link com.au5tie.minecraft.tobnet.game.player.display.component.GamePlayerDisplayComponent}
 * with the {@link com.au5tie.minecraft.tobnet.game.player.display.GamePlayerDisplayManager} when there is already a
 * different unique component registered with the same name.
 *
 * To remedy, either change the name of the display component being registered or de-register the existing component from
 * the manager prior to registering the new one.
 *
 * @author au5tie
 */
public class DuplicatePlayerDisplayComponentException extends RuntimeException {

  public DuplicatePlayerDisplayComponentException(String message) {
    super(message);
  }
}
