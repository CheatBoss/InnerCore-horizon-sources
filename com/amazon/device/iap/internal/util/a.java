package com.amazon.device.iap.internal.util;

import com.amazon.device.iap.model.*;
import com.amazon.device.iap.internal.model.*;
import java.util.*;
import org.json.*;
import com.amazon.device.iap.internal.b.*;
import com.amazon.android.*;
import java.text.*;

public class a
{
    private static final String a;
    
    static {
        a = a.class.getSimpleName();
    }
    
    private static Receipt a(final JSONObject jsonObject) throws JSONException {
        final String optString = jsonObject.optString("token");
        final String string = jsonObject.getString("sku");
        final ProductType value = ProductType.valueOf(jsonObject.getString("itemType").toUpperCase());
        final String optString2 = jsonObject.optString("startDate");
        final boolean a = a(optString2);
        final Date date = null;
        Date b;
        if (a) {
            b = null;
        }
        else {
            b = b(optString2);
        }
        final String optString3 = jsonObject.optString("endDate");
        Date b2;
        if (a(optString3)) {
            b2 = date;
        }
        else {
            b2 = b(optString3);
        }
        return new ReceiptBuilder().setReceiptId(optString).setSku(string).setProductType(value).setPurchaseDate(b).setCancelDate(b2).build();
    }
    
    public static Receipt a(final JSONObject jsonObject, final String s, final String s2) throws com.amazon.device.iap.internal.b.a, d, IllegalArgumentException {
        final int n = a$1.a[b(jsonObject).ordinal()];
        if (n == 1) {
            return c(jsonObject, s, s2);
        }
        if (n != 2) {
            return d(jsonObject, s, s2);
        }
        return b(jsonObject, s, s2);
    }
    
    protected static boolean a(final String s) {
        return s == null || s.trim().length() == 0;
    }
    
    private static c b(final JSONObject jsonObject) {
        final String optString = jsonObject.optString("DeviceId");
        if (!com.amazon.device.iap.internal.util.d.a(jsonObject.optString("receiptId"))) {
            return c.c;
        }
        if (com.amazon.device.iap.internal.util.d.a(optString)) {
            return c.a;
        }
        return c.b;
    }
    
    private static Receipt b(JSONObject string, String a, final String s) throws com.amazon.device.iap.internal.b.a, d {
        final String optString = string.optString("signature");
        if (!com.amazon.device.iap.internal.util.d.a(optString)) {
            try {
                final Receipt a2 = a(string);
                string = (JSONObject)new StringBuilder();
                ((StringBuilder)string).append(a);
                ((StringBuilder)string).append("-");
                ((StringBuilder)string).append(a2.getReceiptId());
                string = (JSONObject)((StringBuilder)string).toString();
                final boolean signedByKiwi = Kiwi.isSignedByKiwi((String)string, optString);
                a = a.a;
                final StringBuilder sb = new StringBuilder();
                sb.append("stringToVerify/legacy:\n");
                sb.append((String)string);
                sb.append("\nsignature:\n");
                sb.append(optString);
                e.a(a, sb.toString());
                if (signedByKiwi) {
                    return a2;
                }
                MetricsHelper.submitReceiptVerificationFailureMetrics(s, (String)string, optString);
                throw new d(s, (String)string, optString);
            }
            catch (JSONException ex) {
                throw new com.amazon.device.iap.internal.b.a(s, string.toString(), (Throwable)ex);
            }
        }
        final String a3 = a.a;
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("a signature was not found in the receipt for request ID ");
        sb2.append(s);
        e.b(a3, sb2.toString());
        MetricsHelper.submitReceiptVerificationFailureMetrics(s, "NO Signature found", optString);
        throw new d(s, null, optString);
    }
    
    protected static Date b(final String s) throws JSONException {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
        try {
            Date parse = simpleDateFormat.parse(s);
            if (0L == parse.getTime()) {
                parse = null;
            }
            return parse;
        }
        catch (ParseException ex) {
            throw new JSONException(ex.getMessage());
        }
    }
    
