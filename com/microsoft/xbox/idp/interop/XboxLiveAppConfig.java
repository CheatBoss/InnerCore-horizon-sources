package com.microsoft.xbox.idp.interop;

public class XboxLiveAppConfig
{
    private final long id;
    
    public XboxLiveAppConfig() {
        this.id = create();
    }
    
    private static native long create();
    
    private static native void delete(final long p0);
    
    private static native String getEnvironment(final long p0);
    
    private static native int getOverrideTitleId(final long p0);
    
    private static native String getSandbox(final long p0);
    
    private static native String getScid(final long p0);
    
    private static native int getTitleId(final long p0);
    
    @Override
    protected void finalize() throws Throwable {
        super.finalize();
        delete(this.id);
    }
    
    public String getEnvironment() {
        return getEnvironment(this.id);
    }
    
    public int getOverrideTitleId() {
        return getOverrideTitleId(this.id);
    }
    
    public String getSandbox() {
        return getSandbox(this.id);
    }
    
    public String getScid() {
        return getScid(this.id);
    }
    
    public int getTitleId() {
        return getTitleId(this.id);
    }
}
