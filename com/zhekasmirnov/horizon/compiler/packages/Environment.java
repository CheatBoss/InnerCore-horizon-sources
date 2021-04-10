package com.zhekasmirnov.horizon.compiler.packages;

import android.os.*;
import java.util.*;
import com.zhekasmirnov.horizon.*;
import android.preference.*;
import android.content.*;
import android.support.annotation.*;
import com.pdaxrom.utils.*;
import java.io.*;

public class Environment
{
    public static final String APPLICATION_DIR_NAME = "CCPlusPlusNIDE";
    private static final String TAG = "Environment";
    
    public static String[] getSupportedABIs() {
        String[] result;
        if (Build.VERSION.SDK_INT >= 21) {
            result = Build.SUPPORTED_ABIS;
        }
        else {
            result = new String[] { Build.CPU_ABI, Build.CPU_ABI2 };
        }
        final List<String> abiList = new ArrayList<String>();
        Collections.addAll(abiList, result);
        Collections.sort(abiList);
        return abiList.toArray(new String[result.length]);
    }
    
    public static File getExternalHorizonDirectory() {
        return new File(android.os.Environment.getExternalStorageDirectory(), "games/horizon");
    }
    
    public static File getDataDirFile(final Context context) {
        return new File("/data/data", context.getApplicationInfo().packageName);
    }
    
    public static File getDataDirFile() {
        return new File("/data/data", HorizonApplication.getInstance().getApplicationInfo().packageName);
    }
    
    public static File getPackExecutionDir(final Context context) {
        return new File(getDataDirFile(context), "soexec_pack");
    }
    
    public static File getApplicationLibraryDirectory() {
        return new File(getDataDirFile(), "soexec_app");
    }
    
    public static String getToolchainsDir(final Context context) {
        final File path = new File(context.getCacheDir().getParentFile(), "root");
        return mkdirIfNotExist(path);
    }
    
    public static String getJavacDir(final Context context) {
        final File path = new File(context.getCacheDir().getParentFile(), "javac");
        return mkdirIfNotExist(path);
    }
    
    public static String getCCtoolsDir(final Context c) {
        final File path = new File(getToolchainsDir(c), "cctools");
        return mkdirIfNotExist(path);
    }
    
    public static String getServiceDir(final Context context) {
        final String path = new File(getToolchainsDir(context), "/cctools/services").getAbsolutePath();
        return mkdirIfNotExist(path);
    }
    
    public static String getHomeDir(final Context context) {
        final String path = new File(getToolchainsDir(context), "/cctools/home").getAbsolutePath();
        return mkdirIfNotExist(path);
    }
    
    public static String getInstalledPackageDir(final Context context) {
        final String path = new File(getToolchainsDir(context), "installed").getAbsolutePath();
        return mkdirIfNotExist(path);
    }
    
    public static String getDalvikCacheDir(final Context context) {
        final File path = new File(getToolchainsDir(context), "cctools/var/dalvik/dalvik-cache");
        return mkdirIfNotExist(path);
    }
    
    public static String getTmpExeDir(final Context context) {
        final File file = new File(getToolchainsDir(context), "tmpdir");
        return mkdirIfNotExist(file);
    }
    
    public static String getSdCardHomeDir() {
        final File path = new File(android.os.Environment.getExternalStorageDirectory().getPath(), "CCPlusPlusNIDE");
        return mkdirIfNotExist(path);
    }
    
    public static String getSdCardBackupDir() {
        final String path = getSdCardHomeDir() + "/backup";
        return mkdirIfNotExist(path);
    }
    
    public static String getSdCardTmpDir() {
        final String path = getSdCardHomeDir() + "/tmp";
        return mkdirIfNotExist(path);
    }
    
    public static String getSdCardSourceDir() {
        return mkdirIfNotExist(new File(getSdCardHomeDir(), "src"));
    }
    
    private static String mkdirIfNotExist(final String path) {
        final File file = new File(path);
        return mkdirIfNotExist(file);
    }
    
    private static String mkdirIfNotExist(final File file) {
        if (!file.exists()) {
            file.mkdir();
        }
        return file.getAbsolutePath();
    }
    
    public static String[] buildDefaultEnv(final Context context) {
        final SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(context);
        final String cctoolsDir = getCCtoolsDir(context);
        final String tmpExeDir = getTmpExeDir(context);
        return new String[] { "TMPDIR=" + tmpExeDir, "TMPEXEDIR=" + tmpExeDir, "TEMPDIR=" + tmpExeDir, "TEMP=" + tmpExeDir, "PATH=" + joinPath(cctoolsDir + "/bin", cctoolsDir + "/sbin", System.getenv("PATH")), "HOME=" + getHomeDir(context), "ANDROID_ASSETS=" + getEnv("ANDROID_ASSETS", "/system/app"), "ANDROID_BOOTLOGO=" + getEnv("ANDROID_BOOTLOGO", "1"), "ANDROID_DATA=" + joinPath(cctoolsDir + "/var/dalvik", getEnv("ANDROID_DATA", null)), "ANDROID_ROOT=" + getEnv("ANDROID_ROOT", "/system"), "ANDROID_PROPERTY_WORKSPACE=" + getEnv(context, "ANDROID_PROPERTY_WORKSPACE"), "BOOTCLASSPATH=" + getBootClassPath(), "EXTERNAL_STORAGE=" + android.os.Environment.getExternalStorageDirectory().getPath(), "LD_LIBRARY_PATH=" + joinPath(cctoolsDir + "/lib", getEnv("LD_LIBRARY_PATH", null)), "CCTOOLSDIR=" + cctoolsDir, "CCTOOLSRES=" + context.getPackageResourcePath(), "CFGDIR=" + getShareDir(context), "SHELL=" + getShell(context), "TERM=", "PS1=$ ", "SDDIR=" + getSdCardHomeDir() };
    }
    
