package com.zhekasmirnov.innercore.api.unlimited;

import com.zhekasmirnov.innercore.api.mod.*;
import com.zhekasmirnov.apparatus.minecraft.version.*;
import org.mozilla.javascript.*;
import com.zhekasmirnov.innercore.api.log.*;
import org.mozilla.javascript.annotations.*;
import org.json.*;
import com.zhekasmirnov.innercore.api.runtime.other.*;
import java.util.*;
import com.zhekasmirnov.apparatus.multiplayer.mod.*;

public class IDRegistry
{
    public static final int BLOCK_ID_OFFSET = 8192;
    public static final int ITEM_ID_OFFSET = 2048;
    public static final int MAX_ID = 65536;
    private static final int MAX_UNAPPROVED_IDS = 750;
    private static final String TYPE_BLOCK = "block";
    private static final String TYPE_ITEM = "item";
    private static HashMap<String, Boolean> approvedIds;
    private static int blockIdIterator;
    private static boolean blockIdLooped;
    private static HashMap<String, Integer> blockIdShortcut;
    private static ScriptableObject blockIds;
    private static int itemIdIterator;
    private static boolean itemIdLooped;
    private static HashMap<String, Integer> itemIdShortcut;
    private static ScriptableObject itemIds;
    private static HashMap<Integer, String> nameById;
    private static LinkedList<String> unapprovedBlocks;
    private static int unapprovedIds;
    private static LinkedList<String> unapprovedItems;
    private static ScriptableObject vanillaBlockIds;
    private static HashMap<String, Integer> vanillaIdShortcut;
    private static ScriptableObject vanillaItemIds;
    private static HashMap<Integer, String> vanillaNameById;
    private static HashMap<Integer, String> vanillaTileById;
    private static ScriptableObject vanillaTileIds;
    
    static {
        while (true) {
            IDRegistry.unapprovedIds = 0;
            IDRegistry.nameById = new HashMap<Integer, String>();
            IDRegistry.vanillaNameById = new HashMap<Integer, String>();
            IDRegistry.vanillaTileById = new HashMap<Integer, String>();
            IDRegistry.approvedIds = new HashMap<String, Boolean>();
            IDRegistry.itemIdShortcut = new HashMap<String, Integer>();
            IDRegistry.blockIdShortcut = new HashMap<String, Integer>();
            IDRegistry.vanillaIdShortcut = new HashMap<String, Integer>();
            IDRegistry.itemIds = ScriptableObjectHelper.createEmpty();
            IDRegistry.blockIds = ScriptableObjectHelper.createEmpty();
            IDRegistry.vanillaItemIds = ScriptableObjectHelper.createEmpty();
            IDRegistry.vanillaBlockIds = ScriptableObjectHelper.createEmpty();
            IDRegistry.vanillaTileIds = ScriptableObjectHelper.createEmpty();
            while (true) {
                Label_0500: {
                    try {
                        final Map<String, Map<String, Integer>> loadScopedIdMapFromAssets = VanillaIdConversionMap.getSingleton().loadScopedIdMapFromAssets();
                        final Map<String, Integer> map = loadScopedIdMapFromAssets.get("blocks");
                        final Map<String, Integer> map2 = loadScopedIdMapFromAssets.get("items");
                        if (map2 != null) {
                            final Iterator<Map.Entry<String, Integer>> iterator = map2.entrySet().iterator();
                            if (iterator.hasNext()) {
                                final Map.Entry<String, Integer> entry = iterator.next();
                                final String s = entry.getKey();
                                final int intValue = entry.getValue();
                                IDRegistry.vanillaNameById.put(intValue, s);
                                IDRegistry.vanillaIdShortcut.put(s, intValue);
                                if (map != null && map.containsKey(s)) {
                                    IDRegistry.vanillaBlockIds.put(s, (Scriptable)IDRegistry.vanillaBlockIds, (Object)intValue);
                                    break Label_0500;
                                }
                                IDRegistry.vanillaItemIds.put(s, (Scriptable)IDRegistry.vanillaItemIds, (Object)intValue);
                                break Label_0500;
                            }
                        }
                        if (map != null) {
                            for (final Map.Entry<String, Integer> entry2 : map.entrySet()) {
                                final String s2 = entry2.getKey();
                                final int intValue2 = entry2.getValue();
                                IDRegistry.vanillaTileIds.put(s2, (Scriptable)IDRegistry.vanillaTileIds, (Object)intValue2);
                                IDRegistry.vanillaTileById.put(intValue2, s2);
                                int n;
                                if ((n = intValue2) > 255) {
                                    n = 255 - intValue2;
                                }
                                if (!IDRegistry.vanillaBlockIds.has(s2, (Scriptable)IDRegistry.vanillaBlockIds) && !IDRegistry.vanillaNameById.containsKey(n)) {
                                    IDRegistry.vanillaBlockIds.put(s2, (Scriptable)IDRegistry.vanillaBlockIds, (Object)n);
                                    IDRegistry.vanillaNameById.put(n, s2);
                                    IDRegistry.vanillaIdShortcut.put(s2, n);
                                }
                            }
                        }
                    }
                    catch (Exception ex) {
                        ICLog.e("INNERCORE-BLOCKS", "Unable to read vanilla numeric IDs", ex);
                    }
                    break;
                }
                continue;
            }
        }
        IDRegistry.unapprovedBlocks = new LinkedList<String>();
        IDRegistry.blockIdIterator = 8192;
        IDRegistry.blockIdLooped = false;
        IDRegistry.unapprovedItems = new LinkedList<String>();
        IDRegistry.itemIdIterator = 2048;
        IDRegistry.itemIdLooped = false;
    }
    
