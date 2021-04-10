package com.zhekasmirnov.mcpe161;

import com.android.tools.r8.annotations.*;
import com.zhekasmirnov.horizon.*;
import android.app.*;
import org.mineprogramming.horizon.innercore.inflater.*;
import android.content.*;
import com.zhekasmirnov.horizon.launcher.pack.*;
import com.zhekasmirnov.innercore.utils.*;
import org.mineprogramming.horizon.innercore.util.*;
import org.mineprogramming.horizon.innercore.*;
import com.zhekasmirnov.horizon.modloader.java.*;
import com.zhekasmirnov.horizon.modloader.library.*;
import com.zhekasmirnov.horizon.modloader.resource.directory.*;
import java.util.*;
import com.zhekasmirnov.horizon.launcher.ads.*;
import java.io.*;
import android.widget.*;
import com.zhekasmirnov.innercore.api.log.*;
import com.zhekasmirnov.horizon.modloader.resource.*;
import com.mojang.minecraftpe.packagesource.*;
import com.zhekasmirnov.innercore.mod.resource.types.enums.*;
import com.zhekasmirnov.horizon.modloader.resource.processor.*;
import com.zhekasmirnov.horizon.modloader.resource.runtime.*;

@SynthesizedClassMap({ -$$Lambda$EnvironmentSetup$_CvtdpdRgA2eXajCocXshTAAMgA.class })
public class EnvironmentSetup
{
    private static TextureAtlas blockTextureAtlas;
    private static InnerCore innerCore;
    private static TextureAtlas itemTextureAtlas;
    
    public static void abortLaunchIfRequired(final Pack pack) {
        final Activity topRunningActivity = HorizonApplication.getTopRunningActivity();
        if (topRunningActivity == null) {
            pack.abortLaunch();
        }
        final boolean b = InnerCore.checkLicence(topRunningActivity) ^ true;
        if (b) {
            pack.abortLaunch();
        }
        if (topRunningActivity != null) {
            topRunningActivity.runOnUiThread((Runnable)new Runnable() {
                @Override
                public void run() {
                    if (b) {
                        new AlertDialog$Builder((Context)topRunningActivity, topRunningActivity.getResources().getIdentifier("AppTheme.Dialog", "style", "com.zheka.horizon")).setMessage((CharSequence)JsonInflater.getString((Context)topRunningActivity, "license_warning")).setTitle((CharSequence)JsonInflater.getString((Context)topRunningActivity, "licence_title")).setPositiveButton((CharSequence)JsonInflater.getString((Context)topRunningActivity, "ok"), (DialogInterface$OnClickListener)null).show();
                        return;
                    }
                    final SharedPreferences sharedPreferences = topRunningActivity.getSharedPreferences("InnerCore", 0);
                    if (!sharedPreferences.getBoolean("first_launch_warn", false)) {
                        sharedPreferences.edit().putBoolean("first_launch_warn", true).commit();
                        new AlertDialog$Builder((Context)topRunningActivity, topRunningActivity.getResources().getIdentifier("AppTheme.Dialog", "style", "com.zheka.horizon")).setMessage((CharSequence)JsonInflater.getString((Context)topRunningActivity, "crashy_warning")).setPositiveButton((CharSequence)JsonInflater.getString((Context)topRunningActivity, "ok"), (DialogInterface$OnClickListener)null).show();
                    }
                }
            });
        }
    }
    
    public static void addEnvironmentLibraries(final ArrayList<File> list, final File file) {
        list.add(new File(file, "libminecraftpe.so"));
    }
    
    public static void addMenuActivities(final Pack pack, final ArrayList<Pack$MenuActivityFactory> list) {
        FileTools.initializeDirectories(pack.directory);
        DencityConverter.initializeDensity(pack.getModContext().context);
        list.add((Pack$MenuActivityFactory)new ModsManagerActivityFactory());
        list.add((Pack$MenuActivityFactory)new AboutActivityFactory());
        FileTools.initializeDirectories(pack.getWorkingDirectory());
    }
    
    public static void getAdditionalJavaDirectories(final Pack pack, final ArrayList<JavaDirectory> list) {
        EnvironmentSetup.innerCore.addJavaDirectories(list);
    }
    
    public static void getAdditionalNativeDirectories(final Pack pack, final ArrayList<LibraryDirectory> list) {
        EnvironmentSetup.innerCore.addNativeDirectories(list);
    }
    
