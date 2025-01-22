package com.ronaldophc.feature.punish.ban;

import com.ronaldophc.database.BanRepository;
import com.ronaldophc.feature.punish.PunishHelper;
import com.ronaldophc.player.PlayerService;
import com.ronaldophc.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class BanService {

    public BanService() {
    }

    public boolean ban(Ban ban) {
        return BanRepository.ban(ban.getUuid(), ban.getEnd_time(), ban.getBanned_by(), ban.getReason());
    }

    public boolean unban(UUID uuid) {
        return BanRepository.unban(uuid);
    }

    public boolean isBanned(UUID uuid) {
        return BanRepository.isBanned(uuid);
    }

    public String getExpire_atFormated(UUID uuid) {
        long duration = BanRepository.getBanEndTime(uuid);
        return PunishHelper.formatTimeYear(duration);
    }

    public List<Ban> getBanHistory(UUID uuid) {
        return BanRepository.getBanHistory(uuid);
    }

    public Ban getActiveBan(UUID uuid) {
        return BanRepository.getActiveBan(uuid);
    }

    public void openBanHistoryInventory(Player player, UUID uuid) {
        List<Ban> banHistory = getBanHistory(uuid);
        String name = PlayerService.getNameByUUID(uuid);
        Inventory inventory = Bukkit.createInventory(null, 54, Util.color3 + "§lBan " + name);

        for (Ban ban : banHistory) {
            ItemStack paper = new ItemStack(Material.EMPTY_MAP);
            if (ban.isActive()) {
                paper = new ItemStack(Material.MAP);
            }
            ItemMeta meta = getItemMeta(ban, paper);
            paper.setItemMeta(meta);
            inventory.addItem(paper);
        }

        player.openInventory(inventory);
    }

    @NotNull
    private static ItemMeta getItemMeta(Ban ban, ItemStack paper) {
        ItemMeta meta = paper.getItemMeta();
        meta.setDisplayName("Ban Info");
        List<String> lore = Arrays.asList(
                "§cEm: §a" + ban.getBanned_atFormated(),
                "§cPor: §a" + ban.getBanned_by(),
                "§cMotivo §a" + ban.getReason(),
                "§cDuração: §a" + ban.getDurationFormated(),
                ban.isExpired() ? "§cExpirou: §a" + ban.getExpire_atFormated() : "§cExpira: §a" + ban.getExpire_atFormated(),
                "§cAtivo: §a" + ban.isActive()
        );
        meta.setLore(lore);
        return meta;
    }
}
