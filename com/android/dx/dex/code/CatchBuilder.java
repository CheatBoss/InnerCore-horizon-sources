package com.android.dx.dex.code;

import java.util.*;
import com.android.dx.rop.type.*;

public interface CatchBuilder
{
    CatchTable build();
    
    HashSet<Type> getCatchTypes();
    
    boolean hasAnyCatches();
}
