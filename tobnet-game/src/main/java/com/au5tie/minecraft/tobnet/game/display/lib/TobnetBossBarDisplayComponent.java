package com.au5tie.minecraft.tobnet.game.display.lib;

import com.au5tie.minecraft.tobnet.game.display.component.GamePlayerDisplayComponent;
import com.au5tie.minecraft.tobnet.game.display.component.GamePlayerDisplayComponentLocation;
import com.au5tie.minecraft.tobnet.game.player.GamePlayer;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;

@Getter
public class TobnetBossBarDisplayComponent extends GamePlayerDisplayComponent {

    private final BossBar bar;
    private String title;
    private BarColor color;
    private BarStyle style;
    private BarFlag flag;

    public TobnetBossBarDisplayComponent(String name, int priority, GamePlayer player, String title, BarColor color, BarStyle style, BarFlag flag) {

        super(name, priority, GamePlayerDisplayComponentLocation.BOSS_BAR, player);

        this.title = title;
        this.color = color;
        this.style = style;
        this.flag = flag;

        bar = Bukkit.createBossBar(getTitle(), getColor(), getStyle(), getFlag());
    }

    @Override
    protected void display() {

        bar.addPlayer(getPlayer().getPlayer());
    }

    @Override
    protected void hide() {

        bar.removePlayer(getPlayer().getPlayer());
    }

    @Override
    protected void destroy() {
        //
    }

    protected void setProgress(double progress) {

        bar.setProgress(progress);
    }
}
