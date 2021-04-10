package com.appboy.ui.feed.listeners;

import android.content.*;
import com.appboy.models.cards.*;
import com.appboy.ui.actions.*;

public interface IFeedClickActionListener
{
    boolean onFeedCardClicked(final Context p0, final Card p1, final IAction p2);
}
