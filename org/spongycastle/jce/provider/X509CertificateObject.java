package org.spongycastle.jce.provider;

import org.spongycastle.jce.interfaces.*;
import org.spongycastle.jcajce.provider.asymmetric.util.*;
import java.net.*;
import org.spongycastle.asn1.x500.style.*;
import org.spongycastle.asn1.x500.*;
import java.security.cert.*;
import java.util.*;
import org.spongycastle.jce.*;
import javax.security.auth.x500.*;
import java.io.*;
import java.math.*;
import org.spongycastle.util.*;
import org.spongycastle.util.encoders.*;
import org.spongycastle.asn1.x509.*;
import org.spongycastle.asn1.*;
import org.spongycastle.asn1.misc.*;
import org.spongycastle.asn1.util.*;
import java.security.*;

public class X509CertificateObject extends X509Certificate implements PKCS12BagAttributeCarrier
{
    private PKCS12BagAttributeCarrier attrCarrier;
    private BasicConstraints basicConstraints;
    private org.spongycastle.asn1.x509.Certificate c;
    private int hashValue;
    private boolean hashValueSet;
    private boolean[] keyUsage;
    
    public X509CertificateObject(final org.spongycastle.asn1.x509.Certificate c) throws CertificateParsingException {
        while (true) {
            this.attrCarrier = new PKCS12BagAttributeCarrierImpl();
            this.c = c;
            while (true) {
                int n = 0;
                Label_0238: {
                    try {
                        final byte[] extensionBytes = this.getExtensionBytes("2.5.29.19");
                        if (extensionBytes != null) {
                            this.basicConstraints = BasicConstraints.getInstance(ASN1Primitive.fromByteArray(extensionBytes));
                        }
                        try {
                            final byte[] extensionBytes2 = this.getExtensionBytes("2.5.29.15");
                            if (extensionBytes2 != null) {
                                final DERBitString instance = DERBitString.getInstance(ASN1Primitive.fromByteArray(extensionBytes2));
                                final byte[] bytes = instance.getBytes();
                                n = bytes.length * 8 - instance.getPadBits();
                                final int n2 = 9;
                                if (n >= 9) {
                                    break Label_0238;
                                }
                                this.keyUsage = new boolean[n2];
                                for (int i = 0; i != n; ++i) {
                                    this.keyUsage[i] = ((bytes[i / 8] & 128 >>> i % 8) != 0x0);
                                }
                            }
                            else {
                                this.keyUsage = null;
                            }
                            return;
                        }
                        catch (Exception ex) {
                            final StringBuilder sb = new StringBuilder();
                            sb.append("cannot construct KeyUsage: ");
                            sb.append(ex);
                            throw new CertificateParsingException(sb.toString());
                        }
                    }
                    catch (Exception ex2) {
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("cannot construct BasicConstraints: ");
                        sb2.append(ex2);
                        throw new CertificateParsingException(sb2.toString());
                    }
                }
                final int n2 = n;
                continue;
            }
        }
    }
    
    private int calculateHashCode() {
        try {
            final byte[] encoded = this.getEncoded();
            int i = 1;
            int n = 0;
            while (i < encoded.length) {
                n += encoded[i] * i;
                ++i;
            }
            return n;
        }
        catch (CertificateEncodingException ex) {
            return 0;
        }
    }
    
    private void checkSignature(final PublicKey publicKey, final Signature signature) throws CertificateException, NoSuchAlgorithmException, SignatureException, InvalidKeyException {
        if (!this.isAlgIdEqual(this.c.getSignatureAlgorithm(), this.c.getTBSCertificate().getSignature())) {
            throw new CertificateException("signature algorithm in TBS cert not same as outer cert");
        }
        X509SignatureUtil.setSignatureParameters(signature, this.c.getSignatureAlgorithm().getParameters());
        signature.initVerify(publicKey);
        signature.update(this.getTBSCertificate());
        if (signature.verify(this.getSignature())) {
            return;
        }
        throw new SignatureException("certificate does not verify with supplied key");
    }
    
