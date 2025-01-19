package com.ronaldophc.kits;

import com.ronaldophc.util.ItemManager;
import com.ronaldophc.util.Util;
import org.bukkit.Material;

import java.util.Arrays;

public class Example extends Kit {

    public Example() {
        super("Example",
                "legendhg.kits.example",
                new ItemManager(Material.AIR, Util.color3 + "Example")
                        .setLore(Arrays.asList(Util.success + "Exemplo", Util.success + "de Kit"))
                        .build(),
                null,
                false);
    }

}
