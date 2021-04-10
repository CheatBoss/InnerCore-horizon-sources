package bo.app;

import java.util.regex.*;
import com.appboy.support.*;
import java.util.concurrent.*;
import java.util.*;
import java.io.*;

public final class bb
{
    static final Pattern a;
    private static final String c;
    private static final OutputStream q;
    final ThreadPoolExecutor b;
    private final File d;
    private final File e;
    private final File f;
    private final File g;
    private final int h;
    private long i;
    private final int j;
    private long k;
    private Writer l;
    private final LinkedHashMap<String, bc> m;
    private int n;
    private long o;
    private final Callable<Void> p;
    
    static {
        a = Pattern.compile("[a-z0-9_-]{1,120}");
        c = AppboyLogger.getAppboyLogTag(bb.class);
        q = new OutputStream() {
            @Override
            public void write(final int n) {
            }
        };
    }
    
    private bb(final File d, final int h, final int j, final long i) {
        this.k = 0L;
        this.m = new LinkedHashMap<String, bc>(0, 0.75f, true);
        this.o = 0L;
        this.b = new ThreadPoolExecutor(0, 1, 60L, TimeUnit.SECONDS, new LinkedBlockingQueue<Runnable>());
        this.p = new Callable<Void>() {
            public Void a() {
                synchronized (bb.this) {
                    if (bb.this.l == null) {
                        return null;
                    }
                    bb.this.i();
                    if (bb.this.g()) {
                        bb.this.f();
                        bb.this.n = 0;
                    }
                    return null;
                }
            }
        };
        this.d = d;
        this.h = h;
        this.e = new File(d, "journal");
        this.f = new File(d, "journal.tmp");
        this.g = new File(d, "journal.bkp");
        this.j = j;
        this.i = i;
    }
    
    private bb.a a(final String s, final long n) {
        synchronized (this) {
            this.h();
            this.e(s);
            bc bc = this.m.get(s);
            if (n != -1L && (bc == null || bc.e != n)) {
                return null;
            }
            if (bc == null) {
                bc = new bc(s, this.j, this.d);
                this.m.put(s, bc);
            }
            else if (bc.d != null) {
                return null;
            }
            final bb.a d = new bb.a(bc);
            bc.d = d;
            final Writer l = this.l;
            final StringBuilder sb = new StringBuilder();
            sb.append("DIRTY ");
            sb.append(s);
            sb.append('\n');
            l.write(sb.toString());
            this.l.flush();
            return d;
        }
    }
    
    public static bb a(final File file, final int n, final int n2, final long n3) {
        if (n3 <= 0L) {
            throw new IllegalArgumentException("maxSize <= 0");
        }
        if (n2 > 0) {
            final File file2 = new File(file, "journal.bkp");
            if (file2.exists()) {
                final File file3 = new File(file, "journal");
                if (file3.exists()) {
                    file2.delete();
                }
                else {
                    a(file2, file3, false);
                }
            }
            final bb bb = new bb(file, n, n2, n3);
            if (bb.e.exists()) {
                try {
                    bb.d();
                    bb.e();
                    return bb;
                }
                catch (IOException ex) {
                    final String c = bo.app.bb.c;
                    final StringBuilder sb = new StringBuilder();
                    sb.append("DiskLruCache ");
                    sb.append(file);
                    sb.append(" is corrupt: ");
                    sb.append(ex.getMessage());
                    sb.append(", removing");
                    AppboyLogger.e(c, sb.toString());
                    bb.b();
                }
            }
            file.mkdirs();
            final bb bb2 = new bb(file, n, n2, n3);
            bb2.f();
            return bb2;
        }
        throw new IllegalArgumentException("valueCount <= 0");
    }
    
