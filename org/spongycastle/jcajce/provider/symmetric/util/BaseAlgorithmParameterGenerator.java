package org.spongycastle.jcajce.provider.symmetric.util;

import org.spongycastle.jcajce.util.*;
import java.security.*;

public abstract class BaseAlgorithmParameterGenerator extends AlgorithmParameterGeneratorSpi
{
    private final JcaJceHelper helper;
    protected SecureRandom random;
    protected int strength;
    
    public BaseAlgorithmParameterGenerator() {
        this.helper = new BCJcaJceHelper();
        this.strength = 1024;
    }
    
    protected final AlgorithmParameters createParametersInstance(final String s) throws NoSuchAlgorithmException, NoSuchProviderException {
        return this.helper.createAlgorithmParameters(s);
    }
    
    @Override
    protected void engineInit(final int strength, final SecureRandom random) {
        this.strength = strength;
        this.random = random;
    }
}
