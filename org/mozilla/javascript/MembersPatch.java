package org.mozilla.javascript;

import com.zhekasmirnov.horizon.runtime.logger.*;
import java.lang.reflect.*;
import java.util.*;

public class MembersPatch
{
    private static HashMap<String, String> overrides;
    
    static {
        MembersPatch.overrides = new HashMap<String, String>();
    }
    
    public static void addOverride(final String s, final String s2) {
        final StringBuilder sb = new StringBuilder();
        sb.append("Adding override ");
        sb.append(s);
        sb.append(" -> ");
        sb.append(s2);
        Logger.debug("MEMBERS_PATCH", sb.toString());
        MembersPatch.overrides.put(s, s2);
    }
    
    static String getOverride(final String s) {
        final String s2 = MembersPatch.overrides.get(s);
        if (s2 != null) {
            return s2;
        }
        return s;
    }
    
    static Method override(Class<?> string, final Method method) {
        final StringBuilder sb = new StringBuilder();
        sb.append(((Class)string).getName());
        sb.append(".");
        sb.append(method.getName());
        string = sb.toString();
        if (MembersPatch.overrides.containsKey(string)) {
            final String s = MembersPatch.overrides.get(string);
            final int lastIndex = s.lastIndexOf(".");
            try {
                final Method method2 = Class.forName(s.substring(0, lastIndex)).getMethod(s.substring(lastIndex + 1), method.getParameterTypes());
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("Successfully overrided method ");
                sb2.append(string);
                Logger.debug("MEMBERS_PATCH", sb2.toString());
                return method2;
            }
            catch (NoSuchMethodException ex) {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("Could't override method ");
                sb3.append(string);
                sb3.append(" with arguments types ");
                sb3.append(Arrays.toString(method.getParameterTypes()));
                Logger.debug("MEMBERS_PATCH", sb3.toString());
                return method;
            }
            catch (ClassNotFoundException | SecurityException ex2) {
                final Object o;
                throw new RuntimeException("Exception occured while trying to override existing method. ", (Throwable)o);
            }
        }
        return method;
    }
}
