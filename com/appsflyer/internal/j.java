package com.appsflyer.internal;

import java.util.*;
import android.util.*;
import java.util.regex.*;

public final class j
{
    public String \u0131;
    public String \u01c3;
    public String \u0269;
    private byte[] \u03b9;
    
    j() {
    }
    
    public j(final String \u0131, final byte[] \u03b9, final String \u0269) {
        this.\u0131 = \u0131;
        this.\u03b9 = \u03b9;
        this.\u0269 = \u0269;
    }
    
    j(final char[] array) {
        final Scanner scanner = new Scanner(new String(array));
        int int1 = 0;
        int int2 = 0;
        while (scanner.hasNextLine()) {
            final String nextLine = scanner.nextLine();
            if (nextLine.startsWith("url=")) {
                this.\u0131 = nextLine.substring(4).trim();
            }
            else if (nextLine.startsWith("version=")) {
                this.\u0269 = nextLine.substring(8).trim();
                final Matcher matcher = Pattern.compile("^(0|[1-9]\\d*)\\.(0|[1-9]\\d*)\\.(0|[1-9]\\d*)(?:-((?:0|[1-9]\\d*|\\d*[a-zA-Z-][0-9a-zA-Z-]*)(?:\\.(?:0|[1-9]\\d*|\\d*[a-zA-Z-][0-9a-zA-Z-]*))*))?(?:\\+([0-9a-zA-Z-]+(?:\\.[0-9a-zA-Z-]+)*))?$").matcher(this.\u0269);
                if (!matcher.matches()) {
                    continue;
                }
                int1 = Integer.parseInt(matcher.group(1));
                int2 = Integer.parseInt(matcher.group(2));
            }
            else {
                if (!nextLine.startsWith("data=")) {
                    continue;
                }
                final String trim = nextLine.substring(5).trim();
                byte[] \u03b9;
                if (int1 <= 4 && int2 < 11) {
                    \u03b9 = trim.getBytes();
                }
                else {
                    \u03b9 = Base64.decode(trim, 2);
                }
                this.\u03b9 = \u03b9;
            }
        }
        scanner.close();
    }
    
    public final byte[] \u01c3() {
        return this.\u03b9;
    }
}
