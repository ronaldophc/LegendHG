package com.ronaldophc.command;

import com.ronaldophc.constant.Tags;
import com.ronaldophc.helper.Helper;
import com.ronaldophc.helper.Util;
import com.ronaldophc.player.account.Account;
import com.ronaldophc.player.account.AccountManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class TagCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("tag")) {

            if (!(commandSender instanceof Player)) {
                commandSender.sendMessage(Util.noConsole);
                return true;
            }

            Player player = (Player) commandSender;

            if (strings.length == 0) {
                sendTagList(player);
                return true;
            }

            if (strings.length == 1) {
                try {
                    Tags tag = Tags.valueOf(strings[0].toUpperCase());

                    if (!player.isOp() && !player.hasPermission(tag.getPermission())) {
                        player.sendMessage(Util.error + "Voce nao tem permissao para usar esta tag.");
                        return true;
                    }

                    Account account = AccountManager.getInstance().getOrCreateAccount(player);

                    account.setTag(tag);
                    Helper.refreshPlayer(player);
                    player.sendMessage(Util.color1 + "Tag alterada para " + tag.getColor() + tag.name());
                } catch (IllegalArgumentException e) {
                    player.sendMessage(Util.color1 + "Tag não encontrada.");
                    return true;
                }
            }
            return true;
        }
        return true;
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
