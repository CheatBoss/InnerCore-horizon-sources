package com.zhekasmirnov.innercore.api;

import java.util.*;

public class NativeActorRenderer
{
    private static final Map<String, Integer> templateNameToId;
    private final Map<String, ModelPart> parts;
    public final long pointer;
    private final NativeShaderUniformSet uniformSet;
    
    static {
        (templateNameToId = new HashMap<String, Integer>()).put("helmet", 0);
        NativeActorRenderer.templateNameToId.put("chestplate", 1);
        NativeActorRenderer.templateNameToId.put("leggings", 2);
        NativeActorRenderer.templateNameToId.put("boots", 3);
    }
    
    public NativeActorRenderer() {
        this.parts = new HashMap<String, ModelPart>();
        this.pointer = nativeConstruct();
        final long nativeGetUniformSet = nativeGetUniformSet(this.pointer);
        NativeShaderUniformSet uniformSet;
        if (nativeGetUniformSet != 0L) {
            uniformSet = new NativeShaderUniformSet(nativeGetUniformSet);
        }
        else {
            uniformSet = null;
        }
        this.uniformSet = uniformSet;
        this.setMaterial("entity_alphatest_custom");
    }
    
    public NativeActorRenderer(long nativeGetUniformSet) {
        this.parts = new HashMap<String, ModelPart>();
        this.pointer = nativeGetUniformSet;
        nativeGetUniformSet = nativeGetUniformSet(nativeGetUniformSet);
        NativeShaderUniformSet uniformSet;
        if (nativeGetUniformSet != 0L) {
            uniformSet = new NativeShaderUniformSet(nativeGetUniformSet);
        }
        else {
            uniformSet = null;
        }
        this.uniformSet = uniformSet;
    }
    
    public NativeActorRenderer(final String s) {
        this.parts = new HashMap<String, ModelPart>();
        final Integer n = NativeActorRenderer.templateNameToId.get(s);
        long pointer;
        if (n != null) {
            pointer = nativeConstructFromTemplate(n);
        }
        else {
            pointer = nativeConstruct();
        }
        this.pointer = pointer;
        final long nativeGetUniformSet = nativeGetUniformSet(this.pointer);
        NativeShaderUniformSet uniformSet;
        if (nativeGetUniformSet != 0L) {
            uniformSet = new NativeShaderUniformSet(nativeGetUniformSet);
        }
        else {
            uniformSet = null;
        }
        this.uniformSet = uniformSet;
        this.setMaterial("entity_alphatest_custom");
    }
    
    private static native long nativeAddPart(final long p0, final long p1, final String p2);
    
    private static native long nativeAddRootPart(final long p0, final String p1);
    
    private static native long nativeConstruct();
    
    private static native long nativeConstructFromTemplate(final int p0);
    
    private static native long nativeGetPart(final long p0, final String p1);
    
    private static native long nativeGetUniformSet(final long p0);
    
    private static native void nativePartAddBox(final long p0, final float p1, final float p2, final float p3, final float p4, final float p5, final float p6, final float p7, final float p8, final float p9);
    
    private static native void nativePartClear(final long p0);
    
    private static native void nativePartSetMaterial(final long p0, final String p1);
    
    private static native void nativePartSetMesh(final long p0, final long p1);
    
    private static native void nativePartSetMirrored(final long p0, final boolean p1);
    
    private static native void nativePartSetOffset(final long p0, final float p1, final float p2, final float p3);
    
    private static native void nativePartSetPivot(final long p0, final float p1, final float p2, final float p3);
    
    private static native void nativePartSetRotation(final long p0, final float p1, final float p2, final float p3);
    
    private static native void nativePartSetTexture(final long p0, final String p1);
    
    private static native void nativePartSetTextureSize(final long p0, final float p1, final float p2);
    
    private static native void nativeSetRendererMaterial(final long p0, final String p1);
    
    private static native void nativeSetRendererTexture(final long p0, final String p1);
    
    public ModelPart addPart(final String s) {
        if (this.getPart(s) != null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("cannot add part ");
            sb.append(s);
            sb.append(", part with this name already exists");
            throw new IllegalArgumentException(sb.toString());
        }
        synchronized (this.parts) {
            final long nativeAddRootPart = nativeAddRootPart(this.pointer, s);
            if (nativeAddRootPart != 0L) {
                final ModelPart modelPart = new ModelPart(nativeAddRootPart);
                this.parts.put(s, modelPart);
                return modelPart;
            }
            return null;
        }
    }
    
