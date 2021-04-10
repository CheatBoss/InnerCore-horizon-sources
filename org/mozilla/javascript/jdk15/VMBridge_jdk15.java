package org.mozilla.javascript.jdk15;

import org.mozilla.javascript.jdk13.*;
import java.util.*;
import org.mozilla.javascript.*;
import java.lang.reflect.*;

public class VMBridge_jdk15 extends VMBridge_jdk13
{
    public VMBridge_jdk15() throws SecurityException, InstantiationException {
        try {
            Method.class.getMethod("isVarArgs", (Class<?>[])null);
        }
        catch (NoSuchMethodException ex) {
            throw new InstantiationException(ex.getMessage());
        }
    }
    
    @Override
    public Iterator<?> getJavaIterator(final Context context, final Scriptable scriptable, final Object o) {
        if (o instanceof Wrapper) {
            final Object unwrap = ((Wrapper)o).unwrap();
            Iterator<?> iterator = null;
            if (unwrap instanceof Iterator) {
                iterator = (Iterator<?>)unwrap;
            }
            if (unwrap instanceof Iterable) {
                iterator = ((Iterable<?>)unwrap).iterator();
            }
            return iterator;
        }
        return null;
    }
    
    public boolean isVarArgs(final Member member) {
        if (member instanceof Method) {
            return ((Method)member).isVarArgs();
        }
        return member instanceof Constructor && ((Constructor)member).isVarArgs();
    }
}
