package com.microsoft.xbox.service.network.managers.xblshared;

import com.microsoft.xbox.toolkit.network.*;
import java.net.*;
import java.util.concurrent.atomic.*;
import android.util.*;
import java.io.*;
import com.microsoft.xbox.idp.util.*;
import java.util.*;
import com.microsoft.xbox.toolkit.*;
import com.microsoft.xbox.service.network.managers.*;
import com.microsoft.xbox.service.model.privacy.*;

public class SLSXsapiServiceManager implements ISLSServiceManager
{
    private static final String TAG;
    
    static {
        TAG = SLSXsapiServiceManager.class.getSimpleName();
    }
    
    @Override
    public IUserProfileResult.UserProfileResult SearchGamertag(final String s) throws XLEException {
        Log.i(SLSXsapiServiceManager.TAG, "SearchGamertag");
        XLEAssert.assertTrue(Thread.currentThread() != ThreadManager.UIThread);
        try {
            final IUserProfileResult.UserProfileResult userProfileResult = TcuiHttpUtil.getResponseSync(HttpUtil.appendCommonParameters(new HttpCall("GET", String.format(XboxLiveEnvironment.Instance().getGamertagSearchUrlFormat(), URLEncoder.encode(s.toLowerCase(), "utf-8")), ""), "3"), IUserProfileResult.UserProfileResult.class);
            TcuiHttpUtil.throwIfNullOrFalse(userProfileResult);
            return userProfileResult;
        }
        catch (UnsupportedEncodingException ex) {
            throw new XLEException(15L, ex);
        }
    }
    
    @Override
    public boolean addFriendToShareIdentitySetting(final String s, final String requestBody) throws XLEException {
        Log.i(SLSXsapiServiceManager.TAG, "addFriendToShareIdentitySetting");
        XLEAssert.assertTrue(Thread.currentThread() != ThreadManager.UIThread);
        final HttpCall appendCommonParameters = HttpUtil.appendCommonParameters(new HttpCall("POST", String.format(XboxLiveEnvironment.Instance().getAddFriendsToShareIdentityUrlFormat(), s), ""), "4");
        appendCommonParameters.setRequestBody(requestBody);
        final boolean responseSyncSucceeded = TcuiHttpUtil.getResponseSyncSucceeded(appendCommonParameters, Arrays.asList(204));
        TcuiHttpUtil.throwIfNullOrFalse(responseSyncSucceeded);
        return responseSyncSucceeded;
    }
    
    @Override
    public boolean addUserToFavoriteList(final String requestBody) throws XLEException {
        Log.i(SLSXsapiServiceManager.TAG, "addUserToFavoriteList");
        XLEAssert.assertTrue(Thread.currentThread() != ThreadManager.UIThread);
        final HttpCall appendCommonParameters = HttpUtil.appendCommonParameters(new HttpCall("POST", String.format(XboxLiveEnvironment.Instance().getProfileFavoriteListUrl(), "add"), ""), "1");
        appendCommonParameters.setRequestBody(requestBody);
        final boolean responseSyncSucceeded = TcuiHttpUtil.getResponseSyncSucceeded(appendCommonParameters, Arrays.asList(204));
        TcuiHttpUtil.throwIfNullOrFalse(responseSyncSucceeded);
        return responseSyncSucceeded;
    }
    
