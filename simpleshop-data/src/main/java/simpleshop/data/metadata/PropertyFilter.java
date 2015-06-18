package simpleshop.data.metadata;

import simpleshop.Constants;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Created by ZHY on 20/12/2014.
 * This is only examined when used on a public getter.
 */
@Target({ElementType.METHOD})
@Retention(java.lang.annotation.RetentionPolicy.RUNTIME)
@Documented
public @interface PropertyFilter {

    String alias() default "";

    /**
     * By default, the filter target property name is inferred from the getter.
     * @return or you can override the property name. The same rule for @see AliasDeclaration property name applies.
     */
    String property() default Constants.REFLECTED_PROPERTY_NAME;

    Operator operator() default Operator.EQUAL;

    boolean negate() default false;

    enum Operator{
        LIKE, EQUAL, GREATER, LESS, IN, IS_NULL
    }

    /**
     * Defines several {@link PropertyFilter} annotations on the same element.
     *
     * @see PropertyFilter
     */
    @Target({ METHOD})
    @Retention(RUNTIME)
    @Documented
    @interface List {

        PropertyFilter[] value();
    }

    final class Comparator implements java.util.Comparator<PropertyFilter> {

        @Override
        public int compare(PropertyFilter p1, PropertyFilter p2) {
            int result = p1.alias().compareTo(p2.alias());
            if(result != 0)
                return result;

            result = p1.property().compareTo(p2.property());
            return result;
        }
    }
}
