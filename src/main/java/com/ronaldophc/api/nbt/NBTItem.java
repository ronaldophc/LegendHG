package com.ronaldophc.api.nbt;

import net.minecraft.server.v1_8_R3.NBTTagCompound;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class NBTItem {

    private final net.minecraft.server.v1_8_R3.ItemStack nmsItem;
    private final NBTTagCompound tag;

    public NBTItem(ItemStack item) {
        this.nmsItem = CraftItemStack.asNMSCopy(item);
        this.tag = nmsItem.hasTag() ? nmsItem.getTag() : new NBTTagCompound();
    }

    public ItemStack getItem() {
        nmsItem.setTag(tag);
        return CraftItemStack.asBukkitCopy(nmsItem);
    }

    public void setString(String key, String value) {
        tag.setString(key, value);
    }

    public String getString(String key) {
        return tag.getString(key);
    }

    public void setInt(String key, int value) {
        tag.setInt(key, value);
    }

    public int getInt(String key) {
        return tag.getInt(key);
    }

    public void setBoolean(String key, boolean value) {
        tag.setBoolean(key, value);
    }

    public boolean getBoolean(String key) {
        return tag.getBoolean(key);
    }

    public void removeTag(String key) {
        tag.remove(key);
    }

    public boolean hasTag(String key) {
        return tag.hasKey(key);
    }
}