    public ModelPart addPart(final String s, final NativeRenderMesh mesh) {
        return this.addPart(s).setMesh(mesh);
    }
    
    public ModelPart addPart(final String s, final String s2) {
        if (this.getPart(s) != null) {
            final StringBuilder sb = new StringBuilder();
            sb.append("cannot add part ");
            sb.append(s);
            sb.append(", part with this name already exists");
            throw new IllegalArgumentException(sb.toString());
        }
        final ModelPart part = this.getPart(s2);
        if (part == null) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("cannot add part ");
            sb2.append(s);
            sb2.append(", parent part ");
            sb2.append(s2);
            sb2.append(" does not exist");
            throw new IllegalArgumentException(sb2.toString());
        }
        synchronized (this.parts) {
            final long nativeAddPart = nativeAddPart(this.pointer, part.pointer, s);
            if (nativeAddPart != 0L) {
                final ModelPart modelPart = new ModelPart(nativeAddPart);
                this.parts.put(s, modelPart);
                return modelPart;
            }
            return null;
        }
    }
    
    public ModelPart addPart(final String s, final String s2, final NativeRenderMesh mesh) {
        return this.addPart(s, s2).setMesh(mesh);
    }
    
    public ModelPart getPart(final String s) {
        synchronized (this.parts) {
            ModelPart modelPart2;
            final ModelPart modelPart = modelPart2 = this.parts.get(s);
            if (modelPart == null) {
                final long nativeGetPart = nativeGetPart(this.pointer, s);
                modelPart2 = modelPart;
                if (nativeGetPart != 0L) {
                    modelPart2 = new ModelPart(nativeGetPart);
                    this.parts.put(s, modelPart2);
                }
            }
            return modelPart2;
        }
    }
    
    public NativeShaderUniformSet getUniformSet() {
        return this.uniformSet;
    }
    
    public NativeActorRenderer setMaterial(final String s) {
        nativeSetRendererMaterial(this.pointer, s);
        return this;
    }
    
    public NativeActorRenderer setTexture(final String s) {
        nativeSetRendererTexture(this.pointer, s);
        return this;
    }
    
    public class ModelPart
    {
        public final long pointer;
        
        private ModelPart(final long pointer) {
            this.pointer = pointer;
        }
        
        public ModelPart addBox(final float n, final float n2, final float n3, final float n4, final float n5, final float n6, final float n7, final float n8, final float n9) {
            nativePartAddBox(this.pointer, n, n2, n3, n4, n5, n6, n7, n8, n9);
            return this;
        }
        
        public ModelPart clear() {
            nativePartClear(this.pointer);
            return this;
        }
        
        public NativeActorRenderer endPart() {
            return NativeActorRenderer.this;
        }
        
        public ModelPart setMaterial(final String s) {
            nativePartSetMaterial(this.pointer, s);
            return this;
        }
        
        public ModelPart setMesh(final NativeRenderMesh nativeRenderMesh) {
            final long pointer = this.pointer;
            long ptr;
            if (nativeRenderMesh != null) {
                ptr = nativeRenderMesh.getPtr();
            }
            else {
                ptr = 0L;
            }
            nativePartSetMesh(pointer, ptr);
            return this;
        }
        
        public ModelPart setMirrored(final boolean b) {
            nativePartSetMirrored(this.pointer, b);
            return this;
        }
        
        public ModelPart setOffset(final float n, final float n2, final float n3) {
            nativePartSetOffset(this.pointer, n, n2, n3);
            return this;
        }
        
        public ModelPart setPivot(final float n, final float n2, final float n3) {
            nativePartSetPivot(this.pointer, n, n2, n3);
            return this;
        }
        
        public ModelPart setRotation(final float n, final float n2, final float n3) {
            nativePartSetRotation(this.pointer, n, n2, n3);
            return this;
        }
        
        public ModelPart setTexture(final String s) {
            nativePartSetTexture(this.pointer, s);
            return this;
        }
        
        public ModelPart setTextureSize(final float n, final float n2) {
            nativePartSetTextureSize(this.pointer, n, n2);
            return this;
        }
    }
}
