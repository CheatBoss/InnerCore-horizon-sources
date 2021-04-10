package okhttp3.internal.publicsuffix;

import java.util.concurrent.atomic.*;
import java.util.concurrent.*;
import okhttp3.internal.*;
import okio.*;
import java.io.*;
import java.net.*;

public final class PublicSuffixDatabase
{
    private static final String[] EMPTY_RULE;
    private static final String[] PREVAILING_RULE;
    private static final byte[] WILDCARD_LABEL;
    private static final PublicSuffixDatabase instance;
    private final AtomicBoolean listRead;
    private byte[] publicSuffixExceptionListBytes;
    private byte[] publicSuffixListBytes;
    private final CountDownLatch readCompleteLatch;
    
    static {
        WILDCARD_LABEL = new byte[] { 42 };
        EMPTY_RULE = new String[0];
        PREVAILING_RULE = new String[] { "*" };
        instance = new PublicSuffixDatabase();
    }
    
    public PublicSuffixDatabase() {
        this.listRead = new AtomicBoolean(false);
        this.readCompleteLatch = new CountDownLatch(1);
    }
    
    private static String binarySearchBytes(final byte[] array, final byte[][] array2, final int n) {
        int length = array.length;
        int i = 0;
        while (i < length) {
            int n2;
            for (n2 = (i + length) / 2; n2 > -1 && array[n2] != 10; --n2) {}
            final int n3 = n2 + 1;
            int n4 = 1;
            int n5;
            while (true) {
                n5 = n3 + n4;
                if (array[n5] == 10) {
                    break;
                }
                ++n4;
            }
            final int n6 = n5 - n3;
            int n7 = n;
            int n8 = 0;
            int n9 = 0;
            int n10 = 0;
            int n12;
            while (true) {
                int n11;
                if (n8 != 0) {
                    n8 = 0;
                    n11 = 46;
                }
                else {
                    n11 = (array2[n7][n9] & 0xFF);
                }
                n12 = n11 - (array[n3 + n10] & 0xFF);
                if (n12 != 0) {
                    break;
                }
                final int n13 = n10 + 1;
                final int n14 = n9 + 1;
                if (n13 == n6) {
                    n9 = n14;
                    n10 = n13;
                    break;
                }
                n9 = n14;
                n10 = n13;
                if (array2[n7].length != n14) {
                    continue;
                }
                if (n7 == array2.length - 1) {
                    n10 = n13;
                    n9 = n14;
                    break;
                }
                ++n7;
                n8 = 1;
                n9 = -1;
                n10 = n13;
            }
            Label_0262: {
                if (n12 >= 0) {
                    if (n12 <= 0) {
                        final int n15 = n6 - n10;
                        int n16 = array2[n7].length - n9;
                        while (true) {
                            ++n7;
                            if (n7 >= array2.length) {
                                break;
                            }
                            n16 += array2[n7].length;
                        }
                        if (n16 < n15) {
                            break Label_0262;
                        }
                        if (n16 <= n15) {
                            return new String(array, n3, n6, Util.UTF_8);
                        }
                    }
                    i = n5 + 1;
                    continue;
                }
            }
            length = n3 - 1;
        }
        return null;
    }
    
