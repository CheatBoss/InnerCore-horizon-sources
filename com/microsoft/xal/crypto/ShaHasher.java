package com.microsoft.xal.crypto;

import java.security.*;

public class ShaHasher
{
    private MessageDigest md;
    
    public ShaHasher() throws NoSuchAlgorithmException {
        this.md = MessageDigest.getInstance("SHA-256");
    }
    
    public void AddBytes(final byte[] array) {
        this.md.update(array);
    }
    
    byte[] SignHash() {
        return this.md.digest();
    }
}
