package com.mojang.minecraftpe.Webview;

import java.io.*;
import android.graphics.*;
import android.webkit.*;
import android.net.*;
import com.mojang.minecraftpe.*;

class MinecraftWebViewClient extends WebViewClient
{
    private MinecraftWebview mView;
    
    public MinecraftWebViewClient(final MinecraftWebview mView) {
        this.mView = mView;
    }
    
    public void onPageFinished(final WebView webView, final String s) {
        final PrintStream out = System.out;
        final StringBuilder sb = new StringBuilder();
        sb.append("Finished loading ");
        sb.append(s);
        out.println(sb.toString());
        super.onPageFinished(webView, s);
    }
    
    public void onPageStarted(final WebView webView, final String s, final Bitmap bitmap) {
        final PrintStream out = System.out;
        final StringBuilder sb = new StringBuilder();
        sb.append("Started loading ");
        sb.append(s);
        out.println(sb.toString());
        super.onPageStarted(webView, s, bitmap);
    }
    
    public void onReceivedError(final WebView webView, final WebResourceRequest webResourceRequest, final WebResourceError webResourceError) {
        System.out.println(String.format("Error %s loading url %s", webResourceError.getDescription().toString(), webResourceRequest.getUrl().toString()));
        this.mView.nativeOnWebError(webResourceError.getErrorCode(), webResourceError.getDescription().toString());
        super.onReceivedError(webView, webResourceRequest, webResourceError);
    }
    
    public boolean shouldOverrideUrlLoading(final WebView webView, final WebResourceRequest webResourceRequest) {
        final Uri url = webResourceRequest.getUrl();
        final Uri parse = Uri.parse(webView.getUrl());
        if (webResourceRequest.hasGesture() && !parse.getHost().equals(url.getHost())) {
            MainActivity.mInstance.launchUri(webResourceRequest.getUrl().toString());
            return true;
        }
        return super.shouldOverrideUrlLoading(webView, webResourceRequest);
    }
}
