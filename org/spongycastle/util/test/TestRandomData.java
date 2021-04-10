package org.spongycastle.util.test;

import org.spongycastle.util.encoders.*;

public class TestRandomData extends FixedSecureRandom
{
    public TestRandomData(final String s) {
        super(new Source[] { new Data(Hex.decode(s)) });
    }
    
    public TestRandomData(final byte[] array) {
        super(new Source[] { new Data(array) });
    }
}
