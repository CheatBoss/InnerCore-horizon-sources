package org.spongycastle.crypto.digests;

import org.spongycastle.crypto.engines.*;
import org.spongycastle.util.*;
import java.util.*;
import org.spongycastle.crypto.*;
import org.spongycastle.crypto.params.*;

public class SkeinEngine implements Memoable
{
    private static final Hashtable INITIAL_STATES;
    private static final int PARAM_TYPE_CONFIG = 4;
    private static final int PARAM_TYPE_KEY = 0;
    private static final int PARAM_TYPE_MESSAGE = 48;
    private static final int PARAM_TYPE_OUTPUT = 63;
    public static final int SKEIN_1024 = 1024;
    public static final int SKEIN_256 = 256;
    public static final int SKEIN_512 = 512;
    long[] chain;
    private long[] initialState;
    private byte[] key;
    private final int outputSizeBytes;
    private Parameter[] postMessageParameters;
    private Parameter[] preMessageParameters;
    private final byte[] singleByte;
    final ThreefishEngine threefish;
    private final UBI ubi;
    
    static {
        INITIAL_STATES = new Hashtable();
        initialState(256, 128, new long[] { -2228972824489528736L, -8629553674646093540L, 1155188648486244218L, -3677226592081559102L });
        initialState(256, 160, new long[] { 1450197650740764312L, 3081844928540042640L, -3136097061834271170L, 3301952811952417661L });
        initialState(256, 224, new long[] { -4176654842910610933L, -8688192972455077604L, -7364642305011795836L, 4056579644589979102L });
        initialState(256, 256, new long[] { -243853671043386295L, 3443677322885453875L, -5531612722399640561L, 7662005193972177513L });
        initialState(512, 128, new long[] { -6288014694233956526L, 2204638249859346602L, 3502419045458743507L, -4829063503441264548L, 983504137758028059L, 1880512238245786339L, -6715892782214108542L, 7602827311880509485L });
        initialState(512, 160, new long[] { 2934123928682216849L, -4399710721982728305L, 1684584802963255058L, 5744138295201861711L, 2444857010922934358L, -2807833639722848072L, -5121587834665610502L, 118355523173251694L });
        initialState(512, 224, new long[] { -3688341020067007964L, -3772225436291745297L, -8300862168937575580L, 4146387520469897396L, 1106145742801415120L, 7455425944880474941L, -7351063101234211863L, -7048981346965512457L });
        initialState(512, 384, new long[] { -6631894876634615969L, -5692838220127733084L, -7099962856338682626L, -2911352911530754598L, 2000907093792408677L, 9140007292425499655L, 6093301768906360022L, 2769176472213098488L });
        initialState(512, 512, new long[] { 5261240102383538638L, 978932832955457283L, -8083517948103779378L, -7339365279355032399L, 6752626034097301424L, -1531723821829733388L, -7417126464950782685L, -5901786942805128141L });
    }
    
