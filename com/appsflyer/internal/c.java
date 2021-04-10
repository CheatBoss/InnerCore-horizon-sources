package com.appsflyer.internal;

import java.util.regex.*;
import java.text.*;
import android.content.pm.*;
import java.lang.reflect.*;
import com.appsflyer.*;
import android.hardware.*;
import android.content.*;
import android.os.*;
import java.util.*;

public final class c
{
    private static char[] \u0131;
    private static int \u01c3 = 0;
    private static long \u0399 = 0L;
    private static int \u03b9 = 1;
    
    static {
        c.\u0131 = new char[] { (char)(-4373), '\u57f2', (char)(-25406), (char)(-14918), '\u0abd', '\u738d', (char)(-18326), (char)(-7874), '\u264e', '\u6f29', (char)(-11206), '\u1d1d', '\u43f2', (char)(-30468), (char)(-3631), '\u36ae', '\u7f93', (char)(-23342), (char)(-4782), '\u524d', (char)(-25762), (char)(-16351), '\u091e', '\u4e18', (char)(-19211), (char)(-574), '\u22cf', '\u6b95', (char)(-12157), '\u199a', '\u5e7f', (char)(-30905), (char)(-13265), '\u352e', (char)(-13183), (char)(-4580), '\u1347', (char)(-21935), '\u616b', '\u3805', (char)(-2229), (char)(-29155), '\u45f7', '\u1c89', (char)(-9238), (char)(-28004), '\u29bd', (char)(-8060), (char)(-16770), '\u754d', '\u0c67', (char)(-13561), (char)(-32211), '\u5912', '\u5462', (char)(-4745), '\u264e', '\u7f27', (char)(-20423), (char)(-14069), '\u02b4', '\u5baf', (char)(-25463), (char)(-10846), '\u6ee9', (char)(-22591), (char)(-1687), '\u327c', '\u4b0d', (char)(-29575), (char)(-15020), '\u1e10', '\u57b8', (char)(-5945), '\u218c', '\u7aab', (char)(-19504), (char)(-2870), '\u0e74', '\u471f', (char)(-26611), (char)(-11980), '\u6a5a', (char)(-23730), (char)(-7003), '\u3d9b', (char)(-18107), '\\', (char)(-13460), (char)(-28140), '\u5d13', '\u2423', (char)(-4156), (char)(-18800), '\u71fb', '\u3891', (char)(-31786), '\u4a94', '\u144d', (char)(-8370), (char)(-22940), '\u610b', '\u2836', (char)(-3285), (char)(-17691), '\u05e7', (char)(-13070), (char)(-26741), '\u5ea7', '\u19bb', (char)(-7354), '\u78b2', (char)(-15957), '\u0a9b', '\u53e3', (char)(-25372), (char)(-6700), '\u2e33', '\u7767', (char)(-20469), (char)(-1676), '\u427d', (char)(-29883), (char)(-10836), '\u1eac', '\u6795', (char)(-24324), (char)(-5731), '\u32f6', '\u7b3a', (char)(-15329), '\u0d18', '\u5672', (char)(-24763), (char)(-23274), '\u1c0f', (char)(-10433), (char)(-29113), '\u4140', '\u3870', (char)(-3177), (char)(-21821), '\u6da8', '\u24c2', (char)(-24699), '\u56c4', '\u080c', (char)(-15600), (char)(-17875), '\u7d5e', '\u3443', (char)(-4256), (char)(-22904), '\u19be', 'a', (char)(-18056), '\u7248', '\u2b30', (char)(-7113), (char)(-25337), '\u56e0', '\u0fb4', (char)(-14114), (char)(-32349), '\u3aa8', (char)(-3108), (char)(-21155), '\u666c', '\u1f5d', (char)(-15820), (char)(-19538), '\u0aca', (char)(-1475), '\u4320', (char)(-30707), (char)(-11916), '\u1e21', '\u6750', (char)(-21316), (char)(-2589), '\u3293', '\u7bfc', (char)(-16133), '\u09c1', '\u5736', (char)(-25541), '/', (char)(-18059), '\u724d', '\u2b21', (char)(-7120), (char)(-25333), '\u34b1', (char)(-29277), '\u469c', '\u1f81', (char)(-12108), (char)(-22053), '\u6233', '\u3b2b', (char)(-1019), (char)(-19088), 'C', (char)(-18050), '\u7249', '\u2b21', (char)(-7117), (char)(-25315), '\u56f1', '\u0ff7', (char)(-14091), (char)(-32322), '\u3abf', (char)(-3177), (char)(-21128), '\u666a', '\u1f5d', (char)(-10203), (char)(-28402), (char)(-15007), '\u7c50', (char)(-18590), (char)(-4599), '\u211a', '\u582d', (char)(-27773), (char)(-13623), '\u0df8', '\u44c1', (char)(-100), '\u36b0', '\u685b', (char)(-23783), (char)(-9616), '\u1d0c', '\u5424', (char)(-28871), (char)(-14642), '\u79a5', (char)(-20241), (char)(-5240), '\u22b7', '\u65b5', (char)(-24809), (char)(-10634), '\u0972', '\u4001', (char)(-1241), '\u3279', '\u75d6', (char)(-21259), (char)(-6268), '\u1e94', '\u51bb', (char)(-29871), (char)(-15786), '\u0566', (char)(-17395), (char)(-2233), '\u2e77', '\u43a1', (char)(-1355), '\u319e', '\u68cd', (char)(-22544), (char)(-8485), '\u1536', '\u4c3d', (char)(-29926), (char)(-15764), '\u797f', (char)(-20400), (char)(-4465), '\u25a8', '\u5c82', (char)(-25632), (char)(-11569), '\u09d3', '\u402b', (char)(-240), '\u3617', '\u6d67', (char)(-23476), (char)(-7345), (char)(-16733), '\u079e', (char)(-13143), (char)(-27199), '\u5ad3', '\u23fd', (char)(-6127), (char)(-20201), '\u7602', '\u3f43', (char)(-31654), '\u4d7e', '\u138d', (char)(-10083), (char)(-24160), '\u66ef', '\u2ff8', (char)(-2827), (char)(-17143), '\u0232', (char)(-13524), (char)(-28601), '\u596b', '\u1e74', (char)(-17177), '\u05d6', (char)(-12572), (char)(-26737), '\u589c', '\u21ab', (char)(-5627), (char)(-19630), '\u747f', '\u3d11', (char)(-31214), '\u4f38', '\u11cc', (char)(-9569), (char)(-23577), '\u648e', '\u2da7', (char)(-2373), (char)(-16568), '`', (char)(-13971), (char)(-28150), '\u5b21', '\u1c7b', (char)(-6436), (char)(-20510), '\u70e9', '\u399b', (char)(-32090), '\u4bbb', '\u0c15', (char)(-10884), (char)(-25080), '\u6703', '\u2825', (char)(-3453), (char)(-17444), '\u7cf7', (char)(-14970), (char)(-29026), '\u57a1', '\u1853', (char)(-7852), (char)(-21892), '\u7307', '\u3445', (char)(-651) };
        c.\u0399 = 5583085843407419670L;
    }
    
