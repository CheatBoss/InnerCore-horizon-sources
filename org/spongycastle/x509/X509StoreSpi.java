package org.spongycastle.x509;

import org.spongycastle.util.*;
import java.util.*;

public abstract class X509StoreSpi
{
    public abstract Collection engineGetMatches(final Selector p0);
    
    public abstract void engineInit(final X509StoreParameters p0);
}
