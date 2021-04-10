package com.zhekasmirnov.innercore.api.mod.recipes.workbench;

import com.zhekasmirnov.apparatus.mcpe.*;
import com.zhekasmirnov.apparatus.adapter.innercore.game.item.*;
import com.zhekasmirnov.innercore.api.mod.ui.container.*;
import java.util.*;
import com.zhekasmirnov.innercore.api.*;

public class InventoryPool
{
    private HashMap<Long, ArrayList<PoolEntry>> entryMap;
    private final NativePlayer player;
    
    public InventoryPool(final long n) {
        this.entryMap = new HashMap<Long, ArrayList<PoolEntry>>();
        this.player = new NativePlayer(n);
    }
    
    public void addPoolEntry(final PoolEntry poolEntry) {
        final ArrayList<PoolEntry> poolEntries = this.getPoolEntries(new RecipeEntry(poolEntry.id, poolEntry.data));
        if (poolEntries != null) {
            poolEntries.add(poolEntry);
        }
    }
    
    public void addRecipeEntry(final RecipeEntry recipeEntry) {
        final long code = recipeEntry.getCode();
        if (!this.entryMap.containsKey(code)) {
            this.entryMap.put(code, new ArrayList<PoolEntry>());
        }
    }
    
    public ArrayList<PoolEntry> getPoolEntries(final RecipeEntry recipeEntry) {
        final Long value = RecipeEntry.getCodeByItem(recipeEntry.id, recipeEntry.data);
        if (this.entryMap.containsKey(value)) {
            return this.entryMap.get(value);
        }
        final Long value2 = RecipeEntry.getCodeByItem(recipeEntry.id, -1);
        if (this.entryMap.containsKey(value2)) {
            return this.entryMap.get(value2);
        }
        return null;
    }
    
    public PoolEntrySet getPoolEntrySet(final RecipeEntry recipeEntry) {
        final ArrayList<PoolEntry> poolEntries = this.getPoolEntries(recipeEntry);
        if (poolEntries != null) {
            return new PoolEntrySet(poolEntries);
        }
        return null;
    }
    
    public void pullFromInventory() {
        for (int i = 0; i < 36; ++i) {
            this.addPoolEntry(new PoolEntry(i));
        }
    }
    
    public class PoolEntry
    {
        public int count;
        public int data;
        public NativeItemInstanceExtra extra;
        public int id;
        public int slot;
        
        public PoolEntry(final int slot) {
            final ItemStack inventorySlot = InventoryPool.this.player.getInventorySlot(slot);
            this.slot = slot;
            this.id = inventorySlot.id;
            this.count = inventorySlot.count;
            this.data = inventorySlot.data;
            this.extra = inventorySlot.extra;
        }
        
        public PoolEntry(final int slot, final int id, final int count, final int data) {
            this.slot = slot;
            this.id = id;
            this.count = count;
            this.data = data;
        }
        
        public int getAmountOfItem(final int n) {
            int count = n;
            if (n > this.count) {
                count = this.count;
            }
            this.count -= count;
            if (this.count <= 0) {
                this.count = 0;
                InventoryPool.this.player.setInventorySlot(this.slot, 0, 0, 0, null);
                return count;
            }
            InventoryPool.this.player.setInventorySlot(this.slot, this.id, this.count, this.data, this.extra);
            return count;
        }
        
        public boolean hasExtra() {
            return this.extra != null && !this.extra.isEmpty();
        }
        
        public boolean isMatches(final PoolEntry poolEntry) {
            return this.id == poolEntry.id && this.data == poolEntry.data;
        }
        
        public boolean isMatchesWithExtra(final PoolEntry poolEntry) {
            return this.id == poolEntry.id && this.data == poolEntry.data && ((this.extra != null && poolEntry.extra != null && this.extra.getValue() == poolEntry.extra.getValue()) || ((this.extra == null || this.extra.isEmpty()) && (poolEntry.extra == null || poolEntry.extra.isEmpty())));
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append("{");
            sb.append(this.id);
            sb.append(", ");
            sb.append(this.count);
            sb.append(", ");
            sb.append(this.data);
            sb.append("}");
            return sb.toString();
        }
    }
    
    public static class PoolEntrySet
    {
        private ArrayList<PoolEntry> entries;
        
        public PoolEntrySet() {
            this.entries = new ArrayList<PoolEntry>();
        }
        
        public PoolEntrySet(final ArrayList<PoolEntry> entries) {
            this.entries = new ArrayList<PoolEntry>();
            this.entries = entries;
        }
        
        public void addEntry(final PoolEntry poolEntry) {
            this.entries.add(poolEntry);
        }
        
        public ArrayList<PoolEntry> getEntries() {
            return this.entries;
        }
        
        public PoolEntry getFirstEntry() {
            if (this.isEmpty()) {
                return null;
            }
            return this.entries.get(0);
        }
        
