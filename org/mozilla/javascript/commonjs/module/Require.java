package org.mozilla.javascript.commonjs.module;

import java.util.*;
import java.util.concurrent.*;
import org.mozilla.javascript.*;
import java.net.*;
import java.io.*;

public class Require extends BaseFunction
{
    private static final ThreadLocal<Map<String, Scriptable>> loadingModuleInterfaces;
    private static final long serialVersionUID = 1L;
    private final Map<String, Scriptable> exportedModuleInterfaces;
    private final Object loadLock;
    private Scriptable mainExports;
    private String mainModuleId;
    private final ModuleScriptProvider moduleScriptProvider;
    private final Scriptable nativeScope;
    private final Scriptable paths;
    private final Script postExec;
    private final Script preExec;
    private final boolean sandboxed;
    
    static {
        loadingModuleInterfaces = new ThreadLocal<Map<String, Scriptable>>();
    }
    
    public Require(final Context context, final Scriptable nativeScope, final ModuleScriptProvider moduleScriptProvider, final Script preExec, final Script postExec, final boolean sandboxed) {
        this.mainModuleId = null;
        this.exportedModuleInterfaces = new ConcurrentHashMap<String, Scriptable>();
        this.loadLock = new Object();
        this.moduleScriptProvider = moduleScriptProvider;
        this.nativeScope = nativeScope;
        this.sandboxed = sandboxed;
        this.preExec = preExec;
        this.postExec = postExec;
        this.setPrototype(ScriptableObject.getFunctionPrototype(nativeScope));
        if (!sandboxed) {
            defineReadOnlyProperty(this, "paths", this.paths = context.newArray(nativeScope, 0));
            return;
        }
        this.paths = null;
    }
    
    private static void defineReadOnlyProperty(final ScriptableObject scriptableObject, final String s, final Object o) {
        ScriptableObject.putProperty(scriptableObject, s, o);
        scriptableObject.setAttributes(s, 5);
    }
    
    private Scriptable executeModuleScript(final Context context, final String s, final Scriptable scriptable, final ModuleScript moduleScript, final boolean b) {
        final ScriptableObject scriptableObject = (ScriptableObject)context.newObject(this.nativeScope);
        final URI uri = moduleScript.getUri();
        final URI base = moduleScript.getBase();
        defineReadOnlyProperty(scriptableObject, "id", s);
        if (!this.sandboxed) {
            defineReadOnlyProperty(scriptableObject, "uri", uri.toString());
        }
        final ModuleScope moduleScope = new ModuleScope(this.nativeScope, uri, base);
        moduleScope.put("exports", moduleScope, scriptable);
        moduleScope.put("module", moduleScope, scriptableObject);
        scriptableObject.put("exports", scriptableObject, scriptable);
        this.install(moduleScope);
        if (b) {
            defineReadOnlyProperty(this, "main", scriptableObject);
        }
        executeOptionalScript(this.preExec, context, moduleScope);
        moduleScript.getScript().exec(context, moduleScope);
        executeOptionalScript(this.postExec, context, moduleScope);
        return ScriptRuntime.toObject(context, this.nativeScope, ScriptableObject.getProperty(scriptableObject, "exports"));
    }
    
    private static void executeOptionalScript(final Script script, final Context context, final Scriptable scriptable) {
        if (script != null) {
            script.exec(context, scriptable);
        }
    }
    
