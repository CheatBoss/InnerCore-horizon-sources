package org.spongycastle.x509;

import java.io.*;
import org.spongycastle.x509.util.*;
import java.util.*;

public abstract class X509StreamParserSpi
{
    public abstract void engineInit(final InputStream p0);
    
    public abstract Object engineRead() throws StreamParsingException;
    
    public abstract Collection engineReadAll() throws StreamParsingException;
}
