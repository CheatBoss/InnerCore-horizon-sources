package com.zhekasmirnov.innercore.api.mod.recipes.furnace;

import com.zhekasmirnov.innercore.api.mod.recipes.workbench.*;
import com.zhekasmirnov.innercore.api.*;
import java.util.*;
import com.zhekasmirnov.innercore.api.commontypes.*;
import com.zhekasmirnov.innercore.utils.*;
import com.zhekasmirnov.innercore.api.log.*;
import java.io.*;
import org.json.*;

public class FurnaceRecipeRegistry
{
    private static HashMap<Long, Integer> fuel;
    private static boolean nativeRecipesLoaded;
    private static HashMap<Long, FurnaceRecipe> recipes;
    
    static {
        FurnaceRecipeRegistry.recipes = new HashMap<Long, FurnaceRecipe>();
        FurnaceRecipeRegistry.fuel = new HashMap<Long, Integer>();
        FurnaceRecipeRegistry.nativeRecipesLoaded = false;
    }
    
    public static void addFuel(final int n, final int n2, final int n3) {
        FurnaceRecipeRegistry.fuel.put(RecipeEntry.getCodeByItem(n, n2), n3);
        NativeFurnaceRegistry.nativeAddFuel(n, n2, n3);
    }
    
    public static void addFurnaceRecipe(final int n, final int n2, final int n3, final int n4, final String s) {
        String prefix = s;
        Label_0034: {
            if (s != null) {
                if (!s.isEmpty()) {
                    prefix = s;
                    if (!s.equals("undefined")) {
                        break Label_0034;
                    }
                }
                prefix = null;
            }
        }
        final FurnaceRecipe furnaceRecipe = new FurnaceRecipe(n, n2, n3, n4);
        furnaceRecipe.setPrefix(prefix);
        addFurnaceRecipe(furnaceRecipe);
    }
    
    public static void addFurnaceRecipe(final FurnaceRecipe furnaceRecipe) {
        if (!furnaceRecipe.isValid()) {
            return;
        }
        final long inputKey = furnaceRecipe.getInputKey();
        if (furnaceRecipe.getPrefix() == null) {
            NativeFurnaceRegistry.nativeAddRecipe(furnaceRecipe.inId, furnaceRecipe.inData, furnaceRecipe.resId, furnaceRecipe.resData);
        }
        else if (FurnaceRecipeRegistry.recipes.containsKey(inputKey)) {
            NativeFurnaceRegistry.nativeRemoveRecipe(furnaceRecipe.inId, furnaceRecipe.inData);
        }
        furnaceRecipe.setPrefix(furnaceRecipe.getPrefix());
        FurnaceRecipeRegistry.recipes.put(inputKey, furnaceRecipe);
    }
    
    public static int getBurnDuration(final int n, final int n2) {
        final long codeByItem = RecipeEntry.getCodeByItem(n, n2);
        if (FurnaceRecipeRegistry.fuel.containsKey(codeByItem)) {
            return FurnaceRecipeRegistry.fuel.get(codeByItem);
        }
        final long codeByItem2 = RecipeEntry.getCodeByItem(n, -1);
        if (FurnaceRecipeRegistry.fuel.containsKey(codeByItem2)) {
            return FurnaceRecipeRegistry.fuel.get(codeByItem2);
        }
        return 0;
    }
    
    public static Collection<FurnaceRecipe> getFurnaceRecipeByResult(final int n, final int n2, final String s) {
        final Collection<FurnaceRecipe> values = FurnaceRecipeRegistry.recipes.values();
        final ArrayList<FurnaceRecipe> list = new ArrayList<FurnaceRecipe>();
        for (final FurnaceRecipe furnaceRecipe : values) {
            final ItemInstance result = furnaceRecipe.getResult();
            if ((result.getId() == n && (n2 == -1 || result.getData() == n2)) || furnaceRecipe.isMatchingPrefix(s)) {
                list.add(furnaceRecipe);
            }
        }
        return list;
    }
    
    public static FurnaceRecipe getRecipe(final int n, final int n2, final String s) {
        if (s == null || s.isEmpty() || s.equals("undefined")) {}
        final long codeByItem = RecipeEntry.getCodeByItem(n, n2);
        if (FurnaceRecipeRegistry.recipes.containsKey(codeByItem)) {
            return FurnaceRecipeRegistry.recipes.get(codeByItem);
        }
        final long codeByItem2 = RecipeEntry.getCodeByItem(n, -1);
        if (FurnaceRecipeRegistry.recipes.containsKey(codeByItem2)) {
            return FurnaceRecipeRegistry.recipes.get(codeByItem2);
        }
        return null;
    }
    
    public static void loadNativeRecipesIfNeeded() {
        if (FurnaceRecipeRegistry.nativeRecipesLoaded) {
            return;
        }
        try {
            final StringBuilder sb = new StringBuilder();
            sb.append(FileTools.DIR_WORK);
            sb.append("furnace.json");
            FileTools.unpackAsset("innercore/recipes/furnace_fuel.json", sb.toString());
            try {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append(FileTools.DIR_WORK);
                sb2.append("furnace.json");
                final JSONObject json = FileTools.readJSON(sb2.toString());
                final JSONArray names = json.names();
                if (names != null) {
                    for (int i = 0; i < names.length(); ++i) {
                        final String optString = names.optString(i);
                        final String[] split = optString.split(":");
                        split[0] = split[0].replace("_", "-");
                        int intValue = -1;
                        int n;
                        if (split.length > 1) {
                            n = Integer.valueOf(split[0]);
                            intValue = Integer.valueOf(split[1]);
                        }
                        else {
                            n = Integer.valueOf(split[0]);
                        }
                        final int optInt = json.optInt(optString);
                        if (optInt > 0) {
                            addFuel(n, intValue, optInt);
                        }
                    }
                }
                FurnaceRecipeRegistry.nativeRecipesLoaded = true;
            }
            catch (Exception ex) {
                ICLog.e("RECIPES", "failed to load recipes", ex);
            }
        }
        catch (IOException ex2) {
            ICLog.e("RECIPES", "failed to unpack recipes", ex2);
        }
    }
    
    public static void removeFuel(final int n, final int n2) {
        final long codeByItem = RecipeEntry.getCodeByItem(n, n2);
        if (FurnaceRecipeRegistry.fuel.containsKey(codeByItem)) {
            NativeFurnaceRegistry.nativeRemoveFuel(n, n2);
        }
        FurnaceRecipeRegistry.fuel.remove(codeByItem);
    }
    
    public static void removeFurnaceRecipe(final int n, final int n2) {
        final long codeByItem = RecipeEntry.getCodeByItem(n, n2);
        if (FurnaceRecipeRegistry.recipes.containsKey(codeByItem)) {
            if (FurnaceRecipeRegistry.recipes.get(codeByItem).getPrefix() == null) {
                NativeFurnaceRegistry.nativeRemoveRecipe(n, n2);
            }
            FurnaceRecipeRegistry.recipes.remove(codeByItem);
        }
    }
}
