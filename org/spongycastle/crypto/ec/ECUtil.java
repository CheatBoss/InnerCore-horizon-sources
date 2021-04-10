package org.spongycastle.crypto.ec;

import java.math.*;
import java.security.*;
import org.spongycastle.math.ec.*;
import java.util.*;

class ECUtil
{
    static BigInteger generateK(final BigInteger bigInteger, final SecureRandom secureRandom) {
        final int bitLength = bigInteger.bitLength();
        BigInteger bigInteger2;
        do {
            bigInteger2 = new BigInteger(bitLength, secureRandom);
        } while (bigInteger2.equals(ECConstants.ZERO) || bigInteger2.compareTo(bigInteger) >= 0);
        return bigInteger2;
    }
}
