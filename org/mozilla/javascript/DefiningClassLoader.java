package org.mozilla.javascript;

public class DefiningClassLoader extends ClassLoader implements GeneratedClassLoader
{
    private final ClassLoader parentLoader;
    
    public DefiningClassLoader() {
        this.parentLoader = this.getClass().getClassLoader();
    }
    
    public DefiningClassLoader(final ClassLoader parentLoader) {
        this.parentLoader = parentLoader;
    }
    
    @Override
    public Class<?> defineClass(final String s, final byte[] array) {
        return super.defineClass(s, array, 0, array.length, SecurityUtilities.getProtectionDomain(this.getClass()));
    }
    
    @Override
    public void linkClass(final Class<?> clazz) {
        this.resolveClass(clazz);
    }
    
    public Class<?> loadClass(final String s, final boolean b) throws ClassNotFoundException {
        Class<?> clazz;
        if ((clazz = this.findLoadedClass(s)) == null) {
            if (this.parentLoader != null) {
                clazz = this.parentLoader.loadClass(s);
            }
            else {
                clazz = this.findSystemClass(s);
            }
        }
        if (b) {
            this.resolveClass(clazz);
        }
        return clazz;
    }
}
