package com.bumptech.glide.request;

import java.util.concurrent.*;
import com.bumptech.glide.request.target.*;

public interface FutureTarget<R> extends Future<R>, Target<R>
{
    void clear();
}
