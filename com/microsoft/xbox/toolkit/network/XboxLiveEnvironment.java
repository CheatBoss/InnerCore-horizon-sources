package com.microsoft.xbox.toolkit.network;

import com.microsoft.xbox.toolkit.*;

public class XboxLiveEnvironment
{
    public static final String NEVER_LIST_CONTRACT_VERSION = "1";
    public static final String SHARE_IDENTITY_CONTRACT_VERSION = "4";
    public static final String SOCIAL_SERVICE_GENERAL_CONTRACT_VERSION = "1";
    public static final String USER_PROFILE_CONTRACT_VERSION = "3";
    public static final String USER_PROFILE_PRIVACY_SETTINGS_CONTRACT_VERSION = "4";
    private static XboxLiveEnvironment instance;
    private Environment environment;
    private final boolean useProxy;
    
    static {
        XboxLiveEnvironment.instance = new XboxLiveEnvironment();
    }
    
    public XboxLiveEnvironment() {
        this.environment = Environment.PROD;
        this.useProxy = false;
    }
    
    public static XboxLiveEnvironment Instance() {
        return XboxLiveEnvironment.instance;
    }
    
    public String getAddFriendsToShareIdentityUrlFormat() {
        final int n = XboxLiveEnvironment$1.$SwitchMap$com$microsoft$xbox$toolkit$network$XboxLiveEnvironment$Environment[this.environment.ordinal()];
        if (n == 1 || n == 2) {
            return "https://social.dnet.xboxlive.com/users/xuid(%s)/people/identityshared/xuids?method=add";
        }
        if (n == 4) {
            return "https://social.xboxlive.com/users/xuid(%s)/people/identityshared/xuids?method=add";
        }
        throw new UnsupportedOperationException();
    }
    
    public Environment getEnvironment() {
        return this.environment;
    }
    
    public String getFriendFinderSettingsUrl() {
        return "https://settings.xboxlive.com/settings/feature/friendfinder/settings";
    }
    
    public String getGamertagSearchUrlFormat() {
        final int n = XboxLiveEnvironment$1.$SwitchMap$com$microsoft$xbox$toolkit$network$XboxLiveEnvironment$Environment[this.environment.ordinal()];
        if (n == 1 || n == 2 || n == 3) {
            return "https://profile.dnet.xboxlive.com/users/gt(%s)/profile/settings?settings=AppDisplayName,DisplayPic,Gamerscore,Gamertag,PublicGamerpic,XboxOneRep";
        }
        if (n == 4) {
            return "https://profile.xboxlive.com/users/gt(%s)/profile/settings?settings=AppDisplayName,DisplayPic,Gamerscore,Gamertag,PublicGamerpic,XboxOneRep";
        }
        throw new UnsupportedOperationException();
    }
    
    public String getMutedServiceUrlFormat() {
        return "https://privacy.xboxlive.com/users/xuid(%s)/people/mute";
    }
    
    public String getPeopleHubFriendFinderStateUrlFormat() {
        return "https://peoplehub.xboxlive.com/users/me/friendfinder";
    }
    
    public String getPeopleHubRecommendationsUrlFormat() {
        return "https://peoplehub.xboxlive.com/users/me/people/recommendations";
    }
    
    public String getProfileFavoriteListUrl() {
        final int n = XboxLiveEnvironment$1.$SwitchMap$com$microsoft$xbox$toolkit$network$XboxLiveEnvironment$Environment[this.environment.ordinal()];
        if (n == 1 || n == 2 || n == 3) {
            return "https://social.dnet.xboxlive.com/users/me/people/favorites/xuids?method=%s";
        }
        if (n == 4) {
            return "https://social.xboxlive.com/users/me/people/favorites/xuids?method=%s";
        }
        throw new UnsupportedOperationException();
    }
    
    public String getProfileNeverListUrlFormat() {
        final int n = XboxLiveEnvironment$1.$SwitchMap$com$microsoft$xbox$toolkit$network$XboxLiveEnvironment$Environment[this.environment.ordinal()];
        if (n == 1 || n == 2 || n == 3) {
            return "https://privacy.dnet.xboxlive.com/users/xuid(%s)/people/never";
        }
        if (n == 4) {
            return "https://privacy.xboxlive.com/users/xuid(%s)/people/never";
        }
        throw new UnsupportedOperationException();
    }
    
    public String getProfileSettingUrlFormat() {
        return "https://privacy.xboxlive.com/users/me/privacy/settings/%s";
    }
    
    public String getProfileSummaryUrlFormat() {
        final int n = XboxLiveEnvironment$1.$SwitchMap$com$microsoft$xbox$toolkit$network$XboxLiveEnvironment$Environment[this.environment.ordinal()];
        if (n == 1 || n == 2) {
            return "https://social.dnet.xboxlive.com/users/xuid(%s)/summary";
        }
        if (n == 4) {
            return "https://social.xboxlive.com/users/xuid(%s)/summary";
        }
        throw new UnsupportedOperationException();
    }
    
    public boolean getProxyEnabled() {
        return false;
    }
    
    public String getRemoveUsersFromShareIdentityUrlFormat() {
        final int n = XboxLiveEnvironment$1.$SwitchMap$com$microsoft$xbox$toolkit$network$XboxLiveEnvironment$Environment[this.environment.ordinal()];
        if (n == 1 || n == 2) {
            return "https://social.dnet.xboxlive.com/users/xuid(%s)/people/identityshared/xuids?method=remove";
        }
        if (n == 4) {
            return "https://social.xboxlive.com/users/xuid(%s)/people/identityshared/xuids?method=remove";
        }
        throw new UnsupportedOperationException();
    }
    
