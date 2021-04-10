package com.appsflyer;

import org.json.*;
import java.lang.reflect.*;
import java.text.*;
import android.view.*;
import android.app.*;
import com.google.android.gms.common.*;
import java.lang.ref.*;
import java.security.*;
import android.text.*;
import android.net.*;
import java.util.*;
import java.net.*;
import android.os.*;
import java.util.regex.*;
import java.util.concurrent.*;
import android.util.*;
import com.appsflyer.internal.referrer.*;
import com.appsflyer.internal.*;
import java.io.*;
import java.util.concurrent.atomic.*;
import com.appsflyer.internal.model.event.*;
import android.content.pm.*;
import android.content.*;

public class AppsFlyerLibCore extends AppsFlyerLib
{
    public static final String AF_PRE_INSTALL_PATH = "AF_PRE_INSTALL_PATH";
    public static String FIRST_LAUNCHES_URL;
    public static final String INSTALL_REFERRER_PREF = "rfr";
    public static final String IS_STOP_TRACKING_USED = "is_stop_tracking_used";
    public static final String LOG_TAG = "AppsFlyer_5.4.1";
    public static final String PRE_INSTALL_SYSTEM_DEFAULT = "/data/local/tmp/pre_install.appsflyer";
    public static final String PRE_INSTALL_SYSTEM_DEFAULT_ETC = "/etc/pre_install.appsflyer";
    public static final String PRE_INSTALL_SYSTEM_RO_PROP = "ro.appsflyer.preinstall.path";
    public static String REFERRER_TRACKING_URL;
    public static String REGISTER_URL;
    public static AppsFlyerLibCore instance;
    public static final String \u0131 = "5.4";
    private static final String \u0196;
    public static AppsFlyerInAppPurchaseValidatorListener \u01c3;
    public static final String \u0269 = "44";
    private static AppsFlyerConversionListener \u026a;
    private static String \u0279;
    private static String \u027e;
    private static String \u0406;
    private static final String \u0456;
    private static final List<String> \u04cf;
    protected Uri latestDeepLink;
    private long \u0140;
    private ScheduledExecutorService \u0142;
    private long \u017f;
    private String \u0197;
    private Map<Long, String> \u019a;
    private boolean \u01c0;
    private long \u0237;
    private boolean \u024d;
    private boolean \u0254;
    private ag \u025f;
    private String \u0268;
    private boolean \u027a;
    private boolean \u027c;
    private long \u027f;
    private long \u0285;
    private GoogleReferrer \u029f;
    private Map<String, Object> \u037b;
    public String \u0399;
    public String \u03b9;
    private Map<String, Object> \u03f2;
    private Application \u03f3;
    private boolean \u0408;
    private boolean \u0433;
    private boolean \u0441;
    private HuaweiReferrer \u0442;
    private String \u0445;
    private String[] \u0491;
    long \u04c0;
    
    static {
        final StringBuilder sb = new StringBuilder();
        sb.append(AppsFlyerLibCore.\u0131);
        sb.append("/androidevent?buildnumber=5.4.1&app_id=");
        \u0196 = sb.toString();
        final StringBuilder sb2 = new StringBuilder("https://%sregister.%s/api/v");
        sb2.append(AppsFlyerLibCore.\u0196);
        AppsFlyerLibCore.REGISTER_URL = sb2.toString();
        final StringBuilder sb3 = new StringBuilder("https://%sadrevenue.%s/api/v");
        sb3.append(AppsFlyerLibCore.\u0131);
        sb3.append("/android?buildnumber=5.4.1&app_id=");
        AppsFlyerLibCore.\u0406 = sb3.toString();
        final StringBuilder sb4 = new StringBuilder();
        sb4.append(AppsFlyerLibCore.\u0131);
        sb4.append("/androidevent?app_id=");
        \u0456 = sb4.toString();
        final StringBuilder sb5 = new StringBuilder("https://%sconversions.%s/api/v");
        sb5.append(AppsFlyerLibCore.\u0456);
        AppsFlyerLibCore.FIRST_LAUNCHES_URL = sb5.toString();
        final StringBuilder sb6 = new StringBuilder("https://%slaunches.%s/api/v");
        sb6.append(AppsFlyerLibCore.\u0456);
        AppsFlyerLibCore.\u0279 = sb6.toString();
        final StringBuilder sb7 = new StringBuilder("https://%sinapps.%s/api/v");
        sb7.append(AppsFlyerLibCore.\u0456);
        AppsFlyerLibCore.\u027e = sb7.toString();
        final StringBuilder sb8 = new StringBuilder("https://%sattr.%s/api/v");
        sb8.append(AppsFlyerLibCore.\u0456);
        AppsFlyerLibCore.REFERRER_TRACKING_URL = sb8.toString();
        \u04cf = Arrays.asList("is_cache");
        AppsFlyerLibCore.\u01c3 = null;
        AppsFlyerLibCore.\u026a = null;
        AppsFlyerLibCore.instance = new AppsFlyerLibCore();
    }
    
    public AppsFlyerLibCore() {
        this.latestDeepLink = null;
        this.\u0237 = -1L;
        this.\u0140 = -1L;
        this.\u027f = TimeUnit.SECONDS.toMillis(5L);
        this.\u0433 = false;
        this.\u0142 = null;
        this.\u024d = false;
        this.\u025f = new ag();
        this.\u0254 = false;
        this.\u027c = false;
        this.\u0408 = false;
        this.\u0441 = false;
        AFVersionDeclaration.init();
    }
    
    public static AppsFlyerLibCore getInstance() {
        return AppsFlyerLibCore.instance;
    }
    
    public static SharedPreferences getSharedPreferences(final Context context) {
        return context.getApplicationContext().getSharedPreferences("appsflyer-data", 0);
    }
    
    private static Map<String, Object> \u0131(final String s) {
        final HashMap<String, Object> hashMap = new HashMap<String, Object>();
        try {
            final JSONObject jsonObject = new JSONObject(s);
            final Iterator keys = jsonObject.keys();
            while (keys.hasNext()) {
                final String s2 = keys.next();
                if (!AppsFlyerLibCore.\u04cf.contains(s2)) {
                    Object value;
                    if (jsonObject.isNull(s2)) {
                        value = null;
                    }
                    else {
                        value = jsonObject.get(s2);
                    }
                    hashMap.put(s2, value);
                }
            }
            return hashMap;
        }
        catch (JSONException ex) {
            AFLogger.afErrorLog(((Throwable)ex).getMessage(), (Throwable)ex);
            return null;
        }
    }
    
    static void \u0131(final Context context, final String s) {
        AFLogger.afDebugLog("received a new (extra) referrer: ".concat(String.valueOf(s)));
        try {
            final long currentTimeMillis = System.currentTimeMillis();
            final String string = getSharedPreferences(context).getString("extraReferrers", (String)null);
            JSONObject jsonObject;
            JSONArray jsonArray;
            if (string == null) {
                jsonObject = new JSONObject();
                jsonArray = new JSONArray();
            }
            else {
                jsonObject = new JSONObject(string);
                if (jsonObject.has(s)) {
                    jsonArray = new JSONArray((String)jsonObject.get(s));
                }
                else {
                    jsonArray = new JSONArray();
                }
            }
            if (jsonArray.length() < 5L) {
                jsonArray.put(currentTimeMillis);
            }
            if (jsonObject.length() >= 4L) {
                \u0131(jsonObject);
            }
            jsonObject.put(s, (Object)jsonArray.toString());
            final String string2 = jsonObject.toString();
            final SharedPreferences$Editor edit = getSharedPreferences(context).edit();
            edit.putString("extraReferrers", string2);
            edit.apply();
        }
        catch (JSONException ex) {}
        finally {
            final StringBuilder sb = new StringBuilder("Couldn't save referrer - ");
            sb.append(s);
            sb.append(": ");
            final Throwable t;
            AFLogger.afErrorLog(sb.toString(), t);
        }
    }
    
    private void \u0131(final AFEvent afEvent) {
        afEvent.\u01c3();
        final boolean b = afEvent.\u04c0 == null;
        if (\u0131()) {
            AFLogger.afInfoLog("CustomerUserId not set, Tracking is disabled", true);
            return;
        }
        if (b) {
            if (AppsFlyerProperties.getInstance().getBoolean("launchProtectEnabled", true)) {
                if (this.\u03b9()) {
                    return;
                }
            }
            else {
                AFLogger.afInfoLog("Allowing multiple launches within a 5 second time window.");
            }
            this.\u0237 = System.currentTimeMillis();
        }
        \u01c3(AFExecutor.getInstance().\u03b9(), new e(afEvent.weakContext(), (byte)0), 150L, TimeUnit.MILLISECONDS);
    }
    
    static /* synthetic */ void \u0131(final AppsFlyerLibCore appsFlyerLibCore, final AFEvent afEvent) throws IOException {
        final StringBuilder sb = new StringBuilder("url: ");
        sb.append(afEvent.urlString());
        AFLogger.afInfoLog(sb.toString());
        String s;
        if (afEvent.\u0279 != null) {
            s = Base64.encodeToString(afEvent.\u03b9(), 2);
            AFLogger.afInfoLog("cached data: ".concat(String.valueOf(s)));
        }
        else {
            s = new JSONObject((Map)afEvent.params()).toString();
            ah.\u0399("data: ".concat(String.valueOf(s)));
        }
        if (ai.\u0269 == null) {
            ai.\u0269 = new ai();
        }
        ai.\u0269.\u0131("server_request", afEvent.urlString(), s);
        try {
            appsFlyerLibCore.\u03b9(afEvent);
        }
        catch (IOException ex) {
            AFLogger.afErrorLog("Exception in sendRequestToServer. ", ex);
            if (AppsFlyerProperties.getInstance().getBoolean("useHttpFallback", false)) {
                appsFlyerLibCore.\u03b9(afEvent.urlString(afEvent.urlString().replace("https:", "http:")));
                return;
            }
            final StringBuilder sb2 = new StringBuilder("failed to send requeset to server. ");
            sb2.append(ex.getLocalizedMessage());
            AFLogger.afInfoLog(sb2.toString());
            throw ex;
        }
    }
    
    static /* synthetic */ void \u0131(final ScheduledExecutorService scheduledExecutorService, final Runnable runnable, final TimeUnit timeUnit) {
        \u01c3(scheduledExecutorService, runnable, 10L, timeUnit);
    }
    
    private static void \u0131(final JSONObject jsonObject) {
        final ArrayList<Long> list = (ArrayList<Long>)new ArrayList<Comparable>();
        final Iterator keys = jsonObject.keys();
        while (true) {
            final boolean hasNext = keys.hasNext();
            int i = 0;
            if (hasNext) {
                final String s = keys.next();
                try {
                    for (JSONArray jsonArray = new JSONArray((String)jsonObject.get(s)); i < jsonArray.length(); ++i) {
                        list.add(jsonArray.getLong(i));
                    }
                    continue;
                }
                catch (JSONException ex) {
                    continue;
                }
                break;
            }
            break;
        }
        Collections.sort((List<Comparable>)list);
        final Iterator keys2 = jsonObject.keys();
        String s2 = null;
    Label_0108:
        while (true) {
            s2 = null;
        Label_0111:
            while (keys2.hasNext() && s2 == null) {
                final String s3 = keys2.next();
                String s4 = s2;
                try {
                    final JSONArray jsonArray2 = new JSONArray((String)jsonObject.get(s3));
                    int n = 0;
                    String s5 = s2;
                    while (true) {
                        s2 = s5;
                        s4 = s5;
                        if (n >= jsonArray2.length()) {
                            continue Label_0111;
                        }
                        s4 = s5;
                        if (jsonArray2.getLong(n) == list.get(0).longValue()) {
                            break;
                        }
                        s4 = s5;
                        if (jsonArray2.getLong(n) == list.get(1).longValue()) {
                            break;
                        }
                        s4 = s5;
                        final long long1 = jsonArray2.getLong(n);
                        s4 = s5;
                        if (long1 == list.get(list.size() - 1).longValue()) {
                            break;
                        }
                        ++n;
                        s5 = s3;
                    }
                    continue Label_0108;
                }
                catch (JSONException ex2) {
                    s2 = s4;
                    continue;
                }
                break;
            }
            break;
        }
        if (s2 != null) {
            jsonObject.remove(s2);
        }
    }
    
    private static boolean \u0131() {
        return \u0269("waitForCustomerId") && AppsFlyerProperties.getInstance().getString("AppUserId") == null;
    }
    
    private boolean \u0131(final Context context) {
        try {
            Class.forName("com.appsflyer.lvl.AppsFlyerLVL");
            final long currentTimeMillis = System.currentTimeMillis();
            this.\u03f2 = new ConcurrentHashMap<String, Object>();
            final t.d d = new t.d() {
                @Override
                public final void \u0131(final String s, final String s2) {
                    AppsFlyerLibCore.this.\u03f2.put("signedData", s);
                    AppsFlyerLibCore.this.\u03f2.put("signature", s2);
                    AppsFlyerLibCore.this.\u03f2.put("ttr", System.currentTimeMillis() - currentTimeMillis);
                    AFLogger.afInfoLog("Successfully retrieved Google LVL data.");
                }
                
                @Override
                public final void \u01c3(final String s, final Exception ex) {
                    String message;
                    if ((message = ex.getMessage()) == null) {
                        message = "unknown";
                    }
                    AppsFlyerLibCore.this.\u03f2.put("error", message);
                    AFLogger.afErrorLog(s, ex, true);
                }
            };
            try {
                final Class<?> forName = Class.forName("com.appsflyer.lvl.AppsFlyerLVL");
                final Class<?> forName2 = Class.forName("com.appsflyer.lvl.AppsFlyerLVL$resultListener");
                forName.getMethod("checkLicense", Long.TYPE, Context.class, forName2).invoke(null, currentTimeMillis, context, Proxy.newProxyInstance(forName2.getClassLoader(), new Class[] { forName2 }, new InvocationHandler() {
                    private /* synthetic */ d \u03b9 = d;
                    
                    @Override
                    public final Object invoke(final Object o, final Method method, final Object[] array) {
                        if (method.getName().equals("onLvlResult")) {
                            String s;
                            if (array[0] != null) {
                                s = (String)array[0];
                            }
                            else {
                                s = null;
                            }
                            String s2;
                            if (array[1] != null) {
                                s2 = (String)array[1];
                            }
                            else {
                                s2 = null;
                            }
                            final d \u03b9 = this.\u03b9;
                            if (\u03b9 == null) {
                                AFLogger.afDebugLog("onLvlResult invocation succeeded, but listener is null");
                                return null;
                            }
                            if (s != null && s2 != null) {
                                \u03b9.\u0131(s, s2);
                                return null;
                            }
                            if (s2 == null) {
                                this.\u03b9.\u01c3("onLvlResult with error", new Exception("AFLVL Invalid signature"));
                                return null;
                            }
                            this.\u03b9.\u01c3("onLvlResult with error", new Exception("AFLVL Invalid signedData"));
                            return null;
                        }
                        else {
                            if (!method.getName().equals("onLvlFailure")) {
                                final d \u03b92 = this.\u03b9;
                                if (\u03b92 != null) {
                                    \u03b92.\u01c3("lvlInvocation failed", new Exception("com.appsflyer.lvl.AppsFlyerLVL$resultListener invocation failed"));
                                }
                                return null;
                            }
                            final d \u03b93 = this.\u03b9;
                            if (\u03b93 == null) {
                                AFLogger.afDebugLog("onLvlFailure: listener is null");
                                return null;
                            }
                            if (array[0] != null) {
                                \u03b93.\u01c3("onLvlFailure with exception", (Exception)array[0]);
                                return null;
                            }
                            \u03b93.\u01c3("onLvlFailure", new Exception("unknown"));
                            return null;
                        }
                    }
                }));
                return true;
            }
            catch (InvocationTargetException ex) {
                ((t.d)d).\u01c3(ex.getClass().getSimpleName(), ex);
                return true;
            }
            catch (IllegalAccessException ex2) {
                ((t.d)d).\u01c3(ex2.getClass().getSimpleName(), ex2);
                return true;
            }
            catch (NoSuchMethodException ex3) {
                ((t.d)d).\u01c3(ex3.getClass().getSimpleName(), ex3);
                return true;
            }
            catch (ClassNotFoundException ex4) {
                ((t.d)d).\u01c3(ex4.getClass().getSimpleName(), ex4);
                return true;
            }
        }
        catch (ClassNotFoundException ex5) {
            return false;
        }
    }
    
    public static boolean \u0131(final SharedPreferences sharedPreferences) {
        return Boolean.parseBoolean(sharedPreferences.getString("sentSuccessfully", (String)null));
    }
    
    private static File \u0196(final String s) {
        if (s != null) {
            try {
                if (s.trim().length() > 0) {
                    return new File(s.trim());
                }
            }
            finally {
                final Throwable t;
                AFLogger.afErrorLog(t.getMessage(), t);
            }
        }
        return null;
    }
    
    private static boolean \u0196(final Context context) {
        return AppsFlyerProperties.getInstance().getBoolean("collectAndroidIdForceByUser", false) || AppsFlyerProperties.getInstance().getBoolean("collectIMEIForceByUser", false) || !\u0279(context);
    }
    
    static /* synthetic */ String \u01c3(final String s) {
        return AppsFlyerProperties.getInstance().getString(s);
    }
    
    private static String \u01c3(final SimpleDateFormat simpleDateFormat, final Context context) {
        String string;
        if ((string = getSharedPreferences(context).getString("appsFlyerFirstInstall", (String)null)) == null) {
            String format;
            if (\u0456(context)) {
                AFLogger.afDebugLog("AppsFlyer: first launch detected");
                format = simpleDateFormat.format(new Date());
            }
            else {
                format = "";
            }
            final SharedPreferences$Editor edit = getSharedPreferences(context).edit();
            edit.putString("appsFlyerFirstInstall", format);
            edit.apply();
            string = format;
        }
        AFLogger.afInfoLog("AppsFlyer: first launch date: ".concat(String.valueOf(string)));
        return string;
    }
    
    static /* synthetic */ void \u01c3(final Context context, final String s, final String s2) {
        final SharedPreferences$Editor edit = getSharedPreferences(context).edit();
        edit.putString(s, s2);
        edit.apply();
    }
    
    private void \u01c3(final Context context, final String s, final String \u0269, final Intent \u01c3) {
        final AFEvent context2 = new Launch().context(context);
        context2.\u04c0 = null;
        final AFEvent key = context2.key(s);
        key.\u0399 = null;
        key.\u0196 = \u0269;
        key.\u01c3 = \u01c3;
        key.\u0406 = null;
        this.\u0131(key);
    }
    
    private static void \u01c3(final Context context, final Map<String, Object> map) {
        final WindowManager windowManager = (WindowManager)context.getSystemService("window");
        if (windowManager != null) {
            final int rotation = windowManager.getDefaultDisplay().getRotation();
            String s;
            if (rotation != 0) {
                if (rotation != 1) {
                    if (rotation != 2) {
                        if (rotation != 3) {
                            s = "";
                        }
                        else {
                            s = "lr";
                        }
                    }
                    else {
                        s = "pr";
                    }
                }
                else {
                    s = "l";
                }
            }
            else {
                s = "p";
            }
            map.put("sc_o", s);
        }
    }
    
    private static void \u01c3(final ScheduledExecutorService scheduledExecutorService, final Runnable runnable, final long n, final TimeUnit timeUnit) {
        while (true) {
            if (scheduledExecutorService != null) {
                while (true) {
                    try {
                        if (!scheduledExecutorService.isShutdown() && !scheduledExecutorService.isTerminated()) {
                            scheduledExecutorService.schedule(runnable, n, timeUnit);
                            return;
                        }
                        AFLogger.afWarnLog("scheduler is null, shut downed or terminated");
                        return;
                        final RejectedExecutionException ex;
                        AFLogger.afErrorLog("scheduleJob failed with RejectedExecutionException Exception", ex);
                        return;
                    }
                    catch (RejectedExecutionException ex2) {}
                    final RejectedExecutionException ex2;
                    final RejectedExecutionException ex = ex2;
                    continue;
                }
            }
            continue;
        }
    }
    
    private static String \u0269(final Context context) {
        final boolean b = context instanceof Activity;
        final Throwable t = null;
        final Throwable t2 = null;
        Throwable t3 = t;
        if (b) {
            final Intent intent = ((Activity)context).getIntent();
            t3 = t;
            if (intent != null) {
                Throwable t4 = null;
                Label_0112: {
                    try {
                        final Bundle extras = intent.getExtras();
                        if (extras != null) {
                            final String string = extras.getString("af");
                            if (string != null) {
                                try {
                                    AFLogger.afInfoLog("Push Notification received af payload = ".concat(String.valueOf(string)));
                                    extras.remove("af");
                                    ((Activity)context).setIntent(intent.putExtras(extras));
                                    return string;
                                }
                                finally {
                                    break Label_0112;
                                }
                            }
                            return string;
                        }
                        return (String)t3;
                    }
                    finally {
                        t4 = t2;
                    }
                }
                AFLogger.afErrorLog(t3.getMessage(), t3);
                t3 = t4;
            }
        }
        return (String)t3;
    }
    
    private static String \u0269(final String s, final PackageManager packageManager, final String s2) {
        try {
            final Bundle metaData = ((PackageItemInfo)packageManager.getApplicationInfo(s2, 128)).metaData;
            if (metaData != null) {
                final Object value = metaData.get(s);
                if (value != null) {
                    final String string = value.toString();
                    if (!string.replaceAll("\\p{C}", "").equals(string)) {
                        final StringBuilder sb = new StringBuilder("Manifest meta-data ");
                        sb.append(s);
                        sb.append(": ");
                        sb.append(string);
                        sb.append(" contains non-printing characters");
                        AFLogger.afWarnLog(sb.toString());
                    }
                    return string;
                }
            }
        }
        finally {
            final StringBuilder sb2 = new StringBuilder("Could not find ");
            sb2.append(s);
            sb2.append(" value in the manifest");
            final Throwable t;
            AFLogger.afErrorLog(sb2.toString(), t);
        }
        return null;
    }
    
    private static void \u0269(final Context context, final String s, final long n) {
        final SharedPreferences$Editor edit = getSharedPreferences(context).edit();
        edit.putLong(s, n);
        edit.apply();
    }
    
    private static void \u0269(final Context context, final Map<String, ? super String> map) {
        final s \u03b9 = s.d.\u03b9;
        final s.a \u01c3 = s.\u01c3(context);
        map.put("network", \u01c3.\u0131);
        if (\u01c3.\u03b9 != null) {
            map.put("operator", \u01c3.\u03b9);
        }
        if (\u01c3.\u0269 != null) {
            map.put("carrier", \u01c3.\u0269);
        }
    }
    
    private static void \u0269(final Map<String, String> map) {
        if (AppsFlyerLibCore.\u026a != null) {
            try {
                final StringBuilder sb = new StringBuilder("Calling onAppOpenAttribution with:\n");
                sb.append(map.toString());
                AFLogger.afDebugLog(sb.toString());
                AppsFlyerLibCore.\u026a.onAppOpenAttribution(map);
            }
            finally {
                final Throwable t;
                AFLogger.afErrorLog(t.getLocalizedMessage(), t);
            }
        }
    }
    
    private boolean \u0269() {
        final GoogleReferrer \u029f = this.\u029f;
        return \u029f != null && \u029f.oldMap.size() > 0;
    }
    
    private static boolean \u0269(final String s) {
        return AppsFlyerProperties.getInstance().getBoolean(s, false);
    }
    
    private static boolean \u0279(final Context context) {
        try {
            if (GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(context) == 0) {
                return true;
            }
        }
        finally {
            final Throwable t;
            AFLogger.afErrorLog("WARNING:  Google play services is unavailable. ", t);
        }
        try {
            context.getPackageManager().getPackageInfo("com.google.android.gms", 0);
            return true;
        }
        catch (PackageManager$NameNotFoundException ex) {
            AFLogger.afErrorLog("WARNING:  Google Play Services is unavailable. ", (Throwable)ex);
            return false;
        }
    }
    
    private static int \u0399(final SharedPreferences sharedPreferences, final String s, final boolean b) {
        int int1;
        final int n = int1 = sharedPreferences.getInt(s, 0);
        if (b) {
            int1 = n + 1;
            final SharedPreferences$Editor edit = sharedPreferences.edit();
            edit.putInt(s, int1);
            edit.apply();
        }
        if (ai.\u0269 == null) {
            ai.\u0269 = new ai();
        }
        if (ai.\u0269.\u0406()) {
            if (ai.\u0269 == null) {
                ai.\u0269 = new ai();
            }
            ai.\u0269.\u0269(String.valueOf(int1));
        }
        return int1;
    }
    
    private static void \u0399(Context context) {
        int n;
        if (AndroidUtils.\u0269()) {
            n = 23;
            AFLogger.afRDLog("OPPO device found");
        }
        else {
            n = 18;
        }
        if (Build$VERSION.SDK_INT >= n && !\u0269("keyPropDisableAFKeystore")) {
            final StringBuilder sb = new StringBuilder("OS SDK is=");
            sb.append(Build$VERSION.SDK_INT);
            sb.append("; use KeyStore");
            AFLogger.afRDLog(sb.toString());
            final AFKeystoreWrapper afKeystoreWrapper = new AFKeystoreWrapper(context);
            while (true) {
                Label_0229: {
                    Label_0118: {
                        if (afKeystoreWrapper.\u01c3()) {
                            break Label_0118;
                        }
                        afKeystoreWrapper.\u03b9 = ae.\u0399(new WeakReference<Context>(context));
                        afKeystoreWrapper.\u0269 = 0;
                        afKeystoreWrapper.\u0269(afKeystoreWrapper.\u0269());
                        break Label_0229;
                    }
                    final String \u0269 = afKeystoreWrapper.\u0269();
                    context = (Context)afKeystoreWrapper.\u0131;
                    synchronized (context) {
                        ++afKeystoreWrapper.\u0269;
                        AFLogger.afInfoLog("Deleting key with alias: ".concat(String.valueOf(\u0269)));
                        try {
                            synchronized (afKeystoreWrapper.\u0131) {
                                afKeystoreWrapper.\u0399.deleteEntry(\u0269);
                            }
                        }
                        catch (KeyStoreException ex) {
                            final StringBuilder sb2 = new StringBuilder("Exception ");
                            sb2.append(ex.getMessage());
                            sb2.append(" occurred");
                            AFLogger.afErrorLog(sb2.toString(), ex);
                        }
                        continue;
                        context = (Context)afKeystoreWrapper.\u0131();
                        AppsFlyerProperties.getInstance().set("KSAppsFlyerId", (String)context);
                        AppsFlyerProperties.getInstance().set("KSAppsFlyerRICounter", String.valueOf(afKeystoreWrapper.\u0399()));
                        return;
                    }
                }
                break;
            }
        }
        final StringBuilder sb3 = new StringBuilder("OS SDK is=");
        sb3.append(Build$VERSION.SDK_INT);
        sb3.append("; no KeyStore usage");
        AFLogger.afRDLog(sb3.toString());
    }
    
    private void \u0399(AFEvent \u01c3) {
        final Context context = \u01c3.context();
        final String \u04cf = \u01c3.\u04c0;
        if (context == null) {
            AFLogger.afDebugLog("sendTrackingWithEvent - got null context. skipping event/launch.");
            return;
        }
        final SharedPreferences sharedPreferences = getSharedPreferences(context);
        AppsFlyerProperties.getInstance().saveProperties(sharedPreferences);
        if (!this.isTrackingStopped()) {
            final StringBuilder sb = new StringBuilder("sendTrackingWithEvent from activity: ");
            sb.append(context.getClass().getName());
            AFLogger.afInfoLog(sb.toString());
        }
        int n = 0;
        final boolean b = false;
        final boolean \u0237 = \u04cf == null;
        final boolean b2 = \u01c3 instanceof BackgroundReferrerLaunch;
        final boolean b3 = \u01c3 instanceof Attr;
        \u01c3.\u0237 = \u0237;
        final Map<String, Object> \u0269 = this.\u0269(\u01c3);
        final String s = \u0269.get("appsflyerKey");
        if (s != null && s.length() != 0) {
            if (!this.isTrackingStopped()) {
                AFLogger.afInfoLog("AppsFlyerLib.sendTrackingWithEvent");
            }
            final int launchCounter = this.getLaunchCounter(sharedPreferences, false);
            String s2;
            if (!b3 && !b2) {
                if (\u0237) {
                    if (launchCounter < 2) {
                        s2 = AppsFlyerLibCore.FIRST_LAUNCHES_URL;
                    }
                    else {
                        s2 = AppsFlyerLibCore.\u0279;
                    }
                }
                else {
                    s2 = AppsFlyerLibCore.\u027e;
                }
            }
            else {
                s2 = AppsFlyerLibCore.REFERRER_TRACKING_URL;
            }
            final String url = ServerConfigHandler.getUrl(s2);
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(url);
            sb2.append(context.getPackageName());
            final String string = sb2.toString();
            final StringBuilder sb3 = new StringBuilder();
            sb3.append(string);
            sb3.append("&buildnumber=5.4.1");
            final String string2 = sb3.toString();
            final String configuredChannel = this.getConfiguredChannel(context);
            String string3 = string2;
            if (configuredChannel != null) {
                final StringBuilder sb4 = new StringBuilder();
                sb4.append(string2);
                sb4.append("&channel=");
                sb4.append(configuredChannel);
                string3 = sb4.toString();
            }
            if ((AppsFlyerProperties.getInstance().getBoolean("collectAndroidIdForceByUser", false) || AppsFlyerProperties.getInstance().getBoolean("collectIMEIForceByUser", false)) && \u0269.get("advertiserId") != null) {
                try {
                    if (TextUtils.isEmpty((CharSequence)this.\u03b9) && \u0269.remove("android_id") != null) {
                        AFLogger.afInfoLog("validateGaidAndIMEI :: removing: android_id");
                    }
                    if (TextUtils.isEmpty((CharSequence)this.\u0399) && \u0269.remove("imei") != null) {
                        AFLogger.afInfoLog("validateGaidAndIMEI :: removing: imei");
                    }
                }
                catch (Exception ex) {
                    AFLogger.afErrorLog("failed to remove IMEI or AndroidID key from params; ", ex);
                }
            }
            \u01c3 = \u01c3.urlString(string3).params(\u0269).\u01c3();
            \u01c3.\u0268 = launchCounter;
            final a a = new a(\u01c3, (byte)0);
            if (\u0237) {
                boolean b4 = b;
                if (GoogleReferrer.allow(this, context)) {
                    b4 = b;
                    if (!this.\u0269()) {
                        AFLogger.afDebugLog("Failed to get new referrer, wait ...");
                        b4 = true;
                    }
                }
                final HuaweiReferrer \u0442 = this.\u0442;
                boolean b5 = b4;
                if (\u0442 != null) {
                    b5 = b4;
                    if (\u0442.valid()) {
                        b5 = true;
                    }
                }
                boolean b6 = b5;
                if (this.\u0408) {
                    b6 = b5;
                    if (!this.\u0399()) {
                        AFLogger.afDebugLog("fetching Facebook deferred AppLink data, wait ...");
                        b6 = true;
                    }
                }
                n = (b6 ? 1 : 0);
                if (this.\u0441) {
                    n = (b6 ? 1 : 0);
                    if (!this.\u04c0()) {
                        n = 1;
                    }
                }
            }
            ScheduledExecutorService scheduledExecutorService;
            if (AFDeepLinkManager.\u0269) {
                AFLogger.afRDLog("ESP deeplink: execute launch on SerialExecutor");
                final AFExecutor instance = AFExecutor.getInstance();
                if (instance.\u01c3 == null) {
                    instance.\u01c3 = Executors.newSingleThreadScheduledExecutor(instance.\u0131);
                }
                scheduledExecutorService = instance.\u01c3;
            }
            else {
                scheduledExecutorService = AFExecutor.getInstance().\u03b9();
            }
            long n2;
            if (n != 0) {
                n2 = 500L;
            }
            else {
                n2 = 0L;
            }
            \u01c3(scheduledExecutorService, a, n2, TimeUnit.MILLISECONDS);
            return;
        }
        AFLogger.afDebugLog("Not sending data yet, waiting for dev key");
    }
    
