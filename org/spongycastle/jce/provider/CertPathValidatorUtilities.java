package org.spongycastle.jce.provider;

import org.spongycastle.x509.*;
import org.spongycastle.asn1.x500.style.*;
import org.spongycastle.util.*;
import org.spongycastle.asn1.x500.*;
import org.spongycastle.jce.exception.*;
import javax.security.auth.x500.*;
import org.spongycastle.jcajce.*;
import java.math.*;
import org.spongycastle.jcajce.util.*;
import java.security.spec.*;
import java.security.interfaces.*;
import java.io.*;
import java.util.*;
import org.spongycastle.asn1.isismtt.*;
import java.text.*;
import org.spongycastle.asn1.x509.*;
import java.security.cert.*;
import org.spongycastle.asn1.*;
import java.security.*;

class CertPathValidatorUtilities
{
    protected static final String ANY_POLICY = "2.5.29.32.0";
    protected static final String AUTHORITY_KEY_IDENTIFIER;
    protected static final String BASIC_CONSTRAINTS;
    protected static final String CERTIFICATE_POLICIES;
    protected static final String CRL_DISTRIBUTION_POINTS;
    protected static final String CRL_NUMBER;
    protected static final int CRL_SIGN = 6;
    protected static final PKIXCRLUtil CRL_UTIL;
    protected static final String DELTA_CRL_INDICATOR;
    protected static final String FRESHEST_CRL;
    protected static final String INHIBIT_ANY_POLICY;
    protected static final String ISSUING_DISTRIBUTION_POINT;
    protected static final int KEY_CERT_SIGN = 5;
    protected static final String KEY_USAGE;
    protected static final String NAME_CONSTRAINTS;
    protected static final String POLICY_CONSTRAINTS;
    protected static final String POLICY_MAPPINGS;
    protected static final String SUBJECT_ALTERNATIVE_NAME;
    protected static final String[] crlReasons;
    
    static {
        CRL_UTIL = new PKIXCRLUtil();
        CERTIFICATE_POLICIES = Extension.certificatePolicies.getId();
        BASIC_CONSTRAINTS = Extension.basicConstraints.getId();
        POLICY_MAPPINGS = Extension.policyMappings.getId();
        SUBJECT_ALTERNATIVE_NAME = Extension.subjectAlternativeName.getId();
        NAME_CONSTRAINTS = Extension.nameConstraints.getId();
        KEY_USAGE = Extension.keyUsage.getId();
        INHIBIT_ANY_POLICY = Extension.inhibitAnyPolicy.getId();
        ISSUING_DISTRIBUTION_POINT = Extension.issuingDistributionPoint.getId();
        DELTA_CRL_INDICATOR = Extension.deltaCRLIndicator.getId();
        POLICY_CONSTRAINTS = Extension.policyConstraints.getId();
        FRESHEST_CRL = Extension.freshestCRL.getId();
        CRL_DISTRIBUTION_POINTS = Extension.cRLDistributionPoints.getId();
        AUTHORITY_KEY_IDENTIFIER = Extension.authorityKeyIdentifier.getId();
        CRL_NUMBER = Extension.cRLNumber.getId();
        crlReasons = new String[] { "unspecified", "keyCompromise", "cACompromise", "affiliationChanged", "superseded", "cessationOfOperation", "certificateHold", "unknown", "removeFromCRL", "privilegeWithdrawn", "aACompromise" };
    }
    
    static void checkCRLsNotEmpty(final Set set, final Object o) throws AnnotatedException {
        if (!set.isEmpty()) {
            return;
        }
        if (o instanceof X509AttributeCertificate) {
            final X509AttributeCertificate x509AttributeCertificate = (X509AttributeCertificate)o;
            final StringBuilder sb = new StringBuilder();
            sb.append("No CRLs found for issuer \"");
            sb.append(x509AttributeCertificate.getIssuer().getPrincipals()[0]);
            sb.append("\"");
            throw new AnnotatedException(sb.toString());
        }
        final X509Certificate x509Certificate = (X509Certificate)o;
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("No CRLs found for issuer \"");
        sb2.append(RFC4519Style.INSTANCE.toString(PrincipalUtils.getIssuerPrincipal(x509Certificate)));
        sb2.append("\"");
        throw new AnnotatedException(sb2.toString());
    }
    
