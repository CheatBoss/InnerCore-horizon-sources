package androidx.core.os;

import android.os.*;
import java.util.*;
import androidx.annotation.*;

final class LocaleListCompatWrapper implements LocaleListInterface
{
    private static final Locale EN_LATN;
    private static final Locale LOCALE_AR_XB;
    private static final Locale LOCALE_EN_XA;
    private static final Locale[] sEmptyList;
    private final Locale[] mList;
    @NonNull
    private final String mStringRepresentation;
    
    static {
        sEmptyList = new Locale[0];
        LOCALE_EN_XA = new Locale("en", "XA");
        LOCALE_AR_XB = new Locale("ar", "XB");
        EN_LATN = LocaleListCompat.forLanguageTagCompat("en-Latn");
    }
    
    LocaleListCompatWrapper(@NonNull final Locale... array) {
        if (array.length == 0) {
            this.mList = LocaleListCompatWrapper.sEmptyList;
            this.mStringRepresentation = "";
            return;
        }
        final Locale[] mList = new Locale[array.length];
        final HashSet<Locale> set = new HashSet<Locale>();
        final StringBuilder sb = new StringBuilder();
        for (int i = 0; i < array.length; ++i) {
            final Locale locale = array[i];
            if (locale == null) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("list[");
                sb2.append(i);
                sb2.append("] is null");
                throw new NullPointerException(sb2.toString());
            }
            if (set.contains(locale)) {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("list[");
                sb3.append(i);
                sb3.append("] is a repetition");
                throw new IllegalArgumentException(sb3.toString());
            }
            final Locale locale2 = (Locale)locale.clone();
            toLanguageTag(sb, mList[i] = locale2);
            if (i < array.length - 1) {
                sb.append(',');
            }
            set.add(locale2);
        }
        this.mList = mList;
        this.mStringRepresentation = sb.toString();
    }
    
    private Locale computeFirstMatch(final Collection<String> collection, final boolean b) {
        final int computeFirstMatchIndex = this.computeFirstMatchIndex(collection, b);
        if (computeFirstMatchIndex == -1) {
            return null;
        }
        return this.mList[computeFirstMatchIndex];
    }
    
    private int computeFirstMatchIndex(final Collection<String> collection, final boolean b) {
        if (this.mList.length == 1) {
            return 0;
        }
        if (this.mList.length == 0) {
            return -1;
        }
        int n2;
        final int n = n2 = Integer.MAX_VALUE;
        if (b) {
            final int firstMatchIndex = this.findFirstMatchIndex(LocaleListCompatWrapper.EN_LATN);
            if (firstMatchIndex == 0) {
                return 0;
            }
            n2 = n;
            if (firstMatchIndex < Integer.MAX_VALUE) {
                n2 = firstMatchIndex;
            }
        }
        final Iterator<String> iterator = collection.iterator();
        while (iterator.hasNext()) {
            final int firstMatchIndex2 = this.findFirstMatchIndex(LocaleListCompat.forLanguageTagCompat(iterator.next()));
            if (firstMatchIndex2 == 0) {
                return 0;
            }
            int n3;
            if (firstMatchIndex2 < (n3 = n2)) {
                n3 = firstMatchIndex2;
            }
            n2 = n3;
        }
        if (n2 == Integer.MAX_VALUE) {
            return 0;
        }
        return n2;
    }
    
    private int findFirstMatchIndex(final Locale locale) {
        for (int i = 0; i < this.mList.length; ++i) {
            if (matchScore(locale, this.mList[i]) > 0) {
                return i;
            }
        }
        return Integer.MAX_VALUE;
    }
    
    private static String getLikelyScript(final Locale locale) {
        if (Build$VERSION.SDK_INT < 21) {
            return "";
        }
        final String script = locale.getScript();
        if (!script.isEmpty()) {
            return script;
        }
        return "";
    }
    
    private static boolean isPseudoLocale(final Locale locale) {
        return LocaleListCompatWrapper.LOCALE_EN_XA.equals(locale) || LocaleListCompatWrapper.LOCALE_AR_XB.equals(locale);
    }
    
    @IntRange(from = 0L, to = 1L)
    private static int matchScore(final Locale locale, final Locale locale2) {
        throw new Runtime("d2j fail translate: java.lang.RuntimeException: can not merge I and Z\r\n\tat com.googlecode.dex2jar.ir.TypeClass.merge(TypeClass.java:100)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeRef.updateTypeClass(TypeTransformer.java:174)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.provideAs(TypeTransformer.java:780)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.enexpr(TypeTransformer.java:659)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:719)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.exExpr(TypeTransformer.java:703)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.s1stmt(TypeTransformer.java:810)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.sxStmt(TypeTransformer.java:840)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer$TypeAnalyze.analyze(TypeTransformer.java:206)\r\n\tat com.googlecode.dex2jar.ir.ts.TypeTransformer.transform(TypeTransformer.java:44)\r\n\tat com.googlecode.d2j.dex.Dex2jar$2.optimize(Dex2jar.java:162)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertCode(Dex2Asm.java:414)\r\n\tat com.googlecode.d2j.dex.ExDex2Asm.convertCode(ExDex2Asm.java:42)\r\n\tat com.googlecode.d2j.dex.Dex2jar$2.convertCode(Dex2jar.java:128)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertMethod(Dex2Asm.java:509)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertClass(Dex2Asm.java:406)\r\n\tat com.googlecode.d2j.dex.Dex2Asm.convertDex(Dex2Asm.java:422)\r\n\tat com.googlecode.d2j.dex.Dex2jar.doTranslate(Dex2jar.java:172)\r\n\tat com.googlecode.d2j.dex.Dex2jar.to(Dex2jar.java:272)\r\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.doCommandLine(Dex2jarCmd.java:108)\r\n\tat com.googlecode.dex2jar.tools.BaseCmd.doMain(BaseCmd.java:288)\r\n\tat com.googlecode.dex2jar.tools.Dex2jarCmd.main(Dex2jarCmd.java:32)\r\n");
    }
    
    @VisibleForTesting
    static void toLanguageTag(final StringBuilder sb, final Locale locale) {
        sb.append(locale.getLanguage());
        final String country = locale.getCountry();
        if (country != null && !country.isEmpty()) {
            sb.append('-');
            sb.append(locale.getCountry());
        }
    }
    
    @Override
    public boolean equals(final Object o) {
        if (o == this) {
            return true;
        }
        if (!(o instanceof LocaleListCompatWrapper)) {
            return false;
        }
        final Locale[] mList = ((LocaleListCompatWrapper)o).mList;
        if (this.mList.length != mList.length) {
            return false;
        }
        for (int i = 0; i < this.mList.length; ++i) {
            if (!this.mList[i].equals(mList[i])) {
                return false;
            }
        }
        return true;
    }
    
    @Override
    public Locale get(final int n) {
        if (n >= 0 && n < this.mList.length) {
            return this.mList[n];
        }
        return null;
    }
    
    @Override
    public Locale getFirstMatch(@NonNull final String[] array) {
        return this.computeFirstMatch(Arrays.asList(array), false);
    }
    
    @Nullable
    @Override
    public Object getLocaleList() {
        return null;
    }
    
    @Override
    public int hashCode() {
        int n = 1;
        for (int i = 0; i < this.mList.length; ++i) {
            n = n * 31 + this.mList[i].hashCode();
        }
        return n;
    }
    
    @Override
    public int indexOf(final Locale locale) {
        for (int i = 0; i < this.mList.length; ++i) {
            if (this.mList[i].equals(locale)) {
                return i;
            }
        }
        return -1;
    }
    
    @Override
    public boolean isEmpty() {
        return this.mList.length == 0;
    }
    
    @Override
    public int size() {
        return this.mList.length;
    }
    
    @Override
    public String toLanguageTags() {
        return this.mStringRepresentation;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        sb.append("[");
        for (int i = 0; i < this.mList.length; ++i) {
            sb.append(this.mList[i]);
            if (i < this.mList.length - 1) {
                sb.append(',');
            }
        }
        sb.append("]");
        return sb.toString();
    }
}
