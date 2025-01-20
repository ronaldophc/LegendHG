package com.ronaldophc.kits.registry;

import com.ronaldophc.kits.Kit;
import com.ronaldophc.util.ItemManager;
import com.ronaldophc.util.Util;
import org.bukkit.Material;
import org.bukkit.potion.PotionEffectType;

import java.util.Arrays;

public class Urgal extends Kit {
    public Urgal() {
        super("Urgal",
                "legendhg.kits.urgal",
                new ItemManager(Material.POTION, Util.color3 + "Urgal")
                        .setLore(Arrays.asList(Util.success + "Inicie a partida com", Util.success + "3 poções de força!"))
                        .setDurability(8201)
                        .addPotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 10, 1)
                        .build(),
                new ItemManager(Material.POTION, Util.color3 + "Urgal")
                        .setDurability(8201)
                        .addPotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 10, 1)
                        .setAmount(3)
                        .build(),
                false);
    }
}