    @Override
    public AddFollowingUserResponseContainer.AddFollowingUserResponse addUserToFollowingList(String requestBody) throws XLEException {
        Log.i(SLSXsapiServiceManager.TAG, "addUserToFollowingList");
        XLEAssert.assertTrue(Thread.currentThread() != ThreadManager.UIThread);
        final HttpCall appendCommonParameters = HttpUtil.appendCommonParameters(new HttpCall("POST", String.format(XboxLiveEnvironment.Instance().updateProfileFollowingListUrl(), "add"), ""), "1");
        appendCommonParameters.setRequestBody(requestBody);
        final AddFollowingUserResponseContainer.AddFollowingUserResponse addFollowingUserResponse = new AddFollowingUserResponseContainer.AddFollowingUserResponse();
        requestBody = (String)new AtomicReference();
        ((AtomicReference<Pair>)requestBody).set(new Pair((Object)false, (Object)null));
        appendCommonParameters.getResponseAsync((HttpCall.Callback)new HttpCall.Callback() {
            @Override
            public void processResponse(final int n, final InputStream inputStream, HttpHeaders val$notifier) throws Exception {
                val$notifier = (HttpHeaders)requestBody;
                // monitorenter(val$notifier)
                Label_0056: {
                    if (n >= 200 || n <= 299) {
                        break Label_0056;
                    }
                    try {
                        ((AtomicReference<Pair>)requestBody).set(new Pair((Object)true, (Object)GsonUtil.deserializeJson(inputStream, AddFollowingUserResponseContainer.AddFollowingUserResponse.class)));
                        while (true) {
                            requestBody.notify();
                            return;
                            addFollowingUserResponse.setAddFollowingRequestStatus(true);
                            ((AtomicReference<Pair>)requestBody).set(new Pair((Object)true, (Object)addFollowingUserResponse));
                            continue;
                        }
                    }
                    finally {
                    }
                    // monitorexit(val$notifier)
                }
            }
        });
        // monitorenter(requestBody)
        try {
            try {
                while (!(boolean)((AtomicReference<Pair>)requestBody).get().first) {
                    requestBody.wait();
                }
            }
            finally {
                // monitorexit(requestBody)
                // monitorexit(requestBody)
                TcuiHttpUtil.throwIfNullOrFalse(((AtomicReference<Pair>)requestBody).get().second);
                return (AddFollowingUserResponseContainer.AddFollowingUserResponse)((AtomicReference<Pair>)requestBody).get().second;
            }
        }
        catch (InterruptedException ex) {}
    }
    
    @Override
    public boolean addUserToMutedList(final String s, final String requestBody) throws XLEException {
        Log.i(SLSXsapiServiceManager.TAG, "addUserToMutedList");
        XLEAssert.assertTrue(Thread.currentThread() != ThreadManager.UIThread);
        final HttpCall appendCommonParameters = HttpUtil.appendCommonParameters(new HttpCall("PUT", String.format(XboxLiveEnvironment.Instance().getMutedServiceUrlFormat(), s), ""), "1");
        appendCommonParameters.setRequestBody(requestBody);
        final boolean responseSyncSucceeded = TcuiHttpUtil.getResponseSyncSucceeded(appendCommonParameters, new ArrayList<Integer>(0));
        TcuiHttpUtil.throwIfNullOrFalse(responseSyncSucceeded);
        return responseSyncSucceeded;
    }
    
    @Override
    public boolean addUserToNeverList(final String s, final String requestBody) throws XLEException {
        Log.i(SLSXsapiServiceManager.TAG, "addUserToNeverList");
        XLEAssert.assertTrue(Thread.currentThread() != ThreadManager.UIThread);
        final HttpCall appendCommonParameters = HttpUtil.appendCommonParameters(new HttpCall("PUT", String.format(XboxLiveEnvironment.Instance().getProfileNeverListUrlFormat(), s), ""), "1");
        appendCommonParameters.setRequestBody(requestBody);
        final boolean responseSyncSucceeded = TcuiHttpUtil.getResponseSyncSucceeded(appendCommonParameters, new ArrayList<Integer>(0));
        TcuiHttpUtil.throwIfNullOrFalse(responseSyncSucceeded);
        return responseSyncSucceeded;
    }
    
    @Override
    public FamilySettings getFamilySettings(final String s) throws XLEException {
        return null;
    }
    
    @Override
    public MutedListResultContainer.MutedListResult getMutedListInfo(final String s) throws XLEException {
        Log.i(SLSXsapiServiceManager.TAG, "getMutedListInfo");
        XLEAssert.assertTrue(Thread.currentThread() != ThreadManager.UIThread);
        XLEAssert.assertTrue(JavaUtil.isNullOrEmpty(s) ^ true);
        final MutedListResultContainer.MutedListResult mutedListResult = TcuiHttpUtil.getResponseSync(HttpUtil.appendCommonParameters(new HttpCall("GET", String.format(XboxLiveEnvironment.Instance().getMutedServiceUrlFormat(), s), ""), "1"), MutedListResultContainer.MutedListResult.class);
        TcuiHttpUtil.throwIfNullOrFalse(mutedListResult);
        return mutedListResult;
    }
    
