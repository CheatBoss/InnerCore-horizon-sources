package org.spongycastle.crypto;

public interface CharToByteConverter
{
    byte[] convert(final char[] p0);
    
    String getType();
}
