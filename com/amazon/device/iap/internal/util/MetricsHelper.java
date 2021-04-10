package com.amazon.device.iap.internal.util;

import org.json.*;
import com.amazon.device.iap.model.*;
import com.amazon.device.iap.internal.b.h.*;

public class MetricsHelper
{
    private static final String DESCRIPTION = "description";
    private static final String EXCEPTION_MESSAGE = "exceptionMessage";
    private static final String EXCEPTION_METRIC = "GenericException";
    private static final String JSON_PARSING_EXCEPTION_METRIC = "JsonParsingFailed";
    private static final String JSON_STRING = "jsonString";
    private static final String RECEIPT_VERIFICATION_FAILED_METRIC = "IapReceiptVerificationFailed";
    private static final String SIGNATURE = "signature";
    private static final String STRING_TO_SIGN = "stringToSign";
    private static final String TAG;
    
    static {
        TAG = MetricsHelper.class.getSimpleName();
    }
    
    public static void submitExceptionMetrics(final String s, String tag, final Exception ex) {
        try {
            final JSONObject jsonObject = new JSONObject();
            jsonObject.put("exceptionMessage", (Object)ex.getMessage());
            jsonObject.put("description", (Object)tag);
            submitMetric(s, "GenericException", jsonObject);
        }
        catch (Exception ex2) {
            tag = MetricsHelper.TAG;
            final StringBuilder sb = new StringBuilder();
            sb.append("error calling submitMetric: ");
            sb.append(ex2);
            e.b(tag, sb.toString());
        }
    }
    
    public static void submitJsonParsingExceptionMetrics(final String s, String tag, final String s2) {
        try {
            final JSONObject jsonObject = new JSONObject();
            jsonObject.put("jsonString", (Object)tag);
            jsonObject.put("description", (Object)s2);
            submitMetric(s, "JsonParsingFailed", jsonObject);
        }
        catch (Exception ex) {
            tag = MetricsHelper.TAG;
            final StringBuilder sb = new StringBuilder();
            sb.append("error calling submitMetric: ");
            sb.append(ex);
            e.b(tag, sb.toString());
        }
    }
    
    protected static void submitMetric(final String s, final String s2, final JSONObject jsonObject) {
        new a(new com.amazon.device.iap.internal.b.e(RequestId.fromString(s)), s2, jsonObject.toString()).a_();
    }
    
    public static void submitReceiptVerificationFailureMetrics(final String s, String tag, final String s2) {
        try {
            final JSONObject jsonObject = new JSONObject();
            jsonObject.put("stringToSign", (Object)tag);
            jsonObject.put("signature", (Object)s2);
            submitMetric(s, "IapReceiptVerificationFailed", jsonObject);
        }
        catch (Exception ex) {
            tag = MetricsHelper.TAG;
            final StringBuilder sb = new StringBuilder();
            sb.append("error calling submitMetric: ");
            sb.append(ex);
            e.b(tag, sb.toString());
        }
    }
}
