package org.spongycastle.util.encoders;

public interface Translator
{
    int decode(final byte[] p0, final int p1, final int p2, final byte[] p3, final int p4);
    
    int encode(final byte[] p0, final int p1, final int p2, final byte[] p3, final int p4);
    
    int getDecodedBlockSize();
    
    int getEncodedBlockSize();
}
