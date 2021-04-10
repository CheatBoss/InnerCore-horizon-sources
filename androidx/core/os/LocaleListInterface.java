package androidx.core.os;

import java.util.*;
import androidx.annotation.*;

interface LocaleListInterface
{
    Locale get(final int p0);
    
    @Nullable
    Locale getFirstMatch(@NonNull final String[] p0);
    
    Object getLocaleList();
    
    @IntRange(from = -1L)
    int indexOf(final Locale p0);
    
    boolean isEmpty();
    
    @IntRange(from = 0L)
    int size();
    
    String toLanguageTags();
}
