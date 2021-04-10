package org.mozilla.javascript;

import org.mozilla.javascript.xml.*;
import org.mozilla.javascript.debug.*;
import org.mozilla.javascript.ast.*;
import java.beans.*;
import java.io.*;
import java.lang.reflect.*;
import java.util.*;

public class Context
{
    public static final int FEATURE_DYNAMIC_SCOPE = 7;
    public static final int FEATURE_E4X = 6;
    public static final int FEATURE_ENHANCED_JAVA_ACCESS = 13;
    public static final int FEATURE_LOCATION_INFORMATION_IN_ERROR = 10;
    public static final int FEATURE_MEMBER_EXPR_AS_FUNCTION_NAME = 2;
    public static final int FEATURE_NON_ECMA_GET_YEAR = 1;
    public static final int FEATURE_PARENT_PROTO_PROPERTIES = 5;
    @Deprecated
    public static final int FEATURE_PARENT_PROTO_PROPRTIES = 5;
    public static final int FEATURE_RESERVED_KEYWORD_AS_IDENTIFIER = 3;
    public static final int FEATURE_STRICT_EVAL = 9;
    public static final int FEATURE_STRICT_MODE = 11;
    public static final int FEATURE_STRICT_VARS = 8;
    public static final int FEATURE_TO_STRING_AS_SOURCE = 4;
    public static final int FEATURE_V8_EXTENSIONS = 14;
    public static final int FEATURE_WARNING_AS_ERROR = 12;
    public static final int VERSION_1_0 = 100;
    public static final int VERSION_1_1 = 110;
    public static final int VERSION_1_2 = 120;
    public static final int VERSION_1_3 = 130;
    public static final int VERSION_1_4 = 140;
    public static final int VERSION_1_5 = 150;
    public static final int VERSION_1_6 = 160;
    public static final int VERSION_1_7 = 170;
    public static final int VERSION_1_8 = 180;
    public static final int VERSION_DEFAULT = 0;
    public static final int VERSION_ES6 = 200;
    public static final int VERSION_UNKNOWN = -1;
    private static Class<?> codegenClass;
    public static final Object[] emptyArgs;
    public static final String errorReporterProperty = "error reporter";
    private static String implementationVersion;
    private static Class<?> interpreterClass;
    public static final String languageVersionProperty = "language version";
    Set<String> activationNames;
    private ClassLoader applicationClassLoader;
    XMLLib cachedXMLLib;
    private ClassShutter classShutter;
    NativeCall currentActivationCall;
    Debugger debugger;
    private Object debuggerData;
    private int enterCount;
    private ErrorReporter errorReporter;
    private final ContextFactory factory;
    public boolean generateObserverCount;
    private boolean generatingDebug;
    private boolean generatingDebugChanged;
    private boolean generatingSource;
    private boolean hasClassShutter;
    int instructionCount;
    int instructionThreshold;
    Object interpreterSecurityDomain;
    boolean isContinuationsTopCall;
    ObjToIntMap iterating;
    Object lastInterpreterFrame;
    private Locale locale;
    private int maximumInterpreterStackDepth;
    private int optimizationLevel;
    ObjArray previousInterpreterInvocations;
    private Object propertyListeners;
    RegExpProxy regExpProxy;
    int scratchIndex;
    Scriptable scratchScriptable;
    long scratchUint32;
    private Object sealKey;
    private boolean sealed;
    private SecurityController securityController;
    private Map<Object, Object> threadLocalMap;
    Scriptable topCallScope;
    BaseFunction typeErrorThrower;
    boolean useDynamicScope;
    int version;
    private WrapFactory wrapFactory;
    
    static {
        emptyArgs = ScriptRuntime.emptyArgs;
        Context.codegenClass = Kit.classOrNull("org.mozilla.javascript.optimizer.Codegen");
        Context.interpreterClass = Kit.classOrNull("org.mozilla.javascript.Interpreter");
    }
    
    @Deprecated
    public Context() {
        this(ContextFactory.getGlobal());
    }
    
    protected Context(final ContextFactory factory) {
        this.generatingSource = true;
        int optimizationLevel = 0;
        this.generateObserverCount = false;
        if (factory == null) {
            throw new IllegalArgumentException("factory == null");
        }
        this.factory = factory;
        this.version = 0;
        if (Context.codegenClass == null) {
            optimizationLevel = -1;
        }
        this.optimizationLevel = optimizationLevel;
        this.maximumInterpreterStackDepth = Integer.MAX_VALUE;
    }
    
    @Deprecated
    public static void addContextListener(final ContextListener contextListener) {
        if ("org.mozilla.javascript.tools.debugger.Main".equals(contextListener.getClass().getName())) {
            final Class<? extends ContextListener> class1 = contextListener.getClass();
            final Class<?> classOrNull = Kit.classOrNull("org.mozilla.javascript.ContextFactory");
            final ContextFactory global = ContextFactory.getGlobal();
            try {
                class1.getMethod("attachTo", classOrNull).invoke(contextListener, global);
                return;
            }
            catch (Exception ex2) {
                final RuntimeException ex = new RuntimeException();
                Kit.initCause(ex, ex2);
                throw ex;
            }
        }
        ContextFactory.getGlobal().addListener((ContextFactory.Listener)contextListener);
    }
    
    @Deprecated
    public static Object call(final ContextAction contextAction) {
        return call(ContextFactory.getGlobal(), contextAction);
    }
    
    public static Object call(final ContextFactory contextFactory, final Callable callable, final Scriptable scriptable, final Scriptable scriptable2, final Object[] array) {
        ContextFactory global = contextFactory;
        if (contextFactory == null) {
            global = ContextFactory.getGlobal();
        }
        return call(global, new ContextAction() {
            @Override
            public Object run(final Context context) {
                return callable.call(context, scriptable, scriptable2, array);
            }
        });
    }
    
