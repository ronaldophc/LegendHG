package com.ronaldophc.feature;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Chest;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class MiniFeast {

    Location location;
    static Random random = new Random();
    static boolean boolX;
    static boolean boolZ;

    List<ItemStack> items;

    public MiniFeast() {
        boolX = random.nextBoolean();
        boolZ = random.nextBoolean();
        location = getRandomSpawnLocation();
        createStructure();
        String x = "x: 175 e x: 350";
        if (!boolX) {
            x = "x: -175 e x: -350";
        }
        String z = "z: 175 e z: 350";
        if (!boolZ) {
            z = "z: -175 e z: -350";
        }
        Bukkit.broadcastMessage("§cMiniFeast apareceu entre ( " + x + " ) ( " + z + " )");
    }

    private void createStructure() {
        int centerX = location.getBlockX();
        int centerY = location.getBlockY();
        int centerZ = location.getBlockZ();
        for (int x = centerX - 1; x <= centerX + 1; x++) {
            for (int z = centerZ - 1; z <= centerZ + 1; z++) {
                location.getWorld().getBlockAt(x, centerY, z).setType(Material.GLASS);
            }
        }

        createBlocks();
    }

    public void createBlocks() {
        location.add(0, 1, 0).getBlock().setType(Material.ENCHANTMENT_TABLE);
        List<Location> chestLocations = Arrays.asList(
                location.clone().add(1.0, 0, 1.0),
                location.clone().add(-1.0, 0, 1.0),
                location.clone().add(-1.0, 0, -1.0),
                location.clone().add(1.0, 0, -1.0)
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
    }

    private void addItemsToChest(Inventory inventory) {
        Collections.shuffle(items);

        int singleCount = 0;
        int min = 2;
        int max = random.nextInt(2) + min;
        Iterator<ItemStack> iterator = items.iterator();
        while (iterator.hasNext()) {
            if (singleCount >= max) {
                break;
            }

            ItemStack item = iterator.next();
            addItemToChest(inventory, item);
            iterator.remove();
            singleCount++;
        }

    }

    private static Location getRandomSpawnLocation() {
        int x = boolX ? random.nextInt(175) + 175 : -(random.nextInt(175) + 175);
        int z = boolZ ? random.nextInt(175) + 175 : -(random.nextInt(175) + 175);
        World world = Bukkit.getWorld("world");
        int y = world.getHighestBlockYAt(x, z) + 5;
        return new Location(world, x, y, z);
    }

    public Location getLocation() {
        return location;
    }

    public void resetArrays() {

        items = new ArrayList<>(Arrays.asList(
                new ItemStack(Material.IRON_HELMET, 1),
                new ItemStack(Material.IRON_BOOTS, 1),
                new ItemStack(Material.IRON_SWORD, 1),
                new ItemStack(Material.IRON_AXE, 1),
                new ItemStack(Material.IRON_PICKAXE, 1),
                new ItemStack(Material.POTION, 1, (short) 8201),
                new ItemStack(Material.POTION, 1, (short) 16428),
                new ItemStack(Material.POTION, 1, (short) 16421),
                new ItemStack(Material.WEB, random.nextInt(4) + 1),
                new ItemStack(Material.FLINT_AND_STEEL, 1),
                new ItemStack(Material.TNT, random.nextInt(4) + 1),
                new ItemStack(Material.ENDER_PEARL, random.nextInt(2) + 1),
                new ItemStack(Material.LAVA_BUCKET, 1),
                new ItemStack(Material.WATER_BUCKET, 1),
                new ItemStack(Material.EXP_BOTTLE, random.nextInt(7) + 1),
                new ItemStack(Material.BREAD, random.nextInt(2) + 1),
                new ItemStack(Material.COOKED_CHICKEN, random.nextInt(2) + 1),
                new ItemStack(Material.WOOD, random.nextInt(15) + 1),
                new ItemStack(Material.DIAMOND, 1),
                new ItemStack(Material.IRON_INGOT, random.nextInt(4) + 1)
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
