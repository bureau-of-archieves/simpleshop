package simpleshop.common;

import java.lang.annotation.*;

@Documented
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
public @interface ObjectGraphIgnore {

    String[] groups() default {""};

}
