package simpleshop.data.metadata;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

/**
 * Annotate on a model search class to specify which properties of the target model can be used for sorting.
 * This should be defined after @{@link AliasDeclaration}.
 */
@Target({ElementType.TYPE})
@Documented
@Retention(RUNTIME)
public @interface SortProperty {

     String alias() default "";
     String propertyName();
     SortDirection sortDirection() default SortDirection.BOTH;

    /**
     * Defines several {@link SortProperty} annotations on the same element.
     * Order is important (forward reference is disallowed).
     * @see SortProperty
     */
    @Target({ TYPE})
    @Retention(RUNTIME)
    @Documented
    @interface List {

        SortProperty[] value();
    }

}
