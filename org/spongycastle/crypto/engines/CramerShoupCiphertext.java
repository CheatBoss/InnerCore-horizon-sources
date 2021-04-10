package org.spongycastle.crypto.engines;

import java.math.*;
import org.spongycastle.util.*;

public class CramerShoupCiphertext
{
    BigInteger e;
    BigInteger u1;
    BigInteger u2;
    BigInteger v;
    
    public CramerShoupCiphertext() {
    }
    
    public CramerShoupCiphertext(final BigInteger u1, final BigInteger u2, final BigInteger e, final BigInteger v) {
        this.u1 = u1;
        this.u2 = u2;
        this.e = e;
        this.v = v;
    }
    
    public CramerShoupCiphertext(final byte[] array) {
        final int n = Pack.bigEndianToInt(array, 0) + 4;
        this.u1 = new BigInteger(Arrays.copyOfRange(array, 4, n));
        final int bigEndianToInt = Pack.bigEndianToInt(array, n);
        final int n2 = n + 4;
        final int n3 = bigEndianToInt + n2;
        this.u2 = new BigInteger(Arrays.copyOfRange(array, n2, n3));
        final int bigEndianToInt2 = Pack.bigEndianToInt(array, n3);
        final int n4 = n3 + 4;
        final int n5 = bigEndianToInt2 + n4;
        this.e = new BigInteger(Arrays.copyOfRange(array, n4, n5));
        final int bigEndianToInt3 = Pack.bigEndianToInt(array, n5);
        final int n6 = n5 + 4;
        this.v = new BigInteger(Arrays.copyOfRange(array, n6, bigEndianToInt3 + n6));
    }
    
    public BigInteger getE() {
        return this.e;
    }
    
    public BigInteger getU1() {
        return this.u1;
    }
    
    public BigInteger getU2() {
        return this.u2;
    }
    
    public BigInteger getV() {
        return this.v;
    }
    
    public void setE(final BigInteger e) {
        this.e = e;
    }
    
    public void setU1(final BigInteger u1) {
        this.u1 = u1;
    }
    
    public void setU2(final BigInteger u2) {
        this.u2 = u2;
    }
    
    public void setV(final BigInteger v) {
        this.v = v;
    }
    
    public byte[] toByteArray() {
        final byte[] byteArray = this.u1.toByteArray();
        final int length = byteArray.length;
        final byte[] byteArray2 = this.u2.toByteArray();
        final int length2 = byteArray2.length;
        final byte[] byteArray3 = this.e.toByteArray();
        final int length3 = byteArray3.length;
        final byte[] byteArray4 = this.v.toByteArray();
        final int length4 = byteArray4.length;
        final byte[] array = new byte[length + length2 + length3 + length4 + 16];
        Pack.intToBigEndian(length, array, 0);
        System.arraycopy(byteArray, 0, array, 4, length);
        final int n = length + 4;
        Pack.intToBigEndian(length2, array, n);
        final int n2 = n + 4;
        System.arraycopy(byteArray2, 0, array, n2, length2);
        final int n3 = n2 + length2;
        Pack.intToBigEndian(length3, array, n3);
        final int n4 = n3 + 4;
        System.arraycopy(byteArray3, 0, array, n4, length3);
        final int n5 = n4 + length3;
        Pack.intToBigEndian(length4, array, n5);
        System.arraycopy(byteArray4, 0, array, n5 + 4, length4);
        return array;
    }
    
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("u1: ");
        sb2.append(this.u1.toString());
        sb.append(sb2.toString());
        final StringBuilder sb3 = new StringBuilder();
        sb3.append("\nu2: ");
        sb3.append(this.u2.toString());
        sb.append(sb3.toString());
        final StringBuilder sb4 = new StringBuilder();
        sb4.append("\ne: ");
        sb4.append(this.e.toString());
        sb.append(sb4.toString());
        final StringBuilder sb5 = new StringBuilder();
        sb5.append("\nv: ");
        sb5.append(this.v.toString());
        sb.append(sb5.toString());
        return sb.toString();
    }
}
