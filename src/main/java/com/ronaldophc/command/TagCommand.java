package com.ronaldophc.command;

import com.ronaldophc.constant.Tags;
import com.ronaldophc.feature.TagManager;
import com.ronaldophc.helper.MasterHelper;
import com.ronaldophc.helper.Util;
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
                TagManager.sendTagList(player);
                return true;
            }

            if (strings.length == 1) {
                try {
                    Tags tag = Tags.valueOf(strings[0].toUpperCase());

                    if (!player.isOp() && !player.hasPermission(tag.getPermission())) {
                        player.sendMessage(Util.error + "Voce nao tem permissao para usar esta tag.");
                        return true;
                    }

                    TagManager.setTag(player, tag);
                    MasterHelper.refreshPlayer(player);
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

}
