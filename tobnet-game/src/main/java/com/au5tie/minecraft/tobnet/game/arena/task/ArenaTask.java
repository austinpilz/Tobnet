package com.au5tie.minecraft.tobnet.game.arena.task;

import com.au5tie.minecraft.tobnet.game.TobnetGamePlugin;
import com.au5tie.minecraft.tobnet.game.arena.manager.ArenaManager;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.bukkit.Bukkit;

/**
 * The Tobnet ArenaTask represents a runnable task which can be run in a variety of modes with the server scheduler.
 *
 * @author au5tie
 */
@Builder
@AllArgsConstructor
@Getter
public final class ArenaTask {

  private final Runnable runnable;
  private final ArenaManager manager;
  private final ArenaTaskType type;
  private ArenaTaskMode mode = ArenaTaskMode.SYNC;
  private int initialDelay = 0;
  private int interval = 20;

  // Represents the server scheduler ID for the task, if scheduled.
  private int taskId = -1;

  /**
   * Schedules the task to be run on the server scheduler.
   *
   * @author au5tie
   */
  public void scheduleTask() {
    // Verify that the task is not already scheduled as we don't want to double schedule a single task.
    if (!isScheduled()) {
      if (ArenaTaskMode.SYNC.equals(getMode())) {
        // The task will be running synchronously.
        if (ArenaTaskType.REPEATING.equals(getType())) {
          // Repeating Sync task.
          scheduleSynchronousRecurringTask();
        } else if (ArenaTaskType.FUTURE_RUN.equals(getType())) {
          // Future Delayed Task.
          scheduleSynchronousDelayedTask();
        }
      }
    }
  }

  /**
   * Schedules the synchronous delayed task to run once in the future. This will use the initial delay as the offset
   * from when the event is scheduled and when it will run.
   *
   * @author au5tie
   */
  private void scheduleSynchronousDelayedTask() {
    taskId =
      Bukkit
        .getScheduler()
        .scheduleSyncDelayedTask(
          TobnetGamePlugin.instance,
          getRunnable(),
          getInitialDelay()
        );
  }

  /**
   * Schedules the synchronous recurring task with the server scheduler.
   *
   * @author au5tie
   */
  private void scheduleSynchronousRecurringTask() {
    taskId =
      Bukkit
        .getScheduler()
        .scheduleSyncRepeatingTask(
          TobnetGamePlugin.instance,
          getRunnable(),
          getInitialDelay(),
          getInterval()
        );
  }

  /**
   * Cancels the scheduled task if it is currently scheduled.
   *
   * @author au5tie
   */
  public void cancelTask() {
    if (isScheduled()) {
      Bukkit.getScheduler().cancelTask(taskId);
    }
  }

  /**
   * Determines if the task is currently scheduled with the server scheduler. This will check to see if the task is
   * currently queued to run in the future or if it's currently running. If either are true, this indicates that the
   * task is scheduled within the server scheduler and will run at some point in the future. If both are false, this
   * means the task is not running nor will it run in the future.
   *
   * @return If the task is scheduled with the server scheduler.
   * @author au5tie
   */
  public boolean isScheduled() {
    if (taskId > -1) {
      return (
        Bukkit.getScheduler().isCurrentlyRunning(taskId) ||
        Bukkit.getScheduler().isQueued(taskId)
      );
    } else {
      return false;
    }
  }
}
