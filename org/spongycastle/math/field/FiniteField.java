package org.spongycastle.math.field;

import java.math.*;

public interface FiniteField
{
    BigInteger getCharacteristic();
    
    int getDimension();
}
