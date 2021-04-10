package org.spongycastle.x509;

import java.security.cert.*;
import org.spongycastle.i18n.*;

public class CertPathReviewerException extends LocalizedException
{
    private CertPath certPath;
    private int index;
    
    public CertPathReviewerException(final ErrorBundle errorBundle) {
        super(errorBundle);
        this.index = -1;
        this.certPath = null;
    }
    
    public CertPathReviewerException(final ErrorBundle errorBundle, final Throwable t) {
        super(errorBundle, t);
        this.index = -1;
        this.certPath = null;
    }
    
    public CertPathReviewerException(final ErrorBundle errorBundle, final Throwable t, final CertPath certPath, final int index) {
        super(errorBundle, t);
        this.index = -1;
        this.certPath = null;
        if (certPath == null || index == -1) {
            throw new IllegalArgumentException();
        }
        if (index >= -1 && (certPath == null || index < certPath.getCertificates().size())) {
            this.certPath = certPath;
            this.index = index;
            return;
        }
        throw new IndexOutOfBoundsException();
    }
    
    public CertPathReviewerException(final ErrorBundle errorBundle, final CertPath certPath, final int index) {
        super(errorBundle);
        this.index = -1;
        this.certPath = null;
        if (certPath == null || index == -1) {
            throw new IllegalArgumentException();
        }
        if (index >= -1 && (certPath == null || index < certPath.getCertificates().size())) {
            this.certPath = certPath;
            this.index = index;
            return;
        }
        throw new IndexOutOfBoundsException();
    }
    
    public CertPath getCertPath() {
        return this.certPath;
    }
    
    public int getIndex() {
        return this.index;
    }
}
