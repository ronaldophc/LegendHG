package com.ronaldophc.command;

import com.ronaldophc.LegendHG;
import com.ronaldophc.database.CurrentGameSQL;
import com.ronaldophc.database.PlayerSQL;
import com.ronaldophc.feature.auth.AuthManager;
import com.ronaldophc.helper.Logger;
import com.ronaldophc.helper.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RegisterCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(Util.noConsole);
            return true;
        }

        Player player = (Player) commandSender;

        if (command.getName().equalsIgnoreCase("register")) {
            if (strings.length == 0) {
                player.sendMessage(Util.title + " > " + Util.usage("/register <password>"));
                return true;
            }
            if (strings.length == 1) {
                String password = strings[0];
                try {
                    if (PlayerSQL.isPlayerRegistered(player)) {
                        player.sendMessage(Util.title + " > " +  Util.color2 + "Você já esta registrado.");
                        return true;
                    }
                    String ipAddress = player.getAddress().getAddress().getHostAddress();
                    PlayerSQL.setPlayerIpAddress(player, ipAddress);
                    PlayerSQL.registerPlayer(player, password);
                    AuthManager.loginPlayer(player);
                    CurrentGameSQL.createCurrentGameStats(player, LegendHG.getGameId());
                    player.sendMessage(Util.title + " > " + Util.success + "Você se registrou.");
                    return true;
                } catch (Exception e) {
                    player.sendMessage(Util.title + " > " +  Util.error + "Erro ao registrar.");
                    Logger.logError("Failed to register player: " + e.getMessage());
                    return true;
                }
            }
        }
        return true;
    }
}
