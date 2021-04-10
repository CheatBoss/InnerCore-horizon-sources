package com.zhekasmirnov.innercore.api.annotations;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface DeprecatedAPIMethod {
}
