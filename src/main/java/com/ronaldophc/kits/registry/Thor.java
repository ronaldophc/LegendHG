package com.ronaldophc.kits.registry;

import com.ronaldophc.LegendHG;
import com.ronaldophc.helper.ItemManager;
import com.ronaldophc.helper.Util;
import com.ronaldophc.kits.Kit;
import com.ronaldophc.player.account.Account;
import com.ronaldophc.player.account.AccountManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Thor extends Kit {

    private final Set<Material> validBaseMaterials = new HashSet<>(Arrays.asList(Material.GRASS, Material.SAND, Material.STONE, Material.COBBLESTONE, Material.GRAVEL, Material.NETHERRACK));

    public Thor() {
        super("Thor",
                "legendhg.kits.thor",
                new ItemManager(Material.WOOD_AXE, Util.color3 + "Thor")
                        .setLore(Arrays.asList(Util.success + "Ao acertar um jogador", Util.success + "ele será atingido por um", Util.success + "raio."))
                        .build(),
                new ItemManager(Material.WOOD_AXE, Util.color3 + "Thor")
                        .setUnbreakable()
                        .build(),
                false);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (!LegendHG.getGameStateManager().getGameState().canTakeDamage()) return;

        Player thor = event.getPlayer();

        Account account = AccountManager.getInstance().getOrCreateAccount(thor);
        if (!account.getKits().contains(this)) return;

        if (event.getItem() == null) return;
        if (!isItemKit(event.getItem())) return;
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.RIGHT_CLICK_AIR) return;
        if (kitManager.isOnCooldown(thor, this)) return;

        Block block = thor.getTargetBlock((Set<Material>) null, 13);

        if (block == null || block.getType() == Material.AIR) return;

        Location targetLocation = block.getLocation();
        targetLocation.getWorld().strikeLightningEffect(targetLocation);

        for (Player player : AccountManager.getInstance().getPlayersAlive()) {
            if (player == thor) continue;
            if (player.getLocation().distance(targetLocation) > 3) continue;
            player.damage(5.0D, event.getPlayer());
        }

        if (validBaseMaterials.contains(block.getType())) {
            block.getLocation().add(0, 1, 0).getBlock().setType(Material.FIRE);
        }

        kitManager.setCooldown(thor, 6, this);
    }

}
