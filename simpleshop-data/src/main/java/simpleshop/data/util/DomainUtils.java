package simpleshop.data.util;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.collection.internal.PersistentBag;
import org.hibernate.collection.internal.PersistentSet;
import org.hibernate.validator.constraints.URL;
import simpleshop.common.ReflectionUtils;
import simpleshop.common.StringUtils;
import simpleshop.data.SortInfo;
import simpleshop.data.infrastructure.SpongeConfigurationException;
import simpleshop.data.metadata.*;
import simpleshop.domain.metadata.*;
import simpleshop.domain.metadata.validation.*;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Id;
import javax.validation.constraints.*;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
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

    /**
     * Check if the given object is a domain object.
     * @param object any object. Cannot pass null here.
     * @return true if is domain object.
     */
    public static boolean isDomainObject(Object object){
        Class<?> clazz = getProxiedClass(object);
        return domainClasses.contains(clazz);
    }

    /**
     * Get the domain and dto metadata.
     * @return unmodifiable map for concurrent access.
     */
    public static Map<String, ModelMetadata> getModelMetadata(){
        return modelMetadataMap;
    }

    /**
     * Get property metadata for a single model.
     * @param modelName name in pascal casing.
     * @return null if not found.
     */
    public static ModelMetadata getModelMetadata(String modelName){
        if(modelMetadataMap != null && modelMetadataMap.containsKey(modelName))
            return modelMetadataMap.get(modelName);

        return null;
    }

    //////////////////////////////////metadata creation logic///////////////////////////////////////////////////
    private static Map<String, ModelMetadata> modelMetadataMap;
    private static Set<Class<?>> domainClasses;

    /**
     * This method can only be called once in MetadataServiceImpl constructor.
     * @param classes all model classes, this is only know in service layer, as data layer does not depend on dto.
     */
    public static synchronized void createModelMetadataMap(Class<?>[] classes) {

        if (modelMetadataMap != null)
            return; //throw new UnsupportedOperationException("DomainUtils.createModelMetadataMap(Class<?>[]) can only be called once.");

        HashSet<Class<?>> set = new HashSet<>(new HashSet<>(Arrays.asList(classes)));
        domainClasses = Collections.unmodifiableSet(set);

        HashMap<String, ModelMetadata> metadataMap = new HashMap<>();
        for (Class<?> clazz : classes) {
            ModelMetadata modelMetadata = createModelMetadata(clazz);
            metadataMap.put(modelMetadata.getName(), modelMetadata);
        }
        modelMetadataMap = Collections.unmodifiableMap(metadataMap);//for thread safety

        finishMetadataCreation(metadataMap);
    }

    /**
     * Update metadata before it is ready for access.
     * @param metadataMap metadata just loaded.
     */
    private static void finishMetadataCreation(HashMap<String, ModelMetadata> metadataMap) {
        for (String modelName : modelMetadataMap.keySet()) {

            //setSearchable
            ModelMetadata modelMetadata = metadataMap.get(modelName);
            if (modelMetadata.getType() == ModelType.DOMAIN) {
                String searchModelName = modelName + "Search";
                if (metadataMap.containsKey(searchModelName) && !StringUtils.isNullOrEmpty(modelMetadata.getIcon()))
                    modelMetadata.setSearchable(true);
            }

            //set returnTypeMetadata & id property name
            for (PropertyMetadata propertyMetadata : modelMetadata.getPropertyMetadataMap().values()) {
                String possibleModelName = propertyMetadata.getPropertyType();
                if (metadataMap.containsKey(possibleModelName)) {
                    propertyMetadata.setReturnTypeMetadata(metadataMap.get(possibleModelName));
                } else {
                    Class<?> returnType = propertyMetadata.getGetter().getReturnType();
                    if(Iterable.class.isAssignableFrom(returnType) || Map.class.isAssignableFrom(returnType)){
                        generateCollectionReturnTypeMetadata(propertyMetadata);
                    }
                }

                //set id property name
                if(propertyMetadata.isIdProperty()){
                    if(modelMetadata.getIdPropertyName() != null)
                        throw new SpongeConfigurationException("There can be at most 1 id property.");

                    modelMetadata.setIdPropertyName(propertyMetadata.getPropertyName());
                }
            }
        }
    }

    /**
     * Property of propertyMetadata returns a collection. Generate the collection metadata.
     * @param propertyMetadata generate collection metadata for the return type of this property.
     */
    private static void generateCollectionReturnTypeMetadata(PropertyMetadata propertyMetadata) {

        Class<?> returnType = propertyMetadata.getGetter().getReturnType();
        ModelMetadata metadata = new ModelMetadata();
        metadata.setName(returnType.getSimpleName());
        metadata.setModelClass(returnType);
        metadata.setType(ModelType.COLLECTION);
        metadata.setSearchable(false);
        metadata.setDisplayFormat(propertyMetadata.getDisplayFormat());
        propertyMetadata.setReturnTypeMetadata(metadata);

        Map<String, PropertyMetadata> propertyMetadataMap = new HashMap<>();
        metadata.setPropertyMetadataMap(propertyMetadataMap);

        //COLLECTION_ELEMENTS = "elements";
        PropertyMetadata elementsProperty = new PropertyMetadata();
        elementsProperty.setPropertyName("elements");
        Class<?> indicesClass = null;
        try{
            Field field = propertyMetadata.getGetter().getDeclaringClass().getDeclaredField(propertyMetadata.getPropertyName());
            Type type = field.getGenericType();
            if (!(type instanceof ParameterizedType)) {
                elementsProperty.setReturnType(Object.class);
            } else {
                Type[] genericArguments = ((ParameterizedType) type).getActualTypeArguments();

                if(genericArguments.length == 1){
                    elementsProperty.setReturnType((Class<?>)genericArguments[0]); //for collection
                    if(List.class.isAssignableFrom(field.getType())){
                        indicesClass = Integer.class;
                    }
                }
                else if(genericArguments.length == 2) {
                    elementsProperty.setReturnType((Class<?>)genericArguments[1]); //for map
                    indicesClass = (Class<?>)genericArguments[0];
                }
                else
                    throw new SpongeConfigurationException("Invalid generic collection type: " + type);
            }

        }catch (NoSuchFieldException ex){
            throw new SpongeConfigurationException("Property getter has not backing field: " + propertyMetadata.getGetter(), ex);
        }
        elementsProperty.setPropertyType(elementsProperty.getReturnType().getSimpleName());
        propertyMetadataMap.put("elements", elementsProperty);

        //COLLECTION_SIZE = "size";
        PropertyMetadata sizeProperty = new PropertyMetadata();
        sizeProperty.setPropertyName("size");
        sizeProperty.setReturnType(Integer.class);
        propertyMetadataMap.put("size", sizeProperty);

        //COLLECTION_INDICES = "indices";
        if(indicesClass != null){
            PropertyMetadata indicesProperty = new PropertyMetadata();
            indicesProperty.setPropertyName("indices");
            indicesProperty.setReturnType(indicesClass);
            indicesProperty.setPropertyType(indicesProperty.getReturnType().getSimpleName());
            propertyMetadataMap.put("indices", indicesProperty);
        }
    }

    /**
     * Extract metadata of a class into an object.
     *
     * @param clazz class.
     * @return ModelMetadata object.
     */
    public static ModelMetadata createModelMetadata(Class<?> clazz) {

        ModelMetadata modelMetadata = new ModelMetadata(clazz);

        setAutoLoadProperties(modelMetadata, clazz); //set auto load properties
        setIcon(modelMetadata, clazz); //set icon
        setDisplayFormat(modelMetadata, clazz);//set display format
        setInterpolateFormat(modelMetadata, clazz);

        if(modelMetadata.isSearchDTO()){
            setAliasAnnotations(modelMetadata, clazz);//set alias annotations
            setSortProperties(modelMetadata, clazz);//set sort properties
        }

        //set property metadata
        Map<String, PropertyMetadata> propertyMap = new TreeMap<>();
        Set<String> ignoredProperties = getJsonIgnoreProperties(clazz);
        Method[] methods = clazz.getMethods();
        for (Method method : methods) {
            if (!ReflectionUtils.isPublicInstanceGetter(method))
                continue;

            PropertyMetadata propertyMetadata = extraPropertyMetadata(method);
            if(ignoredProperties.contains(propertyMetadata.getPropertyName()))
                continue;

            propertyMap.put(propertyMetadata.getPropertyName(), propertyMetadata);
        }
        modelMetadata.setPropertyMetadataMap(Collections.unmodifiableMap(propertyMap));
        return modelMetadata;
    }

    private static void setAutoLoadProperties(ModelMetadata modelMetadata, Class<?> clazz) {
        HashSet<String> autoLoadProperties = new HashSet<>();
        while (clazz != null){
            AutoLoad autoLoad = clazz.getAnnotation(AutoLoad.class);
            if(autoLoad != null){
                autoLoadProperties.addAll(Arrays.asList(autoLoad.value()));
            }
            clazz = clazz.getSuperclass();
        }
        modelMetadata.setAutoLoadProperties(autoLoadProperties);
    }

    private static Set<String> getJsonIgnoreProperties(Class<?> clazz) {
        JsonIgnoreProperties jsonIgnoreProperties = clazz.getAnnotation(JsonIgnoreProperties.class); //only add this annotation at top level
        Set<String> ignoredProperties = new TreeSet<>();
        if(jsonIgnoreProperties != null){
            Collections.addAll(ignoredProperties, jsonIgnoreProperties.value());
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
        //get all super classes
        Stack<Class<?>> classes = new Stack<>();
        Class<?> superClass = clazz;
        while (superClass != Object.class){
            classes.push(superClass);
            superClass = superClass.getSuperclass();
        }

        //get all alias declarations
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

        Map<String, AliasDeclaration> declarationMap = new HashMap<>();
        for(AliasDeclaration aliasDeclaration : aliasDeclarations){
            if(declarationMap.containsKey(aliasDeclaration.aliasName()))
                throw new SpongeConfigurationException("Duplicate alias declaration detected in the same search model.");

            declarationMap.put(aliasDeclaration.aliasName(), aliasDeclaration);
        }

        modelMetadata.setAliases(declarationMap);
    }

    private static void setSortProperties(ModelMetadata modelMetadata, Class<?> clazz) {

        //get all sort properties
        List<SortProperty> sortProperties = new ArrayList<>();
        while (clazz != Object.class){
            SortProperty sortProperty = clazz.getAnnotation(SortProperty.class);
            if(sortProperty != null)
                sortProperties.add(sortProperty);
            SortProperty.List sortPropertyList = clazz.getAnnotation(SortProperty.List.class);
            if(sortPropertyList != null) {
                sortProperties.addAll(Arrays.asList(sortPropertyList.value()));
            }
            clazz = clazz.getSuperclass();
        }

        sortProperties.sort((x, y) -> x.propertyName().compareTo(y.propertyName()));
        modelMetadata.setSortProperties(Collections.unmodifiableList(sortProperties));
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

    private static PropertyMetadata extraPropertyMetadata(Method method) {

        PropertyMetadata propertyMetadata = new PropertyMetadata();
        propertyMetadata.setPropertyName(StringUtils.getPropertyName(method.getName()));
        propertyMetadata.setPropertyType(method.getReturnType().getSimpleName());
        propertyMetadata.setGetter(method);

        setIdProperty(propertyMetadata, method);
        setColumn(propertyMetadata, method);
        setLabel(propertyMetadata, method);
        setDescription(propertyMetadata, method);
        setDisplayFormat(propertyMetadata, method);
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

    private static final String DEFAULT_DATE_DISPLAY_FORMAT = "moment:'YYYY-MM-DD'";
    private static final String DEFAULT_DATETIME_DISPLAY_FORMAT = "moment:'YYYY-MM-DD hh:mm:ss'";
    public static final String PLACEHOLDER_FILTER_NAME = "placeholder";

    private static void setDisplayFormat(PropertyMetadata propertyMetadata, Method method) {

        DisplayFormat displayFormatAnnotation = method.getAnnotation(DisplayFormat.class);
        String propertyDisplayFormat;
        if (displayFormatAnnotation != null){
            propertyDisplayFormat = displayFormatAnnotation.value();
            propertyDisplayFormat = propertyDisplayFormat.trim();
            if(propertyDisplayFormat.startsWith("|")){
                propertyDisplayFormat = PLACEHOLDER_FILTER_NAME + " " + propertyDisplayFormat;
            }
        } else {
            propertyDisplayFormat = PLACEHOLDER_FILTER_NAME;
        }

        if(propertyDisplayFormat.startsWith("placeholder")){
            if(propertyMetadata.getPropertyType().endsWith("Date")){
                propertyDisplayFormat = propertyDisplayFormat.replace(PLACEHOLDER_FILTER_NAME, DEFAULT_DATE_DISPLAY_FORMAT);
            } else if(propertyMetadata.getPropertyType().endsWith("DateTime")){
                propertyDisplayFormat = propertyDisplayFormat.replace(PLACEHOLDER_FILTER_NAME, DEFAULT_DATETIME_DISPLAY_FORMAT);
            }
        }

        if(PLACEHOLDER_FILTER_NAME.equals(propertyDisplayFormat)){
            propertyDisplayFormat = null;
        }
        propertyMetadata.setDisplayFormat(propertyDisplayFormat);
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
                propertyMetadata.setInputFormat("^[+-]?([1-9]\\d*|0)$");
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

}
