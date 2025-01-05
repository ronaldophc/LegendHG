package com.ronaldophc.helper;

import com.ronaldophc.setting.Settings;
import lombok.Getter;

import java.util.Random;

public class GameHelper {

    @Getter
    private static final GameHelper instance = new GameHelper();
    @Getter
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

    public boolean isTwoKits() {
        return kits == 2;
    }

}
