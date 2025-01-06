package com.ronaldophc.register;

import com.ronaldophc.LegendHG;
import com.ronaldophc.command.*;
import com.ronaldophc.feature.report.*;
import com.viaversion.viaversion.libs.kyori.adventure.sound.Sound;

public class RegisterCommands extends LegendHG {

    public static void registerCommands() {
        LegendHG instance = getInstance();
        instance.getCommand("test").setExecutor(new TestCommand());
        instance.getCommand("gm").setExecutor(new GameModeCommand());
        instance.getCommand("countdown").setExecutor(new CountdownCommand());
        instance.getCommand("invincibility").setExecutor(new InvincibilityCommand());
        instance.getCommand("invsee").setExecutor(new InvSeeCommand());
        instance.getCommand("kick").setExecutor(new KickCommand());
        instance.getCommand("ping").setExecutor(new PingCommand());
        instance.getCommand("report").setExecutor(new ReportCommand());
        instance.getCommand("reporthack").setExecutor(new ReportHack());
        instance.getCommand("reportchat").setExecutor(new ReportChat());
        instance.getCommand("reportfreekill").setExecutor(new ReportFreekill());
        instance.getCommand("reportbugs").setExecutor(new ReportBugs());
        instance.getCommand("reportother").setExecutor(new ReportOther());
        instance.getCommand("reports").setExecutor(new ReportMenus());
        instance.getCommand("kit").setExecutor(new KitCommand());
        instance.getCommand("kit2").setExecutor(new Kit2Command());
        instance.getCommand("flyspeed").setExecutor(new FlySpeedCommand());
        instance.getCommand("pull").setExecutor(new PullCommand());
        instance.getCommand("lfeast").setExecutor(new FeastCommand());
        instance.getCommand("feast").setExecutor(new FeastCommand());
        instance.getCommand("minifeast").setExecutor(new MiniFeastCommand());
        instance.getCommand("register").setExecutor(new RegisterCommand());
        instance.getCommand("login").setExecutor(new LoginCommand());
        instance.getCommand("playerson").setExecutor(new PlayersOnCommand());
        instance.getCommand("scoreboard").setExecutor(new ScoreBoardCommand());
        instance.getCommand("skin").setExecutor(new SkinCommand());
        instance.getCommand("crash").setExecutor(new CrashCommand());
        instance.getCommand("fake").setExecutor(new FakeCommand());
        instance.getCommand("tag").setExecutor(new TagCommand());
        instance.getCommand("ip").setExecutor(new IpCommand());
        instance.getCommand("prefs").setExecutor(new PrefsCommand());
        instance.getCommand("staffchat").setExecutor(new StaffChatCommand());
        instance.getCommand("tell").setExecutor(new TellCommand());
        instance.getCommand("teleport").setExecutor(new TeleportCommand());
        instance.getCommand("stats").setExecutor(new StatsCommand());
        instance.getCommand("profile").setExecutor(new ProfileCommand());
        instance.getCommand("kills").setExecutor(new KillsCommand());
        instance.getCommand("sound").setExecutor(new SoundCommand());
    }

}