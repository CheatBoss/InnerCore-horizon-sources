package org.mozilla.javascript.tools.debugger;

import java.net.*;
import java.io.*;
import java.util.*;
import org.mozilla.javascript.debug.*;
import org.mozilla.javascript.*;

public class Dim
{
    public static final int BREAK = 4;
    public static final int EXIT = 5;
    public static final int GO = 3;
    private static final int IPROXY_COMPILE_SCRIPT = 2;
    private static final int IPROXY_DEBUG = 0;
    private static final int IPROXY_EVAL_SCRIPT = 3;
    private static final int IPROXY_LISTEN = 1;
    private static final int IPROXY_OBJECT_IDS = 7;
    private static final int IPROXY_OBJECT_PROPERTY = 6;
    private static final int IPROXY_OBJECT_TO_STRING = 5;
    private static final int IPROXY_STRING_IS_COMPILABLE = 4;
    public static final int STEP_INTO = 1;
    public static final int STEP_OUT = 2;
    public static final int STEP_OVER = 0;
    private boolean breakFlag;
    private boolean breakOnEnter;
    private boolean breakOnExceptions;
    private boolean breakOnReturn;
    private GuiCallback callback;
    private ContextFactory contextFactory;
    private StackFrame evalFrame;
    private String evalRequest;
    private String evalResult;
    private Object eventThreadMonitor;
    private int frameIndex;
    private final Map<String, FunctionSource> functionNames;
    private final Map<DebuggableScript, FunctionSource> functionToSource;
    private boolean insideInterruptLoop;
    private volatile ContextData interruptedContextData;
    private DimIProxy listener;
    private Object monitor;
    private volatile int returnValue;
    private ScopeProvider scopeProvider;
    private SourceProvider sourceProvider;
    private final Map<String, SourceInfo> urlToSourceInfo;
    
    public Dim() {
        this.frameIndex = -1;
        this.monitor = new Object();
        this.eventThreadMonitor = new Object();
        this.returnValue = -1;
        this.urlToSourceInfo = Collections.synchronizedMap(new HashMap<String, SourceInfo>());
        this.functionNames = Collections.synchronizedMap(new HashMap<String, FunctionSource>());
        this.functionToSource = Collections.synchronizedMap(new HashMap<DebuggableScript, FunctionSource>());
    }
    
    private static void collectFunctions_r(final DebuggableScript debuggableScript, final ObjArray objArray) {
        objArray.add(debuggableScript);
        for (int i = 0; i != debuggableScript.getFunctionCount(); ++i) {
            collectFunctions_r(debuggableScript.getFunction(i), objArray);
        }
    }
    
    private static String do_eval(final Context context, final StackFrame stackFrame, final String s) {
        final Debugger debugger = context.getDebugger();
        final Object debuggerContextData = context.getDebuggerContextData();
        final int optimizationLevel = context.getOptimizationLevel();
        context.setDebugger(null, null);
        context.setOptimizationLevel(-1);
        context.setGeneratingDebug(false);
        String message;
        try {
            try {
                final Object call = ((Callable)context.compileString(s, "", 0, null)).call(context, stackFrame.scope, stackFrame.thisObj, ScriptRuntime.emptyArgs);
                if (call != Undefined.instance) {
                    ScriptRuntime.toString(call);
                }
            }
            finally {}
        }
        catch (Exception ex) {
            message = ex.getMessage();
        }
        context.setGeneratingDebug(true);
        context.setOptimizationLevel(optimizationLevel);
        context.setDebugger(debugger, debuggerContextData);
        String s2 = message;
        if (message == null) {
            s2 = "null";
        }
        return s2;
        context.setGeneratingDebug(true);
        context.setOptimizationLevel(optimizationLevel);
        context.setDebugger(debugger, debuggerContextData);
    }
    
    private FunctionSource functionSource(final DebuggableScript debuggableScript) {
        return this.functionToSource.get(debuggableScript);
    }
    
    private static DebuggableScript[] getAllFunctions(final DebuggableScript debuggableScript) {
        final ObjArray objArray = new ObjArray();
        collectFunctions_r(debuggableScript, objArray);
        final DebuggableScript[] array = new DebuggableScript[objArray.size()];
        objArray.toArray(array);
        return array;
    }
    
    private FunctionSource getFunctionSource(final DebuggableScript debuggableScript) {
        final FunctionSource functionSource = this.functionSource(debuggableScript);
        if (functionSource == null) {
            final String normalizedUrl = this.getNormalizedUrl(debuggableScript);
            if (this.sourceInfo(normalizedUrl) == null && !debuggableScript.isGeneratedScript()) {
                final String loadSource = this.loadSource(normalizedUrl);
                if (loadSource != null) {
                    DebuggableScript debuggableScript2 = debuggableScript;
                    while (true) {
                        final DebuggableScript parent = debuggableScript2.getParent();
                        if (parent == null) {
                            break;
                        }
                        debuggableScript2 = parent;
                    }
                    this.registerTopScript(debuggableScript2, loadSource);
                    return this.functionSource(debuggableScript);
                }
            }
        }
        return functionSource;
    }
    
    private String getNormalizedUrl(final DebuggableScript debuggableScript) {
        final String sourceName = debuggableScript.getSourceName();
        if (sourceName == null) {
            return "<stdin>";
        }
        final int length = sourceName.length();
        StringBuilder sb = null;
        int n = 0;
        while (true) {
            final int index = sourceName.indexOf(35, n);
            if (index < 0) {
                break;
            }
            final String s = null;
            int i;
            for (i = index + 1; i != length; ++i) {
                final char char1 = sourceName.charAt(i);
                if ('0' > char1) {
                    break;
                }
                if (char1 > '9') {
                    break;
                }
            }
            int n2 = n;
            String s2 = s;
            if (i != index + 1) {
                n2 = n;
                s2 = s;
                if ("(eval)".regionMatches(0, sourceName, i, 6)) {
                    n2 = i + 6;
                    s2 = "(eval)";
                }
            }
            if (s2 == null) {
                n = n2;
                break;
            }
            StringBuilder sb2;
            if ((sb2 = sb) == null) {
                sb2 = new StringBuilder();
                sb2.append(sourceName.substring(0, index));
            }
            sb2.append(s2);
            n = n2;
            sb = sb2;
        }
        String string = sourceName;
        if (sb != null) {
            if (n != length) {
                sb.append(sourceName.substring(n));
            }
            string = sb.toString();
        }
        return string;
    }
    