    static void approve(final String s, final String s2) {
        final HashMap<String, Boolean> approvedIds = IDRegistry.approvedIds;
        final StringBuilder sb = new StringBuilder();
        sb.append(s2);
        sb.append("$");
        sb.append(s);
        approvedIds.put(sb.toString(), true);
    }
    
    @JSStaticFunction
    public static int ensureBlockId(final int n) {
        if ((IDRegistry.vanillaNameById.containsKey(n) || IDRegistry.vanillaTileById.containsKey(n + 255)) && n < 0) {
            return n + 255;
        }
        return n;
    }
    
    @JSStaticFunction
    public static int ensureItemId(final int n) {
        if (IDRegistry.vanillaTileById.containsKey(n) && n > 255) {
            return 255 - n;
        }
        return n;
    }
    
    static void fromJson(JSONObject optJSONObject) {
        final JSONObject optJSONObject2 = optJSONObject.optJSONObject("blocks");
        optJSONObject = optJSONObject.optJSONObject("items");
        final int n = 0;
        while (true) {
            if (optJSONObject2 != null) {
                while (true) {
                    try {
                        final JSONArray names = optJSONObject2.names();
                        if (names != null) {
                            for (int i = 0; i < names.length(); ++i) {
                                final String optString = names.optString(i);
                                if (optString != null) {
                                    putId(optString, optJSONObject2.optInt(optString));
                                }
                            }
                        }
                        if (optJSONObject != null) {
                            final JSONArray names2 = optJSONObject.names();
                            if (names2 != null) {
                                for (int j = n; j < names2.length(); ++j) {
                                    final String optString2 = names2.optString(j);
                                    if (optString2 != null) {
                                        putId(optString2, optJSONObject.optInt(optString2));
                                    }
                                }
                            }
                        }
                        return;
                        final Exception ex;
                        ICLog.e("INNERCORE-BLOCKS", "failed to load string id bindings", ex);
                        return;
                    }
                    catch (Exception ex2) {}
                    final Exception ex2;
                    final Exception ex = ex2;
                    continue;
                }
            }
            continue;
        }
    }
    
