package org.spongycastle.jcajce.provider.asymmetric.elgamal;

import org.spongycastle.jcajce.provider.asymmetric.util.*;
import org.spongycastle.crypto.generators.*;
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
        this.strength = 1024;
        this.l = 0;
    }
    
    @Override
    protected AlgorithmParameters engineGenerateParameters() {
        final ElGamalParametersGenerator elGamalParametersGenerator = new ElGamalParametersGenerator();
        final SecureRandom random = this.random;
        if (random != null) {
            elGamalParametersGenerator.init(this.strength, 20, random);
        }
        else {
            elGamalParametersGenerator.init(this.strength, 20, new SecureRandom());
        }
        final ElGamalParameters generateParameters = elGamalParametersGenerator.generateParameters();
        try {
            final AlgorithmParameters parametersInstance = this.createParametersInstance("ElGamal");
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
