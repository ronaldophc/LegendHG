package com.ronaldophc.command.admin;

import com.ronaldophc.util.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FlySpeedCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("flyspeed")) {
            if (commandSender == null) return true;
            if (!(commandSender instanceof Player)) {
                commandSender.sendMessage(Util.noConsole);
                return true;
            }

            Player player = (Player) commandSender;

            if (!commandSender.isOp() && !commandSender.hasPermission("legendhg.admin.flyspeed")) {
                commandSender.sendMessage(Util.noPermission);
                return true;
            }

            if (strings.length != 1) {
                commandSender.sendMessage(Util.usage("/flyspeed <speed>"));
                return true;
            }

            try {
                float speed = Float.parseFloat(strings[0]);
                if (speed < -1.0f || speed > 1.0f) {
                    player.sendMessage(Util.error + "A velocidade deve estar entre -1.0 e 1.0");
                    return true;
                }
                player.setFlySpeed(speed);
                player.sendMessage(Util.admin + "Velocidade de voo alterada para " + speed);
                return true;
            } catch (NumberFormatException e) {
                player.sendMessage(Util.error + "A velocidade deve ser um número");
                return true;
            }
        }
        return true;
    }
}
