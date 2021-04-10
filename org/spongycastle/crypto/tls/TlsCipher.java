package org.spongycastle.crypto.tls;

import java.io.*;

public interface TlsCipher
{
    byte[] decodeCiphertext(final long p0, final short p1, final byte[] p2, final int p3, final int p4) throws IOException;
    
    byte[] encodePlaintext(final long p0, final short p1, final byte[] p2, final int p3, final int p4) throws IOException;
    
    int getPlaintextLimit(final int p0);
}
