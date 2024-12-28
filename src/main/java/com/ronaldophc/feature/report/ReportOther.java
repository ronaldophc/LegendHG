package com.ronaldophc.feature.report;

import com.ronaldophc.constant.ReportEnum;
import com.ronaldophc.helper.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ReportOther implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if(command.getName().equalsIgnoreCase("reportother")) {
            if(!(commandSender instanceof Player)) {
                commandSender.sendMessage(Util.noConsole);
                return true;
            }
            Player player = (Player) commandSender;
            if(strings.length == 0) {
                player.sendMessage(Util.usage("/reportother <player>"));
                return true;
            }
            if(strings.length == 1) {
                try {
                    Player target = player.getServer().getPlayer(strings[0]);
                    if(Report.reports.containsKey(target)) {
                        player.sendMessage(Util.color1 + "O jogador " + target.getName() + " já foi reportado por " + Util.color3 + Report.reports.get(target).toString());
                        return true;
                    }
                    Report.reports.put(target, ReportEnum.OTHER);
                    player.sendMessage(Util.color1 + "Você reportou o jogador " + target.getName() + ", um ADM irá em breve verificar o motivo");
                    Report.sendNotification();
                } catch (Exception e) {
                    player.sendMessage(Util.noPlayer);
                }
                return true;
            }
            if(strings.length >= 2) {
                player.sendMessage(Util.usage("/reportother <player>"));
                return true;
            }
        }
        return true;
    }
}
