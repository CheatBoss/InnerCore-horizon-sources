package com.android.dx.ssa;

import com.android.dx.util.*;

public final class SetFactory
{
    private static final int DOMFRONT_SET_THRESHOLD_SIZE = 3072;
    private static final int INTERFERENCE_SET_THRESHOLD_SIZE = 3072;
    private static final int LIVENESS_SET_THRESHOLD_SIZE = 3072;
    
    static IntSet makeDomFrontSet(final int n) {
        if (n <= 3072) {
            return new BitIntSet(n);
        }
        return new ListIntSet();
    }
    
    public static IntSet makeInterferenceSet(final int n) {
        if (n <= 3072) {
            return new BitIntSet(n);
        }
        return new ListIntSet();
    }
    
    static IntSet makeLivenessSet(final int n) {
        if (n <= 3072) {
            return new BitIntSet(n);
        }
        return new ListIntSet();
    }
}
