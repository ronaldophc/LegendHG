package com.ronaldophc.database;

import com.ronaldophc.feature.punish.banip.BanIP;
import com.ronaldophc.util.Logger;
import org.bukkit.Bukkit;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class BanRepository {

    public static boolean banIP(String ipAddress, long duration, String bannedBy, String reason) {
        if (!MySQLManager.isActive) return false;
        try (Connection conn = MySQLManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO ip_bans (ip_address, end_time, banned_by, reason)" +
                     " VALUES (?, ?, ?, ?)")) {

            stmt.setString(1, ipAddress);
            stmt.setLong(2, duration);
            stmt.setString(3, bannedBy);
            stmt.setString(4, reason);
            stmt.executeUpdate();

            return true;
        } catch (SQLException e) {
            Logger.logError("Erro ao banir IP: " + e.getMessage());
            return false;
        }
    }

    public static boolean unbanIP(String ipAddress) {
        if (!MySQLManager.isActive) return false;
        try (Connection conn = MySQLManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("UPDATE ip_bans SET active = false, unbanned_at = CURRENT_TIMESTAMP WHERE ip_address = ? AND active = true")) {

            stmt.setString(1, ipAddress);
            stmt.executeUpdate();

            return true;
        } catch (SQLException e) {
            Logger.logError("Erro ao desbanir IP: " + e.getMessage());
            return false;
        }
    }

    public static long getBanIPTime(String ipAddress) {
        if (!MySQLManager.isActive) return 0;
        try (Connection conn = MySQLManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT end_time FROM ip_bans WHERE ip_address = ? AND active = true")) {
            stmt.setString(1, ipAddress);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getLong("end_time");
            }
        } catch (SQLException e) {
            Logger.logError("Erro ao verificar banIP: " + e.getMessage());
        }
        return 0;
    }

    public static boolean isIPBanned(String ipAddress) {
        if (!MySQLManager.isActive) return false;
        try (Connection conn = MySQLManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT end_time FROM ip_bans WHERE ip_address = ? AND active = true")) {
            stmt.setString(1, ipAddress);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                long time = rs.getLong("end_time");
                if (time < System.currentTimeMillis()) {
                    unbanIP(ipAddress);
                    return false;
                }
                return true;
            }
        } catch (SQLException e) {
            Logger.logError("Erro ao verificar banIP: " + e.getMessage());
        }
        return false;
    }

    public static void unbanExpiredIPs() {
        if (!MySQLManager.isActive) return;
        String query = "UPDATE ip_bans SET active = false, unbanned_at = CURRENT_TIMESTAMP WHERE end_time <= ? AND active = true";

        try (Connection conn = MySQLManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setLong(1, System.currentTimeMillis());
            stmt.executeUpdate();
        } catch (SQLException e) {
            Logger.logError("Erro ao desbanir IPs expirados: " + e.getMessage());
        }
    }

    // Pegar histórico de banimentos
    public static List<BanIP> getBanIPHistory(String ipAddress) {
        if (!MySQLManager.isActive) return new ArrayList<>();
        List<BanIP> bans = new ArrayList<>();
        try (Connection conn = MySQLManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM ip_bans WHERE ip_address = ? ORDER BY banned_at DESC")) {
            stmt.setString(1, ipAddress);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String bannedBy = rs.getString("banned_by");
                String reason = rs.getString("reason");
                long end_time = rs.getLong("end_time");
                boolean active = rs.getBoolean("active");
                Timestamp banned_at = rs.getTimestamp("banned_at");
                Timestamp unbanned_at = rs.getTimestamp("unbanned_at");
                BanIP newBan = new BanIP(InetAddress.getByName(ipAddress), end_time, bannedBy, reason, active, banned_at, unbanned_at);
                bans.add(newBan);
            }
        } catch (SQLException e) {
            Logger.logError("Erro ao carregar histórico banIP: " + e.getMessage());
        } catch (UnknownHostException e) {
            Logger.logError("Erro ao carregar histórico banIP: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return bans;
    }

    public static BanIP getActiveBanIP(String ipAddress) {
        if (!MySQLManager.isActive) return null;
        try (Connection conn = MySQLManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM ip_bans WHERE ip_address = ? AND active = true")) {
            stmt.setString(1, ipAddress);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String bannedBy = rs.getString("banned_by");
                String reason = rs.getString("reason");
                long end_time = rs.getLong("end_time");
                boolean active = rs.getBoolean("active");
                Timestamp banned_at = rs.getTimestamp("banned_at");
                Timestamp unbanned_at = rs.getTimestamp("unbanned_at");
                return new BanIP(InetAddress.getByName(ipAddress), end_time, bannedBy, reason, active, banned_at, unbanned_at);
            }
        } catch (SQLException e) {
            Logger.logError("Erro ao carregar banIP ativo: " + e.getMessage());
        } catch (UnknownHostException e) {
            Logger.logError("Erro ao carregar banIP ativo: " + e.getMessage());
            throw new RuntimeException(e);
        }
        return null;
    }
}
