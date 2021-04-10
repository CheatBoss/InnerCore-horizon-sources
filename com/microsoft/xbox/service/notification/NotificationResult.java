package com.microsoft.xbox.service.notification;

import android.content.*;
import com.microsoft.xboxtcui.*;
import android.util.*;
import java.util.*;
import com.google.firebase.messaging.*;

public class NotificationResult
{
    public String body;
    public String data;
    public NotificationType notificationType;
    public String title;
    
    public NotificationResult(final RemoteMessage remoteMessage, final Context context) {
        final Map data = remoteMessage.getData();
        final String s = data.get("type");
        Label_0185: {
            Label_0027: {
                if (s != null) {
                    String body;
                    if (s.equals("xbox_live_game_invite")) {
                        this.notificationType = NotificationType.Invite;
                        this.title = context.getString(R$string.xbox_live_game_invite_title);
                        final String string = context.getString(R$string.xbox_live_game_invite_body);
                        final RemoteMessage$Notification notification = remoteMessage.getNotification();
                        if (notification == null) {
                            Log.i("XSAPI.Android", "could not parse notification");
                            break Label_0185;
                        }
                        Log.i("XSAPI.Android", "parsing notification");
                        final String[] bodyLocalizationArgs = notification.getBodyLocalizationArgs();
                        if (bodyLocalizationArgs == null) {
                            break Label_0185;
                        }
                        body = String.format(string, bodyLocalizationArgs[0], bodyLocalizationArgs[1]);
                    }
                    else {
                        if (!s.equals("xbox_live_achievement_unlock")) {
                            break Label_0027;
                        }
                        this.notificationType = NotificationType.Achievement;
                        if (remoteMessage.getNotification() == null) {
                            break Label_0185;
                        }
                        this.title = remoteMessage.getNotification().getTitle();
                        body = remoteMessage.getNotification().getBody();
                    }
                    this.body = body;
                    break Label_0185;
                }
            }
            this.notificationType = NotificationType.Unknown;
        }
        this.data = data.get("xbl");
    }
    
    public enum NotificationType
    {
        Achievement, 
        Invite, 
        Unknown;
    }
}
