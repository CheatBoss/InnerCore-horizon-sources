package net.lingala.zip4j.crypto.PBKDF2;

import net.lingala.zip4j.util.*;

public class PBKDF2Engine
{
    protected PBKDF2Parameters parameters;
    protected PRF prf;
    
    public PBKDF2Engine() {
        this.parameters = null;
        this.prf = null;
    }
    
    public PBKDF2Engine(final PBKDF2Parameters parameters) {
        this.parameters = parameters;
        this.prf = null;
    }
    
    public PBKDF2Engine(final PBKDF2Parameters parameters, final PRF prf) {
        this.parameters = parameters;
        this.prf = prf;
    }
    
    protected void INT(final byte[] array, final int n, final int n2) {
        array[n + 0] = (byte)(n2 / 16777216);
        array[n + 1] = (byte)(n2 / 65536);
        array[n + 2] = (byte)(n2 / 256);
        array[n + 3] = (byte)n2;
    }
    
    protected byte[] PBKDF2(final PRF prf, byte[] array, final int n, final int n2) {
        if (array == null) {
            array = new byte[0];
        }
        final int hLen = prf.getHLen();
        final int ceil = this.ceil(n2, hLen);
        final byte[] array2 = new byte[ceil * hLen];
        int i = 1;
        int n3 = 0;
        while (i <= ceil) {
            this._F(array2, n3, prf, array, n, i);
            n3 += hLen;
            ++i;
        }
        if (n2 - (ceil - 1) * hLen < hLen) {
            final byte[] array3 = new byte[n2];
            System.arraycopy(array2, 0, array3, 0, n2);
            return array3;
        }
        return array2;
    }
    
    protected void _F(final byte[] array, final int n, final PRF prf, byte[] doFinal, final int n2, int i) {
        final int hLen = prf.getHLen();
        final byte[] array2 = new byte[hLen];
        final byte[] array3 = new byte[doFinal.length + 4];
        System.arraycopy(doFinal, 0, array3, 0, doFinal.length);
        this.INT(array3, doFinal.length, i);
        doFinal = array3;
        for (i = 0; i < n2; ++i) {
            doFinal = prf.doFinal(doFinal);
            this.xor(array2, doFinal);
        }
        System.arraycopy(array2, 0, array, n, hLen);
    }
    
    protected void assertPRF(final byte[] array) {
        if (this.prf == null) {
            this.prf = new MacBasedPRF(this.parameters.getHashAlgorithm());
        }
        this.prf.init(array);
    }
    
    protected int ceil(final int n, final int n2) {
        int n3 = 0;
        if (n % n2 > 0) {
            n3 = 1;
        }
        return n / n2 + n3;
    }
    
    public byte[] deriveKey(final char[] array) {
        return this.deriveKey(array, 0);
    }
    
    public byte[] deriveKey(final char[] array, final int n) {
        if (array == null) {
            throw new NullPointerException();
        }
        this.assertPRF(Raw.convertCharArrayToByteArray(array));
        int hLen;
        if ((hLen = n) == 0) {
            hLen = this.prf.getHLen();
        }
        return this.PBKDF2(this.prf, this.parameters.getSalt(), this.parameters.getIterationCount(), hLen);
    }
    
    public PBKDF2Parameters getParameters() {
        return this.parameters;
    }
    
    public PRF getPseudoRandomFunction() {
        return this.prf;
    }
    
    public void setParameters(final PBKDF2Parameters parameters) {
        this.parameters = parameters;
    }
    
    public void setPseudoRandomFunction(final PRF prf) {
        this.prf = prf;
    }
    
    public boolean verifyKey(final char[] array) {
        final byte[] derivedKey = this.getParameters().getDerivedKey();
        if (derivedKey == null) {
            return false;
        }
        if (derivedKey.length == 0) {
            return false;
        }
        final byte[] deriveKey = this.deriveKey(array, derivedKey.length);
        if (deriveKey == null) {
            return false;
        }
        if (deriveKey.length != derivedKey.length) {
            return false;
        }
        for (int i = 0; i < deriveKey.length; ++i) {
            if (deriveKey[i] != derivedKey[i]) {
                return false;
            }
        }
        return true;
    }
    
    protected void xor(final byte[] array, final byte[] array2) {
        for (int i = 0; i < array.length; ++i) {
            array[i] ^= array2[i];
        }
    }
}
