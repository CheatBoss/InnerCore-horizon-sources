package com.amazon.device.iap.internal.c;

import org.json.*;

class d
{
    private final String a;
    private final String b;
    private final long c;
    private final String d;
    
    public d(final String a, final String b, final String d, final long c) {
        this.a = a;
        this.b = b;
        this.d = d;
        this.c = c;
    }
    
    public static d a(final String s) throws e {
        try {
            final JSONObject jsonObject = new JSONObject(s);
            return new d(jsonObject.getString("KEY_USER_ID"), jsonObject.getString("KEY_RECEIPT_STRING"), jsonObject.getString("KEY_REQUEST_ID"), jsonObject.getLong("KEY_TIMESTAMP"));
        }
        finally {
            final StringBuilder sb = new StringBuilder();
            sb.append("Input invalid for PendingReceipt Object:");
            sb.append(s);
            final Throwable t;
            throw new e(sb.toString(), t);
        }
    }
    
    public String a() {
        return this.d;
    }
    
    public String b() {
        return this.b;
    }
    
    public long c() {
        return this.c;
    }
    
    public String d() throws JSONException {
        final JSONObject jsonObject = new JSONObject();
        jsonObject.put("KEY_USER_ID", (Object)this.a);
        jsonObject.put("KEY_RECEIPT_STRING", (Object)this.b);
        jsonObject.put("KEY_REQUEST_ID", (Object)this.d);
        jsonObject.put("KEY_TIMESTAMP", this.c);
        return jsonObject.toString();
    }
}
