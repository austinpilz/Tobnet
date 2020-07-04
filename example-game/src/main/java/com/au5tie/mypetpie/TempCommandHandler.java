package com.au5tie.mypetpie;

import com.au5tie.minecraft.tobnet.core.session.SetupSessionStepInvocationContext;
import com.au5tie.minecraft.tobnet.game.TobnetGamePlugin;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class TempCommandHandler implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length > 0) {
            //


            if (args.length == 1 && args[0].equalsIgnoreCase("setup")) {

                // Build the setup session context.
                SetupSessionStepInvocationContext context = SetupSessionStepInvocationContext.builder()
                        .player((Player) sender)
                        .command(command.getName())
                        .commandArguments(Arrays.asList(args))
                        .build();

                TobnetGamePlugin.getArenaController().getSetupSessionController().requestNewSession((Player)sender);

            } else if (args.length > 1 && args[0].equalsIgnoreCase("setup")) {

                Player player = (Player) sender;

                // Build the setup session context.
                SetupSessionStepInvocationContext context = SetupSessionStepInvocationContext.builder()
                        .player((Player) sender)
                        .command(command.getName())
                        .commandArguments(Arrays.asList(args))
                        .build();

                TobnetGamePlugin.getArenaController().getSetupSessionController().sessionStepInvoke(player.getUniqueId().toString(), context);
            }
        }

        return true;
    }
}
