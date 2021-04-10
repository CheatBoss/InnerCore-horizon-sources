package org.spongycastle.crypto.params;

import org.spongycastle.crypto.*;
import java.security.*;

public class NaccacheSternKeyGenerationParameters extends KeyGenerationParameters
{
    private int certainty;
    private int cntSmallPrimes;
    private boolean debug;
    
    public NaccacheSternKeyGenerationParameters(final SecureRandom secureRandom, final int n, final int n2, final int n3) {
        this(secureRandom, n, n2, n3, false);
    }
    
    public NaccacheSternKeyGenerationParameters(final SecureRandom secureRandom, final int n, final int certainty, final int cntSmallPrimes, final boolean debug) {
        super(secureRandom, n);
        this.debug = false;
        this.certainty = certainty;
        if (cntSmallPrimes % 2 == 1) {
            throw new IllegalArgumentException("cntSmallPrimes must be a multiple of 2");
        }
        if (cntSmallPrimes >= 30) {
            this.cntSmallPrimes = cntSmallPrimes;
            this.debug = debug;
            return;
        }
        throw new IllegalArgumentException("cntSmallPrimes must be >= 30 for security reasons");
    }
    
    public int getCertainty() {
        return this.certainty;
    }
    
    public int getCntSmallPrimes() {
        return this.cntSmallPrimes;
    }
    
    public boolean isDebug() {
        return this.debug;
    }
}