    private Object[] getObjectIdsImpl(final Context context, final Object o) {
        if (o instanceof Scriptable && o != Undefined.instance) {
            final Scriptable scriptable = (Scriptable)o;
            Object[] array;
            if (scriptable instanceof DebuggableObject) {
                array = ((DebuggableObject)scriptable).getAllIds();
            }
            else {
                array = scriptable.getIds();
            }
            final Scriptable prototype = scriptable.getPrototype();
            final Scriptable parentScope = scriptable.getParentScope();
            int n = 0;
            if (prototype != null) {
                n = 0 + 1;
            }
            int n2 = n;
            if (parentScope != null) {
                n2 = n + 1;
            }
            Object[] array2 = array;
            if (n2 != 0) {
                final Object[] array3 = new Object[array.length + n2];
                System.arraycopy(array, 0, array3, n2, array.length);
                final Object[] array4 = array3;
                int n3 = 0;
                if (prototype != null) {
                    array4[0] = "__proto__";
                    n3 = 0 + 1;
                }
                array2 = array4;
                if (parentScope != null) {
                    array4[n3] = "__parent__";
                    array2 = array4;
                }
            }
            return array2;
        }
        return Context.emptyArgs;
    }
    
    private Object getObjectPropertyImpl(final Context context, Object o, final Object o2) {
        Object o3 = o;
        if (o2 instanceof String) {
            final String s = (String)o2;
            if (!s.equals("this")) {
                if (s.equals("__proto__")) {
                    o3 = ((Scriptable)o3).getPrototype();
                }
                else if (s.equals("__parent__")) {
                    o3 = ((Scriptable)o3).getParentScope();
                }
                else {
                    o = ScriptableObject.getProperty((Scriptable)o3, s);
                    if ((o3 = o) == ScriptableObject.NOT_FOUND) {
                        o3 = Undefined.instance;
                    }
                }
            }
            return o3;
        }
        o = ScriptableObject.getProperty((Scriptable)o3, (int)o2);
        Object instance;
        if ((instance = o) == ScriptableObject.NOT_FOUND) {
            instance = Undefined.instance;
        }
        return instance;
    }
    
    private void handleBreakpointHit(final StackFrame stackFrame, final Context context) {
        this.breakFlag = false;
        this.interrupted(context, stackFrame, null);
    }
    
    private void handleExceptionThrown(final Context context, final Throwable t, final StackFrame stackFrame) {
        if (this.breakOnExceptions) {
            final ContextData contextData = stackFrame.contextData();
            if (contextData.lastProcessedException != t) {
                this.interrupted(context, stackFrame, t);
                contextData.lastProcessedException = t;
            }
        }
    }
    
