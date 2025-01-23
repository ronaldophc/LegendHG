package com.ronaldophc.kits.manager;

import com.ronaldophc.LegendHG;
import com.ronaldophc.kits.Kit;

import java.util.HashSet;
import java.util.Set;

public class KitCombinationManager {

    private static final KitCombinationManager instance = new KitCombinationManager();
    private final Set<Set<Kit>> prohibitedCombinations = new HashSet<>();

    public KitCombinationManager() {
        Kit stomper = kit("Stomper");
        Kit phantom = kit("Phantom");
        addProhibitedCombination(stomper, phantom);
        Kit kangaroo = kit("Kangaroo");
        addProhibitedCombination(stomper, kangaroo);
        Kit ninja = kit("Ninja");
        addProhibitedCombination(stomper, ninja);
        Kit blink = kit("Blink");
        addProhibitedCombination(stomper, blink);
        Kit launcher = kit("Launcher");
        addProhibitedCombination(stomper, launcher);
        Kit gladiator = kit("Gladiator");
        Kit fisherman = kit("Fisherman");
        addProhibitedCombination(gladiator, fisherman);
    }

    private void addProhibitedCombination(Kit kit1, Kit kit2) {
        Set<Kit> combination = new HashSet<>();
        combination.add(kit1);
        combination.add(kit2);
        prohibitedCombinations.add(combination);
    }

    public boolean isProhibitedCombination(Kit kit1, Kit kit2) {
        Set<Kit> combination = new HashSet<>();
        combination.add(kit1);
        combination.add(kit2);
        return prohibitedCombinations.contains(combination);
    }

    private Kit kit(String name) {
        return LegendHG.getKitManager().searchKit(name);
    }

    public static KitCombinationManager getInstance() {
        return instance;
    }
}