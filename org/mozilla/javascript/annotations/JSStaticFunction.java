package org.mozilla.javascript.annotations;

import java.lang.annotation.*;

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ ElementType.METHOD })
public @interface JSStaticFunction {
    String value() default "";
}
