package com.ronaldophc.command;

import com.ronaldophc.helper.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Pull implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("pull")) {

            if (commandSender == null)
                return true;

            if (!(commandSender instanceof Player)) {
                commandSender.sendMessage(Util.noConsole);
                return true;
            }

            Player player = (Player) commandSender;

            if (!commandSender.isOp() && !commandSender.hasPermission("legendhg.admin.flyspeed")) {
                commandSender.sendMessage(Util.noPermission);
                return true;
            }

            if (strings.length != 1) {
                commandSender.sendMessage(Util.usage("/pull <player>"));
                return true;
            }

            try {

                Player target = player.getServer().getPlayer(strings[0]);
                if (target == null) {
                    player.sendMessage(Util.error + "Jogador não encontrado");
                    return true;
                }

                target.teleport(player);
                target.sendMessage(Util.success + "Você foi puxado por " + player.getName());
                player.sendMessage(Util.success + "Jogador puxado");

            } catch (NumberFormatException e) {

                player.sendMessage(Util.error + "Jogador não encontrado");

            }
            return true;
        }
        return true;
    }
}
