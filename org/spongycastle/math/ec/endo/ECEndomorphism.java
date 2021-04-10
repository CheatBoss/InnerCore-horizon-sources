package org.spongycastle.math.ec.endo;

import org.spongycastle.math.ec.*;

public interface ECEndomorphism
{
    ECPointMap getPointMap();
    
    boolean hasEfficientPointMap();
}
