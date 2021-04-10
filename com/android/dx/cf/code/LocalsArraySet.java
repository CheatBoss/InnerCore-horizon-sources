package com.android.dx.cf.code;

import com.android.dx.util.*;
import com.android.dex.util.*;
import java.util.*;
import com.android.dx.rop.type.*;
import com.android.dx.rop.code.*;

public class LocalsArraySet extends LocalsArray
{
    private final OneLocalsArray primary;
    private final ArrayList<LocalsArray> secondaries;
    
    public LocalsArraySet(final int n) {
        super(n != 0);
        this.primary = new OneLocalsArray(n);
        this.secondaries = new ArrayList<LocalsArray>();
    }
    
    private LocalsArraySet(final LocalsArraySet set) {
        final int maxLocals = set.getMaxLocals();
        int i = 0;
        super(maxLocals > 0);
        this.primary = set.primary.copy();
        this.secondaries = new ArrayList<LocalsArray>(set.secondaries.size());
        while (i < set.secondaries.size()) {
            final LocalsArray localsArray = set.secondaries.get(i);
            if (localsArray == null) {
                this.secondaries.add(null);
            }
            else {
                this.secondaries.add(localsArray.copy());
            }
            ++i;
        }
    }
    
    public LocalsArraySet(final OneLocalsArray primary, final ArrayList<LocalsArray> secondaries) {
        super(primary.getMaxLocals() > 0);
        this.primary = primary;
        this.secondaries = secondaries;
    }
    
    private LocalsArray getSecondaryForLabel(final int n) {
        if (n >= this.secondaries.size()) {
            return null;
        }
        return this.secondaries.get(n);
    }
    
