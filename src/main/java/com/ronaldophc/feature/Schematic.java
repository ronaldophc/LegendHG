package com.ronaldophc.feature;

import com.ronaldophc.LegendHG;
import com.ronaldophc.api.jnbt.*;
import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.Block;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

public class Schematic {
    private short[] blocks;
    private byte[] data;
    private short width, length, height;

    @Getter
    private static final Schematic instance = new Schematic();

    private Schematic() {}

    private Schematic(short[] blocks, byte[] data, short width, short length, short height) {
        this.blocks = blocks;
        this.data = data;
        this.width = width;
        this.length = length;
        this.height = height;
    }

    public void createSchematic(World world, Location loc, String schematicName) {
        try {
            Schematic schematic = loadSchematics(new File(LegendHG.getInstance().getDataFolder(), schematicName + ".schematic"));
            generateSchematic(world, loc, schematic);
        } catch (IOException | DataException e) {
            throw new RuntimeException(e);
        }
    }

    private void generateSchematic(World world, Location loc, Schematic schematic) {
        short[] blocks = schematic.blocks;
        byte[] blockData = schematic.data;
        short length = schematic.length;
        short width = schematic.width;
        short height = schematic.height;

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                for (int z = 0; z < length; z++) {
                    int index = y * width * length + z * width + x;
                    Block block = new Location(world, x + loc.getX(), y + loc.getY(), z + loc.getZ()).getBlock();
                    block.setTypeIdAndData(blocks[index], blockData[index], true);
                }
            }
        }
    }

    private Schematic loadSchematics(File file) throws IOException, DataException {
        Map<String, Tag> schematic = getMap(file);
        short width = (getChildTag(schematic, "Width", ShortTag.class)).getValue();
        short length = (getChildTag(schematic, "Length", ShortTag.class)).getValue();
        short height = (getChildTag(schematic, "Height", ShortTag.class)).getValue();
        byte[] blockId = (getChildTag(schematic, "Blocks", ByteArrayTag.class)).getValue();
        byte[] blockData = (getChildTag(schematic, "Data", ByteArrayTag.class)).getValue();
        byte[] addId = schematic.containsKey("AddBlocks") ? (getChildTag(schematic, "AddBlocks", ByteArrayTag.class)).getValue() : new byte[0];
        short[] blocks = new short[blockId.length];

        for (int index = 0; index < blockId.length; index++) {
            if (index >> 1 >= addId.length) {
                blocks[index] = (short) (blockId[index] & 0xFF);
            } else if ((index & 0x1) == 0) {
                blocks[index] = (short) (((addId[index >> 1] & 0xF) << 8) + (blockId[index] & 0xFF));
            } else {
                blocks[index] = (short) (((addId[index >> 1] & 0xF0) << 4) + (blockId[index] & 0xFF));
            }
        }
        return new Schematic(blocks, blockData, width, length, height);
    }

    private static Map<String, Tag> getMap(File file) throws IOException {
        try (FileInputStream stream = new FileInputStream(file); NBTInputStream nbtStream = new NBTInputStream(stream)) {
            CompoundTag schematicTag = (CompoundTag) nbtStream.readTag();
            if (!schematicTag.getName().equalsIgnoreCase("Schematic")) {
                throw new IllegalArgumentException("Tag \"Schematic\" does not exist or is not first");
            }
            Map<String, Tag> schematic = schematicTag.getValue();
            if (!schematic.containsKey("Blocks")) {
                throw new IllegalArgumentException("Schematic file is missing a \"Blocks\" tag");
            }
            return schematic;
        }
    }

    private <T extends Tag> T getChildTag(Map<String, Tag> items, String key, Class<T> expected) throws DataException {
        Tag tag = items.get(key);
        if (tag == null) {
            throw new DataException("Schematic file is missing a \"" + key + "\" tag");
        }
        if (!expected.isInstance(tag)) {
            throw new DataException(key + " tag is not of tag type " + expected.getName());
        }
        return expected.cast(tag);
    }
}