package com.ronaldophc.command;

import com.ronaldophc.constant.MySQL.PlayerField;
import com.ronaldophc.constant.MySQL.Tables;
import com.ronaldophc.database.MySQLManager;
import com.ronaldophc.database.PlayerSQL;
import com.ronaldophc.util.ItemManager;
import com.ronaldophc.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.sql.SQLException;

public class StatsCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("stats")) {
            if (!(commandSender instanceof Player)) {
                commandSender.sendMessage(Util.noConsole);
                return true;
            }

            Player player = (Player) commandSender;

            if (strings.length == 0) {
                openStatsMenu(player, player.getName());
                return true;
            }

            if (strings.length == 1) {
                try {
                    String name = strings[0];

                    boolean isRegistered = PlayerSQL.isPlayerRegisteredByName(name);
                    if (!isRegistered) {
                        player.sendMessage(Util.error + "Jogador não encontrado.");
                        return true;
                    }
                    openStatsMenu(player, name);
                } catch (SQLException e) {
                    player.sendMessage(Util.error + "Ocorreu um erro ao buscar o jogador no banco de dados.");
                    throw new RuntimeException(e);
                }
                return true;
            }
            return true;
        }
        return true;
    }

    private static void openStatsMenu(Player player, String targetName) {

        try {
            String uuid = MySQLManager.getStringByName(targetName, Tables.PLAYER.getTableName(), PlayerField.UUID.getFieldName());

            int deaths = MySQLManager.getInt(uuid, Tables.PLAYER.getTableName(), PlayerField.DEATHS.getFieldName());
            int kills = MySQLManager.getInt(uuid, Tables.PLAYER.getTableName(), PlayerField.KILLS.getFieldName());
            int wins = MySQLManager.getInt(uuid, Tables.PLAYER.getTableName(), PlayerField.WINS.getFieldName());

            Inventory stats = Bukkit.createInventory(player, 3 * 9, Util.color2 +"Status §7» " + Util.color1 + targetName);

            ItemStack death = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
            SkullMeta deathMeta = (SkullMeta) death.getItemMeta();
            deathMeta.setOwner(targetName);
            deathMeta.setDisplayName(Util.color1 + "Deaths §7» §c" + deaths);
            death.setItemMeta(deathMeta);

            ItemStack kill = new ItemManager(Material.DIAMOND_SWORD, Util.color1 + "Kills §7» §a" + kills).build();
            ItemStack win = new ItemManager(Material.EMPTY_MAP, Util.color1 + "Wins §7» §6" + wins).build();
            stats.setItem(12, kill);
            stats.setItem(13, death);
            stats.setItem(14, win);

            player.openInventory(stats);
        } catch (SQLException e) {
            player.sendMessage(Util.error + "Ocorreu um erro ao buscar as informações no banco de dados.");
            throw new RuntimeException(e);
        }

    }

}
