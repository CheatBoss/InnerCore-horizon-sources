package org.spongycastle.crypto.tls;

import java.io.*;

interface DTLSHandshakeRetransmit
{
    void receivedHandshakeRecord(final int p0, final byte[] p1, final int p2, final int p3) throws IOException;
}
