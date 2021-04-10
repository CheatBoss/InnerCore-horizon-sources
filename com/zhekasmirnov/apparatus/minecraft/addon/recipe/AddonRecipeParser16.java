package com.zhekasmirnov.apparatus.minecraft.addon.recipe;

import android.util.*;
import java.util.*;
import org.json.*;

public class AddonRecipeParser16 extends AddonRecipeParser
{
    private final Map<Integer, String> dyeConversion;
    
    public AddonRecipeParser16() {
        (this.dyeConversion = new HashMap<Integer, String>()).put(0, "black_dye");
        this.dyeConversion.put(1, "red_dye");
        this.dyeConversion.put(2, "green_dye");
        this.dyeConversion.put(3, "cocoa_beans");
        this.dyeConversion.put(4, "lapis_lazuli");
        this.dyeConversion.put(5, "purple_dye");
        this.dyeConversion.put(6, "cyan_dye");
        this.dyeConversion.put(7, "light_gray_dye");
        this.dyeConversion.put(8, "gray_dye");
        this.dyeConversion.put(9, "pink_dye");
        this.dyeConversion.put(10, "lime_dye");
        this.dyeConversion.put(11, "yellow_dye");
        this.dyeConversion.put(12, "light_blue_dye");
        this.dyeConversion.put(13, "magenta_dye");
        this.dyeConversion.put(14, "orange_dye");
        this.dyeConversion.put(15, "bone_meal");
        this.dyeConversion.put(16, "black_dye");
        this.dyeConversion.put(17, "brown_dye");
        this.dyeConversion.put(18, "blue_dye");
        this.dyeConversion.put(19, "white_dye");
    }
    
    @Override
    public Pair<Integer, Integer> getIdAndDataFromItemString(final String s, final int n) {
        if (!"dye".equalsIgnoreCase(s)) {
            final String s2 = s;
            final int n2 = n;
            if (!"minecraft:dye".equalsIgnoreCase(s)) {
                return super.getIdAndDataFromItemString(s2, n2);
            }
        }
        final String s2 = this.dyeConversion.get(n);
        if (s2 == null) {
            return (Pair<Integer, Integer>)new Pair((Object)0, (Object)0);
        }
        final int n2 = 0;
        return super.getIdAndDataFromItemString(s2, n2);
    }
    
    @Override
    public List<ParsedRecipe> parse(final JSONObject jsonObject) {
        final ArrayList<ParsedRecipe> list = new ArrayList<ParsedRecipe>();
        final Iterator keys = jsonObject.keys();
        while (keys.hasNext()) {
            final String s = keys.next();
            final JSONObject optJSONObject = jsonObject.optJSONObject(s);
            if (optJSONObject != null) {
                final ArrayList<String> list2 = new ArrayList<String>();
                String optString = null;
                final JSONObject optJSONObject2 = optJSONObject.optJSONObject("description");
                if (optJSONObject2 != null) {
                    optString = optJSONObject2.optString("identifier");
                }
                final JSONArray optJSONArray = optJSONObject.optJSONArray("tags");
                if (optJSONArray != null) {
                    for (int i = 0; i < optJSONArray.length(); ++i) {
                        list2.add(optJSONArray.optString(i));
                    }
                }
                list.add(new ParsedRecipe(optString, s, list2, optJSONObject));
            }
        }
        return list;
    }
}
