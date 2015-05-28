package simpleshop.domain.metadata;

import java.lang.annotation.ElementType;

/**
 * Label text of this field.
 * By default without the Label annotation, quickshop uses the friendly property name as the label.
 */
@java.lang.annotation.Target({ElementType.METHOD})
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@java.lang.annotation.Documented
public @interface Label {

    String value();
}
