package com.appboy.ui.actions;

import com.appboy.enums.*;
import android.os.*;
import com.appboy.ui.activities.*;
import android.content.*;
import com.appboy.support.*;

public class NewsfeedAction implements IAction
{
    private final Channel mChannel;
    private final Bundle mExtras;
    
    public NewsfeedAction(final Bundle mExtras, final Channel mChannel) {
        this.mExtras = mExtras;
        this.mChannel = mChannel;
    }
    
    @Override
    public void execute(final Context context) {
        try {
            final Intent intent = new Intent(context, (Class)AppboyFeedActivity.class);
            if (this.mExtras != null) {
                intent.putExtras(this.mExtras);
            }
            context.startActivity(intent);
        }
        catch (Exception ex) {
            AppboyLogger.e("ContentValues", "AppboyFeedActivity was not opened successfully.", ex);
        }
    }
    
    @Override
    public Channel getChannel() {
        return this.mChannel;
    }
    
    public Bundle getExtras() {
        return this.mExtras;
    }
}
