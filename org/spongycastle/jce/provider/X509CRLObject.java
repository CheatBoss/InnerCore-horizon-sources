package org.spongycastle.jce.provider;

import org.spongycastle.asn1.x500.*;
import java.io.*;
import org.spongycastle.jce.*;
import javax.security.auth.x500.*;
import java.math.*;
import java.security.cert.*;
import org.spongycastle.util.*;
import org.spongycastle.util.encoders.*;
import org.spongycastle.asn1.*;
import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.util.*;
import java.util.*;
import java.security.*;

public class X509CRLObject extends X509CRL
{
    private CertificateList c;
    private int hashCodeValue;
    private boolean isHashCodeSet;
    private boolean isIndirect;
    private String sigAlgName;
    private byte[] sigAlgParams;
    
    public X509CRLObject(final CertificateList c) throws CRLException {
        this.isHashCodeSet = false;
        this.c = c;
        try {
            this.sigAlgName = X509SignatureUtil.getSignatureName(c.getSignatureAlgorithm());
            if (c.getSignatureAlgorithm().getParameters() != null) {
                this.sigAlgParams = c.getSignatureAlgorithm().getParameters().toASN1Primitive().getEncoded("DER");
            }
            else {
                this.sigAlgParams = null;
            }
            this.isIndirect = isIndirectCRL(this);
        }
        catch (Exception ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("CRL contents invalid: ");
            sb.append(ex);
            throw new CRLException(sb.toString());
        }
    }
    
