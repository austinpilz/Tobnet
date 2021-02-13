package com.au5tie.minecraft.tobnet.game.arena.countdown.display;

import com.au5tie.minecraft.tobnet.game.player.GamePlayer;
import com.au5tie.minecraft.tobnet.game.player.display.lib.TobnetBossBarDisplayComponent;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;

/**
 * Arena countdown specific boss bar display component implementation. This will be a boss bar specifically for displaying
 * the countdown status of the arena to a specific player.
 *
 * @author au5tie
 */
public class ArenaCountdownDisplayComponentBossBar extends TobnetBossBarDisplayComponent implements ArenaCountdownDisplayComponent {

    public ArenaCountdownDisplayComponentBossBar(String name, int priority, GamePlayer player, String title, BarColor color, BarStyle style, BarFlag flag) {

        super(name, priority, player, title, color, style, flag);
    }

    @Override
    public void updateProgress(int secondsLeft, int maxSeconds) {

        float percentage = ((float) secondsLeft) / maxSeconds;

        setProgress(percentage);
    }
}