    private void a(final bb.a a, final boolean b) {
        while (true) {
            while (true) {
                int n2;
                synchronized (this) {
                    final bc a2 = a.b;
                    if (a2.d != a) {
                        throw new IllegalStateException();
                    }
                    final int n = n2 = 0;
                    if (b) {
                        n2 = n;
                        if (!a2.c) {
                            int n3 = 0;
                            while (true) {
                                n2 = n;
                                if (n3 >= this.j) {
                                    break;
                                }
                                if (!a.c[n3]) {
                                    a.b();
                                    final StringBuilder sb = new StringBuilder();
                                    sb.append("Newly created entry didn't create value for index ");
                                    sb.append(n3);
                                    throw new IllegalStateException(sb.toString());
                                }
                                if (!a2.b(n3).exists()) {
                                    a.b();
                                    return;
                                }
                                ++n3;
                            }
                        }
                    }
                    if (n2 >= this.j) {
                        ++this.n;
                        a2.d = null;
                        if (a2.c | b) {
                            a2.c = true;
                            final Writer l = this.l;
                            final StringBuilder sb2 = new StringBuilder();
                            sb2.append("CLEAN ");
                            sb2.append(a2.a);
                            sb2.append(a2.a());
                            sb2.append('\n');
                            l.write(sb2.toString());
                            if (b) {
                                a2.e = this.o++;
                            }
                        }
                        else {
                            this.m.remove(a2.a);
                            final Writer i = this.l;
                            final StringBuilder sb3 = new StringBuilder();
                            sb3.append("REMOVE ");
                            sb3.append(a2.a);
                            sb3.append('\n');
                            i.write(sb3.toString());
                        }
                        this.l.flush();
                        if (this.k > this.i || this.g()) {
                            this.b.submit(this.p);
                        }
                        return;
                    }
                    final File b2 = a2.b(n2);
                    if (b) {
                        if (b2.exists()) {
                            final File a3 = a2.a(n2);
                            b2.renameTo(a3);
                            final long n4 = a2.b[n2];
                            final long length = a3.length();
                            a2.b[n2] = length;
                            this.k = this.k - n4 + length;
                        }
                    }
                    else {
                        a(b2);
                    }
                }
                ++n2;
                continue;
            }
        }
    }
    
    private static void a(final File file) {
        if (!file.exists()) {
            return;
        }
        if (file.delete()) {
            return;
        }
        throw new IOException();
    }
    
    private static void a(final File file, final File file2, final boolean b) {
        if (b) {
            a(file2);
        }
        if (file.renameTo(file2)) {
            return;
        }
        throw new IOException();
    }
    
