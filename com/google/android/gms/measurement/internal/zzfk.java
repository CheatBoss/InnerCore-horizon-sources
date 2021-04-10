package com.google.android.gms.measurement.internal;

import java.util.concurrent.atomic.*;
import java.security.*;
import com.google.android.gms.common.internal.*;
import android.text.*;
import android.os.*;
import android.content.*;
import javax.security.auth.x500.*;
import com.google.android.gms.common.wrappers.*;
import java.io.*;
import java.security.cert.*;
import android.content.pm.*;
import android.net.*;
import com.google.android.gms.common.util.*;
import com.google.android.gms.measurement.*;
import com.google.android.gms.common.*;
import java.util.*;
import java.math.*;

public final class zzfk extends zzcp
{
    private static final String[] zzaui;
    private int zzaed;
    private SecureRandom zzauj;
    private final AtomicLong zzauk;
    private Integer zzaul;
    
    static {
        zzaui = new String[] { "firebase_", "google_", "ga_" };
    }
    
    zzfk(final zzbt zzbt) {
        super(zzbt);
        this.zzaul = null;
        this.zzauk = new AtomicLong(0L);
    }
    
    static MessageDigest getMessageDigest() {
        for (int i = 0; i < 2; ++i) {
            try {
                final MessageDigest instance = MessageDigest.getInstance("MD5");
                if (instance != null) {
                    return instance;
                }
            }
            catch (NoSuchAlgorithmException ex) {}
        }
        return null;
    }
    
    private static Object zza(final int n, final Object o, final boolean b) {
        if (o == null) {
            return null;
        }
        if (o instanceof Long) {
            return o;
        }
        if (o instanceof Double) {
            return o;
        }
        if (o instanceof Integer) {
            return o;
        }
        if (o instanceof Byte) {
            return o;
        }
        if (o instanceof Short) {
            return o;
        }
        if (o instanceof Boolean) {
            long n2;
            if (o) {
                n2 = 1L;
            }
            else {
                n2 = 0L;
            }
            return n2;
        }
        if (o instanceof Float) {
            return o;
        }
        if (!(o instanceof String) && !(o instanceof Character) && !(o instanceof CharSequence)) {
            return null;
        }
        return zza(String.valueOf(o), n, b);
    }
    
    public static String zza(final String s, final int n, final boolean b) {
        String s2 = s;
        if (s.codePointCount(0, s.length()) > n) {
            if (b) {
                return String.valueOf(s.substring(0, s.offsetByCodePoints(0, n))).concat("...");
            }
            s2 = null;
        }
        return s2;
    }
    
    public static String zza(final String s, final String[] array, final String[] array2) {
        Preconditions.checkNotNull(array);
        Preconditions.checkNotNull(array2);
        for (int min = Math.min(array.length, array2.length), i = 0; i < min; ++i) {
            if (zzu(s, array[i])) {
                return array2[i];
            }
        }
        return null;
    }
    
    private static void zza(final Bundle bundle, final Object o) {
        Preconditions.checkNotNull(bundle);
        if (o != null && (o instanceof String || o instanceof CharSequence)) {
            bundle.putLong("_el", (long)String.valueOf(o).length());
        }
    }
    
    static boolean zza(final Context context, final boolean b) {
        Preconditions.checkNotNull(context);
        String s;
        if (Build$VERSION.SDK_INT >= 24) {
            s = "com.google.android.gms.measurement.AppMeasurementJobService";
        }
        else {
            s = "com.google.android.gms.measurement.AppMeasurementService";
        }
        return zzc(context, s);
    }
    
    private static boolean zza(final Bundle bundle, final int n) {
        if (bundle.getLong("_err") == 0L) {
            bundle.putLong("_err", (long)n);
            return true;
        }
        return false;
    }
    