    static /* synthetic */ void \u0399(final AppsFlyerLibCore appsFlyerLibCore) {
        final AFEvent context = new Attr().context((Context)appsFlyerLibCore.\u03f3);
        if (appsFlyerLibCore.\u0269() && appsFlyerLibCore.\u0399(context, getSharedPreferences((Context)appsFlyerLibCore.\u03f3))) {
            appsFlyerLibCore.\u0399(context);
        }
    }
    
    private boolean \u0399() {
        final Map<String, Object> \u037b = this.\u037b;
        return \u037b != null && !\u037b.isEmpty();
    }
    
    private boolean \u0399(final AFEvent afEvent, final SharedPreferences sharedPreferences) {
        final int launchCounter = this.getLaunchCounter(sharedPreferences, false);
        final boolean b = launchCounter == 1 && !(afEvent instanceof Attr);
        return (!sharedPreferences.getBoolean("newGPReferrerSent", false) && launchCounter == 1) || b;
    }
    
    private static String \u03b9(final Context context, final String s) {
        final SharedPreferences sharedPreferences = getSharedPreferences(context);
        if (sharedPreferences.contains("CACHED_CHANNEL")) {
            return sharedPreferences.getString("CACHED_CHANNEL", (String)null);
        }
        final SharedPreferences$Editor edit = getSharedPreferences(context).edit();
        edit.putString("CACHED_CHANNEL", s);
        edit.apply();
        return s;
    }
    
    private static String \u03b9(File file, String property) {
        Throwable t;
        try {
            final Properties properties = new Properties();
            final Object o = new FileReader(file);
            try {
                properties.load((Reader)o);
                AFLogger.afInfoLog("Found PreInstall property!");
                property = properties.getProperty(property);
                try {
                    ((Reader)o).close();
                    return property;
                }
                finally {
                    AFLogger.afErrorLog(((Throwable)file).getMessage(), (Throwable)file);
                    return property;
                }
            }
            catch (FileNotFoundException ex) {}
            finally {
                file = (File)o;
            }
        }
        catch (FileNotFoundException ex2) {
            t = null;
        }
        finally {
            file = null;
        }
        try {
            AFLogger.afErrorLog(t.getMessage(), t);
            if (file == null) {
                return null;
            }
            try {
                ((Reader)file).close();
                return null;
            }
            finally {}
        }
        finally {
            final StringBuilder sb = new StringBuilder("PreInstall file wasn't found: ");
            sb.append(file.getAbsolutePath());
            AFLogger.afDebugLog(sb.toString());
            if (t != null) {
                ((Reader)t).close();
                return null;
            }
            return null;
            while (true) {
                try {
                    final Reader reader;
                    reader.close();
                }
                finally {
                    final Throwable t2;
                    AFLogger.afErrorLog(t2.getMessage(), t2);
                }
                continue;
            }
            AFLogger.afErrorLog(((Throwable)file).getMessage(), (Throwable)file);
            return null;
        }
    }
    
    private static String \u03b9(String s) {
        try {
            s = (String)Class.forName("android.os.SystemProperties").getMethod("get", String.class).invoke(null, s);
            return s;
        }
        finally {
            final Throwable t;
            AFLogger.afErrorLog(t.getMessage(), t);
            return null;
        }
    }
    
    private static Map<String, Object> \u03b9(final Context context) throws y {
        final String string = getSharedPreferences(context).getString("attributionId", (String)null);
        if (string != null && string.length() > 0) {
            return \u0131(string);
        }
        throw new y();
    }
    
    private void \u03b9(final AFEvent afEvent) throws IOException {
        final URL url = new URL(afEvent.\u0456);
        final byte[] \u03b9 = afEvent.\u03b9();
        final String key = afEvent.key();
        final String \u0279 = afEvent.\u0279;
        final boolean \u03b92 = afEvent.\u0399();
        final Context context = afEvent.context();
        Object o = afEvent.\u0269;
        int launchCounter;
        if (\u03b92 && AppsFlyerLibCore.\u026a != null) {
            launchCounter = 1;
        }
        else {
            launchCounter = 0;
        }
        String \u01c3;
        SharedPreferences sharedPreferences;
        while (true) {
            while (true) {
                Label_0903: {
                    try {
                        final HttpURLConnection httpURLConnection = (HttpURLConnection)url.openConnection();
                        try {
                            httpURLConnection.setRequestMethod("POST");
                            httpURLConnection.setRequestProperty("Content-Length", String.valueOf(\u03b9.length));
                            String s;
                            if (afEvent.isEncrypt()) {
                                s = "application/octet-stream";
                            }
                            else {
                                s = "application/json";
                            }
                            httpURLConnection.setRequestProperty("Content-Type", s);
                            httpURLConnection.setConnectTimeout(10000);
                            httpURLConnection.setDoOutput(true);
                            try {
                                final DataOutputStream dataOutputStream = new DataOutputStream(httpURLConnection.getOutputStream());
                                try {
                                    dataOutputStream.write(\u03b9);
                                    dataOutputStream.close();
                                    final int responseCode = httpURLConnection.getResponseCode();
                                    \u01c3 = this.\u01c3(httpURLConnection);
                                    if (ai.\u0269 == null) {
                                        ai.\u0269 = new ai();
                                    }
                                    ai.\u0269.\u0131("server_response", url.toString(), String.valueOf(responseCode), \u01c3);
                                    AFLogger.afInfoLog("response code: ".concat(String.valueOf(responseCode)));
                                    sharedPreferences = getSharedPreferences(context);
                                    if (responseCode == 200) {
                                        if (context == null || !\u03b92) {
                                            break Label_0903;
                                        }
                                        this.\u0140 = System.currentTimeMillis();
                                        if (o != null) {
                                            ((AppsFlyerTrackingRequestListener)o).onTrackingRequestSuccess();
                                        }
                                        if (\u0279 != null) {
                                            aa.\u01c3();
                                            aa.\u01c3(\u0279, context);
                                        }
                                        else {
                                            o = getSharedPreferences(context).edit();
                                            ((SharedPreferences$Editor)o).putString("sentSuccessfully", "true");
                                            ((SharedPreferences$Editor)o).apply();
                                            if (!this.\u0433) {
                                                if (System.currentTimeMillis() - this.\u04c0 >= 15000L) {
                                                    if (this.\u0142 == null) {
                                                        this.\u0142 = AFExecutor.getInstance().\u03b9();
                                                        o = new c(context);
                                                        \u01c3(this.\u0142, (Runnable)o, 1L, TimeUnit.SECONDS);
                                                    }
                                                }
                                            }
                                        }
                                        o = AppsFlyerProperties.getInstance().getString("afUninstallToken");
                                        if (o != null) {
                                            AFLogger.afDebugLog("Uninstall Token exists: ".concat(String.valueOf(o)));
                                            if (!sharedPreferences.getBoolean("sentRegisterRequestToAF", false)) {
                                                AFLogger.afDebugLog("Resending Uninstall token to AF servers: ".concat(String.valueOf(o)));
                                                af.\u0131(context, (String)o);
                                            }
                                        }
                                        if (this.latestDeepLink != null) {
                                            this.latestDeepLink = null;
                                        }
                                        this.\u0254 = ServerConfigHandler.\u0131(\u01c3).optBoolean("send_background", false);
                                    }
                                    else if (o != null) {
                                        ((AppsFlyerTrackingRequestListener)o).onTrackingRequestFailure("Failure: ".concat(String.valueOf(responseCode)));
                                    }
                                    final long long1 = sharedPreferences.getLong("appsflyerConversionDataCacheExpiration", 0L);
                                    if (long1 != 0L && System.currentTimeMillis() - long1 > 5184000000L) {
                                        final SharedPreferences$Editor edit = getSharedPreferences(context).edit();
                                        edit.putBoolean("sixtyDayConversionData", true);
                                        edit.apply();
                                        final SharedPreferences$Editor edit2 = getSharedPreferences(context).edit();
                                        edit2.putString("attributionId", (String)null);
                                        edit2.apply();
                                        \u0269(context, "appsflyerConversionDataCacheExpiration", 0L);
                                    }
                                    if (sharedPreferences.getString("attributionId", (String)null) == null && key != null && launchCounter != 0) {
                                        final b b = new b(this, (Application)context.getApplicationContext(), key, (byte)0);
                                        \u01c3(b.\u0399, b, 10L, TimeUnit.MILLISECONDS);
                                    }
                                    else if (key == null) {
                                        AFLogger.afWarnLog("AppsFlyer dev key is missing.");
                                    }
                                    else if (launchCounter != 0 && sharedPreferences.getString("attributionId", (String)null) != null) {
                                        launchCounter = this.getLaunchCounter(sharedPreferences, false);
                                        if (launchCounter > 1) {
                                            try {
                                                final Map<String, Object> \u03b93 = \u03b9(context);
                                                if (\u03b93 != null) {
                                                    try {
                                                        if (!\u03b93.containsKey("is_first_launch")) {
                                                            \u03b93.put("is_first_launch", Boolean.FALSE);
                                                        }
                                                        final StringBuilder sb = new StringBuilder("Calling onConversionDataSuccess with:\n");
                                                        sb.append(\u03b93.toString());
                                                        AFLogger.afDebugLog(sb.toString());
                                                        AppsFlyerLibCore.\u026a.onConversionDataSuccess(\u03b93);
                                                    }
                                                    finally {
                                                        final Throwable t;
                                                        AFLogger.afErrorLog(t.getLocalizedMessage(), t);
                                                    }
                                                }
                                            }
                                            catch (y y) {
                                                AFLogger.afErrorLog(y.getMessage(), y);
                                            }
                                        }
                                    }
                                    if (httpURLConnection != null) {
                                        httpURLConnection.disconnect();
                                    }
                                    return;
                                }
                                finally {}
                            }
                            finally {}
                            final OutputStream outputStream;
                            if (outputStream == null) {
                                if (o != null) {
                                    ((AppsFlyerTrackingRequestListener)o).onTrackingRequestFailure("No Connectivity");
                                }
                            }
                            else {
                                outputStream.close();
                            }
                        }
                        finally {}
                    }
                    finally {
                        \u01c3 = null;
                    }
                    break;
                }
                continue;
            }
        }
        if (\u01c3 != null) {
            ((HttpURLConnection)\u01c3).disconnect();
        }
        throw sharedPreferences;
    }
    
    private boolean \u03b9() {
        if (this.\u0237 > 0L) {
            final long n = System.currentTimeMillis() - this.\u0237;
            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS Z", Locale.US);
            final long \u0237 = this.\u0237;
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            final String format = simpleDateFormat.format(new Date(\u0237));
            final long \u0140 = this.\u0140;
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            final String format2 = simpleDateFormat.format(new Date(\u0140));
            if (n < this.\u027f && !this.isTrackingStopped()) {
                AFLogger.afInfoLog(String.format(Locale.US, "Last Launch attempt: %s;\nLast successful Launch event: %s;\nThis launch is blocked: %s ms < %s ms", format, format2, n, this.\u027f));
                return true;
            }
            if (!this.isTrackingStopped()) {
                AFLogger.afInfoLog(String.format(Locale.US, "Last Launch attempt: %s;\nLast successful Launch event: %s;\nSending launch (+%s ms)", format, format2, n));
                return false;
            }
        }
        else if (!this.isTrackingStopped()) {
            AFLogger.afInfoLog("Sending first launch for this session!");
        }
        return false;
    }
    
    private static boolean \u03b9(final File file) {
        return file == null || !file.exists();
    }
    
    private static boolean \u0406(final Context context) {
        if (context != null) {
            if (Build$VERSION.SDK_INT >= 23) {
                try {
                    final ConnectivityManager connectivityManager = (ConnectivityManager)context.getSystemService("connectivity");
                    final Network[] allNetworks = connectivityManager.getAllNetworks();
                    for (int length = allNetworks.length, i = 0; i < length; ++i) {
                        final NetworkCapabilities networkCapabilities = connectivityManager.getNetworkCapabilities(allNetworks[i]);
                        if (networkCapabilities.hasTransport(4) && !networkCapabilities.hasCapability(15)) {
                            return true;
                        }
                    }
                    return false;
                }
                catch (Exception ex) {
                    AFLogger.afErrorLog("Failed collecting ivc data", ex);
                    return false;
                }
            }
            if (Build$VERSION.SDK_INT >= 16) {
                final ArrayList<String> list = new ArrayList<String>();
                try {
                    for (final NetworkInterface networkInterface : Collections.list(NetworkInterface.getNetworkInterfaces())) {
                        if (networkInterface.isUp()) {
                            list.add(networkInterface.getName());
                        }
                    }
                    return list.contains("tun0");
                }
                catch (Exception ex2) {
                    AFLogger.afErrorLog("Failed collecting ivc data", ex2);
                }
            }
        }
        return false;
    }
    
    private static boolean \u0456(final Context context) {
        return !getSharedPreferences(context).contains("appsFlyerCount");
    }
    
