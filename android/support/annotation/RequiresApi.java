package android.support.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.CLASS)
@Target({ ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.FIELD })
public @interface RequiresApi {
    @IntRange(from = 1L)
    int api() default 1;
    
    @IntRange(from = 1L)
    int value() default 1;
}
