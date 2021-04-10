package okhttp3;

import javax.annotation.*;
import java.util.*;
import okhttp3.internal.*;

public final class Headers
{
    private final String[] namesAndValues;
    
    Headers(final Builder builder) {
        this.namesAndValues = builder.namesAndValues.toArray(new String[builder.namesAndValues.size()]);
    }
    
    private static String get(final String[] array, final String s) {
        int length = array.length;
        int n;
        do {
            n = length - 2;
            if (n < 0) {
                return null;
            }
            length = n;
        } while (!s.equalsIgnoreCase(array[n]));
        return array[n + 1];
    }
    
    @Override
    public boolean equals(@Nullable final Object o) {
        return o instanceof Headers && Arrays.equals(((Headers)o).namesAndValues, this.namesAndValues);
    }
    
    @Nullable
    public String get(final String s) {
        return get(this.namesAndValues, s);
    }
    
    @Override
    public int hashCode() {
        return Arrays.hashCode(this.namesAndValues);
    }
    
    public String name(final int n) {
        return this.namesAndValues[n * 2];
    }
    
    public Builder newBuilder() {
        final Builder builder = new Builder();
        Collections.addAll(builder.namesAndValues, this.namesAndValues);
        return builder;
    }
    
    public int size() {
        return this.namesAndValues.length / 2;
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder();
        for (int size = this.size(), i = 0; i < size; ++i) {
            sb.append(this.name(i));
            sb.append(": ");
            sb.append(this.value(i));
            sb.append("\n");
        }
        return sb.toString();
    }
    
    public String value(final int n) {
        return this.namesAndValues[n * 2 + 1];
    }
    
    public List<String> values(final String s) {
        final int size = this.size();
        List<? extends String> list = null;
        List<String> list2;
        for (int i = 0; i < size; ++i, list = list2) {
            list2 = (List<String>)list;
            if (s.equalsIgnoreCase(this.name(i))) {
                if ((list2 = (List<String>)list) == null) {
                    list2 = new ArrayList<String>(2);
                }
                list2.add(this.value(i));
            }
        }
        if (list != null) {
            return (List<String>)Collections.unmodifiableList((List<?>)list);
        }
        return Collections.emptyList();
    }
    
    public static final class Builder
    {
        final List<String> namesAndValues;
        
        public Builder() {
            this.namesAndValues = new ArrayList<String>(20);
        }
        
        private void checkNameAndValue(final String s, final String s2) {
            if (s == null) {
                throw new NullPointerException("name == null");
            }
            if (s.isEmpty()) {
                throw new IllegalArgumentException("name is empty");
            }
            for (int length = s.length(), i = 0; i < length; ++i) {
                final char char1 = s.charAt(i);
                if (char1 <= ' ' || char1 >= '\u007f') {
                    throw new IllegalArgumentException(Util.format("Unexpected char %#04x at %d in header name: %s", (int)char1, i, s));
                }
            }
            if (s2 != null) {
                for (int length2 = s2.length(), j = 0; j < length2; ++j) {
                    final char char2 = s2.charAt(j);
                    if ((char2 <= '\u001f' && char2 != '\t') || char2 >= '\u007f') {
                        throw new IllegalArgumentException(Util.format("Unexpected char %#04x at %d in %s value: %s", (int)char2, j, s, s2));
                    }
                }
                return;
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("value for name ");
            sb.append(s);
            sb.append(" == null");
            throw new NullPointerException(sb.toString());
        }
        
        public Builder add(final String s, final String s2) {
            this.checkNameAndValue(s, s2);
            return this.addLenient(s, s2);
        }
        
        Builder addLenient(final String s) {
            final int index = s.indexOf(":", 1);
            if (index != -1) {
                return this.addLenient(s.substring(0, index), s.substring(index + 1));
            }
            if (s.startsWith(":")) {
                return this.addLenient("", s.substring(1));
            }
            return this.addLenient("", s);
        }
        
        Builder addLenient(final String s, final String s2) {
            this.namesAndValues.add(s);
            this.namesAndValues.add(s2.trim());
            return this;
        }
        
        public Headers build() {
            return new Headers(this);
        }
        
        public Builder removeAll(final String s) {
            int n;
            for (int i = 0; i < this.namesAndValues.size(); i = n + 2) {
                n = i;
                if (s.equalsIgnoreCase(this.namesAndValues.get(i))) {
                    this.namesAndValues.remove(i);
                    this.namesAndValues.remove(i);
                    n = i - 2;
                }
            }
            return this;
        }
        
        public Builder set(final String s, final String s2) {
            this.checkNameAndValue(s, s2);
            this.removeAll(s);
            this.addLenient(s, s2);
            return this;
        }
    }
}
