package org.spongycastle.crypto.params;

import java.security.*;

public class DSAParameterGenerationParameters
{
    public static final int DIGITAL_SIGNATURE_USAGE = 1;
    public static final int KEY_ESTABLISHMENT_USAGE = 2;
    private final int certainty;
    private final int l;
    private final int n;
    private final SecureRandom random;
    private final int usageIndex;
    
    public DSAParameterGenerationParameters(final int n, final int n2, final int n3, final SecureRandom secureRandom) {
        this(n, n2, n3, secureRandom, -1);
    }
    
    public DSAParameterGenerationParameters(final int l, final int n, final int certainty, final SecureRandom random, final int usageIndex) {
        this.l = l;
        this.n = n;
        this.certainty = certainty;
        this.usageIndex = usageIndex;
        this.random = random;
    }
    
    public int getCertainty() {
        return this.certainty;
    }
    
    public int getL() {
        return this.l;
    }
    
    public int getN() {
        return this.n;
    }
    
    public SecureRandom getRandom() {
        return this.random;
    }
    
    public int getUsageIndex() {
        return this.usageIndex;
    }
}
