package com.ronaldophc.kits.registry;

import com.ronaldophc.helper.ItemManager;
import com.ronaldophc.helper.Util;
import com.ronaldophc.kits.Kit;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.Arrays;
import java.util.Random;

public class Hermit extends Kit {

    public Hermit() {
        super("Hermit",
                "legendhg.kits.hermit",
                new ItemManager(Material.GRASS, Util.color3 + "Hermit")
                        .setLore(Arrays.asList(Util.success + "Ao iniciar a partida", Util.success + "seja teleportado para borda do mapa."))
                        .build(),
                null,
                false);
    }

    @Override
    public void apply(Player player) {
        super.apply(player);
        teleportHermit(player);
    }

    public static void teleportHermit(Player player) {
        Random random = new Random();
        int x = random.nextBoolean() ? random.nextInt(150) + 200 : -(random.nextInt(150) + 200);
        int z = random.nextBoolean() ? random.nextInt(150) + 200 : -(random.nextInt(150) + 200);
        World world = Bukkit.getWorld("world");
        int y = world.getHighestBlockYAt(x, z) + 5;
        Location spawnLocation =  new Location(world, x, y, z);
        player.teleport(spawnLocation);
    }
}
