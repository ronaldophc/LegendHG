package com.ronaldophc.feature.punish.banip;

import com.ronaldophc.database.BanRepository;
import com.ronaldophc.feature.punish.BanHelper;
import lombok.Getter;
import lombok.Setter;

import java.net.InetAddress;
import java.sql.Timestamp;

@Getter
@Setter
public class BanIP {

    private InetAddress ip_address;
    private long end_time;
    private String banned_by;
    private String reason;
    private boolean active;
    private Timestamp banned_at;
    private Timestamp unbanned_at;

    public BanIP(InetAddress ip_address, long end_time, String banned_by, String reason) {
        this.ip_address = ip_address;
        this.end_time = end_time;
        this.banned_by = banned_by;
        this.reason = reason;
    }

    public BanIP(InetAddress ip_address, long end_time, String banned_by, String reason, boolean active, Timestamp banned_at, Timestamp unbanned_at) {
        this.ip_address = ip_address;
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
        return BanHelper.formatTimeYear(banned_at.getTime());
    }

    public String getExpire_atFormated() {
        return BanHelper.formatTimeYear(end_time);
    }

    public String getBanIPTimeFormated(InetAddress ipAddress) {
        long duration = BanRepository.getBanIPTime(ipAddress.getHostAddress()) - System.currentTimeMillis();
        return BanHelper.formatTime(duration);
    }

    public String getDurationFormated() {
        return BanHelper.formatTime(end_time - banned_at.getTime());
    }
}
