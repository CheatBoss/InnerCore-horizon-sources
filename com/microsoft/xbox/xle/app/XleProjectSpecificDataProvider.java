package com.microsoft.xbox.xle.app;

import java.util.*;
import com.microsoft.xboxtcui.*;
import android.content.res.*;
import com.microsoft.xbox.toolkit.*;
import android.util.*;
import com.microsoft.xbox.service.model.*;
import com.microsoft.xbox.toolkit.network.*;

public class XleProjectSpecificDataProvider implements IProjectSpecificDataProvider
{
    private static final String[][] displayLocales;
    private static XleProjectSpecificDataProvider instance;
    private String androidId;
    private Set<String> blockFeaturedChild;
    private boolean gotSettings;
    private boolean isMeAdult;
    private String meXuid;
    private Set<String> musicBlocked;
    private String privileges;
    private Set<String> promotionalRestrictedRegions;
    private Set<String> purchaseBlocked;
    private String scdRpsTicket;
    private Hashtable<String, String> serviceLocaleMapTable;
    private String[][] serviceLocales;
    private Set<String> videoBlocked;
    
    static {
        XleProjectSpecificDataProvider.instance = new XleProjectSpecificDataProvider();
        displayLocales = new String[][] { { "zh_SG", "zh", "CN" }, { "zh_CN", "zh", "CN" }, { "zh_HK", "zh", "TW" }, { "zh_TW", "zh", "TW" }, { "da", "da", "DK" }, { "nl", "nl", "NL" }, { "en", "en", "GB" }, { "en_US", "en", "US" }, { "fi", "fi", "FI" }, { "fr", "fr", "FR" }, { "de", "de", "DE" }, { "it", "it", "IT" }, { "ja", "ja", "JP" }, { "ko", "ko", "KR" }, { "nb", "nb", "NO" }, { "pl", "pl", "PL" }, { "pt_PT", "pt", "PT" }, { "pt", "pt", "BR" }, { "ru", "ru", "RU" }, { "es_ES", "es", "ES" }, { "es", "es", "MX" }, { "sv", "sv", "SE" }, { "tr", "tr", "TR" } };
    }
    
