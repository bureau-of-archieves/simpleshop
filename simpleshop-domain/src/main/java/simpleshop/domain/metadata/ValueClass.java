package simpleshop.domain.metadata;

import java.lang.annotation.ElementType;

/**
 * Specify the value type of a class.
 */
@java.lang.annotation.Target({ElementType.METHOD})
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@java.lang.annotation.Documented
public @interface ValueClass {

    Class<?> value() default String.class;
}
