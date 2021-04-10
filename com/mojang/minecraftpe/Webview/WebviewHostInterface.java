package com.mojang.minecraftpe.Webview;

import android.webkit.*;
import java.io.*;

class WebviewHostInterface
{
    private MinecraftWebview mView;
    
    public WebviewHostInterface(final MinecraftWebview mView) {
        this.mView = mView;
    }
    
    @JavascriptInterface
    public void dismiss() {
        System.out.println("dismiss");
        this.mView.nativeDismiss();
    }
    
    @JavascriptInterface
    public void sendToHost(final String s) {
        final PrintStream out = System.out;
        final StringBuilder sb = new StringBuilder();
        sb.append("SendToHost ");
        sb.append(s);
        out.println(sb.toString());
        this.mView.nativeSendToHost(s);
    }
}
