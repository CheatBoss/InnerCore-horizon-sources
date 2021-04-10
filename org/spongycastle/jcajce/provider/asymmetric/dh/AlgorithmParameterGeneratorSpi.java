package org.spongycastle.jcajce.provider.asymmetric.dh;

import org.spongycastle.crypto.generators.*;
import org.spongycastle.jcajce.provider.asymmetric.util.*;
import java.security.spec.*;
import org.spongycastle.crypto.params.*;
import javax.crypto.spec.*;
import java.security.*;

public class AlgorithmParameterGeneratorSpi extends BaseAlgorithmParameterGeneratorSpi
{
    private int l;
    protected SecureRandom random;
    protected int strength;
    
    public AlgorithmParameterGeneratorSpi() {
        this.strength = 2048;
        this.l = 0;
    }
    
    @Override
    protected AlgorithmParameters engineGenerateParameters() {
        final DHParametersGenerator dhParametersGenerator = new DHParametersGenerator();
        final int defaultCertainty = PrimeCertaintyCalculator.getDefaultCertainty(this.strength);
        final SecureRandom random = this.random;
        if (random != null) {
            dhParametersGenerator.init(this.strength, defaultCertainty, random);
        }
        else {
            dhParametersGenerator.init(this.strength, defaultCertainty, new SecureRandom());
        }
        final DHParameters generateParameters = dhParametersGenerator.generateParameters();
        try {
            final AlgorithmParameters parametersInstance = this.createParametersInstance("DH");
            parametersInstance.init(new DHParameterSpec(generateParameters.getP(), generateParameters.getG(), this.l));
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
    protected void engineInit(final AlgorithmParameterSpec algorithmParameterSpec, final SecureRandom random) throws InvalidAlgorithmParameterException {
        if (algorithmParameterSpec instanceof DHGenParameterSpec) {
            final DHGenParameterSpec dhGenParameterSpec = (DHGenParameterSpec)algorithmParameterSpec;
            this.strength = dhGenParameterSpec.getPrimeSize();
            this.l = dhGenParameterSpec.getExponentSize();
            this.random = random;
            return;
        }
        throw new InvalidAlgorithmParameterException("DH parameter generator requires a DHGenParameterSpec for initialisation");
    }
}
