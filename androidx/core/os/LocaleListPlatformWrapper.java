package androidx.core.os;

import android.os.*;
import java.util.*;
import androidx.annotation.*;

@RequiresApi(24)
final class LocaleListPlatformWrapper implements LocaleListInterface
{
    private final LocaleList mLocaleList;
    
    LocaleListPlatformWrapper(final LocaleList mLocaleList) {
        this.mLocaleList = mLocaleList;
    }
    
    @Override
    public boolean equals(final Object o) {
        return this.mLocaleList.equals(((LocaleListInterface)o).getLocaleList());
    }
    
    @Override
    public Locale get(final int n) {
        return this.mLocaleList.get(n);
    }
    
    @Nullable
    @Override
    public Locale getFirstMatch(@NonNull final String[] array) {
        return this.mLocaleList.getFirstMatch(array);
    }
    
    @Override
    public Object getLocaleList() {
        return this.mLocaleList;
    }
    
    @Override
    public int hashCode() {
        return this.mLocaleList.hashCode();
    }
    
    @Override
    public int indexOf(final Locale locale) {
        return this.mLocaleList.indexOf(locale);
    }
    
    @Override
    public boolean isEmpty() {
        return this.mLocaleList.isEmpty();
    }
    
    @Override
    public int size() {
        return this.mLocaleList.size();
    }
    
    @Override
    public String toLanguageTags() {
        return this.mLocaleList.toLanguageTags();
    }
    
    @Override
    public String toString() {
        return this.mLocaleList.toString();
    }
}
