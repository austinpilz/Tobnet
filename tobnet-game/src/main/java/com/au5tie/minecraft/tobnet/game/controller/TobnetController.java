package com.au5tie.minecraft.tobnet.game.controller;

public interface TobnetController {

    /**
     * Prepares the controller for use. This is where engine wide associations should be performed if accessing anything
     * outside of the immediate controller itself.
     */
    void prepare();
}