    private Scriptable getExportedModuleInterface(final Context p0, final String p1, final URI p2, final URI p3, final boolean p4) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: getfield        org/mozilla/javascript/commonjs/module/Require.exportedModuleInterfaces:Ljava/util/Map;
        //     4: aload_2        
        //     5: invokeinterface java/util/Map.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //    10: checkcast       Lorg/mozilla/javascript/Scriptable;
        //    13: astore          7
        //    15: aload           7
        //    17: ifnull          38
        //    20: iload           5
        //    22: ifeq            35
        //    25: new             Ljava/lang/IllegalStateException;
        //    28: dup            
        //    29: ldc             "Attempt to set main module after it was loaded"
        //    31: invokespecial   java/lang/IllegalStateException.<init>:(Ljava/lang/String;)V
        //    34: athrow         
        //    35: aload           7
        //    37: areturn        
        //    38: getstatic       org/mozilla/javascript/commonjs/module/Require.loadingModuleInterfaces:Ljava/lang/ThreadLocal;
        //    41: invokevirtual   java/lang/ThreadLocal.get:()Ljava/lang/Object;
        //    44: checkcast       Ljava/util/Map;
        //    47: astore          8
        //    49: aload           8
        //    51: ifnull          79
        //    54: aload           8
        //    56: aload_2        
        //    57: invokeinterface java/util/Map.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //    62: checkcast       Lorg/mozilla/javascript/Scriptable;
        //    65: astore          9
        //    67: aload           9
        //    69: astore          7
        //    71: aload           9
        //    73: ifnull          79
        //    76: aload           9
        //    78: areturn        
        //    79: aload_0        
        //    80: getfield        org/mozilla/javascript/commonjs/module/Require.loadLock:Ljava/lang/Object;
        //    83: astore          10
        //    85: aload           10
        //    87: monitorenter   
        //    88: aload_0        
        //    89: getfield        org/mozilla/javascript/commonjs/module/Require.exportedModuleInterfaces:Ljava/util/Map;
        //    92: aload_2        
        //    93: invokeinterface java/util/Map.get:(Ljava/lang/Object;)Ljava/lang/Object;
        //    98: checkcast       Lorg/mozilla/javascript/Scriptable;
        //   101: astore          9
        //   103: aload           9
        //   105: ifnull          118
        //   108: aload           9
        //   110: astore          7
        //   112: aload           10
        //   114: monitorexit    
        //   115: aload           9
        //   117: areturn        
        //   118: aload           9
        //   120: astore          7
        //   122: aload_0        
        //   123: aload_1        
        //   124: aload_2        
        //   125: aload_3        
        //   126: aload           4
        //   128: invokespecial   org/mozilla/javascript/commonjs/module/Require.getModule:(Lorg/mozilla/javascript/Context;Ljava/lang/String;Ljava/net/URI;Ljava/net/URI;)Lorg/mozilla/javascript/commonjs/module/ModuleScript;
        //   131: astore          11
        //   133: aload           9
        //   135: astore          7
        //   137: aload_0        
        //   138: getfield        org/mozilla/javascript/commonjs/module/Require.sandboxed:Z
        //   141: ifeq            228
        //   144: aload           9
        //   146: astore          7
        //   148: aload           11
        //   150: invokevirtual   org/mozilla/javascript/commonjs/module/ModuleScript.isSandboxed:()Z
        //   153: ifne            228
        //   156: aload           9
        //   158: astore          7
        //   160: aload_0        
        //   161: getfield        org/mozilla/javascript/commonjs/module/Require.nativeScope:Lorg/mozilla/javascript/Scriptable;
        //   164: astore_3       
        //   165: aload           9
        //   167: astore          7
        //   169: new             Ljava/lang/StringBuilder;
        //   172: dup            
        //   173: invokespecial   java/lang/StringBuilder.<init>:()V
        //   176: astore          4
        //   178: aload           9
        //   180: astore          7
        //   182: aload           4
        //   184: ldc             "Module \""
        //   186: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   189: pop            
        //   190: aload           9
        //   192: astore          7
        //   194: aload           4
        //   196: aload_2        
        //   197: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   200: pop            
        //   201: aload           9
        //   203: astore          7
        //   205: aload           4
        //   207: ldc             "\" is not contained in sandbox."
        //   209: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   212: pop            
        //   213: aload           9
        //   215: astore          7
        //   217: aload_1        
        //   218: aload_3        
        //   219: aload           4
        //   221: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   224: invokestatic    org/mozilla/javascript/ScriptRuntime.throwError:(Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/Scriptable;Ljava/lang/String;)Lorg/mozilla/javascript/JavaScriptException;
        //   227: athrow         
        //   228: aload           9
        //   230: astore          7
        //   232: aload_1        
        //   233: aload_0        
        //   234: getfield        org/mozilla/javascript/commonjs/module/Require.nativeScope:Lorg/mozilla/javascript/Scriptable;
        //   237: invokevirtual   org/mozilla/javascript/Context.newObject:(Lorg/mozilla/javascript/Scriptable;)Lorg/mozilla/javascript/Scriptable;
        //   240: astore          4
        //   242: aload           8
        //   244: ifnonnull       253
        //   247: iconst_1       
        //   248: istore          6
        //   250: goto            256
        //   253: iconst_0       
        //   254: istore          6
        //   256: aload           8
        //   258: astore_3       
        //   259: iload           6
        //   261: ifeq            279
        //   264: new             Ljava/util/HashMap;
        //   267: dup            
        //   268: invokespecial   java/util/HashMap.<init>:()V
        //   271: astore_3       
        //   272: getstatic       org/mozilla/javascript/commonjs/module/Require.loadingModuleInterfaces:Ljava/lang/ThreadLocal;
        //   275: aload_3        
        //   276: invokevirtual   java/lang/ThreadLocal.set:(Ljava/lang/Object;)V
        //   279: aload_3        
        //   280: aload_2        
        //   281: aload           4
        //   283: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   288: pop            
        //   289: aload_0        
        //   290: aload_1        
        //   291: aload_2        
        //   292: aload           4
        //   294: aload           11
        //   296: iload           5
        //   298: invokespecial   org/mozilla/javascript/commonjs/module/Require.executeModuleScript:(Lorg/mozilla/javascript/Context;Ljava/lang/String;Lorg/mozilla/javascript/Scriptable;Lorg/mozilla/javascript/commonjs/module/ModuleScript;Z)Lorg/mozilla/javascript/Scriptable;
        //   301: astore_1       
        //   302: aload           4
        //   304: aload_1        
        //   305: if_acmpeq       320
        //   308: aload_3        
        //   309: aload_2        
        //   310: aload_1        
        //   311: invokeinterface java/util/Map.put:(Ljava/lang/Object;Ljava/lang/Object;)Ljava/lang/Object;
        //   316: pop            
        //   317: goto            323
        //   320: aload           4
        //   322: astore_1       
        //   323: iload           6
        //   325: ifeq            348
        //   328: aload_0        
        //   329: getfield        org/mozilla/javascript/commonjs/module/Require.exportedModuleInterfaces:Ljava/util/Map;
        //   332: aload_3        
        //   333: invokeinterface java/util/Map.putAll:(Ljava/util/Map;)V
        //   338: getstatic       org/mozilla/javascript/commonjs/module/Require.loadingModuleInterfaces:Ljava/lang/ThreadLocal;
        //   341: aconst_null    
        //   342: invokevirtual   java/lang/ThreadLocal.set:(Ljava/lang/Object;)V
        //   345: goto            348
        //   348: aload           10
        //   350: monitorexit    
        //   351: aload_1        
        //   352: areturn        
        //   353: astore_1       
        //   354: goto            368
        //   357: astore_1       
        //   358: aload_3        
        //   359: aload_2        
        //   360: invokeinterface java/util/Map.remove:(Ljava/lang/Object;)Ljava/lang/Object;
        //   365: pop            
        //   366: aload_1        
        //   367: athrow         
        //   368: iload           6
        //   370: ifeq            390
        //   373: aload_0        
        //   374: getfield        org/mozilla/javascript/commonjs/module/Require.exportedModuleInterfaces:Ljava/util/Map;
        //   377: aload_3        
        //   378: invokeinterface java/util/Map.putAll:(Ljava/util/Map;)V
        //   383: getstatic       org/mozilla/javascript/commonjs/module/Require.loadingModuleInterfaces:Ljava/lang/ThreadLocal;
        //   386: aconst_null    
        //   387: invokevirtual   java/lang/ThreadLocal.set:(Ljava/lang/Object;)V
        //   390: aload_1        
        //   391: athrow         
        //   392: astore_1       
        //   393: goto            397
        //   396: astore_1       
        //   397: aload           10
        //   399: monitorexit    
        //   400: aload_1        
        //   401: athrow         
        //   402: astore_1       
        //   403: goto            397
        //   406: astore_1       
        //   407: goto            397
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                        
        //  -----  -----  -----  -----  ----------------------------
        //  88     103    396    397    Any
        //  112    115    396    397    Any
        //  122    133    396    397    Any
        //  137    144    396    397    Any
        //  148    156    396    397    Any
        //  160    165    396    397    Any
        //  169    178    396    397    Any
        //  182    190    396    397    Any
        //  194    201    396    397    Any
        //  205    213    396    397    Any
        //  217    228    396    397    Any
        //  232    242    396    397    Any
        //  264    279    402    406    Any
        //  279    289    392    396    Any
        //  289    302    357    368    Ljava/lang/RuntimeException;
        //  289    302    353    392    Any
        //  308    317    357    368    Ljava/lang/RuntimeException;
        //  308    317    353    392    Any
        //  328    345    406    410    Any
        //  348    351    406    410    Any
        //  358    368    353    392    Any
        //  373    390    392    396    Any
        //  390    392    392    396    Any
        //  397    400    402    406    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index: 203, Size: 203
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
    
