package org.spongycastle.jcajce;

import java.security.cert.*;
import java.util.*;
import org.spongycastle.util.*;

public interface PKIXCRLStore<T extends CRL> extends Store<T>
{
    Collection<T> getMatches(final Selector<T> p0) throws StoreException;
}
