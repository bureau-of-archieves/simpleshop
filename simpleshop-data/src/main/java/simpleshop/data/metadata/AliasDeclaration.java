package simpleshop.data.metadata;

import org.hibernate.sql.JoinType;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ElementType.TYPE})
@Documented
@Retention(RUNTIME)
public @interface AliasDeclaration {

    /**
     * Default value is empty string, which represents the alias of the root persistence class.
     * @return must be "" or another alias previously defined on the class (in the annotation list).
     */
    String parentAlias() default "";

    /**
     * A single property name; cannot be dotted compound property path unless accessing the property of an embedded object.
     */
    String propertyName();

    String aliasName();

    /**
     * How to join from parentAlias to the association or collection specified by the property name.
     * @return hibernate criteria join type; defaults to INNER.
     */
    JoinType joinType() default JoinType.INNER_JOIN;

    /**
     * Defines several {@link AliasDeclaration} annotations on the same element.
     * Order is important (forward reference is disallowed).
     *
     * @see AliasDeclaration
     */
    @Target({ TYPE})
    @Retention(RUNTIME)
    @Documented
    @interface List {

        AliasDeclaration[] value();
    }

    final class Comparator implements java.util.Comparator<AliasDeclaration> {

        @Override
        public int compare(AliasDeclaration o1, AliasDeclaration o2) {
            return o1.aliasName().compareTo(o2.aliasName());
        }
    }

}
