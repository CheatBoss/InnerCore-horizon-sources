package org.spongycastle.util.io;

import java.io.*;

public class StreamOverflowException extends IOException
{
    public StreamOverflowException(final String s) {
        super(s);
    }
}
