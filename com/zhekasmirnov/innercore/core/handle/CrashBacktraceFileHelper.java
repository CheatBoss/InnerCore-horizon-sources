package com.zhekasmirnov.innercore.core.handle;

import com.zhekasmirnov.innercore.utils.*;
import java.util.*;
import java.io.*;

public class CrashBacktraceFileHelper
{
    private static final String CRASH_DIR;
    private static final String CRASH_DUMP_ARCHIVE_START = "dump-archive-";
    private static final String CRASH_DUMP_PREFIX = "latest-crash";
    private static final String CRASH_DUMP_PREFIX_ADDITIONAL = "latest-crash-";
    
    static {
        CRASH_DIR = FileTools.assureAndGetCrashDir();
    }
    
    public static String buildDumpArchiveContents(final ArrayList<CrashDumpFile> list, final boolean b) {
        String s;
        if (b) {
            s = "<br>";
        }
        else {
            s = "\n";
        }
        String s2;
        if (b) {
            s2 = "<b>";
        }
        else {
            s2 = "";
        }
        String s3;
        if (b) {
            s3 = "</b>";
        }
        else {
            s3 = "";
        }
        final String string = new Date(System.currentTimeMillis()).toString();
        final StringBuilder sb = new StringBuilder();
        sb.append("Here stored all crash dumps that occurred at ");
        sb.append(string);
        sb.append(",");
        sb.append(s);
        sb.append("such crashes are normally might not occur, so if it ruined your gameplay I apologize for such a disaster");
        sb.append(s);
        sb.append("You might send this file to Inner Core developer by email (innercore2018@gmail.com), try to analyse it by yourself, if you are mod developer,");
        sb.append(s);
        sb.append("or just ignore, because most likely, that Inner Core automatically sent this file for analysis.");
        sb.append(s);
        sb.append(s);
        final ArrayList<Integer> list2 = new ArrayList<Integer>();
        final HashMap<Object, ArrayList<CrashDumpFile>> hashMap = new HashMap<Object, ArrayList<CrashDumpFile>>();
        for (final CrashDumpFile crashDumpFile : list) {
            final int hashCode = crashDumpFile.hashCode();
            if (hashMap.containsKey(hashCode)) {
                hashMap.get(hashCode).add(crashDumpFile);
            }
            else {
                final ArrayList<CrashDumpFile> list3 = new ArrayList<CrashDumpFile>();
                list3.add(crashDumpFile);
                hashMap.put(hashCode, list3);
                list2.add(hashCode);
            }
        }
        final Iterator<Integer> iterator2 = list2.iterator();
        while (iterator2.hasNext()) {
            final ArrayList<CrashDumpFile> list4 = hashMap.get(iterator2.next());
            Collections.sort((List<Object>)list4, (Comparator<? super Object>)new Comparator<CrashDumpFile>() {
                @Override
                public int compare(final CrashDumpFile crashDumpFile, final CrashDumpFile crashDumpFile2) {
                    return crashDumpFile.getIndex() - crashDumpFile2.getIndex();
                }
            });
            final StringBuilder sb2 = new StringBuilder();
            int n2;
            int n = n2 = list4.get(0).getIndex();
            for (final CrashDumpFile crashDumpFile2 : list4) {
                int index = n;
                if (crashDumpFile2.getIndex() > n2 + 1) {
                    if (sb2.length() > 0) {
                        sb2.append(", ");
                    }
                    if (n == n2) {
                        sb2.append(n);
                    }
                    else {
                        sb2.append(n);
                        sb2.append("-");
                        sb2.append(n2);
                    }
                    index = crashDumpFile2.getIndex();
                }
                n2 = crashDumpFile2.getIndex();
                n = index;
            }
            if (sb2.length() > 0) {
                sb2.append(", ");
            }
            if (n == n2) {
                sb2.append(n);
            }
            else {
                sb2.append(n);
                sb2.append("-");
                sb2.append(n2);
            }
            sb.append(s);
            sb.append(s2);
            String s4;
            if (list4.size() > 1) {
                s4 = "DUMPS ";
            }
            else {
                s4 = "DUMP #";
            }
            sb.append(s4);
            sb.append(sb2.toString());
            sb.append(s3);
            sb.append(s);
            sb.append(list4.get(0).toString(b, true));
            sb.append(s);
            sb.append(s);
        }
        return sb.toString();
    }
    
    public static boolean checkLock() {
        return new File(CrashBacktraceFileHelper.CRASH_DIR, "lock").exists();
    }
    
    public static ArrayList<CrashDumpFile> collectCrashDumps() {
        final ArrayList<CrashDumpFile> list = new ArrayList<CrashDumpFile>();
        final File[] listFiles = new File(CrashBacktraceFileHelper.CRASH_DIR).listFiles();
        for (int length = listFiles.length, i = 0; i < length; ++i) {
            final File file = listFiles[i];
            final String name = file.getName();
            if (name.startsWith("latest-crash")) {
                int intValue = 0;
                if (name.startsWith("latest-crash-")) {
                    try {
                        intValue = Integer.valueOf(name.substring("latest-crash-".length(), name.indexOf(".")));
                    }
                    catch (NumberFormatException ex) {
                        intValue = -1;
                    }
                }
                final CrashDumpFile crashDumpFile = new CrashDumpFile(file);
                if (crashDumpFile.isValid()) {
                    crashDumpFile.setIndex(intValue);
                    list.add(crashDumpFile);
                }
            }
        }
        return list;
    }
    
