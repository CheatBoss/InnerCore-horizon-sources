package com.zhekasmirnov.innercore.api.particles;

import java.util.*;
import com.zhekasmirnov.innercore.api.*;
import org.mozilla.javascript.annotations.*;
import com.zhekasmirnov.innercore.api.mod.*;
import org.mozilla.javascript.*;
import com.zhekasmirnov.innercore.api.commontypes.*;
import com.zhekasmirnov.innercore.utils.*;
import com.zhekasmirnov.innercore.api.log.*;
import com.zhekasmirnov.horizon.runtime.logger.*;
import android.graphics.*;

public class ParticleRegistry
{
    private static HashMap<Integer, ParticleType> registeredParticleTypes;
    
    static {
        ParticleRegistry.registeredParticleTypes = new HashMap<Integer, ParticleType>();
    }
    
    @JSStaticFunction
    public static void addFarParticle(final int n, final double n2, final double n3, final double n4, final double n5, final double n6, final double n7, final int n8) {
        NativeAPI.addFarParticle(n, n2, n3, n4, n5, n6, n7, n8);
    }
    
    @JSStaticFunction
    public static void addParticle(final int n, final double n2, final double n3, final double n4, final double n5, final double n6, final double n7, final int n8) {
        NativeAPI.addParticle(n, n2, n3, n4, n5, n6, n7, n8);
    }
    
    @JSStaticFunction
    public static ParticleType getParticleTypeById(final int n) {
        return ParticleRegistry.registeredParticleTypes.get(n);
    }
    
    public static native long nativeNewParticleAnimator(final float p0, final float p1, final float p2, final float p3, final int p4);
    
    public static native long nativeNewParticleEmitter(final float p0, final float p1, final float p2);
    
    public static native long nativeNewParticleSubEmitter(final float p0, final int p1, final int p2, final int p3);
    
    public static native void nativeParticleEmit1(final long p0, final int p1, final int p2, final float p3, final float p4, final float p5);
    
    public static native void nativeParticleEmit2(final long p0, final int p1, final int p2, final float p3, final float p4, final float p5, final float p6, final float p7, final float p8);
    
    public static native void nativeParticleEmit3(final long p0, final int p1, final int p2, final float p3, final float p4, final float p5, final float p6, final float p7, final float p8, final float p9, final float p10, final float p11);
    
    public static native void nativeParticleEmitterAttachTo(final long p0, final long p1, final float p2, final float p3, final float p4);
    
    public static native void nativeParticleEmitterDetach(final long p0);
    
    public static native void nativeParticleEmitterGetPosition(final long p0, final float[] p1);
    
    public static native void nativeParticleEmitterMove(final long p0, final float p1, final float p2, final float p3);
    
    public static native void nativeParticleEmitterMoveTo(final long p0, final float p1, final float p2, final float p3);
    
    public static native void nativeParticleEmitterRelease(final long p0);
    
    public static native void nativeParticleEmitterSetVelocity(final long p0, final float p1, final float p2, final float p3);
    
    public static native void nativeParticleSubEmitterSetKeepEmitter(final long p0, final boolean p1);
    
    public static native void nativeParticleSubEmitterSetKeepVelocity(final long p0, final boolean p1);
    
    public static native void nativeParticleSubEmitterSetRandom(final long p0, final float p1);
    
    public static native int nativeParticleTypeGetID(final long p0);
    
    public static native void nativeParticleTypeSetAnimators(final long p0, final long p1, final long p2, final long p3);
    
    public static native void nativeParticleTypeSetAnimatorsNew(final long p0, final long p1, final long p2, final long p3, final long p4);
    
    public static native void nativeParticleTypeSetCollisionParams(final long p0, final boolean p1, final boolean p2, final int p3);
    
    public static native void nativeParticleTypeSetColor(final long p0, final float p1, final float p2, final float p3, final float p4);
    
    public static native void nativeParticleTypeSetColorNew(final long p0, final float p1, final float p2, final float p3, final float p4, final float p5, final float p6, final float p7, final float p8);
    
    public static native void nativeParticleTypeSetDefaultAcceleration(final long p0, final float p1, final float p2, final float p3);
    
    public static native void nativeParticleTypeSetDefaultVelocity(final long p0, final float p1, final float p2, final float p3);
    
    public static native void nativeParticleTypeSetFriction(final long p0, final float p1, final float p2);
    
    public static native void nativeParticleTypeSetLifetime(final long p0, final int p1, final int p2);
    
    public static native void nativeParticleTypeSetRebuildDelay(final long p0, final int p1);
    
    public static native void nativeParticleTypeSetRenderType(final long p0, final int p1);
    
