package org.spongycastle.jce.provider;

import org.spongycastle.asn1.x509.*;
import org.spongycastle.util.*;
import java.util.*;
import org.spongycastle.x509.*;
import javax.security.auth.x500.*;
import org.spongycastle.jcajce.*;
import java.security.cert.*;
import org.spongycastle.jce.exception.*;
import java.io.*;
import java.security.*;

public class PKIXAttrCertPathBuilderSpi extends CertPathBuilderSpi
{
    private Exception certPathException;
    
    private CertPathBuilderResult build(final X509AttributeCertificate x509AttributeCertificate, final X509Certificate x509Certificate, final PKIXExtendedBuilderParameters pkixExtendedBuilderParameters, final List list) {
        final boolean contains = list.contains(x509Certificate);
        CertPathBuilderResult build = null;
        final CertPathBuilderResult certPathBuilderResult = null;
        if (contains) {
            return null;
        }
        if (pkixExtendedBuilderParameters.getExcludedCerts().contains(x509Certificate)) {
            return null;
        }
        if (pkixExtendedBuilderParameters.getMaxPathLength() != -1 && list.size() - 1 > pkixExtendedBuilderParameters.getMaxPathLength()) {
            return null;
        }
        list.add(x509Certificate);
        try {
            final CertificateFactory instance = CertificateFactory.getInstance("X.509", "SC");
            final CertPathValidator instance2 = CertPathValidator.getInstance("RFC3281", "SC");
            CertPathBuilderResult certPathBuilderResult2 = build;
            CertPathBuilderResult certPathBuilderResult3;
            try {
                if (CertPathValidatorUtilities.isIssuerTrustAnchor(x509Certificate, pkixExtendedBuilderParameters.getBaseParameters().getTrustAnchors(), pkixExtendedBuilderParameters.getBaseParameters().getSigProvider())) {
                    certPathBuilderResult2 = build;
                    try {
                        final CertPath generateCertPath = instance.generateCertPath(list);
                        certPathBuilderResult2 = build;
                        try {
                            final PKIXCertPathValidatorResult pkixCertPathValidatorResult = (PKIXCertPathValidatorResult)instance2.validate(generateCertPath, pkixExtendedBuilderParameters);
                            certPathBuilderResult2 = build;
                            return new PKIXCertPathBuilderResult(generateCertPath, pkixCertPathValidatorResult.getTrustAnchor(), pkixCertPathValidatorResult.getPolicyTree(), pkixCertPathValidatorResult.getPublicKey());
                        }
                        catch (Exception ex) {
                            certPathBuilderResult2 = build;
                            throw new AnnotatedException("Certification path could not be validated.", ex);
                        }
                    }
                    catch (Exception ex2) {
                        certPathBuilderResult2 = build;
                        throw new AnnotatedException("Certification path could not be constructed from certificate list.", ex2);
                    }
                }
                certPathBuilderResult2 = build;
                final ArrayList<Object> list2 = new ArrayList<Object>();
                certPathBuilderResult2 = build;
                list2.addAll(pkixExtendedBuilderParameters.getBaseParameters().getCertificateStores());
                certPathBuilderResult2 = build;
                try {
                    list2.addAll(CertPathValidatorUtilities.getAdditionalStoresFromAltNames(x509Certificate.getExtensionValue(Extension.issuerAlternativeName.getId()), pkixExtendedBuilderParameters.getBaseParameters().getNamedCertificateStoreMap()));
                    certPathBuilderResult2 = build;
                    final HashSet<X509Certificate> set = new HashSet<X509Certificate>();
                    try {
                        set.addAll((Collection<?>)CertPathValidatorUtilities.findIssuerCerts(x509Certificate, pkixExtendedBuilderParameters.getBaseParameters().getCertStores(), (List<PKIXCertStore>)list2));
                        certPathBuilderResult2 = build;
                        if (set.isEmpty()) {
                            certPathBuilderResult2 = build;
                            throw new AnnotatedException("No issuer certificate for certificate in certification path found.");
                        }
                        certPathBuilderResult2 = build;
                        final Iterator<Object> iterator = set.iterator();
                        build = certPathBuilderResult;
                        while (true) {
                            certPathBuilderResult2 = build;
                            certPathBuilderResult3 = build;
                            if (!iterator.hasNext() || (certPathBuilderResult3 = build) != null) {
                                break;
                            }
                            certPathBuilderResult2 = build;
                            final X509Certificate x509Certificate2 = iterator.next();
                            certPathBuilderResult2 = build;
                            if (x509Certificate2.getIssuerX500Principal().equals(x509Certificate2.getSubjectX500Principal())) {
                                continue;
                            }
                            certPathBuilderResult2 = build;
                            build = this.build(x509AttributeCertificate, x509Certificate2, pkixExtendedBuilderParameters, list);
                        }
                    }
                    catch (AnnotatedException ex3) {
                        certPathBuilderResult2 = build;
                        throw new AnnotatedException("Cannot find issuer certificate for certificate in certification path.", ex3);
                    }
                }
                catch (CertificateParsingException ex4) {
                    certPathBuilderResult2 = build;
                    throw new AnnotatedException("No additional X.509 stores can be added from certificate locations.", ex4);
                }
            }
            catch (AnnotatedException ex5) {
                this.certPathException = new AnnotatedException("No valid certification path could be build.", ex5);
                certPathBuilderResult3 = certPathBuilderResult2;
            }
            if (certPathBuilderResult3 == null) {
                list.remove(x509Certificate);
            }
            return certPathBuilderResult3;
        }
        catch (Exception ex6) {
            throw new RuntimeException("Exception creating support classes.");
        }
    }
    