    protected static Collection findCertificates(final PKIXCertStoreSelector pkixCertStoreSelector, final List list) throws AnnotatedException {
        final LinkedHashSet set = new LinkedHashSet();
        for (final Store<? extends E> next : list) {
            if (next instanceof Store) {
                final Store<? extends E> store = next;
                try {
                    set.addAll(store.getMatches(pkixCertStoreSelector));
                    continue;
                }
                catch (StoreException ex) {
                    throw new AnnotatedException("Problem while picking certificates from X.509 store.", ex);
                }
            }
            final CertStore certStore = (CertStore)next;
            try {
                set.addAll(PKIXCertStoreSelector.getCertificates(pkixCertStoreSelector, certStore));
                continue;
            }
            catch (CertStoreException ex2) {
                throw new AnnotatedException("Problem while picking certificates from certificate store.", ex2);
            }
            break;
        }
        return set;
    }
    
    static Collection findIssuerCerts(final X509Certificate x509Certificate, final List<CertStore> list, final List<PKIXCertStore> list2) throws AnnotatedException {
        final X509CertSelector x509CertSelector = new X509CertSelector();
        try {
            x509CertSelector.setSubject(PrincipalUtils.getIssuerPrincipal(x509Certificate).getEncoded());
            try {
                final byte[] extensionValue = x509Certificate.getExtensionValue(CertPathValidatorUtilities.AUTHORITY_KEY_IDENTIFIER);
                if (extensionValue != null) {
                    final byte[] keyIdentifier = AuthorityKeyIdentifier.getInstance(ASN1OctetString.getInstance(extensionValue).getOctets()).getKeyIdentifier();
                    if (keyIdentifier != null) {
                        x509CertSelector.setSubjectKeyIdentifier(new DEROctetString(keyIdentifier).getEncoded());
                    }
                }
            }
            catch (Exception ex3) {}
            final PKIXCertStoreSelector<? extends Certificate> build = new PKIXCertStoreSelector.Builder(x509CertSelector).build();
            final LinkedHashSet<X509Certificate> set = new LinkedHashSet<X509Certificate>();
            try {
                final ArrayList<X509Certificate> list3 = new ArrayList<X509Certificate>();
                list3.addAll((Collection<?>)findCertificates(build, list));
                list3.addAll((Collection<?>)findCertificates(build, list2));
                final Iterator<Object> iterator = list3.iterator();
                while (iterator.hasNext()) {
                    set.add(iterator.next());
                }
                return set;
            }
            catch (AnnotatedException ex) {
                throw new AnnotatedException("Issuer certificate cannot be searched.", ex);
            }
        }
        catch (IOException ex2) {
            throw new AnnotatedException("Subject criteria for certificate selector to find issuer certificate could not be set.", ex2);
        }
    }
    
    protected static TrustAnchor findTrustAnchor(final X509Certificate x509Certificate, final Set set) throws AnnotatedException {
        return findTrustAnchor(x509Certificate, set, null);
    }
    
