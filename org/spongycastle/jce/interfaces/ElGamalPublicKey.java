package org.spongycastle.jce.interfaces;

import javax.crypto.interfaces.*;
import java.math.*;

public interface ElGamalPublicKey extends DHPublicKey, ElGamalKey
{
    BigInteger getY();
}
