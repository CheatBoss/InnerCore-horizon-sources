package com.appboy.ui.contentcards.view;

import android.support.v7.widget.*;
import com.appboy.ui.*;
import android.content.*;
import com.facebook.drawee.view.*;
import android.util.*;
import android.view.*;
import android.widget.*;

public class ContentCardViewHolder extends RecyclerView$ViewHolder
{
    private final ImageView mPinnedIcon;
    private final View mUnreadBar;
    
    public ContentCardViewHolder(final View view, final boolean b) {
        super(view);
        final View viewById = view.findViewById(R$id.com_appboy_content_cards_unread_bar);
        this.mUnreadBar = viewById;
        if (viewById != null) {
            if (b) {
                viewById.setVisibility(0);
                this.mUnreadBar.setBackground(view.getContext().getResources().getDrawable(R$drawable.com_appboy_content_cards_unread_bar_background));
            }
            else {
                viewById.setVisibility(8);
            }
        }
        this.mPinnedIcon = (ImageView)view.findViewById(R$id.com_appboy_content_cards_pinned_icon);
    }
    
    public View createCardImageWithStyle(final Context context, final View view, final boolean b, final int n, final int n2) {
        final ContextThemeWrapper contextThemeWrapper = new ContextThemeWrapper(context, n);
        Object o;
        if (b) {
            o = new SimpleDraweeView((Context)contextThemeWrapper, (AttributeSet)null, n);
        }
        else {
            o = new ImageView((Context)contextThemeWrapper, (AttributeSet)null, n);
        }
        ((View)o).setLayoutParams((ViewGroup$LayoutParams)new RelativeLayout$LayoutParams(-1, -2));
        ((RelativeLayout)view.findViewById(n2)).addView((View)o);
        return (View)o;
    }
    
    public void setPinnedIconVisible(final boolean b) {
        final ImageView mPinnedIcon = this.mPinnedIcon;
        if (mPinnedIcon != null) {
            int visibility;
            if (b) {
                visibility = 0;
            }
            else {
                visibility = 8;
            }
            mPinnedIcon.setVisibility(visibility);
        }
    }
    
    public void setUnreadBarVisible(final boolean b) {
        final View mUnreadBar = this.mUnreadBar;
        if (mUnreadBar != null) {
            int visibility;
            if (b) {
                visibility = 0;
            }
            else {
                visibility = 8;
            }
            mUnreadBar.setVisibility(visibility);
        }
    }
}