    private LocalsArraySet mergeWithOne(final OneLocalsArray oneLocalsArray) {
        final OneLocalsArray merge = this.primary.merge(oneLocalsArray.getPrimary());
        final ArrayList<LocalsArray> list = new ArrayList<LocalsArray>(this.secondaries.size());
        final int size = this.secondaries.size();
        int n = 0;
        for (int i = 0; i < size; ++i) {
            final LocalsArray localsArray = this.secondaries.get(i);
            LocalsArray merge2;
            final LocalsArray localsArray2 = merge2 = null;
            if (localsArray != null) {
                try {
                    merge2 = localsArray.merge(oneLocalsArray);
                }
                catch (SimException ex) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("Merging one locals against caller block ");
                    sb.append(Hex.u2(i));
                    ex.addContext(sb.toString());
                    merge2 = localsArray2;
                }
            }
            if (n == 0 && localsArray == merge2) {
                n = 0;
            }
            else {
                n = 1;
            }
            list.add(merge2);
        }
        if (this.primary == merge && n == 0) {
            return this;
        }
        return new LocalsArraySet(merge, list);
    }
    
    private LocalsArraySet mergeWithSet(final LocalsArraySet set) {
        final OneLocalsArray merge = this.primary.merge(set.getPrimary());
        final int size = this.secondaries.size();
        final int size2 = set.secondaries.size();
        final int max = Math.max(size, size2);
        final ArrayList list = new ArrayList<LocalsArray>(max);
        int n = 0;
        for (int i = 0; i < max; ++i) {
            LocalsArray merge2 = null;
            LocalsArray localsArray;
            if (i < size) {
                localsArray = this.secondaries.get(i);
            }
            else {
                localsArray = null;
            }
            if (i < size2) {
                merge2 = set.secondaries.get(i);
            }
            final LocalsArray localsArray2 = null;
            if (localsArray == merge2) {
                merge2 = localsArray;
            }
            else if (localsArray != null) {
                if (merge2 == null) {
                    merge2 = localsArray;
                }
                else {
                    try {
                        merge2 = localsArray.merge(merge2);
                    }
                    catch (SimException ex) {
                        final StringBuilder sb = new StringBuilder();
                        sb.append("Merging locals set for caller block ");
                        sb.append(Hex.u2(i));
                        ex.addContext(sb.toString());
                        merge2 = localsArray2;
                    }
                }
            }
            if (n == 0 && localsArray == merge2) {
                n = 0;
            }
            else {
                n = 1;
            }
            list.add(merge2);
        }
        if (this.primary == merge && n == 0) {
            return this;
        }
        return new LocalsArraySet(merge, (ArrayList<LocalsArray>)list);
    }
    
    @Override
    public void annotate(final ExceptionWithContext exceptionWithContext) {
        exceptionWithContext.addContext("(locals array set; primary)");
        this.primary.annotate(exceptionWithContext);
        for (int size = this.secondaries.size(), i = 0; i < size; ++i) {
            final LocalsArray localsArray = this.secondaries.get(i);
            if (localsArray != null) {
                final StringBuilder sb = new StringBuilder();
                sb.append("(locals array set: primary for caller ");
                sb.append(Hex.u2(i));
                sb.append(')');
                exceptionWithContext.addContext(sb.toString());
                localsArray.getPrimary().annotate(exceptionWithContext);
            }
        }
    }
    
    @Override
    public LocalsArray copy() {
        return new LocalsArraySet(this);
    }
    
    @Override
    public TypeBearer get(final int n) {
        return this.primary.get(n);
    }
    
    @Override
    public TypeBearer getCategory1(final int n) {
        return this.primary.getCategory1(n);
    }
    
    @Override
    public TypeBearer getCategory2(final int n) {
        return this.primary.getCategory2(n);
    }
    
    @Override
    public int getMaxLocals() {
        return this.primary.getMaxLocals();
    }
    
    @Override
    public TypeBearer getOrNull(final int n) {
        return this.primary.getOrNull(n);
    }
    
    @Override
    protected OneLocalsArray getPrimary() {
        return this.primary;
    }
    
    @Override
    public void invalidate(final int n) {
        this.throwIfImmutable();
        this.primary.invalidate(n);
        for (final LocalsArray localsArray : this.secondaries) {
            if (localsArray != null) {
                localsArray.invalidate(n);
            }
        }
    }
    
    @Override
    public void makeInitialized(final Type type) {
        if (this.primary.getMaxLocals() == 0) {
            return;
        }
        this.throwIfImmutable();
        this.primary.makeInitialized(type);
        for (final LocalsArray localsArray : this.secondaries) {
            if (localsArray != null) {
                localsArray.makeInitialized(type);
            }
        }
    }
    
    @Override
    public LocalsArraySet merge(LocalsArray localsArray) {
        try {
            if (localsArray instanceof LocalsArraySet) {
                localsArray = this.mergeWithSet((LocalsArraySet)localsArray);
            }
            else {
                localsArray = this.mergeWithOne((OneLocalsArray)localsArray);
            }
            ((LocalsArraySet)localsArray).setImmutable();
            return (LocalsArraySet)localsArray;
        }
        catch (SimException ex) {
            ex.addContext("underlay locals:");
            this.annotate(ex);
            ex.addContext("overlay locals:");
            localsArray.annotate(ex);
            throw ex;
        }
    }
    
    @Override
    public LocalsArraySet mergeWithSubroutineCaller(LocalsArray merge, final int n) {
        final LocalsArray secondaryForLabel = this.getSecondaryForLabel(n);
        final OneLocalsArray merge2 = this.primary.merge(merge.getPrimary());
        if (secondaryForLabel == merge) {
            merge = secondaryForLabel;
        }
        else if (secondaryForLabel != null) {
            merge = secondaryForLabel.merge(merge);
        }
        if (merge == secondaryForLabel && merge2 == this.primary) {
            return this;
        }
        OneLocalsArray oneLocalsArray = null;
        final int size = this.secondaries.size();
        final int max = Math.max(n + 1, size);
        final ArrayList list = new ArrayList<LocalsArray>(max);
        OneLocalsArray oneLocalsArray2;
        for (int i = 0; i < max; ++i, oneLocalsArray = oneLocalsArray2) {
            LocalsArray localsArray = null;
            if (i == n) {
                localsArray = merge;
            }
            else if (i < size) {
                localsArray = this.secondaries.get(i);
            }
            oneLocalsArray2 = oneLocalsArray;
            if (localsArray != null) {
                if (oneLocalsArray == null) {
                    oneLocalsArray2 = localsArray.getPrimary();
                }
                else {
                    oneLocalsArray2 = oneLocalsArray.merge(localsArray.getPrimary());
                }
            }
            list.add(localsArray);
        }
        final LocalsArraySet set = new LocalsArraySet(oneLocalsArray, (ArrayList<LocalsArray>)list);
        set.setImmutable();
        return set;
    }
    
    @Override
    public void set(final int n, final TypeBearer typeBearer) {
        this.throwIfImmutable();
        this.primary.set(n, typeBearer);
        for (final LocalsArray localsArray : this.secondaries) {
            if (localsArray != null) {
                localsArray.set(n, typeBearer);
            }
        }
    }
    
    @Override
    public void set(final RegisterSpec registerSpec) {
        this.set(registerSpec.getReg(), registerSpec);
    }
    
    @Override
    public void setImmutable() {
        this.primary.setImmutable();
        for (final LocalsArray localsArray : this.secondaries) {
            if (localsArray != null) {
                localsArray.setImmutable();
            }
        }
        super.setImmutable();
    }
    
    public LocalsArray subArrayForLabel(final int n) {
        return this.getSecondaryForLabel(n);
    }
    
    @Override
    public String toHuman() {
        final StringBuilder sb = new StringBuilder();
        sb.append("(locals array set; primary)\n");
        sb.append(this.getPrimary().toHuman());
        sb.append('\n');
        for (int size = this.secondaries.size(), i = 0; i < size; ++i) {
            final LocalsArray localsArray = this.secondaries.get(i);
            if (localsArray != null) {
                final StringBuilder sb2 = new StringBuilder();
                sb2.append("(locals array set: primary for caller ");
                sb2.append(Hex.u2(i));
                sb2.append(")\n");
                sb.append(sb2.toString());
                sb.append(localsArray.getPrimary().toHuman());
                sb.append('\n');
            }
        }
        return sb.toString();
    }
}
