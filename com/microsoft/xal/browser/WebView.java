package com.microsoft.xal.browser;

import com.android.tools.r8.annotations.*;
import android.app.*;
import com.microsoft.xal.logging.*;
import java.util.concurrent.locks.*;
import com.zhekasmirnov.horizon.*;
import android.content.pm.*;
import java.security.*;
import android.util.*;
import android.webkit.*;
import com.misc.xbox.*;
import com.zhekasmirnov.innercore.api.log.*;
import java.util.*;
import android.content.*;
import java.io.*;
import android.support.customtabs.*;
import android.net.*;
import com.mojang.minecraftpe.*;
import android.os.*;

@SynthesizedClassMap({ -$$Lambda$WebView$JW3uSwDp0VJ6c4_hletzSZW2qCQ.class, -$$Lambda$WebView$fsyuoT79GYMPgo6wHPePjTye1io.class })
public class WebView
{
    public static final String CANCEL_DELAY = "CANCEL_DELAY";
    public static final String END_URL = "END_URL";
    public static final String IN_PROC_BROWSER = "IN_PROC_BROWSER";
    public static final String OPERATION_ID = "OPERATION_ID";
    public static final int RESULT_FAILED = 8052;
    public static final String SHOW_TYPE = "SHOW_TYPE";
    public static final String START_URL = "START_URL";
    public static final int WEB_KIT_WEB_VIEW_REQUEST = 8053;
    private static final Map<String, String> customTabsAllowedBrowsers;
    private android.webkit.WebView curWebView;
    private Intent inputIntent;
    private Activity mActivity;
    private String m_browserInfo;
    private long m_cancelDelay;
    private boolean m_cancelOperationOnResume;
    private final Lock m_lock;
    private final XalLogger m_logger;
    private long m_operationId;
    private boolean m_sharedBrowserUsed;
    
    static {
        ((HashMap<String, String>)(customTabsAllowedBrowsers = new HashMap<String, String>())).put("com.android.chrome", "OJGKRT0HGZNU+LGa8F7GViztV4g=");
        WebView.customTabsAllowedBrowsers.put("org.mozilla.firefox", "kg9Idqale0pqL0zK9l99Kc4m/yw=");
        WebView.customTabsAllowedBrowsers.put("com.microsoft.emmx", "P2QOJ59jvOpxCCrn6MfvotoBTK0=");
        WebView.customTabsAllowedBrowsers.put("com.sec.android.app.sbrowser", "nKUXDzgZGd/gRG/NqxixmhQ7MWM=");
    }
    
    public WebView() {
        this.m_browserInfo = null;
        this.m_cancelDelay = 500L;
        this.m_cancelOperationOnResume = true;
        this.m_lock = new ReentrantLock();
        this.m_logger = new XalLogger("WebView");
        this.m_operationId = 0L;
        this.m_sharedBrowserUsed = false;
        this.curWebView = null;
    }
    
    private boolean browserAllowedForCustomTabs(final String s) {
        final String s2 = WebView.customTabsAllowedBrowsers.get(s);
        if (s2 == null) {
            return false;
        }
        try {
            final PackageInfo packageInfo = HorizonApplication.getInstance().getPackageManager().getPackageInfo(s, 64);
            if (packageInfo == null) {
                final XalLogger logger = this.m_logger;
                final StringBuilder sb = new StringBuilder();
                sb.append("No package info found for package: ");
                sb.append(s);
                logger.Important(sb.toString());
                return false;
            }
            final Signature[] signatures = packageInfo.signatures;
            final int length = signatures.length;
            int i = 0;
            while (i < length) {
                try {
                    if (hashFromSignature(signatures[i]).equals(s2)) {
                        return true;
                    }
                    ++i;
                    continue;
                }
                catch (NoSuchAlgorithmException ex) {
                    final XalLogger logger2 = this.m_logger;
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("browserAllowedForCustomTabs() Error in hashFromSignature(): ");
                    sb2.append(ex);
                    logger2.Error(sb2.toString());
                    return false;
                }
            }
            return false;
        }
        catch (PackageManager$NameNotFoundException ex2) {
            final XalLogger logger3 = this.m_logger;
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("browserAllowedForCustomTabs() Error in getPackageInfo(): ");
            sb3.append(ex2);
            logger3.Error(sb3.toString());
            return false;
        }
    }
    
    private boolean browserSupportsCustomTabs(final String s) {
        final Iterator<ResolveInfo> iterator = HorizonApplication.getInstance().getPackageManager().queryIntentServices(new Intent("android.support.customtabs.action.CustomTabsService"), 0).iterator();
        while (iterator.hasNext()) {
            if (iterator.next().serviceInfo.packageName.equals(s)) {
                return true;
            }
        }
        return false;
    }
    
