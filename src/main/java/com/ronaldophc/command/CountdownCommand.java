package com.ronaldophc.command;

import com.ronaldophc.LegendHG;
import com.ronaldophc.helper.Util;
import com.ronaldophc.gamestate.CountDown;
import com.ronaldophc.constant.GameState;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CountdownCommand implements CommandExecutor, TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("countdown")) {
            if (strings.length == 1) {
                return Arrays.asList("set");
            }
        }
        return Collections.emptyList();
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("countdown")) {
            if (!commandSender.isOp() && !commandSender.hasPermission("legendhg.admin.countdown")) {
                commandSender.sendMessage(Util.noPermission);
                return true;
            }
            if (strings.length == 0) {
                commandSender.sendMessage(Util.usage("/countdown <set> <seconds>"));
                return true;
            }
            if (strings.length == 1) {
                commandSender.sendMessage(Util.usage("/countdown <set> <seconds>"));
                return true;
            }
            if (strings.length == 2) {
                if (!strings[0].equalsIgnoreCase("set")) {
                    commandSender.sendMessage(Util.usage("/countdown <set> <seconds>"));
                    return true;
                }
                if (!(LegendHG.getGameStateManager().getGameState() == GameState.COUNTDOWN)) {
                    commandSender.sendMessage(Util.error + ("O HG nao esta no estado contagem."));
                    return true;
                }
                try {
                    int seconds = Integer.parseInt(strings[1]);
                    if (seconds < 5) {
                        commandSender.sendMessage(Util.error + ("O valor inserido deve ser maior ou igual a 5."));
                        return true;
                    }
                    CountDown.getInstance().setTime(seconds);
                    Bukkit.broadcastMessage(Util.color1 + "Contagem regressiva definida para " + Util.color2 + seconds + Util.color1 + " segundos por um §cADMIN.");
                } catch (NumberFormatException e) {
                    commandSender.sendMessage(Util.error + ("O valor inserido nao e um numero."));
                }
            }
            return true;
        }
        return true;
    }
}
