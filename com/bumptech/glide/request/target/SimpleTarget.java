package com.bumptech.glide.request.target;

import com.bumptech.glide.util.*;

public abstract class SimpleTarget<Z> extends BaseTarget<Z>
{
    private final int height;
    private final int width;
    
    public SimpleTarget() {
        this(Integer.MIN_VALUE, Integer.MIN_VALUE);
    }
    
    public SimpleTarget(final int width, final int height) {
        this.width = width;
        this.height = height;
    }
    
    @Override
    public final void getSize(final SizeReadyCallback sizeReadyCallback) {
        if (!Util.isValidDimensions(this.width, this.height)) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Width and height must both be > 0 or Target#SIZE_ORIGINAL, but given width: ");
            sb.append(this.width);
            sb.append(" and height: ");
            sb.append(this.height);
            sb.append(", either provide dimensions in the constructor");
            sb.append(" or call override()");
            throw new IllegalArgumentException(sb.toString());
        }
        sizeReadyCallback.onSizeReady(this.width, this.height);
    }
}