    protected static TrustAnchor findTrustAnchor(final X509Certificate x509Certificate, Set o, final String s) throws AnnotatedException {
        final X509CertSelector x509CertSelector = new X509CertSelector();
        final X500Name encodedIssuerPrincipal = PrincipalUtils.getEncodedIssuerPrincipal(x509Certificate);
        try {
            x509CertSelector.setSubject(encodedIssuerPrincipal.getEncoded());
            final Iterator<TrustAnchor> iterator = ((Set<TrustAnchor>)o).iterator();
            final TrustAnchor trustAnchor = null;
            final TrustAnchor trustAnchor2;
            o = (trustAnchor2 = trustAnchor);
            Object o2 = trustAnchor;
            while (iterator.hasNext() && o2 == null) {
                TrustAnchor trustAnchor3 = iterator.next();
                PublicKey publicKey = null;
                Label_0153: {
                    if (trustAnchor3.getTrustedCert() != null) {
                        if (x509CertSelector.match(trustAnchor3.getTrustedCert())) {
                            publicKey = trustAnchor3.getTrustedCert().getPublicKey();
                            break Label_0153;
                        }
                    }
                    else if (trustAnchor3.getCAName() != null && trustAnchor3.getCAPublicKey() != null) {
                        try {
                            if (encodedIssuerPrincipal.equals(PrincipalUtils.getCA(trustAnchor3))) {
                                publicKey = trustAnchor3.getCAPublicKey();
                                break Label_0153;
                            }
                        }
                        catch (IllegalArgumentException ex2) {}
                    }
                    trustAnchor3 = null;
                    publicKey = (PublicKey)o;
                }
                o2 = trustAnchor3;
                o = publicKey;
                if (publicKey != null) {
                    try {
                        verifyX509Certificate(x509Certificate, publicKey, s);
                        o2 = trustAnchor3;
                        o = publicKey;
                    }
                    catch (Exception trustAnchor2) {
                        o2 = (o = null);
                    }
                }
            }
            if (o2 != null) {
                return (TrustAnchor)o2;
            }
            if (trustAnchor2 == null) {
                return (TrustAnchor)o2;
            }
            throw new AnnotatedException("TrustAnchor found but certificate validation failed.", (Throwable)trustAnchor2);
        }
        catch (IOException ex) {
            throw new AnnotatedException("Cannot set subject search criteria for trust anchor.", ex);
        }
    }
    
    static List<PKIXCertStore> getAdditionalStoresFromAltNames(final byte[] array, final Map<GeneralName, PKIXCertStore> map) throws CertificateParsingException {
        if (array != null) {
            final GeneralName[] names = GeneralNames.getInstance(ASN1OctetString.getInstance(array).getOctets()).getNames();
            final ArrayList<PKIXCertStore> list = new ArrayList<PKIXCertStore>();
            for (int i = 0; i != names.length; ++i) {
                final PKIXCertStore pkixCertStore = map.get(names[i]);
                if (pkixCertStore != null) {
                    list.add(pkixCertStore);
                }
            }
            return (List<PKIXCertStore>)list;
        }
        return (List<PKIXCertStore>)Collections.EMPTY_LIST;
    }
    
    static List<PKIXCRLStore> getAdditionalStoresFromCRLDistributionPoint(final CRLDistPoint crlDistPoint, final Map<GeneralName, PKIXCRLStore> map) throws AnnotatedException {
        if (crlDistPoint != null) {
            try {
                final DistributionPoint[] distributionPoints = crlDistPoint.getDistributionPoints();
                final ArrayList<PKIXCRLStore> list = new ArrayList<PKIXCRLStore>();
                for (int i = 0; i < distributionPoints.length; ++i) {
                    final DistributionPointName distributionPoint = distributionPoints[i].getDistributionPoint();
                    if (distributionPoint != null && distributionPoint.getType() == 0) {
                        final GeneralName[] names = GeneralNames.getInstance(distributionPoint.getName()).getNames();
                        for (int j = 0; j < names.length; ++j) {
                            final PKIXCRLStore pkixcrlStore = map.get(names[j]);
                            if (pkixcrlStore != null) {
                                list.add(pkixcrlStore);
                            }
                        }
                    }
                }
                return (List<PKIXCRLStore>)list;
            }
            catch (Exception ex) {
                throw new AnnotatedException("Distribution points could not be read.", ex);
            }
        }
        return (List<PKIXCRLStore>)Collections.EMPTY_LIST;
    }
    
    protected static AlgorithmIdentifier getAlgorithmIdentifier(final PublicKey publicKey) throws CertPathValidatorException {
        try {
            return SubjectPublicKeyInfo.getInstance(new ASN1InputStream(publicKey.getEncoded()).readObject()).getAlgorithm();
        }
        catch (Exception ex) {
            throw new ExtCertPathValidatorException("Subject public key cannot be decoded.", ex);
        }
    }
    
