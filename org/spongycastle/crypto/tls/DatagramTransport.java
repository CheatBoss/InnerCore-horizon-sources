package org.spongycastle.crypto.tls;

import java.io.*;

public interface DatagramTransport
{
    void close() throws IOException;
    
    int getReceiveLimit() throws IOException;
    
    int getSendLimit() throws IOException;
    
    int receive(final byte[] p0, final int p1, final int p2, final int p3) throws IOException;
    
    void send(final byte[] p0, final int p1, final int p2) throws IOException;
}
