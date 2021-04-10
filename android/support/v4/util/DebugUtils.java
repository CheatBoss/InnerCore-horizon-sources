package android.support.v4.util;

public class DebugUtils
{
    public static void buildShortClassTag(final Object o, final StringBuilder sb) {
        String hexString;
        if (o == null) {
            hexString = "null";
        }
        else {
            final String simpleName = o.getClass().getSimpleName();
            String substring = null;
            Label_0072: {
                if (simpleName != null) {
                    substring = simpleName;
                    if (simpleName.length() > 0) {
                        break Label_0072;
                    }
                }
                final String name = o.getClass().getName();
                final int lastIndex = name.lastIndexOf(46);
                substring = name;
                if (lastIndex > 0) {
                    substring = name.substring(lastIndex + 1);
                }
            }
            sb.append(substring);
            sb.append('{');
            hexString = Integer.toHexString(System.identityHashCode(o));
        }
        sb.append(hexString);
    }
}