    private static float \u04c0(final Context context) {
        try {
            final Intent registerReceiver = context.getApplicationContext().registerReceiver((BroadcastReceiver)null, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
            final int intExtra = registerReceiver.getIntExtra("level", -1);
            final int intExtra2 = registerReceiver.getIntExtra("scale", -1);
            if (intExtra != -1 && intExtra2 != -1) {
                return intExtra / (float)intExtra2 * 100.0f;
            }
            return 50.0f;
        }
        finally {
            final Throwable t;
            AFLogger.afErrorLog(t.getMessage(), t);
            return 1.0f;
        }
    }
    
    private boolean \u04c0() {
        final Map<String, Object> \u03f2 = this.\u03f2;
        return \u03f2 != null && !\u03f2.isEmpty();
    }
    
    @Override
    public void enableFacebookDeferredApplinks(final boolean \u0458) {
        this.\u0408 = \u0458;
    }
    
    @Override
    public AppsFlyerLib enableLocationCollection(final boolean \u024d) {
        this.\u024d = \u024d;
        return this;
    }
    
    @Override
    public String getAppsFlyerUID(final Context context) {
        if (ai.\u0269 == null) {
            ai.\u0269 = new ai();
        }
        ai.\u0269.\u0131("public_api_call", "getAppsFlyerUID", new String[0]);
        return ae.\u0399(new WeakReference<Context>(context));
    }
    
    @Override
    public String getAttributionId(final Context context) {
        try {
            return new ac(context).\u0269();
        }
        finally {
            final Throwable t;
            AFLogger.afErrorLog("Could not collect facebook attribution id. ", t);
            return null;
        }
    }
    
    public String getConfiguredChannel(final Context context) {
        String s;
        if ((s = AppsFlyerProperties.getInstance().getString("channel")) == null) {
            if (context == null) {
                s = null;
            }
            else {
                s = \u0269("CHANNEL", context.getPackageManager(), context.getPackageName());
            }
        }
        if (s != null && s.equals("")) {
            return null;
        }
        return s;
    }
    
    protected void getConversionData(final Context context, final ConversionDataListener conversionDataListener) {
        AppsFlyerLibCore.\u026a = new AppsFlyerConversionListener() {
            @Override
            public final void onAppOpenAttribution(final Map<String, String> map) {
            }
            
            @Override
            public final void onAttributionFailure(final String s) {
            }
            
            @Override
            public final void onConversionDataFail(final String s) {
                AFLogger.afDebugLog("Calling onConversionFailure with:\n".concat(String.valueOf(s)));
                conversionDataListener.onConversionFailure(s);
            }
            
            @Override
            public final void onConversionDataSuccess(final Map<String, Object> map) {
                final StringBuilder sb = new StringBuilder("Calling onConversionDataLoaded with:\n");
                sb.append(map.toString());
                AFLogger.afDebugLog(sb.toString());
                conversionDataListener.onConversionDataLoaded(map);
            }
        };
    }
    
    @Override
    public String getHostName() {
        final String string = AppsFlyerProperties.getInstance().getString("custom_host");
        if (string != null) {
            return string;
        }
        return "appsflyer.com";
    }
    
    @Override
    public String getHostPrefix() {
        final String string = AppsFlyerProperties.getInstance().getString("custom_host_prefix");
        if (string != null) {
            return string;
        }
        return "";
    }
    
    public final int getLaunchCounter(final SharedPreferences sharedPreferences, final boolean b) {
        return \u0399(sharedPreferences, "appsFlyerCount", b);
    }
    
    @Override
    public String getOutOfStore(final Context context) {
        final String string = AppsFlyerProperties.getInstance().getString("api_store_value");
        if (string != null) {
            return string;
        }
        String \u0269;
        if (context == null) {
            \u0269 = null;
        }
        else {
            \u0269 = \u0269("AF_STORE", context.getPackageManager(), context.getPackageName());
        }
        if (\u0269 != null) {
            return \u0269;
        }
        AFLogger.afInfoLog("No out-of-store value set");
        return null;
    }
    
    @Override
    public String getSdkVersion() {
        if (ai.\u0269 == null) {
            ai.\u0269 = new ai();
        }
        ai.\u0269.\u0131("public_api_call", "getSdkVersion", new String[0]);
        final StringBuilder sb = new StringBuilder("version: 5.4.1 (build ");
        sb.append(AppsFlyerLibCore.\u0269);
        sb.append(")");
        return sb.toString();
    }
    
    protected void handleDeepLinkCallback(final Context context, final Map<String, Object> map, final Uri uri) {
        final String string = uri.toString();
        String replace;
        if (string == null) {
            replace = null;
        }
        else {
            replace = string;
            if (string.matches("fb\\d*?://authorize.*")) {
                replace = string;
                if (string.contains("access_token")) {
                    final int index = string.indexOf(63);
                    String substring;
                    if (index == -1) {
                        substring = "";
                    }
                    else {
                        substring = string.substring(index);
                    }
                    replace = string;
                    if (substring.length() != 0) {
                        ArrayList<String> list = new ArrayList<String>();
                        if (substring.contains("&")) {
                            list = new ArrayList<String>(Arrays.asList(substring.split("&")));
                        }
                        else {
                            list.add(substring);
                        }
                        final StringBuilder sb = new StringBuilder();
                        final Iterator<String> iterator = list.iterator();
                        while (iterator.hasNext()) {
                            final String s = iterator.next();
                            if (s.contains("access_token")) {
                                iterator.remove();
                            }
                            else {
                                Label_0244: {
                                    String s2;
                                    if (sb.length() != 0) {
                                        s2 = "&";
                                    }
                                    else {
                                        if (s.startsWith("?")) {
                                            break Label_0244;
                                        }
                                        s2 = "?";
                                    }
                                    sb.append(s2);
                                }
                                sb.append(s);
                            }
                        }
                        replace = string.replace(substring, sb.toString());
                    }
                }
            }
        }
        if (!map.containsKey("af_deeplink")) {
            map.put("af_deeplink", replace);
        }
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        hashMap.put("link", uri.toString());
        final WeakReference weakReference = new WeakReference((T)context);
        final ab ab = new ab(uri, this);
        ab.setConnProvider(new OneLinkHttpTask.HttpsUrlConnectionProvider());
        if (ab.\u03b9()) {
            ab.\u03b9 = (ab.b)new ab.b() {
                private /* synthetic */ Map \u03b9;
                
                private void \u0131(final Map<String, String> map) {
                    if (weakReference.get() != null) {
                        AppsFlyerLibCore.\u01c3((Context)weakReference.get(), "deeplinkAttribution", new JSONObject((Map)map).toString());
                    }
                }
                
                @Override
                public final void \u0131(final String s) {
                    if (AppsFlyerLibCore.\u026a != null) {
                        this.\u0131(this.\u03b9);
                        AFLogger.afDebugLog("Calling onAttributionFailure with:\n".concat(String.valueOf(s)));
                        AppsFlyerLibCore.\u026a.onAttributionFailure(s);
                    }
                }
                
                @Override
                public final void \u0269(final Map<String, String> map) {
                    for (final String s : map.keySet()) {
                        this.\u03b9.put(s, map.get(s));
                    }
                    this.\u0131(this.\u03b9);
                    \u0269(this.\u03b9);
                }
            };
            AFExecutor.getInstance().getThreadPoolExecutor().execute(ab);
            return;
        }
        \u0269(AndroidUtils.\u03b9(context, hashMap, uri));
    }
    
    @Deprecated
    @Override
    public AppsFlyerLib init(final String s, final AppsFlyerConversionListener \u026a) {
        if (ai.\u0269 == null) {
            ai.\u0269 = new ai();
        }
        final ai \u0269 = ai.\u0269;
        String s2;
        if (\u026a == null) {
            s2 = "null";
        }
        else {
            s2 = "conversionDataListener";
        }
        \u0269.\u0131("public_api_call", "init", s, s2);
        AFLogger.\u01c3(String.format("Initializing AppsFlyer SDK: (v%s.%s)", "5.4.1", AppsFlyerLibCore.\u0269));
        this.\u027a = true;
        AppsFlyerProperties.getInstance().set("AppsFlyerKey", s);
        ah.\u01c3(s);
        AppsFlyerLibCore.\u026a = \u026a;
        return this;
    }
    
    @Override
    public AppsFlyerLib init(final String s, final AppsFlyerConversionListener appsFlyerConversionListener, final Context context) {
        if (context != null) {
            this.\u03f3 = (Application)context.getApplicationContext();
            if (GoogleReferrer.allow(this, context)) {
                if (this.\u029f == null) {
                    this.\u029f = new GoogleReferrer();
                    AFLogger.afDebugLog("Connecting to Install Referrer Library...");
                    this.\u029f.start(context, new Runnable() {
                        @Override
                        public final void run() {
                            try {
                                AFLogger.afDebugLog("Install Referrer collected locally");
                                AppsFlyerLibCore.\u0399(AppsFlyerLibCore.this);
                            }
                            finally {
                                final Throwable t;
                                AFLogger.afErrorLog(t.getMessage(), t);
                            }
                        }
                    });
                }
                else {
                    AFLogger.afWarnLog("GoogleReferrer instance already created");
                }
            }
            final SharedPreferences sharedPreferences = getSharedPreferences(context);
            if (this.getLaunchCounter(sharedPreferences, false) < 2) {
                (this.\u0442 = new HuaweiReferrer(new Runnable() {
                    @Override
                    public final void run() {
                        if (AppsFlyerLibCore.this.getLaunchCounter(sharedPreferences, false) == 1 && (!GoogleReferrer.allow(AppsFlyerLibCore.this, context) || sharedPreferences.getBoolean("newGPReferrerSent", false))) {
                            AppsFlyerLibCore.this.\u0399(new Attr().context(context));
                        }
                    }
                }, context)).start();
            }
            this.\u0441 = this.\u0131(context);
        }
        else {
            AFLogger.afWarnLog("init :: context is null, Google Install Referrer will be not initialized!");
        }
        return this.init(s, appsFlyerConversionListener);
    }
    
    @Override
    public boolean isPreInstalledApp(final Context context) {
        try {
            if ((context.getPackageManager().getApplicationInfo(context.getPackageName(), 0).flags & 0x1) != 0x0) {
                return true;
            }
        }
        catch (PackageManager$NameNotFoundException ex) {
            AFLogger.afErrorLog("Could not check if app is pre installed", (Throwable)ex);
        }
        return false;
    }
    
    @Override
    public boolean isTrackingStopped() {
        return this.\u027c;
    }
    
    @Override
    public void onPause(final Context context) {
        if (Foreground.listener != null) {
            Foreground.listener.onBecameBackground(context);
        }
    }
    
    @Override
    public void performOnAppAttribution(final Context context, final URI uri) {
        if (uri != null && !uri.toString().isEmpty()) {
            if (context != null) {
                AFDeepLinkManager.getInstance();
                AFDeepLinkManager.\u0399(context, new HashMap<String, Object>(), Uri.parse(uri.toString()));
                return;
            }
            final AppsFlyerConversionListener \u026a = AppsFlyerLibCore.\u026a;
            if (\u026a != null) {
                final StringBuilder sb = new StringBuilder("Context is \"");
                sb.append(context);
                sb.append("\"");
                \u026a.onAttributionFailure(sb.toString());
            }
        }
        else {
            final AppsFlyerConversionListener \u026a2 = AppsFlyerLibCore.\u026a;
            if (\u026a2 != null) {
                final StringBuilder sb2 = new StringBuilder("Link is \"");
                sb2.append(uri);
                sb2.append("\"");
                \u026a2.onAttributionFailure(sb2.toString());
            }
        }
    }
    
    @Override
    public void registerConversionListener(final Context context, final AppsFlyerConversionListener \u026a) {
        if (ai.\u0269 == null) {
            ai.\u0269 = new ai();
        }
        ai.\u0269.\u0131("public_api_call", "registerConversionListener", new String[0]);
        if (\u026a != null) {
            AppsFlyerLibCore.\u026a = \u026a;
        }
    }
    
    @Override
    public void registerValidatorListener(final Context context, final AppsFlyerInAppPurchaseValidatorListener \u01c3) {
        if (ai.\u0269 == null) {
            ai.\u0269 = new ai();
        }
        ai.\u0269.\u0131("public_api_call", "registerValidatorListener", new String[0]);
        AFLogger.afDebugLog("registerValidatorListener called");
        if (\u01c3 == null) {
            AFLogger.afDebugLog("registerValidatorListener null listener");
            return;
        }
        AppsFlyerLibCore.\u01c3 = \u01c3;
    }
    
    @Override
    public void reportTrackSession(final Context context) {
        if (ai.\u0269 == null) {
            ai.\u0269 = new ai();
        }
        ai.\u0269.\u0131("public_api_call", "reportTrackSession", new String[0]);
        if (ai.\u0269 == null) {
            ai.\u0269 = new ai();
        }
        ai.\u0269.\u03b9 = false;
        final AFEvent context2 = new InAppEvent().context(context);
        context2.\u04c0 = null;
        context2.\u0399 = null;
        this.\u01c3(context2);
    }
    
    @Override
    public void sendAdRevenue(final Context context, final Map<String, Object> \u03b9) {
        final AFEvent context2 = new AdRevenue().context(context);
        context2.\u0399 = \u03b9;
        final Context context3 = context2.context();
        final String url = ServerConfigHandler.getUrl(AppsFlyerLibCore.\u0406);
        final StringBuilder sb = new StringBuilder();
        sb.append(url);
        sb.append(context3.getPackageName());
        final String string = sb.toString();
        final SharedPreferences sharedPreferences = getSharedPreferences(context3);
        final int launchCounter = this.getLaunchCounter(sharedPreferences, false);
        final int \u03b92 = \u0399(sharedPreferences, "appsFlyerAdRevenueCount", true);
        final HashMap<String, Map<String, Object>> hashMap = new HashMap<String, Map<String, Object>>();
        hashMap.put("ad_network", context2.\u0399);
        hashMap.put("adrevenue_counter", (Map<String, Object>)\u03b92);
        final String string2 = AppsFlyerProperties.getInstance().getString("AppsFlyerKey");
        hashMap.put("af_key", (Map<String, Object>)string2);
        hashMap.put("launch_counter", (Map<String, Object>)launchCounter);
        hashMap.put("af_timestamp", (Map<String, Object>)Long.toString(new Date().getTime()));
        hashMap.put("uid", (Map<String, Object>)ae.\u0399(new WeakReference<Context>(context3)));
        final String string3 = AppsFlyerProperties.getInstance().getString("advertiserId");
        hashMap.put("advertiserIdEnabled", (Map<String, Object>)AppsFlyerProperties.getInstance().getString("advertiserIdEnabled"));
        if (string3 != null) {
            hashMap.put("advertiserId", (Map<String, Object>)string3);
        }
        hashMap.put("device", (Map<String, Object>)Build.DEVICE);
        \u01c3(context3, (Map<String, Object>)hashMap);
        try {
            final PackageInfo packageInfo = context3.getPackageManager().getPackageInfo(context3.getPackageName(), 0);
            hashMap.put("app_version_code", (Map<String, Object>)Integer.toString(packageInfo.versionCode));
            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd_HHmmssZ", Locale.US);
            final long firstInstallTime = packageInfo.firstInstallTime;
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            hashMap.put("install_date", (Map<String, Object>)simpleDateFormat.format(new Date(firstInstallTime)));
            String s;
            if ((s = sharedPreferences.getString("appsFlyerFirstInstall", (String)null)) == null) {
                s = \u01c3(simpleDateFormat, context3);
            }
            hashMap.put("first_launch_date", (Map<String, Object>)s);
        }
        finally {
            final Throwable t;
            AFLogger.afErrorLog("AdRevenue - Exception while collecting app version data ", t);
        }
        final AFEvent \u01c3 = context2.urlString(string).params(hashMap).\u01c3();
        \u01c3.\u0268 = launchCounter;
        \u01c3(AFExecutor.getInstance().\u03b9(), new a(\u01c3.key(string2), (byte)0), 1L, TimeUnit.MILLISECONDS);
    }
    
    @Deprecated
    @Override
    public void sendDeepLinkData(final Activity activity) {
        if (activity != null && activity.getIntent() != null) {
            if (ai.\u0269 == null) {
                ai.\u0269 = new ai();
            }
            final ai \u0269 = ai.\u0269;
            final String localClassName = activity.getLocalClassName();
            final StringBuilder sb = new StringBuilder("activity_intent_");
            sb.append(activity.getIntent().toString());
            \u0269.\u0131("public_api_call", "sendDeepLinkData", localClassName, sb.toString());
        }
        else if (activity != null) {
            if (ai.\u0269 == null) {
                ai.\u0269 = new ai();
            }
            ai.\u0269.\u0131("public_api_call", "sendDeepLinkData", activity.getLocalClassName(), "activity_intent_null");
        }
        else {
            if (ai.\u0269 == null) {
                ai.\u0269 = new ai();
            }
            ai.\u0269.\u0131("public_api_call", "sendDeepLinkData", "activity_null");
        }
        try {
            this.startTracking((Context)activity);
            final StringBuilder sb2 = new StringBuilder("getDeepLinkData with activity ");
            sb2.append(activity.getIntent().getDataString());
            AFLogger.afInfoLog(sb2.toString());
        }
        catch (Exception ex) {
            AFLogger.afInfoLog("getDeepLinkData Exception: ".concat(String.valueOf(ex)));
        }
    }
    
    @Override
    public void sendPushNotificationData(final Activity activity) {
        if (activity != null && activity.getIntent() != null) {
            if (ai.\u0269 == null) {
                ai.\u0269 = new ai();
            }
            final ai \u0269 = ai.\u0269;
            final String localClassName = activity.getLocalClassName();
            final StringBuilder sb = new StringBuilder("activity_intent_");
            sb.append(activity.getIntent().toString());
            \u0269.\u0131("public_api_call", "sendPushNotificationData", localClassName, sb.toString());
        }
        else if (activity != null) {
            if (ai.\u0269 == null) {
                ai.\u0269 = new ai();
            }
            ai.\u0269.\u0131("public_api_call", "sendPushNotificationData", activity.getLocalClassName(), "activity_intent_null");
        }
        else {
            if (ai.\u0269 == null) {
                ai.\u0269 = new ai();
            }
            ai.\u0269.\u0131("public_api_call", "sendPushNotificationData", "activity_null");
        }
        final String \u02692 = \u0269((Context)activity);
        this.\u0197 = \u02692;
        if (\u02692 != null) {
            final long currentTimeMillis = System.currentTimeMillis();
            long longValue = 0L;
            Label_0533: {
                if (this.\u019a == null) {
                    AFLogger.afInfoLog("pushes: initializing pushes history..");
                    this.\u019a = new ConcurrentHashMap<Long, String>();
                    longValue = currentTimeMillis;
                }
                else {
                    long n3;
                    try {
                        final long long1 = AppsFlyerProperties.getInstance().getLong("pushPayloadMaxAging", 1800000L);
                        Object iterator = this.\u019a.keySet().iterator();
                        final long n = currentTimeMillis;
                        while (true) {
                            longValue = n;
                            try {
                                if (!((Iterator)iterator).hasNext()) {
                                    break Label_0533;
                                }
                                final Long n2 = ((Iterator<Long>)iterator).next();
                                final JSONObject jsonObject = new JSONObject(this.\u0197);
                                final JSONObject jsonObject2 = new JSONObject((String)this.\u019a.get(n2));
                                if (jsonObject.get("pid").equals(jsonObject2.get("pid")) && jsonObject.get("c").equals(jsonObject2.get("c"))) {
                                    iterator = new StringBuilder("PushNotificationMeasurement: A previous payload with same PID and campaign was already acknowledged! (old: ");
                                    ((StringBuilder)iterator).append(jsonObject2);
                                    ((StringBuilder)iterator).append(", new: ");
                                    ((StringBuilder)iterator).append(jsonObject);
                                    ((StringBuilder)iterator).append(")");
                                    AFLogger.afInfoLog(iterator.toString());
                                    this.\u0197 = null;
                                    return;
                                }
                                if (currentTimeMillis - n2.longValue() > long1) {
                                    this.\u019a.remove(n2);
                                }
                                if (n2.longValue() <= n) {
                                    longValue = n2.longValue();
                                    continue;
                                }
                                continue;
                            }
                            finally {}
                        }
                    }
                    finally {
                        n3 = currentTimeMillis;
                    }
                    final StringBuilder sb2 = new StringBuilder("Error while handling push notification measurement: ");
                    final Throwable t;
                    sb2.append(t.getClass().getSimpleName());
                    AFLogger.afErrorLog(sb2.toString(), t);
                    longValue = n3;
                }
            }
            if (this.\u019a.size() == AppsFlyerProperties.getInstance().getInt("pushPayloadHistorySize", 2)) {
                final StringBuilder sb3 = new StringBuilder("pushes: removing oldest overflowing push (oldest push:");
                sb3.append(longValue);
                sb3.append(")");
                AFLogger.afInfoLog(sb3.toString());
                this.\u019a.remove(longValue);
            }
            this.\u019a.put(currentTimeMillis, this.\u0197);
            this.startTracking((Context)activity);
        }
    }
    
    @Override
    public void setAdditionalData(final HashMap<String, Object> hashMap) {
        if (hashMap != null) {
            if (ai.\u0269 == null) {
                ai.\u0269 = new ai();
            }
            ai.\u0269.\u0131("public_api_call", "setAdditionalData", hashMap.toString());
            AppsFlyerProperties.getInstance().setCustomData(new JSONObject((Map)hashMap).toString());
        }
    }
    
    @Override
    public void setAndroidIdData(final String \u03b9) {
        if (ai.\u0269 == null) {
            ai.\u0269 = new ai();
        }
        ai.\u0269.\u0131("public_api_call", "setAndroidIdData", \u03b9);
        this.\u03b9 = \u03b9;
    }
    
    @Override
    public void setAppId(final String s) {
        if (ai.\u0269 == null) {
            ai.\u0269 = new ai();
        }
        ai.\u0269.\u0131("public_api_call", "setAppId", s);
        AppsFlyerProperties.getInstance().set("appid", s);
    }
    
    @Override
    public void setAppInviteOneLink(final String s) {
        if (ai.\u0269 == null) {
            ai.\u0269 = new ai();
        }
        ai.\u0269.\u0131("public_api_call", "setAppInviteOneLink", s);
        AFLogger.afInfoLog("setAppInviteOneLink = ".concat(String.valueOf(s)));
        if (s == null || !s.equals(AppsFlyerProperties.getInstance().getString("oneLinkSlug"))) {
            AppsFlyerProperties.getInstance().remove("onelinkDomain");
            AppsFlyerProperties.getInstance().remove("onelinkVersion");
            AppsFlyerProperties.getInstance().remove("onelinkScheme");
        }
        AppsFlyerProperties.getInstance().set("oneLinkSlug", s);
    }
    
    @Override
    public void setCollectAndroidID(final boolean b) {
        if (ai.\u0269 == null) {
            ai.\u0269 = new ai();
        }
        ai.\u0269.\u0131("public_api_call", "setCollectAndroidID", String.valueOf(b));
        AppsFlyerProperties.getInstance().set("collectAndroidId", Boolean.toString(b));
        AppsFlyerProperties.getInstance().set("collectAndroidIdForceByUser", Boolean.toString(b));
    }
    
    @Override
    public void setCollectIMEI(final boolean b) {
        if (ai.\u0269 == null) {
            ai.\u0269 = new ai();
        }
        ai.\u0269.\u0131("public_api_call", "setCollectIMEI", String.valueOf(b));
        AppsFlyerProperties.getInstance().set("collectIMEI", Boolean.toString(b));
        AppsFlyerProperties.getInstance().set("collectIMEIForceByUser", Boolean.toString(b));
    }
    
    @Override
    public void setCollectOaid(final boolean b) {
        if (ai.\u0269 == null) {
            ai.\u0269 = new ai();
        }
        ai.\u0269.\u0131("public_api_call", "setCollectOaid", String.valueOf(b));
        AppsFlyerProperties.getInstance().set("collectOAID", Boolean.toString(b));
    }
    
    @Override
    public void setConsumeAFDeepLinks(final boolean b) {
        AppsFlyerProperties.getInstance().set("consumeAfDeepLink", b);
        if (ai.\u0269 == null) {
            ai.\u0269 = new ai();
        }
        ai.\u0269.\u0131("public_api_call", "setConsumeAFDeepLinks: ".concat(String.valueOf(b)), new String[0]);
    }
    
    @Override
    public void setCurrencyCode(final String s) {
        if (ai.\u0269 == null) {
            ai.\u0269 = new ai();
        }
        ai.\u0269.\u0131("public_api_call", "setCurrencyCode", s);
        AppsFlyerProperties.getInstance().set("currencyCode", s);
    }
    
    @Override
    public void setCustomerIdAndTrack(String s, final Context context) {
        if (context != null) {
            if (\u0131()) {
                this.setCustomerUserId(s);
                AppsFlyerProperties.getInstance().set("waitForCustomerId", false);
                final StringBuilder sb = new StringBuilder("CustomerUserId set: ");
                sb.append(s);
                sb.append(" - Initializing AppsFlyer Tacking");
                AFLogger.afInfoLog(sb.toString(), true);
                final String referrer = AppsFlyerProperties.getInstance().getReferrer(context);
                final String string = AppsFlyerProperties.getInstance().getString("AppsFlyerKey");
                if ((s = referrer) == null) {
                    s = "";
                }
                Intent intent;
                if (context instanceof Activity) {
                    intent = ((Activity)context).getIntent();
                }
                else {
                    intent = null;
                }
                this.\u01c3(context, string, s, intent);
                if (AppsFlyerProperties.getInstance().getString("afUninstallToken") != null) {
                    this.\u0269(context, AppsFlyerProperties.getInstance().getString("afUninstallToken"));
                }
                return;
            }
            this.setCustomerUserId(s);
            AFLogger.afInfoLog("waitForCustomerUserId is false; setting CustomerUserID: ".concat(String.valueOf(s)), true);
        }
    }
    
    @Override
    public void setCustomerUserId(final String s) {
        if (ai.\u0269 == null) {
            ai.\u0269 = new ai();
        }
        ai.\u0269.\u0131("public_api_call", "setCustomerUserId", s);
        AFLogger.afInfoLog("setCustomerUserId = ".concat(String.valueOf(s)));
        AppsFlyerProperties.getInstance().set("AppUserId", s);
    }
    
    @Override
    public void setDebugLog(final boolean b) {
        AFLogger.LogLevel logLevel;
        if (b) {
            logLevel = AFLogger.LogLevel.DEBUG;
        }
        else {
            logLevel = AFLogger.LogLevel.NONE;
        }
        this.setLogLevel(logLevel);
    }
    
    protected void setDeepLinkData(final Intent intent) {
        if (intent != null) {
            try {
                if ("android.intent.action.VIEW".equals(intent.getAction())) {
                    this.latestDeepLink = intent.getData();
                    final StringBuilder sb = new StringBuilder("Unity setDeepLinkData = ");
                    sb.append(this.latestDeepLink);
                    AFLogger.afDebugLog(sb.toString());
                }
            }
            finally {
                final Throwable t;
                AFLogger.afErrorLog("Exception while setting deeplink data (unity). ", t);
            }
        }
    }
    
    @Override
    public void setDeviceTrackingDisabled(final boolean b) {
        if (ai.\u0269 == null) {
            ai.\u0269 = new ai();
        }
        ai.\u0269.\u0131("public_api_call", "setDeviceTrackingDisabled", String.valueOf(b));
        AppsFlyerProperties.getInstance().set("deviceTrackingDisabled", b);
    }
    
    @Override
    public void setExtension(final String s) {
        if (ai.\u0269 == null) {
            ai.\u0269 = new ai();
        }
        ai.\u0269.\u0131("public_api_call", "setExtension", s);
        AppsFlyerProperties.getInstance().set("sdkExtension", s);
    }
    
    @Override
    public void setHost(final String s, final String s2) {
        if (s != null) {
            AppsFlyerProperties.getInstance().set("custom_host_prefix", s);
        }
        if (s2 != null && !s2.isEmpty()) {
            AppsFlyerProperties.getInstance().set("custom_host", s2);
            return;
        }
        AFLogger.afWarnLog("hostName cannot be null or empty");
    }
    
    @Deprecated
    @Override
    public void setHostName(final String s) {
        AppsFlyerProperties.getInstance().set("custom_host", s);
    }
    
    @Override
    public void setImeiData(final String \u03b9) {
        if (ai.\u0269 == null) {
            ai.\u0269 = new ai();
        }
        ai.\u0269.\u0131("public_api_call", "setImeiData", \u03b9);
        this.\u0399 = \u03b9;
    }
    
    @Override
    public void setIsUpdate(final boolean b) {
        if (ai.\u0269 == null) {
            ai.\u0269 = new ai();
        }
        ai.\u0269.\u0131("public_api_call", "setIsUpdate", String.valueOf(b));
        AppsFlyerProperties.getInstance().set("IS_UPDATE", b);
    }
    
    @Override
    public void setLogLevel(final AFLogger.LogLevel logLevel) {
        final boolean b = logLevel.getLevel() > AFLogger.LogLevel.NONE.getLevel();
        if (ai.\u0269 == null) {
            ai.\u0269 = new ai();
        }
        ai.\u0269.\u0131("public_api_call", "log", String.valueOf(b));
        AppsFlyerProperties.getInstance().set("shouldLog", b);
        AppsFlyerProperties.getInstance().set("logLevel", logLevel.getLevel());
    }
    
    @Override
    public void setMinTimeBetweenSessions(final int n) {
        this.\u027f = TimeUnit.SECONDS.toMillis(n);
    }
    
    @Override
    public void setOaidData(final String \u0268) {
        if (ai.\u0269 == null) {
            ai.\u0269 = new ai();
        }
        ai.\u0269.\u0131("public_api_call", "setOaidData", \u0268);
        this.\u0268 = \u0268;
    }
    
    @Override
    public void setOneLinkCustomDomain(final String... \u0131) {
        AFLogger.afDebugLog(String.format("setOneLinkCustomDomain %s", Arrays.toString(\u0131)));
        AFDeepLinkManager.\u0131 = \u0131;
    }
    
    @Override
    public void setOutOfStore(String lowerCase) {
        if (lowerCase != null) {
            lowerCase = lowerCase.toLowerCase();
            AppsFlyerProperties.getInstance().set("api_store_value", lowerCase);
            AFLogger.afInfoLog("Store API set with value: ".concat(String.valueOf(lowerCase)), true);
            return;
        }
        AFLogger.\u0399("Cannot set setOutOfStore with null");
    }
    
    @Override
    public void setPhoneNumber(final String s) {
        this.\u0445 = z.\u0269(s);
    }
    
    @Override
    public void setPluginDeepLinkData(final Intent deepLinkData) {
        this.setDeepLinkData(deepLinkData);
    }
    
    @Override
    public void setPreinstallAttribution(String string, final String s, final String s2) {
        AFLogger.afDebugLog("setPreinstallAttribution API called");
        final JSONObject jsonObject = new JSONObject();
    Label_0063_Outer:
        while (true) {
            if (string != null) {
                while (true) {
                    try {
                        jsonObject.put("pid", (Object)string);
                        if (s != null) {
                            jsonObject.put("c", (Object)s);
                        }
                        if (s2 != null) {
                            jsonObject.put("af_siteid", (Object)s2);
                        }
                        while (true) {
                            if (jsonObject.has("pid")) {
                                string = jsonObject.toString();
                                AppsFlyerProperties.getInstance().set("preInstallName", string);
                                return;
                            }
                            AFLogger.afWarnLog("Cannot set preinstall attribution data without a media source");
                            return;
                            final Throwable t;
                            AFLogger.afErrorLog(t.getMessage(), t);
                            continue Label_0063_Outer;
                        }
                    }
                    catch (JSONException ex) {}
                    final JSONException ex;
                    final Throwable t = (Throwable)ex;
                    continue;
                }
            }
            continue;
        }
    }
    
    @Override
    public void setResolveDeepLinkURLs(final String... \u01c3) {
        AFLogger.afDebugLog(String.format("setResolveDeepLinkURLs %s", Arrays.toString(\u01c3)));
        AFDeepLinkManager.\u01c3 = \u01c3;
    }
    
    @Override
    public void setSharingFilter(String... \u0491) {
        if (\u0491 == null) {
            return;
        }
        if (Arrays.equals(this.\u0491, new String[] { "all" })) {
            return;
        }
        final Pattern compile = Pattern.compile("[\\d\\w_]{1,45}");
        final ArrayList<String> list = new ArrayList<String>();
        for (int length = \u0491.length, i = 0; i < length; ++i) {
            final String s = \u0491[i];
            if (s != null && compile.matcher(s).matches()) {
                list.add(s);
            }
            else {
                AFLogger.afWarnLog("Invalid partner name :".concat(String.valueOf(s)));
            }
        }
        if (!list.isEmpty()) {
            \u0491 = list.toArray(new String[0]);
        }
        else {
            \u0491 = null;
        }
        this.\u0491 = \u0491;
    }
    
    @Override
    public void setSharingFilterForAllPartners() {
        this.\u0491 = new String[] { "all" };
    }
    
    @Override
    public void setUserEmails(final AppsFlyerProperties.EmailsCryptType emailsCryptType, final String... array) {
        final ArrayList<Object> list = new ArrayList<Object>(array.length + 1);
        list.add(emailsCryptType.toString());
        list.addAll(Arrays.asList(array));
        if (ai.\u0269 == null) {
            ai.\u0269 = new ai();
        }
        ai.\u0269.\u0131("public_api_call", "setUserEmails", (String[])list.toArray(new String[array.length + 1]));
        AppsFlyerProperties.getInstance().set("userEmailsCryptType", emailsCryptType.getValue());
        final HashMap<String, ArrayList<String>> hashMap = new HashMap<String, ArrayList<String>>();
        String s = null;
        final ArrayList<String> list2 = new ArrayList<String>();
        for (int length = array.length, i = 0; i < length; ++i) {
            final String s2 = array[i];
            if (AppsFlyerLibCore$10.\u03b9[emailsCryptType.ordinal()] != 2) {
                list2.add(z.\u0269(s2));
                s = "sha256_el_arr";
            }
            else {
                list2.add(s2);
                s = "plain_el_arr";
            }
        }
        hashMap.put(s, list2);
        AppsFlyerProperties.getInstance().setUserEmails(new JSONObject((Map)hashMap).toString());
    }
    
    @Override
    public void setUserEmails(final String... array) {
        if (ai.\u0269 == null) {
            ai.\u0269 = new ai();
        }
        ai.\u0269.\u0131("public_api_call", "setUserEmails", array);
        this.setUserEmails(AppsFlyerProperties.EmailsCryptType.NONE, array);
    }
    
    @Override
    public void startTracking(final Context context) {
        this.startTracking(context, null);
    }
    
    @Override
    public void startTracking(final Context context, final String s) {
        this.startTracking(context, s, null);
    }
    
    @Override
    public void startTracking(final Context context, final String s, final AppsFlyerTrackingRequestListener appsFlyerTrackingRequestListener) {
        if (!this.\u027a) {
            AFLogger.afWarnLog("ERROR: AppsFlyer SDK is not initialized! The API call 'startTracking()' must be called after the 'init(String, AppsFlyerConversionListener)' API method, which should be called on the Application's onCreate.");
            if (s == null) {
                return;
            }
        }
        if (Foreground.listener != null) {
            return;
        }
        this.\u03f3 = (Application)context.getApplicationContext();
        if (ai.\u0269 == null) {
            ai.\u0269 = new ai();
        }
        ai.\u0269.\u0131("public_api_call", "startTracking", s);
        AFLogger.afInfoLog(String.format("Starting AppsFlyer Tracking: (v%s.%s)", "5.4.1", AppsFlyerLibCore.\u0269));
        final StringBuilder sb = new StringBuilder("Build Number: ");
        sb.append(AppsFlyerLibCore.\u0269);
        AFLogger.afInfoLog(sb.toString());
        AppsFlyerProperties.getInstance().loadProperties(((Context)this.\u03f3).getApplicationContext());
        if (!TextUtils.isEmpty((CharSequence)s)) {
            AppsFlyerProperties.getInstance().set("AppsFlyerKey", s);
            ah.\u01c3(s);
        }
        else if (TextUtils.isEmpty((CharSequence)AppsFlyerProperties.getInstance().getString("AppsFlyerKey"))) {
            AFLogger.afWarnLog("ERROR: AppsFlyer SDK is not initialized! You must provide AppsFlyer Dev-Key either in the 'init' API method (should be called on Application's onCreate),or in the startTracking API method (should be called on Activity's onCreate).");
            return;
        }
        final Context baseContext = ((ContextWrapper)this.\u03f3).getBaseContext();
        try {
            if ((baseContext.getPackageManager().getPackageInfo(baseContext.getPackageName(), 0).applicationInfo.flags & 0x8000) != 0x0) {
                if (baseContext.getResources().getIdentifier("appsflyer_backup_rules", "xml", baseContext.getPackageName()) != 0) {
                    AFLogger.afInfoLog("appsflyer_backup_rules.xml detected, using AppsFlyer defined backup rules for AppsFlyer SDK data", true);
                }
                else {
                    AFLogger.\u0399("'allowBackup' is set to true; appsflyer_backup_rules.xml not detected.\nAppsFlyer shared preferences should be excluded from auto backup by adding: <exclude domain=\"sharedpref\" path=\"appsflyer-data\"/> to the Application's <full-backup-content> rules");
                }
            }
        }
        catch (Exception ex) {
            final StringBuilder sb2 = new StringBuilder("checkBackupRules Exception: ");
            sb2.append(ex.toString());
            AFLogger.afRDLog(sb2.toString());
        }
        if (this.\u0408) {
            final Context applicationContext = ((Context)this.\u03f3).getApplicationContext();
            this.\u037b = new HashMap<String, Object>();
            final AFFacebookDeferredDeeplink.AppLinkFetchEvents appLinkFetchEvents = new AFFacebookDeferredDeeplink.AppLinkFetchEvents() {
                private /* synthetic */ long \u03b9 = System.currentTimeMillis();
                
                @Override
                public final void onAppLinkFetchFailed(final String s) {
                    AppsFlyerLibCore.this.\u037b.put("error", s);
                }
                
                @Override
                public final void onAppLinkFetchFinished(final String s, final String s2, final String s3) {
                    if (s != null) {
                        AFLogger.afInfoLog("Facebook Deferred AppLink data received: ".concat(String.valueOf(s)));
                        AppsFlyerLibCore.this.\u037b.put("link", s);
                        if (s2 != null) {
                            AppsFlyerLibCore.this.\u037b.put("target_url", s2);
                        }
                        if (s3 != null) {
                            final HashMap<String, HashMap<String, String>> hashMap = new HashMap<String, HashMap<String, String>>();
                            final HashMap<String, String> hashMap2 = new HashMap<String, String>();
                            hashMap2.put("promo_code", s3);
                            hashMap.put("deeplink_context", hashMap2);
                            AppsFlyerLibCore.this.\u037b.put("extras", hashMap);
                        }
                    }
                    else {
                        AppsFlyerLibCore.this.\u037b.put("link", "");
                    }
                    AppsFlyerLibCore.this.\u037b.put("ttr", String.valueOf(System.currentTimeMillis() - this.\u03b9));
                }
            };
            try {
                Class.forName("com.facebook.FacebookSdk").getMethod("sdkInitialize", Context.class).invoke(null, applicationContext);
                final Class<?> forName = Class.forName("com.facebook.applinks.AppLinkData");
                final Class<?> forName2 = Class.forName("com.facebook.applinks.AppLinkData$CompletionHandler");
                final Method method = forName.getMethod("fetchDeferredAppLinkData", Context.class, String.class, forName2);
                final Object proxyInstance = Proxy.newProxyInstance(forName2.getClassLoader(), new Class[] { forName2 }, new InvocationHandler() {
                    private /* synthetic */ Class \u0131 = forName;
                    private /* synthetic */ AppLinkFetchEvents \u0269 = appLinkFetchEvents;
                    
                    @Override
                    public final Object invoke(Object cast, final Method method, final Object[] array) throws Throwable {
                        if (method.getName().equals("onDeferredAppLinkDataFetched")) {
                            if (array[0] != null) {
                                cast = this.\u0131.cast(array[0]);
                                final Bundle bundle = Bundle.class.cast(this.\u0131.getMethod("getArgumentBundle", (Class[])new Class[0]).invoke(cast, new Object[0]));
                                String string = null;
                                String string2 = null;
                                String string3 = null;
                                Label_0127: {
                                    if (bundle != null) {
                                        string = bundle.getString("com.facebook.platform.APPLINK_NATIVE_URL");
                                        string2 = bundle.getString("target_url");
                                        final Bundle bundle2 = bundle.getBundle("extras");
                                        if (bundle2 != null) {
                                            final Bundle bundle3 = bundle2.getBundle("deeplink_context");
                                            if (bundle3 != null) {
                                                string3 = bundle3.getString("promo_code");
                                                break Label_0127;
                                            }
                                        }
                                        string3 = null;
                                    }
                                    else {
                                        final String s = null;
                                        string2 = (string = s);
                                        string3 = s;
                                    }
                                }
                                final AppLinkFetchEvents \u0269 = this.\u0269;
                                if (\u0269 != null) {
                                    \u0269.onAppLinkFetchFinished(string, string2, string3);
                                    return null;
                                }
                            }
                            else {
                                final AppLinkFetchEvents \u02692 = this.\u0269;
                                if (\u02692 != null) {
                                    \u02692.onAppLinkFetchFinished(null, null, null);
                                }
                            }
                            return null;
                        }
                        final AppLinkFetchEvents \u02693 = this.\u0269;
                        if (\u02693 != null) {
                            \u02693.onAppLinkFetchFailed("onDeferredAppLinkDataFetched invocation failed");
                        }
                        return null;
                    }
                });
                final String string = applicationContext.getString(applicationContext.getResources().getIdentifier("facebook_app_id", "string", applicationContext.getPackageName()));
                if (TextUtils.isEmpty((CharSequence)string)) {
                    ((AFFacebookDeferredDeeplink.AppLinkFetchEvents)appLinkFetchEvents).onAppLinkFetchFailed("Facebook app id not defined in resources");
                }
                else {
                    try {
                        method.invoke(null, applicationContext, string, proxyInstance);
                    }
                    catch (InvocationTargetException | ClassNotFoundException ex2) {
                        final Object o;
                        ((AFFacebookDeferredDeeplink.AppLinkFetchEvents)appLinkFetchEvents).onAppLinkFetchFailed(o.toString());
                    }
                }
            }
            catch (NoSuchMethodException ex3) {}
            catch (InvocationTargetException ex4) {}
            catch (ClassNotFoundException ex5) {}
            catch (IllegalAccessException ex6) {}
        }
        Foreground.\u0269(context, (Foreground.Listener)new Foreground.Listener() {
            @Override
            public final void onBecameBackground(final Context context) {
                AFLogger.afInfoLog("onBecameBackground");
                AppsFlyerLibCore.this.\u017f = System.currentTimeMillis();
                AFLogger.afInfoLog("callStatsBackground background call");
                AppsFlyerLibCore.this.\u0269(new WeakReference<Context>(context));
                if (ai.\u0269 == null) {
                    ai.\u0269 = new ai();
                }
                final ai \u0269 = ai.\u0269;
                if (\u0269.\u0406()) {
                    \u0269.\u03b9();
                    if (context != null) {
                        final String packageName = context.getPackageName();
                        final PackageManager packageManager = context.getPackageManager();
                        try {
                            if (ai.\u0269 == null) {
                                ai.\u0269 = new ai();
                            }
                            ai.\u0269.\u0399(packageName, packageManager);
                            if (ai.\u0269 == null) {
                                ai.\u0269 = new ai();
                            }
                            final BackgroundEvent trackingStopped = new ProxyEvent().body(ai.\u0269.\u0399()).trackingStopped(AppsFlyerLib.getInstance().isTrackingStopped());
                            final StringBuilder sb = new StringBuilder();
                            sb.append(ServerConfigHandler.getUrl("https://%smonitorsdk.%s/remote-debug?app_id="));
                            sb.append(packageName);
                            new Thread(new ad((BackgroundEvent)trackingStopped.urlString(sb.toString()))).start();
                        }
                        finally {}
                    }
                    \u0269.\u0269();
                }
                else {
                    AFLogger.afDebugLog("RD status is OFF");
                }
                final AFExecutor instance = AFExecutor.getInstance();
                try {
                    AFExecutor.\u0269(instance.\u0269);
                    if (instance.\u0399 instanceof ThreadPoolExecutor) {
                        AFExecutor.\u0269((ExecutorService)instance.\u0399);
                    }
                }
                finally {
                    final Throwable t;
                    AFLogger.afErrorLog("failed to stop Executors", t);
                }
                final AFSensorManager \u02692 = AFSensorManager.\u0269(context);
                \u02692.\u0131.post(\u02692.\u0406);
            }
            
            @Override
            public final void onBecameForeground(final Activity activity) {
                if (AppsFlyerLibCore.this.getLaunchCounter(AppsFlyerLibCore.getSharedPreferences((Context)activity), false) < 2) {
                    final AFSensorManager \u0269 = AFSensorManager.\u0269((Context)activity);
                    \u0269.\u0131.post(\u0269.\u0406);
                    \u0269.\u0131.post(\u0269.\u0279);
                }
                AFLogger.afInfoLog("onBecameForeground");
                AppsFlyerLibCore.this.\u0285 = System.currentTimeMillis();
                final AppsFlyerLibCore \u03b9 = AppsFlyerLibCore.this;
                final AFEvent key = new Launch().context((Context)activity).key(s);
                key.\u0269 = appsFlyerTrackingRequestListener;
                \u03b9.\u01c3(key);
                AFLogger.resetDeltaTime();
            }
        });
    }
    
    @Override
    public void stopTracking(final boolean \u027c, final Context context) {
        this.\u027c = \u027c;
        aa.\u01c3();
        try {
            final File \u03b9 = aa.\u03b9(context);
            if (!\u03b9.exists()) {
                \u03b9.mkdir();
            }
            else {
                final File[] listFiles = \u03b9.listFiles();
                for (int length = listFiles.length, i = 0; i < length; ++i) {
                    final File file = listFiles[i];
                    final StringBuilder sb = new StringBuilder("Found cached request");
                    sb.append(file.getName());
                    Log.i("AppsFlyer_5.4.1", sb.toString());
                    aa.\u01c3(aa.\u0131(file).\u01c3, context);
                }
            }
        }
        catch (Exception ex) {
            Log.i("AppsFlyer_5.4.1", "Could not cache request");
        }
        if (this.\u027c) {
            final SharedPreferences$Editor edit = getSharedPreferences(context).edit();
            edit.putBoolean("is_stop_tracking_used", true);
            edit.apply();
        }
    }
    
    @Deprecated
    @Override
    public void trackAppLaunch(final Context context, final String s) {
        if (GoogleReferrer.allow(this, context)) {
            if (this.\u029f == null) {
                this.\u029f = new GoogleReferrer();
                AFLogger.afDebugLog("Connecting to Install Referrer Library...");
                this.\u029f.start(context, new Runnable() {
                    @Override
                    public final void run() {
                        try {
                            AFLogger.afDebugLog("Install Referrer collected locally");
                            AppsFlyerLibCore.\u0399(AppsFlyerLibCore.this);
                        }
                        finally {
                            final Throwable t;
                            AFLogger.afErrorLog(t.getMessage(), t);
                        }
                    }
                });
            }
            else {
                AFLogger.afWarnLog("GoogleReferrer instance already created");
            }
        }
        this.\u01c3(context, s, "", null);
    }
    
    @Override
    public void trackEvent(final Context context, final String s, final Map<String, Object> map) {
        this.trackEvent(context, s, map, null);
    }
    
    @Override
    public void trackEvent(final Context context, final String \u04cf, final Map<String, Object> map, final AppsFlyerTrackingRequestListener \u0269) {
        final AFEvent context2 = new InAppEvent().context(context);
        context2.\u04c0 = \u04cf;
        Map<String, Object> \u03b9;
        if (map == null) {
            \u03b9 = null;
        }
        else {
            \u03b9 = new HashMap<String, Object>(map);
        }
        context2.\u0399 = \u03b9;
        context2.\u0269 = \u0269;
        if (ai.\u0269 == null) {
            ai.\u0269 = new ai();
        }
        final ai \u02692 = ai.\u0269;
        Map<String, Object> \u03b92;
        if (context2.\u0399 == null) {
            \u03b92 = new HashMap<String, Object>();
        }
        else {
            \u03b92 = context2.\u0399;
        }
        \u02692.\u0131("public_api_call", "trackEvent", \u04cf, new JSONObject((Map)\u03b92).toString());
        if (\u04cf != null) {
            final AFSensorManager \u02693 = AFSensorManager.\u0269(context);
            final long currentTimeMillis = System.currentTimeMillis();
            Label_0214: {
                if (\u02693.\u027e != 0L) {
                    ++\u02693.\u0196;
                    if (\u02693.\u027e - currentTimeMillis >= 500L) {
                        break Label_0214;
                    }
                    \u02693.\u0131.removeCallbacks(\u02693.\u0456);
                }
                else {
                    \u02693.\u0131.post(\u02693.\u0406);
                }
                \u02693.\u0131.post(\u02693.\u0279);
            }
            \u02693.\u027e = currentTimeMillis;
        }
        this.\u01c3(context2);
    }
    
    @Override
    public void trackLocation(final Context context, final double n, final double n2) {
        if (ai.\u0269 == null) {
            ai.\u0269 = new ai();
        }
        ai.\u0269.\u0131("public_api_call", "trackLocation", String.valueOf(n), String.valueOf(n2));
        final HashMap<String, String> \u03b9 = new HashMap<String, String>();
        \u03b9.put("af_long", Double.toString(n2));
        \u03b9.put("af_lat", Double.toString(n));
        final AFEvent context2 = new InAppEvent().context(context);
        context2.\u04c0 = "af_location_coordinates";
        context2.\u0399 = (Map<String, Object>)\u03b9;
        this.\u01c3(context2);
    }
    
    @Override
    public void unregisterConversionListener() {
        if (ai.\u0269 == null) {
            ai.\u0269 = new ai();
        }
        ai.\u0269.\u0131("public_api_call", "unregisterConversionListener", new String[0]);
        AppsFlyerLibCore.\u026a = null;
    }
    
    @Override
    public void updateServerUninstallToken(final Context context, final String s) {
        af.\u0131(context, s);
    }
    
    @Override
    public void validateAndTrackInAppPurchase(final Context context, final String s, final String s2, final String s3, final String s4, final String s5, final Map<String, String> map) {
        if (ai.\u0269 == null) {
            ai.\u0269 = new ai();
        }
        final ai \u0269 = ai.\u0269;
        String string;
        if (map == null) {
            string = "";
        }
        else {
            string = map.toString();
        }
        \u0269.\u0131("public_api_call", "validateAndTrackInAppPurchase", s, s2, s3, s4, s5, string);
        if (!this.isTrackingStopped()) {
            final StringBuilder sb = new StringBuilder("Validate in app called with parameters: ");
            sb.append(s3);
            sb.append(" ");
            sb.append(s4);
            sb.append(" ");
            sb.append(s5);
            AFLogger.afInfoLog(sb.toString());
        }
        if (s != null && s4 != null && s2 != null && s5 != null && s3 != null) {
            final Context applicationContext = context.getApplicationContext();
            final String string2 = AppsFlyerProperties.getInstance().getString("AppsFlyerKey");
            Intent intent;
            if (context instanceof Activity) {
                intent = ((Activity)context).getIntent();
            }
            else {
                intent = null;
            }
            new Thread(new x(applicationContext, string2, s, s2, s3, s4, s5, map, intent)).start();
            return;
        }
        final AppsFlyerInAppPurchaseValidatorListener \u01c3 = AppsFlyerLibCore.\u01c3;
        if (\u01c3 != null) {
            \u01c3.onValidateInAppFailure("Please provide purchase parameters");
        }
    }
    
    @Override
    public void waitForCustomerUserId(final boolean b) {
        AFLogger.afInfoLog("initAfterCustomerUserID: ".concat(String.valueOf(b)), true);
        AppsFlyerProperties.getInstance().set("waitForCustomerId", b);
    }
    
    public final String \u01c3(final HttpURLConnection p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: dup            
        //     4: invokespecial   java/lang/StringBuilder.<init>:()V
        //     7: astore          6
        //     9: aconst_null    
        //    10: astore          4
        //    12: aload_1        
        //    13: invokevirtual   java/net/HttpURLConnection.getErrorStream:()Ljava/io/InputStream;
        //    16: astore_3       
        //    17: aload_3        
        //    18: astore_2       
        //    19: aload_3        
        //    20: ifnonnull       28
        //    23: aload_1        
        //    24: invokevirtual   java/net/URLConnection.getInputStream:()Ljava/io/InputStream;
        //    27: astore_2       
        //    28: new             Ljava/io/InputStreamReader;
        //    31: dup            
        //    32: aload_2        
        //    33: invokespecial   java/io/InputStreamReader.<init>:(Ljava/io/InputStream;)V
        //    36: astore_2       
        //    37: new             Ljava/io/BufferedReader;
        //    40: dup            
        //    41: aload_2        
        //    42: invokespecial   java/io/BufferedReader.<init>:(Ljava/io/Reader;)V
        //    45: astore          5
        //    47: aload           5
        //    49: invokevirtual   java/io/BufferedReader.readLine:()Ljava/lang/String;
        //    52: astore_3       
        //    53: aload_3        
        //    54: ifnull          75
        //    57: aload           6
        //    59: aload_3        
        //    60: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    63: pop            
        //    64: aload           6
        //    66: bipush          10
        //    68: invokevirtual   java/lang/StringBuilder.append:(C)Ljava/lang/StringBuilder;
        //    71: pop            
        //    72: goto            47
        //    75: aload           5
        //    77: invokevirtual   java/io/Reader.close:()V
        //    80: goto            149
        //    83: astore_1       
        //    84: goto            153
        //    87: astore_3       
        //    88: aload           5
        //    90: astore          4
        //    92: goto            102
        //    95: astore_3       
        //    96: goto            102
        //    99: astore_3       
        //   100: aconst_null    
        //   101: astore_2       
        //   102: new             Ljava/lang/StringBuilder;
        //   105: dup            
        //   106: ldc_w           "Could not read connection response from: "
        //   109: invokespecial   java/lang/StringBuilder.<init>:(Ljava/lang/String;)V
        //   112: astore          5
        //   114: aload           5
        //   116: aload_1        
        //   117: invokevirtual   java/net/URLConnection.getURL:()Ljava/net/URL;
        //   120: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
        //   123: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   126: pop            
        //   127: aload           5
        //   129: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
        //   132: aload_3        
        //   133: invokestatic    com/appsflyer/AFLogger.afErrorLog:(Ljava/lang/String;Ljava/lang/Throwable;)V
        //   136: aload           4
        //   138: ifnull          231
        //   141: aload           4
        //   143: invokevirtual   java/io/Reader.close:()V
        //   146: goto            231
        //   149: aload_2        
        //   150: invokevirtual   java/io/Reader.close:()V
        //   153: aload           6
        //   155: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
        //   158: astore_1       
        //   159: new             Lorg/json/JSONObject;
        //   162: dup            
        //   163: aload_1        
        //   164: invokespecial   org/json/JSONObject.<init>:(Ljava/lang/String;)V
        //   167: pop            
        //   168: aload_1        
        //   169: areturn        
        //   170: astore_2       
        //   171: new             Lorg/json/JSONObject;
        //   174: dup            
        //   175: invokespecial   org/json/JSONObject.<init>:()V
        //   178: astore_2       
        //   179: aload_2        
        //   180: ldc_w           "string_response"
        //   183: aload_1        
        //   184: invokevirtual   org/json/JSONObject.put:(Ljava/lang/String;Ljava/lang/Object;)Lorg/json/JSONObject;
        //   187: pop            
        //   188: aload_2        
        //   189: invokevirtual   org/json/JSONObject.toString:()Ljava/lang/String;
        //   192: astore_1       
        //   193: aload_1        
        //   194: areturn        
        //   195: astore_1       
        //   196: new             Lorg/json/JSONObject;
        //   199: dup            
        //   200: invokespecial   org/json/JSONObject.<init>:()V
        //   203: invokevirtual   org/json/JSONObject.toString:()Ljava/lang/String;
        //   206: areturn        
        //   207: astore_1       
        //   208: aload           4
        //   210: ifnull          221
        //   213: aload           4
        //   215: invokevirtual   java/io/Reader.close:()V
        //   218: goto            221
        //   221: aload_2        
        //   222: ifnull          229
        //   225: aload_2        
        //   226: invokevirtual   java/io/Reader.close:()V
        //   229: aload_1        
        //   230: athrow         
        //   231: aload_2        
        //   232: ifnull          153
        //   235: goto            149
        //   238: astore_2       
        //   239: goto            229
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                    
        //  -----  -----  -----  -----  ------------------------
        //  12     17     99     102    Any
        //  23     28     99     102    Any
        //  28     37     99     102    Any
        //  37     47     95     99     Any
        //  47     53     87     95     Any
        //  57     72     87     95     Any
        //  75     80     83     87     Any
        //  102    136    207    231    Any
        //  141    146    83     87     Any
        //  149    153    83     87     Any
        //  159    168    170    207    Lorg/json/JSONException;
        //  179    193    195    207    Lorg/json/JSONException;
        //  213    218    238    242    Any
        //  225    229    238    242    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0221:
        //     at com.strobel.decompiler.ast.Error.expressionLinkedFromMultipleLocations(Error.java:27)
        //     at com.strobel.decompiler.ast.AstOptimizer.mergeDisparateObjectInitializations(AstOptimizer.java:2596)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:235)
        //     at com.strobel.decompiler.ast.AstOptimizer.optimize(AstOptimizer.java:42)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:214)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at us.deathmarine.luyten.FileSaver.doSaveJarDecompiled(FileSaver.java:192)
        //     at us.deathmarine.luyten.FileSaver.access$300(FileSaver.java:45)
        //     at us.deathmarine.luyten.FileSaver$4.run(FileSaver.java:112)
        //     at java.lang.Thread.run(Unknown Source)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    final void \u01c3(final Context context, final Intent \u01c3) {
        if (\u01c3.getStringExtra("appsflyer_preinstall") != null) {
            getInstance();
            final String stringExtra = \u01c3.getStringExtra("appsflyer_preinstall");
            try {
                if (new JSONObject(stringExtra).has("pid")) {
                    AppsFlyerProperties.getInstance().set("preInstallName", stringExtra);
                }
                else {
                    AFLogger.afWarnLog("Cannot set preinstall attribution data without a media source");
                }
            }
            catch (JSONException ex) {
                AFLogger.afErrorLog("Error parsing JSON for preinstall", (Throwable)ex);
            }
        }
        AFLogger.afInfoLog("****** onReceive called *******");
        AppsFlyerProperties.getInstance().setOnReceiveCalled();
        final String stringExtra2 = \u01c3.getStringExtra("referrer");
        AFLogger.afInfoLog("Play store referrer: ".concat(String.valueOf(stringExtra2)));
        if (stringExtra2 != null) {
            final SharedPreferences$Editor edit = getSharedPreferences(context).edit();
            edit.putString("referrer", stringExtra2);
            edit.apply();
            AppsFlyerProperties.getInstance().setReferrer(stringExtra2);
            if (AppsFlyerProperties.getInstance().isFirstLaunchCalled()) {
                AFLogger.afInfoLog("onReceive: isLaunchCalled");
                final AFEvent weakContext = new BackgroundReferrerLaunch().context(context).\u01c3().weakContext();
                weakContext.\u0196 = stringExtra2;
                weakContext.\u01c3 = \u01c3;
                if (stringExtra2 != null && stringExtra2.length() > 5 && this.\u0399(weakContext, getSharedPreferences(context))) {
                    \u01c3(AFExecutor.getInstance().\u03b9(), new e(weakContext, (byte)0), 5L, TimeUnit.MILLISECONDS);
                }
            }
        }
    }
    
    final void \u01c3(final AFEvent afEvent) {
        final Context context = afEvent.context();
        Intent intent = null;
        String string = null;
        Label_0059: {
            Intent intent2;
            if (context instanceof Activity) {
                final Activity activity = (Activity)context;
                intent = activity.getIntent();
                final Uri referrer = ActivityCompat.getReferrer(activity);
                intent2 = intent;
                if (referrer != null) {
                    string = referrer.toString();
                    break Label_0059;
                }
            }
            else {
                intent2 = null;
            }
            final String s = "";
            intent = intent2;
            string = s;
        }
        if (AppsFlyerProperties.getInstance().getString("AppsFlyerKey") == null) {
            AFLogger.afWarnLog("[TrackEvent/Launch] AppsFlyer's SDK cannot send any event without providing DevKey.");
            return;
        }
        String referrer2;
        if ((referrer2 = AppsFlyerProperties.getInstance().getReferrer(context)) == null) {
            referrer2 = "";
        }
        afEvent.\u0196 = referrer2;
        afEvent.\u01c3 = intent;
        afEvent.\u0406 = string;
        this.\u0131(afEvent);
    }
    
    public final Map<String, Object> \u0269(final AFEvent p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokevirtual   com/appsflyer/AFEvent.context:()Landroid/content/Context;
        //     4: astore          13
        //     6: aload_1        
        //     7: invokevirtual   com/appsflyer/AFEvent.key:()Ljava/lang/String;
        //    10: astore          19
        //    12: aload_1        
        //    13: getfield        com/appsflyer/AFEvent.\u04c0:Ljava/lang/String;
        //    16: astore          11
        //    18: aload_1        
        //    19: getfield        com/appsflyer/AFEvent.\u0399:Ljava/util/Map;
        //    22: ifnonnull       37
        //    25: new             Ljava/util/HashMap;
        //    28: dup            
        //    29: invokespecial   java/util/HashMap.<init>:()V
        //    32: astore          10
        //    34: goto            43
        //    37: aload_1        
        //    38: getfield        com/appsflyer/AFEvent.\u0399:Ljava/util/Map;
        //    41: astore          10
        //    43: new             Lorg/json/JSONObject;
        //    46: dup            
        //    47: aload           10
        //    49: invokespecial   org/json/JSONObject.<init>:(Ljava/util/Map;)V
        //    52: invokevirtual   org/json/JSONObject.toString:()Ljava/lang/String;
        //    55: astore          18
        //    57: aload_1        
        //    58: getfield        com/appsflyer/AFEvent.\u0196:Ljava/lang/String;
        //    61: astore          10
        //    63: aload           13
        //    65: invokestatic    com/appsflyer/AppsFlyerLibCore.getSharedPreferences:(Landroid/content/Context;)Landroid/content/SharedPreferences;
        //    68: astore          14
        //    70: aload_1        
        //    71: invokevirtual   com/appsflyer/AFEvent.\u0399:()Z
        //    74: istore          4
        //    76: aload_1        
        //    77: invokevirtual   com/appsflyer/AFEvent.intent:()Landroid/content/Intent;
        //    80: astore          16
        //    82: aload_1        
        //    83: getfield        com/appsflyer/AFEvent.\u0406:Ljava/lang/String;
        //    86: astore          17
        //    88: new             Ljava/util/HashMap;
        //    91: dup            
        //    92: invokespecial   java/util/HashMap.<init>:()V
        //    95: astore          12
        //    97: aload           13
        //    99: aload           12
        //   101: invokestatic    com/appsflyer/internal/v.\u03b9:(Landroid/content/Context;Ljava/util/Map;)V
        //   104: new             Ljava/util/Date;
        //   107: dup            
        //   108: invokespecial   java/util/Date.<init>:()V
        //   111: invokevirtual   java/util/Date.getTime:()J
        //   114: lstore          6
        //   116: aload           12
        //   118: ldc_w           "af_timestamp"
        //   121: lload           6
        //   123: invokestatic    java/lang/Long.toString:(J)Ljava/lang/String;
        //   126: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   131: pop            
        //   132: aload           13
        //   134: lload           6
        //   136: invokestatic    com/appsflyer/internal/c.\u0131:(Landroid/content/Context;J)Ljava/lang/String;
        //   139: astore_1       
        //   140: aload_1        
        //   141: ifnull          156
        //   144: aload           12
        //   146: ldc_w           "cksm_v1"
        //   149: aload_1        
        //   150: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   155: pop            
        //   156: aload_0        
        //   157: invokevirtual   com/appsflyer/AppsFlyerLib.isTrackingStopped:()Z
        //   160: ifne            4740
        //   163: new             Ljava/lang/StringBuilder;
        //   166: dup            
        //   167: ldc_w           "******* sendTrackingWithEvent: "
        //   170: invokespecial   java/lang/StringBuilder.<init>:(Ljava/lang/String;)V
        //   173: astore          15
        //   175: iload           4
        //   177: ifeq            187
        //   180: ldc_w           "Launch"
        //   183: astore_1       
        //   184: goto            190
        //   187: aload           11
        //   189: astore_1       
        //   190: aload           15
        //   192: aload_1        
        //   193: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   196: pop            
        //   197: aload           15
        //   199: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
        //   202: astore_1       
        //   203: aload_1        
        //   204: invokestatic    com/appsflyer/AFLogger.afInfoLog:(Ljava/lang/String;)V
        //   207: goto            210
        //   210: invokestatic    com/appsflyer/internal/aa.\u01c3:()Lcom/appsflyer/internal/aa;
        //   213: pop            
        //   214: aload           13
        //   216: invokestatic    com/appsflyer/internal/aa.\u03b9:(Landroid/content/Context;)Ljava/io/File;
        //   219: invokevirtual   java/io/File.exists:()Z
        //   222: ifne            247
        //   225: aload           13
        //   227: invokestatic    com/appsflyer/internal/aa.\u03b9:(Landroid/content/Context;)Ljava/io/File;
        //   230: invokevirtual   java/io/File.mkdir:()Z
        //   233: pop            
        //   234: goto            247
        //   237: astore_1       
        //   238: ldc             "AppsFlyer_5.4.1"
        //   240: ldc_w           "Could not create cache directory"
        //   243: invokestatic    android/util/Log.i:(Ljava/lang/String;Ljava/lang/String;)I
        //   246: pop            
        //   247: aload           13
        //   249: invokevirtual   android/content/Context.getPackageManager:()Landroid/content/pm/PackageManager;
        //   252: aload           13
        //   254: invokevirtual   android/content/Context.getPackageName:()Ljava/lang/String;
        //   257: sipush          4096
        //   260: invokevirtual   android/content/pm/PackageManager.getPackageInfo:(Ljava/lang/String;I)Landroid/content/pm/PackageInfo;
        //   263: getfield        android/content/pm/PackageInfo.requestedPermissions:[Ljava/lang/String;
        //   266: invokestatic    java/util/Arrays.asList:([Ljava/lang/Object;)Ljava/util/List;
        //   269: astore_1       
        //   270: aload_1        
        //   271: ldc_w           "android.permission.INTERNET"
        //   274: invokeinterface java/util/List.contains:(Ljava/lang/Object;)Z
        //   279: ifne            288
        //   282: ldc_w           "Permission android.permission.INTERNET is missing in the AndroidManifest.xml"
        //   285: invokestatic    com/appsflyer/AFLogger.afWarnLog:(Ljava/lang/String;)V
        //   288: aload_1        
        //   289: ldc_w           "android.permission.ACCESS_NETWORK_STATE"
        //   292: invokeinterface java/util/List.contains:(Ljava/lang/Object;)Z
        //   297: ifne            306
        //   300: ldc_w           "Permission android.permission.ACCESS_NETWORK_STATE is missing in the AndroidManifest.xml"
        //   303: invokestatic    com/appsflyer/AFLogger.afWarnLog:(Ljava/lang/String;)V
        //   306: aload_1        
        //   307: ldc_w           "android.permission.ACCESS_WIFI_STATE"
        //   310: invokeinterface java/util/List.contains:(Ljava/lang/Object;)Z
        //   315: ifne            335
        //   318: ldc_w           "Permission android.permission.ACCESS_WIFI_STATE is missing in the AndroidManifest.xml"
        //   321: invokestatic    com/appsflyer/AFLogger.afWarnLog:(Ljava/lang/String;)V
        //   324: goto            335
        //   327: astore_1       
        //   328: ldc_w           "Exception while validation permissions. "
        //   331: aload_1        
        //   332: invokestatic    com/appsflyer/AFLogger.afErrorLog:(Ljava/lang/String;Ljava/lang/Throwable;)V
        //   335: aload           12
        //   337: ldc_w           "af_events_api"
        //   340: ldc_w           "1"
        //   343: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   348: pop            
        //   349: aload           12
        //   351: ldc_w           "brand"
        //   354: getstatic       android/os/Build.BRAND:Ljava/lang/String;
        //   357: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   362: pop            
        //   363: aload           12
        //   365: ldc_w           "device"
        //   368: getstatic       android/os/Build.DEVICE:Ljava/lang/String;
        //   371: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   376: pop            
        //   377: aload           12
        //   379: ldc_w           "product"
        //   382: getstatic       android/os/Build.PRODUCT:Ljava/lang/String;
        //   385: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   390: pop            
        //   391: aload           12
        //   393: ldc_w           "sdk"
        //   396: getstatic       android/os/Build$VERSION.SDK_INT:I
        //   399: invokestatic    java/lang/Integer.toString:(I)Ljava/lang/String;
        //   402: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   407: pop            
        //   408: aload           12
        //   410: ldc_w           "model"
        //   413: getstatic       android/os/Build.MODEL:Ljava/lang/String;
        //   416: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   421: pop            
        //   422: aload           12
        //   424: ldc_w           "deviceType"
        //   427: getstatic       android/os/Build.TYPE:Ljava/lang/String;
        //   430: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   435: pop            
        //   436: aload           13
        //   438: aload           12
        //   440: invokestatic    com/appsflyer/AppsFlyerLibCore.\u01c3:(Landroid/content/Context;Ljava/util/Map;)V
        //   443: invokestatic    com/appsflyer/AppsFlyerProperties.getInstance:()Lcom/appsflyer/AppsFlyerProperties;
        //   446: astore          15
        //   448: iload           4
        //   450: ifeq            876
        //   453: aload           13
        //   455: invokestatic    com/appsflyer/AppsFlyerLibCore.\u0456:(Landroid/content/Context;)Z
        //   458: ifeq            578
        //   461: aload           15
        //   463: invokevirtual   com/appsflyer/AppsFlyerProperties.isOtherSdkStringDisabled:()Z
        //   466: ifne            488
        //   469: aload           12
        //   471: ldc_w           "batteryLevel"
        //   474: aload           13
        //   476: invokestatic    com/appsflyer/AppsFlyerLibCore.\u04c0:(Landroid/content/Context;)F
        //   479: invokestatic    java/lang/String.valueOf:(F)Ljava/lang/String;
        //   482: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   487: pop            
        //   488: aload           13
        //   490: invokestatic    com/appsflyer/AppsFlyerLibCore.\u0399:(Landroid/content/Context;)V
        //   493: getstatic       android/os/Build$VERSION.SDK_INT:I
        //   496: bipush          23
        //   498: if_icmplt       518
        //   501: aload           13
        //   503: ldc_w           Landroid/app/UiModeManager;.class
        //   506: invokevirtual   android/content/Context.getSystemService:(Ljava/lang/Class;)Ljava/lang/Object;
        //   509: astore_1       
        //   510: aload_1        
        //   511: checkcast       Landroid/app/UiModeManager;
        //   514: astore_1       
        //   515: goto            530
        //   518: aload           13
        //   520: ldc_w           "uimode"
        //   523: invokevirtual   android/content/Context.getSystemService:(Ljava/lang/String;)Ljava/lang/Object;
        //   526: astore_1       
        //   527: goto            510
        //   530: aload_1        
        //   531: ifnull          556
        //   534: aload_1        
        //   535: invokevirtual   android/app/UiModeManager.getCurrentModeType:()I
        //   538: iconst_4       
        //   539: if_icmpne       556
        //   542: aload           12
        //   544: ldc_w           "tv"
        //   547: getstatic       java/lang/Boolean.TRUE:Ljava/lang/Boolean;
        //   550: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   555: pop            
        //   556: aload           13
        //   558: invokestatic    com/appsflyer/internal/instant/AFInstantApps.isInstantApp:(Landroid/content/Context;)Z
        //   561: ifeq            578
        //   564: aload           12
        //   566: ldc_w           "inst_app"
        //   569: getstatic       java/lang/Boolean.TRUE:Ljava/lang/Boolean;
        //   572: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   577: pop            
        //   578: aload           13
        //   580: invokestatic    com/appsflyer/AppsFlyerLibCore.getSharedPreferences:(Landroid/content/Context;)Landroid/content/SharedPreferences;
        //   583: ldc_w           "AppsFlyerTimePassedSincePrevLaunch"
        //   586: lconst_0       
        //   587: invokeinterface android/content/SharedPreferences.getLong:(Ljava/lang/String;J)J
        //   592: lstore          6
        //   594: invokestatic    java/lang/System.currentTimeMillis:()J
        //   597: lstore          8
        //   599: aload           13
        //   601: ldc_w           "AppsFlyerTimePassedSincePrevLaunch"
        //   604: lload           8
        //   606: invokestatic    com/appsflyer/AppsFlyerLibCore.\u0269:(Landroid/content/Context;Ljava/lang/String;J)V
        //   609: lload           6
        //   611: lconst_0       
        //   612: lcmp           
        //   613: ifle            4747
        //   616: lload           8
        //   618: lload           6
        //   620: lsub           
        //   621: ldc2_w          1000
        //   624: ldiv           
        //   625: lstore          6
        //   627: goto            630
        //   630: aload           12
        //   632: ldc_w           "timepassedsincelastlaunch"
        //   635: lload           6
        //   637: invokestatic    java/lang/Long.toString:(J)Ljava/lang/String;
        //   640: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   645: pop            
        //   646: invokestatic    com/appsflyer/AppsFlyerProperties.getInstance:()Lcom/appsflyer/AppsFlyerProperties;
        //   649: ldc_w           "oneLinkSlug"
        //   652: invokevirtual   com/appsflyer/AppsFlyerProperties.getString:(Ljava/lang/String;)Ljava/lang/String;
        //   655: astore_1       
        //   656: invokestatic    com/appsflyer/AppsFlyerProperties.getInstance:()Lcom/appsflyer/AppsFlyerProperties;
        //   659: ldc_w           "onelinkVersion"
        //   662: invokevirtual   com/appsflyer/AppsFlyerProperties.getString:(Ljava/lang/String;)Ljava/lang/String;
        //   665: astore          20
        //   667: aload_1        
        //   668: ifnull          683
        //   671: aload           12
        //   673: ldc_w           "onelink_id"
        //   676: aload_1        
        //   677: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   682: pop            
        //   683: aload           20
        //   685: ifnull          701
        //   688: aload           12
        //   690: ldc_w           "onelink_ver"
        //   693: aload           20
        //   695: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   700: pop            
        //   701: aload           14
        //   703: ldc_w           "appsflyerGetConversionDataTiming"
        //   706: lconst_0       
        //   707: invokeinterface android/content/SharedPreferences.getLong:(Ljava/lang/String;J)J
        //   712: lstore          6
        //   714: lload           6
        //   716: lconst_0       
        //   717: lcmp           
        //   718: ifle            762
        //   721: aload           12
        //   723: ldc_w           "gcd_timing"
        //   726: lload           6
        //   728: invokestatic    java/lang/Long.toString:(J)Ljava/lang/String;
        //   731: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   736: pop            
        //   737: aload           14
        //   739: invokeinterface android/content/SharedPreferences.edit:()Landroid/content/SharedPreferences$Editor;
        //   744: astore_1       
        //   745: aload_1        
        //   746: ldc_w           "appsflyerGetConversionDataTiming"
        //   749: lconst_0       
        //   750: invokeinterface android/content/SharedPreferences$Editor.putLong:(Ljava/lang/String;J)Landroid/content/SharedPreferences$Editor;
        //   755: pop            
        //   756: aload_1        
        //   757: invokeinterface android/content/SharedPreferences$Editor.apply:()V
        //   762: aload_0        
        //   763: getfield        com/appsflyer/AppsFlyerLibCore.\u0445:Ljava/lang/String;
        //   766: ifnull          784
        //   769: aload           12
        //   771: ldc_w           "phone"
        //   774: aload_0        
        //   775: getfield        com/appsflyer/AppsFlyerLibCore.\u0445:Ljava/lang/String;
        //   778: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   783: pop            
        //   784: aload           10
        //   786: invokestatic    android/text/TextUtils.isEmpty:(Ljava/lang/CharSequence;)Z
        //   789: ifne            805
        //   792: aload           12
        //   794: ldc_w           "referrer"
        //   797: aload           10
        //   799: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   804: pop            
        //   805: aload           14
        //   807: ldc_w           "extraReferrers"
        //   810: aconst_null    
        //   811: invokeinterface android/content/SharedPreferences.getString:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
        //   816: astore_1       
        //   817: aload_1        
        //   818: ifnull          833
        //   821: aload           12
        //   823: ldc_w           "extraReferrers"
        //   826: aload_1        
        //   827: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   832: pop            
        //   833: aload           15
        //   835: aload           13
        //   837: invokevirtual   com/appsflyer/AppsFlyerProperties.getReferrer:(Landroid/content/Context;)Ljava/lang/String;
        //   840: astore_1       
        //   841: aload_1        
        //   842: invokestatic    android/text/TextUtils.isEmpty:(Ljava/lang/CharSequence;)Z
        //   845: ifne            4755
        //   848: aload           12
        //   850: ldc_w           "referrer"
        //   853: invokeinterface java/util/Map.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //   858: ifnonnull       4755
        //   861: aload           12
        //   863: ldc_w           "referrer"
        //   866: aload_1        
        //   867: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   872: pop            
        //   873: goto            4755
        //   876: aload           13
        //   878: invokestatic    com/appsflyer/AppsFlyerLibCore.getSharedPreferences:(Landroid/content/Context;)Landroid/content/SharedPreferences;
        //   881: astore_1       
        //   882: aload_1        
        //   883: invokeinterface android/content/SharedPreferences.edit:()Landroid/content/SharedPreferences$Editor;
        //   888: astore          10
        //   890: aload_1        
        //   891: ldc_w           "prev_event_name"
        //   894: aconst_null    
        //   895: invokeinterface android/content/SharedPreferences.getString:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
        //   900: astore          20
        //   902: aload           20
        //   904: ifnull          4766
        //   907: new             Lorg/json/JSONObject;
        //   910: dup            
        //   911: invokespecial   org/json/JSONObject.<init>:()V
        //   914: astore          21
        //   916: new             Ljava/lang/StringBuilder;
        //   919: dup            
        //   920: invokespecial   java/lang/StringBuilder.<init>:()V
        //   923: astore          22
        //   925: aload           22
        //   927: aload_1        
        //   928: ldc_w           "prev_event_timestamp"
        //   931: ldc2_w          -1
        //   934: invokeinterface android/content/SharedPreferences.getLong:(Ljava/lang/String;J)J
        //   939: invokevirtual   java/lang/StringBuilder.append:(J)Ljava/lang/StringBuilder;
        //   942: pop            
        //   943: aload           21
        //   945: ldc_w           "prev_event_timestamp"
        //   948: aload           22
        //   950: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
        //   953: invokevirtual   org/json/JSONObject.put:(Ljava/lang/String;Ljava/lang/Object;)Lorg/json/JSONObject;
        //   956: pop            
        //   957: aload           21
        //   959: ldc_w           "prev_event_value"
        //   962: aload_1        
        //   963: ldc_w           "prev_event_value"
        //   966: aconst_null    
        //   967: invokeinterface android/content/SharedPreferences.getString:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
        //   972: invokevirtual   org/json/JSONObject.put:(Ljava/lang/String;Ljava/lang/Object;)Lorg/json/JSONObject;
        //   975: pop            
        //   976: aload           21
        //   978: ldc_w           "prev_event_name"
        //   981: aload           20
        //   983: invokevirtual   org/json/JSONObject.put:(Ljava/lang/String;Ljava/lang/Object;)Lorg/json/JSONObject;
        //   986: pop            
        //   987: aload           12
        //   989: ldc_w           "prev_event"
        //   992: aload           21
        //   994: invokevirtual   org/json/JSONObject.toString:()Ljava/lang/String;
        //   997: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  1002: pop            
        //  1003: goto            1006
        //  1006: aload           10
        //  1008: ldc_w           "prev_event_name"
        //  1011: aload           11
        //  1013: invokeinterface android/content/SharedPreferences$Editor.putString:(Ljava/lang/String;Ljava/lang/String;)Landroid/content/SharedPreferences$Editor;
        //  1018: pop            
        //  1019: aload           10
        //  1021: ldc_w           "prev_event_value"
        //  1024: aload           18
        //  1026: invokeinterface android/content/SharedPreferences$Editor.putString:(Ljava/lang/String;Ljava/lang/String;)Landroid/content/SharedPreferences$Editor;
        //  1031: pop            
        //  1032: aload           10
        //  1034: ldc_w           "prev_event_timestamp"
        //  1037: invokestatic    java/lang/System.currentTimeMillis:()J
        //  1040: invokeinterface android/content/SharedPreferences$Editor.putLong:(Ljava/lang/String;J)Landroid/content/SharedPreferences$Editor;
        //  1045: pop            
        //  1046: aload           10
        //  1048: invokeinterface android/content/SharedPreferences$Editor.apply:()V
        //  1053: goto            1067
        //  1056: astore_1       
        //  1057: goto            4729
        //  1060: ldc_w           "Error while processing previous event."
        //  1063: aload_1        
        //  1064: invokestatic    com/appsflyer/AFLogger.afErrorLog:(Ljava/lang/String;Ljava/lang/Throwable;)V
        //  1067: invokestatic    com/appsflyer/AppsFlyerProperties.getInstance:()Lcom/appsflyer/AppsFlyerProperties;
        //  1070: ldc_w           "KSAppsFlyerId"
        //  1073: invokevirtual   com/appsflyer/AppsFlyerProperties.getString:(Ljava/lang/String;)Ljava/lang/String;
        //  1076: astore_1       
        //  1077: invokestatic    com/appsflyer/AppsFlyerProperties.getInstance:()Lcom/appsflyer/AppsFlyerProperties;
        //  1080: ldc_w           "KSAppsFlyerRICounter"
        //  1083: invokevirtual   com/appsflyer/AppsFlyerProperties.getString:(Ljava/lang/String;)Ljava/lang/String;
        //  1086: astore          10
        //  1088: aload_1        
        //  1089: ifnull          1133
        //  1092: aload           10
        //  1094: ifnull          1133
        //  1097: aload           10
        //  1099: invokestatic    java/lang/Integer.valueOf:(Ljava/lang/String;)Ljava/lang/Integer;
        //  1102: invokevirtual   java/lang/Number.intValue:()I
        //  1105: ifle            1133
        //  1108: aload           12
        //  1110: ldc_w           "reinstallCounter"
        //  1113: aload           10
        //  1115: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  1120: pop            
        //  1121: aload           12
        //  1123: ldc_w           "originalAppsflyerId"
        //  1126: aload_1        
        //  1127: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  1132: pop            
        //  1133: invokestatic    com/appsflyer/AppsFlyerProperties.getInstance:()Lcom/appsflyer/AppsFlyerProperties;
        //  1136: ldc_w           "additionalCustomData"
        //  1139: invokevirtual   com/appsflyer/AppsFlyerProperties.getString:(Ljava/lang/String;)Ljava/lang/String;
        //  1142: astore_1       
        //  1143: aload_1        
        //  1144: ifnull          1159
        //  1147: aload           12
        //  1149: ldc_w           "customData"
        //  1152: aload_1        
        //  1153: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  1158: pop            
        //  1159: aload           13
        //  1161: invokevirtual   android/content/Context.getPackageManager:()Landroid/content/pm/PackageManager;
        //  1164: aload           13
        //  1166: invokevirtual   android/content/Context.getPackageName:()Ljava/lang/String;
        //  1169: invokevirtual   android/content/pm/PackageManager.getInstallerPackageName:(Ljava/lang/String;)Ljava/lang/String;
        //  1172: astore_1       
        //  1173: aload_1        
        //  1174: ifnull          1200
        //  1177: aload           12
        //  1179: ldc_w           "installer_package"
        //  1182: aload_1        
        //  1183: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  1188: pop            
        //  1189: goto            1200
        //  1192: astore_1       
        //  1193: ldc_w           "Exception while getting the app's installer package. "
        //  1196: aload_1        
        //  1197: invokestatic    com/appsflyer/AFLogger.afErrorLog:(Ljava/lang/String;Ljava/lang/Throwable;)V
        //  1200: aload           15
        //  1202: ldc_w           "sdkExtension"
        //  1205: invokevirtual   com/appsflyer/AppsFlyerProperties.getString:(Ljava/lang/String;)Ljava/lang/String;
        //  1208: astore_1       
        //  1209: aload_1        
        //  1210: ifnull          1232
        //  1213: aload_1        
        //  1214: invokevirtual   java/lang/String.length:()I
        //  1217: ifle            1232
        //  1220: aload           12
        //  1222: ldc_w           "sdkExtension"
        //  1225: aload_1        
        //  1226: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  1231: pop            
        //  1232: aload_0        
        //  1233: aload           13
        //  1235: invokevirtual   com/appsflyer/AppsFlyerLibCore.getConfiguredChannel:(Landroid/content/Context;)Ljava/lang/String;
        //  1238: astore_1       
        //  1239: aload           13
        //  1241: aload_1        
        //  1242: invokestatic    com/appsflyer/AppsFlyerLibCore.\u03b9:(Landroid/content/Context;Ljava/lang/String;)Ljava/lang/String;
        //  1245: astore          10
        //  1247: aload           10
        //  1249: ifnull          4769
        //  1252: aload           10
        //  1254: aload_1        
        //  1255: invokevirtual   java/lang/Object.equals:(Ljava/lang/Object;)Z
        //  1258: ifeq            1264
        //  1261: goto            4769
        //  1264: aload           12
        //  1266: ldc_w           "af_latestchannel"
        //  1269: aload_1        
        //  1270: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  1275: pop            
        //  1276: aload           13
        //  1278: invokestatic    com/appsflyer/AppsFlyerLibCore.getSharedPreferences:(Landroid/content/Context;)Landroid/content/SharedPreferences;
        //  1281: astore_1       
        //  1282: aload_1        
        //  1283: ldc_w           "INSTALL_STORE"
        //  1286: invokeinterface android/content/SharedPreferences.contains:(Ljava/lang/String;)Z
        //  1291: ifeq            1308
        //  1294: aload_1        
        //  1295: ldc_w           "INSTALL_STORE"
        //  1298: aconst_null    
        //  1299: invokeinterface android/content/SharedPreferences.getString:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
        //  1304: astore_1       
        //  1305: goto            1389
        //  1308: aload           13
        //  1310: invokestatic    com/appsflyer/AppsFlyerLibCore.\u0456:(Landroid/content/Context;)Z
        //  1313: ifeq            4784
        //  1316: invokestatic    com/appsflyer/AppsFlyerProperties.getInstance:()Lcom/appsflyer/AppsFlyerProperties;
        //  1319: ldc_w           "api_store_value"
        //  1322: invokevirtual   com/appsflyer/AppsFlyerProperties.getString:(Ljava/lang/String;)Ljava/lang/String;
        //  1325: astore_1       
        //  1326: aload_1        
        //  1327: ifnull          1333
        //  1330: goto            4781
        //  1333: aload           13
        //  1335: ifnull          4784
        //  1338: ldc_w           "AF_STORE"
        //  1341: aload           13
        //  1343: invokevirtual   android/content/Context.getPackageManager:()Landroid/content/pm/PackageManager;
        //  1346: aload           13
        //  1348: invokevirtual   android/content/Context.getPackageName:()Ljava/lang/String;
        //  1351: invokestatic    com/appsflyer/AppsFlyerLibCore.\u0269:(Ljava/lang/String;Landroid/content/pm/PackageManager;Ljava/lang/String;)Ljava/lang/String;
        //  1354: astore_1       
        //  1355: goto            4781
        //  1358: aload           13
        //  1360: invokestatic    com/appsflyer/AppsFlyerLibCore.getSharedPreferences:(Landroid/content/Context;)Landroid/content/SharedPreferences;
        //  1363: invokeinterface android/content/SharedPreferences.edit:()Landroid/content/SharedPreferences$Editor;
        //  1368: astore          10
        //  1370: aload           10
        //  1372: ldc_w           "INSTALL_STORE"
        //  1375: aload_1        
        //  1376: invokeinterface android/content/SharedPreferences$Editor.putString:(Ljava/lang/String;Ljava/lang/String;)Landroid/content/SharedPreferences$Editor;
        //  1381: pop            
        //  1382: aload           10
        //  1384: invokeinterface android/content/SharedPreferences$Editor.apply:()V
        //  1389: aload_1        
        //  1390: ifnull          1408
        //  1393: aload           12
        //  1395: ldc_w           "af_installstore"
        //  1398: aload_1        
        //  1399: invokevirtual   java/lang/String.toLowerCase:()Ljava/lang/String;
        //  1402: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  1407: pop            
        //  1408: aload           13
        //  1410: invokestatic    com/appsflyer/AppsFlyerLibCore.getSharedPreferences:(Landroid/content/Context;)Landroid/content/SharedPreferences;
        //  1413: astore          20
        //  1415: invokestatic    com/appsflyer/AppsFlyerProperties.getInstance:()Lcom/appsflyer/AppsFlyerProperties;
        //  1418: ldc_w           "preInstallName"
        //  1421: invokevirtual   com/appsflyer/AppsFlyerProperties.getString:(Ljava/lang/String;)Ljava/lang/String;
        //  1424: astore          10
        //  1426: aload           10
        //  1428: astore_1       
        //  1429: aload           10
        //  1431: ifnonnull       1655
        //  1434: aload           20
        //  1436: ldc_w           "preInstallName"
        //  1439: invokeinterface android/content/SharedPreferences.contains:(Ljava/lang/String;)Z
        //  1444: ifeq            1463
        //  1447: aload           20
        //  1449: ldc_w           "preInstallName"
        //  1452: aconst_null    
        //  1453: invokeinterface android/content/SharedPreferences.getString:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
        //  1458: astore          10
        //  1460: goto            1633
        //  1463: aload           10
        //  1465: astore_1       
        //  1466: aload           13
        //  1468: invokestatic    com/appsflyer/AppsFlyerLibCore.\u0456:(Landroid/content/Context;)Z
        //  1471: ifeq            1592
        //  1474: ldc             "ro.appsflyer.preinstall.path"
        //  1476: invokestatic    com/appsflyer/AppsFlyerLibCore.\u03b9:(Ljava/lang/String;)Ljava/lang/String;
        //  1479: invokestatic    com/appsflyer/AppsFlyerLibCore.\u0196:(Ljava/lang/String;)Ljava/io/File;
        //  1482: astore          10
        //  1484: aload           10
        //  1486: astore_1       
        //  1487: aload           10
        //  1489: invokestatic    com/appsflyer/AppsFlyerLibCore.\u03b9:(Ljava/io/File;)Z
        //  1492: ifeq            1514
        //  1495: ldc             "AF_PRE_INSTALL_PATH"
        //  1497: aload           13
        //  1499: invokevirtual   android/content/Context.getPackageManager:()Landroid/content/pm/PackageManager;
        //  1502: aload           13
        //  1504: invokevirtual   android/content/Context.getPackageName:()Ljava/lang/String;
        //  1507: invokestatic    com/appsflyer/AppsFlyerLibCore.\u0269:(Ljava/lang/String;Landroid/content/pm/PackageManager;Ljava/lang/String;)Ljava/lang/String;
        //  1510: invokestatic    com/appsflyer/AppsFlyerLibCore.\u0196:(Ljava/lang/String;)Ljava/io/File;
        //  1513: astore_1       
        //  1514: aload_1        
        //  1515: astore          10
        //  1517: aload_1        
        //  1518: invokestatic    com/appsflyer/AppsFlyerLibCore.\u03b9:(Ljava/io/File;)Z
        //  1521: ifeq            1531
        //  1524: ldc             "/data/local/tmp/pre_install.appsflyer"
        //  1526: invokestatic    com/appsflyer/AppsFlyerLibCore.\u0196:(Ljava/lang/String;)Ljava/io/File;
        //  1529: astore          10
        //  1531: aload           10
        //  1533: astore_1       
        //  1534: aload           10
        //  1536: invokestatic    com/appsflyer/AppsFlyerLibCore.\u03b9:(Ljava/io/File;)Z
        //  1539: ifeq            1548
        //  1542: ldc             "/etc/pre_install.appsflyer"
        //  1544: invokestatic    com/appsflyer/AppsFlyerLibCore.\u0196:(Ljava/lang/String;)Ljava/io/File;
        //  1547: astore_1       
        //  1548: aload_1        
        //  1549: invokestatic    com/appsflyer/AppsFlyerLibCore.\u03b9:(Ljava/io/File;)Z
        //  1552: ifne            4789
        //  1555: aload_1        
        //  1556: aload           13
        //  1558: invokevirtual   android/content/Context.getPackageName:()Ljava/lang/String;
        //  1561: invokestatic    com/appsflyer/AppsFlyerLibCore.\u03b9:(Ljava/io/File;Ljava/lang/String;)Ljava/lang/String;
        //  1564: astore_1       
        //  1565: aload_1        
        //  1566: ifnull          4789
        //  1569: goto            4791
        //  1572: ldc_w           "AF_PRE_INSTALL_NAME"
        //  1575: aload           13
        //  1577: invokevirtual   android/content/Context.getPackageManager:()Landroid/content/pm/PackageManager;
        //  1580: aload           13
        //  1582: invokevirtual   android/content/Context.getPackageName:()Ljava/lang/String;
        //  1585: invokestatic    com/appsflyer/AppsFlyerLibCore.\u0269:(Ljava/lang/String;Landroid/content/pm/PackageManager;Ljava/lang/String;)Ljava/lang/String;
        //  1588: astore_1       
        //  1589: goto            4795
        //  1592: aload_1        
        //  1593: astore          10
        //  1595: aload_1        
        //  1596: ifnull          1633
        //  1599: aload           13
        //  1601: invokestatic    com/appsflyer/AppsFlyerLibCore.getSharedPreferences:(Landroid/content/Context;)Landroid/content/SharedPreferences;
        //  1604: invokeinterface android/content/SharedPreferences.edit:()Landroid/content/SharedPreferences$Editor;
        //  1609: astore          10
        //  1611: aload           10
        //  1613: ldc_w           "preInstallName"
        //  1616: aload_1        
        //  1617: invokeinterface android/content/SharedPreferences$Editor.putString:(Ljava/lang/String;Ljava/lang/String;)Landroid/content/SharedPreferences$Editor;
        //  1622: pop            
        //  1623: aload           10
        //  1625: invokeinterface android/content/SharedPreferences$Editor.apply:()V
        //  1630: aload_1        
        //  1631: astore          10
        //  1633: aload           10
        //  1635: astore_1       
        //  1636: aload           10
        //  1638: ifnull          1655
        //  1641: invokestatic    com/appsflyer/AppsFlyerProperties.getInstance:()Lcom/appsflyer/AppsFlyerProperties;
        //  1644: ldc_w           "preInstallName"
        //  1647: aload           10
        //  1649: invokevirtual   com/appsflyer/AppsFlyerProperties.set:(Ljava/lang/String;Ljava/lang/String;)V
        //  1652: aload           10
        //  1654: astore_1       
        //  1655: aload_1        
        //  1656: ifnull          1674
        //  1659: aload           12
        //  1661: ldc_w           "af_preinstall_name"
        //  1664: aload_1        
        //  1665: invokevirtual   java/lang/String.toLowerCase:()Ljava/lang/String;
        //  1668: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  1673: pop            
        //  1674: invokestatic    com/appsflyer/AppsFlyerProperties.getInstance:()Lcom/appsflyer/AppsFlyerProperties;
        //  1677: ldc_w           "api_store_value"
        //  1680: invokevirtual   com/appsflyer/AppsFlyerProperties.getString:(Ljava/lang/String;)Ljava/lang/String;
        //  1683: astore_1       
        //  1684: aload_1        
        //  1685: ifnull          4808
        //  1688: goto            1708
        //  1691: ldc_w           "AF_STORE"
        //  1694: aload           13
        //  1696: invokevirtual   android/content/Context.getPackageManager:()Landroid/content/pm/PackageManager;
        //  1699: aload           13
        //  1701: invokevirtual   android/content/Context.getPackageName:()Ljava/lang/String;
        //  1704: invokestatic    com/appsflyer/AppsFlyerLibCore.\u0269:(Ljava/lang/String;Landroid/content/pm/PackageManager;Ljava/lang/String;)Ljava/lang/String;
        //  1707: astore_1       
        //  1708: aload_1        
        //  1709: ifnull          1727
        //  1712: aload           12
        //  1714: ldc_w           "af_currentstore"
        //  1717: aload_1        
        //  1718: invokevirtual   java/lang/String.toLowerCase:()Ljava/lang/String;
        //  1721: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  1726: pop            
        //  1727: aload           19
        //  1729: ifnull          1756
        //  1732: aload           19
        //  1734: invokevirtual   java/lang/String.length:()I
        //  1737: ifle            1756
        //  1740: aload           12
        //  1742: ldc_w           "appsflyerKey"
        //  1745: aload           19
        //  1747: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  1752: pop            
        //  1753: goto            1789
        //  1756: invokestatic    com/appsflyer/AppsFlyerProperties.getInstance:()Lcom/appsflyer/AppsFlyerProperties;
        //  1759: ldc_w           "AppsFlyerKey"
        //  1762: invokevirtual   com/appsflyer/AppsFlyerProperties.getString:(Ljava/lang/String;)Ljava/lang/String;
        //  1765: astore_1       
        //  1766: aload_1        
        //  1767: ifnull          4710
        //  1770: aload_1        
        //  1771: invokevirtual   java/lang/String.length:()I
        //  1774: ifle            4710
        //  1777: aload           12
        //  1779: ldc_w           "appsflyerKey"
        //  1782: aload_1        
        //  1783: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  1788: pop            
        //  1789: invokestatic    com/appsflyer/AppsFlyerProperties.getInstance:()Lcom/appsflyer/AppsFlyerProperties;
        //  1792: ldc_w           "AppUserId"
        //  1795: invokevirtual   com/appsflyer/AppsFlyerProperties.getString:(Ljava/lang/String;)Ljava/lang/String;
        //  1798: astore_1       
        //  1799: aload_1        
        //  1800: ifnull          1815
        //  1803: aload           12
        //  1805: ldc_w           "appUserId"
        //  1808: aload_1        
        //  1809: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  1814: pop            
        //  1815: aload           15
        //  1817: ldc_w           "userEmails"
        //  1820: invokevirtual   com/appsflyer/AppsFlyerProperties.getString:(Ljava/lang/String;)Ljava/lang/String;
        //  1823: astore_1       
        //  1824: aload_1        
        //  1825: ifnull          1847
        //  1828: ldc_w           "user_emails"
        //  1831: astore          10
        //  1833: aload           12
        //  1835: aload           10
        //  1837: aload_1        
        //  1838: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  1843: pop            
        //  1844: goto            1874
        //  1847: invokestatic    com/appsflyer/AppsFlyerProperties.getInstance:()Lcom/appsflyer/AppsFlyerProperties;
        //  1850: ldc_w           "userEmail"
        //  1853: invokevirtual   com/appsflyer/AppsFlyerProperties.getString:(Ljava/lang/String;)Ljava/lang/String;
        //  1856: astore_1       
        //  1857: aload_1        
        //  1858: ifnull          1874
        //  1861: ldc_w           "sha1_el"
        //  1864: astore          10
        //  1866: aload_1        
        //  1867: invokestatic    com/appsflyer/internal/z.\u0399:(Ljava/lang/String;)Ljava/lang/String;
        //  1870: astore_1       
        //  1871: goto            1833
        //  1874: aload           11
        //  1876: ifnull          1910
        //  1879: aload           12
        //  1881: ldc_w           "eventName"
        //  1884: aload           11
        //  1886: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  1891: pop            
        //  1892: aload           18
        //  1894: ifnull          1910
        //  1897: aload           12
        //  1899: ldc_w           "eventValue"
        //  1902: aload           18
        //  1904: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  1909: pop            
        //  1910: invokestatic    com/appsflyer/AppsFlyerProperties.getInstance:()Lcom/appsflyer/AppsFlyerProperties;
        //  1913: ldc_w           "appid"
        //  1916: invokevirtual   com/appsflyer/AppsFlyerProperties.getString:(Ljava/lang/String;)Ljava/lang/String;
        //  1919: ifnull          1942
        //  1922: aload           12
        //  1924: ldc_w           "appid"
        //  1927: invokestatic    com/appsflyer/AppsFlyerProperties.getInstance:()Lcom/appsflyer/AppsFlyerProperties;
        //  1930: ldc_w           "appid"
        //  1933: invokevirtual   com/appsflyer/AppsFlyerProperties.getString:(Ljava/lang/String;)Ljava/lang/String;
        //  1936: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  1941: pop            
        //  1942: invokestatic    com/appsflyer/AppsFlyerProperties.getInstance:()Lcom/appsflyer/AppsFlyerProperties;
        //  1945: ldc_w           "currencyCode"
        //  1948: invokevirtual   com/appsflyer/AppsFlyerProperties.getString:(Ljava/lang/String;)Ljava/lang/String;
        //  1951: astore_1       
        //  1952: aload_1        
        //  1953: ifnull          2012
        //  1956: aload_1        
        //  1957: invokevirtual   java/lang/String.length:()I
        //  1960: iconst_3       
        //  1961: if_icmpeq       2000
        //  1964: new             Ljava/lang/StringBuilder;
        //  1967: dup            
        //  1968: ldc_w           "WARNING: currency code should be 3 characters!!! '"
        //  1971: invokespecial   java/lang/StringBuilder.<init>:(Ljava/lang/String;)V
        //  1974: astore          10
        //  1976: aload           10
        //  1978: aload_1        
        //  1979: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  1982: pop            
        //  1983: aload           10
        //  1985: ldc_w           "' is not a legal value."
        //  1988: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  1991: pop            
        //  1992: aload           10
        //  1994: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
        //  1997: invokestatic    com/appsflyer/AFLogger.afWarnLog:(Ljava/lang/String;)V
        //  2000: aload           12
        //  2002: ldc_w           "currency"
        //  2005: aload_1        
        //  2006: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  2011: pop            
        //  2012: invokestatic    com/appsflyer/AppsFlyerProperties.getInstance:()Lcom/appsflyer/AppsFlyerProperties;
        //  2015: ldc_w           "IS_UPDATE"
        //  2018: invokevirtual   com/appsflyer/AppsFlyerProperties.getString:(Ljava/lang/String;)Ljava/lang/String;
        //  2021: astore_1       
        //  2022: aload_1        
        //  2023: ifnull          2038
        //  2026: aload           12
        //  2028: ldc_w           "isUpdate"
        //  2031: aload_1        
        //  2032: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  2037: pop            
        //  2038: aload           12
        //  2040: ldc_w           "af_preinstalled"
        //  2043: aload_0        
        //  2044: aload           13
        //  2046: invokevirtual   com/appsflyer/AppsFlyerLib.isPreInstalledApp:(Landroid/content/Context;)Z
        //  2049: invokestatic    java/lang/Boolean.toString:(Z)Ljava/lang/String;
        //  2052: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  2057: pop            
        //  2058: aload           15
        //  2060: ldc_w           "collectFacebookAttrId"
        //  2063: iconst_1       
        //  2064: invokevirtual   com/appsflyer/AppsFlyerProperties.getBoolean:(Ljava/lang/String;Z)Z
        //  2067: istore_3       
        //  2068: iload_3        
        //  2069: ifeq            2132
        //  2072: aload           13
        //  2074: invokevirtual   android/content/Context.getPackageManager:()Landroid/content/pm/PackageManager;
        //  2077: ldc_w           "com.facebook.katana"
        //  2080: iconst_0       
        //  2081: invokevirtual   android/content/pm/PackageManager.getApplicationInfo:(Ljava/lang/String;I)Landroid/content/pm/ApplicationInfo;
        //  2084: pop            
        //  2085: aload_0        
        //  2086: aload           13
        //  2088: invokevirtual   com/appsflyer/AppsFlyerLib.getAttributionId:(Landroid/content/Context;)Ljava/lang/String;
        //  2091: astore_1       
        //  2092: goto            2116
        //  2095: astore_1       
        //  2096: ldc_w           "Exception while collecting facebook's attribution ID. "
        //  2099: aload_1        
        //  2100: invokestatic    com/appsflyer/AFLogger.afErrorLog:(Ljava/lang/String;Ljava/lang/Throwable;)V
        //  2103: goto            4818
        //  2106: astore_1       
        //  2107: ldc_w           "Exception while collecting facebook's attribution ID. "
        //  2110: invokestatic    com/appsflyer/AFLogger.afWarnLog:(Ljava/lang/String;)V
        //  2113: goto            4818
        //  2116: aload_1        
        //  2117: ifnull          2132
        //  2120: aload           12
        //  2122: ldc_w           "fb"
        //  2125: aload_1        
        //  2126: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  2131: pop            
        //  2132: invokestatic    com/appsflyer/AppsFlyerProperties.getInstance:()Lcom/appsflyer/AppsFlyerProperties;
        //  2135: astore          18
        //  2137: aload           18
        //  2139: ldc_w           "deviceTrackingDisabled"
        //  2142: iconst_0       
        //  2143: invokevirtual   com/appsflyer/AppsFlyerProperties.getBoolean:(Ljava/lang/String;Z)Z
        //  2146: ifeq            2166
        //  2149: aload           12
        //  2151: ldc_w           "deviceTrackingDisabled"
        //  2154: ldc_w           "true"
        //  2157: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  2162: pop            
        //  2163: goto            2808
        //  2166: aload           13
        //  2168: invokestatic    com/appsflyer/AppsFlyerLibCore.getSharedPreferences:(Landroid/content/Context;)Landroid/content/SharedPreferences;
        //  2171: astore          19
        //  2173: aload           18
        //  2175: ldc_w           "collectIMEI"
        //  2178: iconst_1       
        //  2179: invokevirtual   com/appsflyer/AppsFlyerProperties.getBoolean:(Ljava/lang/String;Z)Z
        //  2182: istore_3       
        //  2183: aload           19
        //  2185: ldc_w           "imeiCached"
        //  2188: aconst_null    
        //  2189: invokeinterface android/content/SharedPreferences.getString:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
        //  2194: astore_1       
        //  2195: iload_3        
        //  2196: ifeq            2362
        //  2199: aload_0        
        //  2200: getfield        com/appsflyer/AppsFlyerLibCore.\u0399:Ljava/lang/String;
        //  2203: invokestatic    android/text/TextUtils.isEmpty:(Ljava/lang/CharSequence;)Z
        //  2206: ifeq            2362
        //  2209: aload           13
        //  2211: invokestatic    com/appsflyer/AppsFlyerLibCore.\u0196:(Landroid/content/Context;)Z
        //  2214: istore_3       
        //  2215: iload_3        
        //  2216: ifeq            4836
        //  2219: aload           13
        //  2221: ldc_w           "phone"
        //  2224: invokevirtual   android/content/Context.getSystemService:(Ljava/lang/String;)Ljava/lang/Object;
        //  2227: checkcast       Landroid/telephony/TelephonyManager;
        //  2230: astore          10
        //  2232: aload           10
        //  2234: invokevirtual   java/lang/Object.getClass:()Ljava/lang/Class;
        //  2237: ldc_w           "getDeviceId"
        //  2240: iconst_0       
        //  2241: anewarray       Ljava/lang/Class;
        //  2244: invokevirtual   java/lang/Class.getMethod:(Ljava/lang/String;[Ljava/lang/Class;)Ljava/lang/reflect/Method;
        //  2247: aload           10
        //  2249: iconst_0       
        //  2250: anewarray       Ljava/lang/Object;
        //  2253: invokevirtual   java/lang/reflect/Method.invoke:(Ljava/lang/Object;[Ljava/lang/Object;)Ljava/lang/Object;
        //  2256: checkcast       Ljava/lang/String;
        //  2259: astore          10
        //  2261: aload           10
        //  2263: ifnull          2272
        //  2266: aload           10
        //  2268: astore_1       
        //  2269: goto            4833
        //  2272: aload_1        
        //  2273: ifnull          4836
        //  2276: ldc_w           "use cached IMEI: "
        //  2279: aload_1        
        //  2280: invokestatic    java/lang/String.valueOf:(Ljava/lang/Object;)Ljava/lang/String;
        //  2283: invokevirtual   java/lang/String.concat:(Ljava/lang/String;)Ljava/lang/String;
        //  2286: invokestatic    com/appsflyer/AFLogger.afDebugLog:(Ljava/lang/String;)V
        //  2289: goto            2377
        //  2292: astore          10
        //  2294: goto            2302
        //  2297: astore          10
        //  2299: goto            2333
        //  2302: aload_1        
        //  2303: ifnull          4823
        //  2306: ldc_w           "use cached IMEI: "
        //  2309: aload_1        
        //  2310: invokestatic    java/lang/String.valueOf:(Ljava/lang/Object;)Ljava/lang/String;
        //  2313: invokevirtual   java/lang/String.concat:(Ljava/lang/String;)Ljava/lang/String;
        //  2316: invokestatic    com/appsflyer/AFLogger.afDebugLog:(Ljava/lang/String;)V
        //  2319: goto            2322
        //  2322: ldc_w           "WARNING: other reason: "
        //  2325: aload           10
        //  2327: invokestatic    com/appsflyer/AFLogger.afErrorLog:(Ljava/lang/String;Ljava/lang/Throwable;)V
        //  2330: goto            2377
        //  2333: aload_1        
        //  2334: ifnull          4828
        //  2337: ldc_w           "use cached IMEI: "
        //  2340: aload_1        
        //  2341: invokestatic    java/lang/String.valueOf:(Ljava/lang/Object;)Ljava/lang/String;
        //  2344: invokevirtual   java/lang/String.concat:(Ljava/lang/String;)Ljava/lang/String;
        //  2347: invokestatic    com/appsflyer/AFLogger.afDebugLog:(Ljava/lang/String;)V
        //  2350: goto            2353
        //  2353: ldc_w           "WARNING: READ_PHONE_STATE is missing."
        //  2356: invokestatic    com/appsflyer/AFLogger.afWarnLog:(Ljava/lang/String;)V
        //  2359: goto            2377
        //  2362: aload_0        
        //  2363: getfield        com/appsflyer/AppsFlyerLibCore.\u0399:Ljava/lang/String;
        //  2366: ifnull          4836
        //  2369: aload_0        
        //  2370: getfield        com/appsflyer/AppsFlyerLibCore.\u0399:Ljava/lang/String;
        //  2373: astore_1       
        //  2374: goto            4833
        //  2377: aload_1        
        //  2378: ifnull          2427
        //  2381: aload           13
        //  2383: invokestatic    com/appsflyer/AppsFlyerLibCore.getSharedPreferences:(Landroid/content/Context;)Landroid/content/SharedPreferences;
        //  2386: invokeinterface android/content/SharedPreferences.edit:()Landroid/content/SharedPreferences$Editor;
        //  2391: astore          10
        //  2393: aload           10
        //  2395: ldc_w           "imeiCached"
        //  2398: aload_1        
        //  2399: invokeinterface android/content/SharedPreferences$Editor.putString:(Ljava/lang/String;Ljava/lang/String;)Landroid/content/SharedPreferences$Editor;
        //  2404: pop            
        //  2405: aload           10
        //  2407: invokeinterface android/content/SharedPreferences$Editor.apply:()V
        //  2412: aload           12
        //  2414: ldc_w           "imei"
        //  2417: aload_1        
        //  2418: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  2423: pop            
        //  2424: goto            2433
        //  2427: ldc_w           "IMEI was not collected."
        //  2430: invokestatic    com/appsflyer/AFLogger.afInfoLog:(Ljava/lang/String;)V
        //  2433: aload           18
        //  2435: ldc_w           "collectAndroidId"
        //  2438: iconst_1       
        //  2439: invokevirtual   com/appsflyer/AppsFlyerProperties.getBoolean:(Ljava/lang/String;Z)Z
        //  2442: istore_3       
        //  2443: aload           19
        //  2445: ldc_w           "androidIdCached"
        //  2448: aconst_null    
        //  2449: invokeinterface android/content/SharedPreferences.getString:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
        //  2454: astore_1       
        //  2455: iload_3        
        //  2456: ifeq            2558
        //  2459: aload_0        
        //  2460: getfield        com/appsflyer/AppsFlyerLibCore.\u03b9:Ljava/lang/String;
        //  2463: invokestatic    android/text/TextUtils.isEmpty:(Ljava/lang/CharSequence;)Z
        //  2466: ifeq            2558
        //  2469: aload           13
        //  2471: invokestatic    com/appsflyer/AppsFlyerLibCore.\u0196:(Landroid/content/Context;)Z
        //  2474: istore_3       
        //  2475: iload_3        
        //  2476: ifeq            4846
        //  2479: aload           13
        //  2481: invokevirtual   android/content/Context.getContentResolver:()Landroid/content/ContentResolver;
        //  2484: ldc_w           "android_id"
        //  2487: invokestatic    android/provider/Settings$Secure.getString:(Landroid/content/ContentResolver;Ljava/lang/String;)Ljava/lang/String;
        //  2490: astore          10
        //  2492: aload           10
        //  2494: ifnull          2503
        //  2497: aload           10
        //  2499: astore_1       
        //  2500: goto            2573
        //  2503: aload_1        
        //  2504: ifnull          4846
        //  2507: ldc_w           "use cached AndroidId: "
        //  2510: aload_1        
        //  2511: invokestatic    java/lang/String.valueOf:(Ljava/lang/Object;)Ljava/lang/String;
        //  2514: invokevirtual   java/lang/String.concat:(Ljava/lang/String;)Ljava/lang/String;
        //  2517: invokestatic    com/appsflyer/AFLogger.afDebugLog:(Ljava/lang/String;)V
        //  2520: goto            2573
        //  2523: astore          10
        //  2525: aload_1        
        //  2526: ifnull          4841
        //  2529: ldc_w           "use cached AndroidId: "
        //  2532: aload_1        
        //  2533: invokestatic    java/lang/String.valueOf:(Ljava/lang/Object;)Ljava/lang/String;
        //  2536: invokevirtual   java/lang/String.concat:(Ljava/lang/String;)Ljava/lang/String;
        //  2539: invokestatic    com/appsflyer/AFLogger.afDebugLog:(Ljava/lang/String;)V
        //  2542: goto            2545
        //  2545: aload           10
        //  2547: invokevirtual   java/lang/Throwable.getMessage:()Ljava/lang/String;
        //  2550: aload           10
        //  2552: invokestatic    com/appsflyer/AFLogger.afErrorLog:(Ljava/lang/String;Ljava/lang/Throwable;)V
        //  2555: goto            2573
        //  2558: aload_0        
        //  2559: getfield        com/appsflyer/AppsFlyerLibCore.\u03b9:Ljava/lang/String;
        //  2562: ifnull          4846
        //  2565: aload_0        
        //  2566: getfield        com/appsflyer/AppsFlyerLibCore.\u03b9:Ljava/lang/String;
        //  2569: astore_1       
        //  2570: goto            2573
        //  2573: aload_1        
        //  2574: ifnull          2623
        //  2577: aload           13
        //  2579: invokestatic    com/appsflyer/AppsFlyerLibCore.getSharedPreferences:(Landroid/content/Context;)Landroid/content/SharedPreferences;
        //  2582: invokeinterface android/content/SharedPreferences.edit:()Landroid/content/SharedPreferences$Editor;
        //  2587: astore          10
        //  2589: aload           10
        //  2591: ldc_w           "androidIdCached"
        //  2594: aload_1        
        //  2595: invokeinterface android/content/SharedPreferences$Editor.putString:(Ljava/lang/String;Ljava/lang/String;)Landroid/content/SharedPreferences$Editor;
        //  2600: pop            
        //  2601: aload           10
        //  2603: invokeinterface android/content/SharedPreferences$Editor.apply:()V
        //  2608: aload           12
        //  2610: ldc_w           "android_id"
        //  2613: aload_1        
        //  2614: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  2619: pop            
        //  2620: goto            2629
        //  2623: ldc_w           "Android ID was not collected."
        //  2626: invokestatic    com/appsflyer/AFLogger.afInfoLog:(Ljava/lang/String;)V
        //  2629: new             Ljava/util/HashMap;
        //  2632: dup            
        //  2633: invokespecial   java/util/HashMap.<init>:()V
        //  2636: astore          19
        //  2638: aload_0        
        //  2639: getfield        com/appsflyer/AppsFlyerLibCore.\u0268:Ljava/lang/String;
        //  2642: ifnull          4851
        //  2645: iconst_1       
        //  2646: istore_3       
        //  2647: goto            2650
        //  2650: iload_3        
        //  2651: ifeq            2662
        //  2654: aload_0        
        //  2655: getfield        com/appsflyer/AppsFlyerLibCore.\u0268:Ljava/lang/String;
        //  2658: astore_1       
        //  2659: goto            2764
        //  2662: aload           18
        //  2664: ldc_w           "collectOAID"
        //  2667: iconst_1       
        //  2668: invokevirtual   com/appsflyer/AppsFlyerProperties.getBoolean:(Ljava/lang/String;Z)Z
        //  2671: istore          5
        //  2673: iload           5
        //  2675: ifeq            4856
        //  2678: new             Lcom/appsflyer/oaid/OaidClient;
        //  2681: dup            
        //  2682: aload           13
        //  2684: invokespecial   com/appsflyer/oaid/OaidClient.<init>:(Landroid/content/Context;)V
        //  2687: astore_1       
        //  2688: aload_1        
        //  2689: aload           18
        //  2691: invokevirtual   com/appsflyer/AppsFlyerProperties.isEnableLog:()Z
        //  2694: invokevirtual   com/appsflyer/oaid/OaidClient.setLogging:(Z)V
        //  2697: aload_1        
        //  2698: invokevirtual   com/appsflyer/oaid/OaidClient.fetch:()Lcom/appsflyer/oaid/OaidClient$Info;
        //  2701: astore_1       
        //  2702: aload_1        
        //  2703: ifnull          4856
        //  2706: aload_1        
        //  2707: invokevirtual   com/appsflyer/oaid/OaidClient$Info.getId:()Ljava/lang/String;
        //  2710: astore          10
        //  2712: aload_1        
        //  2713: invokevirtual   com/appsflyer/oaid/OaidClient$Info.getLat:()Ljava/lang/Boolean;
        //  2716: astore          18
        //  2718: aload           10
        //  2720: astore_1       
        //  2721: aload           18
        //  2723: ifnull          2764
        //  2726: aload           19
        //  2728: ldc_w           "isLat"
        //  2731: aload           18
        //  2733: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  2738: pop            
        //  2739: aload           10
        //  2741: astore_1       
        //  2742: goto            2764
        //  2745: astore_1       
        //  2746: aload           10
        //  2748: astore_1       
        //  2749: goto            2755
        //  2752: astore_1       
        //  2753: aconst_null    
        //  2754: astore_1       
        //  2755: ldc_w           "No OAID library"
        //  2758: invokestatic    com/appsflyer/AFLogger.afDebugLog:(Ljava/lang/String;)V
        //  2761: goto            2764
        //  2764: aload_1        
        //  2765: ifnull          2808
        //  2768: aload           19
        //  2770: ldc_w           "isManual"
        //  2773: iload_3        
        //  2774: invokestatic    java/lang/Boolean.valueOf:(Z)Ljava/lang/Boolean;
        //  2777: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  2782: pop            
        //  2783: aload           19
        //  2785: ldc_w           "val"
        //  2788: aload_1        
        //  2789: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  2794: pop            
        //  2795: aload           12
        //  2797: ldc_w           "oaid"
        //  2800: aload           19
        //  2802: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  2807: pop            
        //  2808: new             Ljava/lang/ref/WeakReference;
        //  2811: dup            
        //  2812: aload           13
        //  2814: invokespecial   java/lang/ref/WeakReference.<init>:(Ljava/lang/Object;)V
        //  2817: invokestatic    com/appsflyer/internal/ae.\u0399:(Ljava/lang/ref/WeakReference;)Ljava/lang/String;
        //  2820: astore_1       
        //  2821: aload_1        
        //  2822: ifnull          2872
        //  2825: aload           12
        //  2827: ldc_w           "uid"
        //  2830: aload_1        
        //  2831: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  2836: pop            
        //  2837: goto            2872
        //  2840: astore_1       
        //  2841: new             Ljava/lang/StringBuilder;
        //  2844: dup            
        //  2845: ldc_w           "ERROR: could not get uid "
        //  2848: invokespecial   java/lang/StringBuilder.<init>:(Ljava/lang/String;)V
        //  2851: astore          10
        //  2853: aload           10
        //  2855: aload_1        
        //  2856: invokevirtual   java/lang/Throwable.getMessage:()Ljava/lang/String;
        //  2859: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  2862: pop            
        //  2863: aload           10
        //  2865: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
        //  2868: aload_1        
        //  2869: invokestatic    com/appsflyer/AFLogger.afErrorLog:(Ljava/lang/String;Ljava/lang/Throwable;)V
        //  2872: aload           12
        //  2874: ldc_w           "lang"
        //  2877: invokestatic    java/util/Locale.getDefault:()Ljava/util/Locale;
        //  2880: invokevirtual   java/util/Locale.getDisplayLanguage:()Ljava/lang/String;
        //  2883: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  2888: pop            
        //  2889: goto            2900
        //  2892: astore_1       
        //  2893: ldc_w           "Exception while collecting display language name. "
        //  2896: aload_1        
        //  2897: invokestatic    com/appsflyer/AFLogger.afErrorLog:(Ljava/lang/String;Ljava/lang/Throwable;)V
        //  2900: aload           12
        //  2902: ldc_w           "lang_code"
        //  2905: invokestatic    java/util/Locale.getDefault:()Ljava/util/Locale;
        //  2908: invokevirtual   java/util/Locale.getLanguage:()Ljava/lang/String;
        //  2911: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  2916: pop            
        //  2917: goto            2928
        //  2920: astore_1       
        //  2921: ldc_w           "Exception while collecting display language code. "
        //  2924: aload_1        
        //  2925: invokestatic    com/appsflyer/AFLogger.afErrorLog:(Ljava/lang/String;Ljava/lang/Throwable;)V
        //  2928: aload           12
        //  2930: ldc_w           "country"
        //  2933: invokestatic    java/util/Locale.getDefault:()Ljava/util/Locale;
        //  2936: invokevirtual   java/util/Locale.getCountry:()Ljava/lang/String;
        //  2939: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  2944: pop            
        //  2945: goto            2956
        //  2948: astore_1       
        //  2949: ldc_w           "Exception while collecting country name. "
        //  2952: aload_1        
        //  2953: invokestatic    com/appsflyer/AFLogger.afErrorLog:(Ljava/lang/String;Ljava/lang/Throwable;)V
        //  2956: aload           12
        //  2958: ldc_w           "platformextension"
        //  2961: aload_0        
        //  2962: getfield        com/appsflyer/AppsFlyerLibCore.\u025f:Lcom/appsflyer/internal/ag;
        //  2965: invokevirtual   com/appsflyer/internal/ag.\u0131:()Ljava/lang/String;
        //  2968: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  2973: pop            
        //  2974: aload           13
        //  2976: aload           12
        //  2978: invokestatic    com/appsflyer/AppsFlyerLibCore.\u0269:(Landroid/content/Context;Ljava/util/Map;)V
        //  2981: new             Ljava/text/SimpleDateFormat;
        //  2984: dup            
        //  2985: ldc_w           "yyyy-MM-dd_HHmmssZ"
        //  2988: getstatic       java/util/Locale.US:Ljava/util/Locale;
        //  2991: invokespecial   java/text/SimpleDateFormat.<init>:(Ljava/lang/String;Ljava/util/Locale;)V
        //  2994: astore_1       
        //  2995: aload           13
        //  2997: invokevirtual   android/content/Context.getPackageManager:()Landroid/content/pm/PackageManager;
        //  3000: aload           13
        //  3002: invokevirtual   android/content/Context.getPackageName:()Ljava/lang/String;
        //  3005: iconst_0       
        //  3006: invokevirtual   android/content/pm/PackageManager.getPackageInfo:(Ljava/lang/String;I)Landroid/content/pm/PackageInfo;
        //  3009: getfield        android/content/pm/PackageInfo.firstInstallTime:J
        //  3012: lstore          6
        //  3014: aload_1        
        //  3015: ldc_w           "UTC"
        //  3018: invokestatic    java/util/TimeZone.getTimeZone:(Ljava/lang/String;)Ljava/util/TimeZone;
        //  3021: invokevirtual   java/text/DateFormat.setTimeZone:(Ljava/util/TimeZone;)V
        //  3024: aload           12
        //  3026: ldc_w           "installDate"
        //  3029: aload_1        
        //  3030: new             Ljava/util/Date;
        //  3033: dup            
        //  3034: lload           6
        //  3036: invokespecial   java/util/Date.<init>:(J)V
        //  3039: invokevirtual   java/text/DateFormat.format:(Ljava/util/Date;)Ljava/lang/String;
        //  3042: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  3047: pop            
        //  3048: goto            3061
        //  3051: astore          10
        //  3053: ldc_w           "Exception while collecting install date. "
        //  3056: aload           10
        //  3058: invokestatic    com/appsflyer/AFLogger.afErrorLog:(Ljava/lang/String;Ljava/lang/Throwable;)V
        //  3061: aload           13
        //  3063: invokevirtual   android/content/Context.getPackageManager:()Landroid/content/pm/PackageManager;
        //  3066: aload           13
        //  3068: invokevirtual   android/content/Context.getPackageName:()Ljava/lang/String;
        //  3071: iconst_0       
        //  3072: invokevirtual   android/content/pm/PackageManager.getPackageInfo:(Ljava/lang/String;I)Landroid/content/pm/PackageInfo;
        //  3075: astore          10
        //  3077: aload           14
        //  3079: ldc_w           "versionCode"
        //  3082: iconst_0       
        //  3083: invokeinterface android/content/SharedPreferences.getInt:(Ljava/lang/String;I)I
        //  3088: istore_2       
        //  3089: aload           10
        //  3091: getfield        android/content/pm/PackageInfo.versionCode:I
        //  3094: iload_2        
        //  3095: if_icmple       3138
        //  3098: aload           10
        //  3100: getfield        android/content/pm/PackageInfo.versionCode:I
        //  3103: istore_2       
        //  3104: aload           13
        //  3106: invokestatic    com/appsflyer/AppsFlyerLibCore.getSharedPreferences:(Landroid/content/Context;)Landroid/content/SharedPreferences;
        //  3109: invokeinterface android/content/SharedPreferences.edit:()Landroid/content/SharedPreferences$Editor;
        //  3114: astore          18
        //  3116: aload           18
        //  3118: ldc_w           "versionCode"
        //  3121: iload_2        
        //  3122: invokeinterface android/content/SharedPreferences$Editor.putInt:(Ljava/lang/String;I)Landroid/content/SharedPreferences$Editor;
        //  3127: pop            
        //  3128: aload           18
        //  3130: invokeinterface android/content/SharedPreferences$Editor.apply:()V
        //  3135: goto            3138
        //  3138: aload           12
        //  3140: ldc_w           "app_version_code"
        //  3143: aload           10
        //  3145: getfield        android/content/pm/PackageInfo.versionCode:I
        //  3148: invokestatic    java/lang/Integer.toString:(I)Ljava/lang/String;
        //  3151: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  3156: pop            
        //  3157: aload           12
        //  3159: ldc_w           "app_version_name"
        //  3162: aload           10
        //  3164: getfield        android/content/pm/PackageInfo.versionName:Ljava/lang/String;
        //  3167: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  3172: pop            
        //  3173: aload           10
        //  3175: getfield        android/content/pm/PackageInfo.firstInstallTime:J
        //  3178: lstore          6
        //  3180: aload           10
        //  3182: getfield        android/content/pm/PackageInfo.lastUpdateTime:J
        //  3185: lstore          8
        //  3187: aload           12
        //  3189: ldc_w           "date1"
        //  3192: new             Ljava/text/SimpleDateFormat;
        //  3195: dup            
        //  3196: ldc_w           "yyyy-MM-dd_HHmmssZ"
        //  3199: getstatic       java/util/Locale.US:Ljava/util/Locale;
        //  3202: invokespecial   java/text/SimpleDateFormat.<init>:(Ljava/lang/String;Ljava/util/Locale;)V
        //  3205: new             Ljava/util/Date;
        //  3208: dup            
        //  3209: lload           6
        //  3211: invokespecial   java/util/Date.<init>:(J)V
        //  3214: invokevirtual   java/text/DateFormat.format:(Ljava/util/Date;)Ljava/lang/String;
        //  3217: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  3222: pop            
        //  3223: aload           12
        //  3225: ldc_w           "date2"
        //  3228: new             Ljava/text/SimpleDateFormat;
        //  3231: dup            
        //  3232: ldc_w           "yyyy-MM-dd_HHmmssZ"
        //  3235: getstatic       java/util/Locale.US:Ljava/util/Locale;
        //  3238: invokespecial   java/text/SimpleDateFormat.<init>:(Ljava/lang/String;Ljava/util/Locale;)V
        //  3241: new             Ljava/util/Date;
        //  3244: dup            
        //  3245: lload           8
        //  3247: invokespecial   java/util/Date.<init>:(J)V
        //  3250: invokevirtual   java/text/DateFormat.format:(Ljava/util/Date;)Ljava/lang/String;
        //  3253: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  3258: pop            
        //  3259: aload           12
        //  3261: ldc_w           "firstLaunchDate"
        //  3264: aload_1        
        //  3265: aload           13
        //  3267: invokestatic    com/appsflyer/AppsFlyerLibCore.\u01c3:(Ljava/text/SimpleDateFormat;Landroid/content/Context;)Ljava/lang/String;
        //  3270: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  3275: pop            
        //  3276: goto            3286
        //  3279: ldc_w           "Exception while collecting app version data "
        //  3282: aload_1        
        //  3283: invokestatic    com/appsflyer/AFLogger.afErrorLog:(Ljava/lang/String;Ljava/lang/Throwable;)V
        //  3286: aload_0        
        //  3287: aload           13
        //  3289: invokestatic    com/appsflyer/internal/af.\u01c3:(Landroid/content/Context;)Z
        //  3292: putfield        com/appsflyer/AppsFlyerLibCore.\u01c0:Z
        //  3295: new             Ljava/lang/StringBuilder;
        //  3298: dup            
        //  3299: ldc_w           "didConfigureTokenRefreshService="
        //  3302: invokespecial   java/lang/StringBuilder.<init>:(Ljava/lang/String;)V
        //  3305: astore_1       
        //  3306: aload_1        
        //  3307: aload_0        
        //  3308: getfield        com/appsflyer/AppsFlyerLibCore.\u01c0:Z
        //  3311: invokevirtual   java/lang/StringBuilder.append:(Z)Ljava/lang/StringBuilder;
        //  3314: pop            
        //  3315: aload_1        
        //  3316: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
        //  3319: invokestatic    com/appsflyer/AFLogger.afDebugLog:(Ljava/lang/String;)V
        //  3322: aload_0        
        //  3323: getfield        com/appsflyer/AppsFlyerLibCore.\u01c0:Z
        //  3326: ifne            3343
        //  3329: aload           12
        //  3331: ldc_w           "tokenRefreshConfigured"
        //  3334: getstatic       java/lang/Boolean.FALSE:Ljava/lang/Boolean;
        //  3337: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  3342: pop            
        //  3343: iload           4
        //  3345: ifeq            3424
        //  3348: invokestatic    com/appsflyer/AFDeepLinkManager.getInstance:()Lcom/appsflyer/AFDeepLinkManager;
        //  3351: pop            
        //  3352: aload           16
        //  3354: aload           13
        //  3356: aload           12
        //  3358: invokestatic    com/appsflyer/AFDeepLinkManager.\u0269:(Landroid/content/Intent;Landroid/content/Context;Ljava/util/Map;)V
        //  3361: aload_0        
        //  3362: getfield        com/appsflyer/AppsFlyerLibCore.\u0197:Ljava/lang/String;
        //  3365: ifnull          3406
        //  3368: new             Lorg/json/JSONObject;
        //  3371: dup            
        //  3372: aload_0        
        //  3373: getfield        com/appsflyer/AppsFlyerLibCore.\u0197:Ljava/lang/String;
        //  3376: invokespecial   org/json/JSONObject.<init>:(Ljava/lang/String;)V
        //  3379: astore_1       
        //  3380: aload_1        
        //  3381: ldc_w           "isPush"
        //  3384: ldc_w           "true"
        //  3387: invokevirtual   org/json/JSONObject.put:(Ljava/lang/String;Ljava/lang/Object;)Lorg/json/JSONObject;
        //  3390: pop            
        //  3391: aload           12
        //  3393: ldc_w           "af_deeplink"
        //  3396: aload_1        
        //  3397: invokevirtual   org/json/JSONObject.toString:()Ljava/lang/String;
        //  3400: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  3405: pop            
        //  3406: aload_0        
        //  3407: aconst_null    
        //  3408: putfield        com/appsflyer/AppsFlyerLibCore.\u0197:Ljava/lang/String;
        //  3411: aload           12
        //  3413: ldc_w           "open_referrer"
        //  3416: aload           17
        //  3418: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  3423: pop            
        //  3424: iload           4
        //  3426: ifne            3534
        //  3429: aload           13
        //  3431: invokestatic    com/appsflyer/AFSensorManager.\u0269:(Landroid/content/Context;)Lcom/appsflyer/AFSensorManager;
        //  3434: astore          10
        //  3436: new             Ljava/util/concurrent/ConcurrentHashMap;
        //  3439: dup            
        //  3440: invokespecial   java/util/concurrent/ConcurrentHashMap.<init>:()V
        //  3443: astore_1       
        //  3444: aload           10
        //  3446: invokevirtual   com/appsflyer/AFSensorManager.\u0269:()Ljava/util/List;
        //  3449: astore          10
        //  3451: aload           10
        //  3453: invokeinterface java/util/List.isEmpty:()Z
        //  3458: ifne            3479
        //  3461: aload_1        
        //  3462: ldc_w           "sensors"
        //  3465: aload           10
        //  3467: invokestatic    com/appsflyer/internal/g.\u0131:(Ljava/util/List;)Ljava/util/Map;
        //  3470: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  3475: pop            
        //  3476: goto            3492
        //  3479: aload_1        
        //  3480: ldc_w           "sensors"
        //  3483: ldc_w           "na"
        //  3486: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  3491: pop            
        //  3492: aload           12
        //  3494: aload_1        
        //  3495: invokeinterface java/util/Map.putAll:(Ljava/util/Map;)V
        //  3500: goto            3534
        //  3503: astore_1       
        //  3504: new             Ljava/lang/StringBuilder;
        //  3507: dup            
        //  3508: ldc_w           "Unexpected exception from AFSensorManager: "
        //  3511: invokespecial   java/lang/StringBuilder.<init>:(Ljava/lang/String;)V
        //  3514: astore          10
        //  3516: aload           10
        //  3518: aload_1        
        //  3519: invokevirtual   java/lang/Throwable.getMessage:()Ljava/lang/String;
        //  3522: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  3525: pop            
        //  3526: aload           10
        //  3528: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
        //  3531: invokestatic    com/appsflyer/AFLogger.afRDLog:(Ljava/lang/String;)V
        //  3534: invokestatic    com/appsflyer/AppsFlyerProperties.getInstance:()Lcom/appsflyer/AppsFlyerProperties;
        //  3537: ldc_w           "advertiserId"
        //  3540: invokevirtual   com/appsflyer/AppsFlyerProperties.getString:(Ljava/lang/String;)Ljava/lang/String;
        //  3543: ifnonnull       3584
        //  3546: aload           13
        //  3548: aload           12
        //  3550: invokestatic    com/appsflyer/internal/v.\u03b9:(Landroid/content/Context;Ljava/util/Map;)V
        //  3553: invokestatic    com/appsflyer/AppsFlyerProperties.getInstance:()Lcom/appsflyer/AppsFlyerProperties;
        //  3556: ldc_w           "advertiserId"
        //  3559: invokevirtual   com/appsflyer/AppsFlyerProperties.getString:(Ljava/lang/String;)Ljava/lang/String;
        //  3562: ifnull          4865
        //  3565: ldc_w           "true"
        //  3568: astore_1       
        //  3569: aload           12
        //  3571: ldc_w           "GAID_retry"
        //  3574: aload_1        
        //  3575: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  3580: pop            
        //  3581: goto            3584
        //  3584: aload           13
        //  3586: invokevirtual   android/content/Context.getContentResolver:()Landroid/content/ContentResolver;
        //  3589: invokestatic    com/appsflyer/internal/v.\u0399:(Landroid/content/ContentResolver;)Lcom/appsflyer/internal/w;
        //  3592: astore_1       
        //  3593: aload_1        
        //  3594: ifnull          3630
        //  3597: aload           12
        //  3599: ldc_w           "amazon_aid"
        //  3602: aload_1        
        //  3603: getfield        com/appsflyer/internal/w.\u03b9:Ljava/lang/String;
        //  3606: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  3611: pop            
        //  3612: aload           12
        //  3614: ldc_w           "amazon_aid_limit"
        //  3617: aload_1        
        //  3618: invokevirtual   com/appsflyer/internal/w.\u0269:()Z
        //  3621: invokestatic    java/lang/String.valueOf:(Z)Ljava/lang/String;
        //  3624: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  3629: pop            
        //  3630: aload           12
        //  3632: ldc_w           "registeredUninstall"
        //  3635: aload           14
        //  3637: ldc_w           "sentRegisterRequestToAF"
        //  3640: iconst_0       
        //  3641: invokeinterface android/content/SharedPreferences.getBoolean:(Ljava/lang/String;Z)Z
        //  3646: invokestatic    java/lang/Boolean.valueOf:(Z)Ljava/lang/Boolean;
        //  3649: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  3654: pop            
        //  3655: aload_0        
        //  3656: aload           14
        //  3658: iload           4
        //  3660: invokevirtual   com/appsflyer/AppsFlyerLibCore.getLaunchCounter:(Landroid/content/SharedPreferences;Z)I
        //  3663: istore_2       
        //  3664: aload           12
        //  3666: ldc_w           "counter"
        //  3669: iload_2        
        //  3670: invokestatic    java/lang/Integer.toString:(I)Ljava/lang/String;
        //  3673: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  3678: pop            
        //  3679: aload           11
        //  3681: ifnull          4872
        //  3684: iconst_1       
        //  3685: istore_3       
        //  3686: goto            3689
        //  3689: aload           12
        //  3691: ldc_w           "iaecounter"
        //  3694: aload           14
        //  3696: ldc_w           "appsFlyerInAppEventCount"
        //  3699: iload_3        
        //  3700: invokestatic    com/appsflyer/AppsFlyerLibCore.\u0399:(Landroid/content/SharedPreferences;Ljava/lang/String;Z)I
        //  3703: invokestatic    java/lang/Integer.toString:(I)Ljava/lang/String;
        //  3706: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  3711: pop            
        //  3712: iload           4
        //  3714: ifeq            3751
        //  3717: iload_2        
        //  3718: iconst_1       
        //  3719: if_icmpne       3751
        //  3722: aload           15
        //  3724: invokevirtual   com/appsflyer/AppsFlyerProperties.setFirstLaunchCalled:()V
        //  3727: ldc_w           "waitForCustomerId"
        //  3730: invokestatic    com/appsflyer/AppsFlyerLibCore.\u0269:(Ljava/lang/String;)Z
        //  3733: ifeq            3751
        //  3736: aload           12
        //  3738: ldc_w           "wait_cid"
        //  3741: iconst_1       
        //  3742: invokestatic    java/lang/Boolean.toString:(Z)Ljava/lang/String;
        //  3745: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  3750: pop            
        //  3751: aload           12
        //  3753: ldc_w           "isFirstCall"
        //  3756: aload           14
        //  3758: invokestatic    com/appsflyer/AppsFlyerLibCore.\u0131:(Landroid/content/SharedPreferences;)Z
        //  3761: iconst_1       
        //  3762: ixor           
        //  3763: invokestatic    java/lang/Boolean.toString:(Z)Ljava/lang/String;
        //  3766: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  3771: pop            
        //  3772: new             Ljava/util/HashMap;
        //  3775: dup            
        //  3776: invokespecial   java/util/HashMap.<init>:()V
        //  3779: astore_1       
        //  3780: aload_1        
        //  3781: ldc_w           "cpu_abi"
        //  3784: ldc_w           "ro.product.cpu.abi"
        //  3787: invokestatic    com/appsflyer/AppsFlyerLibCore.\u03b9:(Ljava/lang/String;)Ljava/lang/String;
        //  3790: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  3795: pop            
        //  3796: aload_1        
        //  3797: ldc_w           "cpu_abi2"
        //  3800: ldc_w           "ro.product.cpu.abi2"
        //  3803: invokestatic    com/appsflyer/AppsFlyerLibCore.\u03b9:(Ljava/lang/String;)Ljava/lang/String;
        //  3806: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  3811: pop            
        //  3812: aload_1        
        //  3813: ldc_w           "arch"
        //  3816: ldc_w           "os.arch"
        //  3819: invokestatic    com/appsflyer/AppsFlyerLibCore.\u03b9:(Ljava/lang/String;)Ljava/lang/String;
        //  3822: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  3827: pop            
        //  3828: aload_1        
        //  3829: ldc_w           "build_display_id"
        //  3832: ldc_w           "ro.build.display.id"
        //  3835: invokestatic    com/appsflyer/AppsFlyerLibCore.\u03b9:(Ljava/lang/String;)Ljava/lang/String;
        //  3838: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  3843: pop            
        //  3844: iload           4
        //  3846: ifeq            4105
        //  3849: aload_0        
        //  3850: getfield        com/appsflyer/AppsFlyerLibCore.\u024d:Z
        //  3853: ifeq            3962
        //  3856: getstatic       com/appsflyer/internal/p$a.\u0399:Lcom/appsflyer/internal/p;
        //  3859: astore          10
        //  3861: aload           13
        //  3863: invokestatic    com/appsflyer/internal/p.\u0131:(Landroid/content/Context;)Landroid/location/Location;
        //  3866: astore          10
        //  3868: new             Ljava/util/HashMap;
        //  3871: dup            
        //  3872: iconst_3       
        //  3873: invokespecial   java/util/HashMap.<init>:(I)V
        //  3876: astore          11
        //  3878: aload           10
        //  3880: ifnull          3940
        //  3883: aload           11
        //  3885: ldc_w           "lat"
        //  3888: aload           10
        //  3890: invokevirtual   android/location/Location.getLatitude:()D
        //  3893: invokestatic    java/lang/String.valueOf:(D)Ljava/lang/String;
        //  3896: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  3901: pop            
        //  3902: aload           11
        //  3904: ldc_w           "lon"
        //  3907: aload           10
        //  3909: invokevirtual   android/location/Location.getLongitude:()D
        //  3912: invokestatic    java/lang/String.valueOf:(D)Ljava/lang/String;
        //  3915: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  3920: pop            
        //  3921: aload           11
        //  3923: ldc_w           "ts"
        //  3926: aload           10
        //  3928: invokevirtual   android/location/Location.getTime:()J
        //  3931: invokestatic    java/lang/String.valueOf:(J)Ljava/lang/String;
        //  3934: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  3939: pop            
        //  3940: aload           11
        //  3942: invokeinterface java/util/Map.isEmpty:()Z
        //  3947: ifne            3962
        //  3950: aload_1        
        //  3951: ldc_w           "loc"
        //  3954: aload           11
        //  3956: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  3961: pop            
        //  3962: getstatic       com/appsflyer/internal/b$e.\u0131:Lcom/appsflyer/internal/b;
        //  3965: aload           13
        //  3967: invokevirtual   com/appsflyer/internal/b.\u0399:(Landroid/content/Context;)Lcom/appsflyer/internal/b$d;
        //  3970: astore          10
        //  3972: aload_1        
        //  3973: ldc_w           "btl"
        //  3976: aload           10
        //  3978: getfield        com/appsflyer/internal/b$d.\u0131:F
        //  3981: invokestatic    java/lang/Float.toString:(F)Ljava/lang/String;
        //  3984: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  3989: pop            
        //  3990: aload           10
        //  3992: getfield        com/appsflyer/internal/b$d.\u0399:Ljava/lang/String;
        //  3995: ifnull          4013
        //  3998: aload_1        
        //  3999: ldc_w           "btch"
        //  4002: aload           10
        //  4004: getfield        com/appsflyer/internal/b$d.\u0399:Ljava/lang/String;
        //  4007: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  4012: pop            
        //  4013: iload_2        
        //  4014: iconst_2       
        //  4015: if_icmpgt       4105
        //  4018: aload           13
        //  4020: invokestatic    com/appsflyer/AFSensorManager.\u0269:(Landroid/content/Context;)Lcom/appsflyer/AFSensorManager;
        //  4023: astore          11
        //  4025: new             Ljava/util/concurrent/ConcurrentHashMap;
        //  4028: dup            
        //  4029: invokespecial   java/util/concurrent/ConcurrentHashMap.<init>:()V
        //  4032: astore          10
        //  4034: aload           11
        //  4036: invokevirtual   com/appsflyer/AFSensorManager.\u03b9:()Ljava/util/List;
        //  4039: astore          16
        //  4041: aload           16
        //  4043: invokeinterface java/util/List.isEmpty:()Z
        //  4048: ifne            4067
        //  4051: aload           10
        //  4053: ldc_w           "sensors"
        //  4056: aload           16
        //  4058: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  4063: pop            
        //  4064: goto            4097
        //  4067: aload           11
        //  4069: invokevirtual   com/appsflyer/AFSensorManager.\u0269:()Ljava/util/List;
        //  4072: astore          11
        //  4074: aload           11
        //  4076: invokeinterface java/util/List.isEmpty:()Z
        //  4081: ifne            4097
        //  4084: aload           10
        //  4086: ldc_w           "sensors"
        //  4089: aload           11
        //  4091: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  4096: pop            
        //  4097: aload_1        
        //  4098: aload           10
        //  4100: invokeinterface java/util/Map.putAll:(Ljava/util/Map;)V
        //  4105: aload_1        
        //  4106: ldc_w           "dim"
        //  4109: aload           13
        //  4111: invokestatic    com/appsflyer/internal/q.\u0399:(Landroid/content/Context;)Ljava/util/Map;
        //  4114: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  4119: pop            
        //  4120: aload           12
        //  4122: ldc_w           "deviceData"
        //  4125: aload_1        
        //  4126: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  4131: pop            
        //  4132: aload           12
        //  4134: ldc_w           "appsflyerKey"
        //  4137: invokeinterface java/util/Map.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //  4142: checkcast       Ljava/lang/String;
        //  4145: astore_1       
        //  4146: aload           12
        //  4148: ldc_w           "af_timestamp"
        //  4151: invokeinterface java/util/Map.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //  4156: checkcast       Ljava/lang/String;
        //  4159: astore          10
        //  4161: aload           12
        //  4163: ldc_w           "uid"
        //  4166: invokeinterface java/util/Map.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //  4171: checkcast       Ljava/lang/String;
        //  4174: astore          11
        //  4176: new             Ljava/lang/StringBuilder;
        //  4179: dup            
        //  4180: invokespecial   java/lang/StringBuilder.<init>:()V
        //  4183: astore          16
        //  4185: aload           16
        //  4187: aload_1        
        //  4188: iconst_0       
        //  4189: bipush          7
        //  4191: invokevirtual   java/lang/String.substring:(II)Ljava/lang/String;
        //  4194: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  4197: pop            
        //  4198: aload           16
        //  4200: aload           11
        //  4202: iconst_0       
        //  4203: bipush          7
        //  4205: invokevirtual   java/lang/String.substring:(II)Ljava/lang/String;
        //  4208: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  4211: pop            
        //  4212: aload           16
        //  4214: aload           10
        //  4216: aload           10
        //  4218: invokevirtual   java/lang/String.length:()I
        //  4221: bipush          7
        //  4223: isub           
        //  4224: invokevirtual   java/lang/String.substring:(I)Ljava/lang/String;
        //  4227: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  4230: pop            
        //  4231: aload           12
        //  4233: ldc_w           "af_v"
        //  4236: aload           16
        //  4238: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
        //  4241: invokestatic    com/appsflyer/internal/z.\u0399:(Ljava/lang/String;)Ljava/lang/String;
        //  4244: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  4249: pop            
        //  4250: aload           12
        //  4252: ldc_w           "appsflyerKey"
        //  4255: invokeinterface java/util/Map.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //  4260: checkcast       Ljava/lang/String;
        //  4263: astore_1       
        //  4264: new             Ljava/lang/StringBuilder;
        //  4267: dup            
        //  4268: invokespecial   java/lang/StringBuilder.<init>:()V
        //  4271: astore          10
        //  4273: aload           10
        //  4275: aload_1        
        //  4276: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  4279: pop            
        //  4280: aload           10
        //  4282: aload           12
        //  4284: ldc_w           "af_timestamp"
        //  4287: invokeinterface java/util/Map.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //  4292: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //  4295: pop            
        //  4296: aload           10
        //  4298: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
        //  4301: astore_1       
        //  4302: new             Ljava/lang/StringBuilder;
        //  4305: dup            
        //  4306: invokespecial   java/lang/StringBuilder.<init>:()V
        //  4309: astore          10
        //  4311: aload           10
        //  4313: aload_1        
        //  4314: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  4317: pop            
        //  4318: aload           10
        //  4320: aload           12
        //  4322: ldc_w           "uid"
        //  4325: invokeinterface java/util/Map.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //  4330: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //  4333: pop            
        //  4334: aload           10
        //  4336: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
        //  4339: astore_1       
        //  4340: new             Ljava/lang/StringBuilder;
        //  4343: dup            
        //  4344: invokespecial   java/lang/StringBuilder.<init>:()V
        //  4347: astore          10
        //  4349: aload           10
        //  4351: aload_1        
        //  4352: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  4355: pop            
        //  4356: aload           10
        //  4358: aload           12
        //  4360: ldc_w           "installDate"
        //  4363: invokeinterface java/util/Map.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //  4368: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //  4371: pop            
        //  4372: aload           10
        //  4374: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
        //  4377: astore_1       
        //  4378: new             Ljava/lang/StringBuilder;
        //  4381: dup            
        //  4382: invokespecial   java/lang/StringBuilder.<init>:()V
        //  4385: astore          10
        //  4387: aload           10
        //  4389: aload_1        
        //  4390: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  4393: pop            
        //  4394: aload           10
        //  4396: aload           12
        //  4398: ldc_w           "counter"
        //  4401: invokeinterface java/util/Map.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //  4406: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //  4409: pop            
        //  4410: aload           10
        //  4412: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
        //  4415: astore_1       
        //  4416: new             Ljava/lang/StringBuilder;
        //  4419: dup            
        //  4420: invokespecial   java/lang/StringBuilder.<init>:()V
        //  4423: astore          10
        //  4425: aload           10
        //  4427: aload_1        
        //  4428: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //  4431: pop            
        //  4432: aload           10
        //  4434: aload           12
        //  4436: ldc_w           "iaecounter"
        //  4439: invokeinterface java/util/Map.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //  4444: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //  4447: pop            
        //  4448: aload           12
        //  4450: ldc_w           "af_v2"
        //  4453: aload           10
        //  4455: invokevirtual   java/lang/Object.toString:()Ljava/lang/String;
        //  4458: invokestatic    com/appsflyer/internal/z.\u01c3:(Ljava/lang/String;)Ljava/lang/String;
        //  4461: invokestatic    com/appsflyer/internal/z.\u0399:(Ljava/lang/String;)Ljava/lang/String;
        //  4464: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  4469: pop            
        //  4470: aload           12
        //  4472: ldc_w           "ivc"
        //  4475: aload           13
        //  4477: invokestatic    com/appsflyer/AppsFlyerLibCore.\u0406:(Landroid/content/Context;)Z
        //  4480: invokestatic    java/lang/Boolean.valueOf:(Z)Ljava/lang/Boolean;
        //  4483: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  4488: pop            
        //  4489: aload           14
        //  4491: ldc             "is_stop_tracking_used"
        //  4493: invokeinterface android/content/SharedPreferences.contains:(Ljava/lang/String;)Z
        //  4498: ifeq            4525
        //  4501: aload           12
        //  4503: ldc_w           "istu"
        //  4506: aload           14
        //  4508: ldc             "is_stop_tracking_used"
        //  4510: iconst_0       
        //  4511: invokeinterface android/content/SharedPreferences.getBoolean:(Ljava/lang/String;Z)Z
        //  4516: invokestatic    java/lang/String.valueOf:(Z)Ljava/lang/String;
        //  4519: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  4524: pop            
        //  4525: aload           15
        //  4527: ldc_w           "consumeAfDeepLink"
        //  4530: invokevirtual   com/appsflyer/AppsFlyerProperties.getObject:(Ljava/lang/String;)Ljava/lang/Object;
        //  4533: ifnull          4559
        //  4536: aload           12
        //  4538: ldc_w           "is_dp_api"
        //  4541: aload           15
        //  4543: ldc_w           "consumeAfDeepLink"
        //  4546: iconst_0       
        //  4547: invokevirtual   com/appsflyer/AppsFlyerProperties.getBoolean:(Ljava/lang/String;Z)Z
        //  4550: invokestatic    java/lang/String.valueOf:(Z)Ljava/lang/String;
        //  4553: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  4558: pop            
        //  4559: new             Ljava/util/HashMap;
        //  4562: dup            
        //  4563: invokespecial   java/util/HashMap.<init>:()V
        //  4566: astore_1       
        //  4567: aload_1        
        //  4568: ldc_w           "mcc"
        //  4571: aload           13
        //  4573: invokevirtual   android/content/Context.getResources:()Landroid/content/res/Resources;
        //  4576: invokevirtual   android/content/res/Resources.getConfiguration:()Landroid/content/res/Configuration;
        //  4579: getfield        android/content/res/Configuration.mcc:I
        //  4582: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  4585: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  4590: pop            
        //  4591: aload_1        
        //  4592: ldc_w           "mnc"
        //  4595: aload           13
        //  4597: invokevirtual   android/content/Context.getResources:()Landroid/content/res/Resources;
        //  4600: invokevirtual   android/content/res/Resources.getConfiguration:()Landroid/content/res/Configuration;
        //  4603: getfield        android/content/res/Configuration.mnc:I
        //  4606: invokestatic    java/lang/Integer.valueOf:(I)Ljava/lang/Integer;
        //  4609: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  4614: pop            
        //  4615: aload           12
        //  4617: ldc_w           "cell"
        //  4620: aload_1        
        //  4621: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  4626: pop            
        //  4627: new             Lcom/appsflyer/internal/EventDataCollector;
        //  4630: dup            
        //  4631: aload           13
        //  4633: invokespecial   com/appsflyer/internal/EventDataCollector.<init>:(Landroid/content/Context;)V
        //  4636: astore_1       
        //  4637: aload           12
        //  4639: ldc_w           "sig"
        //  4642: aload_1        
        //  4643: invokevirtual   com/appsflyer/internal/EventDataCollector.signature:()Ljava/lang/String;
        //  4646: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  4651: pop            
        //  4652: aload           12
        //  4654: ldc_w           "last_boot_time"
        //  4657: aload_1        
        //  4658: invokevirtual   com/appsflyer/internal/EventDataCollector.bootTime:()J
        //  4661: invokestatic    java/lang/Long.valueOf:(J)Ljava/lang/Long;
        //  4664: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  4669: pop            
        //  4670: aload           12
        //  4672: ldc_w           "disk"
        //  4675: aload_1        
        //  4676: invokevirtual   com/appsflyer/internal/EventDataCollector.disk:()Ljava/lang/String;
        //  4679: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  4684: pop            
        //  4685: aload_0        
        //  4686: getfield        com/appsflyer/AppsFlyerLibCore.\u0491:[Ljava/lang/String;
        //  4689: ifnull          4737
        //  4692: aload           12
        //  4694: ldc_w           "sharing_filter"
        //  4697: aload_0        
        //  4698: getfield        com/appsflyer/AppsFlyerLibCore.\u0491:[Ljava/lang/String;
        //  4701: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //  4706: pop            
        //  4707: aload           12
        //  4709: areturn        
        //  4710: ldc_w           "AppsFlyer dev key is missing!!! Please use  AppsFlyerLib.getInstance().setAppsFlyerKey(...) to set it. "
        //  4713: invokestatic    com/appsflyer/AFLogger.afInfoLog:(Ljava/lang/String;)V
        //  4716: ldc_w           "AppsFlyer will not track this event."
        //  4719: invokestatic    com/appsflyer/AFLogger.afInfoLog:(Ljava/lang/String;)V
        //  4722: aconst_null    
        //  4723: areturn        
        //  4724: astore_1       
        //  4725: goto            4729
        //  4728: astore_1       
        //  4729: aload_1        
        //  4730: invokevirtual   java/lang/Throwable.getLocalizedMessage:()Ljava/lang/String;
        //  4733: aload_1        
        //  4734: invokestatic    com/appsflyer/AFLogger.afErrorLog:(Ljava/lang/String;Ljava/lang/Throwable;)V
        //  4737: aload           12
        //  4739: areturn        
        //  4740: ldc_w           "SDK tracking has been stopped"
        //  4743: astore_1       
        //  4744: goto            203
        //  4747: ldc2_w          -1
        //  4750: lstore          6
        //  4752: goto            630
        //  4755: goto            1067
        //  4758: astore_1       
        //  4759: goto            4763
        //  4762: astore_1       
        //  4763: goto            1060
        //  4766: goto            1006
        //  4769: aload           10
        //  4771: ifnonnull       1276
        //  4774: aload_1        
        //  4775: ifnull          1276
        //  4778: goto            1264
        //  4781: goto            1358
        //  4784: aconst_null    
        //  4785: astore_1       
        //  4786: goto            1358
        //  4789: aconst_null    
        //  4790: astore_1       
        //  4791: aload_1        
        //  4792: ifnull          4798
        //  4795: goto            1592
        //  4798: aload           13
        //  4800: ifnonnull       1572
        //  4803: aconst_null    
        //  4804: astore_1       
        //  4805: goto            1592
        //  4808: aload           13
        //  4810: ifnonnull       1691
        //  4813: aconst_null    
        //  4814: astore_1       
        //  4815: goto            1708
        //  4818: aconst_null    
        //  4819: astore_1       
        //  4820: goto            2116
        //  4823: aconst_null    
        //  4824: astore_1       
        //  4825: goto            2322
        //  4828: aconst_null    
        //  4829: astore_1       
        //  4830: goto            2353
        //  4833: goto            2377
        //  4836: aconst_null    
        //  4837: astore_1       
        //  4838: goto            2377
        //  4841: aconst_null    
        //  4842: astore_1       
        //  4843: goto            2545
        //  4846: aconst_null    
        //  4847: astore_1       
        //  4848: goto            2573
        //  4851: iconst_0       
        //  4852: istore_3       
        //  4853: goto            2650
        //  4856: aconst_null    
        //  4857: astore_1       
        //  4858: goto            2764
        //  4861: astore_1       
        //  4862: goto            3279
        //  4865: ldc_w           "false"
        //  4868: astore_1       
        //  4869: goto            3569
        //  4872: iconst_0       
        //  4873: istore_3       
        //  4874: goto            3689
        //    Signature:
        //  (Lcom/appsflyer/AFEvent;)Ljava/util/Map<Ljava/lang/String;Ljava/lang/Object;>;
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                                     
        //  -----  -----  -----  -----  ---------------------------------------------------------
        //  156    175    4728   4729   Any
        //  190    203    4728   4729   Any
        //  203    207    4728   4729   Any
        //  210    214    4728   4729   Any
        //  214    234    237    247    Ljava/lang/Exception;
        //  214    234    4728   4729   Any
        //  238    247    4728   4729   Any
        //  247    288    327    335    Ljava/lang/Exception;
        //  247    288    4728   4729   Any
        //  288    306    327    335    Ljava/lang/Exception;
        //  288    306    4728   4729   Any
        //  306    324    327    335    Ljava/lang/Exception;
        //  306    324    4728   4729   Any
        //  328    335    4728   4729   Any
        //  335    448    4728   4729   Any
        //  453    488    4728   4729   Any
        //  488    510    4728   4729   Any
        //  510    515    4728   4729   Any
        //  518    527    4728   4729   Any
        //  534    556    4728   4729   Any
        //  556    578    4728   4729   Any
        //  578    609    4728   4729   Any
        //  616    627    4728   4729   Any
        //  630    667    4728   4729   Any
        //  671    683    4728   4729   Any
        //  688    701    4728   4729   Any
        //  701    714    4728   4729   Any
        //  721    762    4728   4729   Any
        //  762    784    4728   4729   Any
        //  784    805    4728   4729   Any
        //  805    817    4728   4729   Any
        //  821    833    4728   4729   Any
        //  833    873    4728   4729   Any
        //  876    890    4728   4729   Any
        //  890    902    4762   4763   Ljava/lang/Exception;
        //  890    902    4728   4729   Any
        //  907    925    4762   4763   Ljava/lang/Exception;
        //  907    925    4728   4729   Any
        //  925    1003   4758   4762   Ljava/lang/Exception;
        //  925    1003   1056   1060   Any
        //  1006   1053   4758   4762   Ljava/lang/Exception;
        //  1006   1053   1056   1060   Any
        //  1060   1067   4724   4728   Any
        //  1067   1088   4724   4728   Any
        //  1097   1133   1056   1060   Any
        //  1133   1143   4724   4728   Any
        //  1147   1159   1056   1060   Any
        //  1159   1173   1192   1200   Ljava/lang/Exception;
        //  1159   1173   1056   1060   Any
        //  1177   1189   1192   1200   Ljava/lang/Exception;
        //  1177   1189   1056   1060   Any
        //  1193   1200   4724   4728   Any
        //  1200   1209   4724   4728   Any
        //  1213   1232   1056   1060   Any
        //  1232   1247   4728   4729   Any
        //  1252   1261   4728   4729   Any
        //  1264   1276   4728   4729   Any
        //  1276   1305   4728   4729   Any
        //  1308   1326   4728   4729   Any
        //  1338   1355   4728   4729   Any
        //  1358   1389   4728   4729   Any
        //  1393   1408   4728   4729   Any
        //  1408   1426   4728   4729   Any
        //  1434   1460   4728   4729   Any
        //  1466   1484   4728   4729   Any
        //  1487   1514   4728   4729   Any
        //  1517   1531   4728   4729   Any
        //  1534   1548   4728   4729   Any
        //  1548   1565   4728   4729   Any
        //  1572   1589   4728   4729   Any
        //  1599   1630   4728   4729   Any
        //  1641   1652   4728   4729   Any
        //  1659   1674   4728   4729   Any
        //  1674   1684   4728   4729   Any
        //  1691   1708   4728   4729   Any
        //  1712   1727   4728   4729   Any
        //  1732   1753   4728   4729   Any
        //  1756   1766   4728   4729   Any
        //  1770   1789   4728   4729   Any
        //  1789   1799   4728   4729   Any
        //  1803   1815   4728   4729   Any
        //  1815   1824   4728   4729   Any
        //  1833   1844   4728   4729   Any
        //  1847   1857   4728   4729   Any
        //  1866   1871   4728   4729   Any
        //  1879   1892   4728   4729   Any
        //  1897   1910   4728   4729   Any
        //  1910   1942   4728   4729   Any
        //  1942   1952   4728   4729   Any
        //  1956   2000   4728   4729   Any
        //  2000   2012   4728   4729   Any
        //  2012   2022   4728   4729   Any
        //  2026   2038   4728   4729   Any
        //  2038   2068   4728   4729   Any
        //  2072   2092   2106   2116   Landroid/content/pm/PackageManager$NameNotFoundException;
        //  2072   2092   2095   2106   Any
        //  2096   2103   4728   4729   Any
        //  2107   2113   4728   4729   Any
        //  2120   2132   4728   4729   Any
        //  2132   2163   4728   4729   Any
        //  2166   2195   4728   4729   Any
        //  2199   2215   4728   4729   Any
        //  2219   2261   2297   2362   Ljava/lang/reflect/InvocationTargetException;
        //  2219   2261   2292   2333   Ljava/lang/Exception;
        //  2219   2261   4728   4729   Any
        //  2276   2289   2297   2362   Ljava/lang/reflect/InvocationTargetException;
        //  2276   2289   2292   2333   Ljava/lang/Exception;
        //  2276   2289   4728   4729   Any
        //  2306   2319   4728   4729   Any
        //  2322   2330   4728   4729   Any
        //  2337   2350   4728   4729   Any
        //  2353   2359   4728   4729   Any
        //  2362   2374   4728   4729   Any
        //  2381   2424   4728   4729   Any
        //  2427   2433   4728   4729   Any
        //  2433   2455   4728   4729   Any
        //  2459   2475   4728   4729   Any
        //  2479   2492   2523   2558   Ljava/lang/Exception;
        //  2479   2492   4728   4729   Any
        //  2507   2520   2523   2558   Ljava/lang/Exception;
        //  2507   2520   4728   4729   Any
        //  2529   2542   4728   4729   Any
        //  2545   2555   4728   4729   Any
        //  2558   2570   4728   4729   Any
        //  2577   2620   4728   4729   Any
        //  2623   2629   4728   4729   Any
        //  2629   2645   4728   4729   Any
        //  2654   2659   4728   4729   Any
        //  2662   2673   4728   4729   Any
        //  2678   2702   2752   2755   Any
        //  2706   2712   2752   2755   Any
        //  2712   2718   2745   2752   Any
        //  2726   2739   2745   2752   Any
        //  2755   2761   4728   4729   Any
        //  2768   2808   4728   4729   Any
        //  2808   2821   2840   2872   Ljava/lang/Exception;
        //  2808   2821   4728   4729   Any
        //  2825   2837   2840   2872   Ljava/lang/Exception;
        //  2825   2837   4728   4729   Any
        //  2841   2872   4728   4729   Any
        //  2872   2889   2892   2900   Ljava/lang/Exception;
        //  2872   2889   4728   4729   Any
        //  2893   2900   4728   4729   Any
        //  2900   2917   2920   2928   Ljava/lang/Exception;
        //  2900   2917   4728   4729   Any
        //  2921   2928   4728   4729   Any
        //  2928   2945   2948   2956   Ljava/lang/Exception;
        //  2928   2945   4728   4729   Any
        //  2949   2956   4728   4729   Any
        //  2956   2995   4728   4729   Any
        //  2995   3048   3051   3061   Ljava/lang/Exception;
        //  2995   3048   4728   4729   Any
        //  3053   3061   4728   4729   Any
        //  3061   3135   4861   3286   Any
        //  3138   3276   4861   3286   Any
        //  3279   3286   4728   4729   Any
        //  3286   3343   4728   4729   Any
        //  3348   3406   4728   4729   Any
        //  3406   3424   4728   4729   Any
        //  3429   3476   3503   3534   Ljava/lang/Exception;
        //  3429   3476   4728   4729   Any
        //  3479   3492   3503   3534   Ljava/lang/Exception;
        //  3479   3492   4728   4729   Any
        //  3492   3500   3503   3534   Ljava/lang/Exception;
        //  3492   3500   4728   4729   Any
        //  3504   3534   4728   4729   Any
        //  3534   3565   4728   4729   Any
        //  3569   3581   4728   4729   Any
        //  3584   3593   4728   4729   Any
        //  3597   3630   4728   4729   Any
        //  3630   3679   4728   4729   Any
        //  3689   3712   4728   4729   Any
        //  3722   3751   4728   4729   Any
        //  3751   3844   4728   4729   Any
        //  3849   3878   4728   4729   Any
        //  3883   3940   4728   4729   Any
        //  3940   3962   4728   4729   Any
        //  3962   4013   4728   4729   Any
        //  4018   4064   4728   4729   Any
        //  4067   4097   4728   4729   Any
        //  4097   4105   4728   4729   Any
        //  4105   4525   4728   4729   Any
        //  4525   4559   4728   4729   Any
        //  4559   4707   4728   4729   Any
        //  4710   4722   4728   4729   Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index: 2029, Size: 2029
        //     at java.util.ArrayList.rangeCheck(Unknown Source)
        //     at java.util.ArrayList.get(Unknown Source)
        //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3321)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:113)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:210)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at us.deathmarine.luyten.FileSaver.doSaveJarDecompiled(FileSaver.java:192)
        //     at us.deathmarine.luyten.FileSaver.access$300(FileSaver.java:45)
        //     at us.deathmarine.luyten.FileSaver$4.run(FileSaver.java:112)
        //     at java.lang.Thread.run(Unknown Source)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    public final void \u0269(final Context context, String configuredChannel) {
        if (\u0131()) {
            AFLogger.afInfoLog("CustomerUserId not set, Tracking is disabled", true);
            return;
        }
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        final String string = AppsFlyerProperties.getInstance().getString("AppsFlyerKey");
        if (string == null) {
            AFLogger.afWarnLog("[registerUninstall] AppsFlyer's SDK cannot send any event without providing DevKey.");
            return;
        }
        final PackageManager packageManager = context.getPackageManager();
        final String packageName = context.getPackageName();
        try {
            final PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
            hashMap.put("app_version_code", Integer.toString(packageInfo.versionCode));
            hashMap.put("app_version_name", packageInfo.versionName);
            hashMap.put("app_name", packageManager.getApplicationLabel(packageInfo.applicationInfo).toString());
            final long firstInstallTime = packageInfo.firstInstallTime;
            final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd_HHmmssZ", Locale.US);
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            hashMap.put("installDate", simpleDateFormat.format(new Date(firstInstallTime)));
        }
        finally {
            final Throwable t;
            AFLogger.afErrorLog("Exception while collecting application version info.", t);
        }
        \u0269(context, hashMap);
        final String string2 = AppsFlyerProperties.getInstance().getString("AppUserId");
        if (string2 != null) {
            hashMap.put("appUserId", string2);
        }
        try {
            hashMap.put("model", Build.MODEL);
            hashMap.put("brand", Build.BRAND);
        }
        finally {
            final Throwable t2;
            AFLogger.afErrorLog("Exception while collecting device brand and model.", t2);
        }
        if (AppsFlyerProperties.getInstance().getBoolean("deviceTrackingDisabled", false)) {
            hashMap.put("deviceTrackingDisabled", "true");
        }
        final w \u03b9 = v.\u0399(context.getContentResolver());
        if (\u03b9 != null) {
            hashMap.put("amazon_aid", \u03b9.\u03b9);
            hashMap.put("amazon_aid_limit", String.valueOf(\u03b9.\u0269()));
        }
        final String string3 = AppsFlyerProperties.getInstance().getString("advertiserId");
        if (string3 != null) {
            hashMap.put("advertiserId", string3);
        }
        hashMap.put("devkey", string);
        hashMap.put("uid", ae.\u0399(new WeakReference<Context>(context)));
        hashMap.put("af_gcm_token", configuredChannel);
        hashMap.put("launch_counter", Integer.toString(this.getLaunchCounter(getSharedPreferences(context), false)));
        hashMap.put("sdk", Integer.toString(Build$VERSION.SDK_INT));
        configuredChannel = this.getConfiguredChannel(context);
        if (configuredChannel != null) {
            hashMap.put("channel", configuredChannel);
        }
        try {
            final AFEvent context2 = new UninstallTokenEvent().trackingStopped(this.isTrackingStopped()).params(hashMap).context(context);
            final StringBuilder sb = new StringBuilder();
            sb.append(ServerConfigHandler.getUrl(AppsFlyerLibCore.REGISTER_URL));
            sb.append(packageName);
            new Thread(new ad((BackgroundEvent)context2.urlString(sb.toString()))).start();
        }
        finally {
            final Throwable t3;
            AFLogger.afErrorLog(t3.getMessage(), t3);
        }
    }
    
    final void \u0269(final WeakReference<Context> weakReference) {
        if (weakReference.get() == null) {
            return;
        }
        AFLogger.afInfoLog("app went to background");
        final SharedPreferences sharedPreferences = getSharedPreferences(weakReference.get());
        AppsFlyerProperties.getInstance().saveProperties(sharedPreferences);
        final long \u017f = this.\u017f;
        final long \u0285 = this.\u0285;
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        final String string = AppsFlyerProperties.getInstance().getString("AppsFlyerKey");
        if (string == null) {
            AFLogger.afWarnLog("[callStats] AppsFlyer's SDK cannot send any event without providing DevKey.");
            return;
        }
        final String string2 = AppsFlyerProperties.getInstance().getString("KSAppsFlyerId");
        if (AppsFlyerProperties.getInstance().getBoolean("deviceTrackingDisabled", false)) {
            hashMap.put("deviceTrackingDisabled", "true");
        }
        final w \u03b9 = v.\u0399(weakReference.get().getContentResolver());
        if (\u03b9 != null) {
            hashMap.put("amazon_aid", \u03b9.\u03b9);
            hashMap.put("amazon_aid_limit", String.valueOf(\u03b9.\u0269()));
        }
        final String string3 = AppsFlyerProperties.getInstance().getString("advertiserId");
        if (string3 != null) {
            hashMap.put("advertiserId", string3);
        }
        hashMap.put("app_id", weakReference.get().getPackageName());
        hashMap.put("devkey", string);
        hashMap.put("uid", ae.\u0399(weakReference));
        hashMap.put("time_in_app", String.valueOf((\u017f - \u0285) / 1000L));
        hashMap.put("statType", "user_closed_app");
        hashMap.put("platform", "Android");
        hashMap.put("launch_counter", Integer.toString(this.getLaunchCounter(sharedPreferences, false)));
        hashMap.put("channel", this.getConfiguredChannel(weakReference.get()));
        String s;
        if (string2 != null) {
            s = string2;
        }
        else {
            s = "";
        }
        hashMap.put("originalAppsflyerId", s);
        if (this.\u0254) {
            try {
                AFLogger.afDebugLog("Running callStats task");
                new Thread(new ad((BackgroundEvent)new Stats().trackingStopped(this.isTrackingStopped()).params(hashMap).urlString(ServerConfigHandler.getUrl("https://%sstats.%s/stats")))).start();
                return;
            }
            finally {
                final Throwable t;
                AFLogger.afErrorLog("Could not send callStats request", t);
                return;
            }
        }
        AFLogger.afDebugLog("Stats call is disabled, ignore ...");
    }
    
    final class a implements Runnable
    {
        private final AFEvent \u0269;
        
        private a(final AFEvent afEvent) {
            this.\u0269 = afEvent.weakContext();
        }
        
        @Override
        public final void run() {
            final Map<String, Object> params = this.\u0269.params();
            final boolean \u03b9 = this.\u0269.\u0399();
            final String urlString = this.\u0269.urlString();
            final int \u0268 = this.\u0269.\u0268;
            final Context context = this.\u0269.context();
            if (AppsFlyerLibCore.this.isTrackingStopped()) {
                return;
            }
            Object o = new byte[0];
            if (\u03b9 && \u0268 <= 2) {
                final ArrayList<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
                if (AppsFlyerLibCore.this.\u0269()) {
                    params.put("rfr", AppsFlyerLibCore.this.\u029f.oldMap);
                    AppsFlyerLibCore.getSharedPreferences(context).edit().putBoolean("newGPReferrerSent", true).apply();
                    list.add(AppsFlyerLibCore.this.\u029f.newMap);
                }
                if (AppsFlyerLibCore.this.\u0442 != null) {
                    Object o2 = AppsFlyerLibCore.this.\u0442.map;
                    if (o2 != null) {
                        list.add((Map<String, Object>)o2);
                    }
                    else if (AppsFlyerLibCore.this.\u0442.valid() && \u0268 == 2) {
                        o2 = new HashMap<String, Object>();
                        ((Map<String, String>)o2).put("source", "huawei");
                        ((Map<String, String>)o2).put("response", "TIMEOUT");
                        ((Map<String, Object>)o2).putAll(new MandatoryFields());
                        list.add((Map<String, Object>)o2);
                    }
                }
                if (!list.isEmpty()) {
                    params.put("referrers", list);
                }
                if (AppsFlyerLibCore.this.\u037b != null) {
                    params.put("fb_ddl", AppsFlyerLibCore.this.\u037b);
                }
                if (AppsFlyerLibCore.this.\u04c0()) {
                    params.put("lvl", AppsFlyerLibCore.this.\u03f2);
                }
                else if (AppsFlyerLibCore.this.\u0441) {
                    AppsFlyerLibCore.this.\u03f2 = (Map<String, Object>)new HashMap();
                    AppsFlyerLibCore.this.\u03f2.put("error", "operation timed out.");
                }
            }
            if (!(this.\u0269 instanceof AdRevenue)) {
                params.putAll(new com.appsflyer.internal.c.a(params, context));
            }
            final Writer writer = null;
            final Writer writer2 = null;
            Object \u0269 = o;
            Object o2;
            try {
                final AFEvent \u02692 = this.\u0269;
                \u0269 = o;
                if (this.\u0269 instanceof AdRevenue) {
                    \u0269 = o;
                    o2 = params.get("af_key");
                }
                else {
                    \u0269 = o;
                    o2 = params.get("appsflyerKey");
                }
                \u0269 = o;
                o2 = o2;
                \u0269 = o;
                \u02692.key((String)o2);
                \u0269 = o;
                // monitorenter(params)
                try {
                    \u0269 = this.\u0269;
                    try {
                        final byte[] array = (byte[])((Class)d.\u01c3(48, '\u2dee', 24)).getMethod("\u0269", AFEvent.class).invoke(null, \u0269);
                        try {
                            // monitorexit(params)
                            try {
                                AppsFlyerLibCore.\u0131(AppsFlyerLibCore.this, this.\u0269.post(array));
                                return;
                            }
                            catch (IOException ex2) {}
                        }
                        finally {
                            o = array;
                        }
                    }
                    finally {
                        final Throwable cause = ((Throwable)\u0269).getCause();
                        if (cause != null) {
                            throw cause;
                        }
                    }
                }
                finally {}
                \u0269 = o;
                // monitorexit(params)
                \u0269 = o;
            }
            catch (IOException ex) {
                o2 = \u0269;
                \u0269 = ex;
            }
            AFLogger.afErrorLog("Exception while sending request to server. ", (Throwable)\u0269);
            if (o2 == null || context == null || urlString.contains("&isCachedRequest=true&timeincache=")) {
                return;
            }
            aa.\u01c3();
            final j j = new j(urlString, (byte[])o2, "5.4.1");
            Object o3 = writer2;
            Writer writer3;
            final Throwable t;
            try {
                try {
                    final File \u03b92 = aa.\u03b9(context);
                    o3 = writer2;
                    if (!\u03b92.exists()) {
                        o3 = writer2;
                        \u03b92.mkdir();
                        o3 = \u0269;
                        goto Label_0612;
                    }
                    o3 = writer2;
                    final File[] listFiles = \u03b92.listFiles();
                    if (listFiles != null) {
                        o3 = writer2;
                        if (listFiles.length > 40) {
                            o3 = writer2;
                            Log.i("AppsFlyer_5.4.1", "reached cache limit, not caching request");
                            o3 = \u0269;
                            goto Label_0612;
                        }
                    }
                    o3 = writer2;
                    Log.i("AppsFlyer_5.4.1", "caching request...");
                    o3 = writer2;
                    final File file = new File(aa.\u03b9(context), Long.toString(System.currentTimeMillis()));
                    o3 = writer2;
                    file.createNewFile();
                    o3 = writer2;
                    writer3 = new OutputStreamWriter(new FileOutputStream(file.getPath(), true));
                    try {
                        writer3.write("version=");
                        writer3.write(j.\u0269);
                        writer3.write(10);
                        writer3.write("url=");
                        writer3.write(j.\u0131);
                        writer3.write(10);
                        writer3.write("data=");
                        writer3.write(Base64.encodeToString(j.\u01c3(), 2));
                        writer3.write(10);
                        writer3.flush();
                        final Writer writer4 = writer3;
                        writer4.close();
                    }
                    catch (Exception ex3) {}
                    finally {
                        \u0269 = t;
                        final Writer writer5 = (Writer)(o3 = writer3);
                    }
                }
                finally {
                    final Throwable t2;
                    \u0269 = t2;
                }
            }
            catch (Exception ex4) {
                writer3 = writer;
            }
            Label_0996: {
                try {
                    final Writer writer4 = writer3;
                    writer4.close();
                    \u0269 = t;
                    o3 = writer3;
                    break Label_0996;
                    while (true) {
                        writer3.close();
                        o3 = \u0269;
                        goto Label_0612;
                        Log.i("AppsFlyer_5.4.1", "Could not cache request");
                        o3 = \u0269;
                        continue;
                    }
                }
                // iftrue(Label_0612:, writer3 == null)
                catch (IOException ex5) {
                    o3 = \u0269;
                    goto Label_0612;
                }
                goto Label_0612;
            }
            if (o3 != null) {
                try {
                    ((Writer)o3).close();
                }
                catch (IOException ex6) {}
            }
        }
    }
    
    static final class b implements Runnable
    {
        private static final List<String> \u0269;
        private static String \u03b9 = "https://%sgcdsdk.%s/install_data/v4.0/";
        private final String \u0131;
        private AppsFlyerLibCore \u0196;
        private final Application \u01c3;
        final ScheduledExecutorService \u0399;
        private final int \u0406;
        private final AtomicInteger \u0456;
        
        static {
            \u0269 = Arrays.asList("googleplay", "playstore", "googleplaystore");
        }
        
        private b(final b b) {
            this.\u0399 = AFExecutor.getInstance().\u03b9();
            this.\u0456 = new AtomicInteger(0);
            this.\u0196 = b.\u0196;
            this.\u01c3 = b.\u01c3;
            this.\u0131 = b.\u0131;
            this.\u0406 = b.\u0406 + 1;
        }
        
        private b(final AppsFlyerLibCore \u0269, final Application \u01c3, final String \u0131) {
            this.\u0399 = AFExecutor.getInstance().\u03b9();
            this.\u0456 = new AtomicInteger(0);
            this.\u0196 = \u0269;
            this.\u01c3 = \u01c3;
            this.\u0131 = \u0131;
            this.\u0406 = 0;
        }
        
        private void \u0399(final String s, final int n) {
            if (500 > n || n >= 600) {
                AFLogger.afDebugLog("Calling onConversionFailure with:\n".concat(String.valueOf(s)));
                AppsFlyerLibCore.\u026a.onConversionDataFail(s);
                return;
            }
            if (this.\u0406 == 2) {
                AFLogger.afDebugLog("Calling onConversionFailure with:\n".concat(String.valueOf(s)));
                AppsFlyerLibCore.\u026a.onConversionDataFail(s);
                return;
            }
            final b b = new b(this);
            AppsFlyerLibCore.\u0131(b.\u0399, b, TimeUnit.MILLISECONDS);
        }
        
        @Override
        public final void run() {
            final String \u0131 = this.\u0131;
            if (\u0131 != null) {
                if (\u0131.length() == 0) {
                    return;
                }
                if (this.\u0196.isTrackingStopped()) {
                    return;
                }
                this.\u0456.incrementAndGet();
                Label_1060: {
                    HttpURLConnection httpURLConnection2;
                    try {
                        if (this.\u01c3 == null) {
                            this.\u0456.decrementAndGet();
                            return;
                        }
                        final long currentTimeMillis = System.currentTimeMillis();
                        final String \u03b9 = \u03b9((Context)this.\u01c3, this.\u0196.getConfiguredChannel((Context)this.\u01c3));
                        String concat = null;
                        Label_0140: {
                            if (\u03b9 != null) {
                                if (!b.\u0269.contains(\u03b9.toLowerCase())) {
                                    concat = "-".concat(String.valueOf(\u03b9));
                                    break Label_0140;
                                }
                                AFLogger.afWarnLog(String.format("AF detected using redundant Google-Play channel for attribution - %s. Using without channel postfix.", \u03b9));
                            }
                            concat = "";
                        }
                        final StringBuilder sb = new StringBuilder();
                        sb.append(ServerConfigHandler.getUrl(b.\u03b9));
                        sb.append(((Context)this.\u01c3).getPackageName());
                        sb.append(concat);
                        sb.append("?devkey=");
                        sb.append(this.\u0131);
                        sb.append("&device_id=");
                        sb.append(ae.\u0399(new WeakReference<Context>((Context)this.\u01c3)));
                        final String string = sb.toString();
                        if (ai.\u0269 == null) {
                            ai.\u0269 = new ai();
                        }
                        ai.\u0269.\u0131("server_request", string, "");
                        ah.\u0399("Calling server for attribution url: ".concat(String.valueOf(string)));
                        final HttpURLConnection httpURLConnection = (HttpURLConnection)new URL(string).openConnection();
                        try {
                            httpURLConnection.setRequestMethod("GET");
                            httpURLConnection.setConnectTimeout(10000);
                            httpURLConnection.setRequestProperty("Connection", "close");
                            httpURLConnection.connect();
                            final int responseCode = httpURLConnection.getResponseCode();
                            final String \u01c3 = this.\u0196.\u01c3(httpURLConnection);
                            if (ai.\u0269 == null) {
                                ai.\u0269 = new ai();
                            }
                            ai.\u0269.\u0131("server_response", string, String.valueOf(responseCode), \u01c3);
                            if (responseCode != 200 && responseCode != 404) {
                                if (AppsFlyerLibCore.\u026a != null) {
                                    this.\u0399("Error connection to server: ".concat(String.valueOf(responseCode)), responseCode);
                                }
                                final StringBuilder sb2 = new StringBuilder("AttributionIdFetcher response code: ");
                                sb2.append(responseCode);
                                sb2.append("  url: ");
                                sb2.append(string);
                                ah.\u0399(sb2.toString());
                            }
                            else {
                                \u0269((Context)this.\u01c3, "appsflyerGetConversionDataTiming", System.currentTimeMillis() - currentTimeMillis);
                                ah.\u0399("Attribution data: ".concat(String.valueOf(\u01c3)));
                                if (\u01c3.length() > 0) {
                                    final Map \u03b92 = \u0131(\u01c3);
                                    final Boolean b = \u03b92.get("iscache");
                                    if (responseCode == 404) {
                                        \u03b92.remove("error_reason");
                                        \u03b92.remove("status_code");
                                        \u03b92.put("af_status", "Organic");
                                        \u03b92.put("af_message", "organic install");
                                    }
                                    if (b != null && !b) {
                                        \u0269((Context)this.\u01c3, "appsflyerConversionDataCacheExpiration", System.currentTimeMillis());
                                    }
                                    if (\u03b92.containsKey("af_siteid")) {
                                        String s;
                                        if (\u03b92.containsKey("af_channel")) {
                                            final StringBuilder sb3 = new StringBuilder("[Invite] Detected App-Invite via channel: ");
                                            sb3.append(\u03b92.get("af_channel"));
                                            s = sb3.toString();
                                        }
                                        else {
                                            s = String.format("[CrossPromotion] App was installed via %s's Cross Promotion", \u03b92.get("af_siteid"));
                                        }
                                        AFLogger.afDebugLog(s);
                                    }
                                    if (\u03b92.containsKey("af_siteid")) {
                                        final StringBuilder sb4 = new StringBuilder("[Invite] Detected App-Invite via channel: ");
                                        sb4.append(\u03b92.get("af_channel"));
                                        AFLogger.afDebugLog(sb4.toString());
                                    }
                                    \u03b92.put("is_first_launch", Boolean.FALSE);
                                    final String string2 = new JSONObject(\u03b92).toString();
                                    if (string2 != null) {
                                        AppsFlyerLibCore.\u01c3((Context)this.\u01c3, "attributionId", string2);
                                    }
                                    else {
                                        AppsFlyerLibCore.\u01c3((Context)this.\u01c3, "attributionId", \u01c3);
                                    }
                                    final StringBuilder sb5 = new StringBuilder("iscache=");
                                    sb5.append(b);
                                    sb5.append(" caching conversion data");
                                    AFLogger.afDebugLog(sb5.toString());
                                    if (AppsFlyerLibCore.\u026a != null && this.\u0456.intValue() <= 1) {
                                        Map<String, Boolean> \u01c32;
                                        try {
                                            final Map map = \u01c32 = (Map<String, Boolean>)\u03b9((Context)this.\u01c3);
                                            if (!AppsFlyerLibCore.getSharedPreferences((Context)this.\u01c3).getBoolean("sixtyDayConversionData", false)) {
                                                map.put("is_first_launch", Boolean.TRUE);
                                                \u01c32 = (Map<String, Boolean>)map;
                                            }
                                        }
                                        catch (y y) {
                                            AFLogger.afErrorLog("Exception while trying to fetch attribution data. ", y);
                                            \u01c32 = (Map<String, Boolean>)\u03b92;
                                        }
                                        final StringBuilder sb6 = new StringBuilder("Calling onConversionDataSuccess with:\n");
                                        sb6.append(\u01c32.toString());
                                        AFLogger.afDebugLog(sb6.toString());
                                        AppsFlyerLibCore.\u026a.onConversionDataSuccess((Map<String, Object>)\u01c32);
                                    }
                                }
                            }
                            this.\u0456.decrementAndGet();
                            if (httpURLConnection != null) {
                                break Label_1060;
                            }
                            break Label_1060;
                        }
                        finally {}
                    }
                    finally {
                        httpURLConnection2 = null;
                    }
                    try {
                        final Throwable t;
                        if (AppsFlyerLibCore.\u026a != null) {
                            this.\u0399(t.getMessage(), 0);
                        }
                        AFLogger.afErrorLog(t.getMessage(), t);
                        this.\u0456.decrementAndGet();
                        if (httpURLConnection2 != null) {
                            httpURLConnection2.disconnect();
                        }
                        this.\u0399.shutdown();
                    }
                    finally {
                        this.\u0456.decrementAndGet();
                        if (httpURLConnection2 != null) {
                            httpURLConnection2.disconnect();
                        }
                    }
                }
            }
        }
    }
    
    final class c implements Runnable
    {
        private WeakReference<Context> \u03b9;
        
        public c(final Context context) {
            this.\u03b9 = null;
            this.\u03b9 = new WeakReference<Context>(context);
        }
        
        @Override
        public final void run() {
            if (AppsFlyerLibCore.this.\u0433) {
                return;
            }
            AppsFlyerLibCore.this.\u04c0 = System.currentTimeMillis();
            if (this.\u03b9 == null) {
                return;
            }
            AppsFlyerLibCore.this.\u0433 = true;
            Label_0331: {
                try {
                    final String \u01c3 = AppsFlyerLibCore.\u01c3("AppsFlyerKey");
                    synchronized (this.\u03b9) {
                        aa.\u01c3();
                        for (final j j : aa.\u01c3(this.\u03b9.get())) {
                            final StringBuilder sb = new StringBuilder("resending request: ");
                            sb.append(j.\u0131);
                            AFLogger.afInfoLog(sb.toString());
                            try {
                                final long currentTimeMillis = System.currentTimeMillis();
                                final long long1 = Long.parseLong(j.\u01c3, 10);
                                final AppsFlyerLibCore \u01c32 = AppsFlyerLibCore.this;
                                final CachedEvent cachedEvent = new CachedEvent();
                                final StringBuilder sb2 = new StringBuilder();
                                sb2.append(j.\u0131);
                                sb2.append("&isCachedRequest=true&timeincache=");
                                sb2.append((currentTimeMillis - long1) / 1000L);
                                final AFEvent key = cachedEvent.urlString(sb2.toString()).post(j.\u01c3()).key(\u01c3);
                                key.\u0131 = this.\u03b9;
                                key.\u0279 = j.\u01c3;
                                key.\u0237 = false;
                                AppsFlyerLibCore.\u0131(\u01c32, key);
                            }
                            catch (Exception ex) {
                                AFLogger.afErrorLog("Failed to resend cached request", ex);
                            }
                        }
                    }
                }
                catch (Exception ex2) {
                    AFLogger.afErrorLog("failed to check cache. ", ex2);
                }
                finally {
                    break Label_0331;
                }
                AppsFlyerLibCore.this.\u0433 = false;
                AppsFlyerLibCore.this.\u0142.shutdown();
                AppsFlyerLibCore.this.\u0142 = null;
                return;
            }
            AppsFlyerLibCore.this.\u0433 = false;
        }
    }
    
    final class e implements Runnable
    {
        private AFEvent \u0269;
        
        private e(final AFEvent \u0269) {
            this.\u0269 = \u0269;
        }
        
        @Override
        public final void run() {
            final AppsFlyerLibCore \u0131 = AppsFlyerLibCore.this;
            final AFEvent \u0269 = this.\u0269;
            \u0269.\u03b9 = (Context)\u0269.\u0131.get();
            \u0131.\u0399(\u0269);
        }
    }
}
