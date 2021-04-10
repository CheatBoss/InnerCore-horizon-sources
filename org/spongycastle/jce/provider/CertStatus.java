package org.spongycastle.jce.provider;

import java.util.*;

class CertStatus
{
    public static final int UNDETERMINED = 12;
    public static final int UNREVOKED = 11;
    int certStatus;
    Date revocationDate;
    
    CertStatus() {
        this.certStatus = 11;
        this.revocationDate = null;
    }
    
    public int getCertStatus() {
        return this.certStatus;
    }
    
    public Date getRevocationDate() {
        return this.revocationDate;
    }
    
    public void setCertStatus(final int certStatus) {
        this.certStatus = certStatus;
    }
    
    public void setRevocationDate(final Date revocationDate) {
        this.revocationDate = revocationDate;
    }
}
