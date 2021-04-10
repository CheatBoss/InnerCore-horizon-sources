package org.spongycastle.crypto.ec;

import java.math.*;

public interface ECPairFactorTransform extends ECPairTransform
{
    BigInteger getTransformValue();
}