    c() {
    }
    
    private static String \u0131(final Context context) {
        int n;
        if (System.getProperties().containsKey(\u03b9(172, '\ufa57', 14).intern())) {
            n = 46;
        }
        else {
            n = 67;
        }
        if (n != 67) {
            c.\u03b9 = (c.\u01c3 + 51) % 128;
            try {
                final Matcher matcher = Pattern.compile(\u03b9(192, '\u349f', 10).intern()).matcher(context.getCacheDir().getPath().replace(\u03b9(186, '\0', 6).intern(), ""));
                int n2;
                if (matcher.find()) {
                    n2 = 41;
                }
                else {
                    n2 = 99;
                }
                if (n2 != 41) {
                    return null;
                }
                c.\u01c3 = (c.\u03b9 + 89) % 128;
                return matcher.group(1);
            }
            catch (Exception ex) {
                if (ai.\u0269 == null) {
                    ai.\u0269 = new ai();
                    c.\u03b9 = (c.\u01c3 + 87) % 128;
                }
                final ai \u0269 = ai.\u0269;
                final String intern = \u03b9(202, '\0', 17).intern();
                final StringBuilder sb = new StringBuilder();
                sb.append(\u03b9(219, '\uc527', 41).intern());
                sb.append(ex);
                \u0269.\u0131(null, intern, sb.toString());
            }
        }
        return null;
    }
    
