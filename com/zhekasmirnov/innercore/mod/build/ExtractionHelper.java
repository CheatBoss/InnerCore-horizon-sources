package com.zhekasmirnov.innercore.mod.build;

import java.util.zip.*;
import java.util.*;
import com.zhekasmirnov.horizon.runtime.logger.*;
import com.zhekasmirnov.innercore.utils.*;
import com.zhekasmirnov.innercore.modpack.*;
import com.zhekasmirnov.innercore.api.mod.*;
import java.io.*;
import com.zhekasmirnov.innercore.api.mod.util.*;
import com.zhekasmirnov.innercore.api.runtime.other.*;
import com.zhekasmirnov.innercore.mod.executable.*;
import org.mozilla.javascript.*;

public class ExtractionHelper
{
    public static final String TEMP_DIR;
    private static ArrayList<String> extractionPathList;
    private static String lastLocation;
    
    static {
        final StringBuilder sb = new StringBuilder();
        sb.append(FileTools.DIR_WORK);
        sb.append("temp/extract/");
        FileTools.assureDir(TEMP_DIR = sb.toString());
        ExtractionHelper.extractionPathList = new ArrayList<String>();
    }
    
    static String extractAs(final ZipFile zipFile, final String s, String absolutePath) throws IOException {
        if (absolutePath != null && absolutePath.length() != 0 && absolutePath.indexOf(92) == -1 && absolutePath.indexOf(47) == -1) {
            absolutePath = getInstallationPath(absolutePath).getAbsolutePath();
            final byte[] array = new byte[1024];
            final Enumeration<? extends ZipEntry> entries = zipFile.entries();
            try {
                while (true) {
                    final ZipEntry zipEntry = (ZipEntry)entries.nextElement();
                    if (zipEntry == null) {
                        break;
                    }
                    final String name = zipEntry.getName();
                    if (!name.startsWith(s) || name.contains(".setup/")) {
                        continue;
                    }
                    final String substring = name.substring(s.length());
                    if (zipEntry.isDirectory()) {
                        continue;
                    }
                    final File file = new File(absolutePath, substring);
                    FileTools.assureFileDir(file);
                    final InputStream inputStream = zipFile.getInputStream(zipEntry);
                    final FileOutputStream fileOutputStream = new FileOutputStream(file);
                    while (true) {
                        final int read = inputStream.read(array);
                        if (read == -1) {
                            break;
                        }
                        fileOutputStream.write(array, 0, read);
                    }
                    fileOutputStream.close();
                    inputStream.close();
                }
            }
            catch (NoSuchElementException ex) {}
            ExtractionHelper.extractionPathList.add(absolutePath);
            return absolutePath;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("invalid directory name passed to the method extractAs: '");
        sb.append(absolutePath);
        sb.append("', it must be not empty and must not contain '\\' or '/' symbols");
        throw new IllegalArgumentException(sb.toString());
    }
    
    static void extractEntry(final ZipFile zipFile, final String s, final String s2, final String s3) throws IOException {
        final StringBuilder sb = new StringBuilder();
        sb.append(s);
        sb.append(s2);
        final ZipEntry entry = zipFile.getEntry(sb.toString());
        if (entry == null) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("entry ");
            sb2.append(s);
            sb2.append(s2);
            sb2.append(" does not exist for file ");
            sb2.append(zipFile);
            throw new IllegalArgumentException(sb2.toString());
        }
        FileTools.assureFileDir(new File(s3));
        final StringBuilder sb3 = new StringBuilder();
        sb3.append("started entry extraction ");
        sb3.append(s);
        sb3.append(s2);
        Logger.debug("DEBUG", sb3.toString());
        final byte[] array = new byte[1024];
        final InputStream inputStream = zipFile.getInputStream(entry);
        final FileOutputStream fileOutputStream = new FileOutputStream(s3);
        while (true) {
            final int read = inputStream.read(array);
            if (read == -1) {
                break;
            }
            fileOutputStream.write(array, 0, read);
        }
        fileOutputStream.close();
        inputStream.close();
    }
    
