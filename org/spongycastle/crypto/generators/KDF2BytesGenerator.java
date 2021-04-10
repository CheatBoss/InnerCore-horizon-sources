package org.spongycastle.crypto.generators;

import org.spongycastle.crypto.*;

public class KDF2BytesGenerator extends BaseKDFBytesGenerator
{
    public KDF2BytesGenerator(final Digest digest) {
        super(1, digest);
    }
}
