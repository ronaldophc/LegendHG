package com.ronaldophc.constant.MySQL;

import lombok.Getter;

public enum PlayerField {

    UUID("uuid"),
    NAME("name"),
    PASSWORD("password"),
    KILLS("kills"),
    DEATHS("deaths"),
    WINS("wins"),
    SCOREBOARD("scoreboard"),
    TAG("tag"),
    CHAT("chat"),
    TELL("tell"),
    IP("ip_address"),
    COOLDOWN_TYPE("cooldown_type");

    @Getter
    private final String fieldName;

    PlayerField(String fieldName) {
        this.fieldName = fieldName;
    }
}
