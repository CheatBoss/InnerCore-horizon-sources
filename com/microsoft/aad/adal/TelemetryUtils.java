package com.microsoft.aad.adal;

import java.util.*;
import java.util.regex.*;
import java.io.*;

final class TelemetryUtils
{
    static final Set<String> GDPR_FILTERED_FIELDS;
    private static final String TAG;
    
    static {
        GDPR_FILTERED_FIELDS = new HashSet<String>();
        TAG = TelemetryUtils.class.getSimpleName();
        initializeGdprFilteredFields();
    }
    
    private TelemetryUtils() {
    }
    
    private static void initializeGdprFilteredFields() {
        TelemetryUtils.GDPR_FILTERED_FIELDS.addAll(Arrays.asList("Microsoft.ADAL.login_hint", "Microsoft.ADAL.user_id", "Microsoft.ADAL.tenant_id"));
    }
    
    static CliTelemInfo parseXMsCliTelemHeader(String tag) {
        if (StringExtensions.isNullOrBlank(tag)) {
            return null;
        }
        final String[] split = tag.split(",");
        if (split.length == 0) {
            Logger.w(TelemetryUtils.TAG, "SPE Ring header missing version field.", null, ADALError.X_MS_CLITELEM_VERSION_UNRECOGNIZED);
            return null;
        }
        final String version = split[0];
        final CliTelemInfo cliTelemInfo = new CliTelemInfo();
        cliTelemInfo.setVersion(version);
        if (!version.equals("1")) {
            tag = TelemetryUtils.TAG;
            final StringBuilder sb = new StringBuilder();
            sb.append("Header version: ");
            sb.append(version);
            Logger.w(tag, "Unexpected header version. ", sb.toString(), ADALError.X_MS_CLITELEM_VERSION_UNRECOGNIZED);
            return null;
        }
        if (!Pattern.compile("^[1-9]+\\.?[0-9|\\.]*,[0-9|\\.]*,[0-9|\\.]*,[^,]*[0-9\\.]*,[^,]*$").matcher(tag).matches()) {
            Logger.w(TelemetryUtils.TAG, "", "", ADALError.X_MS_CLITELEM_MALFORMED);
            return null;
        }
        final String[] split2 = tag.split(",", 5);
        cliTelemInfo.setServerErrorCode(split2[1]);
        cliTelemInfo.setServerSubErrorCode(split2[2]);
        cliTelemInfo.setRefreshTokenAge(split2[3]);
        cliTelemInfo.setSpeRing(split2[4]);
        return cliTelemInfo;
    }
    
    static class CliTelemInfo implements Serializable
    {
        private String mRefreshTokenAge;
        private String mServerErrorCode;
        private String mServerSubErrorCode;
        private String mSpeRing;
        private String mVersion;
        
        String getRefreshTokenAge() {
            return this.mRefreshTokenAge;
        }
        
        String getServerErrorCode() {
            return this.mServerErrorCode;
        }
        
        String getServerSubErrorCode() {
            return this.mServerSubErrorCode;
        }
        
        String getSpeRing() {
            return this.mSpeRing;
        }
        
        String getVersion() {
            return this.mVersion;
        }
        
        void setRefreshTokenAge(final String mRefreshTokenAge) {
            this.mRefreshTokenAge = mRefreshTokenAge;
        }
        
        void setServerErrorCode(final String mServerErrorCode) {
            this.mServerErrorCode = mServerErrorCode;
        }
        
        void setServerSubErrorCode(final String mServerSubErrorCode) {
            this.mServerSubErrorCode = mServerSubErrorCode;
        }
        
        void setSpeRing(final String mSpeRing) {
            this.mSpeRing = mSpeRing;
        }
        
        void setVersion(final String mVersion) {
            this.mVersion = mVersion;
        }
    }
}
