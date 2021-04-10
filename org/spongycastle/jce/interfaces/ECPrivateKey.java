package org.spongycastle.jce.interfaces;

import java.security.*;
import java.math.*;

public interface ECPrivateKey extends PrivateKey, ECKey
{
    BigInteger getD();
}
