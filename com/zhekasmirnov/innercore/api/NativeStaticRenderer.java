package com.zhekasmirnov.innercore.api;

import com.android.tools.r8.annotations.*;
import com.zhekasmirnov.innercore.api.runtime.*;

@SynthesizedClassMap({ -$$Lambda$NativeStaticRenderer$LWtuV5nKwWOIm4aoEP7VmhrVVMs.class })
public class NativeStaticRenderer
{
    public static final float HUMANOID_MODEL_OFFSET = 1.6200027f;
    private boolean isHumanoidModel;
    private boolean isLightLocked;
    private boolean isPositionInterpolationEnabled;
    private long pointer;
    private float posX;
    private float posY;
    private float posZ;
    public final Transform transform;
    public final NativeShaderUniformSet uniformSet;
    
    private NativeStaticRenderer(final long pointer, final boolean isHumanoidModel) {
        this.posX = 0.0f;
        this.posY = 0.0f;
        this.posZ = 0.0f;
        this.isLightLocked = false;
        this.isPositionInterpolationEnabled = false;
        this.transform = new Transform();
        this.pointer = pointer;
        this.uniformSet = new NativeShaderUniformSet(nativeGetShaderUniformSet(pointer));
        this.isHumanoidModel = isHumanoidModel;
    }
    
    public static native long createStaticRenderer(final long p0, final float p1, final float p2, final float p3);
    
    public static NativeStaticRenderer createStaticRenderer(final NativeRenderer.Renderer renderer, final float n, final float n2, final float n3) {
        long pointer;
        if (renderer != null) {
            pointer = renderer.getPointer();
        }
        else {
            pointer = 0L;
        }
        final NativeStaticRenderer nativeStaticRenderer = new NativeStaticRenderer(createStaticRenderer(pointer, n, n2, n3), renderer != null && renderer.isHumanoid);
        nativeStaticRenderer.setPos(n, n2, n3);
        nativeStaticRenderer.setPos(n, n2, n3);
        return nativeStaticRenderer;
    }
    
    public static native void finalizeNative(final long p0);
    
    public static native boolean nativeExists(final long p0);
    
    public static native long nativeGetShaderUniformSet(final long p0);
    
    public static native void remove(final long p0);
    
    public static native void resetBlockLightPos(final long p0);
    
    public static native void setBlockLightPos(final long p0, final float p1, final float p2, final float p3);
    
    public static native void setIgnoreBlocklight(final long p0, final boolean p1);
    
    public static native void setInterpolationEnabled(final long p0, final boolean p1);
    
    public static native void setMesh(final long p0, final long p1);
    
    public static native void setMeshMaterial(final long p0, final String p1);
    
    public static native void setPos(final long p0, final float p1, final float p2, final float p3);
    
    public static native void setRenderer(final long p0, final long p1);
    
    public static native void setScale(final long p0, final float p1);
    
    public static native void setSkin(final long p0, final String p1);
    
    public static native void transformAddTransform(final long p0, final float p1, final float p2, final float p3, final float p4, final float p5, final float p6, final float p7, final float p8, final float p9, final float p10, final float p11, final float p12, final float p13, final float p14, final float p15, final float p16);
    
    public static native void transformClear(final long p0);
    
    public static native void transformLock(final long p0);
    
    public static native void transformRotate(final long p0, final float p1, final float p2, final float p3);
    
    public static native void transformScale(final long p0, final float p1, final float p2, final float p3);
    
    public static native void transformScaleLegacy(final long p0, final float p1);
    
    public static native void transformTranslate(final long p0, final float p1, final float p2, final float p3);
    
    public static native void transformUnlock(final long p0);
    
    public boolean exists() {
        return nativeExists(this.pointer);
    }
    
    @Override
    protected void finalize() throws Throwable {
        if (this.pointer != 0L) {
            finalizeNative(this.pointer);
        }
        super.finalize();
    }
    
    public NativeShaderUniformSet getShaderUniforms() {
        return this.uniformSet;
    }
    
    public void remove() {
        remove(this.pointer);
    }
    
    public void resetBlockLightPos() {
        resetBlockLightPos(this.pointer);
        this.isLightLocked = false;
    }
    
    public void setBlockLightPos(final float n, final float n2, final float n3) {
        setBlockLightPos(this.pointer, n, n2, n3);
        this.isLightLocked = true;
    }
    
