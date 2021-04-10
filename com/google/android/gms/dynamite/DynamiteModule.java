package com.google.android.gms.dynamite;

import android.content.*;
import com.google.android.gms.common.internal.*;
import android.util.*;
import android.net.*;
import android.database.*;
import android.os.*;
import java.lang.reflect.*;
import java.io.*;
import com.google.android.gms.dynamic.*;
import com.google.android.gms.common.*;

public final class DynamiteModule
{
    public static final VersionPolicy PREFER_HIGHEST_OR_LOCAL_VERSION;
    public static final VersionPolicy PREFER_HIGHEST_OR_LOCAL_VERSION_NO_FORCE_STAGING;
    public static final VersionPolicy PREFER_HIGHEST_OR_REMOTE_VERSION;
    public static final VersionPolicy PREFER_HIGHEST_OR_REMOTE_VERSION_NO_FORCE_STAGING;
    public static final VersionPolicy PREFER_LOCAL;
    public static final VersionPolicy PREFER_REMOTE;
    private static Boolean zzabr;
    private static IDynamiteLoader zzabs;
    private static IDynamiteLoaderV2 zzabt;
    private static String zzabu;
    private static final ThreadLocal<zza> zzabv;
    private static final IVersions zzabw;
    private final Context zzabx;
    
    static {
        zzabv = new ThreadLocal<zza>();
        zzabw = (IVersions)new com.google.android.gms.dynamite.zza();
        PREFER_REMOTE = (VersionPolicy)new com.google.android.gms.dynamite.zzb();
        PREFER_LOCAL = (VersionPolicy)new zzc();
        PREFER_HIGHEST_OR_LOCAL_VERSION = (VersionPolicy)new zzd();
        PREFER_HIGHEST_OR_LOCAL_VERSION_NO_FORCE_STAGING = (VersionPolicy)new zze();
        PREFER_HIGHEST_OR_REMOTE_VERSION = (VersionPolicy)new zzf();
        PREFER_HIGHEST_OR_REMOTE_VERSION_NO_FORCE_STAGING = (VersionPolicy)new zzg();
    }
    
    private DynamiteModule(final Context context) {
        this.zzabx = Preconditions.checkNotNull(context);
    }
    
    public static int getLocalVersion(final Context context, final String s) {
        try {
            final ClassLoader classLoader = context.getApplicationContext().getClassLoader();
            final StringBuilder sb = new StringBuilder(String.valueOf(s).length() + 61);
            sb.append("com.google.android.gms.dynamite.descriptors.");
            sb.append(s);
            sb.append(".ModuleDescriptor");
            final Class<?> loadClass = classLoader.loadClass(sb.toString());
            final Field declaredField = loadClass.getDeclaredField("MODULE_ID");
            final Field declaredField2 = loadClass.getDeclaredField("MODULE_VERSION");
            if (!declaredField.get(null).equals(s)) {
                final String value = String.valueOf(declaredField.get(null));
                final StringBuilder sb2 = new StringBuilder(String.valueOf(value).length() + 51 + String.valueOf(s).length());
                sb2.append("Module descriptor id '");
                sb2.append(value);
                sb2.append("' didn't match expected id '");
                sb2.append(s);
                sb2.append("'");
                Log.e("DynamiteModule", sb2.toString());
                return 0;
            }
            return declaredField2.getInt(null);
        }
        catch (Exception ex) {
            final String value2 = String.valueOf(ex.getMessage());
            String concat;
            if (value2.length() != 0) {
                concat = "Failed to load module descriptor class: ".concat(value2);
            }
            else {
                concat = new String("Failed to load module descriptor class: ");
            }
            Log.e("DynamiteModule", concat);
            return 0;
        }
        catch (ClassNotFoundException ex2) {
            final StringBuilder sb3 = new StringBuilder(String.valueOf(s).length() + 45);
            sb3.append("Local module descriptor class for ");
            sb3.append(s);
            sb3.append(" not found.");
            Log.w("DynamiteModule", sb3.toString());
            return 0;
        }
    }
    
    public static Uri getQueryUri(final String s, final boolean b) {
        String s2;
        if (b) {
            s2 = "api_force_staging";
        }
        else {
            s2 = "api";
        }
        final StringBuilder sb = new StringBuilder(s2.length() + 42 + String.valueOf(s).length());
        sb.append("content://com.google.android.gms.chimera/");
        sb.append(s2);
        sb.append("/");
        sb.append(s);
        return Uri.parse(sb.toString());
    }
    
