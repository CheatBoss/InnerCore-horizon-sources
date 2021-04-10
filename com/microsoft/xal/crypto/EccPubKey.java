package com.microsoft.xal.crypto;

import java.security.interfaces.*;
import java.math.*;
import android.util.*;

public class EccPubKey
{
    private final ECPublicKey publicKey;
    
    EccPubKey(final ECPublicKey publicKey) {
        this.publicKey = publicKey;
    }
    
    private String getBase64Coordinate(final BigInteger bigInteger) {
        final byte[] byteArray = bigInteger.toByteArray();
        final int length = byteArray.length;
        final int n = 0;
        int n2;
        byte[] array;
        if (length > 32) {
            n2 = byteArray.length - 32;
            array = byteArray;
        }
        else {
            n2 = n;
            array = byteArray;
            if (byteArray.length < 32) {
                array = new byte[32];
                System.arraycopy(byteArray, 0, array, 32 - byteArray.length, byteArray.length);
                n2 = n;
            }
        }
        return Base64.encodeToString(array, n2, 32, 11);
    }
    
    public String getBase64UrlX() {
        return this.getBase64Coordinate(this.getX());
    }
    
    public String getBase64UrlY() {
        return this.getBase64Coordinate(this.getY());
    }
    
    public BigInteger getX() {
        return this.publicKey.getW().getAffineX();
    }
    
    public BigInteger getY() {
        return this.publicKey.getW().getAffineY();
    }
}
