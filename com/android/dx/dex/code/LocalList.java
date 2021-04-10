package com.android.dx.dex.code;

import com.android.dx.util.*;
import java.io.*;
import com.android.dx.rop.cst.*;
import com.android.dx.rop.code.*;
import com.android.dx.rop.type.*;
import java.util.*;

public final class LocalList extends FixedSizeList
{
    private static final boolean DEBUG = false;
    public static final LocalList EMPTY;
    
    static {
        EMPTY = new LocalList(0);
    }
    
    public LocalList(final int n) {
        super(n);
    }
    
    private static void debugVerify(final LocalList list) {
        try {
            debugVerify0(list);
        }
        catch (RuntimeException ex) {
            for (int size = list.size(), i = 0; i < size; ++i) {
                System.err.println(list.get(i));
            }
            throw ex;
        }
    }
    
    private static void debugVerify0(final LocalList list) {
        final int size = list.size();
        final Entry[] array = new Entry[65536];
        for (int i = 0; i < size; ++i) {
            final Entry value = list.get(i);
            final int register = value.getRegister();
            if (value.isStart()) {
                final Entry entry = array[register];
                if (entry != null && value.matches(entry)) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("redundant start at ");
                    sb.append(Integer.toHexString(value.getAddress()));
                    sb.append(": got ");
                    sb.append(value);
                    sb.append("; had ");
                    sb.append(entry);
                    throw new RuntimeException(sb.toString());
                }
                array[register] = value;
            }
            else {
                if (array[register] == null) {
                    final StringBuilder sb2 = new StringBuilder();
                    sb2.append("redundant end at ");
                    sb2.append(Integer.toHexString(value.getAddress()));
                    throw new RuntimeException(sb2.toString());
                }
                final int address = value.getAddress();
                boolean b = false;
                for (int j = i + 1; j < size; ++j) {
                    final Entry value2 = list.get(j);
                    if (value2.getAddress() != address) {
                        break;
                    }
                    if (value2.getRegisterSpec().getReg() == register) {
                        if (!value2.isStart()) {
                            final StringBuilder sb3 = new StringBuilder();
                            sb3.append("redundant end at ");
                            sb3.append(Integer.toHexString(address));
                            throw new RuntimeException(sb3.toString());
                        }
                        if (value.getDisposition() != Disposition.END_REPLACED) {
                            final StringBuilder sb4 = new StringBuilder();
                            sb4.append("improperly marked end at ");
                            sb4.append(Integer.toHexString(address));
                            throw new RuntimeException(sb4.toString());
                        }
                        b = true;
                    }
                }
                if (!b && value.getDisposition() == Disposition.END_REPLACED) {
                    final StringBuilder sb5 = new StringBuilder();
                    sb5.append("improper end replacement claim at ");
                    sb5.append(Integer.toHexString(address));
                    throw new RuntimeException(sb5.toString());
                }
                array[register] = null;
            }
        }
    }
    
    public static LocalList make(final DalvInsnList list) {
        final int size = list.size();
        final MakeState makeState = new MakeState(size);
        for (int i = 0; i < size; ++i) {
            final DalvInsn value = list.get(i);
            if (value instanceof LocalSnapshot) {
                makeState.snapshot(value.getAddress(), ((LocalSnapshot)value).getLocals());
            }
            else if (value instanceof LocalStart) {
                makeState.startLocal(value.getAddress(), ((LocalStart)value).getLocal());
            }
        }
        return makeState.finish();
    }
    
    public void debugPrint(final PrintStream printStream, final String s) {
        for (int size = this.size(), i = 0; i < size; ++i) {
            printStream.print(s);
            printStream.println(this.get(i));
        }
    }
    
    public Entry get(final int n) {
        return (Entry)this.get0(n);
    }
    
    public void set(final int n, final Entry entry) {
        this.set0(n, entry);
    }
    
    public enum Disposition
    {
        END_CLOBBERED_BY_NEXT, 
        END_CLOBBERED_BY_PREV, 
        END_MOVED, 
        END_REPLACED, 
        END_SIMPLY, 
        START;
    }
    
    public static class Entry implements Comparable<Entry>
    {
        private final int address;
        private final Disposition disposition;
        private final RegisterSpec spec;
        private final CstType type;
        
        public Entry(final int address, final Disposition disposition, final RegisterSpec spec) {
            if (address < 0) {
                throw new IllegalArgumentException("address < 0");
            }
            if (disposition == null) {
                throw new NullPointerException("disposition == null");
            }
            try {
                if (spec.getLocalItem() == null) {
                    throw new NullPointerException("spec.getLocalItem() == null");
                }
                this.address = address;
                this.disposition = disposition;
                this.spec = spec;
                this.type = CstType.intern(spec.getType());
            }
            catch (NullPointerException ex) {
                throw new NullPointerException("spec == null");
            }
        }
        
        @Override
        public int compareTo(final Entry entry) {
            final int address = this.address;
            final int address2 = entry.address;
            int n = -1;
            if (address < address2) {
                return -1;
            }
            if (this.address > entry.address) {
                return 1;
            }
            final boolean start = this.isStart();
            if (start != entry.isStart()) {
                if (start) {
                    n = 1;
                }
                return n;
            }
            return this.spec.compareTo(entry.spec);
        }
        
        @Override
        public boolean equals(final Object o) {
            final boolean b = o instanceof Entry;
            boolean b2 = false;
            if (!b) {
                return false;
            }
            if (this.compareTo((Entry)o) == 0) {
                b2 = true;
            }
            return b2;
        }
        
        public int getAddress() {
            return this.address;
        }
        
        public Disposition getDisposition() {
            return this.disposition;
        }
        
        public CstString getName() {
            return this.spec.getLocalItem().getName();
        }
        
        public int getRegister() {
            return this.spec.getReg();
        }
        
        public RegisterSpec getRegisterSpec() {
            return this.spec;
        }
        
        public CstString getSignature() {
            return this.spec.getLocalItem().getSignature();
        }
        
        public CstType getType() {
            return this.type;
        }
        
        public boolean isStart() {
            return this.disposition == Disposition.START;
        }
        
        public boolean matches(final Entry entry) {
            return this.matches(entry.spec);
        }
        
        public boolean matches(final RegisterSpec registerSpec) {
            return this.spec.equalsUsingSimpleType(registerSpec);
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append(Integer.toHexString(this.address));
            sb.append(" ");
            sb.append(this.disposition);
            sb.append(" ");
            sb.append(this.spec);
            return sb.toString();
        }
        
        public Entry withDisposition(final Disposition disposition) {
            if (disposition == this.disposition) {
                return this;
            }
            return new Entry(this.address, disposition, this.spec);
        }
    }
    
    public static class MakeState
    {
        private int[] endIndices;
        private int lastAddress;
        private int nullResultCount;
        private RegisterSpecSet regs;
        private final ArrayList<Entry> result;
        
        public MakeState(final int n) {
            this.result = new ArrayList<Entry>(n);
            this.nullResultCount = 0;
            this.regs = null;
            this.endIndices = null;
            this.lastAddress = 0;
        }
        
        private void aboutToProcess(int n, final int n2) {
            final boolean b = this.endIndices == null;
            if (n == this.lastAddress && !b) {
                return;
            }
            if (n < this.lastAddress) {
                throw new RuntimeException("shouldn't happen");
            }
            if (b || n2 >= this.endIndices.length) {
                n = n2 + 1;
                final RegisterSpecSet regs = new RegisterSpecSet(n);
                final int[] endIndices = new int[n];
                Arrays.fill(endIndices, -1);
                if (!b) {
                    regs.putAll(this.regs);
                    System.arraycopy(this.endIndices, 0, endIndices, 0, this.endIndices.length);
                }
                this.regs = regs;
                this.endIndices = endIndices;
            }
        }
        
        private void add(final int n, final Disposition disposition, final RegisterSpec registerSpec) {
            final int reg = registerSpec.getReg();
            this.result.add(new Entry(n, disposition, registerSpec));
            if (disposition == Disposition.START) {
                this.regs.put(registerSpec);
                this.endIndices[reg] = -1;
                return;
            }
            this.regs.remove(registerSpec);
            this.endIndices[reg] = this.result.size() - 1;
        }
        
        private void addOrUpdateEnd(final int n, final Disposition disposition, final RegisterSpec registerSpec) {
            if (disposition == Disposition.START) {
                throw new RuntimeException("shouldn't happen");
            }
            final int n2 = this.endIndices[registerSpec.getReg()];
            if (n2 >= 0) {
                final Entry entry = this.result.get(n2);
                if (entry.getAddress() == n && entry.getRegisterSpec().equals(registerSpec)) {
                    this.result.set(n2, entry.withDisposition(disposition));
                    this.regs.remove(registerSpec);
                    return;
                }
            }
            this.endLocal(n, registerSpec, disposition);
        }
        
        private boolean checkForEmptyRange(final int n, final RegisterSpec registerSpec) {
            int i;
            for (i = this.result.size() - 1; i >= 0; --i) {
                final Entry entry = this.result.get(i);
                if (entry != null) {
                    if (entry.getAddress() != n) {
                        return false;
                    }
                    if (entry.matches(registerSpec)) {
                        break;
                    }
                }
            }
            this.regs.remove(registerSpec);
            this.result.set(i, null);
            ++this.nullResultCount;
            final int reg = registerSpec.getReg();
            final boolean b = false;
            Entry entry2 = null;
            int n2;
            boolean b2;
            while (true) {
                n2 = i - 1;
                b2 = b;
                if (n2 < 0) {
                    break;
                }
                final Entry entry3 = this.result.get(n2);
                if (entry3 == null) {
                    i = n2;
                    entry2 = entry3;
                }
                else {
                    i = n2;
                    entry2 = entry3;
                    if (entry3.getRegisterSpec().getReg() == reg) {
                        b2 = true;
                        entry2 = entry3;
                        break;
                    }
                    continue;
                }
            }
            if (b2) {
                this.endIndices[reg] = n2;
                if (entry2.getAddress() == n) {
                    this.result.set(n2, entry2.withDisposition(Disposition.END_SIMPLY));
                }
            }
            return true;
        }
        
        private static RegisterSpec filterSpec(final RegisterSpec registerSpec) {
            if (registerSpec != null && registerSpec.getType() == Type.KNOWN_NULL) {
                return registerSpec.withType(Type.OBJECT);
            }
            return registerSpec;
        }
        
        public void endLocal(final int n, final RegisterSpec registerSpec) {
            this.endLocal(n, registerSpec, Disposition.END_SIMPLY);
        }
        
        public void endLocal(final int n, RegisterSpec filterSpec, final Disposition disposition) {
            final int reg = filterSpec.getReg();
            filterSpec = filterSpec(filterSpec);
            this.aboutToProcess(n, reg);
            if (this.endIndices[reg] >= 0) {
                return;
            }
            if (this.checkForEmptyRange(n, filterSpec)) {
                return;
            }
            this.add(n, disposition, filterSpec);
        }
        
        public LocalList finish() {
            final int n = 0;
            this.aboutToProcess(Integer.MAX_VALUE, 0);
            final int size = this.result.size();
            final int n2 = size - this.nullResultCount;
            if (n2 == 0) {
                return LocalList.EMPTY;
            }
            final Entry[] array = new Entry[n2];
            if (size == n2) {
                this.result.toArray(array);
            }
            else {
                int n3 = 0;
                for (final Entry entry : this.result) {
                    int n4 = n3;
                    if (entry != null) {
                        array[n3] = entry;
                        n4 = n3 + 1;
                    }
                    n3 = n4;
                }
            }
            Arrays.sort(array);
            final LocalList list = new LocalList(n2);
            for (int i = n; i < n2; ++i) {
                list.set(i, array[i]);
            }
            list.setImmutable();
            return list;
        }
        
        public void snapshot(final int n, final RegisterSpecSet set) {
            final int maxSize = set.getMaxSize();
            this.aboutToProcess(n, maxSize - 1);
            for (int i = 0; i < maxSize; ++i) {
                final RegisterSpec value = this.regs.get(i);
                final RegisterSpec filterSpec = filterSpec(set.get(i));
                if (value == null) {
                    if (filterSpec != null) {
                        this.startLocal(n, filterSpec);
                    }
                }
                else if (filterSpec == null) {
                    this.endLocal(n, value);
                }
                else if (!filterSpec.equalsUsingSimpleType(value)) {
                    this.endLocal(n, value);
                    this.startLocal(n, filterSpec);
                }
            }
        }
        
        public void startLocal(final int n, RegisterSpec filterSpec) {
            final int reg = filterSpec.getReg();
            filterSpec = filterSpec(filterSpec);
            this.aboutToProcess(n, reg);
            final RegisterSpec value = this.regs.get(reg);
            if (filterSpec.equalsUsingSimpleType(value)) {
                return;
            }
            final RegisterSpec matchingLocal = this.regs.findMatchingLocal(filterSpec);
            if (matchingLocal != null) {
                this.addOrUpdateEnd(n, Disposition.END_MOVED, matchingLocal);
            }
            final int n2 = this.endIndices[reg];
            if (value != null) {
                this.add(n, Disposition.END_REPLACED, value);
            }
            else if (n2 >= 0) {
                final Entry entry = this.result.get(n2);
                if (entry.getAddress() == n) {
                    if (entry.matches(filterSpec)) {
                        this.result.set(n2, null);
                        ++this.nullResultCount;
                        this.regs.put(filterSpec);
                        this.endIndices[reg] = -1;
                        return;
                    }
                    this.result.set(n2, entry.withDisposition(Disposition.END_REPLACED));
                }
            }
            if (reg > 0) {
                final RegisterSpec value2 = this.regs.get(reg - 1);
                if (value2 != null && value2.isCategory2()) {
                    this.addOrUpdateEnd(n, Disposition.END_CLOBBERED_BY_NEXT, value2);
                }
            }
            if (filterSpec.isCategory2()) {
                final RegisterSpec value3 = this.regs.get(reg + 1);
                if (value3 != null) {
                    this.addOrUpdateEnd(n, Disposition.END_CLOBBERED_BY_PREV, value3);
                }
            }
            this.add(n, Disposition.START, filterSpec);
        }
    }
}
