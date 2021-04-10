package org.spongycastle.jce;

import java.util.*;
import org.spongycastle.jce.spec.*;
import org.spongycastle.crypto.ec.*;
import org.spongycastle.asn1.*;
import org.spongycastle.asn1.x9.*;

public class ECNamedCurveTable
{
    public static Enumeration getNames() {
        return org.spongycastle.asn1.x9.ECNamedCurveTable.getNames();
    }
    
    public static ECNamedCurveParameterSpec getParameterSpec(final String s) {
        X9ECParameters x9ECParameters;
        X9ECParameters byOID = x9ECParameters = CustomNamedCurves.getByName(s);
        if (byOID == null) {
            try {
                byOID = CustomNamedCurves.getByOID(new ASN1ObjectIdentifier(s));
            }
            catch (IllegalArgumentException ex) {}
            x9ECParameters = byOID;
            if (byOID == null) {
                final X9ECParameters byName = org.spongycastle.asn1.x9.ECNamedCurveTable.getByName(s);
                if ((x9ECParameters = byName) == null) {
                    try {
                        x9ECParameters = org.spongycastle.asn1.x9.ECNamedCurveTable.getByOID(new ASN1ObjectIdentifier(s));
                    }
                    catch (IllegalArgumentException ex2) {
                        x9ECParameters = byName;
                    }
                }
            }
        }
        if (x9ECParameters == null) {
            return null;
        }
        return new ECNamedCurveParameterSpec(s, x9ECParameters.getCurve(), x9ECParameters.getG(), x9ECParameters.getN(), x9ECParameters.getH(), x9ECParameters.getSeed());
    }
}
