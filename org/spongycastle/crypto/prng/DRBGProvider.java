package org.spongycastle.crypto.prng;

import org.spongycastle.crypto.prng.drbg.*;

interface DRBGProvider
{
    SP80090DRBG get(final EntropySource p0);
}
