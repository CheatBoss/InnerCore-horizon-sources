package org.spongycastle.crypto.paddings;

import java.security.*;
import org.spongycastle.crypto.*;

public interface BlockCipherPadding
{
    int addPadding(final byte[] p0, final int p1);
    
    String getPaddingName();
    
    void init(final SecureRandom p0) throws IllegalArgumentException;
    
    int padCount(final byte[] p0) throws InvalidCipherTextException;
}