    public static int getRemoteVersion(final Context context, final String s, final boolean b) {
        synchronized (DynamiteModule.class) {
            Boolean zzabr;
            if ((zzabr = DynamiteModule.zzabr) == null) {
                try {
                    final Class<?> loadClass = context.getApplicationContext().getClassLoader().loadClass(DynamiteLoaderClassLoader.class.getName());
                    final Field declaredField = loadClass.getDeclaredField("sClassLoader");
                    synchronized (loadClass) {
                        final ClassLoader classLoader = (ClassLoader)declaredField.get(null);
                        Label_0216: {
                            Label_0211: {
                                if (classLoader != null) {
                                    if (classLoader != ClassLoader.getSystemClassLoader()) {
                                        try {
                                            zza(classLoader);
                                        }
                                        catch (LoadingException ex2) {}
                                        final Boolean true = Boolean.TRUE;
                                        break Label_0216;
                                    }
                                }
                                else {
                                    ClassLoader classLoader2 = null;
                                    Label_0115: {
                                        if (!"com.google.android.gms".equals(context.getApplicationContext().getPackageName())) {
                                            try {
                                                final int zzb = zzb(context, s, b);
                                                if (DynamiteModule.zzabu != null && !DynamiteModule.zzabu.isEmpty()) {
                                                    final zzh zzh = new zzh(DynamiteModule.zzabu, ClassLoader.getSystemClassLoader());
                                                    zza((ClassLoader)zzh);
                                                    declaredField.set(null, zzh);
                                                    DynamiteModule.zzabr = Boolean.TRUE;
                                                    return zzb;
                                                }
                                                return zzb;
                                            }
                                            catch (LoadingException ex3) {
                                                classLoader2 = ClassLoader.getSystemClassLoader();
                                                break Label_0115;
                                            }
                                            break Label_0211;
                                        }
                                        classLoader2 = ClassLoader.getSystemClassLoader();
                                    }
                                    declaredField.set(null, classLoader2);
                                }
                            }
                            final Boolean false = Boolean.FALSE;
                        }
                    }
                }
                catch (ClassNotFoundException | IllegalAccessException | NoSuchFieldException ex4) {
                    final Object o;
                    final String value = String.valueOf(o);
                    final StringBuilder sb = new StringBuilder(String.valueOf(value).length() + 30);
                    sb.append("Failed to load module via V2: ");
                    sb.append(value);
                    Log.w("DynamiteModule", sb.toString());
                    zzabr = Boolean.FALSE;
                }
                DynamiteModule.zzabr = zzabr;
            }
            // monitorexit(DynamiteModule.class)
            if (zzabr) {
                try {
                    return zzb(context, s, b);
                }
                catch (LoadingException ex) {
                    final String value2 = String.valueOf(ex.getMessage());
                    String concat;
                    if (value2.length() != 0) {
                        concat = "Failed to retrieve remote module version: ".concat(value2);
                    }
                    else {
                        concat = new String("Failed to retrieve remote module version: ");
                    }
                    Log.w("DynamiteModule", concat);
                    return 0;
                }
            }
            return zza(context, s, b);
        }
    }
    
