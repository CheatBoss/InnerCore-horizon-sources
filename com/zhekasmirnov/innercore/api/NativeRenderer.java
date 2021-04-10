package com.zhekasmirnov.innercore.api;

import java.lang.ref.*;
import org.mozilla.javascript.annotations.*;
import java.util.*;

public class NativeRenderer
{
    private static HashMap<Integer, Renderer> rendererById;
    private static HashMap<Integer, WeakReference<Renderer>> weakRendererById;
    
    static {
        NativeRenderer.rendererById = new HashMap<Integer, Renderer>();
        NativeRenderer.weakRendererById = new HashMap<Integer, WeakReference<Renderer>>();
    }
    
    public static native void addBox(final long p0, final float p1, final float p2, final float p3, final float p4, final float p5, final float p6, final float p7);
    
    public static native void addColoredBox(final long p0, final float p1, final float p2, final float p3, final float p4, final float p5, final float p6, final float p7, final float p8, final float p9, final float p10, final float p11);
    
    public static native long addModelPart(final long p0, final long p1, final String p2);
    
    public static native void clearAllModelParts(final long p0);
    
    public static native void clearModelPart(final long p0);
    
    public static native long constructHumanoidRenderer(final float p0);
    
    public static native long constructHumanoidRendererWithSkin(final String p0, final float p1);
    
    public static native long constructSpriteRenderer(final int p0);
    
    @JSStaticFunction
    public static Renderer createHumanoidRenderer(final double n) {
        double n2 = n;
        if (n == 0.0) {
            n2 = 1.0;
        }
        final Renderer renderer = new Renderer(constructHumanoidRenderer((float)n2));
        renderer.isHumanoid = true;
        NativeRenderer.rendererById.put(renderer.getRenderType(), renderer);
        return renderer;
    }
    
    @JSStaticFunction
    public static Renderer createItemSpriteRenderer(final int n) {
        final SpriteRenderer spriteRenderer = new SpriteRenderer(constructSpriteRenderer(n));
        ((Renderer)spriteRenderer).isHumanoid = false;
        NativeRenderer.rendererById.put(((Renderer)spriteRenderer).getRenderType(), (Renderer)spriteRenderer);
        return (Renderer)spriteRenderer;
    }
    
    @JSStaticFunction
    public static Renderer createRendererWithSkin(final String s, final double n) {
        double n2 = n;
        if (n == 0.0) {
            n2 = 1.0;
        }
        final Renderer renderer = new Renderer(constructHumanoidRendererWithSkin(s, (float)n2));
        renderer.isHumanoid = true;
        NativeRenderer.rendererById.put(renderer.getRenderType(), renderer);
        return renderer;
    }
    
    public static native long getModel(final long p0);
    
    public static native long getModelPart(final long p0, final String p1);
    
    @JSStaticFunction
    public static Renderer getRendererById(final int n) {
        Renderer renderer;
        if (NativeRenderer.rendererById.containsKey(n)) {
            renderer = NativeRenderer.rendererById.get(n);
        }
        else {
            if (!NativeRenderer.weakRendererById.containsKey(n)) {
                return null;
            }
            renderer = NativeRenderer.weakRendererById.get(n).get();
        }
        return renderer;
    }
    
    public static native int getRendererId(final long p0);
    
    public static native float getScale(final long p0);
    
    public static native boolean hasModelPart(final long p0, final String p1);
    
    public static native void resetModel(final long p0);
    
    public static native void setMesh(final long p0, final long p1);
    
    public static native void setOffset(final long p0, final float p1, final float p2, final float p3);
    
    public static native void setRotation(final long p0, final float p1, final float p2, final float p3);
    
    public static native void setScale(final long p0, final float p1);
    
    public static native void setSkin(final long p0, final String p1);
    
    public static native void setTextureOffset(final long p0, final int p1, final int p2);
    
    public static native void setTextureSize(final long p0, final int p1, final int p2);
    
    public static native void transformAddTransform(final long p0, final float p1, final float p2, final float p3, final float p4, final float p5, final float p6, final float p7, final float p8, final float p9, final float p10, final float p11, final float p12, final float p13, final float p14, final float p15, final float p16);
    
    public static native void transformClear(final long p0);
    
    public static native void transformLock(final long p0);
    
    public static native void transformRotate(final long p0, final float p1, final float p2, final float p3);
    
