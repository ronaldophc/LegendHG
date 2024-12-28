package com.ronaldophc.kits.kitCommands;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.ronaldophc.kits.manager.KitCombinationManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import com.ronaldophc.LegendHG;
import com.ronaldophc.constant.GameState;
import com.ronaldophc.helper.GameHelper;
import com.ronaldophc.helper.Logger;
import com.ronaldophc.helper.Util;
import com.ronaldophc.kits.manager.KitManager;
import com.ronaldophc.constant.Kits;
import com.ronaldophc.kits.manager.guis.KitGui;

@SuppressWarnings("unused")
public class Kit2 implements CommandExecutor, TabExecutor {

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("kit2")) {
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
        if (command.getName().equalsIgnoreCase("kit2")) {
            if (commandSender == null) return true;
            if (!(commandSender instanceof Player)) {
                commandSender.sendMessage(Util.noConsole);
                return true;
            }
            Player player = (Player) commandSender;

            if (GameHelper.getInstance().getKits() == 1) {
                player.sendMessage(Util.error + "Modo de jogo de apenas 1 kit!");
                return true;
            }

            GameState gameState = LegendHG.getGameStateManager().getGameState();
            KitManager kitManager = LegendHG.getKitManager();

            if (!gameState.canFreeUpdateKit() && !player.hasPermission("legendhg.vip.kits")) {

                if (!gameState.canFreeUpdateKit()) {

                    commandSender.sendMessage(Util.error + "Nao é possivel alterar o kit 2 no momento.");
                    return true;

                }

                commandSender.sendMessage(Util.error + "Para alterar o kit 2 neste estágio é necessario ter VIP.");
                return true;

            }

            if (!gameState.canFreeUpdateKit()
                    && !(gameState.canVipUpdateKit() && player.hasPermission("legendhg.vip.kits"))) {

                commandSender.sendMessage(Util.error + "Nao é possivel alterar o kit 2 no momento.");
                return true;

            }

            if (!gameState.canFreeUpdateKit() && gameState.canVipUpdateKit() && kitManager.hasPlayerKit2(player)) {

                commandSender.sendMessage(Util.error + "Você já selecionou um kit 2.");
                return true;

            }

            if (strings.length == 0) {
                KitGui.openGui(player, 1, 2);
                return true;
            }

            if (strings.length == 1) {
                String arg = strings[0];

                try {

                    Kits kit = kitManager.findKit(arg);

                    if (kitManager.getPlayerKit(player) == kit) {
                        player.sendMessage(Util.error + "Você já selecionou este kit como kit 1.");
                        return true;
                    }

                    String permission = kit.getPermission();

                    if (!player.hasPermission(permission)) {
                        player.sendMessage(Util.error + "Você não tem permissão para utilizar este kit");
                        return true;
                    }

                    if (GameHelper.getInstance().isTwoKits()) {
                        Kits currentKit1 = kitManager.getPlayerKit(player);
                        if (currentKit1 != null && KitCombinationManager.getInstance().isProhibitedCombination(currentKit1, kit)) {
                            player.sendMessage(Util.error + "Essa combinação de kits é bloqueada!");
                            return true;
                        }
                    }

                    if (kitManager.kitHasItem2(player)) {
                        kitManager.removeItemKit2(player);
                    }

                    kitManager.setPlayerKit2(player, kit);
                    if (gameState == GameState.INVINCIBILITY) {
                        kitManager.addItemKit2(player);
                        kitManager.addCompass(player);
                    }

                    player.sendMessage(Util.success + "Você selecionou o " + Util.color3 + "Kit 2 " + kit.getName());

                } catch (IllegalArgumentException e) {

                    player.sendMessage(Util.errorServer + "Erro ao selecionar o kit 2 " + arg);
                    Logger.logError("Erro ao usar o comando /kit: " + arg + " " + e.getMessage());

                }

                return true;

            }
        }
        return true;
    }
}
