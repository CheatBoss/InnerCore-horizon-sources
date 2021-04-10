package org.spongycastle.util;

import java.io.*;

public interface Encodable
{
    byte[] getEncoded() throws IOException;
}
