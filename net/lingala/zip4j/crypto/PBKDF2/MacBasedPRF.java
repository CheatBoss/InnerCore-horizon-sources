package net.lingala.zip4j.crypto.PBKDF2;

import javax.crypto.*;
import javax.crypto.spec.*;
import java.security.*;

public class MacBasedPRF implements PRF
{
    protected int hLen;
    protected Mac mac;
    protected String macAlgorithm;
    
    public MacBasedPRF(final String macAlgorithm) {
        this.macAlgorithm = macAlgorithm;
        try {
            this.mac = Mac.getInstance(macAlgorithm);
            this.hLen = this.mac.getMacLength();
        }
        catch (NoSuchAlgorithmException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public MacBasedPRF(final String macAlgorithm, final String s) {
        this.macAlgorithm = macAlgorithm;
        try {
            this.mac = Mac.getInstance(macAlgorithm, s);
            this.hLen = this.mac.getMacLength();
        }
        catch (NoSuchProviderException ex) {
            throw new RuntimeException(ex);
        }
        catch (NoSuchAlgorithmException ex2) {
            throw new RuntimeException(ex2);
        }
    }
    
    public byte[] doFinal() {
        return this.mac.doFinal();
    }
    
    @Override
    public byte[] doFinal(final byte[] array) {
        return this.mac.doFinal(array);
    }
    
    @Override
    public int getHLen() {
        return this.hLen;
    }
    
    @Override
    public void init(final byte[] array) {
        try {
            this.mac.init(new SecretKeySpec(array, this.macAlgorithm));
        }
        catch (InvalidKeyException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public void update(final byte[] array) {
        try {
            this.mac.update(array);
        }
        catch (IllegalStateException ex) {
            throw new RuntimeException(ex);
        }
    }
    
    public void update(final byte[] array, final int n, final int n2) {
        try {
            this.mac.update(array, n, n2);
        }
        catch (IllegalStateException ex) {
            throw new RuntimeException(ex);
        }
    }
}
