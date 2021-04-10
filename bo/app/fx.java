package bo.app;

import java.io.*;
import android.net.*;
import com.appboy.support.*;
import java.util.concurrent.*;
import java.util.*;
import android.content.*;
import android.os.*;

public class fx implements ft
{
    private static final String a;
    private final Context b;
    private final ThreadPoolExecutor c;
    private final SharedPreferences d;
    private Map<String, String> e;
    private Map<String, String> f;
    
    static {
        a = AppboyLogger.getAppboyLogTag(fx.class);
    }
    
    public fx(final Context b, final ThreadPoolExecutor c, final String s) {
        this.f = new HashMap<String, String>();
        this.b = b;
        this.c = c;
        final StringBuilder sb = new StringBuilder();
        sb.append("com.appboy.storage.triggers.local_assets.");
        sb.append(s);
        this.d = b.getSharedPreferences(sb.toString(), 0);
        this.e = this.a();
    }
    
    public static void a(final Context context) {
        final File file = new File(context.getCacheDir(), "ab_triggers");
        final String a = fx.a;
        final StringBuilder sb = new StringBuilder();
        sb.append("Deleting triggers directory at: ");
        sb.append(file.getAbsolutePath());
        AppboyLogger.v(a, sb.toString());
        AppboyFileUtils.deleteFileOrDirectory(file);
    }
    
    @Override
    public String a(final ek ek) {
        if (!ek.a()) {
            AppboyLogger.d(fx.a, "Prefetch turned off for this triggered action. Not retrieving local asset path.");
            return null;
        }
        final ga d = ek.d();
        if (d == null) {
            AppboyLogger.i(fx.a, "Remote path was null or blank. Not retrieving local asset path.");
            return null;
        }
        final String b = d.b();
        if (StringUtils.isNullOrBlank(b)) {
            AppboyLogger.w(fx.a, "Remote asset path string was null or blank. Not retrieving local asset path.");
            return null;
        }
        if (!this.e.containsKey(b)) {
            final String a = fx.a;
            final StringBuilder sb = new StringBuilder();
            sb.append("No local asset path found for remote asset path: ");
            sb.append(b);
            AppboyLogger.w(a, sb.toString());
            return null;
        }
        final String s = this.e.get(b);
        if (!new File(s).exists()) {
            final String a2 = fx.a;
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("Local asset for remote asset path did not exist: ");
            sb2.append(b);
            AppboyLogger.w(a2, sb2.toString());
            return null;
        }
        final String a3 = fx.a;
        final StringBuilder sb3 = new StringBuilder();
        sb3.append("Retrieving local asset path for remote asset path: ");
        sb3.append(b);
        AppboyLogger.i(a3, sb3.toString());
        this.f.put(b, s);
        return s;
    }
    
    String a(final ga ga) {
        final File b = this.b();
        final String b2 = ga.b();
        if (!ga.a().equals(fi.a)) {
            final File downloadFileToPath = AppboyFileUtils.downloadFileToPath(b.toString(), b2, Integer.toString(IntentUtils.getRequestCode()), null);
            if (downloadFileToPath != null) {
                final Uri fromFile = Uri.fromFile(downloadFileToPath);
                if (fromFile != null) {
                    final String a = fx.a;
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Storing local triggered action image asset at local path ");
                    sb.append(fromFile.getPath());
                    sb.append(" for remote path ");
                    sb.append(b2);
                    AppboyLogger.i(a, sb.toString());
                    return fromFile.getPath();
                }
                final String a2 = fx.a;
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Failed to store image asset for remote path ");
                sb2.append(b2);
                sb2.append(". Not storing local asset");
                AppboyLogger.d(a2, sb2.toString());
            }
            return null;
        }
        final String localHtmlUrlFromRemoteUrl = WebContentUtils.getLocalHtmlUrlFromRemoteUrl(b, b2);
        if (!StringUtils.isNullOrBlank(localHtmlUrlFromRemoteUrl)) {
            final String a3 = fx.a;
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("Storing local triggered action html zip asset at local path ");
            sb3.append(localHtmlUrlFromRemoteUrl);
            sb3.append(" for remote path ");
            sb3.append(b2);
            AppboyLogger.i(a3, sb3.toString());
            return localHtmlUrlFromRemoteUrl;
        }
        final String a4 = fx.a;
        final StringBuilder sb4 = new StringBuilder();
        sb4.append("Failed to store html zip asset for remote path ");
        sb4.append(b2);
        sb4.append(". Not storing local asset");
        AppboyLogger.d(a4, sb4.toString());
        return null;
    }
    
    Map<String, String> a() {
        final ConcurrentHashMap<String, String> concurrentHashMap = new ConcurrentHashMap<String, String>();
        final Map all = this.d.getAll();
        if (all != null) {
            if (all.size() == 0) {
                return concurrentHashMap;
            }
            final Set<String> keySet = all.keySet();
            if (keySet != null) {
                if (keySet.size() == 0) {
                    return concurrentHashMap;
                }
                try {
                    for (final String s : keySet) {
                        final String string = this.d.getString(s, (String)null);
                        if (!StringUtils.isNullOrBlank(string)) {
                            final String a = fx.a;
                            final StringBuilder sb = new StringBuilder();
                            sb.append("Retrieving trigger local asset path ");
                            sb.append(string);
                            sb.append(" from local storage for remote path ");
                            sb.append(s);
                            sb.append(".");
                            AppboyLogger.d(a, sb.toString());
                            concurrentHashMap.put(s, string);
                        }
                    }
                }
                catch (Exception ex) {
                    AppboyLogger.e(fx.a, "Encountered unexpected exception while parsing stored triggered action local assets.", ex);
                }
            }
        }
        return concurrentHashMap;
    }
    
