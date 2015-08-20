package simpleshop.data.infrastructure;

import org.hibernate.criterion.Criterion;

/**
 * Used to create criterion for a single {@link simpleshop.data.metadata.PropertyFilter.Operator}.
 */
public interface CriterionFactory {

    Criterion createCriterion(String qualifiedPropertyName, Class<?> targetType, Object value, boolean negate);
}
