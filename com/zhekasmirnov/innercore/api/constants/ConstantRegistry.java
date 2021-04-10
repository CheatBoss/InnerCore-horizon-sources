package com.zhekasmirnov.innercore.api.constants;

import com.zhekasmirnov.innercore.api.nbt.*;
import com.zhekasmirnov.innercore.api.mod.*;
import org.mozilla.javascript.*;
import com.zhekasmirnov.innercore.api.log.*;
import java.util.*;
import java.lang.reflect.*;

public class ConstantRegistry
{
    private static ArrayList<Class> constantClasses;
    
    static {
        ConstantRegistry.constantClasses = new ArrayList<Class>();
        registerClass(ArmorType.class);
        registerClass(BlockFace.class);
        registerClass(BlockRenderLayer.class);
        registerClass(ChatColor.class);
        registerClass(DimensionId.class);
        registerClass(Enchantment.class);
        registerClass(EnchantType.class);
        registerClass(EntityRenderType.class);
        registerClass(EntityType.class);
        registerClass(ItemCategory.class);
        registerClass(GameDifficulty.class);
        registerClass(GameMode.class);
        registerClass(MobEffect.class);
        registerClass(ParticleType.class);
        registerClass(UseAnimation.class);
        registerClass(PlayerAbility.class);
        registerClass(TileEntityType.class);
        registerClass(NbtDataType.class);
    }
    
    public static void injectConstants(final ScriptableObject scriptableObject) {
        for (final Class clazz : ConstantRegistry.constantClasses) {
            final Field[] fields = clazz.getFields();
            final ScriptableObject empty = ScriptableObjectHelper.createEmpty();
            scriptableObject.put(clazz.getSimpleName(), (Scriptable)scriptableObject, (Object)empty);
            for (int length = fields.length, i = 0; i < length; ++i) {
                final Field field = fields[i];
                try {
                    empty.put(field.getName(), (Scriptable)empty, field.get(null));
                }
                catch (IllegalAccessException ex) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("failed to inject constant ");
                    sb.append(clazz.getSimpleName());
                    sb.append(".");
                    sb.append(field.getName());
                    ICLog.e("API", sb.toString(), ex);
                }
            }
        }
    }
    
    public static void registerClass(final Class clazz) {
        if (!ConstantRegistry.constantClasses.contains(clazz)) {
            ConstantRegistry.constantClasses.add(clazz);
        }
    }
}
