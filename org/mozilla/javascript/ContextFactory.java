package org.mozilla.javascript;

import java.security.*;
import org.mozilla.javascript.xml.*;

public class ContextFactory
{
    private static ContextFactory global;
    private static volatile boolean hasCustomGlobal;
    private ClassLoader applicationClassLoader;
    private boolean disabledListening;
    private volatile Object listeners;
    private final Object listenersLock;
    private volatile boolean sealed;
    
    static {
        ContextFactory.global = new ContextFactory();
    }
    
    public ContextFactory() {
        this.listenersLock = new Object();
    }
    
    public static ContextFactory getGlobal() {
        return ContextFactory.global;
    }
    
    public static GlobalSetter getGlobalSetter() {
        synchronized (ContextFactory.class) {
            if (ContextFactory.hasCustomGlobal) {
                throw new IllegalStateException();
            }
            ContextFactory.hasCustomGlobal = true;
            return (GlobalSetter)new GlobalSetterImpl();
        }
    }
    
    public static boolean hasExplicitGlobal() {
        return ContextFactory.hasCustomGlobal;
    }
    
    public static void initGlobal(final ContextFactory global) {
        // monitorenter(ContextFactory.class)
        Label_0015: {
            if (global != null) {
                break Label_0015;
            }
            while (true) {
                try {
                    throw new IllegalArgumentException();
                    // iftrue(Label_0029:, !ContextFactory.hasCustomGlobal)
                    throw new IllegalStateException();
                    Label_0029: {
                        ContextFactory.hasCustomGlobal = true;
                    }
                    ContextFactory.global = global;
                    // monitorexit(ContextFactory.class)
                    return;
                    // monitorexit(ContextFactory.class)
                    throw;
                }
                finally {
                    continue;
                }
                break;
            }
        }
    }
    
    private boolean isDom3Present() {
        final Class<?> classOrNull = Kit.classOrNull("org.w3c.dom.Node");
        if (classOrNull == null) {
            return false;
        }
        try {
            classOrNull.getMethod("getUserData", String.class);
            return true;
        }
        catch (NoSuchMethodException ex) {
            return false;
        }
    }
    
    public final void addListener(final Listener listener) {
        this.checkNotSealed();
        synchronized (this.listenersLock) {
            if (this.disabledListening) {
                throw new IllegalStateException();
            }
            this.listeners = Kit.addListener(this.listeners, listener);
        }
    }
    
    public final Object call(final ContextAction contextAction) {
        return Context.call(this, contextAction);
    }
    
    protected final void checkNotSealed() {
        if (this.sealed) {
            throw new IllegalStateException();
        }
    }
    
    protected GeneratedClassLoader createClassLoader(final ClassLoader classLoader) {
        return AccessController.doPrivileged((PrivilegedAction<GeneratedClassLoader>)new PrivilegedAction<DefiningClassLoader>() {
            @Override
            public DefiningClassLoader run() {
                return new DefiningClassLoader(classLoader);
            }
        });
    }
    
    final void disableContextListening() {
        this.checkNotSealed();
        synchronized (this.listenersLock) {
            this.disabledListening = true;
            this.listeners = null;
        }
    }
    
    protected Object doTopCall(final Callable callable, final Context context, final Scriptable scriptable, final Scriptable scriptable2, final Object[] array) {
        final Object call = callable.call(context, scriptable, scriptable2, array);
        if (call instanceof ConsString) {
            return call.toString();
        }
        return call;
    }
    
    @Deprecated
    public final Context enter() {
        return this.enterContext(null);
    }
    
    public Context enterContext() {
        return this.enterContext(null);
    }
    
    public final Context enterContext(final Context context) {
        return Context.enter(context, this);
    }
    
    @Deprecated
    public final void exit() {
        Context.exit();
    }
    
    public final ClassLoader getApplicationClassLoader() {
        return this.applicationClassLoader;
    }
    
