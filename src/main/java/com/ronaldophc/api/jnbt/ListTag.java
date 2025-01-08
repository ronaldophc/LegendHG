package com.ronaldophc.api.jnbt;

import java.util.Collections;
import java.util.List;

public final class ListTag extends Tag {
    private final Class<? extends Tag> type;

    private final List<Tag> value;

    public ListTag(String name, Class<? extends Tag> type, List<Tag> value) {
        super(name);
        this.type = type;
        this.value = Collections.unmodifiableList(value);
    }

    public Class<? extends Tag> getType() {
        return this.type;
    }

    public List<Tag> getValue() {
        return this.value;
    }

    public String toString() {
        String name = getName();
        String append = "";
        if (name != null && !name.isEmpty())
            append = "(\"" + getName() + "\")";
        StringBuilder bldr = new StringBuilder();
        bldr.append("TAG_List").append(append).append(": ").append(this.value.size()).append(" entries of type ").append(NBTUtils.getTypeName(this.type)).append("\r\n{\r\n");
        for (Tag t : this.value)
            bldr.append("   ").append(t.toString().replaceAll("\r\n", "\r\n   ")).append("\r\n");
        bldr.append("}");
        return bldr.toString();
    }
}

