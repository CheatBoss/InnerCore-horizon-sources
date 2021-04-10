package com.zhekasmirnov.innercore.api;

import com.zhekasmirnov.innercore.api.mod.ui.*;
import com.zhekasmirnov.innercore.mod.resource.*;
import org.mozilla.javascript.*;
import com.zhekasmirnov.horizon.runtime.logger.*;

public class NativeBlockModel
{
    public final GuiBlockModel.Builder guiModelBuilder;
    public final long pointer;
    
    public NativeBlockModel() {
        this(constructBlockModel());
    }
    
    private NativeBlockModel(final long pointer) {
        this.guiModelBuilder = new GuiBlockModel.Builder();
        this.pointer = pointer;
    }
    
    public NativeBlockModel(final NativeRenderMesh nativeRenderMesh) {
        this();
        this.addMesh(nativeRenderMesh);
    }
    
    public static native void addBlock(final long p0, final int p1, final int p2, final boolean p3);
    
    public static native void addBoxId(final long p0, final float p1, final float p2, final float p3, final float p4, final float p5, final float p6, final int p7, final int p8);
    
    public static native void addBoxTexture(final long p0, final float p1, final float p2, final float p3, final float p4, final float p5, final float p6, final String p7, final int p8);
    
    public static native void addBoxTextureSet(final long p0, final float p1, final float p2, final float p3, final float p4, final float p5, final float p6, final String p7, final int p8, final String p9, final int p10, final String p11, final int p12, final String p13, final int p14, final String p15, final int p16, final String p17, final int p18);
    
    public static native void addMesh(final long p0, final long p1);
    
    public static native long constructBlockModel();
    
    public static NativeBlockModel createBlockModel() {
        return new NativeBlockModel(constructBlockModel());
    }
    
    public static NativeBlockModel createTexturedBlock(final ScriptableObject scriptableObject) {
        return createTexturedBox(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, scriptableObject);
    }
    
    public static NativeBlockModel createTexturedBox(final float n, final float n2, final float n3, final float n4, final float n5, final float n6, final ScriptableObject scriptableObject) {
        final NativeBlockModel blockModel = createBlockModel();
        blockModel.addBox(n, n2, n3, n4, n5, n6, scriptableObject);
        return blockModel;
    }
    
    public void addBlock(final int n) {
        addBlock(this.pointer, n, 0, false);
    }
    
    public void addBlock(final int n, final int n2) {
        addBlock(this.pointer, n, n2, false);
    }
    
    public void addBlock(final int n, final int n2, final boolean b) {
        addBlock(this.pointer, n, n2, b);
    }
    
    public void addBox(final float n, final float n2, final float n3, final float n4, final float n5, final float n6, final int n7, final int n8) {
        addBoxId(this.pointer, n, n2, n3, n4, n5, n6, n7, n8);
        this.guiModelBuilder.add(new GuiBlockModel.Builder.PrecompiledBox(null, n, n2, 1.0f - n3, n4, n5, 1.0f - n6).setBlock(n7, n8));
    }
    
    public void addBox(final float n, final float n2, final float n3, final float n4, final float n5, final float n6, String s, int n7) {
        if (!ResourcePackManager.isValidBlockTexture(s, n7)) {
            s = "missing_texture";
            n7 = 0;
        }
        final GuiBlockModel.Builder.PrecompiledBox precompiledBox = new GuiBlockModel.Builder.PrecompiledBox(null, n, n2, 1.0f - n3, n4, n5, 1.0f - n6);
        precompiledBox.addTexture(s, n7);
        precompiledBox.addTexture(s, n7);
        precompiledBox.addTexture(s, n7);
        precompiledBox.addTexture(s, n7);
        precompiledBox.addTexture(s, n7);
        precompiledBox.addTexture(s, n7);
        this.guiModelBuilder.add(precompiledBox);
        addBoxTexture(this.pointer, n, n2, n3, n4, n5, n6, s, n7);
    }
    
    public void addBox(final float n, final float n2, final float n3, final float n4, final float n5, final float n6, final String s, final int n7, final String s2, final int n8, final String s3, final int n9, final String s4, final int n10, final String s5, final int n11, final String s6, final int n12) {
        addBoxTextureSet(this.pointer, n, n2, n3, n4, n5, n6, s, n7, s2, n8, s3, n9, s4, n10, s5, n11, s6, n12);
        final GuiBlockModel.Builder.PrecompiledBox precompiledBox = new GuiBlockModel.Builder.PrecompiledBox(null, n, n2, 1.0f - n3, n4, n5, 1.0f - n6);
        precompiledBox.addTexture(s, n7);
        precompiledBox.addTexture(s2, n8);
        precompiledBox.addTexture(s3, n9);
        precompiledBox.addTexture(s4, n10);
        precompiledBox.addTexture(s5, n11);
        precompiledBox.addTexture(s6, n12);
        this.guiModelBuilder.add(precompiledBox);
    }
    
    public void addBox(final float n, final float n2, final float n3, final float n4, final float n5, final float n6, final ScriptableObject scriptableObject) {
        final String[] array = new String[6];
        final int[] array2 = new int[6];
        if (scriptableObject instanceof NativeArray) {
            final Object[] array3 = ((NativeArray)scriptableObject).toArray();
            for (int i = 0; i < 6; ++i) {
                int n7;
                if (i > array3.length - 1) {
                    n7 = array3.length - 1;
                }
                else {
                    n7 = i;
                }
                final Object o = array3[n7];
                if (o != null && o instanceof NativeArray) {
                    final Object[] array4 = ((NativeArray)o).toArray();
                    if (array4[0] instanceof CharSequence && array4[1] instanceof Number) {
                        array[i] = array4[0].toString();
                        array2[i] = ((Number)array4[1]).intValue();
                    }
                }
            }
            for (int j = 0; j < 6; ++j) {
                if (array[j] == null) {
                    array[j] = "missing_block";
                    array2[j] = 0;
                }
            }
            for (int k = 0; k < 6; ++k) {
                if (!ResourcePackManager.isValidBlockTexture(array[k], array2[k])) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("invalid block texture will be replaced with default: ");
                    sb.append(array[k]);
                    sb.append(" ");
                    sb.append(array2[k]);
                    Logger.debug("INNERCORE-BLOCKS", sb.toString());
                    array[k] = "missing_block";
                    array2[k] = 0;
                }
            }
            this.addBox(n, n2, n3, n4, n5, n6, array[0], array2[0], array[1], array2[1], array[2], array2[2], array[3], array2[3], array[4], array2[4], array[5], array2[5]);
            return;
        }
        throw new IllegalArgumentException("texture set must be javascript array!");
    }
    
    public void addMesh(final NativeRenderMesh nativeRenderMesh) {
        final long pointer = this.pointer;
        long ptr;
        if (nativeRenderMesh != null) {
            ptr = nativeRenderMesh.getPtr();
        }
        else {
            ptr = 0L;
        }
        addMesh(pointer, ptr);
    }
    
    public GuiBlockModel buildGuiModel(final boolean b) {
        return this.guiModelBuilder.build(b);
    }
}
