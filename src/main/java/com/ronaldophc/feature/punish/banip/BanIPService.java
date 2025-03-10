package com.ronaldophc.feature.punish.banip;

import com.ronaldophc.database.BanIPRepository;
import com.ronaldophc.feature.punish.PunishHelper;
import com.ronaldophc.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;

import java.net.InetAddress;
import java.util.Arrays;
import java.util.List;

public class BanIPService {

    public BanIPService() {
    }

    public boolean banIP(BanIP ban) {
        return BanIPRepository.banIP(ban.getIp_address().getHostAddress(), ban.getEnd_time(), ban.getBanned_by(), ban.getReason());
    }

    public boolean unbanIP(InetAddress ipAddress) {
        return BanIPRepository.unbanIP(ipAddress.getHostAddress());
    }

    public boolean isIPBanned(InetAddress ipAddress) {
        return BanIPRepository.isIPBanned(ipAddress.getHostAddress());
    }

    public String getExpire_atFormated(InetAddress ipAddress) {
        long duration = BanIPRepository.getBanIPEndTime(ipAddress.getHostAddress());
        return PunishHelper.formatTimeYear(duration);
    }

    public List<BanIP> getBanIPHistory(InetAddress ipAddress) {
        return BanIPRepository.getBanIPHistory(ipAddress.getHostAddress());
    }

    public BanIP getActiveBanIP(InetAddress ipAddress) {
        return BanIPRepository.getActiveBanIP(ipAddress.getHostAddress());
    }

    public void openBanIpHistoryInventory(Player player, InetAddress ipAddress) {
        List<BanIP> banHistory = getBanIPHistory(ipAddress);
        Inventory inventory = Bukkit.createInventory(null, 54, Util.color3 + "§lBanIP " + ipAddress.getHostAddress());

        for (BanIP ban : banHistory) {
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
    private static ItemMeta getItemMeta(BanIP ban, ItemStack paper) {
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
