package com.microsoft.xbox.service.model;

import com.microsoft.xbox.xle.app.*;
import com.microsoft.xboxtcui.*;
import com.microsoft.xbox.toolkit.*;
import java.net.*;
import android.util.*;
import java.util.*;
import java.util.function.*;
import com.microsoft.xbox.toolkit.network.*;
import com.microsoft.xbox.service.network.managers.xblshared.*;
import com.microsoft.xbox.service.network.managers.*;
import com.microsoft.xbox.xle.viewmodel.*;
import com.microsoft.xbox.service.model.privacy.*;
import com.microsoft.xbox.service.model.sls.*;

public class ProfileModel extends ModelBase<ProfileData>
{
    private static final int MAX_PROFILE_MODELS = 20;
    private static final long friendsDataLifetime = 180000L;
    private static ProfileModel meProfileInstance;
    private static ThreadSafeFixedSizeHashtable<String, ProfileModel> profileModelCache;
    private static final long profilePresenceDataLifetime = 180000L;
    private AddFollowingUserResponseContainer.AddFollowingUserResponse addUserToFollowingResponse;
    private SingleEntryLoadingStatus addingUserToFavoriteListLoadingStatus;
    private SingleEntryLoadingStatus addingUserToFollowingListLoadingStatus;
    private SingleEntryLoadingStatus addingUserToMutedListLoadingStatus;
    private SingleEntryLoadingStatus addingUserToNeverListLoadingStatus;
    private SingleEntryLoadingStatus addingUserToShareIdentityListLoadingStatus;
    private ArrayList<FollowersData> favorites;
    private String firstName;
    private ArrayList<FollowersData> following;
    private ArrayList<FollowingSummaryResult.People> followingSummaries;
    private String lastName;
    private Date lastRefreshMutedList;
    private Date lastRefreshNeverList;
    private Date lastRefreshPeopleHubRecommendations;
    private Date lastRefreshPresenceData;
    private Date lastRefreshProfileSummary;
    private MutedListResultContainer.MutedListResult mutedList;
    private SingleEntryLoadingStatus mutedListLoadingStatus;
    private NeverListResultContainer.NeverListResult neverList;
    private SingleEntryLoadingStatus neverListLoadingStatus;
    private IPeopleHubResult.PeopleHubPersonSummary peopleHubPersonSummary;
    private ArrayList<FollowersData> peopleHubRecommendations;
    private IPeopleHubResult.PeopleHubPeopleSummary peopleHubRecommendationsRaw;
    private IFollowerPresenceResult.UserPresence presenceData;
    private SingleEntryLoadingStatus presenceDataLoadingStatus;
    private String profileImageUrl;
    private ProfileSummaryResultContainer.ProfileSummaryResult profileSummary;
    private SingleEntryLoadingStatus profileSummaryLoadingStatus;
    private IUserProfileResult.ProfileUser profileUser;
    private SingleEntryLoadingStatus removingUserFromFavoriteListLoadingStatus;
    private SingleEntryLoadingStatus removingUserFromFollowingListLoadingStatus;
    private SingleEntryLoadingStatus removingUserFromMutedListLoadingStatus;
    private SingleEntryLoadingStatus removingUserFromNeverListLoadingStatus;
    private SingleEntryLoadingStatus removingUserFromShareIdentityListLoadingStatus;
    private boolean shareRealName;
    private String shareRealNameStatus;
    private boolean sharingRealNameTransitively;
    private SingleEntryLoadingStatus submitFeedbackForUserLoadingStatus;
    private String xuid;
    
    static {
        ProfileModel.profileModelCache = new ThreadSafeFixedSizeHashtable<String, ProfileModel>(20);
    }
    
    private ProfileModel(final String xuid) {
        this.xuid = xuid;
        this.mutedListLoadingStatus = new SingleEntryLoadingStatus();
        this.neverListLoadingStatus = new SingleEntryLoadingStatus();
        this.addingUserToNeverListLoadingStatus = new SingleEntryLoadingStatus();
        this.removingUserFromNeverListLoadingStatus = new SingleEntryLoadingStatus();
        this.addingUserToFavoriteListLoadingStatus = new SingleEntryLoadingStatus();
        this.addingUserToShareIdentityListLoadingStatus = new SingleEntryLoadingStatus();
        this.removingUserFromShareIdentityListLoadingStatus = new SingleEntryLoadingStatus();
        this.removingUserFromFavoriteListLoadingStatus = new SingleEntryLoadingStatus();
        this.addingUserToFollowingListLoadingStatus = new SingleEntryLoadingStatus();
        this.removingUserFromFollowingListLoadingStatus = new SingleEntryLoadingStatus();
        this.addingUserToMutedListLoadingStatus = new SingleEntryLoadingStatus();
        this.removingUserFromMutedListLoadingStatus = new SingleEntryLoadingStatus();
        this.submitFeedbackForUserLoadingStatus = new SingleEntryLoadingStatus();
    }
    
    private void buildRecommendationsList(final boolean b) {
        this.peopleHubRecommendations = new ArrayList<FollowersData>();
        if (b) {
            this.peopleHubRecommendations.add(0, new RecommendationsPeopleData(true, FollowersData.DummyType.DUMMY_LINK_TO_FACEBOOK));
        }
        final IPeopleHubResult.PeopleHubPeopleSummary peopleHubRecommendationsRaw = this.peopleHubRecommendationsRaw;
        if (peopleHubRecommendationsRaw != null && !XLEUtil.isNullOrEmpty(peopleHubRecommendationsRaw.people)) {
            final Iterator<IPeopleHubResult.PeopleHubPersonSummary> iterator = this.peopleHubRecommendationsRaw.people.iterator();
            while (iterator.hasNext()) {
                this.peopleHubRecommendations.add(new RecommendationsPeopleData(iterator.next()));
            }
        }
    }
    
    public static int getDefaultColor() {
        return XboxTcuiSdk.getResources().getColor(XLERValueHelper.getColorRValue("XboxOneGreen"));
    }
    
    public static ProfileModel getMeProfileModel() {
        if (ProjectSpecificDataProvider.getInstance().getXuidString() == null) {
            return null;
        }
        if (ProfileModel.meProfileInstance == null) {
            ProfileModel.meProfileInstance = new ProfileModel(ProjectSpecificDataProvider.getInstance().getXuidString());
        }
        return ProfileModel.meProfileInstance;
    }
    
    private String getProfileImageUrl() {
        final String profileImageUrl = this.profileImageUrl;
        if (profileImageUrl != null) {
            return profileImageUrl;
        }
        return this.profileImageUrl = this.getProfileSettingValue(UserProfileSetting.GameDisplayPicRaw);
    }
    
    public static ProfileModel getProfileModel(final String s) {
        if (JavaUtil.isNullOrEmpty(s)) {
            throw new IllegalArgumentException();
        }
        if (JavaUtil.stringsEqualCaseInsensitive(s, ProjectSpecificDataProvider.getInstance().getXuidString())) {
            if (ProfileModel.meProfileInstance == null) {
                ProfileModel.meProfileInstance = new ProfileModel(s);
            }
            return ProfileModel.meProfileInstance;
        }
        ProfileModel profileModel;
        if ((profileModel = ProfileModel.profileModelCache.get(s)) == null) {
            profileModel = new ProfileModel(s);
            ProfileModel.profileModelCache.put(s, profileModel);
        }
        return profileModel;
    }
    
    private String getProfileSettingValue(final UserProfileSetting userProfileSetting) {
        final IUserProfileResult.ProfileUser profileUser = this.profileUser;
        if (profileUser != null && profileUser.settings != null) {
            for (final IUserProfileResult.Settings settings : this.profileUser.settings) {
                if (settings.id != null && settings.id.equals(userProfileSetting.toString())) {
                    return settings.value;
                }
            }
        }
        return null;
    }
    
    private static boolean hasPrivilege(final String s) {
        final String privileges = ProjectSpecificDataProvider.getInstance().getPrivileges();
        return !JavaUtil.isNullOrEmpty(privileges) && privileges.contains(s);
    }
    
    public static boolean hasPrivilegeToAddFriend() {
        return hasPrivilege("255");
    }
    
    public static boolean hasPrivilegeToSendMessage() {
        return hasPrivilege("252");
    }
    
    public static boolean isMeXuid(final String s) {
        final String xuidString = ProjectSpecificDataProvider.getInstance().getXuidString();
        return xuidString != null && s != null && s.compareToIgnoreCase(xuidString) == 0;
    }
    
