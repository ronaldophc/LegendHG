package com.ronaldophc.kits.manager;

import com.ronaldophc.constant.Kits;

import java.util.HashSet;
import java.util.Set;

public class KitCombinationManager {

    private static final KitCombinationManager instance = new KitCombinationManager();
    private final Set<Set<Kits>> prohibitedCombinations = new HashSet<>();

    public KitCombinationManager() {
        addProhibitedCombination(Kits.STOMPER, Kits.PHANTOM);
        addProhibitedCombination(Kits.STOMPER, Kits.KANGAROO);
        addProhibitedCombination(Kits.STOMPER, Kits.NINJA);
        addProhibitedCombination(Kits.STOMPER, Kits.BLINK);
        addProhibitedCombination(Kits.STOMPER, Kits.LAUNCHER);
        addProhibitedCombination(Kits.GLADIATOR, Kits.FISHERMAN);
    }

    private void addProhibitedCombination(Kits kit1, Kits kit2) {
        Set<Kits> combination = new HashSet<>();
        combination.add(kit1);
        combination.add(kit2);
        prohibitedCombinations.add(combination);
    }

    public boolean isProhibitedCombination(Kits kit1, Kits kit2) {
        Set<Kits> combination = new HashSet<>();
        combination.add(kit1);
        combination.add(kit2);
        return prohibitedCombinations.contains(combination);
    }

    public static KitCombinationManager getInstance() {
        return instance;
    }
}