package org.spongycastle.jcajce.provider.asymmetric.gost;

import org.spongycastle.jcajce.provider.asymmetric.util.*;
import org.spongycastle.crypto.generators.*;
import org.spongycastle.jce.spec.*;
import java.security.spec.*;
import org.spongycastle.crypto.params.*;
import java.security.*;

public abstract class AlgorithmParameterGeneratorSpi extends BaseAlgorithmParameterGeneratorSpi
{
    protected SecureRandom random;
    protected int strength;
    
    public AlgorithmParameterGeneratorSpi() {
        this.strength = 1024;
    }
    
    @Override
    protected AlgorithmParameters engineGenerateParameters() {
        final GOST3410ParametersGenerator gost3410ParametersGenerator = new GOST3410ParametersGenerator();
        final SecureRandom random = this.random;
        if (random != null) {
            gost3410ParametersGenerator.init(this.strength, 2, random);
        }
        else {
            gost3410ParametersGenerator.init(this.strength, 2, new SecureRandom());
        }
        final GOST3410Parameters generateParameters = gost3410ParametersGenerator.generateParameters();
        try {
            final AlgorithmParameters parametersInstance = this.createParametersInstance("GOST3410");
            parametersInstance.init(new GOST3410ParameterSpec(new GOST3410PublicKeyParameterSetSpec(generateParameters.getP(), generateParameters.getQ(), generateParameters.getA())));
            return parametersInstance;
        }
        catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }
    
    @Override
    protected void engineInit(final int strength, final SecureRandom random) {
        this.strength = strength;
        this.random = random;
    }
    
    @Override
    protected void engineInit(final AlgorithmParameterSpec algorithmParameterSpec, final SecureRandom secureRandom) throws InvalidAlgorithmParameterException {
        throw new InvalidAlgorithmParameterException("No supported AlgorithmParameterSpec for GOST3410 parameter generation.");
    }
}