    private void interrupted(final Context p0, final StackFrame p1, final Throwable p2) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: invokevirtual   org/mozilla/javascript/tools/debugger/Dim$StackFrame.contextData:()Lorg/mozilla/javascript/tools/debugger/Dim$ContextData;
        //     4: astore          10
        //     6: aload_0        
        //     7: getfield        org/mozilla/javascript/tools/debugger/Dim.callback:Lorg/mozilla/javascript/tools/debugger/GuiCallback;
        //    10: invokeinterface org/mozilla/javascript/tools/debugger/GuiCallback.isGuiEventThread:()Z
        //    15: istore          8
        //    17: aload           10
        //    19: iload           8
        //    21: invokestatic    org/mozilla/javascript/tools/debugger/Dim$ContextData.access$402:(Lorg/mozilla/javascript/tools/debugger/Dim$ContextData;Z)Z
        //    24: pop            
        //    25: iconst_0       
        //    26: istore          7
        //    28: iconst_0       
        //    29: istore          4
        //    31: aload_0        
        //    32: getfield        org/mozilla/javascript/tools/debugger/Dim.eventThreadMonitor:Ljava/lang/Object;
        //    35: astore          9
        //    37: aload           9
        //    39: monitorenter   
        //    40: iload           8
        //    42: ifeq            68
        //    45: iload           7
        //    47: istore          6
        //    49: aload_0        
        //    50: getfield        org/mozilla/javascript/tools/debugger/Dim.interruptedContextData:Lorg/mozilla/javascript/tools/debugger/Dim$ContextData;
        //    53: ifnull          106
        //    56: iconst_1       
        //    57: istore          6
        //    59: iconst_1       
        //    60: istore          4
        //    62: aload           9
        //    64: monitorexit    
        //    65: goto            577
        //    68: iload           7
        //    70: istore          6
        //    72: aload_0        
        //    73: getfield        org/mozilla/javascript/tools/debugger/Dim.interruptedContextData:Lorg/mozilla/javascript/tools/debugger/Dim$ContextData;
        //    76: astore          11
        //    78: aload           11
        //    80: ifnull          106
        //    83: iload           7
        //    85: istore          6
        //    87: aload_0        
        //    88: getfield        org/mozilla/javascript/tools/debugger/Dim.eventThreadMonitor:Ljava/lang/Object;
        //    91: invokevirtual   java/lang/Object.wait:()V
        //    94: goto            68
        //    97: astore_1       
        //    98: iload           7
        //   100: istore          6
        //   102: aload           9
        //   104: monitorexit    
        //   105: return         
        //   106: iload           7
        //   108: istore          6
        //   110: aload_0        
        //   111: aload           10
        //   113: putfield        org/mozilla/javascript/tools/debugger/Dim.interruptedContextData:Lorg/mozilla/javascript/tools/debugger/Dim$ContextData;
        //   116: iload           7
        //   118: istore          6
        //   120: aload           9
        //   122: monitorexit    
        //   123: goto            577
        //   126: iload           4
        //   128: ifeq            132
        //   131: return         
        //   132: aload_0        
        //   133: getfield        org/mozilla/javascript/tools/debugger/Dim.interruptedContextData:Lorg/mozilla/javascript/tools/debugger/Dim$ContextData;
        //   136: ifnonnull       143
        //   139: invokestatic    org/mozilla/javascript/Kit.codeBug:()Ljava/lang/RuntimeException;
        //   142: pop            
        //   143: aload_0        
        //   144: aload           10
        //   146: invokevirtual   org/mozilla/javascript/tools/debugger/Dim$ContextData.frameCount:()I
        //   149: iconst_1       
        //   150: isub           
        //   151: putfield        org/mozilla/javascript/tools/debugger/Dim.frameIndex:I
        //   154: invokestatic    java/lang/Thread.currentThread:()Ljava/lang/Thread;
        //   157: invokevirtual   java/lang/Thread.toString:()Ljava/lang/String;
        //   160: astore          11
        //   162: aload_3        
        //   163: ifnonnull       171
        //   166: aconst_null    
        //   167: astore_3       
        //   168: goto            176
        //   171: aload_3        
        //   172: invokevirtual   java/lang/Throwable.toString:()Ljava/lang/String;
        //   175: astore_3       
        //   176: iconst_m1      
        //   177: istore          4
        //   179: iload           8
        //   181: ifne            386
        //   184: aload_0        
        //   185: getfield        org/mozilla/javascript/tools/debugger/Dim.monitor:Ljava/lang/Object;
        //   188: astore          9
        //   190: aload           9
        //   192: monitorenter   
        //   193: aload_0        
        //   194: getfield        org/mozilla/javascript/tools/debugger/Dim.insideInterruptLoop:Z
        //   197: ifeq            204
        //   200: invokestatic    org/mozilla/javascript/Kit.codeBug:()Ljava/lang/RuntimeException;
        //   203: pop            
        //   204: aload_0        
        //   205: iconst_1       
        //   206: putfield        org/mozilla/javascript/tools/debugger/Dim.insideInterruptLoop:Z
        //   209: aload_0        
        //   210: aconst_null    
        //   211: putfield        org/mozilla/javascript/tools/debugger/Dim.evalRequest:Ljava/lang/String;
        //   214: aload_0        
        //   215: iconst_m1      
        //   216: putfield        org/mozilla/javascript/tools/debugger/Dim.returnValue:I
        //   219: aload_0        
        //   220: getfield        org/mozilla/javascript/tools/debugger/Dim.callback:Lorg/mozilla/javascript/tools/debugger/GuiCallback;
        //   223: aload_2        
        //   224: aload           11
        //   226: aload_3        
        //   227: invokeinterface org/mozilla/javascript/tools/debugger/GuiCallback.enterInterrupt:(Lorg/mozilla/javascript/tools/debugger/Dim$StackFrame;Ljava/lang/String;Ljava/lang/String;)V
        //   232: aload_0        
        //   233: getfield        org/mozilla/javascript/tools/debugger/Dim.monitor:Ljava/lang/Object;
        //   236: invokevirtual   java/lang/Object.wait:()V
        //   239: aload_0        
        //   240: getfield        org/mozilla/javascript/tools/debugger/Dim.evalRequest:Ljava/lang/String;
        //   243: ifnull          310
        //   246: aload_0        
        //   247: aconst_null    
        //   248: putfield        org/mozilla/javascript/tools/debugger/Dim.evalResult:Ljava/lang/String;
        //   251: aload_0        
        //   252: getfield        org/mozilla/javascript/tools/debugger/Dim.evalFrame:Lorg/mozilla/javascript/tools/debugger/Dim$StackFrame;
        //   255: astore_2       
        //   256: aload_0        
        //   257: getfield        org/mozilla/javascript/tools/debugger/Dim.evalRequest:Ljava/lang/String;
        //   260: astore_3       
        //   261: aload_0        
        //   262: aload_1        
        //   263: aload_2        
        //   264: aload_3        
        //   265: invokestatic    org/mozilla/javascript/tools/debugger/Dim.do_eval:(Lorg/mozilla/javascript/Context;Lorg/mozilla/javascript/tools/debugger/Dim$StackFrame;Ljava/lang/String;)Ljava/lang/String;
        //   268: putfield        org/mozilla/javascript/tools/debugger/Dim.evalResult:Ljava/lang/String;
        //   271: aload_0        
        //   272: aconst_null    
        //   273: putfield        org/mozilla/javascript/tools/debugger/Dim.evalRequest:Ljava/lang/String;
        //   276: aload_0        
        //   277: aconst_null    
        //   278: putfield        org/mozilla/javascript/tools/debugger/Dim.evalFrame:Lorg/mozilla/javascript/tools/debugger/Dim$StackFrame;
        //   281: aload_0        
        //   282: getfield        org/mozilla/javascript/tools/debugger/Dim.monitor:Ljava/lang/Object;
        //   285: invokevirtual   java/lang/Object.notify:()V
        //   288: goto            592
        //   291: aload_0        
        //   292: aconst_null    
        //   293: putfield        org/mozilla/javascript/tools/debugger/Dim.evalRequest:Ljava/lang/String;
        //   296: aload_0        
        //   297: aconst_null    
        //   298: putfield        org/mozilla/javascript/tools/debugger/Dim.evalFrame:Lorg/mozilla/javascript/tools/debugger/Dim$StackFrame;
        //   301: aload_0        
        //   302: getfield        org/mozilla/javascript/tools/debugger/Dim.monitor:Ljava/lang/Object;
        //   305: invokevirtual   java/lang/Object.notify:()V
        //   308: aload_1        
        //   309: athrow         
        //   310: aload_0        
        //   311: getfield        org/mozilla/javascript/tools/debugger/Dim.returnValue:I
        //   314: iconst_m1      
        //   315: if_icmpeq       592
        //   318: aload_0        
        //   319: getfield        org/mozilla/javascript/tools/debugger/Dim.returnValue:I
        //   322: istore          5
        //   324: iload           5
        //   326: istore          4
        //   328: goto            338
        //   331: astore_1       
        //   332: invokestatic    java/lang/Thread.currentThread:()Ljava/lang/Thread;
        //   335: invokevirtual   java/lang/Thread.interrupt:()V
        //   338: iload           4
        //   340: istore          5
        //   342: aload_0        
        //   343: iconst_0       
        //   344: putfield        org/mozilla/javascript/tools/debugger/Dim.insideInterruptLoop:Z
        //   347: iload           4
        //   349: istore          5
        //   351: aload           9
        //   353: monitorexit    
        //   354: goto            607
        //   357: iload           4
        //   359: istore          5
        //   361: aload_0        
        //   362: iconst_0       
        //   363: putfield        org/mozilla/javascript/tools/debugger/Dim.insideInterruptLoop:Z
        //   366: iload           4
        //   368: istore          5
        //   370: aload_1        
        //   371: athrow         
        //   372: astore_1       
        //   373: iload           5
        //   375: istore          4
        //   377: goto            381
        //   380: astore_1       
        //   381: aload           9
        //   383: monitorexit    
        //   384: aload_1        
        //   385: athrow         
        //   386: aload_0        
        //   387: iconst_m1      
        //   388: putfield        org/mozilla/javascript/tools/debugger/Dim.returnValue:I
        //   391: aload_0        
        //   392: getfield        org/mozilla/javascript/tools/debugger/Dim.callback:Lorg/mozilla/javascript/tools/debugger/GuiCallback;
        //   395: aload_2        
        //   396: aload           11
        //   398: aload_3        
        //   399: invokeinterface org/mozilla/javascript/tools/debugger/GuiCallback.enterInterrupt:(Lorg/mozilla/javascript/tools/debugger/Dim$StackFrame;Ljava/lang/String;Ljava/lang/String;)V
        //   404: aload_0        
        //   405: getfield        org/mozilla/javascript/tools/debugger/Dim.returnValue:I
        //   408: istore          4
        //   410: iload           4
        //   412: iconst_m1      
        //   413: if_icmpne       432
        //   416: aload_0        
        //   417: getfield        org/mozilla/javascript/tools/debugger/Dim.callback:Lorg/mozilla/javascript/tools/debugger/GuiCallback;
        //   420: invokeinterface org/mozilla/javascript/tools/debugger/GuiCallback.dispatchNextGuiEvent:()V
        //   425: goto            429
        //   428: astore_1       
        //   429: goto            404
        //   432: aload_0        
        //   433: getfield        org/mozilla/javascript/tools/debugger/Dim.returnValue:I
        //   436: istore          4
        //   438: goto            607
        //   441: aload           10
        //   443: invokevirtual   org/mozilla/javascript/tools/debugger/Dim$ContextData.frameCount:()I
        //   446: iconst_1       
        //   447: if_icmple       508
        //   450: aload           10
        //   452: iconst_1       
        //   453: invokestatic    org/mozilla/javascript/tools/debugger/Dim$ContextData.access$1402:(Lorg/mozilla/javascript/tools/debugger/Dim$ContextData;Z)Z
        //   456: pop            
        //   457: aload           10
        //   459: aload           10
        //   461: invokevirtual   org/mozilla/javascript/tools/debugger/Dim$ContextData.frameCount:()I
        //   464: iconst_1       
        //   465: isub           
        //   466: invokestatic    org/mozilla/javascript/tools/debugger/Dim$ContextData.access$1502:(Lorg/mozilla/javascript/tools/debugger/Dim$ContextData;I)I
        //   469: pop            
        //   470: goto            508
        //   473: aload           10
        //   475: iconst_1       
        //   476: invokestatic    org/mozilla/javascript/tools/debugger/Dim$ContextData.access$1402:(Lorg/mozilla/javascript/tools/debugger/Dim$ContextData;Z)Z
        //   479: pop            
        //   480: aload           10
        //   482: iconst_m1      
        //   483: invokestatic    org/mozilla/javascript/tools/debugger/Dim$ContextData.access$1502:(Lorg/mozilla/javascript/tools/debugger/Dim$ContextData;I)I
        //   486: pop            
        //   487: goto            508
        //   490: aload           10
        //   492: iconst_1       
        //   493: invokestatic    org/mozilla/javascript/tools/debugger/Dim$ContextData.access$1402:(Lorg/mozilla/javascript/tools/debugger/Dim$ContextData;Z)Z
        //   496: pop            
        //   497: aload           10
        //   499: aload           10
        //   501: invokevirtual   org/mozilla/javascript/tools/debugger/Dim$ContextData.frameCount:()I
        //   504: invokestatic    org/mozilla/javascript/tools/debugger/Dim$ContextData.access$1502:(Lorg/mozilla/javascript/tools/debugger/Dim$ContextData;I)I
        //   507: pop            
        //   508: aload_0        
        //   509: getfield        org/mozilla/javascript/tools/debugger/Dim.eventThreadMonitor:Ljava/lang/Object;
        //   512: astore_1       
        //   513: aload_1        
        //   514: monitorenter   
        //   515: aload_0        
        //   516: aconst_null    
        //   517: putfield        org/mozilla/javascript/tools/debugger/Dim.interruptedContextData:Lorg/mozilla/javascript/tools/debugger/Dim$ContextData;
        //   520: aload_0        
        //   521: getfield        org/mozilla/javascript/tools/debugger/Dim.eventThreadMonitor:Ljava/lang/Object;
        //   524: invokevirtual   java/lang/Object.notifyAll:()V
        //   527: aload_1        
        //   528: monitorexit    
        //   529: return         
        //   530: astore_2       
        //   531: aload_1        
        //   532: monitorexit    
        //   533: aload_2        
        //   534: athrow         
        //   535: astore_1       
        //   536: goto            540
        //   539: astore_1       
        //   540: aload_0        
        //   541: getfield        org/mozilla/javascript/tools/debugger/Dim.eventThreadMonitor:Ljava/lang/Object;
        //   544: astore_2       
        //   545: aload_2        
        //   546: monitorenter   
        //   547: aload_0        
        //   548: aconst_null    
        //   549: putfield        org/mozilla/javascript/tools/debugger/Dim.interruptedContextData:Lorg/mozilla/javascript/tools/debugger/Dim$ContextData;
        //   552: aload_0        
        //   553: getfield        org/mozilla/javascript/tools/debugger/Dim.eventThreadMonitor:Ljava/lang/Object;
        //   556: invokevirtual   java/lang/Object.notifyAll:()V
        //   559: aload_2        
        //   560: monitorexit    
        //   561: aload_1        
        //   562: athrow         
        //   563: astore_1       
        //   564: aload_2        
        //   565: monitorexit    
        //   566: aload_1        
        //   567: athrow         
        //   568: aload           9
        //   570: monitorexit    
        //   571: aload_1        
        //   572: athrow         
        //   573: astore_1       
        //   574: goto            581
        //   577: goto            126
        //   580: astore_1       
        //   581: goto            568
        //   584: astore_1       
        //   585: goto            291
        //   588: astore_1       
        //   589: goto            291
        //   592: goto            232
        //   595: astore_1       
        //   596: goto            357
        //   599: astore_1       
        //   600: goto            596
        //   603: astore_1       
        //   604: goto            381
        //   607: iload           4
        //   609: tableswitch {
        //                0: 490
        //                1: 473
        //                2: 441
        //          default: 636
        //        }
        //   636: goto            508
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                            
        //  -----  -----  -----  -----  --------------------------------
        //  49     56     580    584    Any
        //  62     65     580    584    Any
        //  72     78     580    584    Any
        //  87     94     97     106    Ljava/lang/InterruptedException;
        //  87     94     580    584    Any
        //  102    105    580    584    Any
        //  110    116    580    584    Any
        //  120    123    580    584    Any
        //  143    162    539    540    Any
        //  171    176    539    540    Any
        //  184    193    539    540    Any
        //  193    204    380    381    Any
        //  204    232    380    381    Any
        //  232    239    331    338    Ljava/lang/InterruptedException;
        //  232    239    595    596    Any
        //  239    251    595    596    Any
        //  251    261    588    592    Any
        //  261    271    584    588    Any
        //  271    288    599    603    Any
        //  291    310    599    603    Any
        //  310    324    599    603    Any
        //  332    338    599    603    Any
        //  342    347    372    380    Any
        //  351    354    372    380    Any
        //  361    366    372    380    Any
        //  370    372    372    380    Any
        //  381    384    603    607    Any
        //  384    386    535    539    Any
        //  386    404    535    539    Any
        //  404    410    535    539    Any
        //  416    425    428    429    Ljava/lang/InterruptedException;
        //  416    425    535    539    Any
        //  432    438    535    539    Any
        //  441    470    535    539    Any
        //  473    487    535    539    Any
        //  490    508    535    539    Any
        //  515    529    530    535    Any
        //  531    533    530    535    Any
        //  547    561    563    568    Any
        //  564    566    563    568    Any
        //  568    571    573    577    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index: 324, Size: 324
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
    
