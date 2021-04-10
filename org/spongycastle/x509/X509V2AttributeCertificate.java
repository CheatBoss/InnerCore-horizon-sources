package org.spongycastle.x509;

import java.text.*;
import java.io.*;
import org.spongycastle.util.*;
import java.util.*;
import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.*;
import java.math.*;
import java.security.cert.*;
import java.security.*;

public class X509V2AttributeCertificate implements X509AttributeCertificate
{
    private AttributeCertificate cert;
    private Date notAfter;
    private Date notBefore;
    
    public X509V2AttributeCertificate(final InputStream inputStream) throws IOException {
        this(getObject(inputStream));
    }
    
    X509V2AttributeCertificate(final AttributeCertificate cert) throws IOException {
        this.cert = cert;
        try {
            this.notAfter = cert.getAcinfo().getAttrCertValidityPeriod().getNotAfterTime().getDate();
            this.notBefore = cert.getAcinfo().getAttrCertValidityPeriod().getNotBeforeTime().getDate();
        }
        catch (ParseException ex) {
            throw new IOException("invalid data structure in certificate!");
        }
    }
    
    public X509V2AttributeCertificate(final byte[] array) throws IOException {
        this(new ByteArrayInputStream(array));
    }
    
    private Set getExtensionOIDs(final boolean b) {
        final Extensions extensions = this.cert.getAcinfo().getExtensions();
        if (extensions != null) {
            final HashSet<String> set = new HashSet<String>();
            final Enumeration oids = extensions.oids();
            while (oids.hasMoreElements()) {
                final ASN1ObjectIdentifier asn1ObjectIdentifier = oids.nextElement();
                if (extensions.getExtension(asn1ObjectIdentifier).isCritical() == b) {
                    set.add(asn1ObjectIdentifier.getId());
                }
            }
            return set;
        }
        return null;
    }
    
    private static AttributeCertificate getObject(final InputStream inputStream) throws IOException {
        try {
            return AttributeCertificate.getInstance(new ASN1InputStream(inputStream).readObject());
        }
        catch (Exception ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("exception decoding certificate structure: ");
            sb.append(ex.toString());
            throw new IOException(sb.toString());
        }
        catch (IOException ex2) {
            throw ex2;
        }
    }
    
    @Override
    public void checkValidity() throws CertificateExpiredException, CertificateNotYetValidException {
        this.checkValidity(new Date());
    }
    