    static Object call(final ContextFactory contextFactory, final ContextAction contextAction) {
        final Context enter = enter(null, contextFactory);
        try {
            return contextAction.run(enter);
        }
        finally {
            exit();
        }
    }
    
    public static void checkLanguageVersion(final int n) {
        if (isValidLanguageVersion(n)) {
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Bad language version: ");
        sb.append(n);
        throw new IllegalArgumentException(sb.toString());
    }
    
    public static void checkOptimizationLevel(final int n) {
        if (isValidOptimizationLevel(n)) {
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Optimization level outside [-1..9]: ");
        sb.append(n);
        throw new IllegalArgumentException(sb.toString());
    }
    
    private Object compileImpl(final Scriptable scriptable, final Reader reader, final String s, String s2, final int n, final Object o, final boolean b, final Evaluator evaluator, ErrorReporter errorReporter) throws IOException {
        if (s2 == null) {
            s2 = "unnamed script";
        }
        if (o != null && this.getSecurityController() == null) {
            throw new IllegalArgumentException("securityDomain should be null if setSecurityController() was never called");
        }
        final boolean b2 = false;
        if (!(reader == null ^ s == null)) {
            Kit.codeBug();
        }
        boolean b3 = b2;
        if (scriptable == null) {
            b3 = true;
        }
        if (!(b3 ^ b)) {
            Kit.codeBug();
        }
        final CompilerEnvirons compilerEnvirons = new CompilerEnvirons();
        compilerEnvirons.initFromContext(this);
        if (errorReporter == null) {
            errorReporter = compilerEnvirons.getErrorReporter();
        }
        String reader2;
        Reader reader3;
        if (this.debugger != null && reader != null) {
            reader2 = Kit.readReader(reader);
            reader3 = null;
        }
        else {
            reader3 = reader;
            reader2 = s;
        }
        final Parser parser = new Parser(compilerEnvirons, errorReporter);
        if (b) {
            parser.calledByCompileFunction = true;
        }
        AstRoot astRoot;
        if (reader2 != null) {
            astRoot = parser.parse(reader2, s2, n);
        }
        else {
            astRoot = parser.parse(reader3, s2, n);
        }
        if (b && (astRoot.getFirstChild() == null || astRoot.getFirstChild().getType() != 109)) {
            final StringBuilder sb = new StringBuilder();
            sb.append("compileFunction only accepts source with single JS function: ");
            sb.append(reader2);
            throw new IllegalArgumentException(sb.toString());
        }
        final ScriptNode transformTree = new IRFactory(compilerEnvirons, errorReporter).transformTree(astRoot);
        Evaluator compiler;
        if (evaluator == null) {
            compiler = this.createCompiler();
        }
        else {
            compiler = evaluator;
        }
        final Object compile = compiler.compile(compilerEnvirons, transformTree, transformTree.getEncodedSource(), b);
        if (this.debugger != null) {
            if (reader2 == null) {
                Kit.codeBug();
            }
            if (!(compile instanceof DebuggableScript)) {
                throw new RuntimeException("NOT SUPPORTED");
            }
            notifyDebugger_r(this, (DebuggableScript)compile, reader2);
        }
        if (b) {
            return compiler.createFunctionObject(this, scriptable, compile, o);
        }
        return compiler.createScriptObject(compile, o);
    }
    
    private Evaluator createCompiler() {
        Evaluator evaluator = null;
        if (this.optimizationLevel >= 0) {
            evaluator = evaluator;
            if (Context.codegenClass != null) {
                evaluator = (Evaluator)Kit.newInstanceOrNull(Context.codegenClass);
            }
        }
        Evaluator interpreter;
        if ((interpreter = evaluator) == null) {
            interpreter = createInterpreter();
        }
        return interpreter;
    }
    
    static Evaluator createInterpreter() {
        return (Evaluator)Kit.newInstanceOrNull(Context.interpreterClass);
    }
    
    public static Context enter() {
        return enter(null);
    }
    
    @Deprecated
    public static Context enter(final Context context) {
        return enter(context, ContextFactory.getGlobal());
    }
    
    static final Context enter(Context context, final ContextFactory contextFactory) {
        final Object threadContextHelper = VMBridge.instance.getThreadContextHelper();
        final Context context2 = VMBridge.instance.getContext(threadContextHelper);
        if (context2 != null) {
            context = context2;
        }
        else {
            Context context3;
            if (context == null) {
                context = contextFactory.makeContext();
                if (context.enterCount != 0) {
                    throw new IllegalStateException("factory.makeContext() returned Context instance already associated with some thread");
                }
                contextFactory.onContextCreated(context);
                context3 = context;
                if (contextFactory.isSealed()) {
                    context3 = context;
                    if (!context.isSealed()) {
                        context.seal(null);
                        context3 = context;
                    }
                }
            }
            else {
                context3 = context;
                if (context.enterCount != 0) {
                    throw new IllegalStateException("can not use Context instance already associated with some thread");
                }
            }
            VMBridge.instance.setContext(threadContextHelper, context3);
            context = context3;
        }
        ++context.enterCount;
        return context;
    }
    
    public static void exit() {
        final Object threadContextHelper = VMBridge.instance.getThreadContextHelper();
        final Context context = VMBridge.instance.getContext(threadContextHelper);
        if (context == null) {
            throw new IllegalStateException("Calling Context.exit without previous Context.enter");
        }
        if (context.enterCount < 1) {
            Kit.codeBug();
        }
        if (--context.enterCount == 0) {
            VMBridge.instance.setContext(threadContextHelper, null);
            context.factory.onContextReleased(context);
        }
    }
    
    private void firePropertyChangeImpl(final Object o, final String s, final Object o2, final Object o3) {
        int n = 0;
        while (true) {
            final Object listener = Kit.getListener(o, n);
            if (listener == null) {
                break;
            }
            if (listener instanceof PropertyChangeListener) {
                ((PropertyChangeListener)listener).propertyChange(new PropertyChangeEvent(this, s, o2, o3));
            }
            ++n;
        }
    }
    
    static Context getContext() {
        final Context currentContext = getCurrentContext();
        if (currentContext == null) {
            throw new RuntimeException("No Context associated with current Thread");
        }
        return currentContext;
    }
    
    public static Context getCurrentContext() {
        return VMBridge.instance.getContext(VMBridge.instance.getThreadContextHelper());
    }
    
    public static DebuggableScript getDebuggableView(final Script script) {
        if (script instanceof NativeFunction) {
            return ((NativeFunction)script).getDebuggableView();
        }
        return null;
    }
    
    static String getSourcePositionFromStack(final int[] array) {
        final Context currentContext = getCurrentContext();
        if (currentContext == null) {
            return null;
        }
        if (currentContext.lastInterpreterFrame != null) {
            final Evaluator interpreter = createInterpreter();
            if (interpreter != null) {
                return interpreter.getSourcePositionFromStack(currentContext, array);
            }
        }
        final StackTraceElement[] stackTrace = new Throwable().getStackTrace();
        for (int length = stackTrace.length, i = 0; i < length; ++i) {
            final StackTraceElement stackTraceElement = stackTrace[i];
            final String fileName = stackTraceElement.getFileName();
            if (fileName != null && !fileName.endsWith(".java")) {
                final int lineNumber = stackTraceElement.getLineNumber();
                if (lineNumber >= 0) {
                    array[0] = lineNumber;
                    return fileName;
                }
            }
        }
        return null;
    }
    
    public static Object getUndefinedValue() {
        return Undefined.instance;
    }
    
    public static boolean isValidLanguageVersion(final int n) {
        switch (n) {
            default: {
                return false;
            }
            case 0:
            case 100:
            case 110:
            case 120:
            case 130:
            case 140:
            case 150:
            case 160:
            case 170:
            case 180:
            case 200: {
                return true;
            }
        }
    }
    
    public static boolean isValidOptimizationLevel(final int n) {
        return -1 <= n && n <= 9;
    }
    
    public static Object javaToJS(final Object o, final Scriptable scriptable) {
        if (o instanceof String || o instanceof Number || o instanceof Boolean) {
            return o;
        }
        if (o instanceof Scriptable) {
            return o;
        }
        if (o instanceof Character) {
            return String.valueOf((char)o);
        }
        final Context context = getContext();
        return context.getWrapFactory().wrap(context, scriptable, o, null);
    }
    
    public static Object jsToJava(final Object o, final Class<?> clazz) throws EvaluatorException {
        return NativeJavaObject.coerceTypeImpl(clazz, o);
    }
    
    private static void notifyDebugger_r(final Context context, final DebuggableScript debuggableScript, final String s) {
        context.debugger.handleCompilationDone(context, debuggableScript, s);
        for (int i = 0; i != debuggableScript.getFunctionCount(); ++i) {
            notifyDebugger_r(context, debuggableScript.getFunction(i), s);
        }
    }
    
    static void onSealedMutation() {
        throw new IllegalStateException();
    }
    
    @Deprecated
    public static void removeContextListener(final ContextListener contextListener) {
        ContextFactory.getGlobal().addListener((ContextFactory.Listener)contextListener);
    }
    
    public static void reportError(final String s) {
        final int[] array = { 0 };
        reportError(s, getSourcePositionFromStack(array), array[0], null, 0);
    }
    
    public static void reportError(final String s, final String s2, final int n, final String s3, final int n2) {
        final Context currentContext = getCurrentContext();
        if (currentContext != null) {
            currentContext.getErrorReporter().error(s, s2, n, s3, n2);
            return;
        }
        throw new EvaluatorException(s, s2, n, s3, n2);
    }
    
    public static EvaluatorException reportRuntimeError(final String s) {
        final int[] array = { 0 };
        return reportRuntimeError(s, getSourcePositionFromStack(array), array[0], null, 0);
    }
    
    public static EvaluatorException reportRuntimeError(final String s, final String s2, final int n, final String s3, final int n2) {
        final Context currentContext = getCurrentContext();
        if (currentContext != null) {
            return currentContext.getErrorReporter().runtimeError(s, s2, n, s3, n2);
        }
        throw new EvaluatorException(s, s2, n, s3, n2);
    }
    
    static EvaluatorException reportRuntimeError0(final String s) {
        return reportRuntimeError(ScriptRuntime.getMessage0(s));
    }
    
    static EvaluatorException reportRuntimeError1(final String s, final Object o) {
        return reportRuntimeError(ScriptRuntime.getMessage1(s, o));
    }
    
    static EvaluatorException reportRuntimeError2(final String s, final Object o, final Object o2) {
        return reportRuntimeError(ScriptRuntime.getMessage2(s, o, o2));
    }
    
    static EvaluatorException reportRuntimeError3(final String s, final Object o, final Object o2, final Object o3) {
        return reportRuntimeError(ScriptRuntime.getMessage3(s, o, o2, o3));
    }
    
    static EvaluatorException reportRuntimeError4(final String s, final Object o, final Object o2, final Object o3, final Object o4) {
        return reportRuntimeError(ScriptRuntime.getMessage4(s, o, o2, o3, o4));
    }
    
    public static void reportWarning(final String s) {
        final int[] array = { 0 };
        reportWarning(s, getSourcePositionFromStack(array), array[0], null, 0);
    }
    
    public static void reportWarning(final String s, final String s2, final int n, final String s3, final int n2) {
        final Context context = getContext();
        if (context.hasFeature(12)) {
            reportError(s, s2, n, s3, n2);
            return;
        }
        context.getErrorReporter().warning(s, s2, n, s3, n2);
    }
    
    public static void reportWarning(final String s, final Throwable t) {
        final int[] array = { 0 };
        final String sourcePositionFromStack = getSourcePositionFromStack(array);
        final StringWriter stringWriter = new StringWriter();
        final PrintWriter printWriter = new PrintWriter(stringWriter);
        printWriter.println(s);
        t.printStackTrace(printWriter);
        printWriter.flush();
        reportWarning(stringWriter.toString(), sourcePositionFromStack, array[0], null, 0);
    }
    
    @Deprecated
    public static void setCachingEnabled(final boolean b) {
    }
    
    public static RuntimeException throwAsScriptRuntimeEx(Throwable targetException) {
        while (targetException instanceof InvocationTargetException) {
            targetException = ((InvocationTargetException)targetException).getTargetException();
        }
        if (targetException instanceof Error) {
            final Context context = getContext();
            if (context == null || !context.hasFeature(13)) {
                throw (Error)targetException;
            }
        }
        if (targetException instanceof RhinoException) {
            throw (RhinoException)targetException;
        }
        throw new WrappedException(targetException);
    }
    
    public static boolean toBoolean(final Object o) {
        return ScriptRuntime.toBoolean(o);
    }
    
    public static double toNumber(final Object o) {
        return ScriptRuntime.toNumber(o);
    }
    
    public static Scriptable toObject(final Object o, final Scriptable scriptable) {
        return ScriptRuntime.toObject(scriptable, o);
    }
    
    @Deprecated
    public static Scriptable toObject(final Object o, final Scriptable scriptable, final Class<?> clazz) {
        return ScriptRuntime.toObject(scriptable, o);
    }
    
    public static String toString(final Object o) {
        return ScriptRuntime.toString(o);
    }
    
    @Deprecated
    public static Object toType(Object jsToJava, final Class<?> clazz) throws IllegalArgumentException {
        try {
            jsToJava = jsToJava(jsToJava, clazz);
            return jsToJava;
        }
        catch (EvaluatorException ex2) {
            final IllegalArgumentException ex = new IllegalArgumentException(ex2.getMessage());
            Kit.initCause(ex, ex2);
            throw ex;
        }
    }
    
    public void addActivationName(final String s) {
        if (this.sealed) {
            onSealedMutation();
        }
        if (this.activationNames == null) {
            this.activationNames = new HashSet<String>();
        }
        this.activationNames.add(s);
    }
    
    public final void addPropertyChangeListener(final PropertyChangeListener propertyChangeListener) {
        if (this.sealed) {
            onSealedMutation();
        }
        this.propertyListeners = Kit.addListener(this.propertyListeners, propertyChangeListener);
    }
    
    public Object callFunctionWithContinuations(final Callable callable, final Scriptable scriptable, final Object[] array) throws ContinuationPending {
        if (!(callable instanceof InterpretedFunction)) {
            throw new IllegalArgumentException("Function argument was not created by interpreted mode ");
        }
        if (ScriptRuntime.hasTopCall(this)) {
            throw new IllegalStateException("Cannot have any pending top calls when executing a script with continuations");
        }
        this.isContinuationsTopCall = true;
        return ScriptRuntime.doTopCall(callable, this, scriptable, scriptable, array);
    }
    
    public ContinuationPending captureContinuation() {
        return new ContinuationPending(Interpreter.captureContinuation(this));
    }
    
    public final Function compileFunction(final Scriptable scriptable, final String s, final String s2, final int n, final Object o) {
        return this.compileFunction(scriptable, s, null, null, s2, n, o);
    }
    
    final Function compileFunction(final Scriptable scriptable, final String s, final Evaluator evaluator, final ErrorReporter errorReporter, final String s2, final int n, final Object o) {
        try {
            return (Function)this.compileImpl(scriptable, null, s, s2, n, o, true, evaluator, errorReporter);
        }
        catch (IOException ex) {
            throw new RuntimeException();
        }
    }
    
    public final Script compileReader(final Reader reader, final String s, final int n, final Object o) throws IOException {
        int n2 = n;
        if (n < 0) {
            n2 = 0;
        }
        return (Script)this.compileImpl(null, reader, null, s, n2, o, false, null, null);
    }
    
    @Deprecated
    public final Script compileReader(final Scriptable scriptable, final Reader reader, final String s, final int n, final Object o) throws IOException {
        return this.compileReader(reader, s, n, o);
    }
    
    public final Script compileString(final String s, final String s2, final int n, final Object o) {
        int n2 = n;
        if (n < 0) {
            n2 = 0;
        }
        return this.compileString(s, null, null, s2, n2, o);
    }
    
    final Script compileString(final String s, final Evaluator evaluator, final ErrorReporter errorReporter, final String s2, final int n, final Object o) {
        try {
            return (Script)this.compileImpl(null, null, s, s2, n, o, false, evaluator, errorReporter);
        }
        catch (IOException ex) {
            throw new RuntimeException();
        }
    }
    
    public GeneratedClassLoader createClassLoader(final ClassLoader classLoader) {
        return this.getFactory().createClassLoader(classLoader);
    }
    
    public final String decompileFunction(final Function function, final int n) {
        if (function instanceof BaseFunction) {
            return ((BaseFunction)function).decompile(n, 0);
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("function ");
        sb.append(function.getClassName());
        sb.append("() {\n\t[native code]\n}\n");
        return sb.toString();
    }
    
    public final String decompileFunctionBody(final Function function, final int n) {
        if (function instanceof BaseFunction) {
            return ((BaseFunction)function).decompile(n, 1);
        }
        return "[native code]\n";
    }
    
    public final String decompileScript(final Script script, final int n) {
        return ((NativeFunction)script).decompile(n, 0);
    }
    
    public final Object evaluateReader(final Scriptable scriptable, final Reader reader, final String s, final int n, final Object o) throws IOException {
        final Script compileReader = this.compileReader(scriptable, reader, s, n, o);
        if (compileReader != null) {
            return compileReader.exec(this, scriptable);
        }
        return null;
    }
    
    public final Object evaluateString(final Scriptable scriptable, final String s, final String s2, final int n, final Object o) {
        final Script compileString = this.compileString(s, s2, n, o);
        if (compileString != null) {
            return compileString.exec(this, scriptable);
        }
        return null;
    }
    
    public Object executeScriptWithContinuations(final Script script, final Scriptable scriptable) throws ContinuationPending {
        if (script instanceof InterpretedFunction && ((InterpretedFunction)script).isScript()) {
            return this.callFunctionWithContinuations((Callable)script, scriptable, ScriptRuntime.emptyArgs);
        }
        throw new IllegalArgumentException("Script argument was not a script or was not created by interpreted mode ");
    }
    
    final void firePropertyChange(final String s, final Object o, final Object o2) {
        final Object propertyListeners = this.propertyListeners;
        if (propertyListeners != null) {
            this.firePropertyChangeImpl(propertyListeners, s, o, o2);
        }
    }
    
    public final ClassLoader getApplicationClassLoader() {
        if (this.applicationClassLoader == null) {
            final ContextFactory factory = this.getFactory();
            ClassLoader applicationClassLoader;
            if ((applicationClassLoader = factory.getApplicationClassLoader()) == null) {
                final ClassLoader currentThreadClassLoader = VMBridge.instance.getCurrentThreadClassLoader();
                if (currentThreadClassLoader != null && Kit.testIfCanLoadRhinoClasses(currentThreadClassLoader)) {
                    return currentThreadClassLoader;
                }
                final Class<? extends ContextFactory> class1 = factory.getClass();
                if (class1 != ScriptRuntime.ContextFactoryClass) {
                    applicationClassLoader = class1.getClassLoader();
                }
                else {
                    applicationClassLoader = this.getClass().getClassLoader();
                }
            }
            this.applicationClassLoader = applicationClassLoader;
        }
        return this.applicationClassLoader;
    }
    
    final ClassShutter getClassShutter() {
        synchronized (this) {
            return this.classShutter;
        }
    }
    
    public final ClassShutterSetter getClassShutterSetter() {
        synchronized (this) {
            if (this.hasClassShutter) {
                return null;
            }
            this.hasClassShutter = true;
            return (ClassShutterSetter)new ClassShutterSetter() {
                @Override
                public ClassShutter getClassShutter() {
                    return Context.this.classShutter;
                }
                
                @Override
                public void setClassShutter(final ClassShutter classShutter) {
                    Context.this.classShutter = classShutter;
                }
            };
        }
    }
    
    public final Debugger getDebugger() {
        return this.debugger;
    }
    
    public final Object getDebuggerContextData() {
        return this.debuggerData;
    }
    
    public XMLLib.Factory getE4xImplementationFactory() {
        return this.getFactory().getE4xImplementationFactory();
    }
    
    public final Object[] getElements(final Scriptable scriptable) {
        return ScriptRuntime.getArrayElements(scriptable);
    }
    
    public final ErrorReporter getErrorReporter() {
        if (this.errorReporter == null) {
            return DefaultErrorReporter.instance;
        }
        return this.errorReporter;
    }
    
    public final ContextFactory getFactory() {
        return this.factory;
    }
    
    public final String getImplementationVersion() {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: ifnonnull       255
        //     6: ldc             Lorg/mozilla/javascript/Context;.class
        //     8: invokevirtual   java/lang/Class.getClassLoader:()Ljava/lang/ClassLoader;
        //    11: ldc_w           "META-INF/MANIFEST.MF"
        //    14: invokevirtual   java/lang/ClassLoader.getResources:(Ljava/lang/String;)Ljava/util/Enumeration;
        //    17: astore          4
        //    19: aload           4
        //    21: invokeinterface java/util/Enumeration.hasMoreElements:()Z
        //    26: ifeq            255
        //    29: aload           4
        //    31: invokeinterface java/util/Enumeration.nextElement:()Ljava/lang/Object;
        //    36: checkcast       Ljava/net/URL;
        //    39: astore_3       
        //    40: aconst_null    
        //    41: astore_2       
        //    42: aconst_null    
        //    43: astore_1       
        //    44: aload_3        
        //    45: invokevirtual   java/net/URL.openStream:()Ljava/io/InputStream;
        //    48: astore_3       
        //    49: aload_3        
        //    50: astore_1       
        //    51: aload_3        
        //    52: astore_2       
        //    53: new             Ljava/util/jar/Manifest;
        //    56: dup            
        //    57: aload_3        
        //    58: invokespecial   java/util/jar/Manifest.<init>:(Ljava/io/InputStream;)V
        //    61: invokevirtual   java/util/jar/Manifest.getMainAttributes:()Ljava/util/jar/Attributes;
        //    64: astore          5
        //    66: aload_3        
        //    67: astore_1       
        //    68: aload_3        
        //    69: astore_2       
        //    70: ldc_w           "Mozilla Rhino"
        //    73: aload           5
        //    75: ldc_w           "Implementation-Title"
        //    78: invokevirtual   java/util/jar/Attributes.getValue:(Ljava/lang/String;)Ljava/lang/String;
        //    81: invokevirtual   java/lang/String.equals:(Ljava/lang/Object;)Z
        //    84: ifeq            207
        //    87: aload_3        
        //    88: astore_1       
        //    89: aload_3        
        //    90: astore_2       
        //    91: new             Ljava/lang/StringBuilder;
        //    94: dup            
        //    95: invokespecial   java/lang/StringBuilder.<init>:()V
        //    98: astore          6
        //   100: aload_3        
        //   101: astore_1       
        //   102: aload_3        
        //   103: astore_2       
        //   104: aload           6
        //   106: ldc_w           "Rhino "
        //   109: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   112: pop            
        //   113: aload_3        
        //   114: astore_1       
        //   115: aload_3        
        //   116: astore_2       
        //   117: aload           6
        //   119: aload           5
        //   121: ldc_w           "Implementation-Version"
        //   124: invokevirtual   java/util/jar/Attributes.getValue:(Ljava/lang/String;)Ljava/lang/String;
        //   127: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   130: pop            
        //   131: aload_3        
        //   132: astore_1       
        //   133: aload_3        
        //   134: astore_2       
        //   135: aload           6
        //   137: ldc_w           " "
        //   140: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   143: pop            
        //   144: aload_3        
        //   145: astore_1       
        //   146: aload_3        
        //   147: astore_2       
        //   148: aload           6
        //   150: aload           5
        //   152: ldc_w           "Built-Date"
        //   155: invokevirtual   java/util/jar/Attributes.getValue:(Ljava/lang/String;)Ljava/lang/String;
        //   158: ldc_w           "-"
        //   161: ldc_w           " "
        //   164: invokevirtual   java/lang/String.replaceAll:(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
        //   167: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   170: pop            
        //   171: aload_3        
        //   172: astore_1       
        //   173: aload_3        
        //   174: astore_2       
        //   175: aload           6
        //   177: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   180: putstatic       org/mozilla/javascript/Context.implementationVersion:Ljava/lang/String;
        //   183: aload_3        
        //   184: astore_1       
        //   185: aload_3        
        //   186: astore_2       
        //   187: getstatic       org/mozilla/javascript/Context.implementationVersion:Ljava/lang/String;
        //   190: astore          5
        //   192: aload_3        
        //   193: ifnull          204
        //   196: aload_3        
        //   197: invokevirtual   java/io/InputStream.close:()V
        //   200: aload           5
        //   202: areturn        
        //   203: astore_1       
        //   204: aload           5
        //   206: areturn        
        //   207: aload_3        
        //   208: ifnull          249
        //   211: aload_3        
        //   212: invokevirtual   java/io/InputStream.close:()V
        //   215: goto            249
        //   218: astore_2       
        //   219: aload_1        
        //   220: ifnull          231
        //   223: aload_1        
        //   224: invokevirtual   java/io/InputStream.close:()V
        //   227: goto            231
        //   230: astore_1       
        //   231: aload_2        
        //   232: athrow         
        //   233: astore_1       
        //   234: aload_2        
        //   235: ifnull          249
        //   238: aload_2        
        //   239: invokevirtual   java/io/InputStream.close:()V
        //   242: goto            249
        //   245: astore_1       
        //   246: goto            249
        //   249: goto            19
        //   252: astore_1       
        //   253: aconst_null    
        //   254: areturn        
        //   255: getstatic       org/mozilla/javascript/Context.implementationVersion:Ljava/lang/String;
        //   258: areturn        
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  6      19     252    255    Ljava/io/IOException;
        //  44     49     233    245    Ljava/io/IOException;
        //  44     49     218    233    Any
        //  53     66     233    245    Ljava/io/IOException;
        //  53     66     218    233    Any
        //  70     87     233    245    Ljava/io/IOException;
        //  70     87     218    233    Any
        //  91     100    233    245    Ljava/io/IOException;
        //  91     100    218    233    Any
        //  104    113    233    245    Ljava/io/IOException;
        //  104    113    218    233    Any
        //  117    131    233    245    Ljava/io/IOException;
        //  117    131    218    233    Any
        //  135    144    233    245    Ljava/io/IOException;
        //  135    144    218    233    Any
        //  148    171    233    245    Ljava/io/IOException;
        //  148    171    218    233    Any
        //  175    183    233    245    Ljava/io/IOException;
        //  175    183    218    233    Any
        //  187    192    233    245    Ljava/io/IOException;
        //  187    192    218    233    Any
        //  196    200    203    204    Ljava/io/IOException;
        //  211    215    245    249    Ljava/io/IOException;
        //  223    227    230    231    Ljava/io/IOException;
        //  238    242    245    249    Ljava/io/IOException;
        // 
        // The error that occurred was:
        // 
        // java.lang.IllegalStateException: Expression is linked from several locations: Label_0231:
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
    
    public final int getInstructionObserverThreshold() {
        return this.instructionThreshold;
    }
    
    public final int getLanguageVersion() {
        return this.version;
    }
    
    public final Locale getLocale() {
        if (this.locale == null) {
            this.locale = Locale.getDefault();
        }
        return this.locale;
    }
    
    public final int getMaximumInterpreterStackDepth() {
        return this.maximumInterpreterStackDepth;
    }
    
    public final int getOptimizationLevel() {
        return this.optimizationLevel;
    }
    
    RegExpProxy getRegExpProxy() {
        if (this.regExpProxy == null) {
            final Class<?> classOrNull = Kit.classOrNull("org.mozilla.javascript.regexp.RegExpImpl");
            if (classOrNull != null) {
                this.regExpProxy = (RegExpProxy)Kit.newInstanceOrNull(classOrNull);
            }
        }
        return this.regExpProxy;
    }
    
    SecurityController getSecurityController() {
        final SecurityController global = SecurityController.global();
        if (global != null) {
            return global;
        }
        return this.securityController;
    }
    
    public final Object getThreadLocal(final Object o) {
        if (this.threadLocalMap == null) {
            return null;
        }
        return this.threadLocalMap.get(o);
    }
    
    public final WrapFactory getWrapFactory() {
        if (this.wrapFactory == null) {
            this.wrapFactory = new WrapFactory();
        }
        return this.wrapFactory;
    }
    
    public boolean hasFeature(final int n) {
        return this.getFactory().hasFeature(this, n);
    }
    
    public final Scriptable initSafeStandardObjects(final ScriptableObject scriptableObject) {
        return this.initSafeStandardObjects(scriptableObject, false);
    }
    
    public final ScriptableObject initSafeStandardObjects() {
        return this.initSafeStandardObjects(null, false);
    }
    
    public ScriptableObject initSafeStandardObjects(final ScriptableObject scriptableObject, final boolean b) {
        return ScriptRuntime.initSafeStandardObjects(this, scriptableObject, b);
    }
    
    public final Scriptable initStandardObjects(final ScriptableObject scriptableObject) {
        return this.initStandardObjects(scriptableObject, false);
    }
    
    public final ScriptableObject initStandardObjects() {
        return this.initStandardObjects(null, false);
    }
    
    public ScriptableObject initStandardObjects(final ScriptableObject scriptableObject, final boolean b) {
        return ScriptRuntime.initStandardObjects(this, scriptableObject, b);
    }
    
    public final boolean isActivationNeeded(final String s) {
        return this.activationNames != null && this.activationNames.contains(s);
    }
    
    public final boolean isGeneratingDebug() {
        return this.generatingDebug;
    }
    
    public final boolean isGeneratingDebugChanged() {
        return this.generatingDebugChanged;
    }
    
    public final boolean isGeneratingSource() {
        return this.generatingSource;
    }
    
    public final boolean isSealed() {
        return this.sealed;
    }
    
    final boolean isVersionECMA1() {
        return this.version == 0 || this.version >= 130;
    }
    
    public Scriptable newArray(final Scriptable scriptable, final int n) {
        final NativeArray nativeArray = new NativeArray(n);
        ScriptRuntime.setBuiltinProtoAndParent(nativeArray, scriptable, TopLevel.Builtins.Array);
        return nativeArray;
    }
    
    public Scriptable newArray(final Scriptable scriptable, final Object[] array) {
        if (array.getClass().getComponentType() != ScriptRuntime.ObjectClass) {
            throw new IllegalArgumentException();
        }
        final NativeArray nativeArray = new NativeArray(array);
        ScriptRuntime.setBuiltinProtoAndParent(nativeArray, scriptable, TopLevel.Builtins.Array);
        return nativeArray;
    }
    
    public Scriptable newObject(final Scriptable scriptable) {
        final NativeObject nativeObject = new NativeObject();
        ScriptRuntime.setBuiltinProtoAndParent(nativeObject, scriptable, TopLevel.Builtins.Object);
        return nativeObject;
    }
    
    public Scriptable newObject(final Scriptable scriptable, final String s) {
        return this.newObject(scriptable, s, ScriptRuntime.emptyArgs);
    }
    
    public Scriptable newObject(final Scriptable scriptable, final String s, final Object[] array) {
        return ScriptRuntime.newObject(this, scriptable, s, array);
    }
    
    protected void observeInstructionCount(final int n) {
        this.getFactory().observeInstructionCount(this, n);
    }
    
    public final void putThreadLocal(final Object o, final Object o2) {
        synchronized (this) {
            if (this.sealed) {
                onSealedMutation();
            }
            if (this.threadLocalMap == null) {
                this.threadLocalMap = new HashMap<Object, Object>();
            }
            this.threadLocalMap.put(o, o2);
        }
    }
    
    public void removeActivationName(final String s) {
        if (this.sealed) {
            onSealedMutation();
        }
        if (this.activationNames != null) {
            this.activationNames.remove(s);
        }
    }
    
    public final void removePropertyChangeListener(final PropertyChangeListener propertyChangeListener) {
        if (this.sealed) {
            onSealedMutation();
        }
        this.propertyListeners = Kit.removeListener(this.propertyListeners, propertyChangeListener);
    }
    
    public final void removeThreadLocal(final Object o) {
        if (this.sealed) {
            onSealedMutation();
        }
        if (this.threadLocalMap == null) {
            return;
        }
        this.threadLocalMap.remove(o);
    }
    
    public Object resumeContinuation(final Object o, final Scriptable scriptable, final Object o2) throws ContinuationPending {
        return Interpreter.restartContinuation((NativeContinuation)o, this, scriptable, new Object[] { o2 });
    }
    
    public final void seal(final Object sealKey) {
        if (this.sealed) {
            onSealedMutation();
        }
        this.sealed = true;
        this.sealKey = sealKey;
    }
    
    public final void setApplicationClassLoader(final ClassLoader applicationClassLoader) {
        if (this.sealed) {
            onSealedMutation();
        }
        if (applicationClassLoader == null) {
            this.applicationClassLoader = null;
            return;
        }
        if (!Kit.testIfCanLoadRhinoClasses(applicationClassLoader)) {
            throw new IllegalArgumentException("Loader can not resolve Rhino classes");
        }
        this.applicationClassLoader = applicationClassLoader;
    }
    
    public final void setClassShutter(final ClassShutter classShutter) {
        synchronized (this) {
            if (this.sealed) {
                onSealedMutation();
            }
            if (classShutter == null) {
                throw new IllegalArgumentException();
            }
            if (this.hasClassShutter) {
                throw new SecurityException("Cannot overwrite existing ClassShutter object");
            }
            this.classShutter = classShutter;
            this.hasClassShutter = true;
        }
    }
    
    public final void setDebugger(final Debugger debugger, final Object debuggerData) {
        if (this.sealed) {
            onSealedMutation();
        }
        this.debugger = debugger;
        this.debuggerData = debuggerData;
    }
    
    public final ErrorReporter setErrorReporter(final ErrorReporter errorReporter) {
        if (this.sealed) {
            onSealedMutation();
        }
        if (errorReporter == null) {
            throw new IllegalArgumentException();
        }
        final ErrorReporter errorReporter2 = this.getErrorReporter();
        if (errorReporter == errorReporter2) {
            return errorReporter2;
        }
        final Object propertyListeners = this.propertyListeners;
        if (propertyListeners != null) {
            this.firePropertyChangeImpl(propertyListeners, "error reporter", errorReporter2, errorReporter);
        }
        this.errorReporter = errorReporter;
        return errorReporter2;
    }
    
    public void setGenerateObserverCount(final boolean generateObserverCount) {
        this.generateObserverCount = generateObserverCount;
    }
    
    public final void setGeneratingDebug(final boolean generatingDebug) {
        if (this.sealed) {
            onSealedMutation();
        }
        this.generatingDebugChanged = true;
        if (generatingDebug && this.getOptimizationLevel() > 0) {
            this.setOptimizationLevel(0);
        }
        this.generatingDebug = generatingDebug;
    }
    
    public final void setGeneratingSource(final boolean generatingSource) {
        if (this.sealed) {
            onSealedMutation();
        }
        this.generatingSource = generatingSource;
    }
    
    public final void setInstructionObserverThreshold(final int instructionThreshold) {
        if (this.sealed) {
            onSealedMutation();
        }
        if (instructionThreshold < 0) {
            throw new IllegalArgumentException();
        }
        this.instructionThreshold = instructionThreshold;
        this.setGenerateObserverCount(instructionThreshold > 0);
    }
    
    public void setLanguageVersion(final int version) {
        if (this.sealed) {
            onSealedMutation();
        }
        checkLanguageVersion(version);
        final Object propertyListeners = this.propertyListeners;
        if (propertyListeners != null && version != this.version) {
            this.firePropertyChangeImpl(propertyListeners, "language version", this.version, version);
        }
        this.version = version;
    }
    
    public final Locale setLocale(final Locale locale) {
        if (this.sealed) {
            onSealedMutation();
        }
        final Locale locale2 = this.locale;
        this.locale = locale;
        return locale2;
    }
    
    public final void setMaximumInterpreterStackDepth(final int maximumInterpreterStackDepth) {
        if (this.sealed) {
            onSealedMutation();
        }
        if (this.optimizationLevel != -1) {
            throw new IllegalStateException("Cannot set maximumInterpreterStackDepth when optimizationLevel != -1");
        }
        if (maximumInterpreterStackDepth < 1) {
            throw new IllegalArgumentException("Cannot set maximumInterpreterStackDepth to less than 1");
        }
        this.maximumInterpreterStackDepth = maximumInterpreterStackDepth;
    }
    
    public final void setOptimizationLevel(final int n) {
        if (this.sealed) {
            onSealedMutation();
        }
        int optimizationLevel;
        if ((optimizationLevel = n) == -2) {
            optimizationLevel = -1;
        }
        checkOptimizationLevel(optimizationLevel);
        if (Context.codegenClass == null) {
            optimizationLevel = -1;
        }
        this.optimizationLevel = optimizationLevel;
    }
    
    public final void setSecurityController(final SecurityController securityController) {
        if (this.sealed) {
            onSealedMutation();
        }
        if (securityController == null) {
            throw new IllegalArgumentException();
        }
        if (this.securityController != null) {
            throw new SecurityException("Can not overwrite existing SecurityController object");
        }
        if (SecurityController.hasGlobal()) {
            throw new SecurityException("Can not overwrite existing global SecurityController object");
        }
        this.securityController = securityController;
    }
    
    public final void setWrapFactory(final WrapFactory wrapFactory) {
        if (this.sealed) {
            onSealedMutation();
        }
        if (wrapFactory == null) {
            throw new IllegalArgumentException();
        }
        this.wrapFactory = wrapFactory;
    }
    
    public final boolean stringIsCompilableUnit(final String s) {
        boolean b = false;
        final CompilerEnvirons compilerEnvirons = new CompilerEnvirons();
        compilerEnvirons.initFromContext(this);
        compilerEnvirons.setGeneratingSource(false);
        final Parser parser = new Parser(compilerEnvirons, DefaultErrorReporter.instance);
        try {
            parser.parse(s, null, 1);
        }
        catch (EvaluatorException ex) {
            b = true;
        }
        return !b || !parser.eof();
    }
    
    public final void unseal(final Object o) {
        if (o == null) {
            throw new IllegalArgumentException();
        }
        if (this.sealKey != o) {
            throw new IllegalArgumentException();
        }
        if (!this.sealed) {
            throw new IllegalStateException();
        }
        this.sealed = false;
        this.sealKey = null;
    }
    
    public interface ClassShutterSetter
    {
        ClassShutter getClassShutter();
        
        void setClassShutter(final ClassShutter p0);
    }
}
