package com.ronaldophc.kits.manager.guis;

import com.ronaldophc.LegendHG;
import com.ronaldophc.kits.Kit;
import com.ronaldophc.kits.manager.KitManager;
import com.ronaldophc.util.ItemManager;
import com.ronaldophc.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

public class KitGui {

    private static final int ITEMS_PER_PAGE = 45;

    public static void openGui(Player player, int page, int whoKit) {
        int totalPages = getNumberOfPages();

        Inventory inv = Bukkit.createInventory(null, 9 * 6, Util.title + " " + Util.color2 + "Kit " + whoKit);

        KitManager kitManager = LegendHG.getKitManager();
        Kit[] kits = kitManager.getKits();
        int startIndex = (page - 1) * ITEMS_PER_PAGE;
        int endIndex = Math.min(startIndex + ITEMS_PER_PAGE, kits.length);

        for (int i = startIndex; i < endIndex; i++) {
            Kit kit = kits[i];
            if (player.hasPermission(kit.getPermission())) {
                inv.addItem(kit.getKitIcon());
            }
        }

        ItemStack buttonBack = new ItemManager(Material.DOUBLE_PLANT, "Voltar").setData((byte) 0).build();
        ItemStack buttonNext = new ItemManager(Material.DOUBLE_PLANT, "Próximo").setData((byte) 0).build();

        if (page > 1) {
            inv.setItem(45, buttonBack);
        }

        if (page < totalPages) {
            inv.setItem(53, buttonNext);
        }

        player.openInventory(inv);
        player.setMetadata("kitGui", new FixedMetadataValue(LegendHG.getInstance(), page));
        player.setMetadata("whoKit", new FixedMetadataValue(LegendHG.getInstance(), whoKit));
    }

    public static int getNumberOfPages() {
        KitManager kitManager = LegendHG.getKitManager();
        int totalKits = kitManager.getKits().length;
        return (int) Math.ceil((double) totalKits / ITEMS_PER_PAGE);
    }

}
