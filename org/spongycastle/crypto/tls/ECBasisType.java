package org.spongycastle.crypto.tls;

public class ECBasisType
{
    public static final short ec_basis_pentanomial = 2;
    public static final short ec_basis_trinomial = 1;
    
    public static boolean isValid(final short n) {
        return n >= 1 && n <= 2;
    }
}
