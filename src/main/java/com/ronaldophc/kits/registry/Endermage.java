package com.ronaldophc.kits.registry;

import com.ronaldophc.LegendHG;
import com.ronaldophc.helper.ItemManager;
import com.ronaldophc.helper.Util;
import com.ronaldophc.kits.Kit;
import com.ronaldophc.kits.registry.gladiator.GladiatorController;
import com.ronaldophc.player.account.Account;
import com.ronaldophc.player.account.AccountManager;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class Endermage extends Kit {

    private static final String ENDERMAGE_ACTIVE_META = "endermage_active";
    private static final FixedMetadataValue ENDERMAGE_ACTIVE_META_VALUE = new FixedMetadataValue(LegendHG.getInstance(), true);

    public Endermage() {
        super("Endermage",
                "legendhg.kits.endermage",
                new ItemManager(Material.ENDER_PORTAL_FRAME, Util.color3 + "Endermage")
                        .setLore(Arrays.asList(Util.success + "Ao colocar o portal no chão", Util.success + "teleportará jogadores", Util.success + "próximos."))
                        .build(),
                new ItemManager(Material.ENDER_PORTAL_FRAME, Util.color3 + "Endermage")
                        .setUnbreakable()
                        .build(),
                false);
    }

    @EventHandler
    public void onEndermageInteract(PlayerInteractEvent event) {

        if (!LegendHG.getGameStateManager().getGameState().canUseKit()) return;
        Player mage = event.getPlayer();

        Account account = LegendHG.getAccountManager().getOrCreateAccount(mage);
        if (!account.getKits().contains(this)) return;
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;
        if (event.getItem() == null) return;
        if (!isItemKit(event.getItem())) return;

        GladiatorController gladiatorController = LegendHG.getGladiatorController();
        if (gladiatorController.isPlayerInFight(mage)) return;

        event.setCancelled(true);
        mage.updateInventory();

        if (isEndermageActive(mage)) return;

        setEndermageActive(mage, true);

        final Block block = event.getClickedBlock();
        final Location loc = block.getLocation();
        final Material material = block.getType();
        final BlockState blockState = block.getState();
        block.setType(Material.ENDER_PORTAL_FRAME);

        (new BukkitRunnable() {
            int time = 5;

            public void run() {
                this.time--;

                List<Entity> nearbyEntities = block.getWorld().getNearbyEntities(loc, 3, 256, 3).stream()
                        .filter(entity -> entity instanceof Player && entity != mage)
                        .collect(Collectors.toList());

                for (int i = 0; i < nearbyEntities.size(); i++) {
                    Entity entity = nearbyEntities.get(i);
                    Player target = (Player) entity;

                    onKitEndermage(loc, mage, target);
                    resetBlock(block, material, blockState);

                    if (i == nearbyEntities.size() - 1) {
                        setEndermageActive(mage, false);
                        cancel();
                    }
                }

                if (this.time <= 0) {
                    resetBlock(block, material, blockState);
                    setEndermageActive(mage, false);
                    cancel();
                }
            }
        }).runTaskTimer(LegendHG.getInstance(), 0L, 20L);
    }

    public void onKitEndermage(Location portal, Player mage, Player player) {
        if (!LegendHG.getAccountManager().getOrCreateAccount(player).isAlive()) return;
        portal = portal.add(0.0D, 1.0D, 0.0D);
        mage.teleport(portal);
        player.teleport(portal);
        mage.setNoDamageTicks(60);
        player.setNoDamageTicks(60);
        player.getWorld().playEffect(player.getLocation(), Effect.ENDER_SIGNAL, 9);
        mage.getWorld().playEffect(portal, Effect.ENDER_SIGNAL, 9);
        player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0F, 1.2F);
        mage.playSound(portal, Sound.ENDERMAN_TELEPORT, 1.0F, 1.2F);
        mage.updateInventory();
        player.updateInventory();
    }

    private void resetBlock(Block block, Material material, BlockState blockState) {
        block.setType(material);
        blockState.update();
    }


    private void setEndermageActive(Player player, boolean active) {
        if (active) {
            player.setMetadata(ENDERMAGE_ACTIVE_META, ENDERMAGE_ACTIVE_META_VALUE);
            return;
        }
        player.removeMetadata(ENDERMAGE_ACTIVE_META, LegendHG.getInstance());

    }

    private boolean isEndermageActive(Player player) {
        return player.hasMetadata(ENDERMAGE_ACTIVE_META);
    }
}
