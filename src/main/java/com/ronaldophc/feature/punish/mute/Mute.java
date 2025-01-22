package com.ronaldophc.feature.punish.mute;

import com.ronaldophc.database.MuteRepository;
import com.ronaldophc.feature.punish.PunishHelper;
import lombok.Getter;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.UUID;

@Getter
@Setter
public class Mute {

    private UUID uuid;
    private long end_time;
    private String muted_by;
    private String reason;
    private boolean active;
    private Timestamp muted_at;
    private Timestamp unmuted_at;

    public Mute(UUID uuid, long end_time, String muted_by, String reason) {
        this.uuid = uuid;
        this.end_time = end_time;
        this.muted_by = muted_by;
        this.reason = reason;
    }

    public Mute(UUID uuid, long end_time, String muted_by, String reason, boolean active, Timestamp muted_at, Timestamp unmuted_at) {
        this.uuid = uuid;
        this.end_time = end_time;
        this.muted_by = muted_by;
        this.reason = reason;
        this.active = active;
        this.muted_at = muted_at;
        this.unmuted_at = unmuted_at;
    }

    public boolean isExpired() {
        return end_time < System.currentTimeMillis();
    }

    public String getMuted_atFormated() {
        return PunishHelper.formatTimeYear(muted_at.getTime());
    }

    public String getExpire_atFormated() {
        return PunishHelper.formatTimeYear(end_time);
    }

    public String getMuteTimeLeftFormated(UUID uuid) {
        long duration = MuteRepository.getMuteEndTime(uuid.toString()) - System.currentTimeMillis();
        return PunishHelper.formatTime(duration);
    }

    public String getDurationFormated() {
        return PunishHelper.formatTime(end_time - muted_at.getTime());
    }
}
