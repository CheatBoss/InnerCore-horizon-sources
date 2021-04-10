package com.zhekasmirnov.innercore.api.mod.ui.icon;

import android.util.*;
import com.zhekasmirnov.innercore.mod.resource.*;
import com.zhekasmirnov.innercore.api.*;
import com.zhekasmirnov.innercore.api.log.*;
import com.zhekasmirnov.innercore.utils.*;
import java.io.*;
import org.json.*;
import com.zhekasmirnov.innercore.api.mod.ui.*;
import java.util.*;
import com.zhekasmirnov.innercore.api.unlimited.*;
import android.graphics.*;

public class ItemIconLoader
{
    private static Pair<String, Integer> getBlockIconName(final Object o) {
        final Pair<String, Integer> pair = null;
        if (o instanceof String) {
            return (Pair<String, Integer>)new Pair((Object)o, (Object)0);
        }
        Pair pair2 = pair;
        if (o instanceof JSONArray) {
            final String optString = ((JSONArray)o).optString(0, (String)null);
            final int optInt = ((JSONArray)o).optInt(1);
            pair2 = pair;
            if (optString != null) {
                pair2 = new Pair((Object)optString, (Object)optInt);
            }
        }
        return (Pair<String, Integer>)pair2;
    }
    
    private static String getBlockIconPath(final Pair<String, Integer> pair) {
        if (pair == null) {
            return null;
        }
        return ResourcePackManager.getBlockTextureName((String)pair.first, (int)pair.second);
    }
    
    private static String getItemIconPath(final Object o) {
        final String s = null;
        if (o instanceof String) {
            return ResourcePackManager.getItemTextureName((String)o, 0);
        }
        String itemTextureName = s;
        if (o instanceof JSONArray) {
            final String optString = ((JSONArray)o).optString(0, (String)null);
            final int optInt = ((JSONArray)o).optInt(1);
            itemTextureName = s;
            if (optString != null) {
                itemTextureName = ResourcePackManager.getItemTextureName(optString, optInt);
            }
        }
        return itemTextureName;
    }
    
    public static void init() {
    }
    
    public static void load() {
        final long currentTimeMillis = System.currentTimeMillis();
        NativeItemModel.setCurrentCacheGroup("vanilla-icons", "1.16-1");
        loadItems();
        loadBlocks();
        NativeItemModel.setCurrentCacheGroup(null, null);
        final StringBuilder sb = new StringBuilder();
        sb.append("loading and constructing icons took ");
        sb.append(System.currentTimeMillis() - currentTimeMillis);
        sb.append(" ms");
        ICLog.i("UI", sb.toString());
    }
    
