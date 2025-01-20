package com.ronaldophc.command.admin;

import com.ronaldophc.feature.admin.AdminManager;
import com.ronaldophc.player.account.Account;
import com.ronaldophc.player.account.AccountManager;
import com.ronaldophc.util.Util;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AdminCommand implements CommandExecutor {

    private final AccountManager accountManager = AccountManager.getInstance();
    private final AdminManager adminManager = AdminManager.getInstance();

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("admin")) {
            if (!(commandSender instanceof Player)) {
                commandSender.sendMessage(Util.noConsole);
                return true;
            }

            Player player = (Player) commandSender;
            if (!player.isOp() && !player.hasPermission("legendhg.admin.admin")) {
                player.sendMessage(Util.noPermission);
                return true;
            }

            if (strings.length == 0) {
                activateAdminMode(player);
                return true;
            }

            if ((strings.length == 1 && strings[0].equalsIgnoreCase("off"))) {
                deactivateAdminMode(player);
                return true;
            }
        }
        return true;
    }

    private void activateAdminMode(Player player) {
        Account account = accountManager.getOrCreateAccount(player);
        adminManager.savePlayerInventory(player);
        account.setBuild(true);
        account.setSeeSpecs(true);
        account.setVanish(true);
        player.setGameMode(GameMode.CREATIVE);
        player.getInventory().clear();
        player.getInventory().setItem(0, AdminManager.playersCompass);
        player.updateInventory();

        player.sendMessage(Util.admin + "Você ativou o modo administrativo.");
        player.sendMessage(Util.admin + "Para sair digite /admin off.");
    }

    private void deactivateAdminMode(Player player) {
        Account account = accountManager.getOrCreateAccount(player);
        account.setBuild(false);
        player.setGameMode(GameMode.SURVIVAL);
        player.getInventory().clear();

        if (adminManager.restorePlayerInventory(player)) {
            player.sendMessage(Util.admin + "Você desativou o modo administrativo.");
        }

        player.updateInventory();
    }
}
