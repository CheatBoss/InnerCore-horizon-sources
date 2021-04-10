package com.microsoft.xbox.xle.model;

import com.microsoft.xbox.service.model.*;
import com.microsoft.xbox.service.model.serialization.*;
import com.microsoft.xbox.xle.app.*;
import android.os.*;
import java.util.*;
import com.microsoft.xbox.toolkit.network.*;
import com.microsoft.xbox.toolkit.*;

public class SystemSettingsModel extends ModelBase<Version>
{
    private final HashSet<String> hiddenMruItems;
    private int latestVersion;
    private String marketUrl;
    private int minRequiredOSVersion;
    private int minVersion;
    private int[] remoteControlSpecialTitleIds;
    private SmartglassSettings smartglassSettings;
    private final SingleEntryLoadingStatus smartglassSettingsLoadingStatus;
    private OnUpdateExistListener updateExistListener;
    
    private SystemSettingsModel() {
        this.minRequiredOSVersion = 0;
        this.minVersion = 0;
        this.latestVersion = 0;
        this.hiddenMruItems = new HashSet<String>();
        this.smartglassSettingsLoadingStatus = new SingleEntryLoadingStatus();
    }
    
    public static SystemSettingsModel getInstance() {
        return SystemSettingsModelContainer.instance;
    }
    
    private int getMinimumVersion() {
        return this.minVersion;
    }
    
    private void onGetSmartglassSettingsCompleted(final AsyncResult<SmartglassSettings> asyncResult) {
        XLEAssert.assertTrue(Thread.currentThread() == ThreadManager.UIThread);
        if (asyncResult.getStatus() == AsyncActionStatus.SUCCESS) {
            final SmartglassSettings smartglassSettings = asyncResult.getResult();
            if ((this.smartglassSettings = smartglassSettings) != null) {
                this.minRequiredOSVersion = smartglassSettings.ANDROID_VERSIONMINOS;
                this.minVersion = this.smartglassSettings.ANDROID_VERSIONMIN;
                this.latestVersion = this.smartglassSettings.ANDROID_VERSIONLATEST;
                this.marketUrl = this.smartglassSettings.ANDROID_VERSIONURL;
                this.populateHiddenMruItems(this.smartglassSettings.HIDDEN_MRU_ITEMS);
                this.populateRemoteControlSpecialTitleIds(this.smartglassSettings.REMOTE_CONTROL_SPECIALS);
                if (this.updateExistListener != null) {
                    if (this.getMustUpdate(ProjectSpecificDataProvider.getInstance().getVersionCode())) {
                        this.updateExistListener.onMustUpdate();
                        return;
                    }
                    if (this.getHasUpdate(ProjectSpecificDataProvider.getInstance().getVersionCode())) {
                        this.updateExistListener.onOptionalUpdate();
                    }
                }
            }
        }
    }
    
    private void populateHiddenMruItems(final String s) {
        this.hiddenMruItems.clear();
        if (s != null) {
            final String[] split = s.split(",");
            if (split != null) {
                for (int length = split.length, i = 0; i < length; ++i) {
                    this.hiddenMruItems.add(split[i]);
                }
            }
        }
    }
    