    public static void createDumpArchiveFile(final boolean b) {
        final ArrayList<CrashDumpFile> collectCrashDumps = collectCrashDumps();
        final File[] listFiles = new File(CrashBacktraceFileHelper.CRASH_DIR).listFiles();
        final int length = listFiles.length;
        int n = 1;
        int max;
        for (int i = 0; i < length; ++i, n = max) {
            final String name = listFiles[i].getName();
            max = n;
            if (name.startsWith("dump-archive-")) {
                try {
                    max = Math.max(n, Integer.valueOf(name.substring("dump-archive-".length(), name.indexOf("."))));
                }
                catch (NumberFormatException ex) {
                    max = n;
                }
            }
        }
        final int n2 = n + 1;
        final String crash_DIR = CrashBacktraceFileHelper.CRASH_DIR;
        final StringBuilder sb = new StringBuilder();
        sb.append("dump-archive-");
        sb.append(n2);
        sb.append(".txt");
        final File file = new File(crash_DIR, sb.toString());
        try {
            final String absolutePath = file.getAbsolutePath();
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("CRASH DUMP ARCHIVE FILE #");
            sb2.append(n2);
            sb2.append("\npath:");
            sb2.append(file);
            sb2.append("\n");
            sb2.append(buildDumpArchiveContents(collectCrashDumps, false));
            FileTools.writeFileText(absolutePath, sb2.toString());
            if (b) {
                final Iterator<CrashDumpFile> iterator = collectCrashDumps.iterator();
                while (iterator.hasNext()) {
                    iterator.next().delete();
                }
            }
        }
        catch (IOException ex2) {}
    }
    
    public static void deleteLock() {
        final File file = new File(CrashBacktraceFileHelper.CRASH_DIR, "lock");
        if (file.exists()) {
            file.delete();
        }
    }
    
    public static class CrashDumpFile
    {
        private static final String MARKER_CHAR = "#";
        private final File file;
        private int index;
        private String rawText;
        
        public CrashDumpFile(final File file) {
            this.index = -1;
            this.file = file;
            try {
                this.rawText = FileTools.readFileText(file.getAbsolutePath());
            }
            catch (IOException ex) {
                this.rawText = null;
            }
            final String rawText = this.rawText;
        }
        
        private String getTextByMarker(String s, final boolean b) {
            if (this.rawText == null) {
                return null;
            }
            String string = s;
            if (!s.startsWith("#")) {
                final StringBuilder sb = new StringBuilder();
                sb.append("#");
                sb.append(s);
                string = sb.toString();
            }
            final int index = this.rawText.indexOf(string);
            if (index == -1) {
                return null;
            }
            final String s2 = s = this.rawText.substring(string.length() + index);
            if (s2.startsWith(":")) {
                s = s2.substring(1);
            }
            String substring = s;
            if (b) {
                final int index2 = s.indexOf("#");
                substring = s;
                if (index2 != -1) {
                    substring = s.substring(0, index2);
                }
            }
            return substring.trim();
        }
        
        public boolean delete() {
            return this.file.delete();
        }
        
        public String getFormattedDate() {
            return new Date(this.file.lastModified()).toString();
        }
        
        public String getFullBacktrace() {
            final StringBuilder sb = new StringBuilder();
            sb.append("\t");
            sb.append(this.getTextByMarker("BACKTRACE", true));
            return sb.toString();
        }
        
        public int getIndex() {
            return this.index;
        }
        
        public String getSignalInfo() {
            return this.getTextByMarker("INFO", true);
        }
        
        @Override
        public int hashCode() {
            return this.rawText.hashCode();
        }
        
        public boolean isValid() {
            return this.rawText != null;
        }
        
        public void setIndex(final int index) {
            this.index = index;
        }
        
        public String toString(final boolean b, final boolean b2) {
            final StringBuilder sb = new StringBuilder();
            String s;
            if (b) {
                s = "<br>";
            }
            else {
                s = "\n";
            }
            String s2;
            if (b) {
                s2 = "<b>";
            }
            else {
                s2 = "";
            }
            String s3;
            if (b) {
                s3 = "</b>";
            }
            else {
                s3 = "";
            }
            String s4;
            if (b) {
                s4 = "<font color='red'>";
            }
            else {
                s4 = "";
            }
            String s5;
            if (b) {
                s5 = "</font>";
            }
            else {
                s5 = "";
            }
            String signalInfo = this.getSignalInfo();
            final String fullBacktrace = this.getFullBacktrace();
            if (!b2) {
                sb.append(s2);
                sb.append("CRASH DUMP #");
                sb.append(this.index);
                sb.append(" [at ");
                sb.append(this.getFormattedDate());
                sb.append("]");
                sb.append(s3);
                sb.append(s);
                sb.append(s);
            }
            sb.append(s2);
            sb.append("SIGNAL INFO:");
            sb.append(s3);
            sb.append(s);
            if (signalInfo == null) {
                signalInfo = "dump missing #INFO tag";
            }
            sb.append(signalInfo);
            sb.append(s);
            sb.append(s);
            sb.append(s2);
            sb.append("SIGNAL BACKTRACE:");
            sb.append(s3);
            sb.append(s);
            sb.append(s4);
            String s6;
            if (fullBacktrace != null) {
                s6 = fullBacktrace;
            }
            else {
                s6 = "dump missing #BACKTRACE tag";
            }
            sb.append(s6);
            sb.append(s5);
            sb.append(s);
            return sb.toString().replaceAll("\n", s);
        }
    }
}