    protected static void getCRLIssuersFromDistributionPoint(final DistributionPoint distributionPoint, final Collection collection, final X509CRLSelector x509CRLSelector) throws AnnotatedException {
        final ArrayList<X500Name> list = new ArrayList<X500Name>();
        if (distributionPoint.getCRLIssuer() != null) {
            final GeneralName[] names = distributionPoint.getCRLIssuer().getNames();
            for (int i = 0; i < names.length; ++i) {
                if (names[i].getTagNo() == 4) {
                    try {
                        list.add(X500Name.getInstance(names[i].getName().toASN1Primitive().getEncoded()));
                    }
                    catch (IOException ex) {
                        throw new AnnotatedException("CRL issuer information from distribution point cannot be decoded.", ex);
                    }
                }
            }
        }
        else {
            if (distributionPoint.getDistributionPoint() == null) {
                throw new AnnotatedException("CRL issuer is omitted from distribution point but no distributionPoint field present.");
            }
            final Iterator<X500Name> iterator = collection.iterator();
            while (iterator.hasNext()) {
                list.add(iterator.next());
            }
        }
        final Iterator<Object> iterator2 = list.iterator();
        while (iterator2.hasNext()) {
            try {
                x509CRLSelector.addIssuerName(iterator2.next().getEncoded());
                continue;
            }
            catch (IOException ex2) {
                throw new AnnotatedException("Cannot decode CRL issuer information.", ex2);
            }
            break;
        }
    }
    
    protected static void getCertStatus(final Date date, final X509CRL x509CRL, final Object o, final CertStatus certStatus) throws AnnotatedException {
        try {
            X509CRLEntry revokedCertificate2;
            if (X509CRLObject.isIndirectCRL(x509CRL)) {
                final X509CRLEntry revokedCertificate = x509CRL.getRevokedCertificate(getSerialNumber(o));
                if (revokedCertificate == null) {
                    return;
                }
                final X500Principal certificateIssuer = revokedCertificate.getCertificateIssuer();
                X500Name x500Name;
                if (certificateIssuer == null) {
                    x500Name = PrincipalUtils.getIssuerPrincipal(x509CRL);
                }
                else {
                    x500Name = X500Name.getInstance(certificateIssuer.getEncoded());
                }
                revokedCertificate2 = revokedCertificate;
                if (!PrincipalUtils.getEncodedIssuerPrincipal(o).equals(x500Name)) {
                    return;
                }
            }
            else {
                if (!PrincipalUtils.getEncodedIssuerPrincipal(o).equals(PrincipalUtils.getIssuerPrincipal(x509CRL))) {
                    return;
                }
                if ((revokedCertificate2 = x509CRL.getRevokedCertificate(getSerialNumber(o))) == null) {
                    return;
                }
            }
            ASN1Enumerated instance = null;
            if (revokedCertificate2.hasExtensions()) {
                try {
                    instance = ASN1Enumerated.getInstance(getExtensionValue(revokedCertificate2, Extension.reasonCode.getId()));
                }
                catch (Exception ex) {
                    throw new AnnotatedException("Reason code CRL entry extension could not be decoded.", ex);
                }
            }
            if (date.getTime() >= revokedCertificate2.getRevocationDate().getTime() || instance == null || instance.getValue().intValue() == 0 || instance.getValue().intValue() == 1 || instance.getValue().intValue() == 2 || instance.getValue().intValue() == 8) {
                int intValue;
                if (instance != null) {
                    intValue = instance.getValue().intValue();
                }
                else {
                    intValue = 0;
                }
                certStatus.setCertStatus(intValue);
                certStatus.setRevocationDate(revokedCertificate2.getRevocationDate());
            }
        }
        catch (CRLException ex2) {
            throw new AnnotatedException("Failed check for indirect CRL.", ex2);
        }
    }
    
    protected static Set getCompleteCRLs(final DistributionPoint distributionPoint, final Object o, Date date, final PKIXExtendedParameters pkixExtendedParameters) throws AnnotatedException {
        final X509CRLSelector x509CRLSelector = new X509CRLSelector();
        try {
            final HashSet<X500Name> set = new HashSet<X500Name>();
            set.add(PrincipalUtils.getEncodedIssuerPrincipal(o));
            getCRLIssuersFromDistributionPoint(distributionPoint, set, x509CRLSelector);
            if (o instanceof X509Certificate) {
                x509CRLSelector.setCertificateChecking((X509Certificate)o);
            }
            final PKIXCRLStoreSelector<? extends CRL> build = new PKIXCRLStoreSelector.Builder(x509CRLSelector).setCompleteCRLEnabled(true).build();
            if (pkixExtendedParameters.getDate() != null) {
                date = pkixExtendedParameters.getDate();
            }
            final Set crLs = CertPathValidatorUtilities.CRL_UTIL.findCRLs(build, date, pkixExtendedParameters.getCertStores(), pkixExtendedParameters.getCRLStores());
            checkCRLsNotEmpty(crLs, o);
            return crLs;
        }
        catch (AnnotatedException ex) {
            throw new AnnotatedException("Could not get issuer information from distribution point.", ex);
        }
    }
    