    public static String \u0131(final Context context, final long n) {
        final StringBuilder sb = new StringBuilder();
        final StringBuilder sb2 = new StringBuilder();
        final StringBuilder sb3 = new StringBuilder();
        String s;
        if (\u0131(\u03b9(0, '\uee8a', 34).intern())) {
            final int n2 = c.\u01c3 + 73;
            c.\u03b9 = n2 % 128;
            if (n2 % 2 == 0) {
                s = \u03b9(113, '\uccb0', 0);
            }
            else {
                s = \u03b9(34, '\uccb0', 1);
            }
        }
        else {
            s = \u03b9(35, '\uee2c', 1);
        }
        sb2.append(s.intern());
        final StringBuilder sb4 = new StringBuilder();
        final String packageName = context.getPackageName();
        final String \u03b9 = \u0399(packageName);
        sb2.append(\u03b9(34, '\uccb0', 1).intern());
        sb4.append(\u03b9);
        String s2;
        if (\u0131(context) != null) {
            s2 = \u03b9(34, '\uccb0', 1);
        }
        else {
            s2 = \u03b9(35, '\uee2c', 1);
        }
        sb2.append(s2.intern());
        sb4.append(packageName);
        final String \u01c3 = \u01c3(context);
        int n3;
        if (\u01c3 == null) {
            n3 = 94;
        }
        else {
            n3 = 53;
        }
        if (n3 != 53) {
            sb2.append(\u03b9(35, '\uee2c', 1).intern());
            sb4.append(packageName);
        }
        else {
            sb2.append(\u03b9(34, '\uccb0', 1).intern());
            sb4.append(\u01c3);
        }
        sb4.append(\u0399(context, packageName));
        sb.append(sb4.toString());
        try {
            sb.append(new SimpleDateFormat(\u03b9(36, '\u133e', 18).intern(), Locale.US).format(new Date(context.getPackageManager().getPackageInfo(context.getPackageName(), 0).firstInstallTime)));
            sb.append(n);
            String s3;
            if (\u0131(\u03b9(86, '\ub924', 25).intern()) ^ true) {
                s3 = \u03b9(35, '\uee2c', 1).intern();
                c.\u01c3 = (c.\u03b9 + 91) % 128;
            }
            else {
                s3 = \u03b9(34, '\uccb0', 1).intern();
            }
            sb3.append(s3);
            int n4;
            if (\u0131(\u03b9(111, '\u78d3', 23).intern())) {
                n4 = 4;
            }
            else {
                n4 = 47;
            }
            String s4;
            if (n4 != 4) {
                s4 = \u03b9(35, '\uee2c', 1);
            }
            else {
                s4 = \u03b9(34, '\uccb0', 1);
            }
            sb3.append(s4.intern());
            String s5;
            if (\u0131(\u03b9(134, '\ua577', 20).intern())) {
                c.\u01c3 = (c.\u03b9 + 105) % 128;
                s5 = \u03b9(34, '\uccb0', 1);
            }
            else {
                s5 = \u03b9(35, '\uee2c', 1);
            }
            sb3.append(s5.intern());
            String s6;
            if (!\u0131(\u03b9(154, '\0', 15).intern())) {
                s6 = \u03b9(35, '\uee2c', 1);
            }
            else {
                final int n5 = c.\u03b9 + 91;
                c.\u01c3 = n5 % 128;
                int n6;
                if (n5 % 2 != 0) {
                    n6 = 52;
                }
                else {
                    n6 = 98;
                }
                if (n6 != 52) {
                    s6 = \u03b9(34, '\uccb0', 1);
                }
                else {
                    s6 = \u03b9(97, '\uccb0', 0);
                }
            }
            sb3.append(s6.intern());
            final String \u01c32 = z.\u01c3(z.\u0269(sb.toString()));
            final String string = sb2.toString();
            final StringBuilder sb5 = new StringBuilder(\u01c32);
            sb5.setCharAt(17, Integer.toString(Integer.parseInt(string, 2), 16).charAt(0));
            final String string2 = sb5.toString();
            final String string3 = sb3.toString();
            final StringBuilder sb6 = new StringBuilder(string2);
            sb6.setCharAt(27, Integer.toString(Integer.parseInt(string3, 2), 16).charAt(0));
            return \u03b9(sb6.toString(), n);
        }
        catch (PackageManager$NameNotFoundException ex) {
            return \u03b9(54, '\u5400', 32).intern();
        }
    }
    
    private static boolean \u0131(final String s) {
        c.\u03b9 = (c.\u01c3 + 89) % 128;
        try {
            Class.forName(s);
            c.\u01c3 = (c.\u03b9 + 49) % 128;
            return true;
        }
        catch (ClassNotFoundException ex) {
            return false;
        }
    }
    