    private static Collection getAlternativeNames(byte[] array) throws CertificateParsingException {
        if (array == null) {
            return null;
        }
    Label_0219_Outer:
        while (true) {
            while (true) {
                Label_0287: {
                    try {
                        final ArrayList<Object> list = new ArrayList<Object>();
                        final Enumeration objects = ASN1Sequence.getInstance(array).getObjects();
                        while (objects.hasMoreElements()) {
                            final GeneralName instance = GeneralName.getInstance(objects.nextElement());
                            final ArrayList<byte[]> list2 = new ArrayList<byte[]>();
                            list2.add(Integers.valueOf(instance.getTagNo()));
                            switch (instance.getTagNo()) {
                                case 8: {
                                    array = (byte[])(Object)ASN1ObjectIdentifier.getInstance(instance.getName()).getId();
                                    break;
                                }
                                case 7: {
                                    array = ASN1OctetString.getInstance(instance.getName()).getOctets();
                                    try {
                                        array = (byte[])(Object)InetAddress.getByAddress(array).getHostAddress();
                                        break;
                                    }
                                    catch (UnknownHostException ex2) {
                                        continue Label_0219_Outer;
                                    }
                                }
                                case 4: {
                                    array = (byte[])(Object)X500Name.getInstance(RFC4519Style.INSTANCE, instance.getName()).toString();
                                    break;
                                }
                                case 1:
                                case 2:
                                case 6: {
                                    array = (byte[])(Object)((ASN1String)instance.getName()).getString();
                                    break;
                                }
                                case 0:
                                case 3:
                                case 5: {
                                    array = instance.getEncoded();
                                    break;
                                }
                                default: {
                                    break Label_0287;
                                }
                            }
                            list2.add(array);
                            list.add(Collections.unmodifiableList((List<?>)list2));
                        }
                        if (list.size() == 0) {
                            return null;
                        }
                        return Collections.unmodifiableCollection((Collection<?>)list);
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Bad tag number: ");
                        GeneralName instance = null;
                        sb.append(instance.getTagNo());
                        throw new IOException(sb.toString());
                    }
                    catch (Exception ex) {
                        throw new CertificateParsingException(ex.getMessage());
                    }
                }
                continue;
            }
        }
    }
    
    private byte[] getExtensionBytes(final String s) {
        final Extensions extensions = this.c.getTBSCertificate().getExtensions();
        if (extensions != null) {
            final Extension extension = extensions.getExtension(new ASN1ObjectIdentifier(s));
            if (extension != null) {
                return extension.getExtnValue().getOctets();
            }
        }
        return null;
    }
    
    private boolean isAlgIdEqual(final AlgorithmIdentifier algorithmIdentifier, final AlgorithmIdentifier algorithmIdentifier2) {
        if (!algorithmIdentifier.getAlgorithm().equals(algorithmIdentifier2.getAlgorithm())) {
            return false;
        }
        if (algorithmIdentifier.getParameters() == null) {
            return algorithmIdentifier2.getParameters() == null || algorithmIdentifier2.getParameters().equals(DERNull.INSTANCE);
        }
        if (algorithmIdentifier2.getParameters() == null) {
            return algorithmIdentifier.getParameters() == null || algorithmIdentifier.getParameters().equals(DERNull.INSTANCE);
        }
        return algorithmIdentifier.getParameters().equals(algorithmIdentifier2.getParameters());
    }
    
    @Override
    public void checkValidity() throws CertificateExpiredException, CertificateNotYetValidException {
        this.checkValidity(new Date());
    }
    