    protected static Set getDeltaCRLs(final Date date, final X509CRL x509CRL, final List<CertStore> list, final List<PKIXCRLStore> list2) throws AnnotatedException {
        final X509CRLSelector x509CRLSelector = new X509CRLSelector();
        try {
            x509CRLSelector.addIssuerName(PrincipalUtils.getIssuerPrincipal(x509CRL).getEncoded());
            try {
                final ASN1Primitive extensionValue = getExtensionValue(x509CRL, CertPathValidatorUtilities.CRL_NUMBER);
                final BigInteger bigInteger = null;
                BigInteger positiveValue;
                if (extensionValue != null) {
                    positiveValue = ASN1Integer.getInstance(extensionValue).getPositiveValue();
                }
                else {
                    positiveValue = null;
                }
                try {
                    final byte[] extensionValue2 = x509CRL.getExtensionValue(CertPathValidatorUtilities.ISSUING_DISTRIBUTION_POINT);
                    BigInteger add;
                    if (positiveValue == null) {
                        add = bigInteger;
                    }
                    else {
                        add = positiveValue.add(BigInteger.valueOf(1L));
                    }
                    x509CRLSelector.setMinCRLNumber(add);
                    final PKIXCRLStoreSelector.Builder builder = new PKIXCRLStoreSelector.Builder(x509CRLSelector);
                    builder.setIssuingDistributionPoint(extensionValue2);
                    builder.setIssuingDistributionPointEnabled(true);
                    builder.setMaxBaseCRLNumber(positiveValue);
                    final Set crLs = CertPathValidatorUtilities.CRL_UTIL.findCRLs(builder.build(), date, list, list2);
                    final HashSet<X509CRL> set = new HashSet<X509CRL>();
                    for (final X509CRL x509CRL2 : crLs) {
                        if (isDeltaCRL(x509CRL2)) {
                            set.add(x509CRL2);
                        }
                    }
                    return set;
                }
                catch (Exception ex) {
                    throw new AnnotatedException("Issuing distribution point extension value could not be read.", ex);
                }
            }
            catch (Exception ex2) {
                throw new AnnotatedException("CRL number extension could not be extracted from CRL.", ex2);
            }
        }
        catch (IOException ex3) {
            throw new AnnotatedException("Cannot extract issuer from CRL.", ex3);
        }
    }
    
    protected static ASN1Primitive getExtensionValue(final X509Extension x509Extension, final String s) throws AnnotatedException {
        final byte[] extensionValue = x509Extension.getExtensionValue(s);
        if (extensionValue == null) {
            return null;
        }
        return getObject(s, extensionValue);
    }
    
    protected static PublicKey getNextWorkingKey(final List list, int n, final JcaJceHelper jcaJceHelper) throws CertPathValidatorException {
        final PublicKey publicKey = list.get(n).getPublicKey();
        if (!(publicKey instanceof DSAPublicKey)) {
            return publicKey;
        }
        final DSAPublicKey dsaPublicKey = (DSAPublicKey)publicKey;
        if (dsaPublicKey.getParams() != null) {
            return dsaPublicKey;
        }
        while (true) {
            ++n;
            if (n >= list.size()) {
                throw new CertPathValidatorException("DSA parameters cannot be inherited from previous certificate.");
            }
            final PublicKey publicKey2 = list.get(n).getPublicKey();
            if (!(publicKey2 instanceof DSAPublicKey)) {
                break;
            }
            final DSAPublicKey dsaPublicKey2 = (DSAPublicKey)publicKey2;
            if (dsaPublicKey2.getParams() == null) {
                continue;
            }
            final DSAParams params = dsaPublicKey2.getParams();
            final DSAPublicKeySpec dsaPublicKeySpec = new DSAPublicKeySpec(dsaPublicKey.getY(), params.getP(), params.getQ(), params.getG());
            try {
                return jcaJceHelper.createKeyFactory("DSA").generatePublic(dsaPublicKeySpec);
            }
            catch (Exception ex) {
                throw new RuntimeException(ex.getMessage());
            }
            break;
        }
        throw new CertPathValidatorException("DSA parameters cannot be inherited from previous certificate.");
    }
    
