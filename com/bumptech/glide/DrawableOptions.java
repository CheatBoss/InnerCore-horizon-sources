package com.bumptech.glide;

import android.view.animation.*;

interface DrawableOptions
{
    GenericRequestBuilder<?, ?, ?, ?> crossFade();
    
    GenericRequestBuilder<?, ?, ?, ?> crossFade(final int p0);
    
    GenericRequestBuilder<?, ?, ?, ?> crossFade(final int p0, final int p1);
    
    @Deprecated
    GenericRequestBuilder<?, ?, ?, ?> crossFade(final Animation p0, final int p1);
}
