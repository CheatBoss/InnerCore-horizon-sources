package org.spongycastle.jce.provider;

import org.spongycastle.jcajce.util.*;
import java.security.*;
import java.util.*;
import org.spongycastle.jce.exception.*;
import org.spongycastle.jcajce.*;
import org.spongycastle.x509.*;
import java.security.cert.*;

public class PKIXAttrCertPathValidatorSpi extends CertPathValidatorSpi
{
    private final JcaJceHelper helper;
    
    public PKIXAttrCertPathValidatorSpi() {
        this.helper = new BCJcaJceHelper();
    }
    
    @Override
    public CertPathValidatorResult engineValidate(final CertPath certPath, final CertPathParameters certPathParameters) throws CertPathValidatorException, InvalidAlgorithmParameterException {
        final boolean b = certPathParameters instanceof ExtendedPKIXParameters;
        if (!b && !(certPathParameters instanceof PKIXExtendedParameters)) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Parameters must be a ");
            sb.append(ExtendedPKIXParameters.class.getName());
            sb.append(" instance.");
            throw new InvalidAlgorithmParameterException(sb.toString());
        }
        Set attrCertCheckers = new HashSet();
        Set prohibitedACAttributes = new HashSet();
        Set necessaryACAttributes = new HashSet();
        final HashSet set = new HashSet();
        PKIXExtendedParameters build;
        if (certPathParameters instanceof PKIXParameters) {
            final PKIXExtendedParameters.Builder builder = new PKIXExtendedParameters.Builder((PKIXParameters)certPathParameters);
            if (b) {
                final ExtendedPKIXParameters extendedPKIXParameters = (ExtendedPKIXParameters)certPathParameters;
                builder.setUseDeltasEnabled(extendedPKIXParameters.isUseDeltasEnabled());
                builder.setValidityModel(extendedPKIXParameters.getValidityModel());
                attrCertCheckers = extendedPKIXParameters.getAttrCertCheckers();
                prohibitedACAttributes = extendedPKIXParameters.getProhibitedACAttributes();
                necessaryACAttributes = extendedPKIXParameters.getNecessaryACAttributes();
            }
            build = builder.build();
        }
        else {
            build = (PKIXExtendedParameters)certPathParameters;
        }
        final PKIXCertStoreSelector targetConstraints = build.getTargetConstraints();
        if (targetConstraints instanceof X509AttributeCertStoreSelector) {
            final X509AttributeCertificate attributeCert = ((X509AttributeCertStoreSelector)targetConstraints).getAttributeCert();
            final CertPath processAttrCert1 = RFC3281CertPathUtilities.processAttrCert1(attributeCert, build);
            final CertPathValidatorResult processAttrCert2 = RFC3281CertPathUtilities.processAttrCert2(certPath, build);
            final X509Certificate x509Certificate = (X509Certificate)certPath.getCertificates().get(0);
            RFC3281CertPathUtilities.processAttrCert3(x509Certificate, build);
            RFC3281CertPathUtilities.processAttrCert4(x509Certificate, set);
            RFC3281CertPathUtilities.processAttrCert5(attributeCert, build);
            RFC3281CertPathUtilities.processAttrCert7(attributeCert, certPath, processAttrCert1, build, attrCertCheckers);
            RFC3281CertPathUtilities.additionalChecks(attributeCert, prohibitedACAttributes, necessaryACAttributes);
            try {
                RFC3281CertPathUtilities.checkCRLs(attributeCert, build, x509Certificate, CertPathValidatorUtilities.getValidCertDateFromValidityModel(build, null, -1), certPath.getCertificates(), this.helper);
                return processAttrCert2;
            }
            catch (AnnotatedException ex) {
                throw new ExtCertPathValidatorException("Could not get validity date from attribute certificate.", ex);
            }
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("TargetConstraints must be an instance of ");
        sb2.append(X509AttributeCertStoreSelector.class.getName());
        sb2.append(" for ");
        sb2.append(this.getClass().getName());
        sb2.append(" class.");
        throw new InvalidAlgorithmParameterException(sb2.toString());
    }
}
