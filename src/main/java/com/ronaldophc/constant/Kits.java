package com.ronaldophc.constant;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;

import com.ronaldophc.helper.ItemManager;
import com.ronaldophc.helper.Util;
import org.bukkit.potion.PotionEffectType;

public enum Kits {

    NENHUM("Nenhum", "", new ItemManager(Material.DEAD_BUSH, Util.color3 + "Nenhum").setLore(Arrays.asList(Util.success + "Não faz absolutamente", Util.color3 + Util.bold + "NADA!")).build(), Collections.emptyList(), false),

    VIPER("Viper", "legendhg.kits.viper", new ItemManager(Material.SPIDER_EYE, Util.color3 + "Viper").setLore(Arrays.asList(Util.success + "Crie uma area envenenada", Util.success + "em sua volta.")).build(), Arrays.asList(new ItemStack[] {new ItemManager(Material.SPIDER_EYE, Util.color3 + "Viper").build()}), false),

    STOMPER("Stomper", "legendhg.kits.stomper", new ItemManager(Material.IRON_BOOTS, Util.color3 + "Stomper").setLore(Arrays.asList(Util.success + "Ao cair de uma altura", Util.success + "causará dano em jogadores", Util.success + "próximos.")).build(), Collections.emptyList(), false),

    ENDERMAGE("Endermage", "legendhg.kits.endermage", new ItemManager(Material.ENDER_PORTAL_FRAME, Util.color3 + "Endermage").setLore(Arrays.asList(Util.success + "Ao colocar o portal no chão", Util.success + "teleportará jogadores", Util.success + "próximos.")).build(), Arrays.asList(new ItemStack[]{new ItemManager(Material.ENDER_PORTAL_FRAME, Util.color3 + "Endermage").setUnbreakable().build()}), false),

    FISHERMAN("Fisherman", "legendhg.kits.fisherman", new ItemManager(Material.FISHING_ROD, Util.color3 + "Fisherman").setLore(Arrays.asList(Util.success + "Ao acertar um jogador", Util.success + "puxará ele até você.")).build(), Arrays.asList(new ItemStack[]{new ItemManager(Material.FISHING_ROD, Util.color3 + "Fisherman").setUnbreakable().build()}), false),

    MINER("Miner", "legendhg.kits.miner", new ItemManager(Material.STONE_PICKAXE, Util.color3 + "Miner").setLore(Arrays.asList(Util.success + "Ao acertar um bloco", Util.success + "ele será instantaneamente", Util.success + "quebrado.")).build(), Arrays.asList(new ItemStack[]{new ItemManager(Material.STONE_PICKAXE, Util.color3 + "Miner").setUnbreakable().addEnchantment(Enchantment.DIG_SPEED, 2).build()}), false),

    NINJA("Ninja", "legendhg.kits.ninja", new ItemManager(Material.NETHER_STAR, Util.color3 + "Ninja").setLore(Arrays.asList(Util.success + "Ao apertar shift", Util.success + "você será teleportado", Util.success + "para o ultimo jogador que bateu.")).build(), Collections.emptyList(), false),

    GLADIATOR("Gladiator", "legendhg.kits.gladiator", new ItemManager(Material.IRON_FENCE, Util.color3 + "Gladiator").setLore(Arrays.asList(Util.success + "Desafie jogadores para 1x1", Util.success + "em uma arena no céu.")).build(), Arrays.asList(new ItemStack[]{new ItemManager(Material.IRON_FENCE, Util.color3 + "Gladiator").setUnbreakable().build()}), false),

    THOR("Thor", "legendhg.kits.thor", new ItemManager(Material.WOOD_AXE, Util.color3 + "Thor").setLore(Arrays.asList(Util.success + "Ao acertar um jogador", Util.success + "ele será atingido por um", Util.success + "raio.")).build(), Arrays.asList(new ItemStack[]{new ItemManager(Material.WOOD_AXE, Util.color3 + "Thor").setUnbreakable().build()}), false),

    GRAVITY("Gravity", "legendhg.kits.gravity", new ItemManager(Material.SEA_LANTERN, Util.color3 + "Gravity").setLore(Arrays.asList(Util.success + "Ao acertar um jogador", Util.success + "ele será jogado para cima.")).build(), Arrays.asList(new ItemStack[]{new ItemManager(Material.SEA_LANTERN, Util.color3 + "Gravity").setUnbreakable().build()}), false),

    ACHILLES("Achilles", "legendhg.kits.achilles", new ItemManager(Material.WOOD_SWORD, Util.color3 + "Achilles").setLore(Arrays.asList(Util.success + "Tome mais dano para", Util.success + "espada de madeira", Util.success + "e menos das outras espadas.")).build(), Collections.emptyList(), false),