    private void finishOperation(final WebResult webResult, final String s) {
        this.m_lock.lock();
        final long operationId = this.m_operationId;
        this.m_operationId = 0L;
        this.m_cancelOperationOnResume = false;
        this.m_lock.unlock();
        this.finish();
        if (operationId == 0L) {
            this.m_logger.Error("finishOperation() called on completed web view.");
            this.m_logger.Flush();
            return;
        }
        this.m_logger.Flush();
        final int ordinal = webResult.ordinal();
        if (ordinal != 1) {
            if (ordinal == 2) {
                urlOperationCanceled(operationId, this.m_sharedBrowserUsed, this.m_browserInfo);
                return;
            }
            if (ordinal == 3) {
                urlOperationFailed(operationId, this.m_sharedBrowserUsed, this.m_browserInfo);
            }
        }
        else {
            urlOperationSucceeded(operationId, s, this.m_sharedBrowserUsed, this.m_browserInfo);
        }
    }
    
    private static String hashFromSignature(final Signature signature) throws NoSuchAlgorithmException {
        final MessageDigest instance = MessageDigest.getInstance("SHA");
        instance.update(signature.toByteArray());
        return Base64.encodeToString(instance.digest(), 2);
    }
    
    private void loadWebViewUrl(final String s) {
        XboxLoginWindow.getSingleton().assureAndLoadUrl(s, (XboxLoginWindow.AttachListener)new -$$Lambda$WebView$JW3uSwDp0VJ6c4_hletzSZW2qCQ(this), (XboxLoginWindow.DetachListener)new -$$Lambda$WebView$fsyuoT79GYMPgo6wHPePjTye1io(this));
    }
    
    private void setBrowserInfo(final String s, final int n, final String s2) {
        this.m_browserInfo = String.format(Locale.US, "%s::%d::%s", s, n, s2);
        final XalLogger logger = this.m_logger;
        final StringBuilder sb = new StringBuilder();
        sb.append("setBrowserInfo() Set browser info: ");
        sb.append(this.m_browserInfo);
        logger.Important(sb.toString());
    }
    
    public static void showUrl(final long n, final Context t, final String s, final String s2, final int n2, final boolean b, final long n3) {
        final XalLogger xalLogger = new XalLogger("WebView.showUrl()");
        Label_0267: {
            try {
                xalLogger.Important("JNI call received.");
                if (!s.isEmpty() && !s2.isEmpty()) {
                    try {
                        final ShowUrlType fromInt = ShowUrlType.fromInt(n2);
                        if (fromInt == null) {
                            try {
                                final StringBuilder sb = new StringBuilder();
                                sb.append("Unrecognized show type received: ");
                                sb.append(n2);
                                xalLogger.Error(sb.toString());
                                urlOperationFailed(n, false, null);
                                xalLogger.close();
                                return;
                            }
                            catch (Throwable t) {
                                break Label_0267;
                            }
                        }
                        try {
                            final Intent inputIntent = new Intent((Context)t, (Class)WebView.class);
                            final Bundle bundle = new Bundle();
                            bundle.putLong("OPERATION_ID", n);
                            bundle.putString("START_URL", s);
                            bundle.putString("END_URL", s2);
                            bundle.putSerializable("SHOW_TYPE", (Serializable)fromInt);
                            bundle.putBoolean("IN_PROC_BROWSER", b);
                            bundle.putLong("CANCEL_DELAY", n3);
                            inputIntent.putExtras(bundle);
                            inputIntent.setFlags(268435456);
                            final WebView webView = new WebView();
                            webView.setActivity((Activity)t);
                            webView.setInputIntent(inputIntent);
                            webView.onCreate(new Bundle());
                            xalLogger.close();
                            return;
                        }
                        catch (Throwable t) {}
                    }
                    catch (Throwable t) {
                        break Label_0267;
                    }
                }
                try {
                    xalLogger.Error("Received invalid start or end URL.");
                    urlOperationFailed(n, false, null);
                    xalLogger.close();
                    return;
                }
                catch (Throwable t) {}
            }
            catch (Throwable t2) {}
            try {
                throw new RuntimeException(t);
            }
            finally {
                try {
                    xalLogger.close();
                }
                catch (Throwable t3) {}
            }
        }
    }
    
    private void startCustomTabsInBrowser(final String package1, final String s, final String s2, final ShowUrlType showUrlType) {
        if (showUrlType == ShowUrlType.CookieRemovalSkipIfSharedCredentials) {
            this.finishOperation(WebResult.SUCCESS, s2);
            return;
        }
        this.m_cancelOperationOnResume = false;
        this.m_sharedBrowserUsed = true;
        final CustomTabsIntent.Builder builder = new CustomTabsIntent.Builder();
        builder.setShowTitle(true);
        final CustomTabsIntent build = builder.build();
        build.intent.setData(Uri.parse(s));
        build.intent.setPackage(package1);
        this.loadWebViewUrl(s);
    }
    