    public static native void nativeParticleTypeSetSize(final long p0, final float p1, final float p2);
    
    public static native void nativeParticleTypeSetSubEmitters(final long p0, final long p1, final long p2, final long p3);
    
    public static native long nativeRegisterNewParticleType(final String p0, final float p1, final float p2, final float p3, final float p4, final int p5, final int p6, final boolean p7);
    
    @JSStaticFunction
    public static int registerParticleType(final Scriptable scriptable) {
        return new ParticleType(scriptable).getId();
    }
    
    public static class ParticleAnimator
    {
        private long ptr;
        
        public ParticleAnimator(final int n, final float n2, final float n3, final float n4, final float n5) {
            this.ptr = ParticleRegistry.nativeNewParticleAnimator(n2, n3, n4, n5, n);
        }
        
        public ParticleAnimator(final ScriptableObjectWrapper scriptableObjectWrapper) {
            this(scriptableObjectWrapper.getInt("period", -1), scriptableObjectWrapper.getFloat("fadeIn"), scriptableObjectWrapper.getFloat("start"), scriptableObjectWrapper.getFloat("fadeOut", 0.0f), scriptableObjectWrapper.getFloat("end", 0.0f));
        }
        
        public ParticleAnimator(final Scriptable scriptable) {
            this(new ScriptableObjectWrapper(scriptable));
        }
    }
    
    public static class ParticleEmitter
    {
        private boolean isRelativeEmittingEnabled;
        private float[] pos;
        private long ptr;
        
        public ParticleEmitter(final float n, final float n2, final float n3) {
            this.isRelativeEmittingEnabled = false;
            this.pos = new float[3];
            this.ptr = ParticleRegistry.nativeNewParticleEmitter(n, n2, n3);
        }
        
        private void refreshPos() {
            ParticleRegistry.nativeParticleEmitterGetPosition(this.ptr, this.pos);
        }
        
        public void attachTo(final Object o) {
            this.attachTo(o, 0.0f, 0.0f, 0.0f);
        }
        
        public void attachTo(Object o, final float n, final float n2, final float n3) {
            if (o instanceof Wrapper) {
                o = ((Wrapper)o).unwrap();
            }
            else {
                o = ((Number)o).longValue();
            }
            ParticleRegistry.nativeParticleEmitterAttachTo(this.ptr, (long)o, n, n2, n3);
        }
        
        public void detach() {
            ParticleRegistry.nativeParticleEmitterDetach(this.ptr);
        }
        
        public void emit(final int n, final int n2, final float n3, final float n4, final float n5) {
            if (this.isRelativeEmittingEnabled) {
                this.refreshPos();
                ParticleRegistry.nativeParticleEmit1(this.ptr, n, n2, this.pos[0] + n3, this.pos[1] + n4, this.pos[2] + n5);
                return;
            }
            ParticleRegistry.nativeParticleEmit1(this.ptr, n, n2, n3, n4, n5);
        }
        
        public void emit(final int n, final int n2, final float n3, final float n4, final float n5, final float n6, final float n7, final float n8) {
            if (this.isRelativeEmittingEnabled) {
                this.refreshPos();
                ParticleRegistry.nativeParticleEmit2(this.ptr, n, n2, this.pos[0] + n3, this.pos[1] + n4, this.pos[2] + n5, n6, n7, n8);
                return;
            }
            ParticleRegistry.nativeParticleEmit2(this.ptr, n, n2, n3, n4, n5, n6, n7, n8);
        }
        
        public void emit(final int n, final int n2, final float n3, final float n4, final float n5, final float n6, final float n7, final float n8, final float n9, final float n10, final float n11) {
            if (this.isRelativeEmittingEnabled) {
                this.refreshPos();
                ParticleRegistry.nativeParticleEmit3(this.ptr, n, n2, this.pos[0] + n3, this.pos[1] + n4, this.pos[2] + n5, n6, n7, n8, n9, n10, n11);
                return;
            }
            ParticleRegistry.nativeParticleEmit3(this.ptr, n, n2, n3, n4, n5, n6, n7, n8, n9, n10, n11);
        }
        
        public Coords getPosition() {
            final float[] array = new float[3];
            ParticleRegistry.nativeParticleEmitterGetPosition(this.ptr, array);
            return new Coords(array[0], array[1], array[2]);
        }
        
        public float[] getPositionArray() {
            final float[] array = new float[3];
            ParticleRegistry.nativeParticleEmitterGetPosition(this.ptr, array);
            return array;
        }
        
        public void move(final float n, final float n2, final float n3) {
            ParticleRegistry.nativeParticleEmitterMove(this.ptr, n, n2, n3);
        }
        
