package com.zhekasmirnov.innercore.api;

import android.util.*;
import com.zhekasmirnov.innercore.api.mod.ui.*;
import java.io.*;
import com.zhekasmirnov.innercore.utils.*;
import org.mozilla.javascript.annotations.*;
import com.zhekasmirnov.apparatus.minecraft.version.*;
import org.mozilla.javascript.*;
import com.zhekasmirnov.innercore.api.log.*;
import java.util.*;
import com.zhekasmirnov.innercore.api.unlimited.*;
import com.zhekasmirnov.innercore.api.commontypes.*;
import android.graphics.*;
import com.zhekasmirnov.innercore.mod.resource.*;

public class NativeItemModel
{
    private static final List<Pair<NativeItemModel, IIconRebuildListener>> asyncReloadQueue;
    private static Thread asyncReloadThread;
    private static final ArrayList<NativeRenderMesh> itemModelMeshPool;
    private static final HashMap<Long, NativeItemModel> modelByPointer;
    public final int data;
    private final Object iconLock;
    public final int id;
    private boolean isIconBitmapDirty;
    public boolean isLazyLoading;
    public boolean isOccupied;
    private boolean isSpriteMeshDirty;
    private boolean isUiModelDirty;
    private boolean isWorldModelDirty;
    private String mCacheGroup;
    private String mCacheKey;
    private String mCachePath;
    private GuiBlockModel mGuiBlockModel;
    private GuiRenderMesh mGuiRenderMesh;
    private Bitmap mIconBitmap;
    private boolean mIsItemTexturePathForced;
    private Bitmap mItemTextureBitmapOverride;
    private String mItemTexturePath;
    private IOverrideCallback mModelOverrideCallback;
    private NativeRenderMesh mSpriteMesh;
    private NativeBlockModel mUiBlockModel;
    private String mUiGlintMaterialName;
    private NativeICRender.Model mUiIcRender;
    private String mUiMaterialName;
    private NativeRenderMesh mUiRenderMesh;
    private String mUiTextureName;
    private NativeBlockModel mWorldBlockModel;
    private GuiBlockModel mWorldCompiledBlockModel;
    private String mWorldGlintMaterialName;
    private NativeICRender.Model mWorldIcRender;
    private String mWorldMaterialName;
    private NativeRenderMesh mWorldRenderMesh;
    private String mWorldTextureName;
    public final long pointer;
    public final NativeShaderUniformSet shaderUniformSet;
    
    static {
        modelByPointer = new HashMap<Long, NativeItemModel>();
        asyncReloadQueue = new ArrayList<Pair<NativeItemModel, IIconRebuildListener>>();
        NativeItemModel.asyncReloadThread = null;
        itemModelMeshPool = new ArrayList<NativeRenderMesh>();
    }
    
    protected NativeItemModel() {
        this.isOccupied = false;
        this.mUiRenderMesh = null;
        this.mUiIcRender = null;
        this.mUiBlockModel = null;
        this.mUiTextureName = null;
        this.mUiMaterialName = null;
        this.mUiGlintMaterialName = null;
        this.mWorldRenderMesh = null;
        this.mWorldIcRender = null;
        this.mWorldBlockModel = null;
        this.mWorldTextureName = null;
        this.mWorldMaterialName = null;
        this.mWorldGlintMaterialName = null;
        this.isUiModelDirty = false;
        this.mGuiBlockModel = null;
        this.mGuiRenderMesh = null;
        this.isWorldModelDirty = false;
        this.mWorldCompiledBlockModel = null;
        this.mCacheKey = null;
        this.mCacheGroup = null;
        this.mCachePath = null;
        this.mItemTexturePath = null;
        this.mIsItemTexturePathForced = false;
        this.mItemTextureBitmapOverride = null;
        this.isSpriteMeshDirty = false;
        this.mSpriteMesh = null;
        this.mModelOverrideCallback = null;
        this.isLazyLoading = false;
        this.iconLock = new Object();
        this.isIconBitmapDirty = false;
        this.mIconBitmap = null;
        this.pointer = nativeConstructStandalone();
        this.data = 0;
        this.id = 0;
        this.shaderUniformSet = null;
    }
    
    protected NativeItemModel(final long pointer, final int id, final int data) {
        this.isOccupied = false;
        this.mUiRenderMesh = null;
        this.mUiIcRender = null;
        this.mUiBlockModel = null;
        this.mUiTextureName = null;
        this.mUiMaterialName = null;
        this.mUiGlintMaterialName = null;
        this.mWorldRenderMesh = null;
        this.mWorldIcRender = null;
        this.mWorldBlockModel = null;
        this.mWorldTextureName = null;
        this.mWorldMaterialName = null;
        this.mWorldGlintMaterialName = null;
        this.isUiModelDirty = false;
        this.mGuiBlockModel = null;
        this.mGuiRenderMesh = null;
        this.isWorldModelDirty = false;
        this.mWorldCompiledBlockModel = null;
        this.mCacheKey = null;
        this.mCacheGroup = null;
        this.mCachePath = null;
        this.mItemTexturePath = null;
        this.mIsItemTexturePathForced = false;
        this.mItemTextureBitmapOverride = null;
        this.isSpriteMeshDirty = false;
        this.mSpriteMesh = null;
        this.mModelOverrideCallback = null;
        this.isLazyLoading = false;
        this.iconLock = new Object();
        this.isIconBitmapDirty = false;
        this.mIconBitmap = null;
        this.pointer = pointer;
        this.id = id;
        this.data = data;
        this.shaderUniformSet = new NativeShaderUniformSet(nativeGetShaderUniformSet(pointer));
        this.mCacheGroup = ItemModelCacheManager.getSingleton().getCurrentCacheGroup();
    }
    
