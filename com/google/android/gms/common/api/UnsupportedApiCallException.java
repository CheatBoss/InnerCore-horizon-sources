package com.google.android.gms.common.api;

import com.google.android.gms.common.*;

public final class UnsupportedApiCallException extends UnsupportedOperationException
{
    private final Feature zzdr;
    
    public UnsupportedApiCallException(final Feature zzdr) {
        this.zzdr = zzdr;
    }
    
    @Override
    public final String getMessage() {
        final String value = String.valueOf(this.zzdr);
        final StringBuilder sb = new StringBuilder(String.valueOf(value).length() + 8);
        sb.append("Missing ");
        sb.append(value);
        return sb.toString();
    }
}