        public void moveTo(final float n, final float n2, final float n3) {
            ParticleRegistry.nativeParticleEmitterMoveTo(this.ptr, n, n2, n3);
        }
        
        public void release() {
            ParticleRegistry.nativeParticleEmitterRelease(this.ptr);
        }
        
        public void setEmitRelatively(final boolean isRelativeEmittingEnabled) {
            this.isRelativeEmittingEnabled = isRelativeEmittingEnabled;
        }
        
        public void setVelocity(final float n, final float n2, final float n3) {
            ParticleRegistry.nativeParticleEmitterSetVelocity(this.ptr, n, n2, n3);
        }
        
        public void stop() {
            this.detach();
            this.setVelocity(0.0f, 0.0f, 0.0f);
        }
    }
    
    public static class ParticleSubEmitter
    {
        private long ptr;
        
        public ParticleSubEmitter(final float n, final int n2, final int n3, final int n4) {
            this.ptr = ParticleRegistry.nativeNewParticleSubEmitter(n, n2, n3, n4);
        }
        
        public ParticleSubEmitter(final ScriptableObjectWrapper scriptableObjectWrapper) {
            this(scriptableObjectWrapper.getFloat("chance", 1.0f), scriptableObjectWrapper.getInt("count", 1), scriptableObjectWrapper.getInt("type", 0), scriptableObjectWrapper.getInt("data", 0));
            this.setKeepVelocity(scriptableObjectWrapper.getBoolean("keepVelocity"));
            this.setKeepEmitter(scriptableObjectWrapper.getBoolean("keepEmitter"));
            if (scriptableObjectWrapper.getFloat("randomize") > 0.001) {
                this.setRandomVelocity(scriptableObjectWrapper.getFloat("randomize"));
            }
        }
        
        public ParticleSubEmitter(final Scriptable scriptable) {
            this(new ScriptableObjectWrapper(scriptable));
        }
        
        public void setKeepEmitter(final boolean b) {
            ParticleRegistry.nativeParticleSubEmitterSetKeepEmitter(this.ptr, b);
        }
        
        public void setKeepVelocity(final boolean b) {
            ParticleRegistry.nativeParticleSubEmitterSetKeepVelocity(this.ptr, b);
        }
        
        public void setRandomVelocity(final float n) {
            ParticleRegistry.nativeParticleSubEmitterSetRandom(this.ptr, n);
        }
    }
    
    public static class ParticleType
    {
        private HashMap<String, ParticleAnimator> animators;
        private int id;
        private long ptr;
        private HashMap<String, ParticleSubEmitter> subEmitters;
        