    private String loadSource(String o) {
        String s = null;
        final int index = ((String)o).indexOf(35);
        Object substring = o;
        if (index >= 0) {
            substring = ((String)o).substring(0, index);
        }
        String s2 = s;
        Object reader = substring;
        try {
            final int index2 = ((String)substring).indexOf(58);
            o = substring;
            Label_0433: {
                if (index2 < 0) {
                    s2 = s;
                    reader = substring;
                    try {
                        if (((String)substring).startsWith("~/")) {
                            s2 = s;
                            reader = substring;
                            o = SecurityUtilities.getSystemProperty("user.home");
                            if (o != null) {
                                s2 = s;
                                reader = substring;
                                final String substring2 = ((String)substring).substring(2);
                                s2 = s;
                                reader = substring;
                                final File file = new File(new File((String)o), substring2);
                                s2 = s;
                                reader = substring;
                                if (file.exists()) {
                                    s2 = s;
                                    reader = substring;
                                    o = new FileInputStream(file);
                                    break Label_0433;
                                }
                            }
                        }
                        s2 = s;
                        reader = substring;
                        final File file2 = new File((String)substring);
                        s2 = s;
                        reader = substring;
                        if (file2.exists()) {
                            s2 = s;
                            reader = substring;
                            o = new FileInputStream(file2);
                            break Label_0433;
                        }
                    }
                    catch (SecurityException ex2) {}
                    s2 = s;
                    reader = substring;
                    if (((String)substring).startsWith("//")) {
                        s2 = s;
                        reader = substring;
                        final StringBuilder sb = new StringBuilder();
                        s2 = s;
                        reader = substring;
                        sb.append("http:");
                        s2 = s;
                        reader = substring;
                        sb.append((String)substring);
                        s2 = s;
                        reader = substring;
                        o = sb.toString();
                    }
                    else {
                        s2 = s;
                        reader = substring;
                        if (((String)substring).startsWith("/")) {
                            s2 = s;
                            reader = substring;
                            final StringBuilder sb2 = new StringBuilder();
                            s2 = s;
                            reader = substring;
                            sb2.append("http://127.0.0.1");
                            s2 = s;
                            reader = substring;
                            sb2.append((String)substring);
                            s2 = s;
                            reader = substring;
                            o = sb2.toString();
                        }
                        else {
                            s2 = s;
                            reader = substring;
                            final StringBuilder sb3 = new StringBuilder();
                            s2 = s;
                            reader = substring;
                            sb3.append("http://");
                            s2 = s;
                            reader = substring;
                            sb3.append((String)substring);
                            s2 = s;
                            reader = substring;
                            o = sb3.toString();
                        }
                    }
                }
                s2 = s;
                reader = o;
                final InputStream openStream = new URL((String)o).openStream();
                substring = o;
                o = openStream;
                try {
                    reader = Kit.readReader(new InputStreamReader((InputStream)o));
                    s = (s2 = (String)reader);
                    reader = substring;
                    ((InputStream)o).close();
                    return s;
                }
                finally {
                    s2 = s;
                    reader = substring;
                    ((InputStream)o).close();
                    s2 = s;
                    reader = substring;
                }
            }
        }
        catch (IOException ex) {
            final PrintStream err = System.err;
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("Failed to load source from ");
            sb4.append((String)reader);
            sb4.append(": ");
            sb4.append(ex);
            err.println(sb4.toString());
            return s2;
        }
    }
    