    @Override
    public void checkValidity(final Date date) throws CertificateExpiredException, CertificateNotYetValidException {
        if (date.after(this.getNotAfter())) {
            final StringBuilder sb = new StringBuilder();
            sb.append("certificate expired on ");
            sb.append(this.getNotAfter());
            throw new CertificateExpiredException(sb.toString());
        }
        if (!date.before(this.getNotBefore())) {
            return;
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("certificate not valid till ");
        sb2.append(this.getNotBefore());
        throw new CertificateNotYetValidException(sb2.toString());
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof X509AttributeCertificate)) {
            return false;
        }
        final X509AttributeCertificate x509AttributeCertificate = (X509AttributeCertificate)o;
        try {
            return Arrays.areEqual(this.getEncoded(), x509AttributeCertificate.getEncoded());
        }
        catch (IOException ex) {
            return false;
        }
    }
    
    @Override
    public X509Attribute[] getAttributes() {
        final ASN1Sequence attributes = this.cert.getAcinfo().getAttributes();
        final X509Attribute[] array = new X509Attribute[attributes.size()];
        for (int i = 0; i != attributes.size(); ++i) {
            array[i] = new X509Attribute(attributes.getObjectAt(i));
        }
        return array;
    }
    
    @Override
    public X509Attribute[] getAttributes(final String s) {
        final ASN1Sequence attributes = this.cert.getAcinfo().getAttributes();
        final ArrayList<X509Attribute> list = new ArrayList<X509Attribute>();
        for (int i = 0; i != attributes.size(); ++i) {
            final X509Attribute x509Attribute = new X509Attribute(attributes.getObjectAt(i));
            if (x509Attribute.getOID().equals(s)) {
                list.add(x509Attribute);
            }
        }
        if (list.size() == 0) {
            return null;
        }
        return list.toArray(new X509Attribute[list.size()]);
    }
    
    @Override
    public Set getCriticalExtensionOIDs() {
        return this.getExtensionOIDs(true);
    }
    
    @Override
    public byte[] getEncoded() throws IOException {
        return this.cert.getEncoded();
    }
    
    @Override
    public byte[] getExtensionValue(final String s) {
        final Extensions extensions = this.cert.getAcinfo().getExtensions();
        if (extensions != null) {
            final Extension extension = extensions.getExtension(new ASN1ObjectIdentifier(s));
            if (extension != null) {
                try {
                    return extension.getExtnValue().getEncoded("DER");
                }
                catch (Exception ex) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("error encoding ");
                    sb.append(ex.toString());
                    throw new RuntimeException(sb.toString());
                }
            }
        }
        return null;
    }
    
    @Override
    public AttributeCertificateHolder getHolder() {
        return new AttributeCertificateHolder((ASN1Sequence)this.cert.getAcinfo().getHolder().toASN1Primitive());
    }
    
    @Override
    public AttributeCertificateIssuer getIssuer() {
        return new AttributeCertificateIssuer(this.cert.getAcinfo().getIssuer());
    }
    
    @Override
    public boolean[] getIssuerUniqueID() {
        final DERBitString issuerUniqueID = this.cert.getAcinfo().getIssuerUniqueID();
        if (issuerUniqueID != null) {
            final byte[] bytes = issuerUniqueID.getBytes();
            final int n = bytes.length * 8 - issuerUniqueID.getPadBits();
            final boolean[] array = new boolean[n];
            for (int i = 0; i != n; ++i) {
                array[i] = ((bytes[i / 8] & 128 >>> i % 8) != 0x0);
            }
            return array;
        }
        return null;
    }
    
    @Override
    public Set getNonCriticalExtensionOIDs() {
        return this.getExtensionOIDs(false);
    }
    
    @Override
    public Date getNotAfter() {
        return this.notAfter;
    }
    
    @Override
    public Date getNotBefore() {
        return this.notBefore;
    }
    
    @Override
    public BigInteger getSerialNumber() {
        return this.cert.getAcinfo().getSerialNumber().getValue();
    }
    
    @Override
    public byte[] getSignature() {
        return this.cert.getSignatureValue().getOctets();
    }
    
    @Override
    public int getVersion() {
        return this.cert.getAcinfo().getVersion().getValue().intValue() + 1;
    }
    
    @Override
    public boolean hasUnsupportedCriticalExtension() {
        final Set criticalExtensionOIDs = this.getCriticalExtensionOIDs();
        return criticalExtensionOIDs != null && !criticalExtensionOIDs.isEmpty();
    }
    
    @Override
    public int hashCode() {
        try {
            return Arrays.hashCode(this.getEncoded());
        }
        catch (IOException ex) {
            return 0;
        }
    }
    
    @Override
    public final void verify(final PublicKey publicKey, final String s) throws CertificateException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException {
        if (this.cert.getSignatureAlgorithm().equals(this.cert.getAcinfo().getSignature())) {
            final Signature instance = Signature.getInstance(this.cert.getSignatureAlgorithm().getAlgorithm().getId(), s);
            instance.initVerify(publicKey);
            try {
                instance.update(this.cert.getAcinfo().getEncoded());
                if (instance.verify(this.getSignature())) {
                    return;
                }
                throw new InvalidKeyException("Public key presented not for certificate signature");
            }
            catch (IOException ex) {
                throw new SignatureException("Exception encoding certificate info object");
            }
        }
        throw new CertificateException("Signature algorithm in certificate info not same as outer certificate");
    }
}
