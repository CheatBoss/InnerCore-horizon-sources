package com.amazon.device.iap.internal.b.b;

import com.amazon.android.framework.resource.*;
import com.amazon.android.framework.context.*;
import com.amazon.device.iap.internal.b.*;
import com.amazon.venezia.command.*;
import com.amazon.android.framework.task.pipeline.*;
import android.content.*;
import com.amazon.android.framework.task.*;
import com.amazon.device.iap.internal.util.*;
import android.app.*;
import java.util.*;
import android.os.*;
import com.amazon.android.framework.exception.*;

abstract class a extends i
{
    private static final String d;
    @Resource
    protected TaskManager a;
    @Resource
    protected ContextManager b;
    protected final String c;
    
    static {
        d = a.class.getSimpleName();
    }
    
    a(final e e, final String s, final String c) {
        super(e, "purchase_item", s);
        this.a("sku", this.c = c);
    }
    
    @Override
    protected boolean a(final SuccessResult successResult) throws RemoteException, KiwiException {
        final Map data = successResult.getData();
        final String d = com.amazon.device.iap.internal.b.b.a.d;
        final StringBuilder sb = new StringBuilder();
        sb.append("data: ");
        sb.append(data);
        com.amazon.device.iap.internal.util.e.a(d, sb.toString());
        if (!data.containsKey("purchaseItemIntent")) {
            com.amazon.device.iap.internal.util.e.b(com.amazon.device.iap.internal.b.b.a.d, "did not find intent");
            return false;
        }
        com.amazon.device.iap.internal.util.e.a(com.amazon.device.iap.internal.b.b.a.d, "found intent");
        this.a.enqueueAtFront(TaskPipelineId.FOREGROUND, (Task)new Task() {
            final /* synthetic */ Intent a = data.remove("purchaseItemIntent");
            
            public void execute() {
                try {
                    Activity activity;
                    if ((activity = com.amazon.device.iap.internal.b.b.a.this.b.getVisible()) == null) {
                        activity = com.amazon.device.iap.internal.b.b.a.this.b.getRoot();
                    }
                    final String a = com.amazon.device.iap.internal.b.b.a.d;
                    final StringBuilder sb = new StringBuilder();
                    sb.append("About to fire intent with activity ");
                    sb.append(activity);
                    com.amazon.device.iap.internal.util.e.a(a, sb.toString());
                    activity.startActivity(this.a);
                }
                catch (Exception ex) {
                    final String a2 = com.amazon.device.iap.internal.b.i.this.c();
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append(com.amazon.device.iap.internal.b.b.a.d);
                    sb2.append(".onResult().execute()");
                    MetricsHelper.submitExceptionMetrics(a2, sb2.toString(), ex);
                    final String a3 = com.amazon.device.iap.internal.b.b.a.d;
                    final StringBuilder sb3 = new StringBuilder();
                    sb3.append("Exception when attempting to fire intent: ");
                    sb3.append(ex);
                    com.amazon.device.iap.internal.util.e.b(a3, sb3.toString());
                }
            }
        });
        return true;
    }
}
