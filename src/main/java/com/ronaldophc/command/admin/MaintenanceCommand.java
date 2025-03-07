package com.ronaldophc.command.admin;

import com.ronaldophc.LegendHG;
import com.ronaldophc.util.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class MaintenanceCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("maintenance")) {
            if (!commandSender.isOp() && !commandSender.hasPermission("legendhg.admin.maintenance")) {
                commandSender.sendMessage(Util.noPermission);
                return true;
            }

            if (strings.length == 0) {
                commandSender.sendMessage(Util.usage("/maintenance <on/off>"));
                String message = "§cManutenção está §a§lativado";
                if (!LegendHG.settings.getBoolean("Maintenance")) {
                    message = "§cManutenção está §c§ldesativado";
                }
                commandSender.sendMessage(message);
                return true;
            }

            if (strings[0].equalsIgnoreCase("on")) {
                LegendHG.settings.set("Maintenance", true);
                commandSender.sendMessage("§cManutenção ativado com sucesso!");
                return true;
            }

            if (strings[0].equalsIgnoreCase("off")) {
                LegendHG.settings.set("Maintenance", false);
                commandSender.sendMessage("§cManutenção desativado com sucesso!");
                return true;
            }

            return true;
        }
        return false;
    }
}
