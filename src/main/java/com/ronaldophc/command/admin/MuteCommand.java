package com.ronaldophc.command.admin;

import com.ronaldophc.LegendHG;
import com.ronaldophc.database.PlayerRepository;
import com.ronaldophc.feature.punish.PunishHelper;
import com.ronaldophc.feature.punish.mute.Mute;
import com.ronaldophc.feature.punish.mute.MuteService;
import com.ronaldophc.player.PlayerService;
import com.ronaldophc.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MuteCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("mute")) {

            MuteService muteService = new MuteService();

            if (strings.length == 0) {
                if (!(commandSender instanceof Player)) {
                    commandSender.sendMessage(Util.noConsole);
                    return true;
                }

                Player player = (Player) commandSender;
                if (!muteService.isMuted(player.getUniqueId())) {
                    player.sendMessage(Util.error + "Você não está mutado!");
                    if (player.hasPermission("legendhg.admin.mute")) {
                        player.sendMessage(Util.usage("§c/mute §e<player> §b<duração§d(m, h, d)§c|§bhistory> §a<razão>"));
                    }
                    return true;
                }

                Mute mute = muteService.getActiveMute(player.getUniqueId());
                String reason = mute.getReason();
                String expire = mute.getExpire_atFormated();
                player.sendMessage(Util.error + "Você está mutado por: §e" + reason + " §c. Expira em: §e" + expire);
                return true;
            }

            if (!commandSender.hasPermission("legendhg.admin.mute")) {
                commandSender.sendMessage(Util.noPermission);
                return true;
            }

            if (strings.length == 1) {
                commandSender.sendMessage(Util.usage("§c/mute §e<player> §b<duração§d(m, h, d)§c|§bhistory> §a<razão>"));
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
                    commandSender.sendMessage(Util.usage("§c/mute §e<player> §b<duração§d(m, h, d)§c|§bhistory> §a<razão>"));
                    return true;
                }

                if (muteService.getMuteHistory(uuid).isEmpty()) {
                    commandSender.sendMessage(Util.error + "Histórico de mute vazio.");
                    return true;
                }

                commandSender.sendMessage(Util.admin + "Histórico de mute do jogador: §e" + name);
                if (commandSender instanceof Player) {
                    Player player = (Player) commandSender;
                    muteService.openMuteHistoryInventory(player, uuid);
                    return true;
                }

                for (Mute mute : muteService.getMuteHistory(uuid)) {
                    List<String> message = getMessage(mute);
                    for (String msg : message) {
                        commandSender.sendMessage(msg);
                    }
                }
                return true;
            }

            if (muteService.isMuted(uuid)) {
                commandSender.sendMessage(
                        Util.error + "Jogador já está mutado: §e" + name + Util.admin + "." +
                                " Expira em: §e" + muteService.getExpire_atFormated(uuid));
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

            Mute mute = new Mute(uuid, System.currentTimeMillis() + duration, commandSender.getName(), reason);
            if (muteService.mute(mute)) {
                LegendHG.logger.info("Jogador mutado: " + name + " por " + commandSender.getName() + " por " + formatedDuration + ". Motivo: " + reason);

                Player target = Bukkit.getPlayer(uuid);
                if (target != null) {
                    target.sendMessage(Util.error + "Você foi mutado por " + formatedDuration + ". Motivo: " + reason);
                }

                commandSender.sendMessage(Util.admin + "Jogador §e" + name + Util.admin + " foi mutado por §e" + formatedDuration + Util.admin + " com sucesso.");
                return true;
            }

            commandSender.sendMessage(Util.error + "Erro ao mutar jogador. Contate o DEV");
        }
        return true;
    }

    private static List<String> getMessage(Mute mute) {
        List<String> message = new ArrayList<>();
        message.add("\n");
        message.add("§b§cEm: §a" + mute.getMuted_atFormated());
        message.add("§cPor: §a" + mute.getMuted_by());
        message.add("§cMotivo §a" + mute.getReason());
        message.add("§cDuração: §a" + mute.getDurationFormated());
        if (mute.isExpired()) {
            message.add("§cExpirou: §a" + mute.getExpire_atFormated());
        } else {
            message.add("§cExpira: §a" + mute.getExpire_atFormated());
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
