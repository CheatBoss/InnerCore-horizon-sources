package com.appboy.models;

import com.appboy.enums.inappmessage.*;
import java.util.*;

public interface IInAppMessageImmersive extends IInAppMessage
{
    int getCloseButtonColor();
    
    Integer getFrameColor();
    
    String getHeader();
    
    TextAlign getHeaderTextAlign();
    
    int getHeaderTextColor();
    
    ImageStyle getImageStyle();
    
    List<MessageButton> getMessageButtons();
    
    boolean logButtonClick(final MessageButton p0);
    
    void setCloseButtonColor(final int p0);
    
    void setFrameColor(final Integer p0);
    
    void setHeader(final String p0);
    
    void setHeaderTextAlign(final TextAlign p0);
    
    void setHeaderTextColor(final int p0);
    
    void setImageStyle(final ImageStyle p0);
    
    void setMessageButtons(final List<MessageButton> p0);
}
