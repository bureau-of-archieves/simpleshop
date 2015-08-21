package simpleshop.data.infrastructure.impl;

import org.hibernate.criterion.Conjunction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.hibernate.metadata.ClassMetadata;
import simpleshop.Constants;
import simpleshop.common.CollectionUtils;
import simpleshop.common.ReflectionUtils;
import simpleshop.common.StringUtils;
import simpleshop.data.PageInfo;
import simpleshop.data.infrastructure.CriterionContext;
import simpleshop.data.infrastructure.CriterionFactory;
import simpleshop.data.infrastructure.SpongeConfigurationException;
import simpleshop.data.metadata.ModelMetadata;
import simpleshop.data.metadata.PropertyFilter;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Locator anti pattern.
 */
public class CriterionFactoryLocator {

    private static final Map<PropertyFilter.Operator, CriterionFactory> criterionFactoryMap = new HashMap<>();

    public static CriterionFactory getCriteriaFactory(PropertyFilter.Operator operator){
        CriterionFactory factory = criterionFactoryMap.get(operator);

        if(factory == null)
            throw new SpongeConfigurationException(String.format("Unimplemented criterion operator '%s'.", operator));

        return factory;
    }

    static {

        criterionFactoryMap.put(PropertyFilter.Operator.LIKE, new SimpleCriterionFactory() {
            @Override
            protected Criterion createSimpleCriterion(String qualifiedPropertyName, Class<?> targetType, Object value, boolean negate) {
                if(!CharSequence.class.isAssignableFrom(targetType))
                    throw new SpongeConfigurationException("LIKE operator does not apply to property type: " + targetType.getName());

                String pattern = StringUtils.wrapLikeKeywords(value.toString());
                Criterion criterion = Restrictions.like(qualifiedPropertyName, pattern);
                if(negate) {
                    criterion = Restrictions.not(criterion);
                }
                return criterion;
            }
        });

        criterionFactoryMap.put(PropertyFilter.Operator.START_WITH, new SimpleCriterionFactory() {
            @Override
            protected Criterion createSimpleCriterion(String qualifiedPropertyName, Class<?> targetType, Object value, boolean negate) {
                if(!CharSequence.class.isAssignableFrom(targetType))
                    throw new SpongeConfigurationException("START_WITH operator does not apply to property type: " + targetType.getName());

                String pattern = StringUtils.wrapStartWithKeywords(value.toString());
                Criterion criterion = Restrictions.like(qualifiedPropertyName, pattern);
                if(negate) {
                    criterion = Restrictions.not(criterion);
                }
                return criterion;
            }
        });

        criterionFactoryMap.put(PropertyFilter.Operator.IN, new SimpleCriterionFactory() {
            @Override
            protected Criterion createSimpleCriterion(String qualifiedPropertyName, Class<?> targetType, Object value, boolean negate) {
                Object[] values = CollectionUtils.objectToObjectArray(value, targetType);
                Criterion criterion = values.length == 0 ? Restrictions.neProperty(qualifiedPropertyName, qualifiedPropertyName)/*false condition*/ : Restrictions.in(qualifiedPropertyName, values);
                if(negate) {
                    criterion = Restrictions.not(criterion);
                }

                return criterion;
            }
        });

        criterionFactoryMap.put(PropertyFilter.Operator.EQUAL, new SimpleCriterionFactory() {
            @Override
            protected Criterion createSimpleCriterion(String qualifiedPropertyName, Class<?> targetType, Object value, boolean negate) {
                Object parsedObject = simpleshop.common.ReflectionUtils.parseObject(value, targetType);
                if(negate){
                    return Restrictions.ne(qualifiedPropertyName, parsedObject);
                } else {
                    return Restrictions.eq(qualifiedPropertyName, parsedObject);
                }
            }
        });

        criterionFactoryMap.put(PropertyFilter.Operator.GREATER, new SimpleCriterionFactory() {
            @Override
            protected Criterion createSimpleCriterion(String qualifiedPropertyName, Class<?> targetType, Object value, boolean negate) {
                Object parsedObject = simpleshop.common.ReflectionUtils.parseObject(value, targetType);
                if(negate){
                    return  Restrictions.le(qualifiedPropertyName, parsedObject);
                } else {
                    return  Restrictions.gt(qualifiedPropertyName, parsedObject);
                }
            }
        });

        criterionFactoryMap.put(PropertyFilter.Operator.LESS, new SimpleCriterionFactory() {
            @Override
            protected Criterion createSimpleCriterion(String qualifiedPropertyName, Class<?> targetType, Object value, boolean negate) {
                Object parsedObject = simpleshop.common.ReflectionUtils.parseObject(value, targetType);
                if(negate){
                    return Restrictions.ge(qualifiedPropertyName, parsedObject);
                } else {
                    return Restrictions.lt(qualifiedPropertyName, parsedObject);
                }
            }
        });

        criterionFactoryMap.put(PropertyFilter.Operator.IS_NULL, new SimpleCriterionFactory() {
            @Override
            protected Criterion createSimpleCriterion(String qualifiedPropertyName, Class<?> targetType, Object value, boolean negate) {
                if(Objects.equals("false", value) ^ negate)
                    return Restrictions.isNull(qualifiedPropertyName);
                else
                    return Restrictions.isNotNull(qualifiedPropertyName);
            }
        });

        criterionFactoryMap.put(PropertyFilter.Operator.VALUE_LIKE, new MapCriterionFactory() {

            @Override
            protected Criterion createEntryCriterion(Object value) {
                String pattern = StringUtils.wrapLikeKeywords(value.toString());
                return Restrictions.like("elements", pattern);
            }

            @Override
            protected boolean isOperatorValidForMapKeyValueType(ModelMetadata collectionModelMetadata) {
                return CharSequence.class.isAssignableFrom(collectionModelMetadata.getPropertyMetadata("elements").getReturnType());
            }
        });

        criterionFactoryMap.put(PropertyFilter.Operator.CONTAINS, new CollectionCriterionFactory() {

            @Override
            protected Criterion createSimpleElementCriterion(Class<?> elementType, Object value) {
                Object parsedObject = simpleshop.common.ReflectionUtils.parseObject(value, elementType);
                return Restrictions.eq("elements", parsedObject);
            }

            @Override
            protected Criterion createModelElementCriterion(CriterionContext nestedCriterionContext, ClassMetadata elementMetadata, Class<?> elementType, Object value) {
                if(!elementType.isAssignableFrom(value.getClass()))
                    throw new SpongeConfigurationException(String.format("CONTAINS operator error: collection element type %s is not assignable from search parameter value type %s.", elementType, value.getClass()));

                Serializable id = (Serializable) ReflectionUtils.getProperty(value, elementMetadata.getIdentifierPropertyName());
                return Restrictions.eq(elementMetadata.getIdentifierPropertyName(), id);
            }
        });

        criterionFactoryMap.put(PropertyFilter.Operator.CONTAINS_ANY, new CollectionCriterionFactory() {

            @Override
            protected Criterion createSimpleElementCriterion(Class<?> elementType, Object value) {
                Object[] values = CollectionUtils.objectToObjectArray(value, elementType);
                return values.length == 0 ? Restrictions.neProperty("element", "element") : Restrictions.in("elements", values);
            }

            @Override
            protected Criterion createModelElementCriterion(CriterionContext nestedCriterionContext, ClassMetadata elementMetadata, Class<?> elementType, Object value) {
                Object[] values = CollectionUtils.objectToObjectArray(value, elementType);
                if(values.length == 0){
                    return Restrictions.neProperty(elementMetadata.getIdentifierPropertyName(), elementMetadata.getIdentifierPropertyName());
                }

                Object[] idArray = Arrays.asList(values).stream().map(val -> {
                    if (!elementType.isAssignableFrom(val.getClass()))
                        throw new SpongeConfigurationException(String.format("CONTAINS_ANY operator error: collection element type %s is not assignable from search parameter value type %s.", elementType, val.getClass()));

                    return ReflectionUtils.getProperty(val, elementMetadata.getIdentifierPropertyName());
                }).toArray();

                return Restrictions.in(elementMetadata.getIdentifierPropertyName(), Arrays.asList(idArray));
            }
        });

        criterionFactoryMap.put(PropertyFilter.Operator.CONTAINS_MATCH, new CollectionCriterionFactory() {

            @Override
            protected Criterion createSimpleElementCriterion(Class<?> elementType, Object value) {
                throw new SpongeConfigurationException("CONTAINS_MATCH operator does not apply to collection of simple type.");
            }

            @Override
            protected Criterion createModelElementCriterion(CriterionContext nestedCriterionContext, ClassMetadata elementMetadata, Class<?> elementType, Object value) {
                if(!(value instanceof PageInfo))
                    throw new SpongeConfigurationException("CONTAINS_MATCH parameter must be an search object.");

                PageInfo searchObject = (PageInfo)value;
                nestedCriterionContext.buildCriteria(searchObject);
                return null;
            }
        });


        //criterionFactoryMap.put(PropertyFilter.Operator.IN, (qualifiedPropertyName, targetType, value, negate) -> {});
    }
}
