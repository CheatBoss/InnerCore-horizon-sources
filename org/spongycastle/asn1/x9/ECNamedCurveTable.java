package org.spongycastle.asn1.x9;

import java.util.*;
import org.spongycastle.asn1.sec.*;
import org.spongycastle.asn1.nist.*;
import org.spongycastle.asn1.teletrust.*;
import org.spongycastle.asn1.anssi.*;
import org.spongycastle.asn1.gm.*;
import org.spongycastle.asn1.*;
import org.spongycastle.asn1.cryptopro.*;

public class ECNamedCurveTable
{
    private static void addEnumeration(final Vector vector, final Enumeration enumeration) {
        while (enumeration.hasMoreElements()) {
            vector.addElement(enumeration.nextElement());
        }
    }
    
    public static X9ECParameters getByName(final String s) {
        X9ECParameters x9ECParameters;
        if ((x9ECParameters = X962NamedCurves.getByName(s)) == null) {
            x9ECParameters = SECNamedCurves.getByName(s);
        }
        X9ECParameters byName;
        if ((byName = x9ECParameters) == null) {
            byName = NISTNamedCurves.getByName(s);
        }
        X9ECParameters byName2;
        if ((byName2 = byName) == null) {
            byName2 = TeleTrusTNamedCurves.getByName(s);
        }
        X9ECParameters byName3;
        if ((byName3 = byName2) == null) {
            byName3 = ANSSINamedCurves.getByName(s);
        }
        X9ECParameters byName4;
        if ((byName4 = byName3) == null) {
            byName4 = GMNamedCurves.getByName(s);
        }
        return byName4;
    }
    
    public static X9ECParameters getByOID(final ASN1ObjectIdentifier asn1ObjectIdentifier) {
        X9ECParameters x9ECParameters;
        if ((x9ECParameters = X962NamedCurves.getByOID(asn1ObjectIdentifier)) == null) {
            x9ECParameters = SECNamedCurves.getByOID(asn1ObjectIdentifier);
        }
        X9ECParameters byOID;
        if ((byOID = x9ECParameters) == null) {
            byOID = TeleTrusTNamedCurves.getByOID(asn1ObjectIdentifier);
        }
        X9ECParameters byOID2;
        if ((byOID2 = byOID) == null) {
            byOID2 = ANSSINamedCurves.getByOID(asn1ObjectIdentifier);
        }
        X9ECParameters byOID3;
        if ((byOID3 = byOID2) == null) {
            byOID3 = GMNamedCurves.getByOID(asn1ObjectIdentifier);
        }
        return byOID3;
    }
    
    public static String getName(final ASN1ObjectIdentifier asn1ObjectIdentifier) {
        String s;
        if ((s = NISTNamedCurves.getName(asn1ObjectIdentifier)) == null) {
            s = SECNamedCurves.getName(asn1ObjectIdentifier);
        }
        String name;
        if ((name = s) == null) {
            name = TeleTrusTNamedCurves.getName(asn1ObjectIdentifier);
        }
        String name2;
        if ((name2 = name) == null) {
            name2 = X962NamedCurves.getName(asn1ObjectIdentifier);
        }
        String name3;
        if ((name3 = name2) == null) {
            name3 = ECGOST3410NamedCurves.getName(asn1ObjectIdentifier);
        }
        String name4;
        if ((name4 = name3) == null) {
            name4 = GMNamedCurves.getName(asn1ObjectIdentifier);
        }
        return name4;
    }
    
    public static Enumeration getNames() {
        final Vector vector = new Vector();
        addEnumeration(vector, X962NamedCurves.getNames());
        addEnumeration(vector, SECNamedCurves.getNames());
        addEnumeration(vector, NISTNamedCurves.getNames());
        addEnumeration(vector, TeleTrusTNamedCurves.getNames());
        addEnumeration(vector, ANSSINamedCurves.getNames());
        addEnumeration(vector, GMNamedCurves.getNames());
        return vector.elements();
    }
    
    public static ASN1ObjectIdentifier getOID(final String s) {
        ASN1ObjectIdentifier asn1ObjectIdentifier;
        if ((asn1ObjectIdentifier = X962NamedCurves.getOID(s)) == null) {
            asn1ObjectIdentifier = SECNamedCurves.getOID(s);
        }
        ASN1ObjectIdentifier oid;
        if ((oid = asn1ObjectIdentifier) == null) {
            oid = NISTNamedCurves.getOID(s);
        }
        ASN1ObjectIdentifier oid2;
        if ((oid2 = oid) == null) {
            oid2 = TeleTrusTNamedCurves.getOID(s);
        }
        ASN1ObjectIdentifier oid3;
        if ((oid3 = oid2) == null) {
            oid3 = ANSSINamedCurves.getOID(s);
        }
        ASN1ObjectIdentifier oid4;
        if ((oid4 = oid3) == null) {
            oid4 = GMNamedCurves.getOID(s);
        }
        return oid4;
    }
}
