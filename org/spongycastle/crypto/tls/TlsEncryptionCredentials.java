package org.spongycastle.crypto.tls;

import java.io.*;

public interface TlsEncryptionCredentials extends TlsCredentials
{
    byte[] decryptPreMasterSecret(final byte[] p0) throws IOException;
}
