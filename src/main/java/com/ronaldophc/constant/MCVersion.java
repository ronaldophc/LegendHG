package com.ronaldophc.constant;

import lombok.Getter;

@Getter
public enum MCVersion {

    MC_1_7(5),
    MC_1_8(47);

    private final int protocolVersion;

    MCVersion(int protocolVersion) {
        this.protocolVersion = protocolVersion;
    }
}