    private void addBoxToSpriteMesh(final NativeRenderMesh nativeRenderMesh, final float n, final float n2, final float n3, float n4, float n5, float n6, final float n7, final float n8) {
        n4 /= 2.0f;
        n5 /= 2.0f;
        n6 /= 2.0f;
        nativeRenderMesh.setNormal(0.0f, -1.0f, 0.0f);
        nativeRenderMesh.addVertex(n + n4, n2 - n5, n3 - n6, n7, n8);
        nativeRenderMesh.addVertex(n + n4, n2 - n5, n3 + n6, n7, n8);
        nativeRenderMesh.addVertex(n - n4, n2 - n5, n3 + n6, n7, n8);
        nativeRenderMesh.addVertex(n - n4, n2 - n5, n3 + n6, n7, n8);
        nativeRenderMesh.addVertex(n - n4, n2 - n5, n3 - n6, n7, n8);
        nativeRenderMesh.addVertex(n + n4, n2 - n5, n3 - n6, n7, n8);
        nativeRenderMesh.setNormal(0.0f, 1.0f, 0.0f);
        nativeRenderMesh.addVertex(n + n4, n2 + n5, n3 - n6, n7, n8);
        nativeRenderMesh.addVertex(n + n4, n2 + n5, n3 + n6, n7, n8);
        nativeRenderMesh.addVertex(n - n4, n2 + n5, n3 + n6, n7, n8);
        nativeRenderMesh.addVertex(n - n4, n2 + n5, n3 + n6, n7, n8);
        nativeRenderMesh.addVertex(n - n4, n2 + n5, n3 - n6, n7, n8);
        nativeRenderMesh.addVertex(n + n4, n2 + n5, n3 - n6, n7, n8);
        nativeRenderMesh.setNormal(-1.0f, 0.0f, 0.0f);
        nativeRenderMesh.addVertex(n - n4, n2 - n5, n3 - n6, n7, n8);
        nativeRenderMesh.addVertex(n - n4, n2 + n5, n3 - n6, n7, n8);
        nativeRenderMesh.addVertex(n - n4, n2 + n5, n3 + n6, n7, n8);
        nativeRenderMesh.addVertex(n - n4, n2 + n5, n3 + n6, n7, n8);
        nativeRenderMesh.addVertex(n - n4, n2 - n5, n3 + n6, n7, n8);
        nativeRenderMesh.addVertex(n - n4, n2 - n5, n3 - n6, n7, n8);
        nativeRenderMesh.setNormal(1.0f, 0.0f, 0.0f);
        nativeRenderMesh.addVertex(n + n4, n2 - n5, n3 - n6, n7, n8);
        nativeRenderMesh.addVertex(n + n4, n2 + n5, n3 - n6, n7, n8);
        nativeRenderMesh.addVertex(n + n4, n2 + n5, n3 + n6, n7, n8);
        nativeRenderMesh.addVertex(n + n4, n2 + n5, n3 + n6, n7, n8);
        nativeRenderMesh.addVertex(n + n4, n2 - n5, n3 + n6, n7, n8);
        nativeRenderMesh.addVertex(n + n4, n2 - n5, n3 - n6, n7, n8);
        nativeRenderMesh.setNormal(0.0f, 0.0f, -1.0f);
        nativeRenderMesh.addVertex(n - n4, n2 - n5, n3 - n6, n7, n8);
        nativeRenderMesh.addVertex(n - n4, n2 + n5, n3 - n6, n7, n8);
        nativeRenderMesh.addVertex(n + n4, n2 + n5, n3 - n6, n7, n8);
        nativeRenderMesh.addVertex(n + n4, n2 + n5, n3 - n6, n7, n8);
        nativeRenderMesh.addVertex(n + n4, n2 - n5, n3 - n6, n7, n8);
        nativeRenderMesh.addVertex(n - n4, n2 - n5, n3 - n6, n7, n8);
        nativeRenderMesh.setNormal(0.0f, 0.0f, 1.0f);
        nativeRenderMesh.addVertex(n - n4, n2 - n5, n3 + n6, n7, n8);
        nativeRenderMesh.addVertex(n - n4, n2 + n5, n3 + n6, n7, n8);
        nativeRenderMesh.addVertex(n + n4, n2 + n5, n3 + n6, n7, n8);
        nativeRenderMesh.addVertex(n + n4, n2 + n5, n3 + n6, n7, n8);
        nativeRenderMesh.addVertex(n + n4, n2 - n5, n3 + n6, n7, n8);
        nativeRenderMesh.addVertex(n - n4, n2 - n5, n3 + n6, n7, n8);
    }
    
    private Bitmap cacheAndReturn(final Bitmap bitmap) {
        if (this.mCachePath != null) {
            FileTools.writeBitmap(new File(this.mCachePath).getAbsolutePath(), bitmap);
        }
        return bitmap;
    }
    
    @JSStaticFunction
    public static Collection<NativeItemModel> getAllModels() {
        return NativeItemModel.modelByPointer.values();
    }
    
    public static NativeItemModel getByPointer(final long n) {
        return NativeItemModel.modelByPointer.get(n);
    }
    
    @JSStaticFunction
    public static NativeRenderMesh getEmptyMeshFromPool() {
        synchronized (NativeItemModel.itemModelMeshPool) {
            if (NativeItemModel.itemModelMeshPool.size() > 0) {
                final NativeRenderMesh nativeRenderMesh = NativeItemModel.itemModelMeshPool.remove(0);
                nativeRenderMesh.clear();
                return nativeRenderMesh;
            }
            return new NativeRenderMesh();
        }
    }
    
    @JSStaticFunction
    public static NativeItemModel getFor(final int n, final int n2) {
        final long nativeGet = nativeGetFor(n, n2);
        NativeItemModel nativeItemModel;
        if ((nativeItemModel = NativeItemModel.modelByPointer.get(nativeGet)) == null) {
            nativeItemModel = new NativeItemModel(nativeGet, n, n2);
            NativeItemModel.modelByPointer.put(nativeGet, nativeItemModel);
        }
        return nativeItemModel;
    }
    
