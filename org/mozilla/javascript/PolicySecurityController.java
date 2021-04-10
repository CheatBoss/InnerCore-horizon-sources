package org.mozilla.javascript;

import java.lang.ref.*;
import java.util.*;
import org.mozilla.classfile.*;
import java.security.*;

public class PolicySecurityController extends SecurityController
{
    private static final Map<CodeSource, Map<ClassLoader, SoftReference<SecureCaller>>> callers;
    private static final byte[] secureCallerImplBytecode;
    
    static {
        secureCallerImplBytecode = loadBytecode();
        callers = new WeakHashMap<CodeSource, Map<ClassLoader, SoftReference<SecureCaller>>>();
    }
    
    private static byte[] loadBytecode() {
        final String name = SecureCaller.class.getName();
        final StringBuilder sb = new StringBuilder();
        sb.append(name);
        sb.append("Impl");
        final ClassFileWriter classFileWriter = new ClassFileWriter(sb.toString(), name, "<generated>");
        int i = 1;
        classFileWriter.startMethod("<init>", "()V", (short)1);
        classFileWriter.addALoad(0);
        classFileWriter.addInvoke(183, name, "<init>", "()V");
        classFileWriter.add(177);
        classFileWriter.stopMethod((short)1);
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("(Lorg/mozilla/javascript/Callable;");
        sb2.append("Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;Lorg/mozilla/javascript/Scriptable;[Ljava/lang/Object;)Ljava/lang/Object;");
        classFileWriter.startMethod("call", sb2.toString(), (short)17);
        while (i < 6) {
            classFileWriter.addALoad(i);
            ++i;
        }
        final StringBuilder sb3 = new StringBuilder();
        sb3.append("(");
        sb3.append("Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;Lorg/mozilla/javascript/Scriptable;[Ljava/lang/Object;)Ljava/lang/Object;");
        classFileWriter.addInvoke(185, "org/mozilla/javascript/Callable", "call", sb3.toString());
        classFileWriter.add(176);
        classFileWriter.stopMethod((short)6);
        return classFileWriter.toByteArray();
    }
    
