package org.mineprogramming.horizon.innercore.view.item;

import android.support.v7.widget.*;
import android.widget.*;
import org.mineprogramming.horizon.innercore.inflater.layout.*;

public class SectionViewHolder extends RecyclerView$ViewHolder
{
    private final TextView tvTitle;
    
    public SectionViewHolder(final InflatedView inflatedView) {
        super(inflatedView.getView());
        this.tvTitle = inflatedView.getViewByJsonId("title");
    }
    
    public void setTitle(final String text) {
        this.tvTitle.setText((CharSequence)text);
    }
}
