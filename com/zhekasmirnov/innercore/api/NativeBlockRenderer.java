package com.zhekasmirnov.innercore.api;

import java.util.*;
import org.mozilla.javascript.annotations.*;
import org.mozilla.javascript.*;
import com.zhekasmirnov.innercore.api.commontypes.*;
import com.zhekasmirnov.innercore.mod.executable.*;
import com.zhekasmirnov.innercore.api.log.*;
import com.zhekasmirnov.innercore.api.runtime.*;

public class NativeBlockRenderer
{
    private static HashMap<Integer, Function> jsRenderCallbacks;
    
    static {
        NativeBlockRenderer.jsRenderCallbacks = new HashMap<Integer, Function>();
    }
    
    public static void _addRenderCallback(final int n, final Function function) {
        NativeBlockRenderer.jsRenderCallbacks.put(n, function);
    }
    
    @JSStaticFunction
    public static void disableCustomRender(final int n, final Object o) {
        NativeIdMapping.iterateMetadata(n, o, (NativeIdMapping.IIdIterator)new NativeIdMapping.IIdIterator() {
            @Override
            public void onIdDataIterated(final int n, final int n2) {
                NativeBlockRenderer.nativeDisableCustomRender(n, n2);
            }
        });
    }
    
    @JSStaticFunction
    public static void enableCoordMapping(final int n, final Object o, final Object o2) {
        NativeIdMapping.iterateMetadata(n, o, (NativeIdMapping.IIdIterator)new NativeIdMapping.IIdIterator() {
            boolean isModelRegistered = false;
            final /* synthetic */ NativeICRender.Model val$icRender = (NativeICRender.Model)Context.jsToJava(o2, (Class)NativeICRender.Model.class);
            
            @Override
            public void onIdDataIterated(final int n, final int n2) {
                if (!this.isModelRegistered) {
                    this.isModelRegistered = true;
                    registerIcRenderItemModel(n, n2, this.val$icRender);
                }
                NativeBlockRenderer.nativeEnableCoordMapping(n, n2, this.val$icRender.getPtr());
            }
        });
    }
    
    @JSStaticFunction
    public static void enableCustomRender(final int n, final Object o) {
        NativeIdMapping.iterateMetadata(n, o, (NativeIdMapping.IIdIterator)new NativeIdMapping.IIdIterator() {
            @Override
            public void onIdDataIterated(final int n, final int n2) {
                NativeBlockRenderer.nativeEnableCustomRender(n, n2);
            }
        });
    }
    
    @JSStaticFunction
    public static void mapAtCoords(final int n, final int n2, final int n3, final Object o, final boolean b) {
        nativeMapICRenderAtCoords(n, n2, n3, ((NativeICRender.Model)Context.jsToJava(o, (Class)NativeICRender.Model.class)).getPtr());
        if (!b) {
            NativeAPI.forceRenderRefresh(n, n2, n3, 0);
        }
    }
    
    @JSStaticFunction
    public static void mapCollisionAndRaycastModelAtCoords(final int n, final int n2, final int n3, final int n4, final Object o) {
        final NativeICRender.CollisionShape collisionShape = (NativeICRender.CollisionShape)Context.jsToJava(o, (Class)NativeICRender.CollisionShape.class);
        final long n5 = 0L;
        long ptr;
        if (collisionShape != null) {
            ptr = collisionShape.getPtr();
        }
        else {
            ptr = 0L;
        }
        nativeMapCollisionModelAtCoords(n, n2, n3, n4, ptr);
        long ptr2;
        if (collisionShape != null) {
            ptr2 = collisionShape.getPtr();
        }
        else {
            ptr2 = n5;
        }
        nativeMapRaycastModelAtCoords(n, n2, n3, n4, ptr2);
    }
    
    @JSStaticFunction
    public static void mapCollisionModelAtCoords(final int n, final int n2, final int n3, final int n4, final Object o) {
        final NativeICRender.CollisionShape collisionShape = (NativeICRender.CollisionShape)Context.jsToJava(o, (Class)NativeICRender.CollisionShape.class);
        long ptr;
        if (collisionShape != null) {
            ptr = collisionShape.getPtr();
        }
        else {
            ptr = 0L;
        }
        nativeMapCollisionModelAtCoords(n, n2, n3, n4, ptr);
    }
    
