package java.util;

public final class StringJoiner
{
    private final String prefix;
    private final String delimiter;
    private final String suffix;
    private StringBuilder value;
    private String emptyValue;
    
    public StringJoiner(final CharSequence charSequence) {
        this(charSequence, "", "");
    }
    
    public StringJoiner(final CharSequence charSequence, final CharSequence charSequence2, final CharSequence charSequence3) {
        Objects.requireNonNull(charSequence2, "The prefix must not be null");
        Objects.requireNonNull(charSequence, "The delimiter must not be null");
        Objects.requireNonNull(charSequence3, "The suffix must not be null");
        this.prefix = charSequence2.toString();
        this.delimiter = charSequence.toString();
        this.suffix = charSequence3.toString();
        this.emptyValue = this.prefix + this.suffix;
    }
    
    public StringJoiner setEmptyValue(final CharSequence charSequence) {
        this.emptyValue = Objects.requireNonNull(charSequence, "The empty value must not be null").toString();
        return this;
    }
    
    @Override
    public String toString() {
        if (this.value == null) {
            return this.emptyValue;
        }
        if (this.suffix.equals("")) {
            return this.value.toString();
        }
        final int length = this.value.length();
        final String string = this.value.append(this.suffix).toString();
        this.value.setLength(length);
        return string;
    }
    
    public StringJoiner add(final CharSequence charSequence) {
        this.prepareBuilder().append(charSequence);
        return this;
    }
    
    public StringJoiner merge(final StringJoiner stringJoiner) {
        Objects.requireNonNull(stringJoiner);
        if (stringJoiner.value != null) {
            this.prepareBuilder().append(stringJoiner.value, stringJoiner.prefix.length(), stringJoiner.value.length());
        }
        return this;
    }
    
    private StringBuilder prepareBuilder() {
        if (this.value != null) {
            this.value.append(this.delimiter);
        }
        else {
            this.value = new StringBuilder().append(this.prefix);
        }
        return this.value;
    }
    
    public int length() {
        return (this.value != null) ? (this.value.length() + this.suffix.length()) : this.emptyValue.length();
    }
}
