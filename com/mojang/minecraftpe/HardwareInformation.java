package com.mojang.minecraftpe;

import android.annotation.*;
import android.content.*;
import android.os.*;
import java.io.*;
import java.util.regex.*;
import com.mojang.minecraftpe.platforms.*;
import java.util.*;
import android.provider.*;
import android.content.pm.*;

@SuppressLint({ "DefaultLocale" })
public class HardwareInformation
{
    private static final CPUInfo cpuInfo;
    private final ApplicationInfo appInfo;
    private final Context context;
    private final PackageManager packageManager;
    
    static {
        cpuInfo = getCPUInfo();
    }
    
    public HardwareInformation(final Context context) {
        this.packageManager = context.getPackageManager();
        this.appInfo = context.getApplicationInfo();
        this.context = context;
    }
    
    private boolean appInstalled(final String s) {
        try {
            this.packageManager.getPackageInfo(s, 0);
            return true;
        }
        catch (Exception ex) {
            return false;
        }
    }
    
    private boolean checkRootA() {
        final String tags = Build.TAGS;
        return tags != null && tags.contains("test-keys");
    }
    
    private boolean checkRootB() {
        final String[] array = { "/sbin/su", "/system/bin/su", "/system/xbin/su", "/data/local/xbin/su", "/data/local/bin/su", "/system/sd/xbin/su", "/system/bin/failsafe/su", "/system/app/Superuser.apk", "/data/local/su", "/su/bin/su" };
        for (int length = array.length, i = 0; i < length; ++i) {
            if (new File(array[i]).exists()) {
                return true;
            }
        }
        return false;
    }
    
    private boolean checkRootC() {
        final String[] array = { "eu.chainfire.supersu", "eu.chainfire.supersu.pro" };
        for (int length = array.length, i = 0; i < length; ++i) {
            if (this.appInstalled(array[i])) {
                return true;
            }
        }
        return false;
    }
    
    public static String getAndroidVersion() {
        final StringBuilder sb = new StringBuilder();
        sb.append("Android ");
        sb.append(Build$VERSION.RELEASE);
        return sb.toString();
    }
    
    public static String getBoard() {
        return Build.BOARD;
    }
    
    public static String getCPUFeatures() {
        return HardwareInformation.cpuInfo.getCPULine("Features");
    }
    
    public static CPUInfo getCPUInfo() {
        new StringBuffer();
        final HashMap<String, String> hashMap = new HashMap<String, String>();
        final boolean b = false;
        int n = 0;
        final int n2 = 0;
        if (new File("/proc/cpuinfo").exists()) {
            int n3 = b ? 1 : 0;
            try {
                final BufferedReader bufferedReader = new BufferedReader(new FileReader(new File("/proc/cpuinfo")));
                n3 = (b ? 1 : 0);
                final Pattern compile = Pattern.compile("(\\w*)\\s*:\\s([^\\n]*)");
                n = n2;
                while (true) {
                    n3 = n;
                    final String line = bufferedReader.readLine();
                    if (line == null) {
                        break;
                    }
                    n3 = n;
                    final Matcher matcher = compile.matcher(line);
                    int n4 = n;
                    n3 = n;
                    if (matcher.find()) {
                        n4 = n;
                        n3 = n;
                        if (matcher.groupCount() == 2) {
                            n3 = n;
                            if (!hashMap.containsKey(matcher.group(1))) {
                                n3 = n;
                                hashMap.put(matcher.group(1), matcher.group(2));
                            }
                            n3 = n;
                            final boolean contentEquals = matcher.group(1).contentEquals("processor");
                            n4 = n;
                            if (contentEquals) {
                                n4 = n + 1;
                            }
                        }
                    }
                    n = n4;
                }
                if (bufferedReader != null) {
                    n3 = n;
                    bufferedReader.close();
                }
            }
            catch (Exception ex) {
                ex.printStackTrace();
                n = n3;
            }
        }
        return new CPUInfo(hashMap, n);
    }
    
    public static String getCPUName() {
        return HardwareInformation.cpuInfo.getCPULine("Hardware");
    }
    
    public static String getCPUType() {
        return Platform.createPlatform(false).getABIS();
    }
    
    public static String getDeviceModelName() {
        final String manufacturer = Build.MANUFACTURER;
        final String model = Build.MODEL;
        if (model.startsWith(manufacturer)) {
            return model.toUpperCase();
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(manufacturer.toUpperCase());
        sb.append(" ");
        sb.append(model);
        return sb.toString();
    }
    
    public static String getLocale() {
        return Locale.getDefault().toString();
    }
    
    public static int getNumCores() {
        return HardwareInformation.cpuInfo.getNumberCPUCores();
    }
    
    public static String getSerialNumber() {
        return Build.SERIAL;
    }
    
    public String getInstallerPackageName() {
        if (this.packageManager == null) {
            return "";
        }
        if (this.appInfo == null) {
            return "";
        }
        return this.packageManager.getInstallerPackageName(this.context.getPackageName());
    }
    
    public boolean getIsRooted() {
        return this.checkRootA() || this.checkRootB() || this.checkRootC();
    }
    
    public String getSecureId() {
        return Settings$Secure.getString(this.context.getContentResolver(), "android_id");
    }
    
    public int getSignaturesHashCode() {
        int i = 0;
        final boolean b = false;
        int n = 0;
        int n2 = b ? 1 : 0;
        try {
            final Signature[] signatures = this.packageManager.getPackageInfo(this.context.getPackageName(), 64).signatures;
            n2 = (b ? 1 : 0);
            while (i < signatures.length) {
                n2 = n;
                n ^= signatures[i].hashCode();
                ++i;
            }
            return n;
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return n2;
        }
    }
    
    public static class CPUInfo
    {
        private final Map<String, String> cpuLines;
        private final int numberCPUCores;
        
        public CPUInfo(final Map<String, String> cpuLines, final int numberCPUCores) {
            this.cpuLines = cpuLines;
            this.numberCPUCores = numberCPUCores;
        }
        
        String getCPULine(final String s) {
            if (this.cpuLines.containsKey(s)) {
                return this.cpuLines.get(s);
            }
            return "";
        }
        
        int getNumberCPUCores() {
            return this.numberCPUCores;
        }
    }
}
