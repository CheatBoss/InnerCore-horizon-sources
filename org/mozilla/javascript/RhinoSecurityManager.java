package org.mozilla.javascript;

public class RhinoSecurityManager extends SecurityManager
{
    protected Class<?> getCurrentScriptClass() {
        final Class[] classContext = this.getClassContext();
        for (int length = classContext.length, i = 0; i < length; ++i) {
            final Class clazz = classContext[i];
            if ((clazz != InterpretedFunction.class && NativeFunction.class.isAssignableFrom(clazz)) || PolicySecurityController.SecureCaller.class.isAssignableFrom(clazz)) {
                return (Class<?>)clazz;
            }
        }
        return null;
    }
}