    @JSStaticFunction
    public static int genBlockID(final String s) {
        if (!NameTranslation.isAscii(s)) {
            final StringBuilder sb = new StringBuilder();
            sb.append("block string id ");
            sb.append(s);
            sb.append(" contains unicode characters, it will not be created");
            ICLog.e("INNERCORE-BLOCKS", sb.toString(), new RuntimeException());
            return 0;
        }
        final Collection<String> values = IDRegistry.vanillaNameById.values();
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("block_");
        sb2.append(s);
        if (values.contains(sb2.toString()) || IDRegistry.vanillaTileById.values().contains(s)) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("block string id ");
            sb3.append(s);
            sb3.append(" is a vanilla string ID, so the item won't be created");
            ICLog.e("INNERCORE-BLOCKS", sb3.toString(), new RuntimeException());
            return 0;
        }
        approve(s, "block");
        if (IDRegistry.blockIdShortcut.containsKey(s)) {
            return IDRegistry.blockIdShortcut.get(s);
        }
        while (isOccupied(IDRegistry.blockIdIterator)) {
            ++IDRegistry.blockIdIterator;
            if (IDRegistry.blockIdIterator > 65536) {
                if (IDRegistry.blockIdLooped) {
                    final StringBuilder sb4 = new StringBuilder();
                    sb4.append("ID LIMIT EXCEEDED while registring block string id ");
                    sb4.append(s);
                    throw new RuntimeException(sb4.toString());
                }
                IDRegistry.blockIdLooped = true;
                IDRegistry.blockIdIterator = 0;
            }
        }
        putId(s, IDRegistry.blockIdIterator);
        final int blockIdIterator = IDRegistry.blockIdIterator;
        IDRegistry.blockIdIterator = blockIdIterator + 1;
        return blockIdIterator;
    }
    
    @JSStaticFunction
    public static int genItemID(final String s) {
        if (!NameTranslation.isAscii(s)) {
            final StringBuilder sb = new StringBuilder();
            sb.append("item string id ");
            sb.append(s);
            sb.append(" contains unicode characters, it will not be created");
            ICLog.e("INNERCORE-BLOCKS", sb.toString(), new RuntimeException());
            return 0;
        }
        final Collection<String> values = IDRegistry.vanillaNameById.values();
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("item_");
        sb2.append(s);
        if (values.contains(sb2.toString()) || IDRegistry.vanillaTileById.values().contains(s)) {
            final StringBuilder sb3 = new StringBuilder();
            sb3.append("item string id ");
            sb3.append(s);
            sb3.append(" is a vanilla string ID, so the item won't be created");
            ICLog.e("INNERCORE-BLOCKS", sb3.toString(), new RuntimeException());
            return 0;
        }
        approve(s, "item");
        if (IDRegistry.itemIdShortcut.containsKey(s)) {
            return IDRegistry.itemIdShortcut.get(s);
        }
        while (isOccupied(IDRegistry.itemIdIterator)) {
            ++IDRegistry.itemIdIterator;
            if (IDRegistry.itemIdIterator > 65536) {
                if (IDRegistry.itemIdLooped) {
                    final StringBuilder sb4 = new StringBuilder();
                    sb4.append("ID LIMIT EXCEEDED while registring item string id ");
                    sb4.append(s);
                    throw new RuntimeException(sb4.toString());
                }
                IDRegistry.itemIdLooped = true;
                IDRegistry.itemIdIterator = 0;
            }
        }
        putId(s, IDRegistry.itemIdIterator);
        final int itemIdIterator = IDRegistry.itemIdIterator;
        IDRegistry.itemIdIterator = itemIdIterator + 1;
        return itemIdIterator;
    }
    
    public static int getIDByName(final String s) {
        if (IDRegistry.vanillaIdShortcut.containsKey(s)) {
            return IDRegistry.vanillaIdShortcut.get(s);
        }
        return 0;
    }
    
    @JSStaticFunction
    public static String getNameByID(final int n) {
        return IDRegistry.nameById.get(n);
    }
    
    public static void injectAPI(final ScriptableObject scriptableObject) {
        scriptableObject.put("BlockID", (Scriptable)scriptableObject, (Object)IDRegistry.blockIds);
        scriptableObject.put("ItemID", (Scriptable)scriptableObject, (Object)IDRegistry.itemIds);
        scriptableObject.put("VanillaItemID", (Scriptable)scriptableObject, (Object)IDRegistry.vanillaItemIds);
        scriptableObject.put("VanillaBlockID", (Scriptable)scriptableObject, (Object)IDRegistry.vanillaBlockIds);
        scriptableObject.put("VanillaTileID", (Scriptable)scriptableObject, (Object)IDRegistry.vanillaTileIds);
    }
    
    static boolean isApproved(final String s, final String s2) {
        final HashMap<String, Boolean> approvedIds = IDRegistry.approvedIds;
        final StringBuilder sb = new StringBuilder();
        sb.append(s2);
        sb.append("$");
        sb.append(s);
        return approvedIds.containsKey(sb.toString());
    }
    
    static boolean isOccupied(final int n) {
        return IDRegistry.nameById.containsKey(n) || IDRegistry.vanillaNameById.containsKey(n) || IDRegistry.vanillaTileById.containsKey(n);
    }
    
    public static boolean isVanilla(final int n) {
        return n == 0 || IDRegistry.vanillaNameById.containsKey(n) || IDRegistry.vanillaTileById.containsKey(n);
    }
    
    static void putId(final String s, final int n) {
        if (n >= 8192) {
            IDRegistry.blockIdShortcut.put(s, n);
            IDRegistry.blockIds.put(s, (Scriptable)IDRegistry.blockIds, (Object)n);
        }
        else if (n >= 2048) {
            IDRegistry.itemIdShortcut.put(s, n);
            IDRegistry.itemIds.put(s, (Scriptable)IDRegistry.itemIds, (Object)n);
        }
        IDRegistry.nameById.put(n, s);
    }
    
    public static void rebuildNetworkIdMap() {
        final IdConversionMap singleton = IdConversionMap.getSingleton();
        singleton.registerIdsFromMap("item", IDRegistry.itemIdShortcut);
        singleton.registerIdsFromMap("block", IDRegistry.blockIdShortcut);
    }
    
    static JSONObject toJson() {
    Label_0113_Outer:
        while (true) {
            while (true) {
            Label_0294:
                while (true) {
                    Label_0291: {
                        try {
                            final JSONObject jsonObject = new JSONObject();
                            final JSONObject jsonObject2 = new JSONObject();
                            final JSONObject jsonObject3 = new JSONObject();
                            final Iterator<String> iterator = IDRegistry.blockIdShortcut.keySet().iterator();
                            if (iterator.hasNext()) {
                                final String s = iterator.next();
                                jsonObject2.put(s, (Object)IDRegistry.blockIdShortcut.get(s));
                                if (!isApproved(s, "block")) {
                                    IDRegistry.unapprovedItems.add(s);
                                    ++IDRegistry.unapprovedIds;
                                    break Label_0291;
                                }
                                break Label_0291;
                            }
                            else {
                                final Iterator<String> iterator2 = IDRegistry.itemIdShortcut.keySet().iterator();
                                if (!iterator2.hasNext()) {
                                    if (IDRegistry.unapprovedIds > 750) {
                                        ICLog.d("INNERCORE-BLOCKS", "too many unused IDs, clearing...");
                                        final Iterator<String> iterator3 = IDRegistry.unapprovedItems.iterator();
                                        while (iterator3.hasNext()) {
                                            jsonObject3.remove((String)iterator3.next());
                                        }
                                        final Iterator<String> iterator4 = IDRegistry.unapprovedBlocks.iterator();
                                        while (iterator4.hasNext()) {
                                            jsonObject2.remove((String)iterator4.next());
                                        }
                                    }
                                    jsonObject.put("blocks", (Object)jsonObject2);
                                    jsonObject.put("items", (Object)jsonObject3);
                                    return jsonObject;
                                }
                                final String s2 = iterator2.next();
                                jsonObject3.put(s2, (Object)IDRegistry.itemIdShortcut.get(s2));
                                if (!isApproved(s2, "item")) {
                                    IDRegistry.unapprovedBlocks.add(s2);
                                    ++IDRegistry.unapprovedIds;
                                    break Label_0294;
                                }
                                break Label_0294;
                            }
                        }
                        catch (Exception ex) {
                            ICLog.e("INNERCORE-BLOCKS", "failed to save string id bindings", ex);
                            return null;
                        }
                    }
                    continue Label_0113_Outer;
                }
                continue;
            }
        }
    }
}