    @JSStaticFunction
    public static void mapRaycastModelAtCoords(final int n, final int n2, final int n3, final int n4, final Object o) {
        final NativeICRender.CollisionShape collisionShape = (NativeICRender.CollisionShape)Context.jsToJava(o, (Class)NativeICRender.CollisionShape.class);
        long ptr;
        if (collisionShape != null) {
            ptr = collisionShape.getPtr();
        }
        else {
            ptr = 0L;
        }
        nativeMapRaycastModelAtCoords(n, n2, n3, n4, ptr);
    }
    
    public static native void nativeDisableCustomRender(final int p0, final int p1);
    
    public static native void nativeEnableCoordMapping(final int p0, final int p1, final long p2);
    
    public static native void nativeEnableCustomRender(final int p0, final int p1);
    
    public static native void nativeEnableStaticModel(final int p0, final int p1, final long p2);
    
    public static native void nativeMapCollisionModelAtCoords(final int p0, final int p1, final int p2, final int p3, final long p4);
    
    public static native void nativeMapICRenderAtCoords(final int p0, final int p1, final int p2, final long p3);
    
    public static native void nativeMapModelAtCoords(final int p0, final int p1, final int p2, final long p3);
    
    public static native void nativeMapRaycastModelAtCoords(final int p0, final int p1, final int p2, final int p3, final long p4);
    
    public static native void nativeSetCollisionShape(final int p0, final int p1, final long p2);
    
    public static native void nativeSetRaycastShape(final int p0, final int p1, final long p2);
    
    public static native void nativeUnmapAtCoords(final int p0, final int p1, final int p2);
    
    public static void onRenderCall(final RenderAPI renderAPI, final Coords coords, final FullBlock fullBlock, final boolean b) {
        Callback.invokeAPICallback("CustomBlockTessellation", renderAPI, coords, fullBlock, b);
        final Function function = NativeBlockRenderer.jsRenderCallbacks.get(fullBlock.id);
        if (function != null) {
            try {
                function.call(Compiler.assureContextForCurrentThread(), function.getParentScope(), function.getParentScope(), new Object[] { renderAPI, coords, fullBlock, b });
            }
            catch (Exception ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append("error occurred in block tessellation id=");
                sb.append(fullBlock.id);
                sb.append(" data=");
                sb.append(fullBlock.data);
                ICLog.e("TESSELLATION", sb.toString(), ex);
                if (LevelInfo.isLoaded()) {
                    NativeAPI.clientMessage("§cerror occurred in block tessellation.");
                }
            }
        }
    }
    
    private static void registerIcRenderItemModel(final int n, final int n2, final NativeICRender.Model model) {
        final NativeItemModel for1 = NativeItemModel.getFor(n, n2);
        if (for1.isEmpty()) {
            for1.setModel(model);
            if (for1.getCacheKey() == null) {
                for1.setCacheKey("icrender-modded");
            }
        }
    }
    
    public static native void renderBlock(final long p0, final int p1, final int p2, final int p3, final int p4, final int p5, final boolean p6);
    
    public static native void renderBlockHere(final long p0, final int p1, final int p2, final boolean p3);
    
    public static native void renderBoxId(final long p0, final int p1, final int p2, final int p3, final float p4, final float p5, final float p6, final float p7, final float p8, final float p9, final int p10, final int p11, final boolean p12);
    
    public static native void renderBoxIdHere(final long p0, final float p1, final float p2, final float p3, final float p4, final float p5, final float p6, final int p7, final int p8, final boolean p9);
    
    public static native void renderBoxPtr(final long p0, final int p1, final int p2, final int p3, final float p4, final float p5, final float p6, final float p7, final float p8, final float p9, final long p10, final int p11, final boolean p12);
    
    public static native void renderBoxPtrHere(final long p0, final float p1, final float p2, final float p3, final float p4, final float p5, final float p6, final long p7, final int p8, final boolean p9);
    
