package com.mojang.minecraftpe;

import java.io.*;
import com.microsoft.aad.adal.*;
import android.app.*;
import android.os.*;
import android.webkit.*;
import android.content.*;

public class ActiveDirectorySignIn implements ActivityListener
{
    private String mAccessToken;
    private AuthenticationContext mAuthenticationContext;
    private boolean mDialogOpen;
    private String mIdentityToken;
    private boolean mIsActivityListening;
    private String mLastError;
    private boolean mResultObtained;
    private String mUserId;
    
    public ActiveDirectorySignIn() {
        this.mDialogOpen = false;
        this.mResultObtained = false;
        this.mIsActivityListening = false;
        MainActivity.mInstance.addListener(this);
    }
    
    public static ActiveDirectorySignIn createActiveDirectorySignIn() {
        return new ActiveDirectorySignIn();
    }
    
    private AuthenticationCallback<AuthenticationResult> getAdalCallback() {
        return new AuthenticationCallback<AuthenticationResult>() {
            @Override
            public void onError(final Exception ex) {
                final PrintStream out = System.out;
                final StringBuilder sb = new StringBuilder();
                sb.append("ADAL sign in error: ");
                sb.append(ex.getMessage());
                out.println(sb.toString());
                ActiveDirectorySignIn.this.mResultObtained = false;
                if (!(ex instanceof AuthenticationCancelError)) {
                    ActiveDirectorySignIn.this.mLastError = ex.getMessage();
                }
                ActiveDirectorySignIn.this.mDialogOpen = false;
                ActiveDirectorySignIn.this.mUserId = "";
                ActiveDirectorySignIn.this.nativeOnDataChanged();
            }
            
            @Override
            public void onSuccess(final AuthenticationResult authenticationResult) {
                System.out.println("ADAL sign in success");
                ActiveDirectorySignIn.this.mResultObtained = true;
                ActiveDirectorySignIn.this.mAccessToken = authenticationResult.getAccessToken();
                ActiveDirectorySignIn.this.mIdentityToken = authenticationResult.getIdToken();
                ActiveDirectorySignIn.this.mLastError = "";
                ActiveDirectorySignIn.this.mDialogOpen = false;
                ActiveDirectorySignIn.this.mUserId = authenticationResult.getUserInfo().getUserId();
                ActiveDirectorySignIn.this.nativeOnDataChanged();
            }
        };
    }
    
    private native void nativeOnDataChanged();
    
    public void authenticate(final int n) {
        boolean b = false;
        this.mResultObtained = false;
        this.mDialogOpen = true;
        PromptBehavior promptBehavior;
        if (n == 0) {
            promptBehavior = PromptBehavior.Always;
        }
        else {
            promptBehavior = PromptBehavior.Auto;
        }
        if (n == 2) {
            b = true;
        }
        MainActivity.mInstance.runOnUiThread((Runnable)new Runnable() {
            @Override
            public void run() {
                ActiveDirectorySignIn.this.mAuthenticationContext = new AuthenticationContext((Context)MainActivity.mInstance, "https://login.windows.net/common", true);
                if (b) {
                    ActiveDirectorySignIn.this.mAuthenticationContext.acquireTokenSilent("https://meeservices.minecraft.net", "b36b1432-1a1c-4c82-9b76-24de1cab42f2", ActiveDirectorySignIn.this.mUserId, ActiveDirectorySignIn.this.getAdalCallback());
                    return;
                }
                ActiveDirectorySignIn.this.mAuthenticationContext.acquireToken((Activity)MainActivity.mInstance, "https://meeservices.minecraft.net", "b36b1432-1a1c-4c82-9b76-24de1cab42f2", "urn:ietf:wg:oauth:2.0:oob", "", promptBehavior, "", ActiveDirectorySignIn.this.getAdalCallback());
            }
        });
    }
    
    public void clearCookies() {
        final CookieManager instance = CookieManager.getInstance();
        if (Build$VERSION.SDK_INT >= 21) {
            instance.removeAllCookies((ValueCallback)null);
            instance.flush();
            return;
        }
        final CookieSyncManager instance2 = CookieSyncManager.createInstance((Context)MainActivity.mInstance);
        instance2.startSync();
        instance.removeAllCookie();
        instance2.stopSync();
        instance2.sync();
    }
    
    public String getAccessToken() {
        return this.mAccessToken;
    }
    
    public boolean getDialogOpen() {
        return this.mDialogOpen;
    }
    
    public String getIdentityToken() {
        return this.mIdentityToken;
    }
    
    public String getLastError() {
        return this.mLastError;
    }
    
    public boolean getResultObtained() {
        return this.mResultObtained;
    }
    
    @Override
    public void onActivityResult(final int n, final int n2, final Intent intent) {
        final AuthenticationContext mAuthenticationContext = this.mAuthenticationContext;
        if (mAuthenticationContext != null) {
            mAuthenticationContext.onActivityResult(n, n2, intent);
        }
    }
    
    @Override
    public void onDestroy() {
    }
    
    @Override
    public void onResume() {
    }
    
    @Override
    public void onStop() {
    }
}