    public static DynamiteModule load(Context context, final VersionPolicy versionPolicy, final String s) throws LoadingException {
        final zza zza = DynamiteModule.zzabv.get();
        final zza zza2 = new zza(null);
        DynamiteModule.zzabv.set(zza2);
        try {
            final SelectionResult selectModule = versionPolicy.selectModule(context, s, DynamiteModule.zzabw);
            final int localVersion = selectModule.localVersion;
            final int remoteVersion = selectModule.remoteVersion;
            final StringBuilder sb = new StringBuilder(String.valueOf(s).length() + 68 + String.valueOf(s).length());
            sb.append("Considering local module ");
            sb.append(s);
            sb.append(":");
            sb.append(localVersion);
            sb.append(" and remote module ");
            sb.append(s);
            sb.append(":");
            sb.append(remoteVersion);
            Log.i("DynamiteModule", sb.toString());
            if (selectModule.selection != 0 && (selectModule.selection != -1 || selectModule.localVersion != 0) && (selectModule.selection != 1 || selectModule.remoteVersion != 0)) {
                Label_0390: {
                    Label_0380: {
                        if (selectModule.selection == -1) {
                            final Context context2 = context = (Context)zzd(context, s);
                            if (zza2.zzaby == null) {
                                break Label_0390;
                            }
                            context = context2;
                        }
                        else {
                            if (selectModule.selection == 1) {
                                try {
                                    final Object zza3 = zza(context, s, selectModule.remoteVersion);
                                    if (zza2.zzaby == null) {
                                        context = (Context)zza3;
                                        break Label_0390;
                                    }
                                    context = (Context)zza3;
                                }
                                catch (LoadingException ex) {
                                    final String value = String.valueOf(ex.getMessage());
                                    String concat;
                                    if (value.length() != 0) {
                                        concat = "Failed to load remote module: ".concat(value);
                                    }
                                    else {
                                        concat = new String("Failed to load remote module: ");
                                    }
                                    Log.w("DynamiteModule", concat);
                                    if (selectModule.localVersion == 0 || versionPolicy.selectModule(context, s, (IVersions)new zzb(selectModule.localVersion, 0)).selection != -1) {
                                        throw new LoadingException("Remote load failed. No local fallback found.", ex, null);
                                    }
                                    final Context context3 = context = (Context)zzd(context, s);
                                    if (zza2.zzaby == null) {
                                        break Label_0390;
                                    }
                                    context = context3;
                                }
                                break Label_0380;
                            }
                            final int selection = selectModule.selection;
                            final StringBuilder sb2 = new StringBuilder(47);
                            sb2.append("VersionPolicy returned invalid code:");
                            sb2.append(selection);
                            throw new LoadingException(sb2.toString(), (com.google.android.gms.dynamite.zza)null);
                        }
                    }
                    zza2.zzaby.close();
                }
                DynamiteModule.zzabv.set(zza);
                return (DynamiteModule)context;
            }
            final int localVersion2 = selectModule.localVersion;
            final int remoteVersion2 = selectModule.remoteVersion;
            final StringBuilder sb3 = new StringBuilder(91);
            sb3.append("No acceptable module found. Local version is ");
            sb3.append(localVersion2);
            sb3.append(" and remote version is ");
            sb3.append(remoteVersion2);
            sb3.append(".");
            throw new LoadingException(sb3.toString(), (com.google.android.gms.dynamite.zza)null);
        }
        finally {
            if (zza2.zzaby != null) {
                zza2.zzaby.close();
            }
            DynamiteModule.zzabv.set(zza);
        }
    }
    
    public static Cursor queryForDynamiteModule(final Context context, final String s, final boolean b) {
        return context.getContentResolver().query(getQueryUri(s, b), (String[])null, (String)null, (String[])null, (String)null);
    }
    
    private static int zza(final Context context, final String s, final boolean b) {
        final IDynamiteLoader zzg = zzg(context);
        if (zzg == null) {
            return 0;
        }
        try {
            return zzg.getModuleVersion2(ObjectWrapper.wrap(context), s, b);
        }
        catch (RemoteException ex) {
            final String value = String.valueOf(ex.getMessage());
            String concat;
            if (value.length() != 0) {
                concat = "Failed to retrieve remote module version: ".concat(value);
            }
            else {
                concat = new String("Failed to retrieve remote module version: ");
            }
            Log.w("DynamiteModule", concat);
            return 0;
        }
    }
    
    private static Context zza(Context context, final String s, final int n, final Cursor cursor, final IDynamiteLoaderV2 dynamiteLoaderV2) {
        try {
            context = ObjectWrapper.unwrap(dynamiteLoaderV2.loadModule2(ObjectWrapper.wrap(context), s, n, ObjectWrapper.wrap(cursor)));
            return context;
        }
        catch (Exception ex) {
            final String value = String.valueOf(ex.toString());
            String concat;
            if (value.length() != 0) {
                concat = "Failed to load DynamiteLoader: ".concat(value);
            }
            else {
                concat = new String("Failed to load DynamiteLoader: ");
            }
            Log.e("DynamiteModule", concat);
            return null;
        }
    }
    
