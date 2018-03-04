package es.jaimemartin.crypto.rc4coder;

import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

public abstract class Coder {
    public static byte[] m31681a(String str) {
    	return java.util.Base64.getDecoder().decode(str);
    }

    public static String m31680a(byte[] bArr) {
    	return Base64.getEncoder().encodeToString(bArr);
    }

    public static byte[] m31682b(byte[] bArr) throws NoSuchAlgorithmException, InvalidKeyException {
        MessageDigest instance = MessageDigest.getInstance("SHA-256");
        instance.update(bArr);
        return instance.digest();
    }
}