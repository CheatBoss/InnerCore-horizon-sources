package org.spongycastle.crypto.generators;

import org.spongycastle.crypto.*;

public class KDF1BytesGenerator extends BaseKDFBytesGenerator
{
    public KDF1BytesGenerator(final Digest digest) {
        super(0, digest);
    }
}
