package com.appboy.ui.actions;

import android.content.*;
import com.appboy.enums.*;

public interface IAction
{
    void execute(final Context p0);
    
    Channel getChannel();
}
