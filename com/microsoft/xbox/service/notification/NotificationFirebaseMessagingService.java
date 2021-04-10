package com.microsoft.xbox.service.notification;

import com.google.firebase.messaging.*;
import com.microsoft.xbox.idp.interop.*;

public class NotificationFirebaseMessagingService extends FirebaseMessagingService
{
    @Override
    public void onNewToken(final String s) {
        Interop.NotificationRegisterCallback(s);
    }
}
