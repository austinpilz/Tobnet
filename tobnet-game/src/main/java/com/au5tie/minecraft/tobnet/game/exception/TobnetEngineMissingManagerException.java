package com.au5tie.minecraft.tobnet.game.exception;

public class TobnetEngineMissingManagerException extends RuntimeException {

  public TobnetEngineMissingManagerException() {
    super("A required Arena Manager was not found/linked");
  }
}
