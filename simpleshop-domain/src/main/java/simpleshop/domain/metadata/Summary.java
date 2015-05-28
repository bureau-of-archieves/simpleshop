package simpleshop.domain.metadata;

import java.lang.annotation.ElementType;

/**
 * Mark this field as a summary field, which should be send to the client side when the entity is retrieved as an association or collection item.
 * No summary fields could be cleared in these cases.
 */
@java.lang.annotation.Target({ElementType.METHOD})
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@java.lang.annotation.Documented
public @interface Summary {
}
