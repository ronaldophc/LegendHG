package com.ronaldophc.util;

import com.ronaldophc.LegendHG;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

public class Util {

    /*
    §0 - Preto
    §1 - Azul escuro
    §2 - Verde escuro
    §3 - Ciano escuro
    §4 - Vermelho escuro
    §5 - Roxo
    §6 - Dourado
    §7 - Cinza claro
    §8 - Cinza escuro
    §9 - Azul claro
    §a - Verde claro
    §b - Ciano claro
    §c - Vermelho claro
    §d - Rosa
    §e - Amarelo
    §f - Branco
    */

    public static String bold = "§l";
    public static String color1 = "§b";
    public static String color2 = "§f";
    public static String color3 = "§e";
    public static String success = "§a";
    public static String admin = "§d";
    public static String error = "§c";
    public static String errorServer = "§4";
    public static String title = "§b§lLegendHG";
    public static String noPermission = error + "Voce não tem permissao para executar este comando.";
    public static String noConsole = error + "Não pode executar este comando no console.";
    public static String noPlayer = error + "Jogador não encontrado.";

    public static String formatSeconds(int totalSeconds) {
        int minutes = totalSeconds / 60;
        int seconds = totalSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    public static String usage(String usage) {
        return Util.bold + Util.color1 + "Use " + Util.bold + Util.color2 + usage;
    }

    public static void errorCommand(String comando, Exception e) {
        LegendHG.logger.info("Erro ao usar o comando " + comando + ": " + e.getMessage());
    }

    public static void playSoundForAll(Sound sound) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.playSound(player.getLocation(), sound, 1.0f, 1.0f);
        }
    }
}