    private void onAddUserToFavoriteListCompleted(final AsyncResult<Boolean> asyncResult, final String s) {
        if (asyncResult.getStatus() == AsyncActionStatus.SUCCESS && asyncResult.getResult()) {
            if (this.following == null) {
                return;
            }
            final ArrayList<Object> favorites = new ArrayList<Object>();
            for (final FollowersData followersData : this.following) {
                if (followersData.xuid.equals(s)) {
                    followersData.isFavorite = true;
                }
                if (followersData.isFavorite) {
                    favorites.add(followersData);
                }
            }
            Collections.sort(favorites, (Comparator<? super Object>)new FollowingAndFavoritesComparator());
            this.favorites = (ArrayList<FollowersData>)favorites;
            this.notifyObservers(new AsyncResult<UpdateData>(new UpdateData(UpdateType.UpdateFriend, true), this, null));
        }
    }
    
    private void onAddUserToFollowingListCompleted(final AsyncResult<AddFollowingUserResponseContainer.AddFollowingUserResponse> asyncResult, final String xuid) {
        final ProfileModel profileModel = getProfileModel(xuid);
        XLEAssert.assertNotNull(profileModel);
        this.addUserToFollowingResponse = asyncResult.getResult();
        if (asyncResult.getStatus() == AsyncActionStatus.SUCCESS) {
            final AddFollowingUserResponseContainer.AddFollowingUserResponse addUserToFollowingResponse = this.addUserToFollowingResponse;
            if (addUserToFollowingResponse != null && addUserToFollowingResponse.getAddFollowingRequestStatus()) {
                final ArrayList<Object> following = new ArrayList<Object>();
                final ArrayList<FollowersData> following2 = this.following;
                int n2;
                if (following2 != null) {
                    final Iterator<FollowersData> iterator = following2.iterator();
                    int n = 0;
                    while (true) {
                        n2 = n;
                        if (!iterator.hasNext()) {
                            break;
                        }
                        final FollowersData followersData = iterator.next();
                        following.add(followersData);
                        if (!followersData.xuid.equals(xuid)) {
                            continue;
                        }
                        n = 1;
                    }
                }
                else {
                    n2 = 0;
                }
                if (n2 == 0) {
                    final FollowersData followersData2 = new FollowersData();
                    followersData2.xuid = xuid;
                    followersData2.isFavorite = false;
                    followersData2.status = UserStatus.Offline;
                    followersData2.userProfileData = new UserProfileData();
                    followersData2.userProfileData.accountTier = profileModel.getAccountTier();
                    followersData2.userProfileData.appDisplayName = profileModel.getAppDisplayName();
                    followersData2.userProfileData.gamerScore = profileModel.getGamerScore();
                    followersData2.userProfileData.gamerTag = profileModel.getGamerTag();
                    followersData2.userProfileData.profileImageUrl = profileModel.getProfileImageUrl();
                    following.add(followersData2);
                    Collections.sort(following, (Comparator<? super Object>)new FollowingAndFavoritesComparator());
                }
                this.following = (ArrayList<FollowersData>)following;
                this.notifyObservers(new AsyncResult<UpdateData>(new UpdateData(UpdateType.UpdateFriend, true), this, null));
                return;
            }
        }
        if (asyncResult.getStatus() != AsyncActionStatus.SUCCESS || (this.addUserToFollowingResponse.code != 1028 && !this.addUserToFollowingResponse.getAddFollowingRequestStatus())) {
            this.addUserToFollowingResponse = null;
        }
    }
    
    private void onAddUserToShareIdentityCompleted(final AsyncResult<Boolean> asyncResult, final ArrayList<String> list) {
        if (asyncResult.getStatus() == AsyncActionStatus.SUCCESS && asyncResult.getResult()) {
            final Iterator<String> iterator = list.iterator();
            while (iterator.hasNext()) {
                final ProfileSummaryResultContainer.ProfileSummaryResult profileSummaryData = getProfileModel(iterator.next()).getProfileSummaryData();
                if (profileSummaryData != null) {
                    profileSummaryData.hasCallerMarkedTargetAsIdentityShared = true;
                }
            }
            final ProfileModel meProfileModel = getMeProfileModel();
            final ArrayList<FollowingSummaryResult.People> profileFollowingSummaryData = meProfileModel.getProfileFollowingSummaryData();
            if (!XLEUtil.isNullOrEmpty((Iterable<Object>)profileFollowingSummaryData)) {
                for (final String s : list) {
                    for (final FollowingSummaryResult.People people : profileFollowingSummaryData) {
                        if (people.xuid.equalsIgnoreCase(s)) {
                            people.isIdentityShared = true;
                            break;
                        }
                    }
                }
                meProfileModel.setProfileFollowingSummaryData(profileFollowingSummaryData);
            }
        }
    }
    
    private void onGetMutedListCompleted(final AsyncResult<MutedListResultContainer.MutedListResult> asyncResult) {
        if (asyncResult.getStatus() == AsyncActionStatus.SUCCESS) {
            final MutedListResultContainer.MutedListResult mutedList = asyncResult.getResult();
            this.lastRefreshMutedList = new Date();
            if (mutedList != null) {
                this.mutedList = mutedList;
                return;
            }
            this.mutedList = new MutedListResultContainer.MutedListResult();
        }
    }
    
    private void onGetNeverListCompleted(final AsyncResult<NeverListResultContainer.NeverListResult> asyncResult) {
        if (asyncResult.getStatus() == AsyncActionStatus.SUCCESS) {
            final NeverListResultContainer.NeverListResult neverList = asyncResult.getResult();
            this.lastRefreshNeverList = new Date();
            if (neverList != null) {
                this.neverList = neverList;
                return;
            }
            this.neverList = new NeverListResultContainer.NeverListResult();
        }
    }
    
    private void onGetPeopleHubPersonDataCompleted(final AsyncResult<IPeopleHubResult.PeopleHubPersonSummary> asyncResult) {
        if (asyncResult.getStatus() == AsyncActionStatus.SUCCESS) {
            this.peopleHubPersonSummary = asyncResult.getResult();
        }
    }
    
    private void onGetPeopleHubRecommendationsCompleted(final AsyncResult<IPeopleHubResult.PeopleHubPeopleSummary> asyncResult) {
        if (asyncResult.getStatus() == AsyncActionStatus.SUCCESS) {
            final IPeopleHubResult.PeopleHubPeopleSummary peopleHubRecommendationsRaw = asyncResult.getResult();
            if (peopleHubRecommendationsRaw == null) {
                this.peopleHubRecommendationsRaw = null;
                this.peopleHubRecommendations = null;
                return;
            }
            this.peopleHubRecommendationsRaw = peopleHubRecommendationsRaw;
            this.lastRefreshPeopleHubRecommendations = new Date();
        }
    }
    
    private void onGetPresenceDataCompleted(final AsyncResult<IFollowerPresenceResult.UserPresence> asyncResult) {
        if (asyncResult.getStatus() == AsyncActionStatus.SUCCESS) {
            this.lastRefreshPresenceData = new Date();
            this.presenceData = asyncResult.getResult();
        }
    }
    
    private void onGetProfileSummaryCompleted(final AsyncResult<ProfileSummaryResultContainer.ProfileSummaryResult> asyncResult) {
        if (asyncResult.getStatus() == AsyncActionStatus.SUCCESS) {
            final ProfileSummaryResultContainer.ProfileSummaryResult profileSummary = asyncResult.getResult();
            this.lastRefreshProfileSummary = new Date();
            this.profileSummary = profileSummary;
            this.notifyObservers(new AsyncResult<UpdateData>(new UpdateData(UpdateType.ActivityAlertsSummary, true), this, null));
        }
    }
    
    private void onPutUserToMutedListCompleted(final AsyncResult<Boolean> asyncResult, final String s) {
        if (asyncResult.getStatus() == AsyncActionStatus.SUCCESS && asyncResult.getResult()) {
            if (this.mutedList == null) {
                this.mutedList = new MutedListResultContainer.MutedListResult();
            }
            if (!this.mutedList.contains(s)) {
                this.mutedList.add(s);
            }
        }
    }
    
    private void onPutUserToNeverListCompleted(final AsyncResult<Boolean> asyncResult, final String s) {
        if (asyncResult.getStatus() == AsyncActionStatus.SUCCESS && asyncResult.getResult()) {
            if (this.neverList == null) {
                this.neverList = new NeverListResultContainer.NeverListResult();
            }
            if (!this.neverList.contains(s)) {
                this.neverList.add(s);
            }
        }
    }
    
