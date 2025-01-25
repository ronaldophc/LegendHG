package com.ronaldophc.feature.punish.mute;

import com.ronaldophc.database.MuteRepository;
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

public class MuteService {

    public MuteService() {
    }

    public boolean mute(Mute mute) {
        return MuteRepository.mute(String.valueOf(mute.getUuid()), mute.getEnd_time(), mute.getMuted_by(), mute.getReason());
    }

    public boolean unmute(UUID uuid) {
        return MuteRepository.unMute(String.valueOf(uuid));
    }

    public boolean isMuted(UUID uuid) {
        return MuteRepository.isMuted(String.valueOf(uuid));
    }

    public String getExpire_atFormated(UUID uuid) {
        long duration = MuteRepository.getMuteEndTime(String.valueOf(uuid));
        return PunishHelper.formatTimeYear(duration);
    }

    public List<Mute> getMuteHistory(UUID uuid) {
        return MuteRepository.getMuteHistory(String.valueOf(uuid));
    }

    public Mute getActiveMute(UUID uuid) {
        return MuteRepository.getActiveMute(String.valueOf(uuid));
    }

    public void openMuteHistoryInventory(Player player, UUID uuid) {
        List<Mute> muteHistory = getMuteHistory(uuid);
        String name = PlayerService.getNameByUUID(uuid);
        Inventory inventory;
        inventory = Bukkit.createInventory(null, 54, Util.color3 + "§lMute " + name);


        for (Mute mute : muteHistory) {
            ItemStack paper = new ItemStack(Material.EMPTY_MAP);
            if (mute.isActive()) {
                paper = new ItemStack(Material.MAP);
            }
            ItemMeta meta = getItemMeta(mute, paper);
            paper.setItemMeta(meta);
            inventory.addItem(paper);
        }

        player.openInventory(inventory);
    }

    @NotNull
    private static ItemMeta getItemMeta(Mute mute, ItemStack paper) {
        ItemMeta meta = paper.getItemMeta();
        meta.setDisplayName("Mute Info");
        List<String> lore = Arrays.asList(
                "§cEm: §a" + mute.getMuted_atFormated(),
                "§cPor: §a" + mute.getMuted_by(),
                "§cMotivo §a" + mute.getReason(),
                "§cDuração: §a" + mute.getDurationFormated(),
                mute.isExpired() ? "§cExpirou: §a" + mute.getExpire_atFormated() : "§cExpira: §a" + mute.getExpire_atFormated(),
                "§cAtivo: §a" + mute.isActive()
        );
        meta.setLore(lore);
        return meta;
    }
}
