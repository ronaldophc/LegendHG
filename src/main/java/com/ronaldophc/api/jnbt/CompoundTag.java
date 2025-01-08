package com.ronaldophc.api.jnbt;

import lombok.Getter;

import java.util.Collections;
import java.util.Map;

@Getter
public final class CompoundTag extends Tag {
    private final Map<String, Tag> value;

    public CompoundTag(String name, Map<String, Tag> value) {
        super(name);
        this.value = Collections.unmodifiableMap(value);
    }

    public String toString() {
        String name = getName();
        String append = "";
        if (name != null && !name.isEmpty())
            append = "(\"" + getName() + "\")";
        StringBuilder bldr = new StringBuilder();
        bldr.append("TAG_Compound").append(append).append(": ").append(this.value.size()).append(" entries\r\n{\r\n");
        for (Map.Entry<String, Tag> entry : this.value.entrySet())
            bldr.append("   ").append(((Tag) entry.getValue()).toString().replaceAll("\r\n", "\r\n   ")).append("\r\n");
        bldr.append("}");
        return bldr.toString();
    }
}