    private static ASN1Primitive getObject(final String s, final byte[] array) throws AnnotatedException {
        try {
            return new ASN1InputStream(((ASN1OctetString)new ASN1InputStream(array).readObject()).getOctets()).readObject();
        }
        catch (Exception ex) {
            final StringBuilder sb = new StringBuilder();
            sb.append("exception processing extension ");
            sb.append(s);
            throw new AnnotatedException(sb.toString(), ex);
        }
    }
    
    protected static final Set getQualifierSet(final ASN1Sequence asn1Sequence) throws CertPathValidatorException {
        final HashSet<PolicyQualifierInfo> set = new HashSet<PolicyQualifierInfo>();
        if (asn1Sequence == null) {
            return set;
        }
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final ASN1OutputStream asn1OutputStream = new ASN1OutputStream(byteArrayOutputStream);
        final Enumeration objects = asn1Sequence.getObjects();
        while (objects.hasMoreElements()) {
            try {
                asn1OutputStream.writeObject(objects.nextElement());
                set.add(new PolicyQualifierInfo(byteArrayOutputStream.toByteArray()));
                byteArrayOutputStream.reset();
                continue;
            }
            catch (IOException ex) {
                throw new ExtCertPathValidatorException("Policy qualifier info cannot be decoded.", ex);
            }
            break;
        }
        return set;
    }
    
    private static BigInteger getSerialNumber(final Object o) {
        return ((X509Certificate)o).getSerialNumber();
    }
    
    protected static Date getValidCertDateFromValidityModel(final PKIXExtendedParameters pkixExtendedParameters, final CertPath certPath, int n) throws AnnotatedException {
        if (pkixExtendedParameters.getValidityModel() == 1) {
            if (n <= 0) {
                return getValidDate(pkixExtendedParameters);
            }
            --n;
            Label_0086: {
                if (n != 0) {
                    break Label_0086;
                }
                ASN1GeneralizedTime instance = null;
                try {
                    final byte[] extensionValue = ((X509Certificate)certPath.getCertificates().get(n)).getExtensionValue(ISISMTTObjectIdentifiers.id_isismtt_at_dateOfCertGen.getId());
                    if (extensionValue != null) {
                        instance = ASN1GeneralizedTime.getInstance(ASN1Primitive.fromByteArray(extensionValue));
                    }
                    if (instance != null) {
                        try {
                            return instance.getDate();
                        }
                        catch (ParseException ex) {
                            throw new AnnotatedException("Date from date of cert gen extension could not be parsed.", ex);
                        }
                    }
                    return ((X509Certificate)certPath.getCertificates().get(n)).getNotBefore();
                }
                catch (IllegalArgumentException ex2) {
                    throw new AnnotatedException("Date of cert gen extension could not be read.");
                }
                catch (IOException ex3) {
                    throw new AnnotatedException("Date of cert gen extension could not be read.");
                }
            }
        }
        return getValidDate(pkixExtendedParameters);
    }
    
    protected static Date getValidDate(final PKIXExtendedParameters pkixExtendedParameters) {
        Date date;
        if ((date = pkixExtendedParameters.getDate()) == null) {
            date = new Date();
        }
        return date;
    }
    
    protected static boolean isAnyPolicy(final Set set) {
        return set == null || set.contains("2.5.29.32.0") || set.isEmpty();
    }
    
    private static boolean isDeltaCRL(final X509CRL x509CRL) {
        final Set<String> criticalExtensionOIDs = x509CRL.getCriticalExtensionOIDs();
        return criticalExtensionOIDs != null && criticalExtensionOIDs.contains(RFC3280CertPathUtilities.DELTA_CRL_INDICATOR);
    }
    