    private void onRemoveUserFromFavoriteListCompleted(final AsyncResult<Boolean> asyncResult, final String s) {
        if (asyncResult.getStatus() == AsyncActionStatus.SUCCESS && asyncResult.getResult()) {
            if (this.following == null) {
                return;
            }
            final ArrayList<FollowersData> favorites = new ArrayList<FollowersData>();
            for (final FollowersData followersData : this.following) {
                if (followersData.xuid.equals(s)) {
                    followersData.isFavorite = false;
                }
                if (followersData.isFavorite) {
                    favorites.add(followersData);
                }
            }
            this.favorites = favorites;
            this.notifyObservers(new AsyncResult<UpdateData>(new UpdateData(UpdateType.UpdateFriend, true), this, null));
        }
    }
    
    private void onRemoveUserFromFollowingListCompleted(final AsyncResult<Boolean> asyncResult, final String s) {
        if (asyncResult.getStatus() == AsyncActionStatus.SUCCESS && asyncResult.getResult() && this.following != null) {
            final ArrayList<FollowersData> following = new ArrayList<FollowersData>();
            final ArrayList<FollowersData> favorites = new ArrayList<FollowersData>();
            for (final FollowersData followersData : this.following) {
                if (!followersData.xuid.equals(s)) {
                    following.add(followersData);
                    if (!followersData.isFavorite) {
                        continue;
                    }
                    favorites.add(followersData);
                }
            }
            this.following = following;
            this.favorites = favorites;
            this.notifyObservers(new AsyncResult<UpdateData>(new UpdateData(UpdateType.UpdateFriend, true), this, null));
        }
    }
    
    private void onRemoveUserFromMutedListCompleted(final AsyncResult<Boolean> asyncResult, final String s) {
        if (asyncResult.getStatus() == AsyncActionStatus.SUCCESS && asyncResult.getResult()) {
            final MutedListResultContainer.MutedListResult mutedList = this.mutedList;
            if (mutedList != null && mutedList.contains(s)) {
                this.mutedList.remove(s);
            }
        }
    }
    
    private void onRemoveUserFromNeverListCompleted(final AsyncResult<Boolean> asyncResult, final String s) {
        if (asyncResult.getStatus() == AsyncActionStatus.SUCCESS && asyncResult.getResult()) {
            final NeverListResultContainer.NeverListResult neverList = this.neverList;
            if (neverList != null && neverList.contains(s)) {
                this.neverList.remove(s);
            }
        }
    }
    
    private void onRemoveUserFromShareIdentityCompleted(final AsyncResult<Boolean> asyncResult, final ArrayList<String> list) {
        if (asyncResult.getStatus() == AsyncActionStatus.SUCCESS && asyncResult.getResult()) {
            final Iterator<String> iterator = list.iterator();
            while (iterator.hasNext()) {
                final ProfileSummaryResultContainer.ProfileSummaryResult profileSummaryData = getProfileModel(iterator.next()).getProfileSummaryData();
                if (profileSummaryData != null) {
                    profileSummaryData.hasCallerMarkedTargetAsIdentityShared = false;
                }
            }
            final ProfileModel meProfileModel = getMeProfileModel();
            final ArrayList<FollowingSummaryResult.People> profileFollowingSummaryData = meProfileModel.getProfileFollowingSummaryData();
            if (!XLEUtil.isNullOrEmpty((Iterable<Object>)profileFollowingSummaryData)) {
                for (final String s : list) {
                    for (final FollowingSummaryResult.People people : profileFollowingSummaryData) {
                        if (people.xuid.equalsIgnoreCase(s)) {
                            people.isIdentityShared = false;
                            break;
                        }
                    }
                }
                meProfileModel.setProfileFollowingSummaryData(profileFollowingSummaryData);
            }
        }
    }
    
    private void onSubmitFeedbackForUserCompleted(final AsyncResult<Boolean> asyncResult) {
    }
    
    public static void reset() {
        XLEAssert.assertTrue(Thread.currentThread() == ThreadManager.UIThread);
        final Enumeration<ProfileModel> elements = ProfileModel.profileModelCache.elements();
        while (elements.hasMoreElements()) {
            elements.nextElement().clearObserver();
        }
        final ProfileModel meProfileInstance = ProfileModel.meProfileInstance;
        if (meProfileInstance != null) {
            meProfileInstance.clearObserver();
            ProfileModel.meProfileInstance = null;
        }
        ProfileModel.profileModelCache = new ThreadSafeFixedSizeHashtable<String, ProfileModel>(20);
    }
    
    private void updateWithProfileData(final AsyncResult<ProfileData> asyncResult, final boolean b) {
        this.updateWithNewData(asyncResult);
        if (b) {
            this.invalidateData();
        }
    }
    
    public AsyncResult<Boolean> addUserToFavoriteList(final boolean b, final String s) {
        XLEAssert.assertIsNotUIThread();
        XLEAssert.assertNotNull(this.xuid);
        XLEAssert.assertNotNull(s);
        return DataLoadUtil.Load(b, this.lifetime, null, this.addingUserToFavoriteListLoadingStatus, (IDataLoaderRunnable<Boolean>)new AddUserToFavoriteListRunner(this, s));
    }
    
    public AsyncResult<AddFollowingUserResponseContainer.AddFollowingUserResponse> addUserToFollowingList(final boolean b, final String s) {
        XLEAssert.assertIsNotUIThread();
        XLEAssert.assertNotNull(this.xuid);
        XLEAssert.assertNotNull(s);
        return DataLoadUtil.Load(b, this.lifetime, null, this.addingUserToFollowingListLoadingStatus, (IDataLoaderRunnable<AddFollowingUserResponseContainer.AddFollowingUserResponse>)new AddUserToFollowingListRunner(this, s));
    }
    
    public AsyncResult<Boolean> addUserToMutedList(final boolean b, final String s) {
        XLEAssert.assertIsNotUIThread();
        XLEAssert.assertNotNull(this.xuid);
        XLEAssert.assertNotNull(s);
        return DataLoadUtil.Load(b, this.lifetime, null, this.addingUserToMutedListLoadingStatus, (IDataLoaderRunnable<Boolean>)new PutUserToMutedListRunner(this, this.xuid, s));
    }
    
    public AsyncResult<Boolean> addUserToNeverList(final boolean b, final String s) {
        XLEAssert.assertIsNotUIThread();
        XLEAssert.assertNotNull(this.xuid);
        XLEAssert.assertNotNull(s);
        return DataLoadUtil.Load(b, this.lifetime, null, this.addingUserToNeverListLoadingStatus, (IDataLoaderRunnable<Boolean>)new PutUserToNeverListRunner(this, this.xuid, s));
    }
    
    public AsyncResult<Boolean> addUserToShareIdentity(final boolean b, final ArrayList<String> list) {
        XLEAssert.assertIsNotUIThread();
        XLEAssert.assertNotNull(this.xuid);
        return DataLoadUtil.Load(b, this.lifetime, null, this.addingUserToShareIdentityListLoadingStatus, (IDataLoaderRunnable<Boolean>)new AddUsersToShareIdentityListRunner(this, list));
    }
    
    public String getAccountTier() {
        return this.getProfileSettingValue(UserProfileSetting.AccountTier);
    }
    
    public AddFollowingUserResponseContainer.AddFollowingUserResponse getAddUserToFollowingResult() {
        return this.addUserToFollowingResponse;
    }
    
    public String getAppDisplayName() {
        return this.getProfileSettingValue(UserProfileSetting.AppDisplayName);
    }
    
    public String getBio() {
        return this.getProfileSettingValue(UserProfileSetting.Bio);
    }
    
    public ArrayList<FollowersData> getFavorites() {
        return this.favorites;
    }
    
    public ArrayList<FollowersData> getFollowingData() {
        return this.following;
    }
    
    public String getGamerPicImageUrl() {
        return this.getProfileImageUrl();
    }
    
    public String getGamerScore() {
        return this.getProfileSettingValue(UserProfileSetting.Gamerscore);
    }
    
    public String getGamerTag() {
        return this.getProfileSettingValue(UserProfileSetting.Gamertag);
    }
    
    public String getLocation() {
        return this.getProfileSettingValue(UserProfileSetting.Location);
    }
    
    public int getMaturityLevel() {
        final IUserProfileResult.ProfileUser profileUser = this.profileUser;
        if (profileUser != null) {
            return profileUser.getMaturityLevel();
        }
        return 0;
    }
    
    public MutedListResultContainer.MutedListResult getMutedList() {
        return this.mutedList;
    }
    
