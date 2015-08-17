package simpleshop.data.infrastructure;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.*;
import org.hibernate.metadata.ClassMetadata;
import org.hibernate.metadata.CollectionMetadata;
import org.hibernate.sql.JoinType;
import org.hibernate.type.Type;
import simpleshop.Constants;
import simpleshop.common.Pair;
import simpleshop.common.StringUtils;
import simpleshop.data.PageInfo;
import simpleshop.data.metadata.AliasDeclaration;
import simpleshop.data.metadata.ModelMetadata;
import simpleshop.data.metadata.PropertyFilter;
import simpleshop.data.metadata.PropertyMetadata;
import simpleshop.data.util.DomainUtils;

import java.io.Serializable;
import java.util.*;

/**
 * An object that contains
 */
public class ModelSearchCriteriaBuilder {

    private ModelMetadata searchMetadata;
    private ModelMetadata modelMetadata;
    private Session session;
    private Map<String, Criteria> criteriaMap;
    public Map<PropertyFilter.Operator, CriterionFactory> criterionFactoryMap = new HashMap<>();

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

        initializeOperators();

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

            addCriteria(propertyCriteria);
            propertyCriteria.clear();
        }
    }

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

    private void addCriteria(List<Pair<Criterion, Criteria>> propertyCriteria) {

        if(propertyCriteria.size() > 0) {
            Disjunction disjunction = Restrictions.disjunction();
            for(Pair<Criterion, Criteria> pair : propertyCriteria){
                disjunction.add(pair.getKey());
            }
            Criteria owner = propertyCriteria.get(0).getValue();
            owner.add(disjunction);
        }
    }

    private Criterion createCriterion(String targetAlias, String targetPropertyName, Class<?> targetType, PropertyFilter.Operator operator, Object value, boolean negate) {
        if(targetPropertyName.indexOf('.') >= 0)
            throw new SpongeConfigurationException("Compound target property is not supported: " + targetPropertyName);

        CriterionFactory factory = getCriteriaFactory(operator);
        String qualifiedPropertyName = (StringUtils.isNullOrEmpty(targetAlias) ? "" : targetAlias + ".") + targetPropertyName;
         return factory.createCriterion(qualifiedPropertyName, targetType, value, negate);
    }

    private CriterionFactory getCriteriaFactory(PropertyFilter.Operator operator){
        CriterionFactory factory = criterionFactoryMap.get(operator);

        if(factory == null)
            throw new SpongeConfigurationException(String.format("Unimplemented criterion operator '%s'.", operator));

        return factory;
    }


    private void initializeOperators(){
        criterionFactoryMap.put(PropertyFilter.Operator.LIKE, (qualifiedPropertyName, targetType, value, negate) -> {
            if(!CharSequence.class.isAssignableFrom(targetType))
                throw new SpongeConfigurationException("LIKE operator does not apply to property type: " + targetType.getName());

            String pattern = StringUtils.wrapLikeKeywords(value.toString());
            Criterion criterion = Restrictions.like(qualifiedPropertyName, pattern);
            if(negate) {
                criterion = Restrictions.not(criterion);
            }
            return criterion;
        });

        criterionFactoryMap.put(PropertyFilter.Operator.IN, (qualifiedPropertyName, targetType, value, negate) -> {
            Object[] values;
            if(value instanceof Iterable<?>){
                Iterable<?> iterable = (Iterable<?>)value;
                List<Object> list = new ArrayList<>();
                for(Object obj : iterable){
                    list.add(obj);
                }
                values = list.toArray();
            } else {
                values = value.toString().split(",");
                for(int i=0; i<values.length;i++)
                    values[i] = simpleshop.common.ReflectionUtils.parseObject(values[i],targetType);
            }
            Criterion criterion = Restrictions.in(qualifiedPropertyName, values);
            if(negate) {
                criterion = Restrictions.not(criterion);
            }

            return criterion;
        });

        criterionFactoryMap.put(PropertyFilter.Operator.EQUAL, (qualifiedPropertyName, targetType, value, negate) -> {
            Object parsedObject = simpleshop.common.ReflectionUtils.parseObject(value, targetType);
            if(negate){
                return Restrictions.ne(qualifiedPropertyName, parsedObject);
            } else {
                return Restrictions.eq(qualifiedPropertyName, parsedObject);
            }
        });

        criterionFactoryMap.put(PropertyFilter.Operator.GREATER, (qualifiedPropertyName, targetType, value, negate) -> {
            Object parsedObject = simpleshop.common.ReflectionUtils.parseObject(value, targetType);
            if(negate){
                return  Restrictions.le(qualifiedPropertyName, parsedObject);
            } else {
                return  Restrictions.gt(qualifiedPropertyName, parsedObject);
            }
        });

        criterionFactoryMap.put(PropertyFilter.Operator.LESS, (qualifiedPropertyName, targetType, value, negate) -> {
            Object parsedObject = simpleshop.common.ReflectionUtils.parseObject(value, targetType);
            if(negate){
                return Restrictions.ge(qualifiedPropertyName, parsedObject);
            } else {
                return Restrictions.lt(qualifiedPropertyName, parsedObject);
            }
        });


        criterionFactoryMap.put(PropertyFilter.Operator.IS_NULL, (qualifiedPropertyName, targetType, value, negate) -> {
            if(Objects.equals("false", value) ^ negate)
                return Restrictions.isNull(qualifiedPropertyName);
            else
                return Restrictions.isNotNull(qualifiedPropertyName);
        });

        criterionFactoryMap.put(PropertyFilter.Operator.CONTAINS, (qualifiedPropertyName, targetType, value, negate) -> {
            if(!Collection.class.isAssignableFrom(targetType)){
                throw new SpongeConfigurationException(String.format("CONTAINS operator does not apply to non-collection property '%s'", qualifiedPropertyName));
            }

            Pair<ModelMetadata, PropertyMetadata> pair = getMetadata(qualifiedPropertyName);

            ModelMetadata targetModelMetadata = pair.getKey();
            DetachedCriteria detachedCriteria = DetachedCriteria.forClass(targetModelMetadata.getModelClass(), "sub1");
            String alias = StringUtils.subStrB4(qualifiedPropertyName, ".");
            detachedCriteria.add(Restrictions.eqProperty(targetModelMetadata.getIdPropertyName(), alias + "." + targetModelMetadata.getIdPropertyName()));

            PropertyMetadata targetPropertyMetadata = pair.getValue();
            DetachedCriteria subPropertyCriteria = detachedCriteria.createCriteria(targetPropertyMetadata.getPropertyName(), "sub1prop");

            String collectionRoleName = targetModelMetadata.getModelClass().getName() + "." + targetPropertyMetadata.getPropertyName();
            Class<?> elementType =  session.getSessionFactory().getCollectionMetadata(collectionRoleName).getElementType().getReturnedClass();
            ClassMetadata elementMetadata = session.getSessionFactory().getClassMetadata(elementType);
            if(elementMetadata == null){
                //not a domain model
                subPropertyCriteria.add(Restrictions.eq("elements", value));
                detachedCriteria.setProjection(Property.forName("sub1prop.elements"));
            } else {
                if(!elementType.isAssignableFrom(value.getClass()))
                    throw new SpongeConfigurationException(String.format("CONTAINS operator error: collection element type %s is not assignable from search parameter value type %s.", elementType, value.getClass()));

                session.refresh(value);
                Serializable id = session.getIdentifier(value);
                subPropertyCriteria.add(Restrictions.eq(elementMetadata.getIdentifierPropertyName(), id));
                detachedCriteria.setProjection(Property.forName("sub1prop." + elementMetadata.getIdentifierPropertyName()));
            }

            Criterion criterion = Subqueries.exists(detachedCriteria);
            if(negate)
                criterion = Restrictions.not(criterion);
            return criterion;
        });

        criterionFactoryMap.put(PropertyFilter.Operator.VALUE_LIKE, (qualifiedPropertyName, targetType, value, negate) -> {

            if(!Map.class.isAssignableFrom(targetType))
                throw new SpongeConfigurationException("Operator VALUE_LIKE is only applicable to Map properties.");

            Pair<ModelMetadata, PropertyMetadata> pair = getMetadata(qualifiedPropertyName);
            PropertyMetadata targetPropertyMetadata = pair.getValue();
            if(!CharSequence.class.isAssignableFrom(targetPropertyMetadata.getReturnTypeMetadata().getPropertyMetadata("elements").getReturnType())){
                throw new SpongeConfigurationException("Operator VALUE_LIKE is only applicable to String values.");
            }

            ModelMetadata targetModelMetadata = pair.getKey();
            DetachedCriteria detachedCriteria = DetachedCriteria.forClass(targetModelMetadata.getModelClass(), "sub1");
            String alias = StringUtils.subStrB4(qualifiedPropertyName, ".");
            detachedCriteria.add(Restrictions.eqProperty(targetModelMetadata.getIdPropertyName(), alias + "." + targetModelMetadata.getIdPropertyName()));
            DetachedCriteria subPropertyCriteria = detachedCriteria.createCriteria(targetPropertyMetadata.getPropertyName(), "sub1prop");
            detachedCriteria.setProjection(Property.forName("sub1prop.elements"));
            String pattern = StringUtils.wrapLikeKeywords(value.toString());
            subPropertyCriteria.add(Restrictions.like("elements", pattern));

            Criterion criterion = Subqueries.exists(detachedCriteria);
            if(negate)
                criterion = Restrictions.not(criterion);
            return criterion;
        });


        //criterionFactoryMap.put(PropertyFilter.Operator.IN, (qualifiedPropertyName, targetType, value, negate) -> {});
    }

    private Pair<ModelMetadata, PropertyMetadata> getMetadata(String qualifiedPropertyName){
        String alias = StringUtils.subStrB4(qualifiedPropertyName, ".");
        String propertyName = StringUtils.subStrAfterFirst(qualifiedPropertyName, ".");
        String fullPath = this.getFullPropertyPath(alias, propertyName);
        Pair<ModelMetadata, PropertyMetadata> pair = modelMetadata.getPathMetadata(fullPath);
        return pair;
    }


}