    public static void getAdditionalResourceDirectories(final Pack pack, final ArrayList<ResourceDirectory> list) {
        EnvironmentSetup.innerCore.addResourceDirectories(list);
    }
    
    static TextureAtlas getBlockTextureAtlas() {
        return EnvironmentSetup.blockTextureAtlas;
    }
    
    public static Activity getCurrentActivity() {
        return EnvironmentSetup.innerCore.getCurrentActivity();
    }
    
    static TextureAtlas getItemTextureAtlas() {
        return EnvironmentSetup.itemTextureAtlas;
    }
    
    public static void initializePackRelatedAds(final Pack pack, final AdsManager adsManager, final AdDistributionModel adDistributionModel) {
        final PrintStream out = System.out;
        final StringBuilder sb = new StringBuilder();
        sb.append("init pack related ads ");
        sb.append(adDistributionModel);
        out.println(sb.toString());
        final AdDistributionModel$DomainFactory adDistributionModel$DomainFactory = (AdDistributionModel$DomainFactory)new AdDistributionModel$DomainFactory() {
            public AdDomain newDomain() {
                final AdDomain adDomain = new AdDomain(adsManager, "ca-app-pub-3152642364854897~5577139781");
                final ArrayList<AdContainer> list = new ArrayList<AdContainer>();
                list.add(AdContainer.newContainer(adsManager, "interstitial", "ca-app-pub-3152642364854897/5851444283"));
                list.add(AdContainer.newContainer(adsManager, "interstitial", "ca-app-pub-3152642364854897/4538362613"));
                list.add(AdContainer.newContainer(adsManager, "interstitial_video", "ca-app-pub-3152642364854897/7340991056"));
                list.add(AdContainer.newContainer(adsManager, "native", "ca-app-pub-3152642364854897/5696365763"));
                list.add(AdContainer.newContainer(adsManager, "native", "ca-app-pub-3152642364854897/3225280945"));
                list.add(AdContainer.newContainer(adsManager, "native", "ca-app-pub-3152642364854897/9599117602"));
                list.add(AdContainer.newContainer(adsManager, "native", "ca-app-pub-3152642364854897/6874264800"));
                list.add(AdContainer.newContainer(adsManager, "banner", "ca-app-pub-3152642364854897/9788202751"));
                list.add(AdContainer.newContainer(adsManager, "banner", "ca-app-pub-3152642364854897/2140264136"));
                list.add(AdContainer.newContainer(adsManager, "banner", "ca-app-pub-3152642364854897/8283549399"));
                list.add(AdContainer.newContainer(adsManager, "banner", "ca-app-pub-3152642364854897/9790689295"));
                adDomain.addContainers((Collection)list);
                return adDomain;
            }
        };
        final AdDistributionModel$DomainFactory adDistributionModel$DomainFactory2 = (AdDistributionModel$DomainFactory)new AdDistributionModel$DomainFactory() {
            public AdDomain newDomain() {
                final AdDomain adDomain = new AdDomain(adsManager, "ca-app-pub-6874502201951300~7445180251");
                final ArrayList<AdContainer> list = new ArrayList<AdContainer>();
                list.add(AdContainer.newContainer(adsManager, "interstitial", "ca-app-pub-6874502201951300/2109093097"));
                list.add(AdContainer.newContainer(adsManager, "interstitial", "ca-app-pub-6874502201951300/3721557666"));
                list.add(AdContainer.newContainer(adsManager, "native", "ca-app-pub-6874502201951300/6156149310"));
                list.add(AdContainer.newContainer(adsManager, "native", "ca-app-pub-6874502201951300/5584948457"));
                list.add(AdContainer.newContainer(adsManager, "banner", "ca-app-pub-6874502201951300/3613746455"));
                list.add(AdContainer.newContainer(adsManager, "banner", "ca-app-pub-6874502201951300/6539292694"));
                adDomain.addContainers((Collection)list);
                return adDomain;
            }
        };
        final ArrayList<AdDistributionModel$Node> list = new ArrayList<AdDistributionModel$Node>();
        adDistributionModel.addDistributionNode("pack-dev", adDistributionModel.getWeight("innercore:pack-dev:pack-main-menu", 8.0), "pack-main-menu");
        adDistributionModel.addDistributionNode("pack-dev", adDistributionModel.getWeight("innercore:pack-dev:pack-mod-browser", 2.0), "pack-mod-browser");
        adDistributionModel.addDistributionNode("pack-main-menu", adDistributionModel.getWeight("innercore:pack-main-menu:innercore-dev", 60.0), "pack-main-menu:innercore-dev");
        adDistributionModel.addDistributionNode("pack-main-menu", adDistributionModel.getWeight("innercore:pack-main-menu:innercore-support-dev", 30.0), "pack-main-menu:innercore-support-dev");
        adDistributionModel.addDistributionNodeFromConfig("pack-main-menu:innercore-dev", adDistributionModel.getWeight("innercore:innercore-dev:self", 100.0), "pack-main-menu:innercore-dev:self", "innercore-dev", (AdDistributionModel$DomainFactory)adDistributionModel$DomainFactory);
        list.add(adDistributionModel.addDistributionNode("pack-main-menu:innercore-dev", adDistributionModel.getWeight("innercore:innercore-dev:mods", 0.0), "pack-main-menu:innercore-dev:mods"));
        adDistributionModel.addDistributionNodeFromConfig("pack-main-menu:innercore-support-dev", adDistributionModel.getWeight("innercore:innercore-support-dev:self", 100.0), "pack-main-menu:innercore-support-dev:self", "innercore-support-dev", (AdDistributionModel$DomainFactory)adDistributionModel$DomainFactory2);
        list.add(adDistributionModel.addDistributionNode("pack-main-menu:innercore-support-dev", adDistributionModel.getWeight("innercore:innercore-support-dev:mods", 0.0), "pack-main-menu:innercore-support-dev:mods"));
        adDistributionModel.addDistributionNode("pack-mod-browser", adDistributionModel.getWeight("innercore:pack-mod-browser:innercore-dev", 30.0), "pack-mod-browser:innercore-dev");
        adDistributionModel.addDistributionNode("pack-mod-browser", adDistributionModel.getWeight("innercore:pack-mod-browser:innercore-support-dev", 70.0), "pack-mod-browser:innercore-support-dev");
        adDistributionModel.addDistributionNodeFromConfig("pack-mod-browser:innercore-dev", adDistributionModel.getWeight("innercore:innercore-dev:self", 100.0), "pack-mod-browser:innercore-dev:self", "innercore-dev", (AdDistributionModel$DomainFactory)adDistributionModel$DomainFactory);
        list.add(adDistributionModel.addDistributionNode("pack-mod-browser:innercore-dev", adDistributionModel.getWeight("innercore:innercore-dev:mods", 0.0), "pack-mod-browser:innercore-dev:mods"));
        adDistributionModel.addDistributionNodeFromConfig("pack-mod-browser:innercore-support-dev", adDistributionModel.getWeight("innercore:innercore-support-dev:self", 100.0), "pack-mod-browser:innercore-support-dev:self", "innercore-support-dev", (AdDistributionModel$DomainFactory)adDistributionModel$DomainFactory2);
        list.add(adDistributionModel.addDistributionNode("pack-mod-browser:innercore-support-dev", adDistributionModel.getWeight("innercore:innercore-support-dev:mods", 0.0), "pack-mod-browser:innercore-support-dev:mods"));
    }
    
