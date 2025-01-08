package com.ronaldophc.api.jnbt;

public final class ByteTag extends Tag {
    private final byte value;

    public ByteTag(String name, byte value) {
        super(name);
        this.value = value;
    }

    public Byte getValue() {
        return this.value;
    }

    public String toString() {
        String name = getName();
        String append = "";
        if (name != null && !name.isEmpty())
            append = "(\"" + getName() + "\")";
        return "TAG_Byte" + append + ": " + this.value;
    }
}

