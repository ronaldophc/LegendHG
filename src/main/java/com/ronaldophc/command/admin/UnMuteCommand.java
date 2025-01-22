package com.ronaldophc.command.admin;

import com.ronaldophc.feature.punish.mute.MuteService;
import com.ronaldophc.player.PlayerService;
import com.ronaldophc.util.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.UUID;

public class UnMuteCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("unmute")) {

            if (!commandSender.isOp() && !commandSender.hasPermission("legendhg.admin.unmute")) {
                commandSender.sendMessage(Util.noPermission);
                return true;
            }

            if (strings.length == 0) {
                commandSender.sendMessage(Util.usage("/unmute <ip>"));
                return true;
            }

            String name = strings[0];
            UUID uuid = PlayerService.getUUIDByName(name);
            if (uuid == null) {
                commandSender.sendMessage(Util.error + "Jogador '" + name + "' não encontrado.");
                return true;
            }

            MuteService muteService = new MuteService();
            if (!muteService.isMuted(uuid)) {
                commandSender.sendMessage(Util.error + name + " não está mutado.");
                return true;
            }

            if (muteService.unmute(uuid)) {
                commandSender.sendMessage(Util.admin + name + " desmutado com sucesso.");
                return true;
            }

            commandSender.sendMessage(Util.errorServer + "Erro ao desmutar. Contate o DEV");

            return true;
        }
        return false;
    }
}
