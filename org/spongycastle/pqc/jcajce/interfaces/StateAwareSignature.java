package org.spongycastle.pqc.jcajce.interfaces;

import java.security.cert.*;
import java.security.*;
import java.nio.*;

public interface StateAwareSignature
{
    String getAlgorithm();
    
    PrivateKey getUpdatedPrivateKey();
    
    void initSign(final PrivateKey p0) throws InvalidKeyException;
    
    void initSign(final PrivateKey p0, final SecureRandom p1) throws InvalidKeyException;
    
    void initVerify(final PublicKey p0) throws InvalidKeyException;
    
    void initVerify(final Certificate p0) throws InvalidKeyException;
    
    int sign(final byte[] p0, final int p1, final int p2) throws SignatureException;
    
    byte[] sign() throws SignatureException;
    
    void update(final byte p0) throws SignatureException;
    
    void update(final ByteBuffer p0) throws SignatureException;
    
    void update(final byte[] p0) throws SignatureException;
    
    void update(final byte[] p0, final int p1, final int p2) throws SignatureException;
    
    boolean verify(final byte[] p0) throws SignatureException;
    
    boolean verify(final byte[] p0, final int p1, final int p2) throws SignatureException;
}
