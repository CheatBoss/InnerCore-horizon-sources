package org.spongycastle.crypto;

public interface DerivationFunction
{
    int generateBytes(final byte[] p0, final int p1, final int p2) throws DataLengthException, IllegalArgumentException;
    
    void init(final DerivationParameters p0);
}
