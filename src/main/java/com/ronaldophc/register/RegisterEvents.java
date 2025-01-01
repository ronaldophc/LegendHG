package com.ronaldophc.register;

import com.ronaldophc.LegendHG;
import com.ronaldophc.kits.manager.guis.KitGuiListener;
import com.ronaldophc.listener.*;
import com.ronaldophc.player.listener.*;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapelessRecipe;
import org.bukkit.material.MaterialData;
import org.bukkit.plugin.PluginManager;

public class RegisterEvents extends LegendHG {

    public static void registerEvents() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(new PlayerEvents(), getInstance());
        pm.registerEvents(new EntityEvents(), getInstance());
        pm.registerEvents(new BlockBreak(), getInstance());
        pm.registerEvents(new BlockPlace(), getInstance());
        pm.registerEvents(new Soup(), getInstance());
        pm.registerEvents(new PlayerDamages(), getInstance());
        pm.registerEvents(new PlayerDeath(), getInstance());
        pm.registerEvents(new Compass(), getInstance());
        pm.registerEvents(new PlayerQuit(), getInstance());
        pm.registerEvents(new KitGuiListener(), getInstance());
        pm.registerEvents(new PlayerJoin(), getInstance());
        pm.registerEvents(new PlayerMove(), getInstance());
        pm.registerEvents(new GameEvents(), getInstance());
        pm.registerEvents(new PlayerLogin(), getInstance());
        pm.registerEvents(new Motd(), getInstance());
        pm.registerEvents(new PlayerCommandPreprocess(), getInstance());
        pm.registerEvents(new PlayerPickupItem(), getInstance());
        pm.registerEvents(new GeneralEvent(), getInstance());
        pm.registerEvents(new AsyncPlayerChat(), getInstance());
        pm.registerEvents(new InventoryClick(), getInstance());
//        try {
//            pm.registerEvents(new AuthListener(), getInstance());
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }

    public static void registerRecipes() {
        ItemStack soup = new ItemStack(Material.MUSHROOM_SOUP);

        ShapelessRecipe melon = new ShapelessRecipe(soup);
        melon.addIngredient(1, Material.MELON);
        melon.addIngredient(1, Material.BOWL);

        ShapelessRecipe melon_seed = new ShapelessRecipe(soup);
        melon_seed.addIngredient(1, Material.MELON_SEEDS);
        melon_seed.addIngredient(1, Material.BOWL);

        ShapelessRecipe cactus = new ShapelessRecipe(soup);
        cactus.addIngredient(1, Material.CACTUS);
        cactus.addIngredient(1, Material.BOWL);

        ShapelessRecipe cocoa = new ShapelessRecipe(soup);
        cocoa.addIngredient(1, new MaterialData(Material.INK_SACK, (byte) 3));
        cocoa.addIngredient(1, Material.BOWL);

        Bukkit.getServer().addRecipe(melon);
        Bukkit.getServer().addRecipe(melon_seed);
        Bukkit.getServer().addRecipe(cocoa);
        Bukkit.getServer().addRecipe(cactus);
    }
}
