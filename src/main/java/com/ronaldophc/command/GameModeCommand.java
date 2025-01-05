package com.ronaldophc.command;

import com.ronaldophc.helper.Util;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GameModeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("gm")) {
            if (commandSender == null) {
                return true;
            }
            if (!(commandSender instanceof Player)) {
                commandSender.sendMessage(Util.noConsole);
                return true;
            }
            Player player = (Player) commandSender;
            if (!commandSender.isOp() && !commandSender.hasPermission("legendhg.admin.gm")) {
                commandSender.sendMessage(Util.noPermission);
                return true;
            }
            if (strings.length == 0) {
                commandSender.sendMessage(Util.usage("/gm <0|1> <player>"));
                return true;
            }
            if (strings.length == 1) {
                if (strings[0].equalsIgnoreCase("0")) {
                    player.setGameMode(GameMode.SURVIVAL);
                    player.sendMessage(Util.success + "Alterado para sobrevivencia");
                    return true;
                }
                if (strings[0].equalsIgnoreCase("1")) {
                    player.setGameMode(GameMode.CREATIVE);
                    player.sendMessage(Util.success + "Alterado para criativo");
                    return true;
                }
                player.sendMessage(Util.usage("/gm <0|1> <player>"));
                return true;
            }
            if (strings.length == 2) {
                Player target = player.getServer().getPlayer(strings[1]);
                if (!target.isOnline()) {
                    player.sendMessage(Util.noPlayer);
                    return true;
                }
                try {
                    if (strings[0].equalsIgnoreCase("0")) {
                        target.setGameMode(GameMode.SURVIVAL);
                        player.sendMessage(Util.success + "Alterado " + Util.color3 + target.getName() + Util.success + "para sobrevivencia");
                        return true;
                    }
                    if (strings[0].equalsIgnoreCase("1")) {
                        target.setGameMode(GameMode.CREATIVE);
                        player.sendMessage(Util.success + "Alterado " + Util.color3 + target.getName() + Util.success + " para criativo");
                        return true;
                    }
                } catch (Exception e) {
                    player.sendMessage(Util.noPlayer);
                    Util.errorCommand("gm", e);
                    return true;
                }
                return true;
            }
        }
        return true;
    }
}
