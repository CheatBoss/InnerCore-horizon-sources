package com.zhekasmirnov.innercore.api.mod.recipes.workbench;

import com.zhekasmirnov.apparatus.mcpe.*;
import com.zhekasmirnov.innercore.api.*;
import com.zhekasmirnov.innercore.api.mod.ui.container.*;
import com.zhekasmirnov.apparatus.adapter.innercore.game.item.*;
import java.util.*;
import com.zhekasmirnov.innercore.api.commontypes.*;
import com.zhekasmirnov.innercore.api.log.*;

public class WorkbenchRecipeRegistry
{
    private static final HashMap<Long, ArrayList<WorkbenchRecipe>> componentQuickAccess;
    private static final Map<Long, WorkbenchRecipe> recipeByUid;
    private static final HashMap<String, ArrayList<WorkbenchRecipe>> recipes;
    
    static {
        recipes = new HashMap<String, ArrayList<WorkbenchRecipe>>();
        componentQuickAccess = new HashMap<Long, ArrayList<WorkbenchRecipe>>();
        recipeByUid = new HashMap<Long, WorkbenchRecipe>();
    }
    
    private static void addItemToInvMap(final HashMap<Long, Integer> hashMap, final int n, final int n2, final int n3) {
        final long codeByItem = RecipeEntry.getCodeByItem(n, n3);
        if (!hashMap.containsKey(codeByItem)) {
            hashMap.put(codeByItem, n2);
        }
        else {
            hashMap.put(codeByItem, hashMap.get(codeByItem) + n2);
        }
        final long codeByItem2 = RecipeEntry.getCodeByItem(n, -1);
        if (!hashMap.containsKey(codeByItem2)) {
            hashMap.put(codeByItem2, n2);
            return;
        }
        hashMap.put(codeByItem2, hashMap.get(codeByItem2) + n2);
    }
    
    public static void addRecipe(final WorkbenchRecipe workbenchRecipe) {
        if (!workbenchRecipe.isValid()) {
            return;
        }
        final ArrayList<WorkbenchRecipe> list = new ArrayList<WorkbenchRecipe>();
        workbenchRecipe.addVariants(list);
        int n = 1;
        for (final WorkbenchRecipe workbenchRecipe2 : list) {
            final ArrayList<WorkbenchRecipe> recipeArrayByMask = getRecipeArrayByMask(workbenchRecipe2.getRecipeMask());
            if (!recipeArrayByMask.contains(workbenchRecipe2)) {
                recipeArrayByMask.add(workbenchRecipe2);
                WorkbenchRecipeRegistry.recipeByUid.put(workbenchRecipe2.getRecipeUid(), workbenchRecipe2);
            }
            int n2;
            if ((n2 = n) != 0) {
                addRecipeToQuickAccess(workbenchRecipe2);
                n2 = 0;
            }
            n = n2;
        }
    }
    
    private static void addRecipeToQuickAccess(final WorkbenchRecipe workbenchRecipe) {
        for (final Long n : workbenchRecipe.getEntryCodes()) {
            ArrayList<WorkbenchRecipe> list;
            if ((list = WorkbenchRecipeRegistry.componentQuickAccess.get(n)) == null) {
                list = new ArrayList<WorkbenchRecipe>();
                WorkbenchRecipeRegistry.componentQuickAccess.put(n, list);
            }
            list.add(workbenchRecipe);
        }
    }
    
    public static void addRecipesThatContainItem(final int n, final int n2, final Collection<WorkbenchRecipe> collection) {
        final ArrayList<WorkbenchRecipe> list = WorkbenchRecipeRegistry.componentQuickAccess.get(RecipeEntry.getCodeByItem(n, n2));
        if (list != null) {
            final Iterator<WorkbenchRecipe> iterator = list.iterator();
            while (iterator.hasNext()) {
                collection.add(iterator.next());
            }
        }
        if (n2 != -1) {
            final ArrayList<WorkbenchRecipe> list2 = WorkbenchRecipeRegistry.componentQuickAccess.get(RecipeEntry.getCodeByItem(n, -1));
            if (list2 != null) {
                final Iterator<WorkbenchRecipe> iterator2 = list2.iterator();
                while (iterator2.hasNext()) {
                    collection.add(iterator2.next());
                }
            }
        }
    }
    
    public static void cleanupWorkbenchField(final WorkbenchField workbenchField, final long n) {
        NativeAPI.getPosition(NativeAPI.getPlayer(), new float[3]);
        final NativePlayer nativePlayer = new NativePlayer(n);
        for (int i = 0; i < 9; ++i) {
            final AbstractSlot fieldSlot = workbenchField.getFieldSlot(i);
            fieldSlot.validate();
            if (fieldSlot.getId() != 0) {
                nativePlayer.addItemToInventory(fieldSlot.getId(), fieldSlot.getCount(), fieldSlot.getData(), fieldSlot.getExtra(), true);
                fieldSlot.set(0, 0, 0, null);
            }
        }
    }
    