    static boolean isIssuerTrustAnchor(final X509Certificate x509Certificate, final Set set, final String s) throws AnnotatedException {
        boolean b = false;
        try {
            if (findTrustAnchor(x509Certificate, set, s) != null) {
                b = true;
            }
            return b;
        }
        catch (Exception ex) {
            return false;
        }
    }
    
    protected static boolean isSelfIssued(final X509Certificate x509Certificate) {
        return x509Certificate.getSubjectDN().equals(x509Certificate.getIssuerDN());
    }
    
    protected static void prepareNextCertB1(final int n, final List[] array, final String s, final Map map, final X509Certificate x509Certificate) throws AnnotatedException, CertPathValidatorException {
        while (true) {
            for (final PKIXPolicyNode pkixPolicyNode : array[n]) {
                if (pkixPolicyNode.getValidPolicy().equals(s)) {
                    final boolean b = true;
                    pkixPolicyNode.expectedPolicies = map.get(s);
                    if (!b) {
                        for (final PKIXPolicyNode pkixPolicyNode2 : array[n]) {
                            if ("2.5.29.32.0".equals(pkixPolicyNode2.getValidPolicy())) {
                                final Set set = null;
                                try {
                                    final Enumeration objects = ASN1Sequence.getInstance(getExtensionValue(x509Certificate, CertPathValidatorUtilities.CERTIFICATE_POLICIES)).getObjects();
                                    Set qualifierSet;
                                    while (true) {
                                        qualifierSet = set;
                                        if (objects.hasMoreElements()) {
                                            try {
                                                final PolicyInformation instance = PolicyInformation.getInstance(objects.nextElement());
                                                if (!"2.5.29.32.0".equals(instance.getPolicyIdentifier().getId())) {
                                                    continue;
                                                }
                                                try {
                                                    qualifierSet = getQualifierSet(instance.getPolicyQualifiers());
                                                }
                                                catch (CertPathValidatorException ex) {
                                                    throw new ExtCertPathValidatorException("Policy qualifier info set could not be built.", ex);
                                                }
                                            }
                                            catch (Exception ex2) {
                                                throw new AnnotatedException("Policy information cannot be decoded.", ex2);
                                            }
                                            break;
                                        }
                                        break;
                                    }
                                    final boolean b2 = x509Certificate.getCriticalExtensionOIDs() != null && x509Certificate.getCriticalExtensionOIDs().contains(CertPathValidatorUtilities.CERTIFICATE_POLICIES);
                                    final PKIXPolicyNode pkixPolicyNode3 = (PKIXPolicyNode)pkixPolicyNode2.getParent();
                                    if ("2.5.29.32.0".equals(pkixPolicyNode3.getValidPolicy())) {
                                        final PKIXPolicyNode pkixPolicyNode4 = new PKIXPolicyNode(new ArrayList(), n, map.get(s), pkixPolicyNode3, qualifierSet, s, b2);
                                        pkixPolicyNode3.addChild(pkixPolicyNode4);
                                        array[n].add(pkixPolicyNode4);
                                        return;
                                    }
                                }
                                catch (Exception ex3) {
                                    throw new AnnotatedException("Certificate policies cannot be decoded.", ex3);
                                }
                                break;
                            }
                        }
                    }
                    return;
                }
            }
            final boolean b = false;
            continue;
        }
    }
    
