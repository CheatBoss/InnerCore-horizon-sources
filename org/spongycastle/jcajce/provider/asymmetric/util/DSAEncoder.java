package org.spongycastle.jcajce.provider.asymmetric.util;

import java.math.*;
import java.io.*;

public interface DSAEncoder
{
    BigInteger[] decode(final byte[] p0) throws IOException;
    
    byte[] encode(final BigInteger p0, final BigInteger p1) throws IOException;
}
