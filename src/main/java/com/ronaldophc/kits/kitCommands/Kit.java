package com.ronaldophc.kits.kitCommands;

import com.ronaldophc.LegendHG;
import com.ronaldophc.helper.GameHelper;
import com.ronaldophc.helper.Logger;
import com.ronaldophc.helper.Util;
import com.ronaldophc.constant.GameState;
import com.ronaldophc.kits.manager.KitCombinationManager;
import com.ronaldophc.kits.manager.guis.KitGui;
import com.ronaldophc.kits.manager.KitManager;
import com.ronaldophc.constant.Kits;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Kit implements CommandExecutor, TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("kit")) {
            if (strings.length == 1) {
                List<String> list = new ArrayList<>(Collections.emptyList());
                for (Kits kit : Kits.values()) {
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
            KitManager kitManager = LegendHG.getKitManager();
            Player player = (Player) commandSender;

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

            if (!gameState.canFreeUpdateKit() && gameState.canVipUpdateKit() && kitManager.hasPlayerKit(player)) {
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
                    Kits kit = kitManager.findKit(arg);
                    String permission = kit.getPermission();

                    if (kitManager.getPlayerKit2(player) == kit) {
                        player.sendMessage(Util.error + "Você já selecionou este kit como kit 2.");
                        return true;
                    }

                    if (!player.hasPermission(permission)) {
                        player.sendMessage(Util.error + "Você não tem permissão para utilizar este kit");
                        return true;
                    }

                    if (GameHelper.getInstance().isTwoKits()) {
                        Kits currentKit2 = kitManager.getPlayerKit2(player);
                        if (currentKit2 != null && KitCombinationManager.getInstance().isProhibitedCombination(currentKit2, kit)) {
                            player.sendMessage(Util.error + "Essa combinação de kits é bloqueada!");
                            return true;
                        }
                    }

                    if (kitManager.kitHasItem(player)) {
                        kitManager.removeItemKit(player);
                    }

                    kitManager.setPlayerKit(player, kit);
                    if (gameState == GameState.INVINCIBILITY) {
                        kitManager.addItemKit(player);
                        kitManager.addCompass(player);
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
