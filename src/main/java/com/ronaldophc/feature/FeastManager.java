package com.ronaldophc.feature;

import com.ronaldophc.LegendHG;
import com.ronaldophc.helper.Util;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.*;

public class FeastManager {

    public enum states {
        COMPLETED, SPAWNED, NOT_SPAWNED
    }

    int size = 40;
    int time = 60;
    private static states state;
    Location location;
    BukkitTask task;
    Random random = new Random();

    List<ItemStack> ironItems;

    List<ItemStack> diamondItems;

    List<ItemStack> potionItems;

    List<ItemStack> singleItems;

    public FeastManager() {
        state = states.NOT_SPAWNED;
        location = getRandomSpawnLocation();
    }

    public void start() {
        if (state != states.NOT_SPAWNED) {
            return;
        }
        Bukkit.broadcastMessage(Util.color1 + "O Feast irá spawnar em §f" + Util.formatSeconds(time) + Util.color1 + "! X: §f" + location.getBlockX() + Util.color1 + " Y: §f" + location.getBlockY() + Util.color1 + " Z: §f" + location.getBlockZ());
        state = states.SPAWNED;
        createCylinder();
        run();
    }

    public void run() {
        task = new BukkitRunnable() {
            @Override
            public void run() {
                time--;
                switch(time) {
                    case 45:
                    case 30:
                    case 15:
                    case 10:
                    case 5:
                    case 4:
                    case 3:
                    case 2:
                    case 1:
                        Bukkit.broadcastMessage(Util.color1 + "O Feast irá spawnar em §f" + Util.formatSeconds(time) + Util.color1 + "! X: §f" + location.getBlockX() + Util.color1 + " Y: §f" + location.getBlockY() + Util.color1 + " Z: §f" + location.getBlockZ());
                }
                if (time <= 0) {
                    spawnChests();
                }
            }
        }.runTaskTimer(LegendHG.getInstance(), 0, 20);
    }

    public void spawnChests() {
        state = states.COMPLETED;
        createBlocks();
        Bukkit.broadcastMessage(Util.color1 + "O Feast spawnou!");
        if (task != null) {
            task.cancel();
        }
    }

    public void createCylinder() {
        int centerX = location.getBlockX();
        int centerY = location.getBlockY();
        int centerZ = location.getBlockZ();
        int radius = size / 2;

        for (int y = centerY; y < centerY + size; y++) {
            for (int x = centerX - radius; x <= centerX + radius; x++) {
                for (int z = centerZ - radius; z <= centerZ + radius; z++) {
                    if (Math.pow(x - centerX, 2) + Math.pow(z - centerZ, 2) <= Math.pow(radius, 2)) {
                        Block block = location.getWorld().getBlockAt(x, y, z);
                        if (y == centerY) {
                            block.setType(Material.GRASS);
                            continue;
                        }
                        block.setType(Material.AIR);
                    }
                }
            }
        }
    }

    public void createBlocks() {
        location.clone().add(0, 1, 0).getBlock().setType(Material.ENCHANTMENT_TABLE);
        List<Location> chestLocations = Arrays.asList(
                location.clone().add(1.0, 1.0, 1.0),
                location.clone().add(-1.0, 1.0, 1.0),
                location.clone().add(-1.0, 1.0, -1.0),
                location.clone().add(1.0, 1.0, -1.0),
                location.clone().add(2.0, 1.0, 2.0),
                location.clone().add(0.0, 1.0, 2.0),
                location.clone().add(-2.0, 1.0, 2.0),
                location.clone().add(2.0, 1.0, 0.0),
                location.clone().add(-2.0, 1.0, 0.0),
                location.clone().add(2.0, 1.0, -2.0),
                location.clone().add(0.0, 1.0, -2.0),
                location.clone().add(-2.0, 1.0, -2.0)
        );

        resetArrays();

        chestLocations.forEach(loc -> {
            loc.getBlock().setType(Material.AIR);
            loc.getBlock().setType(Material.CHEST);
            Chest chest = (Chest) loc.getBlock().getState();
            Inventory inventory = chest.getBlockInventory();
            addItemsToChest(inventory);
        });

        location.getWorld().strikeLightningEffect(location.clone().add(0, 1, 0));

        state = states.COMPLETED;
    }

