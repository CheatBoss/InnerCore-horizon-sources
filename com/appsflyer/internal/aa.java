package com.appsflyer.internal;

import android.content.*;
import java.util.*;
import android.util.*;
import java.io.*;

public final class aa
{
    private static aa \u01c3;
    
    static {
        aa.\u01c3 = new aa();
    }
    
    private aa() {
    }
    
    public static j \u0131(final File file) {
        Object o = null;
        Object o2;
        try {
            final FileReader fileReader = new FileReader(file);
            try {
                o = new char[(int)file.length()];
                fileReader.read((char[])o);
                o = new j((char[])o);
                ((j)o).\u01c3 = file.getName();
                try {
                    fileReader.close();
                    return (j)o;
                }
                catch (IOException ex) {
                    return (j)o;
                }
            }
            catch (Exception ex2) {}
        }
        catch (Exception ex3) {
            o2 = null;
        }
        finally {
            o2 = o;
        }
        try {
            ((Reader)o2).close();
            goto Label_0074;
        }
        catch (IOException ex4) {}
        if (o2 != null) {
            try {
                ((Reader)o2).close();
                return null;
            }
            catch (IOException ex5) {}
        }
        return null;
    }
    
    public static aa \u01c3() {
        return aa.\u01c3;
    }
    
    public static List<j> \u01c3(final Context context) {
        final ArrayList<j> list = new ArrayList<j>();
        try {
            final File file = new File(context.getFilesDir(), "AFRequestCache");
            if (!file.exists()) {
                file.mkdir();
                return list;
            }
            final File[] listFiles = file.listFiles();
            for (int length = listFiles.length, i = 0; i < length; ++i) {
                final File file2 = listFiles[i];
                final StringBuilder sb = new StringBuilder("Found cached request");
                sb.append(file2.getName());
                Log.i("AppsFlyer_5.4.1", sb.toString());
                list.add(\u0131(file2));
            }
        }
        catch (Exception ex) {
            Log.i("AppsFlyer_5.4.1", "Could not cache request");
        }
        return list;
    }
    
    public static void \u01c3(final String s, final Context context) {
        final File file = new File(new File(context.getFilesDir(), "AFRequestCache"), s);
        final StringBuilder sb = new StringBuilder("Deleting ");
        sb.append(s);
        sb.append(" from cache");
        Log.i("AppsFlyer_5.4.1", sb.toString());
        if (file.exists()) {
            try {
                file.delete();
            }
            catch (Exception ex) {
                final StringBuilder sb2 = new StringBuilder("Could not delete ");
                sb2.append(s);
                sb2.append(" from cache");
                Log.i("AppsFlyer_5.4.1", sb2.toString(), (Throwable)ex);
            }
        }
    }
    
    public static File \u03b9(final Context context) {
        return new File(context.getFilesDir(), "AFRequestCache");
    }
}
