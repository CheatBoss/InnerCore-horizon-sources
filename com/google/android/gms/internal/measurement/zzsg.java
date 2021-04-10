package com.google.android.gms.internal.measurement;

import android.net.*;
import java.util.regex.*;
import java.util.concurrent.atomic.*;
import android.content.*;
import java.util.*;
import android.os.*;
import android.util.*;
import android.database.*;

public class zzsg
{
    private static final Uri CONTENT_URI;
    private static final Uri zzbqd;
    public static final Pattern zzbqe;
    public static final Pattern zzbqf;
    private static final AtomicBoolean zzbqg;
    private static HashMap<String, String> zzbqh;
    private static final HashMap<String, Boolean> zzbqi;
    private static final HashMap<String, Integer> zzbqj;
    private static final HashMap<String, Long> zzbqk;
    private static final HashMap<String, Float> zzbql;
    private static Object zzbqm;
    private static boolean zzbqn;
    private static String[] zzbqo;
    
    static {
        CONTENT_URI = Uri.parse("content://com.google.android.gsf.gservices");
        zzbqd = Uri.parse("content://com.google.android.gsf.gservices/prefix");
        zzbqe = Pattern.compile("^(1|true|t|on|yes|y)$", 2);
        zzbqf = Pattern.compile("^(0|false|f|off|no|n)$", 2);
        zzbqg = new AtomicBoolean();
        zzbqi = new HashMap<String, Boolean>();
        zzbqj = new HashMap<String, Integer>();
        zzbqk = new HashMap<String, Long>();
        zzbql = new HashMap<String, Float>();
        zzsg.zzbqo = new String[0];
    }
    
    private static <T> T zza(final HashMap<String, T> hashMap, final String s, final T t) {
        while (true) {
            synchronized (zzsg.class) {
                if (!hashMap.containsKey(s)) {
                    return null;
                }
                final T value = hashMap.get(s);
                if (value != null) {
                    return value;
                }
            }
            return t;
        }
    }
    
