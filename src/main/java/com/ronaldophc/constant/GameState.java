package com.ronaldophc.constant;

public enum GameState {
    COUNTDOWN(false, false, false, true, true, false),
    INVINCIBILITY(true, false, true, false, true, true),
    RUNNING(true, true, true, false, false, true),
    FINAL(false, true, false, false, false, false),
    FINISHED(true, true, true, false, false, false),
    RESTARTING(false, false, false, false, false, false);

    private final boolean canPlaceBlocks;
    private final boolean canBreakBlocks;
    private final boolean canTakeDamage;
    private final boolean canFreeUpdateKit;
    private final boolean canVipUpdateKit;
    private final boolean canUseKit;

    GameState(boolean canBreakBlocks, boolean canTakeDamage, boolean canPlaceBlocks, boolean canFreeUpdateKit, boolean canVipUpdateKit, boolean canUseKit) {
        this.canBreakBlocks = canBreakBlocks;
        this.canTakeDamage = canTakeDamage;
        this.canPlaceBlocks = canPlaceBlocks;
        this.canFreeUpdateKit = canFreeUpdateKit;
        this.canVipUpdateKit = canVipUpdateKit;
        this.canUseKit = canUseKit;
    }

    public boolean canBreakBlocks() {
        return canBreakBlocks;
    }

    public boolean canTakeDamage() {
        return canTakeDamage;
    }

    public boolean canPlaceBlocks() {
        return canPlaceBlocks;
    }

    public boolean canFreeUpdateKit() { return canFreeUpdateKit; }

    public boolean canVipUpdateKit() { return canVipUpdateKit; }

    public boolean canUseKit() { return canUseKit; }

    private static final GameState[] vals = values();

    public GameState next() {
        return vals[(this.ordinal() + 1) % vals.length];
    }

    public GameState previous() {
        return vals[(this.ordinal() - 1 + vals.length) % vals.length];
    }
}
