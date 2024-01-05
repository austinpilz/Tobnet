package com.au5tie.minecraft.tobnet.game.guice;

import com.au5tie.minecraft.tobnet.game.TobnetGamePlugin;
import com.google.inject.AbstractModule;
import java.util.logging.Logger;

public class TobnetPluginInjector extends AbstractModule {

  private final TobnetGamePlugin plugin;

  public TobnetPluginInjector(TobnetGamePlugin plugin) {
    this.plugin = plugin;
  }

  @Override
  protected void configure() {
    this.install(this.plugin);
    //new ConfigScanner(this.plugin, this.binder());
    this.bind(Logger.class)
      .annotatedWith(TobnetPluginLogger.class)
      .toInstance(this.plugin.getLogger());
  }
}