    @Override
    public NeverListResultContainer.NeverListResult getNeverListInfo(final String s) throws XLEException {
        Log.i(SLSXsapiServiceManager.TAG, "getNeverListInfo");
        XLEAssert.assertTrue(Thread.currentThread() != ThreadManager.UIThread);
        XLEAssert.assertTrue(JavaUtil.isNullOrEmpty(s) ^ true);
        final NeverListResultContainer.NeverListResult neverListResult = TcuiHttpUtil.getResponseSync(HttpUtil.appendCommonParameters(new HttpCall("GET", String.format(XboxLiveEnvironment.Instance().getProfileNeverListUrlFormat(), s), ""), "1"), NeverListResultContainer.NeverListResult.class);
        TcuiHttpUtil.throwIfNullOrFalse(neverListResult);
        return neverListResult;
    }
    
    @Override
    public IPeopleHubResult.PeopleHubPeopleSummary getPeopleHubRecommendations() throws XLEException {
        Log.i(SLSXsapiServiceManager.TAG, "getPeopleHubRecommendations");
        XLEAssert.assertTrue(Thread.currentThread() != ThreadManager.UIThread);
        final HttpCall appendCommonParameters = HttpUtil.appendCommonParameters(new HttpCall("GET", XboxLiveEnvironment.Instance().getPeopleHubRecommendationsUrlFormat(), ""), "1");
        appendCommonParameters.setCustomHeader("Accept-Language", ProjectSpecificDataProvider.getInstance().getLegalLocale());
        appendCommonParameters.setCustomHeader("X-XBL-Contract-Version", "1");
        appendCommonParameters.setCustomHeader("X-XBL-Market", ProjectSpecificDataProvider.getInstance().getRegion());
        final IPeopleHubResult.PeopleHubPeopleSummary peopleHubPeopleSummary = TcuiHttpUtil.getResponseSync(appendCommonParameters, IPeopleHubResult.PeopleHubPeopleSummary.class);
        TcuiHttpUtil.throwIfNullOrFalse(peopleHubPeopleSummary);
        return peopleHubPeopleSummary;
    }
    
    @Override
    public PrivacySettings.PrivacySetting getPrivacySetting(final PrivacySettings.PrivacySettingId privacySettingId) throws XLEException {
        Log.i(SLSXsapiServiceManager.TAG, "getPrivacySetting");
        XLEAssert.assertTrue(Thread.currentThread() != ThreadManager.UIThread);
        final PrivacySettings.PrivacySetting privacySetting = TcuiHttpUtil.getResponseSync(HttpUtil.appendCommonParameters(new HttpCall("GET", String.format(XboxLiveEnvironment.Instance().getProfileSettingUrlFormat(), privacySettingId.name()), ""), "4"), PrivacySettings.PrivacySetting.class);
        TcuiHttpUtil.throwIfNullOrFalse(privacySetting);
        return privacySetting;
    }
    
    @Override
    public ProfilePreferredColor getProfilePreferredColor(final String s) throws XLEException {
        Log.i(SLSXsapiServiceManager.TAG, "getProfilePreferredColor");
        XLEAssert.assertTrue(Thread.currentThread() != ThreadManager.UIThread);
        final ProfilePreferredColor profilePreferredColor = TcuiHttpUtil.getResponseSync(HttpUtil.appendCommonParameters(new HttpCall("GET", s, ""), "2"), ProfilePreferredColor.class);
        TcuiHttpUtil.throwIfNullOrFalse(profilePreferredColor);
        return profilePreferredColor;
    }
    
