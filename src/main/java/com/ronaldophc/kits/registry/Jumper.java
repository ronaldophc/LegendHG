package com.ronaldophc.kits.registry;

import com.ronaldophc.LegendHG;
import com.ronaldophc.kits.Kit;
import com.ronaldophc.kits.registry.gladiator.GladiatorController;
import com.ronaldophc.player.account.Account;
import com.ronaldophc.player.account.AccountManager;
import com.ronaldophc.util.ItemManager;
import com.ronaldophc.util.Util;
import net.minecraft.server.v1_8_R3.EntityEnderPearl;
import net.minecraft.server.v1_8_R3.EntityPlayer;
import net.minecraft.server.v1_8_R3.World;
import org.bukkit.Bukkit;
import org.bukkit.Effect;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.EnderPearl;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.spigotmc.event.entity.EntityDismountEvent;

import java.util.*;

public class Jumper extends Kit {

    private final List<UUID> track = new ArrayList<>();
    private final List<UUID> moving = new ArrayList<>();

    public Jumper() {
        super("Jumper",
                "legendhg.kits.jumper",
                new ItemManager(Material.EYE_OF_ENDER, Util.color3 + "Jumper")
                        .setLore(Arrays.asList(Util.success + "Voe com sua enderpearl", Util.success + "ao arremeçá-la."))
                        .build(),
                new ItemManager(Material.EYE_OF_ENDER, Util.color3 + "Jumper")
                        .setUnbreakable()
                        .build(),
                true);
        onTimer();
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onIteract(PlayerInteractEvent event) {
        Player player = event.getPlayer();
        Account account = AccountManager.getInstance().getOrCreateAccount(player);
        if (!account.getKits().contains(this))
            return;

        if (!isItemKit(player.getItemInHand()))
            return;

        GladiatorController gladiatorController = LegendHG.getGladiatorController();
        if (gladiatorController.isPlayerInFight(player))
            return;

        if (!event.getAction().toString().contains("RIGHT"))
            return;
        event.setCancelled(true);

        if (kitManager.isOnCooldown(player, this)) return;

        kitManager.setCooldown(player, 10, this);
        launchEnderPearl(player);

    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onDamage(EntityDamageEvent event) {
        if (event.isCancelled())
            return;
        if (event.getEntity() instanceof Player) {
            EntityDamageEvent.DamageCause cause = event.getCause();
            if (cause == EntityDamageEvent.DamageCause.FALL || cause == EntityDamageEvent.DamageCause.SUFFOCATION) {
                if (moving.contains((event.getEntity()).getUniqueId())) {
                    event.setCancelled(true);
                }
            }
        }
    }

    public void onTimer() {
        Iterator<UUID> iterator = track.iterator();
        while (iterator.hasNext()) {
            Player next = Bukkit.getPlayer(iterator.next());
            if (next == null) {
                iterator.remove();
                continue;
            }
            next.getWorld().playEffect(next.getLocation(), Effect.INSTANT_SPELL, 10);
        }
    }

    @EventHandler
    public void onDismount(EntityDismountEvent event) {
        if (event.getEntity() instanceof Player) {
            Player player = (Player) event.getEntity();
            track.remove(player.getUniqueId());
            cancelMoving(player);
        }
    }

    public void cancelMoving(Player player) {
        new BukkitRunnable() {

            @Override
            public void run() {
                moving.remove(player.getUniqueId());
            }
        }.runTaskLater(LegendHG.getInstance(), 10);
    }

    private void launchEnderPearl(Player bukkitPlayer) {
        bukkitPlayer.setFallDistance(0);
        EntityPlayer nmsPlayer = ((CraftPlayer) bukkitPlayer).getHandle();
        JumperEnderPearl customEnder = new JumperEnderPearl(nmsPlayer.getWorld(), nmsPlayer);
        nmsPlayer.getWorld().addEntity(customEnder);
        EnderPearl bukkitEnder = (EnderPearl) customEnder.getBukkitEntity();
        bukkitEnder.setPassenger(bukkitPlayer);
        track.add(bukkitPlayer.getUniqueId());
        moving.add(bukkitPlayer.getUniqueId());
    }

    static class JumperEnderPearl extends EntityEnderPearl {

        private final EntityPlayer jumper;

        public JumperEnderPearl(World world, EntityPlayer entityliving) {
            super(world, entityliving);
            this.jumper = entityliving;
            shooter = null;
            shooterName = null;
        }

        @Override
        public void t_() {
            super.t_();
            if (dead) {
                getBukkitEntity().eject();
                new BukkitRunnable() {

                    @Override
                    public void run() {
                        while (!getWorld().getCubes(jumper, jumper.getBoundingBox()).isEmpty() && jumper.locY < 256.0D) {
                            jumper.setPosition(jumper.locX, jumper.locY + 1.0D, jumper.locZ);
                        }
                    }
                }.runTaskLater(LegendHG.getInstance(), 5);
            }
        }
    }

}
