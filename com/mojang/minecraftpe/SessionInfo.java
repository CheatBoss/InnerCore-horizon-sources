package com.mojang.minecraftpe;

import java.io.*;
import java.text.*;
import java.util.*;
import android.content.*;
import android.content.pm.*;

public class SessionInfo implements Serializable
{
    private static final String NOT_YET_CONFIGURED = "Not yet configured";
    private static final String SEP = ";";
    public int appVersion;
    public String branchId;
    public String buildId;
    public String commitId;
    public transient Date crashTimestamp;
    public String flavor;
    public String gameVersionName;
    public Date recordDate;
    public String sessionId;
    
    public SessionInfo() {
        this.sessionId = null;
        this.buildId = null;
        this.commitId = null;
        this.branchId = null;
        this.flavor = null;
        this.gameVersionName = null;
        this.appVersion = 0;
        this.recordDate = null;
        this.crashTimestamp = null;
        this.sessionId = "Not yet configured";
        this.buildId = "Not yet configured";
        this.commitId = "Not yet configured";
        this.branchId = "Not yet configured";
        this.flavor = "Not yet configured";
        this.gameVersionName = "Not yet configured";
        this.recordDate = new Date();
    }
    
    public SessionInfo(final String sessionId, final String buildId, final String commitId, final String branchId, final String flavor, final String gameVersionName, final int appVersion, final Date recordDate) {
        this.sessionId = null;
        this.buildId = null;
        this.commitId = null;
        this.branchId = null;
        this.flavor = null;
        this.gameVersionName = null;
        this.appVersion = 0;
        this.recordDate = null;
        this.crashTimestamp = null;
        this.sessionId = sessionId;
        this.buildId = buildId;
        this.commitId = commitId;
        this.branchId = branchId;
        this.flavor = flavor;
        this.gameVersionName = gameVersionName;
        this.appVersion = appVersion;
        this.recordDate = recordDate;
    }
    
