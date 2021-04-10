package com.appboy.ui.contentcards;

import android.support.v7.widget.*;
import com.appboy.ui.*;
import android.view.*;

public class AppboyEmptyContentCardsAdapter extends RecyclerView$Adapter<RecyclerView$ViewHolder>
{
    public int getItemCount() {
        return 1;
    }
    
    public void onBindViewHolder(final RecyclerView$ViewHolder recyclerView$ViewHolder, final int n) {
    }
    
    public RecyclerView$ViewHolder onCreateViewHolder(final ViewGroup viewGroup, final int n) {
        return new NetworkUnavailableViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R$layout.com_appboy_content_cards_empty, viewGroup, false));
    }
    
    class NetworkUnavailableViewHolder extends RecyclerView$ViewHolder
    {
        NetworkUnavailableViewHolder(final View view) {
            super(view);
        }
    }
}