    public static String zza(final ContentResolver p0, final String p1, final String p2) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     2: monitorenter   
        //     3: aload_0        
        //     4: invokestatic    com/google/android/gms/internal/measurement/zzsg.zza:(Landroid/content/ContentResolver;)V
        //     7: getstatic       com/google/android/gms/internal/measurement/zzsg.zzbqm:Ljava/lang/Object;
        //    10: astore          6
        //    12: getstatic       com/google/android/gms/internal/measurement/zzsg.zzbqh:Ljava/util/HashMap;
        //    15: aload_1        
        //    16: invokevirtual   java/util/HashMap.containsKey:(Ljava/lang/Object;)Z
        //    19: ifeq            45
        //    22: getstatic       com/google/android/gms/internal/measurement/zzsg.zzbqh:Ljava/util/HashMap;
        //    25: aload_1        
        //    26: invokevirtual   java/util/HashMap.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //    29: checkcast       Ljava/lang/String;
        //    32: astore_0       
        //    33: aload_0        
        //    34: ifnull          294
        //    37: goto            40
        //    40: ldc             Lcom/google/android/gms/internal/measurement/zzsg;.class
        //    42: monitorexit    
        //    43: aload_0        
        //    44: areturn        
        //    45: getstatic       com/google/android/gms/internal/measurement/zzsg.zzbqo:[Ljava/lang/String;
        //    48: astore_2       
        //    49: aload_2        
        //    50: arraylength    
        //    51: istore          4
        //    53: iconst_0       
        //    54: istore_3       
        //    55: iload_3        
        //    56: iload           4
        //    58: if_icmpge       143
        //    61: aload_1        
        //    62: aload_2        
        //    63: iload_3        
        //    64: aaload         
        //    65: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
        //    68: ifeq            304
        //    71: getstatic       com/google/android/gms/internal/measurement/zzsg.zzbqn:Z
        //    74: ifeq            86
        //    77: getstatic       com/google/android/gms/internal/measurement/zzsg.zzbqh:Ljava/util/HashMap;
        //    80: invokevirtual   java/util/HashMap.isEmpty:()Z
        //    83: ifeq            138
        //    86: getstatic       com/google/android/gms/internal/measurement/zzsg.zzbqo:[Ljava/lang/String;
        //    89: astore_2       
        //    90: getstatic       com/google/android/gms/internal/measurement/zzsg.zzbqh:Ljava/util/HashMap;
        //    93: aload_0        
        //    94: aload_2        
        //    95: invokestatic    com/google/android/gms/internal/measurement/zzsg.zza:(Landroid/content/ContentResolver;[Ljava/lang/String;)Ljava/util/Map;
        //    98: invokevirtual   java/util/HashMap.putAll:(Ljava/util/Map;)V
        //   101: iconst_1       
        //   102: putstatic       com/google/android/gms/internal/measurement/zzsg.zzbqn:Z
        //   105: getstatic       com/google/android/gms/internal/measurement/zzsg.zzbqh:Ljava/util/HashMap;
        //   108: aload_1        
        //   109: invokevirtual   java/util/HashMap.containsKey:(Ljava/lang/Object;)Z
        //   112: ifeq            138
        //   115: getstatic       com/google/android/gms/internal/measurement/zzsg.zzbqh:Ljava/util/HashMap;
        //   118: aload_1        
        //   119: invokevirtual   java/util/HashMap.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //   122: checkcast       Ljava/lang/String;
        //   125: astore_0       
        //   126: aload_0        
        //   127: ifnull          299
        //   130: goto            133
        //   133: ldc             Lcom/google/android/gms/internal/measurement/zzsg;.class
        //   135: monitorexit    
        //   136: aload_0        
        //   137: areturn        
        //   138: ldc             Lcom/google/android/gms/internal/measurement/zzsg;.class
        //   140: monitorexit    
        //   141: aconst_null    
        //   142: areturn        
        //   143: ldc             Lcom/google/android/gms/internal/measurement/zzsg;.class
        //   145: monitorexit    
        //   146: aload_0        
        //   147: getstatic       com/google/android/gms/internal/measurement/zzsg.CONTENT_URI:Landroid/net/Uri;
        //   150: aconst_null    
        //   151: aconst_null    
        //   152: iconst_1       
        //   153: anewarray       Ljava/lang/String;
        //   156: dup            
        //   157: iconst_0       
        //   158: aload_1        
        //   159: aastore        
        //   160: aconst_null    
        //   161: invokevirtual   android/content/ContentResolver.query:(Landroid/net/Uri;[Ljava/lang/String;Ljava/lang/String;[Ljava/lang/String;Ljava/lang/String;)Landroid/database/Cursor;
        //   164: astore          5
        //   166: aload           5
        //   168: ifnonnull       185
        //   171: aload           5
        //   173: ifnull          183
        //   176: aload           5
        //   178: invokeinterface android/database/Cursor.close:()V
        //   183: aconst_null    
        //   184: areturn        
        //   185: aload           5
        //   187: invokeinterface android/database/Cursor.moveToFirst:()Z
        //   192: ifne            216
        //   195: aload           6
        //   197: aload_1        
        //   198: aconst_null    
        //   199: invokestatic    com/google/android/gms/internal/measurement/zzsg.zza:(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/String;)V
        //   202: aload           5
        //   204: ifnull          214
        //   207: aload           5
        //   209: invokeinterface android/database/Cursor.close:()V
        //   214: aconst_null    
        //   215: areturn        
        //   216: aload           5
        //   218: iconst_1       
        //   219: invokeinterface android/database/Cursor.getString:(I)Ljava/lang/String;
        //   224: astore_2       
        //   225: aload_2        
        //   226: astore_0       
        //   227: aload_2        
        //   228: ifnull          243
        //   231: aload_2        
        //   232: astore_0       
        //   233: aload_2        
        //   234: aconst_null    
        //   235: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //   238: ifeq            243
        //   241: aconst_null    
        //   242: astore_0       
        //   243: aload           6
        //   245: aload_1        
        //   246: aload_0        
        //   247: invokestatic    com/google/android/gms/internal/measurement/zzsg.zza:(Ljava/lang/Object;Ljava/lang/String;Ljava/lang/String;)V
        //   250: aload_0        
        //   251: ifnull          257
        //   254: goto            259
        //   257: aconst_null    
        //   258: astore_0       
        //   259: aload           5
        //   261: ifnull          271
        //   264: aload           5
        //   266: invokeinterface android/database/Cursor.close:()V
        //   271: aload_0        
        //   272: areturn        
        //   273: astore_0       
        //   274: aload           5
        //   276: ifnull          286
        //   279: aload           5
        //   281: invokeinterface android/database/Cursor.close:()V
        //   286: aload_0        
        //   287: athrow         
        //   288: astore_0       
        //   289: ldc             Lcom/google/android/gms/internal/measurement/zzsg;.class
        //   291: monitorexit    
        //   292: aload_0        
        //   293: athrow         
        //   294: aconst_null    
        //   295: astore_0       
        //   296: goto            40
        //   299: aconst_null    
        //   300: astore_0       
        //   301: goto            133
        //   304: iload_3        
        //   305: iconst_1       
        //   306: iadd           
        //   307: istore_3       
        //   308: goto            55
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type
        //  -----  -----  -----  -----  ----
        //  3      33     288    294    Any
        //  40     43     288    294    Any
        //  45     53     288    294    Any
        //  61     86     288    294    Any
        //  86     126    288    294    Any
        //  133    136    288    294    Any
        //  138    141    288    294    Any
        //  143    146    288    294    Any
        //  185    202    273    288    Any
        //  216    225    273    288    Any
        //  233    241    273    288    Any
        //  243    250    273    288    Any
        //  289    292    288    294    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        //     at com.strobel.assembler.ir.StackMappingVisitor.push(StackMappingVisitor.java:290)
        //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.execute(StackMappingVisitor.java:833)
        //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.visit(StackMappingVisitor.java:398)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2030)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
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
    