    private static String \u01c3(final Context context) {
        try {
            final String packageName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).packageName;
            c.\u03b9 = (c.\u01c3 + 83) % 128;
            c.\u01c3 = (c.\u03b9 + 79) % 128;
            return packageName;
        }
        catch (PackageManager$NameNotFoundException ex) {
            return null;
        }
    }
    
    private static String \u0399(final Context context, final String s) {
        Block_0: {
            break Block_0;
        Label_0462:
            while (true) {
                int i;
                do {
                    ai ai = null;
                    String s2 = null;
                    String[] array = null;
                    Label_0444: {
                        Label_0459: {
                            Label_0099: {
                                break Label_0099;
                                c.\u01c3 = (c.\u03b9 + 27) % 128;
                                final PackageManager packageManager = context.getPackageManager();
                                try {
                                    final Iterator iterator = ((List)PackageManager.class.getDeclaredMethod(\u03b9(260, '\u43c6', 24).intern(), Integer.TYPE).invoke(packageManager, 0)).iterator();
                                    c.\u01c3 = (c.\u03b9 + 1) % 128;
                                    Label_0084: {
                                        if (iterator.hasNext()) {
                                            i = 55;
                                            continue Label_0462;
                                        }
                                    }
                                    break Label_0459;
                                    while (true) {
                                        final int n = c.\u01c3 + 101;
                                        c.\u03b9 = n % 128;
                                        Block_6: {
                                            break Block_6;
                                            Label_0158:
                                            return Boolean.TRUE.toString();
                                        }
                                        final String string = Boolean.TRUE.toString();
                                        try {
                                            return string;
                                        }
                                        finally {}
                                        return Boolean.TRUE.toString();
                                        continue;
                                    }
                                }
                                // iftrue(Label_0158:, n % 2 != 0)
                                // iftrue(Label_0084:, !(ApplicationInfo)iterator.next().packageName.equals((Object)s))
                                catch (InvocationTargetException ex) {
                                    if (com.appsflyer.internal.ai.\u0269 == null) {
                                        com.appsflyer.internal.ai.\u0269 = new ai();
                                    }
                                    ai = com.appsflyer.internal.ai.\u0269;
                                    s2 = \u03b9(284, '\ubee0', 24).intern();
                                    final StringBuilder sb = new StringBuilder();
                                    sb.append(\u03b9(308, '\ubca1', 47).intern());
                                    sb.append(ex);
                                    array = new String[] { sb.toString() };
                                }
                                catch (NoSuchMethodException ex2) {
                                    if (com.appsflyer.internal.ai.\u0269 == null) {
                                        com.appsflyer.internal.ai.\u0269 = new ai();
                                        c.\u03b9 = (c.\u01c3 + 25) % 128;
                                    }
                                    ai = com.appsflyer.internal.ai.\u0269;
                                    s2 = \u03b9(284, '\ubee0', 24).intern();
                                    final StringBuilder sb2 = new StringBuilder();
                                    sb2.append(\u03b9(308, '\ubca1', 47).intern());
                                    sb2.append(ex2);
                                    array = new String[] { sb2.toString() };
                                }
                                catch (IllegalAccessException ex3) {
                                    if (com.appsflyer.internal.ai.\u0269 == null) {
                                        com.appsflyer.internal.ai.\u0269 = new ai();
                                    }
                                    ai = com.appsflyer.internal.ai.\u0269;
                                    s2 = \u03b9(284, '\ubee0', 24).intern();
                                    final StringBuilder sb3 = new StringBuilder();
                                    sb3.append(\u03b9(308, '\ubca1', 47).intern());
                                    sb3.append(ex3);
                                    array = new String[] { sb3.toString() };
                                }
                            }
                            break Label_0444;
                        }
                        i = 7;
                        continue Label_0462;
                    }
                    ai.\u0131(null, s2, array);
                    return Boolean.FALSE.toString();
                } while (i == 55);
                break;
            }
        }
        return Boolean.FALSE.toString();
    }
    
    private static String \u0399(final String s) {
        c.\u01c3 = (c.\u03b9 + 65) % 128;
        String string = s;
        if (s.contains(\u03b9(169, '\uc21a', 1).intern())) {
            final String[] split = s.split(\u03b9(170, '\ub3f2', 2).intern());
            final int length = split.length;
            final StringBuilder sb = new StringBuilder();
            final int n = length - 1;
            sb.append(split[n]);
            sb.append(\u03b9(169, '\uc21a', 1).intern());
            c.\u03b9 = (c.\u01c3 + 103) % 128;
            for (int i = 1; i < n; ++i) {
                c.\u03b9 = (c.\u01c3 + 61) % 128;
                sb.append(split[i]);
                sb.append(\u03b9(169, '\uc21a', 1).intern());
            }
            sb.append(split[0]);
            string = sb.toString();
        }
        return string;
    }
    
    private static String \u03b9(final int n, final char c, final int n2) {
        final char[] array = new char[n2];
        int n3 = 0;
        while (true) {
            int n4;
            if (n3 < n2) {
                n4 = 74;
            }
            else {
                n4 = 89;
            }
            if (n4 == 89) {
                break;
            }
            final int n5 = c.\u03b9 + 85;
            c.\u01c3 = n5 % 128;
            if (n5 % 2 != 0) {
                array[n3] = (char)(((long)c.\u0131[n + n3] | n3 % c.\u0399) * c);
                n3 += 37;
            }
            else {
                array[n3] = (char)((long)c.\u0131[n + n3] ^ n3 * c.\u0399 ^ (long)c);
                ++n3;
            }
            c.\u01c3 = (c.\u03b9 + 109) % 128;
        }
        return new String(array);
    }
    
    private static String \u03b9(final String s, final Long n) {
        if (s != null) {
            c.\u01c3 = (c.\u03b9 + 125) % 128;
            if (n != null) {
                c.\u03b9 = (c.\u01c3 + 59) % 128;
                if (s.length() == 32) {
                    final StringBuilder sb = new StringBuilder(s);
                    final String string = n.toString();
                    long n2 = 0L;
                    int i = 0;
                    int n3 = 0;
                    while (i < string.length()) {
                        n3 += Character.getNumericValue(string.charAt(i));
                        ++i;
                    }
                    final String hexString = Integer.toHexString(n3);
                    sb.replace(7, hexString.length() + 7, hexString);
                    int n4 = 0;
                    long n5;
                    while (true) {
                        n5 = n2;
                        if (n4 >= sb.length()) {
                            break;
                        }
                        final long n6 = Character.getNumericValue(sb.charAt(n4));
                        ++n4;
                        n2 += n6;
                    }
                    while (n5 > 100L) {
                        n5 %= 100L;
                    }
                    sb.insert(23, (int)n5);
                    int n7;
                    if (n5 < 10L) {
                        n7 = 81;
                    }
                    else {
                        n7 = 96;
                    }
                    if (n7 != 96) {
                        sb.insert(23, \u03b9(35, '\uee2c', 1).intern());
                    }
                    return sb.toString();
                }
            }
        }
        return \u03b9(54, '\u5400', 32).intern();
    }
    
    public static final class a extends HashMap<String, Object>
    {
        private static long \u01c3 = 0L;
        private static int \u0399 = 0;
        private static char[] \u03b9;
        private static int \u0406 = 1;
        private final Map<String, Object> \u0131;
        private final Context \u0269;
        
        static {
            a.\u03b9 = new char[] { 'a', (char)(-3642), (char)(-7393), (char)(-11116), (char)(-14615), (char)(-18355), (char)(-22107), (char)(-25837), (char)(-29324), '\u7ec1', '\u702d', '\u6190', 'b', (char)(-3630), (char)(-7391), (char)(-11122), (char)(-14620), 'N', (char)(-3601), (char)(-7393), (char)(-11102), (char)(-14638), (char)(-18335), (char)(-22130), (char)(-25820), 'k', (char)(-3643), (char)(-7386), 'f', (char)(-3647), (char)(-7383), (char)(-11124), (char)(-14619), (char)(-18364), (char)(-22048), (char)(-25849), (char)(-29339), '\u7ece', '\u7025', '\u6192', '\u53e1', '\u4554', '\u36a9', '\u280e', '\u1a67', '\u0b80', (char)(-725), (char)(-4475), (char)(-7962), (char)(-11776), (char)(-15445), (char)(-19195), (char)(-22663), (char)(-26496), (char)(-30153), '\u7b89', '\u6df4', '\u5f48', '\u50e0', '\u4205', '\u3478', '\u25c3', '\u1725', '\u0890', (char)(-1292), (char)(-5047), (char)(-8785), (char)(-12530), (char)(-16070), (char)(-19840), '\u38c8', (char)(-13978), (char)(-9339), (char)(-4998), (char)(-486), (char)(-32582), (char)(-28326), 'f', (char)(-3639), (char)(-7374), (char)(-11117), (char)(-14604), (char)(-18324), (char)(-22111), (char)(-25835), (char)(-29330), '\u7ec3', '\u7028', '\u61a4', '\u53e1', '\u4554', '\u36a5', '5', (char)(-3698), (char)(-7308), (char)(-11058), (char)(-14671), '\u5fe9', (char)(-20970), (char)(-17177), (char)(-29942), (char)(-26258), '\u37fe', (char)(-14759), (char)(-11087), (char)(-7404), (char)(-3715), (char)(-28708), (char)(-24968), (char)(-21345), (char)(-17667), '\u4956', '\u47bd', '\u560a', '\u6479', '\u72cc', '\u0131', '\u1f96', '\u2dff', '\u3c18', (char)(-13645), (char)(-9955), (char)(-10370), (char)(-6760), (char)(-3026), (char)(-32103), (char)(-28428), (char)(-20659), (char)(-16963), '\u4c58', '\u5a6f', '\u68d1', '\u672c', '\u7590', '\u03b8', '\u125d', '\u20a0', '\u3f1b', (char)(-12931), (char)(-9272), (char)(-5588), (char)(-1903), (char)(-2313), (char)(-31402), (char)(-27678), (char)(-23976), (char)(-13712), '\u3bd3', '\u2930', '\u1e97', '\u0cf3', '\u7257', '\u63e2', '\u5147', '\u4726', (char)(-19326), (char)(-17866), (char)(-21557), (char)(-26205), (char)(-28844), (char)(-800), (char)(-7614), (char)(-12253), (char)(-15990), 'a', (char)(-3634), (char)(-7388), (char)(-11118), (char)(-14609), (char)(-18359), (char)(-22108), (char)(-25778), (char)(-29335), '\u7ece', '\u7034', '\u6185', '\u53ee', '\u4554', '\u36ee', '\u2801', '\u1a63', '\u0bd4', (char)(-727), (char)(-4465), (char)(-7954), (char)(-11762), (char)(-15486), (char)(-19167), (char)(-22700), (char)(-26380), (char)(-30203), '\u7bb2', '\u6dd9', '\u5f7f', '\u5083', '\u4228', '\u3441', '\u25ee', '\u1707', '\u08a5', (char)(-1340), 't', (char)(-3643), (char)(-7379), (char)(-11120), (char)(-14619), (char)(-18350), (char)(-22111), (char)(-25836), (char)(-29323), '\u7ed2', '\u7025', (char)(-784), '\u0d10', '\u1ffe', '\u686e', (char)(-26152), (char)(-29901), (char)(-17266), (char)(-20750), (char)(-12209), '\u5dd5', (char)(-32304), '\u702e', '&', (char)(-3629), '&', (char)(-3632), 'f', (char)(-3696), (char)(-7375), (char)(-11056), (char)(-14607), (char)(-18415), (char)(-22096), (char)(-25775), (char)(-29328), '\u7e92', '\u7072', '\u6183', '\u53e8', '\u4554', '\u36a1', '\u280d' };
            a.\u01c3 = -8771784815112425056L;
        }
        
        public a(final Map<String, Object> \u0131, final Context \u0269) {
            this.\u0131 = \u0131;
            this.\u0269 = \u0269;
            ((AbstractMap<String, String>)this).put(this.\u0399(), this.\u0131());
        }
        
        private String \u0131() {
            String s;
            try {
                final String string = this.\u0131.get(\u0269(0, '\0', 12).intern()).toString();
                final String string2 = this.\u0131.get(\u0269(77, '\0', 15).intern()).toString();
                final String replaceAll = \u0269(92, '\0', 5).intern().replaceAll(\u0269(97, '\u5fc4', 5).intern(), "");
                final StringBuilder sb = new StringBuilder();
                sb.append(string);
                sb.append(string2);
                sb.append(replaceAll);
                final String \u0269 = z.\u0269(sb.toString());
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("");
                sb2.append(\u0269.substring(0, 16));
                s = sb2.toString();
            }
            catch (Exception ex) {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append(\u0269(102, '\u3798', 44).intern());
                sb3.append(ex);
                AFLogger.afRDLog(sb3.toString());
                final StringBuilder sb4 = new StringBuilder();
                sb4.append("");
                sb4.append(\u0269(146, '\uca12', 18).intern());
                s = sb4.toString();
            }
            while (true) {
                while (true) {
                    Label_0783: {
                        try {
                            final Intent registerReceiver = this.\u0269.registerReceiver((BroadcastReceiver)null, new IntentFilter(\u0269(164, '\0', 37).intern()));
                            int n = -2700;
                            if (registerReceiver == null) {
                                final String nativeLibraryDir = this.\u0269.getApplicationInfo().nativeLibraryDir;
                                int n2;
                                if (nativeLibraryDir != null) {
                                    n2 = 48;
                                }
                                else {
                                    n2 = 86;
                                }
                                int n5 = 0;
                                Label_0481: {
                                    if (n2 == 48) {
                                        a.\u0399 = (a.\u0406 + 15) % 128;
                                        if (nativeLibraryDir.contains(\u0269(212, '\ufc88', 3).intern())) {
                                            final int n3 = a.\u0406 + 111;
                                            a.\u0399 = n3 % 128;
                                            int n4;
                                            if (n3 % 2 != 0) {
                                                n4 = 52;
                                            }
                                            else {
                                                n4 = 79;
                                            }
                                            if (n4 == 79) {
                                                n5 = 1;
                                                break Label_0481;
                                            }
                                        }
                                    }
                                    n5 = 0;
                                }
                                final int size = ((SensorManager)this.\u0269.getSystemService(\u0269(215, '\u681d', 6).intern())).getSensorList(-1).size();
                                final StringBuilder sb5 = new StringBuilder();
                                sb5.append(\u0269(221, '\u5db7', 1).intern());
                                sb5.append(n);
                                sb5.append(\u0269(222, '\u81f6', 2).intern());
                                sb5.append(n5);
                                sb5.append(\u0269(224, '\0', 2).intern());
                                sb5.append(size);
                                sb5.append(\u0269(226, '\0', 2).intern());
                                sb5.append(this.\u0131.size());
                                final String string3 = sb5.toString();
                                final StringBuilder sb6 = new StringBuilder();
                                sb6.append(s);
                                sb6.append(c.\u03b9(c.\u0399(c.\u0269(string3))));
                                final String string4 = sb6.toString();
                                a.\u0399 = (a.\u0406 + 13) % 128;
                                return string4;
                            }
                            final int n6 = a.\u0406 + 51;
                            a.\u0399 = n6 % 128;
                            if (n6 % 2 != 0) {
                                n = registerReceiver.getIntExtra(\u0269(15579, '\0', 109).intern(), 25996);
                                break Label_0783;
                            }
                            n = registerReceiver.getIntExtra(\u0269(201, '\0', 11).intern(), -2700);
                            break Label_0783;
                        }
                        catch (Exception ex2) {
                            final StringBuilder sb7 = new StringBuilder();
                            sb7.append(\u0269(102, '\u3798', 44).intern());
                            sb7.append(ex2);
                            AFLogger.afRDLog(sb7.toString());
                            final StringBuilder sb8 = new StringBuilder();
                            sb8.append(s);
                            sb8.append(\u0269(228, '\0', 16).intern());
                            return sb8.toString();
                        }
                    }
                    continue;
                }
            }
        }
        
        private static String \u0269(final int n, final char c, final int n2) {
            a.\u0406 = (a.\u0399 + 19) % 128;
            final char[] array = new char[n2];
            for (int i = 0; i < n2; ++i, a.\u0406 = (a.\u0399 + 125) % 128) {
                a.\u0399 = (a.\u0406 + 95) % 128;
                array[i] = (char)((long)a.\u03b9[n + i] ^ i * a.\u01c3 ^ (long)c);
            }
            return new String(array);
        }
        
        private String \u0399() {
            String string;
            String string2;
            String s;
            int n;
            StringBuilder sb;
            StringBuilder \u03b9;
            int length;
            int n2 = 0;
            int n3;
            StringBuilder sb2;
            Label_0213_Outer:Label_0234_Outer:
            while (true) {
                a.\u0399 = (a.\u0406 + 49) % 128;
                while (true) {
                    while (true) {
                    Label_0322:
                        while (true) {
                            Label_0317: {
                                try {
                                    string = Integer.toString(Build$VERSION.SDK_INT);
                                    string2 = this.\u0131.get(\u0269(0, '\0', 12).intern()).toString();
                                    s = this.\u0131.get(\u0269(12, '\0', 5).intern()).toString();
                                    if (s != null) {
                                        break Label_0317;
                                    }
                                    n = 1;
                                    if (n != 0) {
                                        s = \u0269(17, '\0', 8).intern();
                                        a.\u0399 = (a.\u0406 + 117) % 128;
                                    }
                                    sb = new StringBuilder(string2);
                                    sb.reverse();
                                    \u03b9 = \u0399(string, s, sb.toString());
                                    length = \u03b9.length();
                                    if ((n2 = length) > 4) {
                                        n3 = a.\u0406 + 73;
                                        a.\u0399 = n3 % 128;
                                        if (n3 % 2 != 0) {
                                            \u03b9.delete(3, length);
                                        }
                                        else {
                                            \u03b9.delete(4, length);
                                        }
                                        \u03b9.insert(0, \u0269(25, '\0', 3).intern());
                                        return \u03b9.toString();
                                    }
                                    break Label_0322;
                                    ++n2;
                                    \u03b9.append('1');
                                    a.\u0399 = (a.\u0406 + 67) % 128;
                                    break Label_0322;
                                }
                                catch (Exception ex) {
                                    sb2 = new StringBuilder();
                                    sb2.append(\u0269(28, '\0', 42).intern());
                                    sb2.append(ex);
                                    AFLogger.afRDLog(sb2.toString());
                                    return \u0269(70, '\u38a3', 7).intern();
                                }
                            }
                            n = 0;
                            continue Label_0213_Outer;
                        }
                        if (n2 >= 4) {
                            continue Label_0234_Outer;
                        }
                        break;
                    }
                    continue;
                }
            }
        }
        
        private static StringBuilder \u0399(final String... array) throws Exception {
            final ArrayList<Integer> list = (ArrayList<Integer>)new ArrayList<Comparable>();
            int n = 0;
            while (true) {
                int n2;
                if (n < 3) {
                    n2 = 22;
                }
                else {
                    n2 = 13;
                }
                if (n2 == 13) {
                    break;
                }
                list.add(array[n].length());
                ++n;
            }
            Collections.sort((List<Comparable>)list);
            final int intValue = list.get(0).intValue();
            final StringBuilder sb = new StringBuilder();
            for (int i = 0; i < intValue; ++i) {
                a.\u0406 = (a.\u0399 + 105) % 128;
                Number value = null;
                for (int n3 = 0; n3 < 3; ++n3, a.\u0406 = (a.\u0399 + 87) % 128) {
                    int char1 = array[n3].charAt(i);
                    if (value == null) {
                        a.\u0406 = (a.\u0399 + 95) % 128;
                    }
                    else {
                        char1 ^= value.intValue();
                    }
                    value = char1;
                }
                sb.append(Integer.toHexString(value.intValue()));
            }
            return sb;
        }
    }
    
    public static final class c
    {
        private final Object \u0131;
        public String \u0269;
        private long \u0399;
        
        c() {
        }
        
        public c(final long \u03b9, final String \u0269) {
            this.\u0131 = new Object();
            this.\u0399 = 0L;
            this.\u0269 = "";
            this.\u0399 = \u03b9;
            this.\u0269 = \u0269;
        }
        
        private boolean \u0131(final long \u03b9, final String \u0269) {
            final Object \u0131 = this.\u0131;
            // monitorenter(\u0131)
            if (\u0269 != null) {
                try {
                    if (!\u0269.equals(this.\u0269) && this.\u03b9(\u03b9)) {
                        this.\u0399 = \u03b9;
                        this.\u0269 = \u0269;
                        return true;
                    }
                }
                finally {
                }
                // monitorexit(\u0131)
            }
            // monitorexit(\u0131)
            return false;
        }
        
        static byte[] \u0269(final String s) throws Exception {
            return s.getBytes();
        }
        
        static byte[] \u0399(final byte[] array) throws Exception {
            for (int i = 0; i < array.length; ++i) {
                array[i] ^= (byte)(i % 2 + 42);
            }
            return array;
        }
        
        public static c \u03b9(final String s) {
            if (s == null) {
                return new c(0L, "");
            }
            final String[] split = s.split(",");
            if (split.length < 2) {
                return new c(0L, "");
            }
            return new c(Long.parseLong(split[0]), split[1]);
        }
        
        static String \u03b9(final byte[] array) throws Exception {
            final StringBuilder sb = new StringBuilder();
            for (int length = array.length, i = 0; i < length; ++i) {
                String s2;
                final String s = s2 = Integer.toHexString(array[i]);
                if (s.length() == 1) {
                    s2 = "0".concat(String.valueOf(s));
                }
                sb.append(s2);
            }
            return sb.toString();
        }
        
        private boolean \u03b9(final long n) {
            return n - this.\u0399 > 2000L;
        }
        
        @Override
        public final String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append(this.\u0399);
            sb.append(",");
            sb.append(this.\u0269);
            return sb.toString();
        }
        
        public final boolean \u0269(final c c) {
            return this.\u0131(c.\u0399, c.\u0269);
        }
    }
}
