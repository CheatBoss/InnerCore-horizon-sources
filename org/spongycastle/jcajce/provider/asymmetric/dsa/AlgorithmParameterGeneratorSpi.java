package org.spongycastle.jcajce.provider.asymmetric.dsa;

import org.spongycastle.crypto.generators.*;
import org.spongycastle.crypto.digests.*;
import org.spongycastle.crypto.*;
import org.spongycastle.jcajce.provider.asymmetric.util.*;
import java.security.spec.*;
import org.spongycastle.crypto.params.*;
import java.security.*;

public class AlgorithmParameterGeneratorSpi extends BaseAlgorithmParameterGeneratorSpi
{
    protected DSAParameterGenerationParameters params;
    protected SecureRandom random;
    protected int strength;
    
    public AlgorithmParameterGeneratorSpi() {
        this.strength = 2048;
    }
    
    @Override
    protected AlgorithmParameters engineGenerateParameters() {
        DSAParametersGenerator dsaParametersGenerator;
        if (this.strength <= 1024) {
            dsaParametersGenerator = new DSAParametersGenerator();
        }
        else {
            dsaParametersGenerator = new DSAParametersGenerator(new SHA256Digest());
        }
        if (this.random == null) {
            this.random = new SecureRandom();
        }
        final int defaultCertainty = PrimeCertaintyCalculator.getDefaultCertainty(this.strength);
        final int strength = this.strength;
        if (strength == 1024) {
            dsaParametersGenerator.init(this.params = new DSAParameterGenerationParameters(1024, 160, defaultCertainty, this.random));
        }
        else if (strength > 1024) {
            dsaParametersGenerator.init(this.params = new DSAParameterGenerationParameters(this.strength, 256, defaultCertainty, this.random));
        }
        else {
            dsaParametersGenerator.init(strength, defaultCertainty, this.random);
        }
        final DSAParameters generateParameters = dsaParametersGenerator.generateParameters();
        try {
            final AlgorithmParameters parametersInstance = this.createParametersInstance("DSA");
            parametersInstance.init(new DSAParameterSpec(generateParameters.getP(), generateParameters.getQ(), generateParameters.getG()));
            return parametersInstance;
        }
        catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }
    
    @Override
    protected void engineInit(final int strength, final SecureRandom random) {
        if (strength < 512 || strength > 3072) {
            throw new InvalidParameterException("strength must be from 512 - 3072");
        }
        if (strength <= 1024 && strength % 64 != 0) {
            throw new InvalidParameterException("strength must be a multiple of 64 below 1024 bits.");
        }
        if (strength > 1024 && strength % 1024 != 0) {
            throw new InvalidParameterException("strength must be a multiple of 1024 above 1024 bits.");
        }
        this.strength = strength;
        this.random = random;
    }
    
    @Override
    protected void engineInit(final AlgorithmParameterSpec algorithmParameterSpec, final SecureRandom secureRandom) throws InvalidAlgorithmParameterException {
        throw new InvalidAlgorithmParameterException("No supported AlgorithmParameterSpec for DSA parameter generation.");
    }
}
