package simpleshop.data.infrastructure.impl;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.*;
import org.hibernate.metadata.ClassMetadata;
import simpleshop.Constants;
import simpleshop.common.CollectionUtils;
import simpleshop.common.Pair;
import simpleshop.common.ReflectionUtils;
import simpleshop.common.StringUtils;
import simpleshop.data.PageInfo;
import simpleshop.data.infrastructure.CriteriaBuilder;
import simpleshop.data.infrastructure.CriterionContext;
import simpleshop.data.infrastructure.CriterionFactory;
import simpleshop.data.infrastructure.SpongeConfigurationException;
import simpleshop.data.metadata.AliasDeclaration;
import simpleshop.data.metadata.ModelMetadata;
import simpleshop.data.metadata.PropertyFilter;
import simpleshop.data.metadata.PropertyMetadata;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.*;

/**
 * An object that contains
 */
public class ModelSearchCriteriaBuilder implements CriteriaBuilder, CriterionContext {

    /**
     * The search model metadata.
     */
    private ModelMetadata searchMetadata;

    /**
     * The result model metadata.
     */
    private ModelMetadata modelMetadata;

    /**
     * The session to use to create Hibernate Criteria.
     */
    private Session session;

    /**
     * A map of all created criteria. The key is the alias.
     */
    private Map<String, Criteria> criteriaMap;

    public ModelSearchCriteriaBuilder(ModelMetadata searchMetadata, ModelMetadata modelMetadata, Session session){
        this.searchMetadata = searchMetadata;
        this.modelMetadata = modelMetadata;
        this.session = session;
        criteriaMap = new HashMap<>();
    }

    /**
     * Get the criteria identified by the given alias.
     * @param alias an alias declared on the search model.
     * @return the criteria.
     */
    public Criteria getCriteria(String alias){

        if(!criteriaMap.containsKey(alias)){
            Criteria criteria = loadCriteria(alias);
            if(criteria == null)
                throw new SpongeConfigurationException(String.format("Alias '%s' is not found in search model '%s'.", alias, searchMetadata.getName()));
        }
        return criteriaMap.get(alias);
    }

    /**
     * Lazy load a criteria into the criteria hierarchy.
     */
    private Criteria loadCriteria(String alias){
        //base case
        if(AliasDeclaration.ROOT_CRITERIA_ALIAS.equals(alias)){
            if(!criteriaMap.containsKey(alias)){
                criteriaMap.put(alias, session.createCriteria(modelMetadata.getModelClass(), alias));
            }
            return criteriaMap.get(alias);
        }

        //otherwise
        if(!criteriaMap.containsKey(alias)){
            //load
            Map<String, AliasDeclaration> aliases = searchMetadata.getAliases();
            if(!aliases.containsKey(alias))
                throw new SpongeConfigurationException(String.format("Alias '%s' is not found declared on search model '%s'.", alias, searchMetadata.getName()));

            criteriaMap.put(alias, null);//loading mark
            AliasDeclaration declaration = aliases.get(alias);
            Criteria parent = loadCriteria(declaration.parentAlias());
            Criteria child = parent.createCriteria(declaration.propertyName(), declaration.aliasName(), declaration.joinType());
            criteriaMap.put(alias, child);
        }
        Criteria loaded = criteriaMap.get(alias);
        if(loaded == null)
            throw new SpongeConfigurationException(String.format("Circular reference is found in the alias declarations for search model %s.", searchMetadata.getName()));
        return loaded;
    }

