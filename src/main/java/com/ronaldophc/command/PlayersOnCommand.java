package com.ronaldophc.command;

import com.ronaldophc.player.PlayerAliveManager;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class PlayersOnCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("playerson")) {
            commandSender.sendMessage("Players online: " + PlayerAliveManager.getInstance().getPlayersAlive().size());

            return true;
        }
        return false;
    }
}
