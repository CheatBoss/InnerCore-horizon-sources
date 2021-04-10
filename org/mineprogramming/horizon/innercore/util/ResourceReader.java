package org.mineprogramming.horizon.innercore.util;

import org.mineprogramming.horizon.innercore.inflater.*;
import java.util.*;
import android.graphics.*;
import java.io.*;
import android.content.*;
import com.zhekasmirnov.horizon.launcher.env.*;
import android.content.res.*;
import org.json.*;

public class ResourceReader implements IncludeListener
{
    private static Map<String, JSONObject> cachedDrawables;
    private static Map<String, JSONObject> cachedLayouts;
    private static String language;
    private static String languageFull;
    
    static {
        ResourceReader.cachedLayouts = new HashMap<String, JSONObject>();
        ResourceReader.cachedDrawables = new HashMap<String, JSONObject>();
        JsonInflater.setIncludeListener((JsonInflater.IncludeListener)new ResourceReader());
        ResourceReader.language = Locale.getDefault().getLanguage();
        ResourceReader.languageFull = Locale.getDefault().toString();
    }
    
    public static void init() {
    }
    
    private Bitmap readBitmapFromStream(final InputStream inputStream) {
        if (inputStream != null) {
            final Bitmap decodeStream = BitmapFactory.decodeStream(inputStream);
            try {
                inputStream.close();
                return decodeStream;
            }
            catch (IOException ex) {
                ex.printStackTrace();
                return decodeStream;
            }
        }
        return null;
    }
    
    public static JSONObject readDrawable(final Context context, final String s) throws JSONException {
        final JSONObject jsonObject = ResourceReader.cachedDrawables.get(s);
        if (jsonObject != null) {
            return jsonObject;
        }
        final AssetManager assets = context.getAssets();
        final StringBuilder sb = new StringBuilder();
        sb.append("res/drawable/");
        sb.append(s);
        sb.append(".json");
        final byte[] assetBytes = AssetPatch.getAssetBytes(assets, sb.toString());
        if (assetBytes == null) {
            return null;
        }
        final JSONObject jsonObject2 = new JSONObject(new String(assetBytes));
        ResourceReader.cachedDrawables.put(s, jsonObject2);
        return jsonObject2;
    }
    
    public static JSONObject readLayout(final Context context, final String s) throws JSONException {
        final JSONObject jsonObject = ResourceReader.cachedLayouts.get(s);
        if (jsonObject != null) {
            return jsonObject;
        }
        final AssetManager assets = context.getAssets();
        final StringBuilder sb = new StringBuilder();
        sb.append("res/layout/");
        sb.append(s);
        sb.append(".json");
        final byte[] assetBytes = AssetPatch.getAssetBytes(assets, sb.toString());
        if (assetBytes == null) {
            return null;
        }
        final JSONObject jsonObject2 = new JSONObject(new String(assetBytes));
        ResourceReader.cachedLayouts.put(s, jsonObject2);
        return jsonObject2;
    }
    
    @Override
    public Bitmap getDrawableBitmap(final Context context, final String s) {
        final AssetManager assets = context.getAssets();
        final StringBuilder sb = new StringBuilder();
        sb.append("res/drawable/");
        sb.append(s);
        sb.append(".png");
        InputStream inputStream;
        if ((inputStream = AssetPatch.getAssetInputStream(assets, sb.toString())) == null) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("res/drawable/");
            sb2.append(s);
            sb2.append(".jpg");
            inputStream = AssetPatch.getAssetInputStream(assets, sb2.toString());
        }
        InputStream assetInputStream;
        if ((assetInputStream = inputStream) == null) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("res/drawable/");
            sb3.append(s);
            sb3.append(".gif");
            assetInputStream = AssetPatch.getAssetInputStream(assets, sb3.toString());
        }
        return this.readBitmapFromStream(assetInputStream);
    }
    
    @Override
    public JSONObject getDrawableJson(final Context context, final String s) {
        try {
            return readDrawable(context, s);
        }
        catch (JSONException ex) {
            throw new RuntimeException("Unable to get drawable", (Throwable)ex);
        }
    }
    
    @Override
    public JSONObject getStrings(final Context context) {
        final AssetManager assets = context.getAssets();
        final StringBuilder sb = new StringBuilder();
        sb.append("res/values/strings-");
        sb.append(ResourceReader.languageFull);
        sb.append(".json");
        byte[] array;
        if ((array = AssetPatch.getAssetBytes(assets, sb.toString())) == null) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("res/values/strings-");
            sb2.append(ResourceReader.language);
            sb2.append(".json");
            if ((array = AssetPatch.getAssetBytes(assets, sb2.toString())) == null && (array = AssetPatch.getAssetBytes(assets, "res/values/strings.json")) == null) {
                return null;
            }
        }
        final String s = new String(array);
        try {
            return new JSONObject(s);
        }
        catch (JSONException ex) {
            throw new RuntimeException("Unable to read strings file", (Throwable)ex);
        }
    }
    
    @Override
    public JSONObject includeLayout(final Context context, final String s) {
        try {
            return readLayout(context, s);
        }
        catch (JSONException ex) {
            throw new RuntimeException("Unable to include layout", (Throwable)ex);
        }
    }
}
