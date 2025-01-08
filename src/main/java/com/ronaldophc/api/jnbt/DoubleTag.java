package com.ronaldophc.api.jnbt;

public final class DoubleTag extends Tag {
    private final double value;

    public DoubleTag(String name, double value) {
        super(name);
        this.value = value;
    }

    public Double getValue() {
        return this.value;
    }

    public String toString() {
        String name = getName();
        String append = "";
        if (name != null && !name.isEmpty())
            append = "(\"" + getName() + "\")";
        return "TAG_Double" + append + ": " + this.value;
    }
}

