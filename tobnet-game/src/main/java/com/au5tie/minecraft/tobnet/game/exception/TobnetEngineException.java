package com.au5tie.minecraft.tobnet.game.exception;

public class TobnetEngineException extends RuntimeException {

    public TobnetEngineException() {
        super();
    }

    public TobnetEngineException(String message) {
        super(message);
    }

    public TobnetEngineException(String message, Throwable throwable) {
        super(message, throwable);
    }
}