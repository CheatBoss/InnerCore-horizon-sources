package com.appsflyer.internal;

import java.io.*;
import java.lang.ref.*;
import java.security.*;
import com.appsflyer.*;
import android.content.*;

public final class ae
{
    private static String \u01c3;
    
    ae() {
    }
    
    private static String \u0399(File file) {
        Serializable s = null;
        RandomAccessFile randomAccessFile = null;
        final IOException ex = null;
        Object o = null;
        Label_0112: {
            Throwable t;
            try {
                try {
                    final RandomAccessFile randomAccessFile2 = new RandomAccessFile(file, "r");
                    try {
                        try {
                            file = (File)(Object)new byte[(int)randomAccessFile2.length()];
                            try {
                                randomAccessFile2.readFully((byte[])(Object)file);
                                randomAccessFile2.close();
                                try {
                                    randomAccessFile2.close();
                                    break Label_0112;
                                }
                                catch (IOException s) {
                                    AFLogger.afErrorLog("Exception while trying to close the InstallationFile", (Throwable)s);
                                    break Label_0112;
                                }
                            }
                            catch (IOException randomAccessFile) {
                                s = file;
                            }
                        }
                        finally {}
                    }
                    catch (IOException ex4) {
                        s = ex;
                    }
                    randomAccessFile = randomAccessFile2;
                }
                finally {
                    t = (Throwable)s;
                }
            }
            catch (IOException o) {
                t = null;
            }
            AFLogger.afErrorLog("Exception while reading InstallationFile: ", (Throwable)o);
            if (randomAccessFile != null) {
                try {
                    randomAccessFile.close();
                }
                catch (IOException ex2) {
                    AFLogger.afErrorLog("Exception while trying to close the InstallationFile", ex2);
                }
            }
            o = t;
        }
        if (o == null) {
            o = new byte[0];
        }
        return new String((byte[])o);
        Throwable t = null;
        if (t != null) {
            try {
                ((RandomAccessFile)t).close();
            }
            catch (IOException ex3) {
                AFLogger.afErrorLog("Exception while trying to close the InstallationFile", ex3);
            }
        }
    }
    
    public static String \u0399(final WeakReference<Context> weakReference) {
        synchronized (ae.class) {
            if (weakReference.get() != null && ae.\u01c3 == null) {
                final Object value = weakReference.get();
                String string = null;
                if (value != null) {
                    string = AppsFlyerLibCore.getSharedPreferences(weakReference.get()).getString("AF_INSTALLATION", (String)null);
                }
                if (string != null) {
                    ae.\u01c3 = string;
                }
                else {
                    try {
                        final File file = new File(weakReference.get().getFilesDir(), "AF_INSTALLATION");
                        if (!file.exists()) {
                            final long currentTimeMillis = System.currentTimeMillis();
                            final StringBuilder sb = new StringBuilder();
                            sb.append(currentTimeMillis);
                            sb.append("-");
                            sb.append(Math.abs(new SecureRandom().nextLong()));
                            ae.\u01c3 = sb.toString();
                        }
                        else {
                            ae.\u01c3 = \u0399(file);
                            file.delete();
                        }
                        final String \u01c3 = ae.\u01c3;
                        final SharedPreferences$Editor edit = AppsFlyerLibCore.getSharedPreferences(weakReference.get()).edit();
                        edit.putString("AF_INSTALLATION", \u01c3);
                        edit.apply();
                    }
                    catch (Exception ex) {
                        AFLogger.afErrorLog("Error getting AF unique ID", ex);
                    }
                }
                if (ae.\u01c3 != null) {
                    AppsFlyerProperties.getInstance().set("uid", ae.\u01c3);
                }
            }
            return ae.\u01c3;
        }
    }
}
