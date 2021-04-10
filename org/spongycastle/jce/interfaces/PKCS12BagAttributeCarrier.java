package org.spongycastle.jce.interfaces;

import org.spongycastle.asn1.*;
import java.util.*;

public interface PKCS12BagAttributeCarrier
{
    ASN1Encodable getBagAttribute(final ASN1ObjectIdentifier p0);
    
    Enumeration getBagAttributeKeys();
    
    void setBagAttribute(final ASN1ObjectIdentifier p0, final ASN1Encodable p1);
}
