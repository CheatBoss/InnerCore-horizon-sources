package org.spongycastle.jcajce.provider.asymmetric.util;

import org.spongycastle.jcajce.util.*;
import java.security.*;

public abstract class BaseAlgorithmParameterGeneratorSpi extends AlgorithmParameterGeneratorSpi
{
    private final JcaJceHelper helper;
    
    public BaseAlgorithmParameterGeneratorSpi() {
        this.helper = new BCJcaJceHelper();
    }
    
    protected final AlgorithmParameters createParametersInstance(final String s) throws NoSuchAlgorithmException, NoSuchProviderException {
        return this.helper.createAlgorithmParameters(s);
    }
}
