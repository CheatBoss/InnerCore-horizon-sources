package bo.app;

import com.appboy.support.*;
import java.io.*;

public class eh
{
    private static final String a;
    
    static {
        a = AppboyLogger.getAppboyLogTag(eh.class);
    }
    
    public static String a(String s, String s2) {
        final Serializable s3 = null;
        Serializable line;
        final Serializable s4 = line = null;
        try {
            try {
                final Process exec = Runtime.getRuntime().exec(new String[] { "/system/bin/getprop", (String)s });
                line = s4;
                final BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(exec.getInputStream(), "UTF-8"));
                try {
                    try {
                        line = bufferedReader.readLine();
                        try {
                            exec.destroy();
                            try {
                                bufferedReader.close();
                                return (String)line;
                            }
                            catch (Exception ex2) {}
                        }
                        catch (Exception s4) {
                            s2 = line;
                        }
                    }
                    finally {}
                }
                catch (Exception ex3) {}
            }
            finally {
                s = line;
            }
        }
        catch (Exception s4) {
            s = s3;
        }
        AppboyLogger.e(eh.a, "Caught exception while trying to read Braze logger tag from system properties.", (Throwable)s4);
        Serializable s5 = s2;
        if (s != null) {
            try {
                ((BufferedReader)s).close();
                return (String)s2;
            }
            catch (Exception ex4) {}
            AppboyLogger.e(eh.a, "Caught exception while trying to close system properties reader.", (Throwable)s);
            s5 = s2;
        }
        return (String)s5;
        if (s != null) {
            try {
                ((BufferedReader)s).close();
            }
            catch (Exception ex) {
                AppboyLogger.e(eh.a, "Caught exception while trying to close system properties reader.", ex);
            }
        }
    }
}
