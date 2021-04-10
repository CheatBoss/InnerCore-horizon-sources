package com.mojang.minecraftpe.Webview;

import android.webkit.*;
import com.mojang.minecraftpe.*;

class MinecraftChromeClient extends WebChromeClient
{
    private MinecraftWebview mView;
    
    public MinecraftChromeClient(final MinecraftWebview mView) {
        this.mView = mView;
    }
    
    public void onProgressChanged(final WebView webView, final int n) {
        super.onProgressChanged(webView, n);
        MainActivity.mInstance.runOnUiThread((Runnable)new Runnable() {
            @Override
            public void run() {
                MinecraftChromeClient.this.mView._injectApi();
            }
        });
    }
}
