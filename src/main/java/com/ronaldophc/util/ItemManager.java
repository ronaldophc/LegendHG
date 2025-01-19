package com.ronaldophc.util;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.material.MaterialData;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.List;

public class ItemManager {
    @Setter
    private String name;
    private List<String> lore = new ArrayList<>();
    @Getter
    @Setter
    private Material material;
    private final ItemStack item;
    private final ItemMeta itemMeta;
    private final List<PotionEffect> potionEffects = new ArrayList<>();

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

    public void addLore(String lore) {
        this.lore.add("§f" + lore);
    }

    public ItemManager setAmount(int amount) {
        item.setAmount(amount);
        return this;
    }

    public ItemManager setDurability(int durability) {
        item.setDurability((short) durability);
        return this;
    }

    public ItemManager addEnchantment(Enchantment enchantment, int level) {
        itemMeta.addEnchant(enchantment, level, true);
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

    public ItemManager setLore(List<String> lore) {
        this.lore = new ArrayList<>();
        for (String line : lore) {
            addLore(line);
        }
        return this;
    }

    public ItemManager setSkullOwner(String owner) {
        if (item.getType() == Material.SKULL_ITEM && item.getDurability() == 3) {
            SkullMeta skullMeta = (SkullMeta) itemMeta;
            skullMeta.setOwner(owner);
            item.setItemMeta(skullMeta);
        }
        return this;
    }

}