package com.appboy.ui.inappmessage.jsinterface;

import android.content.*;
import com.appboy.support.*;
import android.webkit.*;
import java.util.*;
import com.appboy.*;
import org.json.*;
import com.appboy.enums.*;

public class AppboyInAppMessageHtmlUserJavascriptInterface
{
    public static final String JS_BRIDGE_ATTRIBUTE_VALUE = "value";
    public static final String JS_BRIDGE_GENDER_FEMALE;
    public static final String JS_BRIDGE_GENDER_MALE;
    public static final String JS_BRIDGE_GENDER_NOT_APPLICABLE;
    public static final String JS_BRIDGE_GENDER_OTHER;
    public static final String JS_BRIDGE_GENDER_PREFER_NOT_TO_SAY;
    public static final String JS_BRIDGE_GENDER_UNKNOWN;
    public static final String JS_BRIDGE_OPTED_IN = "opted_in";
    public static final String JS_BRIDGE_SUBSCRIBED = "subscribed";
    public static final String JS_BRIDGE_UNSUBSCRIBED = "unsubscribed";
    private static final String TAG;
    private Context mContext;
    
    static {
        TAG = AppboyLogger.getAppboyLogTag(AppboyInAppMessageHtmlUserJavascriptInterface.class);
        JS_BRIDGE_GENDER_MALE = Gender.MALE.forJsonPut();
        JS_BRIDGE_GENDER_FEMALE = Gender.FEMALE.forJsonPut();
        JS_BRIDGE_GENDER_OTHER = Gender.OTHER.forJsonPut();
        JS_BRIDGE_GENDER_UNKNOWN = Gender.UNKNOWN.forJsonPut();
        JS_BRIDGE_GENDER_NOT_APPLICABLE = Gender.NOT_APPLICABLE.forJsonPut();
        JS_BRIDGE_GENDER_PREFER_NOT_TO_SAY = Gender.PREFER_NOT_TO_SAY.forJsonPut();
    }
    
    public AppboyInAppMessageHtmlUserJavascriptInterface(final Context mContext) {
        this.mContext = mContext;
    }
    
    @JavascriptInterface
    public void addToCustomAttributeArray(final String s, final String s2) {
        Appboy.getInstance(this.mContext).getCurrentUser().addToCustomAttributeArray(s, s2);
    }
    
    @JavascriptInterface
    public void incrementCustomUserAttribute(final String s) {
        Appboy.getInstance(this.mContext).getCurrentUser().incrementCustomUserAttribute(s);
    }
    
    Month monthFromInt(final int n) {
        if (n >= 1 && n <= 12) {
            return Month.getMonth(n - 1);
        }
        return null;
    }
    
    Gender parseGender(String lowerCase) {
        if (lowerCase == null) {
            return null;
        }
        lowerCase = lowerCase.toLowerCase(Locale.US);
        if (lowerCase.equals(AppboyInAppMessageHtmlUserJavascriptInterface.JS_BRIDGE_GENDER_MALE)) {
            return Gender.MALE;
        }
        if (lowerCase.equals(AppboyInAppMessageHtmlUserJavascriptInterface.JS_BRIDGE_GENDER_FEMALE)) {
            return Gender.FEMALE;
        }
        if (lowerCase.equals(AppboyInAppMessageHtmlUserJavascriptInterface.JS_BRIDGE_GENDER_OTHER)) {
            return Gender.OTHER;
        }
        if (lowerCase.equals(AppboyInAppMessageHtmlUserJavascriptInterface.JS_BRIDGE_GENDER_UNKNOWN)) {
            return Gender.UNKNOWN;
        }
        if (lowerCase.equals(AppboyInAppMessageHtmlUserJavascriptInterface.JS_BRIDGE_GENDER_NOT_APPLICABLE)) {
            return Gender.NOT_APPLICABLE;
        }
        if (lowerCase.equals(AppboyInAppMessageHtmlUserJavascriptInterface.JS_BRIDGE_GENDER_PREFER_NOT_TO_SAY)) {
            return Gender.PREFER_NOT_TO_SAY;
        }
        return null;
    }
    
    String[] parseStringArrayFromJsonString(final String s) {
        try {
            final JSONArray jsonArray = new JSONArray(s);
            final ArrayList<String> list = new ArrayList<String>();
            for (int i = 0; i < jsonArray.length(); ++i) {
                list.add(jsonArray.getString(i));
            }
            return list.toArray(new String[0]);
        }
        catch (Exception ex) {
            AppboyLogger.e(AppboyInAppMessageHtmlUserJavascriptInterface.TAG, "Failed to parse custom attribute array", ex);
            return null;
        }
    }
    
    @JavascriptInterface
    public void removeFromCustomAttributeArray(final String s, final String s2) {
        Appboy.getInstance(this.mContext).getCurrentUser().removeFromCustomAttributeArray(s, s2);
    }
    
    @JavascriptInterface
    public void setCountry(final String country) {
        Appboy.getInstance(this.mContext).getCurrentUser().setCountry(country);
    }
    
    void setCustomAttribute(final AppboyUser appboyUser, final String s, String tag) {
        try {
            final Object value = new JSONObject(tag).get("value");
            if (value instanceof String) {
                appboyUser.setCustomUserAttribute(s, (String)value);
                return;
            }
            if (value instanceof Boolean) {
                appboyUser.setCustomUserAttribute(s, (boolean)value);
                return;
            }
            if (value instanceof Integer) {
                appboyUser.setCustomUserAttribute(s, (int)value);
                return;
            }
            if (value instanceof Double) {
                appboyUser.setCustomUserAttribute(s, ((Double)value).floatValue());
                return;
            }
            final String tag2 = AppboyInAppMessageHtmlUserJavascriptInterface.TAG;
            final StringBuilder sb = new StringBuilder();
            sb.append("Failed to parse custom attribute type for key: ");
            sb.append(s);
            AppboyLogger.e(tag2, sb.toString());
        }
        catch (Exception ex) {
            tag = AppboyInAppMessageHtmlUserJavascriptInterface.TAG;
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Failed to parse custom attribute type for key: ");
            sb2.append(s);
            AppboyLogger.e(tag, sb2.toString(), ex);
        }
    }
    
