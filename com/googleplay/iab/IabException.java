package com.googleplay.iab;

public class IabException extends Exception
{
    IabResult mResult;
    
    public IabException(final int n, final String s) {
        this(new IabResult(n, s));
    }
    
    public IabException(final int n, final String s, final Exception ex) {
        this(new IabResult(n, s), ex);
    }
    
    public IabException(final IabResult iabResult) {
        this(iabResult, null);
    }
    
    public IabException(final IabResult mResult, final Exception ex) {
        super(mResult.getMessage(), ex);
        this.mResult = mResult;
    }
    
    public IabResult getResult() {
        return this.mResult;
    }
}
