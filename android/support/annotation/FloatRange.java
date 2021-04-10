package android.support.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.CLASS)
@Target({ ElementType.METHOD, ElementType.PARAMETER, ElementType.FIELD, ElementType.LOCAL_VARIABLE, ElementType.ANNOTATION_TYPE })
public @interface FloatRange {
    double from() default Double.NEGATIVE_INFINITY;
    
    boolean fromInclusive() default true;
    
    double to() default Double.POSITIVE_INFINITY;
    
    boolean toInclusive() default true;
}