    private static void loadBlocks() {
        JSONObject loadIconJSON = loadIconJSON("block_models.json");
        JSONArray names = loadIconJSON.names();
        final StringBuilder sb = new StringBuilder();
        sb.append(FileTools.DIR_WORK);
        sb.append("cache/item-models-new");
        File file = new File(sb.toString());
        if (!file.exists()) {
            file.mkdirs();
        }
        if (names == null) {
            return;
        }
        String optString;
        Object opt;
        String[] split;
        int n;
        StringBuilder sb2;
        JSONArray jsonArray;
        File file2;
        JSONObject jsonObject;
        JSONArray jsonArray2;
        File file3;
        int int1;
        int n2;
        StringBuilder sb3;
        StringBuilder sb4;
        String string;
        JSONArray jsonArray3;
        GuiBlockModel guiBlockModel;
        boolean b;
        int j;
        Object opt2;
        JSONObject jsonObject2;
        Object opt3;
        boolean b2;
        JSONArray jsonArray5 = null;
        JSONArray jsonArray6;
        ArrayList<Pair<String, Integer>> list;
        Object opt4;
        int optInt;
        GuiBlockModel.VanillaRenderType for1;
        GuiBlockModel buildModel;
        JSONArray optJSONArray;
        BlockShape blockShape;
        GuiBlockModel.Box box;
        Pair<String, Integer> pair;
        StringBuilder sb5;
        Object opt5;
        NativeItemModel for2;
        String blockIconPath;
        File file4;
        JSONArray jsonArray7;
        NativeItemModel for3;
        Label_1159:Label_1079_Outer:
        for (int i = 0; i < names.length(); ++i, loadIconJSON = jsonObject, names = jsonArray2, file = file3) {
            optString = names.optString(i);
            if (optString == null) {
                return;
            }
            opt = loadIconJSON.opt(optString);
            split = optString.split(":");
            try {
                n = Integer.parseInt(split[0]);
            }
            catch (NumberFormatException ex2) {
                n = IDRegistry.getIDByName(split[0]);
                if (n == 0) {
                    sb2 = new StringBuilder();
                    sb2.append("ItemIconLoader: unrecognized block string id: ");
                    sb2.append(split[0]);
                    ICLog.i("WARNING", sb2.toString());
                    jsonArray = names;
                    file2 = file;
                    jsonObject = loadIconJSON;
                    jsonArray2 = jsonArray;
                    file3 = file2;
                    continue;
                }
            }
            if (split.length > 1) {
                int1 = Integer.parseInt(split[1]);
            }
            else {
                int1 = 0;
            }
            n2 = n;
            if (n > 255) {
                n2 = 255 - n;
            }
            sb3 = new StringBuilder();
            sb3.append(n2);
            if (split.length > 1) {
                sb4 = new StringBuilder();
                sb4.append(":");
                sb4.append(split[1]);
                string = sb4.toString();
            }
            else {
                string = "";
            }
            sb3.append(string);
            sb3.toString();
            Label_1147: {
                if (opt != null) {
                    if (opt instanceof JSONArray) {
                        jsonArray3 = (JSONArray)opt;
                    }
                    else {
                        if (!(opt instanceof JSONObject)) {
                            break Label_1147;
                        }
                        jsonArray3 = new JSONArray();
                        try {
                            jsonArray3.put(0, opt);
                        }
                        catch (JSONException ex3) {}
                    }
                    guiBlockModel = new GuiBlockModel();
                    b = false;
                    j = 0;
                Label_1079:
                    while (true) {
                        for (JSONArray jsonArray4 = jsonArray3; j < jsonArray4.length(); ++j) {
                            opt2 = jsonArray4.opt(j);
                            if (opt2 instanceof JSONObject) {
                                jsonObject2 = (JSONObject)opt2;
                                opt3 = jsonObject2.opt("tex");
                                if (opt3 != null) {
                                    b2 = (opt3 instanceof String || (opt3 instanceof JSONArray && ((JSONArray)opt3).length() == 2));
                                    if (b2 || opt3 instanceof JSONArray) {
                                        Label_0534: {
                                            if (b2) {
                                                jsonArray5 = new JSONArray();
                                                while (true) {
                                                    try {
                                                        jsonArray5.put(0, opt3);
                                                        break Label_0534;
                                                    }
                                                    catch (JSONException ex4) {
                                                        continue Label_1079_Outer;
                                                    }
                                                    break;
                                                }
                                            }
                                            jsonArray5 = (JSONArray)opt3;
                                        }
                                        if (jsonArray5.length() != 0) {
                                            if (jsonArray5.length() == 3) {
                                                jsonArray6 = new JSONArray();
                                                try {
                                                    jsonArray6.put(0, jsonArray5.opt(2));
                                                    jsonArray6.put(1, jsonArray5.opt(0));
                                                    jsonArray6.put(2, jsonArray5.opt(1));
                                                    jsonArray6.put(3, jsonArray5.opt(1));
                                                    jsonArray6.put(4, jsonArray5.opt(1));
                                                    jsonArray6.put(5, jsonArray5.opt(1));
                                                    jsonArray5 = jsonArray6;
                                                }
                                                catch (JSONException ex) {
                                                    ex.printStackTrace();
                                                    jsonArray5 = jsonArray6;
                                                }
                                            }
                                            list = new ArrayList<Pair<String, Integer>>();
                                            for (int k = 0; k < Math.min(6, jsonArray5.length()); ++k) {
                                                opt4 = jsonArray5.opt(k);
                                                if (opt4 != null) {
                                                    list.add(getBlockIconName(opt4));
                                                }
                                            }
                                            optInt = jsonObject2.optInt("render", jsonObject2.optInt("rendertype", -1));
                                            if (optInt != -1) {
                                                for1 = GuiBlockModel.VanillaRenderType.getFor(optInt);
                                                if (for1 != null) {
                                                    buildModel = for1.buildModelFor(list);
                                                    break Label_1079;
                                                }
                                            }
                                            optJSONArray = jsonObject2.optJSONArray("shape");
                                            if (optJSONArray != null) {
                                                blockShape = new BlockShape((float)optJSONArray.optDouble(0), (float)optJSONArray.optDouble(1), (float)optJSONArray.optDouble(2), (float)optJSONArray.optDouble(3), (float)optJSONArray.optDouble(4), (float)optJSONArray.optDouble(5));
                                            }
                                            else {
                                                blockShape = new BlockShape();
                                            }
                                            box = new GuiBlockModel.Box(blockShape);
                                            for (int l = 0; l < Math.min(6, list.size()); ++l) {
                                                pair = list.get(l);
                                                if (getBlockIconPath(pair) != null) {
                                                    box.addTexture(pair);
                                                }
                                                else {
                                                    sb5 = new StringBuilder();
                                                    sb5.append("missing texture: ");
                                                    sb5.append((String)pair.first);
                                                    sb5.append(" ");
                                                    sb5.append(pair.second);
                                                    ICLog.i("WARNING", sb5.toString());
                                                    box.addTexturePath(null);
                                                }
                                            }
                                            box.setRenderAllSides(false);
                                            guiBlockModel.addBox(box);
                                        }
                                    }
                                    continue;
                                }
                                opt5 = jsonObject2.opt("sprite");
                                if (opt5 == null) {
                                    continue;
                                }
                                for2 = NativeItemModel.getFor(n2, int1);
                                blockIconPath = getBlockIconPath(getBlockIconName(opt5));
                                if (blockIconPath != null) {
                                    for2.setItemTexturePath(blockIconPath);
                                }
                                b = true;
                                buildModel = guiBlockModel;
                                file4 = file;
                                jsonArray7 = names;
                                jsonObject = loadIconJSON;
                                jsonArray2 = jsonArray7;
                                file3 = file4;
                                if (!b) {
                                    for3 = NativeItemModel.getFor(n2, int1);
                                    for3.setUiBlockModel(buildModel);
                                    for3.setWorldBlockModel(buildModel);
                                    for3.setCacheKey("vanilla");
                                    jsonObject = loadIconJSON;
                                    jsonArray2 = jsonArray7;
                                    file3 = file4;
                                }
                                continue Label_1159;
                            }
                        }
                        buildModel = guiBlockModel;
                        continue Label_1079;
                    }
                }
            }
            file3 = file;
            jsonArray2 = names;
            jsonObject = loadIconJSON;
        }
    }
    
