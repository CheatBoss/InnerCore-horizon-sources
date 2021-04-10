package com.zhekasmirnov.innercore.api;

import com.zhekasmirnov.innercore.api.constants.*;
import java.lang.reflect.*;
import com.zhekasmirnov.innercore.api.runtime.*;

public class NativeAPI
{
    public static native void addEffect(final long p0, final int p1, final int p2, final int p3, final boolean p4, final boolean p5, final boolean p6);
    
    public static native void addFarParticle(final int p0, final double p1, final double p2, final double p3, final double p4, final double p5, final double p6, final int p7);
    
    public static native void addItemToInventory(final int p0, final int p1, final int p2, final long p3, final boolean p4);
    
    public static native void addParticle(final int p0, final double p1, final double p2, final double p3, final double p4, final double p5, final double p6, final int p7);
    
    public static native void addPlayerExperience(final int p0);
    
    public static native void addTextureToLoad(final String p0);
    
    public static native boolean canPlayerFly();
    
    private static String cleanupPathOverride(String s, final boolean b) {
        String s2 = s;
        if (s != null) {
            final String s3 = s = s.replaceAll("\\\\", "/");
            if (b) {
                s2 = s3;
                if (!s3.endsWith("/")) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append(s3);
                    sb.append("/");
                    return sb.toString();
                }
            }
            else {
                while (true) {
                    s2 = s;
                    if (!s.endsWith("/")) {
                        break;
                    }
                    s = s.substring(s.length() - 1);
                }
            }
        }
        return s2;
    }
    
    public static native void clearAllFurnaceRecipes();
    
    public static native void clearAllRenderMappings();
    
    public static native void clearAllStaticRenders();
    
    public static native void clientMessage(final String p0);
    
    public static native int clipWorld(final float p0, final float p1, final float p2, final float p3, final float p4, final float p5, final int p6, final float[] p7);
    
    public static String convertNameId(final String s) {
        final StringBuilder sb = new StringBuilder();
        int n = 0;
        for (int i = 0; i < s.length(); ++i) {
            final char char1 = s.charAt(i);
            if (Character.isUpperCase(char1)) {
                if (n == 0 && sb.length() > 0) {
                    sb.append('_');
                }
                sb.append(Character.toLowerCase(char1));
                ++n;
            }
            else {
                if (n > 1 && char1 != '_') {
                    sb.insert(sb.length() - 1, '_');
                }
                sb.append(char1);
                n = 0;
            }
        }
        return sb.toString();
    }
    
    public static native void dealDamage(final long p0, final int p1, final int p2, final long p3, final boolean p4, final boolean p5);
    
    public static native void destroyBlock(final int p0, final int p1, final int p2, final boolean p3);
    
    public static native String executeCommand(final String p0, final int p1, final int p2, final int p3, final long p4);
    
    public static native void explode(final float p0, final float p1, final float p2, final float p3, final boolean p4);
    
    public static native long[] fetchEntitiesInAABB(final float p0, final float p1, final float p2, final float p3, final float p4, final float p5, final int p6, final boolean p7);
    
    public static native void forceCrash();
    
    public static native void forceLevelSave();
    
    public static void forceRenderRefresh(final int n, final int n2, final int n3, final int n4) {
        forceRenderRefreshUnsafe(n, n2, n3, n4);
    }
    
    public static native void forceRenderRefreshUnsafe(final int p0, final int p1, final int p2, final int p3);
    
    public static native int getAge(final long p0);
    
    public static native void getAtlasTextureCoords(final String p0, final int p1, final float[] p2);
    
    public static native int getBiome(final int p0, final int p1);
    
    public static native int getBiomeMap(final int p0, final int p1);
    
    public static native String getBiomeName(final int p0);
    
    public static native float getBiomeTemperatureAt(final int p0, final int p1, final int p2);
    
    public static native int getBrightness(final int p0, final int p1, final int p2);
    
    public static native int getChunkState(final int p0, final int p1);
    
    public static native int getData(final int p0, final int p1, final int p2);
    
    public static native int getDifficulty();
    
    public static native int getDimension();
    
    public static native long getEntityArmor(final long p0, final int p1);
    
    public static native long getEntityCarriedItem(final long p0);
    
    public static native long getEntityCompoundTag(final long p0);
    
    public static native int getEntityDimension(final long p0);
    
    public static native long getEntityOffhandItem(final long p0);
    
    public static native int getEntityType(final long p0);
    
    public static native String getEntityTypeName(final long p0);
    
    public static native int getFireTicks(final long p0);
    
    public static native String getGameLanguage();
    
    public static native int getGameMode();
    
    public static native long getGlobalShaderUniformSet();
    
    public static native int getGrassColor(final int p0, final int p1);
    
    public static native int getHealth(final long p0);
    
    public static native String getIntegerIDStatus(final int p0);
    
    public static native long getInventorySlot(final int p0);
    
    public static native long getItemFromDrop(final long p0);
    
    public static native long getItemFromProjectile(final long p0);
    
    public static native float getLightningLevel();
    
    public static native long getLocalPlayer();
    
    public static native int getMaxHealth(final long p0);
    
    public static native String getNameTag(final long p0);
    
    public static native long getPlayer();
    
    public static native long getPlayerArmor(final int p0);
    
    public static native boolean getPlayerBooleanAbility(final String p0);
    
    public static native float getPlayerExhaustion();
    
    public static native float getPlayerExperience();
    
    public static native float getPlayerFloatAbility(final String p0);
    
    public static native float getPlayerHunger();
    
    public static native float getPlayerLevel();
    
    public static native float getPlayerSaturation();
    
    public static native int getPlayerScore();
    
    public static native int getPlayerSelectedSlot();
    
    public static native long getPointedData(final int[] p0, final float[] p1);
    
    public static native void getPosition(final long p0, final float[] p1);
    
    public static native float getRainLevel();
    
    public static native int getRenderType(final long p0);
    
    public static native long getRider(final long p0);
    
    public static native long getRiding(final long p0);
    
    public static native void getRotation(final long p0, final float[] p1);
    
    public static native int getSeed();
    
    public static native long getTarget(final long p0);
    
    public static native int getTile(final int p0, final int p1, final int p2);
    
    public static native int getTileAndData(final int p0, final int p1, final int p2);
    
    public static native int getTileUpdateType();
    
    public static native long getTime();
    
    public static native int getUiProfile();
    
    public static native void getVelocity(final long p0, final float[] p1);
    
    public static native void invokeUseItemNoTarget(final int p0, final int p1, final int p2, final long p3);
    
    public static native void invokeUseItemOn(final int p0, final int p1, final int p2, final long p3, final int p4, final int p5, final int p6, final int p7, final float p8, final float p9, final float p10, final long p11);
    
    public static native boolean isChunkLoaded(final int p0, final int p1);
    
    public static native boolean isDefaultPrevented();
    
    public static boolean isGlintItemInstance(final int n, final int n2) {
        return isGlintItemInstance(n, n2, 0L);
    }
    
    public static native boolean isGlintItemInstance(final int p0, final int p1, final long p2);
    
    public static native boolean isImmobile(final long p0);
    
    public static native boolean isLocalServerRunning();
    
    public static native boolean isPlayerFlying();
    
    public static native boolean isSneaking(final long p0);
    
    public static native boolean isTileUpdateAllowed();
    
    public static boolean isValidAbility(final String s) {
        final Field[] declaredFields = PlayerAbility.class.getDeclaredFields();
        for (int length = declaredFields.length, i = 0; i < length; ++i) {
            final Field field = declaredFields[i];
            try {
                if (field.get(null).equals(s)) {
                    return true;
                }
            }
            catch (IllegalAccessException ex) {}
        }
        return false;
    }
    
    public static native boolean isValidEntity(final long p0);
    
    public static native void leaveGame();
    
    public static native void nativeLog(final String p0);
    
    public static native void nativeSetCameraEntity(final long p0);
    
    public static native void nativeSetFov(final float p0);
    
    public static native void nativeVibrate(final int p0);
    
    public static native void overrideItemIcon(final String p0, final int p1);
    
    public static native void overrideItemModel(final long p0);
    
    public static native void overrideItemName(final String p0);
    
    public static native void playSound(final String p0, final float p1, final float p2, final float p3, final float p4, final float p5);
    
    public static native void playSoundEnt(final String p0, final long p1, final float p2, final float p3);
    
    public static native void preventBlockDrop(final int p0, final int p1, final int p2);
    
    public static native void preventDefault();
    
    public static native void preventPendingAppEvent(final int p0, final int p1);
    
    public static native void preventPendingKeyEvent(final int p0, final int p1);
    
    public static native void removeAllEffects(final long p0);
    
    public static native void removeEffect(final long p0, final int p1);
    
    public static native void removeEntity(final long p0);
    
    public static native void resetCloudColor();
    
    public static native void resetFogColor();
    
    public static native void resetFogDistance();
    
    public static native void resetSkyColor();
    
    public static native void resetSunsetColor();
    
    public static native void rideAnimal(final long p0, final long p1);
    
    public static native void sendCachedItemNameOverride(final int p0, final int p1, final String p2);
    
    public static native void setAge(final long p0, final int p1);
    
    public static void setBehaviorPacksPathOverride(final String s) {
        setNativeBehaviorPacksPathOverride(cleanupPathOverride(s, false));
    }
    
    public static native void setBiome(final int p0, final int p1, final int p2);
    
    public static native void setBiomeMap(final int p0, final int p1, final int p2);
    
    public static native void setBlockChangeCallbackEnabled(final int p0, final boolean p1);
    
    public static native void setCloudColor(final float p0, final float p1, final float p2);
    
    public static native void setCollisionSize(final long p0, final float p1, final float p2);
    
    public static native void setDifficulty(final int p0);
    
    public static native void setEntityArmor(final long p0, final int p1, final int p2, final int p3, final int p4, final long p5);
    
    public static native void setEntityCarriedItem(final long p0, final int p1, final int p2, final int p3, final long p4);
    
    public static native void setEntityCompoundTag(final long p0, final long p1);
    
    public static native void setEntityOffhandItem(final long p0, final int p1, final int p2, final int p3, final long p4);
    
    public static native void setFireTicks(final long p0, final int p1, final boolean p2);
    
    public static native void setFogColor(final float p0, final float p1, final float p2);
    
    public static native void setFogDistance(final float p0, final float p1);
    
    public static native void setGameMode(final int p0);
    
    public static native void setGrassColor(final int p0, final int p1, final int p2);
    
    public static native void setHealth(final long p0, final int p1);
    
    public static native void setImmobile(final long p0, final boolean p1);
    
    public static native void setInnerCoreVersion(final String p0);
    
    public static native void setInventorySlot(final int p0, final int p1, final int p2, final int p3, final long p4);
    
    public static native void setItemNameOverrideCallbackForced(final int p0, final boolean p1);
    
    public static native void setItemRequiresIconOverride(final int p0, final boolean p1);
    
    public static native void setItemToDrop(final long p0, final int p1, final int p2, final int p3, final long p4);
    
    public static native void setLightningLevel(final float p0);
    
    public static native void setMaxHealth(final long p0, final int p1);
    
    public static native void setNameTag(final long p0, final String p1);
    
    public static native void setNativeBehaviorPacksPathOverride(final String p0);
    
    public static native void setNativeResourcePacksPathOverride(final String p0);
    
    public static native void setNativeThreadPriorityParams(final int p0, final int p1, final int p2);
    
    public static native void setNativeWorldsPathOverride(final String p0);
    
    public static native void setNightMode(final boolean p0);
    
    public static native void setPlayerArmor(final int p0, final int p1, final int p2, final int p3, final long p4);
    
    public static native void setPlayerBooleanAbility(final String p0, final boolean p1);
    
    public static native void setPlayerCanFly(final boolean p0);
    
    public static native void setPlayerExhaustion(final float p0);
    
    public static native void setPlayerExperience(final float p0);
    
    public static native void setPlayerFloatAbility(final String p0, final float p1);
    
    public static native void setPlayerFlying(final boolean p0);
    
    public static native void setPlayerHunger(final float p0);
    
    public static native void setPlayerLevel(final float p0);
    
    public static native void setPlayerSaturation(final float p0);
    
    public static native void setPlayerSelectedSlot(final int p0);
    
    public static native void setPosition(final long p0, final float p1, final float p2, final float p3);
    
    public static native void setPositionAxis(final long p0, final int p1, final float p2);
    
    public static native void setRainLevel(final float p0);
    
    public static native void setRenderType(final long p0, final int p1);
    
    public static void setResourcePacksPathOverride(final String s) {
        setNativeResourcePacksPathOverride(cleanupPathOverride(s, false));
    }
    
    public static native void setRespawnCoords(final int p0, final int p1, final int p2);
    
    public static native void setRotation(final long p0, final float p1, final float p2);
    
    public static native void setRotationAxis(final long p0, final int p1, final float p2);
    
    public static native void setSkin(final long p0, final String p1);
    
    public static native void setSkyColor(final float p0, final float p1, final float p2);
    
    public static native void setSneaking(final long p0, final boolean p1);
    
    public static native void setSunsetColor(final float p0, final float p1, final float p2);
    
    public static native void setTarget(final long p0, final long p1);
    
    public static native void setTicksPerSecond(final float p0);
    
    public static native void setTile(final int p0, final int p1, final int p2, final int p3, final int p4);
    
    public static native void setTileUpdateAllowed(final boolean p0);
    
    public static native void setTileUpdateType(final int p0);
    
    public static native void setTime(final long p0);
    
    public static native void setVelocity(final long p0, final float p1, final float p2, final float p3);
    
    public static native void setVelocityAxis(final long p0, final int p1, final float p2);
    
    public static void setWorldsPathOverride(String cleanupPathOverride) {
        cleanupPathOverride = cleanupPathOverride(cleanupPathOverride, true);
        setNativeWorldsPathOverride(cleanupPathOverride);
        LevelInfo.worldsPathOverride = cleanupPathOverride;
    }
    
    public static native long spawnDroppedItem(final float p0, final float p1, final float p2, final int p3, final int p4, final int p5, final long p6);
    
    public static native long spawnEntity(final int p0, final float p1, final float p2, final float p3);
    
    public static native void spawnExpOrbs(final float p0, final float p1, final float p2, final int p3);
    
    public static native void teleportTo(final long p0, final float p1, final float p2, final float p3);
    
    public static native void tipMessage(final String p0);
    
    public static native void transferToDimension(final long p0, final int p1);
}