    private void addItemsToChest(Inventory inventory) {
        int potionCount = 0;
        for (Iterator<ItemStack> iterator = potionItems.iterator(); iterator.hasNext(); ) {
            if (potionCount >= 2) {
                break;
            }
            ItemStack item = iterator.next();
            int chance = 16;
            if (random.nextInt(100) < chance) {
                addItemToChest(inventory, item);
                iterator.remove();
                potionCount++;
            }
        }

        int ironCount = 0;
        for (Iterator<ItemStack> iterator = ironItems.iterator(); iterator.hasNext(); ) {
            if (ironCount >= 3) {
                break;
            }
            ItemStack item = iterator.next();
            int chance = 16;
            if (random.nextInt(100) < chance) {
                addItemToChest(inventory, item);
                iterator.remove();
                ironCount++;
            }
        }

        Collections.shuffle(singleItems);

        int singleCount = 0;
        int min = 3;
        int max = random.nextInt(1) + min;
        for (ItemStack singleItem : singleItems) {
            if (singleCount >= max) {
                break;
            }

            addItemToChest(inventory, singleItem);
            singleCount++;
        }

        int diamondCount = 0;
        for (Iterator<ItemStack> iterator = diamondItems.iterator(); iterator.hasNext(); ) {
            if (diamondCount >= 3) {
                break;
            }
            ItemStack item = iterator.next();
            int chance = 16;
            if (random.nextInt(100) < chance) {
                addItemToChest(inventory, item);
                iterator.remove();
                diamondCount++;
            }
        }

    }

    private static Location getRandomSpawnLocation() {
        int range = 100;
        int spawnX = new Random().nextInt(range) - (range / 2);
        int spawnZ = new Random().nextInt(range) - (range / 2);
        World world = Bukkit.getWorld("world");
        int y = world.getHighestBlockYAt(spawnX, spawnZ);
        return new Location(world, spawnX, y + 1, spawnZ);
    }

    public Location getLocation() {
        return location;
    }

    public states getState() {
        return state;
    }

