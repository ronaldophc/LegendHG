package com.ronaldophc.command;

import com.ronaldophc.LegendHG;
import com.ronaldophc.feature.Schematic;
import com.ronaldophc.util.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SchematicCommand implements CommandExecutor, TabCompleter {

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("lschematic")) {
            if (strings.length == 1) {
                List<String> bo2Files = new ArrayList<>();
                File pluginFolder = LegendHG.getInstance().getDataFolder();
                File[] files = pluginFolder.listFiles((dir, name) -> name.toLowerCase().endsWith(".schematic"));

                if (files != null) {
                    for (File file : files) {
                        bo2Files.add(file.getName().replace(".schematic", ""));
                    }
                }

                return bo2Files;
            }
        }
        return Collections.emptyList();
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("lschematic")) {
            if (!(commandSender instanceof Player)) {
                commandSender.sendMessage(Util.noConsole);
                return true;
            }

            Player player = (Player) commandSender;

            if (!(player.isOp())) {
                player.sendMessage(Util.noPermission);
                return true;
            }


            if (strings.length != 1) {
                commandSender.sendMessage(Util.usage("/schematic <name>"));
                return true;
            }

            File schematicFile = new File(LegendHG.getInstance().getDataFolder(), strings[0] + ".schematic");
            if (!schematicFile.exists()) {
                player.sendMessage(Util.admin + "§cSchematic not found!");
                return true;
            }
            Schematic.getInstance().createSchematic(player.getWorld(), player.getLocation(), schematicFile);
            player.sendMessage(Util.admin + "§aSchematic loaded!");

            return true;
        }
        return false;
    }
}
