package org.spongycastle.jce.interfaces;

import javax.crypto.interfaces.*;
import java.math.*;

public interface ElGamalPrivateKey extends DHPrivateKey, ElGamalKey
{
    BigInteger getX();
}
