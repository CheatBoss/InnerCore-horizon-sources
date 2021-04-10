package org.mozilla.javascript.tools.shell;

import java.security.cert.*;
import java.net.*;
import java.io.*;
import org.mozilla.javascript.*;
import java.util.*;
import java.security.*;

public class JavaPolicySecurity extends SecurityProxy
{
    public JavaPolicySecurity() {
        new CodeSource(null, (Certificate[])null);
    }
    
    private ProtectionDomain getDynamicDomain(final ProtectionDomain protectionDomain) {
        return new ProtectionDomain(null, new ContextPermissions(protectionDomain));
    }
    
    private ProtectionDomain getUrlDomain(final URL url) {
        final CodeSource codeSource = new CodeSource(url, (Certificate[])null);
        return new ProtectionDomain(codeSource, Policy.getPolicy().getPermissions(codeSource));
    }
    
    private URL getUrlObj(final String s) {
        try {
            return new URL(s);
        }
        catch (MalformedURLException ex2) {
            String s3;
            final String s2 = s3 = System.getProperty("user.dir").replace((char)92, (char)47);
            if (!s2.endsWith("/")) {
                final StringBuilder sb = new StringBuilder();
                sb.append(s2);
                sb.append('/');
                s3 = sb.toString();
            }
            try {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("file:");
                sb2.append(s3);
                return new URL(new URL(sb2.toString()), s);
            }
            catch (MalformedURLException ex) {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("Can not construct file URL for '");
                sb3.append(s);
                sb3.append("':");
                sb3.append(ex.getMessage());
                throw new RuntimeException(sb3.toString());
            }
        }
    }
    
    @Override
    protected void callProcessFileSecure(final Context context, final Scriptable scriptable, final String s) {
        AccessController.doPrivileged((PrivilegedAction<Object>)new PrivilegedAction<Object>() {
            @Override
            public Object run() {
                final URL access$000 = JavaPolicySecurity.this.getUrlObj(s);
                final ProtectionDomain access$2 = JavaPolicySecurity.this.getUrlDomain(access$000);
                try {
                    Main.processFileSecure(context, scriptable, access$000.toExternalForm(), access$2);
                    return null;
                }
                catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
    }
    
    @Override
    public Object callWithDomain(final Object o, final Context context, final Callable callable, final Scriptable scriptable, final Scriptable scriptable2, final Object[] array) {
        return AccessController.doPrivileged((PrivilegedAction<Object>)new PrivilegedAction<Object>() {
            @Override
            public Object run() {
                return callable.call(context, scriptable, scriptable2, array);
            }
        }, new AccessControlContext(new ProtectionDomain[] { this.getDynamicDomain((ProtectionDomain)o) }));
    }
    
    @Override
    public GeneratedClassLoader createClassLoader(final ClassLoader classLoader, final Object o) {
        return AccessController.doPrivileged((PrivilegedAction<GeneratedClassLoader>)new PrivilegedAction<Loader>() {
            final /* synthetic */ ProtectionDomain val$domain = (ProtectionDomain)o;
            
            @Override
            public Loader run() {
                return new Loader(classLoader, this.val$domain);
            }
        });
    }
    
    @Override
    public Object getDynamicSecurityDomain(final Object o) {
        return this.getDynamicDomain((ProtectionDomain)o);
    }
    
    @Override
    public Class<?> getStaticSecurityDomainClassInternal() {
        return ProtectionDomain.class;
    }
    
    private static class ContextPermissions extends PermissionCollection
    {
        static final long serialVersionUID = -1721494496320750721L;
        AccessControlContext _context;
        PermissionCollection _statisPermissions;
        
        ContextPermissions(final ProtectionDomain protectionDomain) {
            this._context = AccessController.getContext();
            if (protectionDomain != null) {
                this._statisPermissions = protectionDomain.getPermissions();
            }
            this.setReadOnly();
        }
        
        @Override
        public void add(final Permission permission) {
            throw new RuntimeException("NOT IMPLEMENTED");
        }
        
        @Override
        public Enumeration<Permission> elements() {
            return new Enumeration<Permission>() {
                @Override
                public boolean hasMoreElements() {
                    return false;
                }
                
                @Override
                public Permission nextElement() {
                    return null;
                }
            };
        }
        
        @Override
        public boolean implies(final Permission permission) {
            if (this._statisPermissions != null && !this._statisPermissions.implies(permission)) {
                return false;
            }
            try {
                this._context.checkPermission(permission);
                return true;
            }
            catch (AccessControlException ex) {
                return false;
            }
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append(this.getClass().getName());
            sb.append('@');
            sb.append(Integer.toHexString(System.identityHashCode(this)));
            sb.append(" (context=");
            sb.append(this._context);
            sb.append(", static_permitions=");
            sb.append(this._statisPermissions);
            sb.append(')');
            return sb.toString();
        }
    }
    
    private static class Loader extends ClassLoader implements GeneratedClassLoader
    {
        private ProtectionDomain domain;
        
        Loader(ClassLoader systemClassLoader, final ProtectionDomain domain) {
            if (systemClassLoader == null) {
                systemClassLoader = ClassLoader.getSystemClassLoader();
            }
            super(systemClassLoader);
            this.domain = domain;
        }
        
        @Override
        public Class<?> defineClass(final String s, final byte[] array) {
            return super.defineClass(s, array, 0, array.length, this.domain);
        }
        
        @Override
        public void linkClass(final Class<?> clazz) {
            this.resolveClass(clazz);
        }
    }
}
