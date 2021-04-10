package com.appboy.ui.contentcards.listeners;

import android.content.*;
import com.appboy.models.cards.*;
import com.appboy.ui.actions.*;

public class AppboyContentCardsActionListener implements IContentCardsActionListener
{
    @Override
    public boolean onContentCardClicked(final Context context, final Card card, final IAction action) {
        return false;
    }
    
    @Override
    public void onContentCardDismissed(final Context context, final Card card) {
    }
}
