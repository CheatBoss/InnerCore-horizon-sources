package org.mozilla.javascript;

import java.lang.ref.*;
import java.util.*;
import java.lang.reflect.*;
import java.net.*;
import java.io.*;
import java.security.*;

public abstract class SecureCaller
{
    private static final Map<CodeSource, Map<ClassLoader, SoftReference<SecureCaller>>> callers;
    private static final byte[] secureCallerImplBytecode;
    
    static {
        secureCallerImplBytecode = loadBytecode();
        callers = new WeakHashMap<CodeSource, Map<ClassLoader, SoftReference<SecureCaller>>>();
    }
    
    static Object callSecurely(final CodeSource p0, final Callable p1, final Context p2, final Scriptable p3, final Scriptable p4, final Object[] p5) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: dup            
        //     4: invokestatic    java/lang/Thread.currentThread:()Ljava/lang/Thread;
        //     7: invokespecial   org/mozilla/javascript/SecureCaller$1.<init>:(Ljava/lang/Thread;)V
        //    10: invokestatic    java/security/AccessController.doPrivileged:(Ljava/security/PrivilegedAction;)Ljava/lang/Object;
        //    13: checkcast       Ljava/lang/ClassLoader;
        //    16: astore          10
        //    18: getstatic       org/mozilla/javascript/SecureCaller.callers:Ljava/util/Map;
        //    21: astore          8
        //    23: aload           8
        //    25: monitorenter   
        //    26: aconst_null    
        //    27: astore          9
        //    29: getstatic       org/mozilla/javascript/SecureCaller.callers:Ljava/util/Map;
        //    32: aload_0        
        //    33: invokeinterface java/util/Map.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //    38: checkcast       Ljava/util/Map;
        //    41: astore          7
        //    43: aload           7
        //    45: astore          6
        //    47: aload           7
        //    49: ifnonnull       73
        //    52: new             Ljava/util/WeakHashMap;
        //    55: dup            
        //    56: invokespecial   java/util/WeakHashMap.<init>:()V
        //    59: astore          6
        //    61: getstatic       org/mozilla/javascript/SecureCaller.callers:Ljava/util/Map;
        //    64: aload_0        
        //    65: aload           6
        //    67: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //    72: pop            
        //    73: aload           8
        //    75: monitorexit    
        //    76: aload           6
        //    78: monitorenter   
        //    79: aload           8
        //    81: astore          7
        //    83: aload           6
        //    85: aload           10
        //    87: invokeinterface java/util/Map.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //    92: checkcast       Ljava/lang/ref/SoftReference;
        //    95: astore          11
        //    97: aload           11
        //    99: ifnull          119
        //   102: aload           8
        //   104: astore          7
        //   106: aload           11
        //   108: invokevirtual   java/lang/ref/SoftReference.get:()Ljava/lang/Object;
        //   111: checkcast       Lorg/mozilla/javascript/SecureCaller;
        //   114: astore          7
        //   116: goto            123
        //   119: aload           9
        //   121: astore          7
        //   123: aload           7
        //   125: astore          8
        //   127: aload           7
        //   129: ifnonnull       197
        //   132: aload           7
        //   134: astore          9
        //   136: new             Lorg/mozilla/javascript/SecureCaller$2;
        //   139: dup            
        //   140: aload           10
        //   142: aload_0        
        //   143: invokespecial   org/mozilla/javascript/SecureCaller$2.<init>:(Ljava/lang/ClassLoader;Ljava/security/CodeSource;)V
        //   146: invokestatic    java/security/AccessController.doPrivileged:(Ljava/security/PrivilegedExceptionAction;)Ljava/lang/Object;
        //   149: checkcast       Lorg/mozilla/javascript/SecureCaller;
        //   152: astore          8
        //   154: aload           8
        //   156: astore          9
        //   158: aload           8
        //   160: astore          7
        //   162: aload           6
        //   164: aload           10
        //   166: new             Ljava/lang/ref/SoftReference;
        //   169: dup            
        //   170: aload           8
        //   172: invokespecial   java/lang/ref/SoftReference.<init>:(Ljava/lang/Object;)V
        //   175: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   180: pop            
        //   181: goto            197
        //   184: astore_0       
        //   185: new             Ljava/lang/reflect/UndeclaredThrowableException;
        //   188: dup            
        //   189: aload_0        
        //   190: invokevirtual   java/security/PrivilegedActionException.getCause:()Ljava/lang/Throwable;
        //   193: invokespecial   java/lang/reflect/UndeclaredThrowableException.<init>:(Ljava/lang/Throwable;)V
        //   196: athrow         
        //   197: aload           8
        //   199: astore          7
        //   201: aload           6
        //   203: monitorexit    
        //   204: aload           8
        //   206: aload_1        
        //   207: aload_2        
        //   208: aload_3        
        //   209: aload           4
        //   211: aload           5
        //   213: invokevirtual   org/mozilla/javascript/SecureCaller.call:(Lorg/mozilla/javascript/Callable;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;Lorg/mozilla/javascript/Scriptable;[Ljava/lang/Object;)Ljava/lang/Object;
        //   216: areturn        
        //   217: astore_1       
        //   218: aload           6
        //   220: astore_0       
        //   221: aload_0        
        //   222: astore          6
        //   224: aload_0        
        //   225: monitorexit    
        //   226: aload_1        
        //   227: athrow         
        //   228: astore_1       
        //   229: aload           6
        //   231: astore_0       
        //   232: goto            221
        //   235: astore_0       
        //   236: aload           8
        //   238: monitorexit    
        //   239: aload_0        
        //   240: athrow         
        //   241: astore_0       
        //   242: goto            236
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                     
        //  -----  -----  -----  -----  -----------------------------------------
        //  29     43     235    236    Any
        //  52     73     241    245    Any
        //  73     76     241    245    Any
        //  83     97     217    221    Any
        //  106    116    217    221    Any
        //  136    154    184    197    Ljava/security/PrivilegedActionException;
        //  136    154    217    221    Any
        //  162    181    184    197    Ljava/security/PrivilegedActionException;
        //  162    181    217    221    Any
        //  185    197    228    235    Any
        //  201    204    217    221    Any
        //  224    226    228    235    Any
        //  236    239    241    245    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0197:
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
    
    private static byte[] loadBytecode() {
        return AccessController.doPrivileged((PrivilegedAction<byte[]>)new PrivilegedAction<Object>() {
            @Override
            public Object run() {
                return loadBytecodePrivileged();
            }
        });
    }
    
    private static byte[] loadBytecodePrivileged() {
        final URL resource = SecureCaller.class.getResource("SecureCallerImpl.clazz");
        try {
            final InputStream openStream = resource.openStream();
            try {
                final ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                while (true) {
                    final int read = openStream.read();
                    if (read == -1) {
                        break;
                    }
                    byteArrayOutputStream.write(read);
                }
                return byteArrayOutputStream.toByteArray();
            }
            finally {
                openStream.close();
            }
        }
        catch (IOException ex) {
            throw new UndeclaredThrowableException(ex);
        }
    }
    
    public abstract Object call(final Callable p0, final Context p1, final Scriptable p2, final Scriptable p3, final Object[] p4);
    
    private static class SecureClassLoaderImpl extends SecureClassLoader
    {
        SecureClassLoaderImpl(final ClassLoader classLoader) {
            super(classLoader);
        }
        
        Class<?> defineAndLinkClass(final String s, final byte[] array, final CodeSource codeSource) {
            final Class<?> defineClass = this.defineClass(s, array, 0, array.length, codeSource);
            this.resolveClass(defineClass);
            return defineClass;
        }
    }
}