    private void registerTopScript(final DebuggableScript debuggableScript, String functionToSource) {
        if (!debuggableScript.isTopLevel()) {
            throw new IllegalArgumentException();
        }
        final String normalizedUrl = this.getNormalizedUrl(debuggableScript);
        final DebuggableScript[] allFunctions = getAllFunctions(debuggableScript);
        String s = functionToSource;
        if (this.sourceProvider != null) {
            final String source = this.sourceProvider.getSource(debuggableScript);
            s = functionToSource;
            if (source != null) {
                s = source;
            }
        }
        while (true) {
            final SourceInfo sourceInfo = new SourceInfo(s, allFunctions, normalizedUrl);
            while (true) {
                int i;
                synchronized (this.urlToSourceInfo) {
                    final SourceInfo sourceInfo2 = this.urlToSourceInfo.get(normalizedUrl);
                    if (sourceInfo2 != null) {
                        sourceInfo.copyBreakpointsFrom(sourceInfo2);
                    }
                    this.urlToSourceInfo.put(normalizedUrl, sourceInfo);
                    final int n = 0;
                    i = 0;
                    if (i != sourceInfo.functionSourcesTop()) {
                        final FunctionSource functionSource = sourceInfo.functionSource(i);
                        final String name = functionSource.name();
                        if (name.length() != 0) {
                            this.functionNames.put(name, functionSource);
                        }
                    }
                    else {
                        // monitorexit(this.urlToSourceInfo)
                        functionToSource = (String)this.functionToSource;
                        // monitorenter(this.urlToSourceInfo)
                        i = n;
                        try {
                            while (i != allFunctions.length) {
                                this.functionToSource.put(allFunctions[i], sourceInfo.functionSource(i));
                                ++i;
                            }
                            // monitorexit(this.urlToSourceInfo)
                            this.callback.updateSourceText(sourceInfo);
                            return;
                        }
                        finally {
                        }
                        // monitorexit(this.urlToSourceInfo)
                    }
                }
                ++i;
                continue;
            }
        }
    }
    