    public static SessionInfo fromString(final String p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: dup            
        //     4: invokespecial   com/mojang/minecraftpe/SessionInfo.<init>:()V
        //     7: astore_2       
        //     8: aload_0        
        //     9: ifnull          254
        //    12: aload_0        
        //    13: invokevirtual   java/lang/String.length:()I
        //    16: ifeq            254
        //    19: aload_0        
        //    20: ldc             ";"
        //    22: invokevirtual   java/lang/String.split:(Ljava/lang/String;)[Ljava/lang/String;
        //    25: astore_1       
        //    26: aload_1        
        //    27: arraylength    
        //    28: bipush          8
        //    30: if_icmpne       200
        //    33: aload_2        
        //    34: aload_1        
        //    35: iconst_0       
        //    36: aaload         
        //    37: putfield        com/mojang/minecraftpe/SessionInfo.sessionId:Ljava/lang/String;
        //    40: aload_2        
        //    41: aload_1        
        //    42: iconst_1       
        //    43: aaload         
        //    44: putfield        com/mojang/minecraftpe/SessionInfo.buildId:Ljava/lang/String;
        //    47: aload_2        
        //    48: aload_1        
        //    49: iconst_2       
        //    50: aaload         
        //    51: putfield        com/mojang/minecraftpe/SessionInfo.commitId:Ljava/lang/String;
        //    54: aload_2        
        //    55: aload_1        
        //    56: iconst_3       
        //    57: aaload         
        //    58: putfield        com/mojang/minecraftpe/SessionInfo.branchId:Ljava/lang/String;
        //    61: aload_2        
        //    62: aload_1        
        //    63: iconst_4       
        //    64: aaload         
        //    65: putfield        com/mojang/minecraftpe/SessionInfo.flavor:Ljava/lang/String;
        //    68: aload_2        
        //    69: aload_1        
        //    70: iconst_5       
        //    71: aaload         
        //    72: putfield        com/mojang/minecraftpe/SessionInfo.gameVersionName:Ljava/lang/String;
        //    75: aload_2        
        //    76: aload_1        
        //    77: bipush          6
        //    79: aaload         
        //    80: invokestatic    java/lang/Integer.parseInt:(Ljava/lang/String;)I
        //    83: putfield        com/mojang/minecraftpe/SessionInfo.appVersion:I
        //    86: invokestatic    com/mojang/minecraftpe/SessionInfo.getDateFormat:()Ljava/text/SimpleDateFormat;
        //    89: aload_1        
        //    90: bipush          7
        //    92: aaload         
        //    93: new             Ljava/text/ParsePosition;
        //    96: dup            
        //    97: iconst_0       
        //    98: invokespecial   java/text/ParsePosition.<init>:(I)V
        //   101: invokevirtual   java/text/SimpleDateFormat.parse:(Ljava/lang/String;Ljava/text/ParsePosition;)Ljava/util/Date;
        //   104: astore_1       
        //   105: aload_2        
        //   106: aload_1        
        //   107: putfield        com/mojang/minecraftpe/SessionInfo.recordDate:Ljava/util/Date;
        //   110: aload_1        
        //   111: ifnull          116
        //   114: aload_2        
        //   115: areturn        
        //   116: new             Ljava/lang/StringBuilder;
        //   119: dup            
        //   120: invokespecial   java/lang/StringBuilder.<init>:()V
        //   123: astore_1       
        //   124: aload_1        
        //   125: ldc             "Failed to parse date/time in SessionInfo string '"
        //   127: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   130: pop            
        //   131: aload_1        
        //   132: aload_0        
        //   133: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   136: pop            
        //   137: aload_1        
        //   138: ldc             "'"
        //   140: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   143: pop            
        //   144: new             Ljava/lang/IllegalArgumentException;
        //   147: dup            
        //   148: aload_1        
        //   149: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   152: invokespecial   java/lang/IllegalArgumentException.<init>:(Ljava/lang/String;)V
        //   155: athrow         
        //   156: astore_0       
        //   157: new             Ljava/lang/StringBuilder;
        //   160: dup            
        //   161: invokespecial   java/lang/StringBuilder.<init>:()V
        //   164: astore_0       
        //   165: aload_0        
        //   166: ldc             "Failed to convert app version '"
        //   168: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   171: pop            
        //   172: aload_0        
        //   173: aload_1        
        //   174: bipush          6
        //   176: aaload         
        //   177: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   180: pop            
        //   181: aload_0        
        //   182: ldc             "' into an integer"
        //   184: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   187: pop            
        //   188: new             Ljava/lang/IllegalArgumentException;
        //   191: dup            
        //   192: aload_0        
        //   193: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   196: invokespecial   java/lang/IllegalArgumentException.<init>:(Ljava/lang/String;)V
        //   199: athrow         
        //   200: new             Ljava/lang/StringBuilder;
        //   203: dup            
        //   204: invokespecial   java/lang/StringBuilder.<init>:()V
        //   207: astore_1       
        //   208: aload_1        
        //   209: ldc             "Invalid SessionInfo string '"
        //   211: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   214: pop            
        //   215: aload_1        
        //   216: aload_0        
        //   217: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   220: pop            
        //   221: aload_1        
        //   222: ldc             "', must be 8 parts split by '"
        //   224: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   227: pop            
        //   228: aload_1        
        //   229: ldc             ";"
        //   231: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   234: pop            
        //   235: aload_1        
        //   236: ldc             "'"
        //   238: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   241: pop            
        //   242: new             Ljava/lang/IllegalArgumentException;
        //   245: dup            
        //   246: aload_1        
        //   247: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   250: invokespecial   java/lang/IllegalArgumentException.<init>:(Ljava/lang/String;)V
        //   253: athrow         
        //   254: new             Ljava/lang/IllegalArgumentException;
        //   257: dup            
        //   258: ldc             "Empty SessionInfo string"
        //   260: invokespecial   java/lang/IllegalArgumentException.<init>:(Ljava/lang/String;)V
        //   263: athrow         
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                             
        //  -----  -----  -----  -----  ---------------------------------
        //  75     86     156    200    Ljava/lang/NumberFormatException;
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
    
    public static SimpleDateFormat getDateFormat() {
        final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM/dd/yyyy-HH:mm:ss");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return simpleDateFormat;
    }
    
    public void setContents(final Context context, final String sessionId, final String buildId, final String commitId, final String branchId, final String flavor) {
        this.sessionId = sessionId;
        this.buildId = buildId;
        this.commitId = commitId;
        this.branchId = branchId;
        this.flavor = flavor;
        this.updateJavaConstants(context);
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append(this.sessionId);
        sb.append(";");
        sb.append(this.buildId);
        sb.append(";");
        sb.append(this.commitId);
        sb.append(";");
        sb.append(this.branchId);
        sb.append(";");
        sb.append(this.flavor);
        sb.append(";");
        sb.append(this.gameVersionName);
        sb.append(";");
        sb.append(this.appVersion);
        sb.append(";");
        sb.append(getDateFormat().format(this.recordDate));
        return sb.toString();
    }
    
    public void updateJavaConstants(final Context context) {
        this.appVersion = AppConstants.APP_VERSION;
        try {
            this.gameVersionName = context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionName;
        }
        catch (PackageManager$NameNotFoundException ex) {
            this.gameVersionName = "Not found";
        }
    }
}
