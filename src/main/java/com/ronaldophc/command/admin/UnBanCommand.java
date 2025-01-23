package com.ronaldophc.command.admin;

import com.ronaldophc.feature.punish.ban.BanService;
import com.ronaldophc.feature.punish.mute.MuteService;
import com.ronaldophc.player.PlayerService;
import com.ronaldophc.util.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.UUID;

public class UnBanCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("unban")) {

            if (!commandSender.isOp() && !commandSender.hasPermission("legendhg.admin.unban")) {
                commandSender.sendMessage(Util.noPermission);
                return true;
            }

            if (strings.length == 0) {
                commandSender.sendMessage(Util.usage("/unban <name>"));
                return true;
            }

            String name = strings[0];
            UUID uuid = PlayerService.getUUIDByName(name);
            if (uuid == null) {
                commandSender.sendMessage(Util.error + "Jogador '" + name + "' não encontrado.");
                return true;
            }

            BanService banService = new BanService();
            if (!banService.isBanned(uuid)) {
                commandSender.sendMessage(Util.error + name + " não está banido.");
                return true;
            }

            if (banService.unban(uuid)) {
                commandSender.sendMessage(Util.admin + name + " desbanido com sucesso.");
                return true;
            }

            commandSender.sendMessage(Util.errorServer + "Erro ao desbanir. Contate o DEV");

            return true;
        }
        return false;
    }
}
