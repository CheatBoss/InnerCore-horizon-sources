package com.google.firebase.iid;

import javax.annotation.*;
import com.google.android.gms.tasks.*;

public interface MessagingChannel
{
    Task<Void> buildChannel(final String p0, @Nullable final String p1);
    
    Task<String> getToken(final String p0, @Nullable final String p1, final String p2, final String p3);
    
    boolean isAvailable();
    
    boolean isChannelBuilt();
    
    Task<Void> subscribeToTopic(final String p0, final String p1, final String p2);
    
    Task<Void> unsubscribeFromTopic(final String p0, final String p1, final String p2);
}
