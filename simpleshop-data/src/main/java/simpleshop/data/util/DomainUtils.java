package simpleshop.data.util;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.collection.internal.PersistentBag;
import org.hibernate.collection.internal.PersistentSet;
import org.hibernate.validator.constraints.URL;
import simpleshop.common.ReflectionUtils;
import simpleshop.common.StringUtils;
import simpleshop.data.metadata.*;
import simpleshop.domain.metadata.*;
import simpleshop.domain.metadata.validation.*;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Id;
import javax.validation.constraints.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public final class DomainUtils {

    private DomainUtils() {
    }

    /**
     * If obj is a proxy, return the proxied class.
     */
    public static Class<?> getProxiedClass(Object obj) {
        if (obj == null)
            return null;

        Class<?> realClass = obj.getClass();
        while (realClass.getSimpleName().indexOf('$') >= 0) {
            realClass = realClass.getSuperclass();
        }
        if (realClass == PersistentSet.class)
            return Set.class;

        if (realClass == PersistentBag.class)
            return List.class;

        return realClass;
    }

    public static boolean isDomainObject(Object object){
        Class<?> clazz = getProxiedClass(object);
        return domainClasses.contains(clazz);
    }


    private static volatile Map<String, ModelMetadata> modelMetadataMap;

    private static final HashSet<Class<?>> domainClasses = new HashSet<>();
    /**
     * This method can only be called once in MetadataServiceImpl constructor.
     *
     * @param classes all model classes, this is only know in service layer, as data layer does not depend on dto.
     */
    public static void createModelMetadataMap(Class<?>[] classes) {

        if (modelMetadataMap != null)
            throw new UnsupportedOperationException("DomainUtils.createModelMetadataMap(Class<?>[]) can only be called once.");

        domainClasses.addAll(Arrays.asList(classes));

        HashMap<String, ModelMetadata> metadataMap = new HashMap<>();
        for (Class<?> clazz : classes) {
            String modelName = clazz.getSimpleName();
            ModelMetadata modelMetadata = createModelMetadata(clazz);
            metadataMap.put(modelName, modelMetadata);
        }
        modelMetadataMap = Collections.unmodifiableMap(metadataMap);//for thread safety

        for (String modelName : modelMetadataMap.keySet()) {

            //setSearchable
            ModelMetadata modelMetadata = metadataMap.get(modelName);
            if (modelMetadata.getType() == ModelMetadata.ModelType.DOMAIN) {
                String searchModelName = modelName + "Search";
                if (metadataMap.containsKey(searchModelName))
                    modelMetadata.setSearchable(true);
            }

            //set returnTypeMetadata
            for (String fieldName : modelMetadata.getPropertyMetadataMap().keySet()) {
                PropertyMetadata propertyMetadata = modelMetadata.getPropertyMetadataMap().get(fieldName);
                String possibleModelName = propertyMetadata.getPropertyType();
                if (metadataMap.containsKey(possibleModelName)) {
                    propertyMetadata.setReturnTypeMetadata(metadataMap.get(possibleModelName));
                } else {
                    Class<?> returnType = propertyMetadata.getGetter().getReturnType();
                    if(Iterable.class.isAssignableFrom(returnType) || Map.class.isAssignableFrom(returnType)){
                        //create collection return type metadata
                        generateCollectionReturnTypeMetadata(propertyMetadata, metadataMap);
                    }
                }
            }
        }
    }

    private static void generateCollectionReturnTypeMetadata(PropertyMetadata propertyMetadata, HashMap<String, ModelMetadata> metadataMap) {
        ModelMetadata metadata = new ModelMetadata();
        Class<?> returnType = propertyMetadata.getGetter().getReturnType();
        metadata.setName(returnType.getSimpleName());
        metadata.setModelClass(returnType);
        metadata.setType(ModelMetadata.ModelType.COLLECTION);
        Map<String, PropertyMetadata> propertyMetadataMap = new HashMap<>();
        metadata.setSearchable(false);
        metadata.setPropertyMetadataMap(propertyMetadataMap);

        //COLLECTION_ELEMENTS = "elements";
        PropertyMetadata elementsProperty = new PropertyMetadata();
        elementsProperty.setPropertyName("elements");
        if(propertyMetadata.getGetter() != null){
            ValueClass valueClass = propertyMetadata.getGetter().getAnnotation(ValueClass.class);
            if(valueClass != null){
                elementsProperty.setReturnType(valueClass.value());
            }
        }
        if(elementsProperty.getReturnType() == null){
            elementsProperty.setReturnType(Object.class);
        }
        elementsProperty.setPropertyType(elementsProperty.getReturnType().getSimpleName());
        propertyMetadataMap.put("elements", elementsProperty);

        //COLLECTION_SIZE = "size";
        PropertyMetadata sizeProperty = new PropertyMetadata();
        sizeProperty.setPropertyName("size");
        sizeProperty.setReturnType(Integer.class);
        propertyMetadataMap.put("size", sizeProperty);

        propertyMetadata.setReturnTypeMetadata(metadata);

        /*
            todo other properties to create.

	        COLLECTION_INDICES = "indices";
	        COLLECTION_MAX_INDEX = "maxIndex";
	        COLLECTION_MIN_INDEX = "minIndex";
	        COLLECTION_MAX_ELEMENT = "maxElement";
	        COLLECTION_MIN_ELEMENT = "minElement";
	        COLLECTION_INDEX = "index";
        */
    }

    /**
     * Get the domain and dto metadata.
     * @return unmodifiable map for concurrent access.
     */
    public static Map<String, ModelMetadata> getModelMetadata(){
        return modelMetadataMap;
    }

    public static ModelMetadata getModelMetadata(String modelName){
        if(modelMetadataMap != null && modelMetadataMap.containsKey(modelName))
            return modelMetadataMap.get(modelName);

        return null;
    }

    /**
     * Extract metadata of a class into an object.
     *
     * @param clazz class.
     * @return ModelMetadata object.
     */
    public static ModelMetadata createModelMetadata(Class<?> clazz) {

        ModelMetadata modelMetadata = new ModelMetadata();
        modelMetadata.setModelClass(clazz);
        modelMetadata.setName(clazz.getSimpleName());

        setModelType(modelMetadata, clazz); //set model type
        setIcon(modelMetadata, clazz); //set icon
        setDisplayFormat(modelMetadata, clazz);//set display format
        setInterpolateFormat(modelMetadata, clazz);
        setAliasAnnotations(modelMetadata, clazz);//set alias annotations
        setSortProperties(modelMetadata, clazz);//set sort properties

        //set property metadata
        Set<String> ignoredProperties = getJsonIgnoreProperties(clazz);
        Map<String, PropertyMetadata> propertyMap = new TreeMap<>();
        Set<String> noneSummaryProperties = new HashSet<>();
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (!ReflectionUtils.isPublicInstanceGetter(method))
                continue;

            PropertyMetadata propertyMetadata = extraPropertyMetadata(method);
            if(ignoredProperties.contains(propertyMetadata.getPropertyName()))
                continue;

            propertyMap.put(propertyMetadata.getPropertyName(), propertyMetadata);
            if(!propertyMetadata.isSummaryProperty() && !propertyMetadata.isIdProperty()){
                noneSummaryProperties.add(propertyMetadata.getPropertyName());
            }
        }
        modelMetadata.setPropertyMetadataMap(Collections.unmodifiableMap(propertyMap));
        modelMetadata.setNoneSummaryProperties(Collections.unmodifiableSet(noneSummaryProperties));

        return modelMetadata;
    }

    private static Set<String> getJsonIgnoreProperties(Class<?> clazz) {
        JsonIgnoreProperties jsonIgnoreProperties = clazz.getAnnotation(JsonIgnoreProperties.class);
        Set<String> ignoredProperties = new TreeSet<>();
        if(jsonIgnoreProperties != null){
           for(String prop : jsonIgnoreProperties.value()){
               ignoredProperties.add(prop);
           }
        }
        return ignoredProperties;
    }

    private static void setInterpolateFormat(ModelMetadata modelMetadata, Class<?> clazz){
        InterpolateFormat interpolateFormat = clazz.getAnnotation(InterpolateFormat.class);
        if(interpolateFormat != null){
            modelMetadata.setInterpolateFormat(interpolateFormat.value());
        }
    }

    private static void setAliasAnnotations(ModelMetadata modelMetadata, Class<?> clazz) {
        Stack<Class<?>> classes = new Stack<>();
        Class<?> superClass = clazz;
        while (superClass != Object.class){
            classes.push(superClass);
            superClass = superClass.getSuperclass();
        }
        List<AliasDeclaration> aliasDeclarations = new ArrayList<>();
        while (!classes.isEmpty()){
            superClass = classes.pop();
            AliasDeclaration aliasDeclaration = superClass.getAnnotation(AliasDeclaration.class);
            if(aliasDeclaration != null)
                aliasDeclarations.add(aliasDeclaration);
            AliasDeclaration.List aliasDeclarationList = superClass.getAnnotation(AliasDeclaration.List.class);
            if(aliasDeclarationList != null) {
                aliasDeclarations.addAll(Arrays.asList(aliasDeclarationList.value()));
            }
        }
        if (aliasDeclarations.size() > 0) {
            modelMetadata.setAliasDeclarations(Collections.unmodifiableList(aliasDeclarations));
        }
    }

    private static void setSortProperties(ModelMetadata modelMetadata, Class<?> clazz) {
        Stack<Class<?>> classes = new Stack<>();
        Class<?> superClass = clazz;
        while (superClass != Object.class){
            classes.push(superClass);
            superClass = superClass.getSuperclass();
        }
        List<SortProperty> sortProperties = new ArrayList<>();
        while (!classes.isEmpty()){
            superClass = classes.pop();
            SortProperty sortProperty = superClass.getAnnotation(SortProperty.class);
            if(sortProperty != null)
                sortProperties.add(sortProperty);
            SortProperty.List sortPropertyList = superClass.getAnnotation(SortProperty.List.class);
            if(sortPropertyList != null) {
                sortProperties.addAll(Arrays.asList(sortPropertyList.value()));
            }
        }
        if (sortProperties.size() > 0) {
            modelMetadata.setSortProperties(Collections.unmodifiableList(sortProperties));
        }
    }

    private static void setDisplayFormat(ModelMetadata modelMetadata, Class<?> clazz) {
        DisplayFormat displayFormat = clazz.getAnnotation(DisplayFormat.class);
        if(displayFormat != null){
            modelMetadata.setDisplayFormat(displayFormat.value());
        }
    }

    private static void setIcon(ModelMetadata modelMetadata, Class<?> clazz) {
        Icon icon = clazz.getAnnotation(Icon.class);
        if (icon != null) {
            modelMetadata.setIcon(icon.value());
        }
    }

    private static void setModelType(ModelMetadata modelMetadata, Class<?> clazz) {
        String fullName = clazz.getName();
        ModelMetadata.ModelType modelType;
        if (fullName.contains(".component.")) {
            modelType = ModelMetadata.ModelType.EMBEDDED;
        } else if (fullName.contains(".lookup.")) {
            modelType = ModelMetadata.ModelType.LOOKUP;
        } else if (fullName.contains(".domain.")) {
            modelType = ModelMetadata.ModelType.DOMAIN;
        } else {
            modelType = ModelMetadata.ModelType.DTO;
        }
        modelMetadata.setType(modelType);
    }

    private static PropertyMetadata extraPropertyMetadata(Method method) {

        PropertyMetadata propertyMetadata;
        propertyMetadata = new PropertyMetadata();
        propertyMetadata.setPropertyName(StringUtils.getPropertyName(method.getName()));
        propertyMetadata.setPropertyType(method.getReturnType().getSimpleName());
        propertyMetadata.setGetter(method);

        setIdProperty(propertyMetadata, method); //setIdProperty
        setColumn(propertyMetadata, method); //insertable, updatable, length
        setLabel(propertyMetadata, method);
        setDescription(propertyMetadata, method);
        setDisplayFormat(propertyMetadata, method);
        setSummary(method, propertyMetadata);
        setWatermark(method, propertyMetadata);
        setBeanValidation(method, propertyMetadata);
        setPropertyFilters(method, propertyMetadata);

        return propertyMetadata;
    }

    private static void setIdProperty(PropertyMetadata propertyMetadata, Method method) {
        Id idAnnotation = method.getAnnotation(Id.class);
        if (idAnnotation != null)
            propertyMetadata.setIdProperty(true);
        EmbeddedId embeddedIdAnnotation = method.getAnnotation(EmbeddedId.class);
        if (embeddedIdAnnotation != null)
            propertyMetadata.setIdProperty(true);
    }

    private static void setColumn(PropertyMetadata propertyMetadata, Method method) {
        Column columnAnnotation = method.getAnnotation(Column.class);
        if (columnAnnotation != null) {
            propertyMetadata.setInsertable(columnAnnotation.insertable());
            propertyMetadata.setUpdatable(columnAnnotation.updatable());
            if(columnAnnotation.length() > 0)
                propertyMetadata.setMaxLength(columnAnnotation.length());
        }
    }

    private static void setLabel(PropertyMetadata PropertyMetadata, Method method) {
        Label labelAnnotation = method.getAnnotation(Label.class);
        if (labelAnnotation != null)
            PropertyMetadata.setLabel(labelAnnotation.value());
        else
            PropertyMetadata.setLabel(StringUtils.camelNameToFriendlyName(PropertyMetadata.getPropertyName()));
    }

    private static void setDescription(PropertyMetadata PropertyMetadata, Method method) {
        Description descriptionAnnotation = method.getAnnotation(Description.class);
        if (descriptionAnnotation != null) {
            PropertyMetadata.setDescription(descriptionAnnotation.value());
        }
    }

    private static void setDisplayFormat(PropertyMetadata propertyMetadata, Method method) {
        DisplayFormat displayFormatAnnotation = method.getAnnotation(DisplayFormat.class);
        if (displayFormatAnnotation != null){
            propertyMetadata.setDisplayFormat(StringUtils.ngEscape(displayFormatAnnotation.value()));
        }
        if(propertyMetadata.getDisplayFormat() == null){

            if(propertyMetadata.getPropertyType().endsWith("Date")){
                propertyMetadata.setDisplayFormat("moment:'YYYY-MM-DD'");
            } else if(propertyMetadata.getPropertyType().endsWith("DateTime")){
                propertyMetadata.setDisplayFormat("moment:'YYYY-MM-DD hh:mm:ss'");
            }
        }
    }

    private static void setSummary(Method method, PropertyMetadata propertyMetadata) {
        Summary summaryAnnotation = method.getAnnotation(Summary.class);
        if (summaryAnnotation != null)
            propertyMetadata.setSummaryProperty(true);
    }

    private static void setWatermark(Method method, PropertyMetadata propertyMetadata) {
        Watermark watermarkAnnotation = method.getAnnotation(Watermark.class);
        if (watermarkAnnotation != null)
            propertyMetadata.setWatermark(watermarkAnnotation.value());
    }

    private static void setBeanValidation(Method method, PropertyMetadata propertyMetadata) {
        NotNull notNullAnnotation = method.getAnnotation(NotNull.class);
        if (notNullAnnotation != null)
            propertyMetadata.setRequired(true);

        Pattern patternAnnotation = method.getAnnotation(Pattern.class);
        if (patternAnnotation != null)
            propertyMetadata.setInputFormat(patternAnnotation.regexp());

        Size sizeAnnotation = method.getAnnotation(Size.class);
        if (sizeAnnotation != null) {
            propertyMetadata.setMinLength(sizeAnnotation.min());
            propertyMetadata.setMaxLength(sizeAnnotation.max());
        }

        MinString minValue = method.getAnnotation(MinString.class);
        if (minValue != null) {
            propertyMetadata.setMin(minValue.value());
        } else {
            Min min = method.getAnnotation(Min.class);
            if (min != null) {
                propertyMetadata.setMin(String.valueOf(min.value()));
            } else {
                DecimalMin decimalMin = method.getAnnotation(DecimalMin.class);
                if (decimalMin != null) {
                    propertyMetadata.setMin(decimalMin.value());
                }
            }
        }

        MaxString maxValue = method.getAnnotation(MaxString.class);
        if (maxValue != null) {
            propertyMetadata.setMax(maxValue.value());
        } else {
            Max max = method.getAnnotation(Max.class);
            if (max != null) {
                propertyMetadata.setMax(String.valueOf(max.value()));
            } else {
                DecimalMax decimalMax = method.getAnnotation(DecimalMax.class);
                if (decimalMax != null) {
                    propertyMetadata.setMax(decimalMax.value());
                }
            }
        }

        URL url = method.getAnnotation(URL.class);
        if(url!=null && propertyMetadata.getDisplayFormat() == null){
            propertyMetadata.setDisplayFormat("url");
        }

        String propertyType = propertyMetadata.getPropertyType();
        if(propertyMetadata.getInputFormat() == null){
            if("BigDecimal".equals(propertyType)){
                propertyMetadata.setInputFormat("^\\d+(\\.\\d+)?$");
            } else if(StringUtils.isIntegerType(propertyType) || StringUtils.isDateTimeType(propertyType)){//date is expressed as an integer in JSON.
                propertyMetadata.setInputFormat("^[+-]*[1-9]\\d*$");
            }
        }
    }

    private static void setPropertyFilters(Method method, PropertyMetadata propertyMetadata) {
        List<PropertyFilter> propertyFilters = new ArrayList<>();
        PropertyFilter propertyFilter = method.getAnnotation(PropertyFilter.class);
        if(propertyFilter != null)
            propertyFilters.add(propertyFilter);
        PropertyFilter.List propertyFilterList = method.getAnnotation(PropertyFilter.List.class);
        if(propertyFilterList != null) {
            propertyFilters.addAll(Arrays.asList(propertyFilterList.value()));
        }

        if(propertyFilters.size() > 0){
            propertyFilters.sort(new PropertyFilter.Comparator());
            propertyMetadata.setPropertyFilters(Collections.unmodifiableList(propertyFilters));
        }
    }

    /**
     * Extract the properties marked as @ItemText.
     * @param domainObject the domain object used as a select item.
     * @return Item text for the object.
     */
    public static String extractItemText(Object domainObject) {
        if (domainObject == null)
            return null;

        TreeMap<Integer, String> treeMap = new TreeMap<>();
        for (Method method : domainObject.getClass().getMethods()) {
            if (!ReflectionUtils.isPublicInstanceGetter(method))
                continue;

            ItemText text = method.getAnnotation(ItemText.class);
            if (text == null)
                continue;

            try {
                Object value = method.invoke(domainObject);
                if (value != null) {
                    treeMap.put(text.order() * 2 + 1, text.separator());
                    treeMap.put(text.order() * 2 + 2, value.toString());
                }
            } catch (IllegalAccessException | InvocationTargetException ex) {
                throw new RuntimeException(ex);
            }
        }

        String result = "";
        for (Integer key : treeMap.keySet()) {
            if (key % 2 == 1) { //separator
                if (result.length() > 0)
                    result += treeMap.get(key);
            } else {
                result += treeMap.get(key);
            }
        }
        return result;
    }

    /**
     * Extract the properties marked as @ItemValue.
     * @param domainObject the domain object used as a select item.
     * @return Item value for the object.
     */
    public static String extractItemValue(Object domainObject) {
        if (domainObject == null)
            return null;

        TreeMap<Integer, String> treeMap = new TreeMap<>();
        for (Method method : domainObject.getClass().getMethods()) {
            if (!ReflectionUtils.isPublicInstanceGetter(method))
                continue;

            ItemValue text = method.getAnnotation(ItemValue.class);
            if (text == null)
                continue;

            try {
                Object value = method.invoke(domainObject);
                if (value != null) {
                    treeMap.put(text.order() * 2 + 1, text.separator());
                    treeMap.put(text.order() * 2 + 2, value.toString());
                }
            } catch (IllegalAccessException | InvocationTargetException ex) {
                throw new RuntimeException(ex);
            }
        }

        String result = "";
        for (Integer key : treeMap.keySet()) {
            if (key % 2 == 1) { //separator
                if (result.length() > 0)
                    result += treeMap.get(key);
            } else {
                result += treeMap.get(key);
            }
        }
        return result;
    }

}
