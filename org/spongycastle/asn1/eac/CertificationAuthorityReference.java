package org.spongycastle.asn1.eac;

public class CertificationAuthorityReference extends CertificateHolderReference
{
    public CertificationAuthorityReference(final String s, final String s2, final String s3) {
        super(s, s2, s3);
    }
    
    CertificationAuthorityReference(final byte[] array) {
        super(array);
    }
}