    public static native void transformScale(final long p0, final float p1, final float p2, final float p3);
    
    public static native void transformTranslate(final long p0, final float p1, final float p2, final float p3);
    
    public static native void transformUnlock(final long p0);
    
    public interface FinalizeCallback
    {
        void onFinalized(final Renderer p0);
    }
    
    public static class Model
    {
        private long pointer;
        private Renderer renderer;
        
        protected Model(final long pointer, final Renderer renderer) {
            this.pointer = pointer;
            this.renderer = renderer;
        }
        
        public void clearAllParts() {
            NativeRenderer.clearAllModelParts(this.pointer);
        }
        
        public ModelPart getPart(final String s) {
            if (this.hasPart(s)) {
                return new ModelPart(NativeRenderer.getModelPart(this.pointer, s), this);
            }
            return null;
        }
        
        public boolean hasPart(final String s) {
            return NativeRenderer.hasModelPart(this.pointer, s);
        }
        
        public void reset() {
            NativeRenderer.resetModel(this.pointer);
        }
    }
    
    public static class ModelPart
    {
        private NativeRenderMesh mesh;
        private Model model;
        private long pointer;
        
        protected ModelPart(final long pointer, final Model model) {
            this.mesh = null;
            this.pointer = pointer;
            this.model = model;
        }
        
        public void addBox(final float n, final float n2, final float n3, final float n4, final float n5, final float n6) {
            NativeRenderer.addBox(this.pointer, n, n2, n3, n4, n5, n6, 0.0f);
        }
        
        public void addBox(final float n, final float n2, final float n3, final float n4, final float n5, final float n6, final float n7) {
            NativeRenderer.addBox(this.pointer, n, n2, n3, n4, n5, n6, n7);
        }
        
        public ModelPart addPart(final String s) {
            if (this.model.hasPart(s)) {
                final ModelPart part = this.model.getPart(s);
                part.clear();
                return part;
            }
            return new ModelPart(NativeRenderer.addModelPart(this.model.pointer, this.pointer, s), this.model);
        }
        
        public void clear() {
            NativeRenderer.clearModelPart(this.pointer);
            this.setMesh(null);
        }
        
        public NativeRenderMesh getMesh() {
            return this.mesh;
        }
        
        public void setMesh(final NativeRenderMesh mesh) {
            this.mesh = mesh;
            final long pointer = this.pointer;
            long ptr;
            if (mesh != null) {
                ptr = mesh.getPtr();
            }
            else {
                ptr = 0L;
            }
            NativeRenderer.setMesh(pointer, ptr);
        }
        
        public void setOffset(final float n, final float n2, final float n3) {
            NativeRenderer.setOffset(this.pointer, n, n2, n3);
        }
        
        public void setRotation(final float n, final float n2, final float n3) {
            NativeRenderer.setRotation(this.pointer, n, n2, n3);
        }
        
        public void setTextureOffset(final int n, final int n2) {
            NativeRenderer.setTextureOffset(this.pointer, n, n2);
        }
        
        public void setTextureOffset(final int n, final int n2, final boolean b) {
            this.setTextureOffset(n, n2);
        }
        
        public void setTextureSize(final int n, final int n2) {
            NativeRenderer.setTextureSize(this.pointer, n, n2);
        }
        
        public void setTextureSize(final int n, final int n2, final boolean b) {
            this.setTextureSize(n, n2);
        }
    }
    
    public static class RenderPool
    {
        private final ArrayList<Renderer> pool;
        private final IFactory renderFactory;
        private int totalRenderInstances;
        
        public RenderPool() {
            this.pool = new ArrayList<Renderer>();
            this.totalRenderInstances = 0;
            this.renderFactory = (IFactory)new IFactory() {
                @Override
                public Renderer newRender() {
                    return NativeRenderer.createHumanoidRenderer(1.0);
                }
            };
        }
        
        public RenderPool(final IFactory renderFactory) {
            this.pool = new ArrayList<Renderer>();
            this.totalRenderInstances = 0;
            this.renderFactory = renderFactory;
        }
        
