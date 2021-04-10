package com.appsflyer.internal;

import android.net.*;
import android.database.*;
import android.content.*;

public final class ac extends ContentFetcher<String>
{
    public ac(final Context context) {
        super(context, "com.facebook.katana.provider.AttributionIdProvider", "e3f9e1e0cf99d0e56a055ba65e241b3399f7cea524326b0cdd6ec1327ed0fdc1", 500L);
    }
    
    private String \u0131() {
        Cursor cursor = null;
        try {
            final ContentResolver contentResolver = super.context.getContentResolver();
            final StringBuilder sb = new StringBuilder("content://");
            sb.append(super.authority);
            final Cursor query = contentResolver.query(Uri.parse(sb.toString()), new String[] { "aid" }, (String)null, (String[])null, (String)null);
            if (query != null) {
                try {
                    if (query.moveToFirst()) {
                        final String string = query.getString(query.getColumnIndex("aid"));
                        if (query != null) {
                            query.close();
                        }
                        return string;
                    }
                }
                finally {
                    cursor = query;
                }
            }
            if (query != null) {
                query.close();
            }
            return null;
        }
        finally {}
        if (cursor != null) {
            cursor.close();
        }
    }
    
    public final String \u0269() {
        this.start();
        return super.get();
    }
}
