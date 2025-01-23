package com.ronaldophc.command.admin;

import com.ronaldophc.feature.punish.banip.BanIPService;
import com.ronaldophc.util.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class UnBanIPCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("unbanip")) {

            if (!commandSender.isOp() && !commandSender.hasPermission("legendhg.admin.unbanip")) {
                commandSender.sendMessage(Util.noPermission);
                return true;
            }

            if (strings.length == 0) {
                commandSender.sendMessage(Util.usage("/unbanip <ip>"));
                return true;
            }

            try {
                String ip = strings[0];
                InetAddress ipAddress = InetAddress.getByName(ip);

                BanIPService banIPService = new BanIPService();
                if (!banIPService.isIPBanned(ipAddress)) {
                    commandSender.sendMessage(Util.error + "IP não está banido.");
                    return true;
                }

                if (banIPService.unbanIP(ipAddress)) {
                    commandSender.sendMessage(Util.admin + "IP desbanido com sucesso.");
                    return true;
                }

                commandSender.sendMessage(Util.errorServer + "Erro ao desbanir IP. Contate o DEV");
            } catch (UnknownHostException e) {
                commandSender.sendMessage(Util.error + "IP inválido.");
                return true;
            }

            return true;
        }
        return false;
    }
}