    protected static Collection findCertificates(final X509AttributeCertStoreSelector x509AttributeCertStoreSelector, final List list) throws AnnotatedException {
        final HashSet set = new HashSet();
        for (final Store<? extends E> next : list) {
            if (next instanceof Store) {
                final Store<? extends E> store = next;
                try {
                    set.addAll(store.getMatches(x509AttributeCertStoreSelector));
                    continue;
                }
                catch (StoreException ex) {
                    throw new AnnotatedException("Problem while picking certificates from X.509 store.", ex);
                }
                break;
            }
        }
        return set;
    }
    
    @Override
    public CertPathBuilderResult engineBuild(final CertPathParameters certPathParameters) throws CertPathBuilderException, InvalidAlgorithmParameterException {
        final boolean b = certPathParameters instanceof PKIXBuilderParameters;
        if (!b && !(certPathParameters instanceof ExtendedPKIXBuilderParameters) && !(certPathParameters instanceof PKIXExtendedBuilderParameters)) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Parameters must be an instance of ");
            sb.append(PKIXBuilderParameters.class.getName());
            sb.append(" or ");
            sb.append(PKIXExtendedBuilderParameters.class.getName());
            sb.append(".");
            throw new InvalidAlgorithmParameterException(sb.toString());
        }
        List stores = new ArrayList();
        PKIXExtendedBuilderParameters build;
        if (b) {
            final PKIXExtendedBuilderParameters.Builder builder = new PKIXExtendedBuilderParameters.Builder((PKIXBuilderParameters)certPathParameters);
            if (certPathParameters instanceof ExtendedPKIXParameters) {
                final ExtendedPKIXBuilderParameters extendedPKIXBuilderParameters = (ExtendedPKIXBuilderParameters)certPathParameters;
                builder.addExcludedCerts(extendedPKIXBuilderParameters.getExcludedCerts());
                builder.setMaxPathLength(extendedPKIXBuilderParameters.getMaxPathLength());
                stores = extendedPKIXBuilderParameters.getStores();
            }
            build = builder.build();
        }
        else {
            build = (PKIXExtendedBuilderParameters)certPathParameters;
        }
        final ArrayList list = new ArrayList();
        final PKIXCertStoreSelector targetConstraints = build.getBaseParameters().getTargetConstraints();
        if (targetConstraints instanceof X509AttributeCertStoreSelector) {
            try {
                final Collection certificates = findCertificates((X509AttributeCertStoreSelector)targetConstraints, stores);
                if (certificates.isEmpty()) {
                    throw new CertPathBuilderException("No attribute certificate found matching targetContraints.");
                }
                CertPathBuilderResult certPathBuilderResult = null;
                final Iterator<X509AttributeCertificate> iterator = certificates.iterator();
                while (iterator.hasNext() && certPathBuilderResult == null) {
                    final X509AttributeCertificate x509AttributeCertificate = iterator.next();
                    final X509CertStoreSelector x509CertStoreSelector = new X509CertStoreSelector();
                    final Principal[] principals = x509AttributeCertificate.getIssuer().getPrincipals();
                    final HashSet<X509Certificate> set = new HashSet<X509Certificate>();
                    int i = 0;
                    while (i < principals.length) {
                        try {
                            if (principals[i] instanceof X500Principal) {
                                x509CertStoreSelector.setSubject(((X500Principal)principals[i]).getEncoded());
                            }
                            final PKIXCertStoreSelector<? extends Certificate> build2 = new PKIXCertStoreSelector.Builder(x509CertStoreSelector).build();
                            set.addAll((Collection<?>)CertPathValidatorUtilities.findCertificates(build2, build.getBaseParameters().getCertStores()));
                            set.addAll((Collection<?>)CertPathValidatorUtilities.findCertificates(build2, build.getBaseParameters().getCertificateStores()));
                            ++i;
                            continue;
                        }
                        catch (IOException ex) {
                            throw new ExtCertPathBuilderException("cannot encode X500Principal.", ex);
                        }
                        catch (AnnotatedException ex2) {
                            throw new ExtCertPathBuilderException("Public key certificate for attribute certificate cannot be searched.", ex2);
                        }
                        break;
                    }
                    if (set.isEmpty()) {
                        throw new CertPathBuilderException("Public key certificate for attribute certificate cannot be found.");
                    }
                    final Iterator<Object> iterator2 = set.iterator();
                    CertPathBuilderResult build3 = certPathBuilderResult;
                    while (true) {
                        certPathBuilderResult = build3;
                        if (!iterator2.hasNext() || (certPathBuilderResult = build3) != null) {
                            break;
                        }
                        build3 = this.build(x509AttributeCertificate, iterator2.next(), build, list);
                    }
                }
                if (certPathBuilderResult == null && this.certPathException != null) {
                    throw new ExtCertPathBuilderException("Possible certificate chain could not be validated.", this.certPathException);
                }
                if (certPathBuilderResult != null) {
                    return certPathBuilderResult;
                }
                if (this.certPathException != null) {
                    return certPathBuilderResult;
                }
                throw new CertPathBuilderException("Unable to find certificate chain.");
            }
            catch (AnnotatedException ex3) {
                throw new ExtCertPathBuilderException("Error finding target attribute certificate.", ex3);
            }
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("TargetConstraints must be an instance of ");
        sb2.append(X509AttributeCertStoreSelector.class.getName());
        sb2.append(" for ");
        sb2.append(this.getClass().getName());
        sb2.append(" class.");
        throw new CertPathBuilderException(sb2.toString());
    }
}
