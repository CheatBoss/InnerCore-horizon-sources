package org.spongycastle.jce;

import java.util.*;
import org.spongycastle.asn1.cryptopro.*;
import org.spongycastle.jce.spec.*;
import org.spongycastle.asn1.*;
import org.spongycastle.crypto.params.*;

public class ECGOST3410NamedCurveTable
{
    public static Enumeration getNames() {
        return ECGOST3410NamedCurves.getNames();
    }
    
    public static ECNamedCurveParameterSpec getParameterSpec(final String s) {
        ECDomainParameters ecDomainParameters;
        if ((ecDomainParameters = ECGOST3410NamedCurves.getByName(s)) == null) {
            try {
                ecDomainParameters = ECGOST3410NamedCurves.getByOID(new ASN1ObjectIdentifier(s));
            }
            catch (IllegalArgumentException ex) {
                return null;
            }
        }
        if (ecDomainParameters == null) {
            return null;
        }
        return new ECNamedCurveParameterSpec(s, ecDomainParameters.getCurve(), ecDomainParameters.getG(), ecDomainParameters.getN(), ecDomainParameters.getH(), ecDomainParameters.getSeed());
    }
}