    protected static PKIXPolicyNode prepareNextCertB2(final int n, final List[] array, final String s, PKIXPolicyNode removePolicyNode) {
        final Iterator iterator = array[n].iterator();
        PKIXPolicyNode pkixPolicyNode = removePolicyNode;
        while (iterator.hasNext()) {
            removePolicyNode = iterator.next();
            if (removePolicyNode.getValidPolicy().equals(s)) {
                ((PKIXPolicyNode)removePolicyNode.getParent()).removeChild(removePolicyNode);
                iterator.remove();
                int n2 = n - 1;
                removePolicyNode = pkixPolicyNode;
                while (true) {
                    pkixPolicyNode = removePolicyNode;
                    if (n2 < 0) {
                        break;
                    }
                    final List list = array[n2];
                    int n3 = 0;
                    PKIXPolicyNode pkixPolicyNode2;
                    while (true) {
                        pkixPolicyNode2 = removePolicyNode;
                        if (n3 >= list.size()) {
                            break;
                        }
                        final PKIXPolicyNode pkixPolicyNode3 = list.get(n3);
                        PKIXPolicyNode pkixPolicyNode4 = removePolicyNode;
                        if (!pkixPolicyNode3.hasChildren()) {
                            removePolicyNode = removePolicyNode(removePolicyNode, array, pkixPolicyNode3);
                            if ((pkixPolicyNode4 = removePolicyNode) == null) {
                                pkixPolicyNode2 = removePolicyNode;
                                break;
                            }
                        }
                        ++n3;
                        removePolicyNode = pkixPolicyNode4;
                    }
                    --n2;
                    removePolicyNode = pkixPolicyNode2;
                }
            }
        }
        return pkixPolicyNode;
    }
    
    protected static boolean processCertD1i(final int n, final List[] array, final ASN1ObjectIdentifier asn1ObjectIdentifier, final Set set) {
        final List list = array[n - 1];
        for (int i = 0; i < list.size(); ++i) {
            final PKIXPolicyNode pkixPolicyNode = list.get(i);
            if (pkixPolicyNode.getExpectedPolicies().contains(asn1ObjectIdentifier.getId())) {
                final HashSet<String> set2 = new HashSet<String>();
                set2.add(asn1ObjectIdentifier.getId());
                final PKIXPolicyNode pkixPolicyNode2 = new PKIXPolicyNode(new ArrayList(), n, set2, pkixPolicyNode, set, asn1ObjectIdentifier.getId(), false);
                pkixPolicyNode.addChild(pkixPolicyNode2);
                array[n].add(pkixPolicyNode2);
                return true;
            }
        }
        return false;
    }
    
    protected static void processCertD1ii(final int n, final List[] array, final ASN1ObjectIdentifier asn1ObjectIdentifier, final Set set) {
        final List list = array[n - 1];
        for (int i = 0; i < list.size(); ++i) {
            final PKIXPolicyNode pkixPolicyNode = list.get(i);
            if ("2.5.29.32.0".equals(pkixPolicyNode.getValidPolicy())) {
                final HashSet<String> set2 = new HashSet<String>();
                set2.add(asn1ObjectIdentifier.getId());
                final PKIXPolicyNode pkixPolicyNode2 = new PKIXPolicyNode(new ArrayList(), n, set2, pkixPolicyNode, set, asn1ObjectIdentifier.getId(), false);
                pkixPolicyNode.addChild(pkixPolicyNode2);
                array[n].add(pkixPolicyNode2);
                return;
            }
        }
    }
    
    protected static PKIXPolicyNode removePolicyNode(final PKIXPolicyNode pkixPolicyNode, final List[] array, final PKIXPolicyNode pkixPolicyNode2) {
        final PKIXPolicyNode pkixPolicyNode3 = (PKIXPolicyNode)pkixPolicyNode2.getParent();
        if (pkixPolicyNode == null) {
            return null;
        }
        if (pkixPolicyNode3 == null) {
            for (int i = 0; i < array.length; ++i) {
                array[i] = new ArrayList();
            }
            return null;
        }
        pkixPolicyNode3.removeChild(pkixPolicyNode2);
        removePolicyNodeRecurse(array, pkixPolicyNode2);
        return pkixPolicyNode;
    }
    
    private static void removePolicyNodeRecurse(final List[] array, final PKIXPolicyNode pkixPolicyNode) {
        array[pkixPolicyNode.getDepth()].remove(pkixPolicyNode);
        if (pkixPolicyNode.hasChildren()) {
            final Iterator children = pkixPolicyNode.getChildren();
            while (children.hasNext()) {
                removePolicyNodeRecurse(array, children.next());
            }
        }
    }
    
    protected static void verifyX509Certificate(final X509Certificate x509Certificate, final PublicKey publicKey, final String s) throws GeneralSecurityException {
        if (s == null) {
            x509Certificate.verify(publicKey);
            return;
        }
        x509Certificate.verify(publicKey, s);
    }
}
