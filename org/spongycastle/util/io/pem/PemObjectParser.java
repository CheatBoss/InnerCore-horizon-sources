package org.spongycastle.util.io.pem;

import java.io.*;

public interface PemObjectParser
{
    Object parseObject(final PemObject p0) throws IOException;
}