    private final boolean zza(final String s, final String s2, int i, Object value, final boolean b) {
        if (value == null) {
            return true;
        }
        if (!(value instanceof Long) && !(value instanceof Float) && !(value instanceof Integer) && !(value instanceof Byte) && !(value instanceof Short) && !(value instanceof Boolean)) {
            if (value instanceof Double) {
                return true;
            }
            if (!(value instanceof String) && !(value instanceof Character) && !(value instanceof CharSequence)) {
                if (value instanceof Bundle && b) {
                    return true;
                }
                if (value instanceof Parcelable[] && b) {
                    final Parcelable[] array = (Parcelable[])value;
                    int length;
                    Parcelable parcelable;
                    for (length = array.length, i = 0; i < length; ++i) {
                        parcelable = array[i];
                        if (!(parcelable instanceof Bundle)) {
                            this.zzgo().zzjg().zze("All Parcelable[] elements must be of type Bundle. Value type, name", parcelable.getClass(), s2);
                            return false;
                        }
                    }
                    return true;
                }
                if (value instanceof ArrayList && b) {
                    final ArrayList list = (ArrayList)value;
                    final int size = list.size();
                    i = 0;
                    while (i < size) {
                        value = list.get(i);
                        ++i;
                        if (!(value instanceof Bundle)) {
                            this.zzgo().zzjg().zze("All ArrayList elements must be of type Bundle. Value type, name", value.getClass(), s2);
                            return false;
                        }
                    }
                    return true;
                }
                return false;
            }
            else {
                final String value2 = String.valueOf(value);
                if (value2.codePointCount(0, value2.length()) > i) {
                    this.zzgo().zzjg().zzd("Value is too long; discarded. Value kind, name, value length", s, s2, value2.length());
                    return false;
                }
            }
        }
        return true;
    }
    
    static boolean zza(final String s, final String s2, final String s3, final String s4) {
        final boolean empty = TextUtils.isEmpty((CharSequence)s);
        final boolean empty2 = TextUtils.isEmpty((CharSequence)s2);
        if (!empty && !empty2) {
            return !s.equals(s2);
        }
        if (empty && empty2) {
            if (!TextUtils.isEmpty((CharSequence)s3) && !TextUtils.isEmpty((CharSequence)s4)) {
                return !s3.equals(s4);
            }
            return !TextUtils.isEmpty((CharSequence)s4);
        }
        else {
            if (!empty && empty2) {
                return !TextUtils.isEmpty((CharSequence)s4) && (TextUtils.isEmpty((CharSequence)s3) || !s3.equals(s4));
            }
            return TextUtils.isEmpty((CharSequence)s3) || !s3.equals(s4);
        }
    }
    
    static byte[] zza(final Parcelable parcelable) {
        if (parcelable == null) {
            return null;
        }
        final Parcel obtain = Parcel.obtain();
        try {
            parcelable.writeToParcel(obtain, 0);
            return obtain.marshall();
        }
        finally {
            obtain.recycle();
        }
    }
    
    public static long zzc(final long n, final long n2) {
        return (n + n2 * 60000L) / 86400000L;
    }
    
    static long zzc(final byte[] array) {
        Preconditions.checkNotNull(array);
        final int length = array.length;
        int n = 0;
        Preconditions.checkState(length > 0);
        long n2 = 0L;
        long n4;
        int n5;
        for (int n3 = array.length - 1; n3 >= 0 && n3 >= array.length - 8; --n3, n2 += (n4 & 0xFFL) << n, n = n5) {
            n4 = array[n3];
            n5 = n + 8;
        }
        return n2;
    }
    
    private static boolean zzc(final Context context, final String s) {
        try {
            final PackageManager packageManager = context.getPackageManager();
            if (packageManager == null) {
                return false;
            }
            final ServiceInfo serviceInfo = packageManager.getServiceInfo(new ComponentName(context, s), 0);
            return serviceInfo != null && serviceInfo.enabled;
        }
        catch (PackageManager$NameNotFoundException ex) {
            return false;
        }
    }
    
    static boolean zzcq(final String s) {
        Preconditions.checkNotEmpty(s);
        return s.charAt(0) != '_' || s.equals("_ep");
    }
    
    private static boolean zzct(final String s) {
        Preconditions.checkNotNull(s);
        return s.matches("^(1:\\d+:android:[a-f0-9]+|ca-app-pub-.*)$");
    }
    
    private static int zzcu(final String s) {
        if ("_ldl".equals(s)) {
            return 2048;
        }
        if ("_id".equals(s)) {
            return 256;
        }
        return 36;
    }
    
    static boolean zzcv(final String s) {
        return !TextUtils.isEmpty((CharSequence)s) && s.startsWith("_");
    }
    