    public static void prepareForInjection(final Pack pack) {
        (EnvironmentSetup.innerCore = new InnerCore(HorizonApplication.getTopRunningActivity(), pack)).load();
    }
    
    public static void prepareForLaunch(final Pack p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     3: dup            
        //     4: aload_0        
        //     5: getfield        com/zhekasmirnov/horizon/launcher/pack/Pack.directory:Ljava/io/File;
        //     8: ldc_w           "worlds"
        //    11: invokespecial   java/io/File.<init>:(Ljava/io/File;Ljava/lang/String;)V
        //    14: invokevirtual   java/io/File.getAbsolutePath:()Ljava/lang/String;
        //    17: invokestatic    com/zhekasmirnov/innercore/api/NativeAPI.setWorldsPathOverride:(Ljava/lang/String;)V
        //    20: new             Ljava/io/File;
        //    23: dup            
        //    24: getstatic       com/zhekasmirnov/innercore/utils/FileTools.DIR_MINECRAFT:Ljava/lang/String;
        //    27: ldc_w           "resource_packs"
        //    30: invokespecial   java/io/File.<init>:(Ljava/lang/String;Ljava/lang/String;)V
        //    33: invokevirtual   java/io/File.getAbsolutePath:()Ljava/lang/String;
        //    36: invokestatic    com/zhekasmirnov/innercore/api/NativeAPI.setResourcePacksPathOverride:(Ljava/lang/String;)V
        //    39: new             Ljava/io/File;
        //    42: dup            
        //    43: getstatic       com/zhekasmirnov/innercore/utils/FileTools.DIR_MINECRAFT:Ljava/lang/String;
        //    46: ldc_w           "behavior_packs"
        //    49: invokespecial   java/io/File.<init>:(Ljava/lang/String;Ljava/lang/String;)V
        //    52: invokevirtual   java/io/File.getAbsolutePath:()Ljava/lang/String;
        //    55: invokestatic    com/zhekasmirnov/innercore/api/NativeAPI.setBehaviorPacksPathOverride:(Ljava/lang/String;)V
        //    58: new             Ljava/io/File;
        //    61: dup            
        //    62: aload_0        
        //    63: getfield        com/zhekasmirnov/horizon/launcher/pack/Pack.directory:Ljava/io/File;
        //    66: ldc_w           ".installation_package"
        //    69: invokespecial   java/io/File.<init>:(Ljava/io/File;Ljava/lang/String;)V
        //    72: astore          4
        //    74: aload           4
        //    76: invokevirtual   java/io/File.isFile:()Z
        //    79: ifeq            88
        //    82: aload           4
        //    84: invokevirtual   java/io/File.delete:()Z
        //    87: pop            
        //    88: new             Ljava/io/File;
        //    91: dup            
        //    92: aload_0        
        //    93: getfield        com/zhekasmirnov/horizon/launcher/pack/Pack.directory:Ljava/io/File;
        //    96: ldc_w           ".nomedia"
        //    99: invokespecial   java/io/File.<init>:(Ljava/io/File;Ljava/lang/String;)V
        //   102: astore_0       
        //   103: aload_0        
        //   104: invokevirtual   java/io/File.exists:()Z
        //   107: ifne            123
        //   110: aload_0        
        //   111: invokevirtual   java/io/File.createNewFile:()Z
        //   114: pop            
        //   115: goto            123
        //   118: astore_0       
        //   119: aload_0        
        //   120: invokevirtual   java/io/IOException.printStackTrace:()V
        //   123: new             Ljava/io/File;
        //   126: dup            
        //   127: invokestatic    android/os/Environment.getExternalStorageDirectory:()Ljava/io/File;
        //   130: ldc_w           "games/horizon/logs"
        //   133: invokespecial   java/io/File.<init>:(Ljava/io/File;Ljava/lang/String;)V
        //   136: astore_0       
        //   137: aload_0        
        //   138: invokevirtual   java/io/File.isDirectory:()Z
        //   141: ifne            154
        //   144: aload_0        
        //   145: invokevirtual   java/io/File.delete:()Z
        //   148: pop            
        //   149: aload_0        
        //   150: invokevirtual   java/io/File.mkdirs:()Z
        //   153: pop            
        //   154: aload_0        
        //   155: invokevirtual   java/io/File.listFiles:()[Ljava/io/File;
        //   158: ifnull          288
        //   161: aload_0        
        //   162: invokevirtual   java/io/File.listFiles:()[Ljava/io/File;
        //   165: astore_0       
        //   166: aload_0        
        //   167: arraylength    
        //   168: istore_2       
        //   169: iconst_0       
        //   170: istore_1       
        //   171: iload_1        
        //   172: iload_2        
        //   173: if_icmpge       288
        //   176: aload_0        
        //   177: iload_1        
        //   178: aaload         
        //   179: astore          4
        //   181: aload           4
        //   183: invokevirtual   java/io/File.getName:()Ljava/lang/String;
        //   186: ldc_w           "crash.txt"
        //   189: invokevirtual   java/lang/String.startsWith:(Ljava/lang/String;)Z
        //   192: ifeq            281
        //   195: aload           4
        //   197: invokestatic    com/zhekasmirnov/horizon/util/FileUtils.readFileText:(Ljava/io/File;)Ljava/lang/String;
        //   200: invokevirtual   java/lang/String.hashCode:()I
        //   203: istore_3       
        //   204: aload           4
        //   206: invokevirtual   java/io/File.getParent:()Ljava/lang/String;
        //   209: astore          5
        //   211: new             Ljava/lang/StringBuilder;
        //   214: dup            
        //   215: invokespecial   java/lang/StringBuilder.<init>:()V
        //   218: astore          6
        //   220: aload           6
        //   222: ldc_w           "archived-crash-"
        //   225: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   228: pop            
        //   229: aload           6
        //   231: iload_3        
        //   232: invokevirtual   java/lang/StringBuilder.append:(I)Ljava/lang/StringBuilder;
        //   235: pop            
        //   236: aload           6
        //   238: ldc_w           ".txt"
        //   241: invokevirtual   java/lang/StringBuilder.append:(Ljava/lang/String;)Ljava/lang/StringBuilder;
        //   244: pop            
        //   245: aload           4
        //   247: new             Ljava/io/File;
        //   250: dup            
        //   251: aload           5
        //   253: aload           6
        //   255: invokevirtual   java/lang/StringBuilder.toString:()Ljava/lang/String;
        //   258: invokespecial   java/io/File.<init>:(Ljava/lang/String;Ljava/lang/String;)V
        //   261: invokevirtual   java/io/File.renameTo:(Ljava/io/File;)Z
        //   264: pop            
        //   265: goto            281
        //   268: astore          5
        //   270: aload           5
        //   272: invokevirtual   java/io/IOException.printStackTrace:()V
        //   275: aload           4
        //   277: invokevirtual   java/io/File.delete:()Z
        //   280: pop            
        //   281: iload_1        
        //   282: iconst_1       
        //   283: iadd           
        //   284: istore_1       
        //   285: goto            171
        //   288: return         
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                 
        //  -----  -----  -----  -----  ---------------------
        //  110    115    118    123    Ljava/io/IOException;
        //  195    265    268    281    Ljava/io/IOException;
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
    
    private static void reportResourceSetupError(final String s, final Throwable t) {
        final Activity topRunningActivity = HorizonApplication.getTopRunningActivity();
        if (topRunningActivity != null) {
            topRunningActivity.runOnUiThread((Runnable)new -$$Lambda$EnvironmentSetup$_CvtdpdRgA2eXajCocXshTAAMgA(topRunningActivity, s));
            ICLog.e("Resource-Setup-Error", s, t);
        }
        t.printStackTrace();
    }
    
    public static void setupResourceManager(final ResourceManager resourceManager) {
        ICLog.i("DEBUG", PackageSourceFactory.class.toString());
        resourceManager.addResourceProcessor((ResourceProcessor)(EnvironmentSetup.blockTextureAtlas = new TextureAtlas(TextureType.BLOCK, "textures/terrain_texture.json", "block-atlas-descriptor", "terrain-atlas")));
        resourceManager.addRuntimeResourceHandler((RuntimeResourceHandler)EnvironmentSetup.blockTextureAtlas);
        resourceManager.addResourceProcessor((ResourceProcessor)(EnvironmentSetup.itemTextureAtlas = new TextureAtlas(TextureType.ITEM, "textures/item_texture.json", "item-atlas-descriptor", "items-opaque")));
        resourceManager.addRuntimeResourceHandler((RuntimeResourceHandler)EnvironmentSetup.itemTextureAtlas);
        try {
            final FlipbookTextureAtlas flipbookTextureAtlas = new FlipbookTextureAtlas("textures/flipbook_textures.json", "flipbook-texture-descriptor");
            resourceManager.addResourceProcessor((ResourceProcessor)flipbookTextureAtlas);
            resourceManager.addRuntimeResourceHandler((RuntimeResourceHandler)flipbookTextureAtlas);
        }
        catch (Throwable t) {
            reportResourceSetupError("Failed to initialize flipbook texture atlas descriptor", t);
        }
        try {
            final MaterialProcessor materialProcessor = new MaterialProcessor("materials/entity.material", "material-override-entity", "custom-materials", "custom-shaders");
            resourceManager.addRuntimeResourceHandler((RuntimeResourceHandler)materialProcessor.newShaderUniformList("uniforms.json", "shader-uniforms-override"));
            resourceManager.addResourceProcessor((ResourceProcessor)materialProcessor);
            resourceManager.addRuntimeResourceHandler((RuntimeResourceHandler)materialProcessor);
        }
        catch (Throwable t2) {
            reportResourceSetupError("Failed to initialize material processor", t2);
        }
        try {
            final ContentProcessor contentProcessor = new ContentProcessor();
            resourceManager.addResourceProcessor((ResourceProcessor)contentProcessor);
            resourceManager.addRuntimeResourceHandler((RuntimeResourceHandler)contentProcessor);
            resourceManager.addResourcePrefixes(new String[] { "resource_packs/vanilla/" });
        }
        catch (Throwable t3) {
            reportResourceSetupError("Failed to initialize main content manager, you should restart app", t3);
        }
    }
}
