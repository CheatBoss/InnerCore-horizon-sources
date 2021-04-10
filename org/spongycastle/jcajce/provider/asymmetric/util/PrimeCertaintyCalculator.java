package org.spongycastle.jcajce.provider.asymmetric.util;

public class PrimeCertaintyCalculator
{
    private PrimeCertaintyCalculator() {
    }
    
    public static int getDefaultCertainty(final int n) {
        if (n <= 1024) {
            return 80;
        }
        return (n - 1) / 1024 * 16 + 96;
    }
}
