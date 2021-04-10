package org.spongycastle.crypto.modes;

import org.spongycastle.crypto.*;

public interface AEADBlockCipher
{
    int doFinal(final byte[] p0, final int p1) throws IllegalStateException, InvalidCipherTextException;
    
    String getAlgorithmName();
    
    byte[] getMac();
    
    int getOutputSize(final int p0);
    
    BlockCipher getUnderlyingCipher();
    
    int getUpdateOutputSize(final int p0);
    
    void init(final boolean p0, final CipherParameters p1) throws IllegalArgumentException;
    
    void processAADByte(final byte p0);
    
    void processAADBytes(final byte[] p0, final int p1, final int p2);
    
    int processByte(final byte p0, final byte[] p1, final int p2) throws DataLengthException;
    
    int processBytes(final byte[] p0, final int p1, final int p2, final byte[] p3, final int p4) throws DataLengthException;
    
    void reset();
}
