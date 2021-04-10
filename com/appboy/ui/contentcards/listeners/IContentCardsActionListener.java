package com.appboy.ui.contentcards.listeners;

import android.content.*;
import com.appboy.models.cards.*;
import com.appboy.ui.actions.*;

public interface IContentCardsActionListener
{
    boolean onContentCardClicked(final Context p0, final Card p1, final IAction p2);
    
    void onContentCardDismissed(final Context p0, final Card p1);
}
