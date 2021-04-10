package org.spongycastle.crypto.tls;

import java.io.*;
import org.spongycastle.crypto.*;

class SignerInputBuffer extends ByteArrayOutputStream
{
    void updateSigner(final Signer signer) {
        signer.update(this.buf, 0, this.count);
    }
}
