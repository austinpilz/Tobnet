package com.au5tie.minecraft.tobnet.game.arena.task;

import com.au5tie.minecraft.tobnet.game.arena.TobnetArena;
import com.au5tie.minecraft.tobnet.game.arena.manager.ArenaManager;
import com.au5tie.minecraft.tobnet.game.arena.manager.ArenaManagerType;

/**
 * The Tobnet Arena Task Manager is responsible for the management of all scheduled tasks for the arena. It acts as an
 * interface to the server API scheduler.
 *
 * In regards to the Bukkit Scheduler API, tasks are either scheduled or they're cancelled. There is no way to pause or
 * resume events. Once an event has been scheduled, it will run until it has been unscheduled. This assumption is carried
 * over here to the behavior of the tasks. Once a task has been unscheduled, it is removed from the task manager.
 *
 * @author au5tie
 */
@Deprecated
public class ArenaTaskManager extends ArenaManager {

    public ArenaTaskManager(TobnetArena arena) {
        super(arena);
    }

    @Override
    public ArenaManagerType getType() {
        return ArenaManagerType.TASK;
    }

    @Override
    public void prepareManager() {
        //
    }

    @Override
    public void destroyManager() {

    }
}