    public static ArrayList<String> extractICModFile(final File p0, final IMessageReceiver p1, final Runnable p2) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     2: monitorenter   
        //     3: new             Ljava/lang/StringBuilder;
        //     6: dup            
        //     7: invokespecial   java/lang/StringBuilder.<init>:()V
        //    10: astore          5
        //    12: aload           5
        //    14: ldc             "preparing to install "
        //    16: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    19: pop            
        //    20: aload           5
        //    22: aload_0        
        //    23: invokevirtual   java/io/File.getName:()Ljava/lang/String;
        //    26: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //    29: pop            
        //    30: aload_1        
        //    31: aload           5
        //    33: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //    36: invokeinterface com/zhekasmirnov/innercore/utils/IMessageReceiver.message:(Ljava/lang/String;)V
        //    41: getstatic       com/zhekasmirnov/innercore/mod/build/ExtractionHelper.extractionPathList:Ljava/util/ArrayList;
        //    44: invokevirtual   java/util/ArrayList.clear:()V
        //    47: getstatic       android/os/Build$VERSION.SDK_INT:I
        //    50: bipush          24
        //    52: if_icmplt       73
        //    55: new             Ljava/util/zip/ZipFile;
        //    58: dup            
        //    59: aload_0        
        //    60: ldc             "UTF-8"
        //    62: invokestatic    java/nio/charset/Charset.forName:(Ljava/lang/String;)Ljava/nio/charset/Charset;
        //    65: invokespecial   java/util/zip/ZipFile.<init>:(Ljava/io/File;Ljava/nio/charset/Charset;)V
        //    68: astore          5
        //    70: goto            83
        //    73: new             Ljava/util/zip/ZipFile;
        //    76: dup            
        //    77: aload_0        
        //    78: invokespecial   java/util/zip/ZipFile.<init>:(Ljava/io/File;)V
        //    81: astore          5
        //    83: aload           5
        //    85: ldc             "build.config"
        //    87: invokestatic    com/zhekasmirnov/innercore/mod/build/ExtractionHelper.searchForSubPath:(Ljava/util/zip/ZipFile;Ljava/lang/String;)Ljava/lang/String;
        //    90: astore          6
        //    92: aload           6
        //    94: ifnonnull       110
        //    97: aload_1        
        //    98: ldc             "mod archive has incorrect structure: build.config file was not found anywhere"
        //   100: invokeinterface com/zhekasmirnov/innercore/utils/IMessageReceiver.message:(Ljava/lang/String;)V
        //   105: ldc             Lcom/zhekasmirnov/innercore/mod/build/ExtractionHelper;.class
        //   107: monitorexit    
        //   108: aconst_null    
        //   109: areturn        
        //   110: new             Ljava/lang/StringBuilder;
        //   113: dup            
        //   114: invokespecial   java/lang/StringBuilder.<init>:()V
        //   117: astore          7
        //   119: aload           7
        //   121: ldc             "mod installation dir was found at path '/"
        //   123: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   126: pop            
        //   127: aload           7
        //   129: aload           6
        //   131: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   134: pop            
        //   135: aload           7
        //   137: ldc             "'"
        //   139: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   142: pop            
        //   143: aload_1        
        //   144: aload           7
        //   146: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   149: invokeinterface com/zhekasmirnov/innercore/utils/IMessageReceiver.message:(Ljava/lang/String;)V
        //   154: iconst_3       
        //   155: anewarray       [Ljava/lang/String;
        //   158: astore          7
        //   160: aload           7
        //   162: iconst_0       
        //   163: iconst_2       
        //   164: anewarray       Ljava/lang/String;
        //   167: dup            
        //   168: iconst_0       
        //   169: ldc             "cfg"
        //   171: aastore        
        //   172: dup            
        //   173: iconst_1       
        //   174: ldc             "build.config"
        //   176: aastore        
        //   177: aastore        
        //   178: aload           7
        //   180: iconst_1       
        //   181: iconst_2       
        //   182: anewarray       Ljava/lang/String;
        //   185: dup            
        //   186: iconst_0       
        //   187: ldc             "icon"
        //   189: aastore        
        //   190: dup            
        //   191: iconst_1       
        //   192: ldc             "mod_icon.png"
        //   194: aastore        
        //   195: aastore        
        //   196: aload           7
        //   198: iconst_2       
        //   199: iconst_2       
        //   200: anewarray       Ljava/lang/String;
        //   203: dup            
        //   204: iconst_0       
        //   205: ldc             "info"
        //   207: aastore        
        //   208: dup            
        //   209: iconst_1       
        //   210: ldc             "mod.info"
        //   212: aastore        
        //   213: aastore        
        //   214: aload_1        
        //   215: ldc             "extracting installation files"
        //   217: invokeinterface com/zhekasmirnov/innercore/utils/IMessageReceiver.message:(Ljava/lang/String;)V
        //   222: aload           7
        //   224: arraylength    
        //   225: istore          4
        //   227: iconst_0       
        //   228: istore_3       
        //   229: iload_3        
        //   230: iload           4
        //   232: if_icmpge       332
        //   235: aload           7
        //   237: iload_3        
        //   238: aaload         
        //   239: astore          8
        //   241: new             Ljava/io/File;
        //   244: dup            
        //   245: getstatic       com/zhekasmirnov/innercore/mod/build/ExtractionHelper.TEMP_DIR:Ljava/lang/String;
        //   248: aload           8
        //   250: iconst_0       
        //   251: aaload         
        //   252: invokespecial   java/io/File.<init>:(Ljava/lang/String;Ljava/lang/String;)V
        //   255: astore          9
        //   257: aload           9
        //   259: invokevirtual   java/io/File.exists:()Z
        //   262: ifeq            271
        //   265: aload           9
        //   267: invokevirtual   java/io/File.delete:()Z
        //   270: pop            
        //   271: aload           8
        //   273: iconst_1       
        //   274: aaload         
        //   275: astore          9
        //   277: new             Ljava/lang/StringBuilder;
        //   280: dup            
        //   281: invokespecial   java/lang/StringBuilder.<init>:()V
        //   284: astore          10
        //   286: aload           10
        //   288: getstatic       com/zhekasmirnov/innercore/mod/build/ExtractionHelper.TEMP_DIR:Ljava/lang/String;
        //   291: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   294: pop            
        //   295: aload           10
        //   297: aload           8
        //   299: iconst_0       
        //   300: aaload         
        //   301: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   304: pop            
        //   305: aload           5
        //   307: aload           6
        //   309: aload           9
        //   311: aload           10
        //   313: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   316: invokestatic    com/zhekasmirnov/innercore/mod/build/ExtractionHelper.extractEntry:(Ljava/util/zip/ZipFile;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V
        //   319: goto            885
        //   322: astore          8
        //   324: aload           8
        //   326: invokevirtual   java/lang/Exception.printStackTrace:()V
        //   329: goto            885
        //   332: new             Lcom/zhekasmirnov/innercore/mod/build/BuildConfig;
        //   335: dup            
        //   336: new             Ljava/io/File;
        //   339: dup            
        //   340: getstatic       com/zhekasmirnov/innercore/mod/build/ExtractionHelper.TEMP_DIR:Ljava/lang/String;
        //   343: ldc             "cfg"
        //   345: invokespecial   java/io/File.<init>:(Ljava/lang/String;Ljava/lang/String;)V
        //   348: invokespecial   com/zhekasmirnov/innercore/mod/build/BuildConfig.<init>:(Ljava/io/File;)V
        //   351: astore          7
        //   353: aload           7
        //   355: invokevirtual   com/zhekasmirnov/innercore/mod/build/BuildConfig.read:()Z
        //   358: ifne            375
        //   361: aload_1        
        //   362: ldc_w           "build config cannot be loaded correctly, it failed to extract or was corrupted"
        //   365: invokeinterface com/zhekasmirnov/innercore/utils/IMessageReceiver.message:(Ljava/lang/String;)V
        //   370: ldc             Lcom/zhekasmirnov/innercore/mod/build/ExtractionHelper;.class
        //   372: monitorexit    
        //   373: aconst_null    
        //   374: areturn        
        //   375: aload_1        
        //   376: ldc_w           "we are ready to install"
        //   379: invokeinterface com/zhekasmirnov/innercore/utils/IMessageReceiver.message:(Ljava/lang/String;)V
        //   384: aload_2        
        //   385: ifnull          394
        //   388: aload_2        
        //   389: invokeinterface java/lang/Runnable.run:()V
        //   394: aload           7
        //   396: getfield        com/zhekasmirnov/innercore/mod/build/BuildConfig.defaultConfig:Lcom/zhekasmirnov/innercore/mod/build/BuildConfig$DefaultConfig;
        //   399: getfield        com/zhekasmirnov/innercore/mod/build/BuildConfig$DefaultConfig.setupScriptDir:Ljava/lang/String;
        //   402: astore_2       
        //   403: aload           6
        //   405: invokevirtual   java/lang/String.length:()I
        //   408: ifle            454
        //   411: aload           6
        //   413: bipush          47
        //   415: invokevirtual   java/lang/String.indexOf:(I)I
        //   418: aload           6
        //   420: bipush          92
        //   422: invokevirtual   java/lang/String.indexOf:(I)I
        //   425: invokestatic    java/lang/Math.max:(II)I
        //   428: istore_3       
        //   429: iload_3        
        //   430: iconst_m1      
        //   431: if_icmpeq       437
        //   434: goto            443
        //   437: aload           6
        //   439: invokevirtual   java/lang/String.length:()I
        //   442: istore_3       
        //   443: aload           6
        //   445: iconst_0       
        //   446: iload_3        
        //   447: invokevirtual   java/lang/String.substring:(II)Ljava/lang/String;
        //   450: astore_0       
        //   451: goto            485
        //   454: aload_0        
        //   455: invokevirtual   java/io/File.getName:()Ljava/lang/String;
        //   458: astore_0       
        //   459: aload_0        
        //   460: ldc_w           ".icmod"
        //   463: invokevirtual   java/lang/String.endsWith:(Ljava/lang/String;)Z
        //   466: ifeq            892
        //   469: aload_0        
        //   470: iconst_0       
        //   471: aload_0        
        //   472: invokevirtual   java/lang/String.length:()I
        //   475: bipush          6
        //   477: isub           
        //   478: invokevirtual   java/lang/String.substring:(II)Ljava/lang/String;
        //   481: astore_0       
        //   482: goto            485
        //   485: aload_0        
        //   486: invokestatic    com/zhekasmirnov/innercore/mod/build/ExtractionHelper.getFreeLocation:(Ljava/lang/String;)Ljava/lang/String;
        //   489: astore_0       
        //   490: new             Ljava/lang/StringBuilder;
        //   493: dup            
        //   494: invokespecial   java/lang/StringBuilder.<init>:()V
        //   497: astore          7
        //   499: aload           7
        //   501: ldc_w           "installing mod (default directory name is '"
        //   504: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   507: pop            
        //   508: aload           7
        //   510: aload_0        
        //   511: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   514: pop            
        //   515: aload           7
        //   517: ldc_w           "', but it probably will change)."
        //   520: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   523: pop            
        //   524: aload_1        
        //   525: aload           7
        //   527: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   530: invokeinterface com/zhekasmirnov/innercore/utils/IMessageReceiver.message:(Ljava/lang/String;)V
        //   535: aload_2        
        //   536: ifnull          703
        //   539: new             Ljava/lang/StringBuilder;
        //   542: dup            
        //   543: invokespecial   java/lang/StringBuilder.<init>:()V
        //   546: astore          7
        //   548: aload           7
        //   550: getstatic       com/zhekasmirnov/innercore/mod/build/ExtractionHelper.TEMP_DIR:Ljava/lang/String;
        //   553: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   556: pop            
        //   557: aload           7
        //   559: ldc_w           "setup"
        //   562: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   565: pop            
        //   566: aload           5
        //   568: aload           6
        //   570: aload_2        
        //   571: aload           7
        //   573: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   576: invokestatic    com/zhekasmirnov/innercore/mod/build/ExtractionHelper.extractEntry:(Ljava/util/zip/ZipFile;Ljava/lang/String;Ljava/lang/String;Ljava/lang/String;)V
        //   579: aload_1        
        //   580: ldc_w           "running setup script"
        //   583: invokeinterface com/zhekasmirnov/innercore/utils/IMessageReceiver.message:(Ljava/lang/String;)V
        //   588: aload           5
        //   590: aload           6
        //   592: new             Ljava/io/File;
        //   595: dup            
        //   596: getstatic       com/zhekasmirnov/innercore/mod/build/ExtractionHelper.TEMP_DIR:Ljava/lang/String;
        //   599: ldc_w           "setup"
        //   602: invokespecial   java/io/File.<init>:(Ljava/lang/String;Ljava/lang/String;)V
        //   605: aload_0        
        //   606: aload_1        
        //   607: invokestatic    com/zhekasmirnov/innercore/mod/build/ExtractionHelper.runSetupScript:(Ljava/util/zip/ZipFile;Ljava/lang/String;Ljava/io/File;Ljava/lang/String;Lcom/zhekasmirnov/innercore/utils/IMessageReceiver;)V
        //   610: goto            744
        //   613: astore_2       
        //   614: aload_2        
        //   615: astore_0       
        //   616: aload_2        
        //   617: invokevirtual   java/lang/Throwable.getCause:()Ljava/lang/Throwable;
        //   620: ifnull          628
        //   623: aload_2        
        //   624: invokevirtual   java/lang/Throwable.getCause:()Ljava/lang/Throwable;
        //   627: astore_0       
        //   628: new             Ljava/lang/StringBuilder;
        //   631: dup            
        //   632: invokespecial   java/lang/StringBuilder.<init>:()V
        //   635: astore_2       
        //   636: aload_2        
        //   637: ldc_w           "failed to run setup script: "
        //   640: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   643: pop            
        //   644: aload_2        
        //   645: aload_0        
        //   646: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   649: pop            
        //   650: aload_1        
        //   651: aload_2        
        //   652: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   655: invokeinterface com/zhekasmirnov/innercore/utils/IMessageReceiver.message:(Ljava/lang/String;)V
        //   660: ldc             Lcom/zhekasmirnov/innercore/mod/build/ExtractionHelper;.class
        //   662: monitorexit    
        //   663: aconst_null    
        //   664: areturn        
        //   665: astore_0       
        //   666: new             Ljava/lang/StringBuilder;
        //   669: dup            
        //   670: invokespecial   java/lang/StringBuilder.<init>:()V
        //   673: astore_2       
        //   674: aload_2        
        //   675: ldc_w           "failed to extract setup script: "
        //   678: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   681: pop            
        //   682: aload_2        
        //   683: aload_0        
        //   684: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   687: pop            
        //   688: aload_1        
        //   689: aload_2        
        //   690: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   693: invokeinterface com/zhekasmirnov/innercore/utils/IMessageReceiver.message:(Ljava/lang/String;)V
        //   698: ldc             Lcom/zhekasmirnov/innercore/mod/build/ExtractionHelper;.class
        //   700: monitorexit    
        //   701: aconst_null    
        //   702: areturn        
        //   703: new             Ljava/lang/StringBuilder;
        //   706: dup            
        //   707: invokespecial   java/lang/StringBuilder.<init>:()V
        //   710: astore_2       
        //   711: aload_2        
        //   712: ldc_w           "extracting mod to ...mods/"
        //   715: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   718: pop            
        //   719: aload_2        
        //   720: aload_0        
        //   721: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   724: pop            
        //   725: aload_1        
        //   726: aload_2        
        //   727: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   730: invokeinterface com/zhekasmirnov/innercore/utils/IMessageReceiver.message:(Ljava/lang/String;)V
        //   735: aload           5
        //   737: aload           6
        //   739: aload_0        
        //   740: invokestatic    com/zhekasmirnov/innercore/mod/build/ExtractionHelper.extractAs:(Ljava/util/zip/ZipFile;Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
        //   743: pop            
        //   744: aload_0        
        //   745: putstatic       com/zhekasmirnov/innercore/mod/build/ExtractionHelper.lastLocation:Ljava/lang/String;
        //   748: getstatic       com/zhekasmirnov/innercore/mod/build/ExtractionHelper.extractionPathList:Ljava/util/ArrayList;
        //   751: astore_0       
        //   752: ldc             Lcom/zhekasmirnov/innercore/mod/build/ExtractionHelper;.class
        //   754: monitorexit    
        //   755: aload_0        
        //   756: areturn        
        //   757: astore_0       
        //   758: new             Ljava/lang/StringBuilder;
        //   761: dup            
        //   762: invokespecial   java/lang/StringBuilder.<init>:()V
        //   765: astore_2       
        //   766: aload_2        
        //   767: ldc_w           "failed to extract mod archive: "
        //   770: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   773: pop            
        //   774: aload_2        
        //   775: aload_0        
        //   776: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   779: pop            
        //   780: aload_1        
        //   781: aload_2        
        //   782: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   785: invokeinterface com/zhekasmirnov/innercore/utils/IMessageReceiver.message:(Ljava/lang/String;)V
        //   790: ldc             Lcom/zhekasmirnov/innercore/mod/build/ExtractionHelper;.class
        //   792: monitorexit    
        //   793: aconst_null    
        //   794: areturn        
        //   795: astore_0       
        //   796: new             Ljava/lang/StringBuilder;
        //   799: dup            
        //   800: invokespecial   java/lang/StringBuilder.<init>:()V
        //   803: astore_2       
        //   804: aload_2        
        //   805: ldc_w           "io exception occurred: "
        //   808: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   811: pop            
        //   812: aload_2        
        //   813: aload_0        
        //   814: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   817: pop            
        //   818: aload_1        
        //   819: aload_2        
        //   820: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   823: invokeinterface com/zhekasmirnov/innercore/utils/IMessageReceiver.message:(Ljava/lang/String;)V
        //   828: aload_0        
        //   829: invokevirtual   java/io/IOException.printStackTrace:()V
        //   832: ldc             Lcom/zhekasmirnov/innercore/mod/build/ExtractionHelper;.class
        //   834: monitorexit    
        //   835: aconst_null    
        //   836: areturn        
        //   837: astore_0       
        //   838: new             Ljava/lang/StringBuilder;
        //   841: dup            
        //   842: invokespecial   java/lang/StringBuilder.<init>:()V
        //   845: astore_2       
        //   846: aload_2        
        //   847: ldc_w           "mod archive is corrupt: "
        //   850: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   853: pop            
        //   854: aload_2        
        //   855: aload_0        
        //   856: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/Object;)Ljava/lang/StringBuilder;
        //   859: pop            
        //   860: aload_1        
        //   861: aload_2        
        //   862: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   865: invokeinterface com/zhekasmirnov/innercore/utils/IMessageReceiver.message:(Ljava/lang/String;)V
        //   870: aload_0        
        //   871: invokevirtual   java/util/zip/ZipException.printStackTrace:()V
        //   874: ldc             Lcom/zhekasmirnov/innercore/mod/build/ExtractionHelper;.class
        //   876: monitorexit    
        //   877: aconst_null    
        //   878: areturn        
        //   879: astore_0       
        //   880: ldc             Lcom/zhekasmirnov/innercore/mod/build/ExtractionHelper;.class
        //   882: monitorexit    
        //   883: aload_0        
        //   884: athrow         
        //   885: iload_3        
        //   886: iconst_1       
        //   887: iadd           
        //   888: istore_3       
        //   889: goto            229
        //   892: goto            485
        //    Signature:
        //  (Ljava/io/File;Lcom/zhekasmirnov/innercore/utils/IMessageReceiver;Ljava/lang/Runnable;)Ljava/util/ArrayList<Ljava/lang/String;>;
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                        
        //  -----  -----  -----  -----  ----------------------------
        //  3      47     879    885    Any
        //  47     70     837    879    Ljava/util/zip/ZipException;
        //  47     70     795    837    Ljava/io/IOException;
        //  47     70     879    885    Any
        //  73     83     837    879    Ljava/util/zip/ZipException;
        //  73     83     795    837    Ljava/io/IOException;
        //  73     83     879    885    Any
        //  83     92     879    885    Any
        //  97     105    879    885    Any
        //  110    227    879    885    Any
        //  241    271    879    885    Any
        //  277    319    322    332    Ljava/lang/Exception;
        //  277    319    879    885    Any
        //  324    329    879    885    Any
        //  332    370    879    885    Any
        //  375    384    879    885    Any
        //  388    394    879    885    Any
        //  394    429    879    885    Any
        //  437    443    879    885    Any
        //  443    451    879    885    Any
        //  454    482    879    885    Any
        //  485    535    879    885    Any
        //  539    579    665    703    Ljava/lang/Exception;
        //  539    579    879    885    Any
        //  579    588    879    885    Any
        //  588    610    613    665    Ljava/lang/Throwable;
        //  588    610    879    885    Any
        //  616    628    879    885    Any
        //  628    660    879    885    Any
        //  666    698    879    885    Any
        //  703    744    757    795    Ljava/io/IOException;
        //  703    744    879    885    Any
        //  744    752    879    885    Any
        //  758    790    879    885    Any
        //  796    832    879    885    Any
        //  838    874    879    885    Any
        // 
        // The error that occurred was:
        // 
        // java.lang.NullPointerException
        //     at com.strobel.assembler.ir.StackMappingVisitor.push(StackMappingVisitor.java:290)
        //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.execute(StackMappingVisitor.java:833)
        //     at com.strobel.assembler.ir.StackMappingVisitor$InstructionAnalyzer.visit(StackMappingVisitor.java:398)
        //     at com.strobel.decompiler.ast.AstBuilder.performStackAnalysis(AstBuilder.java:2030)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:108)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:210)
        //     at com.strobel.decompiler.languages.java.ast.AstMethodBodyBuilder.createMethodBody(AstMethodBodyBuilder.java:99)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethodBody(AstBuilder.java:782)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createMethod(AstBuilder.java:675)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addTypeMembers(AstBuilder.java:552)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeCore(AstBuilder.java:519)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createTypeNoCache(AstBuilder.java:161)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.createType(AstBuilder.java:150)
        //     at com.strobel.decompiler.languages.java.ast.AstBuilder.addType(AstBuilder.java:125)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.buildAst(JavaLanguage.java:71)
        //     at com.strobel.decompiler.languages.java.JavaLanguage.decompileType(JavaLanguage.java:59)
        //     at us.deathmarine.luyten.FileSaver.doSaveJarDecompiled(FileSaver.java:192)
        //     at us.deathmarine.luyten.FileSaver.access$300(FileSaver.java:45)
        //     at us.deathmarine.luyten.FileSaver$4.run(FileSaver.java:112)
        //     at java.lang.Thread.run(Unknown Source)
        // 
        throw new IllegalStateException("An error occurred while decompiling this method.");
    }
    
    private static String getFreeLocation(final String s) {
        final DirectorySetRequestHandler requestHandler = ModPackContext.getInstance().getCurrentModPack().getRequestHandler(ModPackDirectory.DirectoryType.MODS);
        File file = requestHandler.get(s);
        int n = 0;
        while (file.exists()) {
            final StringBuilder sb = new StringBuilder();
            sb.append(s);
            sb.append("-");
            ++n;
            sb.append(n);
            file = requestHandler.get(sb.toString());
        }
        if (n == 0) {
            return s;
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(s);
        sb2.append("-");
        sb2.append(n);
        return sb2.toString();
    }
    
    private static File getInstallationPath(final String s) {
        return ModPackContext.getInstance().getCurrentModPack().getRequestHandler(ModPackDirectory.DirectoryType.MODS).get(s);
    }
    
    public static String getLastLocation() {
        return ExtractionHelper.lastLocation;
    }
    
    static void runSetupScript(final ZipFile zipFile, final String s, final File file, final String s2, final IMessageReceiver messageReceiver) throws Exception {
        final Executable compileReader = Compiler.compileReader(new FileReader(file), new CompilerConfig(null));
        final ScriptableObject scope = compileReader.getScope();
        scope.put("extractAs", (Scriptable)scope, (Object)new ScriptableFunctionImpl() {
            public Object call(final Context context, final Scriptable scriptable, final Scriptable scriptable2, final Object[] array) {
                String val$defaultDir;
                if (array.length > 0) {
                    val$defaultDir = (String)array[0];
                }
                else {
                    val$defaultDir = null;
                }
                Label_0031: {
                    if (val$defaultDir != null) {
                        break Label_0031;
                    }
                    try {
                        val$defaultDir = s2;
                        final IMessageReceiver val$logger = messageReceiver;
                        final StringBuilder sb = new StringBuilder();
                        sb.append("extracting mod to ...mods/");
                        sb.append(val$defaultDir);
                        val$logger.message(sb.toString());
                        return ExtractionHelper.extractAs(zipFile, s, val$defaultDir);
                    }
                    catch (IOException ex) {
                        throw new RuntimeException(ex);
                    }
                }
            }
        });
        scope.put("unpack", (Scriptable)scope, (Object)new ScriptableFunctionImpl() {
            public Object call(final Context context, final Scriptable scriptable, final Scriptable scriptable2, final Object[] array) {
                try {
                    ExtractionHelper.extractEntry(zipFile, s, String.valueOf(array[0]), String.valueOf(array[1]));
                    return null;
                }
                catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        scope.put("log", (Scriptable)scope, (Object)new ScriptableFunctionImpl() {
            public Object call(final Context context, final Scriptable scriptable, final Scriptable scriptable2, final Object[] array) {
                final StringBuilder sb = new StringBuilder();
                for (int length = array.length, i = 0; i < length; ++i) {
                    sb.append(array[i]);
                    sb.append(" ");
                }
                messageReceiver.message(sb.toString());
                return null;
            }
        });
        scope.put("print", (Scriptable)scope, (Object)new ScriptableFunctionImpl() {
            public Object call(final Context context, final Scriptable scriptable, final Scriptable scriptable2, final Object[] array) {
                for (int length = array.length, i = 0; i < length; ++i) {
                    final Object o = array[i];
                    final StringBuilder sb = new StringBuilder();
                    sb.append(o);
                    sb.append("");
                    PrintStacking.print(sb.toString());
                }
                return null;
            }
        });
        final StringBuilder sb = new StringBuilder();
        sb.append(FileTools.DIR_WORK);
        sb.append("mods/");
        scope.put("__modsdir__", (Scriptable)scope, (Object)sb.toString());
        scope.put("__subpath__", (Scriptable)scope, (Object)s);
        compileReader.run();
        final Throwable lastRunException = compileReader.getLastRunException();
        if (lastRunException != null) {
            throw new RuntimeException(lastRunException);
        }
    }
    
    static String searchForSubPath(final ZipFile zipFile, final String s) {
        final Enumeration<? extends ZipEntry> entries = zipFile.entries();
        while (entries.hasMoreElements()) {
            final ZipEntry zipEntry = (ZipEntry)entries.nextElement();
            if (zipEntry == null) {
                break;
            }
            final String name = zipEntry.getName();
            final StringBuilder sb = new StringBuilder();
            sb.append("searching: ");
            sb.append(name);
            Logger.debug("DEBUG", sb.toString());
            if (name.endsWith(s)) {
                return name.substring(0, name.length() - s.length());
            }
        }
        return null;
    }
}