    private void d() {
        final bd bd = new bd(new FileInputStream(this.e), be.a);
        try {
            final String a = bd.a();
            final String a2 = bd.a();
            final String a3 = bd.a();
            final String a4 = bd.a();
            final String a5 = bd.a();
            if ("libcore.io.DiskLruCache".equals(a) && "1".equals(a2) && Integer.toString(this.h).equals(a3) && Integer.toString(this.j).equals(a4) && "".equals(a5)) {
                int n = 0;
                try {
                    while (true) {
                        this.d(bd.a());
                        ++n;
                    }
                }
                catch (EOFException ex) {
                    this.n = n - this.m.size();
                    if (bd.b()) {
                        this.f();
                    }
                    else {
                        this.l = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.e, true), be.a));
                    }
                    return;
                }
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("unexpected journal header: [");
            sb.append(a);
            sb.append(", ");
            sb.append(a2);
            sb.append(", ");
            sb.append(a4);
            sb.append(", ");
            sb.append(a5);
            sb.append("]");
            throw new IOException(sb.toString());
        }
        finally {
            be.a(bd);
        }
    }
    
    private void d(final String s) {
        final int index = s.indexOf(32);
        if (index == -1) {
            final StringBuilder sb = new StringBuilder();
            sb.append("unexpected journal line: ");
            sb.append(s);
            throw new IOException(sb.toString());
        }
        final int n = index + 1;
        final int index2 = s.indexOf(32, n);
        String s3;
        if (index2 == -1) {
            final String s2 = s3 = s.substring(n);
            if (index == 6) {
                s3 = s2;
                if (s.startsWith("REMOVE")) {
                    this.m.remove(s2);
                    return;
                }
            }
        }
        else {
            s3 = s.substring(n, index2);
        }
        bc bc;
        if ((bc = this.m.get(s3)) == null) {
            bc = new bc(s3, this.j, this.d);
            this.m.put(s3, bc);
        }
        if (index2 != -1 && index == 5 && s.startsWith("CLEAN")) {
            final String[] split = s.substring(index2 + 1).split(" ");
            bc.c = true;
            bc.d = null;
            bc.a(split);
            return;
        }
        if (index2 == -1 && index == 5 && s.startsWith("DIRTY")) {
            bc.d = new bb.a(bc);
            return;
        }
        if (index2 == -1 && index == 4 && s.startsWith("READ")) {
            return;
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("unexpected journal line: ");
        sb2.append(s);
        throw new IOException(sb2.toString());
    }
    
    private void e() {
        a(this.f);
        final Iterator<bc> iterator = this.m.values().iterator();
        while (iterator.hasNext()) {
            final bc bc = iterator.next();
            final bb.a d = bc.d;
            final int n = 0;
            int i = 0;
            if (d == null) {
                while (i < this.j) {
                    this.k += bc.b[i];
                    ++i;
                }
            }
            else {
                bc.d = null;
                for (int j = n; j < this.j; ++j) {
                    a(bc.a(j));
                    a(bc.b(j));
                }
                iterator.remove();
            }
        }
    }
    
    private void e(final String s) {
        if (bb.a.matcher(s).matches()) {
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("keys must match regex [a-z0-9_-]{1,120}: \"");
        sb.append(s);
        sb.append("\"");
        throw new IllegalArgumentException(sb.toString());
    }
    
    private void f() {
        synchronized (this) {
            if (this.l != null) {
                this.l.close();
            }
            final BufferedWriter bufferedWriter = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.f), be.a));
            try {
                bufferedWriter.write("libcore.io.DiskLruCache");
                bufferedWriter.write("\n");
                bufferedWriter.write("1");
                bufferedWriter.write("\n");
                bufferedWriter.write(Integer.toString(this.h));
                bufferedWriter.write("\n");
                bufferedWriter.write(Integer.toString(this.j));
                bufferedWriter.write("\n");
                bufferedWriter.write("\n");
                for (final bc bc : this.m.values()) {
                    String s;
                    if (bc.d != null) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("DIRTY ");
                        sb.append(bc.a);
                        sb.append('\n');
                        s = sb.toString();
                    }
                    else {
                        final StringBuilder sb2 = new StringBuilder();
                        sb2.append("CLEAN ");
                        sb2.append(bc.a);
                        sb2.append(bc.a());
                        sb2.append('\n');
                        s = sb2.toString();
                    }
                    bufferedWriter.write(s);
                }
                bufferedWriter.close();
                if (this.e.exists()) {
                    a(this.e, this.g, true);
                }
                a(this.f, this.e, false);
                this.g.delete();
                this.l = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(this.e, true), be.a));
            }
            finally {
                bufferedWriter.close();
            }
        }
    }
    
    private boolean g() {
        final int n = this.n;
        return n >= 2000 && n >= this.m.size();
    }
    
    private void h() {
        if (this.l != null) {
            return;
        }
        throw new IllegalStateException("cache is closed");
    }
    
    private void i() {
        while (this.k > this.i) {
            this.c(this.m.entrySet().iterator().next().getKey());
        }
    }
    
    public b a(final String s) {
        synchronized (this) {
            this.h();
            this.e(s);
            final bc bc = this.m.get(s);
            if (bc == null) {
                return null;
            }
            if (!bc.c) {
                return null;
            }
            final InputStream[] array = new InputStream[this.j];
            final int n = 0;
            int i = 0;
            try {
                while (i < this.j) {
                    array[i] = new FileInputStream(bc.a(i));
                    ++i;
                }
                ++this.n;
                final Writer l = this.l;
                final StringBuilder sb = new StringBuilder();
                sb.append("READ ");
                sb.append(s);
                sb.append('\n');
                l.append((CharSequence)sb.toString());
                if (this.g()) {
                    this.b.submit(this.p);
                }
                return new b(s, bc.e, array, bc.b);
            }
            catch (FileNotFoundException ex) {
                for (int n2 = n; n2 < this.j && array[n2] != null; ++n2) {
                    be.a(array[n2]);
                }
                return null;
            }
        }
    }
    
    public void a() {
        synchronized (this) {
            if (this.l == null) {
                return;
            }
            for (final bc bc : new ArrayList<bc>(this.m.values())) {
                if (bc.d != null) {
                    bc.d.b();
                }
            }
            this.i();
            this.l.close();
            this.l = null;
        }
    }
    
    public bb.a b(final String s) {
        return this.a(s, -1L);
    }
    
    public void b() {
        this.a();
        be.a(this.d);
    }
    
    public boolean c(final String s) {
        synchronized (this) {
            this.h();
            this.e(s);
            final bc bc = this.m.get(s);
            int i = 0;
            if (bc != null && bc.d == null) {
                while (i < this.j) {
                    final File a = bc.a(i);
                    if (a.exists() && !a.delete()) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("failed to delete ");
                        sb.append(a);
                        throw new IOException(sb.toString());
                    }
                    this.k -= bc.b[i];
                    bc.b[i] = 0L;
                    ++i;
                }
                ++this.n;
                final Writer l = this.l;
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("REMOVE ");
                sb2.append(s);
                sb2.append('\n');
                l.append((CharSequence)sb2.toString());
                this.m.remove(s);
                if (this.g()) {
                    this.b.submit(this.p);
                }
                return true;
            }
            return false;
        }
    }
    
    public final class a
    {
        private final bc b;
        private final boolean[] c;
        private boolean d;
        private boolean e;
        
        private a(final bc b) {
            this.b = b;
            boolean[] c;
            if (b.c) {
                c = null;
            }
            else {
                c = new boolean[bb.this.j];
            }
            this.c = c;
        }
        
        public OutputStream a(final int n) {
            if (n >= 0 && n < bb.this.j) {
                synchronized (bb.this) {
                    if (this.b.d == this) {
                        if (!this.b.c) {
                            this.c[n] = true;
                        }
                        final File b = this.b.b(n);
                        Label_0095: {
                            try {
                                final FileOutputStream fileOutputStream = new FileOutputStream(b);
                                break Label_0095;
                            }
                            catch (FileNotFoundException ex) {
                                bb.this.d.mkdirs();
                                try {
                                    final FileOutputStream fileOutputStream = new FileOutputStream(b);
                                    return new bb.a((OutputStream)fileOutputStream);
                                }
                                catch (FileNotFoundException ex2) {
                                    return bb.q;
                                }
                            }
                        }
                    }
                    throw new IllegalStateException();
                }
            }
            final StringBuilder sb = new StringBuilder();
            sb.append("Expected index ");
            sb.append(n);
            sb.append(" to be greater than 0 and less than the maximum value count of ");
            sb.append(bb.this.j);
            throw new IllegalArgumentException(sb.toString());
        }
        
        public void a() {
            if (this.d) {
                bb.this.a(this, false);
                bb.this.c(this.b.a);
            }
            else {
                bb.this.a(this, true);
            }
            this.e = true;
        }
        
        public void b() {
            bb.this.a(this, false);
        }
    }
    
    class a extends FilterOutputStream
    {
        final /* synthetic */ bb.a a;
        
        private a(final bb.a a, final OutputStream outputStream) {
            this.a = a;
            super(outputStream);
        }
        
        @Override
        public void close() {
            try {
                this.out.close();
            }
            catch (IOException ex) {
                this.a.d = true;
            }
        }
        
        @Override
        public void flush() {
            try {
                this.out.flush();
            }
            catch (IOException ex) {
                this.a.d = true;
            }
        }
        
        @Override
        public void write(final int n) {
            try {
                this.out.write(n);
            }
            catch (IOException ex) {
                this.a.d = true;
            }
        }
        
        @Override
        public void write(final byte[] array, final int n, final int n2) {
            try {
                this.out.write(array, n, n2);
            }
            catch (IOException ex) {
                this.a.d = true;
            }
        }
    }
    
    public final class b implements Closeable
    {
        private final String b;
        private final long c;
        private final InputStream[] d;
        private final long[] e;
        
        private b(final String b, final long c, final InputStream[] d, final long[] e) {
            this.b = b;
            this.c = c;
            this.d = d;
            this.e = e;
        }
        
        public InputStream a(final int n) {
            return this.d[n];
        }
        
        @Override
        public void close() {
            final InputStream[] d = this.d;
            for (int length = d.length, i = 0; i < length; ++i) {
                be.a(d[i]);
            }
        }
    }
}
