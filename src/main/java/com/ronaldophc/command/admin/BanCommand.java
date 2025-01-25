package com.ronaldophc.command.admin;

import com.ronaldophc.LegendHG;
import com.ronaldophc.feature.punish.PunishHelper;
import com.ronaldophc.feature.punish.ban.Ban;
import com.ronaldophc.feature.punish.ban.BanService;
import com.ronaldophc.player.PlayerService;
import com.ronaldophc.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BanCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("ban")) {

            BanService banService = new BanService();

            if (!commandSender.hasPermission("legendhg.admin.ban")) {
                commandSender.sendMessage(Util.noPermission);
                return true;
            }

            if (strings.length <= 1) {
                commandSender.sendMessage(Util.usage("§c/ban §e<player> §b<duração§d(m, h, d)§c|§bhistory> §a<razão>"));
                return true;
            }

            String name = strings[0];

            if (!PlayerService.isPlayerRegistered(name)) {
                commandSender.sendMessage(Util.error + "Jogador '" + name + "' não encontrado.");
                return true;
            }

            UUID uuid = PlayerService.getUUIDByName(name);

            if (uuid == null) {
                commandSender.sendMessage(Util.noPlayer);
                return true;
            }

            if (strings.length == 2) {
                String arg = strings[1];
                if (!arg.equalsIgnoreCase("history")) {
                    commandSender.sendMessage(Util.usage("§c/ban §e<player> §b<duração§d(m, h, d)§c|§bhistory> §a<razão>"));
                    return true;
                }

                if (banService.getBanHistory(uuid).isEmpty()) {
                    commandSender.sendMessage(Util.error + "Histórico de ban vazio.");
                    return true;
                }

                commandSender.sendMessage(Util.admin + "Histórico de ban do jogador: §e" + name);
                if (commandSender instanceof Player) {
                    Player player = (Player) commandSender;
                    banService.openBanHistoryInventory(player, uuid);
                    return true;
                }

                for (Ban ban : banService.getBanHistory(uuid)) {
                    List<String> message = getMessage(ban);
                    for (String msg : message) {
                        commandSender.sendMessage(msg);
                    }
                }
                return true;
            }

            if (banService.isBanned(uuid)) {
                commandSender.sendMessage(
                        Util.error + "Jogador já está banido: §e" + name + Util.admin + "." +
                                " Expira em: §e" + banService.getExpire_atFormated(uuid));
                return true;
            }

            String durationStr = strings[1];

            char unit = durationStr.charAt(durationStr.length() - 1);
            long duration = parseDuration(durationStr, unit);
            String formatedDuration = PunishHelper.formatTime(duration);

            String reason = "";
            for (int i = 2; i < strings.length; i++) {
                reason = String.valueOf(reason) + strings[i] + " ";
            }

            Ban ban = new Ban(uuid, System.currentTimeMillis() + duration, commandSender.getName(), reason);
            if (banService.ban(ban)) {
                LegendHG.logger.info("Jogador banido: " + name + " por " + commandSender.getName() + " por " + formatedDuration + ". Motivo: " + reason);

                for (Player player : Bukkit.getOnlinePlayers()) {
                    if (player.getName().equals(name)) {
                        player.kickPlayer(getKickMessage(ban));
                    }
                }

                commandSender.sendMessage(Util.admin + "Jogador §e" + name + Util.admin + " foi banido por §e" + formatedDuration + Util.admin + " com sucesso.");
                return true;
            }

            commandSender.sendMessage(Util.error + "Erro ao banir jogador. Contate o DEV");
        }
        return true;
    }

    private static String getKickMessage(Ban ban) {
        String expireAt = "§aBanido até: " + Util.color3 + ban.getExpire_atFormated();
        String reason = "§aMotivo: " + Util.color3 + ban.getReason();
        String linkDiscord = LegendHG.settings.getString("Discord");

        return Util.title + "\n\n"
                + "§a§lVocê foi banido!\n"
                + expireAt + "\n"
                + reason + "\n"
                + "§aDiscord: " + Util.color3 + linkDiscord;
    }

    private static List<String> getMessage(Ban ban) {
        List<String> message = new ArrayList<>();
        message.add("\n");
        message.add("§b§cEm: §a" + ban.getBanned_atFormated());
        message.add("§cPor: §a" + ban.getBanned_by());
        message.add("§cMotivo §a" + ban.getReason());
        message.add("§cDuração: §a" + ban.getDurationFormated());
        if (ban.isExpired()) {
            message.add("§cExpirou: §a" + ban.getExpire_atFormated());
        } else {
            message.add("§cExpira: §a" + ban.getExpire_atFormated());
        }
        return message;
    }

    private long parseDuration(String durationStr, char unit) throws NumberFormatException {
        long duration = Long.parseLong(durationStr.substring(0, durationStr.length() - 1));
        switch (unit) {
            case 'm':
                return duration * 60 * 1000; // minutes to milliseconds
            case 'h':
                return duration * 60 * 60 * 1000; // hours to milliseconds
            case 'd':
                return duration * 24 * 60 * 60 * 1000; // days to milliseconds
            default:
                throw new NumberFormatException("Invalid time unit");
        }
    }
}
