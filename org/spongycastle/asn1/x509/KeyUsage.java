package org.spongycastle.asn1.x509;

import org.spongycastle.asn1.*;

public class KeyUsage extends ASN1Object
{
    public static final int cRLSign = 2;
    public static final int dataEncipherment = 16;
    public static final int decipherOnly = 32768;
    public static final int digitalSignature = 128;
    public static final int encipherOnly = 1;
    public static final int keyAgreement = 8;
    public static final int keyCertSign = 4;
    public static final int keyEncipherment = 32;
    public static final int nonRepudiation = 64;
    private DERBitString bitString;
    
    public KeyUsage(final int n) {
        this.bitString = new DERBitString(n);
    }
    
    private KeyUsage(final DERBitString bitString) {
        this.bitString = bitString;
    }
    
    public static KeyUsage fromExtensions(final Extensions extensions) {
        return getInstance(extensions.getExtensionParsedValue(Extension.keyUsage));
    }
    
    public static KeyUsage getInstance(final Object o) {
        if (o instanceof KeyUsage) {
            return (KeyUsage)o;
        }
        if (o != null) {
            return new KeyUsage(DERBitString.getInstance(o));
        }
        return null;
    }
    
    public byte[] getBytes() {
        return this.bitString.getBytes();
    }
    
    public int getPadBits() {
        return this.bitString.getPadBits();
    }
    
    public boolean hasUsages(final int n) {
        return (this.bitString.intValue() & n) == n;
    }
    
    @Override
    public ASN1Primitive toASN1Primitive() {
        return this.bitString;
    }
    
    @Override
    public String toString() {
        final byte[] bytes = this.bitString.getBytes();
        StringBuilder sb;
        int n;
        if (bytes.length == 1) {
            sb = new StringBuilder();
            sb.append("KeyUsage: 0x");
            n = (bytes[0] & 0xFF);
        }
        else {
            sb = new StringBuilder();
            sb.append("KeyUsage: 0x");
            n = ((bytes[0] & 0xFF) | (bytes[1] & 0xFF) << 8);
        }
        sb.append(Integer.toHexString(n));
        return sb.toString();
    }
}
