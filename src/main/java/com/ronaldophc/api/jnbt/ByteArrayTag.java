package com.ronaldophc.api.jnbt;

public final class ByteArrayTag extends Tag {
    private final byte[] value;

    public ByteArrayTag(String name, byte[] value) {
        super(name);
        this.value = value;
    }

    public byte[] getValue() {
        return this.value;
    }

    public String toString() {
        StringBuilder hex = new StringBuilder();
        byte b;
        int i;
        byte[] arrayOfByte;
        for (i = (arrayOfByte = this.value).length, b = 0; b < i; ) {
            byte b1 = arrayOfByte[b];
            String hexDigits = Integer.toHexString(b1).toUpperCase();
            if (hexDigits.length() == 1)
                hex.append("0");
            hex.append(hexDigits).append(" ");
            b++;
        }
        String name = getName();
        String append = "";
        if (name != null && !name.isEmpty())
            append = "(\"" + getName() + "\")";
        return "TAG_Byte_Array" + append + ": " + hex.toString();
    }
}
