package com.zhekasmirnov.innercore.api.dimensions;

import java.util.*;

public class CustomDimensionGenerator
{
    private static final HashMap<String, Integer> generatorTypeMap;
    public final int baseType;
    private int modGenerationBaseDimension;
    private boolean modGenerationEnabled;
    public final long pointer;
    
    static {
        (generatorTypeMap = new HashMap<String, Integer>()).put("overworld", 0);
        CustomDimensionGenerator.generatorTypeMap.put("overworld1", 1);
        CustomDimensionGenerator.generatorTypeMap.put("flat", 2);
        CustomDimensionGenerator.generatorTypeMap.put("nether", 3);
        CustomDimensionGenerator.generatorTypeMap.put("end", 4);
    }
    
    public CustomDimensionGenerator(final int baseType) {
        this.modGenerationBaseDimension = -1;
        this.modGenerationEnabled = true;
        if (baseType <= 4 && baseType >= 0) {
            this.pointer = nativeConstruct(baseType);
            this.baseType = baseType;
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("invalid base generator type: ");
        sb.append(baseType);
        throw new IllegalArgumentException(sb.toString());
    }
    
    public CustomDimensionGenerator(final String s) {
        this(getGeneratorType(s));
    }
    
    private static int getGeneratorType(final String s) {
        if (!CustomDimensionGenerator.generatorTypeMap.containsKey(s)) {
            final StringBuilder sb = new StringBuilder();
            final Iterator<String> iterator = CustomDimensionGenerator.generatorTypeMap.keySet().iterator();
            while (iterator.hasNext()) {
                sb.append(iterator.next());
                sb.append(" ");
            }
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("invalid base generator type: ");
            sb2.append(s);
            sb2.append(", valid types: ");
            sb2.append((Object)sb);
            throw new IllegalArgumentException(sb2.toString());
        }
        return CustomDimensionGenerator.generatorTypeMap.get(s);
    }
    
    private static native long nativeConstruct(final int p0);
    
    private static native void nativeSetBuildVanillaSurfaces(final long p0, final boolean p1);
    
    private static native void nativeSetGenerateModStructures(final long p0, final boolean p1);
    
    private static native void nativeSetGenerateVanillaStructures(final long p0, final boolean p1);
    
    private static native void nativeSetTerrainGenerator(final long p0, final long p1);
    
    public int getModGenerationBaseDimension() {
        return this.modGenerationBaseDimension;
    }
    
    public boolean isModGenerationEnabled() {
        return this.modGenerationEnabled;
    }
    
    public CustomDimensionGenerator removeModGenerationBaseDimension() {
        return this.setModGenerationBaseDimension(-1);
    }
    
    public CustomDimensionGenerator setBuildVanillaSurfaces(final boolean b) {
        nativeSetBuildVanillaSurfaces(this.pointer, b);
        return this;
    }
    
    public CustomDimensionGenerator setGenerateModStructures(final boolean modGenerationEnabled) {
        nativeSetGenerateModStructures(this.pointer, modGenerationEnabled);
        if (!(this.modGenerationEnabled = modGenerationEnabled)) {
            this.modGenerationBaseDimension = -1;
        }
        return this;
    }
    
    public CustomDimensionGenerator setGenerateVanillaStructures(final boolean b) {
        nativeSetGenerateVanillaStructures(this.pointer, b);
        return this;
    }
    
    public CustomDimensionGenerator setModGenerationBaseDimension(final int modGenerationBaseDimension) {
        if (modGenerationBaseDimension != -1 && modGenerationBaseDimension != 0 && modGenerationBaseDimension != 1 && modGenerationBaseDimension != 2) {
            final StringBuilder sb = new StringBuilder();
            sb.append("setModGenerationBaseDimension must receive vanilla id or -1, not ");
            sb.append(modGenerationBaseDimension);
            throw new IllegalArgumentException(sb.toString());
        }
        this.setGenerateModStructures(true);
        this.modGenerationBaseDimension = modGenerationBaseDimension;
        return this;
    }
    
    public CustomDimensionGenerator setTerrainGenerator(final AbstractTerrainGenerator abstractTerrainGenerator) {
        final long pointer = this.pointer;
        long pointer2;
        if (abstractTerrainGenerator != null) {
            pointer2 = abstractTerrainGenerator.getPointer();
        }
        else {
            pointer2 = 0L;
        }
        nativeSetTerrainGenerator(pointer, pointer2);
        return this;
    }
}
