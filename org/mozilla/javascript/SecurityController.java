package org.mozilla.javascript;

public abstract class SecurityController
{
    private static SecurityController global;
    
    public static GeneratedClassLoader createLoader(final ClassLoader classLoader, final Object o) {
        final Context context = Context.getContext();
        ClassLoader applicationClassLoader = classLoader;
        if (classLoader == null) {
            applicationClassLoader = context.getApplicationClassLoader();
        }
        final SecurityController securityController = context.getSecurityController();
        if (securityController == null) {
            return context.createClassLoader(applicationClassLoader);
        }
        return securityController.createClassLoader(applicationClassLoader, securityController.getDynamicSecurityDomain(o));
    }
    
    public static Class<?> getStaticSecurityDomainClass() {
        final SecurityController securityController = Context.getContext().getSecurityController();
        if (securityController == null) {
            return null;
        }
        return securityController.getStaticSecurityDomainClassInternal();
    }
    
    static SecurityController global() {
        return SecurityController.global;
    }
    
    public static boolean hasGlobal() {
        return SecurityController.global != null;
    }
    
    public static void initGlobal(final SecurityController global) {
        if (global == null) {
            throw new IllegalArgumentException();
        }
        if (SecurityController.global != null) {
            throw new SecurityException("Cannot overwrite already installed global SecurityController");
        }
        SecurityController.global = global;
    }
    
    public Object callWithDomain(final Object o, final Context context, final Callable callable, final Scriptable scriptable, final Scriptable scriptable2, final Object[] array) {
        return this.execWithDomain(context, scriptable, new Script() {
            @Override
            public Object exec(final Context context, final Scriptable scriptable) {
                return callable.call(context, scriptable, scriptable2, array);
            }
        }, o);
    }
    
    public abstract GeneratedClassLoader createClassLoader(final ClassLoader p0, final Object p1);
    
    @Deprecated
    public Object execWithDomain(final Context context, final Scriptable scriptable, final Script script, final Object o) {
        throw new IllegalStateException("callWithDomain should be overridden");
    }
    
    public abstract Object getDynamicSecurityDomain(final Object p0);
    
    public Class<?> getStaticSecurityDomainClassInternal() {
        return null;
    }
}