        public Renderer getRender() {
            synchronized (this.pool) {
                if (this.pool.size() > 0) {
                    return this.pool.remove(0);
                }
                final Renderer render = this.renderFactory.newRender();
                render.addFinalizeCallback(new FinalizeCallback() {
                    @Override
                    public void onFinalized(final Renderer renderer) {
                        synchronized (RenderPool.this.pool) {
                            final Renderer renderer2 = new Renderer(renderer.getPointer());
                            renderer2.setFinalizeable(true);
                            renderer2.addFinalizeCallback(this);
                            renderer2.isHumanoid = renderer.isHumanoid;
                            RenderPool.this.pool.add(renderer2);
                        }
                    }
                });
                render.setFinalizeable(true);
                return render;
            }
        }
        
        public interface IFactory
        {
            Renderer newRender();
        }
    }
    
    public static class Renderer
    {
        private final ArrayList<FinalizeCallback> finalizeCallbacks;
        protected int id;
        public boolean isHumanoid;
        private boolean isReleased;
        protected Model model;
        protected long pointer;
        public Transform transform;
        
        private Renderer() {
            this.isHumanoid = false;
            this.transform = new Transform();
            this.isReleased = false;
            this.finalizeCallbacks = new ArrayList<FinalizeCallback>();
            this.model = null;
        }
        
        public Renderer(final long pointer) {
            this.isHumanoid = false;
            this.transform = new Transform();
            this.isReleased = false;
            this.finalizeCallbacks = new ArrayList<FinalizeCallback>();
            this.pointer = pointer;
            this.model = new Model(NativeRenderer.getModel(this.pointer), this);
            this.id = NativeRenderer.getRendererId(this.pointer);
        }
        
        public void addFinalizeCallback(final FinalizeCallback finalizeCallback) {
            this.finalizeCallbacks.add(finalizeCallback);
        }
        
        @Override
        protected void finalize() throws Throwable {
            this.release();
            super.finalize();
        }
        
        public Model getModel() {
            return this.model;
        }
        
        public long getPointer() {
            return this.pointer;
        }
        
        public int getRenderType() {
            return this.id;
        }
        
        public float getScale() {
            return NativeRenderer.getScale(this.pointer);
        }
        
        public void release() {
            if (!this.isReleased) {
                this.isReleased = true;
                final Iterator<FinalizeCallback> iterator = this.finalizeCallbacks.iterator();
                while (iterator.hasNext()) {
                    iterator.next().onFinalized(this);
                }
            }
        }
        
        public void setFinalizeable(final boolean b) {
            if (b) {
                NativeRenderer.rendererById.remove(this.id);
                NativeRenderer.weakRendererById.put(this.id, new WeakReference<Renderer>(this));
                return;
            }
            NativeRenderer.rendererById.put(this.id, this);
            NativeRenderer.weakRendererById.remove(this.id);
        }
        
        public void setScale(final float n) {
            NativeRenderer.setScale(this.pointer, n);
        }
        
        public void setSkin(final String s) {
            NativeRenderer.setSkin(this.pointer, s);
        }
        
        public class Transform
        {
            public Transform clear() {
                NativeRenderer.transformClear(Renderer.this.pointer);
                return this;
            }
            
            public Transform lock() {
                NativeRenderer.transformLock(Renderer.this.pointer);
                return this;
            }
            
            public Transform matrix(final float n, final float n2, final float n3, final float n4, final float n5, final float n6, final float n7, final float n8, final float n9, final float n10, final float n11, final float n12, final float n13, final float n14, final float n15, final float n16) {
                NativeRenderer.transformAddTransform(Renderer.this.pointer, n, n2, n3, n4, n5, n6, n7, n8, n9, n10, n11, n12, n13, n14, n15, n16);
                return this;
            }
            
            public Transform rotate(final double n, final double n2, final double n3) {
                NativeRenderer.transformRotate(Renderer.this.pointer, (float)n, (float)n2, (float)n3);
                return this;
            }
            
            public Transform scale(final float n, final float n2, final float n3) {
                NativeRenderer.transformScale(Renderer.this.pointer, n, n2, n3);
                return this;
            }
            
            public Transform translate(final double n, final double n2, final double n3) {
                NativeRenderer.transformTranslate(Renderer.this.pointer, (float)n, (float)n2, (float)n3);
                return this;
            }
            
            public Transform unlock() {
                NativeRenderer.transformUnlock(Renderer.this.pointer);
                return this;
            }
        }
    }
    
    public static class SpriteRenderer extends Renderer
    {
        protected SpriteRenderer(final long pointer) {
            this.pointer = pointer;
            this.id = NativeRenderer.getRendererId(this.pointer);
        }
    }
}
