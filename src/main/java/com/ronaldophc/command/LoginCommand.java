package com.ronaldophc.command;

import com.ronaldophc.constant.MySQL.PlayerField;
import com.ronaldophc.constant.MySQL.Tables;
import com.ronaldophc.database.MySQLManager;
import com.ronaldophc.database.PlayerSQL;
import com.ronaldophc.feature.auth.AuthManager;
import com.ronaldophc.helper.Logger;
import com.ronaldophc.helper.Util;
import com.ronaldophc.player.account.Account;
import com.ronaldophc.player.account.AccountManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class LoginCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(Util.noConsole);
            return true;
        }

        Player player = (Player) commandSender;

        if (command.getName().equalsIgnoreCase("login")) {
            if (strings.length == 0) {
                player.sendMessage(Util.title + " > " + Util.usage("/login <password>"));
                return true;
            }

            if (strings.length == 1) {
                String password = strings[0];
                Account account = AccountManager.getInstance().getOrCreateAccount(player);
                try {
                    if (account.isLoggedIn()) {
                        player.sendMessage(Util.title + " > " + Util.color2 + "Você já entrou.");
                        return true;
                    }
                    if (!PlayerSQL.isPlayerRegistered(player)) {
                        player.sendMessage(Util.title + " > " + Util.color2 + "Você não está registrado.");
                        return true;
                    }
                    if (account.login(password)) {
                        String ipAddress = player.getAddress().getAddress().getHostAddress();
                        MySQLManager.setString(player.getUniqueId().toString(), Tables.PLAYER.getTableName(), PlayerField.IP.getFieldName(), ipAddress);

                        account.setLoggedIn(true);
                        AuthManager.loginPlayer(player);

                        player.sendMessage(Util.title + " > " + Util.success + "Você entrou.");
                        return true;
                    }
                    player.sendMessage(Util.title + " > " + Util.error + "Senha incorreta.");
                } catch (Exception e) {
                    player.sendMessage(Util.title + " > " + Util.error + "Erro ao entrar.");
                    Logger.logError("Failed to login player: " + e.getMessage());
                }
                return true;
            }
        }
        return true;
    }
}