    ANCHOR("Anchor", "legendhg.kits.anchor", new ItemManager(Material.ANVIL, Util.color3 + "Anchor").setLore(Arrays.asList(Util.success + "Não tome knockback", Util.success + "ao tomar um hit.")).build(), Collections.emptyList(), false),

    BERSERKER("Berserker", "legendhg.kits.berserker", new ItemManager(Material.REDSTONE, Util.color3 + "Berserker").setLore(Arrays.asList(Util.success + "Ao matar um jogador", Util.success + "você ganhará força.")).build(), Collections.emptyList(), false),

    BARBARIAN("Barbarian", "legendhg.kits.barbarian", new ItemManager(Material.DIAMOND_SWORD, Util.color3 + "Barbarian").setLore(Arrays.asList(Util.success + "Ao matar um jogador", Util.success + "evolua sua espada.")).addEnchantment(Enchantment.DAMAGE_ALL, 2).build(), Arrays.asList(new ItemStack[] {new ItemManager(Material.WOOD_SWORD, Util.color3 + "Berserker").setUnbreakable().build(), new ItemManager(Material.STONE_SWORD, Util.color3 + "Berserker").setUnbreakable().build(), new ItemManager(Material.IRON_SWORD, Util.color3 + "Berserker").setUnbreakable().build(), new ItemManager(Material.DIAMOND_SWORD, Util.color3 + "Berserker").setUnbreakable().build(), new ItemManager(Material.DIAMOND_SWORD, Util.color3 + "Berserker").setUnbreakable().addEnchantment(Enchantment.DAMAGE_ALL, 1) .build(), new ItemManager(Material.DIAMOND_SWORD, Util.color3 + "Berserker").setUnbreakable().addEnchantment(Enchantment.DAMAGE_ALL, 2) .build()}), false),

    BLINK("Blink", "legendhg.kits.blink", new ItemManager(Material.NETHER_STAR, Util.color3 + "Blink").setLore(Arrays.asList(Util.success + "Se teleporte", Util.success + "instantaneamente gerando folhas.")).build(), Arrays.asList(new ItemStack[]{new ItemManager(Material.NETHER_STAR, Util.color3 + "Blink").setUnbreakable().build()}), true),

    FIREMAN("Fireman", "legendhg.kits.fireman", new ItemManager(Material.LAVA_BUCKET, Util.color3 + "Fireman").setLore(Arrays.asList(Util.success + "Não tome dano", Util.success + "de lava e fogo.", Util.success + "E ganhe um balde de agua.")).build(), Arrays.asList(new ItemStack[]{new ItemStack(Material.WATER_BUCKET)}), false),

    FLASH("Flash", "legendhg.kits.flash", new ItemManager(Material.REDSTONE_TORCH_ON, Util.color3 + "Flash").setLore(Collections.singletonList(Util.success + "Corra na velocidade da luz")).build(), Arrays.asList(new ItemStack[]{new ItemManager(Material.REDSTONE_TORCH_ON, Util.color3 + "Flash").setUnbreakable().build()}), true),

    FORGER("Forger", "legendhg.kits.forger", new ItemManager(Material.COAL, Util.color3 + "Forger").setLore(Arrays.asList(Util.success + "Queime ferro", Util.success + "sem fornalha.")).build(), Collections.emptyList(), false),

    KANGAROO("Kangaroo", "legendhg.kits.kangaroo", new ItemManager(Material.FIREWORK, Util.color3 + "Kangaroo").setLore(Collections.singletonList(Util.success + "Pule como um canguru")).build(), Arrays.asList(new ItemStack[]{new ItemManager(Material.FIREWORK, Util.color3 + "Kangaroo").setUnbreakable().build()}), true),

    SPECIALIST("Specialist", "legendhg.kits.specialist", new ItemManager(Material.BOOK, Util.color3 + "Specialist").setLore(Arrays.asList(Util.success + "Crie itens especiais", Util.success + "com um livro.")).build(), Arrays.asList(new ItemStack[]{new ItemManager(Material.BOOK, Util.color3 + "Specialist").build()}), false),

    BOXER("Boxer", "legendhg.kits.boxer", new ItemManager(Material.GOLDEN_APPLE, Util.color3 + "Boxer").setLore(Arrays.asList(Util.success + "Leve menos dano", Util.success + "de outros jogadores")).build(), Collections.emptyList(), false),

    SWITCHER("Switcher", "legendhg.kits.switcher", new ItemManager(Material.SNOW_BALL, Util.color3 + "Switcher").setLore(Arrays.asList(Util.success + "Troque de lugar com", Util.success + "o jogador que acertar.")).build(), Arrays.asList(new ItemStack[]{new ItemManager(Material.SNOW_BALL, Util.color3 + "Switcher").build()}), false),

