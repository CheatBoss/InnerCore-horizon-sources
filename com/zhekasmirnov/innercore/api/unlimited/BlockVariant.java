package com.zhekasmirnov.innercore.api.unlimited;

import com.zhekasmirnov.innercore.api.mod.*;
import com.zhekasmirnov.innercore.api.runtime.other.*;
import org.mozilla.javascript.*;
import com.zhekasmirnov.innercore.mod.resource.*;
import com.zhekasmirnov.horizon.runtime.logger.*;
import com.zhekasmirnov.innercore.api.mod.ui.*;

public class BlockVariant
{
    public final int data;
    public final boolean inCreative;
    public boolean isTechnical;
    public final String name;
    public int renderType;
    public BlockShape shape;
    public final int[] textureIds;
    public final String[] textures;
    public final int uid;
    
    public BlockVariant(final int uid, final int data, final String name, final String[] textures, final int[] textureIds, final boolean inCreative) {
        this.renderType = 0;
        this.shape = new BlockShape();
        this.name = name;
        this.textures = textures;
        this.textureIds = textureIds;
        this.inCreative = inCreative;
        this.uid = uid;
        this.data = data;
        this.validate();
    }
    
    public BlockVariant(final int uid, final int data, final ScriptableObject scriptableObject) {
        this.renderType = 0;
        this.shape = new BlockShape();
        this.uid = uid;
        this.data = data;
        String name;
        final String s = name = ScriptableObjectHelper.getStringProperty(scriptableObject, "name", null);
        if (s != null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("block_");
            sb.append(uid);
            sb.append("_");
            sb.append(data);
            name = NameTranslation.fixUnicodeIfRequired(sb.toString(), s);
        }
        while (true) {
            this.name = name;
            this.inCreative = ScriptableObjectHelper.getBooleanProperty(scriptableObject, "inCreative", false);
            this.isTechnical = ScriptableObjectHelper.getBooleanProperty(scriptableObject, "isTech", this.inCreative ^ true);
            this.textures = new String[6];
            this.textureIds = new int[6];
            while (true) {
                Label_0346: {
                    try {
                        final NativeArray nativeArrayProperty = ScriptableObjectHelper.getNativeArrayProperty(scriptableObject, "texture", ScriptableObjectHelper.getNativeArrayProperty(scriptableObject, "textures", null));
                        if (nativeArrayProperty != null) {
                            final Object[] array = nativeArrayProperty.toArray();
                            for (int i = 0; i < 6; ++i) {
                                if (i <= array.length - 1) {
                                    break Label_0346;
                                }
                                final int n = array.length - 1;
                                final Object o = array[n];
                                if (o != null && o instanceof NativeArray) {
                                    final Object[] array2 = ((NativeArray)o).toArray();
                                    if (array2[0] instanceof CharSequence && array2[1] instanceof Number) {
                                        this.textures[i] = array2[0].toString();
                                        this.textureIds[i] = ((Number)array2[1]).intValue();
                                        if (!ResourcePackManager.isValidBlockTexture(this.textures[i], this.textureIds[i])) {
                                            this.textures[i] = "missing_block";
                                            this.textureIds[i] = 0;
                                        }
                                    }
                                }
                            }
                        }
                    }
                    catch (Exception ex) {
                        ex.printStackTrace();
                    }
                    break;
                }
                int i = 0;
                final int n = i;
                continue;
            }
        }
        this.validate();
        NameTranslation.sendNameToGenerateCache(uid, data, name);
    }
    
    private void validate() {
        for (int i = 0; i < 6; ++i) {
            if (this.textures[i] == null) {
                this.textures[i] = "missing_block";
                this.textureIds[i] = 0;
            }
        }
        for (int j = 0; j < 6; ++j) {
            if (!ResourcePackManager.isValidBlockTexture(this.textures[j], this.textureIds[j])) {
                final StringBuilder sb = new StringBuilder();
                sb.append("invalid block texture will be replaced with default: ");
                sb.append(this.textures[j]);
                sb.append(" ");
                sb.append(this.textureIds[j]);
                Logger.debug("INNERCORE-BLOCKS", sb.toString());
                this.textures[j] = "missing_block";
                this.textureIds[j] = 0;
            }
        }
    }
    
    public GuiBlockModel getGuiBlockModel() {
        return GuiBlockModel.createModelForBlockVariant(this);
    }
    
    public String getSpriteTexturePath() {
        return ResourcePackManager.getBlockTextureName(this.textures[0], this.textureIds[0]);
    }
}
