package com.ronaldophc.feature.report;

import com.ronaldophc.helper.Util;
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
            if(strings.length == 0) {
                player.sendMessage(Util.usage("/report <player>"));
                return true;
            }
            if(strings.length == 1) {
                try {
                    Player target = player.getServer().getPlayer(strings[0]);
                    player.sendMessage(Util.color1 + "Reportar o jogador " + target.getName() + " por: " + Util.color3 + "(Clique no motivo)");
                    PlayerConnection connection = ((CraftPlayer) player).getHandle().playerConnection;
                    Report.sendReportPacket(connection, "Hack", "/reporthack " + target.getName(), "Denuncie um Hack!");
                    Report.sendReportPacket(connection, "Chat", "/reportchat " + target.getName(), "Denuncie um jogador ofensa, ameaça, envio de links etc..!");
                    Report.sendReportPacket(connection, "Free Kill", "/reportfreekill " + target.getName(), "Denuncie um jogador por FreeKill!");
                    Report.sendReportPacket(connection, "Abuso de Bugs", "/reportbugs " + target.getName(), "Denuncie um jogador por Abusar de Bugs!");
                    Report.sendReportPacket(connection, "Outro", "/reportother " + target.getName(), "Denuncie um jogador!");
                } catch (Exception e) {
                    player.sendMessage(Util.noPlayer);
                }
                return true;
            }
            if(strings.length >= 2) {
                player.sendMessage(Util.usage("/report <player>"));
                return true;
            }
        }

        return true;
    }

}