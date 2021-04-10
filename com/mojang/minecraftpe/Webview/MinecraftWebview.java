package com.mojang.minecraftpe.Webview;

import com.mojang.minecraftpe.*;
import android.content.*;
import android.view.*;
import java.io.*;
import android.webkit.*;

public class MinecraftWebview
{
    private MainActivity mActivity;
    private WebView mWebView;
    private PopupView mWebViewPopup;
    
    public MinecraftWebview() {
        (this.mActivity = MainActivity.mInstance).runOnUiThread((Runnable)new Runnable() {
            @Override
            public void run() {
                MinecraftWebview.this._createWebView();
            }
        });
    }
    
    private void _createWebView() {
        if (!MainActivity.mInstance.isPublishBuild()) {
            WebView.setWebContentsDebuggingEnabled(true);
        }
        (this.mWebView = new WebView(this.mActivity) {
            public boolean performClick() {
                this.requestFocus();
                return super.performClick();
            }
        }).setLayoutParams(new ViewGroup$LayoutParams(-1, -1));
        this.mWebView.setWebViewClient((WebViewClient)new MinecraftWebViewClient(this));
        this.mWebView.setWebChromeClient((WebChromeClient)new MinecraftChromeClient(this));
        this.mWebView.addJavascriptInterface((Object)new WebviewHostInterface(this), "codeBuilderHostInterface");
        final WebSettings settings = this.mWebView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setRenderPriority(WebSettings$RenderPriority.HIGH);
        this.mWebViewPopup = new PopupView((Context)this.mActivity);
        final View rootView = this.mActivity.findViewById(16908290).getRootView();
        this.mWebViewPopup.setContentView((View)this.mWebView);
        this.mWebViewPopup.setParentView(rootView);
    }
    
    private String _readResource(final int n) {
        final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        final InputStream openRawResource = this.mActivity.getResources().openRawResource(n);
        try {
            final byte[] array = new byte[256];
            while (true) {
                final int read = openRawResource.read(array);
                if (read <= 0) {
                    break;
                }
                byteArrayOutputStream.write(array, 0, read);
            }
            openRawResource.close();
            return byteArrayOutputStream.toString();
        }
        catch (IOException ex) {
            final PrintStream out = System.out;
            final StringBuilder sb = new StringBuilder();
            sb.append("Failed to read resource ");
            sb.append(n);
            sb.append(" with error ");
            sb.append(ex.toString());
            out.println(sb.toString());
            return null;
        }
    }
    
    public void _injectApi() {
        final String readResource = this._readResource(this.mActivity.getResources().getIdentifier("code_builder_hosted_editor", "raw", this.mActivity.getPackageName()));
        if (readResource != null) {
            this.mWebView.evaluateJavascript(readResource, (ValueCallback)null);
            return;
        }
        this.nativeOnWebError(0, "Unable to inject api");
    }
    
    public native void nativeDismiss();
    
    public native void nativeOnWebError(final int p0, final String p1);
    
    public native void nativeSendToHost(final String p0);
    
    public void sendToWebView(final String s) {
        this.mActivity.runOnUiThread((Runnable)new Runnable() {
            @Override
            public void run() {
                MinecraftWebview.this.mWebView.evaluateJavascript(s, (ValueCallback)null);
            }
        });
    }
    
    public void setPropagatedAlpha(final float n) {
        this.setShowView(n == 1.0);
    }
    
    public void setRect(final float n, final float n2, final float n3, final float n4) {
        this.mActivity.runOnUiThread((Runnable)new Runnable() {
            final /* synthetic */ int val$x0 = (int)n;
            final /* synthetic */ int val$x1 = (int)n2;
            final /* synthetic */ int val$y0 = (int)n3;
            final /* synthetic */ int val$y1 = (int)n4;
            
            @Override
            public void run() {
                MinecraftWebview.this.mWebViewPopup.setRect(this.val$x0, this.val$x1, this.val$y0, this.val$y1);
                MinecraftWebview.this.mWebViewPopup.update();
            }
        });
    }
    
    public void setShowView(final boolean b) {
        this.mActivity.runOnUiThread((Runnable)new Runnable() {
            @Override
            public void run() {
                MinecraftWebview.this.mWebViewPopup.setVisible(b);
            }
        });
    }
    
    public void setUrl(final String s) {
        this.mActivity.runOnUiThread((Runnable)new Runnable() {
            @Override
            public void run() {
                MinecraftWebview.this.mWebView.loadUrl(s);
            }
        });
    }
    
    public void teardown() {
        this.mWebViewPopup.dismiss();
        this.mWebViewPopup = null;
        this.mWebView = null;
        this.mActivity = null;
    }
}