    @Override
    public ProfileSummaryResultContainer.ProfileSummaryResult getProfileSummaryInfo(final String s) throws XLEException {
        Log.i(SLSXsapiServiceManager.TAG, "getProfileSummaryInfo");
        XLEAssert.assertTrue(Thread.currentThread() != ThreadManager.UIThread);
        XLEAssert.assertTrue(JavaUtil.isNullOrEmpty(s) ^ true);
        final ProfileSummaryResultContainer.ProfileSummaryResult profileSummaryResult = TcuiHttpUtil.getResponseSync(HttpUtil.appendCommonParameters(new HttpCall("GET", String.format(XboxLiveEnvironment.Instance().getProfileSummaryUrlFormat(), s), ""), "2"), ProfileSummaryResultContainer.ProfileSummaryResult.class);
        TcuiHttpUtil.throwIfNullOrFalse(profileSummaryResult);
        return profileSummaryResult;
    }
    
    @Override
    public IUserProfileResult.UserProfileResult getUserProfileInfo(final String requestBody) throws XLEException {
        Log.i(SLSXsapiServiceManager.TAG, "getUserProfileInfo");
        XLEAssert.assertTrue(Thread.currentThread() != ThreadManager.UIThread);
        final HttpCall httpCall = new HttpCall("POST", XboxLiveEnvironment.Instance().getUserProfileInfoUrl(), "");
        HttpUtil.appendCommonParameters(httpCall, "3");
        httpCall.setRequestBody(requestBody);
        final IUserProfileResult.UserProfileResult userProfileResult = TcuiHttpUtil.getResponseSync(httpCall, IUserProfileResult.UserProfileResult.class);
        TcuiHttpUtil.throwIfNullOrFalse(userProfileResult);
        return userProfileResult;
    }
    
    @Override
    public PrivacySettingsResult getUserProfilePrivacySettings() throws XLEException {
        Log.i(SLSXsapiServiceManager.TAG, "getUserProfilePrivacySettings");
        XLEAssert.assertTrue(Thread.currentThread() != ThreadManager.UIThread);
        final PrivacySettingsResult privacySettingsResult = TcuiHttpUtil.getResponseSync(HttpUtil.appendCommonParameters(new HttpCall("GET", XboxLiveEnvironment.Instance().getUserProfileSettingUrlFormat(), ""), "4"), PrivacySettingsResult.class);
        TcuiHttpUtil.throwIfNullOrFalse(privacySettingsResult);
        return privacySettingsResult;
    }
    
    @Override
    public int[] getXTokenPrivileges() throws XLEException {
        return new int[0];
    }
    
    @Override
    public boolean removeFriendFromShareIdentitySetting(final String s, final String requestBody) throws XLEException {
        Log.i(SLSXsapiServiceManager.TAG, "removeFriendFromShareIdentitySetting");
        XLEAssert.assertTrue(Thread.currentThread() != ThreadManager.UIThread);
        final HttpCall appendCommonParameters = HttpUtil.appendCommonParameters(new HttpCall("POST", String.format(XboxLiveEnvironment.Instance().getRemoveUsersFromShareIdentityUrlFormat(), s), ""), "4");
        appendCommonParameters.setRequestBody(requestBody);
        final boolean responseSyncSucceeded = TcuiHttpUtil.getResponseSyncSucceeded(appendCommonParameters, Arrays.asList(204));
        TcuiHttpUtil.throwIfNullOrFalse(responseSyncSucceeded);
        return responseSyncSucceeded;
    }
    
    @Override
    public boolean removeUserFromFavoriteList(final String requestBody) throws XLEException {
        Log.i(SLSXsapiServiceManager.TAG, "removeUserFromFavoriteList");
        XLEAssert.assertTrue(Thread.currentThread() != ThreadManager.UIThread);
        final HttpCall appendCommonParameters = HttpUtil.appendCommonParameters(new HttpCall("POST", String.format(XboxLiveEnvironment.Instance().getProfileFavoriteListUrl(), "remove"), ""), "1");
        appendCommonParameters.setRequestBody(requestBody);
        final boolean responseSyncSucceeded = TcuiHttpUtil.getResponseSyncSucceeded(appendCommonParameters, Arrays.asList(204));
        TcuiHttpUtil.throwIfNullOrFalse(responseSyncSucceeded);
        return responseSyncSucceeded;
    }
    
