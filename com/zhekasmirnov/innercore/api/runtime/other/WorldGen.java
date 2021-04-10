package com.zhekasmirnov.innercore.api.runtime.other;

import com.zhekasmirnov.apparatus.mcpe.*;
import com.zhekasmirnov.innercore.api.log.*;
import com.zhekasmirnov.apparatus.minecraft.version.*;
import com.zhekasmirnov.innercore.api.dimensions.*;
import com.zhekasmirnov.innercore.api.*;
import java.util.*;
import com.zhekasmirnov.innercore.api.runtime.*;
import com.zhekasmirnov.apparatus.adapter.innercore.*;

public class WorldGen
{
    public static final int LEVEL_POST_VANILLA = 1;
    public static final int LEVEL_PRE_VANILLA = 0;
    private static final int MODE_CUSTOM = 3;
    private static final int MODE_END = 2;
    private static final int MODE_NETHER = 1;
    private static final int MODE_SURFACE = 0;
    public static final List<Integer> allLevels;
    
    static {
        (allLevels = new ArrayList<Integer>()).add(0);
        WorldGen.allLevels.add(1);
    }
    
    private static int dimensionIdToGenerationMode(final int n) {
        switch (n) {
            default: {
                return 3;
            }
            case 2: {
                return 2;
            }
            case 1: {
                return 1;
            }
            case 0: {
                return 0;
            }
        }
    }
    
    public static void generateChunk(final int n, final int n2, final int n3) {
        final NativeBlockSource currentWorldGenRegion = NativeBlockSource.getCurrentWorldGenRegion();
        int dimension;
        if (currentWorldGenRegion != null) {
            dimension = currentWorldGenRegion.getDimension();
        }
        else {
            dimension = -1;
        }
        if (dimension == -1) {
            ICLog.i("ERROR", "generating chunk without block source");
            return;
        }
        if (!MinecraftVersions.getCurrent().isFeatureSupported("vanilla_world_generation_levels")) {
            final Iterator<Integer> iterator = WorldGen.allLevels.iterator();
            while (iterator.hasNext()) {
                generateChunk(n, n2, dimension, iterator.next());
            }
        }
        else {
            generateChunk(n, n2, dimension, n3);
        }
    }
    
    public static void generateChunk(final int n, final int n2, final int n3, final int n4) {
        final CustomDimension dimensionById = CustomDimension.getDimensionById(n3);
        if (dimensionById != null) {
            final CustomDimensionGenerator generator = dimensionById.getGenerator();
            if (generator != null) {
                final int modGenerationBaseDimension = generator.getModGenerationBaseDimension();
                if (modGenerationBaseDimension != -1) {
                    generateChunkByLevelAndMode(n, n2, modGenerationBaseDimension, n4, dimensionIdToGenerationMode(modGenerationBaseDimension));
                }
            }
        }
        generateChunkByLevelAndMode(n, n2, n3, n4, dimensionIdToGenerationMode(n3));
    }
    
    private static void generateChunkByLevelAndMode(final int n, final int n2, final int n3, final int n4, final int n5) {
        if (n4 == 1) {
            switch (n5) {
                case 3: {
                    invokeGenerateChunkCallback("GenerateCustomDimensionChunk", n, n2, n3);
                    break;
                }
                case 2: {
                    invokeGenerateChunkCallback("GenerateEndChunk", n, n2, n3);
                    break;
                }
                case 1: {
                    invokeGenerateChunkCallback("GenerateNetherChunk", n, n2, n3);
                    break;
                }
                case 0: {
                    invokeGenerateChunkCallback("GenerateChunk", n, n2, n3);
                    invokeGenerateChunkCallback("GenerateChunkUnderground", n, n2, n3);
                    break;
                }
            }
            invokeGenerateChunkCallback("GenerateChunkUniversal", n, n2, n3);
        }
        switch (n4) {
            default: {}
            case 1: {
                invokeGenerateChunkCallback("PostProcessChunk", n, n2, n3);
            }
            case 0: {
                invokeGenerateChunkCallback("PreProcessChunk", n, n2, n3);
            }
        }
    }
    
    private static void invokeGenerateChunkCallback(String iterator, final int n, final int n2, final int n3) {
        final int seed = NativeAPI.getSeed();
        final long n4 = seed + ((long)n ^ 0x31DAE99AL) * ((long)n2 ^ 0x3156D4ABL);
        iterator = (String)Callback.getCallbackAsRunnableList(iterator, new Object[] { n, n2, new Random(n4), n3, n4, seed, 131071 * n3 ^ seed }).iterator();
        while (((Iterator)iterator).hasNext()) {
            final Runnable runnable = ((Iterator<Runnable>)iterator).next();
            try {
                runnable.run();
            }
            catch (Throwable t) {
                final StringBuilder sb = new StringBuilder();
                sb.append("error occurred in chunk generation ");
                sb.append(n);
                sb.append(", ");
                sb.append(n2);
                sb.append(" for dimension ");
                sb.append(n3);
                UserDialog.insignificantError(sb.toString(), t, true, true);
            }
        }
    }
    
    public static void onBiomeMapGenerated(final int n, final int n2, final int n3) {
        invokeGenerateChunkCallback("GenerateBiomeMap", n2, n3, n);
    }
    
    public static class ChunkPos
    {
        public final int dimension;
        public final int x;
        public final int z;
        
        public ChunkPos(final int dimension, final int x, final int z) {
            this.dimension = dimension;
            this.x = x;
            this.z = z;
        }
        
        @Override
        public boolean equals(final Object o) {
            final boolean b = o instanceof ChunkPos;
            final boolean b2 = false;
            if (b) {
                final ChunkPos chunkPos = (ChunkPos)o;
                boolean b3 = b2;
                if (this.x == chunkPos.x) {
                    b3 = b2;
                    if (this.z == chunkPos.z) {
                        b3 = b2;
                        if (this.dimension == chunkPos.dimension) {
                            b3 = true;
                        }
                    }
                }
                return b3;
            }
            return false;
        }
    }
}