    /**
     * Build the criteria for the given search object.
     * @param searchObject search dto.
     */
    public void buildCriteria(PageInfo searchObject){

        //add criteria from each search parameter
        List<Pair<Criterion, Criteria>> propertyCriteria = new ArrayList<>(); //expressions for OR clause
        for(PropertyMetadata propertyMetadata : searchMetadata.getPropertyMetadataMap().values()){

            //get property filters
            List<PropertyFilter> propertyFilters = propertyMetadata.getPropertyFilters();
            if(propertyFilters == null)
                continue;

            //get property value
            Object value = org.springframework.util.ReflectionUtils.invokeMethod(propertyMetadata.getGetter(), searchObject);
            if(value == null)
                continue;

            //create Hibernate propertyCriteria
            for(PropertyFilter propertyFilter : propertyFilters) {

                //resolve target property name
                String targetPropertyName = propertyFilter.property();
                if (targetPropertyName == null || Constants.REFLECTED_PROPERTY_NAME.equals(targetPropertyName))
                    targetPropertyName = propertyMetadata.getPropertyName();

                //get target property type
                String fullPropertyPath = getFullPropertyPath(propertyFilter.alias(), targetPropertyName);
                PropertyMetadata targetPropertyMetadata = modelMetadata.getPropertyMetadata(fullPropertyPath);
                Class<?> targetType = targetPropertyMetadata.getReturnType();

                //create Criterion
                Criteria parent = loadCriteria(propertyFilter.alias());
                Criterion criterion = createCriterion(parent.getAlias(), targetPropertyName, targetType, propertyFilter.operator(), value, propertyFilter.negate());
                propertyCriteria.add(new Pair<>(criterion, parent));
            }

            addOrClauses(propertyCriteria);
            propertyCriteria.clear();
        }
    }

    /**
     * Get the full property path from the root object.
     * @param alias alias.
     * @param propertyName property name.
     * @return e.g. ctn.elements -> ct.contactNumbers.elements -> root.contact.contactNumbers.elements.
     */
    private String getFullPropertyPath(String alias, String propertyName) {
        Map<String, AliasDeclaration> aliases = searchMetadata.getAliases();
        String fullPath = propertyName;
        while (!AliasDeclaration.ROOT_CRITERIA_ALIAS.equals(alias)){
            AliasDeclaration declaration = aliases.get(alias);
            if(declaration == null)
                throw new SpongeConfigurationException(String.format("Alias '%s' is not declared on search model '%s'.", alias, searchMetadata.getName()));
            fullPath = declaration.propertyName() + "." + fullPath;
            alias = declaration.parentAlias();
        }
        return fullPath;
    }

    /**
     * All criterion objects created for a single search parameter will be added as a disjunction criterion.
     * i.e. (clause1 or clause2 or clause3 ...). The result will be added to the criteria identified by the first Filter's alias.
     * A criterion usually is added to its parent criteria. However, when multiple criterion objects have different parent criteria object, we will choose the first.
     * This is not a problem unless a criterion object other than the first one is not qualified.
     * This means we have have at most one idEq per search object property and it has to be the first filter.
     * @param clauses list of criterion objects build from a single search object property.
     */
    private void addOrClauses(List<Pair<Criterion, Criteria>> clauses) {

        if(clauses.size() > 0) {
            Disjunction disjunction = Restrictions.disjunction();
            for(Pair<Criterion, Criteria> pair : clauses){
                disjunction.add(pair.getKey());
            }
            Criteria owner = clauses.get(0).getValue();
            owner.add(disjunction);
        }
    }

    private Criterion createCriterion(String targetAlias, String targetPropertyName, Class<?> targetType, PropertyFilter.Operator operator, Object value, boolean negate) {

        CriterionFactory factory = getCriteriaFactory(operator);
        String qualifiedPropertyName = (StringUtils.isNullOrEmpty(targetAlias) ? "" : targetAlias + ".") + targetPropertyName;
        return factory.createCriterion(this, qualifiedPropertyName, targetType, value, negate);
    }

    private CriterionFactory getCriteriaFactory(PropertyFilter.Operator operator){
        CriterionFactory factory = criterionFactoryMap.get(operator);

        if(factory == null)
            throw new SpongeConfigurationException(String.format("Unimplemented criterion operator '%s'.", operator));

        return factory;
    }