    @JSStaticFunction
    public static NativeItemModel getForWithFallback(final int n, final int n2) {
        final NativeItemModel for1 = getFor(n, n2);
        if (for1.isNonExisting()) {
            return getFor(n, 0);
        }
        return for1;
    }
    
    @JSStaticFunction
    public static String getItemMeshTextureFor(final int n, final int n2) {
        return getForWithFallback(n, n2).getMeshTextureName();
    }
    
    @JSStaticFunction
    public static NativeRenderMesh getItemRenderMeshFor(final int n, final int n2, final int n3, final boolean b) {
        return getForWithFallback(n, n3).getItemRenderMesh(n2, b);
    }
    
    private Bitmap loadItemTextureFromFile() {
        return FileTools.bitmapFromBytes(FileTools.getAssetBytes(this.mItemTexturePath, MinecraftVersions.getCurrent().getVanillaResourcePacksDirs(), true));
    }
    
    private static native long nativeConstructStandalone();
    
    private static native long nativeGetFor(final int p0, final int p1);
    
    private static native long nativeGetShaderUniformSet(final long p0);
    
    private static native boolean nativeIsEmpty(final long p0);
    
    private static native boolean nativeOverridesHand(final long p0);
    
    private static native boolean nativeOverridesUi(final long p0);
    
    private static native long nativeSetHandBlockRender(final long p0, final long p1);
    
    private static native long nativeSetHandGlintMaterial(final long p0, final String p1);
    
    private static native long nativeSetHandMaterial(final long p0, final String p1);
    
    private static native long nativeSetHandMesh(final long p0, final long p1);
    
    private static native long nativeSetHandTexture(final long p0, final String p1);
    
    private static native boolean nativeSetHasOverrideCallback(final long p0, final boolean p1);
    
    private static native long nativeSetSpriteInUi(final long p0, final boolean p1);
    
    private static native long nativeSetUiBlockRender(final long p0, final long p1);
    
    private static native long nativeSetUiGlintMaterial(final long p0, final String p1);
    
    private static native long nativeSetUiMaterial(final long p0, final String p1);
    
    private static native long nativeSetUiMesh(final long p0, final long p1);
    
    private static native long nativeSetUiTexture(final long p0, final String p1);
    
    @JSStaticFunction
    public static NativeItemModel newStandalone() {
        final NativeItemModel nativeItemModel = new NativeItemModel();
        NativeItemModel.modelByPointer.put(nativeItemModel.pointer, nativeItemModel);
        return nativeItemModel;
    }
    
    private Bitmap newTexture() {
        return this.newTexture(false);
    }
    
    private Bitmap newTexture(final boolean b) {
        if (this.mItemTextureBitmapOverride != null) {
            if (b) {
                return null;
            }
            return Bitmap.createBitmap(this.mItemTextureBitmapOverride);
        }
        else if (this.mIsItemTexturePathForced) {
            if (b) {
                return null;
            }
            return this.loadItemTextureFromFile();
        }
        else {
            if (this.mCachePath != null) {
                final File file = new File(this.mCachePath);
                if (file.exists()) {
                    if (b) {
                        return null;
                    }
                    final Bitmap fileAsBitmap = FileTools.readFileAsBitmap(file.getAbsolutePath());
                    if (fileAsBitmap != null) {
                        return fileAsBitmap;
                    }
                }
            }
            if (this.mUiRenderMesh != null && this.getGuiRenderMesh() != null) {
                final Bitmap loadItemTextureFromFile = this.loadItemTextureFromFile();
                if (loadItemTextureFromFile != null) {
                    return loadItemTextureFromFile;
                }
            }
            final GuiBlockModel guiBlockModel = this.getGuiBlockModel();
            if (guiBlockModel != null) {
                return this.cacheAndReturn(guiBlockModel.genTexture());
            }
            if (this.mItemTexturePath != null) {
                return this.loadItemTextureFromFile();
            }
            return null;
        }
    }
    
    @JSStaticFunction
    public static void releaseMesh(final Object o) {
        synchronized (NativeItemModel.itemModelMeshPool) {
            NativeItemModel.itemModelMeshPool.add((NativeRenderMesh)Context.jsToJava(o, (Class)NativeRenderMesh.class));
        }
    }
    
