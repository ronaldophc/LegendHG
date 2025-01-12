package com.ronaldophc.api.nbt;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class NBTManager {

    public static ItemStack addNBTTag(ItemStack item, String key, String value) {
        NBTItem nbtItem = new NBTItem(item);
        nbtItem.setString(key, value);
        return nbtItem.getItem();
    }

    public static String getNBTTag(ItemStack item, String key) {
        NBTItem nbtItem = new NBTItem(item);
        return nbtItem.getString(key);
    }

    public static ItemStack removeNBTTag(ItemStack item, String key) {
        NBTItem nbtItem = new NBTItem(item);
        nbtItem.removeTag(key);
        return nbtItem.getItem();
    }

    public static boolean hasNBTTag(ItemStack item, String key) {
        NBTItem nbtItem = new NBTItem(item);
        return nbtItem.hasTag(key);
    }

    // -------------- Example ------------------ //

        ItemStack item = new ItemStack(Material.DIAMOND_SWORD);

//        // Adicionar uma tag NBT
//        item = NBTManager.addNBTTag(item, "CustomTag", "MyCustomValue");
//
//        // Ler uma tag NBT
//        String value = NBTManager.getNBTTag(item, "CustomTag");
//        System.out.println("CustomTag value: " + value);
//
//        // Verificar se uma tag NBT existe
//        boolean hasTag = NBTManager.hasNBTTag(item, "CustomTag");
//        System.out.println("Has CustomTag: " + hasTag);
//
//        // Remover uma tag NBT
//        item = NBTManager.removeNBTTag(item, "CustomTag");
//        hasTag = NBTManager.hasNBTTag(item, "CustomTag");
//        System.out.println("Has CustomTag after removal: " + hasTag);

}