    private static String getShareDir(final Context context) {
        final File file = new File(getCCtoolsDir(context), "share");
        return mkdirIfNotExist(file);
    }
    
    private static String getBootClassPath() {
        String bootClassPath = getEnv("BOOTCLASSPATH", null);
        if (bootClassPath == null) {
            bootClassPath = findBootClassPath();
        }
        if (bootClassPath == null || bootClassPath.isEmpty()) {
            bootClassPath = "/system/framework/core.jar:/system/framework/ext.jar:/system/framework/framework.jar:/system/framework/android.policy.jar:/system/framework/services.jar";
        }
        return bootClassPath;
    }
    
    @Nullable
    private static String findBootClassPath() {
        String classPath = null;
        final File dir = new File("/system/framework");
        if (dir.exists() && dir.isDirectory()) {
            final FilenameFilter filter = new FilenameFilter() {
                @Override
                public boolean accept(final File dir, final String name) {
                    final String lowercaseName = name.toLowerCase();
                    return lowercaseName.endsWith(".jar");
                }
            };
            final String[] list = dir.list(filter);
            for (int i = 0; i < list.length; ++i) {
                final String file = list[i];
                if (i != 0) {
                    classPath += ":";
                }
                classPath = classPath + "/system/framework/" + file;
            }
        }
        return classPath;
    }
    
    private static String getEnv(final String name, final String defValue) {
        final String value = System.getenv(name);
        if (value != null) {
            return value;
        }
        return defValue;
    }
    
    private static String joinPath(final String... paths) {
        final StringBuilder sb = new StringBuilder();
        for (final String path : paths) {
            if (path != null && !path.isEmpty()) {
                if (sb.length() != 0) {
                    sb.append(File.pathSeparator);
                }
                sb.append(path);
            }
        }
        return sb.toString();
    }
    
    public static String getShell(final Context context) {
        return "/system/bin/sh";
    }
    
    protected static String getEnv(final Context context, final String variable) {
        String ret = null;
        final String cctoolsDir = getCCtoolsDir(context);
        final String[] envp = { "TMPDIR=" + getSdCardTmpDir(), "PATH=" + joinPath(cctoolsDir + "/bin", cctoolsDir + "/sbin", System.getenv("PATH")), "ANDROID_ASSETS=" + getEnv("ANDROID_ASSETS", "/system/app"), "ANDROID_BOOTLOGO=" + getEnv("ANDROID_BOOTLOGO", "1"), "ANDROID_DATA=" + joinPath(cctoolsDir + "/var/dalvik", getEnv("ANDROID_DATA", null)), "ANDROID_ROOT=" + getEnv("ANDROID_ROOT", "/system"), "CCTOOLSDIR=" + cctoolsDir, "CCTOOLSRES=" + context.getPackageResourcePath(), "LD_LIBRARY_PATH=" + joinPath(cctoolsDir + "/lib", getEnv("LD_LIBRARY_PATH", null)), "HOME=" + getHomeDir(context), "SHELL=" + getShell(context), "TERM=xterm", "PS1=$ ", "SDDIR=" + getSdCardHomeDir(), "EXTERNAL_STORAGE=" + android.os.Environment.getExternalStorageDirectory().getPath() };
        final String[] argv = { "/system/bin/sh", "-c", "set" };
        final int[] pId = { 0 };
        final FileDescriptor fd = Utils.createSubProcess(cctoolsDir, argv[0], argv, envp, pId);
        final FileInputStream fis = new FileInputStream(fd);
        final DataInputStream in = new DataInputStream(fis);
        final BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        try {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(variable + "=") && line.contains("=")) {
                    ret = line.substring(line.indexOf("=") + 1);
                    break;
                }
            }
            in.close();
            Utils.waitFor(pId[0]);
        }
        catch (Exception ex) {}
        return ret;
    }
    
    public static String[] join(final String[] env1, final String[] env2) {
        final String[] env3 = new String[env1.length + env2.length];
        System.arraycopy(env1, 0, env3, 0, env1.length);
        System.arraycopy(env2, 0, env3, env1.length, env2.length);
        return env3;
    }
    
    public static void mkdirs(final Context context) {
        getServiceDir(context);
        getHomeDir(context);
        getToolchainsDir(context);
        getSdCardHomeDir();
        getSdCardBackupDir();
        getSdCardTmpDir();
    }
    
    public static String getSdCardDir() {
        return android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
    }
    
    public static String getSdCardExampleDir() {
        final File file = new File(getSdCardHomeDir(), "Examples");
        return mkdirIfNotExist(file);
    }
}