    private void reloadIconIfDirty(final boolean p0) {
        // 
        // This method could not be decompiled.
        // 
        // Original Bytecode:
        // 
        //     1: getfield        com/zhekasmirnov/innercore/api/NativeItemModel.iconLock:Ljava/lang/Object;
        //     4: astore_2       
        //     5: aload_2        
        //     6: monitorenter   
        //     7: aload_0        
        //     8: getfield        com/zhekasmirnov/innercore/api/NativeItemModel.mIconBitmap:Landroid/graphics/Bitmap;
        //    11: ifnull          28
        //    14: aload_0        
        //    15: getfield        com/zhekasmirnov/innercore/api/NativeItemModel.isUiModelDirty:Z
        //    18: ifne            28
        //    21: aload_0        
        //    22: getfield        com/zhekasmirnov/innercore/api/NativeItemModel.isIconBitmapDirty:Z
        //    25: ifeq            46
        //    28: aload_0        
        //    29: invokevirtual   com/zhekasmirnov/innercore/api/NativeItemModel.releaseIcon:()V
        //    32: aload_0        
        //    33: aload_0        
        //    34: iload_1        
        //    35: invokespecial   com/zhekasmirnov/innercore/api/NativeItemModel.newTexture:(Z)Landroid/graphics/Bitmap;
        //    38: putfield        com/zhekasmirnov/innercore/api/NativeItemModel.mIconBitmap:Landroid/graphics/Bitmap;
        //    41: aload_0        
        //    42: iload_1        
        //    43: putfield        com/zhekasmirnov/innercore/api/NativeItemModel.isIconBitmapDirty:Z
        //    46: goto            85
        //    49: astore_3       
        //    50: goto            88
        //    53: astore_3       
        //    54: aload_0        
        //    55: invokevirtual   com/zhekasmirnov/innercore/api/NativeItemModel.releaseIcon:()V
        //    58: ldc_w           16777216
        //    61: invokestatic    com/zhekasmirnov/innercore/api/NativeItemModel.tryReleaseModelBitmapsOnLowMemory:(I)V
        //    64: aload_0        
        //    65: aload_0        
        //    66: invokespecial   com/zhekasmirnov/innercore/api/NativeItemModel.newTexture:()Landroid/graphics/Bitmap;
        //    69: putfield        com/zhekasmirnov/innercore/api/NativeItemModel.mIconBitmap:Landroid/graphics/Bitmap;
        //    72: aload_0        
        //    73: iconst_0       
        //    74: putfield        com/zhekasmirnov/innercore/api/NativeItemModel.isIconBitmapDirty:Z
        //    77: goto            85
        //    80: astore_3       
        //    81: aload_0        
        //    82: invokevirtual   com/zhekasmirnov/innercore/api/NativeItemModel.releaseIcon:()V
        //    85: aload_2        
        //    86: monitorexit    
        //    87: return         
        //    88: aload_2        
        //    89: monitorexit    
        //    90: aload_3        
        //    91: athrow         
        //    Exceptions:
        //  Try           Handler
        //  Start  End    Start  End    Type                        
        //  -----  -----  -----  -----  ----------------------------
        //  7      28     53     85     Ljava/lang/OutOfMemoryError;
        //  7      28     49     92     Any
        //  28     46     53     85     Ljava/lang/OutOfMemoryError;
        //  28     46     49     92     Any
        //  54     64     49     92     Any
        //  64     77     80     85     Ljava/lang/OutOfMemoryError;
        //  64     77     49     92     Any
        //  81     85     49     92     Any
        //  85     87     49     92     Any
        //  88     90     49     92     Any
        // 
        // The error that occurred was:
        // 
        // java.lang.IndexOutOfBoundsException: Index: 50, Size: 50
        //     at java.util.ArrayList.rangeCheck(Unknown Source)
        //     at java.util.ArrayList.get(Unknown Source)
        //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3321)
        //     at com.strobel.decompiler.ast.AstBuilder.convertToAst(AstBuilder.java:3569)
        //     at com.strobel.decompiler.ast.AstBuilder.build(AstBuilder.java:113)
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
    
    @JSStaticFunction
    public static void setCurrentCacheGroup(final String s, final String s2) {
        ItemModelCacheManager.getSingleton().setCurrentCacheGroup(s, s2);
    }
    
    @JSStaticFunction
    public static void tryReleaseModelBitmapsOnLowMemory(final int n) {
        int n2 = 0;
        int n3 = 0;
        for (final NativeItemModel nativeItemModel : NativeItemModel.modelByPointer.values()) {
            if (n2 >= n) {
                break;
            }
            int n4 = n2;
            int n5 = n3;
            if (nativeItemModel.mIconBitmap != null) {
                n4 = n2 + nativeItemModel.mIconBitmap.getByteCount();
                n5 = n3 + 1;
                nativeItemModel.releaseIcon();
            }
            n2 = n4;
            n3 = n5;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("released ");
        sb.append(n3);
        sb.append(" of total ");
        sb.append(NativeItemModel.modelByPointer.size());
        sb.append(" cached item icons (");
        sb.append(n2);
        sb.append(" of required ");
        sb.append(n);
        sb.append(" bytes)");
        ICLog.d("ItemModels", sb.toString());
    }
    
    private void updateCachePath() {
        if (this.mCacheKey != null) {
            String s;
            if ((s = IDRegistry.getNameByID(this.id)) == null) {
                s = Integer.toString(this.id);
            }
            final ItemModelCacheManager singleton = ItemModelCacheManager.getSingleton();
            final String mCacheGroup = this.mCacheGroup;
            final StringBuilder sb = new StringBuilder();
            sb.append(s);
            sb.append(":");
            sb.append(this.data);
            sb.append("_");
            sb.append(this.mCacheKey);
            sb.append(".png");
            this.mCachePath = singleton.getCachePath(mCacheGroup, sb.toString()).getAbsolutePath();
            return;
        }
        this.mCachePath = null;
    }
    
    public void addToMesh(final NativeRenderMesh nativeRenderMesh, final float n, final float n2, final float n3) {
        final GuiBlockModel worldBlockModel = this.getWorldBlockModel();
        if (this.mWorldRenderMesh != null) {
            nativeRenderMesh.addMesh(this.mWorldRenderMesh, n - 0.5f, n2 - 0.5f, n3 - 0.5f);
            return;
        }
        if (worldBlockModel != null) {
            worldBlockModel.addToMesh(nativeRenderMesh, n - 0.5f, n2 - 0.5f, n3 - 0.5f);
            return;
        }
        if (this.mItemTexturePath != null) {
            nativeRenderMesh.addMesh(this.getSpriteMesh(), n, n2, n3);
        }
    }
    
    public String getCacheKey() {
        return this.mCacheKey;
    }
    
    public GuiBlockModel getGuiBlockModel() {
        if (this.mGuiBlockModel == null || this.isUiModelDirty) {
            if (this.mUiIcRender != null) {
                this.mGuiBlockModel = this.mUiIcRender.buildGuiModel(true);
            }
            else if (this.mUiBlockModel != null) {
                this.mGuiBlockModel = this.mUiBlockModel.buildGuiModel(true);
            }
            else {
                final BlockVariant blockVariant = BlockRegistry.getBlockVariant(this.id, this.data);
                if (blockVariant != null) {
                    this.mGuiBlockModel = blockVariant.getGuiBlockModel();
                }
            }
            if (this.mUiRenderMesh == null) {
                this.isUiModelDirty = false;
            }
        }
        return this.mGuiBlockModel;
    }
    
    public GuiRenderMesh getGuiRenderMesh() {
        if ((this.mGuiRenderMesh == null || this.isUiModelDirty) && this.mUiRenderMesh != null) {
            this.mGuiRenderMesh = this.mUiRenderMesh.newGuiRenderMesh();
            this.isUiModelDirty = false;
        }
        return this.mGuiRenderMesh;
    }
    
    public Bitmap getIconBitmap() {
        this.reloadIconIfDirty();
        return this.mIconBitmap;
    }
    
    public Bitmap getIconBitmapNoReload() {
        return this.mIconBitmap;
    }
    
    public NativeRenderMesh getItemRenderMesh(final int n, final boolean b) {
        final NativeRenderMesh emptyMeshFromPool = getEmptyMeshFromPool();
        float n2 = 0.0f;
        float n3 = 0.0f;
        float n4 = 0.0f;
        float n5;
        if (this.isSpriteInWorld()) {
            n5 = 0.0625f;
        }
        else {
            n5 = 0.3125f;
        }
        for (int i = 0; i < n; ++i) {
            this.addToMesh(emptyMeshFromPool, n2 * n5, n3 * n5, n4 * n5);
            float n6 = n2;
            float n7 = n3;
            float n8 = n4;
            if (!this.isSpriteInWorld()) {
                final float n9 = n2 + 1.0f;
                final float n10 = n3 + 1.0f;
                n6 = n9;
                n7 = n10;
                n8 = n4;
                if (b) {
                    n8 = (float)(n4 + (Math.random() - 0.5) * 4.0 / n5);
                    n7 = n10;
                    n6 = n9;
                }
            }
            n4 = n8 + 1.0f;
            n2 = (float)(n6 + (Math.random() - 0.5) * 4.0 / n5);
            n3 = (float)(n7 + (Math.random() - 0.5) * 4.0 / n5);
        }
        return emptyMeshFromPool;
    }
    
    public String getMeshTextureName() {
        if (this.isSpriteInWorld() && this.mItemTexturePath != null) {
            return this.mItemTexturePath;
        }
        return "atlas::terrain";
    }
    
    public NativeItemModel getModelForItemInstance(final int n, final int n2, final int n3, final NativeItemInstanceExtra nativeItemInstanceExtra) {
        if (this.mModelOverrideCallback != null) {
            return this.mModelOverrideCallback.overrideModel(new ItemInstance(n, n2, n3, nativeItemInstanceExtra));
        }
        return this;
    }
    
    public NativeShaderUniformSet getShaderUniforms() {
        return this.shaderUniformSet;
    }
    
    public NativeRenderMesh getSpriteMesh() {
    Label_0353_Outer:
        while (true) {
            Bitmap loadItemTextureFromFile;
            int width = 0;
            int height;
            float n;
            float n2;
            int n3 = 0;
            final Bitmap bitmap;
            int n4 = 0;
            Label_0353:Label_0298_Outer:
            while (true) {
                while (true) {
                Label_0221:
                    while (true) {
                    Label_0188_Outer:
                        while (true) {
                        Label_0096:
                            while (true) {
                                Label_0338: {
                                    while (true) {
                                        synchronized (this) {
                                            if (this.mSpriteMesh == null || this.isSpriteMeshDirty) {
                                                if (this.mSpriteMesh != null) {
                                                    this.mSpriteMesh.clear();
                                                }
                                                else {
                                                    this.mSpriteMesh = new NativeRenderMesh();
                                                }
                                                this.mSpriteMesh.setColor(1.0f, 1.0f, 1.0f);
                                                loadItemTextureFromFile = this.loadItemTextureFromFile();
                                                if (loadItemTextureFromFile != null) {
                                                    width = loadItemTextureFromFile.getWidth();
                                                    height = loadItemTextureFromFile.getHeight();
                                                    n = 1.0f / width;
                                                    n2 = 1.0f / height;
                                                    n3 = 0;
                                                    break Label_0323;
                                                }
                                                width = 16;
                                                height = 16;
                                                n = 1.0f / 16;
                                                n2 = 1.0f / 16;
                                                n3 = 0;
                                                break Label_0353;
                                            }
                                            return this.mSpriteMesh;
                                            // iftrue(Label_0368:, n3 >= height)
                                            // iftrue(Label_0338:, Color.alpha(bitmap.getPixel(n4, height - 1 - n3)) <= 0)
                                            while (true) {
                                                Block_10: {
                                                    break Block_10;
                                                    this.addBoxToSpriteMesh(this.mSpriteMesh, (n4 - 0.5f) * n - 0.5f, (n3 - 0.5f) * n2 - 0.5f, -0.03125f, n, n2, 0.0625f, (n4 + 0.5f) / width, 1.0f - (n3 + 0.5f) / height);
                                                    break Label_0338;
                                                    bitmap.recycle();
                                                    this.mSpriteMesh.invalidate();
                                                    return this.mSpriteMesh;
                                                }
                                                this.addBoxToSpriteMesh(this.mSpriteMesh, (n4 - 0.5f) * n - 0.5f, (n3 - 0.5f) * n2 - 0.5f, -0.03125f, n, n2, 0.0625f, (n4 + 0.5f) / width, 1.0f - (n3 + 0.5f) / height);
                                                ++n3;
                                                continue Label_0221;
                                                continue Label_0188_Outer;
                                            }
                                        }
                                        // iftrue(Label_0345:, n3 >= height)
                                        n4 = n3;
                                        if (n4 < width) {
                                            n3 = 0;
                                            continue Label_0096;
                                        }
                                        continue Label_0298_Outer;
                                    }
                                }
                                ++n3;
                                continue Label_0096;
                            }
                            Label_0345: {
                                n3 = n4 + 1;
                            }
                            continue Label_0353_Outer;
                        }
                        n4 = n3;
                        if (n4 < width) {
                            n3 = 0;
                            continue Label_0221;
                        }
                        break;
                    }
                    continue;
                }
                Label_0368: {
                    n3 = n4 + 1;
                }
                continue Label_0353;
            }
        }
    }
    
    public String getUiGlintMaterialName() {
        if (this.mUiGlintMaterialName != null) {
            return this.mUiGlintMaterialName;
        }
        return "ui_custom_item_glint";
    }
    
    public String getUiMaterialName() {
        if (this.mUiMaterialName != null) {
            return this.mUiMaterialName;
        }
        return "ui_custom_item";
    }
    
    public String getUiTextureName() {
        if (this.mUiTextureName != null) {
            return this.mUiTextureName;
        }
        if (this.isSpriteInUi() && this.mItemTexturePath != null) {
            return this.mItemTexturePath;
        }
        return "atlas::terrain";
    }
    
    public GuiBlockModel getWorldBlockModel() {
        if (this.mWorldCompiledBlockModel == null || this.isWorldModelDirty) {
            if (this.mWorldIcRender != null) {
                this.mWorldCompiledBlockModel = this.mWorldIcRender.buildGuiModel(false);
            }
            else if (this.mWorldBlockModel != null) {
                this.mWorldCompiledBlockModel = this.mWorldBlockModel.buildGuiModel(false);
            }
            else {
                final BlockVariant blockVariant = BlockRegistry.getBlockVariant(this.id, this.data);
                if (blockVariant != null) {
                    this.mWorldCompiledBlockModel = blockVariant.getGuiBlockModel();
                }
            }
            if (this.mWorldRenderMesh == null) {
                this.isWorldModelDirty = false;
            }
        }
        return this.mWorldCompiledBlockModel;
    }
    
    public String getWorldGlintMaterialName() {
        if (this.mWorldGlintMaterialName != null) {
            return this.mWorldGlintMaterialName;
        }
        return "entity_alphatest_custom_glint";
    }
    
    public String getWorldMaterialName() {
        if (this.mWorldMaterialName != null) {
            return this.mWorldMaterialName;
        }
        return "entity_alphatest_custom";
    }
    
    public String getWorldTextureName() {
        if (this.mWorldTextureName != null) {
            return this.mWorldTextureName;
        }
        if (this.isSpriteInWorld() && this.mItemTexturePath != null) {
            return this.mItemTexturePath;
        }
        return "atlas::terrain";
    }
    
    public boolean isEmpty() {
        return !this.isOccupied && nativeIsEmpty(this.pointer);
    }
    
    public boolean isEmptyInUi() {
        return this.mUiBlockModel == null && this.mUiIcRender == null && this.mUiRenderMesh == null;
    }
    
    public boolean isEmptyInWorld() {
        return this.mUiBlockModel == null && this.mUiIcRender == null && this.mUiRenderMesh == null;
    }
    
    public boolean isNonExisting() {
        return !this.isOccupied && this.isEmptyInUi() && this.isEmptyInWorld() && this.mItemTexturePath == null && this.mGuiBlockModel == null && this.mWorldCompiledBlockModel == null;
    }
    
    public boolean isSpriteInUi() {
        return this.mItemTexturePath != null && this.isEmptyInUi();
    }
    
    public boolean isSpriteInWorld() {
        return this.isEmptyInWorld();
    }
    
    public boolean isUsingOverrideCallback() {
        return this.mModelOverrideCallback != null;
    }
    
    public NativeItemModel occupy() {
        this.isOccupied = true;
        return this;
    }
    
    public boolean overridesHand() {
        return nativeOverridesHand(this.pointer);
    }
    
    public boolean overridesUi() {
        return nativeOverridesUi(this.pointer);
    }
    
    public Bitmap queueReload() {
        return this.queueReload(null);
    }
    
    public Bitmap queueReload(final IIconRebuildListener iconRebuildListener) {
        if (this.mIconBitmap != null && !this.isUiModelDirty && !this.isIconBitmapDirty) {
            return this.mIconBitmap;
        }
        synchronized (NativeItemModel.asyncReloadQueue) {
            NativeItemModel.asyncReloadQueue.add((Pair<NativeItemModel, IIconRebuildListener>)new Pair((Object)this, (Object)iconRebuildListener));
            if (NativeItemModel.asyncReloadThread == null) {
                (NativeItemModel.asyncReloadThread = new Thread(new Runnable() {
                    @Override
                    public void run() {
                        while (true) {
                            Object o = NativeItemModel.asyncReloadQueue;
                            synchronized (o) {
                                if (NativeItemModel.asyncReloadQueue.size() <= 0) {
                                    return;
                                }
                                final Pair pair = NativeItemModel.asyncReloadQueue.remove(0);
                                // monitorexit(o)
                                o = ((NativeItemModel)pair.first).getIconBitmap();
                                if (pair.second == null) {
                                    continue;
                                }
                                ((IIconRebuildListener)pair.second).onIconRebuild((NativeItemModel)pair.first, (Bitmap)o);
                            }
                        }
                    }
                })).start();
            }
            return null;
        }
    }
    
    public void releaseIcon() {
        synchronized (this.iconLock) {
            if (this.mIconBitmap != null) {
                this.mIconBitmap.recycle();
                this.mIconBitmap = null;
                this.isIconBitmapDirty = true;
            }
        }
    }
    
    public void reloadIcon() {
        this.reloadIcon(false);
    }
    
    public void reloadIcon(final boolean b) {
        this.isIconBitmapDirty = true;
        this.reloadIconIfDirty(b);
    }
    
    public void reloadIconIfDirty() {
        this.reloadIconIfDirty(false);
    }
    
    public NativeItemModel removeModUiSpriteTexture() {
        this.isIconBitmapDirty = true;
        if (this.mItemTextureBitmapOverride != null) {
            this.mItemTextureBitmapOverride.recycle();
            this.mItemTextureBitmapOverride = null;
        }
        this.mIsItemTexturePathForced = false;
        return this;
    }
    
    public void setCacheGroup(final String mCacheGroup) {
        this.mCacheGroup = mCacheGroup;
        this.updateCachePath();
    }
    
    public void setCacheKey(final String mCacheKey) {
        this.mCacheKey = mCacheKey;
        this.updateCachePath();
    }
    
    public NativeItemModel setGlintMaterial(final String s) {
        return this.setHandGlintMaterial(s).setUiGlintMaterial(s);
    }
    
    public NativeItemModel setHandGlintMaterial(final String mWorldGlintMaterialName) {
        nativeSetHandGlintMaterial(this.pointer, mWorldGlintMaterialName);
        this.mWorldGlintMaterialName = mWorldGlintMaterialName;
        return this;
    }
    
    public NativeItemModel setHandMaterial(final String s) {
        nativeSetHandMaterial(this.pointer, s);
        this.mWorldMaterialName = s;
        if (this.mWorldGlintMaterialName == null) {
            this.setHandGlintMaterial(s);
        }
        return this;
    }
    
    public NativeItemModel setHandModel(final NativeBlockModel mWorldBlockModel) {
        final long pointer = this.pointer;
        long pointer2;
        if (mWorldBlockModel != null) {
            pointer2 = mWorldBlockModel.pointer;
        }
        else {
            pointer2 = 0L;
        }
        nativeSetHandBlockRender(pointer, pointer2);
        nativeSetHandMesh(this.pointer, 0L);
        this.mWorldBlockModel = mWorldBlockModel;
        this.mWorldRenderMesh = null;
        this.mWorldIcRender = null;
        this.isWorldModelDirty = true;
        return this;
    }
    
    public NativeItemModel setHandModel(final NativeBlockModel handModel, final String handMaterial) {
        this.setHandModel(handModel);
        this.setHandMaterial(handMaterial);
        return this;
    }
    
    public NativeItemModel setHandModel(final NativeICRender.Model mWorldIcRender) {
        final long pointer = this.pointer;
        long ptr;
        if (mWorldIcRender != null) {
            ptr = mWorldIcRender.getPtr();
        }
        else {
            ptr = 0L;
        }
        nativeSetHandBlockRender(pointer, ptr);
        nativeSetHandMesh(this.pointer, 0L);
        this.mWorldIcRender = mWorldIcRender;
        this.mWorldRenderMesh = null;
        this.mWorldBlockModel = null;
        this.isWorldModelDirty = true;
        return this;
    }
    
    public NativeItemModel setHandModel(final NativeICRender.Model handModel, final String handMaterial) {
        this.setHandModel(handModel);
        this.setHandMaterial(handMaterial);
        return this;
    }
    
    public NativeItemModel setHandModel(final NativeRenderMesh mWorldRenderMesh) {
        final long pointer = this.pointer;
        long ptr;
        if (mWorldRenderMesh != null) {
            ptr = mWorldRenderMesh.getPtr();
        }
        else {
            ptr = 0L;
        }
        nativeSetHandMesh(pointer, ptr);
        nativeSetHandBlockRender(this.pointer, 0L);
        if (mWorldRenderMesh != null) {
            mWorldRenderMesh.invalidate();
        }
        this.mWorldRenderMesh = mWorldRenderMesh;
        this.mWorldBlockModel = null;
        this.mWorldIcRender = null;
        this.isWorldModelDirty = true;
        return this;
    }
    
    public NativeItemModel setHandModel(final NativeRenderMesh handModel, final String handTexture) {
        this.setHandModel(handModel);
        this.setHandTexture(handTexture);
        return this;
    }
    
    public NativeItemModel setHandModel(final NativeRenderMesh handModel, final String handTexture, final String handMaterial) {
        this.setHandModel(handModel);
        this.setHandTexture(handTexture);
        this.setHandMaterial(handMaterial);
        return this;
    }
    
    public NativeItemModel setHandTexture(final String mWorldTextureName) {
        nativeSetHandTexture(this.pointer, mWorldTextureName);
        this.mWorldTextureName = mWorldTextureName;
        return this;
    }
    
    public NativeItemModel setItemTexture(final String s, final int n) {
        this.setItemTexturePath(ResourcePackManager.getItemTextureName(s, n));
        return this;
    }
    
    public NativeItemModel setItemTexturePath(final String s) {
        String string = s;
        if (!s.endsWith(".png")) {
            final StringBuilder sb = new StringBuilder();
            sb.append(s);
            sb.append(".png");
            string = sb.toString();
        }
        this.mItemTexturePath = string;
        return this;
    }
    
    public NativeItemModel setMaterial(final String s) {
        return this.setHandMaterial(s).setUiMaterial(s);
    }
    
    public NativeItemModel setModUiSpriteBitmap(final Bitmap bitmap) {
        this.isIconBitmapDirty = true;
        this.mIsItemTexturePathForced = true;
        this.mItemTextureBitmapOverride = Bitmap.createBitmap(bitmap);
        return this;
    }
    
    public NativeItemModel setModUiSpriteName(final String s, final int n) {
        this.isIconBitmapDirty = true;
        this.mIsItemTexturePathForced = true;
        this.setItemTexture(s, n);
        return this;
    }
    
    public NativeItemModel setModUiSpritePath(final String itemTexturePath) {
        this.isIconBitmapDirty = true;
        this.mIsItemTexturePathForced = true;
        this.setItemTexturePath(itemTexturePath);
        return this;
    }
    
    public NativeItemModel setModel(final NativeBlockModel nativeBlockModel) {
        return this.setHandModel(nativeBlockModel).setUiModel(nativeBlockModel);
    }
    
    public NativeItemModel setModel(final NativeBlockModel nativeBlockModel, final String s) {
        return this.setHandModel(nativeBlockModel, s).setUiModel(nativeBlockModel, s);
    }
    
    public NativeItemModel setModel(final NativeICRender.Model model) {
        return this.setHandModel(model).setUiModel(model);
    }
    
    public NativeItemModel setModel(final NativeICRender.Model model, final String s) {
        return this.setHandModel(model, s).setUiModel(model, s);
    }
    
    public NativeItemModel setModel(final NativeRenderMesh nativeRenderMesh) {
        return this.setHandModel(nativeRenderMesh).setUiModel(nativeRenderMesh);
    }
    
    public NativeItemModel setModel(final NativeRenderMesh nativeRenderMesh, final String s) {
        return this.setHandModel(nativeRenderMesh, s).setUiModel(nativeRenderMesh, s);
    }
    
    public NativeItemModel setModel(final NativeRenderMesh nativeRenderMesh, final String s, final String s2) {
        return this.setHandModel(nativeRenderMesh, s, s2).setUiModel(nativeRenderMesh, s, s2);
    }
    
    public NativeItemModel setModelOverrideCallback(final IOverrideCallback mModelOverrideCallback) {
        this.mModelOverrideCallback = mModelOverrideCallback;
        nativeSetHasOverrideCallback(this.pointer, mModelOverrideCallback != null);
        return this;
    }
    
    public NativeItemModel setSpriteUiRender(final boolean b) {
        nativeSetSpriteInUi(this.pointer, b);
        return this;
    }
    
    public NativeItemModel setTexture(final String s) {
        return this.setHandTexture(s).setUiTexture(s);
    }
    
    public void setUiBlockModel(final GuiBlockModel mGuiBlockModel) {
        this.mGuiBlockModel = mGuiBlockModel;
        this.mGuiRenderMesh = null;
        this.mItemTexturePath = null;
        this.isUiModelDirty = false;
        this.isIconBitmapDirty = true;
    }
    
    public NativeItemModel setUiGlintMaterial(final String mUiGlintMaterialName) {
        nativeSetUiGlintMaterial(this.pointer, mUiGlintMaterialName);
        this.mUiGlintMaterialName = mUiGlintMaterialName;
        return this;
    }
    
    public NativeItemModel setUiMaterial(final String s) {
        nativeSetUiMaterial(this.pointer, s);
        this.mUiMaterialName = s;
        if (this.mUiGlintMaterialName == null) {
            this.setUiGlintMaterial(s);
        }
        return this;
    }
    
    public NativeItemModel setUiModel(final NativeBlockModel mUiBlockModel) {
        final long pointer = this.pointer;
        long pointer2;
        if (mUiBlockModel != null) {
            pointer2 = mUiBlockModel.pointer;
        }
        else {
            pointer2 = 0L;
        }
        nativeSetUiBlockRender(pointer, pointer2);
        nativeSetUiMesh(this.pointer, 0L);
        this.mUiBlockModel = mUiBlockModel;
        this.mUiRenderMesh = null;
        this.mUiIcRender = null;
        this.isUiModelDirty = true;
        if (this.mCacheKey == null) {
            this.setCacheKey("item-model");
        }
        return this;
    }
    
    public NativeItemModel setUiModel(final NativeBlockModel uiModel, final String uiMaterial) {
        this.setUiModel(uiModel);
        this.setUiMaterial(uiMaterial);
        return this;
    }
    
    public NativeItemModel setUiModel(final NativeICRender.Model mUiIcRender) {
        final long pointer = this.pointer;
        long ptr;
        if (mUiIcRender != null) {
            ptr = mUiIcRender.getPtr();
        }
        else {
            ptr = 0L;
        }
        nativeSetUiBlockRender(pointer, ptr);
        nativeSetUiMesh(this.pointer, 0L);
        this.mUiIcRender = mUiIcRender;
        this.mUiRenderMesh = null;
        this.mUiBlockModel = null;
        this.isUiModelDirty = true;
        if (this.mCacheKey == null) {
            this.setCacheKey("item-model");
        }
        return this;
    }
    
    public NativeItemModel setUiModel(final NativeICRender.Model uiModel, final String uiMaterial) {
        this.setUiModel(uiModel);
        this.setUiMaterial(uiMaterial);
        return this;
    }
    
    public NativeItemModel setUiModel(final NativeRenderMesh mUiRenderMesh) {
        final long pointer = this.pointer;
        long ptr;
        if (mUiRenderMesh != null) {
            ptr = mUiRenderMesh.getPtr();
        }
        else {
            ptr = 0L;
        }
        nativeSetUiMesh(pointer, ptr);
        nativeSetUiBlockRender(this.pointer, 0L);
        this.mUiRenderMesh = mUiRenderMesh;
        this.mUiBlockModel = null;
        this.mUiIcRender = null;
        this.isUiModelDirty = true;
        if (this.mCacheKey == null) {
            this.setCacheKey("item-model");
        }
        return this;
    }
    
    public NativeItemModel setUiModel(final NativeRenderMesh uiModel, final String uiTexture) {
        this.setUiModel(uiModel);
        this.setUiTexture(uiTexture);
        return this;
    }
    
    public NativeItemModel setUiModel(final NativeRenderMesh uiModel, final String uiTexture, final String uiMaterial) {
        this.setUiModel(uiModel);
        this.setUiTexture(uiTexture);
        this.setUiMaterial(uiMaterial);
        return this;
    }
    
    public NativeItemModel setUiTexture(final String mUiTextureName) {
        nativeSetUiTexture(this.pointer, mUiTextureName);
        this.mUiTextureName = mUiTextureName;
        return this;
    }
    
    public void setWorldBlockModel(final GuiBlockModel mWorldCompiledBlockModel) {
        this.mWorldCompiledBlockModel = mWorldCompiledBlockModel;
        this.isWorldModelDirty = false;
    }
    
    public void updateForBlockVariant(final BlockVariant blockVariant) {
        if (blockVariant != null) {
            final GuiBlockModel guiBlockModel = blockVariant.getGuiBlockModel();
            if (this.isEmptyInUi()) {
                this.setUiBlockModel(guiBlockModel);
            }
            if (this.isEmptyInWorld()) {
                this.setWorldBlockModel(guiBlockModel);
            }
            if (guiBlockModel == null) {
                this.setItemTexturePath(blockVariant.getSpriteTexturePath());
            }
        }
    }
    
    public interface IIconRebuildListener
    {
        void onIconRebuild(final NativeItemModel p0, final Bitmap p1);
    }
    
    public interface IOverrideCallback
    {
        NativeItemModel overrideModel(final ItemInstance p0);
    }
}
