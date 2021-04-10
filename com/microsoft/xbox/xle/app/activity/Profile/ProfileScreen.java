package com.microsoft.xbox.xle.app.activity.Profile;

import com.microsoft.xbox.xle.app.activity.*;
import android.content.*;
import android.util.*;
import com.microsoft.xbox.toolkit.ui.*;
import com.microsoft.xbox.telemetry.helpers.*;
import com.microsoft.xboxtcui.*;

public class ProfileScreen extends ActivityBase
{
    public ProfileScreen() {
    }
    
    public ProfileScreen(final Context context, final AttributeSet set) {
        super(context, set);
    }
    
    @Override
    protected String getActivityName() {
        return "PeopleHub Info";
    }
    
    @Override
    public void onCreate() {
        super.onCreate();
        this.onCreateContentView();
        final ProfileScreenViewModel viewModel = new ProfileScreenViewModel(this);
        this.viewModel = viewModel;
        UTCPeopleHub.trackPeopleHubView(this.getActivityName(), viewModel.getXuid(), viewModel.isMeProfile());
    }
    
    @Override
    public void onCreateContentView() {
        this.setContentView(R$layout.profile_screen);
    }
}
