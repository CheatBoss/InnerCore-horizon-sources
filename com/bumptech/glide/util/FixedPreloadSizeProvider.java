package com.bumptech.glide.util;

import com.bumptech.glide.*;
import java.util.*;

public class FixedPreloadSizeProvider<T> implements PreloadSizeProvider<T>
{
    private final int[] size;
    
    public FixedPreloadSizeProvider(final int n, final int n2) {
        this.size = new int[] { n, n2 };
    }
    
    @Override
    public int[] getPreloadSize(final T t, final int n, final int n2) {
        return Arrays.copyOf(this.size, this.size.length);
    }
}
