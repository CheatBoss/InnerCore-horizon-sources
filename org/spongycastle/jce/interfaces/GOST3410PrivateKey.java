package org.spongycastle.jce.interfaces;

import java.security.*;
import java.math.*;

public interface GOST3410PrivateKey extends PrivateKey, GOST3410Key
{
    BigInteger getX();
}
