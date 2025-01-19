package com.ronaldophc.feature.report;

import com.ronaldophc.constant.ReportEnum;
import com.ronaldophc.util.Util;
import net.minecraft.server.v1_8_R3.PlayerConnection;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class ReportCommand implements CommandExecutor {

    // O comando para reportar será /reporthack <player>, /reportchat <player>, /reportfreekill <player>, /reportbugs <player>, /reportother <player>

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("report")) {
            if (!(commandSender instanceof Player)) {
                commandSender.sendMessage(Util.noConsole);
                return true;
            }
            Player player = (Player) commandSender;
            if (strings.length == 0) {
                player.sendMessage(Util.usage("/report <player>"));
                return true;
            }
            if (strings.length == 1) {
                try {
                    Player target = player.getServer().getPlayer(strings[0]);
                    player.sendMessage(Util.color1 + "Reportar o jogador " + target.getName() + " por: " + Util.color3 + "(Clique no motivo)");
                    PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
                    Report.sendReportPacket(connection, "Hack", "/report " + target.getName() + " hack", "Denuncie um Hack!");
                    Report.sendReportPacket(connection, "Chat", "/report " + target.getName() + " chat", "Denuncie um jogador ofensa, ameaça, envio de links etc..!");
                    Report.sendReportPacket(connection, "Free Kill", "/report " + target.getName() + " freekill", "Denuncie um jogador por FreeKill!");
                    Report.sendReportPacket(connection, "Abuso de Bugs", "/report " + target.getName() + " bugs", "Denuncie um jogador por Abusar de Bugs!");
                    Report.sendReportPacket(connection, "Outro", "/report " + target.getName() + " other", "Denuncie um jogador!");
                } catch (Exception e) {
                    player.sendMessage(Util.noPlayer);
                }
                return true;
            }

            Player target = player.getServer().getPlayer(strings[1]);
            if (target == null) {
                player.sendMessage(Util.noPlayer);
                return true;
            }

            ReportEnum reportEnum = ReportEnum.valueOf(strings[0].toUpperCase());

            if (Report.reports.containsKey(target)) {
                player.sendMessage(Util.color1 + "O jogador " + target.getName() + " já foi reportado por " + Util.color3 + Report.reports.get(target).toString());
            }

            String reason = reportEnum.toString();

            Report.reports.put(target, reportEnum);
            player.sendMessage(Util.color1 + "Você reportou o jogador " + target.getName() + " por " + Util.color3 + reason + ", um ADM irá em breve verificar o motivo");
            Report.sendNotification(target, reason);

            return true;
        }

        return true;
    }
}