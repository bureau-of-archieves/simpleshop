package simpleshop.domain.metadata;

import java.lang.annotation.ElementType;

/**
 * AJS filters to apply to this field when displaying in html.
 */
@java.lang.annotation.Target({ElementType.METHOD, ElementType.TYPE})
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@java.lang.annotation.Documented
public @interface InterpolateFormat {
    String value();
}
