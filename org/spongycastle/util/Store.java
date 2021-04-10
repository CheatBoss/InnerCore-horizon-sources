package org.spongycastle.util;

import java.util.*;

public interface Store<T>
{
    Collection<T> getMatches(final Selector<T> p0) throws StoreException;
}