    private ModuleScript getModule(final Context context, final String s, final URI uri, final URI uri2) {
        try {
            final ModuleScript moduleScript = this.moduleScriptProvider.getModuleScript(context, s, uri, uri2, this.paths);
            if (moduleScript == null) {
                final Scriptable nativeScope = this.nativeScope;
                final StringBuilder sb = new StringBuilder();
                sb.append("Module \"");
                sb.append(s);
                sb.append("\" not found.");
                throw ScriptRuntime.throwError(context, nativeScope, sb.toString());
            }
            return moduleScript;
        }
        catch (Exception ex) {
            throw Context.throwAsScriptRuntimeEx(ex);
        }
        catch (RuntimeException ex2) {
            throw ex2;
        }
    }
    
    @Override
    public Object call(final Context context, final Scriptable scriptable, final Scriptable scriptable2, final Object[] array) {
        if (array != null && array.length >= 1) {
            final String s = (String)Context.jsToJava(array[0], String.class);
            URI uri = null;
            URI uri2 = null;
            if (!s.startsWith("./")) {
                final String s2 = s;
                if (!s.startsWith("../")) {
                    return this.getExportedModuleInterface(context, s2, uri, uri2, false);
                }
            }
            if (!(scriptable2 instanceof ModuleScope)) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Can't resolve relative module ID \"");
                sb.append(s);
                sb.append("\" when require() is used outside of a module");
                throw ScriptRuntime.throwError(context, scriptable, sb.toString());
            }
            final ModuleScope moduleScope = (ModuleScope)scriptable2;
            final URI base = moduleScope.getBase();
            final URI uri3 = moduleScope.getUri();
            final URI resolve = uri3.resolve(s);
            String s2;
            if (base == null) {
                s2 = resolve.toString();
                uri = resolve;
                uri2 = base;
            }
            else {
                final String s3 = s2 = base.relativize(uri3).resolve(s).toString();
                uri = resolve;
                uri2 = base;
                if (s3.charAt(0) == '.') {
                    if (this.sandboxed) {
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("Module \"");
                        sb2.append(s3);
                        sb2.append("\" is not contained in sandbox.");
                        throw ScriptRuntime.throwError(context, scriptable, sb2.toString());
                    }
                    s2 = resolve.toString();
                    uri2 = base;
                    uri = resolve;
                }
            }
            return this.getExportedModuleInterface(context, s2, uri, uri2, false);
        }
        throw ScriptRuntime.throwError(context, scriptable, "require() needs one argument");
    }
    
    @Override
    public Scriptable construct(final Context context, final Scriptable scriptable, final Object[] array) {
        throw ScriptRuntime.throwError(context, scriptable, "require() can not be invoked as a constructor");
    }
    
    @Override
    public int getArity() {
        return 1;
    }
    
    @Override
    public String getFunctionName() {
        return "require";
    }
    
    @Override
    public int getLength() {
        return 1;
    }
    
    public void install(final Scriptable scriptable) {
        ScriptableObject.putProperty(scriptable, "require", this);
    }
    
    public Scriptable requireMain(final Context context, final String mainModuleId) {
        if (this.mainModuleId != null) {
            if (!this.mainModuleId.equals(mainModuleId)) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Main module already set to ");
                sb.append(this.mainModuleId);
                throw new IllegalStateException(sb.toString());
            }
            return this.mainExports;
        }
        else {
            try {
                if (this.moduleScriptProvider.getModuleScript(context, mainModuleId, null, null, this.paths) != null) {
                    this.mainExports = this.getExportedModuleInterface(context, mainModuleId, null, null, true);
                }
                else if (!this.sandboxed) {
                    URI uri = null;
                    try {
                        uri = new URI(mainModuleId);
                    }
                    catch (URISyntaxException ex3) {}
                    URI uri2 = null;
                    Label_0209: {
                        if (uri != null) {
                            uri2 = uri;
                            if (uri.isAbsolute()) {
                                break Label_0209;
                            }
                        }
                        final File file = new File(mainModuleId);
                        if (!file.isFile()) {
                            final Scriptable nativeScope = this.nativeScope;
                            final StringBuilder sb2 = new StringBuilder();
                            sb2.append("Module \"");
                            sb2.append(mainModuleId);
                            sb2.append("\" not found.");
                            throw ScriptRuntime.throwError(context, nativeScope, sb2.toString());
                        }
                        uri2 = file.toURI();
                    }
                    this.mainExports = this.getExportedModuleInterface(context, uri2.toString(), uri2, null, true);
                }
                this.mainModuleId = mainModuleId;
                return this.mainExports;
            }
            catch (Exception ex) {
                throw new RuntimeException(ex);
            }
            catch (RuntimeException ex2) {
                throw ex2;
            }
        }
    }
}
