package org.spongycastle.math.field;

import org.spongycastle.util.*;

class GF2Polynomial implements Polynomial
{
    protected final int[] exponents;
    
    GF2Polynomial(final int[] array) {
        this.exponents = Arrays.clone(array);
    }
    
    @Override
    public boolean equals(final Object o) {
        return this == o || (o instanceof GF2Polynomial && Arrays.areEqual(this.exponents, ((GF2Polynomial)o).exponents));
    }
    
    @Override
    public int getDegree() {
        final int[] exponents = this.exponents;
        return exponents[exponents.length - 1];
    }
    
    @Override
    public int[] getExponentsPresent() {
        return Arrays.clone(this.exponents);
    }
    
    @Override
    public int hashCode() {
        return Arrays.hashCode(this.exponents);
    }
}
