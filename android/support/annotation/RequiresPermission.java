package android.support.annotation;

import java.lang.annotation.*;

@Retention(RetentionPolicy.CLASS)
@Target({ ElementType.ANNOTATION_TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.FIELD, ElementType.PARAMETER })
public @interface RequiresPermission {
    String[] allOf() default {};
    
    String[] anyOf() default {};
    
    boolean conditional() default false;
    
    String value() default "";
    
    @Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER })
    public @interface Read {
        RequiresPermission value() default @RequiresPermission;
    }
    
    @Target({ ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER })
    public @interface Write {
        RequiresPermission value() default @RequiresPermission;
    }
}
