package com.zhekasmirnov.innercore.api.mod.adaptedscript;

import org.mozilla.javascript.annotations.*;
import java.lang.reflect.*;
import com.zhekasmirnov.apparatus.api.player.armor.*;
import com.zhekasmirnov.apparatus.adapter.innercore.game.entity.*;
import com.zhekasmirnov.apparatus.adapter.innercore.game.block.*;
import com.zhekasmirnov.innercore.api.biomes.*;
import com.zhekasmirnov.innercore.api.dimensions.*;
import org.mozilla.javascript.*;
import com.zhekasmirnov.innercore.api.entities.*;
import com.zhekasmirnov.horizon.util.*;
import com.zhekasmirnov.innercore.api.unlimited.*;
import com.zhekasmirnov.apparatus.api.container.*;
import com.zhekasmirnov.innercore.api.mod.ui.container.*;
import android.util.*;
import com.zhekasmirnov.innercore.api.commontypes.*;
import com.zhekasmirnov.innercore.mod.build.*;
import android.graphics.*;
import com.zhekasmirnov.innercore.mod.executable.*;
import android.app.*;
import com.zhekasmirnov.apparatus.minecraft.version.*;
import com.zhekasmirnov.apparatus.multiplayer.*;
import com.zhekasmirnov.innercore.api.log.*;
import com.zhekasmirnov.innercore.ui.*;
import com.zhekasmirnov.innercore.api.nbt.*;
import com.zhekasmirnov.apparatus.multiplayer.util.list.*;
import com.zhekasmirnov.innercore.api.particles.*;
import com.zhekasmirnov.apparatus.mcpe.*;
import com.zhekasmirnov.innercore.api.mod.recipes.*;
import com.zhekasmirnov.innercore.api.mod.ui.icon.*;
import com.zhekasmirnov.innercore.mod.resource.*;
import com.zhekasmirnov.innercore.utils.*;
import java.io.*;
import com.zhekasmirnov.innercore.api.runtime.saver.serializer.*;
import org.json.*;
import com.zhekasmirnov.innercore.api.runtime.saver.*;
import com.zhekasmirnov.innercore.api.runtime.saver.world.*;
import com.zhekasmirnov.innercore.api.annotations.*;
import com.zhekasmirnov.apparatus.multiplayer.util.entity.*;
import com.zhekasmirnov.innercore.api.mod.*;
import java.util.*;
import com.zhekasmirnov.innercore.api.runtime.other.*;
import com.zhekasmirnov.innercore.api.mod.util.*;
import com.zhekasmirnov.innercore.api.mod.ui.types.*;
import com.zhekasmirnov.innercore.api.mod.ui.*;
import com.zhekasmirnov.innercore.api.mod.ui.window.*;
import com.zhekasmirnov.innercore.api.runtime.*;
import com.zhekasmirnov.innercore.api.*;

public class AdaptedScriptAPI extends API
{
    @JSStaticFunction
    public static void clientMessage(final String s) {
        NativeAPI.clientMessage(s);
    }
    
    @JSStaticFunction
    public static void explode(final double n, final double n2, final double n3, final double n4, final boolean b) {
        NativeAPI.explode((float)n, (float)n2, (float)n3, (float)n4, b);
    }
    
    @JSStaticFunction
    public static long getPlayerEnt() {
        return NativeAPI.getPlayer();
    }
    
    @JSStaticFunction
    public static int getTile(final int n, final int n2, final int n3) {
        return NativeAPI.getTile(n, n2, n3);
    }
    
    @JSStaticFunction
    public static int getTileAndData(final int n, final int n2, final int n3) {
        return NativeAPI.getTileAndData(n, n2, n3);
    }
    
    @JSStaticFunction
    public static void log(final String s) {
        ICLog.d("MOD", s);
    }
    
    @JSStaticFunction
    public static void logDeprecation(final String s) {
        final StringBuilder sb = new StringBuilder();
        sb.append("using deprecated or unimplemented method ");
        sb.append(s);
        sb.append("()");
        ICLog.d("WARNING", sb.toString());
    }
    
    @JSStaticFunction
    public static void preventDefault() {
        NativeAPI.preventDefault();
    }
    
    @JSStaticFunction
    public static void print(final String s) {
        ICLog.d("MOD-PRINT", s);
        PrintStacking.print(s);
    }
    
