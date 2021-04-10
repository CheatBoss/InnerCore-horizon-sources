package org.apache.james.mime4j.message;

import org.apache.james.mime4j.parser.*;
import org.apache.james.mime4j.*;
import java.io.*;
import java.util.function.*;
import java.util.*;

public class Header implements Iterable<Field>
{
    private Map<String, List<Field>> fieldMap;
    private List<Field> fields;
    
    public Header() {
        this.fields = new LinkedList<Field>();
        this.fieldMap = new HashMap<String, List<Field>>();
    }
    
    public Header(final InputStream inputStream) throws IOException, MimeIOException {
        this.fields = new LinkedList<Field>();
        this.fieldMap = new HashMap<String, List<Field>>();
        final MimeStreamParser mimeStreamParser = new MimeStreamParser();
        mimeStreamParser.setContentHandler(new AbstractContentHandler() {
            @Override
            public void endHeader() {
                mimeStreamParser.stop();
            }
            
            @Override
            public void field(final Field field) throws MimeException {
                Header.this.addField(field);
            }
        });
        try {
            mimeStreamParser.parse(inputStream);
        }
        catch (MimeException ex) {
            throw new MimeIOException(ex);
        }
    }
    
    public Header(final Header header) {
        this.fields = new LinkedList<Field>();
        this.fieldMap = new HashMap<String, List<Field>>();
        final Iterator<Field> iterator = header.fields.iterator();
        while (iterator.hasNext()) {
            this.addField(iterator.next());
        }
    }
    
    public void addField(final Field field) {
        List<Field> list;
        if ((list = this.fieldMap.get(field.getName().toLowerCase())) == null) {
            list = new LinkedList<Field>();
            this.fieldMap.put(field.getName().toLowerCase(), list);
        }
        list.add(field);
        this.fields.add(field);
    }
    
    @Override
    public void forEach(final Consumer<?> p0) {
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
    
    public Field getField(final String s) {
        final List<Field> list = this.fieldMap.get(s.toLowerCase());
        if (list != null && !list.isEmpty()) {
            return list.get(0);
        }
        return null;
    }
    
    public List<Field> getFields() {
        return Collections.unmodifiableList((List<? extends Field>)this.fields);
    }
    
    public List<Field> getFields(String lowerCase) {
        lowerCase = lowerCase.toLowerCase();
        final List<Field> list = this.fieldMap.get(lowerCase);
        if (list != null && !list.isEmpty()) {
            return (List<Field>)Collections.unmodifiableList((List<?>)list);
        }
        return Collections.emptyList();
    }
    
    @Override
    public Iterator<Field> iterator() {
        return Collections.unmodifiableList((List<? extends Field>)this.fields).iterator();
    }
    
    public int removeFields(final String s) {
        final List<Field> list = this.fieldMap.remove(s.toLowerCase());
        if (list != null && !list.isEmpty()) {
            final Iterator<Field> iterator = this.fields.iterator();
            while (iterator.hasNext()) {
                if (iterator.next().getName().equalsIgnoreCase(s)) {
                    iterator.remove();
                }
            }
            return list.size();
        }
        return 0;
    }
    
    public void setField(final Field field) {
        final List<Field> list = this.fieldMap.get(field.getName().toLowerCase());
        if (list != null && !list.isEmpty()) {
            list.clear();
            list.add(field);
            final Iterator<Field> iterator = this.fields.iterator();
            int n = -1;
            int n2 = 0;
            while (iterator.hasNext()) {
                int n3 = n;
                if (iterator.next().getName().equalsIgnoreCase(field.getName())) {
                    iterator.remove();
                    if ((n3 = n) == -1) {
                        n3 = n2;
                    }
                }
                ++n2;
                n = n3;
            }
            this.fields.add(n, field);
            return;
        }
        this.addField(field);
    }
    
    @Override
    public Spliterator<Object> spliterator() {
        return (Spliterator<Object>)Iterable-CC.$default$spliterator((Iterable)this);
    }
    
    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder(128);
        final Iterator<Field> iterator = this.fields.iterator();
        while (iterator.hasNext()) {
            sb.append(iterator.next().toString());
            sb.append("\r\n");
        }
        return sb.toString();
    }
}
