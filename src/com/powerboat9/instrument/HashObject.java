package com.powerboat9.instrument;

import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.file.Path;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class HashObject {
    private byte[] data;

    public HashObject(byte[] dataIn) throws IllegalArgumentException {
        if (dataIn.length != 32) throw new IllegalArgumentException("Hash must be 256 bits");
        data = new byte[32];
        System.arraycopy(dataIn, 0, data, 0, 32);
    }

    public static HashObject hash(Path f) throws IOException {
        MessageDigest hash;
        try {
            hash = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        FileReader r = new FileReader(f.toFile());
        byte[] d = new byte[2048];
        int v;
        boolean ok = true;
        while (ok) {
            int i = 0;
            while (i < 2048) {
                if ((v = r.read()) == -1) {
                    ok = false;
                    i++;
                    break;
                }
                d[i] = (byte) v;
                i++;
            }
            hash.update(d, 0, i);
        }
        return new HashObject(hash.digest());
    }
    
    @Override
    public int hashCode() {
        return data[0] + (data[1] << 8) + (data[2] << 16) + (data[3] << 24);
    }
    
    @Override
    public boolean equals(Object o) {
        if (o instanceof HashObject) {
            return Arrays.equals(data, ((HashObject) o).data);
        } else if (o instanceof byte[]) {
            return Arrays.equals(data, ((byte[]) o));
        }
        return false;
    }

    @Override
    public String toString() {
        return String.format("%032x", new BigInteger(1, data));
    }
}