    public static UIRecipeLists getAvailableRecipesForPlayerInventory(final long n, final WorkbenchField workbenchField, final String s) {
        final HashSet<WorkbenchRecipe> set = new HashSet<WorkbenchRecipe>();
        final HashMap<Long, Integer> hashMap = new HashMap<Long, Integer>();
        final NativePlayer nativePlayer = new NativePlayer(n);
        final int n2 = 0;
        for (int i = 0; i < 36; ++i) {
            final ItemStack inventorySlot = nativePlayer.getInventorySlot(i);
            if (inventorySlot.id != 0 && inventorySlot.count > 0) {
                addItemToInvMap(hashMap, inventorySlot.id, inventorySlot.count, inventorySlot.data);
                addRecipesThatContainItem(inventorySlot.id, inventorySlot.data, set);
            }
        }
        if (workbenchField != null) {
            for (int j = n2; j < 9; ++j) {
                final AbstractSlot fieldSlot = workbenchField.getFieldSlot(j);
                if (fieldSlot.getId() != 0 && fieldSlot.getCount() > 0) {
                    addItemToInvMap(hashMap, fieldSlot.getId(), fieldSlot.getCount(), fieldSlot.getData());
                    addRecipesThatContainItem(fieldSlot.getId(), fieldSlot.getData(), set);
                }
            }
        }
        int possibleCount = 0;
        final ArrayList<WorkbenchRecipe> recipes = new ArrayList<WorkbenchRecipe>();
        for (final WorkbenchRecipe workbenchRecipe : set) {
            int n3 = possibleCount;
            if (workbenchRecipe.isMatchingPrefix(s)) {
                if (workbenchRecipe.isPossibleForInventory(hashMap)) {
                    recipes.add(possibleCount, workbenchRecipe);
                    n3 = possibleCount + 1;
                }
                else {
                    recipes.add(workbenchRecipe);
                    n3 = possibleCount;
                }
            }
            possibleCount = n3;
        }
        final UIRecipeLists uiRecipeLists = new UIRecipeLists();
        uiRecipeLists.possibleCount = possibleCount;
        uiRecipeLists.recipes = recipes;
        return uiRecipeLists;
    }
    
    public static String[] getFieldMasks(final WorkbenchField workbenchField) {
        final ArrayList<Comparable> list = new ArrayList<Comparable>();
        String string = "";
        for (int i = 0; i < 9; ++i) {
            final AbstractSlot fieldSlot = workbenchField.getFieldSlot(i);
            int id;
            if (fieldSlot.getCount() > 0) {
                id = fieldSlot.getId();
            }
            else {
                id = 0;
            }
            final char c = (char)id;
            final StringBuilder sb = new StringBuilder();
            sb.append(string);
            sb.append(c);
            string = sb.toString();
            if (c != '\0') {
                list.add(c);
            }
        }
        String string2 = "$$";
        Collections.sort(list);
        for (final Character c2 : list) {
            final StringBuilder sb2 = new StringBuilder();
            sb2.append(string2);
            sb2.append(c2);
            string2 = sb2.toString();
        }
        return new String[] { string, string2 };
    }
    
    private static ArrayList<WorkbenchRecipe> getRecipeArrayByMask(final String s) {
        if (!WorkbenchRecipeRegistry.recipes.containsKey(s)) {
            WorkbenchRecipeRegistry.recipes.put(s, new ArrayList<WorkbenchRecipe>());
        }
        return WorkbenchRecipeRegistry.recipes.get(s);
    }
    
    public static WorkbenchRecipe getRecipeByUid(final long n) {
        return WorkbenchRecipeRegistry.recipeByUid.get(n);
    }
    
    public static WorkbenchRecipe getRecipeFromField(final WorkbenchField workbenchField, final String s) {
        final String[] fieldMasks = getFieldMasks(workbenchField);
        if (WorkbenchRecipeRegistry.recipes.containsKey(fieldMasks[0])) {
            for (final WorkbenchRecipe workbenchRecipe : WorkbenchRecipeRegistry.recipes.get(fieldMasks[0])) {
                if (workbenchRecipe.isMatchingField(workbenchField) && workbenchRecipe.isMatchingPrefix(s)) {
                    return workbenchRecipe;
                }
            }
        }
        if (WorkbenchRecipeRegistry.recipes.containsKey(fieldMasks[1])) {
            for (final WorkbenchRecipe workbenchRecipe2 : WorkbenchRecipeRegistry.recipes.get(fieldMasks[1])) {
                if (workbenchRecipe2.isMatchingField(workbenchField)) {
                    return workbenchRecipe2;
                }
            }
        }
        return null;
    }
    
