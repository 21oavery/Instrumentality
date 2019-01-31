package com.powerboat9.instrument;

import java.util.Arrays;

public class HashObject {
    private byte[] data;

    public HashObject(byte[] dataIn) throws IllegalArgumentException {
        if (dataIn.length != 32) throw new IllegalArgumentException("Hash must be 256 bits");
        data = new byte[32];
        System.arraycopy(dataIn, 0, data, 0, 32);
    }
    
    @Override
    public int hashCode() {
        return data[0] + (data[1] << 8) + (data[2] << 16) + (data[3] << 24);
    }
    
    @Override
    public boolean equals(Object o) {
        if (o instanceof HashObject) {
            return Arrays.equals(data, o.data);
        } else if (o instanceof byte[]) {
            return Arrays.equals(data, o);
        }
        return false;
    }
}