    public void attachTo(final ContextFactory contextFactory) {
        this.detach();
        (this.contextFactory = contextFactory).addListener((ContextFactory.Listener)(this.listener = new DimIProxy(this, 1)));
    }
    
    public void clearAllBreakpoints() {
        final Iterator<SourceInfo> iterator = this.urlToSourceInfo.values().iterator();
        while (iterator.hasNext()) {
            iterator.next().removeAllBreakpoints();
        }
    }
    
    public void compileScript(final String s, final String s2) {
        final DimIProxy dimIProxy = new DimIProxy(this, 2);
        dimIProxy.url = s;
        dimIProxy.text = s2;
        dimIProxy.withContext();
    }
    
    public void contextSwitch(final int frameIndex) {
        this.frameIndex = frameIndex;
    }
    
    public ContextData currentContextData() {
        return this.interruptedContextData;
    }
    
    public void detach() {
        if (this.listener != null) {
            this.contextFactory.removeListener((ContextFactory.Listener)this.listener);
            this.contextFactory = null;
            this.listener = null;
        }
    }
    
    public void dispose() {
        this.detach();
    }
    
    public String eval(final String evalRequest) {
        String evalResult = "undefined";
        if (evalRequest == null) {
            return "undefined";
        }
        final ContextData currentContextData = this.currentContextData();
        if (currentContextData != null) {
            if (this.frameIndex >= currentContextData.frameCount()) {
                return "undefined";
            }
            final StackFrame frame = currentContextData.getFrame(this.frameIndex);
            if (currentContextData.eventThreadFlag) {
                return do_eval(Context.getCurrentContext(), frame, evalRequest);
            }
            synchronized (this.monitor) {
                if (this.insideInterruptLoop) {
                    this.evalRequest = evalRequest;
                    this.evalFrame = frame;
                    this.monitor.notify();
                    try {
                        do {
                            this.monitor.wait();
                        } while (this.evalRequest != null);
                    }
                    catch (InterruptedException ex) {
                        Thread.currentThread().interrupt();
                    }
                    evalResult = this.evalResult;
                }
                return evalResult;
            }
        }
        return "undefined";
    }
    
    public void evalScript(final String s, final String s2) {
        final DimIProxy dimIProxy = new DimIProxy(this, 3);
        dimIProxy.url = s;
        dimIProxy.text = s2;
        dimIProxy.withContext();
    }
    
    public String[] functionNames() {
        synchronized (this.urlToSourceInfo) {
            return this.functionNames.keySet().toArray(new String[this.functionNames.size()]);
        }
    }
    
    public FunctionSource functionSourceByName(final String s) {
        return this.functionNames.get(s);
    }
    
    public Object[] getObjectIds(final Object o) {
        final DimIProxy dimIProxy = new DimIProxy(this, 7);
        dimIProxy.object = o;
        dimIProxy.withContext();
        return dimIProxy.objectArrayResult;
    }
    
    public Object getObjectProperty(final Object o, final Object o2) {
        final DimIProxy dimIProxy = new DimIProxy(this, 6);
        dimIProxy.object = o;
        dimIProxy.id = o2;
        dimIProxy.withContext();
        return dimIProxy.objectResult;
    }
    
    public void go() {
        synchronized (this.monitor) {
            this.returnValue = 3;
            this.monitor.notifyAll();
        }
    }
    
    public String objectToString(final Object o) {
        final DimIProxy dimIProxy = new DimIProxy(this, 5);
        dimIProxy.object = o;
        dimIProxy.withContext();
        return dimIProxy.stringResult;
    }
    
    public void setBreak() {
        this.breakFlag = true;
    }
    
    public void setBreakOnEnter(final boolean breakOnEnter) {
        this.breakOnEnter = breakOnEnter;
    }
    
    public void setBreakOnExceptions(final boolean breakOnExceptions) {
        this.breakOnExceptions = breakOnExceptions;
    }
    
    public void setBreakOnReturn(final boolean breakOnReturn) {
        this.breakOnReturn = breakOnReturn;
    }
    
    public void setGuiCallback(final GuiCallback callback) {
        this.callback = callback;
    }
    
    public void setReturnValue(final int returnValue) {
        synchronized (this.monitor) {
            this.returnValue = returnValue;
            this.monitor.notify();
        }
    }
    
    public void setScopeProvider(final ScopeProvider scopeProvider) {
        this.scopeProvider = scopeProvider;
    }
    
