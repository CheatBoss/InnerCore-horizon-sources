package com.googleplay.licensing;

import java.util.regex.*;
import android.text.*;

public class ResponseData
{
    public String extra;
    public int nonce;
    public String packageName;
    public int responseCode;
    public long timestamp;
    public String userId;
    public String versionCode;
    
    public static ResponseData parse(String substring) {
        final int index = substring.indexOf(58);
        String extra = "";
        String substring2;
        if (-1 == index) {
            substring2 = substring;
        }
        else {
            substring2 = substring.substring(0, index);
            if (index >= substring.length()) {
                substring = extra;
            }
            else {
                substring = substring.substring(index + 1);
            }
            extra = substring;
        }
        final String[] split = TextUtils.split(substring2, Pattern.quote("|"));
        if (split.length >= 6) {
            final ResponseData responseData = new ResponseData();
            responseData.extra = extra;
            responseData.responseCode = Integer.parseInt(split[0]);
            responseData.nonce = Integer.parseInt(split[1]);
            responseData.packageName = split[2];
            responseData.versionCode = split[3];
            responseData.userId = split[4];
            responseData.timestamp = Long.parseLong(split[5]);
            return responseData;
        }
        throw new IllegalArgumentException("Wrong number of fields.");
    }
    
    @Override
    public String toString() {
        return TextUtils.join((CharSequence)"|", new Object[] { this.responseCode, this.nonce, this.packageName, this.versionCode, this.userId, this.timestamp });
    }
}
