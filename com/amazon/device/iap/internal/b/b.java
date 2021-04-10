package com.amazon.device.iap.internal.b;

import com.amazon.android.framework.context.*;
import com.amazon.android.framework.resource.*;
import com.amazon.android.framework.prompt.*;
import com.amazon.device.iap.internal.util.*;
import android.net.*;
import android.content.*;
import android.app.*;

public class b extends SimplePrompt
{
    private static final String a;
    @Resource
    private ContextManager b;
    private final PromptContent c;
    
    static {
        a = b.class.getSimpleName();
    }
    
    public b(final PromptContent c) {
        super(c);
        this.c = c;
    }
    
    protected void doAction() {
        e.a(com.amazon.device.iap.internal.b.b.a, "doAction");
        if (!"Amazon Appstore required".equalsIgnoreCase(this.c.getTitle())) {
            if (!"Amazon Appstore Update Required".equalsIgnoreCase(this.c.getTitle())) {
                return;
            }
        }
        try {
            Activity activity;
            if ((activity = this.b.getVisible()) == null) {
                activity = this.b.getRoot();
            }
            activity.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("http://www.amazon.com/gp/mas/get-appstore/android/ref=mas_mx_mba_iap_dl")));
        }
        catch (Exception ex) {
            final String a = com.amazon.device.iap.internal.b.b.a;
            final StringBuilder sb = new StringBuilder();
            sb.append("Exception in PurchaseItemCommandTask.OnSuccess: ");
            sb.append(ex);
            e.b(a, sb.toString());
        }
    }
    
    protected long getExpirationDurationInSeconds() {
        return 31536000L;
    }
    
    public String toString() {
        return com.amazon.device.iap.internal.b.b.a;
    }
}
