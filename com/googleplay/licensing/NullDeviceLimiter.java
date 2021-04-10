package com.googleplay.licensing;

public class NullDeviceLimiter implements DeviceLimiter
{
    @Override
    public int isDeviceAllowed(final String s) {
        return 256;
    }
}
