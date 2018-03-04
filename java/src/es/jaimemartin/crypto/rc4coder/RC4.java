package es.jaimemartin.crypto.rc4coder;

final class RC4 {
    private int f25906a;
    private int f25907b;
    private final byte[] f25908c = new byte[256];

    public RC4(byte[] bArr) {
        int i;
        int length = bArr.length;
        for (i = 0; i < 256; i++) {
            this.f25908c[i] = (byte) i;
        }
        i = 0;
        for (int i2 = 0; i2 < 256; i2++) {
            i = ((i + bArr[i2 % length]) + this.f25908c[i2]) & 255;
            byte b = this.f25908c[i2];
            this.f25908c[i2] = this.f25908c[i];
            this.f25908c[i] = b;
        }
        this.f25906a = 0;
        this.f25907b = 0;
    }

    public byte m31683a() {
        this.f25906a = (this.f25906a + 1) & 255;
        this.f25907b = (this.f25907b + this.f25908c[this.f25906a]) & 255;
        byte b = this.f25908c[this.f25906a];
        this.f25908c[this.f25906a] = this.f25908c[this.f25907b];
        this.f25908c[this.f25907b] = b;
        return this.f25908c[(this.f25908c[this.f25906a] + this.f25908c[this.f25907b]) & 255];
    }

    public void m31684a(byte[] bArr) {
        for (int i = 0; i < bArr.length; i++) {
            bArr[i] = (byte) (bArr[i] ^ m31683a());
        }
    }
}