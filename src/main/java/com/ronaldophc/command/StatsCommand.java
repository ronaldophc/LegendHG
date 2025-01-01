package com.ronaldophc.command;

import com.ronaldophc.database.PlayerSQL;
import com.ronaldophc.helper.ItemManager;
import com.ronaldophc.helper.Util;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.sql.SQLException;
import java.util.UUID;

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
                openStatsMenu(player, player.getUniqueId());
                return true;
            }

            if (strings.length == 1) {
                try {
                    UUID target = PlayerSQL.getUUIDByName(strings[0]);
                    if (target == null) {
                        player.sendMessage(Util.error + "Jogador não encontrado.");
                        return true;
                    }
                    openStatsMenu(player, target);
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

    private static void openStatsMenu(Player player, UUID target) {

        try {
            String name = PlayerSQL.getNameByUUID(target);
            Inventory stats = Bukkit.createInventory(player, 3 * 9, Util.color2 +"Status §7» " + Util.color1 + name);

            ItemStack deaths = new ItemStack(Material.SKULL_ITEM, 1, (short) 3);
            SkullMeta deathsMeta = (SkullMeta) deaths.getItemMeta();
            deathsMeta.setOwner(name);
            deathsMeta.setDisplayName(Util.color1 + "Deaths §7» §c" + PlayerSQL.getPlayerDeaths(target));
            deaths.setItemMeta(deathsMeta);

            ItemStack kills = new ItemManager(Material.DIAMOND_SWORD, Util.color1 + "Kills §7» §a" + PlayerSQL.getPlayerKills(target)).build();
            ItemStack wins = new ItemManager(Material.EMPTY_MAP, Util.color1 + "Wins §7» §6" + PlayerSQL.getPlayerWins(target)).build();
            stats.setItem(12, kills);
            stats.setItem(13, deaths);
            stats.setItem(14, wins);

            player.openInventory(stats);
        } catch (SQLException e) {
            player.sendMessage(Util.error + "Ocorreu um erro ao buscar as informações no banco de dados.");
            throw new RuntimeException(e);
        }

    }

}
