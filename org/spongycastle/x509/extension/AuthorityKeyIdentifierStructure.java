package org.spongycastle.x509.extension;

import java.security.*;
import java.security.cert.*;
import java.io.*;
import org.spongycastle.jce.*;
import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.*;

public class AuthorityKeyIdentifierStructure extends AuthorityKeyIdentifier
{
    public AuthorityKeyIdentifierStructure(final PublicKey publicKey) throws InvalidKeyException {
        super(fromKey(publicKey));
    }
    
    public AuthorityKeyIdentifierStructure(final X509Certificate x509Certificate) throws CertificateParsingException {
        super(fromCertificate(x509Certificate));
    }
    
    public AuthorityKeyIdentifierStructure(final Extension extension) {
        super((ASN1Sequence)extension.getParsedValue());
    }
    
    public AuthorityKeyIdentifierStructure(final X509Extension x509Extension) {
        super((ASN1Sequence)x509Extension.getParsedValue());
    }
    
    public AuthorityKeyIdentifierStructure(final byte[] array) throws IOException {
        super((ASN1Sequence)X509ExtensionUtil.fromExtensionValue(array));
    }
    
    private static ASN1Sequence fromCertificate(final X509Certificate x509Certificate) throws CertificateParsingException {
        try {
            if (x509Certificate.getVersion() != 3) {
                return (ASN1Sequence)new AuthorityKeyIdentifier(SubjectPublicKeyInfo.getInstance(x509Certificate.getPublicKey().getEncoded()), new GeneralNames(new GeneralName(PrincipalUtil.getIssuerX509Principal(x509Certificate))), x509Certificate.getSerialNumber()).toASN1Primitive();
            }
            final GeneralName generalName = new GeneralName(PrincipalUtil.getIssuerX509Principal(x509Certificate));
            final byte[] extensionValue = x509Certificate.getExtensionValue(Extension.subjectKeyIdentifier.getId());
            if (extensionValue != null) {
                return (ASN1Sequence)new AuthorityKeyIdentifier(((ASN1OctetString)X509ExtensionUtil.fromExtensionValue(extensionValue)).getOctets(), new GeneralNames(generalName), x509Certificate.getSerialNumber()).toASN1Primitive();
            }
            return (ASN1Sequence)new AuthorityKeyIdentifier(SubjectPublicKeyInfo.getInstance(x509Certificate.getPublicKey().getEncoded()), new GeneralNames(generalName), x509Certificate.getSerialNumber()).toASN1Primitive();
        }
        catch (Exception ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Exception extracting certificate details: ");
            sb.append(ex.toString());
            throw new CertificateParsingException(sb.toString());
        }
    }
    
    private static ASN1Sequence fromKey(final PublicKey publicKey) throws InvalidKeyException {
        try {
            return (ASN1Sequence)new AuthorityKeyIdentifier(SubjectPublicKeyInfo.getInstance(publicKey.getEncoded())).toASN1Primitive();
        }
        catch (Exception ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("can't process key: ");
            sb.append(ex);
            throw new InvalidKeyException(sb.toString());
        }
    }
}
