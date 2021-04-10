package org.spongycastle.crypto.tls;

import org.spongycastle.crypto.params.*;
import org.spongycastle.crypto.agreement.*;
import org.spongycastle.crypto.*;
import org.spongycastle.util.*;
import java.math.*;

public class DefaultTlsAgreementCredentials extends AbstractTlsAgreementCredentials
{
    protected BasicAgreement basicAgreement;
    protected Certificate certificate;
    protected AsymmetricKeyParameter privateKey;
    protected boolean truncateAgreement;
    
    public DefaultTlsAgreementCredentials(final Certificate certificate, final AsymmetricKeyParameter privateKey) {
        if (certificate == null) {
            throw new IllegalArgumentException("'certificate' cannot be null");
        }
        if (certificate.isEmpty()) {
            throw new IllegalArgumentException("'certificate' cannot be empty");
        }
        if (privateKey == null) {
            throw new IllegalArgumentException("'privateKey' cannot be null");
        }
        if (privateKey.isPrivate()) {
            boolean truncateAgreement;
            if (privateKey instanceof DHPrivateKeyParameters) {
                this.basicAgreement = new DHBasicAgreement();
                truncateAgreement = true;
            }
            else {
                if (!(privateKey instanceof ECPrivateKeyParameters)) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("'privateKey' type not supported: ");
                    sb.append(privateKey.getClass().getName());
                    throw new IllegalArgumentException(sb.toString());
                }
                this.basicAgreement = new ECDHBasicAgreement();
                truncateAgreement = false;
            }
            this.truncateAgreement = truncateAgreement;
            this.certificate = certificate;
            this.privateKey = privateKey;
            return;
        }
        throw new IllegalArgumentException("'privateKey' must be private");
    }
    
    @Override
    public byte[] generateAgreement(final AsymmetricKeyParameter asymmetricKeyParameter) {
        this.basicAgreement.init(this.privateKey);
        final BigInteger calculateAgreement = this.basicAgreement.calculateAgreement(asymmetricKeyParameter);
        if (this.truncateAgreement) {
            return BigIntegers.asUnsignedByteArray(calculateAgreement);
        }
        return BigIntegers.asUnsignedByteArray(this.basicAgreement.getFieldSize(), calculateAgreement);
    }
    
    @Override
    public Certificate getCertificate() {
        return this.certificate;
    }
}