    public void setIgnoreBlocklight(final boolean b) {
        setIgnoreBlocklight(this.pointer, b);
    }
    
    public void setInterpolationEnabled(final boolean isPositionInterpolationEnabled) {
        setInterpolationEnabled(this.pointer, isPositionInterpolationEnabled);
        this.isPositionInterpolationEnabled = isPositionInterpolationEnabled;
    }
    
    public void setMaterial(final String s) {
        if (s != null) {
            setMeshMaterial(this.pointer, s);
            return;
        }
        setMeshMaterial(this.pointer, "entity_alphatest_custom");
    }
    
    public void setMesh(final NativeRenderMesh nativeRenderMesh) {
        if (nativeRenderMesh != null) {
            setMesh(this.pointer, nativeRenderMesh.getPtr());
            return;
        }
        setMesh(this.pointer, 0L);
    }
    
    public void setPos(final float posX, final float posY, final float posZ) {
        this.posX = posX;
        this.posY = posY;
        this.posZ = posZ;
        if (!this.isLightLocked) {
            setBlockLightPos(this.pointer, posX, posY, posZ);
        }
        float n = posY;
        if (this.isHumanoidModel) {
            n = posY + 1.6200027f;
        }
        if (this.isPositionInterpolationEnabled) {
            MainThreadQueue.localThread.enqueue(new -$$Lambda$NativeStaticRenderer$LWtuV5nKwWOIm4aoEP7VmhrVVMs(this, posX, n, posZ));
            return;
        }
        setPos(this.pointer, posX, n, posZ);
    }
    
    public void setRenderer(final int n) {
        if (n == -1) {
            this.setRenderer(null);
            return;
        }
        final NativeRenderer.Renderer rendererById = NativeRenderer.getRendererById(n);
        if (rendererById != null) {
            this.setRenderer(rendererById);
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("invalid renderer id ");
        sb.append(n);
        sb.append(", id must belong only to custom renderer");
        throw new IllegalArgumentException(sb.toString());
    }
    
    public void setRenderer(final NativeRenderer.Renderer renderer) {
        if (renderer != null) {
            setRenderer(this.pointer, renderer.pointer);
            this.isHumanoidModel = renderer.isHumanoid;
        }
        else {
            setRenderer(this.pointer, 0L);
            this.isHumanoidModel = false;
        }
        this.setPos(this.posX, this.posY, this.posZ);
    }
    
    public void setScale(final float n) {
        setScale(this.pointer, n);
    }
    
    public void setSkin(final String s) {
        setSkin(this.pointer, s);
    }
    
    public class Transform
    {
        public Transform clear() {
            NativeStaticRenderer.transformClear(NativeStaticRenderer.this.pointer);
            return this;
        }
        
        public Transform lock() {
            NativeStaticRenderer.transformLock(NativeStaticRenderer.this.pointer);
            return this;
        }
        
        public Transform matrix(final float n, final float n2, final float n3, final float n4, final float n5, final float n6, final float n7, final float n8, final float n9, final float n10, final float n11, final float n12, final float n13, final float n14, final float n15, final float n16) {
            NativeStaticRenderer.transformAddTransform(NativeStaticRenderer.this.pointer, n, n2, n3, n4, n5, n6, n7, n8, n9, n10, n11, n12, n13, n14, n15, n16);
            return this;
        }
        
        public Transform rotate(final double n, final double n2, final double n3) {
            NativeStaticRenderer.transformRotate(NativeStaticRenderer.this.pointer, (float)n, (float)n2, (float)n3);
            return this;
        }
        
        public Transform scale(final float n, final float n2, final float n3) {
            NativeStaticRenderer.transformScale(NativeStaticRenderer.this.pointer, n, n2, n3);
            return this;
        }
        
        public Transform scaleLegacy(final float n) {
            NativeStaticRenderer.transformScaleLegacy(NativeStaticRenderer.this.pointer, n);
            return this;
        }
        
        public Transform translate(final double n, final double n2, final double n3) {
            NativeStaticRenderer.transformTranslate(NativeStaticRenderer.this.pointer, (float)n, (float)n2, (float)n3);
            return this;
        }
        
        public Transform unlock() {
            NativeStaticRenderer.transformUnlock(NativeStaticRenderer.this.pointer);
            return this;
        }
    }
}