    public SkeinEngine(final int n, final int n2) {
        this.singleByte = new byte[1];
        if (n2 % 8 == 0) {
            this.outputSizeBytes = n2 / 8;
            this.threefish = new ThreefishEngine(n);
            this.ubi = new UBI(this.threefish.getBlockSize());
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Output size must be a multiple of 8 bits. :");
        sb.append(n2);
        throw new IllegalArgumentException(sb.toString());
    }
    
    public SkeinEngine(final SkeinEngine skeinEngine) {
        this(skeinEngine.getBlockSize() * 8, skeinEngine.getOutputSize() * 8);
        this.copyIn(skeinEngine);
    }
    
    private void checkInitialised() {
        if (this.ubi != null) {
            return;
        }
        throw new IllegalArgumentException("Skein engine is not initialised.");
    }
    
    private static Parameter[] clone(final Parameter[] array, final Parameter[] array2) {
        if (array == null) {
            return null;
        }
        Parameter[] array3 = null;
        Label_0025: {
            if (array2 != null) {
                array3 = array2;
                if (array2.length == array.length) {
                    break Label_0025;
                }
            }
            array3 = new Parameter[array.length];
        }
        System.arraycopy(array, 0, array3, 0, array3.length);
        return array3;
    }
    
    private void copyIn(final SkeinEngine skeinEngine) {
        this.ubi.reset(skeinEngine.ubi);
        this.chain = Arrays.clone(skeinEngine.chain, this.chain);
        this.initialState = Arrays.clone(skeinEngine.initialState, this.initialState);
        this.key = Arrays.clone(skeinEngine.key, this.key);
        this.preMessageParameters = clone(skeinEngine.preMessageParameters, this.preMessageParameters);
        this.postMessageParameters = clone(skeinEngine.postMessageParameters, this.postMessageParameters);
    }
    
    private void createInitialState() {
        final long[] array = SkeinEngine.INITIAL_STATES.get(variantIdentifier(this.getBlockSize(), this.getOutputSize()));
        final byte[] key = this.key;
        int n = 0;
        if (key == null && array != null) {
            this.chain = Arrays.clone(array);
        }
        else {
            this.chain = new long[this.getBlockSize() / 8];
            final byte[] key2 = this.key;
            if (key2 != null) {
                this.ubiComplete(0, key2);
            }
            this.ubiComplete(4, new Configuration(this.outputSizeBytes * 8).getBytes());
        }
        if (this.preMessageParameters != null) {
            while (true) {
                final Parameter[] preMessageParameters = this.preMessageParameters;
                if (n >= preMessageParameters.length) {
                    break;
                }
                final Parameter parameter = preMessageParameters[n];
                this.ubiComplete(parameter.getType(), parameter.getValue());
                ++n;
            }
        }
        this.initialState = Arrays.clone(this.chain);
    }
    
    private void initParams(final Hashtable hashtable) {
        final Enumeration<Integer> keys = hashtable.keys();
        final Vector<Parameter> vector = new Vector<Parameter>();
        final Vector<Parameter> vector2 = new Vector<Parameter>();
        while (keys.hasMoreElements()) {
            final Integer n = keys.nextElement();
            final byte[] key = (Object)hashtable.get(n);
            if (n == 0) {
                this.key = key;
            }
            else if (n < 48) {
                vector.addElement(new Parameter(n, key));
            }
            else {
                vector2.addElement(new Parameter(n, key));
            }
        }
        vector.copyInto(this.preMessageParameters = new Parameter[vector.size()]);
        sort(this.preMessageParameters);
        vector2.copyInto(this.postMessageParameters = new Parameter[vector2.size()]);
        sort(this.postMessageParameters);
    }
    
    private static void initialState(final int n, final int n2, final long[] array) {
        SkeinEngine.INITIAL_STATES.put(variantIdentifier(n / 8, n2 / 8), array);
    }
    
    private void output(final long n, final byte[] array, final int n2, final int n3) {
        final byte[] array2 = new byte[8];
        ThreefishEngine.wordToBytes(n, array2, 0);
        final long[] array3 = new long[this.chain.length];
        this.ubiInit(63);
        this.ubi.update(array2, 0, 8, array3);
        this.ubi.doFinal(array3);
        for (int n4 = (n3 + 8 - 1) / 8, i = 0; i < n4; ++i) {
            final int n5 = i * 8;
            final int min = Math.min(8, n3 - n5);
            if (min == 8) {
                ThreefishEngine.wordToBytes(array3[i], array, n5 + n2);
            }
            else {
                ThreefishEngine.wordToBytes(array3[i], array2, 0);
                System.arraycopy(array2, 0, array, n5 + n2, min);
            }
        }
    }
    
    private static void sort(final Parameter[] array) {
        if (array == null) {
            return;
        }
        for (int i = 1; i < array.length; ++i) {
            final Parameter parameter = array[i];
            int j;
            int n;
            for (j = i; j > 0; j = n) {
                final int type = parameter.getType();
                n = j - 1;
                if (type >= array[n].getType()) {
                    break;
                }
                array[j] = array[n];
            }
            array[j] = parameter;
        }
    }
    
    private void ubiComplete(final int n, final byte[] array) {
        this.ubiInit(n);
        this.ubi.update(array, 0, array.length, this.chain);
        this.ubiFinal();
    }
    
    private void ubiFinal() {
        this.ubi.doFinal(this.chain);
    }
    
    private void ubiInit(final int n) {
        this.ubi.reset(n);
    }
    
    private static Integer variantIdentifier(final int n, final int n2) {
        return new Integer(n | n2 << 16);
    }
    
    @Override
    public Memoable copy() {
        return new SkeinEngine(this);
    }
    
    public int doFinal(final byte[] array, final int n) {
        this.checkInitialised();
        if (array.length >= this.outputSizeBytes + n) {
            this.ubiFinal();
            final Parameter[] postMessageParameters = this.postMessageParameters;
            final int n2 = 0;
            if (postMessageParameters != null) {
                int n3 = 0;
                while (true) {
                    final Parameter[] postMessageParameters2 = this.postMessageParameters;
                    if (n3 >= postMessageParameters2.length) {
                        break;
                    }
                    final Parameter parameter = postMessageParameters2[n3];
                    this.ubiComplete(parameter.getType(), parameter.getValue());
                    ++n3;
                }
            }
            final int blockSize = this.getBlockSize();
            for (int n4 = (this.outputSizeBytes + blockSize - 1) / blockSize, i = n2; i < n4; ++i) {
                final int outputSizeBytes = this.outputSizeBytes;
                final int n5 = i * blockSize;
                this.output(i, array, n + n5, Math.min(blockSize, outputSizeBytes - n5));
            }
            this.reset();
            return this.outputSizeBytes;
        }
        throw new OutputLengthException("Output buffer is too short to hold output");
    }
    
    public int getBlockSize() {
        return this.threefish.getBlockSize();
    }
    
    public int getOutputSize() {
        return this.outputSizeBytes;
    }
    
    public void init(final SkeinParameters skeinParameters) {
        this.chain = null;
        this.key = null;
        this.preMessageParameters = null;
        this.postMessageParameters = null;
        if (skeinParameters != null) {
            if (skeinParameters.getKey().length < 16) {
                throw new IllegalArgumentException("Skein key must be at least 128 bits.");
            }
            this.initParams(skeinParameters.getParameters());
        }
        this.createInitialState();
        this.ubiInit(48);
    }
    
    public void reset() {
        final long[] initialState = this.initialState;
        final long[] chain = this.chain;
        System.arraycopy(initialState, 0, chain, 0, chain.length);
        this.ubiInit(48);
    }
    
    @Override
    public void reset(final Memoable memoable) {
        final SkeinEngine skeinEngine = (SkeinEngine)memoable;
        if (this.getBlockSize() == skeinEngine.getBlockSize() && this.outputSizeBytes == skeinEngine.outputSizeBytes) {
            this.copyIn(skeinEngine);
            return;
        }
        throw new IllegalArgumentException("Incompatible parameters in provided SkeinEngine.");
    }
    
    public void update(final byte b) {
        final byte[] singleByte = this.singleByte;
        singleByte[0] = b;
        this.update(singleByte, 0, 1);
    }
    
    public void update(final byte[] array, final int n, final int n2) {
        this.checkInitialised();
        this.ubi.update(array, n, n2, this.chain);
    }
    
    private static class Configuration
    {
        private byte[] bytes;
        
        public Configuration(final long n) {
            final byte[] bytes = new byte[32];
            (this.bytes = bytes)[0] = 83;
            bytes[1] = 72;
            bytes[2] = 65;
            bytes[3] = 51;
            bytes[4] = 1;
            bytes[5] = 0;
            ThreefishEngine.wordToBytes(n, bytes, 8);
        }
        
        public byte[] getBytes() {
            return this.bytes;
        }
    }
    
    public static class Parameter
    {
        private int type;
        private byte[] value;
        
        public Parameter(final int type, final byte[] value) {
            this.type = type;
            this.value = value;
        }
        
        public int getType() {
            return this.type;
        }
        
        public byte[] getValue() {
            return this.value;
        }
    }
    
    private class UBI
    {
        private byte[] currentBlock;
        private int currentOffset;
        private long[] message;
        private final UbiTweak tweak;
        
        public UBI(final int n) {
            this.tweak = new UbiTweak();
            final byte[] currentBlock = new byte[n];
            this.currentBlock = currentBlock;
            this.message = new long[currentBlock.length / 8];
        }
        
        private void processBlock(final long[] array) {
            SkeinEngine.this.threefish.init(true, SkeinEngine.this.chain, this.tweak.getWords());
            final int n = 0;
            int n2 = 0;
            while (true) {
                final long[] message = this.message;
                if (n2 >= message.length) {
                    break;
                }
                message[n2] = ThreefishEngine.bytesToWord(this.currentBlock, n2 * 8);
                ++n2;
            }
            SkeinEngine.this.threefish.processBlock(this.message, array);
            for (int i = n; i < array.length; ++i) {
                array[i] ^= this.message[i];
            }
        }
        
        public void doFinal(final long[] array) {
            int currentOffset = this.currentOffset;
            while (true) {
                final byte[] currentBlock = this.currentBlock;
                if (currentOffset >= currentBlock.length) {
                    break;
                }
                currentBlock[currentOffset] = 0;
                ++currentOffset;
            }
            this.tweak.setFinal(true);
            this.processBlock(array);
        }
        
        public void reset(final int type) {
            this.tweak.reset();
            this.tweak.setType(type);
            this.currentOffset = 0;
        }
        
        public void reset(final UBI ubi) {
            this.currentBlock = Arrays.clone(ubi.currentBlock, this.currentBlock);
            this.currentOffset = ubi.currentOffset;
            this.message = Arrays.clone(ubi.message, this.message);
            this.tweak.reset(ubi.tweak);
        }
        
        public void update(final byte[] array, final int n, final int i, final long[] array2) {
            int n2 = 0;
            while (i > n2) {
                if (this.currentOffset == this.currentBlock.length) {
                    this.processBlock(array2);
                    this.tweak.setFirst(false);
                    this.currentOffset = 0;
                }
                final int min = Math.min(i - n2, this.currentBlock.length - this.currentOffset);
                System.arraycopy(array, n + n2, this.currentBlock, this.currentOffset, min);
                n2 += min;
                this.currentOffset += min;
                this.tweak.advancePosition(min);
            }
        }
    }
    
    private static class UbiTweak
    {
        private static final long LOW_RANGE = 9223372034707292160L;
        private static final long T1_FINAL = Long.MIN_VALUE;
        private static final long T1_FIRST = 4611686018427387904L;
        private boolean extendedPosition;
        private long[] tweak;
        
        public UbiTweak() {
            this.tweak = new long[2];
            this.reset();
        }
        
        public void advancePosition(int i) {
            if (this.extendedPosition) {
                final long[] array = new long[3];
                final long[] tweak = this.tweak;
                array[0] = (tweak[0] & 0xFFFFFFFFL);
                array[1] = (tweak[0] >>> 32 & 0xFFFFFFFFL);
                array[2] = (tweak[1] & 0xFFFFFFFFL);
                long n = i;
                long n2;
                for (i = 0; i < 3; ++i) {
                    n2 = n + array[i];
                    array[i] = n2;
                    n = n2 >>> 32;
                }
                final long[] tweak2 = this.tweak;
                tweak2[0] = ((array[1] & 0xFFFFFFFFL) << 32 | (array[0] & 0xFFFFFFFFL));
                tweak2[1] = ((array[2] & 0xFFFFFFFFL) | (tweak2[1] & 0xFFFFFFFF00000000L));
                return;
            }
            final long[] tweak3 = this.tweak;
            final long n3 = tweak3[0] + i;
            tweak3[0] = n3;
            if (n3 > 9223372034707292160L) {
                this.extendedPosition = true;
            }
        }
        
        public int getType() {
            return (int)(this.tweak[1] >>> 56 & 0x3FL);
        }
        
        public long[] getWords() {
            return this.tweak;
        }
        
        public boolean isFinal() {
            return (this.tweak[1] & Long.MIN_VALUE) != 0x0L;
        }
        
        public boolean isFirst() {
            return (this.tweak[1] & 0x4000000000000000L) != 0x0L;
        }
        
        public void reset() {
            final long[] tweak = this.tweak;
            tweak[1] = (tweak[0] = 0L);
            this.extendedPosition = false;
            this.setFirst(true);
        }
        
        public void reset(final UbiTweak ubiTweak) {
            this.tweak = Arrays.clone(ubiTweak.tweak, this.tweak);
            this.extendedPosition = ubiTweak.extendedPosition;
        }
        
        public void setFinal(final boolean b) {
            if (b) {
                final long[] tweak = this.tweak;
                tweak[1] |= Long.MIN_VALUE;
                return;
            }
            final long[] tweak2 = this.tweak;
            tweak2[1] &= Long.MAX_VALUE;
        }
        
        public void setFirst(final boolean b) {
            if (b) {
                final long[] tweak = this.tweak;
                tweak[1] |= 0x4000000000000000L;
                return;
            }
            final long[] tweak2 = this.tweak;
            tweak2[1] &= 0xBFFFFFFFFFFFFFFFL;
        }
        
        public void setType(final int n) {
            final long[] tweak = this.tweak;
            tweak[1] = ((tweak[1] & 0xFFFFFFC000000000L) | ((long)n & 0x3FL) << 56);
        }
        
        @Override
        public String toString() {
            final StringBuilder sb = new StringBuilder();
            sb.append(this.getType());
            sb.append(" first: ");
            sb.append(this.isFirst());
            sb.append(", final: ");
            sb.append(this.isFinal());
            return sb.toString();
        }
    }
}
