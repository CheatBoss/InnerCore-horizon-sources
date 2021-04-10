package org.spongycastle.asn1.x509;

import org.spongycastle.asn1.*;

public class PolicyQualifierId extends ASN1ObjectIdentifier
{
    private static final String id_qt = "1.3.6.1.5.5.7.2";
    public static final PolicyQualifierId id_qt_cps;
    public static final PolicyQualifierId id_qt_unotice;
    
    static {
        id_qt_cps = new PolicyQualifierId("1.3.6.1.5.5.7.2.1");
        id_qt_unotice = new PolicyQualifierId("1.3.6.1.5.5.7.2.2");
    }
    
    private PolicyQualifierId(final String s) {
        super(s);
    }
}
