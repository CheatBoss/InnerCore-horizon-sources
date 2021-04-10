package com.zhekasmirnov.horizon.activity.main.adapter;

import android.support.v7.widget.*;
import java.util.*;
import android.support.annotation.*;
import android.view.*;
import android.widget.*;

public class MenuAdapter extends RecyclerView.Adapter<MenuViewHolder>
{
    public List<MenuHolder.Entry> entries;
    
    public MenuAdapter() {
        this.entries = new ArrayList<MenuHolder.Entry>();
        this.setHasStableIds(true);
    }
    
    public int getItemViewType(final int position) {
        return 0;
    }
    
    public long getItemId(final int position) {
        return this.entries.get(position).hashCode();
    }
    
    private View inflateInvalidCard(@NonNull final ViewGroup parent) {
        return LayoutInflater.from(parent.getContext()).inflate(2131427388, parent, false);
    }
    
    @NonNull
    public MenuViewHolder onCreateViewHolder(@NonNull final ViewGroup parent, final int position) {
        if (position < this.entries.size()) {
            final MenuHolder.Entry entry = this.entries.get(position);
            if (entry != null) {
                final RelativeLayout relativeLayout = new RelativeLayout(parent.getContext());
                final View result = entry.create((ViewGroup)relativeLayout);
                relativeLayout.setLayoutParams((ViewGroup.LayoutParams)new RelativeLayout.LayoutParams(-1, -2));
                relativeLayout.addView(result);
                entry.bind(result);
                return new MenuViewHolder((View)relativeLayout);
            }
        }
        return new MenuViewHolder(this.inflateInvalidCard(parent));
    }
    
    public void onBindViewHolder(@NonNull final MenuViewHolder menuViewHolder, int position) {
        position = menuViewHolder.getAdapterPosition();
        if (position < this.entries.size()) {
            final MenuHolder.Entry entry = this.entries.get(position);
            if (entry != null) {
                ((ViewGroup)menuViewHolder.itemView).removeAllViews();
                final View result = entry.create((ViewGroup)menuViewHolder.itemView);
                ((ViewGroup)menuViewHolder.itemView).addView(result);
                entry.bind(result);
            }
        }
    }
    
    public int getItemCount() {
        return this.entries.size();
    }
    
    public class MenuViewHolder extends RecyclerView.ViewHolder
    {
        public MenuViewHolder(final View itemView) {
            super(itemView);
        }
    }
}