        public PoolEntrySet getMajorEntrySet() {
            final PoolEntrySet set = new PoolEntrySet();
            PoolEntry poolEntry = null;
            int n = 0;
            for (final PoolEntry poolEntry2 : this.entries) {
                int n2 = 0;
                for (final PoolEntry poolEntry3 : this.entries) {
                    int n3 = n2;
                    if (poolEntry2.isMatches(poolEntry3)) {
                        n3 = n2 + poolEntry3.count;
                    }
                    n2 = n3;
                }
                int n4;
                if ((n4 = n) < n2) {
                    poolEntry = poolEntry2;
                    n4 = n2;
                }
                n = n4;
            }
            if (poolEntry != null) {
                for (final PoolEntry poolEntry4 : this.entries) {
                    if (poolEntry4.isMatches(poolEntry)) {
                        set.addEntry(poolEntry4);
                    }
                }
            }
            return set;
        }
        
        public int getTotalCount() {
            int n = 0;
            final Iterator<PoolEntry> iterator = this.entries.iterator();
            while (iterator.hasNext()) {
                n += iterator.next().count;
            }
            return n;
        }
        
        public boolean isEmpty() {
            return this.entries.size() == 0;
        }
        
        public void removeMatchingEntries(final PoolEntrySet set) {
            final Iterator<PoolEntry> iterator = set.entries.iterator();
            while (iterator.hasNext()) {
                this.entries.remove(iterator.next());
            }
        }
        
        public void spreadItems(final ArrayList<AbstractSlot> list) {
            if (this.isEmpty()) {
                return;
            }
            final PoolEntry poolEntry = this.entries.get(0);
            final ArrayList list2 = new ArrayList();
            final ArrayList<PoolEntry> list3 = new ArrayList<PoolEntry>(this.entries);
            int n = 0;
            final ArrayList<PoolEntry> list4 = new ArrayList<PoolEntry>();
            for (final PoolEntry poolEntry2 : this.entries) {
                int n2 = n;
                if (!poolEntry2.hasExtra()) {
                    list3.remove(poolEntry2);
                    list4.add(poolEntry2);
                    n2 = n + poolEntry2.count;
                }
                n = n2;
            }
            final ArrayList<AbstractSlot> list5 = new ArrayList<AbstractSlot>();
            final int n3 = n / list.size();
            final int size = list.size();
            for (int i = 0; i < list.size(); ++i) {
                final AbstractSlot abstractSlot = list.get(i);
                int n4;
                if (i < n - size * n3) {
                    n4 = 1;
                }
                else {
                    n4 = 0;
                }
                int n5 = n4 + n3;
                final Iterator<PoolEntry> iterator2 = this.entries.iterator();
                int n6 = 0;
                Object o = null;
                while (iterator2.hasNext()) {
                    final PoolEntry poolEntry3 = iterator2.next();
                    if (n5 <= 0) {
                        break;
                    }
                    if (poolEntry3.count <= 0) {
                        continue;
                    }
                    if (poolEntry3.hasExtra()) {
                        continue;
                    }
                    Object o2;
                    if (o == null) {
                        o2 = poolEntry3;
                    }
                    else {
                        o2 = o;
                        if (!((PoolEntry)o).isMatches(poolEntry3)) {
                            continue;
                        }
                    }
                    if (NativeItem.getMaxStackForId(((PoolEntry)o2).id, ((PoolEntry)o2).data) == 1) {
                        n5 = 1;
                    }
                    final int amountOfItem = poolEntry3.getAmountOfItem(n5);
                    n5 -= amountOfItem;
                    n6 += amountOfItem;
                    o = o2;
                }
                if (o != null) {
                    abstractSlot.set(((PoolEntry)o).id, n6, ((PoolEntry)o).data, null);
                    if (n6 == 0) {
                        list5.add(abstractSlot);
                    }
                }
                else {
                    abstractSlot.set(poolEntry.id, 0, poolEntry.data, null);
                    list5.add(abstractSlot);
                }
            }
            for (final AbstractSlot abstractSlot2 : list5) {
                if (list3.size() == 0) {
                    return;
                }
                final PoolEntry poolEntry4 = list3.remove(0);
                abstractSlot2.set(poolEntry4.id, poolEntry4.getAmountOfItem(poolEntry4.count), poolEntry4.data, poolEntry4.extra);
            }
        }
        
        @Override
        public String toString() {
            String string = "";
            for (final PoolEntry poolEntry : this.entries) {
                final StringBuilder sb = new StringBuilder();
                sb.append(string);
                sb.append(poolEntry);
                sb.append(" ");
                string = sb.toString();
            }
            final StringBuilder sb2 = new StringBuilder();
            sb2.append("[");
            sb2.append(string);
            sb2.append("]");
            return sb2.toString();
        }
    }
}
