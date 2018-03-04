package es.jaimemartin.crypto.rc4coder;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Base64;

import javax.crypto.IllegalBlockSizeException;

public class RC4DropCoder {
    private static final byte[] f25909b = new byte[1024];
    RC4 f25910a;

    static {
        Arrays.fill(f25909b, (byte) 0);
    }

    private static boolean m31685c(byte[] bArr) {
        return bArr == null || bArr.length == 0;
    }

    public RC4DropCoder(byte[] bArr) throws SecurityException {
        if (m31685c(bArr)) {
            throw new SecurityException("rc4 key is null");
        } else if (bArr.length != 32) {
            throw new IllegalArgumentException("rc4Key length is invalid");
        } else {
            this.f25910a = new RC4(bArr);
            m31687a(f25909b);
        }
    }

    public RC4DropCoder(String str) throws SecurityException {
        this(Base64.getDecoder().decode(str));
    }

    public byte[] m31687a(byte[] bArr) throws SecurityException {
        if (bArr == null) {
            try {
                throw new IllegalBlockSizeException("no block data");
            } catch (Throwable e) {
                throw new SecurityException(e);
            }
        }
        this.f25910a.m31684a(bArr);
        return bArr;
    }

    public String m31686a(String str) {
        try {
            return new String(m31687a(Base64.getDecoder().decode(str)), "UTF-8");
        } catch (Throwable e) {
            throw new SecurityException(e);
        }
    }

    public byte[] m31689b(byte[] bArr) throws SecurityException {
        if (bArr == null) {
            try {
                throw new IllegalBlockSizeException("no block data");
            } catch (Throwable e) {
                throw new SecurityException(e);
            }
        }
        this.f25910a.m31684a(bArr);
        return bArr;
    }

    public String m31688b(String str) {
        byte[] bArr = null;
        try {
            bArr = str.getBytes("UTF-8");
        } catch (UnsupportedEncodingException e) {
        }
        return String.valueOf(Base64.getEncoder().encode(bArr));
    }
}