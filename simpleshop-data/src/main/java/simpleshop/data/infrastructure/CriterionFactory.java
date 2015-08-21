package simpleshop.data.infrastructure;

import org.hibernate.criterion.Criterion;

/**
 * Used to create criterion for a single {@link simpleshop.data.metadata.PropertyFilter.Operator}.
 */
public interface CriterionFactory {

    /**
     * Create a criterion in a criterion context.
     * @param criterionContext the context (parent criteria, related metadata, etc).
     * @param alias the original alias.
     * @param propertyName the property that to build criterion against. e.g. prod.name. The result will be added to the criteria identified by the alias.
     * @param targetType type of the property.
     * @param value search parameter value.
     * @param negate whether to negate the criterion, e.g. negate 'equal' becomes 'not equal'.
     * @return a criterion for the given property to be added to the criteria specified by the alias.
     */
    Criterion createCriterion(CriterionContext criterionContext, String alias, String propertyName, Class<?> targetType, Object value, boolean negate);

    String SUB_QUERY_LEAD_ALIAS = "sub_lead";

}