    private static Receipt c(JSONObject jsonObject, String a, final String s) throws com.amazon.device.iap.internal.b.a, d {
        final String optString = jsonObject.optString("DeviceId");
        final String optString2 = jsonObject.optString("signature");
        final boolean a2 = com.amazon.device.iap.internal.util.d.a(optString2);
        Object cancelDate = null;
        if (!a2) {
            try {
                final Receipt a3 = a(jsonObject);
                final ProductType productType = a3.getProductType();
                final String sku = a3.getSku();
                final String receiptId = a3.getReceiptId();
                if (ProductType.SUBSCRIPTION == a3.getProductType()) {
                    jsonObject = (JSONObject)a3.getPurchaseDate();
                }
                else {
                    jsonObject = null;
                }
                if (ProductType.SUBSCRIPTION == a3.getProductType()) {
                    cancelDate = a3.getCancelDate();
                }
                jsonObject = (JSONObject)String.format("%s|%s|%s|%s|%s|%s|%s|%tQ|%tQ", "2.0.61.0", a, optString, productType, sku, receiptId, s, jsonObject, cancelDate);
                a = a.a;
                final StringBuilder sb = new StringBuilder();
                sb.append("stringToVerify/v1:\n");
                sb.append((String)jsonObject);
                sb.append("\nsignature:\n");
                sb.append(optString2);
                e.a(a, sb.toString());
                if (Kiwi.isSignedByKiwi((String)jsonObject, optString2)) {
                    return a3;
                }
                MetricsHelper.submitReceiptVerificationFailureMetrics(s, (String)jsonObject, optString2);
                throw new d(s, (String)jsonObject, optString2);
            }
            catch (JSONException ex) {
                throw new com.amazon.device.iap.internal.b.a(s, jsonObject.toString(), (Throwable)ex);
            }
        }
        final String a4 = a.a;
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("a signature was not found in the receipt for request ID ");
        sb2.append(s);
        e.b(a4, sb2.toString());
        MetricsHelper.submitReceiptVerificationFailureMetrics(s, "NO Signature found", optString2);
        throw new d(s, null, optString2);
    }
    
    private static Receipt d(JSONObject format, String a, final String s) throws com.amazon.device.iap.internal.b.a, d {
        final String optString = format.optString("DeviceId");
        final String optString2 = format.optString("signature");
        final boolean a2 = com.amazon.device.iap.internal.util.d.a(optString2);
        Date b = null;
        if (!a2) {
            try {
                final String string = format.getString("receiptId");
                final String string2 = format.getString("sku");
                final ProductType value = ProductType.valueOf(format.getString("itemType").toUpperCase());
                final String optString3 = format.optString("purchaseDate");
                Date b2;
                if (a(optString3)) {
                    b2 = null;
                }
                else {
                    b2 = b(optString3);
                }
                final String optString4 = format.optString("cancelDate");
                if (!a(optString4)) {
                    b = b(optString4);
                }
                final Receipt build = new ReceiptBuilder().setReceiptId(string).setSku(string2).setProductType(value).setPurchaseDate(b2).setCancelDate(b).build();
                format = (JSONObject)String.format("%s|%s|%s|%s|%s|%tQ|%tQ", a, optString, build.getProductType(), build.getSku(), build.getReceiptId(), build.getPurchaseDate(), build.getCancelDate());
                a = a.a;
                final StringBuilder sb = new StringBuilder();
                sb.append("stringToVerify/v2:\n");
                sb.append((String)format);
                sb.append("\nsignature:\n");
                sb.append(optString2);
                e.a(a, sb.toString());
                if (Kiwi.isSignedByKiwi((String)format, optString2)) {
                    return build;
                }
                MetricsHelper.submitReceiptVerificationFailureMetrics(s, (String)format, optString2);
                throw new d(s, (String)format, optString2);
            }
            catch (JSONException ex) {
                throw new com.amazon.device.iap.internal.b.a(s, format.toString(), (Throwable)ex);
            }
        }
        final String a3 = a.a;
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("a signature was not found in the receipt for request ID ");
        sb2.append(s);
        e.b(a3, sb2.toString());
        MetricsHelper.submitReceiptVerificationFailureMetrics(s, "NO Signature found", optString2);
        throw new d(s, null, optString2);
    }
}
