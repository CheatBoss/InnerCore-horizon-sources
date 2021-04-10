package com.appboy.ui.feed.listeners;

import android.content.*;
import com.appboy.models.cards.*;
import com.appboy.ui.actions.*;

public class AppboyDefaultFeedClickActionListener implements IFeedClickActionListener
{
    @Override
    public boolean onFeedCardClicked(final Context context, final Card card, final IAction action) {
        return false;
    }
}
