package com.zhekasmirnov.innercore.api.mod.recipes.workbench;

import com.android.tools.r8.annotations.*;
import com.zhekasmirnov.apparatus.api.container.*;
import android.util.*;
import com.zhekasmirnov.apparatus.adapter.innercore.game.item.*;
import org.json.*;
import java.util.*;

@SynthesizedClassMap({ -$$Lambda$WorkbenchRecipeListBuilder$zmGd0a7mpZIFbFMeXowhdX5samg.class, -$$Lambda$WorkbenchRecipeListBuilder$D0nuxk0Ow-XS_nZLtDeFyci4lBk.class, -$$Lambda$WorkbenchRecipeListBuilder$g31vlJx9cApwegsNMCl4yk3O2JI.class })
public class WorkbenchRecipeListBuilder
{
    private final ItemContainer container;
    private String craftPrefix;
    private final long player;
    
    public WorkbenchRecipeListBuilder(final long player, final ItemContainer container) {
        this.craftPrefix = "";
        this.player = player;
        this.container = container;
    }
    
    public static void deselectRecipe(final ItemContainer itemContainer, final long n) {
        itemContainer.runTransaction((ItemContainer.Transaction)new -$$Lambda$WorkbenchRecipeListBuilder$zmGd0a7mpZIFbFMeXowhdX5samg(itemContainer, n));
    }
    
    public static void selectRecipe(final ItemContainer itemContainer, final WorkbenchRecipe workbenchRecipe, final long n) {
        deselectRecipe(itemContainer, n);
        itemContainer.runTransaction((ItemContainer.Transaction)new -$$Lambda$WorkbenchRecipeListBuilder$g31vlJx9cApwegsNMCl4yk3O2JI(workbenchRecipe, itemContainer, n));
    }
    
    public JSONObject buildAvailableRecipesPacket(RecipeComparator recipeComparator) {
        final WorkbenchRecipeRegistry.UIRecipeLists availableRecipesForPlayerInventory = WorkbenchRecipeRegistry.getAvailableRecipesForPlayerInventory(this.player, this.container, this.craftPrefix);
        int n = 0;
        final ArrayList<Pair> list = new ArrayList<Pair>();
        final Iterator<WorkbenchRecipe> iterator = availableRecipesForPlayerInventory.recipes.iterator();
        while (true) {
            final boolean hasNext = iterator.hasNext();
            boolean b = true;
            if (!hasNext) {
                break;
            }
            final WorkbenchRecipe workbenchRecipe = iterator.next();
            if (n >= availableRecipesForPlayerInventory.possibleCount) {
                b = false;
            }
            list.add(new Pair((Object)workbenchRecipe, (Object)b));
            ++n;
        }
        Collections.sort((List<Object>)list, new -$$Lambda$WorkbenchRecipeListBuilder$D0nuxk0Ow-XS_nZLtDeFyci4lBk(recipeComparator));
        recipeComparator = (RecipeComparator)new JSONObject();
        try {
            final JSONArray jsonArray = new JSONArray();
            ((JSONObject)recipeComparator).put("recipes", (Object)jsonArray);
            for (final Pair pair : list) {
                final ItemStack parse = ItemStack.parse(((WorkbenchRecipe)pair.first).getResult());
                final JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", ((WorkbenchRecipe)pair.first).getRecipeUid());
                jsonObject.put("result", (Object)parse.asJson());
                jsonObject.put("d", (boolean)pair.second ^ true);
                jsonArray.put((Object)jsonObject);
            }
            return (JSONObject)recipeComparator;
        }
        catch (JSONException ex) {
            return (JSONObject)recipeComparator;
        }
    }
    
    public void setCraftPrefix(final String craftPrefix) {
        this.craftPrefix = craftPrefix;
    }
    
    public interface RecipeComparator
    {
        int compare(final WorkbenchRecipe p0, final WorkbenchRecipe p1);
    }
}