    @Override
    public boolean removeUserFromFollowingList(final String requestBody) throws XLEException {
        Log.i(SLSXsapiServiceManager.TAG, "removeUserFromFollowingList");
        XLEAssert.assertTrue(Thread.currentThread() != ThreadManager.UIThread);
        final HttpCall appendCommonParameters = HttpUtil.appendCommonParameters(new HttpCall("POST", String.format(XboxLiveEnvironment.Instance().updateProfileFollowingListUrl(), "remove"), ""), "1");
        appendCommonParameters.setRequestBody(requestBody);
        final boolean responseSyncSucceeded = TcuiHttpUtil.getResponseSyncSucceeded(appendCommonParameters, Arrays.asList(204));
        TcuiHttpUtil.throwIfNullOrFalse(responseSyncSucceeded);
        return responseSyncSucceeded;
    }
    
    @Override
    public boolean removeUserFromMutedList(final String s, final String requestBody) throws XLEException {
        Log.i(SLSXsapiServiceManager.TAG, "removeUserFromMutedList");
        XLEAssert.assertTrue(Thread.currentThread() != ThreadManager.UIThread);
        final HttpCall appendCommonParameters = HttpUtil.appendCommonParameters(new HttpCall("DELETE", String.format(XboxLiveEnvironment.Instance().getMutedServiceUrlFormat(), s), ""), "1");
        appendCommonParameters.setRequestBody(requestBody);
        final boolean responseSyncSucceeded = TcuiHttpUtil.getResponseSyncSucceeded(appendCommonParameters, new ArrayList<Integer>(0));
        TcuiHttpUtil.throwIfNullOrFalse(responseSyncSucceeded);
        return responseSyncSucceeded;
    }
    
    @Override
    public boolean removeUserFromNeverList(final String s, final String requestBody) throws XLEException {
        Log.i(SLSXsapiServiceManager.TAG, "removeUserFromNeverList");
        XLEAssert.assertTrue(Thread.currentThread() != ThreadManager.UIThread);
        final HttpCall appendCommonParameters = HttpUtil.appendCommonParameters(new HttpCall("DELETE", String.format(XboxLiveEnvironment.Instance().getProfileNeverListUrlFormat(), s), ""), "1");
        appendCommonParameters.setRequestBody(requestBody);
        final boolean responseSyncSucceeded = TcuiHttpUtil.getResponseSyncSucceeded(appendCommonParameters, new ArrayList<Integer>(0));
        TcuiHttpUtil.throwIfNullOrFalse(responseSyncSucceeded);
        return responseSyncSucceeded;
    }
    
    @Override
    public boolean setPrivacySettings(final PrivacySettingsResult privacySettingsResult) throws XLEException {
        Log.i(SLSXsapiServiceManager.TAG, "setPrivacySettings");
        XLEAssert.assertTrue(Thread.currentThread() != ThreadManager.UIThread);
        final HttpCall appendCommonParameters = HttpUtil.appendCommonParameters(new HttpCall("PUT", XboxLiveEnvironment.Instance().getUserProfileSettingUrlFormat(), ""), "4");
        appendCommonParameters.setRequestBody(PrivacySettingsResult.getPrivacySettingRequestBody(privacySettingsResult));
        final boolean responseSyncSucceeded = TcuiHttpUtil.getResponseSyncSucceeded(appendCommonParameters, Arrays.asList(201));
        TcuiHttpUtil.throwIfNullOrFalse(responseSyncSucceeded);
        return responseSyncSucceeded;
    }
    
    @Override
    public boolean submitFeedback(final String s, final String requestBody) throws XLEException {
        Log.i(SLSXsapiServiceManager.TAG, "submitFeedback");
        XLEAssert.assertTrue(Thread.currentThread() != ThreadManager.UIThread);
        final HttpCall appendCommonParameters = HttpUtil.appendCommonParameters(new HttpCall("POST", String.format(XboxLiveEnvironment.Instance().getSubmitFeedbackUrlFormat(), s), ""), "101");
        appendCommonParameters.setRequestBody(requestBody);
        final boolean responseSyncSucceeded = TcuiHttpUtil.getResponseSyncSucceeded(appendCommonParameters, new ArrayList<Integer>(202));
        TcuiHttpUtil.throwIfNullOrFalse(responseSyncSucceeded);
        return responseSyncSucceeded;
    }
}