    public NeverListResultContainer.NeverListResult getNeverListData() {
        return this.neverList;
    }
    
    public int getNumberOfFollowers() {
        final ProfileSummaryResultContainer.ProfileSummaryResult profileSummary = this.profileSummary;
        if (profileSummary != null) {
            return profileSummary.targetFollowerCount;
        }
        return 0;
    }
    
    public int getNumberOfFollowing() {
        final ProfileSummaryResultContainer.ProfileSummaryResult profileSummary = this.profileSummary;
        if (profileSummary != null) {
            return profileSummary.targetFollowingCount;
        }
        return 0;
    }
    
    public IPeopleHubResult.PeopleHubPersonSummary getPeopleHubPersonSummary() {
        return this.peopleHubPersonSummary;
    }
    
    public IPeopleHubResult.PeopleHubPeopleSummary getPeopleHubRecommendationsRawData() {
        return this.peopleHubRecommendationsRaw;
    }
    
    public int getPreferedColor() {
        final IUserProfileResult.ProfileUser profileUser = this.profileUser;
        if (profileUser != null && profileUser.colors != null) {
            return this.profileUser.colors.getPrimaryColor();
        }
        return getDefaultColor();
    }
    
    public IFollowerPresenceResult.UserPresence getPresenceData() {
        return this.presenceData;
    }
    
    public ArrayList<FollowingSummaryResult.People> getProfileFollowingSummaryData() {
        return this.followingSummaries;
    }
    
    public ProfileSummaryResultContainer.ProfileSummaryResult getProfileSummaryData() {
        return this.profileSummary;
    }
    
    public String getRealName() {
        if (this.shareRealName) {
            return this.getProfileSettingValue(UserProfileSetting.RealName);
        }
        return null;
    }
    
    public String getShareRealNameStatus() {
        return this.shareRealNameStatus;
    }
    
