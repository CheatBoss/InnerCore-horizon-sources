package org.spongycastle.crypto.tls;

import java.io.*;
import org.spongycastle.crypto.*;

class DigestInputBuffer extends ByteArrayOutputStream
{
    void updateDigest(final Digest digest) {
        digest.update(this.buf, 0, this.count);
    }
}