    private static DynamiteModule zza(final Context context, final String s, final int n) throws LoadingException {
        synchronized (DynamiteModule.class) {
            final Boolean zzabr = DynamiteModule.zzabr;
            // monitorexit(DynamiteModule.class)
            if (zzabr == null) {
                throw new LoadingException("Failed to determine which loading route to use.", (com.google.android.gms.dynamite.zza)null);
            }
            if (zzabr) {
                return zzc(context, s, n);
            }
            return zzb(context, s, n);
        }
    }
    
    private static void zza(final ClassLoader classLoader) throws LoadingException {
        try {
            DynamiteModule.zzabt = IDynamiteLoaderV2.Stub.asInterface((IBinder)classLoader.loadClass("com.google.android.gms.dynamiteloader.DynamiteLoaderV2").getConstructor((Class<?>[])new Class[0]).newInstance(new Object[0]));
        }
        catch (ClassNotFoundException | IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException ex) {
            final Object o;
            throw new LoadingException("Failed to instantiate dynamite loader", (Throwable)o, null);
        }
    }
    
    private static int zzb(final Context context, String queryForDynamiteModule, final boolean b) throws LoadingException {
        Serializable s = null;
        Throwable t = null;
        Label_0208: {
            Label_0177: {
                try {
                    queryForDynamiteModule = (String)queryForDynamiteModule(context, queryForDynamiteModule, b);
                    if (queryForDynamiteModule != null) {
                        try {
                            try {
                                if (((Cursor)queryForDynamiteModule).moveToFirst()) {
                                    final int int1 = ((Cursor)queryForDynamiteModule).getInt(0);
                                    s = queryForDynamiteModule;
                                    if (int1 > 0) {
                                        synchronized (DynamiteModule.class) {
                                            DynamiteModule.zzabu = ((Cursor)queryForDynamiteModule).getString(2);
                                            // monitorexit(DynamiteModule.class)
                                            final zza zza = DynamiteModule.zzabv.get();
                                            if (zza != null && zza.zzaby == null) {
                                                zza.zzaby = (Cursor)queryForDynamiteModule;
                                            }
                                        }
                                    }
                                    if (s != null) {
                                        ((Cursor)s).close();
                                    }
                                    return int1;
                                }
                            }
                            finally {}
                        }
                        catch (Exception s) {
                            break Label_0177;
                        }
                    }
                    Log.w("DynamiteModule", "Failed to retrieve remote module version.");
                    throw new LoadingException("Failed to connect to dynamite module ContentResolver.", (com.google.android.gms.dynamite.zza)null);
                }
                catch (Exception s) {}
                finally {
                    t = (Throwable)s;
                    break Label_0208;
                }
            }
            if (s instanceof LoadingException) {
                throw s;
            }
            throw new LoadingException("V2 version check failed", (Throwable)s, null);
        }
        if (t != null) {
            ((Cursor)t).close();
        }
    }
    
    private static DynamiteModule zzb(final Context context, final String s, final int n) throws LoadingException {
        final StringBuilder sb = new StringBuilder(String.valueOf(s).length() + 51);
        sb.append("Selected remote version of ");
        sb.append(s);
        sb.append(", version >= ");
        sb.append(n);
        Log.i("DynamiteModule", sb.toString());
        final IDynamiteLoader zzg = zzg(context);
        if (zzg != null) {
            try {
                final IObjectWrapper moduleContext = zzg.createModuleContext(ObjectWrapper.wrap(context), s, n);
                if (ObjectWrapper.unwrap(moduleContext) != null) {
                    return new DynamiteModule((Context)ObjectWrapper.unwrap(moduleContext));
                }
                throw new LoadingException("Failed to load remote module.", (com.google.android.gms.dynamite.zza)null);
            }
            catch (RemoteException ex) {
                throw new LoadingException("Failed to load remote module.", (Throwable)ex, null);
            }
        }
        throw new LoadingException("Failed to create IDynamiteLoader.", (com.google.android.gms.dynamite.zza)null);
    }
    