    @Override
    public void a(final List<ek> list) {
        final HashSet<ga> set = new HashSet<ga>();
        final HashSet<String> set2 = new HashSet<String>();
        for (final ek ek : list) {
            final ga d = ek.d();
            if (d != null && !StringUtils.isNullOrBlank(d.b())) {
                if (ek.a()) {
                    final String a = fx.a;
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Received new remote path for triggered action ");
                    sb.append(ek.b());
                    sb.append(" at ");
                    sb.append(d.b());
                    sb.append(".");
                    AppboyLogger.d(a, sb.toString());
                    set.add(d);
                    set2.add(d.b());
                }
                else {
                    final String a2 = fx.a;
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("Pre-fetch off for triggered action ");
                    sb2.append(ek.b());
                    sb2.append(". Not pre-fetching assets at remote path ");
                    sb2.append(d.b());
                    sb2.append(".");
                    AppboyLogger.d(a2, sb2.toString());
                }
            }
        }
        final SharedPreferences$Editor edit = this.d.edit();
        for (final String s : new HashSet<Object>(this.e.keySet())) {
            if (this.f.containsKey(s)) {
                final String a3 = fx.a;
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("Not removing local path for remote path ");
                sb3.append(s);
                sb3.append(" from cache because it is being preserved until the end of the app run.");
                AppboyLogger.d(a3, sb3.toString());
            }
            else {
                if (set2.contains(s)) {
                    continue;
                }
                final String s2 = this.e.get(s);
                final String a4 = fx.a;
                final StringBuilder sb4 = new StringBuilder();
                sb4.append("Removing obsolete local path ");
                sb4.append(s2);
                sb4.append(" for obsolete remote path ");
                sb4.append(s);
                sb4.append(" from cache.");
                AppboyLogger.d(a4, sb4.toString());
                this.e.remove(s);
                edit.remove(s);
                AppboyFileUtils.deleteFileOrDirectory(new File(s2));
            }
        }
        edit.apply();
        try {
            final File[] listFiles = this.b().listFiles();
            if (listFiles != null) {
                for (int length = listFiles.length, i = 0; i < length; ++i) {
                    final File file = listFiles[i];
                    final String path = file.getPath();
                    String s3;
                    String s4;
                    if (!this.e.containsValue(path)) {
                        if (!this.f.containsValue(path)) {
                            final String a5 = fx.a;
                            final StringBuilder sb5 = new StringBuilder();
                            sb5.append("Deleting obsolete asset ");
                            sb5.append(path);
                            sb5.append(" from filesystem.");
                            AppboyLogger.d(a5, sb5.toString());
                            AppboyFileUtils.deleteFileOrDirectory(file);
                            continue;
                        }
                        s3 = fx.a;
                        final StringBuilder sb6 = new StringBuilder();
                        sb6.append("Asset ");
                        sb6.append(path);
                        sb6.append(" is being preserved. Not deleting.");
                        s4 = sb6.toString();
                    }
                    else {
                        s3 = fx.a;
                        final StringBuilder sb7 = new StringBuilder();
                        sb7.append("Asset ");
                        sb7.append(path);
                        sb7.append(" is not obsolete. Not deleting.");
                        s4 = sb7.toString();
                    }
                    AppboyLogger.d(s3, s4);
                }
            }
        }
        catch (Exception ex) {
            AppboyLogger.d(fx.a, "Exception while deleting obsolete assets from filesystem.", ex);
        }
        this.c.execute(new Runnable() {
            @Override
            public void run() {
                Process.setThreadPriority(10);
                for (final ga ga : set) {
                    final String b = ga.b();
                    if (!fx.this.e.containsKey(b)) {
                        try {
                            final String a = fx.this.a(ga);
                            if (StringUtils.isNullOrBlank(a)) {
                                continue;
                            }
                            final String c = fx.a;
                            final StringBuilder sb = new StringBuilder();
                            sb.append("Adding new local path ");
                            sb.append(a);
                            sb.append(" for remote path ");
                            sb.append(b);
                            sb.append(" to cache.");
                            AppboyLogger.d(c, sb.toString());
                            fx.this.e.put(b, a);
                            edit.putString(b, a);
                        }
                        catch (Exception ex) {
                            final String c2 = fx.a;
                            final StringBuilder sb2 = new StringBuilder();
                            sb2.append("Failed to add new local path for remote path ");
                            sb2.append(b);
                            sb2.append(".");
                            AppboyLogger.d(c2, sb2.toString(), ex);
                        }
                    }
                }
                edit.apply();
            }
        });
    }
    
    File b() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.b.getCacheDir().getPath());
        sb.append("/");
        sb.append("ab_triggers");
        return new File(sb.toString());
    }
}
