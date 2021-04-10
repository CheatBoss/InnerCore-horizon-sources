package com.android.multidex;

import java.util.regex.*;
import java.util.zip.*;
import java.io.*;
import com.android.dx.cf.direct.*;
import java.util.*;

class Path
{
    private final ByteArrayOutputStream baos;
    private final String definition;
    List<ClassPathElement> elements;
    private final byte[] readBuffer;
    
    Path(final String definition) throws IOException {
        this.elements = new ArrayList<ClassPathElement>();
        this.baos = new ByteArrayOutputStream(40960);
        this.readBuffer = new byte[20480];
        this.definition = definition;
        final String[] split = definition.split(Pattern.quote(File.pathSeparator));
        final int length = split.length;
        int i = 0;
        while (i < length) {
            final String s = split[i];
            try {
                this.addElement(getClassPathElement(new File(s)));
                ++i;
                continue;
            }
            catch (IOException ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append("Wrong classpath: ");
                sb.append(ex.getMessage());
                throw new IOException(sb.toString(), ex);
            }
            break;
        }
    }
    
    private void addElement(final ClassPathElement classPathElement) {
        this.elements.add(classPathElement);
    }
    
    static ClassPathElement getClassPathElement(final File file) throws ZipException, IOException {
        if (file.isDirectory()) {
            return new FolderPathElement(file);
        }
        if (file.isFile()) {
            return new ArchivePathElement(new ZipFile(file));
        }
        if (file.exists()) {
            final StringBuilder sb = new StringBuilder();
            sb.append("\"");
            sb.append(file.getPath());
            sb.append("\" is not a directory neither a zip file");
            throw new IOException(sb.toString());
        }
        final StringBuilder sb2 = new StringBuilder();
        sb2.append("File \"");
        sb2.append(file.getPath());
        sb2.append("\" not found");
        throw new FileNotFoundException(sb2.toString());
    }
    
    private static byte[] readStream(final InputStream inputStream, final ByteArrayOutputStream byteArrayOutputStream, final byte[] array) throws IOException {
        try {
            while (true) {
                final int read = inputStream.read(array);
                if (read < 0) {
                    break;
                }
                byteArrayOutputStream.write(array, 0, read);
            }
            inputStream.close();
            return byteArrayOutputStream.toByteArray();
        }
        finally {
            inputStream.close();
        }
    }
    
    DirectClassFile getClass(final String s) throws FileNotFoundException {
        // monitorenter(this)
        byte[] array = null;
        try {
            final Iterator<ClassPathElement> iterator = this.elements.iterator();
            byte[] stream;
            while (true) {
                stream = array;
                if (iterator.hasNext()) {
                    final ClassPathElement classPathElement = iterator.next();
                    stream = array;
                    try {
                        final InputStream open = classPathElement.open(s);
                        DirectClassFile directClassFile = (Object)array;
                        try {
                            stream = readStream(open, this.baos, this.readBuffer);
                            directClassFile = (DirectClassFile)(Object)array;
                            this.baos.reset();
                            directClassFile = (DirectClassFile)(Object)array;
                            (directClassFile = new DirectClassFile(stream, s, false)).setAttributeFactory(StdAttributeFactory.THE_ONE);
                        }
                        finally {
                            stream = (byte[])(Object)directClassFile;
                            open.close();
                            stream = (byte[])(Object)directClassFile;
                        }
                    }
                    catch (IOException ex) {
                        array = stream;
                        continue;
                    }
                    break;
                }
                break;
            }
            if (stream == null) {
                final StringBuilder sb = new StringBuilder();
                sb.append("File \"");
                sb.append(s);
                sb.append("\" not found");
                throw new FileNotFoundException(sb.toString());
            }
            return (DirectClassFile)(Object)stream;
        }
        finally {
        }
        // monitorexit(this)
    }
    
    Iterable<ClassPathElement> getElements() {
        return this.elements;
    }
    
    @Override
    public String toString() {
        return this.definition;
    }
}