        public ParticleType(final ScriptableObjectWrapper scriptableObjectWrapper) {
            this(scriptableObjectWrapper.getString("texture", "undefined"), scriptableObjectWrapper.getBoolean("isUsingBlockLight"), scriptableObjectWrapper.getUVTemplate("textureUV"), scriptableObjectWrapper.getInt("framesX", -1), scriptableObjectWrapper.getInt("framesY", -1));
            final ScriptableObjectWrapper scriptableWrapper = scriptableObjectWrapper.getScriptableWrapper("emitters");
            if (scriptableWrapper != null) {
                final ScriptableObjectWrapper scriptableWrapper2 = scriptableWrapper.getScriptableWrapper("idle");
                if (scriptableWrapper2 != null) {
                    this.setSubEmitter("idle", new ParticleSubEmitter(scriptableWrapper2));
                }
                final ScriptableObjectWrapper scriptableWrapper3 = scriptableWrapper.getScriptableWrapper("impact");
                if (scriptableWrapper3 != null) {
                    this.setSubEmitter("impact", new ParticleSubEmitter(scriptableWrapper3));
                }
                final ScriptableObjectWrapper scriptableWrapper4 = scriptableWrapper.getScriptableWrapper("death");
                if (scriptableWrapper4 != null) {
                    this.setSubEmitter("death", new ParticleSubEmitter(scriptableWrapper4));
                }
            }
            final ScriptableObjectWrapper scriptableWrapper5 = scriptableObjectWrapper.getScriptableWrapper("animators");
            if (scriptableWrapper5 != null) {
                final ScriptableObjectWrapper scriptableWrapper6 = scriptableWrapper5.getScriptableWrapper("size");
                if (scriptableWrapper6 != null) {
                    this.setAnimator("size", new ParticleAnimator(scriptableWrapper6));
                }
                final ScriptableObjectWrapper scriptableWrapper7 = scriptableWrapper5.getScriptableWrapper("alpha");
                if (scriptableWrapper7 != null) {
                    this.setAnimator("alpha", new ParticleAnimator(scriptableWrapper7));
                }
                final ScriptableObjectWrapper scriptableWrapper8 = scriptableWrapper5.getScriptableWrapper("icon");
                if (scriptableWrapper8 != null) {
                    this.setAnimator("icon", new ParticleAnimator(scriptableWrapper8));
                }
                final ScriptableObjectWrapper scriptableWrapper9 = scriptableWrapper5.getScriptableWrapper("color");
                if (scriptableWrapper9 != null) {
                    this.setAnimator("color", new ParticleAnimator(scriptableWrapper9));
                }
            }
            final ScriptableObjectWrapper scriptableWrapper10 = scriptableObjectWrapper.getScriptableWrapper("friction");
            if (scriptableWrapper10 != null) {
                this.setFriction(scriptableWrapper10.getFloat("air", 1.0f), scriptableWrapper10.getFloat("block", 1.0f));
            }
            if (scriptableObjectWrapper.has("color")) {
                final float[] colorTemplate = scriptableObjectWrapper.getColorTemplate("color", 1.0f);
                if (scriptableObjectWrapper.has("color2")) {
                    final float[] colorTemplate2 = scriptableObjectWrapper.getColorTemplate("color2", 1.0f);
                    this.setColor(colorTemplate[0], colorTemplate[1], colorTemplate[2], colorTemplate[3], colorTemplate2[0], colorTemplate2[1], colorTemplate2[2], colorTemplate2[3]);
                }
                else {
                    this.setColor(colorTemplate[0], colorTemplate[1], colorTemplate[2], colorTemplate[3]);
                }
            }
            if (scriptableObjectWrapper.has("lifetime")) {
                final float[] minMaxTemplate = scriptableObjectWrapper.getMinMaxTemplate("lifetime", 100.0f);
                this.setLifetime((int)minMaxTemplate[0], (int)minMaxTemplate[1]);
            }
            if (scriptableObjectWrapper.has("size")) {
                final float[] minMaxTemplate2 = scriptableObjectWrapper.getMinMaxTemplate("size", 1.0f);
                this.setSize(minMaxTemplate2[0], minMaxTemplate2[1]);
            }
            if (scriptableObjectWrapper.has("velocity")) {
                final float[] vec3Template = scriptableObjectWrapper.getVec3Template("velocity", 0.0f);
                this.setDefaultVelocity(vec3Template[0], vec3Template[1], vec3Template[2]);
            }
            if (scriptableObjectWrapper.has("acceleration")) {
                final float[] vec3Template2 = scriptableObjectWrapper.getVec3Template("acceleration", 0.0f);
                this.setDefaultAcceleration(vec3Template2[0], vec3Template2[1], vec3Template2[2]);
            }
            this.setCollisionParams(scriptableObjectWrapper.getBoolean("collision"), scriptableObjectWrapper.getBoolean("keepVelocityAfterImpact"), scriptableObjectWrapper.getInt("addLifetimeAfterImpact"));
            this.setRenderType(scriptableObjectWrapper.getInt("render", 1));
            this.setRebuildDelay(scriptableObjectWrapper.getInt("rebuildDelay", 10));
        }
        
        public ParticleType(final String s, final float n, final float n2, final float n3, final float n4, final int n5, final int n6, final boolean b) {
            this.subEmitters = new HashMap<String, ParticleSubEmitter>();
            this.animators = new HashMap<String, ParticleAnimator>();
            this.initPtr(ParticleRegistry.nativeRegisterNewParticleType(s, n, n2, n3, n4, n5, n6, b));
        }
        