    MONK("Monk", "legendhg.kits.monk", new ItemManager(Material.BLAZE_ROD, Util.color3 + "Monk").setLore(Arrays.asList(Util.success + "Use seu monk para", Util.success + "embaralhar o inventario", Util.success + "do seu inimigo!")).build(), Arrays.asList(new ItemStack[]{new ItemManager(Material.BLAZE_ROD, Util.color3 + "Monk").setUnbreakable().build()}), false),

    PHANTOM("Phantom", "legendhg.kits.phantom", new ItemManager(Material.FEATHER, Util.color3 + "Phantom").setLore(Arrays.asList(Util.success + "Use seu phantom", Util.success + "para poder voar", Util.success + "por 5 segundos.")).build(), Arrays.asList(new ItemStack[]{new ItemManager(Material.FEATHER, Util.color3 + "Phantom").setUnbreakable().build()}), true),

    DIGGER("Digger", "legendhg.kits.digger", new ItemManager(Material.DRAGON_EGG, Util.color3 + "Digger").setLore(Arrays.asList(Util.success + "Escave uma area", Util.success + "para baixo.")).build(), Arrays.asList(new ItemStack[]{new ItemManager(Material.DRAGON_EGG, Util.color3 + "Digger").setUnbreakable().build()}), false),

    LAUNCHER("Launcher", "legendhg.kits.launcher", new ItemManager(Material.SPONGE, Util.color3 + "Launcher").setLore(Arrays.asList(Util.success + "Ganhe impulsao", Util.success + "ao subir na esponja.")).build(), Arrays.asList(new ItemStack[]{new ItemManager(Material.SPONGE, Util.color3 + "Launcher").setAmount(20).build()}), false),

    VIKING("Viking", "legendhg.kits.viking", new ItemManager(Material.STONE_AXE, Util.color3 + "Viking").setLore(Arrays.asList(Util.success + "Batalhe como um", Util.success + "verdadeiro viking.")).build(), Arrays.asList(new ItemStack[]{new ItemManager(Material.STONE_AXE, Util.color3 + "Viking").setUnbreakable().build()}), false),

    CANNIBAL("Cannibal", "legendhg.kits.cannibal", new ItemManager(Material.ROTTEN_FLESH, Util.color3 + "Cannibal").setLore(Arrays.asList(Util.success + "Ao matar um jogador", Util.success + "você ganhará vida e regeneração.")).build(), Collections.emptyList(), false),

    CAMEL("Camel", "legendhg.kits.camel", new ItemManager(Material.SAND, Util.color3 + "Camel").setLore(Arrays.asList(Util.success + "Ao andar por uma areia", Util.success + "você ganhará velocidade.")).build(), Collections.emptyList(), false),

    JACKHAMMER("Jackhammer", "legendhg.kits.jackhammer", new ItemManager(Material.STONE_AXE, Util.color3 + "Jackhammer").setLore(Arrays.asList(Util.success + "Ao quebrar um bloco com o machado", Util.success + "quebre tudo até a bedrock")).setUnbreakable().build(), Arrays.asList(new ItemStack[]{new ItemManager(Material.STONE_AXE, Util.color3 + "Jackhammer").setUnbreakable().build()}), false),

    THERMO("Thermo", "legendhg.kits.thermo", new ItemManager(Material.DAYLIGHT_DETECTOR, Util.color3 + "Thermo").setLore(Collections.singletonList(Util.success + "Inverta Agua e Lava")).build(), Arrays.asList(new ItemStack[]{new ItemManager(Material.DAYLIGHT_DETECTOR, Util.color3 + "Thermo").setUnbreakable().build()}), false),

