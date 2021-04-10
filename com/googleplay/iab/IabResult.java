package com.googleplay.iab;

public class IabResult
{
    String mMessage;
    int mResponse;
    
    public IabResult(final int mResponse, String mMessage) {
        this.mResponse = mResponse;
        if (mMessage != null && mMessage.trim().length() != 0) {
            final StringBuilder sb = new StringBuilder();
            sb.append(mMessage);
            sb.append(" (response: ");
            sb.append(IabHelper.getResponseDesc(mResponse));
            sb.append(")");
            mMessage = sb.toString();
        }
        else {
            mMessage = IabHelper.getResponseDesc(mResponse);
        }
        this.mMessage = mMessage;
    }
    
    public String getMessage() {
        return this.mMessage;
    }
    
    public int getResponse() {
        return this.mResponse;
    }
    
    public boolean isFailure() {
        return this.isSuccess() ^ true;
    }
    
    public boolean isSuccess() {
        return this.mResponse == 0;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("IabResult: ");
        sb.append(this.getMessage());
        return sb.toString();
    }
}
