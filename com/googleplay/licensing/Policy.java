package com.googleplay.licensing;

public interface Policy
{
    public static final int LICENSED = 256;
    public static final int NOT_LICENSED = 561;
    public static final int RETRY = 291;
    
    boolean allowAccess();
    
    void processServerResponse(final int p0, final ResponseData p1);
}
