package simpleshop.data.infrastructure;

import org.hibernate.criterion.Criterion;

/**
 * Used to create criterion for a single {@link simpleshop.data.metadata.PropertyFilter.Operator}.
 */
public interface CriterionFactory {

    Criterion createCriterion(String targetPropertyName, Class<?> targetType, Object value, boolean negate);
}