    @JSStaticFunction
    public static Function requireMethodFromNativeAPI(String string, final String s, final boolean b) {
        final String s2 = string = string;
        if (!s2.startsWith("com.zhekasmirnov.innercore.")) {
            final StringBuilder sb = new StringBuilder();
            sb.append("com.zhekasmirnov.innercore.");
            sb.append(s2);
            string = sb.toString();
        }
        try {
            final Method[] methods = Class.forName(string).getMethods();
            final int length = methods.length;
            Method method = null;
            for (int i = 0; i < length; ++i) {
                final Method method2 = methods[i];
                if (method2.getName().equals(s)) {
                    method = method2;
                }
            }
            if (method == null) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("method cannot be found class=");
                sb2.append(string);
                sb2.append(" method=");
                sb2.append(s);
                throw new RuntimeException(sb2.toString());
            }
            final Class<?>[] parameterTypes = method.getParameterTypes();
            final Object[] array = new Object[parameterTypes.length];
            final boolean[] array2 = new boolean[parameterTypes.length];
            final int[] array3 = new int[parameterTypes.length];
            boolean b2 = true;
            boolean b3;
            for (int j = 0; j < parameterTypes.length; ++j, b2 = b3) {
                if (!(array2[j] = (Integer.TYPE.isAssignableFrom(parameterTypes[j]) || Double.TYPE.isAssignableFrom(parameterTypes[j]) || Float.TYPE.isAssignableFrom(parameterTypes[j])))) {
                    b3 = false;
                }
                else if (Integer.TYPE.isAssignableFrom(parameterTypes[j])) {
                    array3[j] = 0;
                    b3 = b2;
                }
                else if (Float.TYPE.isAssignableFrom(parameterTypes[j])) {
                    array3[j] = 1;
                    b3 = b2;
                }
                else {
                    b3 = b2;
                    if (Double.TYPE.isAssignableFrom(parameterTypes[j])) {
                        array3[j] = 1;
                        b3 = b2;
                    }
                }
            }
            return (Function)new ScriptableFunctionImpl() {
                public Object call(final Context context, final Scriptable scriptable, final Scriptable scriptable2, Object[] val$javaParams) {
                    if (!b) {
                        final boolean val$onlyNumbers = b2;
                        int i = 0;
                        if (val$onlyNumbers) {
                            for (int j = 0; j < parameterTypes.length; ++j) {
                                Number n = 0;
                                if (j < val$javaParams.length) {
                                    final Object o = val$javaParams[j];
                                    if (o instanceof Number) {
                                        n = (Number)o;
                                    }
                                    else {
                                        n = 0;
                                    }
                                }
                                switch (array3[j]) {
                                    case 2: {
                                        array[j] = n.doubleValue();
                                        break;
                                    }
                                    case 1: {
                                        array[j] = n.floatValue();
                                        break;
                                    }
                                    case 0: {
                                        array[j] = n.intValue();
                                        break;
                                    }
                                }
                            }
                        }
                        else {
                            while (i < parameterTypes.length) {
                                Object o2;
                                if (i >= val$javaParams.length) {
                                    o2 = null;
                                }
                                else {
                                    o2 = val$javaParams[i];
                                }
                                Object value = o2;
                                if (o2 == null) {
                                    value = o2;
                                    if (array2[i]) {
                                        value = 0.0;
                                    }
                                }
                                array[i] = Context.jsToJava(value, parameterTypes[i]);
                                ++i;
                            }
                        }
                        val$javaParams = array;
                    }
                    try {
                        return Context.javaToJS(method.invoke(null, (Object[])val$javaParams), scriptable);
                    }
                    catch (Exception ex) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("failed to call required java method class=");
                        sb.append(string);
                        sb.append(" method=");
                        sb.append(s);
                        ICLog.i("ERROR", sb.toString());
                        throw ex;
                    }
                    catch (InvocationTargetException ex2) {
                        throw new RuntimeException(ex2.toString());
                    }
                    catch (IllegalAccessException ex3) {
                        throw new RuntimeException(ex3.toString());
                    }
                }
            };
        }
        catch (ClassNotFoundException ex) {
            throw new RuntimeException(ex.toString());
        }
    }
    
    @JSStaticFunction
    public static void runOnClientThread(final Object o) {
        MainThreadQueue.localThread.enqueue((Runnable)Context.jsToJava(o, (Class)Runnable.class));
    }
    
    @JSStaticFunction
    public static void runOnMainThread(final Object o) {
        MainThreadQueue.serverThread.enqueue((Runnable)Context.jsToJava(o, (Class)Runnable.class));
    }
    
    @JSStaticFunction
    public static void setTile(final int n, final int n2, final int n3, final int n4, final int n5) {
        NativeAPI.setTile(n, n2, n3, n4, n5);
    }
    
    @JSStaticFunction
    public static void tipMessage(final String s) {
        NativeAPI.tipMessage(s);
    }
    
    @Override
    public int getLevel() {
        return 1;
    }
    
    @Override
    public String getName() {
        return "AdaptedScript";
    }
    
    @Override
    public void onCallback(final String s, final Object[] array) {
    }
    
    @Override
    public void onLoaded() {
    }
    
    @Override
    public void onModLoaded(final Mod mod) {
    }
    
    @Override
    public void prepareExecutable(final Executable executable) {
        super.prepareExecutable(executable);
    }
    
    @Override
    public void setupCallbacks(final Executable executable) {
    }
    
    public static class ActorRenderer extends NativeActorRenderer
    {
        public ActorRenderer() {
        }
        
        public ActorRenderer(final String s) {
            super(s);
        }
    }
    
    public static class Armor extends ActorArmorHandler
    {
        private Armor(final EntityActor entityActor) {
            super(entityActor);
        }
        
        @JSStaticFunction
        public static void preventDamaging(final int n) {
            ArmorRegistry.preventArmorDamaging(n);
        }
        
        @JSStaticFunction
        public static void registerCallbacks(final int n, final ScriptableObject scriptableObject) {
            ArmorRegistry.registerArmor(n, scriptableObject);
        }
    }
    
    public static class AttachableRender extends NativeAttachable
    {
        public AttachableRender(final long n) {
            super(n);
        }
        
        @JSStaticFunction
        public static void attachRendererToItem(final int n, final Object o, final String s, String s2) {
            final NativeActorRenderer nativeActorRenderer = (NativeActorRenderer)Context.jsToJava(o, (Class)NativeActorRenderer.class);
            String s3;
            if (s != null) {
                s3 = s;
            }
            else {
                s3 = "";
            }
            if (s2 == null) {
                s2 = "";
            }
            NativeAttachable.attachRendererToItem(n, nativeActorRenderer, s3, s2);
        }
        
        @JSStaticFunction
        public static void detachRendererFromItem(final int n) {
            NativeAttachable.detachRendererFromItem(n);
        }
    }
    
    @APIStaticModule
    public static class Block
    {
        private static int anonymousSpecialTypeIndex;
        
        static {
            Block.anonymousSpecialTypeIndex = 0;
        }
        
        @JSStaticFunction
        public static void createBlock(final int n, final String s, final ScriptableObject scriptableObject, final Object o) {
            SpecialType specialType = SpecialType.DEFAULT;
            if (o instanceof SpecialType) {
                specialType = (SpecialType)o;
            }
            else if (o instanceof String) {
                specialType = SpecialType.getSpecialType((String)o);
            }
            else if (o instanceof ScriptableObject) {
                final StringBuilder sb = new StringBuilder();
                sb.append("anonymous_type_");
                final int anonymousSpecialTypeIndex = Block.anonymousSpecialTypeIndex;
                Block.anonymousSpecialTypeIndex = anonymousSpecialTypeIndex + 1;
                sb.append(anonymousSpecialTypeIndex);
                specialType = SpecialType.createSpecialType(sb.toString());
                specialType.setupProperties((ScriptableObject)o);
            }
            BlockRegistry.createBlock(n, s, scriptableObject, specialType);
        }
        
        @JSStaticFunction
        public static String createSpecialType(final String s, final ScriptableObject scriptableObject) {
            final SpecialType specialType = SpecialType.createSpecialType(s);
            specialType.setupProperties(scriptableObject);
            return specialType.name;
        }
        
        @JSStaticFunction
        public static ScriptableObject getBlockAtlasTextureCoords(final String s, final int n) {
            final float[] array = new float[4];
            NativeAPI.getAtlasTextureCoords(s, n, array);
            final ScriptableObject empty = ScriptableObjectHelper.createEmpty();
            empty.put("u1", (Scriptable)empty, (Object)array[0]);
            empty.put("v1", (Scriptable)empty, (Object)array[1]);
            empty.put("u2", (Scriptable)empty, (Object)array[2]);
            empty.put("v2", (Scriptable)empty, (Object)array[3]);
            return empty;
        }
        
        @JSStaticFunction
        public static double getDestroyTime(final int n) {
            return NativeBlock.getDestroyTimeForId(n);
        }
        
        @JSStaticFunction
        public static double getExplosionResistance(final int n) {
            return NativeBlock.getExplosionResistance(n);
        }
        
        @JSStaticFunction
        public static double getFriction(final int n) {
            return NativeBlock.getFriction(n);
        }
        
        @JSStaticFunction
        public static int getLightLevel(final int n) {
            return NativeBlock.getLightLevel(n);
        }
        
        @JSStaticFunction
        public static int getLightOpacity(final int n) {
            return NativeBlock.getLightOpacity(n);
        }
        
        @JSStaticFunction
        public static int getMapColor(final int n) {
            return NativeBlock.getMapColor(n);
        }
        
        @JSStaticFunction
        public static int getRenderLayer(final int n) {
            return NativeBlock.getRenderLayer(n);
        }
        
        @JSStaticFunction
        public static int getRenderType(final int n) {
            return NativeBlock.getRenderType(n);
        }
        
        @JSStaticFunction
        public static double getTranslucency(final int n) {
            return NativeBlock.getTranslucency(n);
        }
        
        @JSStaticFunction
        public static boolean isSolid(final int n) {
            return NativeBlock.isSolid(n);
        }
        
        @JSStaticFunction
        public static void setAnimateTickCallback(final int n, final Function function) {
            NativeBlock.setAnimateTickCallback(n, function);
        }
        
        @JSStaticFunction
        public static void setBlockChangeCallbackEnabled(final int n, final boolean b) {
            NativeAPI.setBlockChangeCallbackEnabled(n, b);
        }
        
        @JSStaticFunction
        public static void setDestroyTime(final int n, final double n2) {
            NativeBlock.setDestroyTimeForId(n, (float)n2);
        }
        
        @JSStaticFunction
        public static void setEntityInsideCallbackEnabled(final int n, final boolean b) {
            NativeBlock.setReceivingEntityInsideEvent(n, b);
        }
        
        @JSStaticFunction
        public static void setEntityStepOnCallbackEnabled(final int n, final boolean b) {
            NativeBlock.setReceivingEntityStepOnEvent(n, b);
        }
        
        @JSStaticFunction
        public static void setNeighbourChangeCallbackEnabled(final int n, final boolean b) {
            NativeBlock.setReceivingNeighbourChangeEvent(n, b);
        }
        
        @JSStaticFunction
        public static void setRandomTickCallback(final int n, final Function function) {
            NativeBlock.setRandomTickCallback(n, function);
        }
        
        @JSStaticFunction
        public static void setRedstoneConnector(final int n, final Object o, final boolean b) {
            NativeIdMapping.iterateMetadata(n, o, (NativeIdMapping.IIdIterator)new NativeIdMapping.IIdIterator() {
                @Override
                public void onIdDataIterated(final int n, final int n2) {
                    NativeBlock.setRedstoneConnectorNative(n, n2, b);
                }
            });
        }
        
        @JSStaticFunction
        public static void setRedstoneEmitter(final int n, final Object o, final boolean b) {
            NativeIdMapping.iterateMetadata(n, o, (NativeIdMapping.IIdIterator)new NativeIdMapping.IIdIterator() {
                @Override
                public void onIdDataIterated(final int n, final int n2) {
                    NativeBlock.setRedstoneEmitterNative(n, n2, b);
                }
            });
        }
        
        @JSStaticFunction
        public static void setRedstoneTile(final int n, final Object o, final boolean b) {
            NativeIdMapping.iterateMetadata(n, o, (NativeIdMapping.IIdIterator)new NativeIdMapping.IIdIterator() {
                @Override
                public void onIdDataIterated(final int n, final int n2) {
                    NativeBlock.setRedstoneTileNative(n, n2, b);
                }
            });
        }
        
        @JSStaticFunction
        public static void setShape(final int n, final double n2, final double n3, final double n4, final double n5, final double n6, final double n7, final Object o) {
            if (o instanceof Number) {
                BlockRegistry.setShape(n, ((Number)o).intValue(), (float)n2, (float)n3, (float)n4, (float)n5, (float)n6, (float)n7);
                return;
            }
            BlockRegistry.setShape(n, -1, (float)n2, (float)n3, (float)n4, (float)n5, (float)n6, (float)n7);
        }
        
        @JSStaticFunction
        public static void setTempDestroyTime(final int n, final double n2) {
            NativeBlock.setTempDestroyTimeForId(n, (float)n2);
        }
    }
    
    @APIStaticModule
    public static class BlockRenderer extends NativeBlockRenderer
    {
        @JSStaticFunction
        public static void addRenderCallback(final int n, final Function function) {
            NativeBlockRenderer._addRenderCallback(n, function);
        }
        
        @JSStaticFunction
        public static NativeBlockModel createModel() {
            return new NativeBlockModel();
        }
        
        @JSStaticFunction
        public static NativeBlockModel createTexturedBlock(final ScriptableObject scriptableObject) {
            return NativeBlockModel.createTexturedBlock(scriptableObject);
        }
        
        @JSStaticFunction
        public static NativeBlockModel createTexturedBox(final double n, final double n2, final double n3, final double n4, final double n5, final double n6, final ScriptableObject scriptableObject) {
            return NativeBlockModel.createTexturedBox((float)n, (float)n2, (float)n3, (float)n4, (float)n5, (float)n6, scriptableObject);
        }
        
        @JSStaticFunction
        public static void forceRenderRebuild(final int n, final int n2, final int n3, final int n4) {
            NativeAPI.forceRenderRefresh(n, n2, n3, n4);
        }
        
        public static class Model extends NativeBlockModel
        {
            public Model() {
            }
            
            public Model(final float n, final float n2, final float n3, final float n4, final float n5, final float n6, final int n7, final int n8) {
                this.addBox(n, n2, n3, n4, n5, n6, n7, n8);
            }
            
            public Model(final float n, final float n2, final float n3, final float n4, final float n5, final float n6, final String s, final int n7) {
                this.addBox(n, n2, n3, n4, n5, n6, s, n7);
            }
            
            public Model(final float n, final float n2, final float n3, final float n4, final float n5, final float n6, final ScriptableObject scriptableObject) {
                this.addBox(n, n2, n3, n4, n5, n6, scriptableObject);
            }
            
            public Model(final int n, final int n2) {
                this(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, n, n2);
            }
            
            public Model(final NativeRenderMesh nativeRenderMesh) {
                super(nativeRenderMesh);
            }
            
            public Model(final String s, final int n) {
                this(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, s, n);
            }
            
            public Model(final ScriptableObject scriptableObject) {
                this(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f, scriptableObject);
            }
        }
    }
    
    public static class BlockSource extends NativeBlockSource
    {
        public BlockSource(final int n) {
            super(n);
        }
        
        public BlockSource(final int n, final boolean b, final boolean b2) {
            super(n, b, b2);
        }
        
        @Override
        protected void finalize() throws Throwable {
            super.finalize();
        }
    }
    
    public static class BlockState extends com.zhekasmirnov.apparatus.adapter.innercore.game.block.BlockState
    {
        public BlockState(final int n, final int n2) {
            super(n, n2);
        }
        
        public BlockState(final int n, final Object o) {
            super(n, (Scriptable)o);
        }
    }
    
    @APIStaticModule
    public static class Callback
    {
        @JSStaticFunction
        public static void addCallback(final String s, final Function function, final int n) {
            com.zhekasmirnov.innercore.api.runtime.Callback.addCallback(s, function, n);
        }
        
        @JSStaticFunction
        public static void invokeCallback(final String s, final Object o, final Object o2, final Object o3, final Object o4, final Object o5, final Object o6, final Object o7, final Object o8, final Object o9, final Object o10) {
            com.zhekasmirnov.innercore.api.runtime.Callback.invokeCallback(s, o, o2, o3, o4, o5, o6, o7, o8, o9, o10);
        }
    }
    
    @APIStaticModule
    public static class Commands
    {
        @JSStaticFunction
        public static String exec(final String s, Object o, final Object o2) {
            o = null;
            try {
                o = Context.jsToJava(o2, (Class)NativeBlockSource.class);
            }
            catch (Exception ex) {}
            long pointer;
            if (o != null) {
                pointer = ((NativeBlockSource)o).getPointer();
            }
            else {
                pointer = 0L;
            }
            return NativeAPI.executeCommand(s, 0, 0, 0, pointer);
        }
        
        @JSStaticFunction
        public static String execAt(final String s, final int n, final int n2, final int n3, final Object o) {
            final NativeBlockSource nativeBlockSource = null;
            NativeBlockSource nativeBlockSource2;
            try {
                nativeBlockSource2 = (NativeBlockSource)Context.jsToJava(o, (Class)NativeBlockSource.class);
            }
            catch (Exception ex) {
                nativeBlockSource2 = nativeBlockSource;
            }
            long pointer;
            if (nativeBlockSource2 != null) {
                pointer = nativeBlockSource2.getPointer();
            }
            else {
                pointer = 0L;
            }
            return NativeAPI.executeCommand(s, n, n2, n3, pointer);
        }
    }
    
    public static class Config extends com.zhekasmirnov.innercore.mod.build.Config
    {
        public Config(final File file) {
            super(file);
        }
        
        public Config(final CharSequence charSequence) {
            super(new File(charSequence.toString()));
        }
    }
    
    public static class CustomBiome extends com.zhekasmirnov.innercore.api.biomes.CustomBiome
    {
        public CustomBiome(final String s) {
            super(s);
        }
    }
    
    @APIStaticModule
    public static class Dimensions
    {
        @JSStaticFunction
        public static com.zhekasmirnov.innercore.api.dimensions.CustomDimension getDimensionById(final int n) {
            return com.zhekasmirnov.innercore.api.dimensions.CustomDimension.getDimensionById(n);
        }
        
        @JSStaticFunction
        public static com.zhekasmirnov.innercore.api.dimensions.CustomDimension getDimensionByName(final String s) {
            return com.zhekasmirnov.innercore.api.dimensions.CustomDimension.getDimensionByName(s);
        }
        
        @JSStaticFunction
        public static boolean isLimboId(final int n) {
            return com.zhekasmirnov.innercore.api.dimensions.CustomDimension.isLimboId(n);
        }
        
        @JSStaticFunction
        public static void overrideGeneratorForVanillaDimension(final int n, final Object o) {
            Object unwrap = o;
            if (o instanceof Wrapper) {
                unwrap = ((Wrapper)o).unwrap();
            }
            com.zhekasmirnov.innercore.api.dimensions.CustomDimension.setCustomGeneratorForVanillaDimension(n, (CustomDimensionGenerator)unwrap);
        }
        
        @JSStaticFunction
        public static void transfer(final Object o, final int n) {
            NativeAPI.transferToDimension(Entity.unwrapEntity(o), n);
        }
        
        public static class CustomDimension extends com.zhekasmirnov.innercore.api.dimensions.CustomDimension
        {
            public CustomDimension(final String s, final int n) {
                super(s, n);
            }
        }
        
        public static class CustomGenerator extends CustomDimensionGenerator
        {
            public CustomGenerator(final int n) {
                super(n);
            }
            
            public CustomGenerator(final String s) {
                super(s);
            }
        }
        
        public static class MonoBiomeTerrainGenerator extends com.zhekasmirnov.innercore.api.dimensions.MonoBiomeTerrainGenerator
        {
        }
        
        public static class NoiseConversion extends com.zhekasmirnov.innercore.api.dimensions.NoiseConversion
        {
        }
        
        public static class NoiseGenerator extends com.zhekasmirnov.innercore.api.dimensions.NoiseGenerator
        {
        }
        
        public static class NoiseLayer extends com.zhekasmirnov.innercore.api.dimensions.NoiseLayer
        {
        }
        
        public static class NoiseOctave extends com.zhekasmirnov.innercore.api.dimensions.NoiseOctave
        {
            public NoiseOctave() {
            }
            
            public NoiseOctave(final int n) {
                super(n);
            }
            
            public NoiseOctave(final String s) {
                super(s);
            }
        }
    }
    
    @APIStaticModule
    public static class Entity
    {
        @JSStaticFunction
        public static void addEffect(final Object o, final int n, final int n2, final int n3, final boolean b, final boolean b2, final boolean b3) {
            NativeAPI.addEffect(unwrapEntity(o), n, n2, n3, b, b2, b3);
        }
        
        @JSStaticFunction
        public static void dealDamage(final Object o, final int n, final int n2, final ScriptableObject scriptableObject) {
            ScriptableObject empty = scriptableObject;
            if (scriptableObject == null) {
                empty = ScriptableObjectHelper.createEmpty();
            }
            final Object property = ScriptableObjectHelper.getProperty(empty, "attacker", null);
            final boolean booleanProperty = ScriptableObjectHelper.getBooleanProperty(empty, "bool1", false);
            final boolean booleanProperty2 = ScriptableObjectHelper.getBooleanProperty(empty, "bool2", false);
            final long unwrapEntity = unwrapEntity(o);
            long unwrapEntity2;
            if (property == null) {
                unwrapEntity2 = -1L;
            }
            else {
                unwrapEntity2 = unwrapEntity(property);
            }
            NativeAPI.dealDamage(unwrapEntity, n, n2, unwrapEntity2, booleanProperty, booleanProperty2);
        }
        
        @JSStaticFunction
        public static NativeArray getAll() {
            return new NativeArray(NativeCallback.getAllEntities().toArray());
        }
        
        @JSStaticFunction
        public static ArrayList<Long> getAllArrayList() {
            return NativeCallback.getAllEntities();
        }
        
        @JSStaticFunction
        public static int getAnimalAge(final Object o) {
            AdaptedScriptAPI.logDeprecation("Entity.getAnimalAge");
            return NativeAPI.getAge(unwrapEntity(o));
        }
        
        @JSStaticFunction
        public static ItemInstance getArmor(final Object o, final int n) {
            return new ItemInstance(new NativeItemInstance(NativeAPI.getEntityArmor(unwrapEntity(o), n)));
        }
        
        @JSStaticFunction
        public static ItemInstance getArmorSlot(final Object o, final int n) {
            return new ItemInstance(new NativeItemInstance(NativeAPI.getEntityArmor(unwrapEntity(o), n)));
        }
        
        @JSStaticFunction
        public static NativeAttributeInstance getAttribute(final Object o, final String s) {
            return new NativeAttributeInstance(unwrapEntity(o), s);
        }
        
        @JSStaticFunction
        public static ItemInstance getCarriedItem(final Object o) {
            return new ItemInstance(new NativeItemInstance(NativeAPI.getEntityCarriedItem(unwrapEntity(o))));
        }
        
        @JSStaticFunction
        public static NativeCompoundTag getCompoundTag(final Object o) {
            final long entityCompoundTag = NativeAPI.getEntityCompoundTag(unwrapEntity(o));
            if (entityCompoundTag != 0L) {
                return new NativeCompoundTag(entityCompoundTag);
            }
            return null;
        }
        
        @JSStaticFunction
        public static int getDimension(final Object o) {
            return NativeAPI.getEntityDimension(unwrapEntity(o));
        }
        
        @JSStaticFunction
        public static ItemInstance getDroppedItem(final Object o) {
            return new ItemInstance(new NativeItemInstance(NativeAPI.getItemFromDrop(unwrapEntity(o))));
        }
        
        @JSStaticFunction
        public static NativeArray getEntitiesInsideBox(final double n, final double n2, final double n3, final double n4, final double n5, final double n6, int n7, final boolean b) {
            final long[] fetchEntitiesInAABB = NativeAPI.fetchEntitiesInAABB((float)n, (float)n2, (float)n3, (float)n4, (float)n5, (float)n6, n7, b);
            final Object[] array = new Object[fetchEntitiesInAABB.length];
            n7 = 0;
            for (int length = fetchEntitiesInAABB.length, i = 0; i < length; ++i, ++n7) {
                array[n7] = fetchEntitiesInAABB[i];
            }
            return ScriptableObjectHelper.createArray(array);
        }
        
        @DeprecatedAPIMethod
        @JSStaticFunction
        public static int getEntityTypeId(final Object o) {
            return NativeAPI.getEntityType(unwrapEntity(o));
        }
        
        @JSStaticFunction
        public static int getFireTicks(final Object o) {
            return NativeAPI.getFireTicks(unwrapEntity(o));
        }
        
        @JSStaticFunction
        public static int getHealth(final Object o) {
            return NativeAPI.getHealth(unwrapEntity(o));
        }
        
        @JSStaticFunction
        public static int getMaxHealth(final Object o) {
            return NativeAPI.getMaxHealth(unwrapEntity(o));
        }
        
        @Placeholder
        @JSStaticFunction
        public static String getMobSkin(final Object o) {
            AdaptedScriptAPI.logDeprecation("Entity.getMobSkin");
            return "missing_texture.png";
        }
        
        @JSStaticFunction
        public static String getNameTag(final Object o) {
            return NativeAPI.getNameTag(unwrapEntity(o));
        }
        
        @JSStaticFunction
        public static ItemInstance getOffhandItem(final Object o) {
            return new ItemInstance(new NativeItemInstance(NativeAPI.getEntityOffhandItem(unwrapEntity(o))));
        }
        
        @JSStaticFunction
        public static NativePathNavigation getPathNavigation(final Object o) {
            return NativePathNavigation.getNavigation(unwrapEntity(o));
        }
        
        @JSStaticFunction
        public static double getPitch(final Object o) {
            return getRotation(o)[1];
        }
        
        @JSStaticFunction
        public static long getPlayerEnt() {
            return NativeAPI.getPlayer();
        }
        
        @JSStaticFunction
        public static float[] getPosition(final Object o) {
            final float[] array = new float[3];
            NativeAPI.getPosition(unwrapEntity(o), array);
            return array;
        }
        
        @JSStaticFunction
        public static ItemInstance getProjectileItem(final Object o) {
            return new ItemInstance(new NativeItemInstance(NativeAPI.getItemFromProjectile(unwrapEntity(o))));
        }
        
        @JSStaticFunction
        public static int getRenderType(final Object o) {
            return NativeAPI.getRenderType(unwrapEntity(o));
        }
        
        @JSStaticFunction
        public static long getRider(final Object o) {
            return NativeAPI.getRider(unwrapEntity(o));
        }
        
        @JSStaticFunction
        public static long getRiding(final Object o) {
            return NativeAPI.getRiding(unwrapEntity(o));
        }
        
        @JSStaticFunction
        public static float[] getRotation(final Object o) {
            final float[] array = new float[2];
            NativeAPI.getRotation(unwrapEntity(o), array);
            return array;
        }
        
        @Placeholder
        @JSStaticFunction
        public static String getSkin(final Object o) {
            AdaptedScriptAPI.logDeprecation("Entity.getSkin");
            return "missing_texture.png";
        }
        
        @JSStaticFunction
        public static long getTarget(final Object o) {
            return NativeAPI.getTarget(unwrapEntity(o));
        }
        
        @JSStaticFunction
        public static int getType(final Object o) {
            return NativeAPI.getEntityType(unwrapEntity(o));
        }
        
        @JSStaticFunction
        public static String getTypeName(final Object o) {
            return NativeAPI.getEntityTypeName(unwrapEntity(o));
        }
        
        @JSStaticFunction
        public static double getVelX(final Object o) {
            return getVelocity(o)[0];
        }
        
        @JSStaticFunction
        public static double getVelY(final Object o) {
            return getVelocity(o)[1];
        }
        
        @JSStaticFunction
        public static double getVelZ(final Object o) {
            return getVelocity(o)[2];
        }
        
        @JSStaticFunction
        public static float[] getVelocity(final Object o) {
            final float[] array = new float[3];
            NativeAPI.getVelocity(unwrapEntity(o), array);
            return array;
        }
        
        @JSStaticFunction
        public static double getX(final Object o) {
            return getPosition(o)[0];
        }
        
        @JSStaticFunction
        public static double getY(final Object o) {
            return getPosition(o)[1];
        }
        
        @JSStaticFunction
        public static double getYaw(final Object o) {
            return getRotation(o)[0];
        }
        
        @JSStaticFunction
        public static double getZ(final Object o) {
            return getPosition(o)[2];
        }
        
        @JSStaticFunction
        public static boolean isImmobile(final Object o) {
            return NativeAPI.isImmobile(unwrapEntity(o));
        }
        
        @JSStaticFunction
        public static boolean isSneaking(final Object o) {
            return NativeAPI.isSneaking(unwrapEntity(o));
        }
        
        @JSStaticFunction
        public static boolean isValid(final Object o) {
            return NativeAPI.isValidEntity(unwrapEntity(o));
        }
        
        @JSStaticFunction
        public static void remove(final Object o) {
            NativeAPI.removeEntity(unwrapEntity(o));
        }
        
        @JSStaticFunction
        public static void removeAllEffects(final Object o) {
            NativeAPI.removeAllEffects(unwrapEntity(o));
        }
        
        @JSStaticFunction
        public static void removeEffect(final Object o, final int n) {
            NativeAPI.removeEffect(unwrapEntity(o), n);
        }
        
        @JSStaticFunction
        public static void rideAnimal(final Object o, final Object o2) {
            NativeAPI.rideAnimal(unwrapEntity(o), unwrapEntity(o2));
        }
        
        @JSStaticFunction
        public static void setAnimalAge(final Object o, final int n) {
            NativeAPI.setAge(unwrapEntity(o), n);
            AdaptedScriptAPI.logDeprecation("Entity.setAnimalAge");
        }
        
        @JSStaticFunction
        public static void setArmor(final Object o, final int n, final int n2, final int n3, final int n4, final Object o2) {
            NativeAPI.setEntityArmor(unwrapEntity(o), n, n2, n3, n4, NativeItemInstanceExtra.unwrapValue(o2));
        }
        
        @JSStaticFunction
        public static void setArmorSlot(final Object o, final int n, final int n2, final int n3, final int n4, final Object o2) {
            NativeAPI.setEntityArmor(unwrapEntity(o), n, n2, n3, n4, NativeItemInstanceExtra.unwrapValue(o2));
        }
        
        @JSStaticFunction
        public static void setCarriedItem(final Object o, final int n, final int n2, final int n3, final Object o2) {
            NativeAPI.setEntityCarriedItem(unwrapEntity(o), n, n2, n3, NativeItemInstanceExtra.unwrapValue(o2));
        }
        
        @JSStaticFunction
        public static void setCollisionSize(final Object o, final double n, final double n2) {
            NativeAPI.setCollisionSize(unwrapEntity(o), (float)n, (float)n2);
        }
        
        @JSStaticFunction
        public static void setCompoundTag(final Object o, final Object o2) {
            final NativeCompoundTag nativeCompoundTag = (NativeCompoundTag)Context.jsToJava(o2, (Class)NativeCompoundTag.class);
            if (nativeCompoundTag != null) {
                NativeAPI.setEntityCompoundTag(unwrapEntity(o), nativeCompoundTag.pointer);
            }
        }
        
        @Indev
        @JSStaticFunction
        public static void setDroppedItem(final Object o, final int n, final int n2, final int n3, final Object o2) {
            NativeAPI.setItemToDrop(unwrapEntity(o), n, n2, n3, NativeItemInstanceExtra.unwrapValue(o2));
        }
        
        @JSStaticFunction
        public static void setFireTicks(final Object o, final int n, final boolean b) {
            NativeAPI.setFireTicks(unwrapEntity(o), n, b);
        }
        
        @JSStaticFunction
        public static void setHealth(final Object o, final int n) {
            NativeAPI.setHealth(unwrapEntity(o), n);
        }
        
        @JSStaticFunction
        public static void setImmobile(final Object o, final boolean b) {
            NativeAPI.setImmobile(unwrapEntity(o), b);
        }
        
        @JSStaticFunction
        public static void setMaxHealth(final Object o, final int n) {
            NativeAPI.setMaxHealth(unwrapEntity(o), n);
        }
        
        @JSStaticFunction
        public static void setMobSkin(final Object o, final String s) {
            NativeAPI.setSkin(unwrapEntity(o), s);
        }
        
        @JSStaticFunction
        public static void setNameTag(final Object o, final String s) {
            NativeAPI.setNameTag(unwrapEntity(o), s);
        }
        
        @JSStaticFunction
        public static void setOffhandItem(final Object o, final int n, final int n2, final int n3, final Object o2) {
            NativeAPI.setEntityOffhandItem(unwrapEntity(o), n, n2, n3, NativeItemInstanceExtra.unwrapValue(o2));
        }
        
        @JSStaticFunction
        public static void setPosition(final Object o, final double n, final double n2, final double n3) {
            NativeAPI.setPosition(unwrapEntity(o), (float)n, (float)n2, (float)n3);
        }
        
        @JSStaticFunction
        public static void setPositionAxis(final Object o, final int n, final double n2) {
            NativeAPI.setPositionAxis(unwrapEntity(o), n, (float)n2);
        }
        
        @JSStaticFunction
        public static void setRenderType(final Object o, final int n) {
            NativeAPI.setRenderType(unwrapEntity(o), n);
        }
        
        @JSStaticFunction
        public static void setRot(final Object o, final double n, final double n2) {
            setRotation(o, n, n2);
        }
        
        @JSStaticFunction
        public static void setRotation(final Object o, final double n, final double n2) {
            NativeAPI.setRotation(unwrapEntity(o), (float)n, (float)n2);
        }
        
        @JSStaticFunction
        public static void setRotationAxis(final Object o, final int n, final double n2) {
            NativeAPI.setRotationAxis(unwrapEntity(o), n, (float)n2);
        }
        
        @JSStaticFunction
        public static void setSkin(final Object o, final String s) {
            NativeAPI.setSkin(unwrapEntity(o), s);
        }
        
        @JSStaticFunction
        public static void setSneaking(final Object o, final boolean b) {
            NativeAPI.setSneaking(unwrapEntity(o), b);
        }
        
        @JSStaticFunction
        public static void setTarget(final Object o, final Object o2) {
            NativeAPI.setTarget(unwrapEntity(o), unwrapEntity(o2));
        }
        
        @JSStaticFunction
        public static void setVelocity(final Object o, final double n, final double n2, final double n3) {
            NativeAPI.setVelocity(unwrapEntity(o), (float)n, (float)n2, (float)n3);
        }
        
        @JSStaticFunction
        public static void setVelocityAxis(final Object o, final int n, final double n2) {
            NativeAPI.setVelocityAxis(unwrapEntity(o), n, (float)n2);
        }
        
        static long unwrapEntity(Object o) {
            if (o instanceof Wrapper) {
                o = ((Wrapper)o).unwrap();
            }
            else {
                o = ((Number)o).longValue();
            }
            return (long)o;
        }
    }
    
    @APIStaticModule
    public static class FileUtil
    {
        @JSStaticFunction
        public static String readFileText(final String s) {
            try {
                return FileUtils.readFileText(new File(s));
            }
            catch (IOException ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append("error in reading file ");
                sb.append(s);
                ICLog.e("FileUtil", sb.toString(), ex);
                return null;
            }
        }
        
        @JSStaticFunction
        public static void writeFileText(final String s, final String s2) {
            try {
                FileUtils.writeFileText(new File(s), s2);
            }
            catch (IOException ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append("error in writing file ");
                sb.append(s);
                ICLog.e("FileUtil", sb.toString(), ex);
            }
        }
    }
    
    @APIStaticModule
    public static class GenerationUtils extends NativeGenerationUtils
    {
    }
    
    @APIStaticModule
    public static class ICRender extends NativeICRender
    {
    }
    
    @APIStaticModule
    public static class IDRegistry extends com.zhekasmirnov.innercore.api.unlimited.IDRegistry
    {
        @Deprecated
        @JSStaticFunction
        public static void __placeholder() {
            AdaptedScriptAPI.logDeprecation("IDRegistry.__placeholder");
        }
        
        @JSStaticFunction
        public static String getIdInfo(final int n) {
            return NativeAPI.getIntegerIDStatus(n);
        }
    }
    
    @APIStaticModule
    public static class Item extends NativeItem
    {
        protected Item(final int n, final long n2, final String s, final String s2) {
            super(n, n2, s, s2);
        }
        
        @JSStaticFunction
        public static NativeItem createFoodItem(final int n, final String s, final String s2, final String s3, final int n2, final int n3) {
            final NativeItem item = NativeItem.createItem(n, s, s2, s3, n2);
            final StringBuilder sb = new StringBuilder();
            sb.append("{\"use_animation\":\"eat\",\"use_duration\": 32,\"food\":{\"nutrition\":");
            sb.append(n3);
            sb.append(",\"saturation_modifier\": \"normal\",\"is_meat\": false}}");
            item.setProperties(sb.toString());
            item.setUseAnimation(1);
            item.setMaxUseDuration(32);
            return item;
        }
        
        @JSStaticFunction
        public static int getMaxDamage(final int n) {
            return NativeItem.getMaxDamageForId(n, 0);
        }
        
        @JSStaticFunction
        public static int getMaxStackSize(final int n, final int n2) {
            return NativeItem.getMaxStackForId(n, n2);
        }
        
        @JSStaticFunction
        public static String getName(final int n, final int n2, final Object o) {
            return NativeItem.getNameForId(n, n2, NativeItemInstanceExtra.unwrapValue(o));
        }
        
        public static void invokeItemUseNoTarget(final int n, final int n2, final int n3, final Object o) {
            NativeAPI.invokeUseItemNoTarget(n, n2, n3, NativeItemInstanceExtra.unwrapValue(o));
        }
        
        @JSStaticFunction
        public static void invokeItemUseOn(final int n, final int n2, final int n3, final Object o, final int n4, final int n5, final int n6, final int n7, final double n8, final double n9, final double n10, final Object o2) {
            NativeAPI.invokeUseItemOn(n, n2, n3, NativeItemInstanceExtra.unwrapValue(o), n4, n5, n6, n7, (float)n8, (float)n9, (float)n10, Entity.unwrapEntity(o2));
        }
        
        @JSStaticFunction
        public static void overrideCurrentIcon(final String s, final int n) {
            NativeItem.overrideItemIcon(s, n);
        }
        
        @JSStaticFunction
        public static void overrideCurrentName(final String s) {
            NativeAPI.overrideItemName(s);
        }
        
        @JSStaticFunction
        public static void setRequiresIconOverride(final int n, final boolean b) {
            NativeItem.setItemRequiresIconOverride(n, b);
        }
    }
    
    public static class ItemContainer extends com.zhekasmirnov.apparatus.api.container.ItemContainer
    {
        public ItemContainer() {
        }
        
        public ItemContainer(final com.zhekasmirnov.innercore.api.mod.ui.container.Container container) {
            super(container);
        }
    }
    
    public static class ItemExtraData extends NativeItemInstanceExtra
    {
        public ItemExtraData() {
        }
        
        public ItemExtraData(final long n) {
            long constructClone = 0L;
            if (n != 0L) {
                constructClone = NativeItemInstanceExtra.constructClone(n);
            }
            super(constructClone);
        }
        
        public ItemExtraData(final NativeItemInstanceExtra nativeItemInstanceExtra) {
            super(nativeItemInstanceExtra);
        }
        
        @Override
        protected void finalize() throws Throwable {
            super.finalize();
        }
    }
    
    @APIStaticModule
    public static class ItemModel extends NativeItemModel
    {
    }
    
    @APIStaticModule
    public static class Level
    {
        @JSStaticFunction
        public static void addFarParticle(final int n, final double n2, final double n3, final double n4, final double n5, final double n6, final double n7, final int n8) {
            NativeAPI.addFarParticle(n, n2, n3, n4, n5, n6, n7, n8);
        }
        
        @JSStaticFunction
        public static void addParticle(final int n, final double n2, final double n3, final double n4, final double n5, final double n6, final double n7, final int n8) {
            NativeAPI.addParticle(n, n2, n3, n4, n5, n6, n7, n8);
        }
        
        @JSStaticFunction
        public static String biomeIdToName(final int n) {
            return NativeAPI.getBiomeName(n);
        }
        
        @JSStaticFunction
        public static ScriptableObject clip(double n, final double n2, final double n3, final double n4, final double n5, final double n6, int n7) {
            final float[] array = new float[4];
            final long n8 = NativeAPI.clipWorld((float)n, (float)n2, (float)n3, (float)n4, (float)n5, (float)n6, n7, array);
            final int n9 = (int)array[3];
            n7 = 0;
            int n10 = 0;
            int n11 = 0;
            switch (n9) {
                case 5: {
                    n7 = 0 + 1;
                    break;
                }
                case 4: {
                    n7 = 0 - 1;
                    break;
                }
                case 3: {
                    n11 = 0 + 1;
                    break;
                }
                case 2: {
                    n11 = 0 - 1;
                    break;
                }
                case 1: {
                    n10 = 0 + 1;
                    break;
                }
                case 0: {
                    n10 = 0 - 1;
                    break;
                }
            }
            Math.sqrt(Math.pow(array[0] - n, 2.0) + Math.pow(array[1] - n2, 2.0) + Math.pow(array[2] - n3, 2.0));
            final boolean b = Math.abs(array[0] - n4) >= 1.0E-4 || Math.abs(array[1] - n5) >= 1.0E-4 || Math.abs(array[2] - n6) >= 1.0E-4;
            final Pair pair = new Pair((Object)"entity", (Object)n8);
            final Pair pair2 = new Pair((Object)"side", (Object)(double)n9);
            final Pair pair3 = new Pair((Object)"collision", (Object)b);
            final Pair pair4 = new Pair((Object)"pos", (Object)new ScriptableParams((Pair<String, Object>[])new Pair[] { new Pair((Object)"x", (Object)(double)array[0]), new Pair((Object)"y", (Object)(double)array[1]), new Pair((Object)"z", (Object)(double)array[2]) }));
            if (b) {
                n = array[0] * 0.99 + (1.0 - 0.99) * n;
            }
            else {
                n = array[0];
            }
            final Pair pair5 = new Pair((Object)"x", (Object)n);
            if (b) {
                n = array[1] * 0.99 + n2 * (1.0 - 0.99);
            }
            else {
                n = array[1];
            }
            final Pair pair6 = new Pair((Object)"y", (Object)n);
            if (b) {
                n = array[2] * 0.99 + (1.0 - 0.99) * n3;
            }
            else {
                n = array[2];
            }
            return new ScriptableParams((Pair<String, Object>[])new Pair[] { pair, pair2, pair3, pair4, new Pair((Object)"pos_limit", (Object)new ScriptableParams((Pair<String, Object>[])new Pair[] { pair5, pair6, new Pair((Object)"z", (Object)n) })), new Pair((Object)"normal", (Object)new ScriptableParams((Pair<String, Object>[])new Pair[] { new Pair((Object)"x", (Object)(double)n7), new Pair((Object)"y", (Object)(double)n10), new Pair((Object)"z", (Object)(double)n11) })) });
        }
        
        @JSStaticFunction
        public static void destroyBlock(final int n, final int n2, final int n3, final boolean b) {
            if (NativeAPI.getTile(n, n2, n3) > 0) {
                NativeAPI.destroyBlock(n, n2, n3, b);
            }
        }
        
        @JSStaticFunction
        public static long dropItem(final double n, final double n2, final double n3, final int n4, final int n5, final int n6, final int n7, final Object o) {
            if (n5 == 0) {
                return -1L;
            }
            return NativeAPI.spawnDroppedItem((float)n, (float)n2, (float)n3, n5, n6, n7, NativeItemInstanceExtra.unwrapValue(o));
        }
        
        @JSStaticFunction
        public static void explode(final double n, final double n2, final double n3, final double n4, final boolean b) {
            NativeAPI.explode((float)n, (float)n2, (float)n3, (float)n4, b);
        }
        
        @JSStaticFunction
        public static int getBiome(final int n, final int n2) {
            return NativeAPI.getBiome(n, n2);
        }
        
        @JSStaticFunction
        public static int getBiomeMap(final int n, final int n2) {
            return NativeAPI.getBiomeMap(n, n2);
        }
        
        @JSStaticFunction
        public static int getBrightness(final int n, final int n2, final int n3) {
            return NativeAPI.getBrightness(n, n2, n3);
        }
        
        @JSStaticFunction
        public static int getChunkState(final int n, final int n2) {
            return NativeAPI.getChunkState(n, n2);
        }
        
        @JSStaticFunction
        public static int getChunkStateAt(final int n, final int n2, final int n3) {
            return NativeAPI.getChunkState((int)Math.floor(n / 16.0), (int)Math.floor(n3 / 16.0));
        }
        
        @JSStaticFunction
        public static int getData(final int n, final int n2, final int n3) {
            return NativeAPI.getData(n, n2, n3);
        }
        
        @JSStaticFunction
        public static int getDifficulty() {
            return NativeAPI.getDifficulty();
        }
        
        @JSStaticFunction
        public static int getGameMode() {
            return NativeAPI.getGameMode();
        }
        
        @JSStaticFunction
        public static int getGrassColor(final int n, final int n2) {
            return NativeAPI.getGrassColor(n, n2);
        }
        
        @JSStaticFunction
        public static double getLightningLevel() {
            return NativeAPI.getLightningLevel();
        }
        
        @JSStaticFunction
        public static double getRainLevel() {
            return NativeAPI.getRainLevel();
        }
        
        @JSStaticFunction
        public static long getSeed() {
            return NativeAPI.getSeed();
        }
        
        @JSStaticFunction
        public static float getTemperature(final int n, final int n2, final int n3) {
            return NativeAPI.getBiomeTemperatureAt(n, n2, n3);
        }
        
        @JSStaticFunction
        public static int getTile(final int n, final int n2, final int n3) {
            return NativeAPI.getTile(n, n2, n3);
        }
        
        @JSStaticFunction
        public static int getTileAndData(final int n, final int n2, final int n3) {
            return NativeAPI.getTileAndData(n, n2, n3);
        }
        
        @JSStaticFunction
        public static NativeTileEntity getTileEntity(final int n, final int n2, final int n3) {
            return NativeTileEntity.getTileEntity(n, n2, n3);
        }
        
        @JSStaticFunction
        public static long getTime() {
            return NativeAPI.getTime();
        }
        
        @JSStaticFunction
        public static String getWorldDir() {
            return LevelInfo.getLevelDir();
        }
        
        @JSStaticFunction
        public static String getWorldName() {
            return LevelInfo.getLevelName();
        }
        
        @JSStaticFunction
        public static boolean isChunkLoaded(final int n, final int n2) {
            return NativeAPI.isChunkLoaded(n, n2);
        }
        
        @JSStaticFunction
        public static boolean isChunkLoadedAt(final int n, final int n2, final int n3) {
            return NativeAPI.isChunkLoaded((int)Math.floor(n / 16.0), (int)Math.floor(n3 / 16.0));
        }
        
        @Placeholder
        @JSStaticFunction
        public static void playSound(final double n, final double n2, final double n3, final String s, final double n4, final double n5) {
            NativeAPI.playSound(s, (float)n, (float)n2, (float)n3, (float)n4, (float)n5);
        }
        
        @Placeholder
        @JSStaticFunction
        public static void playSoundEnt(final Object o, final String s, final double n, final double n2) {
            NativeAPI.playSoundEnt(s, Entity.unwrapEntity(o), (float)n, (float)n2);
        }
        
        @JSStaticFunction
        public static void resetCloudColor() {
            NativeAPI.resetCloudColor();
        }
        
        @JSStaticFunction
        public static void resetFogColor() {
            NativeAPI.resetFogColor();
        }
        
        @JSStaticFunction
        public static void resetFogDistance() {
            NativeAPI.resetFogDistance();
        }
        
        @JSStaticFunction
        public static void resetSkyColor() {
            NativeAPI.resetSkyColor();
        }
        
        @JSStaticFunction
        public static void resetSunsetColor() {
            NativeAPI.resetSunsetColor();
        }
        
        @JSStaticFunction
        public static void setBiome(final int n, final int n2, final int n3) {
            NativeAPI.setBiome(n, n2, n3);
        }
        
        @JSStaticFunction
        public static void setBiomeMap(final int n, final int n2, final int n3) {
            NativeAPI.setBiomeMap(n, n2, n3);
        }
        
        @JSStaticFunction
        public static void setBlockChangeCallbackEnabled(final int n, final boolean b) {
            NativeAPI.setBlockChangeCallbackEnabled(n, b);
        }
        
        @JSStaticFunction
        public static void setCloudColor(final double n, final double n2, final double n3) {
            NativeAPI.setCloudColor((float)n, (float)n2, (float)n3);
        }
        
        @JSStaticFunction
        public static void setDifficulty(final int difficulty) {
            NativeAPI.setDifficulty(difficulty);
        }
        
        @JSStaticFunction
        public static void setFogColor(final double n, final double n2, final double n3) {
            NativeAPI.setFogColor((float)n, (float)n2, (float)n3);
        }
        
        @JSStaticFunction
        public static void setFogDistance(final double n, final double n2) {
            NativeAPI.setFogDistance((float)n, (float)n2);
        }
        
        @JSStaticFunction
        public static void setGameMode(final int gameMode) {
            NativeAPI.setGameMode(gameMode);
        }
        
        @JSStaticFunction
        public static void setGrassColor(final int n, final int n2, final int n3) {
            NativeAPI.setGrassColor(n, n2, n3);
        }
        
        @JSStaticFunction
        public static void setLightningLevel(final double n) {
            NativeAPI.setLightningLevel((float)n);
        }
        
        @JSStaticFunction
        public static void setNightMode(final boolean nightMode) {
            NativeAPI.setNightMode(nightMode);
        }
        
        @JSStaticFunction
        public static void setRainLevel(final double n) {
            NativeAPI.setRainLevel((float)n);
        }
        
        @JSStaticFunction
        public static void setRespawnCoords(final int n, final int n2, final int n3) {
            NativeAPI.setRespawnCoords(n, n2, n3);
        }
        
        @JSStaticFunction
        public static void setSkyColor(final double n, final double n2, final double n3) {
            NativeAPI.setSkyColor((float)n, (float)n2, (float)n3);
        }
        
        @DeprecatedAPIMethod
        @JSStaticFunction
        public static void setSpawn(final int n, final int n2, final int n3) {
            NativeAPI.setRespawnCoords(n, n2, n3);
        }
        
        @JSStaticFunction
        public static void setSunsetColor(final double n, final double n2, final double n3) {
            NativeAPI.setSunsetColor((float)n, (float)n2, (float)n3);
        }
        
        @JSStaticFunction
        public static void setTile(final int n, final int n2, final int n3, final int n4, final int n5) {
            NativeAPI.setTile(n, n2, n3, n4, n5);
        }
        
        @JSStaticFunction
        public static void setTime(final int n) {
            NativeAPI.setTime(n);
        }
        
        @JSStaticFunction
        public static void spawnExpOrbs(final double n, final double n2, final double n3, final int n4) {
            NativeAPI.spawnExpOrbs((float)n, (float)n2, (float)n3, n4);
        }
        
        @JSStaticFunction
        public static long spawnMob(final double n, final double n2, final double n3, final int n4, final String s) {
            final long spawnEntity = NativeAPI.spawnEntity(n4, (float)n, (float)n2, (float)n3);
            if (s != null && s.length() > 0 && !s.equals("undefined")) {
                NativeAPI.setSkin(spawnEntity, s);
            }
            return spawnEntity;
        }
    }
    
    @APIStaticModule
    public static class Logger
    {
        @JSStaticFunction
        public static void Flush() {
            ICLog.flush();
        }
        
        @JSStaticFunction
        public static void Log(final String s, final String s2) {
            String s3 = null;
            Label_0016: {
                if (s2 != null) {
                    s3 = s2;
                    if (!s2.isEmpty()) {
                        break Label_0016;
                    }
                }
                s3 = "MOD";
            }
            ICLog.d(s3, s);
        }
        
        @JSStaticFunction
        public static void LogError(final Object o) {
            try {
                ICLog.e("ERROR", "STACK TRACE:", (Throwable)Context.jsToJava(o, (Class)Throwable.class));
            }
            catch (Throwable t) {}
        }
        
        @JSStaticFunction
        public static void debug(final String s, final String s2) {
            ICLog.d(s, s2);
        }
        
        @JSStaticFunction
        public static void error(final String s, final String s2, final Object o) {
            try {
                ICLog.e(s, s2, (Throwable)Context.jsToJava(o, (Class)Throwable.class));
            }
            catch (Throwable t) {
                final StringBuilder sb = new StringBuilder();
                sb.append("error occurred while logging mod error (");
                sb.append(o);
                sb.append("):");
                ICLog.e(null, sb.toString(), t);
            }
        }
        
        @JSStaticFunction
        public static void info(final String s, final String s2) {
            ICLog.i(s, s2);
        }
    }
    
    @APIStaticModule
    public static class MCSystem
    {
        @JSStaticFunction
        public static String addRuntimePack(final String s, final String s2) {
            return ModLoader.instance.addRuntimePack(ModLoader.MinecraftPackType.fromString(s), s2).getAbsolutePath();
        }
        
        @JSStaticFunction
        public static void debugAPILookUp() {
            API.debugLookUpClass(AdaptedScriptAPI.class);
        }
        
        @JSStaticFunction
        public static void debugBmp(final Object o) {
            DebugAPI.img((Bitmap)Context.jsToJava(o, (Class)Bitmap.class));
        }
        
        @JSStaticFunction
        public static void debugStr(final String s) {
            DebugAPI.dialog(s);
        }
        
        @JSStaticFunction
        public static Object evalInScope(final String s, final Scriptable scriptable, final String s2) {
            return Compiler.assureContextForCurrentThread().evaluateString(scriptable, s, s2, 0, (Object)null);
        }
        
        @JSStaticFunction
        public static void forceNativeCrash() {
            NativeAPI.forceCrash();
        }
        
        @JSStaticFunction
        public static Activity getContext() {
            return UIUtils.getContext();
        }
        
        @JSStaticFunction
        public static Object getInnerCoreVersion() {
            return Version.INNER_CORE_VERSION.name;
        }
        
        @JSStaticFunction
        public static String getMinecraftVersion() {
            return MinecraftVersions.getCurrent().getName();
        }
        
        @JSStaticFunction
        public static NetworkJsAdapter getNetwork() {
            return new NetworkJsAdapter(Network.getSingleton());
        }
        
        @JSStaticFunction
        public static boolean isDefaultPrevented() {
            return NativeAPI.isDefaultPrevented();
        }
        
        @JSStaticFunction
        public static boolean isMainThreadStopped() {
            return TickManager.isStopped();
        }
        
        @JSStaticFunction
        public static void runAsUi(final Object o) {
            getContext().runOnUiThread((Runnable)Context.jsToJava(o, (Class)Runnable.class));
        }
        
        @JSStaticFunction
        public static void runOnClientThread(final Object o) {
            MainThreadQueue.localThread.enqueue((Runnable)Context.jsToJava(o, (Class)Runnable.class));
        }
        
        @JSStaticFunction
        public static void runOnMainThread(final Object o) {
            MainThreadQueue.serverThread.enqueue((Runnable)Context.jsToJava(o, (Class)Runnable.class));
        }
        
        @JSStaticFunction
        public static void setCustomFatalErrorCallback(final Object o) {
            DialogHelper.setCustomFatalErrorCallback((DialogHelper.ICustomErrorCallback)Context.jsToJava(o, (Class)DialogHelper.ICustomErrorCallback.class));
        }
        
        @JSStaticFunction
        public static void setCustomNonFatalErrorCallback(final Object o) {
            DialogHelper.setCustomNonFatalErrorCallback((DialogHelper.ICustomErrorCallback)Context.jsToJava(o, (Class)DialogHelper.ICustomErrorCallback.class));
        }
        
        @JSStaticFunction
        public static void setCustomStartupErrorCallback(final Object o) {
            DialogHelper.setCustomStartupErrorCallback((DialogHelper.ICustomErrorCallback)Context.jsToJava(o, (Class)DialogHelper.ICustomErrorCallback.class));
        }
        
        @JSStaticFunction
        public static void setLoadingTip(final String tip) {
            LoadingUI.setTip(tip);
        }
        
        @JSStaticFunction
        public static void setNativeThreadPriority(final int n) {
            AdaptedScriptAPI.logDeprecation("setNativeThreadPriority");
        }
        
        @JSStaticFunction
        public static void simulateBackPressed() {
        }
        
        @JSStaticFunction
        public static void throwException(final String s) {
            throw new RuntimeException(s);
        }
    }
    
    @APIStaticModule
    public static class NBT extends NbtDataType
    {
        public static class CompoundTag extends NativeCompoundTag
        {
            public CompoundTag() {
            }
            
            public CompoundTag(final NativeCompoundTag nativeCompoundTag) {
                super(nativeCompoundTag);
            }
            
            @Override
            protected void finalize() throws Throwable {
                super.finalize();
            }
        }
        
        public static class ListTag extends NativeListTag
        {
            public ListTag() {
            }
            
            public ListTag(final NativeListTag nativeListTag) {
                super(nativeListTag);
            }
            
            @Override
            protected void finalize() throws Throwable {
                super.finalize();
            }
        }
    }
    
    public static class NetworkConnectedClientList extends ConnectedClientList
    {
        public NetworkConnectedClientList() {
        }
        
        public NetworkConnectedClientList(final boolean b) {
            super(b);
        }
    }
    
    public static class NetworkEntity extends com.zhekasmirnov.apparatus.multiplayer.util.entity.NetworkEntity
    {
        public NetworkEntity(final com.zhekasmirnov.apparatus.multiplayer.util.entity.NetworkEntityType networkEntityType) {
            super(networkEntityType);
        }
        
        public NetworkEntity(final com.zhekasmirnov.apparatus.multiplayer.util.entity.NetworkEntityType networkEntityType, final Object o) {
            super(networkEntityType, o);
        }
        
        public NetworkEntity(final com.zhekasmirnov.apparatus.multiplayer.util.entity.NetworkEntityType networkEntityType, final Object o, final String s) {
            super(networkEntityType, o, s);
        }
    }
    
    public static class NetworkEntityType extends com.zhekasmirnov.apparatus.multiplayer.util.entity.NetworkEntityType
    {
        public NetworkEntityType(final String s) {
            super(s);
        }
    }
    
    @APIStaticModule
    public static class Particles extends ParticleRegistry
    {
    }
    
    @APIStaticModule
    public static class Player
    {
        @JSStaticFunction
        public static void addExp(final int n) {
            NativeAPI.addPlayerExperience(n);
        }
        
        @JSStaticFunction
        public static void addExperience(final int n) {
            NativeAPI.addPlayerExperience(n);
        }
        
        @JSStaticFunction
        public static void addItemCreativeInv(final int n, final int n2, final int n3, final Object o) {
            NativeItem.addToCreative(n, n2, n3, o);
        }
        
        @JSStaticFunction
        public static void addItemInventory(final int n, final int n2, final int n3, final boolean b, final Object o) {
            NativeAPI.addItemToInventory(n, n2, n3, NativeItemInstanceExtra.unwrapValue(o), b ^ true);
        }
        
        @JSStaticFunction
        public static boolean canFly() {
            return NativeAPI.canPlayerFly();
        }
        
        @JSStaticFunction
        public static long get() {
            return NativeAPI.getPlayer();
        }
        
        @JSStaticFunction
        public static ItemInstance getArmorSlot(final int n) {
            return new ItemInstance(new NativeItemInstance(NativeAPI.getPlayerArmor(n)));
        }
        
        @JSStaticFunction
        public static boolean getBooleanAbility(final String s) {
            if (NativeAPI.isValidAbility(s)) {
                return NativeAPI.getPlayerBooleanAbility(s);
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("Invalid ability name: ");
            sb.append(s);
            throw new IllegalArgumentException(sb.toString());
        }
        
        @JSStaticFunction
        public static ItemInstance getCarriedItem() {
            return new ItemInstance(new NativeItemInstance(NativeAPI.getEntityCarriedItem(get())));
        }
        
        @JSStaticFunction
        public static int getDimension() {
            return NativeAPI.getDimension();
        }
        
        @JSStaticFunction
        public static double getExhaustion() {
            return NativeAPI.getPlayerExhaustion();
        }
        
        @JSStaticFunction
        public static double getExp() {
            return NativeAPI.getPlayerExperience();
        }
        
        @JSStaticFunction
        public static double getExperience() {
            return NativeAPI.getPlayerExperience();
        }
        
        @JSStaticFunction
        public static float getFloatAbility(final String s) {
            if (NativeAPI.isValidAbility(s)) {
                return NativeAPI.getPlayerFloatAbility(s);
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("Invalid ability name: ");
            sb.append(s);
            throw new IllegalArgumentException(sb.toString());
        }
        
        @JSStaticFunction
        public static double getHunger() {
            return NativeAPI.getPlayerHunger();
        }
        
        @JSStaticFunction
        public static ItemInstance getInventorySlot(final int n) {
            return new ItemInstance(new NativeItemInstance(NativeAPI.getInventorySlot(n)));
        }
        
        @JSStaticFunction
        public static double getLevel() {
            return NativeAPI.getPlayerLevel();
        }
        
        @JSStaticFunction
        public static ItemInstance getOffhandItem() {
            return new ItemInstance(new NativeItemInstance(NativeAPI.getEntityOffhandItem(get())));
        }
        
        @JSStaticFunction
        public static ScriptableObject getPointed() {
            final int[] array = new int[4];
            final float[] array2 = new float[3];
            return new ScriptableParams((Pair<String, Object>[])new Pair[] { new Pair((Object)"pos", (Object)new ScriptableParams((Pair<String, Object>[])new Pair[] { new Pair((Object)"x", (Object)array[0]), new Pair((Object)"y", (Object)array[1]), new Pair((Object)"z", (Object)array[2]), new Pair((Object)"side", (Object)array[3]) })), new Pair((Object)"vec", (Object)new ScriptableParams((Pair<String, Object>[])new Pair[] { new Pair((Object)"x", (Object)array2[0]), new Pair((Object)"y", (Object)array2[1]), new Pair((Object)"z", (Object)array2[2]) })), new Pair((Object)"entity", (Object)NativeAPI.getPointedData(array, array2)) });
        }
        
        @JSStaticFunction
        public static float[] getPosition() {
            return Entity.getPosition(get());
        }
        
        @JSStaticFunction
        public static double getSaturation() {
            return NativeAPI.getPlayerSaturation();
        }
        
        @JSStaticFunction
        public static int getScore() {
            return NativeAPI.getPlayerScore();
        }
        
        @JSStaticFunction
        public static int getSelectedSlotId() {
            return NativeAPI.getPlayerSelectedSlot();
        }
        
        @JSStaticFunction
        public static double getX() {
            return Entity.getX(get());
        }
        
        @JSStaticFunction
        public static double getY() {
            return Entity.getY(get());
        }
        
        @JSStaticFunction
        public static double getZ() {
            return Entity.getY(get());
        }
        
        @JSStaticFunction
        public static boolean isFlying() {
            return NativeAPI.isPlayerFlying();
        }
        
        @JSStaticFunction
        public static boolean isPlayer(final Object o) {
            return NativeAPI.getEntityType(Entity.unwrapEntity(o)) == 1;
        }
        
        @JSStaticFunction
        public static void resetCamera() {
            NativeAPI.nativeSetCameraEntity(0L);
        }
        
        @JSStaticFunction
        public static void resetFov() {
            NativeAPI.nativeSetFov(-1.0f);
        }
        
        @JSStaticFunction
        public static void setAbility(final String s, final Object o) {
            if (!NativeAPI.isValidAbility(s)) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Invalid ability name: ");
                sb.append(s);
                throw new IllegalArgumentException(sb.toString());
            }
            if (o instanceof Number) {
                NativeAPI.setPlayerFloatAbility(s, ((Number)o).floatValue());
                return;
            }
            NativeAPI.setPlayerBooleanAbility(s, (boolean)o);
        }
        
        @JSStaticFunction
        public static void setArmorSlot(final int n, final int n2, final int n3, final int n4, final Object o) {
            NativeAPI.setPlayerArmor(n, n2, n3, n4, NativeItemInstanceExtra.unwrapValue(o));
        }
        
        @JSStaticFunction
        public static void setCameraEntity(final Object o) {
            NativeAPI.nativeSetCameraEntity(Entity.unwrapEntity(o));
        }
        
        @JSStaticFunction
        public static void setCanFly(final boolean playerCanFly) {
            NativeAPI.setPlayerCanFly(playerCanFly);
        }
        
        @JSStaticFunction
        public static void setCarriedItem(final int n, final int n2, final int n3, final Object o) {
            NativeAPI.setEntityCarriedItem(get(), n, n2, n3, NativeItemInstanceExtra.unwrapValue(o));
        }
        
        @JSStaticFunction
        public static void setExhaustion(final double n) {
            NativeAPI.setPlayerExhaustion((float)n);
        }
        
        @JSStaticFunction
        public static void setExp(final double n) {
            NativeAPI.setPlayerExperience((float)n);
        }
        
        @JSStaticFunction
        public static void setExperience(final double n) {
            NativeAPI.setPlayerExperience((float)n);
        }
        
        @JSStaticFunction
        public static void setFlying(final boolean playerFlying) {
            NativeAPI.setPlayerFlying(playerFlying);
        }
        
        @JSStaticFunction
        public static void setFov(final double n) {
            NativeAPI.nativeSetFov((float)n);
        }
        
        @JSStaticFunction
        public static void setHunger(final double n) {
            NativeAPI.setPlayerHunger((float)n);
        }
        
        @JSStaticFunction
        public static void setInventorySlot(final int n, final int n2, final int n3, final int n4, final Object o) {
            NativeAPI.setInventorySlot(n, n2, n3, n4, NativeItemInstanceExtra.unwrapValue(o));
        }
        
        @JSStaticFunction
        public static void setLevel(final double n) {
            NativeAPI.setPlayerLevel((float)n);
        }
        
        @JSStaticFunction
        public static void setOffhandItem(final int n, final int n2, final int n3, final Object o) {
            NativeAPI.setEntityOffhandItem(get(), n, n2, n3, NativeItemInstanceExtra.unwrapValue(o));
        }
        
        @JSStaticFunction
        public static void setSaturation(final double n) {
            NativeAPI.setPlayerSaturation((float)n);
        }
        
        @JSStaticFunction
        public static void setSelectedSlotId(final int playerSelectedSlot) {
            NativeAPI.setPlayerSelectedSlot(playerSelectedSlot);
        }
    }
    
    public static class PlayerActor extends NativePlayer
    {
        public PlayerActor(final long n) {
            super(n);
        }
    }
    
    @APIStaticModule
    public static class Recipes extends RecipeRegistry
    {
        @Deprecated
        @JSStaticFunction
        public static void __placeholder() {
            AdaptedScriptAPI.logDeprecation("Recipes.__placeholder");
        }
    }
    
    public static class RenderMesh extends NativeRenderMesh
    {
        public RenderMesh() {
        }
        
        public RenderMesh(final String s) {
            this(s, "obj");
        }
        
        public RenderMesh(final String s, final String s2) {
            this(s, s2, null);
        }
        
        public RenderMesh(final String s, final String s2, final Scriptable scriptable) {
            this();
            this.importFromFile(s, s2, scriptable);
        }
    }
    
    @APIStaticModule
    public static class Renderer extends NativeRenderer
    {
        @JSStaticFunction
        public static NativeRenderer.Renderer getItemModel(final int n, final int n2, final int n3, final double n4, final double n5, final double n6, final double n7, final boolean b) {
            return ItemModels.getItemOrBlockModel(n, n2, n3, n4, n5, n6, n7, b);
        }
    }
    
    @APIStaticModule
    public static class Resources
    {
        @JSStaticFunction
        public static String getBlockTextureName(final String s, final int n) {
            return ResourcePackManager.getBlockTextureName(s, n);
        }
        
        @JSStaticFunction
        public static byte[] getBytes(final String s) {
            return FileTools.getAssetBytes(s);
        }
        
        @JSStaticFunction
        public static InputStream getInputStream(final String s) {
            return FileTools.getAssetInputStream(s);
        }
        
        @JSStaticFunction
        public static String getItemTextureName(final String s, final int n) {
            return ResourcePackManager.getItemTextureName(s, n);
        }
    }
    
    @APIStaticModule
    public static class Saver
    {
        @JSStaticFunction
        public static Object deserializeFromString(final String s) {
            try {
                return ScriptableSerializer.scriptableFromJson(ScriptableSerializer.stringToJson(s));
            }
            catch (JSONException ex) {
                throw new RuntimeException(ex.getMessage(), (Throwable)ex);
            }
        }
        
        @JSStaticFunction
        public static int getObjectSaverId(final Object o) {
            final ObjectSaver saver = ObjectSaverRegistry.getSaverFor(o);
            if (saver != null) {
                return saver.getSaverId();
            }
            return -1;
        }
        
        @JSStaticFunction
        public static void registerObject(final Object o, final int n) {
            ObjectSaverRegistry.registerObject(o, n);
        }
        
        @JSStaticFunction
        public static int registerObjectSaver(final String s, final Object o) {
            return ObjectSaverRegistry.registerSaver(s, new ObjectSaver() {
                final /* synthetic */ IObjectSaver val$saver = (IObjectSaver)Context.jsToJava(o, (Class)IObjectSaver.class);
                
                @Override
                public Object read(final ScriptableObject scriptableObject) {
                    return this.val$saver.read(scriptableObject);
                }
                
                @Override
                public ScriptableObject save(final Object o) {
                    return this.val$saver.save(o);
                }
            });
        }
        
        @JSStaticFunction
        public static void registerScopeSaver(final String s, final Object o) {
            WorldDataScopeRegistry.getInstance().addScope(s, (WorldDataScopeRegistry.SaverScope)new ScriptableSaverScope() {
                final /* synthetic */ IScopeSaver val$saver = (IScopeSaver)Context.jsToJava(o, (Class)IScopeSaver.class);
                
                @Override
                public void read(final Object o) {
                    Object empty = o;
                    if (o == null) {
                        empty = ScriptableObjectHelper.createEmpty();
                    }
                    this.val$saver.read(empty);
                }
                
                @Override
                public Object save() {
                    return this.val$saver.save();
                }
            });
        }
        
        @JSStaticFunction
        public static String serializeToString(final Object o) {
            return ScriptableSerializer.jsonToString(ScriptableSerializer.scriptableToJson(o, null));
        }
        
        @JSStaticFunction
        public static void setObjectIgnored(final ScriptableObject scriptableObject, final boolean b) {
            ObjectSaverRegistry.setObjectIgnored(scriptableObject, b);
        }
        
        @APIIgnore
        private interface IObjectSaver
        {
            Object read(final ScriptableObject p0);
            
            ScriptableObject save(final Object p0);
        }
        
        @APIIgnore
        private interface IScopeSaver
        {
            void read(final Object p0);
            
            Object save();
        }
    }
    
    @APIStaticModule
    public static class StaticRenderer
    {
        @JSStaticFunction
        public static NativeStaticRenderer createStaticRenderer(final int n, final double n2, final double n3, final double n4) {
            Label_0029: {
                if (n != -1) {
                    break Label_0029;
                }
                final float n5 = (float)n2;
                final float n6 = (float)n3;
                final float n7 = (float)n4;
                while (true) {
                    try {
                        return NativeStaticRenderer.createStaticRenderer(null, n5, n6, n7);
                        final StringBuilder sb = new StringBuilder();
                        sb.append("invalid renderer id ");
                        sb.append(n);
                        sb.append(", id must belong only to custom renderer");
                        throw new IllegalArgumentException(sb.toString());
                        return NativeStaticRenderer.createStaticRenderer(NativeRenderer.getRendererById(n), (float)n2, (float)n3, (float)n4);
                    }
                    catch (NullPointerException ex) {
                        continue;
                    }
                    break;
                }
            }
        }
    }
    
    public static class SyncedNetworkData extends com.zhekasmirnov.apparatus.multiplayer.util.entity.SyncedNetworkData
    {
        public SyncedNetworkData() {
        }
        
        public SyncedNetworkData(final String s) {
            super(s);
        }
    }
    
    @APIStaticModule
    public static class TagRegistry extends com.zhekasmirnov.innercore.api.mod.TagRegistry
    {
        @JSStaticFunction
        public static void addCommonObject(final String s, final Object o, final NativeArray nativeArray) {
            com.zhekasmirnov.innercore.api.mod.TagRegistry.getOrCreateGroup(s).addCommonObject(o, toTagArray(nativeArray));
        }
        
        @JSStaticFunction
        public static void addTagFactory(final String s, final Object o) {
            com.zhekasmirnov.innercore.api.mod.TagRegistry.getOrCreateGroup(s).addTagFactory((TagFactory)Context.jsToJava(o, (Class)TagFactory.class));
        }
        
        @JSStaticFunction
        public static void addTagFor(final String s, final Object o, final String s2, final boolean b) {
            if (b) {
                com.zhekasmirnov.innercore.api.mod.TagRegistry.getOrCreateGroup(s).addTagsFor(o, s2);
                return;
            }
            com.zhekasmirnov.innercore.api.mod.TagRegistry.getOrCreateGroup(s).addCommonObject(o, s2);
        }
        
        @JSStaticFunction
        public static void addTagsFor(final String s, final Object o, final NativeArray nativeArray, final boolean b) {
            if (b) {
                com.zhekasmirnov.innercore.api.mod.TagRegistry.getOrCreateGroup(s).addTagsFor(o, toTagArray(nativeArray));
                return;
            }
            com.zhekasmirnov.innercore.api.mod.TagRegistry.getOrCreateGroup(s).addCommonObject(o, toTagArray(nativeArray));
        }
        
        @JSStaticFunction
        public static NativeArray getAllWith(final String s, final Object o) {
            return ScriptableObjectHelper.createArray(com.zhekasmirnov.innercore.api.mod.TagRegistry.getOrCreateGroup(s).getAllWhere((TagPredicate)Context.jsToJava(o, (Class)TagPredicate.class)).toArray());
        }
        
        @JSStaticFunction
        public static NativeArray getAllWithTag(final String s, final String s2) {
            return ScriptableObjectHelper.createArray(com.zhekasmirnov.innercore.api.mod.TagRegistry.getOrCreateGroup(s).getAllWithTag(s2).toArray());
        }
        
        @JSStaticFunction
        public static NativeArray getAllWithTags(final String s, final NativeArray nativeArray) {
            return ScriptableObjectHelper.createArray(com.zhekasmirnov.innercore.api.mod.TagRegistry.getOrCreateGroup(s).getAllWithTags(toTagList(nativeArray)).toArray());
        }
        
        @JSStaticFunction
        public static NativeArray getTagsFor(final String s, final Object o) {
            return ScriptableObjectHelper.createArray(com.zhekasmirnov.innercore.api.mod.TagRegistry.getOrCreateGroup(s).getTags(o).toArray());
        }
        
        @JSStaticFunction
        public static void removeCommonObject(final String s, final Object o) {
            com.zhekasmirnov.innercore.api.mod.TagRegistry.getOrCreateGroup(s).removeCommonObject(o);
        }
        
        @JSStaticFunction
        public static void removeTagsFor(final String s, final Object o, final NativeArray nativeArray) {
            com.zhekasmirnov.innercore.api.mod.TagRegistry.getOrCreateGroup(s).removeTagsFor(o, toTagArray(nativeArray));
        }
        
        private static String[] toTagArray(final NativeArray nativeArray) {
            final List<String> tagList = toTagList(nativeArray);
            final String[] array = new String[tagList.size()];
            tagList.toArray(array);
            return array;
        }
        
        private static List<String> toTagList(final NativeArray nativeArray) {
            final ArrayList<String> list = new ArrayList<String>();
            if (nativeArray != null) {
                final Object[] array = nativeArray.toArray();
                for (int length = array.length, i = 0; i < length; ++i) {
                    final Object o = array[i];
                    if (o != null) {
                        list.add(o.toString());
                    }
                }
            }
            return list;
        }
    }
    
    @APIStaticModule
    public static class Translation
    {
        @JSStaticFunction
        public static void addTranslation(final String s, final ScriptableObject scriptableObject) {
            NameTranslation.addTranslation(s, scriptableObject);
        }
        
        @JSStaticFunction
        public static String getLanguage() {
            return NameTranslation.getLanguage();
        }
        
        @JSStaticFunction
        public static String translate(final String s) {
            return NameTranslation.translate(s);
        }
    }
    
    @APIStaticModule
    public static class UI
    {
        @JSStaticFunction
        public static Activity getContext() {
            return UIUtils.getContext();
        }
        
        @JSStaticFunction
        public static float getScreenHeight() {
            return getScreenRelativeHeight();
        }
        
        @JSStaticFunction
        public static float getScreenRelativeHeight() {
            return UIUtils.screenHeight * 1000.0f / UIUtils.screenWidth;
        }
        
        public static class AdaptiveWindow extends UIAdaptiveWindow
        {
            public AdaptiveWindow() {
                super(ScriptableObjectHelper.createEmpty());
            }
            
            public AdaptiveWindow(final ScriptableObject scriptableObject) {
                super(scriptableObject);
            }
        }
        
        public static class ConfigVisualizer extends com.zhekasmirnov.innercore.api.mod.util.ConfigVisualizer
        {
            public ConfigVisualizer(final com.zhekasmirnov.innercore.mod.build.Config config) {
                super(config);
            }
            
            public ConfigVisualizer(final com.zhekasmirnov.innercore.mod.build.Config config, final String s) {
                super(config, s);
            }
        }
        
        public static class Container extends com.zhekasmirnov.innercore.api.mod.ui.container.Container
        {
            public Container() {
            }
            
            public Container(final Object o) {
                super(o);
            }
        }
        
        public static class Font extends com.zhekasmirnov.innercore.api.mod.ui.types.Font
        {
            public Font(final int n, final float n2, final float n3) {
                super(n, n2, n3);
            }
            
            public Font(final ScriptableObject scriptableObject) {
                super(scriptableObject);
            }
        }
        
        @APIStaticModule
        public static class FrameTextureSource
        {
            @JSStaticFunction
            public static FrameTexture get(final String s) {
                return com.zhekasmirnov.innercore.api.mod.ui.types.FrameTextureSource.getFrameTexture(s);
            }
        }
        
        public static class StandardWindow extends UIWindowStandard
        {
            public StandardWindow() {
                super(ScriptableObjectHelper.createEmpty());
            }
            
            public StandardWindow(final ScriptableObject scriptableObject) {
                super(scriptableObject);
            }
            
            @Override
            protected boolean isLegacyFormat() {
                return false;
            }
        }
        
        public static class StandartWindow extends UIWindowStandard
        {
            public StandartWindow() {
                super(ScriptableObjectHelper.createEmpty());
            }
            
            public StandartWindow(final ScriptableObject scriptableObject) {
                super(scriptableObject);
            }
            
            @Override
            protected boolean isLegacyFormat() {
                return true;
            }
        }
        
        public static class TabbedWindow extends UITabbedWindow
        {
            public TabbedWindow() {
                super(ScriptableObjectHelper.createEmpty());
            }
            
            public TabbedWindow(final UIWindowLocation uiWindowLocation) {
                super(uiWindowLocation);
            }
            
            public TabbedWindow(final ScriptableObject scriptableObject) {
                super(scriptableObject);
            }
        }
        
        public static class Texture extends com.zhekasmirnov.innercore.api.mod.ui.types.Texture
        {
            public Texture(final Object o) {
                super(o);
            }
        }
        
        @APIStaticModule
        public static class TextureSource
        {
            @JSStaticFunction
            public static Bitmap get(final String s) {
                return com.zhekasmirnov.innercore.api.mod.ui.TextureSource.instance.getSafe(s);
            }
            
            @JSStaticFunction
            public static Bitmap getNullable(final String s) {
                return com.zhekasmirnov.innercore.api.mod.ui.TextureSource.instance.get(s);
            }
            
            @JSStaticFunction
            public static void put(final String s, final Object o) {
                com.zhekasmirnov.innercore.api.mod.ui.TextureSource.instance.put(s, (Bitmap)Context.jsToJava(o, (Class)Bitmap.class));
            }
        }
        
        public static class Window extends UIWindow
        {
            public Window() {
                super(ScriptableObjectHelper.createEmpty());
            }
            
            public Window(final UIWindowLocation uiWindowLocation) {
                super(uiWindowLocation);
            }
            
            public Window(final ScriptableObject scriptableObject) {
                super(scriptableObject);
            }
        }
        
        public static class WindowGroup extends UIWindowGroup
        {
        }
        
        public static class WindowLocation extends UIWindowLocation
        {
            public WindowLocation() {
            }
            
            public WindowLocation(final ScriptableObject scriptableObject) {
                super(scriptableObject);
            }
        }
    }
    
    @APIStaticModule
    public static class Updatable
    {
        @JSStaticFunction
        public static void addAnimator(final ScriptableObject scriptableObject) {
            com.zhekasmirnov.innercore.api.runtime.Updatable.getForClient().addUpdatable(scriptableObject);
        }
        
        @JSStaticFunction
        public static void addLocalUpdatable(final ScriptableObject scriptableObject) {
            addAnimator(scriptableObject);
        }
        
        @JSStaticFunction
        public static void addUpdatable(final ScriptableObject scriptableObject) {
            com.zhekasmirnov.innercore.api.runtime.Updatable.getForServer().addUpdatable(scriptableObject);
        }
        
        @JSStaticFunction
        public static List<ScriptableObject> getAll() {
            return com.zhekasmirnov.innercore.api.runtime.Updatable.getForServer().getAllUpdatableObjects();
        }
        
        @JSStaticFunction
        public static int getSyncTime() {
            return TickManager.getTime();
        }
    }
    
    @APIStaticModule
    public static class WorldRenderer
    {
        @JSStaticFunction
        public static Object getGlobalUniformSet() {
            return new NativeShaderUniformSet(NativeAPI.getGlobalShaderUniformSet());
        }
    }
}
