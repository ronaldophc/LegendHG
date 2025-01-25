package com.ronaldophc.command.admin;

import com.ronaldophc.LegendHG;
import com.ronaldophc.feature.punish.PunishHelper;
import com.ronaldophc.feature.punish.banip.BanIP;
import com.ronaldophc.feature.punish.banip.BanIPService;
import com.ronaldophc.util.Util;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

public class BanIpCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        if (command.getName().equalsIgnoreCase("banip")) {

            if (!commandSender.hasPermission("legendhg.admin.banip")) {
                commandSender.sendMessage(Util.noPermission);
                return true;
            }

            if (strings.length <= 1) {
                commandSender.sendMessage(Util.usage("§c/banip §e<ipAddress§c|§eplayer> §b<duração§d(m, h, d)§c|§bhistory> §a<razão>"));
                return true;
            }

            try {

                BanIPService banService = new BanIPService();
                String target = strings[0];
                InetAddress inetAddress;

                // Check if the target is a player's nickname
                Player targetPlayer = Bukkit.getPlayer(target);
                if (targetPlayer != null) {
                    inetAddress = targetPlayer.getAddress().getAddress();
                } else {
                    inetAddress = InetAddress.getByName(target);
                }

                if (strings.length == 2) {
                    String arg = strings[1];
                    if (!arg.equalsIgnoreCase("history")) {
                        commandSender.sendMessage("2");
                        commandSender.sendMessage(Util.usage("§c/banip §e<ipAddress§c|§eplayer> §b<duração§d(m, h, d)§c|§bhistory> §a<razão>"));
                        return true;
                    }

                    if (banService.getBanIPHistory(inetAddress).isEmpty()) {
                        commandSender.sendMessage(Util.error + "Histórico de banimento vazio.");
                        return true;
                    }

                    commandSender.sendMessage(Util.admin + "Histórico de banimento do IP: §e" + inetAddress.getHostAddress());
                    if (commandSender instanceof Player) {
                        banService.openBanIpHistoryInventory((Player) commandSender, inetAddress);
                        return true;
                    }

                    for (BanIP ban : banService.getBanIPHistory(inetAddress)) {
                        List<String> message = getStrings(ban);
                        commandSender.sendMessage(message.toArray(new String[0]));
                    }

                    return true;
                }

                String ipAddress = inetAddress.getHostAddress();

                if (banService.isIPBanned(inetAddress)) {
                    commandSender.sendMessage(
                            Util.error + "IP já está banido: §e" + inetAddress.getHostAddress() + Util.admin + "." +
                                    " Expira em: §e" + banService.getExpire_atFormated(inetAddress));
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

                BanIP ban = new BanIP(inetAddress, System.currentTimeMillis() + duration, commandSender.getName(), reason);
                if (banService.banIP(ban)) {
                    for (Player player : Bukkit.getOnlinePlayers()) {
                        if (player.getAddress().getAddress() == inetAddress) {
                            player.kickPlayer(getKickMessage(ban));
                        }
                    }
                    LegendHG.logger.info("IP banido: " + ipAddress + " por " + commandSender.getName() + " por " + formatedDuration + ". Motivo: " + reason);
                    commandSender.sendMessage(Util.admin + "IP banido: §e" + ipAddress + Util.admin + " por §e" + formatedDuration + Util.admin + ". Motivo: §e" + reason);
                    return true;
                }

                commandSender.sendMessage(Util.errorServer + "Erro ao banir IP. Contate o DEV");
            } catch (NumberFormatException e) {

                commandSender.sendMessage(Util.error + "Duração inválida.");
                return true;
            } catch (UnknownHostException e) {

                commandSender.sendMessage(Util.error + "IP inválido.");
                return true;
            }

            return true;
        }
        return true;
    }

    private static String getKickMessage(BanIP banIP) {
        String expireAt = "§aIP banido até: " + Util.color3 + banIP.getExpire_atFormated();
        String reason = "§aMotivo: " + Util.color3 + banIP.getReason();
        String linkDiscord = LegendHG.settings.getString("Discord");

        return Util.title + "\n\n"
                + "§a§lSeu IP foi banido!\n"
                + expireAt + "\n"
                + reason + "\n"
                + "§aDiscord: " + Util.color3 + linkDiscord;
    }

    private static List<String> getStrings(BanIP ban) {
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
