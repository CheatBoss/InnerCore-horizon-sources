package com.misc.xbox;

import com.android.tools.r8.annotations.*;
import android.app.*;
import android.webkit.*;
import com.zhekasmirnov.horizon.*;
import com.zhekasmirnov.innercore.utils.*;
import android.content.*;
import android.graphics.drawable.*;
import android.graphics.*;
import android.widget.*;
import android.view.*;
import com.zhekasmirnov.apparatus.adapter.innercore.*;

@SynthesizedClassMap({ -$$Lambda$XboxLoginWindow$bDZ-lusbvUGMAycldJhUhYUDkeM.class, -$$Lambda$XboxLoginWindow$IrhiXm3TAO-zY47c8r34eadQ7_A.class, -$$Lambda$XboxLoginWindow$jYk490PxlXdvuHYdPqEnFLm4Vno.class, -$$Lambda$XboxLoginWindow$VYhNvGK-2-_DdUOcTrTDZGkEaSo.class, -$$Lambda$XboxLoginWindow$-drnLml4EkCSlKkL2Ew1JwlfP0Q.class, -$$Lambda$XboxLoginWindow$CGGW6d91vcZnI-szDi_MFcflpKE.class, -$$Lambda$XboxLoginWindow$RMp5nl-xGjPhV4QdpHhlQ54l38o.class, -$$Lambda$XboxLoginWindow$j-tTQh_cJOZWoR_iZjf0hhYV0o0.class })
public class XboxLoginWindow
{
    private static XboxLoginWindow singleton;
    private final Activity context;
    private DetachListener currentDetachListener;
    private WebView webView;
    private PopupWindow window;
    
    public XboxLoginWindow(final Activity context) {
        this.context = context;
    }
    
    public static XboxLoginWindow getSingleton() {
        if (XboxLoginWindow.singleton == null) {
            final Activity topRunningActivity = HorizonApplication.getTopRunningActivity();
            if (topRunningActivity == null) {
                throw new IllegalStateException("cannot create xbox login window singleton, context is null");
            }
            XboxLoginWindow.singleton = new XboxLoginWindow(topRunningActivity);
        }
        return XboxLoginWindow.singleton;
    }
    
    public void assureAndLoadUrl(final String s, final AttachListener attachListener, final DetachListener detachListener) {
        this.attach((AttachListener)new -$$Lambda$XboxLoginWindow$bDZ-lusbvUGMAycldJhUhYUDkeM(attachListener, s), detachListener);
    }
    
    public void attach(final AttachListener attachListener, final DetachListener detachListener) {
        this.context.runOnUiThread((Runnable)new -$$Lambda$XboxLoginWindow$CGGW6d91vcZnI-szDi_MFcflpKE(this, detachListener, attachListener));
    }
    
    public void detach() {
        this.context.runOnUiThread((Runnable)new -$$Lambda$XboxLoginWindow$j-tTQh_cJOZWoR_iZjf0hhYV0o0(this));
    }
    
    public void loadUrl(final String s) {
        this.context.runOnUiThread((Runnable)new -$$Lambda$XboxLoginWindow$-drnLml4EkCSlKkL2Ew1JwlfP0Q(this, s));
    }
    
    public interface AttachListener
    {
        void onAttached(final WebView p0);
    }
    
    public interface DetachListener
    {
        void onDetached(final boolean p0);
    }
}
