package com.ronaldophc.kits;

import com.google.common.collect.Lists;
import com.ronaldophc.LegendHG;
import com.ronaldophc.kits.manager.KitManager;

import java.util.List;

public class Kits {

    private Kit primary;
    private Kit secondary;

    public void setPrimary(Kit primary) {
        this.primary = primary;
    }

    public void setSecondary(Kit secondary) {
        this.secondary = secondary;
    }

    public Kit getPrimary() {
        if (primary == null) {
            return LegendHG.getKitManager().searchKit("Nenhum");
        }
        return primary;
    }

    public Kit getSecondary() {
        if (secondary == null) {
            return LegendHG.getKitManager().searchKit("Nenhum");
        }
        return secondary;
    }

    public boolean contains(Kit kit) {
        if (hasPrimary() && this.primary.equals(kit)) {
            return true;
        }
        return hasSecondary() && this.secondary.equals(kit);
    }

    public boolean hasPrimary() {
        return this.primary != null && this.primary != LegendHG.getKitManager().searchKit("Nenhum");
    }

    public boolean hasSecondary() {
        return this.secondary != null && this.secondary != LegendHG.getKitManager().searchKit("Nenhum");
    }

    public boolean hasBoth() {
        return hasPrimary() && hasSecondary();
    }

    public int getSize() {
        int i = 0;
        if (hasPrimary()) {
            ++i;
        }
        if (hasSecondary()) {
            ++i;
        }
        return i;
    }

    public boolean contains(String name) {
        if (hasPrimary() && this.primary.getName().equalsIgnoreCase(name))
            return true;
        return hasSecondary() && this.secondary.getName().equalsIgnoreCase(name);
    }

    public List<Kit> asList() {
        List<Kit> list = Lists.newLinkedList();
        if (hasPrimary())
            list.add(primary);
        if (hasSecondary())
            list.add(secondary);
        return list;
    }
}
