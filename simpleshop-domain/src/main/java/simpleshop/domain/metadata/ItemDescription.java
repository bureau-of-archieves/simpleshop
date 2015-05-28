package simpleshop.domain.metadata;

import java.lang.annotation.ElementType;

/**
 * When a domain object is used as a dropdown item, value of the annotated properties will be used as the item tooltip.
 */
@java.lang.annotation.Target({ElementType.METHOD})
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@java.lang.annotation.Documented
public @interface ItemDescription {
}
