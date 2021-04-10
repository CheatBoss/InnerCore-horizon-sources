package com.google.android.gms.common.api;

public class ApiException extends Exception
{
    protected final Status mStatus;
    
    public ApiException(final Status mStatus) {
        final int statusCode = mStatus.getStatusCode();
        String statusMessage;
        if (mStatus.getStatusMessage() != null) {
            statusMessage = mStatus.getStatusMessage();
        }
        else {
            statusMessage = "";
        }
        final StringBuilder sb = new StringBuilder(String.valueOf(statusMessage).length() + 13);
        sb.append(statusCode);
        sb.append(": ");
        sb.append(statusMessage);
        super(sb.toString());
        this.mStatus = mStatus;
    }
}