    public ArrayList<URI> getWatermarkUris() {
        final ArrayList<URI> list = new ArrayList<URI>();
        final String profileSettingValue = this.getProfileSettingValue(UserProfileSetting.TenureLevel);
        final boolean nullOrEmpty = JavaUtil.isNullOrEmpty(profileSettingValue);
        int i = 0;
        if (!nullOrEmpty && !profileSettingValue.equalsIgnoreCase("0")) {
            try {
                final String tenureWatermarkUrlFormat = XboxLiveEnvironment.Instance().getTenureWatermarkUrlFormat();
                String string = profileSettingValue;
                if (profileSettingValue.length() == 1) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("0");
                    sb.append(profileSettingValue);
                    string = sb.toString();
                }
                list.add(new URI(String.format(tenureWatermarkUrlFormat, string)));
            }
            catch (URISyntaxException ex) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Failed to create URI for tenure watermark: ");
                sb2.append(ex.toString());
                XLEAssert.fail(sb2.toString());
            }
        }
        final String profileSettingValue2 = this.getProfileSettingValue(UserProfileSetting.Watermarks);
        if (!JavaUtil.isNullOrEmpty(profileSettingValue2)) {
            for (String[] split = profileSettingValue2.split("\\|"); i < split.length; ++i) {
                final String s = split[i];
                try {
                    list.add(new URI(XboxLiveEnvironment.Instance().getWatermarkUrl(s)));
                }
                catch (URISyntaxException ex2) {
                    final StringBuilder sb3 = new StringBuilder();
                    sb3.append("Failed to create URI for watermark ");
                    sb3.append(s);
                    sb3.append(" : ");
                    sb3.append(ex2.toString());
                    XLEAssert.fail(sb3.toString());
                }
            }
        }
        return list;
    }
    
    public String getXuid() {
        return this.xuid;
    }
    
    public boolean hasCallerMarkedTargetAsFavorite() {
        final ProfileSummaryResultContainer.ProfileSummaryResult profileSummary = this.profileSummary;
        return profileSummary != null && profileSummary.hasCallerMarkedTargetAsFavorite;
    }
    
    public boolean hasCallerMarkedTargetAsIdentityShared() {
        final ProfileSummaryResultContainer.ProfileSummaryResult profileSummary = this.profileSummary;
        return profileSummary != null && profileSummary.hasCallerMarkedTargetAsIdentityShared;
    }
    
    public boolean isCallerFollowingTarget() {
        final ProfileSummaryResultContainer.ProfileSummaryResult profileSummary = this.profileSummary;
        return profileSummary != null && profileSummary.isCallerFollowingTarget;
    }
    
    public boolean isMeProfile() {
        return isMeXuid(this.xuid);
    }
    
    public boolean isTargetFollowingCaller() {
        final ProfileSummaryResultContainer.ProfileSummaryResult profileSummary = this.profileSummary;
        return profileSummary != null && profileSummary.isTargetFollowingCaller;
    }
    
    public void loadAsync(final boolean b) {
        this.loadInternal(b, UpdateType.MeProfileData, new GetProfileRunner(this, this.xuid, false));
    }
    
    public AsyncResult<IPeopleHubResult.PeopleHubPeopleSummary> loadPeopleHubRecommendations(final boolean b) {
        XLEAssert.assertIsNotUIThread();
        XLEAssert.assertNotNull(this.xuid);
        return DataLoadUtil.Load(b, 180000L, this.lastRefreshPeopleHubRecommendations, new SingleEntryLoadingStatus(), (IDataLoaderRunnable<IPeopleHubResult.PeopleHubPeopleSummary>)new GetPeopleHubRecommendationRunner(this, this.xuid));
    }
    
    public AsyncResult<IFollowerPresenceResult.UserPresence> loadPresenceData(final boolean b) {
        if (this.presenceDataLoadingStatus == null) {
            this.presenceDataLoadingStatus = new SingleEntryLoadingStatus();
        }
        return DataLoadUtil.Load(b, 180000L, this.lastRefreshPresenceData, this.presenceDataLoadingStatus, (IDataLoaderRunnable<IFollowerPresenceResult.UserPresence>)new GetPresenceDataRunner(this, this.xuid));
    }
    
    public AsyncResult<ProfileSummaryResultContainer.ProfileSummaryResult> loadProfileSummary(final boolean b) {
        if (this.profileSummaryLoadingStatus == null) {
            this.profileSummaryLoadingStatus = new SingleEntryLoadingStatus();
        }
        return DataLoadUtil.Load(b, this.lifetime, this.lastRefreshProfileSummary, this.profileSummaryLoadingStatus, (IDataLoaderRunnable<ProfileSummaryResultContainer.ProfileSummaryResult>)new GetProfileSummaryRunner(this, this.xuid));
    }
    
    public AsyncResult<ProfileData> loadSync(final boolean b) {
        return this.loadSync(b, false);
    }
    
    public AsyncResult<ProfileData> loadSync(final boolean b, final boolean b2) {
        return super.loadData(b, new GetProfileRunner(this, this.xuid, b2));
    }
    
    public AsyncResult<MutedListResultContainer.MutedListResult> loadUserMutedList(final boolean b) {
        return DataLoadUtil.Load(b, this.lifetime, this.lastRefreshMutedList, this.mutedListLoadingStatus, (IDataLoaderRunnable<MutedListResultContainer.MutedListResult>)new GetMutedListRunner(this, this.xuid));
    }
    
    public AsyncResult<NeverListResultContainer.NeverListResult> loadUserNeverList(final boolean b) {
        return DataLoadUtil.Load(b, this.lifetime, this.lastRefreshNeverList, this.neverListLoadingStatus, (IDataLoaderRunnable<NeverListResultContainer.NeverListResult>)new GetNeverListRunner(this, this.xuid));
    }
    
    public AsyncResult<Boolean> removeUserFromFavoriteList(final boolean b, final String s) {
        XLEAssert.assertIsNotUIThread();
        XLEAssert.assertNotNull(this.xuid);
        XLEAssert.assertNotNull(s);
        return DataLoadUtil.Load(b, this.lifetime, null, this.removingUserFromFavoriteListLoadingStatus, (IDataLoaderRunnable<Boolean>)new RemoveUserFromFavoriteListRunner(this, s));
    }
    
    public AsyncResult<Boolean> removeUserFromFollowingList(final boolean b, final String s) {
        XLEAssert.assertIsNotUIThread();
        XLEAssert.assertNotNull(this.xuid);
        XLEAssert.assertNotNull(s);
        return DataLoadUtil.Load(b, this.lifetime, null, this.removingUserFromFollowingListLoadingStatus, (IDataLoaderRunnable<Boolean>)new RemoveUserFromFollowingListRunner(this, s));
    }
    
    public AsyncResult<Boolean> removeUserFromMutedList(final boolean b, final String s) {
        XLEAssert.assertIsNotUIThread();
        XLEAssert.assertNotNull(this.xuid);
        XLEAssert.assertNotNull(s);
        return DataLoadUtil.Load(b, this.lifetime, null, this.removingUserFromMutedListLoadingStatus, (IDataLoaderRunnable<Boolean>)new RemoveUserFromMutedListRunner(this, this.xuid, s));
    }
    
    public AsyncResult<Boolean> removeUserFromNeverList(final boolean b, final String s) {
        XLEAssert.assertIsNotUIThread();
        XLEAssert.assertNotNull(this.xuid);
        XLEAssert.assertNotNull(s);
        return DataLoadUtil.Load(b, this.lifetime, null, this.removingUserFromNeverListLoadingStatus, (IDataLoaderRunnable<Boolean>)new RemoveUserFromNeverListRunner(this, this.xuid, s));
    }
    
    public AsyncResult<Boolean> removeUserFromShareIdentity(final boolean b, final ArrayList<String> list) {
        XLEAssert.assertIsNotUIThread();
        XLEAssert.assertNotNull(this.xuid);
        return DataLoadUtil.Load(b, this.lifetime, null, this.removingUserFromShareIdentityListLoadingStatus, (IDataLoaderRunnable<Boolean>)new RemoveUsersFromShareIdentityListRunner(this, list));
    }
    
    public void setFirstName(final String firstName) {
        this.firstName = firstName;
    }
    
    public void setLastName(final String lastName) {
        this.lastName = lastName;
    }
    
    public void setProfileFollowingSummaryData(final ArrayList<FollowingSummaryResult.People> followingSummaries) {
        this.followingSummaries = followingSummaries;
    }
    
    public boolean shouldRefreshPresenceData() {
        return XLEUtil.shouldRefresh(this.lastRefreshPresenceData, this.lifetime);
    }
    
    public boolean shouldRefreshProfileSummary() {
        return XLEUtil.shouldRefresh(this.lastRefreshProfileSummary, this.lifetime);
    }
    
    public AsyncResult<Boolean> submitFeedbackForUser(final boolean b, final FeedbackType feedbackType, final String s) {
        XLEAssert.assertIsNotUIThread();
        XLEAssert.assertNotNull(this.xuid);
        return DataLoadUtil.Load(b, this.lifetime, null, this.submitFeedbackForUserLoadingStatus, (IDataLoaderRunnable<Boolean>)new SubmitFeedbackForUserRunner(this, this.xuid, feedbackType, s));
    }
    
    @Override
    public void updateWithNewData(final AsyncResult<ProfileData> asyncResult) {
        XLEAssert.assertTrue(Thread.currentThread() == ThreadManager.UIThread);
        super.updateWithNewData(asyncResult);
        if (asyncResult.getStatus() == AsyncActionStatus.SUCCESS) {
            final ProfileData profileData = asyncResult.getResult();
            if (profileData != null) {
                this.shareRealName = (!this.isMeProfile() || profileData.getShareRealName());
                this.shareRealNameStatus = profileData.getShareRealNameStatus();
                final StringBuilder sb = new StringBuilder();
                sb.append("shareRealNameStatus: ");
                sb.append(this.shareRealNameStatus);
                Log.i("ProfileModel", sb.toString());
                this.sharingRealNameTransitively = profileData.getSharingRealNameTransitively();
                final IUserProfileResult.UserProfileResult profileResult = profileData.getProfileResult();
                if (profileResult != null && profileResult.profileUsers != null) {
                    this.profileUser = profileResult.profileUsers.get(0);
                    this.profileImageUrl = null;
                }
            }
        }
        this.notifyObservers(new AsyncResult<UpdateData>(new UpdateData(UpdateType.ProfileData, true), this, asyncResult.getException()));
    }
    
    private class AddUserToFavoriteListRunner extends IDataLoaderRunnable<Boolean>
    {
        private ProfileModel caller;
        private String favoriteUserXuid;
        
        public AddUserToFavoriteListRunner(final ProfileModel caller, final String favoriteUserXuid) {
            this.caller = caller;
            this.favoriteUserXuid = favoriteUserXuid;
        }
        
        @Override
        public Boolean buildData() throws XLEException {
            final ArrayList<String> list = new ArrayList<String>();
            list.add(this.favoriteUserXuid);
            return ServiceManagerFactory.getInstance().getSLSServiceManager().addUserToFavoriteList(FavoriteListRequest.getFavoriteListRequestBody(new FavoriteListRequest(list)));
        }
        
        @Override
        public long getDefaultErrorCode() {
            return 3994L;
        }
        
        @Override
        public void onPostExcute(final AsyncResult<Boolean> asyncResult) {
            this.caller.onAddUserToFavoriteListCompleted(asyncResult, this.favoriteUserXuid);
        }
        
        @Override
        public void onPreExecute() {
        }
    }
    
    private class AddUserToFollowingListRunner extends IDataLoaderRunnable<AddFollowingUserResponseContainer.AddFollowingUserResponse>
    {
        private ProfileModel caller;
        private String followingUserXuid;
        
        public AddUserToFollowingListRunner(final ProfileModel caller, final String followingUserXuid) {
            this.caller = caller;
            this.followingUserXuid = followingUserXuid;
        }
        
        @Override
        public AddFollowingUserResponseContainer.AddFollowingUserResponse buildData() throws XLEException {
            final ArrayList<String> list = new ArrayList<String>();
            list.add(this.followingUserXuid);
            return ServiceManagerFactory.getInstance().getSLSServiceManager().addUserToFollowingList(FavoriteListRequest.getFavoriteListRequestBody(new FavoriteListRequest(list)));
        }
        
        @Override
        public long getDefaultErrorCode() {
            return 3011L;
        }
        
        @Override
        public void onPostExcute(final AsyncResult<AddFollowingUserResponseContainer.AddFollowingUserResponse> asyncResult) {
            this.caller.onAddUserToFollowingListCompleted(asyncResult, this.followingUserXuid);
        }
        
        @Override
        public void onPreExecute() {
        }
    }
    
    private class AddUsersToShareIdentityListRunner extends IDataLoaderRunnable<Boolean>
    {
        private ProfileModel caller;
        private ArrayList<String> userIds;
        
        public AddUsersToShareIdentityListRunner(final ProfileModel caller, final ArrayList<String> userIds) {
            this.caller = caller;
            this.userIds = userIds;
        }
        
        @Override
        public Boolean buildData() throws XLEException {
            return ServiceManagerFactory.getInstance().getSLSServiceManager().addFriendToShareIdentitySetting(this.caller.xuid, AddShareIdentityRequest.getAddShareIdentityRequestBody(new AddShareIdentityRequest(this.userIds)));
        }
        
        @Override
        public long getDefaultErrorCode() {
            return 40043L;
        }
        
        @Override
        public void onPostExcute(final AsyncResult<Boolean> asyncResult) {
            this.caller.onAddUserToShareIdentityCompleted(asyncResult, this.userIds);
        }
        
        @Override
        public void onPreExecute() {
        }
    }
    
    private class FollowingAndFavoritesComparator implements Comparator<FollowersData>
    {
        @Override
        public int compare(final FollowersData followersData, final FollowersData followersData2) {
            return followersData.userProfileData.appDisplayName.compareToIgnoreCase(followersData2.userProfileData.appDisplayName);
        }
        
        @Override
        public Comparator<Object> reversed() {
            return Comparator-CC.$default$reversed();
        }
        
        @Override
        public Comparator<Object> thenComparing(final Comparator<?> p0) {
            // 
            // This method could not be decompiled.
            // 
            // Could not show original bytecode, likely due to the same error.
            // 
            // The error that occurred was:
            // 
            // java.lang.NullPointerException
            // 
            throw new IllegalStateException("An error occurred while decompiling this method.");
        }
        
        @Override
        public <U extends Comparable<? super U>> Comparator<Object> thenComparing(final Function<?, ? extends U> p0) {
            // 
            // This method could not be decompiled.
            // 
            // Could not show original bytecode, likely due to the same error.
            // 
            // The error that occurred was:
            // 
            // java.lang.NullPointerException
            //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
            //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:21)
            //     at com.strobel.assembler.metadata.MetadataHelper$5.visitWildcard(MetadataHelper.java:1793)
            //     at com.strobel.assembler.metadata.MetadataHelper$5.visitWildcard(MetadataHelper.java:1790)
            //     at com.strobel.assembler.metadata.WildcardType.accept(WildcardType.java:83)
            //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
            //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:21)
            //     at com.strobel.assembler.metadata.MetadataHelper.getLowerBound(MetadataHelper.java:1240)
            //     at com.strobel.assembler.metadata.MetadataHelper$Adapter.visitWildcard(MetadataHelper.java:2278)
            //     at com.strobel.assembler.metadata.MetadataHelper$Adapter.visitWildcard(MetadataHelper.java:2222)
            //     at com.strobel.assembler.metadata.WildcardType.accept(WildcardType.java:83)
            //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
            //     at com.strobel.assembler.metadata.MetadataHelper$Adapter.adaptRecursive(MetadataHelper.java:2256)
            //     at com.strobel.assembler.metadata.MetadataHelper$Adapter.adaptRecursive(MetadataHelper.java:2233)
            //     at com.strobel.assembler.metadata.MetadataHelper$Adapter.visitParameterizedType(MetadataHelper.java:2246)
            //     at com.strobel.assembler.metadata.MetadataHelper$Adapter.visitParameterizedType(MetadataHelper.java:2222)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedGenericType.accept(CoreMetadataFactory.java:653)
            //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
            //     at com.strobel.assembler.metadata.MetadataHelper.adapt(MetadataHelper.java:1312)
            //     at com.strobel.assembler.metadata.MetadataHelper$11.visitClassType(MetadataHelper.java:2708)
            //     at com.strobel.assembler.metadata.MetadataHelper$11.visitClassType(MetadataHelper.java:2692)
            //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visitParameterizedType(DefaultTypeVisitor.java:65)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedGenericType.accept(CoreMetadataFactory.java:653)
            //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
            //     at com.strobel.assembler.metadata.MetadataHelper.asSubType(MetadataHelper.java:720)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:926)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2515)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:778)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1656)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
            //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:672)
            //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:655)
            //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:365)
            //     at com.strobel.decompiler.ast.TypeAnalysis.run(TypeAnalysis.java:96)
            //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:109)
            //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:576)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
            //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
            //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
            //     at us.deathmarine.luyten.FileSaver.doSaveJarDecompiled(FileSaver.java:192)
            //     at us.deathmarine.luyten.FileSaver.access$300(FileSaver.java:45)
            //     at us.deathmarine.luyten.FileSaver$4.run(FileSaver.java:112)
            //     at java.lang.Thread.run(Unknown Source)
            // 
            throw new IllegalStateException("An error occurred while decompiling this method.");
        }
        
        @Override
        public <U> Comparator<Object> thenComparing(final Function<?, ? extends U> p0, final Comparator<? super U> p1) {
            // 
            // This method could not be decompiled.
            // 
            // Could not show original bytecode, likely due to the same error.
            // 
            // The error that occurred was:
            // 
            // java.lang.NullPointerException
            //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
            //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:21)
            //     at com.strobel.assembler.metadata.MetadataHelper$5.visitWildcard(MetadataHelper.java:1793)
            //     at com.strobel.assembler.metadata.MetadataHelper$5.visitWildcard(MetadataHelper.java:1790)
            //     at com.strobel.assembler.metadata.WildcardType.accept(WildcardType.java:83)
            //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
            //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:21)
            //     at com.strobel.assembler.metadata.MetadataHelper.getLowerBound(MetadataHelper.java:1240)
            //     at com.strobel.assembler.metadata.MetadataHelper$Adapter.visitWildcard(MetadataHelper.java:2278)
            //     at com.strobel.assembler.metadata.MetadataHelper$Adapter.visitWildcard(MetadataHelper.java:2222)
            //     at com.strobel.assembler.metadata.WildcardType.accept(WildcardType.java:83)
            //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
            //     at com.strobel.assembler.metadata.MetadataHelper$Adapter.adaptRecursive(MetadataHelper.java:2256)
            //     at com.strobel.assembler.metadata.MetadataHelper$Adapter.adaptRecursive(MetadataHelper.java:2233)
            //     at com.strobel.assembler.metadata.MetadataHelper$Adapter.visitParameterizedType(MetadataHelper.java:2246)
            //     at com.strobel.assembler.metadata.MetadataHelper$Adapter.visitParameterizedType(MetadataHelper.java:2222)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedGenericType.accept(CoreMetadataFactory.java:653)
            //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
            //     at com.strobel.assembler.metadata.MetadataHelper.adapt(MetadataHelper.java:1312)
            //     at com.strobel.assembler.metadata.MetadataHelper$11.visitClassType(MetadataHelper.java:2708)
            //     at com.strobel.assembler.metadata.MetadataHelper$11.visitClassType(MetadataHelper.java:2692)
            //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visitParameterizedType(DefaultTypeVisitor.java:65)
            //     at com.strobel.assembler.metadata.CoreMetadataFactory$UnresolvedGenericType.accept(CoreMetadataFactory.java:653)
            //     at com.strobel.assembler.metadata.DefaultTypeVisitor.visit(DefaultTypeVisitor.java:25)
            //     at com.strobel.assembler.metadata.MetadataHelper.asSubType(MetadataHelper.java:720)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:926)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:770)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:766)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferCall(TypeAnalysis.java:2515)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1029)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:778)
            //     at com.strobel.decompiler.ast.TypeAnalysis.doInferTypeForExpression(TypeAnalysis.java:1656)
            //     at com.strobel.decompiler.ast.TypeAnalysis.inferTypeForExpression(TypeAnalysis.java:803)
            //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:672)
            //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:655)
            //     at com.strobel.decompiler.ast.TypeAnalysis.runInference(TypeAnalysis.java:365)
            //     at com.strobel.decompiler.ast.TypeAnalysis.run(TypeAnalysis.java:96)
            //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:109)
            //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
            //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:576)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
            //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
            //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
            //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
            //     at us.deathmarine.luyten.FileSaver.doSaveJarDecompiled(FileSaver.java:192)
            //     at us.deathmarine.luyten.FileSaver.access$300(FileSaver.java:45)
            //     at us.deathmarine.luyten.FileSaver$4.run(FileSaver.java:112)
            //     at java.lang.Thread.run(Unknown Source)
            // 
            throw new IllegalStateException("An error occurred while decompiling this method.");
        }
        
        @Override
        public Comparator<Object> thenComparingDouble(final ToDoubleFunction<?> p0) {
            // 
            // This method could not be decompiled.
            // 
            // Could not show original bytecode, likely due to the same error.
            // 
            // The error that occurred was:
            // 
            // java.lang.NullPointerException
            // 
            throw new IllegalStateException("An error occurred while decompiling this method.");
        }
        
        @Override
        public Comparator<Object> thenComparingInt(final ToIntFunction<?> p0) {
            // 
            // This method could not be decompiled.
            // 
            // Could not show original bytecode, likely due to the same error.
            // 
            // The error that occurred was:
            // 
            // java.lang.NullPointerException
            // 
            throw new IllegalStateException("An error occurred while decompiling this method.");
        }
        
        @Override
        public Comparator<Object> thenComparingLong(final ToLongFunction<?> p0) {
            // 
            // This method could not be decompiled.
            // 
            // Could not show original bytecode, likely due to the same error.
            // 
            // The error that occurred was:
            // 
            // java.lang.NullPointerException
            // 
            throw new IllegalStateException("An error occurred while decompiling this method.");
        }
    }
    
    private class GetMutedListRunner extends IDataLoaderRunnable<MutedListResultContainer.MutedListResult>
    {
        private ProfileModel caller;
        private String xuid;
        
        public GetMutedListRunner(final ProfileModel caller, final String xuid) {
            this.caller = caller;
            this.xuid = xuid;
        }
        
        @Override
        public MutedListResultContainer.MutedListResult buildData() throws XLEException {
            return ServiceManagerFactory.getInstance().getSLSServiceManager().getMutedListInfo(this.xuid);
        }
        
        @Override
        public long getDefaultErrorCode() {
            return 40040L;
        }
        
        @Override
        public void onPostExcute(final AsyncResult<MutedListResultContainer.MutedListResult> asyncResult) {
            this.caller.onGetMutedListCompleted(asyncResult);
        }
        
        @Override
        public void onPreExecute() {
        }
    }
    
    private class GetNeverListRunner extends IDataLoaderRunnable<NeverListResultContainer.NeverListResult>
    {
        private ProfileModel caller;
        private String xuid;
        
        public GetNeverListRunner(final ProfileModel caller, final String xuid) {
            this.caller = caller;
            this.xuid = xuid;
        }
        
        @Override
        public NeverListResultContainer.NeverListResult buildData() throws XLEException {
            return ServiceManagerFactory.getInstance().getSLSServiceManager().getNeverListInfo(this.xuid);
        }
        
        @Override
        public long getDefaultErrorCode() {
            return 3203L;
        }
        
        @Override
        public void onPostExcute(final AsyncResult<NeverListResultContainer.NeverListResult> asyncResult) {
            this.caller.onGetNeverListCompleted(asyncResult);
        }
        
        @Override
        public void onPreExecute() {
        }
    }
    
    private class GetPeopleHubRecommendationRunner extends IDataLoaderRunnable<IPeopleHubResult.PeopleHubPeopleSummary>
    {
        private ProfileModel caller;
        private String xuid;
        
        public GetPeopleHubRecommendationRunner(final ProfileModel caller, final String xuid) {
            this.caller = caller;
            this.xuid = xuid;
        }
        
        @Override
        public IPeopleHubResult.PeopleHubPeopleSummary buildData() throws XLEException {
            IPeopleHubResult.PeopleHubPeopleSummary peopleHubRecommendations = new IPeopleHubResult.PeopleHubPeopleSummary();
            if (!JavaUtil.isNullOrEmpty(this.xuid)) {
                peopleHubRecommendations = peopleHubRecommendations;
                if (this.xuid.equalsIgnoreCase(ProjectSpecificDataProvider.getInstance().getXuidString())) {
                    peopleHubRecommendations = ServiceManagerFactory.getInstance().getSLSServiceManager().getPeopleHubRecommendations();
                }
            }
            return peopleHubRecommendations;
        }
        
        @Override
        public long getDefaultErrorCode() {
            return 11L;
        }
        
        @Override
        public void onPostExcute(final AsyncResult<IPeopleHubResult.PeopleHubPeopleSummary> asyncResult) {
            this.caller.onGetPeopleHubRecommendationsCompleted(asyncResult);
        }
        
        @Override
        public void onPreExecute() {
        }
    }
    
    private class GetPresenceDataRunner extends IDataLoaderRunnable<IFollowerPresenceResult.UserPresence>
    {
        private ProfileModel caller;
        private String xuid;
        
        public GetPresenceDataRunner(final ProfileModel caller, final String xuid) {
            this.caller = caller;
            this.xuid = xuid;
        }
        
        @Override
        public IFollowerPresenceResult.UserPresence buildData() throws XLEException {
            return null;
        }
        
        @Override
        public long getDefaultErrorCode() {
            return 3013L;
        }
        
        @Override
        public void onPostExcute(final AsyncResult<IFollowerPresenceResult.UserPresence> asyncResult) {
            this.caller.onGetPresenceDataCompleted(asyncResult);
        }
        
        @Override
        public void onPreExecute() {
        }
    }
    
    private class GetProfileRunner extends IDataLoaderRunnable<ProfileData>
    {
        private ProfileModel caller;
        private boolean loadEssentialsOnly;
        private String xuid;
        
        public GetProfileRunner(final ProfileModel caller, final String xuid, final boolean loadEssentialsOnly) {
            this.caller = caller;
            this.xuid = xuid;
            this.loadEssentialsOnly = loadEssentialsOnly;
        }
        
        @Override
        public ProfileData buildData() throws XLEException {
            final ISLSServiceManager slsServiceManager = ServiceManagerFactory.getInstance().getSLSServiceManager();
            final ArrayList<String> list = new ArrayList<String>();
            list.add(this.xuid);
            final IUserProfileResult.UserProfileResult userProfileInfo = slsServiceManager.getUserProfileInfo(UserProfileRequest.getUserProfileRequestBody(new UserProfileRequest(list, this.loadEssentialsOnly)));
            if (ProjectSpecificDataProvider.getInstance().getXuidString().equalsIgnoreCase(this.xuid)) {
                if (userProfileInfo != null && userProfileInfo.profileUsers != null && userProfileInfo.profileUsers.size() > 0) {
                    final IUserProfileResult.ProfileUser profileUser = userProfileInfo.profileUsers.get(0);
                    profileUser.setPrivilieges(slsServiceManager.getXTokenPrivileges());
                    try {
                        final String settingValue = profileUser.getSettingValue(UserProfileSetting.PreferredColor);
                        if (settingValue != null && settingValue.length() > 0) {
                            profileUser.colors = slsServiceManager.getProfilePreferredColor(settingValue);
                        }
                    }
                    finally {}
                    XLEThreadPool.networkOperationsThreadPool.run(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                final FamilySettings familySettings = slsServiceManager.getFamilySettings(GetProfileRunner.this.xuid);
                                if (familySettings != null && familySettings.familyUsers != null) {
                                    for (int i = 0; i < familySettings.familyUsers.size(); ++i) {
                                        if (familySettings.familyUsers.get(i).xuid.equalsIgnoreCase(GetProfileRunner.this.xuid)) {
                                            profileUser.canViewTVAdultContent = familySettings.familyUsers.get(i).canViewTVAdultContent;
                                            profileUser.setmaturityLevel(familySettings.familyUsers.get(i).maturityLevel);
                                            return;
                                        }
                                    }
                                }
                            }
                            finally {}
                        }
                    });
                }
            }
            else if (userProfileInfo != null && userProfileInfo.profileUsers != null && userProfileInfo.profileUsers.size() > 0) {
                final IUserProfileResult.ProfileUser profileUser2 = userProfileInfo.profileUsers.get(0);
                try {
                    final String settingValue2 = profileUser2.getSettingValue(UserProfileSetting.PreferredColor);
                    if (settingValue2 != null && settingValue2.length() > 0) {
                        profileUser2.colors = slsServiceManager.getProfilePreferredColor(settingValue2);
                    }
                }
                finally {}
            }
            final String xuid = this.xuid;
            String shareRealNameStatus;
            final String s = shareRealNameStatus = null;
            if (xuid != null) {
                shareRealNameStatus = s;
                if (xuid.compareToIgnoreCase(ProjectSpecificDataProvider.getInstance().getXuidString()) == 0) {
                    try {
                        final PrivacySettingsResult userProfilePrivacySettings = slsServiceManager.getUserProfilePrivacySettings();
                        shareRealNameStatus = userProfilePrivacySettings.getShareRealNameStatus();
                        try {
                            if (ShareRealNameSettingFilter.Blocked.toString().compareTo(shareRealNameStatus) != 0) {
                                final boolean b = true;
                            }
                            else {
                                final boolean b = false;
                            }
                            try {
                                final boolean sharingRealNameTransitively = userProfilePrivacySettings.getSharingRealNameTransitively();
                            }
                            catch (Exception ex) {}
                        }
                        catch (Exception s) {}
                    }
                    catch (Exception ex2) {
                        shareRealNameStatus = s;
                    }
                }
            }
            final boolean b = false;
            final boolean sharingRealNameTransitively = false;
            return new ProfileData(userProfileInfo, b, shareRealNameStatus, sharingRealNameTransitively);
        }
        
        @Override
        public long getDefaultErrorCode() {
            return 3002L;
        }
        
        @Override
        public void onPostExcute(final AsyncResult<ProfileData> asyncResult) {
            this.caller.updateWithProfileData(asyncResult, this.loadEssentialsOnly);
        }
        
        @Override
        public void onPreExecute() {
        }
    }
    
    private class GetProfileSummaryRunner extends IDataLoaderRunnable<ProfileSummaryResultContainer.ProfileSummaryResult>
    {
        private ProfileModel caller;
        private String xuid;
        
        public GetProfileSummaryRunner(final ProfileModel caller, final String xuid) {
            this.caller = caller;
            this.xuid = xuid;
        }
        
        @Override
        public ProfileSummaryResultContainer.ProfileSummaryResult buildData() throws XLEException {
            return ServiceManagerFactory.getInstance().getSLSServiceManager().getProfileSummaryInfo(this.xuid);
        }
        
        @Override
        public long getDefaultErrorCode() {
            return 3002L;
        }
        
        @Override
        public void onPostExcute(final AsyncResult<ProfileSummaryResultContainer.ProfileSummaryResult> asyncResult) {
            this.caller.onGetProfileSummaryCompleted(asyncResult);
        }
        
        @Override
        public void onPreExecute() {
        }
    }
    
    private class PutUserToMutedListRunner extends IDataLoaderRunnable<Boolean>
    {
        private ProfileModel caller;
        private String mutedUserXuid;
        private String xuid;
        
        public PutUserToMutedListRunner(final ProfileModel caller, final String xuid, final String mutedUserXuid) {
            this.caller = caller;
            this.xuid = xuid;
            this.mutedUserXuid = mutedUserXuid;
        }
        
        @Override
        public Boolean buildData() throws XLEException {
            return ServiceManagerFactory.getInstance().getSLSServiceManager().addUserToMutedList(this.xuid, MutedListRequest.getNeverListRequestBody(new MutedListRequest(Long.parseLong(this.mutedUserXuid))));
        }
        
        @Override
        public long getDefaultErrorCode() {
            return 4038L;
        }
        
        @Override
        public void onPostExcute(final AsyncResult<Boolean> asyncResult) {
            this.caller.onPutUserToMutedListCompleted(asyncResult, this.mutedUserXuid);
        }
        
        @Override
        public void onPreExecute() {
        }
    }
    
    private class PutUserToNeverListRunner extends IDataLoaderRunnable<Boolean>
    {
        private String blockUserXuid;
        private ProfileModel caller;
        private String xuid;
        
        public PutUserToNeverListRunner(final ProfileModel caller, final String xuid, final String blockUserXuid) {
            this.caller = caller;
            this.xuid = xuid;
            this.blockUserXuid = blockUserXuid;
        }
        
        @Override
        public Boolean buildData() throws XLEException {
            return ServiceManagerFactory.getInstance().getSLSServiceManager().addUserToNeverList(this.xuid, NeverListRequest.getNeverListRequestBody(new NeverListRequest(Long.parseLong(this.blockUserXuid))));
        }
        
        @Override
        public long getDefaultErrorCode() {
            return 3996L;
        }
        
        @Override
        public void onPostExcute(final AsyncResult<Boolean> asyncResult) {
            this.caller.onPutUserToNeverListCompleted(asyncResult, this.blockUserXuid);
        }
        
        @Override
        public void onPreExecute() {
        }
    }
    
    private class RemoveUserFromFavoriteListRunner extends IDataLoaderRunnable<Boolean>
    {
        private ProfileModel caller;
        private String favoriteUserXuid;
        
        public RemoveUserFromFavoriteListRunner(final ProfileModel caller, final String favoriteUserXuid) {
            this.caller = caller;
            this.favoriteUserXuid = favoriteUserXuid;
        }
        
        @Override
        public Boolean buildData() throws XLEException {
            final ArrayList<String> list = new ArrayList<String>();
            list.add(this.favoriteUserXuid);
            return ServiceManagerFactory.getInstance().getSLSServiceManager().removeUserFromFavoriteList(FavoriteListRequest.getFavoriteListRequestBody(new FavoriteListRequest(list)));
        }
        
        @Override
        public long getDefaultErrorCode() {
            return 3995L;
        }
        
        @Override
        public void onPostExcute(final AsyncResult<Boolean> asyncResult) {
            this.caller.onRemoveUserFromFavoriteListCompleted(asyncResult, this.favoriteUserXuid);
        }
        
        @Override
        public void onPreExecute() {
        }
    }
    
    private class RemoveUserFromFollowingListRunner extends IDataLoaderRunnable<Boolean>
    {
        private ProfileModel caller;
        private String followingUserXuid;
        
        public RemoveUserFromFollowingListRunner(final ProfileModel caller, final String followingUserXuid) {
            this.caller = caller;
            this.followingUserXuid = followingUserXuid;
        }
        
        @Override
        public Boolean buildData() throws XLEException {
            final ArrayList<String> list = new ArrayList<String>();
            list.add(this.followingUserXuid);
            return ServiceManagerFactory.getInstance().getSLSServiceManager().removeUserFromFollowingList(FavoriteListRequest.getFavoriteListRequestBody(new FavoriteListRequest(list)));
        }
        
        @Override
        public long getDefaultErrorCode() {
            return 3012L;
        }
        
        @Override
        public void onPostExcute(final AsyncResult<Boolean> asyncResult) {
            this.caller.onRemoveUserFromFollowingListCompleted(asyncResult, this.followingUserXuid);
        }
        
        @Override
        public void onPreExecute() {
        }
    }
    
    private class RemoveUserFromMutedListRunner extends IDataLoaderRunnable<Boolean>
    {
        private ProfileModel caller;
        private String unmutedUserXuid;
        private String xuid;
        
        public RemoveUserFromMutedListRunner(final ProfileModel caller, final String xuid, final String unmutedUserXuid) {
            this.caller = caller;
            this.xuid = xuid;
            this.unmutedUserXuid = unmutedUserXuid;
        }
        
        @Override
        public Boolean buildData() throws XLEException {
            return ServiceManagerFactory.getInstance().getSLSServiceManager().removeUserFromMutedList(this.xuid, MutedListRequest.getNeverListRequestBody(new MutedListRequest(Long.parseLong(this.unmutedUserXuid))));
        }
        
        @Override
        public long getDefaultErrorCode() {
            return 40039L;
        }
        
        @Override
        public void onPostExcute(final AsyncResult<Boolean> asyncResult) {
            this.caller.onRemoveUserFromMutedListCompleted(asyncResult, this.unmutedUserXuid);
        }
        
        @Override
        public void onPreExecute() {
        }
    }
    
    private class RemoveUserFromNeverListRunner extends IDataLoaderRunnable<Boolean>
    {
        private ProfileModel caller;
        private String unblockUserXuid;
        private String xuid;
        
        public RemoveUserFromNeverListRunner(final ProfileModel caller, final String xuid, final String unblockUserXuid) {
            this.caller = caller;
            this.xuid = xuid;
            this.unblockUserXuid = unblockUserXuid;
        }
        
        @Override
        public Boolean buildData() throws XLEException {
            return ServiceManagerFactory.getInstance().getSLSServiceManager().removeUserFromNeverList(this.xuid, NeverListRequest.getNeverListRequestBody(new NeverListRequest(Long.parseLong(this.unblockUserXuid))));
        }
        
        @Override
        public long getDefaultErrorCode() {
            return 3997L;
        }
        
        @Override
        public void onPostExcute(final AsyncResult<Boolean> asyncResult) {
            this.caller.onRemoveUserFromNeverListCompleted(asyncResult, this.unblockUserXuid);
        }
        
        @Override
        public void onPreExecute() {
        }
    }
    
    private class RemoveUsersFromShareIdentityListRunner extends IDataLoaderRunnable<Boolean>
    {
        private ProfileModel caller;
        private ArrayList<String> userIds;
        
        public RemoveUsersFromShareIdentityListRunner(final ProfileModel caller, final ArrayList<String> userIds) {
            this.caller = caller;
            this.userIds = userIds;
        }
        
        @Override
        public Boolean buildData() throws XLEException {
            return ServiceManagerFactory.getInstance().getSLSServiceManager().removeFriendFromShareIdentitySetting(this.caller.xuid, AddShareIdentityRequest.getAddShareIdentityRequestBody(new AddShareIdentityRequest(this.userIds)));
        }
        
        @Override
        public long getDefaultErrorCode() {
            return 40042L;
        }
        
        @Override
        public void onPostExcute(final AsyncResult<Boolean> asyncResult) {
            this.caller.onRemoveUserFromShareIdentityCompleted(asyncResult, this.userIds);
        }
        
        @Override
        public void onPreExecute() {
        }
    }
    
    private class SubmitFeedbackForUserRunner extends IDataLoaderRunnable<Boolean>
    {
        private ProfileModel caller;
        private FeedbackType feedbackType;
        private String textReason;
        private String xuid;
        
        public SubmitFeedbackForUserRunner(final ProfileModel caller, final String xuid, final FeedbackType feedbackType, final String textReason) {
            this.caller = caller;
            this.xuid = xuid;
            this.feedbackType = feedbackType;
            this.textReason = textReason;
        }
        
        @Override
        public Boolean buildData() throws XLEException {
            return ServiceManagerFactory.getInstance().getSLSServiceManager().submitFeedback(this.xuid, SubmitFeedbackRequest.getSubmitFeedbackRequestBody(new SubmitFeedbackRequest(Long.parseLong(this.xuid), null, this.feedbackType, this.textReason, null, null)));
        }
        
        @Override
        public long getDefaultErrorCode() {
            return 40041L;
        }
        
        @Override
        public void onPostExcute(final AsyncResult<Boolean> asyncResult) {
            this.caller.onSubmitFeedbackForUserCompleted(asyncResult);
        }
        
        @Override
        public void onPreExecute() {
        }
    }
}
