package org.spongycastle.jce.interfaces;

import javax.crypto.interfaces.*;
import org.spongycastle.jce.spec.*;

public interface ElGamalKey extends DHKey
{
    ElGamalParameterSpec getParameters();
}
