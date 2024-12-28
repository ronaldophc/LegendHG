package com.ronaldophc.command;

import com.ronaldophc.feature.SkinFix;
import com.ronaldophc.helper.MasterHelper;
import com.ronaldophc.helper.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class FakeCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("fake")) {
            if (commandSender == null) return true;
            if (!(commandSender instanceof Player)) {
                commandSender.sendMessage(Util.noConsole);
                return true;
            }

            Player player = (Player) commandSender;

            if (!commandSender.isOp() && !commandSender.hasPermission("legendhg.fake")) {
                commandSender.sendMessage(Util.noPermission);
                return true;
            }

            if (strings.length != 1) {
                commandSender.sendMessage(Util.usage("/fake <fakeName | reset | list>"));
                return true;
            }

            if (strings[0].equalsIgnoreCase("reset")) {
                try {
                    resetFake(player);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                return true;
            }

            String newName = strings[0];
            try {
                if (newName.length() > 16) {
                    player.sendMessage(Util.error + "O nome não pode ser mais que 16 caracteres");
                    return true;
                }
                setFake(player, newName);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return true;
    }

    private static void setFake(Player player, String name) throws Exception {
        if (!SkinFix.changePlayerSkin(player, name)) {
            player.sendMessage(Util.error + "Skin não encontrada para o fake");
        }
        setSettings(player, name);
        player.sendMessage(Util.color1 + "Seu nome foi alterado para " + player.getCustomName());
    }

    private static void resetFake(Player player) throws Exception {
        if (!SkinFix.fixPlayerSkin(player)) {
            player.sendMessage(Util.error + "Erro ao resetar a skin");
        }
        setSettings(player, player.getName());
        player.sendMessage(Util.color1 + "Seu fake foi resetado");
    }

    private static void setSettings(Player player, String name) throws Exception {
        player.setCustomName(name);
        MasterHelper.refreshPlayer(player);
    }
}
