package com.appboy.ui.contentcards.handlers;

import android.content.*;
import java.util.*;
import com.appboy.models.cards.*;
import com.appboy.ui.contentcards.view.*;
import android.view.*;

public interface IContentCardsViewBindingHandler
{
    int getItemViewType(final Context p0, final List<Card> p1, final int p2);
    
    void onBindViewHolder(final Context p0, final List<Card> p1, final ContentCardViewHolder p2, final int p3);
    
    ContentCardViewHolder onCreateViewHolder(final Context p0, final List<Card> p1, final ViewGroup p2, final int p3);
}
