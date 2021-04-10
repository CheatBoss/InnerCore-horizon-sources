package org.spongycastle.jcajce.provider.symmetric.util;

import java.security.*;
import java.security.spec.*;

public abstract class BaseAlgorithmParameters extends AlgorithmParametersSpi
{
    @Override
    protected AlgorithmParameterSpec engineGetParameterSpec(final Class clazz) throws InvalidParameterSpecException {
        if (clazz != null) {
            return this.localEngineGetParameterSpec(clazz);
        }
        throw new NullPointerException("argument to getParameterSpec must not be null");
    }
    
    protected boolean isASN1FormatString(final String s) {
        return s == null || s.equals("ASN.1");
    }
    
    protected abstract AlgorithmParameterSpec localEngineGetParameterSpec(final Class p0) throws InvalidParameterSpecException;
}
