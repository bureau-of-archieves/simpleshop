package simpleshop.domain.metadata;

import java.lang.annotation.ElementType;

/**
 * When a domain object is used as a dropdown item, values of the annotated properties will form the item text.
 */
@java.lang.annotation.Target({ElementType.METHOD})
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@java.lang.annotation.Documented
public @interface ItemText {

    /**
     * A separator is inserted before the property value if concatenated item text so far is non-empty.
     * @return separator.
     */
    String separator() default "";

    /**
     * @return Concatenation order.
     */
    int order() default 0;
}
