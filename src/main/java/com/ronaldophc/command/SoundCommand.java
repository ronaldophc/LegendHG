package com.ronaldophc.command;

import com.ronaldophc.helper.Util;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class SoundCommand implements CommandExecutor, TabCompleter {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("sound")) {
            if (!(commandSender instanceof Player)) {
                commandSender.sendMessage(Util.noConsole);
                return true;
            }

            Player player = (Player) commandSender;
            if (!commandSender.isOp() && !commandSender.hasPermission("legendhg.sounds")) {
                commandSender.sendMessage(Util.noPermission);
                return true;
            }

            if (strings.length == 0) {
                player.sendMessage(Util.error + "Use /sound <som>");
                return true;
            }

            Sound sound = Sound.valueOf(strings[0]);
            player.playSound(player.getLocation(), sound, 1.0f, 1.0f);
            player.sendMessage(Util.success + "Som " + sound + " tocado com sucesso!");

            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("sound")) {
            if (strings.length == 1) {
                final String name = strings[0].toUpperCase();

                return Arrays.stream(Sound.values())
                        .map(Enum::name)
                        .filter(named -> named.startsWith(name))
                        .collect(Collectors.toList());
            }
        }
        return Collections.emptyList();
    }
}