    public void setSourceProvider(final SourceProvider sourceProvider) {
        this.sourceProvider = sourceProvider;
    }
    
    public SourceInfo sourceInfo(final String s) {
        return this.urlToSourceInfo.get(s);
    }
    
    public boolean stringIsCompilableUnit(final String s) {
        final DimIProxy dimIProxy = new DimIProxy(this, 4);
        dimIProxy.text = s;
        dimIProxy.withContext();
        return dimIProxy.booleanResult;
    }
    
    public static class ContextData
    {
        private boolean breakNextLine;
        private boolean eventThreadFlag;
        private ObjArray frameStack;
        private Throwable lastProcessedException;
        private int stopAtFrameDepth;
        
        public ContextData() {
            this.frameStack = new ObjArray();
            this.stopAtFrameDepth = -1;
        }
        
        public static ContextData get(final Context context) {
            return (ContextData)context.getDebuggerContextData();
        }
        
        private void popFrame() {
            this.frameStack.pop();
        }
        
        private void pushFrame(final StackFrame stackFrame) {
            this.frameStack.push(stackFrame);
        }
        
        public int frameCount() {
            return this.frameStack.size();
        }
        
        public StackFrame getFrame(final int n) {
            return (StackFrame)this.frameStack.get(this.frameStack.size() - n - 1);
        }
    }
    
    private static class DimIProxy implements ContextAction, Listener, Debugger
    {
        private boolean booleanResult;
        private Dim dim;
        private Object id;
        private Object object;
        private Object[] objectArrayResult;
        private Object objectResult;
        private String stringResult;
        private String text;
        private int type;
        private String url;
        
        private DimIProxy(final Dim dim, final int type) {
            this.dim = dim;
            this.type = type;
        }
        
        private void withContext() {
            this.dim.contextFactory.call(this);
        }
        
        @Override
        public void contextCreated(final Context context) {
            if (this.type != 1) {
                Kit.codeBug();
            }
            context.setDebugger(new DimIProxy(this.dim, 0), new ContextData());
            context.setGeneratingDebug(true);
            context.setOptimizationLevel(-1);
        }
        
        @Override
        public void contextReleased(final Context context) {
            if (this.type != 1) {
                Kit.codeBug();
            }
        }
        
        @Override
        public DebugFrame getFrame(final Context context, final DebuggableScript debuggableScript) {
            if (this.type != 0) {
                Kit.codeBug();
            }
            final FunctionSource access$2200 = this.dim.getFunctionSource(debuggableScript);
            if (access$2200 == null) {
                return null;
            }
            return new StackFrame(context, this.dim, access$2200);
        }
        
        @Override
        public void handleCompilationDone(final Context context, final DebuggableScript debuggableScript, final String s) {
            if (this.type != 0) {
                Kit.codeBug();
            }
            if (!debuggableScript.isTopLevel()) {
                return;
            }
            this.dim.registerTopScript(debuggableScript, s);
        }
        
        @Override
        public Object run(final Context context) {
            switch (this.type) {
                default: {
                    throw Kit.codeBug();
                }
                case 7: {
                    this.objectArrayResult = this.dim.getObjectIdsImpl(context, this.object);
                    return null;
                }
                case 6: {
                    this.objectResult = this.dim.getObjectPropertyImpl(context, this.object, this.id);
                    return null;
                }
                case 5: {
                    if (this.object == Undefined.instance) {
                        this.stringResult = "undefined";
                        return null;
                    }
                    if (this.object == null) {
                        this.stringResult = "null";
                        return null;
                    }
                    if (this.object instanceof NativeCall) {
                        this.stringResult = "[object Call]";
                        return null;
                    }
                    this.stringResult = Context.toString(this.object);
                    return null;
                }
                case 4: {
                    this.booleanResult = context.stringIsCompilableUnit(this.text);
                    return null;
                }
                case 3: {
                    Scriptable scope = null;
                    if (this.dim.scopeProvider != null) {
                        scope = this.dim.scopeProvider.getScope();
                    }
                    Scriptable scriptable;
                    if ((scriptable = scope) == null) {
                        scriptable = new ImporterTopLevel(context);
                    }
                    context.evaluateString(scriptable, this.text, this.url, 1, null);
                    return null;
                }
                case 2: {
                    context.compileString(this.text, this.url, 1, null);
                    return null;
                }
            }
        }
    }
    
    public static class FunctionSource
    {
        private int firstLine;
        private String name;
        private SourceInfo sourceInfo;
        
        private FunctionSource(final SourceInfo sourceInfo, final int firstLine, final String name) {
            if (name == null) {
                throw new IllegalArgumentException();
            }
            this.sourceInfo = sourceInfo;
            this.firstLine = firstLine;
            this.name = name;
        }
        
        public int firstLine() {
            return this.firstLine;
        }
        
        public String name() {
            return this.name;
        }
        
        public SourceInfo sourceInfo() {
            return this.sourceInfo;
        }
    }
    
    public static class SourceInfo
    {
        private static final boolean[] EMPTY_BOOLEAN_ARRAY;
        private boolean[] breakableLines;
        private boolean[] breakpoints;
        private FunctionSource[] functionSources;
        private String source;
        private String url;
        
        static {
            EMPTY_BOOLEAN_ARRAY = new boolean[0];
        }
        
