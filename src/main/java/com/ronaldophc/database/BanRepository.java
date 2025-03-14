package com.ronaldophc.database;

import com.ronaldophc.feature.punish.ban.Ban;
import com.ronaldophc.util.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BanRepository {

    public static boolean ban(UUID uuid, long duration, String bannedBy, String reason) {
        if (!MySQLManager.isActive) return false;
        try (Connection conn = MySQLManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO bans (uuid, end_time, banned_by, reason)" +
                     " VALUES (?, ?, ?, ?)")) {

            stmt.setString(1, uuid.toString());
            stmt.setLong(2, duration);
            stmt.setString(3, bannedBy);
            stmt.setString(4, reason);
            stmt.executeUpdate();

            return true;
        } catch (SQLException e) {
            Logger.logError("Erro ao banir: " + e.getMessage());
            return false;
        }
    }

    public static boolean unban(UUID uuid) {
        if (!MySQLManager.isActive) return false;
        try (Connection conn = MySQLManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("UPDATE bans SET active = false, unbanned_at = CURRENT_TIMESTAMP WHERE uuid = ? AND active = true")) {

            stmt.setString(1, uuid.toString());
            stmt.executeUpdate();

            return true;
        } catch (SQLException e) {
            Logger.logError("Erro ao desbanir: " + e.getMessage());
            return false;
        }
    }

    public static long getBanEndTime(UUID uuid) {
        if (!MySQLManager.isActive) return 0;
        try (Connection conn = MySQLManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT end_time FROM bans WHERE uuid = ? AND active = true")) {
            stmt.setString(1, uuid.toString());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getLong("end_time");
            }
        } catch (SQLException e) {
            Logger.logError("Erro ao verificar banIP: " + e.getMessage());
        }
        return 0;
    }

    public static boolean isBanned(UUID uuid) {
        if (!MySQLManager.isActive) return false;
        try (Connection conn = MySQLManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT end_time FROM bans WHERE uuid = ? AND active = true")) {
            stmt.setString(1, uuid.toString());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                long time = rs.getLong("end_time");
                if (time < System.currentTimeMillis()) {
                    unban(uuid);
                    return false;
                }
                return true;
            }
        } catch (SQLException e) {
            Logger.logError("Erro ao verificar ban: " + e.getMessage());
        }
        return false;
    }

    public static void unbanExpired() {
        if (!MySQLManager.isActive) return;
        String query = "UPDATE bans SET active = false, unbanned_at = CURRENT_TIMESTAMP WHERE end_time <= ? AND active = true";

        try (Connection conn = MySQLManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setLong(1, System.currentTimeMillis());
            stmt.executeUpdate();
        } catch (SQLException e) {
            Logger.logError("Erro ao desbanir expirados: " + e.getMessage());
        }
    }

    // Pegar histórico de banimentos
    public static List<Ban> getBanHistory(UUID uuid) {
        if (!MySQLManager.isActive) return new ArrayList<>();
        List<Ban> bans = new ArrayList<>();
        try (Connection conn = MySQLManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM bans WHERE uuid = ? ORDER BY banned_at DESC")) {
            stmt.setString(1, uuid.toString());
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String bannedBy = rs.getString("banned_by");
                String reason = rs.getString("reason");
                long end_time = rs.getLong("end_time");
                boolean active = rs.getBoolean("active");
                Timestamp banned_at = rs.getTimestamp("banned_at");
                Timestamp unbanned_at = rs.getTimestamp("unbanned_at");
                Ban newBan = new Ban(uuid, end_time, bannedBy, reason, active, banned_at, unbanned_at);
                bans.add(newBan);
            }
        } catch (SQLException e) {
            Logger.logError("Erro ao carregar histórico ban: " + e.getMessage());
        }
        return bans;
    }

    public static Ban getActiveBan(UUID uuid) {
        if (!MySQLManager.isActive) return null;
        try (Connection conn = MySQLManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM bans WHERE uuid = ? AND active = true")) {
            stmt.setString(1, uuid.toString());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String bannedBy = rs.getString("banned_by");
                String reason = rs.getString("reason");
                long end_time = rs.getLong("end_time");
                boolean active = rs.getBoolean("active");
                Timestamp banned_at = rs.getTimestamp("banned_at");
                Timestamp unbanned_at = rs.getTimestamp("unbanned_at");
                return new Ban(uuid, end_time, bannedBy, reason, active, banned_at, unbanned_at);
            }
        } catch (SQLException e) {
            Logger.logError("Erro ao carregar banIP ativo: " + e.getMessage());
        }

        return null;
    }
}
