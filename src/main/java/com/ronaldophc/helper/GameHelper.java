package com.ronaldophc.helper;

import com.ronaldophc.setting.Settings;

import java.util.Random;

public class GameHelper {

    private static final GameHelper instance = new GameHelper();
    private int kits;

    private GameHelper() {
        this.kits = Settings.getInstance().getInt("Kits");
        defineType();
    }

    private void defineType() {
        if (kits == 3) {
            kits = new Random().nextInt(2) + 1;
        }
    }

    public int getKits() {
        return kits;
    }

    public boolean isTwoKits() {
        return kits == 2;
    }

    public static GameHelper getInstance() {
        return instance;
    }

}
