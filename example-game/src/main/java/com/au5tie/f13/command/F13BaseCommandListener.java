package com.au5tie.f13.command;

import com.au5tie.minecraft.tobnet.game.TobnetGamePlugin;
import com.au5tie.minecraft.tobnet.game.arena.TobnetArena;
import com.au5tie.minecraft.tobnet.game.arena.manager.ArenaManagerType;
import com.au5tie.minecraft.tobnet.game.arena.player.ArenaPlayerManager;
import com.au5tie.minecraft.tobnet.game.command.CommandListener;
import com.au5tie.minecraft.tobnet.game.player.GamePlayer;
import com.au5tie.minecraft.tobnet.game.util.TobnetChatUtils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Optional;

public class F13BaseCommandListener extends CommandListener {

    @Override
    protected void registerCommands() {
        registerCommand("f13");
    }

    @Override
    public boolean onCommandExecuted(CommandSender sender, Command command, String label, String[] args) {

        if (args.length == 2 && args[0].equalsIgnoreCase("play")) {

            String arenaName = args[1];

            Optional<TobnetArena> arena = TobnetGamePlugin.getArenaController().getArenaByName(arenaName);

            if (arena.isPresent()) {

                ArenaPlayerManager playerManager = (ArenaPlayerManager)arena.get().getManager(ArenaManagerType.PLAYER).get();

                playerManager.joinGame(new GamePlayer(arena.get(), (Player) sender));

            } else {
                TobnetChatUtils.sendPlayerMessage((Player) sender, "Arena " + arenaName + " does not exist.");
            }
        }

        return true;
    }
}
