package com.ronaldophc.command.admin;

import com.ronaldophc.util.Util;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class UpdateCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("updates")) {

            if (!commandSender.isOp() && !commandSender.hasPermission("legendhg.admin.updates")) {
                commandSender.sendMessage(Util.noPermission);
                return true;
            }

            // Abrir um inventário contendo um papel por atualização, nele mostrara um resumo e a data da atualização.
            // Ao clicar no papel fecha o inventário e manda uma mensagem no chat com as informações da atualização.

            commandSender.sendMessage("§7[§f22/01/25§7]§b- §fAdicionado comandos /updates, /ban, /unmute e /mute.");
            commandSender.sendMessage("§7[§f23/01/25§7]§b- §fAdicionado comando /unban.");

            return true;
        }
        return false;
    }
}
