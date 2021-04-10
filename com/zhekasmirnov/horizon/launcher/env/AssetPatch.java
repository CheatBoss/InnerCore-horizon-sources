package com.zhekasmirnov.horizon.launcher.env;

import java.util.*;
import android.content.res.*;
import java.io.*;
import com.zhekasmirnov.horizon.*;

public class AssetPatch
{
    private static String assetDirectory;
    private static HashMap<String, String> assetOverrides;
    
    private static native void nativeSetAssetDirectory(final String p0);
    
    private static native void nativeSetFsRoot(final String p0);
    
    private static native void nativeAddSingleOverride(final String p0, final String p1);
    
    public static void setAssetDirectory(String directory) {
        if (directory != null) {
            while (directory.endsWith("/") || directory.endsWith("\\")) {
                directory = directory.substring(0, directory.length() - 1);
            }
            directory += "/";
        }
        nativeSetAssetDirectory(directory);
        AssetPatch.assetOverrides.clear();
        AssetPatch.assetDirectory = directory;
    }
    
    public static void setRootOverrideDirectory(String directory) {
        if (directory != null) {
            while (directory.endsWith("/") || directory.endsWith("\\")) {
                directory = directory.substring(0, directory.length() - 1);
            }
        }
        nativeSetFsRoot(directory);
    }
    
    public static void addSingleOverride(final String asset, final String path) {
        nativeAddSingleOverride(asset, path);
        AssetPatch.assetOverrides.put(asset, path);
    }
    
    public static void removeSingleOverride(final String asset) {
        nativeAddSingleOverride(asset, null);
        AssetPatch.assetOverrides.remove(asset);
    }
    
    public static String getSingleOverride(final String asset) {
        return AssetPatch.assetOverrides.get(asset);
    }
    
    public static String getRedirectedPath(final String asset) {
        if (AssetPatch.assetDirectory == null) {
            return null;
        }
        final String override = AssetPatch.assetOverrides.get(asset);
        if (override != null) {
            return override;
        }
        return AssetPatch.assetDirectory + asset;
    }
    
    public static InputStream getAssetInputStream(final AssetManager assets, final String asset) {
        if (asset == null || assets == null) {
            return null;
        }
        String redirect = getRedirectedPath(asset);
        if (redirect == null || !new File(redirect).exists()) {
            InputStream stream = null;
            try {
                stream = assets.open(asset);
            }
            catch (IOException ex) {}
            if (stream != null) {
                return stream;
            }
            redirect = asset;
        }
        try {
            return new FileInputStream(new File(redirect));
        }
        catch (IOException ignore) {
            return null;
        }
    }
    
    public static byte[] getAssetBytes(final AssetManager assets, final String asset) {
        final InputStream stream = getAssetInputStream(assets, asset);
        if (stream != null) {
            final byte[] buffer = new byte[65536];
            byte[] contents = new byte[0];
            try {
                int bytesRead;
                while ((bytesRead = stream.read(buffer)) > 0) {
                    final byte[] result = new byte[bytesRead + contents.length];
                    System.arraycopy(contents, 0, result, 0, contents.length);
                    System.arraycopy(buffer, 0, result, contents.length, bytesRead);
                    contents = result;
                }
            }
            catch (IOException exception) {
                exception.printStackTrace();
                return null;
            }
            return contents;
        }
        return null;
    }
    
    public static String[] listAssets(final AssetManager assets, final String path) {
        if (AssetPatch.assetDirectory != null) {
            final File directory = new File(AssetPatch.assetDirectory, path);
            if (directory.isDirectory()) {
                return directory.list();
            }
        }
        try {
            return assets.list(path);
        }
        catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    
    public static InputStream open(final String name) {
        final InputStream result = getAssetInputStream(HorizonApplication.getInstance().getAssets(), name);
        System.out.println("redirected asset manager: " + name + " -> " + result);
        return result;
    }
    
    static {
        HorizonLibrary.include();
        AssetPatch.assetDirectory = null;
        AssetPatch.assetOverrides = new HashMap<String, String>();
    }
}