        private SourceInfo(String source, final DebuggableScript[] array, String functionName) {
            this.source = source;
            this.url = functionName;
            final int length = array.length;
            final int[][] array2 = new int[length][];
            for (int i = 0; i != length; ++i) {
                array2[i] = array[i].getLineNumbers();
            }
            final int[] array3 = new int[length];
            int n = -1;
            int n2 = 0;
            int n3 = 0;
            while (true) {
                int j = 1;
                if (n3 == length) {
                    break;
                }
                final int[] array4 = array2[n3];
                int n9;
                if (array4 != null && array4.length != 0) {
                    int n5;
                    int n4 = n5 = array4[0];
                    while (j != array4.length) {
                        final int n6 = array4[j];
                        int n7;
                        int n8;
                        if (n6 < n4) {
                            n7 = n6;
                            n8 = n5;
                        }
                        else {
                            n7 = n4;
                            if (n6 > (n8 = n5)) {
                                n8 = n6;
                                n7 = n4;
                            }
                        }
                        ++j;
                        n4 = n7;
                        n5 = n8;
                    }
                    array3[n3] = n4;
                    if (n2 > n) {
                        n2 = n4;
                        n9 = n5;
                    }
                    else {
                        int n10;
                        if (n4 < (n10 = n2)) {
                            n10 = n4;
                        }
                        n2 = n10;
                        if (n5 > (n9 = n)) {
                            n2 = n10;
                            n9 = n5;
                        }
                    }
                }
                else {
                    array3[n3] = -1;
                    n9 = n;
                }
                ++n3;
                n = n9;
            }
            if (n2 > n) {
                this.breakableLines = SourceInfo.EMPTY_BOOLEAN_ARRAY;
                this.breakpoints = SourceInfo.EMPTY_BOOLEAN_ARRAY;
            }
            else {
                if (n2 < 0) {
                    throw new IllegalStateException(String.valueOf(n2));
                }
                final int n11 = n + 1;
                this.breakableLines = new boolean[n11];
                this.breakpoints = new boolean[n11];
                for (int k = 0; k != length; ++k) {
                    final int[] array5 = array2[k];
                    if (array5 != null && array5.length != 0) {
                        for (int l = 0; l != array5.length; ++l) {
                            this.breakableLines[array5[l]] = true;
                        }
                    }
                }
            }
            this.functionSources = new FunctionSource[length];
            for (int n12 = 0; n12 != length; ++n12) {
                functionName = array[n12].getFunctionName();
                if ((source = functionName) == null) {
                    source = "";
                }
                this.functionSources[n12] = new FunctionSource(this, array3[n12], source);
            }
        }
        
        private void copyBreakpointsFrom(final SourceInfo sourceInfo) {
            int n;
            if ((n = sourceInfo.breakpoints.length) > this.breakpoints.length) {
                n = this.breakpoints.length;
            }
            for (int i = 0; i != n; ++i) {
                if (sourceInfo.breakpoints[i]) {
                    this.breakpoints[i] = true;
                }
            }
        }
        
        public boolean breakableLine(final int n) {
            return n < this.breakableLines.length && this.breakableLines[n];
        }
        
        public boolean breakpoint(final int n) {
            if (!this.breakableLine(n)) {
                throw new IllegalArgumentException(String.valueOf(n));
            }
            return n < this.breakpoints.length && this.breakpoints[n];
        }
        
        public boolean breakpoint(final int n, final boolean b) {
            if (!this.breakableLine(n)) {
                throw new IllegalArgumentException(String.valueOf(n));
            }
            while (true) {
                final boolean[] breakpoints = this.breakpoints;
                // monitorenter(breakpoints)
                final boolean b2 = false;
                try {
                    if (this.breakpoints[n] != b) {
                        this.breakpoints[n] = b;
                        return true;
                    }
                }
                finally {
                }
                // monitorexit(breakpoints)
                return b2;
            }
        }
        
        public FunctionSource functionSource(final int n) {
            return this.functionSources[n];
        }
        
        public int functionSourcesTop() {
            return this.functionSources.length;
        }
        
        public void removeAllBreakpoints() {
            final boolean[] breakpoints = this.breakpoints;
            // monitorenter(breakpoints)
            int i = 0;
            try {
                while (i != this.breakpoints.length) {
                    this.breakpoints[i] = false;
                    ++i;
                }
            }
            finally {
            }
            // monitorexit(breakpoints)
        }
        
        public String source() {
            return this.source;
        }
        
        public String url() {
            return this.url;
        }
    }
    
    public static class StackFrame implements DebugFrame
    {
        private boolean[] breakpoints;
        private ContextData contextData;
        private Dim dim;
        private FunctionSource fsource;
        private int lineNumber;
        private Scriptable scope;
        private Scriptable thisObj;
        
        private StackFrame(final Context context, final Dim dim, final FunctionSource fsource) {
            this.dim = dim;
            this.contextData = ContextData.get(context);
            this.fsource = fsource;
            this.breakpoints = fsource.sourceInfo().breakpoints;
            this.lineNumber = fsource.firstLine();
        }
        
        public ContextData contextData() {
            return this.contextData;
        }
        
        public String getFunctionName() {
            return this.fsource.name();
        }
        
        public int getLineNumber() {
            return this.lineNumber;
        }
        
        public String getUrl() {
            return this.fsource.sourceInfo().url();
        }
        
        @Override
        public void onDebuggerStatement(final Context context) {
            this.dim.handleBreakpointHit(this, context);
        }
        
        @Override
        public void onEnter(final Context context, final Scriptable scope, final Scriptable thisObj, final Object[] array) {
            this.contextData.pushFrame(this);
            this.scope = scope;
            this.thisObj = thisObj;
            if (this.dim.breakOnEnter) {
                this.dim.handleBreakpointHit(this, context);
            }
        }
        
        @Override
        public void onExceptionThrown(final Context context, final Throwable t) {
            this.dim.handleExceptionThrown(context, t, this);
        }
        
        @Override
        public void onExit(final Context context, final boolean b, final Object o) {
            if (this.dim.breakOnReturn && !b) {
                this.dim.handleBreakpointHit(this, context);
            }
            this.contextData.popFrame();
        }
        
        @Override
        public void onLineChange(final Context context, final int lineNumber) {
            this.lineNumber = lineNumber;
            if (!this.breakpoints[lineNumber] && !this.dim.breakFlag) {
                boolean access$1400;
                final boolean b = access$1400 = this.contextData.breakNextLine;
                if (b) {
                    access$1400 = b;
                    if (this.contextData.stopAtFrameDepth >= 0) {
                        access$1400 = (this.contextData.frameCount() <= this.contextData.stopAtFrameDepth);
                    }
                }
                if (!access$1400) {
                    return;
                }
                this.contextData.stopAtFrameDepth = -1;
                this.contextData.breakNextLine = false;
            }
            this.dim.handleBreakpointHit(this, context);
        }
        
        public Object scope() {
            return this.scope;
        }
        
        public SourceInfo sourceInfo() {
            return this.fsource.sourceInfo();
        }
        
        public Object thisObj() {
            return this.thisObj;
        }
    }
}
