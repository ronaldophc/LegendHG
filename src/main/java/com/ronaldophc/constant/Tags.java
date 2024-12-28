package com.ronaldophc.constant;

import org.bukkit.ChatColor;

public enum Tags {

    NORMAL(ChatColor.WHITE, "legendhg.tag.normal"),
    VIP(ChatColor.GOLD, "legendhg.tag.vip"),
    ADMIN(ChatColor.RED, "legendhg.tag.admin"),
    TRIAL(ChatColor.GRAY, "legendhg.tag.trial"),
    OWNER(ChatColor.DARK_RED, "legendhg.tag.owner"),
    MOD(ChatColor.BLUE, "legendhg.tag.mod"),
    HELPER(ChatColor.GREEN, "legendhg.tag.helper"),
    YOUTUBER(ChatColor.LIGHT_PURPLE, "legendhg.tag.youtuber"),
    BUILDER(ChatColor.AQUA, "legendhg.tag.builder"),
    DEV(ChatColor.DARK_PURPLE, "legendhg.tag.dev"),
    TESTER(ChatColor.YELLOW, "legendhg.tag.tester"),
    BETA(ChatColor.DARK_GREEN, "legendhg.tag.beta");

    private final ChatColor color;
    private final String permission;

    Tags(ChatColor color, String permission) {
        this.color = color;
        this.permission = permission;
    }

    public ChatColor getColor() {
        return color;
    }

    public String getPermission() {
        return permission;
    }

}
