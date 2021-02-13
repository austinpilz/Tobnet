package com.au5tie.minecraft.tobnet.game.player.display.lib;

import com.au5tie.minecraft.tobnet.game.player.GamePlayer;
import com.au5tie.minecraft.tobnet.game.player.display.component.GamePlayerDisplayComponent;
import com.au5tie.minecraft.tobnet.game.player.display.component.GamePlayerDisplayComponentLocation;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarFlag;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;

/**
 * Engine provided boss bar display component. This will display a boss bar (stackable) to the player and allow for
 * value updates to progress the bar. It is easily configurable with the provided constructor arguments or can be
 * overridden.
 *
 * @author au5tie
 */
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
        bar.removeAll();
    }

    @Override
    protected void destroy() {
        // Destroy process already calls hide, so let's just double check everyone has been removed.
        bar.removeAll();
    }

    /**
     * Sets the boss bar progress on a scale of 0.0 - 1.0.
     *
     * @param progress Bar progress.
     * @author au5tie
     */
    protected void setProgress(double progress) {

        bar.setProgress(progress);
    }
}
