package org.spongycastle.jce.provider;

import org.spongycastle.jcajce.util.*;
import org.spongycastle.x509.*;
import org.spongycastle.jcajce.*;
import org.spongycastle.jce.exception.*;
import java.security.*;
import org.spongycastle.asn1.x500.*;
import java.security.cert.*;
import org.spongycastle.asn1.x509.*;
import java.util.*;

public class PKIXCertPathValidatorSpi extends CertPathValidatorSpi
{
    private final JcaJceHelper helper;
    
    public PKIXCertPathValidatorSpi() {
        this.helper = new BCJcaJceHelper();
    }
    
    static void checkCertificate(final X509Certificate x509Certificate) throws AnnotatedException {
        try {
            TBSCertificate.getInstance(x509Certificate.getTBSCertificate());
        }
        catch (IllegalArgumentException ex) {
            throw new AnnotatedException(ex.getMessage());
        }
        catch (CertificateEncodingException ex2) {
            throw new AnnotatedException("unable to process TBSCertificate");
        }
    }
    
    @Override
    public CertPathValidatorResult engineValidate(final CertPath certPath, CertPathParameters ex) throws CertPathValidatorException, InvalidAlgorithmParameterException {
        if (ex instanceof PKIXParameters) {
            final PKIXExtendedParameters.Builder builder = new PKIXExtendedParameters.Builder((PKIXParameters)ex);
            if (ex instanceof ExtendedPKIXParameters) {
                final ExtendedPKIXParameters extendedPKIXParameters = (ExtendedPKIXParameters)ex;
                builder.setUseDeltasEnabled(extendedPKIXParameters.isUseDeltasEnabled());
                builder.setValidityModel(extendedPKIXParameters.getValidityModel());
            }
            ex = (IllegalArgumentException)builder.build();
        }
        else if (ex instanceof PKIXExtendedBuilderParameters) {
            ex = (IllegalArgumentException)((PKIXExtendedBuilderParameters)ex).getBaseParameters();
        }
        else {
            if (!(ex instanceof PKIXExtendedParameters)) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Parameters must be a ");
                sb.append(PKIXParameters.class.getName());
                sb.append(" instance.");
                throw new InvalidAlgorithmParameterException(sb.toString());
            }
            ex = ex;
        }
        if (((PKIXExtendedParameters)ex).getTrustAnchors() == null) {
            throw new InvalidAlgorithmParameterException("trustAnchors is null, this is not allowed for certification path validation.");
        }
        final List<X509Certificate> certificates;
        Object o = certificates = (List<X509Certificate>)certPath.getCertificates();
        final int size = certificates.size();
        if (!certificates.isEmpty()) {
            final Set initialPolicies = ((PKIXExtendedParameters)ex).getInitialPolicies();
            try {
                final TrustAnchor trustAnchor = CertPathValidatorUtilities.findTrustAnchor(certificates.get(certificates.size() - 1), ((PKIXExtendedParameters)ex).getTrustAnchors(), ((PKIXExtendedParameters)ex).getSigProvider());
                if (trustAnchor != null) {
                    checkCertificate(trustAnchor.getTrustedCert());
                    final PKIXExtendedParameters build = new PKIXExtendedParameters.Builder((PKIXExtendedParameters)ex).setTrustAnchor(trustAnchor).build();
                    int n = size + 1;
                    final ArrayList[] array = new ArrayList[n];
                    for (int i = 0; i < n; ++i) {
                        array[i] = new ArrayList();
                    }
                    ex = (IllegalArgumentException)new HashSet();
                    ((Set<String>)ex).add("2.5.29.32.0");
                    final PKIXPolicyNode pkixPolicyNode = new PKIXPolicyNode(new ArrayList(), 0, (Set)ex, null, new HashSet(), "2.5.29.32.0", false);
                    array[0].add(pkixPolicyNode);
                    final PKIXNameConstraintValidator pkixNameConstraintValidator = new PKIXNameConstraintValidator();
                    final HashSet set = new HashSet();
                    if (build.isExplicitPolicyRequired()) {
                        final int n2 = 0;
                    }
                    else {
                        final int n2 = n;
                    }
                    if (build.isAnyPolicyInhibited()) {
                        final int n3 = 0;
                    }
                    else {
                        final int n3 = n;
                    }
                    if (build.isPolicyMappingInhibited()) {
                        n = 0;
                    }
                    X509Certificate trustedCert = trustAnchor.getTrustedCert();
                    Label_0388: {
                        if (trustedCert == null) {
                            break Label_0388;
                        }
                        while (true) {
                            Label_0404: {
                                try {
                                    ex = (IllegalArgumentException)PrincipalUtils.getSubjectPrincipal(trustedCert);
                                    o = trustedCert.getPublicKey();
                                    break Label_0404;
                                }
                                catch (IllegalArgumentException ex) {
                                    throw new ExtCertPathValidatorException("Subject of trust anchor could not be (re)encoded.", ex, certPath, -1);
                                }
                                try {
                                    ex = (IllegalArgumentException)PrincipalUtils.getCA(trustAnchor);
                                    o = trustAnchor.getCAPublicKey();
                                    continue;
                                    try {
                                        final AlgorithmIdentifier algorithmIdentifier = CertPathValidatorUtilities.getAlgorithmIdentifier((PublicKey)o);
                                        algorithmIdentifier.getAlgorithm();
                                        algorithmIdentifier.getParameters();
                                        if (build.getTargetConstraints() != null && !build.getTargetConstraints().match(certificates.get(0))) {
                                            throw new ExtCertPathValidatorException("Target certificate in certification path does not match targetConstraints.", null, certPath, 0);
                                        }
                                        PublicKey nextWorkingKey = (PublicKey)o;
                                        o = build.getCertPathCheckers();
                                        final Iterator<X509Certificate> iterator = ((List<X509Certificate>)o).iterator();
                                        while (iterator.hasNext()) {
                                            ((PKIXCertPathChecker)iterator.next()).init(false);
                                        }
                                        final int size2 = certificates.size();
                                        final int n4 = size;
                                        final int n5 = n;
                                        final int n6 = size2 - 1;
                                        final int n3;
                                        int prepareNextCertJ = n3;
                                        final PKIXPolicyNode pkixPolicyNode2 = pkixPolicyNode;
                                        final int n2;
                                        int prepareNextCertI1 = n2;
                                        final X509Certificate x509Certificate = null;
                                        int prepareNextCertM = n4;
                                        Object o2 = ex;
                                        PKIXPolicyNode processCertE = pkixPolicyNode2;
                                        int prepareNextCertI2 = n5;
                                        int j = n6;
                                        final PKIXExtendedParameters pkixExtendedParameters = build;
                                        X509Certificate x509Certificate2 = x509Certificate;
                                        while (j >= 0) {
                                            final int n7 = size - j;
                                            x509Certificate2 = certificates.get(j);
                                            boolean b;
                                            if (j == certificates.size() - 1) {
                                                b = true;
                                            }
                                            else {
                                                b = false;
                                            }
                                            try {
                                                checkCertificate(x509Certificate2);
                                                RFC3280CertPathUtilities.processCertA(certPath, pkixExtendedParameters, j, nextWorkingKey, b, (X500Name)o2, trustedCert, this.helper);
                                                RFC3280CertPathUtilities.processCertBC(certPath, j, pkixNameConstraintValidator);
                                                processCertE = RFC3280CertPathUtilities.processCertE(certPath, j, RFC3280CertPathUtilities.processCertD(certPath, j, set, processCertE, array, prepareNextCertJ));
                                                RFC3280CertPathUtilities.processCertF(certPath, j, processCertE, prepareNextCertI1);
                                                if (n7 != size) {
                                                    if (x509Certificate2 == null || x509Certificate2.getVersion() != 1) {
                                                        RFC3280CertPathUtilities.prepareNextCertA(certPath, j);
                                                        final PKIXPolicyNode prepareCertB = RFC3280CertPathUtilities.prepareCertB(certPath, j, array, processCertE, prepareNextCertI2);
                                                        RFC3280CertPathUtilities.prepareNextCertG(certPath, j, pkixNameConstraintValidator);
                                                        final int prepareNextCertH1 = RFC3280CertPathUtilities.prepareNextCertH1(certPath, j, prepareNextCertI1);
                                                        final int prepareNextCertH2 = RFC3280CertPathUtilities.prepareNextCertH2(certPath, j, prepareNextCertI2);
                                                        final int prepareNextCertH3 = RFC3280CertPathUtilities.prepareNextCertH3(certPath, j, prepareNextCertJ);
                                                        prepareNextCertI1 = RFC3280CertPathUtilities.prepareNextCertI1(certPath, j, prepareNextCertH1);
                                                        prepareNextCertI2 = RFC3280CertPathUtilities.prepareNextCertI2(certPath, j, prepareNextCertH2);
                                                        prepareNextCertJ = RFC3280CertPathUtilities.prepareNextCertJ(certPath, j, prepareNextCertH3);
                                                        RFC3280CertPathUtilities.prepareNextCertK(certPath, j);
                                                        prepareNextCertM = RFC3280CertPathUtilities.prepareNextCertM(certPath, j, RFC3280CertPathUtilities.prepareNextCertL(certPath, j, prepareNextCertM));
                                                        RFC3280CertPathUtilities.prepareNextCertN(certPath, j);
                                                        final Set<String> criticalExtensionOIDs = x509Certificate2.getCriticalExtensionOIDs();
                                                        if (criticalExtensionOIDs != null) {
                                                            ex = (IllegalArgumentException)new HashSet(criticalExtensionOIDs);
                                                            ((Set)ex).remove(RFC3280CertPathUtilities.KEY_USAGE);
                                                            ((Set)ex).remove(RFC3280CertPathUtilities.CERTIFICATE_POLICIES);
                                                            ((Set)ex).remove(RFC3280CertPathUtilities.POLICY_MAPPINGS);
                                                            ((Set)ex).remove(RFC3280CertPathUtilities.INHIBIT_ANY_POLICY);
                                                            ((Set)ex).remove(RFC3280CertPathUtilities.ISSUING_DISTRIBUTION_POINT);
                                                            ((Set)ex).remove(RFC3280CertPathUtilities.DELTA_CRL_INDICATOR);
                                                            ((Set)ex).remove(RFC3280CertPathUtilities.POLICY_CONSTRAINTS);
                                                            ((Set)ex).remove(RFC3280CertPathUtilities.BASIC_CONSTRAINTS);
                                                            ((Set)ex).remove(RFC3280CertPathUtilities.SUBJECT_ALTERNATIVE_NAME);
                                                            ((Set)ex).remove(RFC3280CertPathUtilities.NAME_CONSTRAINTS);
                                                        }
                                                        else {
                                                            ex = (IllegalArgumentException)new HashSet();
                                                        }
                                                        RFC3280CertPathUtilities.prepareNextCertO(certPath, j, (Set)ex, (List)o);
                                                        final X500Name subjectPrincipal = PrincipalUtils.getSubjectPrincipal(x509Certificate2);
                                                        try {
                                                            ex = (IllegalArgumentException)certPath.getCertificates();
                                                            try {
                                                                nextWorkingKey = CertPathValidatorUtilities.getNextWorkingKey((List)ex, j, this.helper);
                                                                final AlgorithmIdentifier algorithmIdentifier2 = CertPathValidatorUtilities.getAlgorithmIdentifier(nextWorkingKey);
                                                                algorithmIdentifier2.getAlgorithm();
                                                                algorithmIdentifier2.getParameters();
                                                                trustedCert = x509Certificate2;
                                                                processCertE = prepareCertB;
                                                                o2 = subjectPrincipal;
                                                            }
                                                            catch (CertPathValidatorException ex) {}
                                                        }
                                                        catch (CertPathValidatorException ex3) {}
                                                        throw new CertPathValidatorException("Next working key could not be retrieved.", ex, certPath, j);
                                                    }
                                                    if (n7 != 1 || !x509Certificate2.equals(trustAnchor.getTrustedCert())) {
                                                        throw new CertPathValidatorException("Version 1 certificates can't be used as CA ones.", null, certPath, j);
                                                    }
                                                }
                                                --j;
                                                continue;
                                            }
                                            catch (AnnotatedException ex2) {
                                                throw new CertPathValidatorException(ex2.getMessage(), ex2.getUnderlyingException(), certPath, j);
                                            }
                                            break;
                                        }
                                        final int wrapupCertA = RFC3280CertPathUtilities.wrapupCertA(prepareNextCertI1, x509Certificate2);
                                        final int n8 = j + 1;
                                        final int wrapupCertB = RFC3280CertPathUtilities.wrapupCertB(certPath, n8, wrapupCertA);
                                        final Set<String> criticalExtensionOIDs2 = x509Certificate2.getCriticalExtensionOIDs();
                                        HashSet set2;
                                        if (criticalExtensionOIDs2 != null) {
                                            set2 = new HashSet(criticalExtensionOIDs2);
                                            set2.remove(RFC3280CertPathUtilities.KEY_USAGE);
                                            set2.remove(RFC3280CertPathUtilities.CERTIFICATE_POLICIES);
                                            set2.remove(RFC3280CertPathUtilities.POLICY_MAPPINGS);
                                            set2.remove(RFC3280CertPathUtilities.INHIBIT_ANY_POLICY);
                                            set2.remove(RFC3280CertPathUtilities.ISSUING_DISTRIBUTION_POINT);
                                            set2.remove(RFC3280CertPathUtilities.DELTA_CRL_INDICATOR);
                                            set2.remove(RFC3280CertPathUtilities.POLICY_CONSTRAINTS);
                                            set2.remove(RFC3280CertPathUtilities.BASIC_CONSTRAINTS);
                                            set2.remove(RFC3280CertPathUtilities.SUBJECT_ALTERNATIVE_NAME);
                                            set2.remove(RFC3280CertPathUtilities.NAME_CONSTRAINTS);
                                            set2.remove(RFC3280CertPathUtilities.CRL_DISTRIBUTION_POINTS);
                                            set2.remove(Extension.extendedKeyUsage.getId());
                                        }
                                        else {
                                            set2 = new HashSet();
                                        }
                                        RFC3280CertPathUtilities.wrapupCertF(certPath, n8, (List)o, set2);
                                        final PKIXPolicyNode wrapupCertG = RFC3280CertPathUtilities.wrapupCertG(certPath, pkixExtendedParameters, initialPolicies, n8, array, processCertE, set);
                                        if (wrapupCertB <= 0 && wrapupCertG == null) {
                                            throw new CertPathValidatorException("Path processing failed on policy.", null, certPath, j);
                                        }
                                        return new PKIXCertPathValidatorResult(trustAnchor, wrapupCertG, x509Certificate2.getPublicKey());
                                    }
                                    catch (CertPathValidatorException ex) {
                                        throw new ExtCertPathValidatorException("Algorithm identifier of public key of trust anchor could not be read.", ex, certPath, -1);
                                    }
                                }
                                catch (IllegalArgumentException ex4) {}
                            }
                            break;
                        }
                    }
                    throw new ExtCertPathValidatorException("Subject of trust anchor could not be (re)encoded.", ex, certPath, -1);
                }
                try {
                    throw new CertPathValidatorException("Trust anchor for certification path not found.", null, certPath, -1);
                }
                catch (AnnotatedException ex5) {}
            }
            catch (AnnotatedException ex) {}
            throw new CertPathValidatorException(ex.getMessage(), ((AnnotatedException)ex).getUnderlyingException(), certPath, ((List)o).size() - 1);
        }
        throw new CertPathValidatorException("Certification path is empty.", null, certPath, -1);
    }
}
