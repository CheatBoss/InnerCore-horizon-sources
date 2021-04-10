package com.appsflyer.internal;

import android.content.*;

public final class b
{
    private IntentFilter \u0131;
    
    b() {
        this.\u0131 = new IntentFilter("android.intent.action.BATTERY_CHANGED");
    }
    
    public final d \u0399(final Context context) {
        String s3;
        float n4;
        while (true) {
            final String s = null;
            final String s2 = null;
            final float n = 0.0f;
            s3 = s2;
            while (true) {
                Label_0199: {
                    try {
                        final Intent registerReceiver = context.registerReceiver((BroadcastReceiver)null, this.\u0131);
                        s3 = s;
                        if (registerReceiver != null) {
                            s3 = s2;
                            if (2 != registerReceiver.getIntExtra("status", -1)) {
                                break Label_0199;
                            }
                            final int n2 = 1;
                            String s4;
                            if (n2 != 0) {
                                s3 = s2;
                                final int intExtra = registerReceiver.getIntExtra("plugged", -1);
                                if (intExtra != 1) {
                                    if (intExtra != 2) {
                                        if (intExtra != 4) {
                                            s4 = "other";
                                        }
                                        else {
                                            s4 = "wireless";
                                        }
                                    }
                                    else {
                                        s4 = "usb";
                                    }
                                }
                                else {
                                    s4 = "ac";
                                }
                            }
                            else {
                                s4 = "no";
                            }
                            s3 = s4;
                            final int intExtra2 = registerReceiver.getIntExtra("level", -1);
                            s3 = s4;
                            final int intExtra3 = registerReceiver.getIntExtra("scale", -1);
                            s3 = s4;
                            if (-1 != intExtra2) {
                                s3 = s4;
                                if (-1 != intExtra3) {
                                    final float n3 = intExtra2 * 100.0f / intExtra3;
                                    s3 = s4;
                                }
                            }
                        }
                    }
                    finally {
                        n4 = n;
                    }
                    break;
                }
                final int n2 = 0;
                continue;
            }
        }
        return new d(n4, s3);
    }
    
    public static final class d
    {
        public final float \u0131;
        public final String \u0399;
        
        d(final float \u0131, final String \u03b9) {
            this.\u0131 = \u0131;
            this.\u0399 = \u03b9;
        }
    }
    
    public static final class e
    {
        public static final b \u0131;
        
        static {
            \u0131 = new b();
        }
    }
}
