package com.ronaldophc.command.admin;

import com.ronaldophc.util.Util;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TeleportCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("teleport")) {
            if (!(commandSender instanceof Player)) {
                commandSender.sendMessage(Util.noConsole);
                return true;
            }

            Player player = (Player) commandSender;
            if (!commandSender.isOp() && !commandSender.hasPermission("legendhg.admin.teleport")) {
                commandSender.sendMessage(Util.noPermission);
                return true;
            }

            if (strings.length < 1) {
                player.sendMessage(Util.usage("/teleport <player>"));
                return true;
            }

            if (strings.length == 1) {
                Player target = player.getServer().getPlayer(strings[0]);
                if (target == null) {
                    player.sendMessage(Util.error + "Player não encontrado.");
                    return true;
                }

                player.teleport(target);
                player.sendMessage("§7Teleportado para §f" + target.getName());
                return true;
            }

            if (strings.length == 2) {
                Player target = player.getServer().getPlayer(strings[0]);
                Player target2 = player.getServer().getPlayer(strings[1]);
                if (target == null || target2 == null) {
                    player.sendMessage(Util.error + "Player não encontrado.");
                    return true;
                }

                target.teleport(target2);
                player.sendMessage("§7Teleportado §f" + target.getName() + " §7para §f" + target2.getName());
                return true;
            }

            if (strings.length == 3) {
                float target = Float.parseFloat(strings[0]);
                float target2 = Float.parseFloat(strings[1]);
                float target3 = Float.parseFloat(strings[2]);

                Location location = new Location(player.getWorld(), target, target2, target3);
                player.teleport(location);
                player.sendMessage("§7Teleportado para §f" + target + " " + target2 + " " + target3);
                return true;
            }

            return true;
        }
        return true;
    }
}
