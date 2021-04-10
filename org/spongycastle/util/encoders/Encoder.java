package org.spongycastle.util.encoders;

import java.io.*;

public interface Encoder
{
    int decode(final String p0, final OutputStream p1) throws IOException;
    
    int decode(final byte[] p0, final int p1, final int p2, final OutputStream p3) throws IOException;
    
    int encode(final byte[] p0, final int p1, final int p2, final OutputStream p3) throws IOException;
}
