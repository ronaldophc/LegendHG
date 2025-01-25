package com.ronaldophc.database;

import com.ronaldophc.feature.punish.mute.Mute;
import com.ronaldophc.util.Logger;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MuteRepository {

    //    "id INT PRIMARY KEY AUTO_INCREMENT, " +
//            "uuid VARCHAR(36), " +
//            "end_time BIGINT NOT NULL, " +
//            "muted_by VARCHAR(36) NOT NULL, " +
//            "reason TEXT NOT NULL, " +
//            "active BOOLEAN DEFAULT FALSE, " +
//            "muted_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
//            "unmuted_at TIMESTAMP)";

    public static boolean mute(String uuid, long duration, String mutedBy, String reason) {
        if (!MySQLManager.isActive) return false;
        try (Connection conn = MySQLManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO mutes (uuid, end_time, muted_by, reason)" +
                     " VALUES (?, ?, ?, ?)")) {

            stmt.setString(1, uuid);
            stmt.setLong(2, duration);
            stmt.setString(3, mutedBy);
            stmt.setString(4, reason);
            stmt.executeUpdate();

            return true;
        } catch (SQLException e) {
            Logger.logError("Erro ao mutar: " + e.getMessage());
            return false;
        }
    }

    public static boolean unMute(String uuid) {
        if (!MySQLManager.isActive) return false;
        try (Connection conn = MySQLManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("UPDATE mutes SET active = false, unmuted_at = CURRENT_TIMESTAMP WHERE uuid = ? AND active = true")) {

            stmt.setString(1, uuid);
            stmt.executeUpdate();

            return true;
        } catch (SQLException e) {
            Logger.logError("Erro ao desmutar: " + e.getMessage());
            return false;
        }
    }

    public static long getMuteEndTime(String uuid) {
        if (!MySQLManager.isActive) return 0;
        try (Connection conn = MySQLManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT end_time FROM mutes WHERE uuid = ? AND active = true")) {
            stmt.setString(1, uuid);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                return rs.getLong("end_time");
            }
        } catch (SQLException e) {
            Logger.logError("Erro ao requisitar end_time do Mute: " + e.getMessage());
        }
        return 0;
    }

    public static boolean isMuted(String uuid) {
        if (!MySQLManager.isActive) return false;
        try (Connection conn = MySQLManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT end_time FROM mutes WHERE uuid = ? AND active = true")) {
            stmt.setString(1, uuid);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                long time = rs.getLong("end_time");
                if (time < System.currentTimeMillis()) {
                    unMute(uuid);
                    return false;
                }
                return true;
            }
        } catch (SQLException e) {
            Logger.logError("Erro ao verificar Mute: " + e.getMessage());
        }
        return false;
    }

    public static void unMuteExpired() {
        if (!MySQLManager.isActive) return;
        String query = "UPDATE mutes SET active = false, unmuted_at = CURRENT_TIMESTAMP WHERE end_time <= ? AND active = true";

        try (Connection conn = MySQLManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setLong(1, System.currentTimeMillis());
            stmt.executeUpdate();
        } catch (SQLException e) {
            Logger.logError("Erro ao desmutar Mutes expirados: " + e.getMessage());
        }
    }

    // Pegar histórico de banimentos
    public static List<Mute> getMuteHistory(String uuid) {
        if (!MySQLManager.isActive) return new ArrayList<>();
        List<Mute> mutes = new ArrayList<>();
        try (Connection conn = MySQLManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM mutes WHERE uuid = ? ORDER BY muted_at DESC")) {
            stmt.setString(1, uuid);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                String mutedBy = rs.getString("muted_by");
                String reason = rs.getString("reason");
                long end_time = rs.getLong("end_time");
                boolean active = rs.getBoolean("active");
                Timestamp muted_at = rs.getTimestamp("muted_at");
                Timestamp unmuted_at = rs.getTimestamp("unmuted_at");
                Mute newMute = new Mute(UUID.fromString(uuid), end_time, mutedBy, reason, active, muted_at, unmuted_at);
                mutes.add(newMute);
            }
        } catch (SQLException e) {
            Logger.logError("Erro ao carregar histórico de Mutes: " + e.getMessage());
        }
        return mutes;
    }

    public static Mute getActiveMute(String uuid) {
        if (!MySQLManager.isActive) return null;
        try (Connection conn = MySQLManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM mutes WHERE uuid = ? AND active = true")) {
            stmt.setString(1, uuid);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                String mutedBy = rs.getString("muted_by");
                String reason = rs.getString("reason");
                long end_time = rs.getLong("end_time");
                boolean active = rs.getBoolean("active");
                Timestamp muted_at = rs.getTimestamp("muted_at");
                Timestamp unmuted_at = rs.getTimestamp("unmuted_at");
                return new Mute(UUID.fromString(uuid), end_time, mutedBy, reason, active, muted_at, unmuted_at);
            }
        } catch (SQLException e) {
            Logger.logError("Erro ao carregar Mute ativo: " + e.getMessage());
        }
        return null;
    }
}
