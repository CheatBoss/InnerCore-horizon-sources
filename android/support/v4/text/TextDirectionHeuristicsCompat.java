package android.support.v4.text;

import java.nio.*;
import java.util.*;

public final class TextDirectionHeuristicsCompat
{
    public static final TextDirectionHeuristicCompat ANYRTL_LTR;
    public static final TextDirectionHeuristicCompat FIRSTSTRONG_LTR;
    public static final TextDirectionHeuristicCompat FIRSTSTRONG_RTL;
    public static final TextDirectionHeuristicCompat LOCALE;
    public static final TextDirectionHeuristicCompat LTR;
    public static final TextDirectionHeuristicCompat RTL;
    private static final int STATE_FALSE = 1;
    private static final int STATE_TRUE = 0;
    private static final int STATE_UNKNOWN = 2;
    
    static {
        LTR = new TextDirectionHeuristicInternal((TextDirectionAlgorithm)null, false);
        RTL = new TextDirectionHeuristicInternal((TextDirectionAlgorithm)null, true);
        FIRSTSTRONG_LTR = new TextDirectionHeuristicInternal((TextDirectionAlgorithm)FirstStrong.INSTANCE, false);
        FIRSTSTRONG_RTL = new TextDirectionHeuristicInternal((TextDirectionAlgorithm)FirstStrong.INSTANCE, true);
        ANYRTL_LTR = new TextDirectionHeuristicInternal((TextDirectionAlgorithm)AnyStrong.INSTANCE_RTL, false);
        LOCALE = TextDirectionHeuristicLocale.INSTANCE;
    }
    
    private TextDirectionHeuristicsCompat() {
    }
    
    private static int isRtlText(final int n) {
        switch (n) {
            default: {
                return 2;
            }
            case 1:
            case 2: {
                return 0;
            }
            case 0: {
                return 1;
            }
        }
    }
    
    private static int isRtlTextOrFormat(final int n) {
        switch (n) {
            default: {
                switch (n) {
                    default: {
                        return 2;
                    }
                    case 16:
                    case 17: {
                        return 0;
                    }
                    case 14:
                    case 15: {
                        return 1;
                    }
                }
                break;
            }
            case 1:
            case 2: {
                return 0;
            }
            case 0: {
                return 1;
            }
        }
    }
    
    private static class AnyStrong implements TextDirectionAlgorithm
    {
        public static final AnyStrong INSTANCE_LTR;
        public static final AnyStrong INSTANCE_RTL;
        private final boolean mLookForRtl;
        
        static {
            INSTANCE_RTL = new AnyStrong(true);
            INSTANCE_LTR = new AnyStrong(false);
        }
        
        private AnyStrong(final boolean mLookForRtl) {
            this.mLookForRtl = mLookForRtl;
        }
        
        @Override
        public int checkRtl(final CharSequence charSequence, final int n, final int n2) {
            int i = n;
            boolean b = false;
            while (i < n + n2) {
                Label_0077: {
                    switch (isRtlText(Character.getDirectionality(charSequence.charAt(i)))) {
                        default: {
                            break Label_0077;
                        }
                        case 1: {
                            if (this.mLookForRtl) {
                                break;
                            }
                            return 1;
                        }
                        case 0: {
                            if (this.mLookForRtl) {
                                return 0;
                            }
                            break;
                        }
                    }
                    b = true;
                }
                ++i;
            }
            if (!b) {
                return 2;
            }
            if (!this.mLookForRtl) {
                return 0;
            }
            return 1;
        }
    }
    
    private static class FirstStrong implements TextDirectionAlgorithm
    {
        public static final FirstStrong INSTANCE;
        
        static {
            INSTANCE = new FirstStrong();
        }
        
        @Override
        public int checkRtl(final CharSequence charSequence, final int n, final int n2) {
            int n3;
            int access$100;
            for (n3 = n, access$100 = 2; n3 < n + n2 && access$100 == 2; access$100 = isRtlTextOrFormat(Character.getDirectionality(charSequence.charAt(n3))), ++n3) {}
            return access$100;
        }
    }
    
    private interface TextDirectionAlgorithm
    {
        int checkRtl(final CharSequence p0, final int p1, final int p2);
    }
    
    private abstract static class TextDirectionHeuristicImpl implements TextDirectionHeuristicCompat
    {
        private final TextDirectionAlgorithm mAlgorithm;
        
        public TextDirectionHeuristicImpl(final TextDirectionAlgorithm mAlgorithm) {
            this.mAlgorithm = mAlgorithm;
        }
        
        private boolean doCheck(final CharSequence charSequence, final int n, final int n2) {
            switch (this.mAlgorithm.checkRtl(charSequence, n, n2)) {
                default: {
                    return this.defaultIsRtl();
                }
                case 1: {
                    return false;
                }
                case 0: {
                    return true;
                }
            }
        }
        
        protected abstract boolean defaultIsRtl();
        
        @Override
        public boolean isRtl(final CharSequence charSequence, final int n, final int n2) {
            if (charSequence == null || n < 0 || n2 < 0 || charSequence.length() - n2 < n) {
                throw new IllegalArgumentException();
            }
            if (this.mAlgorithm == null) {
                return this.defaultIsRtl();
            }
            return this.doCheck(charSequence, n, n2);
        }
        
        @Override
        public boolean isRtl(final char[] array, final int n, final int n2) {
            return this.isRtl(CharBuffer.wrap(array), n, n2);
        }
    }
    
    private static class TextDirectionHeuristicInternal extends TextDirectionHeuristicImpl
    {
        private final boolean mDefaultIsRtl;
        
        private TextDirectionHeuristicInternal(final TextDirectionAlgorithm textDirectionAlgorithm, final boolean mDefaultIsRtl) {
            super(textDirectionAlgorithm);
            this.mDefaultIsRtl = mDefaultIsRtl;
        }
        
        @Override
        protected boolean defaultIsRtl() {
            return this.mDefaultIsRtl;
        }
    }
    
    private static class TextDirectionHeuristicLocale extends TextDirectionHeuristicImpl
    {
        public static final TextDirectionHeuristicLocale INSTANCE;
        
        static {
            INSTANCE = new TextDirectionHeuristicLocale();
        }
        
        public TextDirectionHeuristicLocale() {
            super(null);
        }
        
        @Override
        protected boolean defaultIsRtl() {
            return TextUtilsCompat.getLayoutDirectionFromLocale(Locale.getDefault()) == 1;
        }
    }
}
