package com.ronaldophc.command.admin;

import com.ronaldophc.LegendHG;
import com.ronaldophc.constant.GameState;
import com.ronaldophc.game.CountDown;
import com.ronaldophc.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class InvincibilityCommand implements CommandExecutor, TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("invincibility")) {
            if (strings.length == 1) {
                return Arrays.asList("set", "start");
            }
        }
        return Collections.emptyList();
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("invincibility")) {
            if (!commandSender.isOp() && !commandSender.hasPermission("legendhg.admin.invincibility")) {
                commandSender.sendMessage(Util.noPermission);
                return true;
            }
            if (strings.length == 0) {
                commandSender.sendMessage(Util.usage("/invincibility <set:start> <seconds>"));
                return true;
            }
            if (strings.length == 1) {
                if (!strings[0].equalsIgnoreCase("start")) {
                    commandSender.sendMessage(Util.usage("/invincibility <set:start> <seconds>"));
                    return true;
                }
                if (!(LegendHG.getGameStateManager().getGameState() == GameState.COUNTDOWN)) {
                    commandSender.sendMessage(Util.error + ("O HG nao esta no estado contagem para iniciar a invencilidade."));
                    return true;
                }
                LegendHG.getGameStateManager().startInvincibilityForced();
                return true;
            }
            if (strings.length == 2) {
                if (!strings[0].equalsIgnoreCase("set")) {
                    commandSender.sendMessage(Util.usage("/invincibility <set:start> <seconds>"));
                    return true;
                }
                if (!(LegendHG.getGameStateManager().getGameState() == GameState.INVINCIBILITY)) {
                    commandSender.sendMessage(Util.error + ("O HG nao esta no estado de invencibilidade para alterar o tempo."));
                    return true;
                }
                try {
                    int seconds = Integer.parseInt(strings[1]);
                    if (seconds < 3) {
                        commandSender.sendMessage(Util.error + ("O valor inserido deve ser maior ou igual a 3."));
                        return true;
                    }
                    CountDown.getInstance().setTime(seconds);
                    Bukkit.broadcastMessage(Util.color1 + "Tempo de invencibilidade definido para " + Util.color2 + seconds + Util.color1 + " segundos por um §cADMIN.");
                } catch (NumberFormatException e) {
                    commandSender.sendMessage(Util.error + ("O valor inserido nao e um numero."));
                }
                return true;
            }
            return true;
        }
        return true;
    }
}