    @Override
    public void checkValidity(final Date date) throws CertificateExpiredException, CertificateNotYetValidException {
        if (date.getTime() > this.getNotAfter().getTime()) {
            final StringBuilder sb = new StringBuilder();
            sb.append("certificate expired on ");
            sb.append(this.c.getEndDate().getTime());
            throw new CertificateExpiredException(sb.toString());
        }
        if (date.getTime() >= this.getNotBefore().getTime()) {
            return;
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("certificate not valid till ");
        sb2.append(this.c.getStartDate().getTime());
        throw new CertificateNotYetValidException(sb2.toString());
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof Certificate)) {
            return false;
        }
        final Certificate certificate = (Certificate)o;
        try {
            return Arrays.areEqual(this.getEncoded(), certificate.getEncoded());
        }
        catch (CertificateEncodingException ex) {
            return false;
        }
    }
    
    @Override
    public ASN1Encodable getBagAttribute(final ASN1ObjectIdentifier asn1ObjectIdentifier) {
        return this.attrCarrier.getBagAttribute(asn1ObjectIdentifier);
    }
    
    @Override
    public Enumeration getBagAttributeKeys() {
        return this.attrCarrier.getBagAttributeKeys();
    }
    
    @Override
    public int getBasicConstraints() {
        final BasicConstraints basicConstraints = this.basicConstraints;
        if (basicConstraints == null || !basicConstraints.isCA()) {
            return -1;
        }
        if (this.basicConstraints.getPathLenConstraint() == null) {
            return Integer.MAX_VALUE;
        }
        return this.basicConstraints.getPathLenConstraint().intValue();
    }
    
    @Override
    public Set getCriticalExtensionOIDs() {
        if (this.getVersion() == 3) {
            final HashSet<String> set = new HashSet<String>();
            final Extensions extensions = this.c.getTBSCertificate().getExtensions();
            if (extensions != null) {
                final Enumeration oids = extensions.oids();
                while (oids.hasMoreElements()) {
                    final ASN1ObjectIdentifier asn1ObjectIdentifier = oids.nextElement();
                    if (extensions.getExtension(asn1ObjectIdentifier).isCritical()) {
                        set.add(asn1ObjectIdentifier.getId());
                    }
                }
                return set;
            }
        }
        return null;
    }
    
    @Override
    public byte[] getEncoded() throws CertificateEncodingException {
        try {
            return this.c.getEncoded("DER");
        }
        catch (IOException ex) {
            throw new CertificateEncodingException(ex.toString());
        }
    }
    
    @Override
    public List getExtendedKeyUsage() throws CertificateParsingException {
        final byte[] extensionBytes = this.getExtensionBytes("2.5.29.37");
        if (extensionBytes != null) {
            try {
                final ASN1Sequence asn1Sequence = (ASN1Sequence)new ASN1InputStream(extensionBytes).readObject();
                final ArrayList<String> list = new ArrayList<String>();
                for (int i = 0; i != asn1Sequence.size(); ++i) {
                    list.add(((ASN1ObjectIdentifier)asn1Sequence.getObjectAt(i)).getId());
                }
                return Collections.unmodifiableList((List<?>)list);
            }
            catch (Exception ex) {
                throw new CertificateParsingException("error processing extended key usage extension");
            }
        }
        return null;
    }
    
    @Override
    public byte[] getExtensionValue(final String s) {
        final Extensions extensions = this.c.getTBSCertificate().getExtensions();
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
    public Collection getIssuerAlternativeNames() throws CertificateParsingException {
        return getAlternativeNames(this.getExtensionBytes(Extension.issuerAlternativeName.getId()));
    }
    
    @Override
    public Principal getIssuerDN() {
        try {
            return new X509Principal(X500Name.getInstance(this.c.getIssuer().getEncoded()));
        }
        catch (IOException ex) {
            return null;
        }
    }
    
    @Override
    public boolean[] getIssuerUniqueID() {
        final DERBitString issuerUniqueId = this.c.getTBSCertificate().getIssuerUniqueId();
        if (issuerUniqueId != null) {
            final byte[] bytes = issuerUniqueId.getBytes();
            final int n = bytes.length * 8 - issuerUniqueId.getPadBits();
            final boolean[] array = new boolean[n];
            for (int i = 0; i != n; ++i) {
                array[i] = ((bytes[i / 8] & 128 >>> i % 8) != 0x0);
            }
            return array;
        }
        return null;
    }
    
    @Override
    public X500Principal getIssuerX500Principal() {
        try {
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            new ASN1OutputStream(byteArrayOutputStream).writeObject(this.c.getIssuer());
            return new X500Principal(byteArrayOutputStream.toByteArray());
        }
        catch (IOException ex) {
            throw new IllegalStateException("can't encode issuer DN");
        }
    }
    
    @Override
    public boolean[] getKeyUsage() {
        return this.keyUsage;
    }
    
    @Override
    public Set getNonCriticalExtensionOIDs() {
        if (this.getVersion() == 3) {
            final HashSet<String> set = new HashSet<String>();
            final Extensions extensions = this.c.getTBSCertificate().getExtensions();
            if (extensions != null) {
                final Enumeration oids = extensions.oids();
                while (oids.hasMoreElements()) {
                    final ASN1ObjectIdentifier asn1ObjectIdentifier = oids.nextElement();
                    if (!extensions.getExtension(asn1ObjectIdentifier).isCritical()) {
                        set.add(asn1ObjectIdentifier.getId());
                    }
                }
                return set;
            }
        }
        return null;
    }
    
    @Override
    public Date getNotAfter() {
        return this.c.getEndDate().getDate();
    }
    
    @Override
    public Date getNotBefore() {
        return this.c.getStartDate().getDate();
    }
    
    @Override
    public PublicKey getPublicKey() {
        try {
            return BouncyCastleProvider.getPublicKey(this.c.getSubjectPublicKeyInfo());
        }
        catch (IOException ex) {
            return null;
        }
    }
    
    @Override
    public BigInteger getSerialNumber() {
        return this.c.getSerialNumber().getValue();
    }
    
    @Override
    public String getSigAlgName() {
        final Provider provider = Security.getProvider("SC");
        if (provider != null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Alg.Alias.Signature.");
            sb.append(this.getSigAlgOID());
            final String property = provider.getProperty(sb.toString());
            if (property != null) {
                return property;
            }
        }
        final Provider[] providers = Security.getProviders();
        for (int i = 0; i != providers.length; ++i) {
            final Provider provider2 = providers[i];
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Alg.Alias.Signature.");
            sb2.append(this.getSigAlgOID());
            final String property2 = provider2.getProperty(sb2.toString());
            if (property2 != null) {
                return property2;
            }
        }
        return this.getSigAlgOID();
    }
    
    @Override
    public String getSigAlgOID() {
        return this.c.getSignatureAlgorithm().getAlgorithm().getId();
    }
    
    @Override
    public byte[] getSigAlgParams() {
        if (this.c.getSignatureAlgorithm().getParameters() != null) {
            try {
                return this.c.getSignatureAlgorithm().getParameters().toASN1Primitive().getEncoded("DER");
            }
            catch (IOException ex) {}
        }
        return null;
    }
    
    @Override
    public byte[] getSignature() {
        return this.c.getSignature().getOctets();
    }
    
    @Override
    public Collection getSubjectAlternativeNames() throws CertificateParsingException {
        return getAlternativeNames(this.getExtensionBytes(Extension.subjectAlternativeName.getId()));
    }
    
    @Override
    public Principal getSubjectDN() {
        return new X509Principal(X500Name.getInstance(this.c.getSubject().toASN1Primitive()));
    }
    
    @Override
    public boolean[] getSubjectUniqueID() {
        final DERBitString subjectUniqueId = this.c.getTBSCertificate().getSubjectUniqueId();
        if (subjectUniqueId != null) {
            final byte[] bytes = subjectUniqueId.getBytes();
            final int n = bytes.length * 8 - subjectUniqueId.getPadBits();
            final boolean[] array = new boolean[n];
            for (int i = 0; i != n; ++i) {
                array[i] = ((bytes[i / 8] & 128 >>> i % 8) != 0x0);
            }
            return array;
        }
        return null;
    }
    
    @Override
    public X500Principal getSubjectX500Principal() {
        try {
            final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            new ASN1OutputStream(byteArrayOutputStream).writeObject(this.c.getSubject());
            return new X500Principal(byteArrayOutputStream.toByteArray());
        }
        catch (IOException ex) {
            throw new IllegalStateException("can't encode issuer DN");
        }
    }
    
    @Override
    public byte[] getTBSCertificate() throws CertificateEncodingException {
        try {
            return this.c.getTBSCertificate().getEncoded("DER");
        }
        catch (IOException ex) {
            throw new CertificateEncodingException(ex.toString());
        }
    }
    
    @Override
    public int getVersion() {
        return this.c.getVersionNumber();
    }
    
    @Override
    public boolean hasUnsupportedCriticalExtension() {
        if (this.getVersion() == 3) {
            final Extensions extensions = this.c.getTBSCertificate().getExtensions();
            if (extensions != null) {
                final Enumeration oids = extensions.oids();
                while (oids.hasMoreElements()) {
                    final ASN1ObjectIdentifier asn1ObjectIdentifier = oids.nextElement();
                    final String id = asn1ObjectIdentifier.getId();
                    if (!id.equals(RFC3280CertPathUtilities.KEY_USAGE) && !id.equals(RFC3280CertPathUtilities.CERTIFICATE_POLICIES) && !id.equals(RFC3280CertPathUtilities.POLICY_MAPPINGS) && !id.equals(RFC3280CertPathUtilities.INHIBIT_ANY_POLICY) && !id.equals(RFC3280CertPathUtilities.CRL_DISTRIBUTION_POINTS) && !id.equals(RFC3280CertPathUtilities.ISSUING_DISTRIBUTION_POINT) && !id.equals(RFC3280CertPathUtilities.DELTA_CRL_INDICATOR) && !id.equals(RFC3280CertPathUtilities.POLICY_CONSTRAINTS) && !id.equals(RFC3280CertPathUtilities.BASIC_CONSTRAINTS) && !id.equals(RFC3280CertPathUtilities.SUBJECT_ALTERNATIVE_NAME)) {
                        if (id.equals(RFC3280CertPathUtilities.NAME_CONSTRAINTS)) {
                            continue;
                        }
                        if (extensions.getExtension(asn1ObjectIdentifier).isCritical()) {
                            return true;
                        }
                        continue;
                    }
                }
            }
        }
        return false;
    }
    
    @Override
    public int hashCode() {
        synchronized (this) {
            if (!this.hashValueSet) {
                this.hashValue = this.calculateHashCode();
                this.hashValueSet = true;
            }
            return this.hashValue;
        }
    }
    
    @Override
    public void setBagAttribute(final ASN1ObjectIdentifier asn1ObjectIdentifier, final ASN1Encodable asn1Encodable) {
        this.attrCarrier.setBagAttribute(asn1ObjectIdentifier, asn1Encodable);
    }
    
    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        final String lineSeparator = Strings.lineSeparator();
        sb.append("  [0]         Version: ");
        sb.append(this.getVersion());
        sb.append(lineSeparator);
        sb.append("         SerialNumber: ");
        sb.append(this.getSerialNumber());
        sb.append(lineSeparator);
        sb.append("             IssuerDN: ");
        sb.append(this.getIssuerDN());
        sb.append(lineSeparator);
        sb.append("           Start Date: ");
        sb.append(this.getNotBefore());
        sb.append(lineSeparator);
        sb.append("           Final Date: ");
        sb.append(this.getNotAfter());
        sb.append(lineSeparator);
        sb.append("            SubjectDN: ");
        sb.append(this.getSubjectDN());
        sb.append(lineSeparator);
        sb.append("           Public Key: ");
        sb.append(this.getPublicKey());
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
        final Extensions extensions = this.c.getTBSCertificate().getExtensions();
        if (extensions != null) {
            final Enumeration oids = extensions.oids();
            if (oids.hasMoreElements()) {
                sb.append("       Extensions: \n");
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
                        if (asn1ObjectIdentifier.equals(Extension.basicConstraints)) {
                            sb.append(BasicConstraints.getInstance(asn1InputStream.readObject()));
                        }
                        else if (asn1ObjectIdentifier.equals(Extension.keyUsage)) {
                            sb.append(KeyUsage.getInstance(asn1InputStream.readObject()));
                        }
                        else if (asn1ObjectIdentifier.equals(MiscObjectIdentifiers.netscapeCertType)) {
                            sb.append(new NetscapeCertType((DERBitString)asn1InputStream.readObject()));
                        }
                        else if (asn1ObjectIdentifier.equals(MiscObjectIdentifiers.netscapeRevocationURL)) {
                            sb.append(new NetscapeRevocationURL((DERIA5String)asn1InputStream.readObject()));
                        }
                        else if (asn1ObjectIdentifier.equals(MiscObjectIdentifiers.verisignCzagExtension)) {
                            sb.append(new VerisignCzagExtension((DERIA5String)asn1InputStream.readObject()));
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
                        sb.append("*****");
                    }
                }
                sb.append(lineSeparator);
            }
        }
        return sb.toString();
    }
    
    @Override
    public final void verify(final PublicKey publicKey) throws CertificateException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException {
        final String signatureName = X509SignatureUtil.getSignatureName(this.c.getSignatureAlgorithm());
        Signature signature;
        try {
            signature = Signature.getInstance(signatureName, "SC");
        }
        catch (Exception ex) {
            signature = Signature.getInstance(signatureName);
        }
        this.checkSignature(publicKey, signature);
    }
    
    @Override
    public final void verify(final PublicKey publicKey, final String s) throws CertificateException, NoSuchAlgorithmException, InvalidKeyException, NoSuchProviderException, SignatureException {
        final String signatureName = X509SignatureUtil.getSignatureName(this.c.getSignatureAlgorithm());
        Signature signature;
        if (s != null) {
            signature = Signature.getInstance(signatureName, s);
        }
        else {
            signature = Signature.getInstance(signatureName);
        }
        this.checkSignature(publicKey, signature);
    }
    
    @Override
    public final void verify(final PublicKey publicKey, final Provider provider) throws CertificateException, NoSuchAlgorithmException, InvalidKeyException, SignatureException {
        final String signatureName = X509SignatureUtil.getSignatureName(this.c.getSignatureAlgorithm());
        Signature signature;
        if (provider != null) {
            signature = Signature.getInstance(signatureName, provider);
        }
        else {
            signature = Signature.getInstance(signatureName);
        }
        this.checkSignature(publicKey, signature);
    }
}
