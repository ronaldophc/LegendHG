package com.ronaldophc.kits.registry.blink;

import com.ronaldophc.LegendHG;
import com.ronaldophc.helper.ItemManager;
import com.ronaldophc.helper.Util;
import com.ronaldophc.kits.Kit;
import com.ronaldophc.player.account.Account;
import com.ronaldophc.player.account.AccountManager;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.Arrays;

public class Blink extends Kit {

    public Blink() {
        super("Blink",
                "legendhg.kits.blink",
                new ItemManager(Material.NETHER_STAR, Util.color3 + "Blink")
                        .setLore(Arrays.asList(Util.success + "Se teleporte", Util.success + "instantaneamente gerando folhas."))
                        .build(),
                new ItemManager(Material.NETHER_STAR, Util.color3 + "Blink").setUnbreakable().build(),
                true
        );
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        if (!LegendHG.getGameStateManager().getGameState().canUseKit()) return;
        Player blinker = event.getPlayer();

        Account account = AccountManager.getInstance().getOrCreateAccount(blinker);
        if (!account.getKits().contains(this)) return;

        if (!(isItemKit(event.getItem()))) return;

        if (kitManager.isOnCooldown(blinker, this)) return;

        BlinkManager blinkManager = BlinkManager.getInstance();

        Block block = blinker.getEyeLocation().add(blinker.getEyeLocation().getDirection().multiply(5.0D)).getBlock();
        if (block.getY() > 130) {
            blinker.sendMessage(Util.errorServer + "Você não pode usar o blink acima de 130 blocos.");
            return;
        }

        int times = 0;

        if (blinkManager.isSetBlinkPlayer(blinker)) {
            times = blinkManager.getTimes(blinker);
        }

        if (block.getRelative(BlockFace.DOWN).getType() == Material.AIR) {
            block.getRelative(BlockFace.DOWN).setType(Material.LEAVES);
        }

        blinker.teleport(new Location(blinker.getWorld(), block.getX(), block.getY(), block.getZ(), blinker.getLocation().getYaw(), blinker.getLocation().getPitch()));
        blinker.setFallDistance(0.0F);
        blinker.playSound(blinker.getLocation(), Sound.FIREWORK_LAUNCH, 1.0F, 50.0F);

        times++;
        if (times >= 3) {
            kitManager.setCooldown(blinker, 25, this);
            blinkManager.setBlinkPlayer(blinker, 0);
            return;
        }

        blinkManager.setBlinkPlayer(blinker, times);
    }
}
