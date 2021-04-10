package com.zhekasmirnov.horizon.activity.main.adapter;

import android.app.*;
import android.content.*;
import android.support.v7.widget.*;
import android.support.v7.widget.helper.*;
import android.support.annotation.*;
import java.util.*;
import com.zhekasmirnov.horizon.runtime.task.*;
import android.view.*;

public class MenuHolder
{
    private final Activity context;
    private final MenuAdapter adapter;
    private final List<String> order;
    private final HashMap<String, List<Entry>> entryMap;
    private final List<MenuEntryBuilder> entryBuilders;
    
    public MenuHolder(final Activity context, final MenuAdapter adapter) {
        this.order = new ArrayList<String>();
        this.entryMap = new HashMap<String, List<Entry>>();
        this.entryBuilders = new ArrayList<MenuEntryBuilder>();
        this.context = context;
        this.adapter = adapter;
    }
    
    public MenuHolder(final Activity context, final View view) {
        this.order = new ArrayList<String>();
        this.entryMap = new HashMap<String, List<Entry>>();
        this.entryBuilders = new ArrayList<MenuEntryBuilder>();
        this.context = context;
        this.adapter = new MenuAdapter();
        final RecyclerView recyclerView = (RecyclerView)view;
        recyclerView.setLayoutManager((RecyclerView.LayoutManager)new LinearLayoutManager((Context)context));
        recyclerView.setItemAnimator((RecyclerView.ItemAnimator)new DefaultItemAnimator());
        recyclerView.addItemDecoration((RecyclerView.ItemDecoration)new DividerItemDecoration((Context)context, 1));
        recyclerView.setAdapter((RecyclerView.Adapter)this.adapter);
        final ItemTouchHelper touchHelper = new ItemTouchHelper((ItemTouchHelper.Callback)new ItemTouchHelper.SimpleCallback(0, 12) {
            public boolean isLongPressDragEnabled() {
                return true;
            }
            
            public boolean isItemViewSwipeEnabled() {
                return true;
            }
            
            public int getSwipeDirs(@NonNull final RecyclerView recyclerView, @NonNull final RecyclerView.ViewHolder viewHolder) {
                final int position = viewHolder.getAdapterPosition();
                final Entry entry = MenuHolder.this.getEntryForPosition(position);
                return (entry != null && entry.handler.isRemovable()) ? 12 : 0;
            }
            
            public int getDragDirs(@NonNull final RecyclerView recyclerView, @NonNull final RecyclerView.ViewHolder viewHolder) {
                final int position = viewHolder.getAdapterPosition();
                final Entry entry = MenuHolder.this.getEntryForPosition(position);
                return (entry != null && entry.handler.isDraggable()) ? 51 : 0;
            }
            
            public boolean onMove(@NonNull final RecyclerView recyclerView, @NonNull final RecyclerView.ViewHolder viewHolder1, @NonNull final RecyclerView.ViewHolder viewHolder2) {
                final int fromPosition = viewHolder1.getAdapterPosition();
                final int toPosition = viewHolder2.getAdapterPosition();
                final Entry entry = MenuHolder.this.getEntryForPosition(toPosition);
                if (entry != null && !entry.handler.isDraggable()) {
                    return false;
                }
                if (fromPosition < toPosition) {
                    for (int i = fromPosition; i < toPosition; ++i) {
                        Collections.swap(MenuHolder.this.adapter.entries, i, i + 1);
                    }
                }
                else {
                    for (int i = fromPosition; i > toPosition; --i) {
                        Collections.swap(MenuHolder.this.adapter.entries, i, i - 1);
                    }
                }
                MenuHolder.this.adapter.notifyItemMoved(fromPosition, toPosition);
                return true;
            }
            
            public void onSwiped(@NonNull final RecyclerView.ViewHolder viewHolder, final int par) {
                final int position = viewHolder.getAdapterPosition();
                MenuHolder.this.removeEntry(MenuHolder.this.getEntryForPosition(position));
            }
        });
        touchHelper.attachToRecyclerView(recyclerView);
    }
    
    public void addEntry(final String name, final EntryBuilder builder, final EntryHandler handler) {
        this.context.runOnUiThread((Runnable)new Runnable() {
            @Override
            public void run() {
                List<Entry> entries = MenuHolder.this.entryMap.get(name);
                if (entries == null) {
                    entries = new ArrayList<Entry>();
                    MenuHolder.this.entryMap.put(name, entries);
                    MenuHolder.this.order.add(name);
                }
                final Entry entry = new Entry(builder, handler);
                entries.add(entry);
                MenuHolder.this.adapter.entries.add(entry);
                MenuHolder.this.adapter.notifyItemInserted(MenuHolder.this.adapter.entries.size() - 1);
                handler.onCreated(entry);
            }
        });
    }
    
    public void removeEntry(final Entry entry) {
        this.context.runOnUiThread((Runnable)new Runnable() {
            @Override
            public void run() {
                for (final String name : MenuHolder.this.order) {
                    final List<Entry> entries = MenuHolder.this.entryMap.get(name);
                    if (entries != null && entries.contains(entry)) {
                        entries.remove(entry);
                        entry.onRemoved();
                    }
                }
                for (int pos = 0; pos < MenuHolder.this.adapter.entries.size(); ++pos) {
                    if (entry == MenuHolder.this.adapter.entries.get(pos)) {
                        MenuHolder.this.adapter.notifyItemRemoved(pos);
                        MenuHolder.this.adapter.entries.remove(pos);
                        break;
                    }
                }
            }
        });
    }
    
    public void clearByName(final String name) {
        this.order.remove(name);
        final List<Entry> entries = this.entryMap.get(name);
        if (entries != null) {
            for (final Entry entry : new ArrayList<Entry>(entries)) {
                this.removeEntry(entry);
            }
            this.entryMap.remove(name);
        }
    }
    
    public Entry getEntryForPosition(final int position) {
        return (position >= 0 && position < this.adapter.entries.size()) ? this.adapter.entries.get(position) : null;
    }
    
    public void addAndAttachEntryBuilder(final TaskManager taskManager, final MenuEntryBuilder builder) {
        this.entryBuilders.add(builder);
        builder.attachTo(taskManager, this);
    }
    
    public void addEntryBuilder(final MenuEntryBuilder builder) {
        this.entryBuilders.add(builder);
    }
    
    public void attachBuilders(final TaskManager manager) {
        for (final MenuEntryBuilder builder : this.entryBuilders) {
            builder.attachTo(manager, this);
        }
    }
    
    public static class Entry
    {
        public View root;
        public final EntryBuilder builder;
        public final EntryHandler handler;
        
        protected Entry(final EntryBuilder builder, final EntryHandler handler) {
            this.builder = builder;
            this.handler = handler;
        }
        
        public View create(final ViewGroup parent) {
            return this.builder.create(parent);
        }
        
        public void bind(final View view) {
            this.root = view;
            if (this.handler != null) {
                this.root.setOnClickListener((View.OnClickListener)this.handler);
            }
            this.builder.bind(this.root);
        }
        
        protected void onRemoved() {
            if (this.handler != null) {
                this.handler.onRemoved();
            }
        }
    }
    
    public interface EntryBuilder
    {
        View create(final ViewGroup p0);
        
        void bind(final View p0);
    }
    
    public interface EntryHandler extends View.OnClickListener
    {
        boolean isDraggable();
        
        boolean isRemovable();
        
        void onRemoved();
        
        void onCreated(final Entry p0);
    }
}
