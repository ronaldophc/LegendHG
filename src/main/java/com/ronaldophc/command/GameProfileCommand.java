package com.ronaldophc.command;

import com.mojang.authlib.GameProfile;
import com.ronaldophc.helper.MasterHelper;
import com.ronaldophc.helper.Util;
import com.ronaldophc.hook.MojangHook;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class GameProfileCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("gameprofile")) {

            if (!(commandSender instanceof Player)) {
                commandSender.sendMessage(Util.noConsole);
                return true;
            }

            if (!commandSender.isOp()) {
                commandSender.sendMessage(Util.noPermission);
                return true;
            }

            Player player = (Player) commandSender;
            MasterHelper.refreshPlayer(player);
            return true;
        }
        return false;
    }
}