    private void doVerify(final PublicKey publicKey, final Signature signature) throws CRLException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        if (!this.c.getSignatureAlgorithm().equals(this.c.getTBSCertList().getSignature())) {
            throw new CRLException("Signature algorithm on CertificateList does not match TBSCertList.");
        }
        signature.initVerify(publicKey);
        signature.update(this.getTBSCertList());
        if (signature.verify(this.getSignature())) {
            return;
        }
        throw new SignatureException("CRL does not verify with supplied public key.");
    }
    
    private Set getExtensionOIDs(final boolean b) {
        if (this.getVersion() == 2) {
            final Extensions extensions = this.c.getTBSCertList().getExtensions();
            if (extensions != null) {
                final HashSet<String> set = new HashSet<String>();
                final Enumeration oids = extensions.oids();
                while (oids.hasMoreElements()) {
                    final ASN1ObjectIdentifier asn1ObjectIdentifier = oids.nextElement();
                    if (b == extensions.getExtension(asn1ObjectIdentifier).isCritical()) {
                        set.add(asn1ObjectIdentifier.getId());
                    }
                }
                return set;
            }
        }
        return null;
    }
    
    public static boolean isIndirectCRL(final X509CRL x509CRL) throws CRLException {
        try {
            final byte[] extensionValue = x509CRL.getExtensionValue(Extension.issuingDistributionPoint.getId());
            return extensionValue != null && IssuingDistributionPoint.getInstance(ASN1OctetString.getInstance(extensionValue).getOctets()).isIndirectCRL();
        }
        catch (Exception ex) {
            throw new ExtCRLException("Exception reading IssuingDistributionPoint", ex);
        }
    }
    
    private Set loadCRLEntries() {
        final HashSet<X509CRLEntryObject> set = new HashSet<X509CRLEntryObject>();
        final Enumeration revokedCertificateEnumeration = this.c.getRevokedCertificateEnumeration();
        X500Name instance = null;
        while (revokedCertificateEnumeration.hasMoreElements()) {
            final TBSCertList.CRLEntry crlEntry = revokedCertificateEnumeration.nextElement();
            set.add(new X509CRLEntryObject(crlEntry, this.isIndirect, instance));
            if (this.isIndirect && crlEntry.hasExtensions()) {
                final Extension extension = crlEntry.getExtensions().getExtension(Extension.certificateIssuer);
                if (extension == null) {
                    continue;
                }
                instance = X500Name.getInstance(GeneralNames.getInstance(extension.getParsedValue()).getNames()[0].getName());
            }
        }
        return set;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof X509CRL)) {
            return false;
        }
        if (o instanceof X509CRLObject) {
            final X509CRLObject x509CRLObject = (X509CRLObject)o;
            return (!this.isHashCodeSet || !x509CRLObject.isHashCodeSet || x509CRLObject.hashCodeValue == this.hashCodeValue) && this.c.equals(x509CRLObject.c);
        }
        return super.equals(o);
    }
    
    @Override
    public Set getCriticalExtensionOIDs() {
        return this.getExtensionOIDs(true);
    }
    
    @Override
    public byte[] getEncoded() throws CRLException {
        try {
            return this.c.getEncoded("DER");
        }
        catch (IOException ex) {
            throw new CRLException(ex.toString());
        }
    }
    
    @Override
    public byte[] getExtensionValue(final String s) {
        final Extensions extensions = this.c.getTBSCertList().getExtensions();
        if (extensions != null) {
            final Extension extension = extensions.getExtension(new ASN1ObjectIdentifier(s));
            if (extension != null) {
                try {
                    return extension.getExtnValue().getEncoded();
                }
                catch (Exception ex) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("error parsing ");
                    sb.append(ex.toString());
                    throw new IllegalStateException(sb.toString());
                }
            }
        }
        return null;
    }
    
    @Override
    public Principal getIssuerDN() {
        return new X509Principal(X500Name.getInstance(this.c.getIssuer().toASN1Primitive()));
    }
    
    @Override
    public X500Principal getIssuerX500Principal() {
        try {
            return new X500Principal(this.c.getIssuer().getEncoded());
        }
        catch (IOException ex) {
            throw new IllegalStateException("can't encode issuer DN");
        }
    }
    
    @Override
    public Date getNextUpdate() {
        if (this.c.getNextUpdate() != null) {
            return this.c.getNextUpdate().getDate();
        }
        return null;
    }
    
    @Override
    public Set getNonCriticalExtensionOIDs() {
        return this.getExtensionOIDs(false);
    }
    
    @Override
    public X509CRLEntry getRevokedCertificate(final BigInteger bigInteger) {
        final Enumeration revokedCertificateEnumeration = this.c.getRevokedCertificateEnumeration();
        X500Name instance = null;
        while (revokedCertificateEnumeration.hasMoreElements()) {
            final TBSCertList.CRLEntry crlEntry = revokedCertificateEnumeration.nextElement();
            if (bigInteger.equals(crlEntry.getUserCertificate().getValue())) {
                return new X509CRLEntryObject(crlEntry, this.isIndirect, instance);
            }
            if (!this.isIndirect || !crlEntry.hasExtensions()) {
                continue;
            }
            final Extension extension = crlEntry.getExtensions().getExtension(Extension.certificateIssuer);
            if (extension == null) {
                continue;
            }
            instance = X500Name.getInstance(GeneralNames.getInstance(extension.getParsedValue()).getNames()[0].getName());
        }
        return null;
    }
    
    @Override
    public Set getRevokedCertificates() {
        final Set loadCRLEntries = this.loadCRLEntries();
        if (!loadCRLEntries.isEmpty()) {
            return Collections.unmodifiableSet((Set<?>)loadCRLEntries);
        }
        return null;
    }
    
    @Override
    public String getSigAlgName() {
        return this.sigAlgName;
    }
    
    @Override
    public String getSigAlgOID() {
        return this.c.getSignatureAlgorithm().getAlgorithm().getId();
    }
    
    @Override
    public byte[] getSigAlgParams() {
        final byte[] sigAlgParams = this.sigAlgParams;
        if (sigAlgParams != null) {
            final int length = sigAlgParams.length;
            final byte[] array = new byte[length];
            System.arraycopy(sigAlgParams, 0, array, 0, length);
            return array;
        }
        return null;
    }
    
    @Override
    public byte[] getSignature() {
        return this.c.getSignature().getOctets();
    }
    
    @Override
    public byte[] getTBSCertList() throws CRLException {
        try {
            return this.c.getTBSCertList().getEncoded("DER");
        }
        catch (IOException ex) {
            throw new CRLException(ex.toString());
        }
    }
    
    @Override
    public Date getThisUpdate() {
        return this.c.getThisUpdate().getDate();
    }
    
    @Override
    public int getVersion() {
        return this.c.getVersionNumber();
    }
    
    @Override
    public boolean hasUnsupportedCriticalExtension() {
        final Set criticalExtensionOIDs = this.getCriticalExtensionOIDs();
        if (criticalExtensionOIDs == null) {
            return false;
        }
        criticalExtensionOIDs.remove(RFC3280CertPathUtilities.ISSUING_DISTRIBUTION_POINT);
        criticalExtensionOIDs.remove(RFC3280CertPathUtilities.DELTA_CRL_INDICATOR);
        return criticalExtensionOIDs.isEmpty() ^ true;
    }
    
    @Override
    public int hashCode() {
        if (!this.isHashCodeSet) {
            this.isHashCodeSet = true;
            this.hashCodeValue = super.hashCode();
        }
        return this.hashCodeValue;
    }
    
    @Override
    public boolean isRevoked(final Certificate certificate) {
        if (certificate.getType().equals("X.509")) {
            final Enumeration revokedCertificateEnumeration = this.c.getRevokedCertificateEnumeration();
            X500Name issuer = this.c.getIssuer();
            if (revokedCertificateEnumeration != null) {
                final X509Certificate x509Certificate = (X509Certificate)certificate;
                final BigInteger serialNumber = x509Certificate.getSerialNumber();
                while (revokedCertificateEnumeration.hasMoreElements()) {
                    final TBSCertList.CRLEntry instance = TBSCertList.CRLEntry.getInstance(revokedCertificateEnumeration.nextElement());
                    X500Name instance2 = issuer;
                    if (this.isIndirect) {
                        instance2 = issuer;
                        if (instance.hasExtensions()) {
                            final Extension extension = instance.getExtensions().getExtension(Extension.certificateIssuer);
                            instance2 = issuer;
                            if (extension != null) {
                                instance2 = X500Name.getInstance(GeneralNames.getInstance(extension.getParsedValue()).getNames()[0].getName());
                            }
                        }
                    }
                    issuer = instance2;
                    if (instance.getUserCertificate().getValue().equals(serialNumber)) {
                        Label_0180: {
                            if (certificate instanceof X509Certificate) {
                                final X500Name x500Name = X500Name.getInstance(x509Certificate.getIssuerX500Principal().getEncoded());
                                break Label_0180;
                            }
                            try {
                                final X500Name x500Name = org.spongycastle.asn1.x509.Certificate.getInstance(certificate.getEncoded()).getIssuer();
                                return instance2.equals(x500Name);
                            }
                            catch (CertificateEncodingException ex) {
                                throw new RuntimeException("Cannot process certificate");
                            }
                        }
                        break;
                    }
                }
            }
            return false;
        }
        throw new RuntimeException("X.509 CRL used with non X.509 Cert");
    }
    
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        final String lineSeparator = Strings.lineSeparator();
        sb.append("              Version: ");
        sb.append(this.getVersion());
        sb.append(lineSeparator);
        sb.append("             IssuerDN: ");
        sb.append(this.getIssuerDN());
        sb.append(lineSeparator);
        sb.append("          This update: ");
        sb.append(this.getThisUpdate());
        sb.append(lineSeparator);
        sb.append("          Next update: ");
        sb.append(this.getNextUpdate());
        sb.append(lineSeparator);
        sb.append("  Signature Algorithm: ");
        sb.append(this.getSigAlgName());
        sb.append(lineSeparator);
        final byte[] signature = this.getSignature();
        sb.append("            Signature: ");
        sb.append(new String(Hex.encode(signature, 0, 20)));
        sb.append(lineSeparator);
        for (int i = 20; i < signature.length; i += 20) {
            String s;
            if (i < signature.length - 20) {
                sb.append("                       ");
                s = new String(Hex.encode(signature, i, 20));
            }
            else {
                sb.append("                       ");
                s = new String(Hex.encode(signature, i, signature.length - i));
            }
            sb.append(s);
            sb.append(lineSeparator);
        }
        final Extensions extensions = this.c.getTBSCertList().getExtensions();
        Label_0648: {
            if (extensions != null) {
                final Enumeration oids = extensions.oids();
                while (true) {
                    Label_0316: {
                        String s2 = null;
                        Label_0310: {
                            if (oids.hasMoreElements()) {
                                s2 = "           Extensions: ";
                                break Label_0310;
                            }
                            while (oids.hasMoreElements()) {
                                final ASN1ObjectIdentifier asn1ObjectIdentifier = oids.nextElement();
                                final Extension extension = extensions.getExtension(asn1ObjectIdentifier);
                                if (extension.getExtnValue() != null) {
                                    final ASN1InputStream asn1InputStream = new ASN1InputStream(extension.getExtnValue().getOctets());
                                    sb.append("                       critical(");
                                    sb.append(extension.isCritical());
                                    sb.append(") ");
                                    try {
                                        if (asn1ObjectIdentifier.equals(Extension.cRLNumber)) {
                                            sb.append(new CRLNumber(ASN1Integer.getInstance(asn1InputStream.readObject()).getPositiveValue()));
                                        }
                                        else if (asn1ObjectIdentifier.equals(Extension.deltaCRLIndicator)) {
                                            final StringBuilder sb2 = new StringBuilder();
                                            sb2.append("Base CRL: ");
                                            sb2.append(new CRLNumber(ASN1Integer.getInstance(asn1InputStream.readObject()).getPositiveValue()));
                                            sb.append(sb2.toString());
                                        }
                                        else if (asn1ObjectIdentifier.equals(Extension.issuingDistributionPoint)) {
                                            sb.append(IssuingDistributionPoint.getInstance(asn1InputStream.readObject()));
                                        }
                                        else if (asn1ObjectIdentifier.equals(Extension.cRLDistributionPoints)) {
                                            sb.append(CRLDistPoint.getInstance(asn1InputStream.readObject()));
                                        }
                                        else if (asn1ObjectIdentifier.equals(Extension.freshestCRL)) {
                                            sb.append(CRLDistPoint.getInstance(asn1InputStream.readObject()));
                                        }
                                        else {
                                            sb.append(asn1ObjectIdentifier.getId());
                                            sb.append(" value = ");
                                            sb.append(ASN1Dump.dumpAsString(asn1InputStream.readObject()));
                                        }
                                        sb.append(lineSeparator);
                                        continue;
                                    }
                                    catch (Exception ex) {
                                        sb.append(asn1ObjectIdentifier.getId());
                                        sb.append(" value = ");
                                        s2 = "*****";
                                        break Label_0310;
                                    }
                                    break;
                                }
                                break Label_0316;
                            }
                            break Label_0648;
                        }
                        sb.append(s2);
                    }
                    sb.append(lineSeparator);
                    continue;
                }
            }
        }
        final Set revokedCertificates = this.getRevokedCertificates();
        if (revokedCertificates != null) {
            final Iterator<Object> iterator = revokedCertificates.iterator();
            while (iterator.hasNext()) {
                sb.append(iterator.next());
                sb.append(lineSeparator);
            }
        }
        return sb.toString();
    }
    
    @Override
    public void verify(final PublicKey publicKey) throws CRLException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException {
        Signature signature;
        try {
            signature = Signature.getInstance(this.getSigAlgName(), "SC");
        }
        catch (Exception ex) {
            signature = Signature.getInstance(this.getSigAlgName());
        }
        this.doVerify(publicKey, signature);
    }
    
    @Override
    public void verify(final PublicKey publicKey, final String s) throws CRLException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException {
        Signature signature;
        if (s != null) {
            signature = Signature.getInstance(this.getSigAlgName(), s);
        }
        else {
            signature = Signature.getInstance(this.getSigAlgName());
        }
        this.doVerify(publicKey, signature);
    }
    
    @Override
    public void verify(final PublicKey publicKey, final Provider provider) throws CRLException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        Signature signature;
        if (provider != null) {
            signature = Signature.getInstance(this.getSigAlgName(), provider);
        }
        else {
            signature = Signature.getInstance(this.getSigAlgName());
        }
        this.doVerify(publicKey, signature);
    }
}