    public static native void renderBoxTexture(final long p0, final int p1, final int p2, final int p3, final float p4, final float p5, final float p6, final float p7, final float p8, final float p9, final String p10, final int p11);
    
    public static native void renderBoxTextureHere(final long p0, final float p1, final float p2, final float p3, final float p4, final float p5, final float p6, final String p7, final int p8);
    
    public static native void renderBoxTextureSet(final long p0, final int p1, final int p2, final int p3, final float p4, final float p5, final float p6, final float p7, final float p8, final float p9, final String p10, final int p11, final String p12, final int p13, final String p14, final int p15, final String p16, final int p17, final String p18, final int p19, final String p20, final int p21);
    
    public static native void renderBoxTextureSetHere(final long p0, final float p1, final float p2, final float p3, final float p4, final float p5, final float p6, final String p7, final int p8, final String p9, final int p10, final String p11, final int p12, final String p13, final int p14, final String p15, final int p16, final String p17, final int p18);
    
    public static native void renderModel(final long p0, final int p1, final int p2, final int p3, final long p4);
    
    public static native void renderModelHere(final long p0, final long p1);
    
    @JSStaticFunction
    public static void setCustomCollisionAndRaycastShape(final int n, final Object o, final Object o2) {
        NativeIdMapping.iterateMetadata(n, o, (NativeIdMapping.IIdIterator)new NativeIdMapping.IIdIterator() {
            @Override
            public void onIdDataIterated(final int n, final int n2) {
                final NativeICRender.CollisionShape collisionShape = (NativeICRender.CollisionShape)Context.jsToJava(o2, (Class)NativeICRender.CollisionShape.class);
                final long n3 = 0L;
                long ptr;
                if (collisionShape != null) {
                    ptr = collisionShape.getPtr();
                }
                else {
                    ptr = 0L;
                }
                NativeBlockRenderer.nativeSetCollisionShape(n, n2, ptr);
                long ptr2 = n3;
                if (collisionShape != null) {
                    ptr2 = collisionShape.getPtr();
                }
                NativeBlockRenderer.nativeSetRaycastShape(n, n2, ptr2);
            }
        });
    }
    
    @JSStaticFunction
    public static void setCustomCollisionShape(final int n, final Object o, final Object o2) {
        NativeIdMapping.iterateMetadata(n, o, (NativeIdMapping.IIdIterator)new NativeIdMapping.IIdIterator() {
            @Override
            public void onIdDataIterated(final int n, final int n2) {
                final NativeICRender.CollisionShape collisionShape = (NativeICRender.CollisionShape)Context.jsToJava(o2, (Class)NativeICRender.CollisionShape.class);
                long ptr;
                if (collisionShape != null) {
                    ptr = collisionShape.getPtr();
                }
                else {
                    ptr = 0L;
                }
                NativeBlockRenderer.nativeSetCollisionShape(n, n2, ptr);
            }
        });
    }
    
    @JSStaticFunction
    public static void setCustomRaycastShape(final int n, final Object o, final Object o2) {
        NativeIdMapping.iterateMetadata(n, o, (NativeIdMapping.IIdIterator)new NativeIdMapping.IIdIterator() {
            @Override
            public void onIdDataIterated(final int n, final int n2) {
                final NativeICRender.CollisionShape collisionShape = (NativeICRender.CollisionShape)Context.jsToJava(o2, (Class)NativeICRender.CollisionShape.class);
                long ptr;
                if (collisionShape != null) {
                    ptr = collisionShape.getPtr();
                }
                else {
                    ptr = 0L;
                }
                NativeBlockRenderer.nativeSetRaycastShape(n, n2, ptr);
            }
        });
    }
    