        public ParticleType(final String s, final boolean b, float[] array, int n, int n2) {
            this.subEmitters = new HashMap<String, ParticleSubEmitter>();
            this.animators = new HashMap<String, ParticleAnimator>();
            String string = null;
            final StringBuilder sb = new StringBuilder();
            sb.append("resource_packs/vanilla/particle-atlas/");
            sb.append(s);
            sb.append(".png");
            if (FileTools.assetExists(sb.toString())) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("particle-atlas/");
                sb2.append(s);
                string = sb2.toString();
            }
            else {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("resource_packs/vanilla/");
                sb3.append(s);
                sb3.append(".png");
                if (FileTools.assetExists(sb3.toString())) {
                    string = s;
                }
            }
            if (array == null) {
                array = new float[] { 0.0f, 0.0f, 1.0f, 1.0f };
            }
            if (string == null) {
                final StringBuilder sb4 = new StringBuilder();
                sb4.append("invalid particle location name: ");
                sb4.append(s);
                sb4.append(", it does not exist and will be replaced with default.");
                ICLog.i("ERROR", sb4.toString());
                this.initPtr(ParticleRegistry.nativeRegisterNewParticleType("textures/logo.png", array[0], array[1], array[2], array[3], 1, 1, b));
                return;
            }
            if (n <= 0 || n2 <= 0) {
                final StringBuilder sb5 = new StringBuilder();
                sb5.append("resource_packs/vanilla/");
                sb5.append(string);
                sb5.append(".png");
                Logger.debug(sb5.toString());
                final StringBuilder sb6 = new StringBuilder();
                sb6.append("resource_packs/vanilla/");
                sb6.append(string);
                sb6.append(".png");
                final Bitmap assetAsBitmap = FileTools.getAssetAsBitmap(sb6.toString());
                if (assetAsBitmap.getHeight() > assetAsBitmap.getWidth()) {
                    n2 = assetAsBitmap.getHeight() / assetAsBitmap.getWidth();
                    n = 1;
                }
                else {
                    n = 1;
                    n2 = 1;
                }
            }
            this.initPtr(ParticleRegistry.nativeRegisterNewParticleType(string, array[0], array[1], array[2], array[3], n, n2, b));
        }
        
        public ParticleType(final Scriptable scriptable) {
            this(new ScriptableObjectWrapper(scriptable));
        }
        
        private long getAnimatorPtr(final String s) {
            final ParticleAnimator particleAnimator = this.animators.get(s);
            if (particleAnimator != null) {
                return particleAnimator.ptr;
            }
            return 0L;
        }
        
        private long getSubEmitterPtr(final String s) {
            final ParticleSubEmitter particleSubEmitter = this.subEmitters.get(s);
            if (particleSubEmitter != null) {
                return particleSubEmitter.ptr;
            }
            return 0L;
        }
        
        private void initPtr(final long ptr) {
            this.ptr = ptr;
            this.id = ParticleRegistry.nativeParticleTypeGetID(ptr);
            ParticleRegistry.registeredParticleTypes.put(this.id, this);
        }
        
        public int getId() {
            return this.id;
        }
        
        public void setAnimator(final String s, final ParticleAnimator particleAnimator) {
            this.animators.put(s, particleAnimator);
            ParticleRegistry.nativeParticleTypeSetAnimatorsNew(this.ptr, this.getAnimatorPtr("size"), this.getAnimatorPtr("alpha"), this.getAnimatorPtr("icon"), this.getAnimatorPtr("color"));
        }
        
        public void setCollisionParams(final boolean b, final boolean b2, final int n) {
            ParticleRegistry.nativeParticleTypeSetCollisionParams(this.ptr, b, b2, n);
        }
        
        public void setColor(final float n, final float n2, final float n3, final float n4) {
            ParticleRegistry.nativeParticleTypeSetColor(this.ptr, n, n2, n3, n4);
        }
        
        public void setColor(final float n, final float n2, final float n3, final float n4, final float n5, final float n6, final float n7, final float n8) {
            ParticleRegistry.nativeParticleTypeSetColorNew(this.ptr, n, n2, n3, n4, n5, n6, n7, n8);
        }
        
        public void setDefaultAcceleration(final float n, final float n2, final float n3) {
            ParticleRegistry.nativeParticleTypeSetDefaultAcceleration(this.ptr, n, n2, n3);
        }
        
        public void setDefaultVelocity(final float n, final float n2, final float n3) {
            ParticleRegistry.nativeParticleTypeSetDefaultVelocity(this.ptr, n, n2, n3);
        }
        
        public void setFriction(final float n, final float n2) {
            ParticleRegistry.nativeParticleTypeSetFriction(this.ptr, n, n2);
        }
        
        public void setLifetime(final int n, final int n2) {
            ParticleRegistry.nativeParticleTypeSetLifetime(this.ptr, n, n2);
        }
        
        public void setRebuildDelay(final int n) {
            ParticleRegistry.nativeParticleTypeSetRebuildDelay(this.ptr, n);
        }
        
        public void setRenderType(final int n) {
            ParticleRegistry.nativeParticleTypeSetRenderType(this.ptr, n);
        }
        
        public void setSize(final float n, final float n2) {
            ParticleRegistry.nativeParticleTypeSetSize(this.ptr, n, n2);
        }
        
        public void setSubEmitter(final String s, final ParticleSubEmitter particleSubEmitter) {
            this.subEmitters.put(s, particleSubEmitter);
            ParticleRegistry.nativeParticleTypeSetSubEmitters(this.ptr, this.getSubEmitterPtr("idle"), this.getSubEmitterPtr("impact"), this.getSubEmitterPtr("death"));
        }
    }
}
