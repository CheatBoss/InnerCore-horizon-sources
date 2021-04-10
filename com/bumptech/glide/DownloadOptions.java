package com.bumptech.glide;

import com.bumptech.glide.request.*;
import java.io.*;
import com.bumptech.glide.request.target.*;

interface DownloadOptions
{
    FutureTarget<File> downloadOnly(final int p0, final int p1);
    
     <Y extends Target<File>> Y downloadOnly(final Y p0);
}