    @Override
    public Object callWithDomain(final Object p0, final Context p1, final Callable p2, final Scriptable p3, final Scriptable p4, final Object[] p5) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: dup            
        //     4: aload_0        
        //     5: aload_2        
        //     6: invokespecial   org/mozilla/javascript/PolicySecurityController$2.<init>:(Lorg/mozilla/javascript/PolicySecurityController;Lorg/mozilla/javascript/Context;)V
        //     9: invokestatic    java/security/AccessController.doPrivileged:(Ljava/security/PrivilegedAction;)Ljava/lang/Object;
        //    12: checkcast       Ljava/lang/ClassLoader;
        //    15: astore          10
        //    17: aload_1        
        //    18: checkcast       Ljava/security/CodeSource;
        //    21: astore          11
        //    23: getstatic       org/mozilla/javascript/PolicySecurityController.callers:Ljava/util/Map;
        //    26: astore          7
        //    28: aload           7
        //    30: monitorenter   
        //    31: aconst_null    
        //    32: astore          9
        //    34: getstatic       org/mozilla/javascript/PolicySecurityController.callers:Ljava/util/Map;
        //    37: aload           11
        //    39: invokeinterface java/util/Map.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //    44: checkcast       Ljava/util/Map;
        //    47: astore_1       
        //    48: aload_1        
        //    49: astore          8
        //    51: aload_1        
        //    52: ifnonnull       77
        //    55: new             Ljava/util/WeakHashMap;
        //    58: dup            
        //    59: invokespecial   java/util/WeakHashMap.<init>:()V
        //    62: astore          8
        //    64: getstatic       org/mozilla/javascript/PolicySecurityController.callers:Ljava/util/Map;
        //    67: aload           11
        //    69: aload           8
        //    71: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //    76: pop            
        //    77: aload           7
        //    79: monitorexit    
        //    80: aload           8
        //    82: monitorenter   
        //    83: aload           7
        //    85: astore_1       
        //    86: aload           8
        //    88: aload           10
        //    90: invokeinterface java/util/Map.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //    95: checkcast       Ljava/lang/ref/SoftReference;
        //    98: astore          12
        //   100: aload           12
        //   102: ifnull          120
        //   105: aload           7
        //   107: astore_1       
        //   108: aload           12
        //   110: invokevirtual   java/lang/ref/SoftReference.get:()Ljava/lang/Object;
        //   113: checkcast       Lorg/mozilla/javascript/PolicySecurityController$SecureCaller;
        //   116: astore_1       
        //   117: goto            123
        //   120: aload           9
        //   122: astore_1       
        //   123: aload_1        
        //   124: astore          7
        //   126: aload_1        
        //   127: ifnonnull       195
        //   130: aload_1        
        //   131: astore          9
        //   133: new             Lorg/mozilla/javascript/PolicySecurityController$3;
        //   136: dup            
        //   137: aload_0        
        //   138: aload           10
        //   140: aload           11
        //   142: invokespecial   org/mozilla/javascript/PolicySecurityController$3.<init>:(Lorg/mozilla/javascript/PolicySecurityController;Ljava/lang/ClassLoader;Ljava/security/CodeSource;)V
        //   145: invokestatic    java/security/AccessController.doPrivileged:(Ljava/security/PrivilegedExceptionAction;)Ljava/lang/Object;
        //   148: checkcast       Lorg/mozilla/javascript/PolicySecurityController$SecureCaller;
        //   151: astore          7
        //   153: aload           7
        //   155: astore          9
        //   157: aload           7
        //   159: astore_1       
        //   160: aload           8
        //   162: aload           10
        //   164: new             Ljava/lang/ref/SoftReference;
        //   167: dup            
        //   168: aload           7
        //   170: invokespecial   java/lang/ref/SoftReference.<init>:(Ljava/lang/Object;)V
        //   173: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   178: pop            
        //   179: goto            195
        //   182: astore_1       
        //   183: new             Ljava/lang/reflect/UndeclaredThrowableException;
        //   186: dup            
        //   187: aload_1        
        //   188: invokevirtual   java/security/PrivilegedActionException.getCause:()Ljava/lang/Throwable;
        //   191: invokespecial   java/lang/reflect/UndeclaredThrowableException.<init>:(Ljava/lang/Throwable;)V
        //   194: athrow         
        //   195: aload           8
        //   197: monitorexit    
        //   198: aload           7
        //   200: aload_3        
        //   201: aload_2        
        //   202: aload           4
        //   204: aload           5
        //   206: aload           6
        //   208: invokevirtual   org/mozilla/javascript/PolicySecurityController$SecureCaller.call:(Lorg/mozilla/javascript/Callable;Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;Lorg/mozilla/javascript/Scriptable;[Ljava/lang/Object;)Ljava/lang/Object;
        //   211: areturn        
        //   212: astore_1       
        //   213: goto            217
        //   216: astore_1       
        //   217: aload           8
        //   219: monitorexit    
        //   220: aload_1        
        //   221: athrow         
        //   222: astore_1       
        //   223: goto            217
        //   226: astore_1       
        //   227: goto            231
        //   230: astore_1       
        //   231: aload           7
        //   233: monitorexit    
        //   234: aload_1        
        //   235: athrow         
        //   236: astore_1       
        //   237: goto            231
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                                     
        //  -----  -----  -----  -----  -----------------------------------------
        //  34     48     230    231    Any
        //  55     77     236    240    Any
        //  77     80     226    230    Any
        //  86     100    216    217    Any
        //  108    117    216    217    Any
        //  133    153    182    195    Ljava/security/PrivilegedActionException;
        //  133    153    216    217    Any
        //  160    179    182    195    Ljava/security/PrivilegedActionException;
        //  160    179    216    217    Any
        //  183    195    222    226    Any
        //  195    198    212    216    Any
        //  217    220    222    226    Any
        //  231    234    236    240    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0195:
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
    
    @Override
    public GeneratedClassLoader createClassLoader(final ClassLoader classLoader, final Object o) {
        return AccessController.doPrivileged((PrivilegedAction<Loader>)new PrivilegedAction<Object>() {
            @Override
            public Object run() {
                return new Loader(classLoader, (CodeSource)o);
            }
        });
    }
    
    @Override
    public Object getDynamicSecurityDomain(final Object o) {
        return o;
    }
    
    @Override
    public Class<?> getStaticSecurityDomainClassInternal() {
        return CodeSource.class;
    }
    
    private static class Loader extends SecureClassLoader implements GeneratedClassLoader
    {
        private final CodeSource codeSource;
        
        Loader(final ClassLoader classLoader, final CodeSource codeSource) {
            super(classLoader);
            this.codeSource = codeSource;
        }
        
        @Override
        public Class<?> defineClass(final String s, final byte[] array) {
            return this.defineClass(s, array, 0, array.length, this.codeSource);
        }
        
        @Override
        public void linkClass(final Class<?> clazz) {
            this.resolveClass(clazz);
        }
    }
    
    public abstract static class SecureCaller
    {
        public abstract Object call(final Callable p0, final Context p1, final Scriptable p2, final Scriptable p3, final Object[] p4);
    }
}
