package com.ronaldophc.game;

import com.ronaldophc.setting.Settings;
import lombok.Getter;
import org.bukkit.*;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;

import java.util.Random;

public class GameHelper {

    @Getter
    private static final GameHelper instance = new GameHelper();
    @Getter
    private int kits;

    private GameHelper() {
        this.kits = Settings.getInstance().getInt("Kits");
        defineType();
    }

    private void defineType() {
        if (kits == 3) {
            kits = new Random().nextInt(2) + 1;
        }
    }

    public boolean isTwoKits() {
        return kits == 2;
    }

    public static void createCakePlatform(Player player) {
        Location loc = player.getLocation();
        World world = loc.getWorld();
        int platformY = world.getHighestBlockYAt(loc) + 30;

        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                Location glassLoc = loc.clone().add(x, platformY - loc.getY(), z);
                world.getBlockAt(glassLoc).setType(Material.GLASS);
                world.getBlockAt(glassLoc.add(0, 1, 0)).setType(Material.CAKE_BLOCK);
            }
        }
        player.teleport(loc.add(0, platformY - loc.getY() + 2, 0));
        launchFireworks(player);
    }

    public static void launchFireworks(Player player) {
        World world = player.getWorld();
        Location loc = player.getLocation();
        for (int i = 0; i < 10; i++) {
            Firework firework = world.spawn(loc, Firework.class);
            FireworkMeta meta = firework.getFireworkMeta();
            meta.addEffect(FireworkEffect.builder().withColor(Color.RED, Color.BLUE).with(FireworkEffect.Type.BALL).trail(true).flicker(true).build());
            meta.setPower(1);
            firework.setFireworkMeta(meta);
        }
    }

}
