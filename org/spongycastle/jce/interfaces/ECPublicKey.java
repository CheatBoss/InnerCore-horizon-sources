package org.spongycastle.jce.interfaces;

import java.security.*;
import org.spongycastle.math.ec.*;

public interface ECPublicKey extends PublicKey, ECKey
{
    ECPoint getQ();
}