    private static JSONObject loadIconJSON(final String s) {
        try {
            final StringBuilder sb = new StringBuilder();
            sb.append("innercore/icons/");
            sb.append(s);
            return new JSONObject(FileTools.getAssetAsString(sb.toString()));
        }
        catch (JSONException ex) {
            ex.printStackTrace();
            return new JSONObject();
        }
    }
    
    private static void loadItems() {
        final JSONObject loadIconJSON = loadIconJSON("item_textures.json");
        final JSONArray names = loadIconJSON.names();
        if (names == null) {
            return;
        }
        for (int i = 0; i < names.length(); ++i) {
            final String optString = names.optString(i);
            if (optString == null) {
                return;
            }
            final String[] split = optString.split(":");
            int n;
            try {
                n = Integer.parseInt(split[0]);
            }
            catch (NumberFormatException ex) {
                n = IDRegistry.getIDByName(split[0]);
                if (n == 0) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("ItemIconLoader: unrecognized item string id: ");
                    sb.append(split[0]);
                    ICLog.i("WARNING", sb.toString());
                    continue;
                }
            }
            int int1;
            if (split.length > 1) {
                int1 = Integer.parseInt(split[1]);
            }
            else {
                int1 = 0;
            }
            final Object opt = loadIconJSON.opt(optString);
            if (opt != null) {
                final String itemIconPath = getItemIconPath(opt);
                if (itemIconPath != null) {
                    NativeItemModel.getFor(n, int1).setItemTexturePath(itemIconPath);
                }
            }
        }
    }
    
    private static void registerIcon(String s, final Object o) {
        final String[] split = s.split(":");
        try {
            final int intValue = Integer.valueOf(split[0]);
            int intValue2 = 0;
            if (split.length == 2) {
                intValue2 = Integer.valueOf(split[1]);
            }
            if (o instanceof String) {
                s = (String)o;
                ItemIconSource.instance.registerIcon(intValue, intValue2, s);
            }
            if (o instanceof Bitmap) {
                ItemIconSource.instance.registerIcon(intValue, intValue2, (Bitmap)o);
            }
        }
        catch (NumberFormatException ex) {
            ex.printStackTrace();
        }
    }
}
