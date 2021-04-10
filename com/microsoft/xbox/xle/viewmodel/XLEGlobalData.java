package com.microsoft.xbox.xle.viewmodel;

import com.microsoft.xbox.xle.app.activity.*;
import java.util.*;
import java.net.*;
import com.microsoft.xbox.toolkit.*;

public class XLEGlobalData
{
    private static final int MAX_SEARCH_TEXT_LENGTH = 120;
    private boolean autoLoginStarted;
    private Class<? extends ActivityBase> defaultScreenClass;
    private long errorCodeForLogin;
    private boolean forceRefreshProfile;
    private HashSet<Class<? extends ViewModelBase>> forceRefreshVMs;
    private boolean friendListUpdated;
    private boolean hideCollectionFilter;
    private boolean isLoggedIn;
    private boolean isOffline;
    private boolean launchTitleIsBrowser;
    private String pivotTitle;
    private Class<? extends ViewModelBase> searchFilterSetterClass;
    private String searchTag;
    private String selectedAchievementKey;
    private String selectedDataSource;
    private String selectedGamertag;
    private ArrayList<URI> selectedImages;
    private MultiSelection<FriendSelectorItem> selectedRecipients;
    private String selectedXuid;
    private boolean showLoginError;
    private long titleId;
    
    private XLEGlobalData() {
        this.isOffline = true;
        this.friendListUpdated = false;
        this.launchTitleIsBrowser = false;
        this.hideCollectionFilter = false;
    }
    
    public static XLEGlobalData getInstance() {
        return XLEGlobalDataHolder.instance;
    }
    
    public void AddForceRefresh(final Class<? extends ViewModelBase> clazz) {
        XLEAssert.assertIsUIThread();
        if (this.forceRefreshVMs == null) {
            this.forceRefreshVMs = new HashSet<Class<? extends ViewModelBase>>();
        }
        this.forceRefreshVMs.add(clazz);
    }
    
    public boolean CheckDrainShouldRefresh(final Class<? extends ViewModelBase> clazz) {
        final HashSet<Class<? extends ViewModelBase>> forceRefreshVMs = this.forceRefreshVMs;
        return forceRefreshVMs != null && forceRefreshVMs.remove(clazz);
    }
    
    public boolean getAutoLoginStarted() {
        return this.autoLoginStarted;
    }
    
    public Class<? extends ActivityBase> getDefaultScreenClass() {
        return this.defaultScreenClass;
    }
    
    public boolean getForceRefreshProfile() {
        return this.forceRefreshProfile;
    }
    
    public boolean getFriendListUpdated() {
        return this.friendListUpdated;
    }
    
    public boolean getHideCollectionFilter() {
        return this.hideCollectionFilter;
    }
    
    public boolean getIsLoggedIn() {
        return this.isLoggedIn;
    }
    
    public boolean getIsOffline() {
        return this.isOffline;
    }
    
    public boolean getIsOnline() {
        return this.isOffline ^ true;
    }
    
    public long getLastLoginError() {
        final long errorCodeForLogin = this.errorCodeForLogin;
        this.errorCodeForLogin = 0L;
        return errorCodeForLogin;
    }
    
    public boolean getLaunchTitleIsBrowser() {
        return this.launchTitleIsBrowser;
    }
    
    public String getPivotTitle() {
        return this.pivotTitle;
    }
    
    public Class<? extends ViewModelBase> getSearchFilterSetterClass() {
        return this.searchFilterSetterClass;
    }
    
    public String getSearchTag() {
        return this.searchTag;
    }
    
    public String getSelectedAchievementKey() {
        return this.selectedAchievementKey;
    }
    
    public String getSelectedDataSource() {
        return this.selectedDataSource;
    }
    
    public String getSelectedGamertag() {
        return this.selectedGamertag;
    }
    
    public ArrayList<URI> getSelectedImages() {
        return this.selectedImages;
    }
    
    public MultiSelection<FriendSelectorItem> getSelectedRecipients() {
        if (this.selectedRecipients == null) {
            this.selectedRecipients = new MultiSelection<FriendSelectorItem>();
        }
        return this.selectedRecipients;
    }
    
    public long getSelectedTitleId() {
        return this.titleId;
    }
    
    public String getSelectedXuid() {
        if (JavaUtil.isNullOrEmpty(this.selectedXuid)) {
            return ProjectSpecificDataProvider.getInstance().getXuidString();
        }
        return this.selectedXuid;
    }
    
    public boolean getShowLoginError() {
        final boolean showLoginError = this.showLoginError;
        this.showLoginError = false;
        return showLoginError;
    }
    
    public void resetGlobalParameters() {
        this.selectedGamertag = null;
        this.selectedAchievementKey = null;
        this.selectedDataSource = null;
        this.isLoggedIn = false;
        this.showLoginError = false;
        this.isOffline = true;
        this.searchTag = null;
        this.selectedImages = null;
        this.titleId = 0L;
        this.forceRefreshVMs = null;
    }
    
    public void setAutoLoginStarted(final boolean autoLoginStarted) {
        this.autoLoginStarted = autoLoginStarted;
    }
    
    public void setDefaultScreenClass(final Class<? extends ActivityBase> defaultScreenClass) {
        this.defaultScreenClass = defaultScreenClass;
    }
    
    public void setForceRefreshProfile(final boolean forceRefreshProfile) {
        this.forceRefreshProfile = forceRefreshProfile;
    }
    
    public void setFriendListUpdated(final boolean friendListUpdated) {
        this.friendListUpdated = friendListUpdated;
    }
    
    public void setHideCollectionFilter(final boolean hideCollectionFilter) {
        this.hideCollectionFilter = hideCollectionFilter;
    }
    
    public void setIsOffline(final boolean isOffline) {
        this.isOffline = isOffline;
    }
    
    public void setLaunchTitleIsBrowser(final boolean launchTitleIsBrowser) {
        this.launchTitleIsBrowser = launchTitleIsBrowser;
    }
    
    public void setLoggedIn(final boolean isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
    }
    
    public void setLoginErrorCode(final long errorCodeForLogin) {
        this.errorCodeForLogin = errorCodeForLogin;
    }
    
    public void setPivotTitle(final String pivotTitle) {
        this.pivotTitle = pivotTitle;
    }
    
    public void setSearchTag(final String searchTag) {
        if (searchTag != null && searchTag.length() > 120) {
            this.searchTag = searchTag.substring(0, 120);
            return;
        }
        this.searchTag = searchTag;
    }
    
    public void setSelectedAchievementKey(final String selectedAchievementKey) {
        this.selectedAchievementKey = selectedAchievementKey;
    }
    
    public void setSelectedDataSource(final String selectedDataSource) {
        this.selectedDataSource = selectedDataSource;
    }
    
    public void setSelectedGamertag(final String selectedGamertag) {
        this.selectedGamertag = selectedGamertag;
    }
    
    public void setSelectedImages(final ArrayList<URI> selectedImages) {
        this.selectedImages = selectedImages;
    }
    
    public void setSelectedTitleId(final long titleId) {
        this.titleId = titleId;
    }
    
    public void setSelectedXuid(final String selectedXuid) {
        this.selectedXuid = selectedXuid;
    }
    
    public void setShowLoginError(final boolean showLoginError) {
        this.showLoginError = showLoginError;
    }
    
    private static class XLEGlobalDataHolder
    {
        public static final XLEGlobalData instance;
        
        static {
            instance = new XLEGlobalData(null);
        }
    }
}