    private XleProjectSpecificDataProvider() {
        this.serviceLocaleMapTable = new Hashtable<String, String>();
        this.musicBlocked = new HashSet<String>();
        this.videoBlocked = new HashSet<String>();
        this.purchaseBlocked = new HashSet<String>();
        this.blockFeaturedChild = new HashSet<String>();
        this.promotionalRestrictedRegions = new HashSet<String>();
        this.serviceLocales = new String[][] { { "es_AR", "es-AR" }, { "AR", "es-AR" }, { "en_AU", "en-AU" }, { "AU", "en-AU" }, { "de_AT", "de-AT" }, { "AT", "de-AT" }, { "fr_BE", "fr-BE" }, { "nl_BE", "nl-BE" }, { "BE", "fr-BE" }, { "pt_BR", "pt-BR" }, { "BR", "pt-BR" }, { "en_CA", "en-CA" }, { "fr_CA", "fr-CA" }, { "CA", "en-CA" }, { "en_CZ", "en-CZ" }, { "CZ", "en-CZ" }, { "da_DK", "da-DK" }, { "DK", "da-DK" }, { "fi_FI", "fi-FI" }, { "FI", "fi-FI" }, { "fr_FR", "fr-FR" }, { "FR", "fr-FR" }, { "de_DE", "de-DE" }, { "DE", "de-DE" }, { "en_GR", "en-GR" }, { "GR", "en-GR" }, { "en_HK", "en-HK" }, { "zh_HK", "zh-HK" }, { "HK", "en-HK" }, { "en_HU", "en-HU" }, { "HU", "en-HU" }, { "en_IN", "en-IN" }, { "IN", "en-IN" }, { "en_GB", "en-GB" }, { "GB", "en-GB" }, { "en_IL", "en-IL" }, { "IL", "en-IL" }, { "it_IT", "it-IT" }, { "IT", "it-IT" }, { "ja_JP", "ja-JP" }, { "JP", "ja-JP" }, { "zh_CN", "zh-CN" }, { "CN", "zh-CN" }, { "es_MX", "es-MX" }, { "MX", "es-MX" }, { "es_CL", "es-CL" }, { "CL", "es-CL" }, { "es_CO", "es-CO" }, { "CO", "es-CO" }, { "nl_NL", "nl-NL" }, { "NL", "nl-NL" }, { "en_NZ", "en-NZ" }, { "NZ", "en-NZ" }, { "nb_NO", "nb-NO" }, { "NO", "nb-NO" }, { "pl_PL", "pl-PL" }, { "PL", "pl-PL" }, { "pt_PT", "pt-PT" }, { "PT", "pt-PT" }, { "ru_RU", "ru-RU" }, { "RU", "ru-RU" }, { "en_SA", "en-SA" }, { "SA", "en-SA" }, { "en_SG", "en-SG" }, { "zh_SG", "zh-SG" }, { "SG", "en-SG" }, { "en_SK", "en-SK" }, { "SK", "en-SK" }, { "en_ZA", "en-ZA" }, { "ZA", "en-ZA" }, { "ko_KR", "ko-KR" }, { "KR", "ko-KR" }, { "es_ES", "es-ES" }, { "es", "es-ES" }, { "de_CH", "de-CH" }, { "fr_CH", "fr-CH" }, { "CH", "fr-CH" }, { "zh_TW", "zh-TW" }, { "TW", "zh-TW" }, { "en_AE", "en-AE" }, { "AE", "en-AE" }, { "en_US", "en-US" }, { "US", "en-US" }, { "sv_SE", "sv-SE" }, { "SE", "sv-SE" }, { "tr_Tr", "tr-TR" }, { "Tr", "tr-TR" }, { "en_IE", "en-IE" }, { "IE", "en-IE" } };
        int n = 0;
        while (true) {
            final String[][] serviceLocales = this.serviceLocales;
            if (n >= serviceLocales.length) {
                break;
            }
            this.serviceLocaleMapTable.put(serviceLocales[n][0], serviceLocales[n][1]);
            ++n;
        }
        this.serviceLocales = null;
    }
    
    private void addRegions(final String s, final Set<String> set) {
        if (!JavaUtil.isNullOrEmpty(s)) {
            final String[] split = s.split("[|]");
            if (!XLEUtil.isNullOrEmpty(split)) {
                set.clear();
                for (int length = split.length, i = 0; i < length; ++i) {
                    final String s2 = split[i];
                    if (!JavaUtil.isNullOrEmpty(s2)) {
                        set.add(s2);
                    }
                }
            }
        }
    }
    
    private String getDeviceLocale() {
        final Locale default1 = Locale.getDefault();
        final String string = default1.toString();
        String s;
        if (this.serviceLocaleMapTable.containsKey(string)) {
            s = this.serviceLocaleMapTable.get(string);
        }
        else {
            final String country = default1.getCountry();
            if (JavaUtil.isNullOrEmpty(country) || !this.serviceLocaleMapTable.containsKey(country)) {
                return "en-US";
            }
            s = this.serviceLocaleMapTable.get(country);
        }
        return s;
    }
    
    public static XleProjectSpecificDataProvider getInstance() {
        return XleProjectSpecificDataProvider.instance;
    }
    
