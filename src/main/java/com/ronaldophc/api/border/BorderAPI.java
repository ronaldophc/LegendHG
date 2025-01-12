package com.ronaldophc.api.border;

import org.bukkit.*;

public class BorderAPI {

    public static void generateConstruction(World world, Location startLocation, boolean rotate) {
        // Blocos de Stone Bricks
        Material[][] structure = {
                {Material.LOG, Material.SMOOTH_BRICK, Material.MOSSY_COBBLESTONE, Material.SMOOTH_BRICK, Material.LOG},
                {Material.LOG, Material.SMOOTH_BRICK, Material.SMOOTH_BRICK, Material.COBBLESTONE, Material.LOG},
                {Material.LOG, Material.OBSIDIAN, Material.LOG, Material.SMOOTH_BRICK, Material.LOG},
                {Material.LOG, Material.SMOOTH_BRICK, Material.LOG, Material.OBSIDIAN, Material.LOG},
                {Material.LOG, Material.COBBLESTONE, Material.SMOOTH_BRICK, Material.SMOOTH_BRICK, Material.LOG},
                {Material.LOG, Material.SMOOTH_BRICK, Material.SMOOTH_BRICK, Material.MOSSY_COBBLESTONE, Material.LOG},
        };

        // Loop para construir a parede
        for (int y = 0; y < structure.length; y++) {
            for (int x = 0; x < structure[y].length; x++) {
                Material material = structure[y][x];
                if (material != Material.AIR) {
                    Location block;
                    if (rotate) {
                        block = startLocation.clone().add(0, y, x);
                    } else {
                        block = startLocation.clone().add(x, y, 0);
                    }
                    world.getBlockAt(block).setType(material);
                }
            }
        }
    }

    public static void generateBorder(World world, int min, int max) {
        // Gerar estrutura ao longo do eixo X e Z de y 0 até 200
        for (int y = 0; y <= 200; y += 5) {
            for (int z = min; z <= max; z += 5) {
                generateConstruction(world, new Location(world, min, y, z), true);
                generateConstruction(world, new Location(world, max, y, z), true);
            }

            for (int x = min; x <= max; x += 5) {
                generateConstruction(world, new Location(world, x, y, min), false);
                generateConstruction(world, new Location(world, x, y, max), false);
            }
        }
    }

    public static void setWorldBorder() {
        World world = Bukkit.getWorld("world");
        if (world != null) {
            int y = world.getHighestBlockYAt(0, 0);
            world.setSpawnLocation(0, y, 0);

            WorldBorder border = world.getWorldBorder();
            border.setCenter(0, 0);
            border.setSize(700);
        }
    }
}