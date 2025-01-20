package com.ronaldophc.kits.registry.gladiator;

import com.ronaldophc.LegendHG;
import com.ronaldophc.player.account.Account;
import com.ronaldophc.player.account.AccountManager;
import com.ronaldophc.util.Util;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

public class GladiatorFight extends GladiatorController implements Listener {

    private final HashMap<UUID, UUID> gladiatorBattles = new HashMap<>();
    @Getter
    public final Set<Block> arenaBlocks = new HashSet<>();
    public final Set<Block> placedBlocks = new HashSet<>();
    public final Player gladiator;
    public final Player target;
    public Location arenaCenter;
    public Location originalLocation;
    private int timeRemaining;
    private final Account targetAccount;
    private final Account gladiatorAccount;

    public GladiatorFight(Player gladiator, Player target, Location arenaCenter, Location originalLocation) {
        super();
        this.gladiator = gladiator;
        this.target = target;
        this.arenaCenter = arenaCenter;
        this.timeRemaining = 60 * 3;
        this.originalLocation = originalLocation;
        targetAccount = AccountManager.getInstance().getOrCreateAccount(target);
        gladiatorAccount = AccountManager.getInstance().getOrCreateAccount(gladiator);
        registerListener();
        initializeBattle();
    }

    private void initializeBattle() {
        startBattle(gladiator, target);
    }

    private void registerListener() {
        PluginManager pm = Bukkit.getPluginManager();
        pm.registerEvents(this, LegendHG.getInstance());
    }

    public void addPlacedBlock(Block block) {
        placedBlocks.add(block);
    }

    public boolean isInFight(Player player) {
        return gladiatorBattles.containsKey(player.getUniqueId()) || gladiatorBattles.containsValue(player.getUniqueId());
    }

    public void startBattle(Player gladiator, Player target) {
        // Create arena
        createArena(arenaCenter);

        // Teleport players
        gladiator.teleport(arenaCenter.clone().add(-6.5, 1, -6.5));
        target.teleport(arenaCenter.clone().add(6.5, 1, 6.5));

        gladiator.sendMessage(Util.color3 + "Você desafiou " + Util.color1 + target.getName() + Util.color3 + " para um duelo no Gladiator!");
        target.sendMessage(Util.color1 + gladiator.getName() + Util.color3 + " desafiou você para um duelo no Gladiator!");
        gladiator.playSound(gladiator.getLocation(), org.bukkit.Sound.NOTE_PLING, 1, 1);
        target.playSound(target.getLocation(), Sound.NOTE_PLING, 1, 1);

        // Add to battles
        gladiatorBattles.put(gladiator.getUniqueId(), target.getUniqueId());

        // Monitor battle end conditions
        new BukkitRunnable() {
            @Override
            public void run() {
                timeRemaining--;

                if (timeRemaining <= 0) {
                    cancel();
                    endBattle(gladiator, target);
                    endGladiatorFight(GladiatorFight.this);
                }
                if (timeRemaining == 30) {
                    gladiator.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 30 * 20, 1));
                    target.addPotionEffect(new PotionEffect(PotionEffectType.WITHER, 30 * 20, 1));
                }
                if (!targetAccount.isAlive() || !gladiatorAccount.isAlive() || !gladiator.isOnline() || !target.isOnline() || gladiator.isDead() || target.isDead() || !isInArena(gladiator, arenaCenter) || !isInArena(target, arenaCenter)) {
                    endBattle(gladiator, target);
                    endGladiatorFight(GladiatorFight.this);
                    cancel();
                }
            }
        }.runTaskTimer(LegendHG.getInstance(), 0, 20);
    }

    private void createArena(Location center) {
        for (int x = -8; x <= 8; x++) {
            for (int y = 0; y <= 8; y++) {
                for (int z = -8; z <= 8; z++) {
                    if (x == -8 || x == 8 || y == 0 || z == -8 || z == 8) {
                        Location blockLocation = center.clone().add(x, y, z);
                        Block block = blockLocation.getBlock();
                        block.setType(Material.GLASS);
                        arenaBlocks.add(block);
                    }
                }
            }
        }
    }

    public boolean isInArena(Player player, Location center) {
        Location loc = player.getLocation();
        return loc.getX() >= center.getX() - 8 && loc.getX() <= center.getX() + 8 &&
                loc.getY() >= center.getY() && loc.getY() <= center.getY() + 8 &&
                loc.getZ() >= center.getZ() - 8 && loc.getZ() <= center.getZ() + 8;
    }

    public boolean hasArenaAbove(Location location) {
        return (location.distance(arenaCenter) <= 16);
    }

    public void endBattle(Player gladiator, Player target) {
        gladiatorBattles.remove(gladiator.getUniqueId());
        gladiatorBattles.remove(target.getUniqueId());

        // Remove arena
        for (Block block : arenaBlocks) {
            block.setType(Material.AIR);
        }
        arenaBlocks.clear();

        // Remove placed blocks
        for (Block block : placedBlocks) {
            block.setType(Material.AIR);
        }
        placedBlocks.clear();

        Location loc;

        if (!target.isOnline()) {
            targetAccount.setAlive(false);
        }
        if (!gladiator.isOnline()) {
            gladiatorAccount.setAlive(false);
        }

        if (gladiatorAccount.isAlive()) {
            gladiator.removePotionEffect(PotionEffectType.WITHER);
            int y = Bukkit.getWorld("world").getHighestBlockYAt(gladiator.getLocation());
            loc = gladiator.getLocation();
            loc.setY(y);
            gladiator.teleport(loc);
            new GladiatorEndsEvent(gladiator).callEvent();
        }

        if (targetAccount.isAlive()) {
            target.removePotionEffect(PotionEffectType.WITHER);
            int y = Bukkit.getWorld("world").getHighestBlockYAt(target.getLocation());
            loc = target.getLocation();
            loc.setY(y);
            target.teleport(loc);
        }

    }


    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        if (isInFight(gladiator)) {
            addPlacedBlock(event.getBlock());
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        if (getArenaBlocks().contains(event.getBlock())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        Account account = AccountManager.getInstance().getOrCreateAccount(player);
        if (!account.isAlive()) {
            return;
        }
        if (isInFight(player)) {
            event.setQuitMessage(Util.color3 + "O jogador " + Util.color1 + player.getName() + Util.color3 + " saiu da partida durante um duelo no Gladiator!");
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        if (!isInFight(player)) {
            return;
        }
        for (ItemStack item : event.getDrops()) {
            if (item != null && item.getType() != Material.AIR) {
                player.getWorld().dropItemNaturally(originalLocation, item);
            }
        }
        event.getDrops().clear();
    }
}