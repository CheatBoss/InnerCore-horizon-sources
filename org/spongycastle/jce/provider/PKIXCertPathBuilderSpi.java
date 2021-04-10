package org.spongycastle.jce.provider;

import org.spongycastle.jcajce.provider.asymmetric.x509.*;
import org.spongycastle.asn1.x509.*;
import org.spongycastle.x509.*;
import java.util.*;
import java.security.cert.*;
import org.spongycastle.jce.exception.*;
import java.security.*;
import org.spongycastle.jcajce.*;

public class PKIXCertPathBuilderSpi extends CertPathBuilderSpi
{
    private Exception certPathException;
    
    protected CertPathBuilderResult build(final X509Certificate x509Certificate, final PKIXExtendedBuilderParameters pkixExtendedBuilderParameters, final List list) {
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
            final CertificateFactory certificateFactory = new CertificateFactory();
            final PKIXCertPathValidatorSpi pkixCertPathValidatorSpi = new PKIXCertPathValidatorSpi();
            CertPathBuilderResult certPathBuilderResult2 = build;
            CertPathBuilderResult certPathBuilderResult3;
            try {
                if (CertPathValidatorUtilities.isIssuerTrustAnchor(x509Certificate, pkixExtendedBuilderParameters.getBaseParameters().getTrustAnchors(), pkixExtendedBuilderParameters.getBaseParameters().getSigProvider())) {
                    certPathBuilderResult2 = build;
                    try {
                        final CertPath engineGenerateCertPath = certificateFactory.engineGenerateCertPath(list);
                        certPathBuilderResult2 = build;
                        try {
                            final PKIXCertPathValidatorResult pkixCertPathValidatorResult = (PKIXCertPathValidatorResult)pkixCertPathValidatorSpi.engineValidate(engineGenerateCertPath, pkixExtendedBuilderParameters);
                            certPathBuilderResult2 = build;
                            return new PKIXCertPathBuilderResult(engineGenerateCertPath, pkixCertPathValidatorResult.getTrustAnchor(), pkixCertPathValidatorResult.getPolicyTree(), pkixCertPathValidatorResult.getPublicKey());
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
                final ArrayList<PKIXCertStore> list2 = new ArrayList<PKIXCertStore>();
                certPathBuilderResult2 = build;
                list2.addAll((Collection<?>)pkixExtendedBuilderParameters.getBaseParameters().getCertificateStores());
                certPathBuilderResult2 = build;
                try {
                    list2.addAll((Collection<?>)CertPathValidatorUtilities.getAdditionalStoresFromAltNames(x509Certificate.getExtensionValue(Extension.issuerAlternativeName.getId()), pkixExtendedBuilderParameters.getBaseParameters().getNamedCertificateStoreMap()));
                    certPathBuilderResult2 = build;
                    final HashSet<Object> set = new HashSet<Object>();
                    try {
                        set.addAll(CertPathValidatorUtilities.findIssuerCerts(x509Certificate, pkixExtendedBuilderParameters.getBaseParameters().getCertStores(), list2));
                        certPathBuilderResult2 = build;
                        if (set.isEmpty()) {
                            certPathBuilderResult2 = build;
                            throw new AnnotatedException("No issuer certificate for certificate in certification path found.");
                        }
                        certPathBuilderResult2 = build;
                        final Iterator<X509Certificate> iterator = set.iterator();
                        build = certPathBuilderResult;
                        while (true) {
                            certPathBuilderResult2 = build;
                            certPathBuilderResult3 = build;
                            if (!iterator.hasNext() || (certPathBuilderResult3 = build) != null) {
                                break;
                            }
                            certPathBuilderResult2 = build;
                            build = this.build(iterator.next(), pkixExtendedBuilderParameters, list);
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
            catch (AnnotatedException certPathException) {
                this.certPathException = certPathException;
                certPathBuilderResult3 = certPathBuilderResult2;
            }
            if (certPathBuilderResult3 == null) {
                list.remove(x509Certificate);
            }
            return certPathBuilderResult3;
        }
        catch (Exception ex5) {
            throw new RuntimeException("Exception creating support classes.");
        }
    }
    
    @Override
    public CertPathBuilderResult engineBuild(final CertPathParameters certPathParameters) throws CertPathBuilderException, InvalidAlgorithmParameterException {
        Label_0341: {
            PKIXExtendedBuilderParameters build;
            if (certPathParameters instanceof PKIXBuilderParameters) {
                final PKIXBuilderParameters pkixBuilderParameters = (PKIXBuilderParameters)certPathParameters;
                final PKIXExtendedParameters.Builder builder = new PKIXExtendedParameters.Builder(pkixBuilderParameters);
                PKIXExtendedBuilderParameters.Builder builder2;
                if (certPathParameters instanceof ExtendedPKIXParameters) {
                    final ExtendedPKIXBuilderParameters extendedPKIXBuilderParameters = (ExtendedPKIXBuilderParameters)certPathParameters;
                    final Iterator iterator = extendedPKIXBuilderParameters.getAdditionalStores().iterator();
                    while (iterator.hasNext()) {
                        builder.addCertificateStore(iterator.next());
                    }
                    builder2 = new PKIXExtendedBuilderParameters.Builder(builder.build());
                    builder2.addExcludedCerts(extendedPKIXBuilderParameters.getExcludedCerts());
                    builder2.setMaxPathLength(extendedPKIXBuilderParameters.getMaxPathLength());
                }
                else {
                    builder2 = new PKIXExtendedBuilderParameters.Builder(pkixBuilderParameters);
                }
                build = builder2.build();
            }
            else {
                if (!(certPathParameters instanceof PKIXExtendedBuilderParameters)) {
                    break Label_0341;
                }
                build = (PKIXExtendedBuilderParameters)certPathParameters;
            }
            final ArrayList list = new ArrayList();
            final PKIXCertStoreSelector targetConstraints = build.getBaseParameters().getTargetConstraints();
            try {
                final Collection certificates = CertPathValidatorUtilities.findCertificates(targetConstraints, build.getBaseParameters().getCertificateStores());
                certificates.addAll(CertPathValidatorUtilities.findCertificates(targetConstraints, build.getBaseParameters().getCertStores()));
                if (certificates.isEmpty()) {
                    throw new CertPathBuilderException("No certificate found matching targetContraints.");
                }
                CertPathBuilderResult build2 = null;
                for (Iterator<X509Certificate> iterator2 = certificates.iterator(); iterator2.hasNext() && build2 == null; build2 = this.build(iterator2.next(), build, list)) {}
                if (build2 == null) {
                    final Exception certPathException = this.certPathException;
                    if (certPathException != null) {
                        if (certPathException instanceof AnnotatedException) {
                            throw new CertPathBuilderException(this.certPathException.getMessage(), this.certPathException.getCause());
                        }
                        throw new CertPathBuilderException("Possible certificate chain could not be validated.", this.certPathException);
                    }
                }
                if (build2 != null) {
                    return build2;
                }
                if (this.certPathException != null) {
                    return build2;
                }
                throw new CertPathBuilderException("Unable to find certificate chain.");
            }
            catch (AnnotatedException ex) {
                throw new ExtCertPathBuilderException("Error finding target certificate.", ex);
            }
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Parameters must be an instance of ");
        sb.append(PKIXBuilderParameters.class.getName());
        sb.append(" or ");
        sb.append(PKIXExtendedBuilderParameters.class.getName());
        sb.append(".");
        throw new InvalidAlgorithmParameterException(sb.toString());
    }
}
