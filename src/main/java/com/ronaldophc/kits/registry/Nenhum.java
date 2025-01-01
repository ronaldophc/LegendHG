package com.ronaldophc.kits.registry;

import com.ronaldophc.helper.ItemManager;
import com.ronaldophc.helper.Util;
import com.ronaldophc.kits.Kit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class Nenhum extends Kit {

    public Nenhum() {
        super("Nenhum",
                "",
                new ItemManager(Material.DEAD_BUSH, Util.color3 + "Nenhum")
                        .setLore(Arrays.asList(Util.success + "Não faz absolutamente", Util.color3 + Util.bold + "NADA!"))
                        .build(),
                Collections.emptyList(),
                false);
    }

}