    private static DynamiteModule zzc(Context zza, final String s, final int n) throws LoadingException {
        final StringBuilder sb = new StringBuilder(String.valueOf(s).length() + 51);
        sb.append("Selected remote version of ");
        sb.append(s);
        sb.append(", version >= ");
        sb.append(n);
        Log.i("DynamiteModule", sb.toString());
        synchronized (DynamiteModule.class) {
            final IDynamiteLoaderV2 zzabt = DynamiteModule.zzabt;
            // monitorexit(DynamiteModule.class)
            if (zzabt == null) {
                throw new LoadingException("DynamiteLoaderV2 was not cached.", (com.google.android.gms.dynamite.zza)null);
            }
            final zza zza2 = DynamiteModule.zzabv.get();
            if (zza2 == null || zza2.zzaby == null) {
                throw new LoadingException("No result cursor", (com.google.android.gms.dynamite.zza)null);
            }
            zza = zza(zza.getApplicationContext(), s, n, zza2.zzaby, zzabt);
            if (zza != null) {
                return new DynamiteModule(zza);
            }
            throw new LoadingException("Failed to get module context", (com.google.android.gms.dynamite.zza)null);
        }
    }
    
    private static DynamiteModule zzd(final Context context, String s) {
        s = String.valueOf(s);
        if (s.length() != 0) {
            s = "Selected local version of ".concat(s);
        }
        else {
            s = new String("Selected local version of ");
        }
        Log.i("DynamiteModule", s);
        return new DynamiteModule(context.getApplicationContext());
    }
    
    private static IDynamiteLoader zzg(final Context context) {
        synchronized (DynamiteModule.class) {
            if (DynamiteModule.zzabs != null) {
                return DynamiteModule.zzabs;
            }
            if (GoogleApiAvailabilityLight.getInstance().isGooglePlayServicesAvailable(context) != 0) {
                return null;
            }
            try {
                final IDynamiteLoader interface1 = IDynamiteLoader.Stub.asInterface((IBinder)context.createPackageContext("com.google.android.gms", 3).getClassLoader().loadClass("com.google.android.gms.chimera.container.DynamiteLoaderImpl").newInstance());
                if (interface1 != null) {
                    return DynamiteModule.zzabs = interface1;
                }
            }
            catch (Exception ex) {
                final String value = String.valueOf(ex.getMessage());
                String concat;
                if (value.length() != 0) {
                    concat = "Failed to load IDynamiteLoader from GmsCore: ".concat(value);
                }
                else {
                    concat = new String("Failed to load IDynamiteLoader from GmsCore: ");
                }
                Log.e("DynamiteModule", concat);
            }
            return null;
        }
    }
    
    public final IBinder instantiate(String s) throws LoadingException {
        try {
            return (IBinder)this.zzabx.getClassLoader().loadClass(s).newInstance();
        }
        catch (ClassNotFoundException | InstantiationException | IllegalAccessException ex3) {
            final IllegalAccessException ex2;
            final IllegalAccessException ex = ex2;
            s = String.valueOf(s);
            if (s.length() != 0) {
                s = "Failed to instantiate module class: ".concat(s);
            }
            else {
                s = new String("Failed to instantiate module class: ");
            }
            throw new LoadingException(s, ex, null);
        }
    }
    
    public static class DynamiteLoaderClassLoader
    {
    }
    
    public static class LoadingException extends Exception
    {
        private LoadingException(final String s) {
            super(s);
        }
        
        private LoadingException(final String s, final Throwable t) {
            super(s, t);
        }
    }
    
    public interface VersionPolicy
    {
        SelectionResult selectModule(final Context p0, final String p1, final IVersions p2) throws LoadingException;
    }
    
    public interface IVersions
    {
        int getLocalVersion(final Context p0, final String p1);
        
        int getRemoteVersion(final Context p0, final String p1, final boolean p2) throws LoadingException;
    }
    
    public static class SelectionResult
    {
        public int localVersion;
        public int remoteVersion;
        public int selection;
        
        public SelectionResult() {
            this.localVersion = 0;
            this.remoteVersion = 0;
            this.selection = 0;
        }
    }
    
    private static final class zza
    {
        public Cursor zzaby;
    }
    
    private static final class zzb implements IVersions
    {
        private final int zzabz;
        private final int zzaca;
        
        public zzb(final int zzabz, final int n) {
            this.zzabz = zzabz;
            this.zzaca = 0;
        }
        
        @Override
        public final int getLocalVersion(final Context context, final String s) {
            return this.zzabz;
        }
        
        @Override
        public final int getRemoteVersion(final Context context, final String s, final boolean b) {
            return 0;
        }
    }
}
