package com.ronaldophc.kits.registry;

import com.ronaldophc.kits.Kit;
import com.ronaldophc.util.ItemManager;
import com.ronaldophc.util.Util;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;

import java.util.Arrays;

public class Grandpa extends Kit {
    public Grandpa() {
        super("Grandpa",
                "legendhg.kits.grandpa",
                new ItemManager(Material.STICK, Util.color3 + "Grandpa")
                        .addEnchantment(Enchantment.KNOCKBACK, 2)
                        .setLore(Arrays.asList(Util.success + "Ganhe um graveto", Util.success + "com knockback."))
                        .build(),
                new ItemManager(Material.STICK, Util.color3 + "Grandpa")
                        .addEnchantment(Enchantment.KNOCKBACK, 2)
                        .setUnbreakable()
                        .build(),
                false);
    }
}