    URGAL("Urgal", "legendhg.kits.urgal", new ItemManager(Material.POTION, Util.color3 + "Urgal").setLore(Arrays.asList(Util.success + "Inicie a partida com", Util.success + "3 poções de força!")).setDurability(8201).addPotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 10, 1).build(), Arrays.asList(new ItemStack[]{new ItemManager(Material.POTION, Util.color3 + "Urgal").setDurability(8201).addPotionEffect(PotionEffectType.INCREASE_DAMAGE, 20 * 10, 1).setAmount(3).build()}), false),

    CULTIVATOR("Cultivator", "legendhg.kits.cultivator", new ItemManager(Material.SAPLING, Util.color3 + "Cultivator").setLore(Arrays.asList(Util.success + "Ao plantar uma semente", Util.success + "nascerá instantaneamente")).build(), Collections.emptyList(), false),

    TANK("Tank", "legendhg.kits.tank", new ItemManager(Material.TNT, Util.color3 + "Tank").setLore(Arrays.asList(Util.success + "Ganhe resistência a explosão.", Util.success + "Crie uma explosão ao matar alguém.")).build(), Collections.emptyList(), false),

    PYRO("Pyro", "legendhg.kits.pyro", new ItemManager(Material.FIREBALL, Util.color3 + "Pyro").setLore(Arrays.asList(Util.success + "Lance uma bola de fogo", Util.success + "na direção que estiver olhando.")).build(), Arrays.asList(new ItemStack[] {new ItemManager(Material.FIREBALL, Util.color3 + "Pyro").build()}), false),

    GRANDPA("Grandpa", "legendhg.kits.grandpa", new ItemManager(Material.STICK, Util.color3 + "Grandpa").addEnchantment(Enchantment.KNOCKBACK, 2).setLore(Arrays.asList(Util.success + "Ganhe um graveto", Util.success + "com knockback.")).build(), Arrays.asList(new ItemStack[] {new ItemManager(Material.STICK, Util.color3 + "Grandpa").addEnchantment(Enchantment.KNOCKBACK, 2).setUnbreakable().build()}), false),

    HERMIT("Hermit", "legendhg.kits.hermit", new ItemManager(Material.GRASS, Util.color3 + "Hermit").setLore(Arrays.asList(Util.success + "Ao iniciar a partida", Util.success + "seja teleportado para borda do mapa.")).build(), Collections.emptyList(), false),

    TITAN("Titan", "legendhg.kits.titan", new ItemManager(Material.BEDROCK, Util.color3 + "Titan").setLore(Arrays.asList(Util.success + "Fique invencivel", Util.success + "por 10 segundos.")).build(), Arrays.asList(new ItemStack[] {new ItemManager(Material.BEDROCK, Util.color3 + "Titan").build()}), false),

    BRAND("Brand", "legendhg.kits.brand", new ItemManager(Material.FLINT_AND_STEEL, Util.color3 + "Brand").setLore(Arrays.asList(Util.success + "Ganhe força", Util.success + "enquanto estiver pegando fogo.")).build(), Arrays.asList(new ItemStack[] {new ItemManager(Material.FLINT_AND_STEEL, Util.color3 + "Brand").build()}), false),

    IRONMAN("Ironman", "legendhg.kits.ironman", new ItemManager(Material.IRON_INGOT, Util.color3 + "Ironman").setLore(Arrays.asList(Util.success + "Ganhe iron a cada kill.", Util.success + "Ganhe 2 iron a partir de 5 kills.")).build(), Collections.emptyList(), false),

    MAGMA("Magma", "legendhg.kits.magma", new ItemManager(Material.MAGMA_CREAM, Util.color3 + "Magma").setLore(Arrays.asList(Util.success + "Ao bater em um jogador", Util.success + "tenha chance de aplicar fogo nele.", Util.success + "Não recebe dano de fogo.")).build(), Collections.emptyList(), false),

    POPAI("Popai", "legendhg.kits.popai", new ItemManager(Material.CARROT_ITEM, Util.color3 + "Popai").setLore(Arrays.asList(Util.success + "Ao comer fique imune", Util.success + "a efeitos negativos", Util.success + "e ganhe regeneração por 60s.")).build(), Arrays.asList(new ItemStack[] {new ItemManager(Material.CARROT_ITEM, Util.color3 + "Popai").setAmount(5).build()}), false),

    TIMERLORD("Timerlord", "legendhg.kits.timerlord", new ItemManager(Material.WATCH, Util.color3 + "Timerlord").setLore(Arrays.asList(Util.success + "Ao clicar com o relogio", Util.success + "congele os jogadores em sua volta.")).build(), Arrays.asList(new ItemStack[] {new ItemManager(Material.WATCH, Util.color3 + "Timerlord").setUnbreakable().build()}), false),

    POSEIDON("Poseidon", "legendhg.kits.poseidon", new ItemManager(Material.WATER_BUCKET, Util.color3 + "Poseidon").setLore(Arrays.asList(Util.success + "Fique forte", Util.success + "ao entrar na agua!")).build(), Collections.emptyList(), false),
    ;

    private final String permission;
    private final String name;
    private final ItemStack kitIcon;
    private final List<ItemStack> kitItem;
    private final boolean combatLog;

    Kits(String name, String permission, ItemStack kitIcon, List<ItemStack> kitItem, boolean combatLog) {
        this.permission = permission;
        this.name = name;
        this.kitIcon = kitIcon;
        this.kitItem = kitItem;
        this.combatLog = combatLog;
    }

    public String getPermission() {
        return permission;
    }

    public String getName() {
        return name;
    }

    public ItemStack getKitIcon() {
        return kitIcon;
    }

    public List<ItemStack> getKitItem() {
        return kitItem;
    }

    public boolean getCombatLog() {
        return combatLog;
    }

}
