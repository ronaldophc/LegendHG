package com.ronaldophc.feature;

import com.ronaldophc.constant.Tags;
import com.ronaldophc.database.PlayerSQL;
import com.ronaldophc.helper.Util;
import com.ronaldophc.player.account.Account;
import com.ronaldophc.player.account.AccountManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import java.sql.SQLException;

public class TagManager {

    public static void setTag(Player player, Tags tag) {
        try {
            Account account = AccountManager.getOrCreateAccount(player);
            account.setTag(tag);
            PlayerSQL.setPlayerTag(player, tag);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Tags getTagSQL(Player player) {
        try {
            return PlayerSQL.getPlayerTag(player);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void sendTagList(Player player) {
        player.sendMessage(Util.color1 + "Escolha sua tag:");

        for (Tags tag : Tags.values()) {
            if (player.hasPermission(tag.getPermission())) {
                TextComponent message = new TextComponent(tag.name());
                message.setColor(ChatColor.valueOf(tag.getColor().name()));
                message.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/tag " + tag.name()));
                player.spigot().sendMessage(message);
            }
        }
    }
}
