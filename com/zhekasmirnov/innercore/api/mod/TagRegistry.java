package com.zhekasmirnov.innercore.api.mod;

import org.mozilla.javascript.annotations.*;
import org.mozilla.javascript.*;
import java.util.*;

public class TagRegistry
{
    private static final HashMap<String, TagGroup> groups;
    
    static {
        groups = new HashMap<String, TagGroup>();
    }
    
    @JSFunction
    public static TagGroup getOrCreateGroup(final String s) {
        if (TagRegistry.groups.containsKey(s)) {
            return TagRegistry.groups.get(s);
        }
        final TagGroup tagGroup = new TagGroup(s);
        TagRegistry.groups.put(s, tagGroup);
        return tagGroup;
    }
    
    public interface TagFactory
    {
        void addTags(final Object p0, final Collection<String> p1);
    }
    
    public static class TagGroup
    {
        private final HashMap<Object, HashSet<String>> commonObjectCollection;
        private boolean isCommonObjectsCollectionDirty;
        public final String name;
        private final HashMap<String, HashSet<String>> primaryTagMap;
        private final List<TagFactory> tagFactories;
        
        public TagGroup(final String name) {
            this.primaryTagMap = new HashMap<String, HashSet<String>>();
            this.tagFactories = new ArrayList<TagFactory>();
            this.isCommonObjectsCollectionDirty = false;
            this.commonObjectCollection = new HashMap<Object, HashSet<String>>();
            this.name = name;
        }
        
        public void addCommonObject(final Object o, final String... array) {
            this.addTagsFor(o, array);
            synchronized (this.commonObjectCollection) {
                this.commonObjectCollection.put(o, this.getTags(o));
            }
        }
        
        public void addTagFactory(final TagFactory tagFactory) {
            if (tagFactory != null) {
                this.tagFactories.add(tagFactory);
                this.isCommonObjectsCollectionDirty = true;
            }
        }
        
        public void addTags(final Object o, final Collection<String> collection) {
            final HashMap<String, HashSet<String>> primaryTagMap = this.primaryTagMap;
            String string;
            if (o != null) {
                string = o.toString();
            }
            else {
                string = null;
            }
            final HashSet<String> set = primaryTagMap.get(string);
            if (set != null) {
                collection.addAll(set);
            }
            if (o instanceof Scriptable) {
                final ScriptableObjectWrapper scriptableObjectWrapper = new ScriptableObjectWrapper((Scriptable)o);
                if (scriptableObjectWrapper.has("_tags")) {
                    final ScriptableObjectWrapper scriptableWrapper = scriptableObjectWrapper.getScriptableWrapper("_tags");
                    if (scriptableWrapper != null) {
                        final Object[] array = scriptableWrapper.asArray();
                        for (int length = array.length, i = 0; i < length; ++i) {
                            final Object o2 = array[i];
                            if (o2 != null) {
                                collection.add(o2.toString());
                            }
                        }
                    }
                }
            }
            final Iterator<TagFactory> iterator = this.tagFactories.iterator();
            while (iterator.hasNext()) {
                iterator.next().addTags(o, collection);
            }
        }
        
        public void addTagsFor(final Object o, final String... array) {
            final HashMap<Object, HashSet<String>> commonObjectCollection = this.commonObjectCollection;
            // monitorenter(commonObjectCollection)
            while (true) {
                Label_0109: {
                    if (o == null) {
                        break Label_0109;
                    }
                    while (true) {
                        try {
                            final String string = o.toString();
                            HashSet<String> set;
                            if ((set = this.primaryTagMap.get(string)) == null) {
                                set = new HashSet<String>();
                                this.primaryTagMap.put(string, set);
                            }
                            for (int length = array.length, i = 0; i < length; ++i) {
                                set.add(array[i]);
                            }
                            this.isCommonObjectsCollectionDirty = true;
                            // monitorexit(commonObjectCollection)
                            return;
                            // monitorexit(commonObjectCollection)
                            throw;
                        }
                        finally {
                            continue;
                        }
                        break;
                    }
                }
                final String string = null;
                continue;
            }
        }
        
        public List<Object> getAllWhere(final TagPredicate tagPredicate) {
            while (true) {
                final ArrayList<Object> list = new ArrayList<Object>();
                while (true) {
                    Label_0139: {
                        synchronized (this.commonObjectCollection) {
                            final boolean isCommonObjectsCollectionDirty = this.isCommonObjectsCollectionDirty;
                            this.isCommonObjectsCollectionDirty = false;
                            final Iterator<Map.Entry<Object, HashSet<String>>> iterator = this.commonObjectCollection.entrySet().iterator();
                            if (!iterator.hasNext()) {
                                return list;
                            }
                            final Map.Entry<Object, HashSet<String>> entry = iterator.next();
                            if (isCommonObjectsCollectionDirty) {
                                entry.setValue(this.getTags(entry.getKey()));
                            }
                            if (tagPredicate.check(entry.getKey(), entry.getValue())) {
                                list.add(entry.getKey());
                                break Label_0139;
                            }
                            break Label_0139;
                        }
                    }
                    continue;
                }
            }
        }
        
        public List<Object> getAllWithTag(final String s) {
            return this.getAllWhere(new TagPredicate() {
                @Override
                public boolean check(final Object o, final Collection<String> collection) {
                    return collection.contains(s);
                }
            });
        }
        
        public List<Object> getAllWithTags(final Collection<String> collection) {
            return this.getAllWhere(new TagPredicate() {
                @Override
                public boolean check(final Object o, final Collection<String> collection) {
                    final Iterator<String> iterator = collection.iterator();
                    while (iterator.hasNext()) {
                        if (!collection.contains(iterator.next())) {
                            return false;
                        }
                    }
                    return true;
                }
            });
        }
        
        public HashSet<String> getTags(final Object o) {
            final HashSet<String> set = new HashSet<String>();
            this.addTags(o, set);
            return set;
        }
        
        public void removeCommonObject(final Object o) {
            synchronized (this.commonObjectCollection) {
                this.commonObjectCollection.remove(o);
            }
        }
        
        public void removeTagsFor(final Object o, final String... array) {
            final HashMap<Object, HashSet<String>> commonObjectCollection = this.commonObjectCollection;
            // monitorenter(commonObjectCollection)
            while (true) {
                Label_0082: {
                    if (o == null) {
                        break Label_0082;
                    }
                    while (true) {
                        try {
                            final String string = o.toString();
                            final HashSet<String> set = this.primaryTagMap.get(string);
                            if (set != null) {
                                for (int length = array.length, i = 0; i < length; ++i) {
                                    set.remove(array[i]);
                                }
                            }
                            this.isCommonObjectsCollectionDirty = true;
                            // monitorexit(commonObjectCollection)
                            return;
                            // monitorexit(commonObjectCollection)
                            throw;
                        }
                        finally {
                            continue;
                        }
                        break;
                    }
                }
                final String string = null;
                continue;
            }
        }
    }
    
    public interface TagPredicate
    {
        boolean check(final Object p0, final Collection<String> p1);
    }
}
