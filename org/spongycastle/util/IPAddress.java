package org.spongycastle.util;

public class IPAddress
{
    private static boolean isMaskValue(final String s, final int n) {
        final boolean b = false;
        try {
            final int int1 = Integer.parseInt(s);
            boolean b2 = b;
            if (int1 >= 0) {
                b2 = b;
                if (int1 <= n) {
                    b2 = true;
                }
            }
            return b2;
        }
        catch (NumberFormatException ex) {
            return false;
        }
    }
    
    public static boolean isValid(final String s) {
        return isValidIPv4(s) || isValidIPv6(s);
    }
    
    public static boolean isValidIPv4(String string) {
        final int length = string.length();
        boolean b = false;
        if (length == 0) {
            return false;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(string);
        sb.append(".");
        string = sb.toString();
        int i = 0;
        int n = 0;
        while (i < string.length()) {
            final int index = string.indexOf(46, i);
            if (index <= i) {
                break;
            }
            if (n == 4) {
                return false;
            }
            try {
                final int int1 = Integer.parseInt(string.substring(i, index));
                if (int1 < 0) {
                    return false;
                }
                if (int1 > 255) {
                    return false;
                }
                i = index + 1;
                ++n;
                continue;
            }
            catch (NumberFormatException ex) {
                return false;
            }
            break;
        }
        if (n == 4) {
            b = true;
        }
        return b;
    }
    
    public static boolean isValidIPv4WithNetmask(final String s) {
        final int index = s.indexOf("/");
        final String substring = s.substring(index + 1);
        boolean b2;
        final boolean b = b2 = false;
        if (index > 0) {
            b2 = b;
            if (isValidIPv4(s.substring(0, index))) {
                if (!isValidIPv4(substring)) {
                    b2 = b;
                    if (!isMaskValue(substring, 32)) {
                        return b2;
                    }
                }
                b2 = true;
            }
        }
        return b2;
    }
    
    public static boolean isValidIPv6(String string) {
        final int length = string.length();
        boolean b = false;
        if (length == 0) {
            return false;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(string);
        sb.append(":");
        string = sb.toString();
        int i = 0;
        int n = 0;
        int n2 = 0;
        while (i < string.length()) {
            final int index = string.indexOf(58, i);
            if (index < i) {
                break;
            }
            if (n == 8) {
                return false;
            }
            Label_0187: {
                if (i != index) {
                    final String substring = string.substring(i, index);
                    if (index == string.length() - 1 && substring.indexOf(46) > 0) {
                        if (!isValidIPv4(substring)) {
                            return false;
                        }
                        ++n;
                        break Label_0187;
                    }
                    else {
                        try {
                            final int int1 = Integer.parseInt(string.substring(i, index), 16);
                            if (int1 < 0 || int1 > 65535) {
                                return false;
                            }
                            break Label_0187;
                        }
                        catch (NumberFormatException ex) {
                            return false;
                        }
                    }
                }
                if (index != 1 && index != string.length() - 1 && n2 != 0) {
                    return false;
                }
                n2 = 1;
            }
            i = index + 1;
            ++n;
        }
        if (n == 8 || n2 != 0) {
            b = true;
        }
        return b;
    }
    
    public static boolean isValidIPv6WithNetmask(final String s) {
        final int index = s.indexOf("/");
        final String substring = s.substring(index + 1);
        boolean b2;
        final boolean b = b2 = false;
        if (index > 0) {
            b2 = b;
            if (isValidIPv6(s.substring(0, index))) {
                if (!isValidIPv6(substring)) {
                    b2 = b;
                    if (!isMaskValue(substring, 128)) {
                        return b2;
                    }
                }
                b2 = true;
            }
        }
        return b2;
    }
    
    public static boolean isValidWithNetMask(final String s) {
        return isValidIPv4WithNetmask(s) || isValidIPv6WithNetmask(s);
    }
}
