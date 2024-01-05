package com.au5tie.sandfall.command;

import com.au5tie.minecraft.tobnet.game.TobnetGamePlugin;
import com.au5tie.minecraft.tobnet.game.arena.TobnetArena;
import com.au5tie.minecraft.tobnet.game.arena.manager.ArenaManagerType;
import com.au5tie.minecraft.tobnet.game.arena.player.ArenaPlayerManager;
import com.au5tie.minecraft.tobnet.game.chat.TobnetChatUtils;
import com.au5tie.minecraft.tobnet.game.command.listener.CommandListener;
import com.au5tie.minecraft.tobnet.game.player.GamePlayer;
import java.util.Optional;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SandFallCommandListener extends CommandListener {

  @Override
  public void registerCommands() {
    registerCommand("sf");
  }

  @Override
  public boolean onCommandExecuted(
    CommandSender sender,
    Command command,
    String label,
    String[] args
  ) {
    if (args.length == 2 && args[0].equalsIgnoreCase("play")) {
      String arenaName = args[1];

      Optional<TobnetArena> arena = TobnetGamePlugin
        .getArenaController()
        .getArenaByName(arenaName);

      if (arena.isPresent()) {
        ArenaPlayerManager playerManager = (ArenaPlayerManager) arena
          .get()
          .getManager(ArenaManagerType.PLAYER)
          .get();

        playerManager.joinGame(new GamePlayer(arena.get(), (Player) sender));
      } else {
        TobnetChatUtils.sendPlayerMessage(
          (Player) sender,
          "Arena " + arenaName + " does not exist."
        );
      }
    } else if (args.length == 1 && args[0].equalsIgnoreCase("quit")) {
      Optional<TobnetArena> arena = TobnetGamePlugin
        .getArenaController()
        .getPlayerArena(((Player) sender).getUniqueId().toString());

      if (arena.isPresent()) {
        ArenaPlayerManager playerManager = (ArenaPlayerManager) arena
          .get()
          .getManager(ArenaManagerType.PLAYER)
          .get();

        playerManager.leaveGame(
          playerManager.getGamePlayer(
            ((Player) sender).getUniqueId().toString()
          )
        );
      } else {
        TobnetChatUtils.sendPlayerMessage(
          (Player) sender,
          "You're not currently playing."
        );
      }
    }

    return true;
  }
}