    public static ItemInstance getRecipeResult(final WorkbenchField workbenchField, final String s) {
        final WorkbenchRecipe recipeFromField = getRecipeFromField(workbenchField, s);
        if (recipeFromField != null) {
            return recipeFromField.getResult();
        }
        return null;
    }
    
    public static Collection<WorkbenchRecipe> getRecipesByIngredient(final int n, final int n2) {
        final ArrayList<WorkbenchRecipe> list = new ArrayList<WorkbenchRecipe>();
        addRecipesThatContainItem(n, n2, list);
        return list;
    }
    
    public static Collection<WorkbenchRecipe> getRecipesByResult(final int n, final int n2, final int n3) {
        final HashSet<WorkbenchRecipe> set = new HashSet<WorkbenchRecipe>();
        for (final ArrayList<WorkbenchRecipe> list : WorkbenchRecipeRegistry.componentQuickAccess.values()) {
            for (int i = 0; i < list.size(); ++i) {
                final WorkbenchRecipe workbenchRecipe = list.get(i);
                if (workbenchRecipe.id == n && (workbenchRecipe.data == n3 || n3 == -1) && (workbenchRecipe.count == n2 || n2 == -1)) {
                    set.add(workbenchRecipe);
                }
            }
        }
        return set;
    }
    
    public static ItemInstance provideRecipe(final WorkbenchField workbenchField, final String s) {
        return provideRecipeForPlayer(workbenchField, s, NativeAPI.getPlayer());
    }
    
    public static ItemInstance provideRecipeForPlayer(final WorkbenchField workbenchField, final String s, final long n) {
        final WorkbenchRecipe recipeFromField = getRecipeFromField(workbenchField, s);
        if (recipeFromField != null) {
            return recipeFromField.provideRecipeForPlayer(workbenchField, n);
        }
        return null;
    }
    
    public static void removeRecipeByResult(final int n, final int n2, final int n3) {
        int n4 = 0;
        final Iterator<ArrayList<WorkbenchRecipe>> iterator = WorkbenchRecipeRegistry.recipes.values().iterator();
        while (true) {
            final boolean hasNext = iterator.hasNext();
            int i = 0;
            if (!hasNext) {
                break;
            }
            int n5;
            int n6;
            for (ArrayList<WorkbenchRecipe> list = iterator.next(); i < list.size(); i = n6 + 1, n4 = n5) {
                final WorkbenchRecipe workbenchRecipe = list.get(i);
                n5 = n4;
                n6 = i;
                if (workbenchRecipe.id == n) {
                    if (workbenchRecipe.data != n3) {
                        n5 = n4;
                        n6 = i;
                        if (n3 != -1) {
                            continue;
                        }
                    }
                    if (workbenchRecipe.count != n2) {
                        n5 = n4;
                        n6 = i;
                        if (n2 != -1) {
                            continue;
                        }
                    }
                    list.remove(i);
                    n5 = n4 + 1;
                    n6 = i - 1;
                }
            }
        }
        for (final ArrayList<WorkbenchRecipe> list2 : WorkbenchRecipeRegistry.componentQuickAccess.values()) {
            int n7;
            int n8;
            for (int j = 0; j < list2.size(); j = n7 + 1, n4 = n8) {
                final WorkbenchRecipe workbenchRecipe2 = list2.get(j);
                n7 = j;
                n8 = n4;
                if (workbenchRecipe2.id == n) {
                    if (workbenchRecipe2.data != n3) {
                        n7 = j;
                        n8 = n4;
                        if (n3 != -1) {
                            continue;
                        }
                    }
                    if (workbenchRecipe2.count != n2) {
                        n7 = j;
                        n8 = n4;
                        if (n2 != -1) {
                            continue;
                        }
                    }
                    list2.remove(j);
                    n8 = n4 + 1;
                    n7 = j - 1;
                }
            }
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("removing recipe (");
        sb.append(n);
        sb.append(", ");
        sb.append(n2);
        sb.append(", ");
        sb.append(n3);
        sb.append(") complete, ");
        sb.append(n4);
        sb.append(" variations and entries were removed.");
        ICLog.d("RECIPES", sb.toString());
    }
    
    public static class UIRecipeLists
    {
        int possibleCount;
        List<WorkbenchRecipe> recipes;
    }
}
