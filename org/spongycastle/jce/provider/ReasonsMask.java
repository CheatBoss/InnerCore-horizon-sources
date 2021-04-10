package org.spongycastle.jce.provider;

import org.spongycastle.asn1.x509.*;

class ReasonsMask
{
    static final ReasonsMask allReasons;
    private int _reasons;
    
    static {
        allReasons = new ReasonsMask(33023);
    }
    
    ReasonsMask() {
        this(0);
    }
    
    private ReasonsMask(final int reasons) {
        this._reasons = reasons;
    }
    
    ReasonsMask(final ReasonFlags reasonFlags) {
        this._reasons = reasonFlags.intValue();
    }
    
    void addReasons(final ReasonsMask reasonsMask) {
        this._reasons |= reasonsMask.getReasons();
    }
    
    int getReasons() {
        return this._reasons;
    }
    
    boolean hasNewReasons(final ReasonsMask reasonsMask) {
        return ((reasonsMask.getReasons() ^ this._reasons) | this._reasons) != 0x0;
    }
    
    ReasonsMask intersect(final ReasonsMask reasonsMask) {
        final ReasonsMask reasonsMask2 = new ReasonsMask();
        reasonsMask2.addReasons(new ReasonsMask(reasonsMask.getReasons() & this._reasons));
        return reasonsMask2;
    }
    
    boolean isAllReasons() {
        return this._reasons == ReasonsMask.allReasons._reasons;
    }
}
