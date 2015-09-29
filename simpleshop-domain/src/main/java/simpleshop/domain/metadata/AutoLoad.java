package simpleshop.domain.metadata;

import java.lang.annotation.ElementType;

/**
 * This class level annotation is used to list all the lazy properties that should be initialized before exiting the service layer.
 */
@java.lang.annotation.Target(ElementType.TYPE)
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@java.lang.annotation.Documented
public @interface AutoLoad {

    String[] value() default {};
}
