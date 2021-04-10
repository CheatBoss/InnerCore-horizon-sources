package org.spongycastle.crypto;

import java.math.*;

public interface BasicAgreement
{
    BigInteger calculateAgreement(final CipherParameters p0);
    
    int getFieldSize();
    
    void init(final CipherParameters p0);
}
