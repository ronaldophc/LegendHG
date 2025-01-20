package com.ronaldophc.command;

import com.ronaldophc.util.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;

public class HourCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("hour")) {

            commandSender.sendMessage(Util.admin + "Hora atual: " + new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(System.currentTimeMillis()));

            return true;
        }
        return true;
    }
}
