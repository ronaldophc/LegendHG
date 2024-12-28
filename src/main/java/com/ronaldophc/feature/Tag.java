package com.ronaldophc.feature;

import com.ronaldophc.constant.Tags;
import com.ronaldophc.database.PlayerSQL;
import com.ronaldophc.helper.Util;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.entity.Player;

import java.sql.SQLException;

public class Tag {

    public static void setTag(Player player, Tags tag) {
        try {
            String name = tag.getColor() + player.getCustomName();
            PlayerSQL.setPlayerTag(player, tag);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void setTag(Player player) {

        try {
            Tags tag = getTag(player);
            setTag(player, tag);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Tags getTag(Player player) throws SQLException {
        return PlayerSQL.getPlayerTag(player);
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