    public Pair<ModelMetadata, PropertyMetadata> getMetadata(String qualifiedPropertyName){
        String alias = StringUtils.subStrB4(qualifiedPropertyName, ".");
        String propertyName = StringUtils.subStrAfterFirst(qualifiedPropertyName, ".");
        String fullPath = this.getFullPropertyPath(alias, propertyName);
        Pair<ModelMetadata, PropertyMetadata> pair = modelMetadata.getPathMetadata(fullPath);
        return pair;
    }

    public Session getSession() {
        return session;
    }


    private static final Map<PropertyFilter.Operator, CriterionFactory> criterionFactoryMap = new HashMap<>();

    static {
        criterionFactoryMap.put(PropertyFilter.Operator.LIKE, (criterionContext, qualifiedPropertyName, targetType, value, negate) -> {
            if(!CharSequence.class.isAssignableFrom(targetType))
                throw new SpongeConfigurationException("LIKE operator does not apply to property type: " + targetType.getName());

            String pattern = StringUtils.wrapLikeKeywords(value.toString());
            Criterion criterion = Restrictions.like(qualifiedPropertyName, pattern);
            if(negate) {
                criterion = Restrictions.not(criterion);
            }
            return criterion;
        });

        criterionFactoryMap.put(PropertyFilter.Operator.START_WITH, (criterionContext, qualifiedPropertyName, targetType, value, negate) -> {
            if(!CharSequence.class.isAssignableFrom(targetType))
                throw new SpongeConfigurationException("START_WITH operator does not apply to property type: " + targetType.getName());

            String pattern = StringUtils.wrapStartWithKeywords(value.toString());
            Criterion criterion = Restrictions.like(qualifiedPropertyName, pattern);
            if(negate) {
                criterion = Restrictions.not(criterion);
            }
            return criterion;
        });

        criterionFactoryMap.put(PropertyFilter.Operator.IN, (criterionContext, qualifiedPropertyName, targetType, value, negate) -> {
            Object[] values = CollectionUtils.objectToObjectArray(value, targetType);
            Criterion criterion = values.length == 0 ? Restrictions.neProperty(qualifiedPropertyName, qualifiedPropertyName)/*false condition*/ : Restrictions.in(qualifiedPropertyName, values);
            if(negate) {
                criterion = Restrictions.not(criterion);
            }

            return criterion;
        });

        criterionFactoryMap.put(PropertyFilter.Operator.EQUAL, (criterionContext, qualifiedPropertyName, targetType, value, negate) -> {
            Object parsedObject = simpleshop.common.ReflectionUtils.parseObject(value, targetType);
            if(negate){
                return Restrictions.ne(qualifiedPropertyName, parsedObject);
            } else {
                return Restrictions.eq(qualifiedPropertyName, parsedObject);
            }
        });

        criterionFactoryMap.put(PropertyFilter.Operator.GREATER, (criterionContext, qualifiedPropertyName, targetType, value, negate) -> {
            Object parsedObject = simpleshop.common.ReflectionUtils.parseObject(value, targetType);
            if(negate){
                return  Restrictions.le(qualifiedPropertyName, parsedObject);
            } else {
                return  Restrictions.gt(qualifiedPropertyName, parsedObject);
            }
        });

        criterionFactoryMap.put(PropertyFilter.Operator.LESS, (criterionContext, qualifiedPropertyName, targetType, value, negate) -> {
            Object parsedObject = simpleshop.common.ReflectionUtils.parseObject(value, targetType);
            if(negate){
                return Restrictions.ge(qualifiedPropertyName, parsedObject);
            } else {
                return Restrictions.lt(qualifiedPropertyName, parsedObject);
            }
        });


        criterionFactoryMap.put(PropertyFilter.Operator.IS_NULL, (criterionContext, qualifiedPropertyName, targetType, value, negate) -> {
            if(Objects.equals("false", value) ^ negate)
                return Restrictions.isNull(qualifiedPropertyName);
            else
                return Restrictions.isNotNull(qualifiedPropertyName);
        });

        criterionFactoryMap.put(PropertyFilter.Operator.CONTAINS, new CollectionCriterionFactory() {

            @Override
            protected Criterion createElementCriterion(Class<?> elementType, ClassMetadata elementMetadata, Object value) {
                if(elementMetadata == null){ //collection of simple type
                    Object parsedObject = simpleshop.common.ReflectionUtils.parseObject(value, elementType);
                    return Restrictions.eq("elements", parsedObject);
                } else {
                    if(!elementType.isAssignableFrom(value.getClass()))
                        throw new SpongeConfigurationException(String.format("CONTAINS operator error: collection element type %s is not assignable from search parameter value type %s.", elementType, value.getClass()));

                    Serializable id = (Serializable)ReflectionUtils.getProperty(value, elementMetadata.getIdentifierPropertyName());
                    return Restrictions.eq(elementMetadata.getIdentifierPropertyName(), id);
                }
            }
        });

        criterionFactoryMap.put(PropertyFilter.Operator.CONTAINS_ANY, new CollectionCriterionFactory() {
            @Override
            protected Criterion createElementCriterion(Class<?> elementType, ClassMetadata elementMetadata, Object value) {
                Object[] values = CollectionUtils.objectToObjectArray(value, elementType);
                if(values.length == 0)
                    return null;

                if(elementMetadata == null) { //collection of simple type
                    return Restrictions.in("elements", values);
                } else {
                    List<Serializable> ids = new ArrayList<>();
                    Arrays.asList(values).forEach(val -> {
                        if (!elementType.isAssignableFrom(val.getClass()))
                            throw new SpongeConfigurationException(String.format("CONTAINS_ANY operator error: collection element type %s is not assignable from search parameter value type %s.", elementType, val.getClass()));

                        Serializable id = (Serializable)ReflectionUtils.getProperty(val, elementMetadata.getIdentifierPropertyName());
                        ids.add(id);
                    });

                    return Restrictions.in(elementMetadata.getIdentifierPropertyName(), ids);
                }
            }
        });

        criterionFactoryMap.put(PropertyFilter.Operator.CONTAINS_MATCH, new CollectionCriterionFactory() {

            @Override
            protected Criterion createElementCriterion(Class<?> elementType, ClassMetadata elementMetadata, Object value) {
                if(elementMetadata == null){ //collection of simple type
                    throw new SpongeConfigurationException("CONTAINS_MATCH operator does not apply to collection of simple type.");
                } else {

                    Conjunction criterion = Restrictions.conjunction();
                    for(Method method : value.getClass().getMethods()) {
                        if (!ReflectionUtils.isPublicInstanceGetter(method))
                            continue;

                        Object val = org.springframework.util.ReflectionUtils.invokeMethod(method, value);
                        if (val == null)
                            continue;

                        PropertyFilter filter = method.getDeclaredAnnotation(PropertyFilter.class);
                        if (filter == null)
                            continue;

                        PropertyFilter.Operator operator = filter.operator();
                        if (!operator.isSimple()) {
                            throw new SpongeConfigurationException("Must be a simple operator at nested level.");
                        }

                        String propertyName = filter.property();
                        if (propertyName == null || Constants.REFLECTED_PROPERTY_NAME.equals(propertyName))
                            propertyName = StringUtils.getPropertyName(method.getName());

                        CriterionFactory factory = criterionFactoryMap.get(operator);
                        criterion.add(factory.createCriterion(null /*Simple criterion does not require context*/, propertyName, elementMetadata.getPropertyType(propertyName).getReturnedClass(), val, filter.negate()));
                    }

                    return criterion;
                }
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

        //criterionFactoryMap.put(PropertyFilter.Operator.IN, (qualifiedPropertyName, targetType, value, negate) -> {});
    }

}
