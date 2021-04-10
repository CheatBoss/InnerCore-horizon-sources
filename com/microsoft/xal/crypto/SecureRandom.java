package com.microsoft.xal.crypto;

public class SecureRandom
{
    public static byte[] GenerateRandomBytes(final int n) {
        final java.security.SecureRandom secureRandom = new java.security.SecureRandom();
        final byte[] array = new byte[n];
        secureRandom.nextBytes(array);
        return array;
    }
}
