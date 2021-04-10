package com.zhekasmirnov.innercore.api.mod.ui.icon;

import com.zhekasmirnov.innercore.utils.*;
import com.zhekasmirnov.innercore.mod.resource.*;
import android.graphics.*;
import java.io.*;
import java.util.*;
import com.zhekasmirnov.innercore.api.*;
import com.zhekasmirnov.innercore.api.log.*;
import com.zhekasmirnov.innercore.api.mod.ui.*;
import com.zhekasmirnov.innercore.api.unlimited.*;
import com.zhekasmirnov.horizon.runtime.logger.*;

public class ItemModels
{
    public static final String ATLAS_NAME = "textures/entity/camera_tripod";
    public static final String ATLAS_PATH;
    public static final String CACHE_DIR;
    private static HashMap<String, AtlasUnit> atlasOffsets;
    private static int currentAtlasPos;
    private static int currentAtlasWidth;
    private static HashMap<String, NativeRenderer.Renderer> itemModels;
    private static HashMap<String, ModelInfo> modelInfoMap;
    private static final NativeRenderer.RenderPool renderPool;
    
    static {
        final StringBuilder sb = new StringBuilder();
        sb.append(FileTools.DIR_WORK);
        sb.append("cache/item-models/");
        CACHE_DIR = sb.toString();
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(ResourcePackManager.getSourcePath());
        sb2.append("textures/entity/camera_tripod");
        sb2.append(".png");
        ATLAS_PATH = sb2.toString();
        FileTools.mkdirs(ItemModels.CACHE_DIR);
        ItemModels.modelInfoMap = new HashMap<String, ModelInfo>();
        ItemModels.currentAtlasPos = 0;
        ItemModels.currentAtlasWidth = 0;
        ItemModels.atlasOffsets = new HashMap<String, AtlasUnit>();
        ItemModels.itemModels = new HashMap<String, NativeRenderer.Renderer>();
        renderPool = new NativeRenderer.RenderPool();
    }
    
    private static void addBlock(final NativeRenderer.ModelPart modelPart, final ModelInfo modelInfo, final float n, final float n2, final float n3) {
        if (modelInfo.model != null) {
            modelInfo.model.addToRenderModelPart(modelPart, n - 8.0f, n2 - 8.0f, n3 - 8.0f);
        }
    }
    
    private static void addSprite(final NativeRenderer.ModelPart modelPart, final float n, final float n2, final float n3) {
        modelPart.setTextureSize(1024, 1024);
        for (int i = 0; i < 16; ++i) {
            for (int j = 0; j < 16; ++j) {
                modelPart.setTextureOffset(i * 1024 / 16, j * 1024 / 16);
                modelPart.addBox(i + n - 16 / 2, j + n2 - 16 / 2, n3 - 0.5f, 1.0f, 1.0f, 1.0f);
            }
        }
    }
    
    static void createAtlas() {
        if (ItemModels.currentAtlasPos <= 0) {
            return;
        }
        if (ItemModels.currentAtlasWidth <= 0) {
            return;
        }
        final Bitmap bitmap = Bitmap.createBitmap(ItemModels.currentAtlasWidth, ItemModels.currentAtlasPos, Bitmap$Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);
        for (final AtlasUnit atlasUnit : ItemModels.atlasOffsets.values()) {
            final Bitmap scaledBitmap = Bitmap.createScaledBitmap(atlasUnit.bitmap, atlasUnit.size, atlasUnit.size, false);
            canvas.drawBitmap(scaledBitmap, 0.0f, (float)(atlasUnit.pos + atlasUnit.size / 16), (Paint)null);
            if (scaledBitmap != atlasUnit.bitmap) {
                scaledBitmap.recycle();
            }
        }
        FileTools.assureFileDir(new File(ItemModels.ATLAS_PATH));
        FileTools.writeBitmap(ItemModels.ATLAS_PATH, bitmap);
        bitmap.recycle();
    }
    
