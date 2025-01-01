package com.ronaldophc.player.account;

import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class AccountManager {

    private static final List<Account> accounts = new ArrayList<>();

    public static Account getOrCreateAccount(Player player) {
        for (Account account : accounts) {
            if (account.getUUID().equals(player.getUniqueId())) {
                return account;
            }
        }
        Account newAccount = new Account(player);
        accounts.add(newAccount);
        return newAccount;
    }
}