    private void startWebView(final String s, final String s2, final ShowUrlType showUrlType) {
        this.m_cancelOperationOnResume = false;
        this.m_sharedBrowserUsed = false;
        final Intent intent = new Intent((Context)HorizonApplication.getInstance(), (Class)WebKitWebViewController.class);
        final Bundle bundle = new Bundle();
        bundle.putString("START_URL", s);
        this.loadWebViewUrl(s);
        bundle.putString("END_URL", s2);
        bundle.putSerializable("SHOW_TYPE", (Serializable)showUrlType);
        intent.putExtras(bundle);
    }
    
    private static native void urlOperationCanceled(final long p0, final boolean p1, final String p2);
    
    private static native void urlOperationFailed(final long p0, final boolean p1, final String p2);
    
    private static native void urlOperationSucceeded(final long p0, final String p1, final boolean p2, final String p3);
    
    public void finish() {
    }
    
    public Activity getActivity() {
        if (this.mActivity != null) {
            return this.mActivity;
        }
        return (Activity)MainActivity.mInstance;
    }
    
    public Intent getIntent() {
        return this.inputIntent;
    }
    
    public void onActivityResult(final int n, final int n2, final Intent intent) {
        this.m_logger.Important("onActivityResult() Result received.");
        if (n == 8053) {
            if (n2 == -1) {
                final String string = intent.getExtras().getString("RESPONSE", "");
                if (!string.isEmpty()) {
                    this.finishOperation(WebResult.SUCCESS, string);
                    return;
                }
                this.m_logger.Error("onActivityResult() Invalid final URL received from web view.");
            }
            else {
                if (n2 == 0) {
                    this.finishOperation(WebResult.CANCEL, null);
                    return;
                }
                if (n2 != 8054) {
                    final XalLogger logger = this.m_logger;
                    final StringBuilder sb = new StringBuilder();
                    sb.append("onActivityResult() Unrecognized result code received from web view:");
                    sb.append(n2);
                    logger.Warning(sb.toString());
                }
            }
            this.finishOperation(WebResult.FAIL, null);
            return;
        }
        this.m_logger.Warning("onActivityResult() Result received from unrecognized request.");
    }
    
