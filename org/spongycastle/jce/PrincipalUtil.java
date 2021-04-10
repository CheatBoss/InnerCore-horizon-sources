package org.spongycastle.jce;

import org.spongycastle.asn1.*;
import java.io.*;
import org.spongycastle.asn1.x509.*;
import java.security.cert.*;

public class PrincipalUtil
{
    public static X509Principal getIssuerX509Principal(final X509CRL x509CRL) throws CRLException {
        try {
            return new X509Principal(X509Name.getInstance(TBSCertList.getInstance(ASN1Primitive.fromByteArray(x509CRL.getTBSCertList())).getIssuer()));
        }
        catch (IOException ex) {
            throw new CRLException(ex.toString());
        }
    }
    
    public static X509Principal getIssuerX509Principal(final X509Certificate x509Certificate) throws CertificateEncodingException {
        try {
            return new X509Principal(X509Name.getInstance(TBSCertificateStructure.getInstance(ASN1Primitive.fromByteArray(x509Certificate.getTBSCertificate())).getIssuer()));
        }
        catch (IOException ex) {
            throw new CertificateEncodingException(ex.toString());
        }
    }
    
    public static X509Principal getSubjectX509Principal(final X509Certificate x509Certificate) throws CertificateEncodingException {
        try {
            return new X509Principal(X509Name.getInstance(TBSCertificateStructure.getInstance(ASN1Primitive.fromByteArray(x509Certificate.getTBSCertificate())).getSubject()));
        }
        catch (IOException ex) {
            throw new CertificateEncodingException(ex.toString());
        }
    }
}