    private static Map<String, String> zza(ContentResolver query, final String... array) {
        query = (ContentResolver)query.query(zzsg.zzbqd, (String[])null, (String)null, array, (String)null);
        final TreeMap<String, String> treeMap = new TreeMap<String, String>();
        if (query == null) {
            return treeMap;
        }
        try {
            while (((Cursor)query).moveToNext()) {
                treeMap.put(((Cursor)query).getString(0), ((Cursor)query).getString(1));
            }
            return treeMap;
        }
        finally {
            ((Cursor)query).close();
        }
    }
    
    private static void zza(final ContentResolver contentResolver) {
        if (zzsg.zzbqh == null) {
            zzsg.zzbqg.set(false);
            zzsg.zzbqh = new HashMap<String, String>();
            zzsg.zzbqm = new Object();
            zzsg.zzbqn = false;
            contentResolver.registerContentObserver(zzsg.CONTENT_URI, true, (ContentObserver)new zzsh(null));
            return;
        }
        if (zzsg.zzbqg.getAndSet(false)) {
            zzsg.zzbqh.clear();
            zzsg.zzbqi.clear();
            zzsg.zzbqj.clear();
            zzsg.zzbqk.clear();
            zzsg.zzbql.clear();
            zzsg.zzbqm = new Object();
            zzsg.zzbqn = false;
        }
    }
    
    private static void zza(final Object o, final String s, final String s2) {
        synchronized (zzsg.class) {
            if (o == zzsg.zzbqm) {
                zzsg.zzbqh.put(s, s2);
            }
        }
    }
    
    private static <T> void zza(final Object o, final HashMap<String, T> hashMap, final String s, final T t) {
        synchronized (zzsg.class) {
            if (o == zzsg.zzbqm) {
                hashMap.put(s, t);
                zzsg.zzbqh.remove(s);
            }
        }
    }
    
    public static boolean zza(final ContentResolver contentResolver, final String s, final boolean b) {
        final Object zzb = zzb(contentResolver);
        final Boolean b2 = zza(zzsg.zzbqi, s, b);
        if (b2 != null) {
            return b2;
        }
        final String zza = zza(contentResolver, s, null);
        Boolean b3 = b2;
        boolean b4 = b;
        if (zza != null) {
            if (zza.equals("")) {
                b3 = b2;
                b4 = b;
            }
            else if (zzsg.zzbqe.matcher(zza).matches()) {
                b3 = true;
                b4 = true;
            }
            else if (zzsg.zzbqf.matcher(zza).matches()) {
                b3 = false;
                b4 = false;
            }
            else {
                final StringBuilder sb = new StringBuilder("attempt to read gservices key ");
                sb.append(s);
                sb.append(" (value \"");
                sb.append(zza);
                sb.append("\") as boolean");
                Log.w("Gservices", sb.toString());
                b4 = b;
                b3 = b2;
            }
        }
        zza(zzb, zzsg.zzbqi, s, b3);
        return b4;
    }
    
    private static Object zzb(final ContentResolver contentResolver) {
        synchronized (zzsg.class) {
            zza(contentResolver);
            return zzsg.zzbqm;
        }
    }
}