    static boolean zzd(final Intent intent) {
        final String stringExtra = intent.getStringExtra("android.intent.extra.REFERRER_NAME");
        return "android-app://com.google.android.googlequicksearchbox/https/www.google.com".equals(stringExtra) || "https://www.google.com".equals(stringExtra) || "android-app://com.google.appcrawler".equals(stringExtra);
    }
    
    private final boolean zze(final Context context, final String s) {
        final X500Principal x500Principal = new X500Principal("CN=Android Debug,O=Android,C=US");
        zzar zzar;
        String s2;
        try {
            final PackageInfo packageInfo = Wrappers.packageManager(context).getPackageInfo(s, 64);
            if (packageInfo != null && packageInfo.signatures != null && packageInfo.signatures.length > 0) {
                return ((X509Certificate)CertificateFactory.getInstance("X.509").generateCertificate(new ByteArrayInputStream(packageInfo.signatures[0].toByteArray()))).getSubjectX500Principal().equals(x500Principal);
            }
            return true;
        }
        catch (PackageManager$NameNotFoundException ex) {
            zzar = this.zzgo().zzjd();
            s2 = "Package name not found";
        }
        catch (CertificateException ex) {
            zzar = this.zzgo().zzjd();
            s2 = "Error obtaining certificate";
        }
        final PackageManager$NameNotFoundException ex;
        zzar.zzg(s2, ex);
        return true;
    }
    
    static Bundle[] zze(final Object o) {
        if (o instanceof Bundle) {
            return new Bundle[] { (Bundle)o };
        }
        Object[] array2;
        if (o instanceof Parcelable[]) {
            final Parcelable[] array = (Parcelable[])o;
            array2 = Arrays.copyOf(array, array.length, (Class<? extends Object[]>)Bundle[].class);
        }
        else {
            if (!(o instanceof ArrayList)) {
                return null;
            }
            final ArrayList list = (ArrayList)o;
            array2 = list.toArray(new Bundle[list.size()]);
        }
        return (Bundle[])array2;
    }
    
    public static Bundle zzf(Bundle bundle) {
        if (bundle == null) {
            return new Bundle();
        }
        bundle = new Bundle(bundle);
        for (final String s : bundle.keySet()) {
            final Object value = bundle.get(s);
            if (value instanceof Bundle) {
                bundle.putBundle(s, new Bundle((Bundle)value));
            }
            else {
                final boolean b = value instanceof Parcelable[];
                final int n = 0;
                int i = 0;
                if (b) {
                    for (Parcelable[] array = (Parcelable[])value; i < array.length; ++i) {
                        if (array[i] instanceof Bundle) {
                            array[i] = (Parcelable)new Bundle((Bundle)array[i]);
                        }
                    }
                }
                else {
                    if (!(value instanceof List)) {
                        continue;
                    }
                    final List<Object> list = (List<Object>)value;
                    for (int j = n; j < list.size(); ++j) {
                        final Bundle value2 = list.get(j);
                        if (value2 instanceof Bundle) {
                            list.set(j, new Bundle((Bundle)value2));
                        }
                    }
                }
            }
        }
        return bundle;
    }
    
