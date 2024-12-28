package com.ronaldophc.helper;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.material.MaterialData;

public class ItemManager {
    private String name;
    private List<String> lore = new ArrayList<>();
    private Material material;
    private final ItemStack item;
    private final ItemMeta itemMeta;
    private boolean hasPermission;
    private String permission;
    private List<PotionEffect> potionEffects = new ArrayList<>();

    public ItemManager(Material material, String name) {
        this.material = material;
        this.name = name;
        this.item = new ItemStack(material);
        this.itemMeta = item.getItemMeta();
        this.itemMeta.setDisplayName(name);
    }

    @SuppressWarnings("deprecation")
    public ItemManager setData(byte data) {
        item.setData(new MaterialData(material, data));
        return this;
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void resetLore() {
        lore.clear();
    }

    public ItemManager addLore(String lore) {
        this.lore.add("§f" + lore);
        return this;
    }

    public ItemManager setAmount(int amount) {
        item.setAmount(amount);
        return this;
    }

    public void addPermission(String permission) {
        this.hasPermission = true;
        this.permission = permission;
    }

    public void removePermission() {
        this.hasPermission = false;
        this.permission = null;
    }

    public ItemManager setDurability(int durability) {
        item.setDurability((short) durability);
        return this;
    }

    public ItemManager addEnchantment(Enchantment enchantment, int level) {
        itemMeta.addEnchant(enchantment, level, true);
        return this;
    }

    public ItemManager addUnsafeEnchantment(Enchantment enchantment, int level) {
        item.addUnsafeEnchantment(enchantment, level);
        return this;
    }

    public ItemManager setUnbreakable() {
        itemMeta.spigot().setUnbreakable(true);
        return this;
    }

    public ItemManager addPotionEffect(PotionEffectType effectType, int duration, int amplifier) {
        potionEffects.add(new PotionEffect(effectType, duration, amplifier));
        return this;
    }

    public ItemStack build() {
        itemMeta.setLore(lore);
        item.setType(material);
        itemMeta.setDisplayName(name);
        if (itemMeta instanceof PotionMeta) {
            PotionMeta potionMeta = (PotionMeta) itemMeta;
            for (PotionEffect effect : potionEffects) {
                potionMeta.addCustomEffect(effect, true);
            }
        }
        item.setItemMeta(itemMeta);
        return item;
    }

    public ItemStack build(Player player) {
        if (!hasPermission || player.hasPermission(permission)) {
            return build();
        }
        return new ItemStack(Material.AIR);
    }

    public ItemManager setLore(List<String> lore) {
        this.lore = new ArrayList<>();
        for (String line : lore) {
            addLore(line);
        }
        return this;
    }
}