    public void ensureDisplayLocale() {
        final Locale default1 = Locale.getDefault();
        final String string = default1.toString();
        final String language = default1.getLanguage();
        final String country = default1.getCountry();
        int n = 0;
        Locale locale;
        while (true) {
            final String[][] displayLocales = XleProjectSpecificDataProvider.displayLocales;
            if (n >= displayLocales.length) {
                locale = null;
                break;
            }
            if (displayLocales[n][0].equals(string)) {
                if (XleProjectSpecificDataProvider.displayLocales[n][1].equals(language) && XleProjectSpecificDataProvider.displayLocales[n][2].equals(country)) {
                    return;
                }
                final String[][] displayLocales2 = XleProjectSpecificDataProvider.displayLocales;
                locale = new Locale(displayLocales2[n][1], displayLocales2[n][2]);
                break;
            }
            else {
                ++n;
            }
        }
        Locale locale2 = locale;
        if (locale == null) {
            int n2 = 0;
            while (true) {
                final String[][] displayLocales3 = XleProjectSpecificDataProvider.displayLocales;
                locale2 = locale;
                if (n2 >= displayLocales3.length) {
                    break;
                }
                if (displayLocales3[n2][0].equals(language)) {
                    final String[][] displayLocales4 = XleProjectSpecificDataProvider.displayLocales;
                    locale2 = new Locale(displayLocales4[n2][1], displayLocales4[n2][2]);
                    break;
                }
                ++n2;
            }
        }
        if (locale2 != null) {
            final DisplayMetrics displayMetrics = XboxTcuiSdk.getResources().getDisplayMetrics();
            final Configuration configuration = XboxTcuiSdk.getResources().getConfiguration();
            configuration.locale = locale2;
            XboxTcuiSdk.getResources().updateConfiguration(configuration, displayMetrics);
        }
    }
    
    @Override
    public boolean getAllowExplicitContent() {
        return true;
    }
    
    @Override
    public String getAutoSuggestdDataSource() {
        return "bbxall2";
    }
    
    @Override
    public String getCombinedContentRating() {
        return "";
    }
    
    @Override
    public String getConnectedLocale() {
        return this.getDeviceLocale();
    }
    
    @Override
    public String getConnectedLocale(final boolean b) {
        return this.getConnectedLocale();
    }
    
    @Override
    public String getContentRestrictions() {
        final String region = this.getRegion();
        final int meMaturityLevel = this.getMeMaturityLevel();
        if (!JavaUtil.isNullOrEmpty(region) && meMaturityLevel != 255) {
            final String jsonString = GsonUtil.toJsonString(new ContentRestrictions(region, meMaturityLevel, this.isPromotionalRestricted()));
            if (!JavaUtil.isNullOrEmpty(jsonString)) {
                return Base64.encodeToString(jsonString.getBytes(), 2);
            }
        }
        return null;
    }
    
    @Override
    public String getCurrentSandboxID() {
        return "PROD";
    }
    
    @Override
    public boolean getInitializeComplete() {
        return this.getXuidString() != null;
    }
    
    @Override
    public boolean getIsForXboxOne() {
        return true;
    }
    
    @Override
    public boolean getIsFreeAccount() {
        return false;
    }
    
    @Override
    public boolean getIsXboxMusicSupported() {
        return true;
    }
    
    @Override
    public String getLegalLocale() {
        return this.getConnectedLocale();
    }
    
    public int getMeMaturityLevel() {
        final ProfileModel meProfileModel = ProfileModel.getMeProfileModel();
        if (meProfileModel != null) {
            return meProfileModel.getMaturityLevel();
        }
        return 0;
    }
    
    @Override
    public String getMembershipLevel() {
        if (ProfileModel.getMeProfileModel().getAccountTier() == null) {
            return "Gold";
        }
        return ProfileModel.getMeProfileModel().getAccountTier();
    }
    
    @Override
    public String getPrivileges() {
        return this.privileges;
    }
    
    @Override
    public String getRegion() {
        return Locale.getDefault().getCountry();
    }
    
    @Override
    public String getSCDRpsTicket() {
        return this.scdRpsTicket;
    }
    
    @Override
    public String getVersionCheckUrl() {
        final int n = XleProjectSpecificDataProvider$1.$SwitchMap$com$microsoft$xbox$toolkit$network$XboxLiveEnvironment$Environment[XboxLiveEnvironment.Instance().getEnvironment().ordinal()];
        if (n != 1) {
            if (n == 2 || n == 3) {
                return "http://www.rtm.vint.xbox.com/en-US/Platform/Android/XboxLIVE/sgversion";
            }
            if (n != 4) {
                throw new UnsupportedOperationException();
            }
        }
        return "http://www.xbox.com/en-US/Platform/Android/XboxLIVE/sgversion";
    }
    
