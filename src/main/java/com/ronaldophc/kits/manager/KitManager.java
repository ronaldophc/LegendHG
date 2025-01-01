package com.ronaldophc.kits.manager;

import com.ronaldophc.LegendHG;
import com.ronaldophc.constant.Kits;
import com.ronaldophc.helper.Logger;
import com.ronaldophc.helper.TitleHelper;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

public class KitManager {

    private final static KitManager INSTANCE = new KitManager();

    public static HashMap<UUID, Kits> playerKit = new HashMap<>();
    public static HashMap<UUID, Kits> playerKit2 = new HashMap<>();

    public KitManager() {
    }

    public boolean isSetPlayerKit(Player player) {
        return playerKit.containsKey(player.getUniqueId());
    }

    public boolean hasPlayerKit(Player player) {
        return getPlayerKit(player) != Kits.NENHUM;
    }

    public Kits getPlayerKit(Player player) {
        return playerKit.get(player.getUniqueId());
    }

    public String getPlayerKitName(Player player) {
        return getPlayerKit(player).getName();
    }

    public void setPlayerKit(Player player, Kits kit) {
        if (!isSetPlayerKit(player)) {
            playerKit.put(player.getUniqueId(), kit);
            return;
        }
        playerKit.replace(player.getUniqueId(), kit);
    }

    public Kits findKit(String name) {
        for (Kits kit : Kits.values()) {
            if (kit.getName().equalsIgnoreCase(name)) {
                return kit;
            }
        }
        return null;
    }

    public boolean isItemKit(ItemStack item, Kits kit) {
        if (kit.getKitItem().isEmpty()) return false;
        for (ItemStack kitItem : kit.getKitItem()) {
            if (kitItem.equals(item)) {
                return true;
            }
        }
        return false;
    }

    public boolean isItemIconKit(ItemStack item) {
        for (Kits kit : Kits.values()) {
            if (kit.getKitIcon() == null) continue;
            if (kit.getKitIcon().equals(item)) {
                return true;
            }
        }
        return false;
    }

    public boolean kitHasItem(Player player) {
        Kits kit = getPlayerKit(player);
        return !(kit.getKitItem().isEmpty());
    }


    public void removeItemKit(Player player) {
        Kits kit = getPlayerKit(player);
        for (ItemStack item : kit.getKitItem()) {
            if (player.getInventory().contains(item)) {
                player.getInventory().remove(item);
            }
        }
        player.updateInventory();
    }

    // Singleton

    public static KitManager getInstance() {
        return INSTANCE;
    }

    public void addItemKit(Player player) {
        Kits kit = LegendHG.getKitManager().getPlayerKit(player);
        if (kit.getKitItem().isEmpty()) return;
        for (ItemStack item : kit.getKitItem()) {
            player.getInventory().addItem(item);
        }
        player.updateInventory();
    }

    public void addItemKit(Player player, Kits kit) {
        if (kit.getKitItem().isEmpty()) return;
        for (ItemStack item : kit.getKitItem()) {
            player.getInventory().addItem(item);
        }
        player.updateInventory();
    }


    public boolean isThePlayerKit(Player player, Kits kit) {
        return getPlayerKit(player) == kit || getPlayerKit2(player) == kit;
    }

    public int whoPlayerKit(Player player, Kits kit) {
        if (getPlayerKit(player) == kit) {
            return 1;
        }
        if (getPlayerKit2(player) == kit) {
            return 2;
        }
        return 0;
    }

    // PlayerKit2

    public boolean isSetPlayerKit2(Player player) {
        return playerKit2.containsKey(player.getUniqueId());
    }

    public boolean hasPlayerKit2(Player player) {
        return getPlayerKit2(player) != Kits.NENHUM;
    }

    public Kits getPlayerKit2(Player player) {
        return playerKit2.get(player.getUniqueId());
    }

    public String getPlayerKitName2(Player player) {
        Kits kit = getPlayerKit2(player);
        return kit.getName();
    }

    public void setPlayerKit2(Player player, Kits kit) {
        if (!isSetPlayerKit2(player)) {
            playerKit2.put(player.getUniqueId(), kit);
            return;
        }
        if (!hasPlayerKit2(player)) {
            playerKit2.replace(player.getUniqueId(), kit);
            return;
        }
        playerKit2.replace(player.getUniqueId(), kit);
    }

    public void removeItemKit2(Player player) {
        Kits kit = getPlayerKit2(player);
        for (ItemStack item : kit.getKitItem()) {
            if (player.getInventory().contains(item)) {
                player.getInventory().remove(item);
            }
        }
        player.updateInventory();
    }

    public boolean kitHasItem2(Player player) {
        Kits kit = getPlayerKit2(player);
        return !(kit.getKitItem().isEmpty());
    }


    public void addItemKit2(Player player) {
        Kits kit = LegendHG.getKitManager().getPlayerKit2(player);
        if (kit.getKitItem().isEmpty()) return;
        for (ItemStack item : kit.getKitItem()) {
            player.getInventory().addItem(item);
        }
        player.updateInventory();
    }

    public void setCooldown(Player player, int seconds, Kits kit) {
        try {
            if (whoPlayerKit(player, kit) == 1) {
                if (isOnCooldown(player, kit)) {
                    int cooldown = getCooldown(player, kit);
                    if (cooldown > seconds) return;
                }
                LegendHG.getCooldownKits().setCooldown(player, seconds);
                return;
            }
            if (isOnCooldown(player, kit)) {
                int cooldown = getCooldown(player, kit);
                if (cooldown > seconds) return;
            }
            LegendHG.getCooldownKits2().setCooldown(player, seconds);
        } catch (Exception e) {
            Logger.logError("Função: setCooldown - KitManager.\nPlayer: " + player.getName() + ", kit: " + kit.getName());
        }
    }

    public void addCompass(Player player) {
        if (player.getInventory().contains(Material.COMPASS)) return;
        player.getInventory().addItem(new ItemStack(Material.COMPASS));
        player.updateInventory();
    }

    public boolean isOnCooldown(Player player, Kits kit) {
        try {
            if (whoPlayerKit(player, kit) == 1) {
                if (LegendHG.getCooldownKits().isOnCooldown(player)) {
                    TitleHelper.sendCooldownBar(player, kit);
                    return true;
                }
                return false;
            }
            if (LegendHG.getCooldownKits2().isOnCooldown(player)) {
                TitleHelper.sendCooldownBar(player, kit);
                return true;
            }
            return false;
        } catch (Exception e) {
            Logger.logError("Função: isOnCooldown - KitManager.\nPlayer: " + player.getName() + ", kit: " + kit.getName());
            return false;
        }
    }

    public int getCooldown(Player player, Kits kit) {
        if (whoPlayerKit(player, kit) == 1) {
            return LegendHG.getCooldownKits().getCooldown(player);
        }
        return LegendHG.getCooldownKits2().getCooldown(player);
    }
}
