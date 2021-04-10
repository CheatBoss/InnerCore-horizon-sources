package org.spongycastle.math.field;

import java.math.*;

class PrimeField implements FiniteField
{
    protected final BigInteger characteristic;
    
    PrimeField(final BigInteger characteristic) {
        this.characteristic = characteristic;
    }
    
    @Override
    public boolean equals(final Object o) {
        return this == o || (o instanceof PrimeField && this.characteristic.equals(((PrimeField)o).characteristic));
    }
    
    @Override
    public BigInteger getCharacteristic() {
        return this.characteristic;
    }
    
    @Override
    public int getDimension() {
        return 1;
    }
    
    @Override
    public int hashCode() {
        return this.characteristic.hashCode();
    }
}