    private String[] findMatchingRule(String[] array) {
        final boolean value = this.listRead.get();
        final int n = 0;
        if (!value && this.listRead.compareAndSet(false, true)) {
            this.readTheListUninterruptibly();
        }
        else {
            try {
                this.readCompleteLatch.await();
            }
            catch (InterruptedException ex) {}
        }
        synchronized (this) {
            if (this.publicSuffixListBytes != null) {
                // monitorexit(this)
                final int length = array.length;
                final byte[][] array2 = new byte[length][];
                for (int i = 0; i < array.length; ++i) {
                    array2[i] = array[i].getBytes(Util.UTF_8);
                }
                int j = 0;
                while (true) {
                    while (j < length) {
                        final String binarySearchBytes = binarySearchBytes(this.publicSuffixListBytes, array2, j);
                        if (binarySearchBytes != null) {
                            String binarySearchBytes2 = null;
                            Label_0196: {
                                if (length > 1) {
                                    final byte[][] array3 = array2.clone();
                                    for (int k = 0; k < array3.length - 1; ++k) {
                                        array3[k] = PublicSuffixDatabase.WILDCARD_LABEL;
                                        binarySearchBytes2 = binarySearchBytes(this.publicSuffixListBytes, array3, k);
                                        if (binarySearchBytes2 != null) {
                                            break Label_0196;
                                        }
                                    }
                                }
                                binarySearchBytes2 = null;
                            }
                            String binarySearchBytes3 = null;
                            Label_0241: {
                                if (binarySearchBytes2 != null) {
                                    for (int l = n; l < length - 1; ++l) {
                                        binarySearchBytes3 = binarySearchBytes(this.publicSuffixExceptionListBytes, array2, l);
                                        if (binarySearchBytes3 != null) {
                                            break Label_0241;
                                        }
                                    }
                                }
                                binarySearchBytes3 = null;
                            }
                            if (binarySearchBytes3 != null) {
                                final StringBuilder sb = new StringBuilder();
                                sb.append("!");
                                sb.append(binarySearchBytes3);
                                return sb.toString().split("\\.");
                            }
                            if (binarySearchBytes == null && binarySearchBytes2 == null) {
                                return PublicSuffixDatabase.PREVAILING_RULE;
                            }
                            if (binarySearchBytes != null) {
                                array = binarySearchBytes.split("\\.");
                            }
                            else {
                                array = PublicSuffixDatabase.EMPTY_RULE;
                            }
                            String[] array4;
                            if (binarySearchBytes2 != null) {
                                array4 = binarySearchBytes2.split("\\.");
                            }
                            else {
                                array4 = PublicSuffixDatabase.EMPTY_RULE;
                            }
                            if (array.length > array4.length) {
                                return array;
                            }
                            return array4;
                        }
                        else {
                            ++j;
                        }
                    }
                    final String binarySearchBytes = null;
                    continue;
                }
            }
            throw new IllegalStateException("Unable to load publicsuffixes.gz resource from the classpath.");
        }
    }
    
    public static PublicSuffixDatabase get() {
        return PublicSuffixDatabase.instance;
    }
    
    private void readTheList() throws IOException {
        final InputStream resourceAsStream = PublicSuffixDatabase.class.getResourceAsStream("publicsuffixes.gz");
        if (resourceAsStream == null) {
            return;
        }
        final BufferedSource buffer = Okio.buffer(new GzipSource(Okio.source(resourceAsStream)));
        try {
            final byte[] publicSuffixListBytes = new byte[buffer.readInt()];
            buffer.readFully(publicSuffixListBytes);
            final byte[] publicSuffixExceptionListBytes = new byte[buffer.readInt()];
            buffer.readFully(publicSuffixExceptionListBytes);
            Util.closeQuietly(buffer);
            synchronized (this) {
                this.publicSuffixListBytes = publicSuffixListBytes;
                this.publicSuffixExceptionListBytes = publicSuffixExceptionListBytes;
                // monitorexit(this)
                this.readCompleteLatch.countDown();
            }
        }
        finally {
            Util.closeQuietly(buffer);
        }
    }
    
    private void readTheListUninterruptibly() {
        int n = 0;
        while (true) {
            try {
                try {
                    this.readTheList();
                    if (n != 0) {
                        Thread.currentThread().interrupt();
                    }
                }
                finally {
                    if (n != 0) {
                        Thread.currentThread().interrupt();
                    }
                    Thread.currentThread().interrupt();
                }
            }
            catch (IOException ex) {}
            catch (InterruptedIOException ex2) {
                n = 1;
                continue;
            }
            break;
        }
    }
    
    public String getEffectiveTldPlusOne(final String s) {
        if (s == null) {
            throw new NullPointerException("domain == null");
        }
        final String[] split = IDN.toUnicode(s).split("\\.");
        final String[] matchingRule = this.findMatchingRule(split);
        if (split.length == matchingRule.length && matchingRule[0].charAt(0) != '!') {
            return null;
        }
        int n;
        int length;
        if (matchingRule[0].charAt(0) == '!') {
            n = split.length;
            length = matchingRule.length;
        }
        else {
            n = split.length;
            length = matchingRule.length + 1;
        }
        int i = n - length;
        final StringBuilder sb = new StringBuilder();
        for (String[] split2 = s.split("\\."); i < split2.length; ++i) {
            sb.append(split2[i]);
            sb.append('.');
        }
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
}
