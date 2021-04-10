package org.reflections.scanners;

import com.google.common.collect.*;
import com.google.common.base.*;
import org.reflections.adapters.*;
import org.reflections.vfs.*;
import org.reflections.*;

public abstract class AbstractScanner implements Scanner
{
    private Configuration configuration;
    private Predicate<String> resultFilter;
    private Multimap<String, String> store;
    
    public AbstractScanner() {
        this.resultFilter = (Predicate<String>)Predicates.alwaysTrue();
    }
    
    @Override
    public boolean acceptResult(final String s) {
        return s != null && this.resultFilter.apply((Object)s);
    }
    
    @Override
    public boolean acceptsInput(final String s) {
        return this.getMetadataAdapter().acceptsInput(s);
    }
    
    @Override
    public boolean equals(final Object o) {
        return this == o || (o != null && this.getClass() == o.getClass());
    }
    
    @Override
    public Scanner filterResultsBy(final Predicate<String> resultFilter) {
        this.setResultFilter(resultFilter);
        return this;
    }
    
    public Configuration getConfiguration() {
        return this.configuration;
    }
    
    protected MetadataAdapter getMetadataAdapter() {
        return this.configuration.getMetadataAdapter();
    }
    
    public Predicate<String> getResultFilter() {
        return this.resultFilter;
    }
    
    @Override
    public Multimap<String, String> getStore() {
        return this.store;
    }
    
    @Override
    public int hashCode() {
        return this.getClass().hashCode();
    }
    
    @Override
    public Object scan(final Vfs.File file, final Object o) {
        Object ofCreateClassObject = o;
        if (o == null) {
            try {
                ofCreateClassObject = this.configuration.getMetadataAdapter().getOfCreateClassObject(file);
            }
            catch (Exception ex) {
                final StringBuilder sb = new StringBuilder();
                sb.append("could not create class object from file ");
                sb.append(file.getRelativePath());
                throw new ReflectionsException(sb.toString());
            }
        }
        this.scan(ofCreateClassObject);
        return ofCreateClassObject;
    }
    
    public abstract void scan(final Object p0);
    
    @Override
    public void setConfiguration(final Configuration configuration) {
        this.configuration = configuration;
    }
    
    public void setResultFilter(final Predicate<String> resultFilter) {
        this.resultFilter = resultFilter;
    }
    
    @Override
    public void setStore(final Multimap<String, String> store) {
        this.store = store;
    }
}
