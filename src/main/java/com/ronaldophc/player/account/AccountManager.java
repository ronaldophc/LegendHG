package com.ronaldophc.player.account;

import lombok.Getter;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@Getter
public class AccountManager {

    private final List<Account> accounts = new ArrayList<>();
    private static AccountManager instance = new AccountManager();

    private AccountManager() {
    }

    public Account getOrCreateAccount(Player player) {
        for (Account account : accounts) {
            if (account.getUUID().equals(player.getUniqueId())) {
                account.setPlayer(player);
                return account;
            }
        }
        Account newAccount = new Account(player);
        accounts.add(newAccount);
        return newAccount;
    }

    public Account getAccountByName(String name) {
        for (Account account : accounts) {
            if (account.getActualName().equalsIgnoreCase(name)) {
                return account;
            }
            if (account.getOriginalName().equalsIgnoreCase(name)) {
                return account;
            }
        }
        return null;
    }

    public List<Player> getPlayersAlive() {
        List<Player> playersAlive = new ArrayList<>();
        for (Account account : accounts) {
            if (account.isAlive()) {
                playersAlive.add(account.getPlayer());
            }
        }
        return playersAlive;
    }



    public static AccountManager getInstance() {
        if (instance == null) {
            instance = new AccountManager();
        }
        return instance;
    }
}
