package simpleshop.domain.metadata;

import java.lang.annotation.ElementType;

/**
 * Specify the icon name of a domain class, which can be used in menu item or title bar.
 * The icon name is by default interpreted as Bootstrap Glyphicon.
 * //todo support other types of icon with ns:icon_name syntax.
 */
@java.lang.annotation.Target({ElementType.TYPE})
@java.lang.annotation.Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@java.lang.annotation.Documented
public @interface Icon {
    String value();
}