    @JSStaticFunction
    public static void setStaticICRender(final int n, final Object o, final Object o2) {
        NativeIdMapping.iterateMetadata(n, o, (NativeIdMapping.IIdIterator)new NativeIdMapping.IIdIterator() {
            boolean isModelRegistered = false;
            final /* synthetic */ NativeICRender.Model val$icRender = (NativeICRender.Model)Context.jsToJava(o2, (Class)NativeICRender.Model.class);
            
            @Override
            public void onIdDataIterated(final int n, final int n2) {
                if (!this.isModelRegistered) {
                    this.isModelRegistered = true;
                    registerIcRenderItemModel(n, n2, this.val$icRender);
                }
                NativeBlockRenderer.nativeEnableStaticModel(n, n2, this.val$icRender.getPtr());
            }
        });
    }
    
    @JSStaticFunction
    public static void unmapAtCoords(final int n, final int n2, final int n3, final boolean b) {
        nativeUnmapAtCoords(n, n2, n3);
        if (!b) {
            NativeAPI.forceRenderRefresh(n, n2, n3, 0);
        }
    }
    
    @JSStaticFunction
    public static void unmapCollisionAndRaycastModelAtCoords(final int n, final int n2, final int n3, final int n4) {
        nativeMapCollisionModelAtCoords(n, n2, n3, n4, 0L);
        nativeMapRaycastModelAtCoords(n, n2, n3, n4, 0L);
    }
    
    @JSStaticFunction
    public static void unmapCollisionModelAtCoords(final int n, final int n2, final int n3, final int n4) {
        nativeMapCollisionModelAtCoords(n, n2, n3, n4, 0L);
    }
    
    @JSStaticFunction
    public static void unmapRaycastModelAtCoords(final int n, final int n2, final int n3, final int n4) {
        nativeMapRaycastModelAtCoords(n, n2, n3, n4, 0L);
    }
    
    public static class RenderAPI
    {
        private long pointer;
        
        public RenderAPI(final long pointer) {
            this.pointer = pointer;
        }
        
        public long getAddr() {
            return this.pointer;
        }
        
        public void renderBlock(final int n, final int n2, final int n3, final int n4, final int n5) {
            NativeBlockRenderer.renderBlock(this.pointer, n, n2, n3, n4, n5, true);
        }
        
        public void renderBlock(final int n, final int n2, final int n3, final int n4, final int n5, final boolean b) {
            NativeBlockRenderer.renderBlock(this.pointer, n, n2, n3, n4, n5, b);
        }
        
        public void renderBlockHere(final int n, final int n2) {
            NativeBlockRenderer.renderBlockHere(this.pointer, n, n2, true);
        }
        
        public void renderBlockHere(final int n, final int n2, final boolean b) {
            NativeBlockRenderer.renderBlockHere(this.pointer, n, n2, b);
        }
        
        public void renderBoxId(final int n, final int n2, final int n3, final float n4, final float n5, final float n6, final float n7, final float n8, final float n9, final int n10, final int n11) {
            NativeBlockRenderer.renderBoxId(this.pointer, n, n2, n3, n4, n5, n6, n7, n8, n9, n10, n11, true);
        }
        
        public void renderBoxIdHere(final float n, final float n2, final float n3, final float n4, final float n5, final float n6, final int n7, final int n8) {
            NativeBlockRenderer.renderBoxIdHere(this.pointer, n, n2, n3, n4, n5, n6, n7, n8, true);
        }
        
        public void renderBoxTexture(final int n, final int n2, final int n3, final float n4, final float n5, final float n6, final float n7, final float n8, final float n9, final String s, final int n10) {
            NativeBlockRenderer.renderBoxTexture(this.pointer, n, n2, n3, n4, n5, n6, n7, n8, n9, s, n10);
        }
        
        public void renderBoxTextureHere(final float n, final float n2, final float n3, final float n4, final float n5, final float n6, final String s, final int n7) {
            NativeBlockRenderer.renderBoxTextureHere(this.pointer, n, n2, n3, n4, n5, n6, s, n7);
        }
        
        public void renderModel(final int n, final int n2, final int n3, final NativeBlockModel nativeBlockModel) {
            NativeBlockRenderer.renderModel(this.pointer, n, n2, n3, nativeBlockModel.pointer);
        }
        
        public void renderModelHere(final NativeBlockModel nativeBlockModel) {
            NativeBlockRenderer.renderModelHere(this.pointer, nativeBlockModel.pointer);
        }
    }
}
