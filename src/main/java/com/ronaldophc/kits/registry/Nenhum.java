package com.ronaldophc.kits.registry;

import com.ronaldophc.util.ItemManager;
import com.ronaldophc.util.Util;
import com.ronaldophc.kits.Kit;
import org.bukkit.Material;

import java.util.Arrays;

public class Nenhum extends Kit {

    public Nenhum() {
        super("Nenhum",
                "",
                new ItemManager(Material.BOOK_AND_QUILL, Util.color3 + "Nenhum")
                        .setLore(Arrays.asList(Util.success + "Não faz absolutamente", Util.color3 + Util.bold + "NADA!"))
                        .build(),
                null,
                false);
    }

}