    public void resetArrays() {
        ironItems = new ArrayList<>(Arrays.asList(
                new ItemStack(Material.IRON_HELMET, 1),
                new ItemStack(Material.IRON_CHESTPLATE, 1),
                new ItemStack(Material.IRON_LEGGINGS, 1),
                new ItemStack(Material.IRON_BOOTS, 1),
                new ItemStack(Material.IRON_SWORD, 1),
                new ItemStack(Material.IRON_AXE, 1),
                new ItemStack(Material.IRON_PICKAXE, 1),

                new ItemStack(Material.IRON_HELMET, 1),
                new ItemStack(Material.IRON_CHESTPLATE, 1),
                new ItemStack(Material.IRON_LEGGINGS, 1),
                new ItemStack(Material.IRON_BOOTS, 1),
                new ItemStack(Material.IRON_SWORD, 1),
                new ItemStack(Material.IRON_SWORD, 1),
                new ItemStack(Material.IRON_AXE, 1),
                new ItemStack(Material.IRON_PICKAXE, 1)
        ));

        diamondItems = new ArrayList<>(Arrays.asList(
                new ItemStack(Material.DIAMOND_HELMET, 1),
                new ItemStack(Material.DIAMOND_CHESTPLATE, 1),
                new ItemStack(Material.DIAMOND_LEGGINGS, 1),
                new ItemStack(Material.DIAMOND_BOOTS, 1),
                new ItemStack(Material.DIAMOND_SWORD, 1),
                new ItemStack(Material.DIAMOND_AXE, 1),
                new ItemStack(Material.DIAMOND_PICKAXE, 1),

                new ItemStack(Material.DIAMOND_HELMET, 1),
                new ItemStack(Material.DIAMOND_CHESTPLATE, 1),
                new ItemStack(Material.DIAMOND_LEGGINGS, 1),
                new ItemStack(Material.DIAMOND_BOOTS, 1),
                new ItemStack(Material.DIAMOND_SWORD, 1)
        ));

        potionItems = new ArrayList<>(Arrays.asList(
                new ItemStack(Material.POTION, 1, (short) 8201),
                new ItemStack(Material.POTION, 1, (short) 16428),
                new ItemStack(Material.POTION, 1, (short) 16421),
                new ItemStack(Material.POTION, 1, (short) 8201),
                new ItemStack(Material.POTION, 1, (short) 16428),
                new ItemStack(Material.POTION, 1, (short) 16421),
                new ItemStack(Material.POTION, 1, (short) 8201),
                new ItemStack(Material.POTION, 1, (short) 16428),
                new ItemStack(Material.POTION, 1, (short) 16421),
                new ItemStack(Material.POTION, 1, (short) 8201),
                new ItemStack(Material.POTION, 1, (short) 16428),
                new ItemStack(Material.POTION, 1, (short) 16421),

                new ItemStack(Material.ANVIL, 1)
        ));

        singleItems = new ArrayList<>(Arrays.asList(
                new ItemStack(Material.GOLDEN_APPLE, random.nextInt(2) + 1),
                new ItemStack(Material.WEB, random.nextInt(4) + 1),
                new ItemStack(Material.FLINT_AND_STEEL, 1),
                new ItemStack(Material.TNT, random.nextInt(4) + 1),
                new ItemStack(Material.ENDER_PEARL, random.nextInt(2) + 1),
                new ItemStack(Material.LAVA_BUCKET, 1),
                new ItemStack(Material.WATER_BUCKET, 1),
                new ItemStack(Material.BOW, 1),
                new ItemStack(Material.OBSIDIAN, random.nextInt(2) + 1),
                new ItemStack(Material.EXP_BOTTLE, random.nextInt(7) + 1),
                new ItemStack(Material.EXP_BOTTLE, random.nextInt(7) + 1),
                new ItemStack(Material.BREAD, random.nextInt(2) + 1),
                new ItemStack(Material.WOOD, random.nextInt(15) + 1),
                new ItemStack(Material.DIAMOND, 1),
                new ItemStack(Material.IRON_INGOT, random.nextInt(4) + 1),
                new ItemStack(Material.BOWL, random.nextInt(15) + 1),
                new ItemStack(Material.ARROW, random.nextInt(15) + 1),
                new ItemStack(Material.MUSHROOM_SOUP, random.nextInt(10) + 1),

                new ItemStack(Material.GOLDEN_APPLE, random.nextInt(2) + 1),
                new ItemStack(Material.WEB, random.nextInt(4) + 1),
                new ItemStack(Material.TNT, random.nextInt(4) + 1),
                new ItemStack(Material.ENDER_PEARL, random.nextInt(2) + 1),
                new ItemStack(Material.LAVA_BUCKET, 1),
                new ItemStack(Material.WATER_BUCKET, 1),
                new ItemStack(Material.OBSIDIAN, random.nextInt(2) + 1),
                new ItemStack(Material.EXP_BOTTLE, random.nextInt(7) + 1),
                new ItemStack(Material.EXP_BOTTLE, random.nextInt(7) + 1),
                new ItemStack(Material.BOWL, random.nextInt(15) + 1),
                new ItemStack(Material.MUSHROOM_SOUP, random.nextInt(10) + 1)

        ));
    }

    public void addItemToChest(Inventory inventory, ItemStack item) {
        int pos = random.nextInt(24);
        while (inventory.getItem(pos) != null) {
            pos = random.nextInt(24);
        }
        inventory.setItem(pos, item);
    }
}