    @Override
    public int getVersionCode() {
        return 1;
    }
    
    @Override
    public String getWindowsLiveClientId() {
        final int n = XleProjectSpecificDataProvider$1.$SwitchMap$com$microsoft$xbox$toolkit$network$XboxLiveEnvironment$Environment[XboxLiveEnvironment.Instance().getEnvironment().ordinal()];
        if (n == 1) {
            return "0000000048093EE3";
        }
        if (n != 2 && n != 3 && n != 4) {
            throw new UnsupportedOperationException();
        }
        return "0000000068036303";
    }
    
    @Override
    public String getXuidString() {
        return this.meXuid;
    }
    
    public boolean gotSettings() {
        return this.gotSettings;
    }
    
    @Override
    public boolean isDeviceLocaleKnown() {
        final Locale default1 = Locale.getDefault();
        if (this.serviceLocaleMapTable.containsKey(default1.toString())) {
            return true;
        }
        final String country = default1.getCountry();
        return !JavaUtil.isNullOrEmpty(country) && this.serviceLocaleMapTable.containsKey(country);
    }
    
    public boolean isFeaturedBlocked() {
        return !this.isMeAdult() && this.blockFeaturedChild.contains(this.getRegion());
    }
    
    public boolean isMeAdult() {
        return this.isMeAdult;
    }
    
    public boolean isMusicBlocked() {
        return true;
    }
    
    public boolean isPromotionalRestricted() {
        return !this.isMeAdult() && this.promotionalRestrictedRegions.contains(this.getRegion());
    }
    
    public boolean isPurchaseBlocked() {
        return this.purchaseBlocked.contains(this.getRegion());
    }
    
    public boolean isVideoBlocked() {
        return true;
    }
    
    public void processContentBlockedList(final SmartglassSettings smartglassSettings) {
        this.addRegions(smartglassSettings.VIDEO_BLOCKED, this.videoBlocked);
        this.addRegions(smartglassSettings.MUSIC_BLOCKED, this.musicBlocked);
        this.addRegions(smartglassSettings.PURCHASE_BLOCKED, this.purchaseBlocked);
        this.addRegions(smartglassSettings.BLOCK_FEATURED_CHILD, this.blockFeaturedChild);
        this.addRegions(smartglassSettings.PROMOTIONAL_CONTENT_RESTRICTED_REGIONS, this.promotionalRestrictedRegions);
        this.gotSettings = true;
    }
    
    @Override
    public void resetModels(final boolean b) {
        ProfileModel.reset();
    }
    
    public void setIsMeAdult(final boolean isMeAdult) {
        this.isMeAdult = isMeAdult;
    }
    
    @Override
    public void setPrivileges(final String privileges) {
        this.privileges = privileges;
    }
    
    @Override
    public void setSCDRpsTicket(final String scdRpsTicket) {
        this.scdRpsTicket = scdRpsTicket;
    }
    
    @Override
    public void setXuidString(final String meXuid) {
        this.meXuid = meXuid;
    }
    
    private class ContentRestrictions
    {
        public Data data;
        public int version;
        
        public ContentRestrictions(final String geographicRegion, final int n, final boolean restrictPromotionalContent) {
            this.version = 2;
            final Data data = new Data(this);
            this.data = data;
            data.geographicRegion = geographicRegion;
            final Data data2 = this.data;
            data2.preferredAgeRating = n;
            data2.maxAgeRating = n;
            this.data.restrictPromotionalContent = restrictPromotionalContent;
        }
    }
    
    public class Data
    {
        public String geographicRegion;
        public int maxAgeRating;
        public int preferredAgeRating;
        public boolean restrictPromotionalContent;
        final /* synthetic */ ContentRestrictions this$1;
        
        public Data(final ContentRestrictions this$1) {
            this.this$1 = this$1;
        }
    }
}
