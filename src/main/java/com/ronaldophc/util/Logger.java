package com.ronaldophc.util;

import com.ronaldophc.LegendHG;
import com.ronaldophc.yaml.Yaml;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {

    private static final String LOG_FILE_ERROR = "error.log";
    private static final String LOG_FILE_DEBUG = "debug.log";
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    public static void logError(String message) {
        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        StackTraceElement element = stackTrace[2]; // Pega o elemento do stack trace que chamou este método
        String fileName = element.getFileName();
        int lineNumber = element.getLineNumber();

        try (FileWriter fw = new FileWriter(LOG_FILE_ERROR, true);
             PrintWriter pw = new PrintWriter(fw)) {
            String timestamp = dtf.format(LocalDateTime.now());
            pw.println(timestamp + " - ERROR: " + message + " (File: " + fileName + ", Line: " + lineNumber + ")");
            LegendHG.logger.severe("An error occurred, check the error log.");
            for (Player player : Bukkit.getOnlinePlayers()) {
                if (player.isOp()) {
                    player.sendMessage(Util.errorServer + "Ocorreu um erro, verifique o log error.");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void debug(String message) {
        try (FileWriter fw = new FileWriter(LOG_FILE_DEBUG, true);
             PrintWriter pw = new PrintWriter(fw)) {
            String timestamp = dtf.format(LocalDateTime.now());
            pw.println(timestamp + " - DEBUG: " + message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void debugMySql(String message) {
        Yaml debug = LegendHG.debug;
        if (debug.getString("MySQL").equalsIgnoreCase("off")) return;
        try (FileWriter fw = new FileWriter(LOG_FILE_DEBUG, true);
             PrintWriter pw = new PrintWriter(fw)) {
            String timestamp = dtf.format(LocalDateTime.now());
            pw.println(timestamp + " - DEBUG: " + message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
