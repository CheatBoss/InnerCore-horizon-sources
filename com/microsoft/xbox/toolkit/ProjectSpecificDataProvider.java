package com.microsoft.xbox.toolkit;

public class ProjectSpecificDataProvider implements IProjectSpecificDataProvider
{
    private static ProjectSpecificDataProvider instance;
    private IProjectSpecificDataProvider provider;
    
    static {
        ProjectSpecificDataProvider.instance = new ProjectSpecificDataProvider();
    }
    
    private void checkProvider() {
    }
    
    public static ProjectSpecificDataProvider getInstance() {
        return ProjectSpecificDataProvider.instance;
    }
    
    @Override
    public boolean getAllowExplicitContent() {
        this.checkProvider();
        final IProjectSpecificDataProvider provider = this.provider;
        return provider != null && provider.getAllowExplicitContent();
    }
    
    @Override
    public String getAutoSuggestdDataSource() {
        this.checkProvider();
        final IProjectSpecificDataProvider provider = this.provider;
        if (provider != null) {
            return provider.getAutoSuggestdDataSource();
        }
        return null;
    }
    
    @Override
    public String getCombinedContentRating() {
        this.checkProvider();
        final IProjectSpecificDataProvider provider = this.provider;
        if (provider != null) {
            return provider.getCombinedContentRating();
        }
        return null;
    }
    
    @Override
    public String getConnectedLocale() {
        this.checkProvider();
        final IProjectSpecificDataProvider provider = this.provider;
        if (provider != null) {
            return provider.getConnectedLocale();
        }
        return null;
    }
    
    @Override
    public String getConnectedLocale(final boolean b) {
        this.checkProvider();
        final IProjectSpecificDataProvider provider = this.provider;
        if (provider != null) {
            return provider.getConnectedLocale(b);
        }
        return null;
    }
    
    @Override
    public String getContentRestrictions() {
        this.checkProvider();
        final IProjectSpecificDataProvider provider = this.provider;
        if (provider != null) {
            return provider.getContentRestrictions();
        }
        return null;
    }
    
    @Override
    public String getCurrentSandboxID() {
        this.checkProvider();
        final IProjectSpecificDataProvider provider = this.provider;
        if (provider != null) {
            return provider.getCurrentSandboxID();
        }
        return null;
    }
    
    @Override
    public boolean getInitializeComplete() {
        this.checkProvider();
        final IProjectSpecificDataProvider provider = this.provider;
        return provider != null && provider.getInitializeComplete();
    }
    
    @Override
    public boolean getIsForXboxOne() {
        this.checkProvider();
        final IProjectSpecificDataProvider provider = this.provider;
        return provider != null && provider.getIsForXboxOne();
    }
    
    @Override
    public boolean getIsFreeAccount() {
        this.checkProvider();
        final IProjectSpecificDataProvider provider = this.provider;
        return provider == null || provider.getIsFreeAccount();
    }
    
    @Override
    public boolean getIsXboxMusicSupported() {
        this.checkProvider();
        final IProjectSpecificDataProvider provider = this.provider;
        return provider != null && provider.getIsXboxMusicSupported();
    }
    
    @Override
    public String getLegalLocale() {
        this.checkProvider();
        final IProjectSpecificDataProvider provider = this.provider;
        if (provider != null) {
            return provider.getLegalLocale();
        }
        return null;
    }
    
    @Override
    public String getMembershipLevel() {
        this.checkProvider();
        final IProjectSpecificDataProvider provider = this.provider;
        if (provider != null) {
            return provider.getMembershipLevel();
        }
        return null;
    }
    
    @Override
    public String getPrivileges() {
        this.checkProvider();
        final IProjectSpecificDataProvider provider = this.provider;
        if (provider != null) {
            return provider.getPrivileges();
        }
        return "";
    }
    
    @Override
    public String getRegion() {
        this.checkProvider();
        final IProjectSpecificDataProvider provider = this.provider;
        if (provider != null) {
            return provider.getRegion();
        }
        return null;
    }
    
    @Override
    public String getSCDRpsTicket() {
        this.checkProvider();
        final IProjectSpecificDataProvider provider = this.provider;
        if (provider != null) {
            return provider.getSCDRpsTicket();
        }
        return null;
    }
    
    @Override
    public String getVersionCheckUrl() {
        this.checkProvider();
        final IProjectSpecificDataProvider provider = this.provider;
        if (provider != null) {
            return provider.getVersionCheckUrl();
        }
        return null;
    }
    
    @Override
    public int getVersionCode() {
        this.checkProvider();
        final IProjectSpecificDataProvider provider = this.provider;
        if (provider != null) {
            return provider.getVersionCode();
        }
        return 0;
    }
    
    @Override
    public String getWindowsLiveClientId() {
        this.checkProvider();
        final IProjectSpecificDataProvider provider = this.provider;
        if (provider != null) {
            return provider.getWindowsLiveClientId();
        }
        return null;
    }
    
    @Override
    public String getXuidString() {
        this.checkProvider();
        final IProjectSpecificDataProvider provider = this.provider;
        if (provider != null) {
            return provider.getXuidString();
        }
        return null;
    }
    
    @Override
    public boolean isDeviceLocaleKnown() {
        this.checkProvider();
        final IProjectSpecificDataProvider provider = this.provider;
        return provider == null || provider.isDeviceLocaleKnown();
    }
    
    @Override
    public void resetModels(final boolean b) {
        this.checkProvider();
        final IProjectSpecificDataProvider provider = this.provider;
        if (provider != null) {
            provider.resetModels(b);
        }
    }
    
    @Override
    public void setPrivileges(final String privileges) {
        this.checkProvider();
        final IProjectSpecificDataProvider provider = this.provider;
        if (provider != null) {
            provider.setPrivileges(privileges);
        }
    }
    
    public void setProvider(final IProjectSpecificDataProvider provider) {
        this.provider = provider;
    }
    
    @Override
    public void setSCDRpsTicket(final String scdRpsTicket) {
        this.checkProvider();
        final IProjectSpecificDataProvider provider = this.provider;
        if (provider != null) {
            provider.setSCDRpsTicket(scdRpsTicket);
        }
    }
    
    @Override
    public void setXuidString(final String xuidString) {
        this.checkProvider();
        final IProjectSpecificDataProvider provider = this.provider;
        if (provider != null) {
            provider.setXuidString(xuidString);
        }
    }
}
