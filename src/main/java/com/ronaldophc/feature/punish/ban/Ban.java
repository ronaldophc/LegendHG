package com.ronaldophc.feature.punish.ban;

import com.ronaldophc.feature.punish.PunishHelper;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
public class Ban {

    private UUID uuid;
    private long end_time;
    private String banned_by;
    private String reason;
    private boolean active;
    private Timestamp banned_at;
    private Timestamp unbanned_at;

    public Ban(UUID uuid, long end_time, String banned_by, String reason) {
        this.uuid = uuid;
        this.end_time = end_time;
        this.banned_by = banned_by;
        this.reason = reason;
    }

    public Ban(UUID uuid, long end_time, String banned_by, String reason, boolean active, Timestamp banned_at, Timestamp unbanned_at) {
        this.uuid = uuid;
        this.end_time = end_time;
        this.banned_by = banned_by;
        this.reason = reason;
        this.active = active;
        this.banned_at = banned_at;
        this.unbanned_at = unbanned_at;
    }

    public boolean isExpired() {
        return end_time < System.currentTimeMillis();
    }

    public String getBanned_atFormated() {
        return PunishHelper.formatTimeYear(banned_at.getTime());
    }

    public String getExpire_atFormated() {
        return PunishHelper.formatTimeYear(end_time);
    }

    public String getBanTimeLeftFormated(UUID uuid) {
        long duration = end_time - System.currentTimeMillis();
        return PunishHelper.formatTime(duration);
    }

    public String getDurationFormated() {
        return PunishHelper.formatTime(end_time - banned_at.getTime());
    }
}
