package com.ronaldophc.database;

import com.ronaldophc.util.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class BanManager {

    public static boolean banIP(String ipAddress, long duration) {
        if (!MySQLManager.isActive) return false;
        try (Connection conn = MySQLManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO ip_bans (ip_address, end_time)" +
                     " VALUES (?, ?) ON DUPLICATE KEY UPDATE end_time = ?")) {

            stmt.setString(1, ipAddress);
            stmt.setLong(2, System.currentTimeMillis() + duration);
            stmt.setLong(3, System.currentTimeMillis() + duration);
            stmt.executeUpdate();

            return true;
        } catch (SQLException e) {
            Logger.logError(e.getMessage());
            return false;
        }
    }

    public static boolean unbanIP(String ipAddress) {
        if (!MySQLManager.isActive) return false;
        try (Connection conn = MySQLManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM ip_bans WHERE ip_address = ?")) {
            stmt.setString(1, ipAddress);
            stmt.executeUpdate();

            return true;
        } catch (SQLException e) {
            Logger.logError(e.getMessage());
            return false;
        }
    }

    public static boolean isIPBanned(String ipAddress) {
        if (!MySQLManager.isActive) return false;
        try (Connection conn = MySQLManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT end_time FROM ip_bans WHERE ip_address = ?")) {
            stmt.setString(1, ipAddress);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                long endTime = rs.getLong("end_time");
                return endTime > System.currentTimeMillis();
            }
        } catch (SQLException e) {
            Logger.logError(e.getMessage());
        }
        return false;
    }

    public static boolean banPlayer(UUID playerUUID, long duration, String bannedBy, String reason) {
        if (!MySQLManager.isActive) return false;
        try (Connection conn = MySQLManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO bans (uuid, end_time, banned_by, reason, banned_at)" +
                     " VALUES (?, ?, ?, ?, CURRENT_TIMESTAMP) ON DUPLICATE KEY UPDATE end_time = ?, banned_by = ?, reason = ?, banned_at = CURRENT_TIMESTAMP")) {

            stmt.setString(1, playerUUID.toString());
            stmt.setLong(2, System.currentTimeMillis() + duration);
            stmt.setString(3, bannedBy);
            stmt.setString(4, reason);
            stmt.setLong(5, System.currentTimeMillis() + duration);
            stmt.setString(6, bannedBy);
            stmt.setString(7, reason);
            stmt.executeUpdate();

            return true;
        } catch (SQLException e) {
            Logger.logError(e.getMessage());
            return false;
        }
    }

    public static boolean unbanPlayer(UUID playerUUID) {
        if (!MySQLManager.isActive) return false;
        try (Connection conn = MySQLManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("DELETE FROM bans WHERE uuid = ?")) {
            stmt.setString(1, playerUUID.toString());
            stmt.executeUpdate();

            return true;
        } catch (SQLException e) {
            Logger.logError(e.getMessage());
            return false;
        }
    }

    public static boolean isBanned(UUID playerUUID) {
        if (!MySQLManager.isActive) return false;
        try (Connection conn = MySQLManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT end_time FROM bans WHERE uuid = ?")) {
            stmt.setString(1, playerUUID.toString());
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                long endTime = rs.getLong("end_time");
                return endTime > System.currentTimeMillis();
            }
        } catch (SQLException e) {
            Logger.logError(e.getMessage());
        }
        return false;
    }

}
