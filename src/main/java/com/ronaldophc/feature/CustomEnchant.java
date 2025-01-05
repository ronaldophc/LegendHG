package com.ronaldophc.feature;

import net.minecraft.server.v1_8_R3.BlockPosition;
import net.minecraft.server.v1_8_R3.ContainerEnchantTable;
import net.minecraft.server.v1_8_R3.PlayerInventory;
import net.minecraft.server.v1_8_R3.World;

public final class CustomEnchant extends ContainerEnchantTable {

    public CustomEnchant(PlayerInventory playerinventory, World world, BlockPosition blockposition) {
        super(playerinventory, world, blockposition);
        this.checkReachable = false;
    }

}