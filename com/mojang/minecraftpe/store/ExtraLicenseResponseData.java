package com.mojang.minecraftpe.store;

public class ExtraLicenseResponseData
{
    private long mRetryAttempts;
    private long mRetryUntilTime;
    private long mValidationTime;
    
    public ExtraLicenseResponseData(final long mValidationTime, final long mRetryUntilTime, final long mRetryAttempts) {
        this.mValidationTime = 0L;
        this.mRetryUntilTime = 0L;
        this.mRetryAttempts = 0L;
        this.mValidationTime = mValidationTime;
        this.mRetryUntilTime = mRetryUntilTime;
        this.mRetryAttempts = mRetryAttempts;
    }
    
    public long getRetryAttempts() {
        return this.mRetryAttempts;
    }
    
    public long getRetryUntilTime() {
        return this.mRetryUntilTime;
    }
    
    public long getValidationTime() {
        return this.mValidationTime;
    }
}
