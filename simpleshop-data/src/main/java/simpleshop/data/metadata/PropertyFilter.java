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

    String alias() default AliasDeclaration.ROOT_CRITERIA_ALIAS;

    /**
     * By default, the filter target property name is inferred from the getter.
     * @return or you can override the property name. The same rule for @see AliasDeclaration property name applies.
     */
    String property() default Constants.REFLECTED_PROPERTY_NAME;

    Operator operator() default Operator.EQUAL;

    boolean negate() default false;

    enum Operator{

        /**
         * String property like.
         */
        LIKE,

        /**
         * String property start with.
         */
        START_WITH,

        /**
         * Property equal.
         */
        EQUAL,

        /**
         * Property greater than. Make sure the property type is applicable to this operator.
         */
        GREATER,

        /**
         * Property less than. Make sure the property type is applicable to this operator.
         */
        LESS,

        /**
         * Property in a list of values.
         */
        IN,

        /**
         * Property is null.
         */
        IS_NULL,

        /**
         * Property is a String valued map.
         * The owner object has a map entry whose value like.
         */
        VALUE_LIKE(false),

        /**
         * Property is a collection which contains parameter object.
         */
        CONTAINS(false),

        /**
         * Property is a collection and the parameter object is also a collection.
         * The property collection contains at least one item in the parameter collection.
         */
        CONTAINS_ANY(false),

        /**
         * Property is a collection and the parameter object is a parameter object.
         * The property collection contains at least one item which matches the criteria specified in the parameter object.
         */
         CONTAINS_MATCH(false),

        /**
         * Property is a domain object and the object matches the search parameter.
         */
        MATCH(false);

        private final boolean simple;

        Operator(){
            this.simple = true;
        }

        Operator(boolean isSimple){
            this.simple = isSimple;
        }

        public boolean isSimple() {
            return simple;
        }
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
