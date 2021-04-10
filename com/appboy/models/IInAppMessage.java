package com.appboy.models;

import org.json.*;
import android.graphics.*;
import java.util.*;
import android.net.*;
import com.appboy.enums.inappmessage.*;

public interface IInAppMessage extends IPutIntoJson<JSONObject>
{
    boolean getAnimateIn();
    
    boolean getAnimateOut();
    
    int getBackgroundColor();
    
    Bitmap getBitmap();
    
    ClickAction getClickAction();
    
    CropType getCropType();
    
    DismissType getDismissType();
    
    int getDurationInMilliseconds();
    
    long getExpirationTimestamp();
    
    Map<String, String> getExtras();
    
    String getIcon();
    
    int getIconBackgroundColor();
    
    int getIconColor();
    
    boolean getImageDownloadSuccessful();
    
    String getImageUrl();
    
    String getLocalImageUrl();
    
    String getMessage();
    
    TextAlign getMessageTextAlign();
    
    int getMessageTextColor();
    
    boolean getOpenUriInWebView();
    
    Orientation getOrientation();
    
    String getRemoteAssetPathForPrefetch();
    
    String getRemoteImageUrl();
    
    Uri getUri();
    
    boolean isControl();
    
    boolean logClick();
    
    boolean logDisplayFailure(final InAppMessageFailureType p0);
    
    boolean logImpression();
    
    void onAfterClosed();
    
    void setAnimateIn(final boolean p0);
    
    void setAnimateOut(final boolean p0);
    
    void setBackgroundColor(final int p0);
    
    void setBitmap(final Bitmap p0);
    
    boolean setClickAction(final ClickAction p0);
    
    boolean setClickAction(final ClickAction p0, final Uri p1);
    
    void setCropType(final CropType p0);
    
    void setDismissType(final DismissType p0);
    
    void setDurationInMilliseconds(final int p0);
    
    void setExpirationTimestamp(final long p0);
    
    void setIcon(final String p0);
    
    void setIconBackgroundColor(final int p0);
    
    void setIconColor(final int p0);
    
    void setImageDownloadSuccessful(final boolean p0);
    
    void setImageUrl(final String p0);
    
    void setLocalAssetPathForPrefetch(final String p0);
    
    void setLocalImageUrl(final String p0);
    
    void setMessage(final String p0);
    
    void setMessageTextAlign(final TextAlign p0);
    
    void setMessageTextColor(final int p0);
    
    void setOpenUriInWebView(final boolean p0);
    
    void setOrientation(final Orientation p0);
    
    void setRemoteImageUrl(final String p0);
}
