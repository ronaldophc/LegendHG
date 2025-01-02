package com.ronaldophc.listener;

import com.ronaldophc.LegendHG;
import com.ronaldophc.constant.Tags;
import com.ronaldophc.database.PlayerSQL;
import com.ronaldophc.helper.Util;
import com.ronaldophc.player.account.Account;
import com.ronaldophc.player.account.AccountManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.scheduler.BukkitRunnable;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class AsyncPlayerChat implements Listener {

    private final String[] blockedWords = {
            "aidético", "aidética", "aleijado", "aleijada", "analfabeto", "analfabeta",
            "anus", "arrombado", "babaca", "bacura", "bagos", "baitola", "bicha", "bixa", "boceta", "boiola",
            "bokete", "bolcat", "boquete", "bosseta", "bosta", "bostana", "boçal", "bronha", "buca", "buceta",
            "bunduda", "burra", "burro", "busseta", "caceta", "cacete", "canalha", "canceroso", "casseta", "cassete",
            "checheca", "chereca", "chibumba", "chibumbo", "chifruda", "chifrudo", "chochota", "chota", "chupada", "chupado", "cocaina", "cocaína",
            "comunista", "corna", "cornagem", "cornisse", "corno", "cornuda", "cornudo", "cornão", "coxo", "cretina", "cretino", "criolo", "crioulo",
            "cu", "cú", "culhao", "culhão", "curalho", "cuzao", "cuzão", "cuzuda", "cuzudo",
            "debil", "débil", "debiloide", "debilóide", "deficiente", "detento", "difunto", "doida", "doido", "egua",
            "égua", "esclerosado", "escrota", "escroto", "esporrada", "esporrado", "estupida", "estúpida", "estupidez", "estupido", "estúpido",
            "facista", "fascista", "fedida", "fedido", "fedorenta", "feiosa", "feioso", "feioza", "feiozo",
            "fodida", "fodido", "fudeção", "fudida", "fudido", "furada", "furado", "gai", "gaiata",
            "gaiato", "gay", "gonorrea", "gonorreia", "gonorréia", "gosmenta", "gosmento", "grelinho", "homo-sexual", "homosexual", "homosexualismo",
            "homossexual", "homossexualismo", "idiota", "idiotice", "imbecil", "iscrota", "iscroto", "lazarento", "leprosa", "leproso", "lesbica", "lésbica", "louco",
            "macaca", "macaco", "machona", "macumbeiro", "malandro", "maluco", "maneta", "marginal", "masturba",
            "minorias", "mocrea", "mocreia", "mocréia", "mongol", "mongoloide", "mongolóide", "mulata", "mulato", "nazista", "negro",
            "otaria", "otario", "otária", "otário", "pau", "peia", "pemba", "pentelha", "pentelho", "perereca", "pica", "picao",
            "picão", "pinto", "pintudo", "pintão", "piranha", "piroca", "piroco", "piru", "prega", "prequito", "priquito",
            "prostibulo", "prostituta", "prostituto", "punheta", "punhetao", "punhetão", "pus", "pustula", "puta", "puto", "penis", "pênis", "rachada",
            "rachadao", "rachadinha", "rachadinho", "rachado", "retardada", "retardado", "ridícula", "rola", "rolinha",
            "sifilis", "sífilis", "siririca", "tarada", "tarado", "tesuda", "tesudo", "tezao", "tezuda", "tezudo", "traveco", "trocha",
            "trolha", "troucha", "trouxa", "troxa", "tuberculoso", "vadia", "vagabunda", "vagabundo", "vagina",
            "viada", "viadagem", "viadao", "viadão", "viado", "víado", "xana", "xaninha", "xavasca", "xerereca", "xexeca", "xibumba", "xiíta", "xochota", "xota", "xoxota"
    };

    private final List<Player> cooldown;

    public AsyncPlayerChat() {
        cooldown = new ArrayList<>();
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();

        try {
            if (!PlayerSQL.isPlayerLoggedIn(player)) {
                event.setCancelled(true);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        if (cooldown.contains(player)) {
            player.sendMessage(Util.error + "Espere um pouco para mandar mensagem novamente");
            event.setCancelled(true);
        }

        cooldown.add(player);
        new BukkitRunnable() {

            @Override
            public void run() {
                cooldown.remove(player);
            }
        }.runTaskLater(LegendHG.getInstance(), 20);

        String message = event.getMessage();

        for (String word : blockedWords) {
            message = message.replaceAll("(?i)" + word, new String(new char[word.length()]).replace("\0", "*"));
        }


        Account account = LegendHG.getAccountManager().getOrCreateAccount(player);
        Tags tag = account.getTag();
        event.setFormat(tag.getColor() + tag.name() + " §7" + account.getActualName() + " §8» §f" + message);

    }
}