    public String getSetFriendFinderOptInStatusUrlFormat() {
        return "https://friendfinder.xboxlive.com/users/me/networks/%s/optin";
    }
    
    public String getShortCircuitProfileUrlFormat() {
        return "https://pf.directory.live.com/profile/mine/System.ShortCircuitProfile.json";
    }
    
    public String getSubmitFeedbackUrlFormat() {
        return "https://reputation.xboxlive.com/users/xuid(%s)/feedback";
    }
    
    public String getTenureWatermarkUrlFormat() {
        return "http://dlassets.xboxlive.com/public/content/ppl/watermarks/tenure/%s.png";
    }
    
    public String getUpdateThirdPartyTokenUrlFormat() {
        return "https://thirdpartytokens.xboxlive.com/users/me/networks/%s/token";
    }
    
    public String getUploadingPhoneContactsUrlFormat() {
        return "https://people.directory.live.com/people/ExternalSCDLookup";
    }
    
    public String getUserProfileInfoUrl() {
        final int n = XboxLiveEnvironment$1.$SwitchMap$com$microsoft$xbox$toolkit$network$XboxLiveEnvironment$Environment[this.environment.ordinal()];
        if (n == 1 || n == 2 || n == 3) {
            return "https://profile.dnet.xboxlive.com/users/batch/profile/settings";
        }
        if (n == 4) {
            return "https://profile.xboxlive.com/users/batch/profile/settings";
        }
        throw new UnsupportedOperationException();
    }
    
    public String getUserProfileSettingUrlFormat() {
        return "https://privacy.xboxlive.com/users/me/privacy/settings";
    }
    
    public String getWatermarkUrl(final String s) {
        final String lowerCase = s.toLowerCase();
        int n = 0;
        Label_0203: {
            switch (lowerCase.hashCode()) {
                case 2056113039: {
                    if (lowerCase.equals("xboxlivelaunchteam")) {
                        n = 2;
                        break Label_0203;
                    }
                    break;
                }
                case 1584505217: {
                    if (lowerCase.equals("xboxoriginalteam")) {
                        n = 1;
                        break Label_0203;
                    }
                    break;
                }
                case 949652176: {
                    if (lowerCase.equals("xboxnxoeteam")) {
                        n = 7;
                        break Label_0203;
                    }
                    break;
                }
                case 742262976: {
                    if (lowerCase.equals("cheater")) {
                        n = 0;
                        break Label_0203;
                    }
                    break;
                }
                case 547378320: {
                    if (lowerCase.equals("launchteam")) {
                        n = 3;
                        break Label_0203;
                    }
                    break;
                }
                case 467871267: {
                    if (lowerCase.equals("kinectteam")) {
                        n = 5;
                        break Label_0203;
                    }
                    break;
                }
                case -69693424: {
                    if (lowerCase.equals("xboxoneteam")) {
                        n = 6;
                        break Label_0203;
                    }
                    break;
                }
                case -1921480520: {
                    if (lowerCase.equals("nxeteam")) {
                        n = 4;
                        break Label_0203;
                    }
                    break;
                }
            }
            n = -1;
        }
        switch (n) {
            default: {
                final StringBuilder sb = new StringBuilder();
                sb.append("Unsupported watermark value: ");
                sb.append(s);
                XLEAssert.fail(sb.toString());
                return "";
            }
            case 7: {
                return "http://dlassets.xboxlive.com/public/content/ppl/watermarks/launch/xboxnxoeteam.png";
            }
            case 6: {
                return "http://dlassets.xboxlive.com/public/content/ppl/watermarks/launch/xboxoneteam.png";
            }
            case 5: {
                return "http://dlassets.xboxlive.com/public/content/ppl/watermarks/launch/kinectteam.png";
            }
            case 4: {
                return "http://dlassets.xboxlive.com/public/content/ppl/watermarks/launch/nxeteam.png";
            }
            case 3: {
                return "http://dlassets.xboxlive.com/public/content/ppl/watermarks/launch/launchteam.png";
            }
            case 2: {
                return "http://dlassets.xboxlive.com/public/content/ppl/watermarks/launch/xboxlivelaunchteam.png";
            }
            case 1: {
                return "http://dlassets.xboxlive.com/public/content/ppl/watermarks/launch/xboxoriginalteam.png";
            }
            case 0: {
                return "http://dlassets.xboxlive.com/public/content/ppl/watermarks/cheater.png";
            }
        }
    }
    
    public String updateProfileFollowingListUrl() {
        final int n = XboxLiveEnvironment$1.$SwitchMap$com$microsoft$xbox$toolkit$network$XboxLiveEnvironment$Environment[this.environment.ordinal()];
        if (n == 1 || n == 2 || n == 3) {
            return "https://social.dnet.xboxlive.com/users/me/people/xuids?method=%s";
        }
        if (n == 4) {
            return "https://social.xboxlive.com/users/me/people/xuids?method=%s";
        }
        throw new UnsupportedOperationException();
    }
    
    public enum Environment
    {
        CERTNET, 
        DNET, 
        PARTNERNET, 
        PROD, 
        STUB, 
        VINT;
    }
}
