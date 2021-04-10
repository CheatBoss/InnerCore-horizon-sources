package org.spongycastle.crypto;

import org.spongycastle.crypto.params.*;
import java.io.*;

public interface KeyParser
{
    AsymmetricKeyParameter readKey(final InputStream p0) throws IOException;
}
