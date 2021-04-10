package org.spongycastle.crypto;

import org.spongycastle.util.*;

public abstract class PBEParametersGenerator
{
    protected int iterationCount;
    protected byte[] password;
    protected byte[] salt;
    
    protected PBEParametersGenerator() {
    }
    
    public static byte[] PKCS12PasswordToBytes(final char[] array) {
        int i = 0;
        if (array != null && array.length > 0) {
            final byte[] array2 = new byte[(array.length + 1) * 2];
            while (i != array.length) {
                final int n = i * 2;
                array2[n] = (byte)(array[i] >>> 8);
                array2[n + 1] = (byte)array[i];
                ++i;
            }
            return array2;
        }
        return new byte[0];
    }
    
    public static byte[] PKCS5PasswordToBytes(final char[] array) {
        int i = 0;
        if (array != null) {
            final int length = array.length;
            final byte[] array2 = new byte[length];
            while (i != length) {
                array2[i] = (byte)array[i];
                ++i;
            }
            return array2;
        }
        return new byte[0];
    }
    
    public static byte[] PKCS5PasswordToUTF8Bytes(final char[] array) {
        if (array != null) {
            return Strings.toUTF8ByteArray(array);
        }
        return new byte[0];
    }
    
    public abstract CipherParameters generateDerivedMacParameters(final int p0);
    
    public abstract CipherParameters generateDerivedParameters(final int p0);
    
    public abstract CipherParameters generateDerivedParameters(final int p0, final int p1);
    
    public int getIterationCount() {
        return this.iterationCount;
    }
    
    public byte[] getPassword() {
        return this.password;
    }
    
    public byte[] getSalt() {
        return this.salt;
    }
    
    public void init(final byte[] password, final byte[] salt, final int iterationCount) {
        this.password = password;
        this.salt = salt;
        this.iterationCount = iterationCount;
    }
}
