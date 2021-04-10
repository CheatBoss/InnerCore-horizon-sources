package android.support.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.SOURCE)
@Target({ ElementType.ANNOTATION_TYPE })
public @interface IntDef {
    boolean flag() default false;
    
    long[] value() default {};
}