    @JavascriptInterface
    public void setCustomUserAttributeArray(final String s, String tag) {
        final String[] stringArrayFromJsonString = this.parseStringArrayFromJsonString(tag);
        if (stringArrayFromJsonString == null) {
            tag = AppboyInAppMessageHtmlUserJavascriptInterface.TAG;
            final StringBuilder sb = new StringBuilder();
            sb.append("Failed to set custom attribute array for key ");
            sb.append(s);
            AppboyLogger.e(tag, sb.toString());
            return;
        }
        Appboy.getInstance(this.mContext).getCurrentUser().setCustomAttributeArray(s, stringArrayFromJsonString);
    }
    
    @JavascriptInterface
    public void setCustomUserAttributeJSON(final String s, final String s2) {
        this.setCustomAttribute(Appboy.getInstance(this.mContext).getCurrentUser(), s, s2);
    }
    
    @JavascriptInterface
    public void setDateOfBirth(final int n, final int n2, final int n3) {
        final Month monthFromInt = this.monthFromInt(n2);
        if (monthFromInt == null) {
            final String tag = AppboyInAppMessageHtmlUserJavascriptInterface.TAG;
            final StringBuilder sb = new StringBuilder();
            sb.append("Failed to parse month for value ");
            sb.append(n2);
            AppboyLogger.e(tag, sb.toString());
            return;
        }
        Appboy.getInstance(this.mContext).getCurrentUser().setDateOfBirth(n, monthFromInt, n3);
    }
    
    @JavascriptInterface
    public void setEmail(final String email) {
        Appboy.getInstance(this.mContext).getCurrentUser().setEmail(email);
    }
    
    @JavascriptInterface
    public void setEmailNotificationSubscriptionType(final String s) {
        final NotificationSubscriptionType subscriptionTypeFromJavascriptString = this.subscriptionTypeFromJavascriptString(s);
        if (subscriptionTypeFromJavascriptString == null) {
            final String tag = AppboyInAppMessageHtmlUserJavascriptInterface.TAG;
            final StringBuilder sb = new StringBuilder();
            sb.append("Failed to parse email subscription type in Braze HTML in-app message javascript interface with subscription ");
            sb.append(s);
            AppboyLogger.e(tag, sb.toString());
            return;
        }
        Appboy.getInstance(this.mContext).getCurrentUser().setEmailNotificationSubscriptionType(subscriptionTypeFromJavascriptString);
    }
    
    @JavascriptInterface
    public void setFirstName(final String firstName) {
        Appboy.getInstance(this.mContext).getCurrentUser().setFirstName(firstName);
    }
    
    @JavascriptInterface
    public void setGender(final String s) {
        final Gender gender = this.parseGender(s);
        if (gender == null) {
            final String tag = AppboyInAppMessageHtmlUserJavascriptInterface.TAG;
            final StringBuilder sb = new StringBuilder();
            sb.append("Failed to parse gender in Braze HTML in-app message javascript interface with gender: ");
            sb.append(s);
            AppboyLogger.e(tag, sb.toString());
            return;
        }
        Appboy.getInstance(this.mContext).getCurrentUser().setGender(gender);
    }
    
    @JavascriptInterface
    public void setHomeCity(final String homeCity) {
        Appboy.getInstance(this.mContext).getCurrentUser().setHomeCity(homeCity);
    }
    
    @JavascriptInterface
    public void setLanguage(final String language) {
        Appboy.getInstance(this.mContext).getCurrentUser().setLanguage(language);
    }
    
    @JavascriptInterface
    public void setLastName(final String lastName) {
        Appboy.getInstance(this.mContext).getCurrentUser().setLastName(lastName);
    }
    
    @JavascriptInterface
    public void setPhoneNumber(final String phoneNumber) {
        Appboy.getInstance(this.mContext).getCurrentUser().setPhoneNumber(phoneNumber);
    }
    
    @JavascriptInterface
    public void setPushNotificationSubscriptionType(final String s) {
        final NotificationSubscriptionType subscriptionTypeFromJavascriptString = this.subscriptionTypeFromJavascriptString(s);
        if (subscriptionTypeFromJavascriptString == null) {
            final String tag = AppboyInAppMessageHtmlUserJavascriptInterface.TAG;
            final StringBuilder sb = new StringBuilder();
            sb.append("Failed to parse push subscription type in Braze HTML in-app message javascript interface with subscription: ");
            sb.append(s);
            AppboyLogger.e(tag, sb.toString());
            return;
        }
        Appboy.getInstance(this.mContext).getCurrentUser().setPushNotificationSubscriptionType(subscriptionTypeFromJavascriptString);
    }
    
    NotificationSubscriptionType subscriptionTypeFromJavascriptString(String lowerCase) {
        lowerCase = lowerCase.toLowerCase(Locale.US);
        if (lowerCase.equals("subscribed")) {
            return NotificationSubscriptionType.SUBSCRIBED;
        }
        if (lowerCase.equals("unsubscribed")) {
            return NotificationSubscriptionType.UNSUBSCRIBED;
        }
        if (lowerCase.equals("opted_in")) {
            return NotificationSubscriptionType.OPTED_IN;
        }
        return null;
    }
}