    public static Object zzf(final Object p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ifnonnull       6
        //     4: aconst_null    
        //     5: areturn        
        //     6: new             Ljava/io/ByteArrayOutputStream;
        //     9: dup            
        //    10: invokespecial   java/io/ByteArrayOutputStream.<init>:()V
        //    13: astore_1       
        //    14: new             Ljava/io/ObjectOutputStream;
        //    17: dup            
        //    18: aload_1        
        //    19: invokespecial   java/io/ObjectOutputStream.<init>:(Ljava/io/OutputStream;)V
        //    22: astore_2       
        //    23: aload_2        
        //    24: aload_0        
        //    25: invokevirtual   java/io/ObjectOutputStream.writeObject:(Ljava/lang/Object;)V
        //    28: aload_2        
        //    29: invokevirtual   java/io/ObjectOutputStream.flush:()V
        //    32: new             Ljava/io/ObjectInputStream;
        //    35: dup            
        //    36: new             Ljava/io/ByteArrayInputStream;
        //    39: dup            
        //    40: aload_1        
        //    41: invokevirtual   java/io/ByteArrayOutputStream.toByteArray:()[B
        //    44: invokespecial   java/io/ByteArrayInputStream.<init>:([B)V
        //    47: invokespecial   java/io/ObjectInputStream.<init>:(Ljava/io/InputStream;)V
        //    50: astore_1       
        //    51: aload_1        
        //    52: invokevirtual   java/io/ObjectInputStream.readObject:()Ljava/lang/Object;
        //    55: astore_0       
        //    56: aload_2        
        //    57: invokevirtual   java/io/ObjectOutputStream.close:()V
        //    60: aload_1        
        //    61: invokevirtual   java/io/ObjectInputStream.close:()V
        //    64: aload_0        
        //    65: areturn        
        //    66: aload_2        
        //    67: ifnull          77
        //    70: aload_2        
        //    71: invokevirtual   java/io/ObjectOutputStream.close:()V
        //    74: goto            77
        //    77: aload_1        
        //    78: ifnull          85
        //    81: aload_1        
        //    82: invokevirtual   java/io/ObjectInputStream.close:()V
        //    85: aload_0        
        //    86: athrow         
        //    87: astore_0       
        //    88: goto            66
        //    91: astore_0       
        //    92: aconst_null    
        //    93: astore_1       
        //    94: goto            66
        //    97: astore_0       
        //    98: aconst_null    
        //    99: astore_1       
        //   100: aload_1        
        //   101: astore_2       
        //   102: goto            66
        //   105: astore_0       
        //   106: aconst_null    
        //   107: areturn        
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                              
        //  -----  -----  -----  -----  ----------------------------------
        //  6      23     97     105    Any
        //  23     51     91     97     Any
        //  51     56     87     91     Any
        //  56     64     105    108    Ljava/io/IOException;
        //  56     64     105    108    Ljava/lang/ClassNotFoundException;
        //  70     74     105    108    Ljava/io/IOException;
        //  70     74     105    108    Ljava/lang/ClassNotFoundException;
        //  81     85     105    108    Ljava/io/IOException;
        //  81     85     105    108    Ljava/lang/ClassNotFoundException;
        //  85     87     105    108    Ljava/io/IOException;
        //  85     87     105    108    Ljava/lang/ClassNotFoundException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0066:
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
    
    private final boolean zzs(final String s, final String s2) {
        if (s2 == null) {
            this.zzgo().zzjd().zzg("Name is required and can't be null. Type", s);
            return false;
        }
        if (s2.length() == 0) {
            this.zzgo().zzjd().zzg("Name is required and can't be empty. Type", s);
            return false;
        }
        final int codePoint = s2.codePointAt(0);
        if (!Character.isLetter(codePoint) && codePoint != 95) {
            this.zzgo().zzjd().zze("Name must start with a letter or _ (underscore). Type, name", s, s2);
            return false;
        }
        int codePoint2;
        for (int length = s2.length(), i = Character.charCount(codePoint); i < length; i += Character.charCount(codePoint2)) {
            codePoint2 = s2.codePointAt(i);
            if (codePoint2 != 95 && !Character.isLetterOrDigit(codePoint2)) {
                this.zzgo().zzjd().zze("Name must consist of letters, digits or _ (underscores). Type, name", s, s2);
                return false;
            }
        }
        return true;
    }
    
    static boolean zzu(final String s, final String s2) {
        return (s == null && s2 == null) || (s != null && s.equals(s2));
    }
    
    final Bundle zza(final Uri uri) {
        if (uri == null) {
            return null;
        }
        try {
            String queryParameter;
            String queryParameter2;
            String queryParameter3;
            String queryParameter4;
            if (uri.isHierarchical()) {
                queryParameter = uri.getQueryParameter("utm_campaign");
                queryParameter2 = uri.getQueryParameter("utm_source");
                queryParameter3 = uri.getQueryParameter("utm_medium");
                queryParameter4 = uri.getQueryParameter("gclid");
            }
            else {
                final String s2;
                final String s = s2 = null;
                queryParameter4 = (queryParameter3 = s2);
                queryParameter2 = s2;
                queryParameter = s;
            }
            if (TextUtils.isEmpty((CharSequence)queryParameter) && TextUtils.isEmpty((CharSequence)queryParameter2) && TextUtils.isEmpty((CharSequence)queryParameter3) && TextUtils.isEmpty((CharSequence)queryParameter4)) {
                return null;
            }
            final Bundle bundle = new Bundle();
            if (!TextUtils.isEmpty((CharSequence)queryParameter)) {
                bundle.putString("campaign", queryParameter);
            }
            if (!TextUtils.isEmpty((CharSequence)queryParameter2)) {
                bundle.putString("source", queryParameter2);
            }
            if (!TextUtils.isEmpty((CharSequence)queryParameter3)) {
                bundle.putString("medium", queryParameter3);
            }
            if (!TextUtils.isEmpty((CharSequence)queryParameter4)) {
                bundle.putString("gclid", queryParameter4);
            }
            final String queryParameter5 = uri.getQueryParameter("utm_term");
            if (!TextUtils.isEmpty((CharSequence)queryParameter5)) {
                bundle.putString("term", queryParameter5);
            }
            final String queryParameter6 = uri.getQueryParameter("utm_content");
            if (!TextUtils.isEmpty((CharSequence)queryParameter6)) {
                bundle.putString("content", queryParameter6);
            }
            final String queryParameter7 = uri.getQueryParameter("aclid");
            if (!TextUtils.isEmpty((CharSequence)queryParameter7)) {
                bundle.putString("aclid", queryParameter7);
            }
            final String queryParameter8 = uri.getQueryParameter("cp1");
            if (!TextUtils.isEmpty((CharSequence)queryParameter8)) {
                bundle.putString("cp1", queryParameter8);
            }
            final String queryParameter9 = uri.getQueryParameter("anid");
            if (!TextUtils.isEmpty((CharSequence)queryParameter9)) {
                bundle.putString("anid", queryParameter9);
            }
            return bundle;
        }
        catch (UnsupportedOperationException ex) {
            this.zzgo().zzjg().zzg("Install referrer url isn't a hierarchical URI", ex);
            return null;
        }
    }
    
    final Bundle zza(final String s, final String s2, final Bundle bundle, final List<String> list, final boolean b, final boolean b2) {
        if (bundle != null) {
            final Bundle bundle2 = new Bundle(bundle);
            final Iterator<String> iterator = (Iterator<String>)bundle.keySet().iterator();
            int n = 0;
            while (iterator.hasNext()) {
                final String s3 = iterator.next();
                int n3 = 0;
                Label_0203: {
                    Label_0070: {
                        if (list == null || !list.contains(s3)) {
                            final int n2 = 14;
                            Label_0143: {
                                Label_0140: {
                                    if (b) {
                                        if (this.zzr("event param", s3)) {
                                            if (!this.zza("event param", null, s3)) {
                                                n3 = 14;
                                                break Label_0143;
                                            }
                                            if (this.zza("event param", 40, s3)) {
                                                break Label_0140;
                                            }
                                        }
                                        n3 = 3;
                                        break Label_0143;
                                    }
                                }
                                n3 = 0;
                            }
                            if (n3 == 0) {
                                if (this.zzs("event param", s3)) {
                                    if (!this.zza("event param", null, s3)) {
                                        n3 = n2;
                                        break Label_0203;
                                    }
                                    if (this.zza("event param", 40, s3)) {
                                        break Label_0070;
                                    }
                                }
                                n3 = 3;
                            }
                            break Label_0203;
                        }
                    }
                    n3 = 0;
                }
                int n7 = 0;
                Label_0596: {
                    if (n3 != 0) {
                        if (zza(bundle2, n3)) {
                            bundle2.putString("_ev", zza(s3, 40, true));
                            if (n3 == 3) {
                                zza(bundle2, s3);
                            }
                        }
                    }
                    else {
                        final Object value = bundle.get(s3);
                        this.zzaf();
                        int n5 = 0;
                        Label_0439: {
                            if (b2) {
                                boolean b3 = false;
                                Label_0355: {
                                    Label_0352: {
                                        int n4;
                                        if (value instanceof Parcelable[]) {
                                            n4 = ((Parcelable[])value).length;
                                        }
                                        else {
                                            if (!(value instanceof ArrayList)) {
                                                break Label_0352;
                                            }
                                            n4 = ((ArrayList)value).size();
                                        }
                                        if (n4 > 1000) {
                                            this.zzgo().zzjg().zzd("Parameter array is too long; discarded. Value kind, name, array length", "param", s3, n4);
                                            b3 = false;
                                            break Label_0355;
                                        }
                                    }
                                    b3 = true;
                                }
                                if (!b3) {
                                    n5 = 17;
                                    break Label_0439;
                                }
                            }
                            int n6;
                            if ((this.zzgq().zzay(s) && zzcv(s2)) || zzcv(s3)) {
                                n6 = 256;
                            }
                            else {
                                n6 = 100;
                            }
                            if (this.zza("param", s3, n6, value, b2)) {
                                n5 = 0;
                            }
                            else {
                                n5 = 4;
                            }
                        }
                        if (n5 != 0 && !"_ev".equals(s3)) {
                            if (zza(bundle2, n5)) {
                                bundle2.putString("_ev", zza(s3, 40, true));
                                zza(bundle2, bundle.get(s3));
                            }
                        }
                        else {
                            n7 = n;
                            if (!zzcq(s3)) {
                                break Label_0596;
                            }
                            ++n;
                            if ((n7 = n) > 25) {
                                final StringBuilder sb = new StringBuilder(48);
                                sb.append("Event can't contain more than 25 params");
                                this.zzgo().zzjd().zze(sb.toString(), this.zzgl().zzbs(s2), this.zzgl().zzd(bundle));
                                zza(bundle2, 5);
                                bundle2.remove(s3);
                                continue;
                            }
                            break Label_0596;
                        }
                    }
                    bundle2.remove(s3);
                    n7 = n;
                }
                n = n7;
            }
            return bundle2;
        }
        return null;
    }
    
    final zzad zza(final String s, final String s2, Bundle bundle, final String s3, final long n, final boolean b, final boolean b2) {
        if (TextUtils.isEmpty((CharSequence)s2)) {
            return null;
        }
        if (this.zzcr(s2) == 0) {
            if (bundle != null) {
                bundle = new Bundle(bundle);
            }
            else {
                bundle = new Bundle();
            }
            bundle.putString("_o", s3);
            return new zzad(s2, new zzaa(this.zze(this.zza(s, s2, bundle, CollectionUtils.listOf("_o"), false, false))), s3, n);
        }
        this.zzgo().zzjd().zzg("Invalid conditional property event name", this.zzgl().zzbu(s2));
        throw new IllegalArgumentException();
    }
    
    public final void zza(final int n, final String s, final String s2, final int n2) {
        this.zza(null, n, s, s2, n2);
    }
    
    final void zza(final Bundle bundle, final String s, final Object o) {
        if (bundle == null) {
            return;
        }
        if (o instanceof Long) {
            bundle.putLong(s, (long)o);
            return;
        }
        if (o instanceof String) {
            bundle.putString(s, String.valueOf(o));
            return;
        }
        if (o instanceof Double) {
            bundle.putDouble(s, (double)o);
            return;
        }
        if (s != null) {
            String simpleName;
            if (o != null) {
                simpleName = o.getClass().getSimpleName();
            }
            else {
                simpleName = null;
            }
            this.zzgo().zzji().zze("Not putting event parameter. Invalid value type. name, type", this.zzgl().zzbt(s), simpleName);
        }
    }
    
    final void zza(final String s, final int n, final String s2, final String s3, final int n2) {
        final Bundle bundle = new Bundle();
        zza(bundle, n);
        if (!TextUtils.isEmpty((CharSequence)s2)) {
            bundle.putString(s2, s3);
        }
        if (n == 6 || n == 7 || n == 2) {
            bundle.putLong("_el", (long)n2);
        }
        this.zzadj.zzgr();
        this.zzadj.zzge().logEvent("auto", "_err", bundle);
    }
    
    final boolean zza(final String s, final int n, final String s2) {
        if (s2 == null) {
            this.zzgo().zzjd().zzg("Name is required and can't be null. Type", s);
            return false;
        }
        if (s2.codePointCount(0, s2.length()) > n) {
            this.zzgo().zzjd().zzd("Name is too long. Type, maximum supported length, name", s, n, s2);
            return false;
        }
        return true;
    }
    
    final boolean zza(final String s, final String[] array, final String s2) {
        if (s2 == null) {
            this.zzgo().zzjd().zzg("Name is required and can't be null. Type", s);
            return false;
        }
        Preconditions.checkNotNull(s2);
        final String[] zzaui = zzfk.zzaui;
        final int length = zzaui.length;
        int i = 0;
        while (true) {
            while (i < length) {
                if (s2.startsWith(zzaui[i])) {
                    final boolean b = true;
                    if (b) {
                        this.zzgo().zzjd().zze("Name starts with reserved prefix. Type, name", s, s2);
                        return false;
                    }
                    if (array != null) {
                        Preconditions.checkNotNull(array);
                        final int length2 = array.length;
                        int j = 0;
                        while (true) {
                            while (j < length2) {
                                if (zzu(s2, array[j])) {
                                    final boolean b2 = true;
                                    if (b2) {
                                        this.zzgo().zzjd().zze("Name is reserved. Type, name", s, s2);
                                        return false;
                                    }
                                    return true;
                                }
                                else {
                                    ++j;
                                }
                            }
                            final boolean b2 = false;
                            continue;
                        }
                    }
                    return true;
                }
                else {
                    ++i;
                }
            }
            final boolean b = false;
            continue;
        }
    }
    
    final int zzcr(final String s) {
        if (!this.zzs("event", s)) {
            return 2;
        }
        if (!this.zza("event", AppMeasurement.Event.zzadk, s)) {
            return 13;
        }
        if (!this.zza("event", 40, s)) {
            return 2;
        }
        return 0;
    }
    
    final int zzcs(final String s) {
        if (!this.zzs("user property", s)) {
            return 6;
        }
        if (!this.zza("user property", AppMeasurement.UserProperty.zzado, s)) {
            return 15;
        }
        if (!this.zza("user property", 24, s)) {
            return 6;
        }
        return 0;
    }
    
    final boolean zzcw(final String s) {
        if (TextUtils.isEmpty((CharSequence)s)) {
            return false;
        }
        final String zzhy = this.zzgq().zzhy();
        this.zzgr();
        return zzhy.equals(s);
    }
    
    final long zzd(final Context context, final String s) {
        this.zzaf();
        Preconditions.checkNotNull(context);
        Preconditions.checkNotEmpty(s);
        final PackageManager packageManager = context.getPackageManager();
        final MessageDigest messageDigest = getMessageDigest();
        if (messageDigest == null) {
            this.zzgo().zzjd().zzbx("Could not get MD5 instance");
            return -1L;
        }
        if (packageManager != null) {
            try {
                if (!this.zze(context, s)) {
                    final PackageInfo packageInfo = Wrappers.packageManager(context).getPackageInfo(this.getContext().getPackageName(), 64);
                    if (packageInfo.signatures != null && packageInfo.signatures.length > 0) {
                        return zzc(messageDigest.digest(packageInfo.signatures[0].toByteArray()));
                    }
                    this.zzgo().zzjg().zzbx("Could not get signatures");
                    return -1L;
                }
            }
            catch (PackageManager$NameNotFoundException ex) {
                this.zzgo().zzjd().zzg("Package name not found", ex);
            }
        }
        return 0L;
    }
    
    final Bundle zze(final Bundle bundle) {
        final Bundle bundle2 = new Bundle();
        if (bundle != null) {
            for (final String s : bundle.keySet()) {
                final Object zzh = this.zzh(s, bundle.get(s));
                if (zzh == null) {
                    this.zzgo().zzjg().zzg("Param value can't be null", this.zzgl().zzbt(s));
                }
                else {
                    this.zza(bundle2, s, zzh);
                }
            }
        }
        return bundle2;
    }
    
    @Override
    protected final boolean zzgt() {
        return true;
    }
    
    @Override
    protected final void zzgu() {
        this.zzaf();
        final SecureRandom secureRandom = new SecureRandom();
        long n;
        if ((n = secureRandom.nextLong()) == 0L) {
            final long n2 = n = secureRandom.nextLong();
            if (n2 == 0L) {
                this.zzgo().zzjg().zzbx("Utils falling back to Random for random id");
                n = n2;
            }
        }
        this.zzauk.set(n);
    }
    
    final Object zzh(final String s, final Object o) {
        final boolean equals = "_ev".equals(s);
        int n = 256;
        boolean b;
        if (equals) {
            b = true;
        }
        else {
            if (!zzcv(s)) {
                n = 100;
            }
            b = false;
        }
        return zza(n, o, b);
    }
    
    final int zzi(final String s, final Object o) {
        boolean b;
        if ("_ldl".equals(s)) {
            b = this.zza("user property referrer", s, zzcu(s), o, false);
        }
        else {
            b = this.zza("user property", s, zzcu(s), o, false);
        }
        if (b) {
            return 0;
        }
        return 7;
    }
    
    final Object zzj(final String s, final Object o) {
        int n;
        boolean b;
        if ("_ldl".equals(s)) {
            n = zzcu(s);
            b = true;
        }
        else {
            n = zzcu(s);
            b = false;
        }
        return zza(n, o, b);
    }
    
    public final long zzmc() {
        if (this.zzauk.get() == 0L) {
            synchronized (this.zzauk) {
                final long nextLong = new Random(System.nanoTime() ^ this.zzbx().currentTimeMillis()).nextLong();
                final int zzaed = this.zzaed + 1;
                this.zzaed = zzaed;
                final long n = zzaed;
                // monitorexit(this.zzauk)
                return nextLong + n;
            }
        }
        synchronized (this.zzauk) {
            this.zzauk.compareAndSet(-1L, 1L);
            return this.zzauk.getAndIncrement();
        }
    }
    
    final SecureRandom zzmd() {
        this.zzaf();
        if (this.zzauj == null) {
            this.zzauj = new SecureRandom();
        }
        return this.zzauj;
    }
    
    public final int zzme() {
        if (this.zzaul == null) {
            this.zzaul = GoogleApiAvailabilityLight.getInstance().getApkVersion(this.getContext()) / 1000;
        }
        return this.zzaul;
    }
    
    final String zzmf() {
        final byte[] array = new byte[16];
        this.zzmd().nextBytes(array);
        return String.format(Locale.US, "%032x", new BigInteger(1, array));
    }
    
    final boolean zzr(final String s, final String s2) {
        if (s2 == null) {
            this.zzgo().zzjd().zzg("Name is required and can't be null. Type", s);
            return false;
        }
        if (s2.length() == 0) {
            this.zzgo().zzjd().zzg("Name is required and can't be empty. Type", s);
            return false;
        }
        final int codePoint = s2.codePointAt(0);
        if (!Character.isLetter(codePoint)) {
            this.zzgo().zzjd().zze("Name must start with a letter. Type, name", s, s2);
            return false;
        }
        int codePoint2;
        for (int length = s2.length(), i = Character.charCount(codePoint); i < length; i += Character.charCount(codePoint2)) {
            codePoint2 = s2.codePointAt(i);
            if (codePoint2 != 95 && !Character.isLetterOrDigit(codePoint2)) {
                this.zzgo().zzjd().zze("Name must consist of letters, digits or _ (underscores). Type, name", s, s2);
                return false;
            }
        }
        return true;
    }
    
    final boolean zzt(final String s, final String s2) {
        if (!TextUtils.isEmpty((CharSequence)s)) {
            if (!zzct(s)) {
                if (this.zzadj.zzkj()) {
                    this.zzgo().zzjd().zzg("Invalid google_app_id. Firebase Analytics disabled. See https://goo.gl/NAOOOI. provided id", zzap.zzbv(s));
                }
                return false;
            }
        }
        else {
            if (TextUtils.isEmpty((CharSequence)s2)) {
                if (this.zzadj.zzkj()) {
                    this.zzgo().zzjd().zzbx("Missing google_app_id. Firebase Analytics disabled. See https://goo.gl/NAOOOI");
                }
                return false;
            }
            if (!zzct(s2)) {
                this.zzgo().zzjd().zzg("Invalid gma_app_id. Analytics disabled.", zzap.zzbv(s2));
                return false;
            }
        }
        return true;
    }
    
    final boolean zzx(final String s) {
        this.zzaf();
        if (Wrappers.packageManager(this.getContext()).checkCallingOrSelfPermission(s) == 0) {
            return true;
        }
        this.zzgo().zzjk().zzg("Permission not granted", s);
        return false;
    }
}