    private void populateRemoteControlSpecialTitleIds(final String p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: ifnull          76
        //     4: aload_1        
        //     5: ldc             ","
        //     7: invokevirtual   java/lang/String.split:(Ljava/lang/String;)[Ljava/lang/String;
        //    10: astore_1       
        //    11: aload_1        
        //    12: ifnull          76
        //    15: aload_0        
        //    16: aload_1        
        //    17: arraylength    
        //    18: newarray        I
        //    20: putfield        com/microsoft/xbox/xle/model/SystemSettingsModel.remoteControlSpecialTitleIds:[I
        //    23: aload_1        
        //    24: arraylength    
        //    25: istore          5
        //    27: iconst_0       
        //    28: istore_3       
        //    29: iconst_0       
        //    30: istore_2       
        //    31: iload_3        
        //    32: iload           5
        //    34: if_icmpge       76
        //    37: aload_1        
        //    38: iload_3        
        //    39: aaload         
        //    40: astore          6
        //    42: aload           6
        //    44: invokestatic    java/lang/Integer.parseInt:(Ljava/lang/String;)I
        //    47: istore          4
        //    49: goto            57
        //    52: astore          6
        //    54: iconst_0       
        //    55: istore          4
        //    57: aload_0        
        //    58: getfield        com/microsoft/xbox/xle/model/SystemSettingsModel.remoteControlSpecialTitleIds:[I
        //    61: iload_2        
        //    62: iload           4
        //    64: iastore        
        //    65: iload_3        
        //    66: iconst_1       
        //    67: iadd           
        //    68: istore_3       
        //    69: iload_2        
        //    70: iconst_1       
        //    71: iadd           
        //    72: istore_2       
        //    73: goto            31
        //    76: return         
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                             
        //  -----  -----  -----  -----  ---------------------------------
        //  42     49     52     57     Ljava/lang/NumberFormatException;
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
    
    public boolean getHasUpdate(final int n) {
        XLEAssert.assertTrue(Thread.currentThread() == ThreadManager.UIThread);
        return Build$VERSION.SDK_INT >= this.minRequiredOSVersion && this.getLatestVersion() > n;
    }
    
    public int getLatestVersion() {
        return this.latestVersion;
    }
    
    public String getMarketUrl() {
        return this.marketUrl;
    }
    
    public boolean getMustUpdate(final int n) {
        XLEAssert.assertTrue(Thread.currentThread() == ThreadManager.UIThread);
        return Build$VERSION.SDK_INT >= this.minRequiredOSVersion && this.getMinimumVersion() > n;
    }
    
    public int[] getRemoteControlSpecialTitleIds() {
        return this.remoteControlSpecialTitleIds;
    }
    
    public boolean isInHiddenMruItems(final String s) {
        return this.hiddenMruItems.contains(s);
    }
    
    public void loadAsync(final boolean b) {
        XLEAssert.assertTrue(Thread.currentThread() == ThreadManager.UIThread);
        DataLoadUtil.StartLoadFromUI(b, this.lifetime, null, this.smartglassSettingsLoadingStatus, (IDataLoaderRunnable<Object>)new GetSmartglassSettingsRunner(this));
    }
    
    public AsyncResult<SmartglassSettings> loadSystemSettings(final boolean b) {
        return DataLoadUtil.Load(b, this.lifetime, null, this.smartglassSettingsLoadingStatus, (IDataLoaderRunnable<SmartglassSettings>)new GetSmartglassSettingsRunner(this));
    }
    
    public void setOnUpdateExistListener(final OnUpdateExistListener updateExistListener) {
        this.updateExistListener = updateExistListener;
    }
    
    private class GetSmartglassSettingsRunner extends IDataLoaderRunnable<SmartglassSettings>
    {
        private final SystemSettingsModel caller;
        
        public GetSmartglassSettingsRunner(final SystemSettingsModel caller) {
            this.caller = caller;
        }
        
        @Override
        public SmartglassSettings buildData() throws XLEException {
            return null;
        }
        
        @Override
        public long getDefaultErrorCode() {
            return 5001L;
        }
        
        @Override
        public void onPostExcute(final AsyncResult<SmartglassSettings> asyncResult) {
            this.caller.onGetSmartglassSettingsCompleted(asyncResult);
        }
        
        @Override
        public void onPreExecute() {
        }
    }
    
    public interface OnUpdateExistListener
    {
        void onMustUpdate();
        
        void onOptionalUpdate();
    }
    
    private static class SystemSettingsModelContainer
    {
        private static SystemSettingsModel instance;
        
        static {
            SystemSettingsModelContainer.instance = new SystemSettingsModel(null);
        }
    }
}
