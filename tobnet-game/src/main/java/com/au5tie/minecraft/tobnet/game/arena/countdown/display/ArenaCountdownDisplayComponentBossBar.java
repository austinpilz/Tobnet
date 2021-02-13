package com.au5tie.minecraft.tobnet.game.arena.countdown.display;

import com.au5tie.minecraft.tobnet.game.display.lib.TobnetBossBarDisplayComponent;
import com.au5tie.minecraft.tobnet.game.player.GamePlayer;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;

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
