package com.zhekasmirnov.innercore.api.unlimited;

import java.util.*;
import com.zhekasmirnov.apparatus.minecraft.enums.*;
import com.zhekasmirnov.innercore.api.*;
import org.mozilla.javascript.*;
import com.zhekasmirnov.innercore.api.mod.*;
import com.zhekasmirnov.apparatus.minecraft.version.*;

public class SpecialType
{
    public static final SpecialType DEFAULT;
    public static final SpecialType NONE;
    private static final String NONE_NAME = "__none__";
    public static final SpecialType OPAQUE;
    private static HashMap<String, SpecialType> specialTypeByName;
    public int base;
    public String color_source;
    public float destroytime;
    public float explosionres;
    public float friction;
    private boolean isApproved;
    public int lightlevel;
    public int lightopacity;
    public int mapcolor;
    public int material;
    public final String name;
    public boolean renderallfaces;
    public int renderlayer;
    public int rendertype;
    public boolean solid;
    public String sound;
    public float translucency;
    
    static {
        SpecialType.specialTypeByName = new HashMap<String, SpecialType>();
        NONE = getSpecialType("__none__");
        DEFAULT = getSpecialType("__default__");
        OPAQUE = createSpecialType("opaque");
        SpecialType.OPAQUE.solid = true;
        SpecialType.OPAQUE.base = 1;
        SpecialType.OPAQUE.lightopacity = 15;
        SpecialType.OPAQUE.explosionres = 4.0f;
        SpecialType.OPAQUE.renderlayer = 2;
        SpecialType.OPAQUE.translucency = 0.0f;
        SpecialType.OPAQUE.sound = "stone";
        SpecialType.OPAQUE.approve();
    }
    
    public SpecialType(final String name) {
        this.isApproved = false;
        this.sound = "";
        this.material = 3;
        this.base = 0;
        this.rendertype = 0;
        this.renderlayer = GameEnums.getInt(GameEnums.getSingleton().getEnum("block_render_layer", "alpha"));
        this.lightlevel = 0;
        this.lightopacity = 0;
        this.explosionres = 3.0f;
        this.destroytime = 1.0f;
        this.friction = 0.6f;
        this.translucency = 1.0f;
        this.solid = false;
        this.renderallfaces = false;
        this.mapcolor = 0;
        this.color_source = "";
        this.name = name;
    }
    
    public static SpecialType createSpecialType(final String s) {
        final SpecialType specialType = getSpecialType(s);
        if (!specialType.equals(SpecialType.DEFAULT)) {
            specialType.approve();
        }
        return specialType;
    }
    
    public static SpecialType getSpecialType(final String s) {
        if (SpecialType.specialTypeByName.containsKey(s)) {
            return SpecialType.specialTypeByName.get(s);
        }
        final SpecialType specialType = new SpecialType(s);
        SpecialType.specialTypeByName.put(s, specialType);
        return specialType;
    }
    
    public SpecialType approve() {
        this.isApproved = true;
        return this;
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o instanceof SpecialType) {
            final SpecialType specialType = (SpecialType)o;
            return this.name.equals("__none__") || specialType.name.equals("__none__") || specialType.name.equals(this.name);
        }
        return super.equals(o);
    }
    
    public boolean isApproved() {
        return this.isApproved;
    }
    
    public void setupBlock(final int n) {
        NativeBlock.setMaterial(n, this.material);
        NativeBlock.setMaterialBase(n, this.base);
        NativeBlock.setSoundType(n, this.sound);
        NativeBlock.setSolid(n, this.solid);
        NativeBlock.setRenderAllFaces(n, this.renderallfaces);
        NativeBlock.setRenderType(n, this.rendertype);
        NativeBlock.setRenderLayer(n, this.renderlayer);
        NativeBlock.setLightLevel(n, this.lightlevel);
        NativeBlock.setLightOpacity(n, this.lightopacity);
        NativeBlock.setExplosionResistance(n, this.explosionres);
        NativeBlock.setFriction(n, this.friction);
        NativeBlock.setDestroyTime(n, this.destroytime);
        NativeBlock.setTranslucency(n, this.translucency);
        NativeBlock.setMapColor(n, this.mapcolor);
        BlockColorSource blockColorSource = BlockColorSource.NONE;
        try {
            if (this.color_source != null) {
                blockColorSource = BlockColorSource.valueOf(this.color_source.toUpperCase());
            }
            else {
                blockColorSource = BlockColorSource.NONE;
            }
        }
        catch (IllegalArgumentException ex) {}
        NativeBlock.setBlockColorSource(n, blockColorSource.id);
        for (int i = 0; i < 16; ++i) {
            final BlockVariant blockVariant = BlockRegistry.getBlockVariant(n, i);
            if (blockVariant != null) {
                blockVariant.renderType = this.rendertype;
                NativeItemModel.getFor(n, i).updateForBlockVariant(blockVariant);
            }
        }
    }
    
    public void setupProperties(final ScriptableObject scriptableObject) {
        if (scriptableObject != null) {
            this.base = ScriptableObjectHelper.getIntProperty(scriptableObject, "base", this.base);
            this.material = ScriptableObjectHelper.getIntProperty(scriptableObject, "material", this.material);
            this.sound = ScriptableObjectHelper.getStringProperty(scriptableObject, "sound", this.sound);
            this.solid = ScriptableObjectHelper.getBooleanProperty(scriptableObject, "solid", this.solid);
            this.renderallfaces = ScriptableObjectHelper.getBooleanProperty(scriptableObject, "renderallfaces", this.renderallfaces);
            this.rendertype = ScriptableObjectHelper.getIntProperty(scriptableObject, "rendertype", this.rendertype);
            this.renderlayer = GameEnums.getSingleton().getIntEnumOrConvertFromLegacyVersion("block_render_layer", ScriptableObjectHelper.getProperty(scriptableObject, "renderlayer", null), this.renderlayer, MinecraftVersions.MINECRAFT_1_11_4);
            this.lightlevel = ScriptableObjectHelper.getIntProperty(scriptableObject, "lightlevel", this.lightlevel);
            this.lightopacity = ScriptableObjectHelper.getIntProperty(scriptableObject, "lightopacity", this.lightopacity);
            this.mapcolor = ScriptableObjectHelper.getIntProperty(scriptableObject, "mapcolor", this.mapcolor);
            this.explosionres = ScriptableObjectHelper.getFloatProperty(scriptableObject, "explosionres", this.explosionres);
            this.friction = ScriptableObjectHelper.getFloatProperty(scriptableObject, "friction", this.friction);
            this.destroytime = ScriptableObjectHelper.getFloatProperty(scriptableObject, "destroytime", this.destroytime);
            this.translucency = ScriptableObjectHelper.getFloatProperty(scriptableObject, "translucency", this.translucency);
            this.color_source = ScriptableObjectHelper.getStringProperty(scriptableObject, "color_source", this.color_source);
        }
    }
    
    enum BlockColorSource
    {
        GRASS(2), 
        LEAVES(1), 
        NONE(0), 
        WATER(3);
        
        public final int id;
        
        private BlockColorSource(final int id) {
            this.id = id;
        }
    }
}
