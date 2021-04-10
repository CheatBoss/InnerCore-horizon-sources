package com.zhekasmirnov.innercore.api.mod.recipes;

import com.zhekasmirnov.innercore.api.unlimited.*;
import com.zhekasmirnov.innercore.utils.*;
import com.zhekasmirnov.innercore.api.log.*;
import org.json.*;
import com.zhekasmirnov.apparatus.minecraft.version.*;
import com.zhekasmirnov.apparatus.minecraft.addon.*;
import com.zhekasmirnov.apparatus.minecraft.addon.recipe.*;
import java.util.*;
import com.zhekasmirnov.innercore.api.mod.recipes.workbench.*;
import com.zhekasmirnov.innercore.api.mod.recipes.furnace.*;

public class RecipeLoader
{
    private List<String> JSONArrayToList(final JSONArray jsonArray) {
        final ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < jsonArray.length(); ++i) {
            list.add(jsonArray.optString(i));
        }
        return list;
    }
    
    public static IDDataPair getIdData(final String s) {
        return getIdData(s, -1);
    }
    
    public static IDDataPair getIdData(String s, int int1) {
        final String[] split = s.split(":");
        if (split.length == 1) {
            s = split[0];
        }
        else {
            s = split[1];
        }
        final int idByName = IDRegistry.getIDByName(s.toLowerCase());
        if (idByName == 0) {
            return null;
        }
        if (split.length == 3) {
            int1 = Integer.parseInt(split[2]);
        }
        return new IDDataPair(idByName, int1);
    }
    
    private void loadLegacy() {
    Label_0096_Outer:
        while (true) {
        Label_0093_Outer:
            while (true) {
            Label_0110_Outer:
                while (true) {
                    while (true) {
                        int n = 0;
                        Label_0190: {
                            try {
                                final JSONArray assetAsJSONArray = FileTools.getAssetAsJSONArray("innercore/recipes/workbench_recipes.json");
                                int i = 0;
                                while (true) {
                                    String optString;
                                    while (true) {
                                        while (i < assetAsJSONArray.length()) {
                                            final JSONObject jsonObject = assetAsJSONArray.getJSONObject(i);
                                            optString = jsonObject.optString("type");
                                            if (optString != null) {
                                                n = -1;
                                                final int hashCode = optString.hashCode();
                                                if (hashCode != -1638582086) {
                                                    if (hashCode != -903568157) {
                                                        break Label_0190;
                                                    }
                                                    if (optString.equals("shaped")) {
                                                        n = 0;
                                                    }
                                                    break Label_0190;
                                                }
                                                else {
                                                    if (optString.equals("shapeless")) {
                                                        n = 1;
                                                    }
                                                    break Label_0190;
                                                }
                                            }
                                            else {
                                                ++i;
                                            }
                                        }
                                        return;
                                        JSONObject jsonObject = null;
                                        final WorkbenchRecipeLegacy workbenchRecipeLegacy = new WorkbenchShapelessRecipeLegacy(jsonObject);
                                        WorkbenchRecipeRegistry.addRecipe(workbenchRecipeLegacy.getRecipe());
                                        continue Label_0096_Outer;
                                    }
                                    final StringBuilder sb = new StringBuilder();
                                    sb.append("Invalid recipe type in workbench_recipes.json: ");
                                    sb.append(optString);
                                    throw new IllegalArgumentException(sb.toString());
                                    JSONObject jsonObject = null;
                                    final WorkbenchRecipeLegacy workbenchRecipeLegacy = new WorkbenchShapedRecipeLegacy(jsonObject);
                                    continue Label_0093_Outer;
                                }
                            }
                            catch (JSONException ex) {
                                ICLog.e("INNERCORE-RECIPES", "unable to read workbench_recipes.json", (Throwable)ex);
                                return;
                            }
                        }
                        switch (n) {
                            case 1: {
                                continue Label_0093_Outer;
                            }
                            case 0: {
                                continue;
                            }
                            default: {
                                continue Label_0110_Outer;
                            }
                        }
                        break;
                    }
                    break;
                }
                break;
            }
        }
    }
    
    public List<String> listRecipeDefinitions() {
        final ArrayList<String> list = new ArrayList<String>();
        final List<String> listRecipeDirectories = this.listRecipeDirectories();
        Collections.reverse(listRecipeDirectories);
        for (final String s : listRecipeDirectories) {
            final String[] listAssets = FileTools.listAssets(s);
            if (listAssets != null) {
                for (int length = listAssets.length, i = 0; i < length; ++i) {
                    final String s2 = listAssets[i];
                    final StringBuilder sb = new StringBuilder();
                    sb.append(s);
                    sb.append(s2);
                    list.add(sb.toString());
                }
            }
        }
        return list;
    }
    
    public List<String> listRecipeDirectories() {
        final ArrayList<String> list = new ArrayList<String>();
        list.add("definitions/recipe/");
        final String[] vanillaBehaviorPacksDirs = MinecraftVersions.getCurrent().getVanillaBehaviorPacksDirs();
        for (int length = vanillaBehaviorPacksDirs.length, i = 0; i < length; ++i) {
            final String s = vanillaBehaviorPacksDirs[i];
            final StringBuilder sb = new StringBuilder();
            sb.append(s);
            sb.append("recipes/");
            list.add(sb.toString());
        }
        return list;
    }
    
    public void load() {
        final List<String> listRecipeDefinitions = this.listRecipeDefinitions();
        final AddonRecipeParser recipeParser = AddonContext.getInstance().getRecipeParser();
        recipeParser.prepare();
        final ArrayList<AddonRecipeParser.ParsedRecipe> list = new ArrayList<AddonRecipeParser.ParsedRecipe>();
        final HashMap<String, AddonRecipeParser.ParsedRecipe> hashMap = new HashMap<String, AddonRecipeParser.ParsedRecipe>();
        for (final String s : listRecipeDefinitions) {
            try {
                final Iterator<AddonRecipeParser.ParsedRecipe> iterator2 = recipeParser.parse(FileTools.getAssetAsJSON(s)).iterator();
                while (true) {
                    Label_0144: {
                        if (!iterator2.hasNext()) {
                            break Label_0144;
                        }
                        final AddonRecipeParser.ParsedRecipe parsedRecipe = iterator2.next();
                        final String identifier = parsedRecipe.getIdentifier();
                        Label_0141: {
                            if (identifier != null) {
                                hashMap.put(identifier, parsedRecipe);
                                break Label_0141;
                            }
                            try {
                                list.add(parsedRecipe);
                                continue;
                            }
                            catch (IllegalArgumentException ex) {
                                final StringBuilder sb = new StringBuilder();
                                sb.append("unable to read recipe definition ");
                                sb.append(s);
                                ICLog.e("INNERCORE-RECIPES", sb.toString(), ex);
                            }
                        }
                    }
                }
            }
            catch (JSONException ex2) {}
            catch (IllegalArgumentException ex3) {}
            catch (NullPointerException ex4) {}
        }
        final ArrayList<AddonRecipeParser.ParsedRecipe> list2 = new ArrayList<AddonRecipeParser.ParsedRecipe>();
        list2.addAll((Collection<?>)list);
        list2.addAll((Collection<?>)hashMap.values());
        final Iterator<Object> iterator3 = list2.iterator();
        while (iterator3.hasNext()) {
        Label_0410_Outer:
            while (true) {
                final AddonRecipeParser.ParsedRecipe parsedRecipe2 = iterator3.next();
            Label_0352_Outer:
                while (true) {
                Label_0381_Outer:
                    while (true) {
                    Label_0436_Outer:
                        while (true) {
                            while (true) {
                                int n = 0;
                                Label_0485: {
                                    try {
                                        parsedRecipe2.getContents();
                                        final List<String> tags = parsedRecipe2.getTags();
                                        final String type = parsedRecipe2.getType();
                                        n = -1;
                                        final int hashCode = type.hashCode();
                                        if (hashCode != -1262046858) {
                                            if (hashCode != 1105741223) {
                                                if (hashCode != 1653409044) {
                                                    break Label_0485;
                                                }
                                                if (type.equals("minecraft:recipe_furnace")) {
                                                    n = 0;
                                                }
                                                break Label_0485;
                                            }
                                            else {
                                                if (type.equals("minecraft:recipe_shaped")) {
                                                    n = 1;
                                                }
                                                break Label_0485;
                                            }
                                        }
                                        else {
                                            if (type.equals("minecraft:recipe_shapeless")) {
                                                n = 2;
                                            }
                                            break Label_0485;
                                        }
                                        // iftrue(Label_0436:, !tags.contains((Object)"furnace"))
                                        // iftrue(Label_0436:, !tags.contains((Object)"crafting_table"))
                                        // iftrue(Label_0436:, !tags.contains((Object)"crafting_table"))
                                        Block_22: {
                                        Block_20:
                                            while (true) {
                                                WorkbenchRecipeRegistry.addRecipe(new WorkbenchShapedRecipe(recipeParser, parsedRecipe2));
                                                break;
                                                break Block_22;
                                                break Block_20;
                                                continue Label_0410_Outer;
                                            }
                                            WorkbenchRecipeRegistry.addRecipe(new WorkbenchShapelessRecipe(recipeParser, parsedRecipe2));
                                            break;
                                        }
                                        FurnaceRecipeRegistry.addFurnaceRecipe(new FurnaceRecipe(recipeParser, parsedRecipe2));
                                        continue;
                                    }
                                    catch (IllegalArgumentException | NullPointerException ex5) {
                                        final Object o2;
                                        final Object o = o2;
                                        final StringBuilder sb2 = new StringBuilder();
                                        sb2.append("unable to read recipe definition ");
                                        sb2.append(parsedRecipe2.getIdentifier());
                                        ICLog.e("INNERCORE-RECIPES", sb2.toString(), (Throwable)o);
                                    }
                                    break;
                                }
                                switch (n) {
                                    case 2: {
                                        continue Label_0381_Outer;
                                    }
                                    case 1: {
                                        continue Label_0436_Outer;
                                    }
                                    case 0: {
                                        continue Label_0352_Outer;
                                    }
                                    default: {
                                        continue;
                                    }
                                }
                                break;
                            }
                            break;
                        }
                        break;
                    }
                    break;
                }
            }
        }
    }
}
