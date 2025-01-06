package com.ronaldophc.command;

import com.ronaldophc.LegendHG;
import com.ronaldophc.constant.GameState;
import com.ronaldophc.helper.Logger;
import com.ronaldophc.helper.Util;
import com.ronaldophc.kits.Kit;
import com.ronaldophc.kits.manager.KitManager;
import com.ronaldophc.kits.manager.guis.KitGui;
import com.ronaldophc.player.account.Account;
import com.ronaldophc.player.account.AccountManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KitCommand implements CommandExecutor, TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("kit")) {
            if (strings.length == 1) {
                List<String> list = new ArrayList<>(Collections.emptyList());
                KitManager kitManager = LegendHG.getKitManager();
                for (Kit kit : kitManager.getKits()) {
                    if (kit.getName().toLowerCase().startsWith(strings[0].toLowerCase())) {
                        list.add(kit.getName());
                    }
                }
                return list;
            }
        }
        return Collections.emptyList();
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("kit")) {

            if (!(commandSender instanceof Player)) {
                commandSender.sendMessage(Util.noConsole);
                return true;
            }

            GameState gameState = LegendHG.getGameStateManager().getGameState();

            Player player = (Player) commandSender;
            Account account = AccountManager.getInstance().getOrCreateAccount(player);

            if (!gameState.canFreeUpdateKit() && !player.hasPermission("legendhg.vip.kits")) {
                if (!gameState.canFreeUpdateKit()) {
                    commandSender.sendMessage(Util.error + "Nao é possivel alterar o kit no momento.");
                    return true;
                }

                commandSender.sendMessage(Util.error + "Para alterar o kit neste estágio é necessario ter VIP.");
                return true;
            }

            if (!gameState.canFreeUpdateKit() && !(gameState.canVipUpdateKit() && player.hasPermission("legendhg.vip.kits"))) {
                commandSender.sendMessage(Util.error + "Nao é possivel alterar o kit no momento.");
                return true;
            }

            if (!gameState.canFreeUpdateKit() && gameState.canVipUpdateKit() && account.getKits().hasPrimary()) {
                commandSender.sendMessage(Util.error + "Você já selecionou um kit.");
                return true;
            }

            if (strings.length == 0) {
                KitGui.openGui(player, 1, 1);
                return true;
            }

            if (strings.length == 1) {
                String arg = strings[0];

                try {
                    Kit kit = LegendHG.getKitManager().searchKit(arg);
                    String permission = kit.getPermission();

                    // Verificar se não é igual ao kit atual, tanto o primary, quanto secondary
                    if (account.getKits().getSecondary() == kit) {
                        player.sendMessage(Util.error + "Você já selecionou este kit como kit 2.");
                        return true;
                    }

                    if (!player.hasPermission(permission)) {
                        player.sendMessage(Util.error + "Você não tem permissão para utilizar este kit");
                        return true;
                    }

                    // Bloquear combinações de kits
//                    if (GameHelper.getInstance().isTwoKits()) {
//                        Kits currentKit2 = kitManager.getPlayerKit2(player);
//                        if (currentKit2 != null && KitCombinationManager.getInstance().isProhibitedCombination(currentKit2, kit)) {
//                            player.sendMessage(Util.error + "Essa combinação de kits é bloqueada!");
//                            return true;
//                        }
//                    }

                    account.getKits().setPrimary(kit);
                    if (gameState == GameState.INVINCIBILITY) {
                        kit.apply(player);
                    }

                    player.sendMessage(Util.success + "Você selecionou o " + Util.color3 + "Kit " + kit.getName());
                } catch (Exception e) {
                    player.sendMessage(Util.errorServer + "Erro ao selecionar o kit " + arg);
                    Logger.logError("Erro ao usar o comando /kit: " + arg + " " + e.getMessage());
                }

                return true;
            }
        }
        return true;
    }
}
