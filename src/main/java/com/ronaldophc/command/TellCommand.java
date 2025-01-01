package com.ronaldophc.command;

import com.ronaldophc.helper.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TellCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("tell")) {
            if (!(commandSender instanceof Player)) {
                commandSender.sendMessage(Util.noConsole);
                return true;
            }

            Player player = (Player) commandSender;

            if (strings.length < 2) {
                player.sendMessage(Util.usage("/tell <player> <message>"));
                return true;
            }

            Player target = player.getServer().getPlayer(strings[0]);
            if (target == null) {
                player.sendMessage(Util.error + "Player não encontrado.");
                return true;
            }

            StringBuilder message = new StringBuilder();
            for (int i = 1; i < strings.length; i++) {
                message.append(strings[i]).append(" ");
            }

            player.sendMessage("§8[§7Você §8» §7" + target.getName() + "§8] §f" + message.toString());
            target.sendMessage("§8[§7" + player.getName() + " §8» §7Você§8] §f" + message.toString());



            return true;
        }
        return false;
    }
}