    public static int createAtlasLink(final String s) {
        String string = s;
        if (!s.endsWith(".png")) {
            final StringBuilder sb = new StringBuilder();
            sb.append(s);
            sb.append(".png");
            string = sb.toString();
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("resource_packs/vanilla/");
        sb2.append(string);
        final Bitmap assetAsBitmap = FileTools.getAssetAsBitmap(sb2.toString());
        if (assetAsBitmap != null) {
            return createAtlasLink(string, assetAsBitmap);
        }
        return -1;
    }
    
    public static int createAtlasLink(final String s, final Bitmap bitmap) {
        if (ItemModels.atlasOffsets.containsKey(s)) {
            return ItemModels.atlasOffsets.get(s).pos;
        }
        final int currentAtlasPos = ItemModels.currentAtlasPos;
        int width;
        int i;
        for (width = bitmap.getWidth(), i = 16; i < width; i *= 2) {}
        final int currentAtlasWidth = (int)(i * 2 * 1.0625);
        if (currentAtlasWidth > ItemModels.currentAtlasWidth) {
            ItemModels.currentAtlasWidth = currentAtlasWidth;
        }
        ItemModels.currentAtlasPos += (int)(i * 1.125);
        ItemModels.atlasOffsets.put(s, new AtlasUnit(bitmap, currentAtlasPos, i));
        return currentAtlasPos;
    }
    
    public static int getAtlasHeight() {
        if (ItemModels.currentAtlasPos < 1) {
            return 1;
        }
        return ItemModels.currentAtlasPos;
    }
    
    public static AtlasUnit getAtlasUnit(final String s) {
        if (ItemModels.atlasOffsets.containsKey(s)) {
            return ItemModels.atlasOffsets.get(s);
        }
        return null;
    }
    
    public static int getAtlasWidth() {
        if (ItemModels.currentAtlasWidth < 1) {
            return 1;
        }
        return ItemModels.currentAtlasWidth;
    }
    
    public static NativeRenderer.Renderer getItemOrBlockModel(final int n, int n2, final int n3, double n4, final double n5, final double n6, final double n7, final boolean b) {
        if (n2 > 3) {
            n2 = 3;
        }
        if (n4 == 0.0) {
            n4 = 0.5;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append(n);
        sb.append("$");
        sb.append(n2);
        sb.append("$");
        sb.append(n3);
        sb.append("$");
        sb.append((int)(1000.0 * n4));
        sb.append("$");
        sb.append((int)(n5 * 3000.0));
        sb.append("$");
        sb.append((int)(n6 * 3000.0));
        sb.append("$");
        sb.append((int)(3000.0 * n7));
        sb.append("$");
        sb.append(b);
        final NativeRenderer.Renderer renderer = ItemModels.itemModels.get(sb.toString());
        if (renderer != null) {
            return renderer;
        }
        final ModelInfo modelInfo = getModelInfo(n, n3);
        String string;
        if (modelInfo != null) {
            string = "atlas::terrain";
            modelInfo.isSprite();
        }
        else {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(ItemIconSource.instance.getIconPath(n, n3));
            sb2.append("");
            string = sb2.toString();
        }
        final NativeRenderer.Renderer render = ItemModels.renderPool.getRender();
        render.setScale((float)n4);
        render.setSkin(string);
        final NativeRenderer.Model model = render.getModel();
        model.getPart("body").clear();
        model.getPart("head").clear();
        model.getPart("headwear").clear();
        model.getPart("leftArm").clear();
        model.getPart("rightArm").clear();
        model.getPart("leftLeg").clear();
        model.getPart("rightLeg").clear();
        NativeRenderMesh nativeRenderMesh = null;
        if (model.hasPart("item")) {
            final NativeRenderMesh mesh = model.getPart("item").getMesh();
            if ((nativeRenderMesh = mesh) != null) {
                mesh.clear();
                nativeRenderMesh = mesh;
            }
        }
        NativeRenderMesh mesh2;
        if ((mesh2 = nativeRenderMesh) == null) {
            mesh2 = new NativeRenderMesh();
        }
        final NativeRenderer.ModelPart addPart = model.getPart("head").addPart("item");
        addPart.setMesh(mesh2);
        addPart.setRotation((float)n5, (float)n6, (float)n7);
        addPart.setOffset(0.0f, 24.0f, 0.0f);
        final NativeItemModel forWithFallback = NativeItemModel.getForWithFallback(n, n3);
        forWithFallback.addToMesh(mesh2, 0.0f, 0.0f, 0.0f);
        mesh2.scale(16.0f, 16.0f, 16.0f);
        render.setSkin(forWithFallback.getMeshTextureName());
        render.getModel().reset();
        return render;
    }
    
    public static ModelInfo getModelInfo(final int n, final int n2) {
        final HashMap<String, ModelInfo> modelInfoMap = ItemModels.modelInfoMap;
        final StringBuilder sb = new StringBuilder();
        sb.append(n);
        sb.append(":");
        sb.append(n2);
        if (modelInfoMap.containsKey(sb.toString())) {
            final HashMap<String, ModelInfo> modelInfoMap2 = ItemModels.modelInfoMap;
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(n);
            sb2.append(":");
            sb2.append(n2);
            return modelInfoMap2.get(sb2.toString());
        }
        final HashMap<String, ModelInfo> modelInfoMap3 = ItemModels.modelInfoMap;
        final StringBuilder sb3 = new StringBuilder();
        sb3.append(n);
        sb3.append("");
        return modelInfoMap3.get(sb3.toString());
    }
    
    public static ModelInfo getModelInfo(final String s) {
        return ItemModels.modelInfoMap.get(s);
    }
    
    public static void init() {
        final long currentTimeMillis = System.currentTimeMillis();
        loadModBlockTextures();
        createAtlas();
        final StringBuilder sb = new StringBuilder();
        sb.append("creating item model atlas took ");
        sb.append(System.currentTimeMillis() - currentTimeMillis);
        sb.append(" ms");
        ICLog.i("UI", sb.toString());
    }
    
    private static void loadModBlockTextures() {
        final String[] listAssets = FileTools.listAssets("resource_packs/vanilla/textures/blocks");
        if (listAssets != null) {
            for (int length = listAssets.length, i = 0; i < length; ++i) {
                final String s = listAssets[i];
                final StringBuilder sb = new StringBuilder();
                sb.append("textures/blocks/");
                sb.append(s);
                createAtlasLink(sb.toString());
            }
        }
    }
    
    public static ModelInfo prepareModelInfo(final String s) {
        final ModelInfo modelInfo = ItemModels.modelInfoMap.get(s);
        if (modelInfo != null) {
            return modelInfo;
        }
        return new ModelInfo(s);
    }
    
    public static ModelInfo prepareModelInfo(final String s, final GuiBlockModel guiBlockModel) {
        final ModelInfo prepareModelInfo = prepareModelInfo(s);
        prepareModelInfo.setSprite(false);
        prepareModelInfo.setModel(guiBlockModel);
        return prepareModelInfo;
    }
    
    public static ModelInfo prepareModelInfo(final String s, final String s2) {
        final ModelInfo prepareModelInfo = prepareModelInfo(s);
        prepareModelInfo.setSprite(true);
        prepareModelInfo.setSpritePath(s2);
        return prepareModelInfo;
    }
    
    public static void setCustomUiModel(final int n, final int n2, final GuiBlockModel guiBlockModel) {
        getModelInfo(n, n2);
    }
    
    public static void updateBlockShape(final int n, final int n2, final BlockShape blockShape) {
        getModelInfo(n, n2);
    }
    
    public static class AtlasUnit
    {
        public final Bitmap bitmap;
        public final int pos;
        public final int size;
        
        public AtlasUnit(final Bitmap bitmap, final int pos, final int size) {
            this.pos = pos;
            this.size = size;
            this.bitmap = bitmap;
        }
    }
    
    public static class ModelInfo
    {
        private String idKey;
        private boolean isCustomized;
        private boolean isSprite;
        private GuiBlockModel model;
        private String spritePath;
        
        private ModelInfo(final String idKey) {
            this.isCustomized = false;
            this.idKey = idKey;
            ItemModels.modelInfoMap.put(idKey, this);
        }
        
        private File getCacheFile() {
            final String cache_DIR = ItemModels.CACHE_DIR;
            final StringBuilder sb = new StringBuilder();
            sb.append(this.idKey);
            sb.append(".png");
            return new File(cache_DIR, sb.toString());
        }
        
        private void setModel(final GuiBlockModel model) {
            this.model = model;
        }
        
        private void setSprite(final boolean isSprite) {
            this.isSprite = isSprite;
        }
        
        private void setSpritePath(final String spritePath) {
            this.spritePath = spritePath;
        }
        
        public Bitmap getCache() {
            if (this.isSprite) {
                final StringBuilder sb = new StringBuilder();
                sb.append("spritePath: ");
                sb.append(this.spritePath);
                Logger.debug(sb.toString());
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("resource_packs/vanilla/");
                sb2.append(this.spritePath);
                return FileTools.getAssetAsBitmap(sb2.toString());
            }
            final File cacheFile = this.getCacheFile();
            if (cacheFile.exists()) {
                return FileTools.readFileAsBitmap(cacheFile.getAbsolutePath());
            }
            return null;
        }
        
        public GuiBlockModel getModel() {
            return this.model;
        }
        
        public String getSkinName() {
            if (this.isSprite) {
                return this.spritePath;
            }
            return "textures/entity/camera_tripod";
        }
        
        public boolean isCustomized() {
            return this.isCustomized;
        }
        
        public boolean isSprite() {
            return this.isSprite;
        }
        
        public void setShape(final BlockShape blockShape) {
            if (this.isCustomized) {
                this.model.updateShape(blockShape);
            }
        }
        
        public void writeToCache(final Bitmap bitmap) {
            if (!this.isSprite) {
                FileTools.writeBitmap(this.getCacheFile().getAbsolutePath(), bitmap);
            }
        }
    }
}
