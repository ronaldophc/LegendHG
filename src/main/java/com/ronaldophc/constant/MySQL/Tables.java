package com.ronaldophc.constant.MySQL;

import lombok.Getter;

public enum Tables {

    PLAYER("players"),
    GAME("games"),
    PLAYER_LOGIN("player_login");

    @Getter
    private final String tableName;

    Tables(String tableName) {
        this.tableName = tableName;
    }
}
