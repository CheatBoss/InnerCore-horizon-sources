package org.spongycastle.jce.interfaces;

import java.security.*;
import java.math.*;

public interface GOST3410PublicKey extends PublicKey, GOST3410Key
{
    BigInteger getY();
}
