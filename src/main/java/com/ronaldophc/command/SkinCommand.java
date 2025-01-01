package com.ronaldophc.command;

import com.ronaldophc.feature.SkinManager;
import com.ronaldophc.helper.Logger;
import com.ronaldophc.helper.MasterHelper;
import com.ronaldophc.helper.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SkinCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (!command.getName().equalsIgnoreCase("skin")) {
            return true;
        }

        if (!(commandSender instanceof Player)) {
            commandSender.sendMessage(Util.noConsole);
            return true;
        }

        Player player = (Player) commandSender;
        if (!commandSender.isOp() && !commandSender.hasPermission("legendhg.skin")) {
            commandSender.sendMessage(Util.noPermission);
            return true;
        }

        if (strings.length == 0) {
            commandSender.sendMessage(Util.usage("/skin <nome>"));
            return true;
        }

        String skin = strings[0];

        if (skin.length() > 16) {
            player.sendMessage(Util.error + "O nome da skin não pode ter mais de 16 caracteres.");
            return true;
        }

        try {
            if (skin.equalsIgnoreCase("reset")) {
                if (SkinManager.fixPlayerSkin(player)) {
                    MasterHelper.refreshPlayer(player);
                    player.sendMessage(Util.success + "Sua skin foi resetada com sucesso.");
                } else {
                    player.sendMessage(Util.error + "Ocorreu um erro ao tentar alterar sua skin.");
                }
                return true;
            }
            if (SkinManager.changePlayerSkin(player, skin)) {
                MasterHelper.refreshPlayer(player);
                player.sendMessage(Util.success + "Sua skin foi alterada com sucesso.");
                return true;
            }
            player.sendMessage(Util.error + "Skin não encontrada");
        } catch (Exception e) {
            Logger.logError("Erro ao alterar skin: " + e.getMessage());
        }
        return true;
    }
}