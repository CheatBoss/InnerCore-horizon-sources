package org.spongycastle.jcajce.spec;

import javax.crypto.spec.*;
import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.pkcs.*;
import org.spongycastle.asn1.*;

public class PBKDF2KeySpec extends PBEKeySpec
{
    private static final AlgorithmIdentifier defaultPRF;
    private AlgorithmIdentifier prf;
    
    static {
        defaultPRF = new AlgorithmIdentifier(PKCSObjectIdentifiers.id_hmacWithSHA1, DERNull.INSTANCE);
    }
    
    public PBKDF2KeySpec(final char[] array, final byte[] array2, final int n, final int n2, final AlgorithmIdentifier prf) {
        super(array, array2, n, n2);
        this.prf = prf;
    }
    
    public AlgorithmIdentifier getPrf() {
        return this.prf;
    }
    
    public boolean isDefaultPrf() {
        return PBKDF2KeySpec.defaultPRF.equals(this.prf);
    }
}
