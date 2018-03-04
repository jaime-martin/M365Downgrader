package es.jaimemartin.utils;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import es.jaimemartin.crypto.rc4coder.Coder;
import es.jaimemartin.crypto.rc4coder.RC4DropCoder;

public class Descifra {
	String sid = "";
    String cUserId = "";
    String serviceToken = "";
    String ssecurity = "";
    long timeDiff = 200;
    
    public Descifra(String serviceToken, String ssecurity){
    	super();
    	this.serviceToken = serviceToken;
    	this.ssecurity = ssecurity;
    }
    
    public Descifra(String sid, String cUserId, String serviceToken, String ssecurity, long timeDiff, String domain) {
		super();
		this.serviceToken = serviceToken;
		this.ssecurity = ssecurity;
	}
	
	public String m22352a(String str, String str2) throws SecurityException {
        String str3 = null;
        try {
            String a = Coder.m31680a(Coder.m31682b(m22337a(Coder.m31681a(this.serviceToken), Coder.m31681a(str2))));
            if (a != null) {
                try {
                    str3 = new RC4DropCoder(a).m31686a(str);
                } catch (Exception e) {
                }
            }
        } catch (NoSuchAlgorithmException e2) {
        	e2.printStackTrace();
        } catch (InvalidKeyException e3) {
        	e3.printStackTrace();
        } catch (Exception e4) {
        	e4.printStackTrace();
        }
        return str3;
    }
	
	private byte[] m22337a(byte[] bArr, byte[] bArr2) {
        byte[] obj = new byte[(bArr.length + bArr2.length)];
        System.arraycopy(bArr, 0, obj, 0, bArr.length);
        System.arraycopy(bArr2, 0, obj, bArr.length, bArr2.length);
        return obj;
    }
	
	/****************************************************************/
	
	public String cifra(String str, String str2) throws SecurityException {
        byte[] hola = null;
        try {
            String a = Coder.m31680a(Coder.m31682b(m22337a(Coder.m31681a(this.ssecurity), Coder.m31681a(str2))));
            if (a != null) {
                try {
                    hola = new RC4DropCoder(a).m31689b(str.getBytes());
                } catch (Exception e) {
                }
            }
        } catch (NoSuchAlgorithmException e2) {
        	e2.printStackTrace();
        } catch (InvalidKeyException e3) {
        	e3.printStackTrace();
        } catch (Exception e4) {
        	e4.printStackTrace();
        }
        return Base64.getEncoder().encodeToString(hola);
    }	
}