    public void onCreate(Bundle packageName) {
        this.m_logger.Important("onCreate()");
        if (this.getIntent().getData() == null) {
            this.m_logger.Important("onCreate() Called with no data.");
            final Bundle extras = this.getIntent().getExtras();
            if (extras == null) {
                this.m_logger.Error("onCreate() Created with no extras. Finishing.");
                this.m_logger.Flush();
                this.setResult(8052);
                this.finish();
            }
            else {
                this.m_operationId = extras.getLong("OPERATION_ID");
                this.m_cancelDelay = extras.getLong("CANCEL_DELAY", 500L);
                final XalLogger logger = this.m_logger;
                final StringBuilder sb = new StringBuilder();
                sb.append("onCreate() received delay:");
                sb.append(this.m_cancelDelay);
                logger.Information(sb.toString());
                final String string = extras.getString("START_URL", "");
                final String string2 = extras.getString("END_URL", "");
                final boolean empty = string.isEmpty();
                packageName = null;
                if (!empty && !string2.isEmpty()) {
                    final ShowUrlType showUrlType = (ShowUrlType)extras.get("SHOW_TYPE");
                    boolean boolean1 = extras.getBoolean("IN_PROC_BROWSER");
                    if (showUrlType == ShowUrlType.NonAuthFlow) {
                        boolean1 = true;
                    }
                    final ResolveInfo resolveActivity = HorizonApplication.getInstance().getPackageManager().resolveActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://")), 65536);
                    if (resolveActivity != null) {
                        packageName = (Bundle)resolveActivity.activityInfo.packageName;
                    }
                    if (boolean1) {
                        this.m_logger.Important("onCreate() Operation requested in-proc. Choosing WebKit strategy.");
                        this.setBrowserInfo("webkit-inProcRequested", 0, "none");
                        this.startWebView(string, string2, showUrlType);
                    }
                    else if (packageName != null && !((String)packageName).equals("android")) {
                        int versionCode = -1;
                        String versionName;
                        try {
                            final PackageInfo packageInfo = HorizonApplication.getInstance().getPackageManager().getPackageInfo((String)packageName, 0);
                            versionCode = packageInfo.versionCode;
                            versionName = packageInfo.versionName;
                        }
                        catch (PackageManager$NameNotFoundException ex) {
                            final XalLogger logger2 = this.m_logger;
                            final StringBuilder sb2 = new StringBuilder();
                            sb2.append("onCreate() Error in getPackageInfo(): ");
                            sb2.append(ex);
                            logger2.Error(sb2.toString());
                            versionName = "unknown";
                        }
                        if (!this.browserSupportsCustomTabs((String)packageName)) {
                            this.m_logger.Important("onCreate() Default browser does not support custom tabs. Choosing WebKit strategy.");
                            final StringBuilder sb3 = new StringBuilder();
                            sb3.append((String)packageName);
                            sb3.append("-noCustomTabs");
                            this.setBrowserInfo(sb3.toString(), versionCode, versionName);
                            this.startWebView(string, string2, showUrlType);
                        }
                        else if (!this.browserAllowedForCustomTabs((String)packageName)) {
                            this.m_logger.Important("onCreate() Default browser supports custom tabs, but is not allowed. Choosing WebKit strategy.");
                            final StringBuilder sb4 = new StringBuilder();
                            sb4.append((String)packageName);
                            sb4.append("-customTabsNotAllowed");
                            this.setBrowserInfo(sb4.toString(), versionCode, versionName);
                            this.startWebView(string, string2, showUrlType);
                        }
                        else {
                            this.m_logger.Important("onCreate() Default browser supports custom tabs and is allowed. Choosing CustomTabs strategy.");
                            final StringBuilder sb5 = new StringBuilder();
                            sb5.append((String)packageName);
                            sb5.append("-customTabsAllowed");
                            this.setBrowserInfo(sb5.toString(), versionCode, versionName);
                            this.startCustomTabsInBrowser((String)packageName, string, string2, showUrlType);
                        }
                    }
                    else {
                        this.m_logger.Important("onCreate() No default browser. Choosing WebKit strategy.");
                        this.setBrowserInfo("webkit-noDefault", 0, "none");
                        this.startWebView(string, string2, showUrlType);
                    }
                }
                else {
                    this.m_logger.Error("onCreate() Received invalid start or end URL.");
                    this.finishOperation(WebResult.FAIL, null);
                }
            }
            return;
        }
        this.m_logger.Warning("onCreate() Called with data. Dropping flow and starting app's main activity.");
        final Intent launchIntentForPackage = HorizonApplication.getInstance().getPackageManager().getLaunchIntentForPackage(HorizonApplication.getInstance().getPackageName());
        this.m_logger.Flush();
        this.getActivity().startActivity(launchIntentForPackage);
        this.finish();
    }
    
    public void onDestroy() {
        this.m_logger.Important("onDestroy() Activity destroyed.");
        if (this.m_cancelOperationOnResume) {
            this.m_logger.Important("onDestroy() Cancelling operation.");
            this.finishOperation(WebResult.CANCEL, null);
            return;
        }
        this.m_logger.Flush();
    }
    
    public void onNewIntent(final Intent intent) {
        this.m_logger.Important("onNewIntent() New intent received.");
        final Uri data = intent.getData();
        if (data == null) {
            this.m_logger.Error("onNewIntent() New intent received with no data.");
            this.finishOperation(WebResult.FAIL, null);
            return;
        }
        this.finishOperation(WebResult.SUCCESS, data.toString());
    }
    
    public void onPause() {
        this.m_logger.Important("onPause() Activity paused.");
        this.m_cancelOperationOnResume = true;
    }
    
    public void onResume() {
        this.m_logger.Important("onResume() Activity resumed.");
        if (this.m_cancelOperationOnResume) {
            this.m_logger.Important("onResume() Starting timer to cancel operation.");
            new Handler().postDelayed((Runnable)new Runnable() {
                @Override
                public void run() {
                    WebView.this.m_logger.Important("WebView.onResume() Cancelling operation.");
                    WebView.this.finishOperation(WebResult.CANCEL, null);
                }
            }, this.m_cancelDelay);
        }
    }
    
    public void setActivity(final Activity mActivity) {
        this.mActivity = mActivity;
    }
    
    public void setInputIntent(final Intent inputIntent) {
        this.inputIntent = inputIntent;
    }
    
    public void setResult(final int n) {
    }
    
    public enum ShowUrlType
    {
        CookieRemoval, 
        CookieRemovalSkipIfSharedCredentials, 
        NonAuthFlow, 
        Normal;
        
        public static ShowUrlType fromInt(final int n) {
            if (n == 0) {
                return ShowUrlType.Normal;
            }
            if (n == 1) {
                return ShowUrlType.CookieRemoval;
            }
            if (n == 2) {
                return ShowUrlType.CookieRemovalSkipIfSharedCredentials;
            }
            if (n != 3) {
                return null;
            }
            return ShowUrlType.NonAuthFlow;
        }
    }
    
    private enum WebResult
    {
        CANCEL, 
        FAIL, 
        SUCCESS;
    }
}
