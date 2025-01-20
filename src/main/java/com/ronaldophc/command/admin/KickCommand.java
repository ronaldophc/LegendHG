package com.ronaldophc.command.admin;

import com.ronaldophc.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class KickCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("kick")) {

            if (!commandSender.isOp() && !commandSender.hasPermission("legendhg.admin.kick")) {
                commandSender.sendMessage(Util.noPermission);
                return true;
            }

            if (strings.length == 0) {
                commandSender.sendMessage(Util.usage("/kick <player> <motivo>"));
                return true;
            }

            if(strings.length == 1) {
                try {
                    Player targetPlayer = Bukkit.getServer().getPlayer(strings[0]);
                    commandSender.sendMessage(Util.color3 + targetPlayer.getName() + Util.color1 + " foi kickado do servidor");
                    targetPlayer.kickPlayer(Util.color1 + "Voce foi kickado do servidor");
                } catch (Exception e) {
                    commandSender.sendMessage(Util.error + "Jogador nao encontrado.");
                    Util.errorCommand("kick", e);
                }
                return true;
            }

            try {
                Player targetPlayer = Bukkit.getServer().getPlayer(strings[0]);
                String message = "";
                for (int i = 1; i < strings.length; i++) {
                    message = String.valueOf(message) + "§f" + strings[i] + " ";
                }
                targetPlayer.kickPlayer(Util.color1 + "Voce foi kickado do servidor pelo motivo " + message);
            } catch (Exception e) {
                commandSender.sendMessage(Util.error + "Jogador nao encontrado.");
                Util.errorCommand("kick", e);
            }

            return true;
        }
        return false;
    }
}
