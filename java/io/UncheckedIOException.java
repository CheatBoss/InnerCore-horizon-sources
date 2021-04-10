package java.io;

import java.util.*;

public class UncheckedIOException extends RuntimeException
{
    private static final long serialVersionUID = -8134305061645241065L;
    
    public UncheckedIOException(final String s, final IOException ex) {
        super(s, Objects.requireNonNull(ex));
    }
    
    public UncheckedIOException(final IOException ex) {
        super(Objects.requireNonNull(ex));
    }
    
    @Override
    public IOException getCause() {
        return (IOException)super.getCause();
    }
    
    private void readObject(final ObjectInputStream objectInputStream) throws IOException, ClassNotFoundException {
        objectInputStream.defaultReadObject();
        if (!(super.getCause() instanceof IOException)) {
            throw new InvalidObjectException("Cause must be an IOException");
        }
    }
}
