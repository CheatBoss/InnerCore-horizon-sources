package java.io;

import java.util.function.*;
import java.util.*;
import java.util.stream.*;

public class DesugarBufferedReader
{
    private DesugarBufferedReader() {
    }
    
    public static Stream<String> lines(final BufferedReader bufferedReader) {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize((Iterator<? extends String>)new Iterator<String>() {
            String nextLine = null;
            
            @Override
            public void forEachRemaining(final Consumer<?> p0) {
                // 
                // This method could not be decompiled.
                // 
                // Could not show original bytecode, likely due to the same error.
                // 
                // The error that occurred was:
                // 
                // java.lang.NullPointerException
                // 
                throw new IllegalStateException("An error occurred while decompiling this method.");
            }
            
            @Override
            public boolean hasNext() {
                if (this.nextLine != null) {
                    return true;
                }
                try {
                    this.nextLine = bufferedReader.readLine();
                    return this.nextLine != null;
                }
                catch (IOException ex) {
                    throw new UncheckedIOException(ex);
                }
            }
            
            @Override
            public String next() {
                if (this.nextLine == null && !this.hasNext()) {
                    throw new NoSuchElementException();
                }
                final String nextLine = this.nextLine;
                this.nextLine = null;
                return nextLine;
            }
            
            @Override
            public void remove() {
                Iterator-CC.$default$remove(this);
            }
        }, 272), false);
    }
}
