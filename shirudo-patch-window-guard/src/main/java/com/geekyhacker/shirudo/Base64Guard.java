package com.geekyhacker.shirudo;

import java.util.Base64;

public final class Base64Guard {

    private final int maxEncodedLength;

    public Base64Guard(int maxEncodedLength) {
        this.maxEncodedLength = maxEncodedLength;
    }

    public byte[] decode(String input) {
        if (input == null || input.length() > maxEncodedLength) {
            throw new IllegalArgumentException("Base64 input rejected: too large");
        }
        return Base64.getDecoder().decode(input);
    }
}