    protected XMLLib.Factory getE4xImplementationFactory() {
        if (this.isDom3Present()) {
            return XMLLib.Factory.create("org.mozilla.javascript.xmlimpl.XMLLibImpl");
        }
        return null;
    }
    
    protected boolean hasFeature(final Context context, int n) {
        final boolean b = true;
        boolean b2 = true;
        switch (n) {
            default: {
                throw new IllegalArgumentException(String.valueOf(n));
            }
            case 14: {
                return true;
            }
            case 13: {
                return false;
            }
            case 12: {
                return false;
            }
            case 11: {
                return false;
            }
            case 10: {
                return false;
            }
            case 9: {
                return false;
            }
            case 8: {
                return false;
            }
            case 7: {
                return false;
            }
            case 6: {
                n = context.getLanguageVersion();
                if (n != 0) {
                    if (n >= 160) {
                        return true;
                    }
                    b2 = false;
                }
                return b2;
            }
            case 5: {
                return true;
            }
            case 4: {
                return context.getLanguageVersion() == 120;
            }
            case 3: {
                return true;
            }
            case 2: {
                return false;
            }
            case 1: {
                n = context.getLanguageVersion();
                boolean b3 = b;
                if (n != 100) {
                    b3 = b;
                    if (n != 110) {
                        if (n == 120) {
                            return true;
                        }
                        b3 = false;
                    }
                }
                return b3;
            }
        }
    }
    
    public final void initApplicationClassLoader(final ClassLoader applicationClassLoader) {
        if (applicationClassLoader == null) {
            throw new IllegalArgumentException("loader is null");
        }
        if (!Kit.testIfCanLoadRhinoClasses(applicationClassLoader)) {
            throw new IllegalArgumentException("Loader can not resolve Rhino classes");
        }
        if (this.applicationClassLoader != null) {
            throw new IllegalStateException("applicationClassLoader can only be set once");
        }
        this.checkNotSealed();
        this.applicationClassLoader = applicationClassLoader;
    }
    
    public final boolean isSealed() {
        return this.sealed;
    }
    
    protected Context makeContext() {
        return new Context(this);
    }
    
    protected void observeInstructionCount(final Context context, final int n) {
    }
    
    protected void onContextCreated(final Context context) {
        final Object listeners = this.listeners;
        int n = 0;
        while (true) {
            final Listener listener = (Listener)Kit.getListener(listeners, n);
            if (listener == null) {
                break;
            }
            listener.contextCreated(context);
            ++n;
        }
    }
    
    protected void onContextReleased(final Context context) {
        final Object listeners = this.listeners;
        int n = 0;
        while (true) {
            final Listener listener = (Listener)Kit.getListener(listeners, n);
            if (listener == null) {
                break;
            }
            listener.contextReleased(context);
            ++n;
        }
    }
    
    public final void removeListener(final Listener listener) {
        this.checkNotSealed();
        synchronized (this.listenersLock) {
            if (this.disabledListening) {
                throw new IllegalStateException();
            }
            this.listeners = Kit.removeListener(this.listeners, listener);
        }
    }
    
    public final void seal() {
        this.checkNotSealed();
        this.sealed = true;
    }
    
    class GlobalSetterImpl implements GlobalSetter
    {
        @Override
        public ContextFactory getContextFactoryGlobal() {
            return ContextFactory.global;
        }
        
        @Override
        public void setContextFactoryGlobal(ContextFactory contextFactory) {
            if (contextFactory == null) {
                contextFactory = new ContextFactory();
            }
            ContextFactory.global = contextFactory;
        }
    }
    
    public interface GlobalSetter
    {
        ContextFactory getContextFactoryGlobal();
        
        void setContextFactoryGlobal(final ContextFactory p0);
    }
    
    public interface Listener
    {
        void contextCreated(final Context p0);
        
        void contextReleased(final Context p0);
    }
}
