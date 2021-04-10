package com.amazon.device.iap.internal.b;

import com.amazon.android.framework.task.command.*;
import com.amazon.android.licensing.*;
import java.util.*;
import com.amazon.android.*;
import com.amazon.android.framework.prompt.*;
import com.amazon.android.framework.exception.*;
import com.amazon.venezia.command.*;
import android.os.*;
import com.amazon.device.iap.internal.util.*;

public abstract class i extends AbstractCommandTask
{
    private static final String a;
    private final e b;
    private final String c;
    private final String d;
    private final String e;
    private final Map<String, Object> f;
    private final LicenseFailurePromptContentMapper g;
    private boolean h;
    private i i;
    private i j;
    private boolean k;
    
    static {
        a = i.class.getSimpleName();
    }
    
    public i(final e b, final String d, final String e) {
        this.g = new LicenseFailurePromptContentMapper();
        this.k = false;
        this.b = b;
        this.c = b.c().toString();
        this.d = d;
        this.e = e;
        (this.f = new HashMap<String, Object>()).put("requestId", this.c);
        this.f.put("sdkVersion", "2.0.61.0");
        this.h = true;
        this.i = null;
        this.j = null;
    }
    
    private void a(final PromptContent promptContent) {
        if (promptContent == null) {
            return;
        }
        Kiwi.getPromptManager().present((Prompt)new b(promptContent));
    }
    
    public i a(final boolean k) {
        this.k = k;
        return this;
    }
    
    public void a(final i i) {
        this.i = i;
    }
    
    protected void a(final String s, final Object o) {
        this.f.put(s, o);
    }
    
    protected abstract boolean a(final SuccessResult p0) throws Exception;
    
    public void a_() {
        Kiwi.addCommandToCommandTaskPipeline((AbstractCommandTask)this);
    }
    
    protected e b() {
        return this.b;
    }
    
    public void b(final i j) {
        this.j = j;
    }
    
    protected void b(final boolean h) {
        this.h = h;
    }
    
    protected String c() {
        return this.c;
    }
    
    protected Map<String, Object> getCommandData() {
        return this.f;
    }
    
    protected String getCommandName() {
        return this.d;
    }
    
    protected String getCommandVersion() {
        return this.e;
    }
    
    protected boolean isExecutionNeeded() {
        return true;
    }
    
    protected final void onException(final KiwiException ex) {
        final String a = com.amazon.device.iap.internal.b.i.a;
        final StringBuilder sb = new StringBuilder();
        sb.append("onException: exception = ");
        sb.append(ex.getMessage());
        com.amazon.device.iap.internal.util.e.a(a, sb.toString());
        if ("UNHANDLED_EXCEPTION".equals(ex.getType()) && "2.0".equals(this.e)) {
            final i j = this.j;
            if (j != null) {
                j.a(this.k);
                this.j.a_();
                return;
            }
        }
        if (this.h) {
            this.a(this.g.map(ex));
        }
        if (!this.k) {
            this.b.b();
        }
    }
    
    protected final void onFailure(final FailureResult failureResult) throws RemoteException, KiwiException {
        final String a = com.amazon.device.iap.internal.b.i.a;
        final StringBuilder sb = new StringBuilder();
        sb.append("onFailure: result = ");
        sb.append(failureResult);
        com.amazon.device.iap.internal.util.e.a(a, sb.toString());
        boolean b = false;
        Label_0078: {
            if (failureResult != null) {
                final String s = failureResult.getExtensionData().get("maxVersion");
                if (s != null && s.equalsIgnoreCase("1.0")) {
                    b = true;
                    break Label_0078;
                }
            }
            b = false;
        }
        if (b) {
            final i j = this.j;
            if (j != null) {
                j.a(this.k);
                this.j.a_();
                return;
            }
        }
        if (this.h) {
            this.a(new PromptContent(failureResult.getDisplayableName(), failureResult.getDisplayableMessage(), failureResult.getButtonLabel(), failureResult.show()));
        }
        if (!this.k) {
            this.b.b();
        }
    }
    
    protected final void onSuccess(final SuccessResult successResult) throws RemoteException {
        final String s = successResult.getData().get("errorMessage");
        final String a = com.amazon.device.iap.internal.b.i.a;
        final StringBuilder sb = new StringBuilder();
        sb.append("onSuccess: result = ");
        sb.append(successResult);
        sb.append(", errorMessage: ");
        sb.append(s);
        com.amazon.device.iap.internal.util.e.a(a, sb.toString());
        if (com.amazon.device.iap.internal.util.d.a(s)) {
            boolean a2;
            try {
                a2 = this.a(successResult);
            }
            catch (Exception ex) {
                final String a3 = com.amazon.device.iap.internal.b.i.a;
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Error calling onResult: ");
                sb2.append(ex);
                com.amazon.device.iap.internal.util.e.b(a3, sb2.toString());
                a2 = false;
            }
            if (a2) {
                final i i = this.i;
                if (i != null) {
                    i.a_();
                    return;
                }
            }
            if (!this.k) {
                if (a2) {
                    this.b.a();
                    return;
                }
                this.b.b();
            }
        }
        else if (!this.k) {
            this.b.b();
        }
    }
}
