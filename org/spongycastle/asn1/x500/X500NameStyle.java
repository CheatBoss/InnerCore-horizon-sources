package org.spongycastle.asn1.x500;

import org.spongycastle.asn1.*;

public interface X500NameStyle
{
    boolean areEqual(final X500Name p0, final X500Name p1);
    
    ASN1ObjectIdentifier attrNameToOID(final String p0);
    
    int calculateHashCode(final X500Name p0);
    
    RDN[] fromString(final String p0);
    
    String[] oidToAttrNames(final ASN1ObjectIdentifier p0);
    
    String oidToDisplayName(final ASN1ObjectIdentifier p0);
    
    ASN1Encodable stringToValue(final ASN1ObjectIdentifier p0, final String p1);
    
    String toString(final X500Name p0);
}
